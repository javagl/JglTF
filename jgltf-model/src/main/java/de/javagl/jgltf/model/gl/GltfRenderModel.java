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
package de.javagl.jgltf.model.gl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Node;
import de.javagl.jgltf.impl.v1.Skin;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueParameters;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MathUtils;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.Utils;

/**
 * A class that serves as a data model for a glTF asset that will be
 * rendered with GL, using the technique-based concepts of glTF 1.0.
 */
public class GltfRenderModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfRenderModel.class.getName());
    
    /**
     * The {@link GltfModel}
     */
    private final GltfModel gltfModel;
    
    /**
     * The {@link GltfData}
     */
    private final GltfData gltfData;
    
    /**
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * A supplier that supplies the viewport, as 4 float elements, 
     * [x, y, width, height]
     */
    private final Supplier<float[]> viewportSupplier;
    
    /**
     * The mapping from joint names to the ID of the {@link Node} with the 
     * respective {@link Node#getJointName() joint name}
     */
    private final Map<String, String> jointNameToNodeId;
    
    /**
     * Creates a new render model
     * 
     * @param gltfModel The {@link GltfModel}
     * @param gltfData The {@link GltfData}
     * @param viewportSupplier A supplier that supplies the viewport, 
     * as 4 float elements, [x, y, width, height]
     */
    public GltfRenderModel(
        GltfModel gltfModel, GltfData gltfData, 
        Supplier<float[]> viewportSupplier)
    {
        this.gltfModel = gltfModel;
        this.gltfData = gltfData;
        this.gltf = gltfData.getGltf();
        this.viewportSupplier = Objects.requireNonNull(viewportSupplier, 
            "The viewportSupplier may not be null");
        
        this.jointNameToNodeId = computeJointNameToNodeIdMap();
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
     * global transform of the given node and the joint node. See the glTF 
     * specification for details.<br>
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
            Utils.getChecked(gltf.getNodes(), parameterNodeId, 
                "technique parameter node");
            accessedNodeId = parameterNodeId;
        }
        else
        {
            Utils.getChecked(gltf.getNodes(), currentNodeId, "current node");
        }
        
        // TODO: This will be replaced by an index after the update to glTF 2.0
        NodeModel nodeModel = gltfModel.getNodeModelsMap().get(accessedNodeId);
        
        switch (semantic)
        {
            case LOCAL:
            {
                return nodeModel.createLocalTransformSupplier();
            }
            
            case MODEL:
            {
                return nodeModel.createGlobalTransformSupplier();
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
                    nodeModel.createGlobalTransformSupplier();
                return MatrixOps
                    .create4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case MODELVIEWPROJECTION:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    nodeModel.createGlobalTransformSupplier();
                return MatrixOps
                    .create4x4(projectionMatrixSupplier)
                    .multiply4x4(viewMatrixSupplier)
                    .multiply4x4(modelMatrixSupplier)
                    .log(semanticString, Level.FINE)
                    .build();
            }
            
            case MODELINVERSE:
            {
                Supplier<float[]> modelMatrixSupplier = 
                    nodeModel.createGlobalTransformSupplier();
                return MatrixOps
                    .create4x4(modelMatrixSupplier)
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
                    nodeModel.createGlobalTransformSupplier();
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
                    nodeModel.createGlobalTransformSupplier();
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
                    nodeModel.createGlobalTransformSupplier();
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
                    nodeModel.createGlobalTransformSupplier();
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
        Node node = Utils.getChecked(
            gltf.getNodes(), nodeId, "joint node");
        
        // TODO: This will be replaced by an index after the update to glTF 2.0
        NodeModel nodeModel = gltfModel.getNodeModelsMap().get(nodeId);
        
        String skinId = node.getSkin();
        Skin skin = Utils.getChecked(
            gltf.getSkins(), skinId, "joint node skin");
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
        Accessor accessor = Utils.getChecked(gltf.getAccessors(), 
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
            
            // TODO: This will be replaced by an index after the update to glTF 2.0
            NodeModel jointNodeModel = 
                gltfModel.getNodeModelsMap().get(jointNodeId);
            
            Supplier<float[]> inverseBindMatrixSupplier = 
                inverseBindMatrixSuppliers.get(j);
            
            Supplier<float[]> jointMatrixSupplier = MatrixOps
                .create4x4(nodeModel.createGlobalTransformSupplier())
                .invert4x4()
                .multiply4x4(jointNodeModel.createGlobalTransformSupplier())
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
    
    
}


