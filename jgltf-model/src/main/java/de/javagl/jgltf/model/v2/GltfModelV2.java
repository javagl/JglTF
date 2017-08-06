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
package de.javagl.jgltf.model.v2;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.Animation;
import de.javagl.jgltf.impl.v2.AnimationChannel;
import de.javagl.jgltf.impl.v2.AnimationChannelTarget;
import de.javagl.jgltf.impl.v2.AnimationSampler;
import de.javagl.jgltf.impl.v2.Buffer;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.Camera;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.impl.v2.Material;
import de.javagl.jgltf.impl.v2.Mesh;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.impl.v2.Node;
import de.javagl.jgltf.impl.v2.Sampler;
import de.javagl.jgltf.impl.v2.Scene;
import de.javagl.jgltf.impl.v2.Skin;
import de.javagl.jgltf.impl.v2.Texture;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.GltfReference;
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

/**
 * Implementation of a {@link GltfModel}, based on a {@link GlTF glTF 2.0}.<br>
 */
public final class GltfModelV2 implements GltfModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModelV2.class.getName());

    /**
     * The {@link GlTF} of this model
     */
    private final GlTF gltf;
    
    /**
     * The binary data that is associated with this model in the case
     * that the glTF was a binary glTF
     */
    private final ByteBuffer binaryData;
    
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
     * Creates a new model for the given glTF
     * 
     * @param gltf The {@link GlTF}
     * @param binaryData The binary data, for the case that the glTF was
     * read from a binary file
     */
    public GltfModelV2(GlTF gltf, ByteBuffer binaryData)
    {
        this.gltf = Objects.requireNonNull(gltf, 
            "The gltf may not be null");
        this.binaryData = binaryData;
        
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
        
        initBufferModels();
        initBufferViewModels();
        
        BinaryGltfDataResolverV2 binaryGltfDataResolver =
            new BinaryGltfDataResolverV2(this);
        binaryGltfDataResolver.resolve();
        
        initAccessorModels();
        initAnimationModels();
        initMaterialModels();
        initMeshModels();
        initNodeModels();
        initSceneModels();
        initSkinModels();
        initTextureModels();
        
        instantiateCameraModels();
    }
    
    
    @Override
    public List<GltfReference> getReferences()
    {
        List<GltfReference> references = new ArrayList<GltfReference>();
        references.addAll(getBufferReferences());
        references.addAll(getImageReferences());
        return references;
    }
    
    /**
     * Create a list containing all {@link GltfReference} objects for the
     * buffers that are contained in this model.
     * 
     * @return The references
     */
    public List<GltfReference> getBufferReferences()
    {
        List<GltfReference> references = new ArrayList<GltfReference>();
        List<Buffer> buffers = Optionals.of(gltf.getBuffers());
        for (int i = 0; i < buffers.size(); i++)
        {
            Buffer buffer = buffers.get(i);
            if (buffer.getUri() == null)
            {
                // This is the binary glTF buffer
                continue;
            }
            BufferModel bufferModel = bufferModels.get(i);
            String uri = bufferModel.getUri();
            Consumer<ByteBuffer> target = 
                byteBuffer -> bufferModel.setBufferData(byteBuffer);
            GltfReference reference =
                new GltfReference("buffer " + i, uri, target);
            references.add(reference);
        }
        return references;
    }
    
    /**
     * Create a list containing all {@link GltfReference} objects for the
     * images that are contained in this model.
     * 
     * @return The references
     */
    public List<GltfReference> getImageReferences()
    {
        List<GltfReference> references = new ArrayList<GltfReference>();
        List<Image> images = Optionals.of(gltf.getImages());
        for (int i = 0; i < images.size(); i++)
        {
            Image image = images.get(i);
            if (image.getUri() == null)
            {
                // This is an image that refers to a buffer view
                continue;
            }
            ImageModel imageModel = imageModels.get(i);
            String uri = imageModel.getUri();
            Consumer<ByteBuffer> target = 
                byteBuffer -> imageModel.setImageData(byteBuffer);
            GltfReference reference = 
                new GltfReference("image " + i, uri, target);
            references.add(reference);
        }
        return references;
    }
    
    
    /**
     * Create the {@link AccessorModel} instances
     */
    private void createAccessorModels()
    {
        List<Accessor> accessors = Optionals.of(gltf.getAccessors());
        for (int i = 0; i < accessors.size(); i++)
        {
            Accessor accessor = accessors.get(i);
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
        ElementType elementType = ElementType.valueOf(accessor.getType());
        Integer byteStride = elementType.getNumComponents() *
            Accessors.getNumBytesForAccessorComponentType(componentType);
        DefaultAccessorModel accessorModel = 
            new DefaultAccessorModel(componentType, byteOffset, count,
                elementType, byteStride);
        return accessorModel;
    }

    /**
     * Create the {@link AnimationModel} instances
     */
    private void createAnimationModels()
    {
        List<Animation> animations = Optionals.of(gltf.getAnimations());
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
        List<Buffer> buffers = Optionals.of(gltf.getBuffers());
        for (int i = 0; i < buffers.size(); i++)
        {
            Buffer buffer = buffers.get(i);
            DefaultBufferModel bufferModel = 
                new DefaultBufferModel(buffer.getUri());
            bufferModels.add(bufferModel);
        }
    }
    
    /**
     * Create the {@link BufferViewModel} instances
     */
    private void createBufferViewModels()
    {
        List<BufferView> bufferViews = Optionals.of(gltf.getBufferViews());
        for (int i = 0; i < bufferViews.size(); i++)
        {
            BufferView bufferView = bufferViews.get(i);
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
        Integer byteStride = bufferView.getByteStride();
        Integer target = bufferView.getTarget();
        DefaultBufferViewModel bufferViewModel = 
            new DefaultBufferViewModel(
                byteOffset, byteLength, byteStride, target);
        return bufferViewModel;
    }
    
    /**
     * Create the {@link ImageModel} instances
     */
    private void createImageModels()
    {
        List<Image> images = Optionals.of(gltf.getImages());
        for (int i = 0; i < images.size(); i++)
        {
            Image image = images.get(i);
            String uri = image.getUri();
            DefaultImageModel imageModel = 
                new DefaultImageModel(uri, null, null);
            imageModels.add(imageModel);
        }
    }
    
    /**
     * Create the {@link MaterialModel} instances
     */
    private void createMaterialModels()
    {
        List<Material> materials = Optionals.of(gltf.getMaterials());
        for (int i = 0; i < materials.size(); i++)
        {
            Material material = materials.get(i);
            // TODO Create MaterialModel
            Map<String, Object> values = Collections.emptyMap();
            materialModels.add(new DefaultMaterialModel(values));
        }
    }
    
    /**
     * Create the {@link MeshModel} instances
     */
    private void createMeshModels()
    {
        List<Mesh> meshes = Optionals.of(gltf.getMeshes());
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
        List<Node> nodes = Optionals.of(gltf.getNodes());
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
        List<Scene> scenes = Optionals.of(gltf.getScenes());
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
        List<Skin> skins = Optionals.of(gltf.getSkins());
        for (int i = 0; i < skins.size(); i++)
        {
            Skin skin = skins.get(i);
            skinModels.add(new DefaultSkinModel(null));
        }
    }

    /**
     * Create the {@link TextureModel} instances
     */
    private void createTextureModels()
    {
        List<Texture> textures = Optionals.of(gltf.getTextures());
        List<Sampler> samplers = Optionals.of(gltf.getSamplers());
        for (int i = 0; i < textures.size(); i++)
        {
            Texture texture = textures.get(i);
            int samplerIndex = texture.getSampler();
            Sampler sampler = samplers.get(samplerIndex);
            
            Integer magFilter = sampler.getMagFilter();
            Integer minFilter = sampler.getMinFilter();
            int wrapS = Optionals.of(
                sampler.getWrapS(), sampler.defaultWrapS());
            int wrapT = Optionals.of(
                sampler.getWrapT(), sampler.defaultWrapT());
            
            textureModels.add(new DefaultTextureModel(
                magFilter, minFilter, wrapS, wrapT));
        }
    }
    
    /**
     * Initialize the {@link AccessorModel} instances
     */
    private void initAccessorModels()
    {
        List<Accessor> accessors = Optionals.of(gltf.getAccessors());
        for (int i = 0; i < accessors.size(); i++)
        {
            Accessor accessor = accessors.get(i);
            int bufferViewIndex = accessor.getBufferView();
            BufferViewModel bufferViewModel = 
                bufferViewModels.get(bufferViewIndex);
            DefaultAccessorModel accessorModel = accessorModels.get(i);
            accessorModel.setBufferViewModel(bufferViewModel);
        }
    }

    /**
     * Initialize the {@link AnimationModel} instances
     */
    private void initAnimationModels()
    {
        List<Animation> animations = Optionals.of(gltf.getAnimations());
        for (int i = 0; i < animations.size(); i++)
        {
            Animation animation = animations.get(i);
            DefaultAnimationModel animationModel = animationModels.get(i);
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
        List<AnimationSampler> samplers = 
            Optionals.of(animation.getSamplers());

        int samplerIndex = animationChannel.getSampler();
        AnimationSampler animationSampler = samplers.get(samplerIndex);
        
        int inputAccessorIndex = animationSampler.getInput();
        DefaultAccessorModel inputAccessorModel = 
            accessorModels.get(inputAccessorIndex);
        
        int outputAccessorIndex = animationSampler.getOutput();
        DefaultAccessorModel outputAccessorModel = 
            accessorModels.get(outputAccessorIndex);
        
        String interpolationString = 
            animationSampler.getInterpolation();
        Interpolation interpolation = 
            interpolationString == null ? Interpolation.LINEAR :
            Interpolation.valueOf(interpolationString);
        
        AnimationModel.Sampler sampler = new DefaultSampler(
            inputAccessorModel, interpolation, outputAccessorModel);
        
        AnimationChannelTarget animationChannelTarget = 
            animationChannel.getTarget();
        
        Integer nodeIndex = animationChannelTarget.getNode();
        NodeModel nodeModel = null;
        if (nodeIndex == null)
        {
            // Should not happen yet. Targets always refer to nodes
            logger.warning("No node index given for animation channel target");
        }
        else
        {
            nodeModel = nodeModels.get(nodeIndex);
        }
        String path = animationChannelTarget.getPath();
        
        AnimationModel.Channel channel = 
            new DefaultChannel(sampler, nodeModel, path);
        return channel;
    }

    /**
     * Initialize the {@link BufferModel} instances
     */
    private void initBufferModels()
    {
        List<Buffer> buffers = Optionals.of(gltf.getBuffers());
        if (binaryData != null)
        {
            if (buffers.isEmpty()) 
            {
                logger.warning("Binary data was given, but no buffers");
            }
            else
            {
                BufferModel bufferModel = bufferModels.get(0);
                bufferModel.setBufferData(binaryData);
            }
        }
    }
    
    
    /**
     * Initialize the {@link BufferViewModel} instances
     */
    private void initBufferViewModels()
    {
        List<BufferView> bufferViews = Optionals.of(gltf.getBufferViews());
        for (int i = 0; i < bufferViews.size(); i++)
        {
            BufferView bufferView = bufferViews.get(i);
            
            int bufferIndex = bufferView.getBuffer();
            BufferModel bufferModel = bufferModels.get(bufferIndex);
            DefaultBufferViewModel bufferViewModel = bufferViewModels.get(i);
            bufferViewModel.setBufferModel(bufferModel);
        }
    }
    

    /**
     * Initialize the {@link MeshModel} instances
     */
    private void initMeshModels()
    {
        List<Mesh> meshes = Optionals.of(gltf.getMeshes());
        for (int i = 0; i < meshes.size(); i++)
        {
            Mesh mesh = meshes.get(i);
            List<MeshPrimitive> primitives = 
                Optionals.of(mesh.getPrimitives());
            DefaultMeshModel meshModel = meshModels.get(i);
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
        
        Integer indicesIndex = meshPrimitive.getIndices();
        if (indicesIndex != null)
        {
            AccessorModel indices = accessorModels.get(indicesIndex);
            meshPrimitiveModel.setIndices(indices);
        }
        Map<String, Integer> attributes = 
            Optionals.of(meshPrimitive.getAttributes());
        for (Entry<String, Integer> entry : attributes.entrySet())
        {
            String attributeName = entry.getKey();
            int attributeIndex = entry.getValue();
            AccessorModel attribute = accessorModels.get(attributeIndex);
            meshPrimitiveModel.putAttribute(attributeName, attribute);
        }
        
        Integer materialIndex = meshPrimitive.getMaterial();
        if (materialIndex == null)
        {
            int XXX; // TODO Review this: DefaultModels here?
            meshPrimitiveModel.setMaterialModel(
                de.javagl.jgltf.model.v1.gl.DefaultModels
                    .getDefaultMaterialModel());
        }
        else
        {
            MaterialModel materialModel = materialModels.get(materialIndex);
            meshPrimitiveModel.setMaterialModel(materialModel);
        }
        
        return meshPrimitiveModel;
    }

    /**
     * Initialize the {@link NodeModel} instances
     */
    private void initNodeModels()
    {
        List<Node> nodes = Optionals.of(gltf.getNodes());
        for (int i = 0; i < nodes.size(); i++)
        {
            Node node = nodes.get(i);
            
            DefaultNodeModel nodeModel = nodeModels.get(i);
            List<Integer> childIndices = Optionals.of(node.getChildren());
            for (Integer childIndex : childIndices)
            {
                DefaultNodeModel child = nodeModels.get(childIndex);
                nodeModel.addChild(child);
            }
            Integer meshIndex = node.getMesh();
            if (meshIndex != null)
            {
                MeshModel meshModel = meshModels.get(meshIndex);
                nodeModel.addMeshModel(meshModel);
            }
            Integer skinIndex = node.getSkin();
            if (skinIndex != null)
            {
                SkinModel skinModel = skinModels.get(skinIndex);
                nodeModel.setSkinModel(skinModel);
            }
            
            float matrix[] = node.getMatrix();
            float translation[] = node.getTranslation();
            float rotation[] = node.getRotation();
            float scale[] = node.getScale();
            nodeModel.setMatrix(
                matrix == null ? null : matrix.clone());
            nodeModel.setTranslation(
                translation == null ? null : translation.clone());
            nodeModel.setRotation(
                rotation == null ? null : rotation.clone());
            nodeModel.setScale(
                scale == null ? null : scale.clone());
            
            List<Float> weights = node.getWeights();
            if (weights != null)
            {
                float weightsArray[] = new float[weights.size()];
                for (int j = 0; j < weights.size(); j++)
                {
                    weightsArray[j] = weights.get(j);
                }
                nodeModel.setWeights(weightsArray);
            }
        }
    }
    
    /**
     * Initialize the {@link SceneModel} instances
     */
    private void initSceneModels()
    {
        List<Scene> scenes = Optionals.of(gltf.getScenes());
        for (int i = 0; i < scenes.size(); i++)
        {
            Scene scene = scenes.get(i);

            DefaultSceneModel sceneModel = sceneModels.get(i);
            List<Integer> nodeIndices = Optionals.of(scene.getNodes());
            for (Integer nodeIndex : nodeIndices)
            {
                NodeModel nodeModel = nodeModels.get(nodeIndex);
                sceneModel.addNode(nodeModel);
            }
        }
    }
    
    /**
     * Initialize the {@link SkinModel} instances
     */
    private void initSkinModels()
    {
        List<Skin> skins = Optionals.of(gltf.getSkins());
        for (int i = 0; i < skins.size(); i++)
        {
            Skin skin = skins.get(i);
            DefaultSkinModel skinModel = skinModels.get(i);
            
            List<Integer> jointIndices = skin.getJoints();
            for (Integer jointIndex : jointIndices)
            {
                NodeModel jointNodeModel = nodeModels.get(jointIndex);
                skinModel.addJoint(jointNodeModel);
            }
            
            Integer inverseBindMatricesIndex = skin.getInverseBindMatrices();
            DefaultAccessorModel inverseBindMatrices = 
                accessorModels.get(inverseBindMatricesIndex);
            skinModel.setInverseBindMatrices(inverseBindMatrices);
        }
    }
    
    /**
     * Initialize the {@link TextureModel} instances
     */
    private void initTextureModels()
    {
        List<Texture> textures = Optionals.of(gltf.getTextures());
        for (int i = 0; i < textures.size(); i++)
        {
            Texture texture = textures.get(i);
            DefaultTextureModel textureModel = textureModels.get(i);
            Integer imageIndex = texture.getSource();
            DefaultImageModel imageModel = imageModels.get(imageIndex);
            textureModel.setImageModel(imageModel);
        }
    }
    
    /**
     * Initialize the {@link MaterialModel} instances
     */
    private void initMaterialModels()
    {
        List<Material> materials = Optionals.of(gltf.getMaterials());
        for (int i = 0; i < materials.size(); i++)
        {
            Material material = materials.get(i);
            // TODO Initialize MaterialModel
            DefaultMaterialModel materialModel = materialModels.get(i);
            materialModel.setTechniqueModel(
                de.javagl.jgltf.model.v1.gl.DefaultModels
                    .getDefaultTechniqueModel());
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
        List<Node> nodes = Optionals.of(gltf.getNodes());
        List<Camera> cameras = Optionals.of(gltf.getCameras());
        for (int i = 0; i < nodes.size(); i++)
        {
            Node node = nodes.get(i);
            
            Integer cameraIndex = node.getCamera();
            if (cameraIndex != null)
            {
                Camera camera = cameras.get(cameraIndex);
                NodeModel nodeModel = nodeModels.get(i);
                String name = "node" + i + "." + "camera" + cameraIndex;
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
                    CamerasV2.computeProjectionMatrix(
                        camera, aspectRatio, localResult);
                    return localResult;
                };
                DefaultCameraModel cameraModel = new DefaultCameraModel(
                    viewMatrixComputer, projectionMatrixComputer);
                cameraModel.setName(name);
                cameraModel.setNodeModel(nodeModel);
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
    
    @Override
    public GlTF getGltf()
    {
        return gltf;
    }
    
}
