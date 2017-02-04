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

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;

/**
 * A class storing a {@link GlTF} and the associated (binary) data that was 
 * read from external sources linked in the {@link GlTF}, or from the 
 * glTF file itself for the case of binary or embedded glTFs.
 */
public final class GltfData
{
    /**
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * The {@link GlTF#getBuffers()}, as byte buffers
     */
    private final Map<String, ByteBuffer> bufferDatas;

    /**
     * The {@link GlTF#getBufferViews()}, as byte buffers
     */
    private final Map<String, ByteBuffer> bufferViewDatas;

    /**
     * The {@link GlTF#getImages()}, as byte buffers containing the raw data
     */
    private final Map<String, ByteBuffer> imageDatas;

    /**
     * The {@link GlTF#getShaders()}, as byte buffers containing the raw data
     */
    private final Map<String, ByteBuffer> shaderDatas;
    
    /**
     * Creates the new {@link GltfData}. 
     * 
     * @param gltf The {@link GlTF}
     */
    public GltfData(GlTF gltf)
    {
        this.gltf = gltf;
        
        this.bufferDatas = new ConcurrentHashMap<String, ByteBuffer>();
        this.bufferViewDatas = new ConcurrentHashMap<String, ByteBuffer>();
        this.imageDatas = new ConcurrentHashMap<String, ByteBuffer>();
        this.shaderDatas = new ConcurrentHashMap<String, ByteBuffer>();
    }
    
    /**
     * Copy the data from the given {@link GltfData} into this one. This
     * will establish all ID-to-data mappings that exist in the given 
     * {@link GltfData} in this one (overwriting any existing mappings).
     * The data elements will be copied by reference (that is, it will
     * not create a deep copy of the data elements).
     * 
     * @param that The {@link GltfData} to copy
     */
    public void copy(GltfData that)
    {
        this.bufferDatas.putAll(that.bufferDatas);
        this.bufferViewDatas.putAll(that.bufferViewDatas);
        this.imageDatas.putAll(that.imageDatas);
        this.shaderDatas.putAll(that.shaderDatas);
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
     * Store the given byte buffer under the given {@link Shader} id
     * 
     * @param id The {@link Shader} ID
     * @param byteBuffer The byte buffer containing the shader data
     */
    public void putShaderData(String id, ByteBuffer byteBuffer)
    {
        shaderDatas.put(id, byteBuffer);
    }
    
    /**
     * Returns the byte buffer containing the data of the {@link Shader}
     * with the given ID, or <code>null</code> if no such data is found.
     * 
     * @param id The {@link Shader} ID
     * @return The byte buffer
     */
    public ByteBuffer getShaderData(String id)
    {
        return shaderDatas.get(id);
    }
    
    /**
     * Store the given byte buffer under the given {@link Image} ID
     * 
     * @param id The {@link Image} ID
     * @param byteBuffer The byte buffer containing the image data
     */
    public void putImageData(String id, ByteBuffer byteBuffer)
    {
        imageDatas.put(id, byteBuffer);
    }
    
    /**
     * Returns the byte buffer containing the data of the {@link Image}
     * with the given ID, or <code>null</code> if no such data is found.
     * 
     * @param id The {@link Image} ID
     * @return The byte buffer
     */
    public ByteBuffer getImageData(String id)
    {
        return imageDatas.get(id);
    }
    
    /**
     * Store the given byte buffer under the given {@link Buffer} ID
     * 
     * @param id The {@link Buffer} ID
     * @param byteBuffer The byte buffer
     */
    public void putBufferData(String id, ByteBuffer byteBuffer)
    {
        bufferDatas.put(id, byteBuffer);
    }
    
    /**
     * Returns the byte buffer that contains the data of the {@link Buffer}
     * with the given ID, or <code>null</code> if no such data is found.
     * 
     * @param id The {@link Buffer} ID
     * @return The byte buffer
     */
    public ByteBuffer getBufferData(String id)
    {
        return bufferDatas.get(id);
    }
    
    /**
     * Store the given byte buffer under the given {@link BufferView} ID
     * 
     * @param id The {@link BufferView} ID
     * @param byteBuffer The byte buffer
     */
    public void putBufferViewData(String id, ByteBuffer byteBuffer)
    {
        bufferViewDatas.put(id, byteBuffer);
    }
    
    /**
     * Returns the byte buffer that contains the data of the {@link BufferView} 
     * with the given ID, or <code>null</code> if no such data is found.
     * 
     * @param id The {@link BufferView} ID
     * @return The byte buffer
     */
    public ByteBuffer getBufferViewData(String id)
    {
        return bufferViewDatas.get(id);
    }
    
}