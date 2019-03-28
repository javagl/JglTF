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
package de.javagl.jgltf.viewer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.gl.Semantic;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.gl.TechniqueParametersModel;

/**
 * A class that provides a suppliers for the values of uniform variables, 
 * based on a {@link RenderedMaterial}.
 */
class UniformGetterFactory
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(UniformGetterFactory.class.getName());
    
    /**
     * A supplier for the view matrix.  
     */
    private final Supplier<float[]> viewMatrixSupplier;
    
    /**
     * A supplier for the projection matrix.
     */
    private final Supplier<float[]> projectionMatrixSupplier;
    
    /**
     * A supplier that supplies the viewport, as 4 float elements, 
     * [x, y, width, height]
     */
    private final Supplier<float[]> viewportSupplier;
    
    /**
     * The set of uniform names for which a <code>null</code> value 
     * was already reported. This is mainly intended for debugging.
     */
    private final Set<String> reportedNullUniformNames;
    
    /**
     * Creates a new instance
     * 
     * @param viewportSupplier A supplier that supplies the viewport, 
     * as 4 float elements, [x, y, width, height]
     * @param viewMatrixSupplier A supplier that supplies the view matrix,
     * which is a 4x4 matrix, given as a float array, in column-major order
     * @param projectionMatrixSupplier A supplier that supplies the projection
     * matrix, which is a 4x4 matrix, given as a float array, in 
     * column-major order
     */
    public UniformGetterFactory(
        Supplier<float[]> viewportSupplier,
        Supplier<float[]> viewMatrixSupplier,
        Supplier<float[]> projectionMatrixSupplier)
    {
        this.viewportSupplier = Objects.requireNonNull(viewportSupplier, 
            "The viewportSupplier may not be null");
        this.viewMatrixSupplier = Objects.requireNonNull(viewMatrixSupplier, 
            "The viewMatrixSupplier may not be null");
        this.projectionMatrixSupplier = 
            Objects.requireNonNull(projectionMatrixSupplier, 
                "The projectionMatrixSupplier may not be null");
        this.reportedNullUniformNames = new LinkedHashSet<String>();
    }
    
    /**
     * Create a supplier that supplies the value for the specified uniform.
     * If there is no {@link TechniqueParametersModel#getSemantic() semantic} 
     * defined in the {@link TechniqueModel} of the given 
     * {@link RenderedMaterial}, then this value will be obtained from the 
     * {@link TechniqueModel} of the {@link RenderedMaterial}. Otherwise, the 
     * value will be derived from the context of the currently rendered node, 
     * which is given by the local and global transform of the 
     * given {@link NodeModel}  
     * 
     * @param uniformName The name of the uniform
     * @param renderedMaterial The {@link RenderedMaterial}
     * @param nodeModel The {@link NodeModel}
     * @return The supplier for the uniform value
     */
    public Supplier<?> createUniformValueSupplier(String uniformName, 
        RenderedMaterial renderedMaterial, NodeModel nodeModel)
    {
        TechniqueModel techniqueModel = renderedMaterial.getTechniqueModel();
        Map<String, String> uniforms = techniqueModel.getUniforms();
        String parameterName = uniforms.get(uniformName);
        Map<String, TechniqueParametersModel> parameters = 
            techniqueModel.getParameters();
        TechniqueParametersModel techniqueParametersModel =
            parameters.get(parameterName);
        
        String semantic = techniqueParametersModel.getSemantic();
        if (semantic == null)
        {
            Supplier<?> supplier = UniformGetters.createGenericSupplier(
                uniformName, renderedMaterial);
            return createNullLoggingSupplier(uniformName, supplier);
        }
        return createSemanticBasedSupplier(
            uniformName, techniqueModel, nodeModel);
    }
    
    /**
     * Create a supplier that wraps the given one, and prints a log message
     * once when the given supplier returns <code>null</code>. This is 
     * mainly intended for debugging.
     * 
     * @param uniformName The uniform name
     * @param supplier The delegate supplier
     * @return The new supplier
     */
    private Supplier<?> createNullLoggingSupplier(
        String uniformName, Supplier<?> supplier)
    {
        return () -> 
        {
            Object result = supplier.get();
            if (result == null)
            {
                if (!reportedNullUniformNames.contains(uniformName))
                {
                    logger.warning("Uniform value is null for " + uniformName);
                    reportedNullUniformNames.add(uniformName);
                }
            }
            return result;
        };
    }

    /**
     * Creates a supplier for the value of the uniform with the given name,
     * based on the {@link TechniqueParametersModel#getSemantic() semantic of
     * the TechniqueParametersModel} that the uniform has in the given 
     * {@link TechniqueModel}.<br>
     * <br>
     * Note: This method assumes that the semantic is not <code>null</code>.
     * If it is <code>null</code>, then an <code>IllegalArgumentException</code>
     * will be thrown.<br> 
     * <br>
     * The <code>currentNodeModel</code> acts as a source for local and global 
     * transforms of the node that is being rendered, unless a node is
     * referenced via the {@link TechniqueParametersModel#getNodeModel()}.
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
     * If the semantic does not have any of these values, then an
     * <code>IllegalArgumentException</code> will be thrown.<br>
     * <br>
     * All matrices will be in column-major order. The returned suppliers 
     * MAY always return the same array instance. So callers MUST NOT 
     * store or modify the returned arrays.<br>
     * <br>
     * About the <code>numJoints</code> factor in the <code>JOINTMATRIX</code>
     * case: The supplier will provide <code>numJoints</code> 4x4 matrices
     * in a single array of size <code>16*numJoints</code>. The number of
     * joints is determined as follows:
     * <ul>
     *   <li>
     *     The {@link NodeModel} that is given as the 
     *     <code>currentNodeModel</code> assumed to have a 
     *     {@link NodeModel#getSkinModel()}
     *   </li>
     *   <li>
     *     The {@link SkinModel} for this skin ID is looked up. The skin model
     *     has several {@link SkinModel#getJoints() joints}. The number of 
     *     joints is used as <code>numJoints</code>.
     *   </li>
     * </ul>
     * The actual contents of these joint matrices is computed from the
     * {@link SkinModel#getBindShapeMatrix(float[]) bind shape matrix}, the
     * {@link SkinModel#getInverseBindMatrices() inverse bind matrices} and 
     * the global transform of the given node and the joint node. See the glTF 
     * specification for details.<br>
     * <br>
     * 
     * @param uniformName The uniform name
     * @param techniqueModel The {@link TechniqueModel}
     * @param currentNodeModel The current {@link NodeModel}
     * @return The supplier, or <code>null</code> if the semantic did
     * not have any of the valid values mentioned above.
     * @throws IllegalArgumentException If the semantic of the
     * {@link TechniqueParametersModel} for the given uniform name
     * in the given {@link TechniqueModel} is is <code>null</code>
     * or not a valid {@link Semantic}
     */
    private Supplier<?> createSemanticBasedSupplier(
        String uniformName, TechniqueModel techniqueModel, 
        NodeModel currentNodeModel)
    {
        Objects.requireNonNull(uniformName, 
            "The uniformName may not be null");
        Objects.requireNonNull(techniqueModel, 
            "The techniqueModel may not be null");
        Objects.requireNonNull(currentNodeModel, 
            "The currentNodeModel may not be null");
        
        TechniqueParametersModel techniqueParameters =
            techniqueModel.getUniformParameters(uniformName);
        String semanticString = techniqueParameters.getSemantic();
        if (!Semantic.contains(semanticString))
        {
            throw new IllegalArgumentException(
                "Uniform " + uniformName + " has invalid semantic " + 
                semanticString + " in technique " + techniqueModel);
        }
        Semantic semantic = Semantic.valueOf(semanticString);
        
        NodeModel nodeModel = currentNodeModel;
        NodeModel parameterNodeModel = techniqueParameters.getNodeModel();
        if (parameterNodeModel != null)
        {
            nodeModel = parameterNodeModel;
        }

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
                return createJointMatrixSupplier(currentNodeModel);
            }
            
            default:
                break;
            
        }
        logger.severe("Unsupported semantic: "+semantic);
        return null;
    }
    
    
    /**
     * Create a supplier for the joint matrix (actually, joint matrices) of
     * the {@link SkinModel} that is contained in the given {@link NodeModel}.
     * 
     * @param nodeModel The {@link NodeModel} 
     * @return The supplier
     */
    private static Supplier<float[]> createJointMatrixSupplier(
        NodeModel nodeModel)
    {
        SkinModel skinModel = nodeModel.getSkinModel();

        // Create the supplier for the bind shape matrix (or a supplier
        // of the identity matrix, if the bind shape matrix is null)
        float bindShapeMatrix[] = skinModel.getBindShapeMatrix(null);
        Supplier<float[]> bindShapeMatrixSupplier = 
            MatrixOps.create4x4(() -> bindShapeMatrix)
            .log("bindShapeMatrix", Level.FINE)
            .build();

        List<NodeModel> joints = skinModel.getJoints();
        int numJoints = joints.size();

        // Create one supplier for each inverse bind matrix. Each of them will 
        // extract one element of the inverse bind matrix accessor data and 
        // provide it as a single float[16] array, representing a 4x4 matrix
        List<Supplier<float[]>> inverseBindMatrixSuppliers =
            new ArrayList<Supplier<float[]>>();
        for (int i = 0; i < numJoints; i++)
        {
            final int currentJointIndex = i;
            float inverseBindMatrix[] = new float[16];
            Supplier<float[]> inverseBindMatrixSupplier = () ->
            {
                return skinModel.getInverseBindMatrix(
                    currentJointIndex, inverseBindMatrix);
            };
            Supplier<float[]> loggingInverseBindMatrixSupplier =
                MatrixOps.create4x4(inverseBindMatrixSupplier)
                    .log("inverseBindMatrix "+i, Level.FINE)
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
        for (int j = 0; j < numJoints; j++)
        {
            NodeModel jointNodeModel = joints.get(j);
            
            Supplier<float[]> inverseBindMatrixSupplier = 
                inverseBindMatrixSuppliers.get(j);
            
            Supplier<float[]> jointMatrixSupplier = MatrixOps
                .create4x4(nodeModel.createGlobalTransformSupplier())
                .invert4x4()
                .multiply4x4(jointNodeModel.createGlobalTransformSupplier())
                .multiply4x4(inverseBindMatrixSupplier)
                .multiply4x4(bindShapeMatrixSupplier)
                .log("jointMatrix "+j, Level.FINE)
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


