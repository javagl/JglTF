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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.gl.ProgramModel;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.gl.TechniqueParametersModel;
import de.javagl.jgltf.model.gl.TechniqueStatesFunctionsModel;
import de.javagl.jgltf.model.gl.TechniqueStatesModel;
import de.javagl.jgltf.viewer.Morphing.MorphableAttribute;

/**
 * Default implementation of a {@link RenderedGltfModel}. This class builds 
 * and maintains the internal structures that are required for rendering the 
 * model using a {@link GlContext}. 
 */
class DefaultRenderedGltfModel implements RenderedGltfModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(DefaultRenderedGltfModel.class.getName());

    /**
     * The {@link GlContext} in which the {@link GltfModel} is rendered
     */
    private final GlContext glContext;
    
    /**
     * The view configuration for this rendered glTF
     */
    private final ViewConfiguration viewConfiguration;
    
    /**
     * The {@link GltfRenderData} which stores mappings from the 
     * {@link ProgramModel}, {@link TextureModel} and 
     * {@link BufferViewModel} instances to the corresponding
     * GL identifiers.
     */
    private final GltfRenderData gltfRenderData;
    
    /**
     * The factory that creates <code>Supplier</code> instances for
     * obtaining the values of uniform variables from a 
     * {@link MaterialModel}
     */
    private final UniformGetterFactory uniformGetterFactory;
    
    /**
     * The factory that creates <code>Runnable</code> instances that
     * set the values of uniform variables in the {@link GlContext},
     * to the values that are provided by the <code>Supplier</code> 
     * instances that are created by the {@link #uniformGetterFactory}
     */
    private final UniformSetterFactory uniformSetterFactory;
    
    /**
     * A function that is used for looking up the {@link TextureModel} for
     * a reference that is given in a {@link MaterialModel}. The 
     * {@link MaterialModel#getValues() material values} may contain
     * a reference to a texture for one of the uniform names. This
     * reference may be a texture ID (for glTF 1.0) or a texture index
     * (for glTF 2.0). 
     */
    private final Function<Object, ? extends TextureModel> textureModelLookup;
    
    /**
     * The list of commands that have to be executed for rendering the
     * opaque mesh primitives
     */
    private final List<Runnable> opaqueRenderCommands;

    /**
     * The list of commands that have to be executed for rendering the
     * transparent mesh primitives
     */
    private final List<Runnable> transparentRenderCommands;
    
    
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
     * @param textureModelLookup A function that is used for looking up 
     * the {@link TextureModel} for a reference that is given in the 
     * {@link MaterialModel#getValues() material values}
     * @param viewConfiguration The {@link ViewConfiguration}
     */
    public DefaultRenderedGltfModel(
        GlContext glContext, GltfModel gltfModel, 
        Function<Object, ? extends TextureModel> textureModelLookup, 
        ViewConfiguration viewConfiguration)
    {
        Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
        this.textureModelLookup = Objects.requireNonNull(textureModelLookup,
            "The textureModelLookup may not be null");
        this.glContext = glContext;
        this.viewConfiguration = Objects.requireNonNull(viewConfiguration,
            "The viewConfiguration may not be null");

        this.gltfRenderData = new GltfRenderData(glContext);
        this.uniformGetterFactory = new UniformGetterFactory( 
            viewConfiguration::getViewport,
            viewConfiguration::getViewMatrix,
            viewConfiguration::getProjectionMatrix);
        this.uniformSetterFactory = new UniformSetterFactory(glContext);

        this.opaqueRenderCommands = new ArrayList<Runnable>();
        this.transparentRenderCommands = new ArrayList<Runnable>();
        
        logger.fine("Processing scenes...");
        Optionals.of(gltfModel.getSceneModels())
            .forEach(this::processSceneModel);
        logger.fine("Processing scenes DONE...");
    }
    
    
    @Override
    public void delete()
    {
        gltfRenderData.delete();
        opaqueRenderCommands.clear();
        transparentRenderCommands.clear();
        opaqueRenderCommands.add(() ->
        {
            logger.warning("Rendered object has been deleted");
        });
    }

    @Override
    public void render()
    {
        for (Runnable renderCommand : opaqueRenderCommands)
        {
            renderCommand.run();
        }
        for (Runnable renderCommand : transparentRenderCommands)
        {
            renderCommand.run();
        }
    }
    
    @Override
    public void setCurrentCameraModel(CameraModel cameraModel)
    {
        viewConfiguration.setCurrentCameraModel(cameraModel);
    }
    
    /**
     * Process the given {@link SceneModel}, passing all its nodes to the
     * {@link #processNodeModel(NodeModel)} method
     * 
     * @param sceneModel The {@link SceneModel}
     */
    private void processSceneModel(SceneModel sceneModel)
    {
        logger.fine("Processing scene " + sceneModel);
        
        List<NodeModel> nodeModels = sceneModel.getNodeModels();
        for (NodeModel nodeModel : nodeModels)
        {
            processNodeModel(nodeModel);
        }
        logger.fine("Processing scene " + sceneModel + " DONE");
    }
    
    /**
     * Recursively process the given {@link NodeModel} and all its children, and
     * generate the internal rendering structures for the data that is found in
     * these nodes. This mainly refers to the {@link MeshPrimitiveModel}
     * instances, which will be passed to the
     * {@link #processMeshPrimitiveModel( NodeModel, MeshModel, MeshPrimitiveModel)}
     * method.
     * 
     * @param nodeModel The {@link NodeModel}
     */
    private void processNodeModel(NodeModel nodeModel)
    {
        logger.fine("Processing node " + nodeModel);

        List<MeshModel> meshModels = nodeModel.getMeshModels();
        for (MeshModel meshModel : meshModels)
        {
            List<MeshPrimitiveModel> primitives =
                meshModel.getMeshPrimitiveModels();
            for (int i = 0; i < primitives.size(); i++)
            {
                MeshPrimitiveModel meshPrimitiveModel = primitives.get(i);
                processMeshPrimitiveModel(nodeModel, meshModel,
                    meshPrimitiveModel);
            }
        }

        List<NodeModel> children = nodeModel.getChildren();
        for (NodeModel childNode : children)
        {
            processNodeModel(childNode);
        }

        logger.fine("Processing node " + nodeModel + " DONE");

    }

    /**
     * Process the given {@link MeshPrimitiveModel} that was found in a
     * {@link MeshModel} in the given {@link NodeModel}. This will create the
     * rendering commands for rendering the mesh primitive.
     * 
     * @param nodeModel The {@link NodeModel}
     * @param meshModel The {@link MeshModel}
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     */
    private void processMeshPrimitiveModel(NodeModel nodeModel,
        MeshModel meshModel, MeshPrimitiveModel meshPrimitiveModel)
    {
        logger.fine("Processing meshPrimitive...");

        MaterialModel materialModel = meshPrimitiveModel.getMaterialModel();
        TechniqueModel techniqueModel = materialModel.getTechniqueModel();
        ProgramModel programModel = techniqueModel.getProgramModel();

        // Obtain the GL program for the Program of the Technique
        Integer glProgram = gltfRenderData.obtainGlProgram(programModel);
        if (glProgram == null)
        {
            logger.warning("No GL program found for program " + programModel
                + " in technique " + techniqueModel);
            return;
        }

        // Create the vertex array and the attributes for the mesh primitive
        int glVertexArray = glContext.createGlVertexArray();
        gltfRenderData.addGlVertexArray(glVertexArray);
        List<Runnable> attributeUpdateCommands = 
            createAttributes(glVertexArray,
                nodeModel, meshModel, meshPrimitiveModel);

        // Create a list that contains all commands for rendering
        // the given mesh primitive
        List<Runnable> commands = new ArrayList<Runnable>();
        
        // Create the command to enable the program
        commands.add(() -> glContext.useGlProgram(glProgram));
        
        // Create the commands to set the uniforms
        List<Runnable> uniformSettingCommands = 
            createUniformSettingCommands(
                materialModel, nodeModel, glProgram);
        commands.addAll(uniformSettingCommands);

        // Create the commands to set the technique.states and 
        // the technique.states.functions values 
        commands.add(() -> glContext.disable(
            TechniqueStatesModel.getAllStates()));
        
        TechniqueStatesModel techniqueStatesModel = 
            techniqueModel.getTechniqueStatesModel();
        List<Integer> enabledStates = techniqueStatesModel.getEnable();
        commands.add(() -> {
            glContext.enable(enabledStates);
        });
        TechniqueStatesFunctionsModel techniqueStatesFunctionsModel = 
            techniqueStatesModel.getTechniqueStatesFunctionsModel();
        commands.addAll(
            createTechniqueStatesFunctionsSettingCommands(
                glContext, techniqueStatesFunctionsModel));
        
        commands.addAll(attributeUpdateCommands);
        
        // Create the command for the actual render call
        Runnable renderCommand = 
            createRenderCommand(meshPrimitiveModel, glVertexArray);
        commands.add(renderCommand);
        
        
        // Summarize all commands of this mesh primitive in a single one
        Runnable meshPrimitiveRenderCommand = new Runnable()
        {
            @Override
            public void run()
            {
                //logger.info("Executing " + this);
                for (Runnable command : commands)
                {
                    command.run();
                }
            }
            
            @Override
            public String toString()
            {
                return super.toString(); // XXX TODO
//                return RenderCommandUtils.createInfoString(
//                    gltf, meshPrimitiveName, techniqueId, 
//                    uniformSettingCommands);
            }
        };
        
        boolean isOpaque = !enabledStates.contains(GltfConstants.GL_BLEND);
        if (isOpaque)
        {
            opaqueRenderCommands.add(meshPrimitiveRenderCommand);
        }
        else
        {
            transparentRenderCommands.add(meshPrimitiveRenderCommand);
        }
        
        logger.fine("Processing meshPrimitive DONE");
    }
    
    
    /**
     * Create the command for actually rendering the given 
     * {@link MeshPrimitiveModel}, which is represented by the given
     * GL vertex array object
     * 
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     * @param glVertexArray The GL vertex array object
     * @return The rendering command
     */
    private Runnable createRenderCommand(
        MeshPrimitiveModel meshPrimitiveModel, int glVertexArray)
    {
        int mode = meshPrimitiveModel.getMode();

        AccessorModel indices = meshPrimitiveModel.getIndices();
        if (indices != null)
        {
            BufferViewModel indicesBufferViewModel = 
                indices.getBufferViewModel();
            Integer glIndicesBufferView = 
                gltfRenderData.obtainGlBufferView(indicesBufferViewModel);
            if (glIndicesBufferView == null)
            {
                logger.warning("No GL bufferView found for indices " + 
                    "bufferView " + indicesBufferViewModel);
                return emptyRunnable();
            }
            int count = indices.getCount();
            int type = indices.getComponentType();
            int offset = indices.getByteOffset();
            
            return () -> glContext.renderIndexed(
                glVertexArray, mode, glIndicesBufferView, count, type, offset);        
        }

        // Guess the number of vertices from the accessor.count of an
        // arbitrary attribute of the meshPrimitive, and create a command
        // for non-indexed rendering
        Map<String, AccessorModel> attributes = 
            meshPrimitiveModel.getAttributes(); 
        if (attributes.isEmpty())
        {
            logger.warning(
                "No indices and no attributes found in meshPrimitive");
            return emptyRunnable();
        }
        AccessorModel accessorModel = attributes.values().iterator().next();
        int count = accessorModel.getCount();
        return () -> glContext.renderNonIndexed(glVertexArray, mode, count);        
    }

    
    /**
     * Create a list of commands that set the values of the uniforms of the 
     * given {@link MaterialModel} in the {@link GlContext}.
     * 
     * @param materialModel The {@link MaterialModel}
     * @param nodeModel The {@link NodeModel} that contains the rendered
     * object
     * @param glProgram The OpenGL program
     * @return The list of commands
     */
    private List<Runnable> createUniformSettingCommands(
        MaterialModel materialModel, NodeModel nodeModel, Integer glProgram)
    {
        List<Runnable> uniformSettingCommands =
            new ArrayList<Runnable>();
        
        TechniqueModel techniqueModel = materialModel.getTechniqueModel();
        Map<String, String> uniforms = techniqueModel.getUniforms();
        int textureCounter = 0;
        for (String uniformName : uniforms.keySet())
        {
            // Fetch the technique.parameters for the uniform
            TechniqueParametersModel techniqueParametersModel = 
                techniqueModel.getUniformParameters(uniformName);
            
            // Create the supplier for the value that corresponds
            // the the uniform
            Supplier<?> uniformValueSupplier = 
                uniformGetterFactory.createUniformValueSupplier(
                    uniformName, materialModel, nodeModel);
            
            // Create the command for setting the uniform value
            // in the GL context
            int location = 
                glContext.getUniformLocation(glProgram, uniformName);
            
            if (location == -1)
            {
                logger.warning(
                    "No uniform location for uniform " + uniformName);
                continue;
            }
            
            // For GL_SAMPLER_2D uniforms, the command has to be created
            // here, because it depends on the (local) textureCounter
            Integer type = techniqueParametersModel.getType();
            if (type == GltfConstants.GL_SAMPLER_2D)
            {
                int textureIndex = textureCounter;
                Runnable uniformSettingCommand = () ->
                {
                    Object value[] = (Object[])uniformValueSupplier.get();
                    Object textureIdOrIndex = value[0];
                    
                    // TODO Should this handled by a return here?
                    // What about the texture counter etc?
                    if (textureIdOrIndex == null)
                    {
                        return;
                    }
                    
                    TextureModel textureModel = 
                        textureModelLookup.apply(textureIdOrIndex);
                    
                    // TODO Handle the case that the textureModel is not found
                    if (textureIdOrIndex == null)
                    {
                        logger.warning("No texture ID or index found "
                            + "for uniform " + uniformName);
                    }
                    else
                    {
                        Integer glTexture = 
                            gltfRenderData.obtainGlTexture(textureModel);
                        if (glTexture == null)
                        {
                            logger.warning("Could not obtain GL texture "
                                + "for texture " + textureIdOrIndex );
                        }
                        else
                        {
                            glContext.setUniformSampler(
                                location, textureIndex, glTexture);
                        }
                    }
                };
                textureCounter++;
                uniformSettingCommands.add(
                    RenderCommandUtils.debugUniformSettingCommand(
                        uniformSettingCommand, uniformName, 
                        uniformValueSupplier));
            }
            else
            {
                int count = techniqueParametersModel.getCount();
                Runnable uniformSettingCommand = 
                    uniformSetterFactory.createUniformSettingCommand(
                        location, type, count, uniformValueSupplier);
                uniformSettingCommands.add(
                    RenderCommandUtils.debugUniformSettingCommand(
                        uniformSettingCommand,  uniformName, 
                        uniformValueSupplier));
            }
        }
        return uniformSettingCommands;
    }
    
    /**
     * Create the functions that, when executed, call the functions
     * in the {@link GlContext} for setting the states that have been 
     * defined in the {@link TechniqueStatesFunctionsModel}. When any 
     * information is missing, the default values will be set.
     * 
     * @param glContext The {@link GlContext}
     * @param techniqueStatesFunctionsModel The 
     * {@link TechniqueStatesFunctionsModel} 
     * @return The list of commands
     */
    private static List<Runnable> createTechniqueStatesFunctionsSettingCommands(
        GlContext glContext, 
        TechniqueStatesFunctionsModel techniqueStatesFunctionsModel)
    {
        List<Runnable> commands = new ArrayList<Runnable>();
        
        float[] blendColor = 
            techniqueStatesFunctionsModel.getBlendColor();
        commands.add(() ->
        {
            glContext.setBlendColor(
                blendColor[0], blendColor[1], 
                blendColor[2], blendColor[3]);
        });
        
        int[] blendEquationSeparate = 
            techniqueStatesFunctionsModel.getBlendEquationSeparate();
        commands.add(() ->
        {
            glContext.setBlendEquationSeparate(
                blendEquationSeparate[0], blendEquationSeparate[1]);
        });
        
        int[] blendFuncSeparate = 
            techniqueStatesFunctionsModel.getBlendFuncSeparate();
        commands.add(() ->
        {
            glContext.setBlendFuncSeparate(
                blendFuncSeparate[0], blendFuncSeparate[1],
                blendFuncSeparate[2], blendFuncSeparate[3]);
        });
        
        boolean[] colorMask = 
            techniqueStatesFunctionsModel.getColorMask();
        commands.add(() ->
        {
            glContext.setColorMask(
                colorMask[0], colorMask[1],
                colorMask[2], colorMask[3]);
        });
        
        int[] cullFace = 
            techniqueStatesFunctionsModel.getCullFace();
        commands.add(() ->
        {
            glContext.setCullFace(cullFace[0]);
        });
        
        int[] depthFunc = 
            techniqueStatesFunctionsModel.getDepthFunc();
        commands.add(() ->
        {
            glContext.setDepthFunc(depthFunc[0]);
        });
        
        boolean[] depthMask = 
            techniqueStatesFunctionsModel.getDepthMask();
        commands.add(() ->
        {
            glContext.setDepthMask(depthMask[0]);
        });
        
        float[] depthRange = 
            techniqueStatesFunctionsModel.getDepthRange();
        commands.add(() ->
        {
            glContext.setDepthRange(depthRange[0], depthRange[1]);
        });
        
        int[] frontFace = 
            techniqueStatesFunctionsModel.getFrontFace();
        commands.add(() ->
        {
            glContext.setFrontFace(frontFace[0]);
        });
        
        float[] lineWidth = 
            techniqueStatesFunctionsModel.getLineWidth();
        commands.add(() ->
        {
            glContext.setLineWidth(lineWidth[0]);
        });
        
        float[] polygonOffset = 
            techniqueStatesFunctionsModel.getPolygonOffset();
        commands.add(() ->
        {
            glContext.setPolygonOffset(
                polygonOffset[0], polygonOffset[1]);
        });
        
        return commands;
    }

    /**
     * Walk through the {@link MeshPrimitiveModel#getAttributes() attributes} of
     * the given {@link MeshPrimitiveModel} and create the corresponding OpenGL
     * vertex attributes, bound to the given GL vertex array identifier. <br>
     * <br>
     * The returned list may contain commands that have to be executed before a
     * rendering pass, in order to update the attribute values: For attributes
     * that are interpolated with morph targets, these commands will update the
     * attribute data accordingly.
     * 
     * @param glVertexArray The GL vertex array
     * @param nodeModel The {@link NodeModel}
     * @param meshModel The {@link MeshModel}
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     * @return A (possibly) empty list of commands for updating the attributes
     */
    private List<Runnable> createAttributes(int glVertexArray,
        NodeModel nodeModel, MeshModel meshModel,
        MeshPrimitiveModel meshPrimitiveModel)
    {
        List<Runnable> attributeUpdateCommands = new ArrayList<Runnable>();

        MaterialModel materialModel = meshPrimitiveModel.getMaterialModel();
        TechniqueModel techniqueModel = materialModel.getTechniqueModel();
        ProgramModel programModel = techniqueModel.getProgramModel();

        // Obtain the GL program for the Program of the Technique
        Integer glProgram = gltfRenderData.obtainGlProgram(programModel);
        if (glProgram == null)
        {
            logger.warning("No GL program found for program " + 
                programModel + " in technique " + techniqueModel);
            return attributeUpdateCommands;
        }

        Map<String, AccessorModel> meshPrimitiveAttributes =
            meshPrimitiveModel.getAttributes();
        Map<String, String> attributes = techniqueModel.getAttributes();
        for (String attributeName : attributes.keySet())
        {
            TechniqueParametersModel attributeTechniqueParametersModel =
                techniqueModel.getAttributeParameters(attributeName);
            String semantic = attributeTechniqueParametersModel.getSemantic();

            AccessorModel accessorModel = null;
            MorphableAttribute morphableAttribute = null;
            if (Morphing.isMorphableAttribute(meshPrimitiveModel, semantic))
            {
                morphableAttribute = Morphing.createMorphableAttribute(
                    meshPrimitiveModel, semantic);
                accessorModel = morphableAttribute.getMorphedAccessorModel();
            }
            else
            {
                accessorModel = meshPrimitiveAttributes.get(semantic);
            }
            

            if (accessorModel == null)
            {
                logger.fine(
                    "No accessor model found for semantic " + semantic);
                continue;
            }

            BufferViewModel bufferViewModel =
                accessorModel.getBufferViewModel();

            Integer glBufferView =
                gltfRenderData.obtainGlBufferView(bufferViewModel);
            if (glBufferView == null)
            {
                logger.warning("No GL bufferView found for " + 
                    "bufferView " + bufferViewModel);
                continue;
            }

            if (morphableAttribute != null)
            {
                Runnable attributeUpdateCommand =
                    createAttributeUpdateCommand(glVertexArray, glBufferView,
                        nodeModel, meshModel, morphableAttribute);
                attributeUpdateCommands.add(attributeUpdateCommand);
            }

            // Collect the parameters for the GL calls
            int attributeLocation = 
                glContext.getAttributeLocation(glProgram, attributeName);
            if (attributeLocation == -1)
            {
                logger.warning("No attribute location for attribute " + 
                    attributeName + " in program " + programModel + ". " +
                    "The attribute name in the shader must match the " +
                    "key of the 'attributes' dictionary.");
            }
            int target = Optionals.of(
                bufferViewModel.getTarget(), GltfConstants.GL_ARRAY_BUFFER);
            int size = accessorModel.getElementType().getNumComponents();
            int type = accessorModel.getComponentType();
            int stride = accessorModel.getByteStride();
            int offset = accessorModel.getByteOffset();
            
            glContext.createVertexAttribute(glVertexArray, 
                target, glBufferView, attributeLocation, size,
                type, stride, offset);
        }
        return attributeUpdateCommands;
    }

    /**
     * Create a command that updates the specified attribute data. <br>
     * <br>
     * This command is supposed to be called on the rendering thread
     * prior to rendering a mesh primitive that contains the given
     * morphable attribute.<br>
     * <br>
     * Upon execution of the command, the current weights for the morph 
     * targets are obtained from the given {@link NodeModel} (or from
     * the given {@link MeshModel}, if those of the node are <code>null</code>).
     * These weights will be used to update the given {@link MorphableAttribute}
     * by calling {@link MorphableAttribute#updateMorphedAccessorData(float[])}.
     * The interpolated data will be written into the buffer that backs the
     * morphed attribute, and this buffer will be passed to the GL context
     * to be updated.
     * 
     * @param glVertexArray The GL vertex array
     * @param glBufferView The GL buffer view 
     * @param nodeModel The {@link NodeModel}
     * @param meshModel The {@link MeshModel}
     * @param morphableAttribute The {@link MorphableAttribute}
     * @return The command
     */
    private Runnable createAttributeUpdateCommand(
        int glVertexArray, int glBufferView,
        NodeModel nodeModel, MeshModel meshModel,
        MorphableAttribute morphableAttribute)
    {
        // Obtain the buffer view data from the morphed accessor model.
        // This buffer will be filled with the morphed data when
        // MorphableAttribute#updateMorphedAccessorData is called.
        AccessorModel morphedAccessorModel = 
            morphableAttribute.getMorphedAccessorModel();
        BufferViewModel morphedBufferViewModel =
            morphedAccessorModel.getBufferViewModel();
        morphedBufferViewModel.getByteLength();
        ByteBuffer morphedBufferViewData =
            morphedBufferViewModel.getBufferViewData();
        int bufferSize = morphedBufferViewData.capacity();

        float weights[] = new float[morphableAttribute.getNumTargets()];
        
        // Create the actual command that is executed before rendering
        Runnable attributeUpdateCommand = new Runnable()
        {
            @Override
            public void run()
            {
                // Update the weights based on the weights from the node
                // or the mesh 
                if (nodeModel.getWeights() != null)
                {
                    System.arraycopy(
                        nodeModel.getWeights(), 0, weights, 0, weights.length);
                } 
                else if (meshModel.getWeights() != null)
                {
                    System.arraycopy(
                        meshModel.getWeights(), 0, weights, 0, weights.length);
                }
                
                // Perform the update, and pass the updated buffer to GL
                morphableAttribute.updateMorphedAccessorData(weights);
                glContext.updateVertexAttribute(glVertexArray,
                    GltfConstants.GL_ARRAY_BUFFER, glBufferView, 0, bufferSize,
                    morphedBufferViewData);
            }
        }; 
        return attributeUpdateCommand;
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

}
