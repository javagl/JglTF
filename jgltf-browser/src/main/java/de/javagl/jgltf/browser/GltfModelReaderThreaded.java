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
package de.javagl.jgltf.browser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.GltfReference;
import de.javagl.jgltf.model.io.GltfModelReader;
import de.javagl.jgltf.model.io.GltfReferenceLoader;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.JsonError;
import de.javagl.jgltf.model.io.ProgressInputStream;
import de.javagl.swing.tasks.ProgressListener;
import de.javagl.swing.tasks.executors.GenericProgressTask;
import de.javagl.swing.tasks.executors.ObservableExecutorService;
import de.javagl.swing.tasks.executors.ObservableExecutors;

/**
 * A reader that loads {@link GltfModel} using multiple threads,
 * and publishes progress information. 
 */
class GltfModelReaderThreaded
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModelReaderThreaded.class.getName());
    
    /**
     * The {@link GltfModelReader} that does the initial read step 
     */
    private final GltfModelReader gltfModelReader;
    
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
     * The {@link GltfModel} that is currently being read
     */
    private GltfModel gltfModel;

    /**
     * Creates a threaded glTF data reader
     * 
     * @param numThreads The number of threads to use. If this is less than
     * 0, then a cached thread pool will be used, taking as many threads as
     * necessary. If this is 0, then the number of threads will be the same
     * as the number of available processors. 
     */
    GltfModelReaderThreaded(int numThreads)
    {
        this.numThreads = numThreads;
        this.progressListeners = new CopyOnWriteArrayList<ProgressListener>();
        this.gltfModelReader = new GltfModelReader();
    }
    
    /**
     * Set the given consumer to receive {@link JsonError}s that may 
     * occur when a glTF is read
     * 
     * @param jsonErrorConsumer The {@link JsonError} consumer
     */
    void setJsonErrorConsumer(
        Consumer<? super JsonError> jsonErrorConsumer)
    {
        gltfModelReader.setJsonErrorConsumer(jsonErrorConsumer);
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
    void cancel()
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
    final ObservableExecutorService getObservableExecutorService()
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
    final void addProgressListener(ProgressListener progressListener)
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
    final void removeProgressListener(ProgressListener progressListener)
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
     * Read the {@link GltfModel} from the given URI
     * 
     * @param uri The URI
     * @return The {@link GltfModel}
     * @throws IOException If an IO error occurs
     */
    GltfModel readGltfModel(URI uri) throws IOException
    {
        Objects.requireNonNull(uri, "The URI may not be null");

        this.uri = uri;
        
        Callable<Void> gltfModelLoadingTask = 
            createGltfModelLoadingTask();
        
        fireMessageChanged("Loading glTF model");
        Throwable gltfLoadingError = 
            ExecutorServiceUtils.invokeAll(
                getObservableExecutorService(),
                progress -> fireProgressChanged(progress),
                Collections.singletonList(gltfModelLoadingTask));
        if (gltfLoadingError != null)
        {
            throw new IOException(gltfLoadingError);
        }
        if (gltfModel == null)
        {
            throw new IOException("Could not load glTF model");
        }
        
        List<Callable<Void>> loadingTasks = new ArrayList<Callable<Void>>();
        List<GltfReference> references = gltfModel.getReferences();
        for (GltfReference reference : references)
        {
            Callable<Void> loadingTask = createReferenceLoadingTask(reference);
            loadingTasks.add(loadingTask);
        }
        
        // Schedule the loading tasks 
        fireMessageChanged("Loading references");
        Throwable uriLoadingError = 
            ExecutorServiceUtils.invokeAll(
                getObservableExecutorService(),
                progress -> fireProgressChanged(progress),
                loadingTasks);
        if (uriLoadingError != null)
        {
            throw new IOException(uriLoadingError);
        }
        
        // Clean up and return the result
        fireMessageChanged("Done");
        
        GltfModel result = gltfModel;
        setGltfModel(null);
        return result;
    }
    
    /**
     * Set the {@link GltfModel} that is currently being read
     * 
     * @param gltfModel The {@link GltfModel}
     */
    private void setGltfModel(GltfModel gltfModel)
    {
        this.gltfModel = gltfModel;
    }
    
    
    /**
     * Create the task for loading the {@link GltfModel} from the current URI
     * 
     * @return The task
     * @throws IOException If the task cannot be created 
     */
    @SuppressWarnings("resource")
    private Callable<Void> createGltfModelLoadingTask() throws IOException
    {
        InputStream uriInputStream = uri.toURL().openStream();
        ProgressInputStream progressInputStream = 
            new ProgressInputStream(uriInputStream);
        
        Callable<Void> basicTask = () -> 
        {
            // Only one glTF is loaded at a time. Forward the progress
            // information to the progress listener
            LongConsumer progressForwarder = 
                createTotalNumBytesReadConsumer(uri, 
                    forwardingProgressListener);
            progressInputStream.addTotalNumBytesReadConsumer(
                progressForwarder);
            GltfModel loadedGltfModel = 
                gltfModelReader.readWithoutReferences(progressInputStream);
            setGltfModel(loadedGltfModel);
            try
            {
                progressInputStream.close();
            }
            catch (IOException e)
            {
                logger.warning("Could not close stream: " + e.getMessage());
            }
            return null;
        }; 
        
        String description = "glTF data from " + IO.extractFileName(uri);
        GenericProgressTask<Void> loadingTask =
            new GenericProgressTask<Void>(description);
        loadingTask.setCallable(basicTask);
        attach(progressInputStream, loadingTask);
        return loadingTask; 
    }
    
    
    /**
     * Create the task for loading the data of the given {@link GltfReference}
     * 
     * @param reference The {@link GltfReference}
     * @return The task
     * @throws IOException If the task cannot be created 
     */
    @SuppressWarnings("resource")
    private Callable<Void> createReferenceLoadingTask(GltfReference reference)
        throws IOException
    {
        String name = reference.getName();
        String uriString = reference.getUri();

        URI baseUri = IO.getParent(uri);
        URI absoluteUri = IO.makeAbsolute(baseUri, uriString);
        InputStream inputStream = IO.createInputStream(absoluteUri);
        
        ProgressInputStream progressInputStream = 
            new ProgressInputStream(inputStream);
        Callable<Void> basicTask = () -> 
        {
            GltfReferenceLoader.load(reference, progressInputStream);
            try
            {
                progressInputStream.close();
            }
            catch (IOException e)
            {
                logger.warning("Could not close stream: " + e.getMessage());
            }
            return null;
        };
        
        String taskName = name + " from ";
        if (IO.isDataUriString(uriString))
        {
            taskName += "data URI";
        }
        else
        {
            taskName += uriString;
        }
        GenericProgressTask<Void> loadingTask =
            new GenericProgressTask<Void>(taskName);
        loadingTask.setCallable(basicTask);
        attach(progressInputStream, loadingTask);
        return loadingTask;
    }
    
    
    /**
     * Connect the progress of reading from the given stream to the given
     * task. The progress from the stream will afterwards be forwarded
     * to all progress listeners that are attached to the given task
     * 
     * @param progressInputStream The {@link ProgressInputStream}
     * @param progressTask The {@link GenericProgressTask}
     */
    private static void attach(
        ProgressInputStream progressInputStream, 
        GenericProgressTask<?> progressTask)
    {
        ProgressListener progressListener = 
            progressTask.getDispatchingProgressListener();
        progressInputStream.addTotalNumBytesReadConsumer(t -> 
        {
            String numBytesString = 
                NumberFormat.getNumberInstance().format(t);
            String message = " (" + numBytesString + " bytes)";
            progressListener.messageChanged(message);
        });
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
    

}
