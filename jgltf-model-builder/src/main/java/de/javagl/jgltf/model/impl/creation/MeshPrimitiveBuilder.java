/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.impl.creation;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class for building {@link MeshPrimitiveModel} instances.
 */
public final class MeshPrimitiveBuilder 
{
    /**
     * A class encapsulating the info that is required for creating an
     * {@link AccessorModel} in the buffer structure
     */
    private static class AccessorInfo
    {
        /**
         * The prefix for the ID
         */
        private final String idPrefix;
        
        /**
         * The component type (e.g. GL_FLOAT)
         */
        private final int componentType;
        
        /**
         * The type (e.g. "VEC3")
         */
        private final String type;
        
        /**
         * The actual data
         */
        private final ByteBuffer data;

        /**
         * Default constructor
         * 
         * @param idPrefix The ID prefix
         * @param componentType The component type
         * @param type The type
         * @param data The data
         */
        private AccessorInfo(
            String idPrefix, int componentType, String type, ByteBuffer data)
        {
            this.idPrefix = idPrefix;
            this.componentType = componentType;
            this.type = type;
            this.data = data;
        }
    }
    
    /**
     * The {@link BufferStructureBuilder} that receives the data
     */
    private final BufferStructureBuilder bufferStructureBuilder;
    
    /**
     * The {@link MeshPrimitiveModel#getMode() rendering mode}
     */
    private int mode;
    
    /**
     * The {@link AccessorInfo} for the indices
     */
    private AccessorInfo indicesAccessorInfo;
    
    /**
     * The mapping from attribute names (e.g. "POSITION") to the
     * {@link AccessorInfo}
     */
    private final Map<String, AccessorInfo> attributeAccessorInfos;
    
    /**
     * A consumer that will be notified about all instances that are created
     */
    private final Consumer<? super MeshPrimitiveModel> resultConsumer;
    
    /**
     * Default constructor
     */
    MeshPrimitiveBuilder()
    {
        this(new BufferStructureBuilder(), null);
    }
    
    /**
     * Default constructor.
     * 
     * @param bufferStructureBuilder The {@link BufferStructureBuilder} that 
     * will internally keep track of the data that was added, to eventually 
     * create the {@link BufferStructure} that includes the data for the 
     * {@link MeshPrimitiveModel}
     * @param resultConsumer An optional consumer for the results that are
     * created
     */
    MeshPrimitiveBuilder(
        BufferStructureBuilder bufferStructureBuilder,
        Consumer<? super MeshPrimitiveModel> resultConsumer)
    {
        this.bufferStructureBuilder = 
            Objects.requireNonNull(bufferStructureBuilder, 
                "The bufferStructureBuilder may not be null");
        this.mode = GltfConstants.GL_TRIANGLES;
        this.attributeAccessorInfos = new LinkedHashMap<String, AccessorInfo>();
        this.resultConsumer = resultConsumer;
    }
    
    /**
     * Set the {@link MeshPrimitiveModel#getMode() rendering mode} to
     * "triangles"
     * 
     * @return This builder
     */
    public MeshPrimitiveBuilder setTriangles()
    {
        this.mode = GltfConstants.GL_TRIANGLES;
        return this;
    }
    
    /**
     * Set the {@link MeshPrimitiveModel#getMode() rendering mode} to
     * "lines"
     * 
     * @return This builder
     */
    public MeshPrimitiveBuilder setLines()
    {
        this.mode = GltfConstants.GL_LINES;
        return this;
    }
    
    /**
     * Set the {@link MeshPrimitiveModel#getMode() rendering mode} to
     * "points"
     * 
     * @return This builder
     */
    public MeshPrimitiveBuilder setPoints()
    {
        this.mode = GltfConstants.GL_POINTS;
        return this;
    }
    
    /**
     * Set the given indices as the indices for the mesh primitive. 
     * The indices will be of the type "unsigned int"
     * 
     * @param indices The indices 
     * @return This builder
     */
    public MeshPrimitiveBuilder setIntIndices(IntBuffer indices)
    {
        return setIndicesInternal(  
            GltfConstants.GL_UNSIGNED_INT, "SCALAR",
            Buffers.createByteBufferFrom(indices));
    }
    
    /**
     * Set the given indices as the indices for the mesh primitive. 
     * The indices will be of the type "unsigned short", and be
     * created by casting the elements of the given buffer to 
     * "short"
     * 
     * @param indices The indices 
     * @return This builder
     */
    public MeshPrimitiveBuilder setIntIndicesAsShort(IntBuffer indices)
    {
        return setIndicesInternal(  
            GltfConstants.GL_UNSIGNED_SHORT, "SCALAR",
            Buffers.castToShortByteBuffer(indices));
    }

    /**
     * Set the given indices as the indices for the mesh primitive. 
     * The indices will be of the type "unsigned short".
     * 
     * @param indices The indices 
     * @return This builder
     */
    public MeshPrimitiveBuilder setShortIndices(ShortBuffer indices)
    {
        return setIndicesInternal(  
            GltfConstants.GL_UNSIGNED_SHORT, "SCALAR",
            Buffers.createByteBufferFrom(indices));
    }
    
    /**
     * Set the given indices as the indices for the mesh primitive. 
     * The indices will be of the type "unsigned byte".
     * 
     * @param indices The indices 
     * @return This builder
     */
    public MeshPrimitiveBuilder setByteIndices(ByteBuffer indices)
    {
        return setIndicesInternal(  
            GltfConstants.GL_UNSIGNED_BYTE, "SCALAR", 
            Buffers.copyOf(indices, indices.capacity()));
    }
    
    
    /**
     * Internal method to set the indices
     * 
     * @param componentType The component type
     * @param type The type string
     * @param byteBuffer The byte buffer with the data
     * @return This builder
     */
    private MeshPrimitiveBuilder setIndicesInternal(
        int componentType, String type, ByteBuffer byteBuffer)
    {
        indicesAccessorInfo = new AccessorInfo(
            "indices accessor", componentType, type, byteBuffer);
        return this;
    }
    
    /**
     * Add the given data as the "POSITION" attribute of the mesh primitive.
     * The data will be interpreted as 3D float values.
     * 
     * @param data The data
     * @return This builder
     */
    public MeshPrimitiveBuilder addPositions3D(FloatBuffer data)
    {
        return addAttributeInternal("positions", "POSITION", 3, data);
    }

    /**
     * Add the given data as the "POSITION" attribute of the mesh primitive.
     * The data will be interpreted as 4D float values.
     * 
     * @param data The data
     * @return This builder
     */
    public MeshPrimitiveBuilder addPositions4D(FloatBuffer data)
    {
        return addAttributeInternal("positions", "POSITION", 4, data);
    }
    
    /**
     * Add the given data as the "NORMAL" attribute of the mesh primitive.
     * The data will be interpreted as 3D float values.
     * 
     * @param data The data
     * @return This builder
     */
    public MeshPrimitiveBuilder addNormals3D(FloatBuffer data)
    {
        return addAttributeInternal("normals", "NORMAL", 3, data);
    }

    /**
     * Add the given data as the "NORMAL" attribute of the mesh primitive.
     * The data will be interpreted as 4D float values.
     * 
     * @param data The data
     * @return This builder
     */
    public MeshPrimitiveBuilder addNormals4D(FloatBuffer data)
    {
        return addAttributeInternal("normals", "NORMAL", 4, data);
    }
    
    /**
     * Add the given data as the "TEXCOORD_0" attribute of the mesh primitive.
     * The data will be interpreted as 2D float values.
     * 
     * @param data The data
     * @return This builder
     */
    public MeshPrimitiveBuilder addTexCoords02D(FloatBuffer data)
    {
        return addAttributeInternal("texcoords0", "TEXCOORD_0", 2, data);
    }
    
    /**
     * Add the given data as the "TANGENT" attribute of the mesh primitive.
     * The data will be interpreted as 3D float values.
     * 
     * @param data The data
     * @return This builder
     */
    public MeshPrimitiveBuilder addTangents3D(FloatBuffer data)
    {
        return addAttributeInternal("tangents", "TANGENT", 3, data);
    }

    /**
     * Add the given data as the "TANGENT" attribute of the mesh primitive.
     * The data will be interpreted as 4D float values.
     * 
     * @param data The data
     * @return This builder
     */
    public MeshPrimitiveBuilder addTangents4D(FloatBuffer data)
    {
        return addAttributeInternal("tangents", "TANGENT", 4, data);
    }

    /**
     * Add the given data as attribute data to the mesh primitive.
     * 
     * @param name The name for the data, only used in the ID
     * @param attributeName The attribute name, e.g. "POSITION"
     * @param dimensions The dimensions that the data has
     * @param data The actual data 
     * @return This builder
     */
    private MeshPrimitiveBuilder addAttributeInternal(
        String name, String attributeName, int dimensions, FloatBuffer data)
    {
        AccessorInfo accessorInfo = new AccessorInfo(name + " accessor",
            GltfConstants.GL_FLOAT, "VEC" + dimensions, 
            Buffers.createByteBufferFrom(data));
        attributeAccessorInfos.put(attributeName, accessorInfo);
        return this;
    }
    
    /**
     * Create the {@link MeshPrimitiveModel} containing the indices and
     * attributes that have been added.
     * 
     * @return The {@link MeshPrimitiveModel}
     */
    public MeshPrimitiveModel build()
    {
        DefaultMeshPrimitiveModel result =  
            new DefaultMeshPrimitiveModel(mode);
        
        if (indicesAccessorInfo != null)
        {
            AccessorModel indicesAccessorModel = 
                bufferStructureBuilder.createAccessorModel(
                    indicesAccessorInfo.idPrefix, 
                    indicesAccessorInfo.componentType,
                    indicesAccessorInfo.type,
                    indicesAccessorInfo.data);
            bufferStructureBuilder.createArrayElementBufferViewModel(
                "indices bufferView");
            result.setIndices(indicesAccessorModel);
        }
        
        if (!attributeAccessorInfos.isEmpty())
        {
            for (Entry<String, AccessorInfo> entry : 
                attributeAccessorInfos.entrySet())
            {
                String name = entry.getKey();
                AccessorInfo accessorInfo = entry.getValue();
                
                AccessorModel accessorModel = 
                    bufferStructureBuilder.createAccessorModel(
                        accessorInfo.idPrefix, 
                        accessorInfo.componentType,
                        accessorInfo.type,
                        accessorInfo.data);
                result.putAttribute(name, accessorModel);
            }
            bufferStructureBuilder.createArrayBufferViewModel("bufferView");
        }
        if (resultConsumer != null)
        {
            resultConsumer.accept(result);
        }
        return result;
    }
}
