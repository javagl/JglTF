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
package de.javagl.jgltf.obj;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.MeshPrimitive;

/**
 * A class for creating {@link Buffer}s for a {@link GlTF}. The 
 * {@link #createMeshPrimitive} method may be used to generate the 
 * {@link MeshPrimitive} instances that should refer to the buffer. 
 * After mesh primitives have been created, the {@link #commitBuffer()} 
 * method may be used to finish the buffer and add it to the glTF.
 */
class BufferCreator
{
    /**
     * The {@link GlTF} that the {@link BufferView}s, {@link Accessor}s 
     * and {@link Buffer}s will be added to.  
     */
    private final GlTF gltf;

    /**
     * The prefix that should be used for the {@link Buffer#getUri() buffer 
     * URIs}
     */
    private final String uriPrefix;

    /**
     * The ID of the {@link Buffer} that is currently built
     */
    private String bufferId;
    
    /**
     * The {@link Buffer#getUri() buffer URI} of the {@link Buffer} that 
     * is currently built
     */
    private String bufferUri;

    /**
     * The byte buffers that have been added so far for the buffer that
     * is currently being built, and whose data will eventually be combined 
     * to obtain the actual {@link Buffer} data
     */
    private List<ByteBuffer> byteBuffers;
    
    /**
     * The byte offset implied by the byte buffers that have been added so far
     * for the current buffer 
     */
    private int byteOffset;

    /**
     * The mapping from the IDs of the {@link Buffer}s that have been built
     * to the {@link Buffer} data 
     */
    private final Map<String, ByteBuffer> bufferDatas;
    
    /**
     * Helper for creating the {@link MeshPrimitive} instances
     */
    private MeshPrimitiveCreator meshPrimitiveCreator;
    
    /**
     * Creates a new creator for {@link Buffer}s in the given {@link GlTF}.
     * It will use the given prefix for the {@link Buffer#getUri() buffer URIs}
     * 
     * @param gltf The {@link GlTF}
     * @param uriPrefix The {@link Buffer#getUri() buffer URI} prefix
     */
    BufferCreator(GlTF gltf, String uriPrefix)
    {
        this.gltf = gltf;
        this.uriPrefix = uriPrefix;
        this.bufferDatas = new LinkedHashMap<String, ByteBuffer>();
    }
    
    /**
     * Start the creation of a new {@link Buffer}
     */
    private void startBuffer()
    {
        bufferId = Gltfs.generateId("buffer", gltf.getBuffers());
        bufferUri = uriPrefix + bufferDatas.size() + ".bin";
        byteBuffers = new ArrayList<ByteBuffer>();
        byteOffset = 0;
        meshPrimitiveCreator = new MeshPrimitiveCreator(gltf, this);
    }
    
    /**
     * Store the {@link Buffer} that is currently being built in the 
     * {@link GlTF}, and the associated buffer data in the
     * {@link #getBufferDatas() buffer datas}. 
     */
    void commitBuffer()
    {
        Buffer buffer = createBuffer();
        gltf.addBuffers(bufferId, buffer);
        ByteBuffer bufferData = createBufferData();
        bufferDatas.put(bufferId, bufferData);
        bufferId = null;
    }
    
    /**
     * Returns an unmodifiable view on the mapping from {@link Buffer} IDs
     * to the data that belongs to the respective buffer.
     * 
     * @return The mapping
     */
    Map<String, ByteBuffer> getBufferDatas()
    {
        return Collections.unmodifiableMap(bufferDatas);
    }
    
    /**
     * Create a new {@link MeshPrimitive} from the given data. The required
     * {@link BufferView} and {@link Accessor} elements will be created 
     * and added to the {@link GlTF} that was given in the constructor, and
     * the data will be appended to the {@link Buffer} that is currently 
     * being built.  
     * 
     * @param idSuffix The suffix that should be appended to the 
     * {@link BufferView} and {@link Accessor} IDs
     * @param indices The indices
     * @param indicesComponentType The component type that should be used
     * for the indices. The given integer values will be casted to this
     * target type, if necessary
     * @param vertices The vertices. Three consecutive elements of this
     * buffer will be the 3D coordinates of one vertex
     * @param texCoords The optional texture coordinates. If this is not
     * <code>null</code> and not empty, then two consecutive elements
     * of this buffer will be the 2D texture coordinates
     * @param normals The optional normals. If this is not
     * <code>null</code> and not empty, then three consecutive elements
     * of this buffer will be the 3D normals
     * @return The {@link MeshPrimitive}
     */
    MeshPrimitive createMeshPrimitive(String idSuffix, 
        IntBuffer indices, int indicesComponentType,
        FloatBuffer vertices, FloatBuffer texCoords, FloatBuffer normals)
    {
        if (bufferId == null)
        {
            startBuffer();
        }
        return meshPrimitiveCreator.createMeshPrimitive(
            idSuffix, indices, indicesComponentType, 
            vertices, texCoords, normals);
    }

    /**
     * Returns the ID of the {@link Buffer} that is created by this instance
     * 
     * @return The {@link Buffer} ID
     */
    String getBufferId()
    {
        return bufferId;
    }
    
    /**
     * Append the given byte buffer to the data that should be contained
     * in the {@link Buffer}
     * 
     * @param byteBuffer The byte buffer to add
     */
    void append(ByteBuffer byteBuffer)
    {
        byteBuffers.add(byteBuffer);
        byteOffset += byteBuffer.capacity();
    }
    
    /**
     * Append the given number of padding bytes to the {@link Buffer}
     * 
     * @param bytes The number of bytes to append
     */
    void appendPadding(int bytes)
    {
        if (bytes > 0)
        {
            append(ByteBuffer.allocate(bytes));
        }
    }
    
    
    /**
     * Returns the byte offset that is implied by the data that has been
     * added to the buffer so far
     *  
     * @return The current byte offset
     */
    int getByteOffset()
    {
        return byteOffset;
    }
    
    /**
     * Create the {@link Buffer} instance for the data that has been
     * added so far
     * 
     * @return The {@link Buffer}
     */
    Buffer createBuffer()
    {
        Buffer buffer = new Buffer();
        buffer.setType("arraybuffer");
        buffer.setUri(bufferUri);
        buffer.setByteLength(byteOffset);
        return buffer;
    }
    
    /**
     * Create a new direct byte buffer with native byte order that contains
     * the {@link Buffer} data. That is, the data of all byte buffers that 
     * have been {@link #append(ByteBuffer) appended} so far
     * 
     * @return The {@link Buffer} data
     */
    ByteBuffer createBufferData()
    {
        return Buffers.concat(byteBuffers);
    }
    
}