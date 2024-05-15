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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureModel;
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
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v1.MaterialModelV1;
import de.javagl.jgltf.model.v2.MaterialModelV2;

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
        copyGltfPropertyElements(source, target);

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
                MaterialModelV1::new,
                target::addMaterialModel);
        } 
        else
        {
            materialModelsMap = computeMapping(
                source.getMaterialModels(),
                MaterialModelV2::new,
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
        }
        
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
        DefaultBufferBuilderStrategy.Config config = 
            new DefaultBufferBuilderStrategy.Config();
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
        DefaultBufferBuilderStrategy.Config config = 
            new DefaultBufferBuilderStrategy.Config();
        config.imagesInBufferViews = true;
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
        DefaultBufferBuilderStrategy.Config config = 
            new DefaultBufferBuilderStrategy.Config();
        
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
    private DefaultGltfModel create(DefaultBufferBuilderStrategy.Config config)
    {
        if (this.target == null)
        {
            throw new GltfException("The 'prepare' method has not bee called");
        }
        Level level = Level.FINE;
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
        
        BufferBuilderStrategy bbs = BufferBuilderStrategies.create(config);
        bbs.process(target);
        
        for (DefaultImageModel imageModel : imageModelsMap.values())
        {
            bbs.validateImageModel(imageModel);
        }
        target.addBufferViewModels(bbs.getBufferViewModels());
        target.addBufferModels(bbs.getBufferModels());
        
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
        
        return result;
        
    }
    
    
    /**
     * Copy the extensions and extras from the given source to
     * the given target
     * 
     * @param source The source
     * @param target The target
     */
    private static void copyGltfPropertyElements(
        AbstractModelElement source, AbstractModelElement target)
    {
        target.setExtensions(source.getExtensions());
        target.setExtras(source.getExtras());
    }
    
    /**
     * Copy the name and extensions and extras from the given source to
     * the given target
     * 
     * @param source The source
     * @param target The target
     */
    private static void copyGltfChildOfRootPropertyElements(
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
    private static DefaultAccessorModel copy(DefaultAccessorModel input)
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
            copyGltfChildOfRootPropertyElements(
                sourceAnimationModel, targetAnimationModel);
            
            List<Channel> sourceChannels = 
                sourceAnimationModel.getChannels();
            for (Channel sourceChannel : sourceChannels)
            {
                Channel targetChannel = copyChannel(sourceChannel);
                targetAnimationModel.addChannel(targetChannel);
            }
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
            copyGltfChildOfRootPropertyElements(
                sourceMeshModel, targetMeshModel);
            
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
            float[] weights = sourceMeshModel.getWeights();
            targetMeshModel.setWeights(Optionals.clone(weights));
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
        copyGltfPropertyElements(
            sourceMeshPrimitiveModel, targetMeshPrimitiveModel);
        
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
            
            copyGltfChildOfRootPropertyElements(
                sourceNodeModel, targetNodeModel);            
            
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

            float matrix[] = sourceNodeModel.getMatrix();
            float translation[] = sourceNodeModel.getTranslation();
            float rotation[] = sourceNodeModel.getRotation();
            float scale[] = sourceNodeModel.getScale();
            float weights[] = sourceNodeModel.getWeights();
            
            targetNodeModel.setMatrix(Optionals.clone(matrix));
            targetNodeModel.setTranslation(Optionals.clone(translation));
            targetNodeModel.setRotation(Optionals.clone(rotation));
            targetNodeModel.setScale(Optionals.clone(scale));
            targetNodeModel.setWeights(Optionals.clone(weights));
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
            
            copyGltfChildOfRootPropertyElements(
                sourceSceneModel, targetSceneModel);            
            
            List<NodeModel> sourceNodeModels = sourceSceneModel.getNodeModels();
            for (NodeModel sourceNodeModel : sourceNodeModels)
            {
                DefaultNodeModel targetNodeModel = 
                    nodeModelsMap.get(sourceNodeModel);
                targetSceneModel.addNode(targetNodeModel);
            }
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
            copyGltfChildOfRootPropertyElements(
                sourceSkinModel, targetSkinModel);
            
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
            copyGltfChildOfRootPropertyElements(
                sourceTextureModel, targetTextureModel);
            
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
            copyGltfChildOfRootPropertyElements(
                sourceImageModel, targetImageModel);
            
            targetImageModel.setUri(sourceImageModel.getUri());
            targetImageModel.setMimeType(sourceImageModel.getMimeType());
            targetImageModel.setImageData(sourceImageModel.getImageData());
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
            copyGltfChildOfRootPropertyElements(
                sourceTechniqueModel, targetTechniqueModel);
            
            initTechniqueModel(sourceTechniqueModel, targetTechniqueModel);
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
            MaterialModelV1 sourceMaterialModel = 
                (MaterialModelV1) sourceMaterialModels.get(i);
            MaterialModelV1 targetMaterialModel = 
                (MaterialModelV1) materialModelsMap.get(sourceMaterialModel);
            
            copyGltfChildOfRootPropertyElements(
                sourceMaterialModel, targetMaterialModel);            
            initMaterialModel(sourceMaterialModel, targetMaterialModel);
        }
    }
    
    /**
     * Initialize the given {@link MaterialModelV1} based on the given
     * {@link MaterialModelV1}
     * 
     * @param sourceMaterialModel The source {@link MaterialModelV1}
     * @param targetMaterialModel The target {@link MaterialModelV1}
     */
    private void initMaterialModel(
        MaterialModelV1 sourceMaterialModel,
        MaterialModelV1 targetMaterialModel)
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
            MaterialModelV2 sourceMaterialModel = 
                (MaterialModelV2) sourceMaterialModels.get(i);
            MaterialModelV2 targetMaterialModel = 
                (MaterialModelV2) materialModelsMap.get(sourceMaterialModel);
            
            copyGltfChildOfRootPropertyElements(
                sourceMaterialModel, targetMaterialModel);            
            initMaterialModel(sourceMaterialModel, targetMaterialModel);
        }
    }
    
    /**
     * Initialize the given {@link MaterialModelV2} based on the given
     * {@link MaterialModelV2}
     * 
     * @param sourceMaterialModel The source {@link MaterialModelV2}
     * @param targetMaterialModel The target {@link MaterialModelV2}
     */
    private void initMaterialModel(
        MaterialModelV2 sourceMaterialModel, 
        MaterialModelV2 targetMaterialModel)
    {
        targetMaterialModel.setAlphaMode(
            sourceMaterialModel.getAlphaMode());
        targetMaterialModel.setAlphaCutoff(
            sourceMaterialModel.getAlphaCutoff());
        
        targetMaterialModel.setDoubleSided(
            sourceMaterialModel.isDoubleSided());
        
        TextureModel sourceBaseColorTexture = 
            sourceMaterialModel.getBaseColorTexture();
        DefaultTextureModel targetBaseColorTexture = 
            textureModelsMap.get(sourceBaseColorTexture);
        targetMaterialModel.setBaseColorTexture(
            targetBaseColorTexture);
        targetMaterialModel.setBaseColorTexcoord(
            sourceMaterialModel.getBaseColorTexcoord());
        
        float[] baseColorFactor = sourceMaterialModel.getBaseColorFactor();
        targetMaterialModel.setBaseColorFactor(
            Optionals.clone(baseColorFactor));

        TextureModel sourceMetallicRoughnessTexture = 
            sourceMaterialModel.getMetallicRoughnessTexture();
        DefaultTextureModel targetMetallicRoughnessTexture = 
            textureModelsMap.get(sourceMetallicRoughnessTexture);
        targetMaterialModel.setMetallicRoughnessTexture(
            targetMetallicRoughnessTexture);
        targetMaterialModel.setMetallicRoughnessTexcoord(
            sourceMaterialModel.getMetallicRoughnessTexcoord());

        targetMaterialModel.setMetallicFactor(
            sourceMaterialModel.getMetallicFactor());
        targetMaterialModel.setRoughnessFactor(
            sourceMaterialModel.getRoughnessFactor());

        TextureModel sourceNormalTexture = 
            sourceMaterialModel.getNormalTexture();
        DefaultTextureModel targetNormalTexture = 
            textureModelsMap.get(sourceNormalTexture);
        targetMaterialModel.setNormalTexture(
            targetNormalTexture);
        targetMaterialModel.setNormalTexcoord(
            sourceMaterialModel.getNormalTexcoord());
        
        targetMaterialModel.setNormalScale(
            sourceMaterialModel.getNormalScale());

        TextureModel sourceOcclusionTexture = 
            sourceMaterialModel.getOcclusionTexture();
        DefaultTextureModel targetOcclusionTexture = 
            textureModelsMap.get(sourceOcclusionTexture);
        targetMaterialModel.setOcclusionTexture(
            targetOcclusionTexture);
        targetMaterialModel.setOcclusionTexcoord(
            sourceMaterialModel.getOcclusionTexcoord());
        
        targetMaterialModel.setOcclusionStrength(
            sourceMaterialModel.getOcclusionStrength());
        

        TextureModel sourceEmissiveTexture = 
            sourceMaterialModel.getEmissiveTexture();
        DefaultTextureModel targetEmissiveTexture = 
            textureModelsMap.get(sourceEmissiveTexture);
        targetMaterialModel.setEmissiveTexture(
            targetEmissiveTexture);
        targetMaterialModel.setEmissiveTexcoord(
            sourceMaterialModel.getEmissiveTexcoord());
        
        float emissiveFactor[] = sourceMaterialModel.getEmissiveFactor();
        targetMaterialModel.setEmissiveFactor(
            Optionals.clone(emissiveFactor));
    }
    
    
    /**
     * Initialize the {@link ShaderModel} instances
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
            copyGltfChildOfRootPropertyElements(
                sourceCameraModel, targetCameraModel);
            
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
            copyGltfChildOfRootPropertyElements(
                sourceShaderModel, targetShaderModel);
            
            targetShaderModel.setUri(sourceShaderModel.getUri());
            targetShaderModel.setShaderType(sourceShaderModel.getShaderType());
            targetShaderModel.setShaderData(sourceShaderModel.getShaderData());
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
            copyGltfChildOfRootPropertyElements(
                sourceProgramModel, targetProgramModel);

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
        copyGltfChildOfRootPropertyElements(
            sourceAssetModel, targetAssetModel);
        
        targetAssetModel.setCopyright(sourceAssetModel.getCopyright());
        targetAssetModel.setGenerator(sourceAssetModel.getGenerator());
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
