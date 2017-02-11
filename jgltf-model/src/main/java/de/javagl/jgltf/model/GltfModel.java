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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Camera;
import de.javagl.jgltf.impl.v1.CameraOrthographic;
import de.javagl.jgltf.impl.v1.CameraPerspective;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Node;

/**
 * A class that serves as a data model for a {@link GlTF} and allows obtaining
 * suppliers for the data elements that are described in the {@link GlTF}.<br>
 * <br>
 * Unless otherwise noted, none of the parameters for the methods of this
 * class may be <code>null</code>.<br>
 * <br>
 * Note: The suppliers that are provided by this class may throw a
 * {@link GltfException} when they encounter an invalid state (for
 * example, an ID that is not mapped to a valid object).
 */
public final class GltfModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModel.class.getName());

    /**
     * The {@link GltfData} of this model
     */
    private final GltfData gltfData;

    /**
     * The {@link GlTF} of this model
     */
    private final GlTF gltf;

    /**
     * A mapping from {@link Node} IDs to their parent nodes
     */
    private final Map<String, String> nodeIdToParentNodeId;
    
    /**
     * The mapping from joint names to the ID of the {@link Node} with the 
     * respective {@link Node#getJointName() joint name}
     */
    private final Map<String, String> jointNameToNodeId;
    
    /**
     * The root transform of the {@link GlTF}, as a column-major 4x4 matrix
     */
    private final float rootTransform[];
    
    /**
     * A supplier for the aspect ratio. This may be used to override the
     * aspect ratio that is provided by the camera, using an aspect ratio
     * that depends, for example, on the rendering window size. If this is 
     * <code>null</code>, then the aspect ratio of the camera will be used
     */
    private final DoubleSupplier aspectRatioSupplier;
    
    /**
     * Creates a new model for the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @param rootTransform The root transform of the {@link GlTF}, as a 
     * column-major 4x4 matrix. If this is <code>null</code>, then the
     * identity matrix will be used.
     * @param aspectRatioSupplier An optional supplier for the aspect ratio. 
     * If this is <code>null</code>, then the aspect ratio of the 
     * camera will be used
     */
    public GltfModel(GltfData gltfData, 
        float rootTransform[],
        DoubleSupplier aspectRatioSupplier)
    {
        Objects.requireNonNull(gltfData, 
            "The gltfData may not be null");

        this.gltfData = gltfData;
        this.gltf = gltfData.getGltf();
        this.rootTransform = rootTransform != null ? 
            rootTransform : MathUtils.createIdentity4x4();
        this.aspectRatioSupplier = aspectRatioSupplier;
        
        this.nodeIdToParentNodeId = computeNodeIdToParentNodeIdMap();
        this.jointNameToNodeId = computeJointNameToNodeIdMap();
    }
    
    /**
     * Returns the {@link GltfData} that this model was created from
     * 
     * @return The {@link GltfData}
     */
    public GltfData getGltfData()
    {
        return gltfData;
    }
    
    /**
     * Compute the mapping from {@link Node} IDs to their parent node IDs
     * 
     * @return The mapping
     */
    private Map<String, String> computeNodeIdToParentNodeIdMap()
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        for (String nodeId : nodes.keySet())
        {
            Node node = nodes.get(nodeId);
            if (node == null)
            {
                logger.severe("No node found for ID "+nodeId);
            }
            else
            {
                List<String> children = Optionals.of(node.getChildren());
                for (String childNodeId : children)
                {
                    String oldParent = map.put(childNodeId, nodeId);
                    if (oldParent != null)
                    {
                        logger.severe("Node with ID " + childNodeId + 
                            " has two parents: " + oldParent + 
                            " and " + nodeId);
                    }
                }
            }
        }
        return map;
    }
    
    /**
     * Compute the mapping from joint names to the ID of the {@link Node} with
     * the respective {@link Node#getJointName() joint name}
     * 
     * @return The mapping
     */
    private Map<String, String> computeJointNameToNodeIdMap()
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        for (Entry<String, Node> entry : nodes.entrySet())
        {
            String nodeId = entry.getKey();
            Node node = entry.getValue();
            if (node.getJointName() != null)
            {
                map.put(node.getJointName(), nodeId);
            }
        }
        return map;
    }
    
    /**
     * Returns the ID of the node with the given joint name, or 
     * <code>null</code> if no such node exists.
     * 
     * TODO: This will be refactored for glTF 2.0!
     * 
     * @param jointName The joint name
     * @return The node ID
     */
    public String getNodeIdForJointName(String jointName)
    {
        return jointNameToNodeId.get(jointName);
    }
    

    /**
     * Create a supplier for the view matrix of the camera that is attached
     * to the {@link Node} whose ID is provided by the given supplier. This 
     * will be the inverse of the global transform of the node.<br>
     * <br>
     * If the given supplier provides an ID of a {@link Node} that is
     * not contained in the glTF (or one of its parents is not contained
     * in the glTF) then a warning will be printed and the supplier will 
     * assume the identity matrix for the respective node.
     * <br> 
     * The matrix will be provided as a float array with 16 elements, 
     * storing the matrix entries in column-major order.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     * 
     * @param cameraNodeIdSupplier The supplier for the camera node ID
     * @return The supplier.
     */
    public Supplier<float[]> createViewMatrixSupplier(
        Supplier<String> cameraNodeIdSupplier)
    {
        float cameraMatrix[] = new float[16];
        float viewMatrix[] = new float[16];
        float temp[] = new float[16];
        return () ->
        {
            String nodeId = cameraNodeIdSupplier.get();
            computeGlobalTransform(nodeId, temp, cameraMatrix);
            MathUtils.invert4x4(cameraMatrix, viewMatrix);
            return viewMatrix;
        };
    }
    
    /**
     * Create the supplier of the projection matrix for the {@link Camera}
     * with the ID that is contained in the {@link Node} with the ID
     * that is provided by the given {@link Supplier}.<br>
     * <br>
     * If the given supplier provides an ID of a {@link Node} that is
     * not contained in the glTF, or of a node that does not have a
     * {@link Node#getCamera() camera}, then a warning will be printed
     * and the supplier will return the identity matrix.
     * <br> 
     * The matrix will be provided as a float array with 16 elements, 
     * storing the matrix entries in column-major order.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array.<br>
     * <br>
     * Note: If the {@link Camera#getType()} is neither 
     * <code>"perspective"</code> nor <code>"orthographic"</code>,
     * then the supplier will print an error message and return
     * the identity matrix.
     * 
     * @param cameraNodeIdSupplier The supplier of the {@link Node} ID
     * of the node that contains the {@link Camera} ID
     * @return The supplier
     */
    public Supplier<float[]> createProjectionMatrixSupplier(
        Supplier<String> cameraNodeIdSupplier)
    {
        float result[] = new float[16];
        return () ->
        {
            String cameraNodeId = cameraNodeIdSupplier.get();
            Node node = getExpected(
                gltf.getNodes(), cameraNodeId, "camera node");
            if (node == null)
            {
                MathUtils.setIdentity4x4(result);  
                return result;
            }
            String cameraId = node.getCamera();
            Camera camera = getExpected(
                gltf.getCameras(), cameraId, "camera");
            if (camera == null)
            {
                MathUtils.setIdentity4x4(result);  
                return result;
            }
            computeProjectionMatrix(camera, aspectRatioSupplier, result);
            return result;
        };
    }
    
    /**
     * Compute the projection matrix for the given {@link Camera}, and write
     * it into the given result array, which is a float array with 16 
     * elements, storing the matrix entries in column-major order.<br>
     * <br>
     * If the {@link Camera#getType()} is neither <code>"perspective"</code> 
     * nor <code>"orthographic"</code>, then this method will print an error 
     * message and set the given matrix to identity.
     * 
     * @param camera The {@link Camera}
     * @param aspectRatioSupplier An optional supplier for the aspect ratio
     * to use. If this is <code>null</code>, then the aspect ratio of the
     * camera will be used.
     * @param result The array storing the result
     */
    private static void computeProjectionMatrix(
        Camera camera, DoubleSupplier aspectRatioSupplier, float result[])
    {
        String cameraType = camera.getType();
        if ("perspective".equals(cameraType))
        {
            CameraPerspective cameraPerspective = camera.getPerspective();
            float fovRad = cameraPerspective.getYfov();
            float fovDeg = (float)Math.toDegrees(fovRad);
            Float aspect = 1.0f;
            if (aspectRatioSupplier != null)
            {
                aspect = (float)aspectRatioSupplier.getAsDouble();
            }
            else if (cameraPerspective.getAspectRatio() != null)
            {
                aspect = cameraPerspective.getAspectRatio();
            }
            float zNear = cameraPerspective.getZnear();
            float zFar = cameraPerspective.getZfar();
            MathUtils.perspective4x4(fovDeg, aspect, zNear, zFar, result);
        }
        else if ("orthographic".equals(cameraType))
        {
            CameraOrthographic cameraOrthographic = 
                camera.getOrthographic();
            float xMag = cameraOrthographic.getXmag();
            float yMag = cameraOrthographic.getYmag();
            float zNear = cameraOrthographic.getZnear();
            float zFar = cameraOrthographic.getZfar();
            MathUtils.setIdentity4x4(result);
            result[0] = xMag;
            result[5] = yMag;
            result[10] = -2.0f / (zFar - zNear);
        }
        else
        {
            logger.severe("Invalid camera type: "+cameraType);
            MathUtils.setIdentity4x4(result);
        }
    }

    

    /**
     * Creates a supplier for the global transform matrix of the 
     * {@link Node} with the given ID.<br>
     * <br> 
     * The matrix will be provided as a float array with 16 elements, 
     * storing the matrix entries in column-major order.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     * 
     * @param nodeId The {@link Node} ID 
     * @return The supplier
     */
    public Supplier<float[]> createNodeGlobalTransformSupplier(String nodeId)
    {
        float globalTransform[] = new float[16];
        float tempLocalTransform[] = new float[16];
        return () ->
        {
            computeGlobalTransform(nodeId, tempLocalTransform, globalTransform);
            return globalTransform;
        };
    }
    
    /**
     * Compute the global transform of the {@link Node} with the given ID,
     * and store it in the given result array, as a 4x4 matrix in column-major
     * order.<br>
     * <br>
     * If one of the required nodes can not be found in the glTF, then
     * a warning will be printed, and the identity matrix will be 
     * assumed for the respective node.
     * <br>
     * If the given array is <code>null</code> or does not have a length 
     * of 16, then a new array will be created and returned
     * 
     * @param nodeId The {@link Node} ID
     * @param result The array that will store the result
     * @return The result array
     */
    public float[] computeGlobalTransform(String nodeId, float result[])
    {
        float localResult[] = result;
        if (localResult == null || localResult.length != 16)
        {
            localResult = new float[16];
        }
        float tempLocalTransform[] = new float[16];
        computeGlobalTransform(nodeId, tempLocalTransform, localResult);
        return localResult;
    }

    /**
     * Compute the global transform of the {@link Node} with the given ID,
     * and store the result in the given <code>globalTransform</code> array,
     * in column-major order.<br>
     * <br>
     * If one of the required nodes can not be found in the glTF, then
     * a warning will be printed, and the identity matrix will be 
     * assumed for the respective node.
     * 
     * @param nodeId The {@link Node} ID
     * @param tempLocalTransform A 16-element array for temporary storage
     * @param globalTransform The array that will store the result
     */
    private void computeGlobalTransform(
        String nodeId, float tempLocalTransform[], float globalTransform[])
    {
        String currentNodeId = nodeId;
        MathUtils.setIdentity4x4(globalTransform);
        while (currentNodeId != null)
        {
            Node currentNode = getExpected(
                gltf.getNodes(), currentNodeId, "node");
            if (currentNode != null)
            {
                Nodes.computeLocalTransform(currentNode, tempLocalTransform);
                MathUtils.mul4x4(
                    tempLocalTransform, globalTransform, globalTransform);
            }
            currentNodeId = nodeIdToParentNodeId.get(currentNodeId);
        }
        MathUtils.mul4x4(rootTransform, globalTransform, globalTransform);
    }
    
    
    
    /**
     * Creates a supplier for the local transform matrix of the 
     * given {@link Node}.<br>
     * <br> 
     * The matrix will be provided as a float array with 16 elements, 
     * storing the matrix entries in column-major order.<br>
     * <br>
     * If the glTF does not contain the specified {@link Node}, then
     * a warning will be printed and the resulting supplier will 
     * return the identity matrix.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     * 
     * @param node The {@link Node}
     * @return The supplier
     */
    public static Supplier<float[]> createNodeLocalTransformSupplier(Node node)
    {
        float localTransform[] = new float[16];
        MathUtils.setIdentity4x4(localTransform);
        if (node == null)
        {
            return () -> localTransform;
        }
        return () ->
        {
            Nodes.computeLocalTransform(node, localTransform);
            return localTransform;
        };
    }
    

    
    
    /**
     * Obtains the value for the given ID from the given map. If the given 
     * ID is <code>null</code>, or the map is <code>null</code>, or there 
     * is no non-<code>null</code> value found for the given ID, then a 
     * warning will be printed, and <code>null</code> will be returned.
     * 
     * @param map The map
     * @param id The ID
     * @param description A description of what was looked up in the map.
     * This will be part of the possible log message
     * @return The value that was found in the map
     */
    static <T> T getExpected(Map<String, T> map, String id, String description)
    {
        if (id == null)
        {
            logger.warning("The ID of " + description + " is null");
            return null;
        }
        if (map == null)
        {
            logger.warning( 
                "No map for looking up " + description + " with ID " + id);
            return null;
        }
        T result = map.get(id);
        if (result == null)
        {
            logger.warning( 
                "The " + description + " with ID " + id + " does not exist");
        }
        return result;
    }
    
    
}
