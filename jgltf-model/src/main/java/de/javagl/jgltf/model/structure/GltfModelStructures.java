/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.structure;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.AssetModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.CameraOrthographicModel;
import de.javagl.jgltf.model.CameraPerspectiveModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.ExtensionsModel;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.gl.ProgramModel;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.gl.TechniqueParametersModel;
import de.javagl.jgltf.model.gl.TechniqueStatesModel;
import de.javagl.jgltf.model.gl.impl.DefaultProgramModel;
import de.javagl.jgltf.model.gl.impl.DefaultShaderModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueParametersModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueStatesFunctionsModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueStatesModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;
import de.javagl.jgltf.model.impl.AbstractNamedModelElement;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultChannel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultSampler;
import de.javagl.jgltf.model.impl.DefaultAssetModel;
import de.javagl.jgltf.model.impl.DefaultCameraModel;
import de.javagl.jgltf.model.impl.DefaultCameraOrthographicModel;
import de.javagl.jgltf.model.impl.DefaultCameraPerspectiveModel;
import de.javagl.jgltf.model.impl.DefaultExtensionsModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultNormalTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultOcclusionTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultPbrMetallicRoughnessModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;
import de.javagl.jgltf.model.impl.DefaultTechniqueMaterialModel;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.v1.GltfModelV1;

/**
 * A class for modifying the structure of a glTF model.<br>
 * <br>
 * This class is not part of the public API!
 */
public class GltfModelStructures
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModelStructures.class.getName());
    
    /**
     * The source model
     */
    private DefaultGltfModel source;

    /**
     * The target model
     */
    private DefaultGltfModel target;
    
    /**
     * The mapping from accessor models of the source to the target
     */
    private Map<AccessorModel, DefaultAccessorModel> accessorModelsMap;
    
    /**
     * The mapping from animation models of the source to the target
     */
    private Map<AnimationModel, DefaultAnimationModel> animationModelsMap;
    
    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<CameraModel, DefaultCameraModel> cameraModelsMap;

    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<ImageModel, DefaultImageModel> imageModelsMap;

    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<MaterialModel, MaterialModel> materialModelsMap;

    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<MeshModel, DefaultMeshModel> meshModelsMap;

    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<NodeModel, DefaultNodeModel> nodeModelsMap;

    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<SceneModel, DefaultSceneModel> sceneModelsMap;

    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<SkinModel, DefaultSkinModel> skinModelsMap;

    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<TextureModel, DefaultTextureModel> textureModelsMap;
    
    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<ShaderModel, DefaultShaderModel> shaderModelsMap;
    
    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<ProgramModel, DefaultProgramModel> programModelsMap;
    
    /**
     * The mapping from elements in the source to elements in the target
     */
    private Map<TechniqueModel, DefaultTechniqueModel> techniqueModelsMap;
    
    /**
     * The mapping from ALL elements in the source to elements in the target
     */
    private Map<ModelElement, ModelElement> modelElementMap;

    
    /**
     * Default constructor
     */
    public GltfModelStructures()
    {
        // Default constructor
    }
    
    /**
     * Prepare generating a restructured version of the given glTF model.
     * 
     * @param sourceGltfModel The {@link GltfModel}
     */
    public void prepare(GltfModel sourceGltfModel)
    {
        this.source = (DefaultGltfModel) sourceGltfModel;
        
        GltfModelV1 sourceV1 = null;
        GltfModelV1 targetV1 = null;
        
        if (sourceGltfModel instanceof GltfModelV1) 
        {
            sourceV1 = (GltfModelV1) sourceGltfModel;
            targetV1 = new GltfModelV1();
            this.target = targetV1;
        }
        else
        {
            this.target = new DefaultGltfModel();
        }

        accessorModelsMap = 
            new LinkedHashMap<AccessorModel, DefaultAccessorModel>();
        copyAccessorModels(source, target);
        
        animationModelsMap = computeMapping(
            source.getAnimationModels(), 
            DefaultAnimationModel::new, 
            target::addAnimationModel);
        
        cameraModelsMap = computeMapping(
            source.getCameraModels(),
            DefaultCameraModel::new,
            target::addCameraModel);
            
        imageModelsMap = computeMapping(
            source.getImageModels(),
            DefaultImageModel::new,
            target::addImageModel);
            
        if (sourceV1 != null && targetV1 != null) 
        {
            materialModelsMap = computeMapping(
                source.getMaterialModels(),
                DefaultTechniqueMaterialModel::new,
                target::addMaterialModel);
        } 
        else
        {
            materialModelsMap = computeMapping(
                source.getMaterialModels(),
                DefaultPbrMaterialModel::new,
                target::addMaterialModel);
        }
            
        meshModelsMap = computeMapping(
            source.getMeshModels(),
            DefaultMeshModel::new,
            target::addMeshModel);
            
        nodeModelsMap = computeMapping(
            source.getNodeModels(),
            DefaultNodeModel::new,
            target::addNodeModel);
            
        sceneModelsMap = computeMapping(
            source.getSceneModels(),
            DefaultSceneModel::new,
            target::addSceneModel);
            
        skinModelsMap = computeMapping(
            source.getSkinModels(),
            DefaultSkinModel::new,
            target::addSkinModel);
            
        textureModelsMap = computeMapping(
            source.getTextureModels(),
            DefaultTextureModel::new,
            target::addTextureModel);

        modelElementMap = new LinkedHashMap<ModelElement, ModelElement>();
        modelElementMap.putAll(accessorModelsMap);
        modelElementMap.putAll(animationModelsMap);
        modelElementMap.putAll(cameraModelsMap);
        modelElementMap.putAll(imageModelsMap);
        modelElementMap.putAll(materialModelsMap);
        modelElementMap.putAll(meshModelsMap);
        modelElementMap.putAll(nodeModelsMap);
        modelElementMap.putAll(sceneModelsMap);
        modelElementMap.putAll(skinModelsMap);
        modelElementMap.putAll(textureModelsMap);
        
        if (sourceV1 != null && targetV1 != null) 
        {
            shaderModelsMap = computeMapping(
                sourceV1.getShaderModels(), 
                DefaultShaderModel::new, 
                targetV1::addShaderModel);
            programModelsMap = computeMapping(
                sourceV1.getProgramModels(), 
                DefaultProgramModel::new, 
                targetV1::addProgramModel);
            techniqueModelsMap = computeMapping(
                sourceV1.getTechniqueModels(), 
                DefaultTechniqueModel::new, 
                targetV1::addTechniqueModel);
            
            modelElementMap.putAll(shaderModelsMap);
            modelElementMap.putAll(programModelsMap);
            modelElementMap.putAll(techniqueModelsMap);
        }
        
        copyGltfPropertyElements(source, target);
        
        initAnimationModels();
        initImageModels();
        
        if (sourceV1 != null && targetV1 != null) 
        {
            initTechniqueModels();
        }
        
        initMeshModels();
        initNodeModels();
        initSceneModels();
        initSkinModels();
        initTextureModels();
        initMaterialModels();
        initCameraModels();
        
        if (sourceV1 != null && targetV1 != null) 
        {
            initShaderModels();
            initProgramModels();
        }
        
        initExtensionsModel();
        initAssetModel();
    }

    /**
     * Create a restructured version of the glTF model that was last given
     * to {@link #prepare(GltfModel)}.<br>
     * <br>
     * The resulting model will have a structure that is suitable for
     * writing it as a default glTF. None of its images will refer to 
     * a buffer view (so they will be stored via URIs, either as external 
     * files or data URIs) 
     * 
     * @return The restructured model
     */
    public DefaultGltfModel createDefault()
    {
        BufferBuilderConfig config = 
            new BufferBuilderConfig();
        return create(config);
    }
    
    /**
     * Create a restructured version of the glTF model that was last given
     * to {@link #prepare(GltfModel)}.<br>
     * <br>
     * The resulting model will have a structure that is suitable for
     * writing it as a binary glTF: It will have a single buffer, and
     * all images will refer to a buffer view within that buffer. 
     * 
     * @return The restructured model
     */
    public DefaultGltfModel createBinary()
    {
        BufferBuilderConfig config = 
            new BufferBuilderConfig();
        config.imagesInBufferViews = true;
        config.bufferViewPerAttributeAccessor = true;
        return create(config);
    }

    /**
     * Create a restructured version of the glTF model that was last given
     * to {@link #prepare(GltfModel)}.
     * 
     * @return The restructured model
     */
    public DefaultGltfModel createCustom()
    {
        BufferBuilderConfig config = 
            new BufferBuilderConfig();
        
        config.bufferForAnimations = true;
        config.bufferForMeshes = true;
        config.bufferPerMesh = true;
        config.bufferPerMeshPrimitive = true;
        config.bufferForAnimations = true;
        
        return create(config);
    }
    
    

    /**
     * Create a restructured version of the glTF model that was last given
     * to {@link #prepare(GltfModel)}.
     * 
     * @param config The configuration
     * @return The restructured model
     */
    private DefaultGltfModel create(BufferBuilderConfig config)
    {
        if (this.target == null)
        {
            throw new GltfException("The 'prepare' method has not been called");
        }
        Level level = Level.FINE;
        // @formatter:off
        if (logger.isLoggable(level)) 
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Creating model with configuration:\n");
            sb.append("  bufferPerMeshPrimitive : " + config.bufferPerMeshPrimitive + "\n");
            sb.append("  bufferPerMesh : " + config.bufferPerMesh + "\n");
            sb.append("  bufferForMeshes : " + config.bufferForMeshes + "\n");
            sb.append("  bufferPerAnimation : " + config.bufferPerAnimation + "\n");
            sb.append("  bufferForAnimations : " + config.bufferForAnimations + "\n");
            sb.append("  bufferPerSkin : " + config.bufferPerSkin + "\n");
            sb.append("  bufferForSkins : " + config.bufferForSkins + "\n");
            sb.append("  bufferPerImage : " + config.bufferPerImage + "\n");
            sb.append("  bufferForImages : " + config.bufferForImages + "\n");
            sb.append("  bufferForAdditionalAccessors : " + config.bufferForAdditionalAccessors + "\n");
            sb.append("  imagesInBufferViews : " + config.imagesInBufferViews + "\n");
            logger.log(level, sb.toString());
        }
        // @formatter:on
        
        BufferBuilderStrategy bbs = BufferBuilderStrategies.create(config);
        bbs.process(target);
        
        for (DefaultImageModel imageModel : imageModelsMap.values())
        {
            bbs.validateImageModel(imageModel);
        }
        target.addBufferViewModels(bbs.getBufferViewModels());
        target.addBufferModels(bbs.getBufferModels());
        
        DefaultExtensionsModel extensionsModel = target.getExtensionsModel();
        for (ModelElement modelElement : modelElementMap.values())
        {
            if (modelElement instanceof ExtensionModel)
            {
                ExtensionModel extensionModel = (ExtensionModel) modelElement;
                String extensionName = extensionModel.getExtensionName();
                extensionsModel.addExtensionUsed(extensionName);
                if (extensionModel.isRequired())
                {
                    extensionsModel.addExtensionRequired(extensionName);
                }
            }
        }
        
        DefaultGltfModel result = target;
        this.source = null;
        target = null;
        
        accessorModelsMap = null;
        animationModelsMap = null;
        cameraModelsMap = null;
        imageModelsMap = null;
        materialModelsMap = null;
        meshModelsMap = null;
        nodeModelsMap = null;
        sceneModelsMap = null;
        skinModelsMap = null;
        textureModelsMap = null;
        shaderModelsMap = null;
        programModelsMap = null;
        techniqueModelsMap = null;
        modelElementMap = null;
        
        return result;
    }
    
    /**
     * Copy the extensions and extras from the given source to
     * the given target
     * 
     * @param source The source
     * @param target The target
     */
    private void copyGltfPropertyElements(
        AbstractModelElement source, AbstractModelElement target)
    {
        target.setExtensions(source.getExtensions());
        target.setExtras(source.getExtras());
        copyExtensionModels(source, target);
    }

    /**
     * Copy the name and extensions and extras from the given source to
     * the given target
     * 
     * @param source The source
     * @param target The target
     */
    private void copyGltfChildOfRootPropertyElements(
        AbstractNamedModelElement source, 
        AbstractNamedModelElement target)
    {
        target.setName(source.getName());
        copyGltfPropertyElements(source, target);
    }
    
    
    /**
     * Copy all accessor models from the given source to the given target, 
     * and store the mapping from source to target elements.
     * 
     * @param source The source
     * @param target The target
     */
    private void copyAccessorModels(
        DefaultGltfModel source, DefaultGltfModel target)
    {
        List<AccessorModel> accessorModels = source.getAccessorModels();
        for (AccessorModel input : accessorModels)
        {
            DefaultAccessorModel output = copy((DefaultAccessorModel) input);
            target.addAccessorModel(output);
            accessorModelsMap.put(input, output);
        }
    }
    
    /**
     * Creates a copy of the given input model.<br>
     * <br>
     * This will return a copy that contains a reference to the same data
     * as the given one, but <i>without</i> an associated 
     * {@link BufferViewModel}.
     * 
     * @param input The input model
     * @return The copy
     */
    private DefaultAccessorModel copy(DefaultAccessorModel input)
    {
        AccessorData inputAccessorData = input.getAccessorData();
        ByteBuffer byteBuffer = inputAccessorData.createByteBuffer();
        int componentType = input.getComponentType();
        ElementType elementType = input.getElementType();
        int count = input.getCount();
        boolean normalized = input.isNormalized();
        DefaultAccessorModel output = new DefaultAccessorModel(
            componentType, count, elementType);
        output.setNormalized(normalized);
        output.setAccessorData(AccessorDatas.create(output, byteBuffer));
        
        copyGltfChildOfRootPropertyElements(input, output);
        return output;
    }

    
    /**
     * Initialize the {@link AnimationModel} instances
     */
    private void initAnimationModels()
    {
        List<AnimationModel> sourceAnimationModels = 
            source.getAnimationModels();
        for (int i = 0; i < sourceAnimationModels.size(); i++)
        {
            DefaultAnimationModel sourceAnimationModel = 
                (DefaultAnimationModel)sourceAnimationModels.get(i);
            DefaultAnimationModel targetAnimationModel = 
                animationModelsMap.get(sourceAnimationModel); 
            
            List<Channel> sourceChannels = 
                sourceAnimationModel.getChannels();
            for (Channel sourceChannel : sourceChannels)
            {
                Channel targetChannel = copyChannel(sourceChannel);
                targetAnimationModel.addChannel(targetChannel);
            }

            copyGltfChildOfRootPropertyElements(
                sourceAnimationModel, targetAnimationModel);
        }
    }
    
    /**
     * Create a copy of the given {@link Channel} object
     * 
     * @param sourceChannel The source {@link Channel}
     * @return The {@link Channel}
     */
    private Channel copyChannel(
        Channel sourceChannel)
    {
        // Copy the sampler
        Sampler sourceSampler = sourceChannel.getSampler();
        
        // Obtain the input/output for the copied sampler 
        AccessorModel sourceInput = sourceSampler.getInput();
        AccessorModel sourceOutput = sourceSampler.getOutput();
        DefaultAccessorModel targetInput = accessorModelsMap.get(sourceInput);
        DefaultAccessorModel targetOutput = accessorModelsMap.get(sourceOutput);
        
        Interpolation interpolation = sourceSampler.getInterpolation();
        Sampler targetSampler = new DefaultSampler(
            targetInput, interpolation, targetOutput);
        
        // Obtain the node for the copied channel
        NodeModel sourceNodeModel = sourceChannel.getNodeModel();
        DefaultNodeModel targetNodeModel = nodeModelsMap.get(sourceNodeModel);
        
        // Create the copy of the channel
        String path = sourceChannel.getPath();
        Channel targetChannel = new DefaultChannel(
            targetSampler, targetNodeModel, path);
        return targetChannel;
    }



    /**
     * Initialize the {@link MeshModel} instances
     */
    private void initMeshModels()
    {
        List<MeshModel> sourceMeshModels = source.getMeshModels();
        for (int i = 0; i < sourceMeshModels.size(); i++)
        {
            DefaultMeshModel sourceMeshModel = 
                (DefaultMeshModel)sourceMeshModels.get(i);
            DefaultMeshModel targetMeshModel = 
                meshModelsMap.get(sourceMeshModel);
            
            List<MeshPrimitiveModel> sourceMeshPrimitiveModels =
                sourceMeshModel.getMeshPrimitiveModels();
            for (MeshPrimitiveModel sourceMeshPrimitiveModel : 
                sourceMeshPrimitiveModels)
            {
                DefaultMeshPrimitiveModel targetMeshPrimitiveModel = 
                    copyMeshPrimitiveModel(
                        (DefaultMeshPrimitiveModel) sourceMeshPrimitiveModel);
                targetMeshModel.addMeshPrimitiveModel(targetMeshPrimitiveModel);
            }
            double[] weights = sourceMeshModel.getWeights();
            targetMeshModel.setWeights(Optionals.clone(weights));
            
            copyGltfChildOfRootPropertyElements(
                sourceMeshModel, targetMeshModel);
        }
    }
    
    /**
     * Create a copy of the given {@link MeshPrimitiveModel}.<br>
     * 
     * @param sourceMeshPrimitiveModel The source {@link MeshPrimitiveModel}
     * @return The copied {@link MeshPrimitiveModel}
     */
    private DefaultMeshPrimitiveModel copyMeshPrimitiveModel(
        DefaultMeshPrimitiveModel sourceMeshPrimitiveModel)
    {
        int mode = sourceMeshPrimitiveModel.getMode();
        DefaultMeshPrimitiveModel targetMeshPrimitiveModel =
            new DefaultMeshPrimitiveModel(mode);
        modelElementMap.put(sourceMeshPrimitiveModel, targetMeshPrimitiveModel);
        
        AccessorModel sourceIndices =
            sourceMeshPrimitiveModel.getIndices();
        DefaultAccessorModel targetIndices =
            accessorModelsMap.get(sourceIndices);
        targetMeshPrimitiveModel.setIndices(targetIndices);

        Map<String, AccessorModel> sourceAttributes =
            sourceMeshPrimitiveModel.getAttributes();
        for (Entry<String, AccessorModel> sourceEntry : 
            sourceAttributes.entrySet())
        {
            String name = sourceEntry.getKey();
            AccessorModel sourceAttribute = sourceEntry.getValue();
            DefaultAccessorModel targetAttribute =
                accessorModelsMap.get(sourceAttribute);
            targetMeshPrimitiveModel.putAttribute(name,
                targetAttribute);
        }
        
        List<Map<String, AccessorModel>> sourceTargets =
            sourceMeshPrimitiveModel.getTargets();
        for (Map<String, AccessorModel> sourceTarget : sourceTargets)
        {
            Map<String, AccessorModel> targetTarget =
                new LinkedHashMap<String, AccessorModel>();
            for (Entry<String, AccessorModel> sourceEntry : 
                sourceTarget.entrySet())
            {
                String name = sourceEntry.getKey();
                AccessorModel sourceTargetValue = sourceEntry.getValue();
                DefaultAccessorModel targetTargetValue =
                    accessorModelsMap.get(sourceTargetValue);
                targetTarget.put(name, targetTargetValue);
            }
            targetMeshPrimitiveModel.addTarget(targetTarget);
        }        
        
        MaterialModel sourceMaterial = 
            sourceMeshPrimitiveModel.getMaterialModel();
        MaterialModel targetMaterial = materialModelsMap.get(sourceMaterial);
        targetMeshPrimitiveModel.setMaterialModel(targetMaterial);
        
        copyGltfPropertyElements(
            sourceMeshPrimitiveModel, targetMeshPrimitiveModel);
        return targetMeshPrimitiveModel;
    }

    /**
     * Initialize the {@link NodeModel} instances
     */
    private void initNodeModels()
    {
        List<NodeModel> sourceNodeModels = source.getNodeModels();
        for (int i = 0; i < sourceNodeModels.size(); i++)
        {
            DefaultNodeModel sourceNodeModel = 
                (DefaultNodeModel) sourceNodeModels.get(i);
            DefaultNodeModel targetNodeModel = 
                nodeModelsMap.get(sourceNodeModel);
            
            List<NodeModel> sourceChildren = sourceNodeModel.getChildren();
            for (NodeModel sourceChild : sourceChildren)
            {
                DefaultNodeModel targetChild = nodeModelsMap.get(sourceChild);
                targetNodeModel.addChild(targetChild);
            }
            
            List<MeshModel> sourceMeshes = sourceNodeModel.getMeshModels();
            for (MeshModel sourceMesh : sourceMeshes)
            {
                DefaultMeshModel targetMesh = meshModelsMap.get(sourceMesh);
                targetNodeModel.addMeshModel(targetMesh);
            }
            
            SkinModel sourceSkin = sourceNodeModel.getSkinModel();
            DefaultSkinModel targetSkin = skinModelsMap.get(sourceSkin);
            targetNodeModel.setSkinModel(targetSkin);
            
            CameraModel sourceCamera = sourceNodeModel.getCameraModel();
            DefaultCameraModel targetCamera = cameraModelsMap.get(sourceCamera);
            targetNodeModel.setCameraModel(targetCamera);

            double matrix[] = sourceNodeModel.getMatrix();
            double translation[] = sourceNodeModel.getTranslation();
            double rotation[] = sourceNodeModel.getRotation();
            double scale[] = sourceNodeModel.getScale();
            double weights[] = sourceNodeModel.getWeights();
            
            targetNodeModel.setMatrix(Optionals.clone(matrix));
            targetNodeModel.setTranslation(Optionals.clone(translation));
            targetNodeModel.setRotation(Optionals.clone(rotation));
            targetNodeModel.setScale(Optionals.clone(scale));
            targetNodeModel.setWeights(Optionals.clone(weights));

            copyGltfChildOfRootPropertyElements(
                sourceNodeModel, targetNodeModel);            
        }
    }
    

    /**
     * Initialize the {@link SceneModel} instances
     */
    private void initSceneModels()
    {
        List<SceneModel> sourceSceneModels = source.getSceneModels();
        for (int i = 0; i < sourceSceneModels.size(); i++)
        {
            DefaultSceneModel sourceSceneModel = 
                (DefaultSceneModel) sourceSceneModels.get(i);
            DefaultSceneModel targetSceneModel = 
                sceneModelsMap.get(sourceSceneModel);
            
            List<NodeModel> sourceNodeModels = sourceSceneModel.getNodeModels();
            for (NodeModel sourceNodeModel : sourceNodeModels)
            {
                DefaultNodeModel targetNodeModel = 
                    nodeModelsMap.get(sourceNodeModel);
                targetSceneModel.addNode(targetNodeModel);
            }
            
            copyGltfChildOfRootPropertyElements(
                sourceSceneModel, targetSceneModel);            
        }
    }
    
    /**
     * Initialize the {@link SkinModel} instances
     */
    private void initSkinModels()
    {
        List<SkinModel> sourceSkinModels = source.getSkinModels();
        for (int i = 0; i < sourceSkinModels.size(); i++)
        {
            DefaultSkinModel sourceSkinModel = 
                (DefaultSkinModel) sourceSkinModels.get(i);
            DefaultSkinModel targetSkinModel = 
                skinModelsMap.get(sourceSkinModel);
            
            List<NodeModel> sourceJoints = sourceSkinModel.getJoints();
            for (NodeModel sourceJoint : sourceJoints)
            {
                DefaultNodeModel targetJoint = nodeModelsMap.get(sourceJoint);
                targetSkinModel.addJoint(targetJoint);
            }
            
            AccessorModel sourceInverseBindMatrices = 
                sourceSkinModel.getInverseBindMatrices();
            DefaultAccessorModel targetInverseBindMatrices = 
                accessorModelsMap.get(sourceInverseBindMatrices);
            targetSkinModel.setInverseBindMatrices(targetInverseBindMatrices);

            NodeModel sourceSkeleton = sourceSkinModel.getSkeleton();
            DefaultNodeModel targetSkeleton = nodeModelsMap.get(sourceSkeleton);
            targetSkinModel.setSkeleton(targetSkeleton);
            
            copyGltfChildOfRootPropertyElements(
                sourceSkinModel, targetSkinModel);
        }
    }
    
    /**
     * Initialize the {@link TextureModel} instances
     */
    private void initTextureModels()
    {
        List<TextureModel> sourceTextureModels = source.getTextureModels();
        for (int i = 0; i < sourceTextureModels.size(); i++)
        {
            DefaultTextureModel sourceTextureModel = 
                (DefaultTextureModel) sourceTextureModels.get(i);
            DefaultTextureModel targetTextureModel = 
                textureModelsMap.get(sourceTextureModel);
            
            targetTextureModel.setMagFilter(
                sourceTextureModel.getMagFilter());
            targetTextureModel.setMinFilter(
                sourceTextureModel.getMinFilter());
            targetTextureModel.setWrapS(
                sourceTextureModel.getWrapS());
            targetTextureModel.setWrapT(
                sourceTextureModel.getWrapT());
            
            ImageModel sourceImageModel = sourceTextureModel.getImageModel();
            DefaultImageModel targetImageModel = 
                imageModelsMap.get(sourceImageModel);
            targetTextureModel.setImageModel(targetImageModel);
            
            copyGltfChildOfRootPropertyElements(
                sourceTextureModel, targetTextureModel);
        }
    }
    
    /**
     * Initialize the {@link ImageModel} instances
     */
    private void initImageModels()
    {
        List<ImageModel> sourceImageModels = source.getImageModels();
        for (int i = 0; i < sourceImageModels.size(); i++)
        {
            DefaultImageModel sourceImageModel = 
                (DefaultImageModel) sourceImageModels.get(i);
            DefaultImageModel targetImageModel = 
                imageModelsMap.get(sourceImageModel);
            
            targetImageModel.setUri(sourceImageModel.getUri());
            targetImageModel.setMimeType(sourceImageModel.getMimeType());
            targetImageModel.setImageData(sourceImageModel.getImageData());
            
            copyGltfChildOfRootPropertyElements(
                sourceImageModel, targetImageModel);
        }
    }
    
    /**
     * Initialize the {@link TechniqueModel} instances
     */
    private void initTechniqueModels()
    {
        if (!(source instanceof GltfModelV1))
        {
            return;
        }
        GltfModelV1 sourceV1 = (GltfModelV1) source;

        List<TechniqueModel> sourceTechniqueModels = 
            sourceV1.getTechniqueModels();
        for (int i = 0; i < sourceTechniqueModels.size(); i++)
        {
            DefaultTechniqueModel sourceTechniqueModel = 
                (DefaultTechniqueModel) sourceTechniqueModels.get(i);
            DefaultTechniqueModel targetTechniqueModel = 
                techniqueModelsMap.get(sourceTechniqueModel);
            
            initTechniqueModel(sourceTechniqueModel, targetTechniqueModel);
            
            copyGltfChildOfRootPropertyElements(
                sourceTechniqueModel, targetTechniqueModel);
        }
    }

    /**
     * Initialize the given {@link TechniqueModel} based on the given
     * {@link TechniqueModel}
     * 
     * @param sourceTechniqueModel The source {@link TechniqueModel}
     * @param targetTechniqueModel The target {@link TechniqueModel}
     */
    private void initTechniqueModel(
        DefaultTechniqueModel sourceTechniqueModel,
        DefaultTechniqueModel targetTechniqueModel)
    {
        ProgramModel sourceProgramModel = 
            sourceTechniqueModel.getProgramModel();
        DefaultProgramModel targetProgramModel = 
            programModelsMap.get(sourceProgramModel);
        targetTechniqueModel.setProgramModel(targetProgramModel);
        
        Map<String, TechniqueParametersModel> sourceParameters =
            sourceTechniqueModel.getParameters();
        for (Entry<String, TechniqueParametersModel> entry : 
            sourceParameters.entrySet())
        {
            String parameterName = entry.getKey();
            TechniqueParametersModel sourceValue = entry.getValue();

            int type = sourceValue.getType();
            int count = sourceValue.getCount();
            String semantic = sourceValue.getSemantic();
            Object value = sourceValue.getValue();
            NodeModel sourceNodeModel = sourceValue.getNodeModel();
            NodeModel targetNodeModel = nodeModelsMap.get(sourceNodeModel);

            DefaultTechniqueParametersModel targetValue =
                new DefaultTechniqueParametersModel(type, count, semantic,
                    value, targetNodeModel);
            targetTechniqueModel.addParameter(parameterName, targetValue);
        }
        
        Map<String, String> sourceAttributes =
            sourceTechniqueModel.getAttributes();
        for (Entry<String, String> entry : sourceAttributes.entrySet())
        {
            String attributeName = entry.getKey();
            String parameterName = entry.getValue();
            targetTechniqueModel.addAttribute(attributeName, parameterName);
        }        
        
        Map<String, String> sourceUniforms =
            sourceTechniqueModel.getUniforms();
        for (Entry<String, String> entry : sourceUniforms.entrySet())
        {
            String uniformName = entry.getKey();
            String parameterName = entry.getValue();
            targetTechniqueModel.addUniform(uniformName, parameterName);
        }        
        
        TechniqueStatesModel sourceTechniqueStatesModel =
            sourceTechniqueModel.getTechniqueStatesModel();
        if (sourceTechniqueStatesModel != null)
        {
            List<Integer> targetEnable = null;
            List<Integer> sourceEnable = sourceTechniqueStatesModel.getEnable();
            if (sourceEnable != null)
            {
                targetEnable = new ArrayList<Integer>(sourceEnable);
            }
            DefaultTechniqueStatesFunctionsModel 
                targetTechniqueStatesFunctionsModel =
                    new DefaultTechniqueStatesFunctionsModel();

            DefaultTechniqueStatesModel targetTechniqueStatesModel =
                new DefaultTechniqueStatesModel(targetEnable,
                    targetTechniqueStatesFunctionsModel);
            targetTechniqueModel.setTechniqueStatesModel(
                targetTechniqueStatesModel);
        }
    }


    /**
     * Initialize the {@link MaterialModel} instances
     */
    private void initMaterialModels() 
    {
        if (target instanceof GltfModelV1)
        {
            initMaterialModelsV1();
        } 
        else
        {
            initMaterialModelsV2();
        }
    }

    /**
     * Initialize the {@link MaterialModel} instances
     */
    private void initMaterialModelsV1()
    {
        List<MaterialModel> sourceMaterialModels = source.getMaterialModels();
        for (int i = 0; i < sourceMaterialModels.size(); i++)
        {
            DefaultTechniqueMaterialModel sourceMaterialModel = 
                (DefaultTechniqueMaterialModel) sourceMaterialModels.get(i);
            DefaultTechniqueMaterialModel targetMaterialModel = 
                (DefaultTechniqueMaterialModel) materialModelsMap.get(
                    sourceMaterialModel);
            
            initMaterialModel(sourceMaterialModel, targetMaterialModel);
            
            copyGltfChildOfRootPropertyElements(
                sourceMaterialModel, targetMaterialModel);            
        }
    }
    
    /**
     * Initialize the given {@link DefaultTechniqueMaterialModel} based on the 
     * given {@link DefaultTechniqueMaterialModel}
     * 
     * @param sourceMaterialModel The source 
     * {@link DefaultTechniqueMaterialModel}
     * @param targetMaterialModel The target 
     * {@link DefaultTechniqueMaterialModel}
     */
    private void initMaterialModel(
        DefaultTechniqueMaterialModel sourceMaterialModel,
        DefaultTechniqueMaterialModel targetMaterialModel)
    {
        TechniqueModel sourceTechniqueModel = 
            sourceMaterialModel.getTechniqueModel();
        TechniqueModel targetTechniqueModel =
            techniqueModelsMap.get(sourceTechniqueModel);
        targetMaterialModel.setTechniqueModel(targetTechniqueModel);
        
        
        Map<String, Object> sourceValues = sourceMaterialModel.getValues();
        Map<String, Object> targetValues = new LinkedHashMap<String, Object>();
        Map<String, TechniqueParametersModel> 
        sourceParameters = sourceTechniqueModel.getParameters();
        for (Entry<String, Object> entry : sourceValues.entrySet())
        {
            String parameterName = entry.getKey();
            Object sourceValue = entry.getValue();
            TechniqueParametersModel sourceTechniqueParametersModel = 
                sourceParameters.get(parameterName);
            if (sourceTechniqueParametersModel != null &&
                sourceTechniqueParametersModel.getType() == 
                    GltfConstants.GL_SAMPLER_2D)
            {
                if (sourceValue instanceof TextureModel)
                {
                    TextureModel targetValue = 
                        textureModelsMap.get(sourceValue);
                    targetValues.put(parameterName, targetValue);
                }
            }
            else
            {
                targetValues.put(parameterName, entry.getValue());
            }
        }
        targetMaterialModel.setValues(targetValues);        
    }


    /**
     * Initialize the {@link MaterialModel} instances
     */
    private void initMaterialModelsV2()
    {
        List<MaterialModel> sourceMaterialModels = source.getMaterialModels();
        for (int i = 0; i < sourceMaterialModels.size(); i++)
        {
            DefaultPbrMaterialModel sourceMaterialModel = 
                (DefaultPbrMaterialModel) sourceMaterialModels.get(i);
            DefaultPbrMaterialModel targetMaterialModel = 
                (DefaultPbrMaterialModel) materialModelsMap.get(
                    sourceMaterialModel);
            
            initMaterialModel(sourceMaterialModel, targetMaterialModel);
            
            copyGltfChildOfRootPropertyElements(
                sourceMaterialModel, targetMaterialModel);            
        }
    }
    
    /**
     * Initialize the given {@link DefaultPbrMaterialModel} based on the given
     * {@link DefaultPbrMaterialModel}
     * 
     * @param sourceMaterial The source {@link DefaultPbrMaterialModel}
     * @param targetMaterial The target {@link DefaultPbrMaterialModel}
     */
    private void initMaterialModel(
        PbrMaterialModel sourceMaterial, 
        DefaultPbrMaterialModel targetMaterial)
    {
        targetMaterial.setAlphaMode(sourceMaterial.getAlphaMode());
        targetMaterial.setAlphaCutoff(sourceMaterial.getAlphaCutoff());

        targetMaterial.setDoubleSided(sourceMaterial.isDoubleSided());

        DefaultPbrMetallicRoughnessModel sourcePbrMetallicRoughness =
            (DefaultPbrMetallicRoughnessModel) sourceMaterial
                .getPbrMetallicRoughnessModel();
        DefaultPbrMetallicRoughnessModel targetPbrMetallicRoughness =
            copyPbrMetallicRoughnessModel(sourcePbrMetallicRoughness);
        targetMaterial.setPbrMetallicRoughnessModel(
            targetPbrMetallicRoughness);

        DefaultNormalTextureInfoModel sourceNormalTextureInfo =
            (DefaultNormalTextureInfoModel) sourceMaterial
                .getNormalTextureInfoModel();
        DefaultNormalTextureInfoModel targetNormalTextureInfo =
            copyNormalTextureInfoModel(sourceNormalTextureInfo);
        targetMaterial.setNormalTextureInfoModel(targetNormalTextureInfo);

        DefaultOcclusionTextureInfoModel sourceOcclusionTextureInfo =
            (DefaultOcclusionTextureInfoModel) sourceMaterial
            .getOcclusionTextureInfoModel();
        DefaultOcclusionTextureInfoModel targetOcclusionTextureInfo =
            copyOcclusionTextureInfoModel(sourceOcclusionTextureInfo);
        targetMaterial.setOcclusionTextureInfoModel(
            targetOcclusionTextureInfo);

        DefaultTextureInfoModel sourceEmissiveTextureInfo =
            (DefaultTextureInfoModel) sourceMaterial
            .getEmissiveTextureInfoModel();
        DefaultTextureInfoModel targetEmissiveTextureInfo =
            copyTextureInfoModel(sourceEmissiveTextureInfo);
        targetMaterial.setEmissiveTextureInfoModel(
            targetEmissiveTextureInfo);

        double emissiveFactor[] = sourceMaterial.getEmissiveFactor();
        targetMaterial.setEmissiveFactor(Optionals.clone(emissiveFactor));
    }

    /**
     * Create a new {@link DefaultPbrMetallicRoughnessModel} based on the given
     * {@link DefaultPbrMetallicRoughnessModel}, or <code>null</code> if the
     * given source object was <code>null</code>.
     * 
     * @param sourcePbrMetallicRoughness The source 
     * {@link DefaultPbrMetallicRoughnessModel}
     * @return The resulting {@link DefaultPbrMetallicRoughnessModel}
     */
    private DefaultPbrMetallicRoughnessModel copyPbrMetallicRoughnessModel(
        DefaultPbrMetallicRoughnessModel sourcePbrMetallicRoughness)
    {
        if (sourcePbrMetallicRoughness == null)
        {
            return null;
        }
        DefaultPbrMetallicRoughnessModel targetPbrMetallicRoughness =
            new DefaultPbrMetallicRoughnessModel();

        double[] sourceBaseColorFactor = 
            sourcePbrMetallicRoughness.getBaseColorFactor();
        targetPbrMetallicRoughness.setBaseColorFactor(
            Optionals.clone(sourceBaseColorFactor));
        
        DefaultTextureInfoModel sourceBaseColorTextureInfo =
            (DefaultTextureInfoModel) sourcePbrMetallicRoughness
            .getBaseColorTextureInfoModel();
        DefaultTextureInfoModel targetBaseColorTextureInfo = 
            copyTextureInfoModel(sourceBaseColorTextureInfo);
        targetPbrMetallicRoughness.setBaseColorTextureInfoModel(
            targetBaseColorTextureInfo);
        
        targetPbrMetallicRoughness.setMetallicFactor(
            sourcePbrMetallicRoughness.getMetallicFactor());
        targetPbrMetallicRoughness.setRoughnessFactor(
            sourcePbrMetallicRoughness.getRoughnessFactor());
        
        DefaultTextureInfoModel sourceMetallicRoughnessTextureInfo =
            (DefaultTextureInfoModel) sourcePbrMetallicRoughness
            .getMetallicRoughnessTextureInfoModel();
        DefaultTextureInfoModel targetMetallicRoughnessTextureInfo =
            copyTextureInfoModel(sourceMetallicRoughnessTextureInfo);
        targetPbrMetallicRoughness.setMetallicRoughnessTextureInfoModel(
            targetMetallicRoughnessTextureInfo);
        
        copyGltfPropertyElements(
            sourcePbrMetallicRoughness, targetPbrMetallicRoughness);            
        return targetPbrMetallicRoughness;
    }
    
    /**
     * Copy the given {@link DefaultTextureInfoModel}, returning 
     * <code>null</code> if the given source was <code>null</code>.
     * 
     * @param sourceTextureInfo The source {@link DefaultTextureInfoModel}
     * @return The copied {@link DefaultTextureInfoModel}
     */
    private DefaultTextureInfoModel copyTextureInfoModel(
        DefaultTextureInfoModel sourceTextureInfo)
    {
        if (sourceTextureInfo == null)
        {
            return null;
        }
        DefaultTextureInfoModel targetTextureInfo =
            new DefaultTextureInfoModel();

        TextureModel sourceTextureModel =
            sourceTextureInfo.getTextureModel();
        DefaultTextureModel targetEmissiveTexture =
            textureModelsMap.get(sourceTextureModel);
        targetTextureInfo.setTextureModel(targetEmissiveTexture);
        targetTextureInfo.setTexCoord(
            sourceTextureInfo.getTexCoord());
        
        copyGltfPropertyElements(
            sourceTextureInfo, targetTextureInfo);            
        return targetTextureInfo;
    }


    /**
     * Copy the given {@link DefaultNormalTextureInfoModel}, returning 
     * <code>null</code> if the given source was <code>null</code>.
     * 
     * @param sourceTextureInfo The source {@link DefaultNormalTextureInfoModel}
     * @return The copied {@link DefaultNormalTextureInfoModel}
     */
    private DefaultNormalTextureInfoModel copyNormalTextureInfoModel(
        DefaultNormalTextureInfoModel sourceTextureInfo)
    {
        if (sourceTextureInfo == null)
        {
            return null;
        }
        DefaultNormalTextureInfoModel targetTextureInfo =
            new DefaultNormalTextureInfoModel();
        
        TextureModel sourceTextureModel =
            sourceTextureInfo.getTextureModel();
        DefaultTextureModel targetEmissiveTexture =
            textureModelsMap.get(sourceTextureModel);
        targetTextureInfo.setTextureModel(targetEmissiveTexture);
        targetTextureInfo.setTexCoord(
            sourceTextureInfo.getTexCoord());
        targetTextureInfo.setScale(sourceTextureInfo.getScale());
        
        copyGltfPropertyElements(
            sourceTextureInfo, targetTextureInfo);            
        return targetTextureInfo;
    }

    /**
     * Copy the given {@link DefaultOcclusionTextureInfoModel}, returning 
     * <code>null</code> if the given source was <code>null</code>.
     * 
     * @param sourceTextureInfo The source 
     * {@link DefaultOcclusionTextureInfoModel}
     * @return The copied {@link DefaultOcclusionTextureInfoModel}
     */
    private DefaultOcclusionTextureInfoModel copyOcclusionTextureInfoModel(
        DefaultOcclusionTextureInfoModel sourceTextureInfo)
    {
        if (sourceTextureInfo == null)
        {
            return null;
        }
        DefaultOcclusionTextureInfoModel targetTextureInfo =
            new DefaultOcclusionTextureInfoModel();
        
        TextureModel sourceTextureModel =
            sourceTextureInfo.getTextureModel();
        DefaultTextureModel targetEmissiveTexture =
            textureModelsMap.get(sourceTextureModel);
        targetTextureInfo.setTextureModel(targetEmissiveTexture);
        targetTextureInfo.setTexCoord(
            sourceTextureInfo.getTexCoord());
        targetTextureInfo.setStrength(sourceTextureInfo.getStrength());
        
        copyGltfPropertyElements(
            sourceTextureInfo, targetTextureInfo);            
        return targetTextureInfo;
    }

    
    
    /**
     * Initialize the {@link CameraModel} instances
     */
    private void initCameraModels()
    {
        List<CameraModel> sourceCameraModels = source.getCameraModels();
        for (int i = 0; i < sourceCameraModels.size(); i++)
        {
            DefaultCameraModel sourceCameraModel = 
                (DefaultCameraModel) sourceCameraModels.get(i);
            DefaultCameraModel targetCameraModel = 
                cameraModelsMap.get(sourceCameraModel);
            
            CameraPerspectiveModel sourceCameraPerspectiveModel = 
                sourceCameraModel.getCameraPerspectiveModel();
            if (sourceCameraPerspectiveModel != null) 
            {
                DefaultCameraPerspectiveModel targetCameraPerspectiveModel = 
                    new DefaultCameraPerspectiveModel();
                targetCameraPerspectiveModel.setAspectRatio(
                    sourceCameraPerspectiveModel.getAspectRatio());
                targetCameraPerspectiveModel.setYfov(
                    sourceCameraPerspectiveModel.getYfov());
                targetCameraPerspectiveModel.setZfar(
                    sourceCameraPerspectiveModel.getZfar());
                targetCameraPerspectiveModel.setZnear(
                    sourceCameraPerspectiveModel.getZnear());
                
                targetCameraModel.setCameraPerspectiveModel(
                    targetCameraPerspectiveModel);
            }
            
            CameraOrthographicModel sourceCameraOrthographicModel = 
                sourceCameraModel.getCameraOrthographicModel();
            if (sourceCameraOrthographicModel != null) 
            {
                DefaultCameraOrthographicModel targetCameraOrthographicModel = 
                    new DefaultCameraOrthographicModel();
                
                targetCameraOrthographicModel.setXmag(
                    sourceCameraOrthographicModel.getXmag());
                targetCameraOrthographicModel.setYmag(
                    sourceCameraOrthographicModel.getYmag());
                targetCameraOrthographicModel.setZfar(
                    sourceCameraOrthographicModel.getZfar());
                targetCameraOrthographicModel.setZnear(
                    sourceCameraOrthographicModel.getZnear());
                
                targetCameraModel.setCameraOrthographicModel(
                    targetCameraOrthographicModel);
            }
            
            copyGltfChildOfRootPropertyElements(
                sourceCameraModel, targetCameraModel);
        }
    }
    
    
    /**
     * Initialize the {@link ShaderModel} instances
     */
    private void initShaderModels()
    {
        if (!(source instanceof GltfModelV1))
        {
            return;
        }
        GltfModelV1 sourceV1 = (GltfModelV1) source;
        
        List<ShaderModel> sourceShaderModels = sourceV1.getShaderModels();
        for (int i = 0; i < sourceShaderModels.size(); i++)
        {
            DefaultShaderModel sourceShaderModel = 
                (DefaultShaderModel) sourceShaderModels.get(i);
            DefaultShaderModel targetShaderModel = 
                shaderModelsMap.get(sourceShaderModel);
            
            targetShaderModel.setUri(sourceShaderModel.getUri());
            targetShaderModel.setShaderType(sourceShaderModel.getShaderType());
            targetShaderModel.setShaderData(sourceShaderModel.getShaderData());
            
            copyGltfChildOfRootPropertyElements(
                sourceShaderModel, targetShaderModel);
        }
    }

    /**
     * Initialize the {@link ProgramModel} instances
     */
    private void initProgramModels()
    {
        if (!(source instanceof GltfModelV1))
        {
            return;
        }
        GltfModelV1 sourceV1 = (GltfModelV1) source;
        
        List<ProgramModel> sourceProgramModels = sourceV1.getProgramModels();
        for (int i = 0; i < sourceProgramModels.size(); i++)
        {
            DefaultProgramModel sourceProgramModel = 
                (DefaultProgramModel) sourceProgramModels.get(i);
            DefaultProgramModel targetProgramModel = 
                programModelsMap.get(sourceProgramModel);

            ShaderModel sourceVertexShaderModel = 
                sourceProgramModel.getVertexShaderModel();
            ShaderModel targetVertexShaderModel =
                shaderModelsMap.get(sourceVertexShaderModel);
            targetProgramModel.setVertexShaderModel(
                targetVertexShaderModel);
            
            ShaderModel sourceFragmentShaderModel = 
                sourceProgramModel.getFragmentShaderModel();
            ShaderModel targetFragmentShaderModel =
                shaderModelsMap.get(sourceFragmentShaderModel);
            targetProgramModel.setFragmentShaderModel(
                targetFragmentShaderModel);
            
            
            List<String> sourceAttributes = sourceProgramModel.getAttributes();
            for (String attribute : sourceAttributes)
            {
                targetProgramModel.addAttribute(attribute);
            }
            
            copyGltfChildOfRootPropertyElements(
                sourceProgramModel, targetProgramModel);
        }
    }
    
    /**
     * Initialize the {@link ExtensionsModel} with the extensions that
     * are used or required in the glTF.
     */
    private void initExtensionsModel() 
    {
        DefaultExtensionsModel sourceExtensionsModel = 
            source.getExtensionsModel();
        DefaultExtensionsModel targetExtensionsModel =
            target.getExtensionsModel();

        List<String> extensionsUsed = 
            sourceExtensionsModel.getExtensionsUsed();
        targetExtensionsModel.addExtensionsUsed(extensionsUsed);

        List<String> extensionsRequired = 
            sourceExtensionsModel.getExtensionsRequired();
        targetExtensionsModel.addExtensionsRequired(extensionsRequired);
    }

    /**
     * Initialize the {@link AssetModel} with the asset information that
     * was given in the glTF.
     */
    private void initAssetModel() 
    {
        DefaultAssetModel sourceAssetModel = source.getAssetModel();
        DefaultAssetModel targetAssetModel = target.getAssetModel();
        targetAssetModel.setCopyright(sourceAssetModel.getCopyright());
        targetAssetModel.setGenerator(sourceAssetModel.getGenerator());
        copyGltfChildOfRootPropertyElements(
            sourceAssetModel, targetAssetModel);
    }

    /**
     * Compute the set of all class objects that represent interfaces that
     * are implemented by the given class or any of its superclasses, and
     * that are assignable to {@link ModelElement}
     * 
     * @param c The class
     * @return The resulting types
     */
    private static Set<Class<?>> computeModelElementInterfaceTypes(Class<?> c)
    {
        Set<Class<?>> types = new LinkedHashSet<Class<?>>();
        computeModelElementInterfaceTypes(c, types);
        return types;
    }
    
    /**
     * Implementation for {@link #computeModelElementInterfaceTypes(Class)}
     * 
     * @param c The class
     * @param types The resulting types
     */
    private static void computeModelElementInterfaceTypes(
        Class<?> c, Set<Class<?>> types)
    {
        if (c.isInterface() && ModelElement.class.isAssignableFrom(c))
        {
            types.add(c);
        }
        Class<?> superclass = c.getSuperclass();
        if (superclass != null)
        {
            computeModelElementInterfaceTypes(superclass, types);
        }
        Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> i : interfaces)
        {
            computeModelElementInterfaceTypes(i, types);
        }
    }
    
    /**
     * Copy all extension model objects that are stored in the given source
     * model element into the target model element.
     * 
     * @param sourceElement The source {@link ModelElement}
     * @param targetElement The target {@link ModelElement}
     */
    private void copyExtensionModels(
        ModelElement sourceElement, ModelElement targetElement)
    {
        Class<? extends ModelElement> sourceType = sourceElement.getClass();
        Set<Class<?>> types = computeModelElementInterfaceTypes(sourceType);
        for (Class<?> type : types)
        {
            logger.fine("Copying extension models based on type " + type);
            copyExtensionModels(sourceElement, targetElement, type);
        }
    }
    
    /**
     * Copy all extension model objects that are stored in the given source
     * model element into the target model element, based on the given
     * model class
     * 
     * @param sourceElement The source {@link ModelElement}
     * @param targetElement The target {@link ModelElement}
     * @param modelClass The model class
     */
    private void copyExtensionModels(ModelElement sourceElement,
        ModelElement targetElement, Class<?> modelClass)
    {
        if (!(targetElement instanceof AbstractModelElement))
        {
            logger.warning("The target element is not an AbstractModelElement");
            return;
        }
        AbstractModelElement targetModelElement = 
            (AbstractModelElement) targetElement;
        ExtensionModels.copyExtensionModels(source, sourceElement,
            targetModelElement, modelClass, modelElementMap);
    }
    
    /**
     * Create a mapping from each source element to a newly created target
     * element, passing the target elements to the given consumer.
     * 
     * @param <S> The source element type
     * @param <T> The target element type
     * @param sources The sources
     * @param supplier The supplier for the target elements
     * @param consumer The consumer of the target elements
     * @return The mapping
     */
    private static <S, T> Map<S, T> computeMapping(
        Iterable<? extends S> sources, 
        Supplier<? extends T> supplier, 
        Consumer<? super T> consumer)
    {
        Map<S, T> map = new LinkedHashMap<S, T>();
        for (S s : sources)
        {
            T t = supplier.get();
            consumer.accept(t);
            map.put(s, t);
        }
        return map;
    }
    
}
