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
package de.javagl.jgltf.viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.Camera;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Material;
import de.javagl.jgltf.impl.Mesh;
import de.javagl.jgltf.impl.MeshPrimitive;
import de.javagl.jgltf.impl.Node;
import de.javagl.jgltf.impl.Program;
import de.javagl.jgltf.impl.Scene;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;
import de.javagl.jgltf.impl.TechniqueStates;
import de.javagl.jgltf.impl.Texture;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.GltfModels;
import de.javagl.jgltf.model.Maps;

/**
 * A representation of a rendered {@link GlTF}. This class uses a 
 * {@link GltfModel} and builds the internal structures that are
 * required for rendering the {@link GlTF} using a {@link GlContext}. 
 */
public class RenderedGltf
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(RenderedGltf.class.getName());
    
    /**
     * The {@link GltfModel} that wraps the {@link GltfData} and the 
     * {@link GlTF}, and provides methods to create suppliers for 
     * the {@link TechniqueParameters#getSemantic() semantic}-based 
     * properties of the glTF. 
     */
    private final GltfModel gltfModel;
    
    /**
     * The {@link GltfData} that was contained in the {@link GltfModel},
     * and stores the {@link GlTF} and the associated (binary) data
     */
    private final GltfData gltfData;
    
    /**
     * The {@link GlTF} that was contained in the {@link GltfData}
     */
    private final GlTF gltf;
    
    /**
     * The OpenGL context that is used for rendering
     */
    private final GlContext glContext;

    /**
     * The {@link GltfRenderData} that maintains the OpenGL data for the
     * {@link Program}s, {@link Texture}s and {@link BufferView}s of
     * the {@link GlTF}.
     */
    private final GltfRenderData gltfRenderData;
    
    /**
     * The list of IDs of {@link Node}s that contain a non-null
     * {@link Node#getCamera() camera ID} 
     */
    private final Map<String, String> cameraIdToNodeId;
    
    /**
     * A {@link SettableSupplier} that provides the ID of the current 
     * camera node. That is, it provides the ID of the {@link Node} 
     * that contains a {@link Node#getCamera() camera ID} that refers
     * to the {@link Camera} that should be used for rendering. 
     * See {@link #setCurrentCameraId(String)}
     */
    private SettableSupplier<String> currentCameraNodeIdSupplier;
    
    /**
     * A supplier for the view matrix. 
     * See {@link #createViewMatrixSupplier(Supplier)} for details.
     */
    private final Supplier<float[]> viewMatrixSupplier;
    
    /**
     * A supplier for the projection matrix.
     * See {@link #createProjectionMatrixSupplier(Supplier)} for details.
     */
    private final Supplier<float[]> projectionMatrixSupplier;

    /**
     * The list of commands that have to be executed for rendering
     */
    private final List<Runnable> renderCommands;
    
    /**
     * Creates a new instance that renders the given {@link GltfModel} using 
     * the given {@link GlContext}.<br>
     * <br>
     * The view- and projection matrix suppliers are optional and may be
     * <code>null</code>. If they are not null, they are assumed to 
     * provide float arrays with 16 elements, representing the respective
     * matrices in column-major order. Thus, they may be used to render
     * the object with a different camera configuration than the ones
     * that are stored in the glTF.  
     * 
     * @param gltfModel The {@link GltfModel}
     * @param glContext The {@link GlContext}
     * @param externalViewMatrixSupplier The optional external supplier of
     * a view matrix.
     * @param externalProjectionMatrixSupplier The optional external supplier
     * of a projection matrix.
     */
    public RenderedGltf(
        GltfModel gltfModel, GlContext glContext, 
        Supplier<float[]> externalViewMatrixSupplier, 
        Supplier<float[]> externalProjectionMatrixSupplier)
    {
        this.gltfModel = Objects.requireNonNull(gltfModel,
            "The gltfModel may not be null");
        this.glContext = Objects.requireNonNull(glContext,
            "The glContext may not be null");

        this.gltfData = gltfModel.getGltfData();
        this.gltf = gltfData.getGltf();
        this.gltfRenderData = new GltfRenderData(gltfData, glContext);
        
        this.renderCommands = new ArrayList<Runnable>();
        
        this.cameraIdToNodeId = new LinkedHashMap<String, String>();
        this.currentCameraNodeIdSupplier = new SettableSupplier<String>();

        this.viewMatrixSupplier = 
            createViewMatrixSupplier(externalViewMatrixSupplier);
        this.projectionMatrixSupplier = 
            createProjectionMatrixSupplier(externalProjectionMatrixSupplier);
        
        logger.fine("Processing scenes...");
        Maps.forEachEntry(gltf.getScenes(), this::processScene);
        logger.fine("Processing scenes DONE...");
        
        if (externalViewMatrixSupplier == null)
        {
            if (!cameraIdToNodeId.isEmpty())
            {
                String cameraId = cameraIdToNodeId.keySet().iterator().next();
                setCurrentCameraId(cameraId);
            }
        }
    }
    
    /**
     * Delete this object by removing its GL data from the {@link GlContext}.
     * <br>
     * After this method has been called, attempting to {@link #render()}
     * this object will result in a warning to be printed.
     */
    void delete()
    {
        Collection<Integer> glTextures = 
            gltfRenderData.getGlTextures();
        for (int glTexture : glTextures)
        {
            glContext.deleteGlTexture(glTexture);
        }
        Collection<Integer> glBufferViews = 
            gltfRenderData.getGlBufferViews();
        for (int glBufferView : glBufferViews)
        {
            glContext.deleteGlBufferView(glBufferView);
        }
        Collection<Integer> glPrograms = 
            gltfRenderData.getGlPrograms();
        for (int glProgram : glPrograms)
        {
            glContext.deleteGlProgram(glProgram);
        }
        Collection<Integer> glVertexArrays = 
            gltfRenderData.getGlVertexArrays();
        for (int glVertexArray : glVertexArrays)
        {
            glContext.deleteGlVertexArray(glVertexArray);
        }
        
        renderCommands.clear();
        renderCommands.add(() ->
        {
            logger.warning("Rendered object has been deleted");
        });
    }

    /**
     * Render this instance. This is assumed to be called on the GL thread.
     */
    public void render()
    {
        for (Runnable renderCommand : renderCommands)
        {
            renderCommand.run();
        }
    }
    
    /**
     * Create a supplier for the view matrix, as a float array with 16 
     * elements, containing the view matrix in column-major order.<br>
     * <br>
     * The resulting supplier will supply a view matrix as follows:
     * <ul>
     *   <li> 
     *     If a non-<code>null</code> {@link #setCurrentCameraId(String)
     *     current camera ID} has been set, then the view matrix from
     *     the {@link GltfModel} will be returned
     *   </li>
     *   <li>
     *     Otherwise, if the external matrix supplier is non-<code>null</code>,
     *     then its matrix will be returned
     *   </li>
     *   <li>
     *     Otherwise, an identity matrix will be returned
     *   </li>
     * </ul>
     * 
     * @param externalViewMatrixSupplier The optional external view matrix
     * supplier that may have been given in the constructor
     * @return The view matrix supplier
     */
    private Supplier<float[]> createViewMatrixSupplier(
        Supplier<float[]> externalViewMatrixSupplier)
    {
        Supplier<float[]> gltfViewMatrixSupplier = 
            gltfModel.createViewMatrixSupplier(
                currentCameraNodeIdSupplier);
        float identity[] = createIdentityMatrix4x4();
        return () ->
        {
            String cameraNodeId = currentCameraNodeIdSupplier.get();
            if (cameraNodeId == null)
            {
                if (externalViewMatrixSupplier == null)
                {
                    return identity;
                }
                return externalViewMatrixSupplier.get();
            }
            return gltfViewMatrixSupplier.get();
        };
    }
    
    /**
     * Create a supplier for the projection matrix, as a float array with 16 
     * elements, containing the projection matrix in column-major order.<br>
     * <br>
     * The resulting supplier will supply a projection matrix as follows:
     * <ul>
     *   <li> 
     *     If a non-<code>null</code> {@link #setCurrentCameraId(String)
     *     current camera ID} has been set, then the projection matrix 
     *     from the {@link GltfModel} will be returned
     *   </li>
     *   <li>
     *     Otherwise, if the external matrix supplier is non-<code>null</code>,
     *     then its matrix will be returned
     *   </li>
     *   <li>
     *     Otherwise, an identity matrix will be returned
     *   </li>
     * </ul>
     * 
     * @param externalProjectionMatrixSupplier The optional external projection
     * matrix supplier that may have been given in the constructor
     * @return The view matrix supplier
     */
    private Supplier<float[]> createProjectionMatrixSupplier(
        Supplier<float[]> externalProjectionMatrixSupplier)
    {
        Supplier<float[]> gltfProjectionMatrixSupplier = 
            gltfModel.createProjectionMatrixSupplier(
                currentCameraNodeIdSupplier);
        float identity[] = createIdentityMatrix4x4();
        return () ->
        {
            String cameraNodeId = currentCameraNodeIdSupplier.get();
            if (cameraNodeId == null)
            {
                if (externalProjectionMatrixSupplier == null)
                {
                    return identity;
                }
                return externalProjectionMatrixSupplier.get();
            }
            return gltfProjectionMatrixSupplier.get();
        };
    }
    
    /**
     * Returns a 4x4 identity matrix, as an array with 16 elements
     * 
     * @return The matrix
     */
    private static float[] createIdentityMatrix4x4()
    {
        float result[] = new float[16];
        result[0] = 1.0f;
        result[5] = 1.0f;
        result[10] = 1.0f;
        result[15] = 1.0f;
        return result;
    }
    
    
    /**
     * Returns an unmodifiable (possibly empty) view on the collection of IDs 
     * of {@link Camera}s.
     *  
     * @return The {@link Camera} IDs
     */
    Collection<String> getCameraIds()
    {
        return Collections.unmodifiableCollection(cameraIdToNodeId.keySet());
    }
    
    /**
     * Set the ID of the {@link Camera} that should be used for rendering.<br>
     * <br>
     * If the given ID is <code>null</code>, then the external camera
     * will be used.
     *  
     * @param cameraId The ID of the {@link Camera}
     * @throws IllegalArgumentException If the given ID is not <code>null</code>
     * and not contained in the {@link #getCameraIds() camera IDs}
     */
    void setCurrentCameraId(String cameraId)
    {
        if (cameraId == null)
        {
            currentCameraNodeIdSupplier.set(null);
            return;
        }
        if (!cameraIdToNodeId.containsKey(cameraId))
        {
            throw new IllegalArgumentException(
                "The ID " + cameraId + " is not a valid ID for " + 
                "a camera. Valid IDs are " + cameraIdToNodeId.keySet());
        }
        String cameraNodeId = cameraIdToNodeId.get(cameraId);
        currentCameraNodeIdSupplier.set(cameraNodeId);
    }
    
    /**
     * Process the given {@link Scene}, passing all its nodes to the
     * {@link #processNode(String)} method
     * 
     * @param sceneId The {@link Scene} ID
     * @param scene The {@link Scene}
     */
    private void processScene(String sceneId, Scene scene)
    {
        logger.info("Processing scene " + sceneId);
        
        List<String> sceneNodes = optional(scene.getNodes());
        for (String sceneNodeId : sceneNodes)
        {
            processNode(sceneNodeId);
        }
        logger.info("Processing scene " + sceneId + " DONE");
    }
    
    
    /**
     * Recursively process the given {@link Node} and all its children,
     * and generate the internal rendering structures for the data that 
     * is found in these nodes. This mainly refers to the 
     * {@link MeshPrimitive}s, which will be passed to the 
     * {@link #processMeshPrimitive(MeshPrimitive, String)} method.
     * 
     * @param nodeId The {@link Node} ID
     */
    private void processNode(String nodeId) 
    {
        logger.fine("Processing node " + nodeId);

        Node node = gltf.getNodes().get(nodeId);
        
        List<String> meshes = optional(node.getMeshes());
        for (String meshId : meshes)
        {
            Mesh mesh = gltf.getMeshes().get(meshId);

            List<MeshPrimitive> primitives = optional(mesh.getPrimitives());
            for (MeshPrimitive meshPrimitive : primitives)
            {
                processMeshPrimitive(meshPrimitive, nodeId);
            }
        }
        
        String cameraId = node.getCamera();
        if (cameraId != null)
        {
            cameraIdToNodeId.put(cameraId, nodeId);
        }
        
        List<String> children = optional(node.getChildren());
        for (String childNodeId : children)
        {
            processNode(childNodeId);
        }
        
        logger.fine("Processing node " + nodeId + " DONE");
        
    }
    
    

    /**
     * Process the given {@link MeshPrimitive} that was found in a {@link Mesh}
     * in a {@link Node} with the given ID. This will create the rendering 
     * commands for rendering the mesh primitive.
     *  
     * @param meshPrimitive The {@link MeshPrimitive}
     * @param nodeId The {@link Node} ID
     */
    private void processMeshPrimitive(
        MeshPrimitive meshPrimitive, String nodeId)
    {
        logger.fine("Processing meshPrimitive...");
        
        String materialId = meshPrimitive.getMaterial();
        Material material = gltf.getMaterials().get(materialId);
        String techniqueId = material.getTechnique();
        Technique technique = gltf.getTechniques().get(techniqueId);

        // Obtain the GL program for the Program of the Technique
        String programId = technique.getProgram();
        Integer glProgram = gltfRenderData.obtainGlProgram(programId);
        if (glProgram == null)
        {
            logger.warning("No GL program found for program " + 
                programId + " in technique " + techniqueId);
            return;
        }
        
        // Create the command to enable the program
        renderCommands.add(() -> glContext.useGlProgram(glProgram));
        
        // Create the commands to set the technique.states
        renderCommands.add(() -> glContext.disable(getAllStates()));
        renderCommands.add(() -> glContext.enable(getEnabledStates(technique)));
        
        // Create the commands to set the uniforms
        List<Runnable> uniformSettingCommands = 
            createUniformSettingCommands(
                technique, material, nodeId, glProgram);
        renderCommands.addAll(uniformSettingCommands);
        
        // Create the vertex array and the attributes for the mesh primitive
        int glVertexArray = glContext.createGlVertexArray();
        gltfRenderData.addGlVertexArray(glVertexArray);
        createAttributes(glVertexArray, meshPrimitive);

        // Finally, create the command for rendering the mesh primitive
        Runnable renderCommand = 
            createRenderCommand(meshPrimitive, glVertexArray);
        renderCommands.add(renderCommand);
        
        logger.fine("Processing meshPrimitive DONE");
    }

    /**
     * Returns a list containing all possible states that may be contained
     * in a <code>technique.states.enable</code> list.
     * 
     * @return All possible states
     */
    private static List<Integer> getAllStates()
    {
        List<Integer> allStates = Arrays.asList(
            3042,  // GL_BLEND
            2884,  // GL_CULL_FACE
            2929,  // GL_DEPTH_TEST
            32823, // GL_POLYGON_OFFSET_FILL
            32926, // GL_SAMPLE_ALPHA_TO_COVERAGE
            3089   // GL_SCISSOR_TEST
        );
        return allStates;
    }
    
    /**
     * Returns the set of states that should be enabled for the given 
     * {@link Technique}
     * 
     * @param technique The {@link Technique}
     * @return The enabled states
     */
    private List<Integer> getEnabledStates(Technique technique)
    {
        TechniqueStates states = technique.getStates();
        if (states == null)
        {
            return new TechniqueStates().defaultEnable();
        }
        List<Integer> enable = states.getEnable();
        if (enable == null)
        {
            return states.defaultEnable();
        }
        return enable;
    }

    /**
     * Create a list of commands that set the values of the uniforms of the 
     * given {@link Technique} in the {@link GlContext}, based on the values 
     * that are obtained from the {@link GlTF}, each time that they are
     * executed.
     * 
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @param nodeId The ID of the {@link Node} that contains the rendered
     * object
     * @param glProgram The OpenGL program
     * @return The list of commands
     */
    private List<Runnable> createUniformSettingCommands(
        Technique technique, Material material,
        String nodeId, Integer glProgram)
    {
        List<Runnable> uniformSettingCommands =
            new ArrayList<Runnable>();

        UniformSetterFactory uniformSetterFactory = 
            new UniformSetterFactory(glContext);
        
        Map<String, String> uniforms = 
            optional(technique.getUniforms());
        int textureCounter = 0;
        for (String uniformName : uniforms.keySet())
        {
            // Fetch the technique.parameters for the uniform
            String techniqueParameterId = uniforms.get(uniformName);
            TechniqueParameters techniqueParameters = 
                technique.getParameters().get(techniqueParameterId);
            
            // Create the supplier for the value that corresponds
            // the the uniform
            Supplier<?> uniformValueSupplier = 
                createUniformValueSupplier(
                    uniformName, technique, material, 
                    nodeId, techniqueParameters);
            
            // Create the command for setting the uniform value
            // in the GL context
            int location = 
                glContext.getUniformLocation(glProgram, uniformName);
            Integer type = techniqueParameters.getType();
            if (type == GltfConstants.GL_SAMPLER_2D)
            {
                int textureIndex = textureCounter;
                Runnable uniformSettingCommand = () ->
                {
                    Object value = uniformValueSupplier.get();
                    String textureId = String.valueOf(value);
                    Integer glTexture = 
                        gltfRenderData.obtainGlTexture(textureId);
                    if (glTexture == null)
                    {
                        logger.warning("Could not obtain GL texture for " + 
                            "texture " + textureId );
                    }
                    else
                    {
                        glContext.setUniformSampler(
                            location, textureIndex, glTexture);
                    }
                };
                textureCounter++;
                uniformSettingCommands.add(uniformSettingCommand);
            }
            else
            {
                Integer count = Optional
                    .ofNullable(techniqueParameters.getCount())
                    .orElse(1);
                Runnable uniformSettingCommand = 
                    uniformSetterFactory.createUniformSettingCommand(
                        location, type, count, uniformValueSupplier);
                uniformSettingCommands.add(
                    debugUniformSettingCommand(uniformSettingCommand, 
                        uniformName, uniformValueSupplier));
            }
        }
        return uniformSettingCommands;
    }
    
    /**
     * For debugging: Create a wrapper around the given delegate that prints 
     * the value that is set for the specified uniform
     * 
     * @param delegate The delegate command
     * @param uniformName The uniform name
     * @param uniformValueSupplier The supplier for the uniform value
     * @return The wrapping command
     */
    private static Runnable debugUniformSettingCommand(
        Runnable delegate, String uniformName, Supplier<?> uniformValueSupplier)
    {
        return () ->
        {
            Level level = Level.FINEST;
            if (logger.isLoggable(level))
            {
                String valueString = debugString(uniformValueSupplier.get());
                logger.log(level,
                    "For uniform " + uniformName + " setting " + valueString);
            }
            delegate.run();
        };
    }
    
    /**
     * Create a debugging string for the given uniform value
     * 
     * @param value The value
     * @return The debug string
     */
    private static String debugString(Object value)
    {
        if (value instanceof int[])
        {
            return Arrays.toString((int[])value);
        }
        if (value instanceof float[])
        {
            return Arrays.toString((float[])value);
        }
        return String.valueOf(value);
    }
    
    /**
     * Create a supplier that supplies the value for the specified uniform.
     * If there is no {@link TechniqueParameters#getSemantic() semantic}, 
     * then this value will be obtained from the {@link Technique} or the 
     * {@link Material}. Otherwise, the value will be derived from the 
     * context of the currently rendered {@link Node}, which is given by 
     * the local and global transform of the {@link Node} with the given ID 
     * 
     * @param uniformName The name of the uniform
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @param nodeId The {@link Node} ID
     * @param techniqueParameters The {@link TechniqueParameters}
     * @return The supplier for the uniform value
     */
    private Supplier<?> createUniformValueSupplier(
        String uniformName, Technique technique, Material material,
        String nodeId, TechniqueParameters techniqueParameters)
    {
        String semantic = techniqueParameters.getSemantic();
        if (semantic == null)
        {
            return GltfModels.createGenericSupplier(
                uniformName, technique, material);
        }
        return gltfModel.createSemanticBasedSupplier(
            uniformName, technique, nodeId, 
            viewMatrixSupplier, projectionMatrixSupplier);
    }

    
    /**
     * Walk through the {@link MeshPrimitive#getAttributes() attributes} of
     * the given {@link MeshPrimitive} and create the corresponding OpenGL 
     * vertex attributes, bound to the given GL vertex array identifier.
     * 
     * @param glVertexArray The GL vertex array
     * @param meshPrimitive The {@link MeshPrimitive}
     */
    private void createAttributes(int glVertexArray, 
        MeshPrimitive meshPrimitive)
    {
        String materialId = meshPrimitive.getMaterial();
        Material material = gltf.getMaterials().get(materialId);
        String techniqueId = material.getTechnique();
        Technique technique = gltf.getTechniques().get(techniqueId);

        // Obtain the GL program for the Program of the Technique
        String programId = technique.getProgram();
        Integer glProgram = gltfRenderData.obtainGlProgram(programId);
        if (glProgram == null)
        {
            logger.warning("No GL program found for program " + 
                programId + " in technique " + techniqueId);
            return;
        }
        
        // Don't mix these up! (See notes below) 
        Map<String, String> techniqueAttributes = 
            optional(technique.getAttributes());
        Map<String, String> meshPrimitiveAttributes = 
            optional(meshPrimitive.getAttributes());
        
        for (String attributeName : techniqueAttributes.keySet())
        {
            // The technique.attributes map GLSL attribute
            // names to TechniqueParameter IDs
            String techniqueParameterId = 
                techniqueAttributes.get(attributeName);
            Map<String, TechniqueParameters> parameters = 
                technique.getParameters();
            TechniqueParameters techniqueParameters = 
                parameters.get(techniqueParameterId);
            String semantic = techniqueParameters.getSemantic();
            
            // The meshPrimitive.attributes map semantics of
            // TechniqueParameters to accessor IDs
            String accessorId = meshPrimitiveAttributes.get(semantic);
            Accessor accessor = gltf.getAccessors().get(accessorId);

            String bufferViewId = accessor.getBufferView();
            BufferView bufferView = gltf.getBufferViews().get(bufferViewId);

            Integer glBufferView = 
                gltfRenderData.obtainGlBufferView(bufferViewId);
            if (glBufferView == null)
            {
                logger.warning("No GL bufferView found for " + 
                    "bufferView " + bufferViewId);
                continue;
            }

            // Collect the parameters for the GL calls
            int attributeLocation = 
                glContext.getAttributeLocation(glProgram, attributeName);
            int target = Optional
                .ofNullable(bufferView.getTarget())
                .orElse(GltfConstants.GL_ARRAY_BUFFER);
            int size = Accessors.getNumComponentsForAccessorType(
                accessor.getType());
            int type = accessor.getComponentType();
            int numBytesForComponentType = 
                Accessors.getNumBytesForAccessorComponentType(type);
            int defaultStride = size * numBytesForComponentType;
            int stride = Optional
                .ofNullable(accessor.getByteStride())
                .orElse(defaultStride);
            int offset = accessor.getByteOffset();
            
            glContext.createVertexAttribute(glVertexArray, 
                target, glBufferView, attributeLocation, size,
                type, stride, offset);
        }
    }
    
    /**
     * Create the command for actually rendering the given 
     * {@link MeshPrimitive}, which is represented by the given
     * GL vertex array object
     * 
     * @param meshPrimitive The {@link MeshPrimitive}
     * @param glVertexArray The GL vertex array object
     * @return The rendering command
     */
    private Runnable createRenderCommand(
        MeshPrimitive meshPrimitive, int glVertexArray)
    {
        int mode = Optional
            .ofNullable(meshPrimitive.getMode())
            .orElse(meshPrimitive.defaultMode());

        String indicesAccessorId = meshPrimitive.getIndices();
        if (indicesAccessorId != null)
        {
            Accessor indicesAccessor = 
                gltf.getAccessors().get(indicesAccessorId);
            String indicesBufferViewId = indicesAccessor.getBufferView();
            Integer glIndicesBufferView = 
                gltfRenderData.obtainGlBufferView(indicesBufferViewId);
            if (glIndicesBufferView == null)
            {
                logger.warning("No GL bufferView found for indices " + 
                    "bufferView " + indicesBufferViewId);
                return emptyRunnable();
            }
            int count = indicesAccessor.getCount();
            int type = indicesAccessor.getComponentType();
            int offset = indicesAccessor.getByteOffset();
            
            return () -> glContext.renderIndexed(
                glVertexArray, mode, glIndicesBufferView, count, type, offset);        
        }

        // Guess the number of vertices from the accessor.count of an
        // arbitrary attribute of the meshPrimitive, and create a command
        // for non-indexed rendering
        Map<String, String> attributes = 
            optional(meshPrimitive.getAttributes());
        if (attributes.isEmpty())
        {
            logger.warning(
                "No indices and no attributes found in meshPrimitive");
            return emptyRunnable();
        }
        String accessorId = attributes.values().iterator().next();
        Accessor accessor = gltf.getAccessors().get(accessorId);
        int count = accessor.getCount();
        return () -> glContext.renderNonIndexed(glVertexArray, mode, count);        
    }

    /**
     * Return an empty runnable, as a last resort for errors
     * 
     * @return The empty runnable
     */
    private static Runnable emptyRunnable()
    {
        return () ->
        {
            // Empty
        };
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
        return Optional.ofNullable(list).orElse(Collections.emptyList());
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
        return Optional.ofNullable(map).orElse(Collections.emptyMap());
    }
    
    
}
