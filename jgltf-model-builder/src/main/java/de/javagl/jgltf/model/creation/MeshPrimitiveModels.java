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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Methods related to {@link MeshPrimitiveModel} instances.<br>
 * <br>
 * These methods are convenience methods for common cases of mesh primitives.
 * The {@link MeshPrimitiveBuilder} class offers more control and further
 * methods for creating mesh primitive models. 
 */
public class MeshPrimitiveModels
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(MeshPrimitiveModels.class.getName());
    
    
    /**
     * Creates a new mesh primitive model from the given properties.<br>
     * <br>
     * All properties except for the <code>positions3D</code> are optional.<br>
     * <br>
     * If the given <code>indices</code> are <code>null</code>, then
     * a mesh primitive with mode <code>GL_POINTS</code> will be 
     * created. Otherwise, a mesh primitive with mode <code>GL_TRIANGLES</code>
     * will be created.
     * 
     * @param indices The optional indices
     * @param positions3D The 3D positions (required)
     * @param normals3D The optional normals
     * @param texCoords2D The optional texture coordinates
     * @return The mesh primitive model
     */
    public static DefaultMeshPrimitiveModel create(
        int indices[], float positions3D[], 
        float normals3D[], float texCoords2D[])
    {
        return create(wrapOptional(indices),
            FloatBuffer.wrap(positions3D), 
            wrapOptional(normals3D),
            wrapOptional(texCoords2D));
    }

    /**
     * Creates a new mesh primitive model from the given properties.<br>
     * <br>
     * All properties except for the <code>positions3D</code> are optional.<br>
     * <br>
     * If the given <code>indices</code> are <code>null</code>, then
     * a mesh primitive with mode <code>GL_POINTS</code> will be 
     * created. Otherwise, a mesh primitive with mode <code>GL_TRIANGLES</code>
     * will be created.
     * 
     * @param indices The optional indices
     * @param positions3D The 3D positions (required)
     * @param normals3D The optional normals
     * @param texCoords2D The optional texture coordinates
     * @return The mesh primitive model
     */
    public static DefaultMeshPrimitiveModel create(
        IntBuffer indices, FloatBuffer positions3D, 
        FloatBuffer normals3D, FloatBuffer texCoords2D)
    {
        MeshPrimitiveBuilder builder = MeshPrimitiveBuilder.create();
        if (indices != null)
        {
            builder.setTriangles();
            builder.setIntIndicesAsShort(indices);
        }
        else
        {
            builder.setPoints();
        }
        builder.addPositions3D(positions3D);
        if (normals3D != null)
        {
            builder.addNormals3D(normals3D);
        }
        if (texCoords2D != null)
        {
            builder.addTexCoords02D(texCoords2D);
        }
        DefaultMeshPrimitiveModel result = builder.build();
        return result;
    }
    
    /**
     * Convenience method for {@link #addMorphTarget(DefaultMeshPrimitiveModel, 
     * int, String, FloatBuffer)} that accepts a float array.
     * 
     * @see #addMorphTarget(DefaultMeshPrimitiveModel, int, String, FloatBuffer)
     * 
     * @param meshPrimitive The mesh primitive to add the morph target to
     * @param index The index of the morph target
     * @param attributeName The attribute name
     * @param morphedAttributeValues The morphed attribute values
     * @throws GltfException If the mesh primitive does not contain an
     * attribute with the given name
     */
    public static void addMorphTarget(DefaultMeshPrimitiveModel meshPrimitive,
        int index, String attributeName, float morphedAttributeValues[])
    {
        addMorphTarget(meshPrimitive, index, attributeName,
            FloatBuffer.wrap(morphedAttributeValues));
    }

    /**
     * Add a morph target entry to the given mesh primitive model.
     * 
     * This will set the morph target values for the specified attribute,
     * in the morph target with the given index. 
     * 
     * If the mesh primitive does not contain an attribute with the 
     * given name, then an exception will be thrown.
     * 
     * Otherwise, it is assumed that the given buffer contains the morphed
     * values for the specified attribute, with the same type and component
     * type as the attribute (e.g. 3D floating point values when the
     * <code>POSITION</code> attribute contains 3D floating point values).
     * 
     * If the morph target with the given index does not yet exist, then
     * it is created. If the index is larger than the number of morph
     * targets that have been created, then a warning will be printed,
     * and the necessary (empty) morph targets will be created.
     * 
     * If the specified target entry already existed, then a warning will
     * be printed, and it will be overwritten with the given data.
     * 
     * @param meshPrimitive The mesh primitive to add the morph target to
     * @param index The index of the morph target
     * @param attributeName The attribute name
     * @param morphedAttributeValues The morphed attribute values
     * @throws GltfException If the mesh primitive does not contain an
     * attribute with the given name
     */
    public static void addMorphTarget(DefaultMeshPrimitiveModel meshPrimitive,
        int index, String attributeName, FloatBuffer morphedAttributeValues)
    {
        Map<String, AccessorModel> attributes = meshPrimitive.getAttributes();
        AccessorModel attribute = attributes.get(attributeName);
        if (attribute == null)
        {
            throw new GltfException("The mesh primitive does not contain a "
                + attributeName + " attribute");
        }
        
        int componentType = attribute.getComponentType();
        ElementType elementType = attribute.getElementType();
        String type = elementType.toString();
        ByteBuffer byteBuffer =
            Buffers.createByteBufferFrom(morphedAttributeValues);
        DefaultAccessorModel accessorModel =
            AccessorModels.create(componentType, type, false, byteBuffer);
        
        int numExistingTargets = meshPrimitive.getTargets().size();
        if (index == numExistingTargets)
        {
            meshPrimitive.addTarget(new LinkedHashMap<String, AccessorModel>());
        }
        else if (index > numExistingTargets - 1)
        {
            logger.warning("Setting attribute in morph target " + index
                + ", even " + "though only " + numExistingTargets
                + " targets have " + "been created until now");
            int targetsToAdd = index - numExistingTargets + 1;
            for (int i = 0; i < targetsToAdd; i++)
            {
                meshPrimitive
                    .addTarget(new LinkedHashMap<String, AccessorModel>());
            }
        }
        List<Map<String, AccessorModel>> targets = meshPrimitive.getTargets();
        Map<String, AccessorModel> target = targets.get(index);
        if (target.containsKey(attributeName))
        {
            logger.warning("Overwriting existing " + attributeName
                + " in morph target " + index);
        }
        target.put(attributeName, accessorModel);
    }
    
    /**
     * Returns the given array wrapped into a buffer, or <code>null</code> if
     * the given array is <code>null</code>
     * 
     * @param array The array
     * @return The buffer
     */
    private static IntBuffer wrapOptional(int array[])
    {
        if (array == null)
        {
            return null;
        }
        return IntBuffer.wrap(array);
    }

    /**
     * Returns the given array wrapped into a buffer, or <code>null</code> if
     * the given array is <code>null</code>
     * 
     * @param array The array
     * @return The buffer
     */
    private static FloatBuffer wrapOptional(float array[])
    {
        if (array == null)
        {
            return null;
        }
        return FloatBuffer.wrap(array);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private MeshPrimitiveModels()
    {
        // Private constructor to prevent instantiation
    }
    
}
