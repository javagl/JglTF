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
package de.javagl.jgltf.model.creation;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class for building {@link MeshPrimitiveModel} instances.
 */
public final class MeshPrimitiveBuilder 
{
    /**
     * Create a new {@link MeshPrimitiveBuilder}
     * 
     * @return The {@link MeshPrimitiveBuilder}
     */
    public static MeshPrimitiveBuilder create()
    {
        return new MeshPrimitiveBuilder();
    }
    
    /**
     * The {@link MeshPrimitiveModel#getMode() rendering mode}
     */
    private int mode;
    
    /**
     * The {@link AccessorModel} for the indices
     */
    private DefaultAccessorModel indicesAccessorModel;
    
    /**
     * The mapping from attribute names (e.g. "POSITION") to the
     * {@link AccessorModel}
     */
    private final Map<String, DefaultAccessorModel> attributeAccessorModels;
    
    /**
     * Private constructor
     */
    private MeshPrimitiveBuilder()
    {
        this.mode = GltfConstants.GL_TRIANGLES;
        this.attributeAccessorModels = 
            new LinkedHashMap<String, DefaultAccessorModel>();
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
     * The indices will be of the type "unsigned byte", and be
     * created by casting the elements of the given buffer to 
     * "byte"
     * 
     * @param indices The indices 
     * @return This builder
     */
    public MeshPrimitiveBuilder setIntIndicesAsByte(IntBuffer indices)
    {
        return setIndicesInternal(  
            GltfConstants.GL_UNSIGNED_BYTE, "SCALAR",
            Buffers.castToByteBuffer(indices));
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
     * Set the given indices as the indices for the mesh primitive. 
     * The component type may be <code>GL_UNSIGNED_BYTE</code>, 
     * <code>GL_UNSIGNED_SHORT</code>, or <code>GL_UNSIGNED_INT</code>,
     * and the given indices will be casted to the given component type 
     * if necessary.
     * 
     * @param indices The indices 
     * @param componentType The component type
     * @return This builder
     * @throws IllegalArgumentException If the component type is not one
     * of the valid types listed above
     */
    public MeshPrimitiveBuilder setIndicesAs(
        IntBuffer indices, int componentType)
    {
        switch (componentType)
        {
            case GltfConstants.GL_UNSIGNED_BYTE:
                return setIntIndicesAsShort(indices);
                
            case GltfConstants.GL_UNSIGNED_SHORT:
                return setIntIndicesAsShort(indices);
                
            case GltfConstants.GL_UNSIGNED_INT:
                return setIntIndices(indices);
                
            default:
                break;
        }
        throw new IllegalArgumentException(
            "The component type must be GL_UNSIGNED_BYTE," + 
            "GL_UNSIGNED_SHORT or GL_UNSIGNED_INT, but is " +
            GltfConstants.stringFor(componentType));
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
        DefaultAccessorModel indices = AccessorModels.create(
            componentType, type, byteBuffer);
        return setIndices(indices);
    }
    
    /**
     * Set the indices of the currently built {@link MeshPrimitiveModel} 
     * to the given {@link AccessorModel}
     * 
     * @param indices The {@link AccessorModel} for the indices
     * @return This builder
     */
    public MeshPrimitiveBuilder setIndices(DefaultAccessorModel indices)
    {
        indicesAccessorModel = indices;
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
        return addAttributeInternal("POSITION", 3, data);
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
        return addAttributeInternal("POSITION", 4, data);
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
        return addAttributeInternal("NORMAL", 3, data);
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
        return addAttributeInternal("NORMAL", 4, data);
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
        return addAttributeInternal("TEXCOORD_0", 2, data);
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
        return addAttributeInternal("TANGENT", 3, data);
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
        return addAttributeInternal("TANGENT", 4, data);
    }

    /**
     * Add the given data as attribute data to the mesh primitive.
     * 
     * @param attributeName The attribute name, e.g. "POSITION"
     * @param dimensions The dimensions that the data has
     * @param data The actual data 
     * @return This builder
     */
    private MeshPrimitiveBuilder addAttributeInternal(
        String attributeName, int dimensions, FloatBuffer data)
    {
        DefaultAccessorModel accessorModel = AccessorModels.create(
            GltfConstants.GL_FLOAT, "VEC" + dimensions, 
            Buffers.createByteBufferFrom(data));
        return addAttribute(attributeName, accessorModel);
    }
    
    /**
     * Add the given {@link AccessorModel} as an attribute to the 
     * {@link MeshPrimitiveModel} that is currently being built.
     * 
     * @param attributeName The attribute name, e.g. "POSITION"
     * @param attribute The {@link AccessorModel} with the attribute data
     * @return This builder
     */
    public MeshPrimitiveBuilder addAttribute(
        String attributeName, DefaultAccessorModel attribute)
    {
        attributeAccessorModels.put(attributeName, attribute);
        return this;
    }
    
    
    /**
     * Create the {@link MeshPrimitiveModel} containing the indices and
     * attributes that have been added.
     * 
     * @return The {@link MeshPrimitiveModel}
     */
    public DefaultMeshPrimitiveModel build()
    {
        DefaultMeshPrimitiveModel result =  
            new DefaultMeshPrimitiveModel(mode);
        
        if (indicesAccessorModel != null)
        {
            result.setIndices(indicesAccessorModel);
            indicesAccessorModel = null;
        }
        
        if (!attributeAccessorModels.isEmpty())
        {
            for (Entry<String, DefaultAccessorModel> entry : 
                attributeAccessorModels.entrySet())
            {
                String name = entry.getKey();
                DefaultAccessorModel accessorModel = entry.getValue();
                result.putAttribute(name, accessorModel);
            }
            attributeAccessorModels.clear();
        }
        return result;
    }
}

