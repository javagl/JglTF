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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Camera;
import de.javagl.jgltf.impl.CameraOrthographic;
import de.javagl.jgltf.impl.CameraPerspective;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Node;
import de.javagl.jgltf.impl.Scene;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;

/**
 * A class that serves as a data model for a {@link GlTF} and allows obtaining
 * suppliers for the data elements that are described in the {@link GlTF}.<br>
 * <br>
 * Unless otherwise noted, none of the parameters for the methods of this
 * class may be <code>null</code>.
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
     * A mapping from {@link Node} IDs to their parent nodes
     */
    private final Map<String, String> nodeIdToParentNodeId;
    
    /**
     * The root transform of the {@link GlTF}, as a column-major 4x4 matrix
     */
    private final float rootTransform[];
    
    /**
     * A supplier that supplies the viewport, as 4 float elements, 
     * [x, y, width, height]
     */
    private final Supplier<float[]> viewportSupplier;
    
    /**
     * A supplier for the aspect ratio. This may be used to override the
     * aspect ratio that is provided by the camera, using an aspect ratio
     * that depends, for example, on the rendering window size. If this is 
     * <code>null</code>, then the aspect ratio of the camera will be used
     */
    private final DoubleSupplier aspectRatioSupplier;
    
    /**
     * Creates a new model for the given {@link GlTF}
     * 
     * @param gltf The {@link GlTF}
     * @param rootTransform The root transform of the {@link GlTF}, as a 
     * column-major 4x4 matrix. If this is <code>null</code>, then the
     * identity matrix will be used.
     * @param viewportSupplier A supplier that supplies the viewport, 
     * as 4 float elements, [x, y, width, height]
     * @param aspectRatioSupplier An optional supplier for the aspect ratio. 
     * If this is <code>null</code>, then the aspect ratio of the 
     * camera will be used
     */
    public GltfModel(GlTF gltf, 
        float rootTransform[],
        Supplier<float[]> viewportSupplier,
        DoubleSupplier aspectRatioSupplier)
    {
        Objects.requireNonNull(gltf, "The gltf may not be null");
        Objects.requireNonNull(viewportSupplier, 
            "The viewportSupplier may not be null");
        this.gltf = gltf;
        this.rootTransform = rootTransform != null ? 
            rootTransform : MathUtils.createIdentity4x4();
        this.viewportSupplier = viewportSupplier;
        this.aspectRatioSupplier = aspectRatioSupplier;
        
        this.nodeIdToParentNodeId = computeNodeIdToParentNodeIdMap();
    }
    
    /**
     * Compute the mapping from {@link Node} IDs to their parent node IDs
     * 
     * @return The mapping
     */
    private Map<String, String> computeNodeIdToParentNodeIdMap()
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Map<String, Scene> scenes = gltf.getScenes();
        if (scenes != null)
        {
            for (Scene scene : scenes.values())
            {
                Set<String> nodes = scene.getNodes();
                if (nodes != null)
                {
                    for (String nodeId : nodes)
                    {
                        computeNodeIdToParentNodeIdMap(nodeId, map);
                    }
                }
            }
        }
        return map;
    }
    
    /**
     * Recursively compute the mapping from {@link Node} IDs to their parent 
     * node IDs, starting at the given node
     *  
     * @param nodeId The current node
     * @param map The map that will store the result
     */
    private void computeNodeIdToParentNodeIdMap(
        String nodeId, Map<String, String> map)
    {
        Map<String, Node> nodes = gltf.getNodes();
        if (nodes != null)
        {
            Node node = nodes.get(nodeId);
            if (node.getChildren() != null)
            {
                for (String childNodeId : node.getChildren())
                {
                    map.put(childNodeId, nodeId);
                    computeNodeIdToParentNodeIdMap(childNodeId, map);
                }
            }
        }
    }
    
    
    /**
     * Creates a supplier for the value of the uniform with the given name,
     * based on the {@link TechniqueParameters#getSemantic() semantic of
     * the TechniqueParameters} that the uniform has in the given 
     * {@link Technique}.<br>
     * <br>
     * Note: This method assumes that the semantic is not <code>null</code>.
     * If it is <code>null</code>, then an <code>IllegalArgumentException</code>
     * will be thrown.<br> 
     * <br>
     * The <code>currentNodeId</code> acts as a source for local and global 
     * transforms of the node that is being rendered, unless a node is
     * referenced via the {@link TechniqueParameters#getNode()} ID.
     * (See the section about "Semantics" in the glTF specification).<br>
     * <br>
     * The supported semantics and the types that are supplied by the 
     * returned suppliers are 
     * <pre><code>
     *  LOCAL                       float[16]
     *  MODEL                       float[16]
     *  VIEW                        float[16]
     *  PROJECTION                  float[16]
     *  MODELVIEW                   float[16]
     *  MODELVIEWPROJECTION         float[16]
     *  MODELINVERSE                float[16]
     *  VIEWINVERSE                 float[16]
     *  PROJECTIONINVERSE           float[16]
     *  MODELVIEWINVERSE            float[16]
     *  MODELVIEWPROJECTIONINVERSE  float[16]
     *  MODELINVERSETRANSPOSE       float[9]
     *  MODELVIEWINVERSETRANSPOSE   float[9]
     *  VIEWPORT                    float[4]    
     * </code></pre>
     * (where all matrices will be in column-major order). If the semantic 
     * does not have any of these values, then a warning will be printed 
     * and <code>null</code> will be returned.<br>
     * <br>
     * The returned suppliers MAY always return the same array instance.
     * So callers MUST NOT store or modify the returned arrays.
     * 
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param currentNodeId The current {@link Node} ID
     * @param viewMatrixSupplier The supplier for view matrix 
     * @param projectionMatrixSupplier The supplier for projection matrix 
     * @return The supplier
     * @throws IllegalArgumentException If the semantic of the
     * {@link TechniqueParameters} for the given uniform name
     * in the given {@link Technique} is is <code>null</code> 
     */
    public Supplier<?> createSemanticBasedSupplier(
        String uniformName, Technique technique, String currentNodeId, 
        Supplier<float[]> viewMatrixSupplier,
        Supplier<float[]> projectionMatrixSupplier)
    {
        TechniqueParameters techniqueParameters =
            Techniques.getUniformTechniqueParameters(technique, uniformName);
        String semantic = techniqueParameters.getSemantic();
        if (semantic == null)
        {
            throw new IllegalArgumentException(
                "Uniform "+uniformName+" has no semantic " + 
                "in technique "+technique);
        }
        
        String accessedNodeId = currentNodeId;
        String parameterNodeId = techniqueParameters.getNode();
        if (parameterNodeId != null)
        {
            accessedNodeId = parameterNodeId;
        }
        
        switch (semantic)
        {
            case "LOCAL":
            {
                return createNodeLocalTransformSupplier(accessedNodeId);
            }
            
            case "MODEL":
            {
                return createNodeGlobalTransformSupplier(accessedNodeId);
            }
            
            case "VIEW":
            {
                return viewMatrixSupplier;
            }

            case "PROJECTION":
            {
                return projectionMatrixSupplier;
            }
            
            case "MODELVIEW":
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .log("MODELVIEW", Level.FINE)
                    .build();
            }
            
            case "MODELVIEWPROJECTION":
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(projectionMatrixSupplier)
                    .multiply4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .log("MODELVIEWPROJECTION", Level.FINE)
                    .build();
            }
            
            case "MODELINVERSE":
            {
                Supplier<float[]> model = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(model)
                    .invert4x4()
                    .log("MODELINVERSE", Level.FINE)
                    .build();
            }
            
            case "VIEWINVERSE":
            {
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .invert4x4()
                    .log("VIEWINVERSE", Level.FINE)
                    .build();
            }

            case "PROJECTIONINVERSE":
            {
                return MatrixOps
                    .create4x4(projectionMatrixSupplier)
                    .invert4x4()
                    .log("PROJECTIONINVERSE", Level.FINE)
                    .build();
            }
            
            case "MODELVIEWPROJECTIONINVERSE":
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(projectionMatrixSupplier)
                    .multiply4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .invert4x4()
                    .log("MODELVIEWPROJECTIONINVERSE", Level.FINE)
                    .build();
            }

            case "MODELINVERSETRANSPOSE":
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(modelMatrixSupplier)
                    .invert4x4()
                    .transpose4x4()
                    .getRotationScale()
                    .log("MODELINVERSETRANSPOSE", Level.FINE)
                    .build();
            }
            
            case "MODELVIEWINVERSETRANSPOSE":
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .invert4x4()
                    .transpose4x4()
                    .getRotationScale()
                    .log("MODELVIEWINVERSETRANSPOSE", Level.FINE)
                    .build();
            }
            
            case "VIEWPORT":
            {
                return viewportSupplier;
            }
            default:
                break;
            
        }
        logger.severe("Unsupported semantic: "+semantic);
        return null;
    }

    /**
     * Create a supplier for the view matrix of the camera that is attached
     * to the {@link Node} whose ID is provided by the given supplier. This 
     * will be the inverse of the global transform of the node.<br>
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
     * The matrix will be provided as a float array with 16 elements, 
     * storing the matrix entries in column-major order.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     * 
     * @param cameraNodeIdSupplier The supplier of the {@link Node} ID
     * of the node that contains the {@link Camera} ID
     * @return The supplier
     */
    public Supplier<float[]> createProjectionMatrixSupplier(
        Supplier<String> cameraNodeIdSupplier)
    {
        Map<String, Camera> cameras = gltf.getCameras();
        Map<String, Node> nodes = gltf.getNodes();
        float result[] = new float[16];
        return () ->
        {
            String cameraNodeId = cameraNodeIdSupplier.get();
            Node node = nodes.get(cameraNodeId);
            String cameraId = node.getCamera();
            Camera camera = cameras.get(cameraId);
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
            return result;
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
    private Supplier<float[]> createNodeGlobalTransformSupplier(String nodeId)
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
     * in column-major order.
     * 
     * @param nodeId The {@link Node} ID
     * @param tempLocalTransform A 16-element array for temporary storage
     * @param globalTransform The array that will store the result
     */
    private void computeGlobalTransform(
        String nodeId, float tempLocalTransform[], float globalTransform[])
    {
        Map<String, Node> nodes = gltf.getNodes();
        String currentNodeId = nodeId;
        MathUtils.setIdentity4x4(globalTransform);
        while (currentNodeId != null)
        {
            Node currentNode = nodes.get(currentNodeId);
            Nodes.computeLocalTransform(currentNode, tempLocalTransform);
            MathUtils.mul4x4(
                tempLocalTransform, globalTransform, globalTransform);
            currentNodeId = nodeIdToParentNodeId.get(currentNodeId);
        }
        MathUtils.mul4x4(rootTransform, globalTransform, globalTransform);
    }
    
    
    /**
     * Creates a supplier for the local transform matrix of the 
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
    private Supplier<float[]> createNodeLocalTransformSupplier(String nodeId)
    {
        Map<String, Node> nodes = gltf.getNodes();
        Node node = nodes.get(nodeId);
        float localTransform[] = new float[16];
        return () ->
        {
            Nodes.computeLocalTransform(node, localTransform);
            return localTransform;
        };
    }
    
}
