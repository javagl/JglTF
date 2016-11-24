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
import java.util.Locale;
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
import de.javagl.jgltf.impl.Shader;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;
import de.javagl.jgltf.impl.TechniqueStates;
import de.javagl.jgltf.impl.TechniqueStatesFunctions;
import de.javagl.jgltf.impl.Texture;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.GltfModels;
import de.javagl.jgltf.model.Maps;
import de.javagl.jgltf.model.io.IO;

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
        
        this.opaqueRenderCommands = new ArrayList<Runnable>();
        this.transparentRenderCommands = new ArrayList<Runnable>();
        
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
        
        opaqueRenderCommands.clear();
        transparentRenderCommands.clear();
        opaqueRenderCommands.add(() ->
        {
            logger.warning("Rendered object has been deleted");
        });
    }

    /**
     * Render this instance. This is assumed to be called on the GL thread.
     */
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
     * {@link #processMeshPrimitive(MeshPrimitive, String, String)} method.
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
            for (int i = 0; i < primitives.size(); i++)
            {
                MeshPrimitive meshPrimitive = primitives.get(i);
                String meshPrimitiveName = meshId + ".primitives[" + i + "]";
                processMeshPrimitive(meshPrimitive, meshPrimitiveName, nodeId);
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
     * @param meshPrimitiveName A human-readable name identifying the given
     * {@link MeshPrimitive}, used only for logging and debugging
     * @param nodeId The {@link Node} ID
     */
    private void processMeshPrimitive(
        MeshPrimitive meshPrimitive, String meshPrimitiveName, String nodeId)
    {
        logger.fine("Processing meshPrimitive...");
        
        String materialId = meshPrimitive.getMaterial();
        Material material = obtainMaterial(materialId);
        String techniqueId = material.getTechnique();
        Technique technique = obtainTechnique(techniqueId);

        // Obtain the GL program for the Program of the Technique
        String programId = technique.getProgram();
        Integer glProgram = gltfRenderData.obtainGlProgram(programId);
        if (glProgram == null)
        {
            logger.warning("No GL program found for program " + 
                programId + " in technique " + techniqueId);
            return;
        }
        
        // Create the vertex array and the attributes for the mesh primitive
        int glVertexArray = glContext.createGlVertexArray();
        gltfRenderData.addGlVertexArray(glVertexArray);
        createAttributes(glVertexArray, meshPrimitive);

        // Create a list that contains all commands for rendering
        // the given mesh primitive
        List<Runnable> commands = new ArrayList<Runnable>();
        
        // Create the command to enable the program
        commands.add(() -> glContext.useGlProgram(glProgram));
        
        // Create the commands to set the technique.states and 
        // the technique.states.functions values 
        commands.add(() -> glContext.disable(getAllStates()));
        List<Integer> enabledStates = getEnabledStates(technique);
        commands.add(() -> {
            glContext.enable(enabledStates);
        });
        commands.addAll(
            createTechniqueStatesFunctionsSettingCommands(technique));
        
        // Create the commands to set the uniforms
        List<Runnable> uniformSettingCommands = 
            createUniformSettingCommands(
                technique, material, nodeId, glProgram);
        commands.addAll(uniformSettingCommands);

        // Create the command for the actual render call
        Runnable renderCommand = 
            createRenderCommand(meshPrimitive, glVertexArray);
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
                return createInfoString();
            }
            
            /**
             * Create an elaborate info string for this render command
             * (solely intended for debugging)
             * 
             * @return The info string
             */
            private String createInfoString()
            {
                StringBuilder sb = new StringBuilder();
                sb.append("RenderCommand for " + meshPrimitiveName + "\n");

                sb.append("    technique: " + techniqueId + "\n");
                
                String programId = technique.getProgram();
                Program program = gltf.getPrograms().get(programId);
                String vertexShaderId = program.getVertexShader();
                String fragmentShaderId = program.getFragmentShader();
                Shader vertexShader = gltf.getShaders().get(vertexShaderId);
                Shader fragmentShader = gltf.getShaders().get(fragmentShaderId);
                String vertexShaderUriString = vertexShader.getUri();
                String fragmentShaderUriString = fragmentShader.getUri();
                String shaderNames = "";
                if (IO.isDataUriString(vertexShaderUriString))
                {
                    shaderNames += "v: (data URI), ";
                }
                else
                {
                    shaderNames += "v: " + vertexShaderUriString + ", ";
                }
                if (IO.isDataUriString(fragmentShaderUriString))
                {
                    shaderNames += "f: (data URI)";
                }
                else
                {
                    shaderNames += "f: " + fragmentShaderUriString;
                }
                sb.append("    program: " + programId + 
                    " (" + shaderNames + ")\n");
                
                sb.append("    enabledStates: ");
                for (Integer enabledState : enabledStates)
                {
                    sb.append(GltfConstants.stringFor(enabledState)+", ");
                }
                sb.append("\n");
                sb.append("    uniforms:\n");
                for (Runnable uniformSettingCommand : uniformSettingCommands)
                {
                    sb.append("        " + uniformSettingCommand + "\n");
                }
                sb.append("\n");
                return sb.toString();
            }
            
        };
        
        if (enabledStates.contains(GltfConstants.GL_BLEND))
        {
            transparentRenderCommands.add(meshPrimitiveRenderCommand);
        }
        else
        {
            opaqueRenderCommands.add(meshPrimitiveRenderCommand);
        }
        
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
            GltfConstants.GL_BLEND,
            GltfConstants.GL_CULL_FACE,
            GltfConstants.GL_DEPTH_TEST,
            GltfConstants.GL_POLYGON_OFFSET_FILL,
            GltfConstants.GL_SAMPLE_ALPHA_TO_COVERAGE,
            GltfConstants.GL_SCISSOR_TEST
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
        TechniqueStates states = obtainTechniqueStates(technique);
        List<Integer> enable = states.getEnable();
        if (enable == null)
        {
            return states.defaultEnable();
        }
        return enable;
    }
    
    /**
     * Create the functions that, when executed, call the functions
     * in the {@link GlContext} for setting the states that have been 
     * defined in the {@link TechniqueStatesFunctions}. When any 
     * information is missing, the default values will be set.
     * 
     * @param technique The {@link Technique}
     * @return The list of commands
     */
    private List<Runnable> createTechniqueStatesFunctionsSettingCommands(
        Technique technique)
    {
        TechniqueStatesFunctions functions = 
            obtainTechniqueStatesFunctions(technique);
        List<Runnable> commands = new ArrayList<Runnable>();
        
        float[] blendColor = optional(
            functions.getBlendColor(), 
            functions.defaultBlendColor());
        commands.add(() ->
        {
            glContext.setBlendColor(
                blendColor[0], blendColor[1], 
                blendColor[2], blendColor[3]);
        });
        
        int[] blendEquationSeparate = optional(
            functions.getBlendEquationSeparate(),
            functions.defaultBlendEquationSeparate());
        commands.add(() ->
        {
            glContext.setBlendEquationSeparate(
                blendEquationSeparate[0], blendEquationSeparate[1]);
        });
        
        int[] blendFuncSeparate = optional(
            functions.getBlendFuncSeparate(),
            functions.defaultBlendFuncSeparate());
        commands.add(() ->
        {
            glContext.setBlendFuncSeparate(
                blendFuncSeparate[0], blendFuncSeparate[1],
                blendFuncSeparate[2], blendFuncSeparate[3]);
        });
        
        boolean[] colorMask = optional(
            functions.getColorMask(),
            functions.defaultColorMask());
        commands.add(() ->
        {
            glContext.setColorMask(
                colorMask[0], colorMask[1],
                colorMask[2], colorMask[3]);
        });
        
        int[] cullFace = optional(
            functions.getCullFace(),
            functions.defaultCullFace());
        commands.add(() ->
        {
            glContext.setCullFace(cullFace[0]);
        });
        
        int[] depthFunc = optional(
            functions.getDepthFunc(),
            functions.defaultDepthFunc());
        commands.add(() ->
        {
            glContext.setDepthFunc(depthFunc[0]);
        });
        
        boolean[] depthMask = optional(
            functions.getDepthMask(),
            functions.defaultDepthMask());
        commands.add(() ->
        {
            glContext.setDepthMask(depthMask[0]);
        });
        
        float[] depthRange = optional(
            functions.getDepthRange(),
            functions.defaultDepthRange());
        commands.add(() ->
        {
            glContext.setDepthRange(depthRange[0], depthRange[1]);
        });
        
        int[] frontFace = optional(
            functions.getFrontFace(),
            functions.defaultFrontFace());
        commands.add(() ->
        {
            glContext.setFrontFace(frontFace[0]);
        });
        
        float[] lineWidth = optional(
            functions.getLineWidth(),
            functions.defaultLineWidth());
        commands.add(() ->
        {
            glContext.setLineWidth(lineWidth[0]);
        });
        
        float[] polygonOffset = optional( 
            functions.getPolygonOffset(),
            functions.defaultPolygonOffset());
        commands.add(() ->
        {
            glContext.setPolygonOffset(
                polygonOffset[0], polygonOffset[1]);
        });
        
        // Scissor was removed in glTF 1.1, but still handled here
        float[] theScissor = functions.getScissor();
        float[] scissor;
        if (theScissor != null)
        {
            scissor = theScissor;
        }
        else
        {
            // The fact that no sensible default values for the scissor
            // width and height can be given here was one of the reasons
            // of why it was removed in glTF 1.1
            float defaultWidth = Short.MAX_VALUE;
            float defaultHeight = Short.MAX_VALUE;
            scissor = new float[] {
                0.0f, 0.0f, defaultWidth, defaultHeight
            };
        }
        commands.add(() ->
        {
            glContext.setScissor(
                (int)scissor[0], (int)scissor[1], 
                (int)scissor[2], (int)scissor[3]);
        });
        
        return commands;
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
                uniformSettingCommands.add(
                    debugUniformSettingCommand(uniformSettingCommand, 
                        uniformName, uniformValueSupplier));
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
        Runnable command = new Runnable()
        {
            @Override
            public void run()
            {
                Level level = Level.FINEST;
                if (logger.isLoggable(level))
                {
                    String valueString = 
                        debugString(uniformValueSupplier.get());
                    logger.log(level,
                        "For uniform " + uniformName + 
                        " setting " + valueString);
                }
                delegate.run();
            }
            
            @Override
            public String toString()
            {
                String valueString = 
                    debugString(uniformValueSupplier.get());
                return "For uniform " + uniformName + 
                    " setting " + valueString;
            }
        };
        return command;
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
            float array[] = (float[])value;
            if (array.length == 16)
            {
                return "\n"+createMatrixString(array, 4);
            }
            if (array.length == 9)
            {
                return "\n"+createMatrixString(array, 3);
            }
            return Arrays.toString(array);
        }
        return String.valueOf(value);
    }
    
    /**
     * Create an unspecified string for the given matrix, suitable for debug
     * output.
     * 
     * @param matrix The matrix
     * @param columns The number of columns
     * @return The matrix string
     */
    private static String createMatrixString(float matrix[], int columns)
    {
        String format = "%8.3f";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int rows = matrix.length / columns;
        for (int r = 0; r < rows; r++)
        {
            if (r > 0)
            {
                sb.append("\n");
                sb.append(" ");
            }
            for (int c = 0; c < columns; c++)
            {
                if (c > 0)
                {
                    sb.append("  ");
                }
                int index = c + r * columns;
                if (index < matrix.length)
                {
                    float value = matrix[index];
                    sb.append(String.format(Locale.ENGLISH, format, value));
                }
            }
        }
        sb.append("]");
        return sb.toString();
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
        Material material = obtainMaterial(materialId);
        String techniqueId = material.getTechnique();
        Technique technique = obtainTechnique(techniqueId);

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
     * Obtain the {@link Material} with the given ID from the {@link GlTF},
     * or return the {@link GltfDefaults#getDefaultMaterial() default
     * material} if the given ID is <code>null</code> or the 
     * {@link GltfDefaults#isDefaultMaterialId(String) default material ID}.
     * 
     * @param materialId The {@link Material} ID
     * @return The {@link Material}
     */
    private Material obtainMaterial(String materialId)
    {
        if (materialId == null ||
            GltfDefaults.isDefaultMaterialId(materialId))
        {
            return GltfDefaults.getDefaultMaterial();
        }
        return gltf.getMaterials().get(materialId);
    }

    /**
     * Obtain the {@link Technique} with the given ID from the {@link GlTF},
     * or return the {@link GltfDefaults#getDefaultTechnique() default
     * technique} if the given ID is <code>null</code> or the 
     * {@link GltfDefaults#isDefaultTechniqueId(String) default technique ID}.
     * 
     * @param techniqueId The {@link Technique} ID
     * @return The {@link Technique}
     */
    private Technique obtainTechnique(String techniqueId)
    {
        if (techniqueId == null ||
            GltfDefaults.isDefaultTechniqueId(techniqueId))
        {
            return GltfDefaults.getDefaultTechnique();
        }
        return gltf.getTechniques().get(techniqueId);
    }
    
    /**
     * Return the {@link TechniqueStates} from the given {@link Technique},
     * or the {@link TechniqueStates} from the 
     * {@link GltfDefaults#getDefaultTechnique() default technique} if
     * the given {@link Technique} does not contain any {@link TechniqueStates}
     *  
     * @param technique The {@link Technique}
     * @return The {@link TechniqueStates}
     */
    TechniqueStates obtainTechniqueStates(Technique technique)
    {
        TechniqueStates states = technique.getStates();
        if (states == null)
        {
            return GltfDefaults.getDefaultTechnique().getStates();
        }
        return states;
    }
    
    /**
     * Return the {@link TechniqueStatesFunctions} from the 
     * {@link TechniqueStates} of the given {@link Technique},
     * or the {@link TechniqueStatesFunctions} from the 
     * {@link GltfDefaults#getDefaultTechnique() default technique} if
     * the given {@link Technique} does not contain any 
     * {@link TechniqueStates} or {@link TechniqueStatesFunctions}
     *  
     * @param technique The {@link Technique}
     * @return The {@link TechniqueStatesFunctions}
     */
    TechniqueStatesFunctions obtainTechniqueStatesFunctions(Technique technique)
    {
        TechniqueStates states = obtainTechniqueStates(technique);
        TechniqueStatesFunctions functions = states.getFunctions();
        if (functions == null)
        {
            TechniqueStates defaultStates = 
                GltfDefaults.getDefaultTechnique().getStates();
            return defaultStates.getFunctions();
        }
        return functions;
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
    
    /**
     * Returns the given value, or the given default value if the
     * given value is <code>null</code>
     * 
     * @param t The value
     * @param defaultValue The default value
     * @return The value or the default value
     */
    private static <T> T optional(T t, T defaultValue)
    {
        return t != null ? t : defaultValue;
    }
}
