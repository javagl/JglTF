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
package de.javagl.jgltf.model.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.Animation;
import de.javagl.jgltf.impl.v2.AnimationChannel;
import de.javagl.jgltf.impl.v2.AnimationChannelTarget;
import de.javagl.jgltf.impl.v2.AnimationSampler;
import de.javagl.jgltf.impl.v2.Asset;
import de.javagl.jgltf.impl.v2.Buffer;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.Camera;
import de.javagl.jgltf.impl.v2.CameraOrthographic;
import de.javagl.jgltf.impl.v2.CameraPerspective;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.impl.v2.Material;
import de.javagl.jgltf.impl.v2.MaterialNormalTextureInfo;
import de.javagl.jgltf.impl.v2.MaterialOcclusionTextureInfo;
import de.javagl.jgltf.impl.v2.MaterialPbrMetallicRoughness;
import de.javagl.jgltf.impl.v2.Mesh;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.impl.v2.Node;
import de.javagl.jgltf.impl.v2.Scene;
import de.javagl.jgltf.impl.v2.Skin;
import de.javagl.jgltf.impl.v2.Texture;
import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.AssetModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.CameraOrthographicModel;
import de.javagl.jgltf.model.CameraPerspectiveModel;
import de.javagl.jgltf.model.ExtensionsModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.OcclusionTextureInfoModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.PbrMaterialModel.AlphaMode;
import de.javagl.jgltf.model.PbrMetallicRoughnessModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.impl.DefaultNodeModel;

/**
 * A class for creating the {@link GlTF version 2.0 glTF} from a 
 * {@link GltfModel} 
 */
public class GltfCreatorV2
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfCreatorV2.class.getName());
    
    /**
     * Creates a {@link GlTF} from the given {@link GltfModel}
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The {@link GlTF}
     */
    public static GlTF create(GltfModel gltfModel)
    {
        GltfCreatorV2 creator = new GltfCreatorV2(gltfModel);
        return creator.create();
    }
    
    /**
     * Inner class containing the information that is necessary to define
     * a glTF {@link de.javagl.jgltf.impl.v2.Sampler}
     */
    @SuppressWarnings("javadoc")
    private static class SamplerInfo
    {
        final Integer magFilter;
        final Integer minFilter;
        final Integer wrapS;
        final Integer wrapT;
        
        SamplerInfo()
        {
            this.magFilter = null;
            this.minFilter = null;
            this.wrapS = null;
            this.wrapT = null;
        }

        SamplerInfo(TextureModel textureModel)
        {
            this.magFilter = textureModel.getMagFilter();
            this.minFilter = textureModel.getMinFilter();
            this.wrapS = textureModel.getWrapS();
            this.wrapT = textureModel.getWrapT();
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(magFilter, minFilter, wrapS, wrapT);
        }

        @Override
        public boolean equals(Object object)
        {
            if (this == object)
            {
                return true;
            }
            if (object == null)
            {
                return false;
            }
            if (getClass() != object.getClass())
            {
                return false;
            }
            SamplerInfo other = (SamplerInfo) object;
            if (!Objects.equals(magFilter, other.magFilter))
            {
                return false;
            }
            if (!Objects.equals(minFilter, other.minFilter))
            {
                return false;
            }
            if (!Objects.equals(wrapS, other.wrapS))
            {
                return false;
            }
            if (!Objects.equals(wrapT, other.wrapT))
            {
                return false;
            }
            return true;
        }
    }
    
    /**
     * The default sampler info, with all values being <code>null</code>.
     */
    private static final SamplerInfo DEFAULT_SAMPLER_INFO = new SamplerInfo(); 
    
    /**
     * The {@link GltfModel} that this instance operates on
     */
    private final GltfModel gltfModel;
    
    /**
     * A list of {@link NodeModel} instances that are the same as the
     * ones in the {@link GltfModel}, but extended so that each node
     * refers to at most one {@link MeshModel}. 
     * See {@link #createNodesWithSingleMeshes(List)}
     */
    private final List<NodeModel> nodesWithSingleMeshes;
    
    /**
     * A map from {@link AccessorModel} objects to their indices
     */
    private final Map<AccessorModel, Integer> accessorIndices;

    /**
     * A map from {@link BufferModel} objects to their indices
     */
    private final Map<BufferModel, Integer> bufferIndices;

    /**
     * A map from {@link BufferViewModel} objects to their indices
     */
    private final Map<BufferViewModel, Integer> bufferViewIndices;

    /**
     * A map from {@link CameraModel} objects to their indices
     */
    private final Map<CameraModel, Integer> cameraIndices;

    /**
     * A map from {@link ImageModel} objects to their indices
     */
    private final Map<ImageModel, Integer> imageIndices;

    /**
     * A map from {@link MaterialModel} objects to their indices
     */
    private final Map<MaterialModel, Integer> materialIndices;

    /**
     * A map from {@link MeshModel} objects to their indices
     */
    private final Map<MeshModel, Integer> meshIndices;

    /**
     * A map from {@link NodeModel} objects to their indices
     */
    private final Map<NodeModel, Integer> nodeIndices;

    /**
     * A map from {@link SkinModel} objects to their indices
     */
    private final Map<SkinModel, Integer> skinIndices;

    /**
     * A map from {@link TextureModel} objects to their indices
     */
    private final Map<TextureModel, Integer> textureIndices;
    
    /**
     * A map from {@link SamplerInfo} objects to their indices
     */
    private final Map<SamplerInfo, Integer> samplerIndices;
    
    /**
     * Creates a new instance with the given {@link GltfModel}
     * 
     * @param gltfModel The {@link GltfModel}
     */
    private GltfCreatorV2(GltfModel gltfModel)
    {
        this.gltfModel = Objects.requireNonNull(
            gltfModel, "The gltfModel may not be null");
        
        accessorIndices = computeIndexMap(gltfModel.getAccessorModels());
        bufferIndices = computeIndexMap(gltfModel.getBufferModels());
        bufferViewIndices = computeIndexMap(gltfModel.getBufferViewModels());
        cameraIndices = computeIndexMap(gltfModel.getCameraModels());
        imageIndices = computeIndexMap(gltfModel.getImageModels());
        materialIndices = computeIndexMap(gltfModel.getMaterialModels());
        meshIndices = computeIndexMap(gltfModel.getMeshModels());
        
        this.nodesWithSingleMeshes = 
            createNodesWithSingleMeshes(gltfModel.getNodeModels());
        nodeIndices = computeIndexMap(nodesWithSingleMeshes);
        skinIndices = computeIndexMap(gltfModel.getSkinModels());
        textureIndices = computeIndexMap(gltfModel.getTextureModels());
        
        samplerIndices = createSamplerIndices(gltfModel.getTextureModels());
    }
    
    /**
     * Create the {@link GlTF} instance from the {@link GltfModel}
     * 
     * @return The {@link GlTF} instance
     */
    public GlTF create()
    {
        GlTF gltf = new GlTF();
        ModelElementsV2.transferGltfPropertyElementsFromModel(gltfModel, gltf);
        
        gltf.setAccessors(map(
            gltfModel.getAccessorModels(), this::createAccessor));
        gltf.setAnimations(map(
            gltfModel.getAnimationModels(), this::createAnimation));
        gltf.setBuffers(map(
            gltfModel.getBufferModels(), this::createBuffer));
        gltf.setBufferViews(map(
            gltfModel.getBufferViewModels(), this::createBufferView));
        gltf.setCameras(map(
            gltfModel.getCameraModels(), this::createCamera));
        gltf.setImages(map(
            gltfModel.getImageModels(), this::createImage));
        gltf.setMaterials(map(
            gltfModel.getMaterialModels(), this::createMaterial));
        gltf.setMeshes(map(
            gltfModel.getMeshModels(), this::createMesh));
        gltf.setNodes(map(
            nodesWithSingleMeshes, this::createNode));
        gltf.setScenes(map(
            gltfModel.getSceneModels(), this::createScene));
        gltf.setSkins(map(
            gltfModel.getSkinModels(), this::createSkin));
        
        gltf.setSamplers(createSamplers());
        
        gltf.setTextures(map(
            gltfModel.getTextureModels(), this::createTexture));
        
        if (gltf.getScenes() != null && !gltf.getScenes().isEmpty())
        {
            gltf.setScene(0);
        }
        
        ExtensionsModel extensionsModel = gltfModel.getExtensionsModel();
        List<String> extensionsUsed = extensionsModel.getExtensionsUsed();
        if (!extensionsUsed.isEmpty()) 
        {
            gltf.setExtensionsUsed(extensionsUsed);
        }
        List<String> extensionsRequired = 
            extensionsModel.getExtensionsRequired();
        if (!extensionsRequired.isEmpty()) 
        {
            gltf.setExtensionsRequired(extensionsRequired);
        }
        
        Asset asset = createAsset(gltfModel.getAssetModel());
        gltf.setAsset(asset);
        
        return gltf;
    }
    
    /**
     * Creates a list of nodes that contains the same elements as the given
     * list, but replaces each node that has multiple {@link MeshModel} 
     * references with a new node that has the appropriate number of child
     * nodes that each refers to one {@link MeshModel}
     * 
     * @param nodeModels The input {@link NodeModel} objects
     * @return The resulting {@link NodeModel} objects
     */
    private static List<NodeModel> createNodesWithSingleMeshes(
        List<NodeModel> nodeModels)
    {
        List<NodeModel> newNodes = new ArrayList<NodeModel>();
        List<NodeModel> nodeModelsWithSingleMeshes = 
            new ArrayList<NodeModel>(nodeModels);
        for (int i=0; i<nodeModelsWithSingleMeshes.size(); i++)
        {
            NodeModel nodeModel = nodeModelsWithSingleMeshes.get(i);
            List<MeshModel> meshModels = nodeModel.getMeshModels();
            if (meshModels.size() > 1)
            {
                DefaultNodeModel newParentNodeModel = 
                    new DefaultNodeModel(nodeModel);
                
                for (int j=0; j<meshModels.size(); j++)
                {
                    MeshModel meshModel = meshModels.get(j);
                    DefaultNodeModel child = new DefaultNodeModel();
                    child.addMeshModel(meshModel);
                    newNodes.add(child);
                    newParentNodeModel.addChild(child);
                }
                nodeModelsWithSingleMeshes.set(i, newParentNodeModel);
            }
        }
        nodeModelsWithSingleMeshes.addAll(newNodes);
        return nodeModelsWithSingleMeshes;
    }
    
    /**
     * Create the {@link Accessor} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The {@link Accessor}
     */
    private Accessor createAccessor(AccessorModel accessorModel)
    {
        Integer bufferViewIndex = 
            bufferViewIndices.get(accessorModel.getBufferViewModel());
        return createAccessor(accessorModel, bufferViewIndex);
    }
    
    /**
     * Create the {@link Accessor} for the given {@link AccessorModel}.
     * 
     * This method is not part of tbe public API.
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewIndex The index of the {@link BufferViewModel}
     * that the {@link AccessorModel} refers to
     * @return The {@link Accessor}
     */
    public static Accessor createAccessorImpl(
        AccessorModel accessorModel, Integer bufferViewIndex)
    {
        Accessor accessor = new Accessor();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            accessorModel, accessor);
        
        accessor.setBufferView(bufferViewIndex);
        
        if (accessorModel.getByteOffset() != 0)
        {
            accessor.setByteOffset(accessorModel.getByteOffset());
        }
        accessor.setComponentType(accessorModel.getComponentType());
        accessor.setCount(accessorModel.getCount());
        accessor.setType(accessorModel.getElementType().toString());
        accessor.setNormalized(
            accessorModel.isNormalized() ? true : null);
        
        AccessorData accessorData = accessorModel.getAccessorData();
        accessor.setMax(AccessorDatas.computeMax(accessorData));
        accessor.setMin(AccessorDatas.computeMin(accessorData));
        
        return accessor;
    }

    /**
     * Create the {@link Accessor} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewIndex The index of the {@link BufferViewModel}
     * that the {@link AccessorModel} refers to
     * @return The {@link Accessor}
     */
    private Accessor createAccessor(
        AccessorModel accessorModel, Integer bufferViewIndex)
    {
        Accessor accessor = createAccessorImpl(accessorModel, bufferViewIndex);
        
        ExtensionModels.createExtensionImpls(
            gltfModel, accessorModel, AccessorModel.class, accessor);
        
        return accessor;
    }
    
    /**
     * Create the {@link Animation} for the given {@link AnimationModel}
     * 
     * @param animationModel The {@link AnimationModel}
     * @return The {@link Animation}
     */
    private Animation createAnimation(AnimationModel animationModel)
    {
        Animation animation = new Animation();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            animationModel, animation);
        
        List<Sampler> samplers = new ArrayList<Sampler>();
        List<Channel> channels = animationModel.getChannels();
        for (Channel channel : channels)
        {
            samplers.add(channel.getSampler());
        }
        
        List<AnimationChannel> animationChannels = 
            new ArrayList<AnimationChannel>();
        for (Channel channel : channels)
        {
            AnimationChannel animationChannel = new AnimationChannel();
            
            AnimationChannelTarget target = new AnimationChannelTarget();
            NodeModel nodeModel = channel.getNodeModel();
            target.setNode(nodeIndices.get(nodeModel));
            target.setPath(channel.getPath());
            animationChannel.setTarget(target);
            
            Sampler sampler = channel.getSampler();
            animationChannel.setSampler(samplers.indexOf(sampler));
            
            animationChannels.add(animationChannel);
        }
        animation.setChannels(animationChannels);
        
        List<AnimationSampler> animationSamplers = 
            new ArrayList<AnimationSampler>();
        for (Sampler sampler : samplers)
        {
            AnimationSampler animationSampler = new AnimationSampler();
            animationSampler.setInput(
                accessorIndices.get(sampler.getInput()));
            animationSampler.setInterpolation(
                sampler.getInterpolation().name());
            animationSampler.setOutput(
                accessorIndices.get(sampler.getOutput()));
            animationSamplers.add(animationSampler);
        }
        animation.setSamplers(animationSamplers);
        
        ExtensionModels.createExtensionImpls(
            gltfModel, animationModel, AnimationModel.class, animation);
        
        return animation;
    }
    
    
    /**
     * Create the {@link Buffer} for the given {@link BufferModel}.
     * 
     * This method is not part of the public API.
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The {@link Buffer}
     */
    public static Buffer createBufferImpl(BufferModel bufferModel)
    {
        Buffer buffer = new Buffer();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            bufferModel, buffer);

        buffer.setUri(bufferModel.getUri());
        buffer.setByteLength(bufferModel.getByteLength());
        
        return buffer;
    }
    
    /**
     * Create the {@link Buffer} for the given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The {@link Buffer}
     */
    private Buffer createBuffer(BufferModel bufferModel)
    {
        Buffer buffer = createBufferImpl(bufferModel);
        
        ExtensionModels.createExtensionImpls(
            gltfModel, bufferModel, BufferModel.class, buffer);
        
        return buffer;
    }
    
    /**
     * Create the {@link BufferView} for the given {@link BufferViewModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The {@link BufferView}
     */
    private BufferView createBufferView(BufferViewModel bufferViewModel)
    {
        Integer bufferIndex = 
            bufferIndices.get(bufferViewModel.getBufferModel());
        return createBufferView(bufferViewModel, bufferIndex);
    }
    
    /**
     * Create the {@link BufferView} for the given {@link BufferViewModel}.
     * 
     * This mthod is not part of the public API.
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @param bufferIndex The index of the {@link BufferModel} that the
     * {@link BufferViewModel} refers to
     * @return The {@link BufferView}
     */
    public static BufferView createBufferViewImpl(
        BufferViewModel bufferViewModel, Integer bufferIndex)
    {
        BufferView bufferView = new BufferView();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            bufferViewModel, bufferView);

        bufferView.setBuffer(bufferIndex);
        bufferView.setByteOffset(bufferViewModel.getByteOffset());
        bufferView.setByteLength(bufferViewModel.getByteLength());
        bufferView.setByteStride(bufferViewModel.getByteStride());
        bufferView.setTarget(bufferViewModel.getTarget());
        
        return bufferView;
    }
    
    /**
     * Create the {@link BufferView} for the given {@link BufferViewModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @param bufferIndex The index of the {@link BufferModel} that the
     * {@link BufferViewModel} refers to
     * @return The {@link BufferView}
     */
    private BufferView createBufferView(
        BufferViewModel bufferViewModel, Integer bufferIndex)
    {
        BufferView bufferView = createBufferViewImpl(
            bufferViewModel, bufferIndex);
        
        ExtensionModels.createExtensionImpls(
            gltfModel, bufferViewModel, BufferViewModel.class, bufferView);
        
        return bufferView;
    }
    

    /**
     * Create the {@link Camera} for the given {@link CameraModel}
     * 
     * @param cameraModel The {@link CameraModel}
     * @return The {@link Camera}
     */
    private Camera createCamera(CameraModel cameraModel)
    {
        Camera camera = new Camera();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            cameraModel, camera);
        
        CameraPerspectiveModel cameraPerspectiveModel = 
            cameraModel.getCameraPerspectiveModel();
        CameraOrthographicModel cameraOrthographicModel = 
            cameraModel.getCameraOrthographicModel();
        if (cameraPerspectiveModel != null)
        {
            CameraPerspective cameraPerspective = new CameraPerspective();
            cameraPerspective.setAspectRatio(
                cameraPerspectiveModel.getAspectRatio());
            cameraPerspective.setYfov(
                cameraPerspectiveModel.getYfov());
            cameraPerspective.setZfar(
                cameraPerspectiveModel.getZfar());
            cameraPerspective.setZnear(
                cameraPerspectiveModel.getZnear());
            camera.setPerspective(cameraPerspective);
            camera.setType("perspective");
        }
        else if (cameraOrthographicModel != null)
        {
            CameraOrthographic cameraOrthographic = new CameraOrthographic();
            cameraOrthographic.setXmag(
                cameraOrthographicModel.getXmag());
            cameraOrthographic.setYmag(
                cameraOrthographicModel.getYmag());
            cameraOrthographic.setZfar(
                cameraOrthographicModel.getZfar());
            cameraOrthographic.setZnear(
                cameraOrthographicModel.getZnear());
            camera.setOrthographic(cameraOrthographic);
            camera.setType("orthographic");
        }
        else
        {
            logger.severe("Camera is neither perspective nor orthographic");
        }
        
        ExtensionModels.createExtensionImpls(
            gltfModel, cameraModel, CameraModel.class, camera);
        
        return camera;
    }
    
    /**
     * Create the {@link Image} for the given {@link ImageModel}
     * 
     * @param imageModel The {@link ImageModel}
     * @return The {@link Image}
     */
    private Image createImage(ImageModel imageModel)
    {
        Image image = new Image();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            imageModel, image);
        
        Integer bufferView = 
            bufferViewIndices.get(imageModel.getBufferViewModel());
        image.setBufferView(bufferView);
        
        image.setMimeType(imageModel.getMimeType());
        image.setUri(imageModel.getUri());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, imageModel, ImageModel.class, image);
        
        return image;
    }
    
    /**
     * Create the {@link Material} for the given {@link MaterialModel}.
     * If the given {@link MaterialModel} is not a {@link PbrMaterialModel}, 
     * then a warning is printed and <code>null</code> is returned.
     * 
     * @param materialModel The {@link MaterialModel}
     * @return The {@link Material}
     */
    private Material createMaterial(MaterialModel materialModel)
    {
        if (materialModel instanceof PbrMaterialModel)
        {
            PbrMaterialModel pbrMaterialModel = (PbrMaterialModel)materialModel;
            return createMaterialV2(pbrMaterialModel);
        }
        // TODO It should be possible to use a glTF 1.0 material model here
        logger.severe("Cannot store glTF 1.0 material in glTF 2.0");
        return null;
    }
    
    /**
     * Create the {@link Material} for the given {@link PbrMaterialModel}
     * 
     * @param materialModel The {@link PbrMaterialModel}
     * @return The {@link Material}
     */
    private Material createMaterialV2(PbrMaterialModel materialModel)
    {
        Material material = new Material();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            materialModel, material);
        
        AlphaMode alphaMode = materialModel.getAlphaMode();
        if (alphaMode != null)
        {
            material.setAlphaMode(alphaMode.name());
        }
        material.setAlphaCutoff(materialModel.getAlphaCutoff());
        material.setDoubleSided(materialModel.isDoubleSided());
        
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            materialModel.getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel != null)
        {
            MaterialPbrMetallicRoughness pbrMetallicRoughness = 
                createPbrMetallicRoughness(pbrMetallicRoughnessModel);
            material.setPbrMetallicRoughness(pbrMetallicRoughness);
        }
            
        NormalTextureInfoModel normalTextureInfoModel = 
            materialModel.getNormalTextureInfoModel();
        if (normalTextureInfoModel != null)
        {
            MaterialNormalTextureInfo normalTextureInfo = 
                createNormalTextureInfo(normalTextureInfoModel);
            material.setNormalTexture(normalTextureInfo);
        }

        OcclusionTextureInfoModel occlusionTextureInfoModel = 
            materialModel.getOcclusionTextureInfoModel();
        if (occlusionTextureInfoModel != null)
        {
            
            MaterialOcclusionTextureInfo occlusionTextureInfo =
                createOcclusionTextureInfo(occlusionTextureInfoModel);
            material.setOcclusionTexture(occlusionTextureInfo);
        }
        
        TextureInfoModel emissiveTextureInfoModel = 
            materialModel.getEmissiveTextureInfoModel();
        if (emissiveTextureInfoModel != null)
        {
            TextureInfo emissiveTextureInfo = 
                createTextureInfo(emissiveTextureInfoModel);
            material.setEmissiveTexture(emissiveTextureInfo);
        }
        material.setEmissiveFactor(
            materialModel.getEmissiveFactor());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, materialModel, MaterialModel.class, material);
        
        return material;
    }

    /**
     * Create the {@link MaterialPbrMetallicRoughness} for the given 
     * {@link PbrMetallicRoughnessModel}
     * 
     * @param pbrMetallicRoughnessModel The {@link PbrMetallicRoughnessModel}
     * @return The {@link MaterialPbrMetallicRoughness}
     */
    private MaterialPbrMetallicRoughness createPbrMetallicRoughness(
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel)
    {
        MaterialPbrMetallicRoughness pbrMetallicRoughness =
            new MaterialPbrMetallicRoughness();

        pbrMetallicRoughness.setBaseColorFactor(
            pbrMetallicRoughnessModel.getBaseColorFactor());

        TextureInfoModel baseColorTextureInfoModel =
            pbrMetallicRoughnessModel.getBaseColorTextureInfoModel();
        if (baseColorTextureInfoModel != null)
        {
            TextureInfo baseColorTextureInfo = 
                createTextureInfo(baseColorTextureInfoModel);
            pbrMetallicRoughness.setBaseColorTexture(baseColorTextureInfo);
        }

        pbrMetallicRoughness.setMetallicFactor(
            pbrMetallicRoughnessModel.getMetallicFactor());
        pbrMetallicRoughness.setRoughnessFactor(
            pbrMetallicRoughnessModel.getRoughnessFactor());
        
        TextureInfoModel metallicRoughnessTextureInfoModel =
            pbrMetallicRoughnessModel
                .getMetallicRoughnessTextureInfoModel();
        if (metallicRoughnessTextureInfoModel != null)
        {
            TextureInfo metallicRoughnessTextureInfo = 
                createTextureInfo(metallicRoughnessTextureInfoModel);
            pbrMetallicRoughness.setMetallicRoughnessTexture(
                metallicRoughnessTextureInfo);
        }
        
        ExtensionModels.createExtensionImpls(
            gltfModel, pbrMetallicRoughnessModel, 
            PbrMetallicRoughnessModel.class, pbrMetallicRoughness);
        
        return pbrMetallicRoughness;
    }
    
    /**
     * Create the {@link MaterialNormalTextureInfo} for the given 
     * {@link NormalTextureInfoModel}
     * 
     * @param normalTextureInfoModel The {@link NormalTextureInfoModel}
     * @return The {@link MaterialNormalTextureInfo}
     */
    private MaterialNormalTextureInfo createNormalTextureInfo(
        NormalTextureInfoModel normalTextureInfoModel)
    {
        MaterialNormalTextureInfo normalTextureInfo = 
            new MaterialNormalTextureInfo();
        TextureModel textureModel = normalTextureInfoModel.getTextureModel();
        normalTextureInfo.setIndex(
            textureIndices.get(textureModel));
        normalTextureInfo.setTexCoord(
            normalTextureInfo.getTexCoord());
        normalTextureInfo.setScale(
            normalTextureInfo.getScale());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, normalTextureInfoModel, 
            NormalTextureInfoModel.class, normalTextureInfo);
        
        return normalTextureInfo;
    }

    /**
     * Create the {@link MaterialOcclusionTextureInfo} for the given 
     * {@link OcclusionTextureInfoModel}
     * 
     * @param occlusionTextureInfoModel The {@link OcclusionTextureInfoModel}
     * @return The {@link MaterialOcclusionTextureInfo}
     */
    private MaterialOcclusionTextureInfo createOcclusionTextureInfo(
        OcclusionTextureInfoModel occlusionTextureInfoModel)
    {
        MaterialOcclusionTextureInfo occlusionTextureInfo = 
            new MaterialOcclusionTextureInfo();
        TextureModel textureModel = occlusionTextureInfoModel.getTextureModel();
        occlusionTextureInfo.setIndex(
            textureIndices.get(textureModel));
        occlusionTextureInfo.setTexCoord(
            occlusionTextureInfoModel.getTexCoord());
        occlusionTextureInfo.setStrength(
            occlusionTextureInfoModel.getStrength());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, occlusionTextureInfoModel, 
            OcclusionTextureInfoModel.class, occlusionTextureInfo);
        
        return occlusionTextureInfo;
    }

    /**
     * Create the {@link TextureInfo} for the given {@link TextureInfoModel}
     * 
     * @param textureInfoModel The {@link TextureInfoModel}
     * @return The {@link TextureInfo}
     */
    private TextureInfo createTextureInfo(TextureInfoModel textureInfoModel)
    {
        TextureInfo textureInfo = new TextureInfo();
        TextureModel textureModel = textureInfoModel.getTextureModel();
        textureInfo.setIndex(textureIndices.get(textureModel));
        textureInfo.setTexCoord(textureInfoModel.getTexCoord());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, textureInfoModel, TextureInfoModel.class, textureInfo);
        
        return textureInfo;
    }

    
    /**
     * Create the {@link Mesh} for the given {@link MeshModel}
     * 
     * @param meshModel The {@link MeshModel}
     * @return The {@link Mesh}
     */
    private Mesh createMesh(MeshModel meshModel)
    {
        Mesh mesh = new Mesh();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            meshModel, mesh);
        
        List<MeshPrimitive> meshPrimitives = new ArrayList<MeshPrimitive>();
        List<MeshPrimitiveModel> meshPrimitiveModels = 
            meshModel.getMeshPrimitiveModels();
        for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitiveModels)
        {
            MeshPrimitive meshPrimitive = 
                createMeshPrimitive(meshPrimitiveModel);
            meshPrimitives.add(meshPrimitive);
        }
        mesh.setPrimitives(meshPrimitives);
        mesh.setWeights(toList(meshModel.getWeights()));
        
        ExtensionModels.createExtensionImpls(
            gltfModel, meshModel, MeshModel.class, mesh);
        
        return mesh;
    }
    
    /**
     * Create the {@link MeshPrimitive} for the given {@link MeshPrimitiveModel}
     * 
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     * @return The {@link MeshPrimitive}
     */
    private MeshPrimitive createMeshPrimitive(
        MeshPrimitiveModel meshPrimitiveModel)
    {
        MeshPrimitive meshPrimitive = new MeshPrimitive();
        ModelElementsV2.transferGltfPropertyElementsFromModel(
            meshPrimitiveModel, meshPrimitive);

        meshPrimitive.setMode(meshPrimitiveModel.getMode());
        
        Map<String, Integer> attributes = resolveIndices(
            meshPrimitiveModel.getAttributes(), 
            accessorIndices::get);
        meshPrimitive.setAttributes(attributes);

        AccessorModel indices = meshPrimitiveModel.getIndices();
        meshPrimitive.setIndices(accessorIndices.get(indices));
        
        List<Map<String, AccessorModel>> modelTargetsList = 
            meshPrimitiveModel.getTargets();
        if (!modelTargetsList.isEmpty())
        {
            List<Map<String, Integer>> targetsList = 
                new ArrayList<Map<String, Integer>>();
            for (Map<String, AccessorModel> modelTargets : modelTargetsList)
            {
                Map<String, Integer> targets = resolveIndices(
                    modelTargets, accessorIndices::get);
                targetsList.add(targets);
            }
            meshPrimitive.setTargets(targetsList);
        }
        
        Integer material = materialIndices.get(
            meshPrimitiveModel.getMaterialModel());
        meshPrimitive.setMaterial(material);
        
        ExtensionModels.createExtensionImpls(
            gltfModel, meshPrimitiveModel, 
            MeshPrimitiveModel.class, meshPrimitive);
        
        return meshPrimitive;
    }

    /**
     * Create the {@link Node} for the given {@link NodeModel}
     * 
     * @param nodeModel The {@link NodeModel}
     * @return The {@link Node}
     */
    private Node createNode(NodeModel nodeModel)
    {
        Node node = new Node();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            nodeModel, node);
        
        if (!nodeModel.getChildren().isEmpty())
        {
            node.setChildren(map(
                nodeModel.getChildren(), nodeIndices::get));
        }

        node.setTranslation(Optionals.clone(nodeModel.getTranslation()));
        node.setRotation(Optionals.clone(nodeModel.getRotation()));
        node.setScale(Optionals.clone(nodeModel.getScale()));
        node.setMatrix(Optionals.clone(nodeModel.getMatrix()));
        
        Integer camera = cameraIndices.get(nodeModel.getCameraModel());
        node.setCamera(camera);
        
        Integer skin = skinIndices.get(nodeModel.getSkinModel());
        node.setSkin(skin);
        
        node.setWeights(toList(nodeModel.getWeights()));
        
        List<MeshModel> nodeMeshModels = nodeModel.getMeshModels();
        if (nodeMeshModels.size() > 1)
        {
            // This should never me the case here, because this method
            // is called with the nodes that have been preprocessed
            // using #createNodesWithSingleMeshes
            logger.severe("Warning: glTF 2.0 only supports one mesh per node");
        }
        if (!nodeMeshModels.isEmpty())
        {
            MeshModel nodeMeshModel = nodeMeshModels.get(0);
            Integer mesh = meshIndices.get(nodeMeshModel);
            node.setMesh(mesh);
        }
        
        ExtensionModels.createExtensionImpls(
            gltfModel, nodeModel, NodeModel.class, node);
        return node;
    }
    
    /**
     * Create the {@link Scene} for the given {@link SceneModel}
     * 
     * @param sceneModel The {@link SceneModel}
     * @return The {@link Scene}
     */
    private Scene createScene(SceneModel sceneModel)
    {
        Scene scene = new Scene();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            sceneModel, scene);
        
        scene.setNodes(map(
            sceneModel.getNodeModels(), nodeIndices::get));
        
        ExtensionModels.createExtensionImpls(
            gltfModel, sceneModel, 
            SceneModel.class, scene);
        
        return scene;
    }
    
    /**
     * Create the {@link Skin} for the given {@link SkinModel}
     * 
     * @param skinModel The {@link SkinModel}
     * @return The {@link Skin}
     */
    private Skin createSkin(SkinModel skinModel)
    {
        Skin skin = new Skin();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            skinModel, skin);
        
        Integer inverseBindMatrices = 
            accessorIndices.get(skinModel.getInverseBindMatrices());
        skin.setInverseBindMatrices(inverseBindMatrices);
        
        skin.setJoints(map(
            skinModel.getJoints(), nodeIndices::get));
        
        Integer skeleton = nodeIndices.get(skinModel.getSkeleton());
        skin.setSkeleton(skeleton);
        
        ExtensionModels.createExtensionImpls(
            gltfModel, skinModel, 
            SkinModel.class, skin);
        
        return skin;
    }
    
    /**
     * Create a mapping from {@link SamplerInfo} objects to consecutive
     * indices, based on the {@link SamplerInfo} objects that are 
     * created from the given {@link TextureModel} objects
     * 
     * @param textureModels The {@link TextureModel} objects
     * @return The indices
     */
    private Map<SamplerInfo, Integer> createSamplerIndices(
        List<TextureModel> textureModels)
    {
        Map<SamplerInfo, Integer> samplerIndices = 
            new LinkedHashMap<SamplerInfo, Integer>();
        for (TextureModel textureModel : textureModels)
        {
            SamplerInfo samplerInfo = new SamplerInfo(textureModel);
            if (!samplerInfo.equals(DEFAULT_SAMPLER_INFO))
            {
                if (!samplerIndices.containsKey(samplerInfo))
                {
                    samplerIndices.put(samplerInfo, samplerIndices.size());
                }
            }
        }
        return samplerIndices;
    }
    
    /**
     * Create the {@link de.javagl.jgltf.impl.v2.Sampler} objects for
     * the current glTF model, returning <code>null</code> if there
     * are no samplers.
     * 
     * @return The samplers
     */
    private List<de.javagl.jgltf.impl.v2.Sampler> createSamplers()
    {
        if (samplerIndices.isEmpty())
        {
            return null;
        }
        List<de.javagl.jgltf.impl.v2.Sampler> samplers = 
            new ArrayList<de.javagl.jgltf.impl.v2.Sampler>();
        for (SamplerInfo samplerInfo : samplerIndices.keySet())
        {
            de.javagl.jgltf.impl.v2.Sampler sampler = 
                createSampler(samplerInfo);
            samplers.add(sampler);
        }
        return samplers;
    }
    
    /**
     * Create a {@link de.javagl.jgltf.impl.v2.Sampler} from the given 
     * {@link SamplerInfo}
     * 
     * @param samplerInfo The {@link SamplerInfo}
     * @return The {@link de.javagl.jgltf.impl.v2.Sampler}
     */
    private static de.javagl.jgltf.impl.v2.Sampler createSampler(
        SamplerInfo samplerInfo)
    {
        de.javagl.jgltf.impl.v2.Sampler sampler = 
            new de.javagl.jgltf.impl.v2.Sampler();
        sampler.setMagFilter(samplerInfo.magFilter);
        sampler.setMinFilter(samplerInfo.minFilter);
        sampler.setWrapS(samplerInfo.wrapS);
        sampler.setWrapT(samplerInfo.wrapT);
        return sampler;
    }
    

    /**
     * Creates a texture for the given {@link TextureModel}
     * 
     * @param textureModel The {@link TextureModel}
     * @return The {@link Texture}
     */
    private Texture createTexture(TextureModel textureModel)
    {
        Texture texture = new Texture();
        ModelElementsV2.transferGltfChildOfRootPropertyElementsFromModel(
            textureModel, texture);
        
        SamplerInfo samplerInfo = new SamplerInfo(textureModel);
        if (!samplerInfo.equals(DEFAULT_SAMPLER_INFO))
        {
            Integer index = samplerIndices.get(samplerInfo);
            texture.setSampler(index);
        }
        
        texture.setSource(imageIndices.get(textureModel.getImageModel()));
        
        ExtensionModels.createExtensionImpls(
            gltfModel, textureModel, 
            TextureModel.class, texture);
        
        return texture;
    }
    
    /**
     * Creates an asset for the given {@link AssetModel}
     * 
     * @param assetModel The {@link AssetModel}
     * @return The {@link Asset}
     */
    private Asset createAsset(AssetModel assetModel)
    {
        Asset asset = new Asset();
        asset.setVersion("2.0");
        asset.setGenerator("JglTF from https://github.com/javagl/JglTF");
        
        ModelElementsV2.transferGltfPropertyElementsFromModel(
            assetModel, asset);
        
        if (assetModel.getCopyright() != null)
        {
            asset.setCopyright(assetModel.getCopyright());
        }
        if (assetModel.getGenerator() != null)
        {
            asset.setGenerator(assetModel.getGenerator());
        }
        
        ExtensionModels.createExtensionImpls(
            gltfModel, assetModel, 
            AssetModel.class, asset);
        
        return asset;
    }
    
    /**
     * Returns a list containing the result of mapping the given elements with
     * the given function, or <code>null</code> if the given collection is 
     * empty
     *  
     * @param collection The collection
     * @param mapper The mapper
     * @return The list
     */
    private static <T, U> List<U> map(
        Collection<? extends T> collection, 
        Function<? super T, ? extends U> mapper)
    {
        if (collection.isEmpty())
        {
            return null;
        }
        return collection.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * Creates a map that has the same keys as the given map, mapped to 
     * the indices that are looked up for the respective values, using 
     * the given function
     * 
     * @param map The map
     * @param indexLookup The index lookup
     * @return The index map
     */
    private static <K, T> Map<K, Integer> resolveIndices(
        Map<K, ? extends T> map, 
        Function<? super T, Integer> indexLookup)
    {
        Map<K, Integer> result = new LinkedHashMap<K, Integer>();
        for (Entry<K, ? extends T> entry : map.entrySet())
        {
            K key = entry.getKey();
            T value = entry.getValue();
            Integer index = indexLookup.apply(value);
            result.put(key, index);
        }
        return result;
    }
    
    /**
     * Create an ordered map that contains a mapping of the given elements
     * to consecutive integers
     * 
     * @param elements The elements
     * @return The index map
     */
    private static <T> Map<T, Integer> computeIndexMap(
        Collection<? extends T> elements)
    {
        Map<T, Integer> indices = new LinkedHashMap<T, Integer>();
        int index = 0;
        for (T element : elements)
        {
            indices.put(element, index);
            index++;
        }
        return indices;
    }
    
    
    /**
     * Returns a new list containing the elements of the given array,
     * or <code>null</code> if the given array is <code>null</code>
     * 
     * @param array The array
     * @return The list
     */
    private static List<Double> toList(double array[])
    {
        if (array == null)
        {
            return null;
        }
        List<Double> list = new ArrayList<Double>();
        for (double d : array)
        {
            list.add(d);
        }
        return list;
    }
}