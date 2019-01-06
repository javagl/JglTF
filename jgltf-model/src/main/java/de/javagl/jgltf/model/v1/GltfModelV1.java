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
package de.javagl.jgltf.model.v1;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.impl.v1.Animation;
import de.javagl.jgltf.impl.v1.AnimationChannel;
import de.javagl.jgltf.impl.v1.AnimationChannelTarget;
import de.javagl.jgltf.impl.v1.AnimationSampler;
import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.Camera;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Mesh;
import de.javagl.jgltf.impl.v1.MeshPrimitive;
import de.javagl.jgltf.impl.v1.Node;
import de.javagl.jgltf.impl.v1.Program;
import de.javagl.jgltf.impl.v1.Sampler;
import de.javagl.jgltf.impl.v1.Scene;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.impl.v1.Skin;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueParameters;
import de.javagl.jgltf.impl.v1.TechniqueStatesFunctions;
import de.javagl.jgltf.impl.v1.Texture;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MathUtils;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.Utils;
import de.javagl.jgltf.model.gl.ProgramModel;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.gl.ShaderModel.ShaderType;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.gl.TechniqueParametersModel;
import de.javagl.jgltf.model.gl.TechniqueStatesFunctionsModel;
import de.javagl.jgltf.model.gl.TechniqueStatesModel;
import de.javagl.jgltf.model.gl.impl.DefaultProgramModel;
import de.javagl.jgltf.model.gl.impl.DefaultShaderModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueParametersModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueStatesModel;
import de.javagl.jgltf.model.gl.impl.v1.DefaultTechniqueStatesFunctionsModelV1;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultChannel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultSampler;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.impl.DefaultCameraModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMaterialModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.v1.gl.DefaultModels;
import de.javagl.jgltf.model.v1.gl.GltfDefaults;
import de.javagl.jgltf.model.v1.gl.Techniques;

/**
 * Implementation of a {@link GltfModel}, based on a {@link GlTF glTF 1.0}.<br>
 */
public final class GltfModelV1 implements GltfModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModelV1.class.getName());

    /**
     * The {@link GltfAssetV1} of this model
     */
    private final GltfAssetV1 gltfAsset;
    
    /**
     * The {@link GlTF} of the {@link GltfAssetV1}    
     */
    private final GlTF gltf;
    
    /**
     * The binary data that is associated with this model in the case
     * that the glTF was a binary glTF
     */
    private final ByteBuffer binaryData;
    
    /**
     * The {@link IndexMappingSet}
     */
    private final IndexMappingSet indexMappingSet;

    /**
     * The {@link AccessorModel} instances that have been created from
     * the {@link Accessor} instances
     */
    private final List<DefaultAccessorModel> accessorModels;

    /**
     * The {@link AnimationModel} instances that have been created from
     * the {@link Animation} instances
     */
    private final List<DefaultAnimationModel> animationModels;
    
    /**
     * The {@link BufferModel} instances that have been created from
     * the {@link Buffer} instances
     */
    private final List<DefaultBufferModel> bufferModels;

    /**
     * The {@link BufferViewModel} instances that have been created from
     * the {@link Buffer} instances
     */
    private final List<DefaultBufferViewModel> bufferViewModels;
    
    /**
     * The {@link CameraModel} instances that have been created from
     * the {@link Camera} references of {@link Node} instances
     */
    private final List<DefaultCameraModel> cameraModels;

    /**
     * The {@link ImageModel} instances that have been created from
     * the {@link Image} references of {@link Node} instances
     */
    private final List<DefaultImageModel> imageModels;

    /**
     * The {@link MaterialModel} instances that have been created from
     * the {@link Material} instances
     */
    private final List<DefaultMaterialModel> materialModels;
    
    /**
     * The {@link MeshModel} instances that have been created from
     * the {@link Mesh} instances
     */
    private final List<DefaultMeshModel> meshModels;

    /**
     * The {@link NodeModel} instances that have been created from
     * the {@link Node} instances
     */
    private final List<DefaultNodeModel> nodeModels;

    /**
     * The {@link SceneModel} instances that have been created from
     * the {@link Scene} instances
     */
    private final List<DefaultSceneModel> sceneModels;

    /**
     * The {@link SkinModel} instances that have been created from
     * the {@link Skin} instances
     */
    private final List<DefaultSkinModel> skinModels;

    /**
     * The {@link TextureModel} instances that have been created from
     * the {@link Texture} instances
     */
    private final List<DefaultTextureModel> textureModels;

    /**
     * The {@link ShaderModel} instances that have been created from
     * the {@link Shader} instances
     */
    private final List<DefaultShaderModel> shaderModels;

    /**
     * The {@link ProgramModel} instances that have been created from
     * the {@link Program} instances
     */
    private final List<DefaultProgramModel> programModels;
    
    /**
     * The {@link TechniqueModel} instances that have been created from
     * the {@link Technique} instances
     */
    private final List<DefaultTechniqueModel> techniqueModels;
    

    /**
     * Creates a new model for the given glTF
     * 
     * @param gltfAsset The {@link GltfAssetV1}
     */
    public GltfModelV1(GltfAssetV1 gltfAsset)
    {
        this.gltfAsset = Objects.requireNonNull(gltfAsset, 
            "The gltf may not be null");
        this.gltf = gltfAsset.getGltf();

        ByteBuffer binaryData = gltfAsset.getBinaryData();
        if (binaryData != null && binaryData.capacity() > 0)
        {
            this.binaryData = binaryData;
        }
        else
        {
            this.binaryData = null;
        }
        
        this.indexMappingSet = IndexMappingSets.create(gltf);
        
        this.accessorModels = new ArrayList<DefaultAccessorModel>();
        this.animationModels = new ArrayList<DefaultAnimationModel>();
        this.bufferModels = new ArrayList<DefaultBufferModel>();
        this.bufferViewModels = new ArrayList<DefaultBufferViewModel>();
        this.cameraModels = new ArrayList<DefaultCameraModel>();
        this.imageModels = new ArrayList<DefaultImageModel>();
        this.materialModels = new ArrayList<DefaultMaterialModel>();
        this.meshModels = new ArrayList<DefaultMeshModel>();
        this.nodeModels = new ArrayList<DefaultNodeModel>();
        this.sceneModels = new ArrayList<DefaultSceneModel>();
        this.skinModels = new ArrayList<DefaultSkinModel>();
        this.textureModels = new ArrayList<DefaultTextureModel>();
        this.shaderModels = new ArrayList<DefaultShaderModel>();
        this.programModels = new ArrayList<DefaultProgramModel>();
        this.techniqueModels = new ArrayList<DefaultTechniqueModel>();
        
        createAccessorModels();
        createAnimationModels();
        createBufferModels();
        createBufferViewModels();
        createImageModels();
        createMaterialModels();
        createMeshModels();
        createNodeModels();
        createSceneModels();
        createSkinModels();
        createTextureModels();
        createShaderModels();
        createProgramModels();
        createTechniqueModels();
        
        initBufferModels();
        initBufferViewModels();
        
        initAccessorModels();
        
        assignBufferViewByteStrides();
        
        initAnimationModels();
        initImageModels();
        initMaterialModels();
        initMeshModels();
        initNodeModels();
        initSceneModels();
        initSkinModels();
        initTextureModels();
        initShaderModels();
        initProgramModels();
        initTechniqueModels();
        
        instantiateCameraModels();
    }
    
    /**
     * Returns the {@link BufferModel} for the {@link Buffer} with the given ID.
     * If the given ID is not valid, then a warning will be printed and 
     * <code>null</code> will be returned.
     *  
     * @param bufferId The {@link Buffer} ID
     * @return The {@link BufferModel}
     */
    public BufferModel getBufferModelById(String bufferId)
    {
        return get("buffers", bufferId, bufferModels);
    }
    
    /**
     * Returns the {@link ShaderModel} for the {@link Shader} with the given ID.
     * If the given ID is not valid, then a warning will be printed and 
     * <code>null</code> will be returned.
     *  
     * @param shaderId The {@link Shader} ID
     * @return The {@link ShaderModel}
     */
    public ShaderModel getShaderModelById(String shaderId)
    {
        return get("shaders", shaderId, shaderModels);
    }

    /**
     * Returns the {@link ImageModel} for the {@link Image} with the given ID.
     * If the given ID is not valid, then a warning will be printed and 
     * <code>null</code> will be returned.
     *  
     * @param imageId The {@link Image} ID
     * @return The {@link ImageModel}
     */
    public ImageModel getImageModelById(String imageId)
    {
        return get("images", imageId, imageModels);
    }
    
    /**
     * Returns the {@link TextureModel} for the {@link Texture} with the given
     * ID. If the given ID is not valid, then a warning will be printed and 
     * <code>null</code> will be returned.<br>
     * <br>
     * This is only used for supporting the legacy technique-based rendering.
     *  
     * @param textureId The {@link Texture} ID
     * @return The {@link TextureModel}
     */
    public TextureModel getTextureModelById(String textureId)
    {
        return get("textures", textureId, textureModels);
    }
    
    /**
     * Returns the {@link AccessorModel} for the {@link Accessor} with the 
     * given ID.
     * If the given ID is not valid, then a warning will be printed and 
     * <code>null</code> will be returned.
     *  
     * @param accessorId The {@link Accessor} ID
     * @return The {@link AccessorModel}
     */
    public AccessorModel getAccessorModelById(String accessorId)
    {
        return get("accessors", accessorId, accessorModels);
    }
    
    /**
     * Returns the {@link BufferViewModel} for the {@link BufferView} with the 
     * given ID.
     * If the given ID is not valid, then a warning will be printed and 
     * <code>null</code> will be returned.
     *  
     * @param bufferViewId The {@link BufferView} ID
     * @return The {@link BufferViewModel}
     */
    private BufferViewModel getBufferViewModelById(String bufferViewId)
    {
        return get("bufferViews", bufferViewId, bufferViewModels);
    }
    
    /**
     * Create the {@link AccessorModel} instances
     */
    private void createAccessorModels()
    {
        Map<String, Accessor> accessors = Optionals.of(gltf.getAccessors());
        for (Accessor accessor : accessors.values())
        {
            DefaultAccessorModel accessorModel = createAccessorModel(accessor);
            accessorModels.add(accessorModel);
        }
    }

    /**
     * Create a {@link DefaultAccessorModel} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @return The {@link AccessorModel}
     */
    private static DefaultAccessorModel createAccessorModel(Accessor accessor)
    {
        Integer componentType = accessor.getComponentType();
        Integer byteOffset = accessor.getByteOffset();
        Integer count = accessor.getCount();
        ElementType elementType = ElementType.forString(accessor.getType());
        Integer byteStride = accessor.getByteStride();
        if (byteStride == null)
        {
            byteStride = elementType.getNumComponents() *
                Accessors.getNumBytesForAccessorComponentType(
                    componentType);
        }
        DefaultAccessorModel accessorModel = new DefaultAccessorModel(
            componentType, count, elementType);
        accessorModel.setByteOffset(byteOffset);
        accessorModel.setByteStride(byteStride);
        return accessorModel;
    }

    /**
     * Create the {@link AnimationModel} instances
     */
    private void createAnimationModels()
    {
        Map<String, Animation> animations = Optionals.of(gltf.getAnimations());
        for (int i = 0; i < animations.size(); i++)
        {
            animationModels.add(new DefaultAnimationModel());
        }
    }
    
    /**
     * Create the {@link BufferModel} instances
     */
    private void createBufferModels()
    {
        Map<String, Buffer> buffers = Optionals.of(gltf.getBuffers());
        for (Buffer buffer : buffers.values())
        {
            DefaultBufferModel bufferModel = new DefaultBufferModel();
            bufferModel.setUri(buffer.getUri());
            bufferModels.add(bufferModel);
        }
    }
    
    /**
     * Create the {@link BufferViewModel} instances
     */
    private void createBufferViewModels()
    {
        Map<String, BufferView> bufferViews = 
            Optionals.of(gltf.getBufferViews());
        for (BufferView bufferView : bufferViews.values())
        {
            DefaultBufferViewModel bufferViewModel = 
                createBufferViewModel(bufferView);
            bufferViewModels.add(bufferViewModel);
        }
    }

    /**
     * Create a {@link DefaultBufferViewModel} for the given {@link BufferView}
     * 
     * @param bufferView The {@link BufferView}
     * @return The {@link BufferViewModel}
     */
    private static DefaultBufferViewModel createBufferViewModel(
        BufferView bufferView)
    {
        int byteOffset = bufferView.getByteOffset();
        Integer byteLength = bufferView.getByteLength();
        if (byteLength == null)
        {
            logger.warning("No byteLength found in BufferView");
            byteLength = 0;
        }
        Integer target = bufferView.getTarget();
        DefaultBufferViewModel bufferViewModel = 
            new DefaultBufferViewModel(target);
        bufferViewModel.setByteOffset(byteOffset);
        bufferViewModel.setByteLength(byteLength);
        return bufferViewModel;
    }
    
    /**
     * Create the {@link ImageModel} instances
     */
    private void createImageModels()
    {
        Map<String, Image> images = 
            Optionals.of(gltf.getImages());
        for (Image image : images.values())
        {
            DefaultImageModel imageModel = 
                new DefaultImageModel(null, null);
            String uri = image.getUri();
            imageModel.setUri(uri);
            imageModels.add(imageModel);
        }
    }
    
    /**
     * Create the {@link MaterialModel} instances
     */
    private void createMaterialModels()
    {
        Map<String, Material> materials = Optionals.of(gltf.getMaterials());
        for (int i = 0; i < materials.size(); i++)
        {
            materialModels.add(new DefaultMaterialModel());
        }
    }
    
    /**
     * Create the {@link MeshModel} instances
     */
    private void createMeshModels()
    {
        Map<String, Mesh> meshes = Optionals.of(gltf.getMeshes());
        for (int i = 0; i < meshes.size(); i++)
        {
            meshModels.add(new DefaultMeshModel());
        }
    }

    /**
     * Create the {@link NodeModel} instances
     */
    private void createNodeModels()
    {
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        for (int i = 0; i < nodes.size(); i++)
        {
            nodeModels.add(new DefaultNodeModel());
        }
    }

    /**
     * Create the {@link SceneModel} instances
     */
    private void createSceneModels()
    {
        Map<String, Scene> scenes = Optionals.of(gltf.getScenes());
        for (int i = 0; i < scenes.size(); i++)
        {
            sceneModels.add(new DefaultSceneModel());
        }
    }
    
    /**
     * Create the {@link SkinModel} instances
     */
    private void createSkinModels()
    {
        Map<String, Skin> skins = Optionals.of(gltf.getSkins());
        for (Entry<String, Skin> entry : skins.entrySet())
        {
            Skin skin = entry.getValue();
            float[] bindShapeMatrix = skin.getBindShapeMatrix();
            skinModels.add(new DefaultSkinModel(bindShapeMatrix));
        }
    }

    /**
     * Create the {@link TextureModel} instances
     */
    private void createTextureModels()
    {
        Map<String, Texture> textures = Optionals.of(gltf.getTextures());
        Map<String, Sampler> samplers = Optionals.of(gltf.getSamplers());
        for (Entry<String, Texture> entry : textures.entrySet())
        {
            Texture texture = entry.getValue();
            String samplerId = texture.getSampler();
            Sampler sampler = samplers.get(samplerId);
            
            int magFilter = Optionals.of(
                sampler.getMagFilter(), sampler.defaultMagFilter());
            int minFilter = Optionals.of(
                sampler.getMinFilter(), sampler.defaultMinFilter());
            int wrapS = Optionals.of(
                sampler.getWrapS(), sampler.defaultWrapS());
            int wrapT = Optionals.of(
                sampler.getWrapT(), sampler.defaultWrapT());
            
            textureModels.add(new DefaultTextureModel(
                magFilter, minFilter, wrapS, wrapT));
        }
    }
    
    /**
     * Create the {@link ShaderModel} instances
     */
    private void createShaderModels()
    {
        Map<String, Shader> shaders = Optionals.of(gltf.getShaders());
        for (Entry<String, Shader> entry : shaders.entrySet())
        {
            Shader shader = entry.getValue();
            Integer type = shader.getType();
            ShaderType shaderType = null;
            if (type == GltfConstants.GL_VERTEX_SHADER)
            {
                shaderType = ShaderType.VERTEX_SHADER;
            }
            else 
            {
                shaderType = ShaderType.FRAGMENT_SHADER;
            }
            DefaultShaderModel shaderModel =
                new DefaultShaderModel(shader.getUri(), shaderType);
            shaderModels.add(shaderModel);
        }
    }

    /**
     * Create the {@link ProgramModel} instances
     */
    private void createProgramModels()
    {
        Map<String, Program> programs = Optionals.of(gltf.getPrograms());
        for (int i = 0; i < programs.size(); i++)
        {
            programModels.add(new DefaultProgramModel());
        }
    }
    
    /**
     * Create the {@link TechniqueModel} instances
     */
    private void createTechniqueModels()
    {
        Map<String, Technique> techniques = Optionals.of(gltf.getTechniques());
        for (int i = 0; i < techniques.size(); i++)
        {
            techniqueModels.add(new DefaultTechniqueModel());
        }
    }
    

    /**
     * Initialize the {@link AccessorModel} instances
     */
    private void initAccessorModels()
    {
        Map<String, Accessor> accessors = Optionals.of(gltf.getAccessors());
        for (Entry<String, Accessor> entry : accessors.entrySet())
        {
            String accessorId = entry.getKey();
            Accessor accessor = entry.getValue();
            String bufferViewId = accessor.getBufferView();
            BufferViewModel bufferViewModel = 
                get("bufferViews", bufferViewId, bufferViewModels);
            DefaultAccessorModel accessorModel =
                get("accessors", accessorId, accessorModels);
            accessorModel.setName(accessor.getName());
            accessorModel.setBufferViewModel(bufferViewModel);
        }
    }

    /**
     * Initialize the {@link AnimationModel} instances
     */
    private void initAnimationModels()
    {
        Map<String, Animation> animations = Optionals.of(gltf.getAnimations());
        for (Entry<String, Animation> entry : animations.entrySet())
        {
            String animationId = entry.getKey();
            Animation animation = entry.getValue();
            DefaultAnimationModel animationModel =
                get("animations", animationId, animationModels);
            animationModel.setName(animation.getName());

            List<AnimationChannel> channels = 
                Optionals.of(animation.getChannels());
            for (AnimationChannel animationChannel : channels)
            {
                Channel channel = createChannel(animation, animationChannel);
                animationModel.addChannel(channel);
            }
        }
    }
    
    /**
     * Initialize the {@link ImageModel} instances
     */
    private void initImageModels()
    {
        Map<String, Image> images = Optionals.of(gltf.getImages());
        for (Entry<String, Image> entry : images.entrySet())
        {
            String imageId = entry.getKey();
            Image image = entry.getValue();
            DefaultImageModel imageModel =
                get("images", imageId, imageModels);
            imageModel.setName(image.getName());
            
            if (BinaryGltfV1.hasBinaryGltfExtension(image))
            {
                String bufferViewId = 
                    BinaryGltfV1.getBinaryGltfBufferViewId(image);
                BufferViewModel bufferViewModel = 
                    getBufferViewModelById(bufferViewId);
                imageModel.setBufferViewModel(bufferViewModel);
            }
            else
            {
                String uri = image.getUri();
                if (IO.isDataUriString(uri))
                {
                    byte data[] = IO.readDataUri(uri);
                    ByteBuffer imageData = Buffers.create(data);
                    imageModel.setImageData(imageData);
                }
                else
                {
                    ByteBuffer imageData = gltfAsset.getReferenceData(uri);
                    imageModel.setImageData(imageData);
                }
            }
        }
    }
    
    /**
     * Create the {@link Channel} object for the given animation and animation
     * channel
     * 
     * @param animation The {@link Animation}
     * @param animationChannel The {@link AnimationChannel}
     * @return The {@link Channel}
     */
    private Channel createChannel(
        Animation animation, AnimationChannel animationChannel)
    {
        Map<String, String> parameters = 
            Optionals.of(animation.getParameters());
        Map<String, AnimationSampler> samplers = 
            Optionals.of(animation.getSamplers());

        String samplerId = animationChannel.getSampler();
        AnimationSampler animationSampler = samplers.get(samplerId);
        
        String inputParameterId = animationSampler.getInput();
        String inputAccessorId = parameters.get(inputParameterId);
        if (inputAccessorId == null)
        {
            // This was valid for a short time, when glTF 2.0 was still 
            // called glTF 1.1. The check here is not perfectly reliable, 
            // but there should be a decreasing number of glTF 1.0 models 
            // out there, and even fewer glTF 1.1 ones.
            logger.warning(
                "Assuming " + inputParameterId + " to be an accessor ID");
            inputAccessorId = inputParameterId;
        }
        DefaultAccessorModel inputAccessorModel = 
            get("accessors", inputAccessorId, accessorModels);
        
        String outputParameterId = animationSampler.getOutput();
        String outputAccessorId = parameters.get(outputParameterId);
        if (outputAccessorId == null)
        {
            // This was valid for a short time, when glTF 2.0 was still 
            // called glTF 1.1. The check here is not perfectly reliable, 
            // but there should be a decreasing number of glTF 1.0 models 
            // out there, and even fewer glTF 1.1 ones.
            logger.warning(
                "Assuming " + outputParameterId + " to be an accessor ID");
            outputAccessorId = outputParameterId;
        }
        DefaultAccessorModel outputAccessorModel = 
            get("accessors", outputAccessorId, accessorModels);
        
        String interpolationString = 
            animationSampler.getInterpolation();
        Interpolation interpolation = 
            interpolationString == null ? Interpolation.LINEAR :
            Interpolation.valueOf(interpolationString);
        
        AnimationModel.Sampler sampler = new DefaultSampler(
            inputAccessorModel, interpolation, outputAccessorModel);
        
        AnimationChannelTarget animationChannelTarget = 
            animationChannel.getTarget();
        String nodeId = animationChannelTarget.getId();
        String path = animationChannelTarget.getPath();
        
        NodeModel nodeModel = get("nodes", nodeId, nodeModels);
        
        AnimationModel.Channel channel = 
            new DefaultChannel(sampler, nodeModel, path);
        return channel;
    }

    /**
     * Initialize the {@link BufferModel} instances
     */
    private void initBufferModels()
    {
        Map<String, Buffer> buffers = Optionals.of(gltf.getBuffers());
        for (Entry<String, Buffer> entry : buffers.entrySet())
        {
            String bufferId = entry.getKey();
            Buffer buffer = entry.getValue();
            DefaultBufferModel bufferModel = 
                get("buffers", bufferId, bufferModels);
            bufferModel.setName(buffer.getName());
            
            if (BinaryGltfV1.isBinaryGltfBufferId(bufferId))
            {
                if (binaryData == null)
                {
                    logger.severe("The glTF contains a buffer with the binary"
                        + " buffer ID, but no binary data has been given");
                    continue;
                }
                bufferModel.setBufferData(binaryData);
            }
            else
            {
                String uri = buffer.getUri();
                if (IO.isDataUriString(uri))
                {
                    byte data[] = IO.readDataUri(uri);
                    ByteBuffer bufferData = Buffers.create(data);
                    bufferModel.setBufferData(bufferData);
                }
                else
                {
                    ByteBuffer bufferData = gltfAsset.getReferenceData(uri);
                    bufferModel.setBufferData(bufferData);
                }
            }
        }
    }
    
    
    /**
     * Initialize the {@link BufferViewModel} instances
     */
    private void initBufferViewModels()
    {
        Map<String, BufferView> bufferViews = 
            Optionals.of(gltf.getBufferViews());
        for (Entry<String, BufferView> entry : bufferViews.entrySet())
        {
            String bufferViewId = entry.getKey();
            BufferView bufferView = entry.getValue();
            
            String bufferId = bufferView.getBuffer();
            BufferModel bufferModel = 
                get("buffers", bufferId, bufferModels);
            DefaultBufferViewModel bufferViewModel = 
                get("bufferViews", bufferViewId, bufferViewModels);
            bufferViewModel.setName(bufferView.getName());
            bufferViewModel.setBufferModel(bufferModel);
        }
    }
    
    /**
     * Compute all {@link AccessorModel} instances that refer to the
     * given {@link BufferViewModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The list of {@link AccessorModel} instances
     */
    private List<DefaultAccessorModel> computeAccessorModelsOf(
        BufferViewModel bufferViewModel)
    {
        List<DefaultAccessorModel> result = 
            new ArrayList<DefaultAccessorModel>();
        for (DefaultAccessorModel accessorModel : accessorModels)
        {
            BufferViewModel b = accessorModel.getBufferViewModel();
            if (bufferViewModel.equals(b))
            {
                result.add(accessorModel);
            }
        }
        return result;
    }
    
    /**
     * Computes the {@link AccessorModel#getByteStride() byte stride} of
     * the given {@link AccessorModel} instances. If the given instances
     * do not have the same byte stride, then a warning will be printed.
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The common byte stride
     */
    private static int computeCommonByteStride(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int commonByteStride = -1;
        for (AccessorModel accessorModel : accessorModels)
        {
            int byteStride = accessorModel.getByteStride();
            if (commonByteStride == -1)
            {
                commonByteStride = byteStride;
            }
            else
            {
                if (commonByteStride != byteStride)
                {
                    logger.warning("The accessor models do not have the "
                        + "same byte stride: " + commonByteStride 
                        + " and " + byteStride);
                }
            }
        }
        return commonByteStride;
    }
    

    /**
     * Set the {@link BufferViewModel#getByteStride() byte strides} of all
     * {@link BufferViewModel} instances, depending on the 
     * {@link AccessorModel} instances that refer to them
     */
    private void assignBufferViewByteStrides()
    {
        for (DefaultBufferViewModel bufferViewModel : bufferViewModels)
        {
            List<DefaultAccessorModel> accessorModelsOfBufferView = 
                computeAccessorModelsOf(bufferViewModel);
            if (accessorModelsOfBufferView.size() > 1)
            {
                int byteStride = 
                    computeCommonByteStride(accessorModelsOfBufferView);
                bufferViewModel.setByteStride(byteStride);
            }
        }
    }
    
    /**
     * Initialize the {@link MeshModel} instances
     */
    private void initMeshModels()
    {
        Map<String, Mesh> meshes = 
            Optionals.of(gltf.getMeshes());
        for (Entry<String, Mesh> entry : meshes.entrySet())
        {
            String meshId = entry.getKey();
            Mesh mesh = entry.getValue();
            List<MeshPrimitive> primitives = 
                Optionals.of(mesh.getPrimitives());
            DefaultMeshModel meshModel = 
                get("meshes", meshId, meshModels);
            meshModel.setName(mesh.getName());

            for (MeshPrimitive meshPrimitive : primitives)
            {
                MeshPrimitiveModel meshPrimitiveModel = 
                    createMeshPrimitiveModel(meshPrimitive);
                meshModel.addMeshPrimitiveModel(meshPrimitiveModel);
            }
        }
    }
    
    /**
     * Create a {@link MeshPrimitiveModel} for the given {@link MeshPrimitive}
     * 
     * @param meshPrimitive The {@link MeshPrimitive}
     * @return The {@link MeshPrimitiveModel}
     */
    private DefaultMeshPrimitiveModel createMeshPrimitiveModel(
        MeshPrimitive meshPrimitive)
    {
        Integer mode = meshPrimitive.getMode();
        if (mode == null)
        {
            mode = meshPrimitive.defaultMode();
        }
        DefaultMeshPrimitiveModel meshPrimitiveModel = 
            new DefaultMeshPrimitiveModel(mode);
        
        String indicesId = meshPrimitive.getIndices();
        if (indicesId != null)
        {
            AccessorModel indices = 
                get("accessors", indicesId, accessorModels);
            meshPrimitiveModel.setIndices(indices);
        }
        Map<String, String> attributes = 
            Optionals.of(meshPrimitive.getAttributes());
        for (Entry<String, String> entry : attributes.entrySet())
        {
            String attributeName = entry.getKey();
            String attributeId = entry.getValue();
            
            AccessorModel attribute = 
                get("accessors", attributeId, accessorModels);
            meshPrimitiveModel.putAttribute(attributeName, attribute);
        }
        
        String materialId = meshPrimitive.getMaterial();
        if (materialId == null ||
            GltfDefaults.isDefaultMaterialId(materialId))
        {
            meshPrimitiveModel.setMaterialModel(
                DefaultModels.getDefaultMaterialModel());
        }
        else
        {
            MaterialModel materialModel = 
                get("materials", materialId, materialModels);
            meshPrimitiveModel.setMaterialModel(materialModel);
        }
        
        return meshPrimitiveModel;
    }

    /**
     * Initialize the {@link NodeModel} instances
     */
    private void initNodeModels()
    {
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        for (Entry<String, Node> entry : nodes.entrySet())
        {
            String nodeId = entry.getKey();
            Node node = entry.getValue();
            
            DefaultNodeModel nodeModel = get("nodes", nodeId, nodeModels);
            nodeModel.setName(node.getName());
            
            List<String> childIds = Optionals.of(node.getChildren());
            for (String childId : childIds)
            {
                DefaultNodeModel child = get("nodes", childId, nodeModels);
                nodeModel.addChild(child);
            }
            List<String> meshIds = Optionals.of(node.getMeshes());
            for (String meshId : meshIds)
            {
                MeshModel meshModel = get("meshes", meshId, meshModels);
                nodeModel.addMeshModel(meshModel);
            }
            String skinId = node.getSkin();
            if (skinId != null)
            {
                SkinModel skinModel = get("skins", skinId, skinModels);
                nodeModel.setSkinModel(skinModel);
            }
            
            float matrix[] = node.getMatrix();
            float translation[] = node.getTranslation();
            float rotation[] = node.getRotation();
            float scale[] = node.getScale();
            nodeModel.setMatrix(Optionals.clone(matrix));
            nodeModel.setTranslation(Optionals.clone(translation));
            nodeModel.setRotation(Optionals.clone(rotation));
            nodeModel.setScale(Optionals.clone(scale));
        }
    }
    
    /**
     * Initialize the {@link SceneModel} instances
     */
    private void initSceneModels()
    {
        Map<String, Scene> scenes = Optionals.of(gltf.getScenes());
        for (Entry<String, Scene> entry : scenes.entrySet())
        {
            String sceneId = entry.getKey();
            Scene scene = entry.getValue();

            DefaultSceneModel sceneModel =
                get("scenes", sceneId, sceneModels);
            sceneModel.setName(scene.getName());
            
            List<String> nodes = Optionals.of(scene.getNodes());
            for (String nodeId : nodes)
            {
                NodeModel nodeModel = get("nodes", nodeId, nodeModels);
                sceneModel.addNode(nodeModel);
            }
        }
    }
    
    /**
     * Compute the mapping from joint names to the ID of the {@link Node} with
     * the respective {@link Node#getJointName() joint name}
     * 
     * @param gltf The {@link GlTF}
     * @return The mapping
     */
    private static Map<String, String> computeJointNameToNodeIdMap(GlTF gltf)
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        for (Entry<String, Node> entry : nodes.entrySet())
        {
            String nodeId = entry.getKey();
            Node node = entry.getValue();
            if (node.getJointName() != null)
            {
                String oldNodeId = map.put(node.getJointName(), nodeId);
                if (oldNodeId != null)
                {
                    logger.warning("Joint name " + node.getJointName()
                        + " is mapped to nodes with IDs " + nodeId + " and "
                        + oldNodeId);
                }
            }
        }
        return map;
    }

    /**
     * Initialize the {@link SkinModel} instances
     */
    private void initSkinModels()
    {
        Map<String, String> jointNameToNodeIdMap = 
            computeJointNameToNodeIdMap(gltf);
        Map<String, Skin> skins = Optionals.of(gltf.getSkins());
        for (Entry<String, Skin> entry : skins.entrySet())
        {
            String skinId = entry.getKey();
            Skin skin = entry.getValue();
            DefaultSkinModel skinModel = get("skins", skinId, skinModels);
            skinModel.setName(skin.getName());
            
            List<String> jointNames = skin.getJointNames();
            for (String jointName : jointNames)
            {
                String nodeId = jointNameToNodeIdMap.get(jointName);
                NodeModel nodeModel = get("nodes", nodeId, nodeModels);
                skinModel.addJoint(nodeModel);
            }
            
            String inverseBindMatricesId = skin.getInverseBindMatrices();
            DefaultAccessorModel inverseBindMatrices =
                get("accessors", inverseBindMatricesId, accessorModels);
            skinModel.setInverseBindMatrices(inverseBindMatrices);
        }
    }
    
    /**
     * Initialize the {@link TextureModel} instances
     */
    private void initTextureModels()
    {
        Map<String, Texture> textures = Optionals.of(gltf.getTextures());
        for (Entry<String, Texture> entry : textures.entrySet())
        {
            String textureId = entry.getKey();
            Texture texture = entry.getValue();
            DefaultTextureModel textureModel = 
                get("textures", textureId, textureModels);
            textureModel.setName(texture.getName());
            
            String imageId = texture.getSource();
            DefaultImageModel imageModel = 
                get("images", imageId, imageModels);
            textureModel.setImageModel(imageModel);
        }
    }
    
    /**
     * Initialize the {@link ShaderModel} instances
     */
    private void initShaderModels()
    {
        Map<String, Shader> shaders = Optionals.of(gltf.getShaders());
        for (Entry<String, Shader> entry : shaders.entrySet())
        {
            String shaderId = entry.getKey();
            Shader shader = entry.getValue();
            DefaultShaderModel shaderModel = 
                get("shaders", shaderId, shaderModels);
            shaderModel.setName(shader.getName());
            
            if (BinaryGltfV1.hasBinaryGltfExtension(shader))
            {
                String bufferViewId = 
                    BinaryGltfV1.getBinaryGltfBufferViewId(shader);
                BufferViewModel bufferViewModel = 
                    getBufferViewModelById(bufferViewId);
                shaderModel.setShaderData(bufferViewModel.getBufferViewData());
            }
            else
            {
                String uri = shader.getUri();
                if (IO.isDataUriString(uri))
                {
                    byte data[] = IO.readDataUri(uri);
                    ByteBuffer shaderData = Buffers.create(data);
                    shaderModel.setShaderData(shaderData);
                }
                else
                {
                    ByteBuffer shaderData = gltfAsset.getReferenceData(uri);
                    shaderModel.setShaderData(shaderData);
                }
            }
        }
    }
    
    /**
     * Initialize the {@link ProgramModel} instances
     */
    void initProgramModels()
    {
        Map<String, Program> programs = Optionals.of(gltf.getPrograms());
        for (Entry<String, Program> entry : programs.entrySet())
        {
            String programId = entry.getKey();
            Program program = entry.getValue();
            DefaultProgramModel programModel = 
                get("programs", programId, programModels);
            programModel.setName(program.getName());
            
            String vertexShaderId = program.getVertexShader();
            DefaultShaderModel vertexShaderModel =
                get("shaders", vertexShaderId, shaderModels);
            programModel.setVertexShaderModel(vertexShaderModel);
            
            String fragmentShaderId = program.getFragmentShader();
            DefaultShaderModel fragmentShaderModel =
                get("shaders", fragmentShaderId, shaderModels);
            programModel.setFragmentShaderModel(fragmentShaderModel);
        }
    }

    
    /**
     * Add all {@link TechniqueParametersModel} instances for the 
     * attributes of the given {@link Technique} to the given
     * {@link TechniqueModel}
     * 
     * @param technique The {@link Technique}
     * @param techniqueModel The {@link TechniqueModel}
     */
    private void addParameters(Technique technique,
        DefaultTechniqueModel techniqueModel)
    {
        Map<String, TechniqueParameters> parameters = 
            Optionals.of(technique.getParameters());
        for (Entry<String, TechniqueParameters> entry : parameters.entrySet())
        {
            String parameterName = entry.getKey();
            TechniqueParameters parameter = entry.getValue();
            
            int type = parameter.getType();
            int count = Optionals.of(parameter.getCount(), 1);
            String semantic = parameter.getSemantic();
            Object value = parameter.getValue();
            String nodeId = parameter.getNode();
            NodeModel nodeModel = null;
            if (nodeId != null)
            {
                nodeModel = get("nodes", nodeId, nodeModels);
            }
            
            TechniqueParametersModel techniqueParametersModel =
                new DefaultTechniqueParametersModel(
                    type, count, semantic, value, nodeModel);
            techniqueModel.addParameter(
                parameterName, techniqueParametersModel);
        }
    }

    /**
     * Add all attribute entries of the given {@link Technique} to the given
     * {@link TechniqueModel}
     * 
     * @param technique The {@link Technique}
     * @param techniqueModel The {@link TechniqueModel}
     */
    private static void addAttributes(Technique technique,
        DefaultTechniqueModel techniqueModel)
    {
        Map<String, String> attributes = 
            Optionals.of(technique.getAttributes());
        for (Entry<String, String> entry : attributes.entrySet())
        {
            String attributeName = entry.getKey();
            String parameterName = entry.getValue();
            techniqueModel.addAttribute(attributeName, parameterName);
        }
    }

    /**
     * Add all uniform entries of the given {@link Technique} to the given
     * {@link TechniqueModel}
     * 
     * @param technique The {@link Technique}
     * @param techniqueModel The {@link TechniqueModel}
     */
    private static void addUniforms(Technique technique,
        DefaultTechniqueModel techniqueModel)
    {
        Map<String, String> uniforms = 
            Optionals.of(technique.getUniforms());
        for (Entry<String, String> entry : uniforms.entrySet())
        {
            String uniformName = entry.getKey();
            String parameterName = entry.getValue();
            techniqueModel.addUniform(uniformName, parameterName);
        }
    }
    
    
    /**
     * Initialize the {@link TechniqueModel} instances
     */
    private void initTechniqueModels()
    {
        Map<String, Technique> techniques = Optionals.of(gltf.getTechniques());
        for (Entry<String, Technique> entry : techniques.entrySet())
        {
            String techniqueId = entry.getKey();
            Technique technique = entry.getValue();
            DefaultTechniqueModel techniqueModel = 
                get("techniques", techniqueId, techniqueModels);
            techniqueModel.setName(technique.getName());
            
            String programId = technique.getProgram();
            DefaultProgramModel programModel = 
                get("programs", programId, programModels);
            techniqueModel.setProgramModel(programModel);
            
            addParameters(technique, techniqueModel);
            addAttributes(technique, techniqueModel);
            addUniforms(technique, techniqueModel);
            
            List<Integer> enable = 
                Techniques.obtainEnabledStates(technique);
            TechniqueStatesFunctions functions = 
                Techniques.obtainTechniqueStatesFunctions(technique);

            TechniqueStatesFunctionsModel techniqueStatesFunctionsModel =
                new DefaultTechniqueStatesFunctionsModelV1(functions);
            TechniqueStatesModel techniqueStatesModel = 
                new DefaultTechniqueStatesModel(
                    enable, techniqueStatesFunctionsModel);
            techniqueModel.setTechniqueStatesModel(techniqueStatesModel);
        }
    }

    
    
    /**
     * Initialize the {@link MaterialModel} instances
     */
    private void initMaterialModels()
    {
        Map<String, Material> materials = Optionals.of(gltf.getMaterials());
        for (Entry<String, Material> entry : materials.entrySet())
        {
            String materialId = entry.getKey();
            Material material = entry.getValue();
            DefaultMaterialModel materialModel = 
                get("materials", materialId, materialModels);
            
            materialModel.setValues(material.getValues());
            materialModel.setName(material.getName());
            
            String techniqueId = material.getTechnique();
            if (techniqueId == null ||
                GltfDefaults.isDefaultTechniqueId(techniqueId))
            {
                materialModel.setTechniqueModel(
                    DefaultModels.getDefaultTechniqueModel());
            }
            else
            {
                DefaultTechniqueModel techniqueModel =
                    get("techniques", techniqueId, techniqueModels);
                materialModel.setTechniqueModel(techniqueModel);
            }
        }
    }



    /**
     * Create the {@link CameraModel} instances. This has to be be called
     * <b>after</b> the {@link #nodeModels} have been created: Each time
     * that a node refers to a camera, a new instance of this camera
     * has to be created.
     */
    private void instantiateCameraModels()
    {
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        Map<String, Camera> cameras = Optionals.of(gltf.getCameras());
        for (Entry<String, Node> entry : nodes.entrySet())
        {
            String nodeId = entry.getKey();
            Node node = entry.getValue();
            
            String cameraId = node.getCamera();
            if (cameraId != null)
            {
                Camera camera = cameras.get(cameraId);
                NodeModel nodeModel = get("nodes", nodeId, nodeModels);
                
                Function<float[], float[]> viewMatrixComputer = result -> 
                {
                    float localResult[] = Utils.validate(result, 16);
                    nodeModel.computeGlobalTransform(localResult);
                    MathUtils.invert4x4(localResult, localResult);
                    return localResult;
                };
                BiFunction<float[], Float, float[]> projectionMatrixComputer = 
                    (result, aspectRatio) -> 
                {
                    float localResult[] = Utils.validate(result, 16);
                    CamerasV1.computeProjectionMatrix(
                        camera, aspectRatio, localResult);
                    return localResult;
                };
                DefaultCameraModel cameraModel = new DefaultCameraModel(
                    viewMatrixComputer, projectionMatrixComputer);
                cameraModel.setName(camera.getName());
                
                cameraModel.setNodeModel(nodeModel);

                String nodeName = Optionals.of(node.getName(), nodeId);
                String cameraName = Optionals.of(camera.getName(), cameraId);
                String instanceName = nodeName + "." + cameraName;
                cameraModel.setInstanceName(instanceName);
                
                cameraModels.add(cameraModel);
            }
        }
    }
    
    @Override
    public List<AccessorModel> getAccessorModels()
    {
        return Collections.unmodifiableList(accessorModels);
    }
    
    @Override
    public List<AnimationModel> getAnimationModels()
    {
        return Collections.unmodifiableList(animationModels);
    }
    
    @Override
    public List<BufferModel> getBufferModels()
    {
        return Collections.unmodifiableList(bufferModels);
    }
    
    @Override
    public List<BufferViewModel> getBufferViewModels()
    {
        return Collections.unmodifiableList(bufferViewModels);
    }
    
    @Override
    public List<CameraModel> getCameraModels()
    {
        return Collections.unmodifiableList(cameraModels);
    }
    
    @Override
    public List<ImageModel> getImageModels()
    {
        return Collections.unmodifiableList(imageModels);
    }
    
    @Override
    public List<MaterialModel> getMaterialModels()
    {
        return Collections.unmodifiableList(materialModels);
    }
    
    @Override
    public List<NodeModel> getNodeModels()
    {
        return Collections.unmodifiableList(nodeModels);
    }
    
    @Override
    public List<SceneModel> getSceneModels()
    {
        return Collections.unmodifiableList(sceneModels);
    }

    @Override
    public List<TextureModel> getTextureModels()
    {
        return Collections.unmodifiableList(textureModels);
    }
    
    /**
     * Returns an unmodifiable view on the list of {@link ShaderModel} 
     * instances that have been created for the glTF.
     * 
     * @return The {@link ShaderModel} instances
     */
    public List<ShaderModel> getShaderModels()
    {
        return Collections.unmodifiableList(shaderModels);
    }

    /**
     * Returns the raw glTF object, which is a 
     * {@link de.javagl.jgltf.impl.v1.GlTF version 1.0 glTF}.<br>
     * <br>
     * This method should usually not be called by clients. It may be
     * omitted in future versions.
     * 
     * @return The glTF object
     */
    public GlTF getGltf()
    {
        return gltf;
    }
    
    /**
     * Return the element from the given list, based on the 
     * {@link #indexMappingSet} for the given name and ID. 
     * If the ID is <code>null</code>, then <code>null</code> is 
     * returned. If there is no proper index stored for the given
     * ID, then a warning will be printed and <code>null</code>
     * will be returned. If the index is not valid for the given
     * list, then a warning will be printed, and <code>null</code>
     * will be returned.
     * 
     * @param name The name
     * @param id The ID
     * @param list The list
     * @return The element
     */
    private <T> T get(String name, String id, List<T> list)
    {
        Integer index = indexMappingSet.getIndex(name, id);
        if (index == null)
        {
            logger.severe("No index found for " + name + " ID " + id);
            return null;
        }
        if (index < 0 || index >= list.size())
        {
            logger.severe("Index for " + name + " ID " + id + " is " + index
                + ", but must be in [0, " + list.size() + ")");
            return null;
        }
        T element = list.get(index);
        return element;
    }
    
    
}
