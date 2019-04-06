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
package de.javagl.jgltf.model.creation;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultCameraModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * A class for building {@link GltfModel} instances.<br>
 * <br>
 * Instances may be created with the {@link #create()} method. Note that this 
 * builder assumes that the model instances that are added are the 
 * <i>default</i> implementation, e.g. a {@link DefaultNodeModel}.<br>
 * <br>
 * The builder will collect the model elements that are referred to by model 
 * elements that are added. For example, when adding a {@link NodeModel} with
 * {@link #addNodeModel(NodeModel)}, then all child nodes will be added 
 * as well. So it is possible to manually construct a complete 
 * {@link DefaultSceneModel}, and then only call 
 * {@link #addSceneModel(SceneModel)}, and all required model elements
 * will be added internally.<br>
 * <br>  
 * The {@link #build()} method will then collect the {@link AccessorModel}
 * objects from all scene elements and create the required 
 * {@link BufferViewModel} and {@link BufferModel} instances. The exact
 * strategy of how these models are created is not yet specified or
 * configurable.
 */
public class GltfModelBuilder
{
    /**
     * Creates a new instance
     * 
     * @return The {@link GltfModelBuilder}
     */
    public static GltfModelBuilder create()
    {
        return new GltfModelBuilder();
    }

    /**
     * The set of {@link AnimationModel} objects
     */
    private final Set<DefaultAnimationModel> animationModelsSet;
    
    /**
     * The set of {@link CameraModel} objects
     */
    private final Set<DefaultCameraModel> cameraModelsSet;

    /**
     * The set of {@link ImageModel} objects
     */
    private final Set<DefaultImageModel> imageModelsSet;

    /**
     * The set of {@link MaterialModel} objects
     */
    private final Set<MaterialModel> materialModelsSet;

    /**
     * The set of {@link MeshModel} objects
     */
    private final Set<DefaultMeshModel> meshModelsSet;

    /**
     * The set of {@link NodeModel} objects
     */
    private final Set<DefaultNodeModel> nodeModelsSet;

    /**
     * The set of {@link SceneModel} objects
     */
    private final Set<DefaultSceneModel> sceneModelsSet;

    /**
     * The set of {@link SkinModel} objects
     */
    private final Set<DefaultSkinModel> skinModelsSet;

    /**
     * The set of {@link TextureModel} objects
     */
    private final Set<DefaultTextureModel> textureModelsSet;

    /**
     * Private constructor
     */
    private GltfModelBuilder()
    {
        this.animationModelsSet = new LinkedHashSet<DefaultAnimationModel>();
        this.cameraModelsSet = new LinkedHashSet<DefaultCameraModel>();
        this.imageModelsSet = new LinkedHashSet<DefaultImageModel>();
        this.materialModelsSet = new LinkedHashSet<MaterialModel>();
        this.meshModelsSet = new LinkedHashSet<DefaultMeshModel>();
        this.nodeModelsSet = new LinkedHashSet<DefaultNodeModel>();
        this.sceneModelsSet = new LinkedHashSet<DefaultSceneModel>();
        this.skinModelsSet = new LinkedHashSet<DefaultSkinModel>();
        this.textureModelsSet = new LinkedHashSet<DefaultTextureModel>();
    }
    
    /**
     * Build the {@link GltfModel} containing all elements that have been
     * added to this builder.
     * 
     * @return The {@link GltfModel}
     */
    public DefaultGltfModel build()
    {
        BufferBuilderStrategy bufferBuilderStrategy = 
            new DefaultBufferBuilderStrategy();
        
        bufferBuilderStrategy.processMeshModels(meshModelsSet);
        bufferBuilderStrategy.processImageModels(imageModelsSet);
        bufferBuilderStrategy.processAnimationModels(animationModelsSet);
        bufferBuilderStrategy.processSkinModels(skinModelsSet);
        
        bufferBuilderStrategy.finish();
        
        DefaultGltfModel gltfModel = new DefaultGltfModel();
        
        gltfModel.addAnimationModels(animationModelsSet);
        gltfModel.addCameraModels(cameraModelsSet);
        gltfModel.addImageModels(imageModelsSet);
        gltfModel.addMaterialModels(materialModelsSet);
        gltfModel.addMeshModels(meshModelsSet);
        gltfModel.addNodeModels(nodeModelsSet);
        gltfModel.addSceneModels(sceneModelsSet);
        gltfModel.addSkinModels(skinModelsSet);
        gltfModel.addTextureModels(textureModelsSet);
        
        gltfModel.addAccessorModels(
            bufferBuilderStrategy.getAccessorModels());
        gltfModel.addBufferViewModels(
            bufferBuilderStrategy.getBufferViewModels());
        gltfModel.addBufferModels(
            bufferBuilderStrategy.getBufferModels());
        
        return gltfModel;
    }
    
    
    
    
    /**
     * Add the given {@link AnimationModel}
     *
     * @param animationModel The instance to add
     */
    public void addAnimationModel(AnimationModel animationModel)
    {
        if (animationModel == null)
        {
            return;
        }
        DefaultAnimationModel defaultAnimationModel = 
            (DefaultAnimationModel)animationModel;
        boolean added = animationModelsSet.add(defaultAnimationModel);
        if (added) 
        {
            for (Channel channel : animationModel.getChannels())
            {
                addNodeModel(channel.getNodeModel());
            }
        }
    }
    

    /**
     * Add the given {@link AnimationModel} instances
     *
     * @param animationModels The instances to add
     */
    public void addAnimationModels(
        Collection<? extends AnimationModel> animationModels)
    {
        for (AnimationModel animationModel : animationModels)
        {
            addAnimationModel(animationModel);
        }
    }


    /**
     * Add the given {@link CameraModel}
     * 
     * @param cameraModel The instance to add
     */
    public void addCameraModel(CameraModel cameraModel)
    {
        if (cameraModel == null)
        {
            return;
        }
        DefaultCameraModel defaultCameraModel = (DefaultCameraModel)cameraModel;
        cameraModelsSet.add(defaultCameraModel);
    }

    /**
     * Add the given {@link CameraModel} instances
     * 
     * @param cameraModels The instances to add
     */
    public void addCameraModels(
        Collection<? extends CameraModel> cameraModels)
    {
        for (CameraModel cameraModel : cameraModels)
        {
            addCameraModel(cameraModel);
        }
    }

    /**
     * Add the given {@link ImageModel}
     * 
     * @param imageModel The instance to add
     */
    public void addImageModel(ImageModel imageModel)
    {
        if (imageModel == null)
        {
            return;
        }
        DefaultImageModel defaultImageModel = (DefaultImageModel)imageModel;
        imageModelsSet.add(defaultImageModel);
    }

    /**
     * Add the given {@link ImageModel} instances
     * 
     * @param imageModels The instances to add
     */
    public void addImageModels(
        Collection<? extends ImageModel> imageModels)
    {
        for (ImageModel imageModel : imageModels)
        {
            addImageModel(imageModel);
        }
    }

    /**
     * Add the given {@link MaterialModel}
     * 
     * @param materialModel The instance to add
     */
    public void addMaterialModel(MaterialModel materialModel)
    {
        if (materialModel == null)
        {
            return;
        }
        boolean added = materialModelsSet.add(materialModel);
        if (added)
        {
            if (materialModel instanceof MaterialModelV2)
            {
                MaterialModelV2 materialModelV2 = 
                    (MaterialModelV2)materialModel;
                addTextureModel(materialModelV2.getBaseColorTexture());
                addTextureModel(materialModelV2.getOcclusionTexture());
                addTextureModel(materialModelV2.getMetallicRoughnessTexture());
                addTextureModel(materialModelV2.getEmissiveTexture());
            }
        }
    }

    /**
     * Add the given {@link MaterialModel} instances
     * 
     * @param materialModels The instances to add
     */
    public void addMaterialModels(
        Collection<? extends MaterialModel> materialModels)
    {
        for (MaterialModel materialModel : materialModels)
        {
            addMaterialModel(materialModel);
        }
    }

    /**
     * Add the given {@link MeshModel}
     * 
     * @param meshModel The instance to add
     */
    public void addMeshModel(MeshModel meshModel)
    {
        if (meshModel == null)
        {
            return;
        }
        DefaultMeshModel defaultMeshModel = (DefaultMeshModel)meshModel;
        boolean added = meshModelsSet.add(defaultMeshModel);
        if (added) 
        {
            List<MeshPrimitiveModel> meshPrimitives = 
                meshModel.getMeshPrimitiveModels();
            for (MeshPrimitiveModel meshPrimitive : meshPrimitives)
            {
                addMaterialModel(meshPrimitive.getMaterialModel());
            }
        }
    }

    /**
     * Add the given {@link MeshModel} instances
     * 
     * @param meshModels The instances to add
     */
    public void addMeshModels(
        Collection<? extends MeshModel> meshModels)
    {
        for (MeshModel meshModel : meshModels)
        {
            addMeshModel(meshModel);
        }
    }

    /**
     * Add the given {@link NodeModel}
     * 
     * @param nodeModel The instance to add
     */
    public void addNodeModel(NodeModel nodeModel)
    {
        if (nodeModel == null)
        {
            return;
        }
        DefaultNodeModel defaultNodeModel = (DefaultNodeModel)nodeModel;
        boolean added = nodeModelsSet.add(defaultNodeModel);
        if (added) 
        {
            addCameraModel(nodeModel.getCameraModel());
            addMeshModels(nodeModel.getMeshModels());
            addNodeModels(nodeModel.getChildren());
            addSkinModel(nodeModel.getSkinModel());
        }
    }

    /**
     * Add the given {@link NodeModel} instances
     * 
     * @param nodeModels The instances to add
     */
    public void addNodeModels(
        Collection<? extends NodeModel> nodeModels)
    {
        for (NodeModel nodeModel : nodeModels)
        {
            addNodeModel(nodeModel);
        }
    }

    /**
     * Add the given {@link SceneModel}
     * 
     * @param sceneModel The instance to add
     */
    public void addSceneModel(SceneModel sceneModel)
    {
        if (sceneModel == null)
        {
            return;
        }
        DefaultSceneModel defaultSceneModel = (DefaultSceneModel)sceneModel;
        boolean added = sceneModelsSet.add(defaultSceneModel);
        if (added) 
        {
            addNodeModels(sceneModel.getNodeModels());
        }
    }

    /**
     * Add the given {@link SceneModel} instances
     * 
     * @param sceneModels The instances to add
     */
    public void addSceneModels(
        Collection<? extends SceneModel> sceneModels)
    {
        for (SceneModel sceneModel : sceneModels)
        {
            addSceneModel(sceneModel);
        }
    }

    /**
     * Add the given {@link SkinModel}
     * 
     * @param skinModel The instance to add
     */
    public void addSkinModel(SkinModel skinModel)
    {
        if (skinModel == null)
        {
            return;
        }
        DefaultSkinModel defaultSkinModel = (DefaultSkinModel)skinModel;
        boolean added = skinModelsSet.add(defaultSkinModel);
        if (added) 
        {
            addNodeModels(skinModel.getJoints());
            addNodeModel(skinModel.getSkeleton());
        }
    }

    /**
     * Add the given {@link SkinModel} instances
     * 
     * @param skinModels The instances to add
     */
    public void addSkinModels(
        Collection<? extends SkinModel> skinModels)
    {
        for (SkinModel skinModel : skinModels)
        {
            addSkinModel(skinModel);
        }
    }

    /**
     * Add the given {@link TextureModel}
     * 
     * @param textureModel The instance to add
     */
    public void addTextureModel(TextureModel textureModel)
    {
        if (textureModel == null)
        {
            return;
        }
        DefaultTextureModel defaultTextureModel = 
            (DefaultTextureModel)textureModel;
        boolean added = textureModelsSet.add(defaultTextureModel);
        if (added) 
        {
            addImageModel(textureModel.getImageModel());
        }
    }

    /**
     * Add the given {@link TextureModel} instances
     * 
     * @param textureModels The instances to add
     */
    public void addTextureModels(
        Collection<? extends TextureModel> textureModels)
    {
        for (TextureModel textureModel : textureModels)
        {
            addTextureModel(textureModel);
        }
    }
   
}