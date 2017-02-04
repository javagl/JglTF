/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.browser.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.BinaryGltf;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.Maps;
import de.javagl.jgltf.model.io.BinaryGltfDatas;
import de.javagl.jgltf.model.io.BufferViews;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.GltfReader;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.JsonError;
import de.javagl.swing.tasks.ProgressListener;
import de.javagl.swing.tasks.executors.GenericProgressTask;
import de.javagl.swing.tasks.executors.ObservableExecutorService;
import de.javagl.swing.tasks.executors.ObservableExecutors;
import de.javagl.swing.tasks.executors.ProgressTask;

/**
 * A reader that loads {@link GltfData} using multiple threads,
 * and publishes progress information. 
 */
public class GltfDataReaderThreaded
{
    /**
     * The reader for the {@link GlTF}
     */
    private final GltfReader gltfReader;
    
    /**
     * The list of tasks to load {@link GltfData}
     */
    private List<Callable<Void>> gltfDataLoadingTasks;
    
    /**
     * The list of tasks to load {@link Buffer} objects
     */
    private List<Callable<Void>> bufferLoadingTasks;

    /**
     * The list of tasks to load {@link Image} objects
     */
    private List<Callable<Void>> imageLoadingTasks;

    /**
     * The list of tasks to load {@link Shader} objects
     */
    private List<Callable<Void>> shaderLoadingTasks;

    /**
     * The list of {@link ProgressListener}s that have been attached to
     * this reader
     */
    private final List<ProgressListener> progressListeners;
    
    /**
     * A {@link ProgressListener} that will forward all information to
     * the registered {@link #progressListeners}
     */
    private final ProgressListener forwardingProgressListener = 
        new ProgressListener()
    {
        @Override
        public void progressChanged(double progress)
        {
            fireProgressChanged(progress);
        }
        
        @Override
        public void messageChanged(String message)
        {
            fireMessageChanged(message);
        }
    };
    
    /**
     * The number of threads, as given in the constructor
     */
    private final int numThreads;
    
    /**
     * The {@link ObservableExecutorService} that processes the loading tasks
     */
    private ObservableExecutorService observableExecutorService;
    
    /**
     * The URI that the data is currently read from
     */
    private URI uri;

    /**
     * The string that was read from the URI. This may either be the
     * contents of the input stream that was created from the URI, or
     * the "scene" part of the binary glTF data.
     */
    private String jsonString;
    
    /**
     * The {@link GltfData} that is currently being read
     */
    private GltfData gltfData;
    
    /**
     * Creates a threaded glTF data reader
     * 
     * @param numThreads The number of threads to use. If this is less than
     * 0, then a cached thread pool will be used, taking as many threads as
     * necessary. If this is 0, then the number of threads will be the same
     * as the number of available processors. 
     */
    public GltfDataReaderThreaded(int numThreads)
    {
        this.numThreads = numThreads;
        this.progressListeners = new CopyOnWriteArrayList<ProgressListener>();
        this.gltfReader = new GltfReader();
    }
    
    /**
     * Set the given consumer to receive {@link JsonError}s that may 
     * occur when a glTF is read
     * 
     * @param jsonErrorConsumer The {@link JsonError} consumer
     */
    public void setJsonErrorConsumer(
        Consumer<? super JsonError> jsonErrorConsumer)
    {
        gltfReader.setJsonErrorConsumer(jsonErrorConsumer);
    }
    
    /**
     * Creates the executor service for the given number of threads, as 
     * given in the constructor.
     * 
     * @param numThreads The number of threads
     * @return The executor service
     */
    private static ObservableExecutorService createObservableExecutorService(
        int numThreads)
    {
        ObservableExecutorService executorService = null;
        if (numThreads < 0)
        {
            executorService = 
                ObservableExecutors.newCachedThreadPool();
        }
        else if (numThreads == 0)
        {
            executorService = 
                ObservableExecutors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors());
        }
        else
        {
            executorService = 
                ObservableExecutors.newFixedThreadPool(numThreads);
        }
        executorService.setKeepAliveTime(3, TimeUnit.SECONDS);
        executorService.allowCoreThreadTimeOut(true);
        return executorService;
    }

    /**
     * Cancel all tasks that are currently executed
     */
    public void cancel()
    {
        if (observableExecutorService != null)
        {
            observableExecutorService.shutdownNow();
            observableExecutorService = null;
        }
    }
    
    /**
     * Returns the {@link ObservableExecutorService} that executes the 
     * loading tasks. (Note that the instance that is returned here 
     * may change when {@link #cancel()} was called between two calls
     * to this method)
     * 
     * @return The {@link ObservableExecutorService}
     */
    public final ObservableExecutorService getObservableExecutorService()
    {
        if (observableExecutorService == null)
        {
            observableExecutorService = 
                createObservableExecutorService(numThreads);
        }
        return observableExecutorService;
    }
    
    /**
     * Add the given progress listener to be informed about changes in
     * the loading progress
     * 
     * @param progressListener The {@link ProgressListener} to add
     */
    public final void addProgressListener(ProgressListener progressListener)
    {
        if (progressListener != null)
        {
            this.progressListeners.add(progressListener);
        }
    }
    
    /**
     * Remove the given progress listener 
     * 
     * @param progressListener The {@link ProgressListener} to remove
     */
    public final void removeProgressListener(ProgressListener progressListener)
    {
        this.progressListeners.remove(progressListener);
    }
    
    /**
     * Dispatches the given message to all {@link ProgressListener}s
     * 
     * @param message The message
     */
    private void fireMessageChanged(String message)
    {
        for (ProgressListener progressListener : progressListeners)
        {
            progressListener.messageChanged(message);
        }
    }
    
    /**
     * Dispatches the given progress to all {@link ProgressListener}s
     * 
     * @param progress The progress
     */
    private void fireProgressChanged(double progress)
    {
        for (ProgressListener progressListener : progressListeners)
        {
            progressListener.progressChanged(progress);
        }
    }
    
    /**
     * Read the {@link GltfData} from the given URI
     * 
     * @param uri The URI
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public GltfData readGltfData(URI uri) throws IOException
    {
        Objects.requireNonNull(uri, "The URI may not be null");

        this.uri = uri;

        // Create fresh lists of tasks, to be filled in 
        // the create*LoadingTasks methods 
        this.gltfDataLoadingTasks = new ArrayList<Callable<Void>>();
        this.bufferLoadingTasks = new ArrayList<Callable<Void>>();
        this.imageLoadingTasks = new ArrayList<Callable<Void>>();
        this.shaderLoadingTasks = new ArrayList<Callable<Void>>();
        
        createGltfDataLoadingTask();
        
        fireMessageChanged("Loading glTF data");
        Throwable gltfLoadingError = 
            ExecutorServiceUtils.invokeAll(
                getObservableExecutorService(),
                progress -> fireProgressChanged(progress),
                gltfDataLoadingTasks);
        if (gltfLoadingError != null)
        {
            throw new IOException(gltfLoadingError);
        }

        if (gltfData == null)
        {
            throw new IOException("Could not load glTF data");
        }
        GlTF gltf = gltfData.getGltf();
        
        // Process the buffers, images and shaders, creating tasks for
        // loading them, that are placed in the task lists
        Maps.forEachEntry(gltf.getBuffers(), 
            this::createBufferLoadingTask);
        Maps.forEachEntry(gltf.getImages(), 
            this::createImageLoadingTask);
        Maps.forEachEntry(gltf.getShaders(), 
            this::createShaderLoadingTask);

        // Schedule the loading tasks 
        fireMessageChanged("Loading references");
        Throwable uriLoadingError = 
            ExecutorServiceUtils.invokeAll(
                getObservableExecutorService(),
                progress -> fireProgressChanged(progress),
                bufferLoadingTasks, 
                imageLoadingTasks, 
                shaderLoadingTasks);
        if (uriLoadingError != null)
        {
            throw new IOException(uriLoadingError);
        }
        
        fireMessageChanged("Building buffer views");
        BufferViews.createBufferViewByteBuffers(gltfData);
        BinaryGltfDatas.createBinaryImageDatas(gltfData);
        BinaryGltfDatas.createBinaryShaderDatas(gltfData);
        
        // Clean up and return the result
        fireMessageChanged("Done");
        this.gltfDataLoadingTasks = null;
        this.bufferLoadingTasks = null;
        this.imageLoadingTasks = null;
        this.shaderLoadingTasks = null;
        
        GltfData result = gltfData;
        setGltfData(null);
        return result;
    }
    
    /**
     * Set the {@link GltfData} that is currently being read
     * 
     * @param gltfData The {@link GltfData}
     */
    private void setGltfData(GltfData gltfData)
    {
        this.gltfData = gltfData;
    }
    
    /**
     * Returns the JSON string that was read from the URI. This may either 
     * be the contents of the input stream that was created from the URI, or
     * the "scene" part of the binary glTF data. Returns <code>null</code> if
     * no {@link GltfData} was read yet.
     * 
     * @return The JSON string
     */
    public String getJsonString()
    {
        return jsonString;
    }
    
    /**
     * Returns the base URI, which is the "directory" that contains the
     * original glTF file
     * 
     * @return The base URI
     */
    private URI getBaseUri()
    {
        return IO.getParent(uri);
    }
    
    
    /**
     * Create the task for loading the {@link GltfData} from the current URI,
     * and place it into the {@link #gltfDataLoadingTasks} list
     * 
     * @throws IOException If the connection to the current URI can not be
     * opened 
     */
    private void createGltfDataLoadingTask() throws IOException
    {
        boolean isBinaryGltf = 
            uri.toString().toLowerCase().endsWith(".glb");

        DispatchingLongConsumer totalNumBytesReadConsumer = 
            new DispatchingLongConsumer();
        Callable<Void> callable = () -> 
        {
            // Only one glTF is loaded at a time. Forward the progress
            // information to the progress listener
            LongConsumer progressForwarder = 
                createTotalNumBytesReadConsumer(uri, 
                    forwardingProgressListener);
            totalNumBytesReadConsumer.addConsumer(progressForwarder);
            
            try (InputStream inputStream = createInputStream(
                    uri, totalNumBytesReadConsumer))
            {
                if (isBinaryGltf)
                {
                    GltfData gltfData = readBinaryGltfData(inputStream);
                    setGltfData(gltfData);
                    return null;
                }
                GltfData gltfData = readGltfData(inputStream);
                if (Thread.currentThread().isInterrupted())
                {
                    throw new IOException("Interrupted while reading glTF");
                }
                setGltfData(gltfData);
                return null;
            }
        };
        GenericProgressTask<Void> loadingTask = createLoadingTask(
            (isBinaryGltf ? "binary " : "") + "glTF data from " + 
            IO.extractFileName(uri), callable, totalNumBytesReadConsumer);
        gltfDataLoadingTasks.add(loadingTask);
    }
    
    /**
     * Create a new {@link GltfData} by reading the {@link GlTF} from 
     * the given input stream. The caller is responsible for closing
     * the stream
     * 
     * @param inputStream The input stream
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    private GltfData readGltfData(InputStream inputStream) 
        throws IOException
    {
        byte data[] = IO.readStream(inputStream);
        jsonString = new String(data);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data))
        {
            GlTF gltf = gltfReader.readGltf(bais);
            return new GltfData(gltf);
        }
    }
    
    /**
     * Create a new binary {@link GltfData} by reading the data from 
     * the given input stream, and creating a binary {@link GltfData}
     * from the resulting raw data. The caller is responsible for 
     * closing the stream
     * 
     * @param inputStream The input stream
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    private GltfData readBinaryGltfData(InputStream inputStream) 
        throws IOException
    {
        byte data[] = IO.readStream(inputStream);
        jsonString = BinaryGltfDatas.extractJsonString(data);
        return BinaryGltfDatas.create(data, gltfReader);
    }

    
    /**
     * Create an input stream for reading from the given URI, forwarding
     * progress information to the given consumer. The caller is responsible
     * for closing the returned stream.
     * 
     * @param uri The URI
     * @param totalNumBytesReadConsumer The consumer for the number of 
     * read bytes
     * @return The input stream
     * @throws IOException If the stream can not be opened
     */
    @SuppressWarnings("resource")
    private static InputStream createInputStream(
        URI uri, LongConsumer totalNumBytesReadConsumer) 
            throws IOException
    {
        InputStream uriInputStream = uri.toURL().openStream();
        InputStream inputStream = 
            ProgressInputStreams.createProgressInputStream(
                uriInputStream, totalNumBytesReadConsumer);
        return inputStream;
    }
    

    /**
     * Create the task for loading the specified {@link Buffer} data,
     * and place it into the {@link #bufferLoadingTasks} list
     * 
     * @param id The {@link Buffer} ID
     * @param buffer The {@link Buffer}
     */
    private void createBufferLoadingTask(String id, Buffer buffer)
    {
        if (BinaryGltf.isBinaryGltfBufferId(id))
        {
            return;
        }
        DispatchingLongConsumer totalNumBytesReadConsumer = 
            new DispatchingLongConsumer();
        Callable<Void> callable = () -> 
        {
            URI absoluteUri = IO.makeAbsolute(getBaseUri(), buffer.getUri());
            byte data[] = read(absoluteUri, totalNumBytesReadConsumer);
            ByteBuffer byteBuffer = Buffers.create(data);
            gltfData.putBufferData(id, byteBuffer);
            return null;
        };
        
        String description = IO.isDataUriString(buffer.getUri()) ? 
            "data URI for buffer " + id : buffer.getUri();
        GenericProgressTask<Void> loadingTask = createLoadingTask(
            description, callable, totalNumBytesReadConsumer);
        bufferLoadingTasks.add(loadingTask);
    }

    /**
     * Create the task for loading the specified {@link Image} data, 
     * and place it into the {@link #imageLoadingTasks} list
     * 
     * @param id The {@link Image} ID
     * @param image The {@link Image}
     */
    private void createImageLoadingTask(String id, Image image)
    {
        if (BinaryGltf.hasBinaryGltfExtension(image))
        {
            return;
        }
        DispatchingLongConsumer totalNumBytesReadConsumer = 
            new DispatchingLongConsumer();
        Callable<Void> callable = () ->
        {
            String uriString = image.getUri();
            URI absoluteUri = IO.makeAbsolute(getBaseUri(), uriString);
            byte[] data = read(absoluteUri, totalNumBytesReadConsumer);
            gltfData.putImageData(id, Buffers.create(data));
            return null;
        };
        String description = IO.isDataUriString(image.getUri()) ? 
            "data URI for image " + id : image.getUri();
        GenericProgressTask<Void> loadingTask = 
            createLoadingTask(description, callable, totalNumBytesReadConsumer);
        imageLoadingTasks.add(loadingTask);
        
    }



    /**
     * Create the task for loading the specified {@link Shader} data, 
     * and place it into the {@link #shaderLoadingTasks} list
     * 
     * @param id The {@link Shader} ID
     * @param shader The {@link Shader}
     */
    private void createShaderLoadingTask(String id, Shader shader)
    {
        if (BinaryGltf.hasBinaryGltfExtension(shader))
        {
            return;
        }
        DispatchingLongConsumer totalNumBytesReadConsumer = 
            new DispatchingLongConsumer();
        Callable<Void> callable = () ->
        {
            String uriString = shader.getUri();
            URI absoluteUri = IO.makeAbsolute(getBaseUri(), uriString);
            byte[] shaderCodeData = 
                read(absoluteUri, totalNumBytesReadConsumer);
            gltfData.putShaderData(id, Buffers.create(shaderCodeData));
            return null;
        };
        String description = IO.isDataUriString(shader.getUri()) ? 
            "data URI for shader " + id : shader.getUri();
        GenericProgressTask<Void> loadingTask = 
            createLoadingTask(description, callable, totalNumBytesReadConsumer);
        shaderLoadingTasks.add(loadingTask);
    }
    
    

    /**
     * Read the whole contents of the given URI (which may also be a data
     * URI) and return it as an array, informing the given consumer about 
     * the number of bytes that have been read.
     * 
     * @param uri The URI
     * @param totalNumBytesReadConsumer The consumer for the number of bytes
     * @return The data 
     * @throws IOException If an IO error occurs
     */
    private static byte[] read(URI uri, LongConsumer totalNumBytesReadConsumer)
        throws IOException
    {
        try (InputStream inputStream = 
                ProgressInputStreams.createProgressInputStream(
                    IO.createInputStream(uri), totalNumBytesReadConsumer))
        {
            return IO.readStream(inputStream);
        }
    }
    
    /**
     * Create a consumer for the number of bytes that have been read from
     * the given URI, forwarding the information as a progress value in 
     * [0.0, 1.0] to the given progress listener. If the total size can not 
     * be obtained from the given URI, then no information will be forwarded.
     *  
     * @param uri The URI
     * @param progressListener The progress listener
     * @return The consumer
     */
    private static LongConsumer createTotalNumBytesReadConsumer(
        URI uri, ProgressListener progressListener)
    {
        long contentLength = IO.getContentLength(uri);
        if (contentLength <= 0)
        {
            return t -> 
            {
                progressListener.progressChanged(-1.0); 
            };
        }
        return totalNumBytesRead -> 
        {
            double progress = (double)totalNumBytesRead / contentLength;
            progressListener.progressChanged(
                Math.max(0.0, Math.min(1.0, progress)));
        };
    }
    
    /**
     * Creates a {@link GenericProgressTask} with the given description,
     * that executes the given callable. If the given dispatcher is not 
     * <code>null</code>, then a consumer will be added to it, that forwards
     * information about the number of bytes read to the progress listeners
     * of the returned progress task.
     *   
     * @param description The task description
     * @param callable The callable to be executed
     * @param totalNumBytesReadConsumer The optional dispatcher for 
     * information about the number of bytes read
     * @return The {@link ProgressTask}
     */
    private static <T> GenericProgressTask<T> createLoadingTask(
        String description, Callable<T> callable, 
        DispatchingLongConsumer totalNumBytesReadConsumer)
    {
        GenericProgressTask<T> loadingTask =
            new GenericProgressTask<T>(description);
        if (totalNumBytesReadConsumer != null)
        {
            ProgressListener progressListener = 
                loadingTask.getDispatchingProgressListener();
            totalNumBytesReadConsumer.addConsumer(t -> 
            {
                String numBytesString = 
                    NumberFormat.getNumberInstance().format(t);
                String message = " (" + numBytesString + " bytes)";
                progressListener.messageChanged(message);
            });
        }
        loadingTask.setCallable(callable);
        return loadingTask; 
    }



}
