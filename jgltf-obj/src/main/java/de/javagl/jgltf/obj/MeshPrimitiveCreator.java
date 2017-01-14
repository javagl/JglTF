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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.MeshPrimitive;
import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.GltfConstants;

/**
 * Utility class for creating {@link MeshPrimitive} instances for a 
 * {@link GlTF}, based on simple indexed geometry data, consisting
 * of indices, vertices (vertex positions), optional texture coordinates
 * and optional normals.<br>
 * <br>
 * It will take care of creating the appropriate {@link BufferView} and
 * {@link Accessor} instances of the data that has been added, and 
 * accumulate the geometry data in a {@link BufferCreator} that may 
 * then be used to create the {@link Buffer} for the {@link GlTF}.
 */
class MeshPrimitiveCreator
{
    /**
     * The ID that will be used for the indices {@link BufferView}
     */
    private String indicesBufferViewId;

    /**
     * The ID that will be used for the vertices {@link BufferView}
     */
    private String verticesBufferViewId;

    /**
     * The ID that will be used for the texture coordinates {@link BufferView}
     */
    private String texCoordsBufferViewId;

    /**
     * The ID that will be used for the normals {@link BufferView}
     */
    private String normalsBufferViewId;

    /**
     * The ID that will be used for the indices {@link Accessor}
     */
    private String indicesAccessorId;

    /**
     * The ID that will be used for the vertices {@link Accessor}
     */
    private String verticesAccessorId;

    /**
     * The ID that will be used for the texture coordinates {@link Accessor}
     */
    private String texCoordsAccessorId;

    /**
     * The ID that will be used for the normals {@link Accessor}
     */
    private String normalsAccessorId;

    /**
     * The {@link GlTF} that the {@link BufferView}s and {@link Accessor}s
     * will be added to
     */
    private final GlTF gltf;
    
    /**
     * The {@link BufferCreator} that will accumulate the data that the
     * {@link MeshPrimitive}s are created from
     */
    private final BufferCreator bufferCreator;
    
    /**
     * Creates a new mesh primitive creator that will add the 
     * {@link BufferView}s and {@link Accessor}s that are created for
     * the mesh primitives to the given {@link GlTF}
     * 
     * @param gltf The {@link GlTF}
     * @param bufferCreator The {@link BufferCreator} that will accumulate 
     * the data that the {@link MeshPrimitive}s are created from, and which 
     * will afterwards be used to create the appropriate {@link Buffer}
     */
    MeshPrimitiveCreator(GlTF gltf, BufferCreator bufferCreator)
    {
        this.gltf = gltf;
        this.bufferCreator = bufferCreator;
            
    }
    
    /**
     * Create a new {@link MeshPrimitive} from the given data. The required
     * {@link BufferView} and {@link Accessor} elements will be created 
     * and added to the {@link GlTF} that was given in the constructor, and
     * the data will be appended to the {@link BufferCreator} that was 
     * given in the constructor.  
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
        Objects.requireNonNull(indices, "The indices may not be null");
        Objects.requireNonNull(vertices, "The vertices may not be null");
        
        this.indicesBufferViewId = "indicesBufferView" + idSuffix;
        this.verticesBufferViewId =  "verticesBufferView" + idSuffix;
        this.texCoordsBufferViewId = "texCoordsBufferView" + idSuffix;
        this.normalsBufferViewId = "normalsBufferView" + idSuffix;

        this.indicesAccessorId = "indicesAccessor" + idSuffix;
        this.verticesAccessorId =  "verticesAccessor" + idSuffix;
        this.texCoordsAccessorId = "texCoordsAccessor" + idSuffix;
        this.normalsAccessorId = "normalsAccessor" + idSuffix;
        
        addIndices(indices, indicesComponentType);
        addVertices(vertices);
        
        boolean hasTexCoords = texCoords != null && texCoords.capacity() != 0;
        if (hasTexCoords)
        {
            addTexCoords(texCoords);
        }
        
        boolean hasNormals = normals != null && normals.capacity() != 0;
        if (hasNormals)
        {
            addNormals(normals);
        }
        
        MeshPrimitive meshPrimitive = new MeshPrimitive();
        meshPrimitive.setMode(GltfConstants.GL_TRIANGLES);
        meshPrimitive.setIndices(indicesAccessorId);
        Map<String, String> attributes = new LinkedHashMap<String, String>();
        attributes.put("POSITION", verticesAccessorId);
        if (hasTexCoords)
        {
            attributes.put("TEXCOORD_0", texCoordsAccessorId);
        }
        if (hasNormals)
        {
            attributes.put("NORMAL", normalsAccessorId);
        }
        meshPrimitive.setAttributes(attributes);
        return meshPrimitive;
        
    }
    
    /**
     * Add the elements the {@link GlTF} that are required for representing
     * the given indices. This will create the {@link BufferView} and the
     * {@link Accessor} for the given indices, and add the given data to
     * the {@link BufferCreator}. The indices will be casted to the given 
     * component type, if necessary.
     *  
     * @param indices The indices. See {@link #createMeshPrimitive}
     * @param indicesComponentType The {@link Accessor#getComponentType()
     * component type} for the {@link Accessor} that will be created
     */
    private void addIndices(IntBuffer indices, int indicesComponentType)
    {
        int numIndices = indices.capacity();
        int indicesComponentSize =
            Accessors.getNumBytesForAccessorComponentType(indicesComponentType);
        ByteBuffer byteBuffer = 
            Buffers.convertToByteBuffer(indices, indicesComponentSize);

        BufferView bufferView = createSimpleBufferView(
            GltfConstants.GL_ELEMENT_ARRAY_BUFFER, byteBuffer);

        gltf.addBufferViews(indicesBufferViewId, bufferView);
        
        Accessor accessor = 
            createSimpleAccessor(indicesComponentType, "SCALAR", 
                numIndices, indicesBufferViewId);
         
        accessor.setMin(computeMin(accessor, byteBuffer));
        accessor.setMax(computeMax(accessor, byteBuffer));
        
        gltf.addAccessors(indicesAccessorId, accessor);
    }
    
    /**
     * Compute the {@link Accessor#getMin()} values from the given parameters
     * 
     * @param accessor The {@link Accessor}
     * @param byteBuffer The data of the {@link BufferView} of the 
     * {@link Accessor}
     * @return The minimum values
     * @throws IllegalArgumentException If the given {@link Accessor} has a
     * unknown {@link Accessor#getComponentType()} that is not a valid 
     * integral type
     */
    private static Number[] computeMin(Accessor accessor, ByteBuffer byteBuffer)
    {
        switch (accessor.getComponentType())
        {
            case GltfConstants.GL_BYTE:
            case GltfConstants.GL_UNSIGNED_BYTE:
            {
                AccessorByteData accessorData = 
                    AccessorDatas.createByte(accessor, byteBuffer);
                return NumberArrays.asNumbers(accessorData.getMinInt());
            }
            
            case GltfConstants.GL_SHORT:
            case GltfConstants.GL_UNSIGNED_SHORT:
            {
                AccessorShortData accessorData = 
                    AccessorDatas.createShort(accessor, byteBuffer);
                return NumberArrays.asNumbers(accessorData.getMinInt());
            }
            
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            {
                AccessorIntData accessorData = 
                    AccessorDatas.createInt(accessor, byteBuffer);
                return NumberArrays.asNumbers(accessorData.getMinLong());
            }
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid component type " + accessor.getComponentType());
    }
    
    /**
     * Compute the {@link Accessor#getMax()} values from the given parameters
     * 
     * @param accessor The {@link Accessor}
     * @param byteBuffer The data of the {@link BufferView} of the 
     * {@link Accessor}
     * @return The maximum values
     * @throws IllegalArgumentException If the given {@link Accessor} has a
     * unknown {@link Accessor#getComponentType()} that is not a valid 
     * integral type
     */
    private static Number[] computeMax(Accessor accessor, ByteBuffer byteBuffer)
    {
        switch (accessor.getComponentType())
        {
            case GltfConstants.GL_BYTE:
            case GltfConstants.GL_UNSIGNED_BYTE:
            {
                AccessorByteData accessorData = 
                    AccessorDatas.createByte(accessor, byteBuffer);
                return NumberArrays.asNumbers(accessorData.getMaxInt());
            }
            
            case GltfConstants.GL_SHORT:
            case GltfConstants.GL_UNSIGNED_SHORT:
            {
                AccessorShortData accessorData = 
                    AccessorDatas.createShort(accessor, byteBuffer);
                return NumberArrays.asNumbers(accessorData.getMaxInt());
            }
            
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            {
                AccessorIntData accessorData = 
                    AccessorDatas.createInt(accessor, byteBuffer);
                return NumberArrays.asNumbers(accessorData.getMaxLong());
            }
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid component type " + accessor.getComponentType());
    }
    

    /**
     * Add the elements the {@link GlTF} that are required for representing
     * the given vertices. This will create the {@link BufferView} and the
     * {@link Accessor} for the given vertices, and add the given data to
     * the {@link BufferCreator}. 
     *  
     * @param vertices The vertices. See {@link #createMeshPrimitive}
     */
    private void addVertices(FloatBuffer vertices)
    {
        int numVertices = vertices.capacity() / 3;
        ByteBuffer byteBuffer = 
            Buffers.createFloatByteBuffer(vertices);

        BufferView bufferView = createSimpleBufferView(
            GltfConstants.GL_ARRAY_BUFFER, byteBuffer);

        gltf.addBufferViews(verticesBufferViewId, bufferView);

        Accessor accessor = createSimpleAccessor(
            GltfConstants.GL_FLOAT, "VEC3", 
            numVertices, verticesBufferViewId);

        AccessorFloatData accessorData = 
            AccessorDatas.createFloat(accessor, byteBuffer);
        accessor.setMin(NumberArrays.asNumbers(accessorData.getMin()));
        accessor.setMax(NumberArrays.asNumbers(accessorData.getMax()));
        
        gltf.addAccessors(verticesAccessorId, accessor);
        
    }
    
    /**
     * Add the elements the {@link GlTF} that are required for representing
     * the given texCoords. This will create the {@link BufferView} and the
     * {@link Accessor} for the given texCoords, and add the given data to
     * the {@link BufferCreator}. 
     *  
     * @param texCoords The texCoords. See {@link #createMeshPrimitive}
     */
    private void addTexCoords(FloatBuffer texCoords)
    {
        int numTexCoords = texCoords.capacity() / 2;
        ByteBuffer byteBuffer = 
            Buffers.createFloatByteBuffer(texCoords);

        BufferView bufferView = createSimpleBufferView(
            GltfConstants.GL_ARRAY_BUFFER, byteBuffer);

        gltf.addBufferViews(texCoordsBufferViewId, bufferView);

        Accessor accessor = createSimpleAccessor(
            GltfConstants.GL_FLOAT, "VEC2", 
            numTexCoords, texCoordsBufferViewId);
        
        AccessorFloatData accessorData = 
            AccessorDatas.createFloat(accessor, byteBuffer);
        accessor.setMin(NumberArrays.asNumbers(accessorData.getMin()));
        accessor.setMax(NumberArrays.asNumbers(accessorData.getMax()));

        gltf.addAccessors(texCoordsAccessorId, accessor);
    }
    
    /**
     * Add the elements the {@link GlTF} that are required for representing
     * the given normals. This will create the {@link BufferView} and the
     * {@link Accessor} for the given normals, and add the given data to
     * the {@link BufferCreator}. 
     *  
     * @param normals The normals. See {@link #createMeshPrimitive}
     */
    private void addNormals(FloatBuffer normals)
    {
        int numNormals = normals.capacity() / 3;
        ByteBuffer byteBuffer = 
            Buffers.createFloatByteBuffer(normals);

        BufferView bufferView = createSimpleBufferView(
            GltfConstants.GL_ARRAY_BUFFER, byteBuffer);

        gltf.addBufferViews(normalsBufferViewId, bufferView);

        Accessor accessor = createSimpleAccessor(
            GltfConstants.GL_FLOAT, "VEC3", 
            numNormals, normalsBufferViewId);

        AccessorFloatData accessorData = 
            AccessorDatas.createFloat(accessor, byteBuffer);
        accessor.setMin(NumberArrays.asNumbers(accessorData.getMin()));
        accessor.setMax(NumberArrays.asNumbers(accessorData.getMax()));

        gltf.addAccessors(normalsAccessorId, accessor);
    }

    /**
     * Create a simple {@link BufferView} with the given 
     * {@link BufferView#getTarget() target} and data. The given data
     * will be appended to the {@link BufferCreator} that was given 
     * in the constructor.
     * 
     * @param target The {@link BufferView#getTarget() target}
     * @param byteBuffer The byte buffer containing the data
     * @return The {@link BufferView}
     */
    private BufferView createSimpleBufferView(int target, ByteBuffer byteBuffer)
    {
        
        BufferView bufferView = new BufferView();
        bufferView.setBuffer(bufferCreator.getBufferId());
        bufferView.setTarget(target);
        
        // Compute the byte offset, inserting padding bytes if necessary.
        // The alignment for the bufferView is fixed to be 4 here:
        // For the indices bufferView, it will have no effect, because 
        // the initial buffer length is 0. For the other bufferViews,
        // the alignment is always 4, for the float component type.
        final int alignment = 4;
        int remainder = bufferCreator.getByteOffset() % alignment;
        if (remainder > 0)
        {
            int paddingBytes = alignment - remainder;
            bufferCreator.appendPadding(paddingBytes);
        }
        
        bufferView.setByteOffset(bufferCreator.getByteOffset());
        bufferView.setByteLength(byteBuffer.capacity());
        
        bufferCreator.append(byteBuffer);
        
        return bufferView;
    }
    
    /**
     * Create a "simple" {@link Accessor} from the given parameters. The
     * {@link Accessor} will have a {@link Accessor#getByteOffset() byte 
     * offset} of 0, and a {@link Accessor#getByteStride() byte stride}
     * that depends only of the size of the component type and the number
     * of components per element for the given type (that is, it will
     * describe a <b>non</b>-interleaved access pattern).
     * 
     * @param componentType The {@link Accessor#getComponentType() component 
     * type}
     * @param type The {@link Accessor#getType() type}
     * @param count The {@link Accessor#getCount() count} 
     * @param bufferViewId The {@link Accessor#getBufferView() buffer view ID}
     * @return The new {@link Accessor}
     */
    private static Accessor createSimpleAccessor(
        int componentType, String type, int count, String bufferViewId)
    {
        Accessor accessor = new Accessor();
        accessor.setComponentType(componentType);
        accessor.setType(type);
        accessor.setByteOffset(0);
        int numComponents = Accessors.getNumComponentsForAccessorType(type);
        int numBytesPerComponent = 
            Accessors.getNumBytesForAccessorComponentType(componentType);
        accessor.setByteStride(numComponents * numBytesPerComponent);
        accessor.setCount(count);
        accessor.setBufferView(bufferViewId);
        return accessor;
    }

}