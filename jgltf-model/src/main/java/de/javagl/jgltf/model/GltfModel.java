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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Camera;
import de.javagl.jgltf.impl.CameraOrthographic;
import de.javagl.jgltf.impl.CameraPerspective;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Node;
import de.javagl.jgltf.impl.Scene;
import de.javagl.jgltf.impl.Skin;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;

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
     * Creates a new model for the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @param rootTransform The root transform of the {@link GlTF}, as a 
     * column-major 4x4 matrix. If this is <code>null</code>, then the
     * identity matrix will be used.
     * @param viewportSupplier A supplier that supplies the viewport, 
     * as 4 float elements, [x, y, width, height]
     * @param aspectRatioSupplier An optional supplier for the aspect ratio. 
     * If this is <code>null</code>, then the aspect ratio of the 
     * camera will be used
     */
    public GltfModel(GltfData gltfData, 
        float rootTransform[],
        Supplier<float[]> viewportSupplier,
        DoubleSupplier aspectRatioSupplier)
    {
        Objects.requireNonNull(gltfData, 
            "The gltfData may not be null");
        Objects.requireNonNull(viewportSupplier, 
            "The viewportSupplier may not be null");

        this.gltfData = gltfData;
        this.gltf = gltfData.getGltf();
        this.rootTransform = rootTransform != null ? 
            rootTransform : MathUtils.createIdentity4x4();
        this.viewportSupplier = viewportSupplier;
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
        Map<String, Scene> scenes = gltf.getScenes();
        if (scenes != null)
        {
            for (Scene scene : scenes.values())
            {
                List<String> nodes = scene.getNodes();
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
            if (node == null)
            {
                logger.severe("No node found for ID "+nodeId);
            }
            else
            {
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
        Map<String, Node> nodes = gltf.getNodes();
        if (nodes != null)
        {
            for (Entry<String, Node> entry : nodes.entrySet())
            {
                String nodeId = entry.getKey();
                Node node = entry.getValue();
                if (node.getJointName() != null)
                {
                    map.put(node.getJointName(), nodeId);
                }
            }
        }
        return map;
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
     *  JOINTMATRIX                 float[16*numJoints] (see notes below)    
     * </code></pre>
     * All matrices will be in column-major order. If the semantic does 
     * not have any of these values, then a warning will be printed 
     * and <code>null</code> will be returned.<br>
     * <br>
     * The returned suppliers MAY always return the same array instance.
     * So callers MUST NOT store or modify the returned arrays.<br>
     * <br>
     * About the <code>numJoints</code> factor in the <code>JOINTMATRIX</code>
     * case: The supplier will provide <code>numJoints</code> 4x4 matrices
     * in a single array of size <code>16*numJoints</code>. The number of
     * joints is determined as follows:
     * <ul>
     *   <li>
     *     The {@link Node} for the given <code>currentNodeId</code> is 
     *     looked up. This node is assumed to have a 
     *     {@link Node#getSkin() skin ID}
     *   </li>
     *   <li>
     *     The {@link Skin} for this skin ID is looked up. The skin is 
     *     assumed to have several {@link Skin#getJointNames() joint names}. 
     *     The number of joint names in this array is used as
     *     <code>numJoints</code>.
     *   </li>
     * </ul>
     * The actual contents of these joint matrices is computed from the
     * {@link Skin#getBindShapeMatrix() bind shape matrix}, the
     * {@link Skin#getInverseBindMatrices() inverse bind matrices} and the
     * {@link #computeGlobalTransform(String, float[]) global transform}
     * of the given node and the joint node. See the glTF specification
     * for details.<br>
     * <br>
     * 
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param currentNodeId The current {@link Node} ID
     * @param viewMatrixSupplier The supplier for view matrix 
     * @param projectionMatrixSupplier The supplier for projection matrix 
     * @return The supplier, or <code>null</code> if the semantic did
     * not have any of the valid values mentioned above.
     * @throws IllegalArgumentException If the semantic of the
     * {@link TechniqueParameters} for the given uniform name
     * in the given {@link Technique} is is <code>null</code>
     * or not a valid {@link Semantic}
     * @throws GltfException May be thrown if the computations involve an ID 
     * of an element that is not found in the {@link GlTF}.
     */
    public Supplier<?> createSemanticBasedSupplier(
        String uniformName, Technique technique, String currentNodeId, 
        Supplier<float[]> viewMatrixSupplier,
        Supplier<float[]> projectionMatrixSupplier)
    {
        Objects.requireNonNull(uniformName, 
            "The uniformName may not be null");
        Objects.requireNonNull(technique, 
            "The technique may not be null");
        Objects.requireNonNull(currentNodeId, 
            "The currentNodeId may not be null");
        Objects.requireNonNull(viewMatrixSupplier, 
            "The viewMatrixSupplier may not be null");
        Objects.requireNonNull(projectionMatrixSupplier, 
            "The projectionMatrixSupplier may not be null");
        
        TechniqueParameters techniqueParameters =
            Techniques.getUniformTechniqueParameters(technique, uniformName);
        String semanticString = techniqueParameters.getSemantic();
        if (!Semantic.contains(semanticString))
        {
            throw new IllegalArgumentException(
                "Uniform " + uniformName + " has invalid semantic " + 
                semanticString + " in technique " + technique);
        }
        Semantic semantic = Semantic.valueOf(semanticString);
        
        String accessedNodeId = currentNodeId;
        String parameterNodeId = techniqueParameters.getNode();
        if (parameterNodeId != null)
        {
            getChecked(gltf.getNodes(), parameterNodeId, 
                "technique parameter node");
            accessedNodeId = parameterNodeId;
        }
        else
        {
            getChecked(gltf.getNodes(), currentNodeId, "current node");
        }
        
        
        switch (semantic)
        {
            case LOCAL:
            {
                return createNodeLocalTransformSupplier(accessedNodeId);
            }
            
            case MODEL:
            {
                return createNodeGlobalTransformSupplier(accessedNodeId);
            }
            
            case VIEW:
            {
                return viewMatrixSupplier;
            }

            case PROJECTION:
            {
                return projectionMatrixSupplier;
            }
            
            case MODELVIEW:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case MODELVIEWPROJECTION:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(projectionMatrixSupplier)
                    .multiply4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case MODELINVERSE:
            {
                Supplier<float[]> model = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(model)
                    .invert4x4()
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case VIEWINVERSE:
            {
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .invert4x4()
                    .log(semanticString, Level.FINE)
                    .build();
            }

            case MODELVIEWINVERSE:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .invert4x4()
                    .log(semanticString, Level.FINE)
                    .build();
            }

            case PROJECTIONINVERSE:
            {
                return MatrixOps
                    .create4x4(projectionMatrixSupplier)
                    .invert4x4()
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case MODELVIEWPROJECTIONINVERSE:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(projectionMatrixSupplier)
                    .multiply4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .invert4x4()
                    .log(semanticString, Level.FINE)
                    .build();
            }

            case MODELINVERSETRANSPOSE:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(modelMatrixSupplier)
                    .invert4x4()
                    .transpose4x4()
                    .getRotationScale()
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case MODELVIEWINVERSETRANSPOSE:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    createNodeGlobalTransformSupplier(accessedNodeId);
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .invert4x4()
                    .transpose4x4()
                    .getRotationScale()
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case VIEWPORT:
            {
                return viewportSupplier;
            }
            
            case JOINTMATRIX:
            {
                return createJointMatrixSupplier(currentNodeId);
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
            computeProjectionMatrix(camera, result);
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
     * @param result The array storing the result
     */
    private void computeProjectionMatrix(Camera camera, float result[])
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
     * Create a supplier for the joint matrix (actually, joint matrices) of
     * the {@link Node} with the given ID.
     * 
     * @param nodeId The {@link Node} ID
     * @return The supplier
     * @throws GltfException If the {@link Node} with the given ID does not
     * exist, or the {@link Skin} with the {@link Node#getSkin() node skin ID}
     * does not exist, or the {@link Accessor} with the 
     * {@link Skin#getInverseBindMatrices() inverse bind matrices accessor ID}
     * does not exist.
     * @throws GltfException If the number of {@link Skin#getJointNames() joint 
     * names} in the {@link Skin} does not match the number of elements in 
     * the {@link Accessor} with the {@link Skin#getInverseBindMatrices() 
     * inverse bind matrices accessor ID}
     */
    private Supplier<float[]> createJointMatrixSupplier(String nodeId)
    {
        Node node = getChecked(gltf.getNodes(), nodeId, "joint node");
        String skinId = node.getSkin();
        Skin skin = getChecked(gltf.getSkins(), skinId, "joint node skin");
        List<String> jointNames = skin.getJointNames();

        // Create the supplier for the bind shape matrix (or a supplier
        // of the identity matrix, if the bind shape matrix is null)
        float bindShapeMatrix[] = skin.getBindShapeMatrix() != null ?
            skin.getBindShapeMatrix() : MathUtils.createIdentity4x4();
        Supplier<float[]> bindShapeMatrixSupplier = 
            MatrixOps.create4x4(() -> bindShapeMatrix)
            .log("bindShapeMatrix", Level.FINE)
            .build();

        // Obtain the accessor data for the inverse bind matrices
        String inverseBindMatricesAccessorId = skin.getInverseBindMatrices();
        Accessor accessor = getChecked(gltf.getAccessors(), 
            inverseBindMatricesAccessorId, "inverse bind matrices accessor");
        AccessorFloatData inverseBindMatricesData = 
            AccessorDatas.createFloat(accessor, gltfData);

        if (inverseBindMatricesData.getNumElements() != jointNames.size())
        {
            throw new GltfException("There are " + jointNames.size() + 
                " joint names in skin with ID " + skinId + " in the node " +
                "with ID " + nodeId + ", but the accessor for the inverse " + 
                "bind matrices with ID " +inverseBindMatricesAccessorId + 
                " contains " + inverseBindMatricesData.getNumElements() + 
                " elements");
        }

        // Create one supplier for each inverse bind matrix. Each of them will 
        // extract one element of the inverse bind matrix accessor data and 
        // provide it as a single float[16] array, representing a 4x4 matrix
        List<Supplier<float[]>> inverseBindMatrixSuppliers =
            new ArrayList<Supplier<float[]>>();
        for (int i=0; i<jointNames.size(); i++)
        {
            final int currentJointIndex = i;
            float inverseBindMatrix[] = new float[16];
            Supplier<float[]> inverseBindMatrixSupplier = () ->
            {
                for (int j=0; j<16; j++)
                {
                    inverseBindMatrix[j] = inverseBindMatricesData.get(
                        currentJointIndex, j);
                }
                return inverseBindMatrix;
            };
            Supplier<float[]> loggingInverseBindMatrixSupplier =
                MatrixOps.create4x4(inverseBindMatrixSupplier)
                    .log("inverseBindMatrix "+jointNames.get(i), Level.FINE)
                    .build();
            inverseBindMatrixSuppliers.add(loggingInverseBindMatrixSupplier);
        }

        // Create the joint matrix suppliers. Each of them will provide a
        // matrix that is computed as
        // [jointMatrix(j)] = 
        //     [globalTransformOfNode^-1] *
        //     [globalTransformOfJointNode] *
        //     [inverseBindMatrix(j)] *
        //     [bindShapeMatrix]
        List<Supplier<float[]>> jointMatrixSuppliers = 
            new ArrayList<Supplier<float[]>>();
        for (int j=0; j<jointNames.size(); j++)
        {
            String jointName = jointNames.get(j);            
            String jointNodeId = jointNameToNodeId.get(jointName);
            
            Supplier<float[]> inverseBindMatrixSupplier = 
                inverseBindMatrixSuppliers.get(j);
            
            Supplier<float[]> jointMatrixSupplier = MatrixOps
                .create4x4(createNodeGlobalTransformSupplier(nodeId))
                .invert4x4()
                .multiply4x4(createNodeGlobalTransformSupplier(jointNodeId))
                .multiply4x4(inverseBindMatrixSupplier)
                .multiply4x4(bindShapeMatrixSupplier)
                .log("jointMatrix "+jointName, Level.FINE)
                .build();
            jointMatrixSuppliers.add(jointMatrixSupplier);
        }
        
        // Create a supplier for the joint matrices, which combines the
        // joint matrices of the individual joint matrix suppliers 
        // into one array
        float jointMatrices[] = new float[jointMatrixSuppliers.size() * 16];
        return () -> 
        {
            for (int i=0; i<jointMatrixSuppliers.size(); i++)
            {
                Supplier<float[]> jointMatrixSupplier = 
                    jointMatrixSuppliers.get(i);
                float[] jointMatrix = jointMatrixSupplier.get();
                System.arraycopy(jointMatrix, 0, jointMatrices, i * 16, 16);
            }
            return jointMatrices;
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
     * {@link Node} with the given ID.<br>
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
     * @param nodeId The {@link Node} ID
     * @return The supplier
     */
    private Supplier<float[]> createNodeLocalTransformSupplier(String nodeId)
    {
        float localTransform[] = new float[16];
        MathUtils.setIdentity4x4(localTransform);
        Node node = getExpected(gltf.getNodes(), nodeId, 
            "node for the local transform");
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
     * Obtains the value for the given ID from the given map, and throws
     * a <code>GltfException</code> with an appropriate error message
     * if the given map is <code>null</code>, or there is no 
     * non-<code>null</code> value found for the given ID.
     * 
     * @param map The map
     * @param id The ID
     * @param description A description of what was looked up in the map.
     * This will be part of the possible exception message
     * @return The value that was found in the map
     * @throws GltfException If there was no value found in the map
     */
    static <T> T getChecked(
        Map<String, T> map, String id, String description)
    {
        if (map == null)
        {
            throw new GltfException(
                "No map for looking up " + description + " with ID " + id);
        }
        T result = map.get(id);
        if (result == null)
        {
            throw new GltfException(
                "The " + description + " with ID " + id + " does not exist");
        }
        return result;
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
