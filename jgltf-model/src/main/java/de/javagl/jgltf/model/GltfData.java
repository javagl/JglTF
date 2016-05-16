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
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Shader;

/**
 * A class storing a {@link GlTF} and the associated (binary) data that was 
 * read from external sources linked in the {@link GlTF}, or from the 
 * glTF file itself for the case of binary or embedded glTFs.
 */
public final class GltfData
{
    /**
     * The URI that the {@link GlTF} was read from
     */
    private final URI uri;
    
    /**
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * The {@link GlTF#getBuffers()}, as byte buffers
     */
    private final Map<String, ByteBuffer> buffersAsByteBuffers;

    /**
     * The {@link GlTF#getBufferViews()}, as byte buffers
     */
    private final Map<String, ByteBuffer> bufferViewsAsByteBuffers;

    /**
     * The {@link GlTF#getAccessors()}, as single byte buffers that
     * have been extracted from the bufferView byte buffers
     */
    private final Map<String, ByteBuffer> extractedAccessorByteBuffers;
    
    /**
     * The {@link GlTF#getImages()}, as buffered images
     */
    private final Map<String, BufferedImage> imagesAsBufferedImages;
    
    /**
     * The {@link GlTF#getShaders()}, as strings
     */
    private final Map<String, String> shadersAsStrings;
    
    /**
     * Creates the new {@link GltfData}. 
     * 
     * @param uri The base URI that the {@link GlTF} was read from, and 
     * against which all relative URIs will be resolved
     * @param gltf The {@link GlTF}
     */
    GltfData(URI uri, GlTF gltf)
    {
        this.uri = uri;
        this.gltf = gltf;
        
        this.buffersAsByteBuffers = 
            new LinkedHashMap<String, ByteBuffer>();
        this.bufferViewsAsByteBuffers = 
            new LinkedHashMap<String, ByteBuffer>();
        this.imagesAsBufferedImages = 
            new LinkedHashMap<String, BufferedImage>();
        this.shadersAsStrings = 
            new LinkedHashMap<String, String>();
        this.extractedAccessorByteBuffers = 
            new LinkedHashMap<String, ByteBuffer>();
    }
    
    /**
     * Return the URI that the {@link GlTF} was read from
     * 
     * @return The URI
     */
    URI getUri()
    {
        return uri;
    }
    
    /**
     * Returns the {@link GlTF}
     * 
     * @return The {@link GlTF}
     */
    public GlTF getGltf()
    {
        return gltf;
    }

    /**
     * Store the given shader code under the given {@link Shader} id
     * 
     * @param id The ID
     * @param shaderCode The shader code
     */
    void putShaderAsString(String id, String shaderCode)
    {
        shadersAsStrings.put(id, shaderCode);
    }
    
    /**
     * Returns the string that corresponds to the code of the {@link Shader}
     * with the given ID, or <code>null</code> if no such shader is found.
     * 
     * @param shaderId The {@link Shader} ID
     * @return The shader source code
     */
    public String getShaderAsString(String shaderId)
    {
        return shadersAsStrings.get(shaderId);
    }
    
    /**
     * Store the given buffered image under the given {@link Image} id
     * 
     * @param id The ID
     * @param bufferedImage The buffered image
     */
    void putImageAsBufferedImage(String id, BufferedImage bufferedImage)
    {
        imagesAsBufferedImages.put(id, bufferedImage);
    }
    
    /**
     * Returns the buffered image that corresponds to the {@link Image}
     * with the given ID, or <code>null</code> if no such image is found.
     * 
     * @param imageId The {@link Image} ID
     * @return The byte buffer
     */
    public BufferedImage getImageAsBufferedImage(String imageId)
    {
        return imagesAsBufferedImages.get(imageId);
    }
    
    
    /**
     * Store the given byte buffer under the given {@link BufferView} id.<br>
     * <br>
     * This assumes to receive a byte buffer that was extracted from the
     * byte buffer of the buffer view that is referenced by the accessor.
     * So the  buffer will contain exactly the elements that are
     * relevant for the accessor, regardless of the offset and byte stride 
     * of the accessor. 
     * 
     * @param id The ID
     * @param byteBuffer The buffer
     */
    void putExtractedAccessorByteBuffer(String id, ByteBuffer byteBuffer)
    {
        extractedAccessorByteBuffers.put(id, byteBuffer);
    }
    
    /**
     * Returns the byte buffer that corresponds to the {@link Accessor}
     * with the given ID, or <code>null</code> if no such buffer is found.<br>
     * <br>
     * This return a byte buffer that was extracted from the byte buffer 
     * of the buffer view that is referenced by the accessor.
     * So the returned buffer will contain exactly the elements that are
     * relevant for the accessor, regardless of the offset and byte stride 
     * of the accessor. 
     * 
     * @param accessorId The {@link Accessor} ID
     * @return The byte buffer
     */
    public ByteBuffer getExtractedAccessorByteBuffer(String accessorId)
    {
        return extractedAccessorByteBuffers.get(accessorId);
    }
    
    
    /**
     * Store the given byte buffer under the given {@link Buffer} id
     * 
     * @param id The ID
     * @param byteBuffer The buffer
     */
    void putBufferAsByteBuffer(String id, ByteBuffer byteBuffer)
    {
        buffersAsByteBuffers.put(id, byteBuffer);
    }
    
    /**
     * Returns the byte buffer that corresponds to the {@link Buffer}
     * with the given ID, or <code>null</code> if no such buffer is found.
     * 
     * @param bufferId The {@link Buffer} ID
     * @return The byte buffer
     */
    ByteBuffer getBufferAsByteBuffer(String bufferId)
    {
        return buffersAsByteBuffers.get(bufferId);
    }

    /**
     * Store the given byte buffer under the given {@link BufferView} id
     * 
     * @param id The ID
     * @param byteBuffer The buffer
     */
    void putBufferViewAsByteBuffer(String id, ByteBuffer byteBuffer)
    {
        bufferViewsAsByteBuffers.put(id, byteBuffer);
    }
    
    /**
     * Returns the byte buffer that corresponds to the {@link BufferView}
     * with the given ID, or <code>null</code> if no such buffer is found.
     * 
     * @param bufferViewId The {@link BufferView} ID
     * @return The byte buffer
     */
    public ByteBuffer getBufferViewAsByteBuffer(String bufferViewId)
    {
        return bufferViewsAsByteBuffers.get(bufferViewId);
    }
    
    
}