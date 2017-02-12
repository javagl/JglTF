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
import java.util.Objects;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Camera;
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
     * The {@link GlTF} of this model
     */
    private final GlTF gltf;

    /**
     * The mapping from {@link Node} IDs to {@link NodeModel} instances
     */
    private final Map<String, NodeModel> nodeIdToNodeModel;
    
    /**
     * The mapping from {@link Node} IDs (IMPORTANT: NOT from camera IDs!) to 
     * {@link CameraModel} instances that have been created from
     * the {@link Camera} reference of the respective {@link Node}
     */
    private final Map<String, CameraModel> nodeIdToCameraModel;
    
    /**
     * Creates a new model for the given glTF
     * 
     * @param gltf The {@link GlTF}
     */
    public GltfModel(GlTF gltf)
    {
        Objects.requireNonNull(gltf, 
            "The gltf may not be null");

        this.gltf = gltf;
        this.nodeIdToNodeModel = createNodeIdToNodeModel();
        this.nodeIdToCameraModel = createNodeIdToCameraModel();
    }

    /**
     * Compute the mapping from {@link Node} IDs to {@link NodeModel} instances
     * 
     * @return The mapping
     */
    private Map<String, NodeModel> createNodeIdToNodeModel()
    {
        Map<String, NodeModel> map = new LinkedHashMap<String, NodeModel>();
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        for (String nodeId : nodes.keySet())
        {
            Node node = nodes.get(nodeId);
            NodeModel nodeModel = new NodeModel(node);
            map.put(nodeId, nodeModel);
        }
        for (String nodeId : map.keySet())
        {
            NodeModel node = map.get(nodeId);
            List<String> childIds = Optionals.of(node.getNode().getChildren());
            for (String childId : childIds)
            {
                NodeModel child = map.get(childId);
                if (child == null)
                {
                    logger.severe("Node with ID " + childId + " not found");
                }
                else
                {
                    node.addChild(child);
                }
            }
        }
        return map;
    }
    
    /**
     * Create the mapping from {@link Node} IDs to {@link CameraModel} 
     * instances, based on the {@link Node} objects of the glTF that 
     * refer to a {@link Camera}.
     * 
     * @return The {@link CameraModel} instances
     */
    private Map<String, CameraModel> createNodeIdToCameraModel()
    {
        Map<String, CameraModel> map = new LinkedHashMap<String, CameraModel>();
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        Map<String, Camera> cameras = Optionals.of(gltf.getCameras());
        for (String nodeId : nodes.keySet())
        {
            Node node = nodes.get(nodeId);
            String cameraId = node.getCamera();
            if (cameraId != null)
            {
                Camera camera = cameras.get(cameraId);
                if (camera == null)
                {
                    logger.severe("Camera with ID " + cameraId + " not found");
                }
                else
                {
                    NodeModel nodeModel = nodeIdToNodeModel.get(nodeId);
                    CameraModel cameraModel = 
                        new CameraModel(camera, nodeModel);
                    map.put(nodeId, cameraModel);
                }
            }
        }
        return map;
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
        float viewMatrix[] = new float[16];
        return () ->
        {
            String nodeId = cameraNodeIdSupplier.get();
            CameraModel cameraModel = nodeIdToCameraModel.get(nodeId);
            if (cameraModel == null)
            {
                logger.warning("No camera model found for node " + nodeId);
                MathUtils.setIdentity4x4(viewMatrix);
                return viewMatrix;
            }
            cameraModel.computeViewMatrix(viewMatrix);
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
     * @param aspectRatioSupplier The optional supplier for the aspect
     * ratio of the camera. If this is <code>null</code>, then the
     * {@link CameraPerspective#getAspectRatio() aspect ratio of the camera}
     * will be used.
     * @return The supplier
     */
    public Supplier<float[]> createProjectionMatrixSupplier(
        Supplier<String> cameraNodeIdSupplier,
        DoubleSupplier aspectRatioSupplier)
    {
        float projectionMatrix[] = new float[16];
        return () -> 
        {
            String nodeId = cameraNodeIdSupplier.get();
            CameraModel cameraModel = nodeIdToCameraModel.get(nodeId);
            if (cameraModel == null)
            {
                logger.warning("No camera model found for node " + nodeId);
                MathUtils.setIdentity4x4(projectionMatrix);
                return projectionMatrix;
            }
            Float aspectRatio = null;
            if (aspectRatioSupplier != null)
            {
                double a = aspectRatioSupplier.getAsDouble();
                aspectRatio = (float)a;
            }
            cameraModel.computeProjectionMatrix(projectionMatrix, aspectRatio);
            return projectionMatrix;
        };
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
        NodeModel node = nodeIdToNodeModel.get(nodeId);
        if (node == null)
        {
            return createIdentityTransformSupplier();
        }
        float globalTransform[] = new float[16];
        return () ->
        {
            return node.computeGlobalTransform(globalTransform);
        };
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
        if (node == null)
        {
            return createIdentityTransformSupplier();
        }
        float localTransform[] = new float[16];
        return () ->
        {
            Nodes.computeLocalTransform(node, localTransform);
            return localTransform;
        };
    }

    /**
     * Creates a supplier that returns the 4x4 identity matrix.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     *  
     * @return The supplier
     */
    private static Supplier<float[]> createIdentityTransformSupplier()
    {
        float matrix[] = new float[16];
        return () -> 
        {
            MathUtils.setIdentity4x4(matrix);
            return matrix;
        };
    }
    

    
    
    
    
}
