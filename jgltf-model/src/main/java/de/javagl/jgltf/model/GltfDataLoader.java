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
package de.javagl.jgltf.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.GlTFProperty;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Program;
import de.javagl.jgltf.impl.Shader;


/**
 * A class for loading {@link GltfData} from an URI
 */
public class GltfDataLoader
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfDataLoader.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.INFO;
    
    /**
     * The name of the KHR_binary_glTF extension
     */
    private static final String KHRONOS_BINARY_GLTF_EXTENSION_NAME =
        "KHR_binary_glTF";
    
    /**
     * The unique, universal buffer ID for the binary data that is stored
     * in a binary glTF file
     */
    private static final String BINARY_GLTF_BUFFER_ID = "binary_glTF";
    
    /**
     * The constant indicating JSON scene format
     */
    private static final int SCENE_FORMAT_JSON = 0;
    
    /**
     * Interface for classes that may be informed about the progress 
     * of loading the glTF data
     */
    public interface ProgressListener
    {
        /**
         * Will be called when the message changed.
         * 
         * @param message A message indicating what is currently loaded
         */
        void updateMessage(String message);
        
        /**
         * Will be called when the progress of loading the data changed.
         * 
         * @param progress A progress value. This may be a value in [0.0, 1.0]
         * to indicate the progress, or it may be &lt;0 to indicate that
         * the progress is not known.
         */
        void updateProgress(double progress);
        
    }
    
    /**
     * Load the {@link GltfData} from the given URI
     * 
     * @param uri The URI
     * @param jsonErrorConsumer The consumer for {@link JsonError}s. If this 
     * is <code>null</code>, then log messages will be created for errors
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public static GltfData load(URI uri, 
        Consumer<JsonError> jsonErrorConsumer) 
        throws IOException
    {
        return load(uri, jsonErrorConsumer, null);
    }
    
    /**
     * Load the {@link GltfData} from the given URI
     * 
     * @param uri The URI
     * @param jsonErrorConsumer The consumer for {@link JsonError}s. If this 
     * is <code>null</code>, then log messages will be created for errors
     * @param progressListener An optional {@link ProgressListener} that will 
     * be informed about the progress of loading the data
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public static GltfData load(URI uri, 
        Consumer<JsonError> jsonErrorConsumer, 
        ProgressListener progressListener) 
        throws IOException
    {
        GltfData gltfData = null;
        if (uri.toString().toLowerCase().endsWith(".glb"))
        {
            byte data[] = IO.read(uri, 
                createReadBytesConsumer(uri, progressListener));
            gltfData = createBinaryGltfData(uri, data, jsonErrorConsumer);
        }
        else
        {
            GlTF gltf = readGltf(uri, jsonErrorConsumer, progressListener);
            gltfData = new GltfData(uri, gltf);
        }

        URI baseUri = IO.getParent(uri);
        readBuffersAsByteBuffers(gltfData, baseUri, progressListener);
        createBufferViewsAsByteBuffers(gltfData);
        readImagesAsBufferedImages(gltfData, baseUri, progressListener);
        readShadersAsStrings(gltfData, baseUri, progressListener);
        createExtractedAccessorByteBuffers(gltfData);
        
        return gltfData;
    }
    
    /**
     * Create a consumer for the number of bytes that have been read from
     * the given URI, forwarding the information as a progress value in 
     * [0.0, 1.0] to the given progress listener. If the given progress
     * listener is <code>null</code>, or the total size can not be 
     * obtained from the given URI, then <code>null</code> will be returned.
     *  
     * @param uri The URI
     * @param progressListener The progress listener
     * @return The consumer
     */
    private static LongConsumer createReadBytesConsumer(
        URI uri, ProgressListener progressListener)
    {
        if (progressListener == null)
        {
            return null;
        }
        long contentLength = IO.getContentLength(uri);
        if (contentLength <= 0)
        {
            return null;
        }
        return totalNumBytesRead -> 
        {
            double progress = (double)totalNumBytesRead / contentLength;
            if (progress >= 1.0)
            {
                progress = -1.0;
            }
            progressListener.updateProgress(progress);
        };
    }

    
    /**
     * Read the {@link Buffer} data of the {@link GlTF} and store
     * it in the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @param baseUri The base URI against which relative URIs will be resolved
     * @param progressListener An optional {@link ProgressListener} that will 
     * be informed about the progress of loading the data
     * @throws IOException If the data can not be read
     */
    private static void readBuffersAsByteBuffers(
        GltfData gltfData, URI baseUri, ProgressListener progressListener) 
            throws IOException
    {
        GlTF gltf = gltfData.getGltf();
        if (gltf.getBuffers() == null)
        {
            return;
        }
        for (Entry<String, Buffer> entry : gltf.getBuffers().entrySet())
        {
            String id = entry.getKey();
            if (!id.equals(BINARY_GLTF_BUFFER_ID))
            {
                Buffer buffer = entry.getValue();
                String bufferUriString = buffer.getUri();
                URI absoluteUri = IO.makeAbsolute(baseUri, bufferUriString);
                
                String message = createMessageString(
                    "Reading buffer data from", absoluteUri);
                if (progressListener != null)
                {
                    progressListener.updateMessage(message);
                }
                logger.log(level, message);
    
                ByteBuffer bufferAsByteBuffer = 
                    IO.readAsByteBuffer(absoluteUri);
                
                logger.log(level, "Reading buffer data DONE");
                
                gltfData.putBufferAsByteBuffer(id, bufferAsByteBuffer);
            }
        }
    }
    
    
    /**
     * Read the {@link Image} data of the {@link GlTF} and store
     * it in the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @param baseUri The base URI against which relative URIs will be resolved
     * @param progressListener An optional {@link ProgressListener} that will 
     * be informed about the progress of loading the data
     * @throws IOException If the data can not be read
     */
    private static void readImagesAsBufferedImages(
        GltfData gltfData, URI baseUri, ProgressListener progressListener) 
            throws IOException
    {
        GlTF gltf = gltfData.getGltf();
        if (gltf.getImages() == null)
        {
            return;
        }
        for (Entry<String, Image> entry : gltf.getImages().entrySet())
        {
            String id = entry.getKey();
            Image image = entry.getValue();
            
            Map<String, Object> binaryGltfExtensionMap = 
                getExtensionMap(image, KHRONOS_BINARY_GLTF_EXTENSION_NAME);
            if (binaryGltfExtensionMap != null)
            {
                String message = "Reading image data from binary buffer";
                logger.log(level, message);
                if (progressListener != null)
                {
                    progressListener.updateMessage(message);
                }

                String bufferViewId = String.valueOf(
                    binaryGltfExtensionMap.get("bufferView"));
                ByteBuffer bufferViewByteBuffer = 
                    gltfData.getBufferViewAsByteBuffer(bufferViewId);
                try (InputStream inputStream = 
                    createByteBufferInputStream(bufferViewByteBuffer.slice()))
                {
                    BufferedImage imageAsBufferedImage =
                        ImageIO.read(inputStream);
                    gltfData.putImageAsBufferedImage(id, imageAsBufferedImage);
                    logger.log(level, 
                        "Reading image data from binary buffer DONE");
                }
            }
            else
            {
                String imageUriString = image.getUri();
                URI absoluteUri = IO.makeAbsolute(baseUri, imageUriString);
                
                String message = createMessageString(
                    "Reading image data from", absoluteUri);
                if (progressListener != null)
                {
                    progressListener.updateMessage(message);
                }
                logger.log(level, message);
                
                BufferedImage imageAsBufferedImage = 
                    IO.readAsBufferedImage(absoluteUri);
                
                logger.log(level, "Reading image data DONE");
                
                gltfData.putImageAsBufferedImage(id, imageAsBufferedImage);
            }
        }
    }
        
    
    /**
     * Create the {@link GltfData} from the given data that was read from
     * a binary glTF file
     * 
     * @param uri The URI that the data was read from
     * @param data The actual data
     * @param jsonErrorConsumer The consumer for {@link JsonError}s. If this 
     * is <code>null</code>, then log messages will be created for errors
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    private static GltfData createBinaryGltfData(
        URI uri, byte[] data, Consumer<JsonError> jsonErrorConsumer) 
            throws IOException
    {
        GltfData gltfData = null;
        final int headerLengthInBytes = 20;
        if (data.length < headerLengthInBytes)
        {
            throw new IOException(
                "Expected " + headerLengthInBytes + " bytes for header, " + 
                "but only found " + data.length);
        }
        String magicHeader = new String(data, 0, 4);
        if (!magicHeader.equals("glTF"))
        {
            throw new IOException(
                "Expected \"glTF\" header, but found " + magicHeader +
                ", as ASCII: " + Arrays.toString(Arrays.copyOf(data, 4)));
        }
        ByteBuffer byteData = 
            ByteBuffer.wrap(data).order(ByteOrder.nativeOrder());
        IntBuffer intData = byteData.asIntBuffer();
        int version = intData.get(1);
        if (version != 1)
        {
            logger.warning("Found binary glTF version " + version + 
                ", only 1 is supported");
        }
        int length = intData.get(2);
        if (length != data.length)
        {
            throw new IOException(
                "The length field indicated "+length+" bytes, " + 
                "but only found " + data.length + " have been read");
        }
        int sceneLength = intData.get(3);
        int sceneFormat = intData.get(4);
        if (sceneFormat != SCENE_FORMAT_JSON)
        {
            throw new IOException(
                "The scene format is " + sceneFormat + ", but only " + 
                "JSON (" + SCENE_FORMAT_JSON + ") is supported");
        }
        try (InputStream sceneInputStream = 
             new ByteArrayInputStream(data, 20, sceneLength))
        {
            GlTF gltf = readGltf(sceneInputStream, jsonErrorConsumer);
            gltfData = new GltfData(uri, gltf);
        }
        
        int binaryDataLength = length - sceneLength - headerLengthInBytes;
        ByteBuffer binaryDataBuffer = 
            ByteBuffer.allocateDirect(binaryDataLength);
        binaryDataBuffer.order(ByteOrder.nativeOrder());
        binaryDataBuffer.put(data, 
            sceneLength + headerLengthInBytes, binaryDataLength);
        binaryDataBuffer.position(0);
        gltfData.putBufferAsByteBuffer(
            BINARY_GLTF_BUFFER_ID, binaryDataBuffer);
        
        return gltfData;
    }
    
    /**
     * Return the key-value mapping that is stored as the 
     * {@link GlTFProperty#getExtensions() extension} with the
     * given name in the given glTF property, or <code>null</code>
     * if no such entry can be found.
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @param extensionName The extension name
     * @return The extension property mapping, or <code>null</code>
     */
    private static Map<String, Object> getExtensionMap(
        GlTFProperty gltfProperty, String extensionName)
    {
        Map<String, Object> extensions = gltfProperty.getExtensions();
        if (extensions == null)
        {
            return null;
        }
        Object value = extensions.get(extensionName);
        if (value == null)
        {
            return null;
        }
        if (value instanceof Map<?, ?>)
        {
            Map<?, ?> map = (Map<?, ?>)value;
            Map<?, ?> unmodifiableMap = Collections.unmodifiableMap(map);
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>)unmodifiableMap;
            return result; 
        }
        return null;
    }
    
    /**
     * Create an input stream from the given byte buffer, starting at its
     * current position, up to its current limit. Reading the returned
     * stream will advance the position of the buffer. If this is not 
     * desired, a slice of the buffer may be passed to this method.
     * 
     * @param byteBuffer The buffer
     * @return The input stream
     */
    private static InputStream createByteBufferInputStream(
        ByteBuffer byteBuffer)
    {
        return new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                if (!byteBuffer.hasRemaining())
                {
                    return -1;
                }
                return byteBuffer.get() & 0xFF;
            }

            @Override
            public int read(byte[] bytes, int off, int len) throws IOException
            {
                if (!byteBuffer.hasRemaining())
                {
                    return -1;
                }
                int readLength = Math.min(len, byteBuffer.remaining());
                byteBuffer.get(bytes, off, readLength);
                return len;
            }
        };
    }

    /**
     * Read the {@link GlTF} from the given stream
     *  
     * @param inputStream The input stream
     * @param jsonErrorConsumer The consumer for {@link JsonError}s. If this 
     * is <code>null</code>, then log messages will be created for errors
     * @return The {@link GlTF}
     * @throws IOException If the {@link GlTF} could not be read
     */
    private static GlTF readGltf(
        InputStream inputStream, Consumer<JsonError> jsonErrorConsumer) 
            throws IOException
    {
        logger.log(level, "Reading glTF from binary buffer");

        ObjectMapper objectMapper = new ObjectMapper();
        JacksonUtils.configure(objectMapper, jsonErrorConsumer);
        GlTF gltf = objectMapper.readValue(inputStream, GlTF.class);
        inputStream.close();
        
        logger.log(level, "Reading glTF DONE");
        
        return gltf;
    }

    
    /**
     * Read the {@link GlTF} from the given URI
     *  
     * @param uri The input URI
     * @param jsonErrorConsumer The consumer for {@link JsonError}s. If this 
     * is <code>null</code>, then log messages will be created for errors
     * @param progressListener An optional {@link ProgressListener} that will 
     * be informed about the progress of loading the data
     * @return The {@link GlTF}
     * @throws IOException If the {@link GlTF} could not be read
     */
    private static GlTF readGltf(
        URI uri, Consumer<JsonError> jsonErrorConsumer,
        ProgressListener progressListener) 
        throws IOException
    {
        String message = createMessageString("Reading glTF", uri);
        if (progressListener != null)
        {
            progressListener.updateMessage(message);
        }
        logger.log(level, message);

        ObjectMapper objectMapper = new ObjectMapper();
        JacksonUtils.configure(objectMapper, jsonErrorConsumer);
        LongConsumer totalNumBytesReadConsumer = 
            createReadBytesConsumer(uri, progressListener);
        try (InputStream inputStream = 
            IO.createInputStream(uri, totalNumBytesReadConsumer))
        {
            GlTF gltf = objectMapper.readValue(inputStream, GlTF.class);
            logger.log(level, "Reading glTF DONE");
            return gltf;
        }
    }
    


    /**
     * Read the {@link Shader} data of the {@link GlTF} and store
     * it in the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @param baseUri The base URI against which relative URIs will be resolved
     * @param progressListener An optional {@link ProgressListener} that will 
     * be informed about the progress of loading the data
     * @throws IOException If the data can not be read
     */
    private static void readShadersAsStrings(
        GltfData gltfData, URI baseUri, ProgressListener progressListener) 
            throws IOException
    {
        GlTF gltf = gltfData.getGltf();
        if (gltf.getPrograms() == null)
        {
            return;
        }
        for (Entry<String, Program> entry : gltf.getPrograms().entrySet())
        {
            Program program = entry.getValue();
            readShader(gltfData, baseUri, program, "vertex", 
                program.getVertexShader(), progressListener);
            readShader(gltfData, baseUri, program, "fragment", 
                program.getFragmentShader(), progressListener);
            
        }
    }
    
    /**
     * Read the specified shader for the {@link GlTF} and store it in
     * the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @param baseUri The base URI against which relative URIs will be resolved
     * @param program The {@link Program} to which the shader belongs
     * @param shaderType The shader type string, for example,
     * <code>"vertex"</code> or <code>"fragment"</code>
     * @param shaderId The {@link Shader} ID
     * @param progressListener An optional {@link ProgressListener} that will 
     * be informed about the progress of loading the data
     * @throws IOException If the data can not be read
     */
    private static void readShader(
        GltfData gltfData, URI baseUri, Program program, 
        String shaderType, String shaderId, 
        ProgressListener progressListener) throws IOException
    {
        GlTF gltf = gltfData.getGltf();
        Shader shader = gltf.getShaders().get(shaderId);
        
        Map<String, Object> binaryGltfExtensionMap = 
            getExtensionMap(shader, KHRONOS_BINARY_GLTF_EXTENSION_NAME);
        if (binaryGltfExtensionMap != null)
        {
            String message = "Reading " + shaderType +
                " shader data from binary buffer";
            if (progressListener != null)
            {
                progressListener.updateMessage(message);
            }
            logger.log(level, message);
            
            String bufferViewId = String.valueOf(
                binaryGltfExtensionMap.get("bufferView"));
            ByteBuffer bufferViewByteBuffer = 
                gltfData.getBufferViewAsByteBuffer(bufferViewId);
            byte shaderCodeData[] = new byte[bufferViewByteBuffer.capacity()];
            bufferViewByteBuffer.get(shaderCodeData);
            bufferViewByteBuffer.position(0);
            gltfData.putShaderAsString(shaderId, new String(shaderCodeData));
            
            logger.log(level, "Reading " + shaderType + " shader data DONE");
        }
        else
        {
            String shaderUriString = shader.getUri();
            URI absoluteUri = IO.makeAbsolute(baseUri, shaderUriString);
            
            String message = createMessageString("Reading " + shaderType +
                " shader data from", absoluteUri);
            if (progressListener != null)
            {
                progressListener.updateMessage(message);
            }
            logger.log(level, message);
            
            String shaderCode = IO.readAsString(absoluteUri);
            
            logger.log(level, "Reading " + shaderType + " shader data DONE");
            
            gltfData.putShaderAsString(shaderId, shaderCode);
        }
    }
    
    /**
     * Creates the byte buffers for the {@link BufferView}s of the 
     * {@link GlTF}, and stores them in the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     */
    private static void createBufferViewsAsByteBuffers(GltfData gltfData)
    {
        GlTF gltf = gltfData.getGltf();
        if (gltf.getBufferViews() == null)
        {
            return;
        }
        for (Entry<String, BufferView> entry : gltf.getBufferViews().entrySet())
        {
            String id = entry.getKey();
            BufferView bufferView = entry.getValue();
            if (bufferView == null)
            {
                logger.warning(
                    "Could not find bufferView for bufferView ID "+id);
                continue;
            }
            String bufferId = bufferView.getBuffer();
            ByteBuffer bufferAsByteBuffer = 
                gltfData.getBufferAsByteBuffer(bufferId);
            if (bufferAsByteBuffer == null)
            {
                logger.warning(
                    "Could not find buffer data for buffer ID "+bufferId);
                continue;
            }
            ByteBuffer bufferViewAsByteBuffer = 
                createBufferViewByteBuffer(bufferAsByteBuffer, bufferView);
            gltfData.putBufferViewAsByteBuffer(id, bufferViewAsByteBuffer);
        }
    }
    
    /**
     * Creates a byte buffer for the given {@link BufferView}, which is
     * a view on the {@link Buffer} that the given byte buffer belongs to.
     * Returns <code>null</code> if the offset and length in the 
     * {@link BufferView} are not valid for the given byte buffer. 
     * 
     * @param bufferAsByteBuffer The {@link Buffer} byte buffer
     * @param bufferView The {@link BufferView}
     * @return The byte buffer
     */
    private static ByteBuffer createBufferViewByteBuffer(
        ByteBuffer bufferAsByteBuffer, BufferView bufferView)  
    {
        Integer byteOffset = bufferView.getByteOffset();
        Integer byteLength = bufferView.getByteLength();
        if (byteLength == null)
        {
            // TODO The default value should be 0, but this simply
            // does not make sense. 
            // Updated: In glTF 1.0.1, the byte length will be required:
            // https://github.com/KhronosGroup/glTF/issues/560
            byteLength = bufferAsByteBuffer.capacity() - byteOffset;
        }
        
        if (byteOffset < 0)
        {
            logger.warning("Negative byteOffset in bufferView: "+byteOffset);
            return null;
        }
        if (byteLength < 0)
        {
            logger.warning("Negative byteLength in bufferView: "+byteLength);
            return null;
        }
        if (byteOffset + byteLength > bufferAsByteBuffer.capacity())
        {
            logger.warning(
                "The bufferView byteOffset is " + byteOffset + 
                " and the byteLength is " + byteLength + ", " +
                " but the buffer capacity is only " + 
                bufferAsByteBuffer.capacity());
            return null;
        }
        ByteBuffer bufferViewAsByteBuffer = 
            Buffers.createSlice(
                bufferAsByteBuffer, byteOffset, byteLength);
        return bufferViewAsByteBuffer;
    }

    /**
     * Creates the byte buffers for the {@link Accessor}s of the {@link GlTF}, 
     * and stores them in the given {@link GltfData}.<br>
     * <br>
     * These byte buffers will contain exactly the data that is relevant
     * for the accessor, regardless of the offset of byte stride of
     * the accessor.
     * 
     * @param gltfData The {@link GltfData}
     */
    private static void createExtractedAccessorByteBuffers(GltfData gltfData)
    {
        GlTF gltf = gltfData.getGltf();
        if (gltf.getAccessors() == null)
        {
            return;
        }
        for (Entry<String, Accessor> entry : gltf.getAccessors().entrySet())
        {
            String id = entry.getKey();
            Accessor accessor = entry.getValue();
            if (accessor == null)
            {
                logger.warning(
                    "Could not find accessor for accessor ID "+id);
                continue;
            }
                
            String bufferViewId = accessor.getBufferView();
            ByteBuffer bufferViewAsByteBuffer = 
                gltfData.getBufferViewAsByteBuffer(bufferViewId);
            if (bufferViewAsByteBuffer == null)
            {
                logger.warning(
                    "Could not find bufferView data for bufferView ID " + 
                    bufferViewId);
                continue;
            }
            
            ByteBuffer accessorByteBuffer = 
                createExtractedAccessorByteBuffer(
                    bufferViewAsByteBuffer, accessor);
            gltfData.putExtractedAccessorByteBuffer(
                    id, accessorByteBuffer);
        }
    }
    
    /**
     * Create a single byte buffer containing the data that is described
     * by the given {@link Accessor}, which accesses the given 
     * byte buffer of a {@link BufferView}.<br>
     * <br>
     * The odd naming of an "extracted" byte buffer stems from the fact 
     * that this actually defeats the whole concept of Buffers, BufferViews
     * and Accessors: The user should use the byte buffer of the Buffer 
     * directly. The Accessor should only describe how this data should be 
     * interpreted. But in some cases, it may be convenient to have the
     * relevant data of one Accessor as a single memory block. 
     * 
     * @param bufferViewAsByteBuffer The byte buffer of the {@link BufferView}
     * @param accessor The {@link Accessor}
     * @return The byte buffer for the {@link Accessor}
     */
    static ByteBuffer createExtractedAccessorByteBuffer(
        ByteBuffer bufferViewAsByteBuffer, Accessor accessor) 
    {
        String type = accessor.getType();
        Integer componentType = accessor.getComponentType();
        Integer elementCount = accessor.getCount();

        int numComponentsPerElement = 
            Accessors.getNumComponentsForAccessorType(type);
        int numBytesPerComponent = 
            Accessors.getNumBytesForAccessorComponentType(componentType);
        int numBytesPerElement = 
            numComponentsPerElement * numBytesPerComponent;
        int numBytesTotal = numBytesPerElement * elementCount;

        Integer byteOffset = accessor.getByteOffset();
        Integer byteStride = accessor.getByteStride();
        if (byteStride == null || byteStride == 0)
        {
            byteStride = numBytesPerElement;
        }
        ByteBuffer accessorByteBuffer = 
            ByteBuffer.allocateDirect(numBytesTotal);
        accessorByteBuffer.order(ByteOrder.nativeOrder());
        byte elementData[] = new byte[numBytesPerElement];
        int offset = byteOffset;
        for (int e = 0; e < elementCount; e++)
        {
            bufferViewAsByteBuffer.position(offset);
            bufferViewAsByteBuffer.get(elementData);
            accessorByteBuffer.put(elementData);
            offset += byteStride;
        }
        accessorByteBuffer.position(0);
        bufferViewAsByteBuffer.position(0);
        return accessorByteBuffer;
    }
    
    /**
     * Create a string for logging- and progress messages
     * 
     * @param messagePrefix The prefix for the message
     * @param absoluteUri The URI that the data is read from
     * @return The message string
     */
    private static String createMessageString(
        String messagePrefix, URI absoluteUri)
    {
        if ("data".equalsIgnoreCase(absoluteUri.getScheme()))
        {
            return messagePrefix + " data URI";
        }
        String message = messagePrefix + " " + extractFileName(absoluteUri);
        long contentLength = IO.getContentLength(absoluteUri);
        if (contentLength >= 0)
        {
            
            message += " (" + 
                NumberFormat.getNumberInstance().format(contentLength) + 
                " bytes)";
        }
        return message;
    }
    
    /**
     * Tries to extract the "file name" that is referred to with the 
     * given URI. If no file name can be extracted, then the string
     * representation of the URI is returned.
     * 
     * @param uri The URI
     * @return The file name
     */
    private static String extractFileName(URI uri)
    {
        String s = uri.toString();
        int lastSlashIndex = s.lastIndexOf('/');
        if (lastSlashIndex != -1)
        {
            return s.substring(lastSlashIndex+1);
        }
        return s;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfDataLoader()
    {
        // Private constructor to prevent instantiation
    }
    
}
