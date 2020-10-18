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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.AccessorSparse;
import de.javagl.jgltf.impl.v2.AccessorSparseIndices;
import de.javagl.jgltf.impl.v2.AccessorSparseValues;
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
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
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
import de.javagl.jgltf.model.gl.TechniqueModel;
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
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.v2.gl.Materials;

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
     * The {@link GltfAssetV2} of this model
     */
    private final GltfAssetV2 gltfAsset;
    
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
     * The {@link MaterialModelHandler} that will manage the 
     * {@link MaterialModel} instances that have to be created
     */
    private final MaterialModelHandler materialModelHandler;

    /**
     * Creates a new model for the given glTF
     * 
     * @param gltfAsset The {@link GltfAssetV2}
     */
    public GltfModelV2(GltfAssetV2 gltfAsset)
    {
        this.gltfAsset = Objects.requireNonNull(gltfAsset, 
            "The gltfAsset may not be null");
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
        
        this.materialModelHandler = new MaterialModelHandler();
        
        createAccessorModels();
        createAnimationModels();
        createBufferModels();
        createBufferViewModels();
        createImageModels();
        createMeshModels();
        createNodeModels();
        createSceneModels();
        createSkinModels();
        createTextureModels();

        initBufferModels();
        initBufferViewModels();
        
        initAccessorModels();
        initAnimationModels();
        initImageModels();
        initMeshModels();
        initNodeModels();
        initSceneModels();
        initSkinModels();
        initTextureModels();
        
        instantiateCameraModels();
        instantiateMaterialModels();
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
            Integer componentType = accessor.getComponentType();
            Integer count = accessor.getCount();
            ElementType elementType = ElementType.forString(accessor.getType());
            DefaultAccessorModel accessorModel =  new DefaultAccessorModel(
                componentType, count, elementType);
            accessorModels.add(accessorModel);
        }
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
        int byteOffset = Optionals.of(bufferView.getByteOffset(), 0);
        int byteLength = bufferView.getByteLength();
        Integer byteStride = bufferView.getByteStride();
        Integer target = bufferView.getTarget();
        DefaultBufferViewModel bufferViewModel = 
            new DefaultBufferViewModel(target);
        bufferViewModel.setByteOffset(byteOffset);
        bufferViewModel.setByteLength(byteLength);
        bufferViewModel.setByteStride(byteStride);
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
            String mimeType = image.getMimeType();
            DefaultImageModel imageModel = 
                new DefaultImageModel(mimeType, null);
            String uri = image.getUri();
            imageModel.setUri(uri);
            imageModels.add(imageModel);
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
            Integer samplerIndex = texture.getSampler();
            
            Integer magFilter = GltfConstants.GL_LINEAR;
            Integer minFilter = GltfConstants.GL_LINEAR;
            int wrapS = GltfConstants.GL_REPEAT;
            int wrapT = GltfConstants.GL_REPEAT;
            
            if (samplerIndex != null)
            {
                Sampler sampler = samplers.get(samplerIndex);
                magFilter = sampler.getMagFilter();
                minFilter = sampler.getMinFilter();
                wrapS = Optionals.of(
                    sampler.getWrapS(), sampler.defaultWrapS());
                wrapT = Optionals.of(
                    sampler.getWrapT(), sampler.defaultWrapT());
            }
            
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
            DefaultAccessorModel accessorModel = accessorModels.get(i);
            
            int byteOffset = Optionals.of(accessor.getByteOffset(), 0);
            accessorModel.setByteOffset(byteOffset);

            AccessorSparse accessorSparse = accessor.getSparse();
            if (accessorSparse == null)
            {
                initDenseAccessorModel(i, accessor, accessorModel);
            }
            else
            {
                initSparseAccessorModel(i, accessor, accessorModel);
            }
        }
    }


    /**
     * Initialize the {@link AccessorModel} by setting its 
     * {@link AccessorModel#getBufferViewModel() buffer view model}
     * for the case that the accessor is dense (i.e. not sparse)
     * 
     * @param accessorIndex The accessor index. Only used for constructing
     * the URI string of buffers that may have to be created internally 
     * @param accessor The {@link Accessor}
     * @param accessorModel The {@link AccessorModel}
     */
    private void initDenseAccessorModel(int accessorIndex,
        Accessor accessor, DefaultAccessorModel accessorModel)
    {
        Integer bufferViewIndex = accessor.getBufferView();
        if (bufferViewIndex != null)
        {
            // When there is a BufferView referenced from the accessor, then 
            // the corresponding BufferViewModel may be assigned directly
            DefaultBufferViewModel bufferViewModel = 
                bufferViewModels.get(bufferViewIndex);
            accessorModel.setBufferViewModel(bufferViewModel);
        }
        else
        {
            // When there is no BufferView referenced from the accessor,
            // then a NEW BufferViewModel (and Buffer) have to be created
            int count = accessorModel.getCount();
            int elementSizeInBytes = accessorModel.getElementSizeInBytes();
            int byteLength = elementSizeInBytes * count;
            ByteBuffer bufferData = Buffers.create(byteLength);
            String uriString = "buffer_for_accessor" + accessorIndex + ".bin";
            DefaultBufferViewModel bufferViewModel = 
                createBufferViewModel(uriString, bufferData);
            accessorModel.setBufferViewModel(bufferViewModel);
        }
        
        BufferViewModel bufferViewModel = accessorModel.getBufferViewModel(); 
        Integer byteStride = bufferViewModel.getByteStride();
        if (byteStride == null)
        {
            accessorModel.setByteStride(
                accessorModel.getElementSizeInBytes());
        }
        else
        {
            accessorModel.setByteStride(byteStride);
        }
    }
    
    
    /**
     * Initialize the given {@link AccessorModel} by setting its 
     * {@link AccessorModel#getBufferViewModel() buffer view model}
     * for the case that the accessor is sparse. 
     * 
     * @param accessorIndex The accessor index. Only used for constructing
     * the URI string of buffers that may have to be created internally 
     * @param accessor The {@link Accessor}
     * @param accessorModel The {@link AccessorModel}
     */
    private void initSparseAccessorModel(int accessorIndex,
        Accessor accessor, DefaultAccessorModel accessorModel)
    {
        // When the (sparse!) Accessor already refers to a BufferView,
        // then this BufferView has to be replaced with a new one,
        // to which the data substitution will be applied 
        int count = accessorModel.getCount();
        int elementSizeInBytes = accessorModel.getElementSizeInBytes();
        int byteLength = elementSizeInBytes * count;
        ByteBuffer bufferData = Buffers.create(byteLength);
        String uriString = "buffer_for_accessor" + accessorIndex + ".bin";
        DefaultBufferViewModel denseBufferViewModel = 
            createBufferViewModel(uriString, bufferData);
        accessorModel.setBufferViewModel(denseBufferViewModel);
        accessorModel.setByteOffset(0);
        
        Integer bufferViewIndex = accessor.getBufferView();
        if (bufferViewIndex != null)
        {
            // If the accessor refers to a BufferView, then the corresponding
            // data serves as the basis for the initialization of the values, 
            // before the sparse substitution is applied
            Consumer<ByteBuffer> sparseSubstitutionCallback = denseByteBuffer -> 
            {
                logger.fine("Substituting sparse accessor data,"
                    + " based on existing buffer view");
                
                DefaultBufferViewModel baseBufferViewModel = 
                    bufferViewModels.get(bufferViewIndex);
                ByteBuffer baseBufferViewData = 
                    baseBufferViewModel.getBufferViewData();
                AccessorData baseAccessorData = AccessorDatas.create(
                    accessorModel, baseBufferViewData);
                AccessorData denseAccessorData = 
                    AccessorDatas.create(accessorModel, bufferData);
                substituteSparseAccessorData(accessor, accessorModel, 
                    denseAccessorData, baseAccessorData); 
            };
            denseBufferViewModel.setSparseSubstitutionCallback(
                sparseSubstitutionCallback);
        }
        else
        {
            // When the sparse accessor does not yet refer to a BufferView,
            // then a new one is created, 
            Consumer<ByteBuffer> sparseSubstitutionCallback = denseByteBuffer -> 
            {
                logger.fine("Substituting sparse accessor data, "
                    + "without an existing buffer view");
                
                AccessorData denseAccessorData = 
                    AccessorDatas.create(accessorModel, bufferData);
                substituteSparseAccessorData(accessor, accessorModel, 
                    denseAccessorData, null); 
            };
            denseBufferViewModel.setSparseSubstitutionCallback(
                sparseSubstitutionCallback);
        }
    }
    
    /**
     * Create a new {@link BufferViewModel} with an associated 
     * {@link BufferModel} that serves as the basis for a sparse accessor, or 
     * an accessor that does not refer to a {@link BufferView})
     * 
     * @param uriString The URI string that will be assigned to the 
     * {@link BufferModel} that is created internally. This string 
     * is not strictly required, but helpful for debugging, at least
     * @param bufferData The buffer data
     * @return The new {@link BufferViewModel}
     */
    private static DefaultBufferViewModel createBufferViewModel(
        String uriString, ByteBuffer bufferData)
    {
        DefaultBufferModel bufferModel = new DefaultBufferModel();
        bufferModel.setUri(uriString);
        bufferModel.setBufferData(bufferData);

        DefaultBufferViewModel bufferViewModel = 
            new DefaultBufferViewModel(null);
        bufferViewModel.setByteOffset(0);
        bufferViewModel.setByteLength(bufferData.capacity());
        bufferViewModel.setBufferModel(bufferModel);
        
        return bufferViewModel;
    }
    
    /**
     * Substitute the sparse accessor data in the given dense 
     * {@link AccessorData} for the given {@link AccessorModel}
     * based on the sparse accessor data that is defined in the given 
     * {@link Accessor}.
     * 
     * @param accessor The {@link Accessor}
     * @param accessorModel The {@link AccessorModel}
     * @param denseAccessorData The dense {@link AccessorData}
     * @param baseAccessorData The optional {@link AccessorData} that contains 
     * the base data. If this is not <code>null</code>, then it will be used 
     * to initialize the {@link AccessorData}, before the sparse data 
     * substitution takes place
     */
    private void substituteSparseAccessorData(
        Accessor accessor, AccessorModel accessorModel, 
        AccessorData denseAccessorData, AccessorData baseAccessorData)
    {
        AccessorSparse accessorSparse = accessor.getSparse();
        int count = accessorSparse.getCount();
        
        AccessorSparseIndices accessorSparseIndices = 
            accessorSparse.getIndices();
        AccessorData sparseIndicesAccessorData = 
            createSparseIndicesAccessorData(accessorSparseIndices, count);
        
        AccessorSparseValues accessorSparseValues = accessorSparse.getValues();
        ElementType elementType = accessorModel.getElementType();
        AccessorData sparseValuesAccessorData =
            createSparseValuesAccessorData(accessorSparseValues, 
                accessorModel.getComponentType(),
                elementType.getNumComponents(), count);
     
        AccessorSparseUtils.substituteAccessorData(
            denseAccessorData, 
            baseAccessorData, 
            sparseIndicesAccessorData, 
            sparseValuesAccessorData);
    }
    
    
    /**
     * Create the {@link AccessorData} for the given 
     * {@link AccessorSparseIndices}
     * 
     * @param accessorSparseIndices The {@link AccessorSparseIndices}
     * @param count The count from the {@link AccessorSparse} 
     * @return The {@link AccessorData}
     */
    private AccessorData createSparseIndicesAccessorData(
        AccessorSparseIndices accessorSparseIndices, int count)
    {
        Integer componentType = accessorSparseIndices.getComponentType();
        Integer bufferViewIndex = accessorSparseIndices.getBufferView();
        BufferViewModel bufferViewModel = bufferViewModels.get(bufferViewIndex);
        ByteBuffer bufferViewData = bufferViewModel.getBufferViewData();
        int byteOffset = Optionals.of(accessorSparseIndices.getByteOffset(), 0);
        return AccessorDatas.create(
            componentType, bufferViewData, byteOffset, count, 1, null);
    }
    
    /**
     * Create the {@link AccessorData} for the given 
     * {@link AccessorSparseValues}
     * 
     * @param accessorSparseValues The {@link AccessorSparseValues}
     * @param componentType The component type of the {@link Accessor}
     * @param numComponentsPerElement The number of components per element
     * of the {@link AccessorModel#getElementType() accessor element type}
     * @param count The count from the {@link AccessorSparse} 
     * @return The {@link AccessorData}
     */
    private AccessorData createSparseValuesAccessorData(
        AccessorSparseValues accessorSparseValues, 
        int componentType, int numComponentsPerElement, int count)
    {
        Integer bufferViewIndex = accessorSparseValues.getBufferView();
        BufferViewModel bufferViewModel = bufferViewModels.get(bufferViewIndex);
        ByteBuffer bufferViewData = bufferViewModel.getBufferViewData();
        int byteOffset = Optionals.of(accessorSparseValues.getByteOffset(), 0);
        return AccessorDatas.create(
            componentType, bufferViewData, byteOffset, count, 
            numComponentsPerElement, null);
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

        if (buffers.isEmpty() && binaryData != null)
        {
            logger.warning("Binary data was given, but no buffers");
            return;
        }

        for (int i = 0; i < buffers.size(); i++)
        {
            Buffer buffer = buffers.get(i);
            DefaultBufferModel bufferModel = bufferModels.get(i);
            bufferModel.setName(buffer.getName());
            if (i == 0 && binaryData != null)
            {
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
        List<BufferView> bufferViews = Optionals.of(gltf.getBufferViews());
        for (int i = 0; i < bufferViews.size(); i++)
        {
            BufferView bufferView = bufferViews.get(i);
            
            DefaultBufferViewModel bufferViewModel = bufferViewModels.get(i);
            bufferViewModel.setName(bufferView.getName());
            
            int bufferIndex = bufferView.getBuffer();
            BufferModel bufferModel = bufferModels.get(bufferIndex);
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
            DefaultMeshModel meshModel = meshModels.get(i);
            meshModel.setName(mesh.getName());
            
            List<MeshPrimitive> primitives = 
                Optionals.of(mesh.getPrimitives());
            for (MeshPrimitive meshPrimitive : primitives)
            {
                MeshPrimitiveModel meshPrimitiveModel = 
                    createMeshPrimitiveModel(meshPrimitive);
                meshModel.addMeshPrimitiveModel(meshPrimitiveModel);
            }
        }
    }
    
    /**
     * Create a {@link MeshPrimitiveModel} for the given 
     * {@link MeshPrimitive}.<br>
     * <br>
     * Note: The resulting {@link MeshPrimitiveModel} will not have any
     * {@link MaterialModel} assigned. The material model may have to
     * be instantiated multiple times, with different {@link TechniqueModel}
     * instances. This is done in {@link #instantiateMaterialModels()}
     * 
     * @param meshPrimitive The {@link MeshPrimitive}
     * @return The {@link MeshPrimitiveModel}
     */
    private DefaultMeshPrimitiveModel createMeshPrimitiveModel(
        MeshPrimitive meshPrimitive)
    {
        Integer mode = Optionals.of(
            meshPrimitive.getMode(), 
            meshPrimitive.defaultMode());
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
        
        List<Map<String, Integer>> morphTargets =
            Optionals.of(meshPrimitive.getTargets());
        for (Map<String, Integer> morphTarget : morphTargets)
        {
            Map<String, AccessorModel> morphTargetModel = 
                new LinkedHashMap<String, AccessorModel>();
            for (Entry<String, Integer> entry : morphTarget.entrySet())
            {
                String attribute = entry.getKey();
                Integer accessorIndex = entry.getValue();
                DefaultAccessorModel accessorModel = 
                    accessorModels.get(accessorIndex);
                morphTargetModel.put(attribute, accessorModel);
            }
            meshPrimitiveModel.addTarget(
                Collections.unmodifiableMap(morphTargetModel));
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
            nodeModel.setName(node.getName());
            
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
            nodeModel.setMatrix(Optionals.clone(matrix));
            nodeModel.setTranslation(Optionals.clone(translation));
            nodeModel.setRotation(Optionals.clone(rotation));
            nodeModel.setScale(Optionals.clone(scale));
            
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
            sceneModel.setName(scene.getName());
            
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
            skinModel.setName(skin.getName());
            
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
            textureModel.setName(texture.getName());
            
            Integer imageIndex = texture.getSource();
            DefaultImageModel imageModel = imageModels.get(imageIndex);
            textureModel.setImageModel(imageModel);
        }
    }
    
    /**
     * Initialize the {@link ImageModel} instances
     */
    private void initImageModels()
    {
        List<Image> images = Optionals.of(gltf.getImages());
        for (int i = 0; i < images.size(); i++)
        {
            Image image = images.get(i);
            DefaultImageModel imageModel = imageModels.get(i);
            imageModel.setName(image.getName());
            
            Integer bufferViewIndex = image.getBufferView();
            if (bufferViewIndex != null)
            {
                BufferViewModel bufferViewModel = 
                    bufferViewModels.get(bufferViewIndex);
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
                cameraModel.setName(camera.getName());
                
                cameraModel.setNodeModel(nodeModel);

                String nodeName = Optionals.of(node.getName(), "node" + i);
                String cameraName = 
                    Optionals.of(camera.getName(), "camera" + cameraIndex);
                String instanceName = nodeName + "." + cameraName;
                cameraModel.setInstanceName(instanceName);
                
                cameraModels.add(cameraModel);
            }
        }
    }
    
    /**
     * For each mesh that is instantiated in a node, call
     * {@link #instantiateMaterialModels(Mesh, MeshModel, int)} 
     */
    private void instantiateMaterialModels()
    {
        List<Node> nodes = Optionals.of(gltf.getNodes());
        List<Mesh> meshes = Optionals.of(gltf.getMeshes());
        for (int i = 0; i < nodes.size(); i++)
        {
            Node node = nodes.get(i);
            
            Integer meshIndex = node.getMesh();
            
            if (meshIndex != null)
            {
                MeshModel meshModel = meshModels.get(meshIndex);
                
                int numJoints = 0;
                Integer skinIndex = node.getSkin();
                if (skinIndex != null)
                {
                    SkinModel skinModel = skinModels.get(skinIndex);
                    numJoints = skinModel.getJoints().size();
                }
                Mesh mesh = meshes.get(meshIndex);
                instantiateMaterialModels(mesh, meshModel, numJoints);
            }
        }
    }
    
    /**
     * Create the {@link MaterialModel} instances that are required for
     * rendering the {@link MeshPrimitiveModel} instances of the given 
     * {@link MeshModel}, based on the corresponding {@link MeshPrimitive} 
     * and the given number of joints.
     *  
     * @param mesh The {@link Mesh}
     * @param meshModel The {@link MeshModel}
     * @param numJoints The number of joints
     */
    private void instantiateMaterialModels(
        Mesh mesh, MeshModel meshModel, int numJoints)
    {
        List<MeshPrimitive> meshPrimitives = mesh.getPrimitives();
        List<MeshPrimitiveModel> meshPrimitiveModels = 
            meshModel.getMeshPrimitiveModels();
        
        for (int i = 0; i < meshPrimitives.size(); i++)
        {
            MeshPrimitive meshPrimitive = meshPrimitives.get(i);
            DefaultMeshPrimitiveModel meshPrimitiveModel = 
                (DefaultMeshPrimitiveModel)meshPrimitiveModels.get(i);

            Material material = null;
            Integer materialIndex = meshPrimitive.getMaterial();
            if (materialIndex == null)
            {
                material = Materials.createDefaultMaterial();
            }
            else
            {
                material = gltf.getMaterials().get(materialIndex);
            }
            DefaultMaterialModel materialModel = 
                materialModelHandler.createMaterialModel(material, numJoints);
            materialModel.setName(material.getName());
            
            meshPrimitiveModel.setMaterialModel(materialModel);
            materialModels.add(materialModel);
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
     * Returns the raw glTF object, which is a
     * {@link de.javagl.jgltf.impl.v1.GlTF version 2.0 glTF}.<br>
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
    
}
