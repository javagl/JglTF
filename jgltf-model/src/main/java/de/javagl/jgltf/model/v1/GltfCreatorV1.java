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
package de.javagl.jgltf.model.v1;

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

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.impl.v1.Animation;
import de.javagl.jgltf.impl.v1.AnimationChannel;
import de.javagl.jgltf.impl.v1.AnimationChannelTarget;
import de.javagl.jgltf.impl.v1.AnimationSampler;
import de.javagl.jgltf.impl.v1.Asset;
import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.Camera;
import de.javagl.jgltf.impl.v1.CameraOrthographic;
import de.javagl.jgltf.impl.v1.CameraPerspective;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Mesh;
import de.javagl.jgltf.impl.v1.MeshPrimitive;
import de.javagl.jgltf.impl.v1.Node;
import de.javagl.jgltf.impl.v1.Scene;
import de.javagl.jgltf.impl.v1.Skin;
import de.javagl.jgltf.impl.v1.Texture;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.CameraOrthographicModel;
import de.javagl.jgltf.model.CameraPerspectiveModel;
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

/**
 * A class for creating the {@link GlTF version 1.0 glTF} from a 
 * {@link GltfModel}.<br>
 * <br>
 * TODO: Not all features that could be supported are supported yet. 
 */
public class GltfCreatorV1
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfCreatorV1.class.getName());
    
    /**
     * Inner class containing the information that is necessary to define
     * a glTF {@link de.javagl.jgltf.impl.v1.Sampler}
     */
    @SuppressWarnings("javadoc")
    private static class SamplerInfo
    {
        final Integer magFilter;
        final Integer minFilter;
        final Integer wrapS;
        final Integer wrapT;
        
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
     * The {@link GltfModel} that this instance operates on
     */
    private final GltfModel gltfModel;
    
    /**
     * A map from {@link AccessorModel} objects to their IDs
     */
    private final Map<AccessorModel, String> accessorIds;

    /**
     * A map from {@link BufferModel} objects to their IDs
     */
    private final Map<BufferModel, String> bufferIds;

    /**
     * A map from {@link BufferViewModel} objects to their IDs
     */
    private final Map<BufferViewModel, String> bufferViewIds;

    /**
     * A map from {@link CameraModel} objects to their IDs
     */
    private final Map<CameraModel, String> cameraIds;

    /**
     * A map from {@link ImageModel} objects to their IDs
     */
    private final Map<ImageModel, String> imageIds;

    /**
     * A map from {@link MaterialModel} objects to their IDs
     */
    private final Map<MaterialModel, String> materialIds;

    /**
     * A map from {@link MeshModel} objects to their IDs
     */
    private final Map<MeshModel, String> meshIds;

    /**
     * A map from {@link NodeModel} objects to their IDs
     */
    private final Map<NodeModel, String> nodeIds;

    /**
     * A map from {@link SkinModel} objects to their IDs
     */
    private final Map<SkinModel, String> skinIds;

    /**
     * A map from {@link TextureModel} objects to their IDs
     */
    private final Map<TextureModel, String> textureIds;
    
    /**
     * A map from {@link SamplerInfo} objects to their IDs
     */
    private final Map<SamplerInfo, String> samplerIds;
    
    /**
     * Creates a new instance with the given {@link GltfModel}
     * 
     * @param gltfModel The {@link GltfModel}
     */
    public GltfCreatorV1(GltfModel gltfModel)
    {
        this.gltfModel = Objects.requireNonNull(
            gltfModel, "The gltfModel may not be null");
        
        accessorIds = computeIdMap(
            "accessor", gltfModel.getAccessorModels());
        bufferIds = computeIdMap(
            "buffer", gltfModel.getBufferModels());
        bufferViewIds = computeIdMap(
            "bufferView", gltfModel.getBufferViewModels());
        cameraIds = computeIdMap(
            "camera", gltfModel.getCameraModels());
        imageIds = computeIdMap(
            "image", gltfModel.getImageModels());
        materialIds = computeIdMap(
            "material", gltfModel.getMaterialModels());
        meshIds = computeIdMap(
            "mesh", gltfModel.getMeshModels());
        nodeIds = computeIdMap(
            "node", gltfModel.getNodeModels());
        skinIds = computeIdMap(
            "skin", gltfModel.getSkinModels());
        textureIds = computeIdMap(
            "texture", gltfModel.getTextureModels());
        
        samplerIds = createSamplerIds(gltfModel.getTextureModels());
    }
    
    /**
     * Create the {@link GlTF} instance from the {@link GltfModel}
     * 
     * @return The {@link GlTF} instance
     */
    public GlTF create()
    {
        GlTF gltf = new GlTF();
        
        gltf.setAccessors(map("accessor",
            gltfModel.getAccessorModels(), 
            this::createAccessor));
        gltf.setAnimations(map("animation",
            gltfModel.getAnimationModels(), 
            this::createAnimation));
        gltf.setBuffers(map("buffer",
            gltfModel.getBufferModels(), 
            GltfCreatorV1::createBuffer));
        gltf.setBufferViews(map("bufferView",
            gltfModel.getBufferViewModels(), 
            this::createBufferView));
        gltf.setCameras(map("camera", 
            gltfModel.getCameraModels(), 
            this::createCamera));
        gltf.setImages(map("image",
            gltfModel.getImageModels(), 
            this::createImage));
        gltf.setMaterials(map("material", 
            gltfModel.getMaterialModels(), 
            this::createMaterial));
        gltf.setMeshes(map("mesh", 
            gltfModel.getMeshModels(), 
            this::createMesh));
        gltf.setNodes(map("node",
            gltfModel.getNodeModels(), 
            this::createNode));
        gltf.setScenes(map("scene",
            gltfModel.getSceneModels(), 
            this::createScene));
        gltf.setSkins(map("skin", 
            gltfModel.getSkinModels(), 
            this::createSkin));
        
        gltf.setSamplers(createSamplers());
        
        gltf.setTextures(map("texture",
            gltfModel.getTextureModels(), 
            this::createTexture));
        
        if (gltf.getScenes() != null && !gltf.getScenes().isEmpty())
        {
            gltf.setScene(gltf.getScenes().keySet().iterator().next());
        }
        
        Asset asset = new Asset();
        asset.setVersion("1.0");
        asset.setGenerator("JglTF from https://github.com/javagl/JglTF");
        gltf.setAsset(asset);
        
        return gltf;
    }
    
    /**
     * Create the {@link Accessor} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The {@link Accessor}
     */
    private Accessor createAccessor(AccessorModel accessorModel)
    {
        String bufferViewId = 
            bufferViewIds.get(accessorModel.getBufferViewModel());
        return createAccessor(accessorModel, bufferViewId);
    }
    
    /**
     * Create the {@link Accessor} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewId The ID of the {@link BufferViewModel}
     * that the {@link AccessorModel} refers to
     * @return The {@link Accessor}
     */
    public static Accessor createAccessor(
        AccessorModel accessorModel, String bufferViewId)
    {
        Accessor accessor = new Accessor();
        accessor.setName(accessorModel.getName());
        
        accessor.setBufferView(bufferViewId);
        
        accessor.setByteOffset(accessorModel.getByteOffset());
        accessor.setComponentType(accessorModel.getComponentType());
        accessor.setCount(accessorModel.getCount());
        accessor.setType(accessorModel.getElementType().toString());
        
        AccessorData accessorData = accessorModel.getAccessorData();
        accessor.setMax(AccessorDatas.computeMax(accessorData));
        accessor.setMin(AccessorDatas.computeMin(accessorData));
        
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
        animation.setName(animationModel.getName());
        
        Map<Sampler, String> samplers = new LinkedHashMap<Sampler, String>();
        List<Channel> channels = animationModel.getChannels();
        int counter = 0;
        for (Channel channel : channels)
        {
            String id = "sampler_" + counter;
            samplers.put(channel.getSampler(), id);
            counter++;
        }
        
        List<AnimationChannel> animationChannels = 
            new ArrayList<AnimationChannel>();
        for (Channel channel : channels)
        {
            AnimationChannel animationChannel = new AnimationChannel();
            
            AnimationChannelTarget target = new AnimationChannelTarget();
            NodeModel nodeModel = channel.getNodeModel();
            target.setId(nodeIds.get(nodeModel));
            target.setPath(channel.getPath());
            animationChannel.setTarget(target);
            
            Sampler sampler = channel.getSampler();
            animationChannel.setSampler(samplers.get(sampler));
            
            animationChannels.add(animationChannel);
        }
        animation.setChannels(animationChannels);
        
        Map<String, AnimationSampler> animationSamplers = 
            new LinkedHashMap<String, AnimationSampler>();
        for (Sampler sampler : samplers.keySet())
        {
            AnimationSampler animationSampler = new AnimationSampler();
            animationSampler.setInput(
                accessorIds.get(sampler.getInput()));
            animationSampler.setInterpolation(
                sampler.getInterpolation().name());
            animationSampler.setOutput(
                accessorIds.get(sampler.getOutput()));
            String key = samplers.get(sampler);
            animationSamplers.put(key, animationSampler);
        }
        animation.setSamplers(animationSamplers);
        
        return animation;
    }
    
    
    /**
     * Create the {@link Buffer} for the given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The {@link Buffer}
     */
    public static Buffer createBuffer(BufferModel bufferModel)
    {
        Buffer buffer = new Buffer();
        buffer.setName(bufferModel.getName());

        buffer.setUri(bufferModel.getUri());
        buffer.setByteLength(bufferModel.getByteLength());
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
        String bufferId = 
            bufferIds.get(bufferViewModel.getBufferModel());
        return createBufferView(bufferViewModel, bufferId);
    }
    
    /**
     * Create the {@link BufferView} for the given {@link BufferViewModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @param bufferId The ID of the {@link BufferModel} that the
     * {@link BufferViewModel} refers to
     * @return The {@link BufferView}
     */
    public static BufferView createBufferView(
        BufferViewModel bufferViewModel, String bufferId)
    {
        BufferView bufferView = new BufferView();
        bufferView.setName(bufferViewModel.getName());

        bufferView.setBuffer(bufferId);
        bufferView.setByteOffset(bufferViewModel.getByteOffset());
        bufferView.setByteLength(bufferViewModel.getByteLength());
        bufferView.setTarget(bufferViewModel.getTarget());
        
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
        camera.setName(cameraModel.getName());
        
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
        }
        else
        {
            logger.severe("Camera is neither perspective nor orthographic");
        }
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
        image.setName(imageModel.getName());
        
        String bufferView = 
            bufferViewIds.get(imageModel.getBufferViewModel());
        if (bufferView != null)
        {
            // TODO Handle images with BufferView
            logger.severe(
                "Images with BufferView are not supported in glTF 1.0");
            //image.setBufferView(bufferView);
            //image.setMimeType(imageModel.getMimeType());
        }
        image.setUri(imageModel.getUri());
        
        return image;
    }
    
    /**
     * Create the {@link Material} for the given {@link MaterialModel}.
     * If the given {@link MaterialModel} is not a {@link MaterialModelV1},
     * then a warning is printed and <code>null</code> is returned.
     * 
     * @param materialModel The {@link MaterialModel}
     * @return The {@link Material}
     */
    private Material createMaterial(MaterialModel materialModel)
    {
        if (materialModel instanceof MaterialModelV1)
        {
            MaterialModelV1 materialModelV1 = (MaterialModelV1)materialModel;
            return createMaterialV1(materialModelV1);
        }
        // TODO It should be possible to use a glTF 2.0 material model here
        logger.severe("Cannot store glTF 2.0 material in glTF 1.0");
        return null;
    }
    
    /**
     * Create the {@link Material} for the given {@link MaterialModelV1}
     * 
     * @param materialModel The {@link MaterialModelV1}
     * @return The {@link Material}
     */
    private Material createMaterialV1(MaterialModelV1 materialModel)
    {
        Material material = new Material();
        material.setName(materialModel.getName());
        
        // TODO Convert techniques etc.
        
        return material;
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
        mesh.setName(meshModel.getName());
        
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
        
        if (meshModel.getWeights() != null)
        {
            // TODO Handle morph target weights?
            logger.severe("Morph target weights are not supported in glTF 1.0");
            //mesh.setWeights(toList(meshModel.getWeights()));
        }
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
        meshPrimitive.setMode(meshPrimitiveModel.getMode());
        
        Map<String, String> attributes = resolveIds(
            meshPrimitiveModel.getAttributes(), 
            accessorIds::get);
        meshPrimitive.setAttributes(attributes);

        AccessorModel Ids = meshPrimitiveModel.getIndices();
        meshPrimitive.setIndices(accessorIds.get(Ids));
        
        List<Map<String, AccessorModel>> modelTargetsList = 
            meshPrimitiveModel.getTargets();
        if (!modelTargetsList.isEmpty())
        {
            logger.severe("Morph targets are not supported in glTF 1.0");
            /*
            List<Map<String, Integer>> targetsList = 
                new ArrayList<Map<String, Integer>>();
            for (Map<String, AccessorModel> modelTargets : modelTargetsList)
            {
                Map<String, String> targets = resolveIds(
                    modelTargets, accessorIds::get);
                targetsList.add(targets);
            }
            meshPrimitive..setTargets(targetsList);
            */
        }
        
        String material = materialIds.get(
            meshPrimitiveModel.getMaterialModel());
        meshPrimitive.setMaterial(material);
        
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
        node.setName(nodeModel.getName());
        
        if (!nodeModel.getChildren().isEmpty())
        {
            node.setChildren(map(
                nodeModel.getChildren(), nodeIds::get));
        }

        node.setTranslation(Optionals.clone(nodeModel.getTranslation()));
        node.setRotation(Optionals.clone(nodeModel.getRotation()));
        node.setScale(Optionals.clone(nodeModel.getScale()));
        node.setMatrix(Optionals.clone(nodeModel.getMatrix()));
        
        String camera = cameraIds.get(nodeModel.getCameraModel());
        node.setCamera(camera);
        
        String skin = skinIds.get(nodeModel.getSkinModel());
        node.setSkin(skin);
        
        if (nodeModel.getWeights() != null)
        {
            // TODO Handle morph target weights?
            logger.severe("Morph target weights are not supported in glTF 1.0");
            //node.setWeights(toList(nodeModel.getWeights()));
        }
        
        List<MeshModel> nodeMeshModels = nodeModel.getMeshModels();
        if (!nodeMeshModels.isEmpty())
        {
            List<String> meshes = new ArrayList<String>();
            for (MeshModel meshModel : nodeMeshModels)
            {
                String id = meshIds.get(meshModel);
                meshes.add(id);
            }
            node.setMeshes(meshes);
        }
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
        scene.setName(sceneModel.getName());
        
        scene.setNodes(map(
            sceneModel.getNodeModels(), nodeIds::get));
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
        skin.setName(skinModel.getName());
        
        String inverseBindMatrices = 
            accessorIds.get(skinModel.getInverseBindMatrices());
        skin.setInverseBindMatrices(inverseBindMatrices);
        
        // TODO Skinning is not supported yet
//        skin.setJoints(map(
//            skinModel.getJoints(), nodeIds::get));
//        
//        String skeleton = nodeIds.get(skinModel.getSkeleton());
//        skin.setSkeleton(skeleton);
        
        return skin;
    }
    
    /**
     * Create a mapping from {@link SamplerInfo} objects to IDs,
     * based on the {@link SamplerInfo} objects that are 
     * created from the given {@link TextureModel} objects
     * 
     * @param textureModels The {@link TextureModel} objects
     * @return The IDs
     */
    private Map<SamplerInfo, String> createSamplerIds(
        List<TextureModel> textureModels)
    {
        Map<SamplerInfo, String> samplerIndices = 
            new LinkedHashMap<SamplerInfo, String>();
        for (TextureModel textureModel : textureModels)
        {
            SamplerInfo samplerInfo = new SamplerInfo(textureModel);
            if (!samplerIndices.containsKey(samplerInfo))
            {
                samplerIndices.put(samplerInfo, 
                    "sampler_" + samplerIndices.size());
            }
        }
        return samplerIndices;
    }
    
    /**
     * Create the {@link de.javagl.jgltf.impl.v1.Sampler} objects for
     * the current glTF model, returning <code>null</code> if there
     * are no samplers.
     * 
     * @return The samplers
     */
    private Map<String, de.javagl.jgltf.impl.v1.Sampler> createSamplers()
    {
        if (samplerIds.isEmpty())
        {
            return null;
        }
        Map<String, de.javagl.jgltf.impl.v1.Sampler> samplers = 
            new LinkedHashMap<String, de.javagl.jgltf.impl.v1.Sampler>();
        for (SamplerInfo samplerInfo : samplerIds.keySet())
        {
            de.javagl.jgltf.impl.v1.Sampler sampler = 
                createSampler(samplerInfo);
            String key = samplerIds.get(samplerInfo);
            samplers.put(key, sampler);
        }
        return samplers;
    }
    
    /**
     * Create a {@link de.javagl.jgltf.impl.v1.Sampler} from the given 
     * {@link SamplerInfo}
     * 
     * @param samplerInfo The {@link SamplerInfo}
     * @return The {@link de.javagl.jgltf.impl.v1.Sampler}
     */
    private static de.javagl.jgltf.impl.v1.Sampler createSampler(
        SamplerInfo samplerInfo)
    {
        de.javagl.jgltf.impl.v1.Sampler sampler = 
            new de.javagl.jgltf.impl.v1.Sampler();
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
        texture.setName(textureModel.getName());
        
        SamplerInfo samplerInfo = new SamplerInfo(textureModel);
        String id = samplerIds.get(samplerInfo);
        texture.setSampler(id);
        
        texture.setSource(imageIds.get(textureModel.getImageModel()));
        
        return texture;
    }

    
    
    private static <T, U> Map<String, U> map(
        String prefix, 
        Collection<? extends T> elements,
        Function<? super T, ? extends U> mapper)
    {
        return map(prefix, map(elements, mapper));
    }

    private static <T> Map<String, T> map(
        String prefix, Collection<? extends T> elements)
    {
        Map<String, T> map = new LinkedHashMap<String, T>();
        int index = 0;
        for (T element : elements)
        {
            map.put(prefix + "_" + index, element);
            index++;
        }
        return map;
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
     * the IDs that are looked up for the respective values, using 
     * the given function
     * 
     * @param map The map
     * @param idLookup The index lookup
     * @return The index map
     */
    private static <K, T> Map<K, String> resolveIds(
        Map<K, ? extends T> map, 
        Function<? super T, String> idLookup)
    {
        Map<K, String> result = new LinkedHashMap<K, String>();
        for (Entry<K, ? extends T> entry : map.entrySet())
        {
            K key = entry.getKey();
            T value = entry.getValue();
            String id = idLookup.apply(value);
            result.put(key, id);
        }
        return result;
    }
    
    /**
     * Create an ordered map that contains a mapping of the given elements
     * to IDs that start with the given prefix
     * 
     * @param prefix The prefix
     * @param elements The elements
     * @return The ID map
     */
    private static <T> Map<T, String> computeIdMap(
        String prefix, Collection<? extends T> elements)
    {
        Map<T, String> ids = new LinkedHashMap<T, String>();
        int index = 0;
        for (T element : elements)
        {
            ids.put(element, prefix + "_" + index);
            index++;
        }
        return ids;
    }
    
    
    /**
     * Returns a new list containing the elements of the given array,
     * or <code>null</code> if the given array is <code>null</code>
     * 
     * @param array The array
     * @return The list
     */
    private static List<Float> toList(float array[])
    {
        if (array == null)
        {
            return null;
        }
        List<Float> list = new ArrayList<Float>();
        for (float f : array)
        {
            list.add(f);
        }
        return list;
    }
}