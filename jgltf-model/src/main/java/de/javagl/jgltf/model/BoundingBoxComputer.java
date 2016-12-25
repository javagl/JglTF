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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Mesh;
import de.javagl.jgltf.impl.MeshPrimitive;
import de.javagl.jgltf.impl.Node;
import de.javagl.jgltf.impl.Scene;

/**
 * A (package-private!) utility class to compute bounding volumes
 */
class BoundingBoxComputer
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(BoundingBoxComputer.class.getName());
    
    /**
     * The {@link GltfData} 
     */
    private final GltfData gltfData;
    
    /**
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * Create a new bounding box computer for the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     */
    BoundingBoxComputer(GltfData gltfData)
    {
        this.gltfData = gltfData;
        this.gltf = gltfData.getGltf();
    }
    
    /**
     * Compute the bounding box of the {@link GltfData}
     * 
     * @return The bounding box
     */
    BoundingBox compute()
    {
        BoundingBox boundingBox = new BoundingBox();
        Map<String, Scene> scenes = optional(gltf.getScenes());
        for (String sceneId : scenes.keySet())
        {
            float rootTransform[] = MathUtils.createIdentity4x4();
            computeSceneBoundingBox(sceneId, rootTransform, boundingBox);
        }
        return boundingBox;
    }
    
    /**
     * Recursively compute the bounding box of the {@link MeshPrimitive}
     * objects of all {@link Mesh} objects in the specified scene (including 
     * the respective global node transforms). 
     * If the given result is <code>null</code>, then a new bounding box
     * will be created and returned.
     * 
     * @param sceneId The {@link Scene} ID
     * @param transform The root transform, as a column major 4x4 matrix
     * @param boundingBox The optional bounding box that will store the result 
     * @return The result
     */
    private BoundingBox computeSceneBoundingBox(
        String sceneId, float transform[], BoundingBox boundingBox)
    {
        BoundingBox localResult = boundingBox;
        if (localResult == null)
        {
            localResult = new BoundingBox();
        }
        Scene scene = gltf.getScenes().get(sceneId);
        List<String> sceneNodes = optional(scene.getNodes());
        for (String sceneNodeId : sceneNodes)
        {
            computeNodeBoundingBox(sceneNodeId, transform, localResult);
        }
        return localResult;
    }
    
    
    /**
     * Recursively compute the bounding box of the {@link MeshPrimitive}
     * objects of all {@link Mesh} objects in the specified node and its 
     * children (including the respective global node transforms). 
     * If the given result is <code>null</code>, then a new bounding box
     * will be created and returned.
     * 
     * @param nodeId The {@link Node} ID
     * @param parentTransform The transform, as a column major 4x4 matrix
     * @param boundingBox The optional bounding box that will store the result 
     * @return The result
     */
    private BoundingBox computeNodeBoundingBox(
        String nodeId, float parentTransform[], BoundingBox boundingBox) 
    {
        BoundingBox result = boundingBox;
        if (result == null)
        {
            result = new BoundingBox();
        }

        Node node = gltf.getNodes().get(nodeId);
        
        float[] localTransform = Nodes.computeLocalTransform(node, null);
        float[] transform = new float[16];
        MathUtils.mul4x4(parentTransform, localTransform, transform);
        
        List<String> meshes = node.getMeshes();
        if (meshes != null && !meshes.isEmpty())
        {
            for (String meshId : meshes)
            {
                BoundingBox meshBoundingBox =
                    computeMeshBoundingBox(
                        meshId, transform, result);
                result.combine(meshBoundingBox);
            }
        }
        
        List<String> children = optional(node.getChildren());
        for (String childNodeId : children)
        {
            computeNodeBoundingBox(childNodeId, transform, result);
        }
        return result;
    }

    /**
     * Compute the bounding box of the specified {@link Mesh}, under
     * the given transform.
     * If the given result is <code>null</code>, then a new bounding box
     * will be created and returned.
     * 
     * @param meshId The {@link Mesh} ID
     * @param transform The optional transform. If this is <code>null</code>,
     * then the identity matrix will be assumed.
     * @param boundingBox The optional bounding box that will store the result 
     * @return The result
     */
    private BoundingBox computeMeshBoundingBox(
        String meshId, float transform[], BoundingBox boundingBox)
    {
        BoundingBox result = boundingBox;
        if (result == null)
        {
            result = new BoundingBox();
        }
        
        Mesh mesh = gltf.getMeshes().get(meshId);
        List<MeshPrimitive> primitives = optional(mesh.getPrimitives());
        for (MeshPrimitive meshPrimitive : primitives)
        {
            BoundingBox meshPrimitiveBoundingBox =
                computeBoundingBox(meshPrimitive, transform);
            if (meshPrimitiveBoundingBox != null)
            {
                result.combine(meshPrimitiveBoundingBox);
            }
        }
        return result;
    }
    
    /**
     * Compute the bounding box of the given {@link MeshPrimitive}, under
     * the given transform.
     * 
     * @param meshPrimitive The {@link MeshPrimitive}
     * @param transform The optional transform. If this is <code>null</code>,
     * then the identity matrix will be assumed.
     * @return The {@link BoundingBox}, or <code>null</code> if the given
     * {@link MeshPrimitive} does not refer to an {@link Accessor} 
     * with its <code>"POSITION"</code> attribute. If if refers to
     * an {@link Accessor} that does not contain 3D float elements,
     * then a warning will be printed and <code>null</code> will be
     * returned. 
     */
    private BoundingBox computeBoundingBox(
        MeshPrimitive meshPrimitive, float transform[])
    {
        Map<String, String> attributes = 
            optional(meshPrimitive.getAttributes());
        
        String positionsAttributeName = "POSITION";
        String positionAccessorId = attributes.get(positionsAttributeName);
        if (positionAccessorId == null)
        {
            return null;
        }
        Map<String, Accessor> accessors = gltf.getAccessors();
        Accessor accessor = accessors.get(positionAccessorId);
        if (accessor == null)
        {
            return null;
        }
        
        String accessorType = accessor.getType();
        int numComponents = 
            Accessors.getNumComponentsForAccessorType(accessorType);
        if (numComponents != 3)
        {
            logger.warning("Mesh primitive " + positionsAttributeName + 
                " attribute refers to an accessor with type " + accessorType + 
                " - expected \"VEC3\"");
            return null;
        }
        if (!AccessorDatas.hasFloatComponents(accessor))
        {
            logger.warning("Mesh primitive " + positionsAttributeName + 
                " attribute refers to an accessor with component type " + 
                GltfConstants.stringFor(accessor.getComponentType()) + 
                " - expected GL_FLOAT");
            return null;
        }
        
        
        AccessorFloatData accessorData = 
            AccessorDatas.createFloat(accessor, gltfData);
        float point[] = new float[3];
        float transformedPoint[];
        if (transform != null)
        {
            transformedPoint = new float[3];
        }
        else
        {
            transformedPoint = point;
        }
        
        BoundingBox boundingBox = new BoundingBox();
        for (int e = 0; e < accessorData.getNumElements(); e++)
        {
            for (int c = 0; c < accessorData.getNumComponentsPerElement(); c++)
            {
                point[c] = accessorData.get(e, c);
            }
            if (transform != null)
            {
                MathUtils.transformPoint3D(transform, point, transformedPoint);
            }
            boundingBox.combine(
                transformedPoint[0], 
                transformedPoint[1], 
                transformedPoint[2]);
        }
        return boundingBox;
    }
    
    
    /**
     * Returns the given list, or an empty list if the given list 
     * is <code>null</code>
     * 
     * @param list The list
     * @return The result
     */
    private static <T> List<T> optional(List<T> list)
    {
        return list != null ? list : Collections.emptyList();
    }
    
    /**
     * Returns the given map, or an empty list if the given map 
     * is <code>null</code>
     * 
     * @param map The map
     * @return The result
     */
    private static <K, V> Map<K, V> optional(Map<K, V> map)
    {
        return map != null ? map : Collections.emptyMap();
    }
    
}
