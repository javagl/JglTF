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

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AssetModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.OcclusionTextureInfoModel;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.PbrMetallicRoughnessModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.impl.DefaultAssetModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;

/**
 * An internal class that just summarizes the process of creating the
 * extension model objects for all top-level elements of a {@link GltfModel}. 
 */
class GltfModelExtensionProcessorV2
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModelExtensionProcessorV2.class.getName());

    /**
     * The {@link GltfModel}
     */
    private final DefaultGltfModel gltfModel;
    
    /**
     * Creates a new instance
     * 
     * @param gltfModel The {@link GltfModel}
     */
    GltfModelExtensionProcessorV2(DefaultGltfModel gltfModel)
    {
        this.gltfModel = Objects.requireNonNull(gltfModel, 
            "The gltfModel may not be null");
    }
    
    /**
     * Create and initialize all models
     */
    void process()
    {
        processGltfModelExtensions();
        processAssetModelExtensions();
        processAccessorModelsExtensions();
        processAnimationModelsExtensions();
        processBufferModelsExtensions();
        processBufferViewModelsExtensions();
        processCameraModelsExtensions();
        processImageModelsExtensions();
        processMaterialModelsExtensions();
        processMeshModelsExtensions();
        processNodeModelsExtensions();
        processSceneModelsExtensions();
        processSkinModelsExtensions();
        processTextureModelsExtensions();
    }
    
    /**
     * Create the extension models for the {@link GltfModel} object.
     */
    private void processGltfModelExtensions()
    {
        ExtensionModels.createExtensionModels(
            gltfModel, gltfModel, GltfModel.class);
    }

    /**
     * Create the extension models for the {@link AssetModel} object.
     */
    private void processAssetModelExtensions()
    {
        DefaultAssetModel assetModel = gltfModel.getAssetModel();
        ExtensionModels.createExtensionModels(
            gltfModel, assetModel, AssetModel.class);
    }

    /**
     * Create the extension models for the {@link AccessorModel} objects.
     */
    private void processAccessorModelsExtensions()
    {
        List<AccessorModel> accessorModels = gltfModel.getAccessorModels();
        for (AccessorModel accessorModel : accessorModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, accessorModel, AccessorModel.class);
        }
    }
    
    /**
     * Create the extension models for the {@link AnimationModel} objects.
     */
    private void processAnimationModelsExtensions()
    {
        List<AnimationModel> animationModels = gltfModel.getAnimationModels();
        for (AnimationModel animationModel : animationModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, animationModel, AnimationModel.class);
        }
    }
    
    /**
     * Create the extension models for the {@link BufferModel} objects.
     */
    private void processBufferModelsExtensions()
    {
        List<BufferModel> bufferModels = gltfModel.getBufferModels();
        for (BufferModel bufferModel : bufferModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, bufferModel, BufferModel.class);
        }
    }
    
    /**
     * Create the extension models for the {@link BufferViewModel} objects.
     */
    private void processBufferViewModelsExtensions()
    {
        List<BufferViewModel> bufferViewModels = gltfModel.getBufferViewModels();
        for (BufferViewModel bufferViewModel : bufferViewModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, bufferViewModel, BufferViewModel.class);
        }
    }
    
    /**
     * Create the extension models for the {@link CameraModel} objects.
     */
    private void processCameraModelsExtensions()
    {
        List<CameraModel> cameraModels = gltfModel.getCameraModels();
        for (CameraModel cameraModel : cameraModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, cameraModel, CameraModel.class);
        }
    }
    
    /**
     * Create the extension models for the {@link ImageModel} objects.
     */
    private void processImageModelsExtensions()
    {
        List<ImageModel> imageModels = gltfModel.getImageModels();
        for (ImageModel imageModel : imageModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, imageModel, ImageModel.class);
        }
    }
    
    /**
     * Create the extension models for all {@link MaterialModel} objects.
     */
    private void processMaterialModelsExtensions()
    {
        List<MaterialModel> materialModels = gltfModel.getMaterialModels();
        for (MaterialModel materialModel : materialModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, materialModel, MaterialModel.class);
            
            if (materialModel instanceof PbrMaterialModel) 
            {
                PbrMaterialModel pbrMaterialModel = 
                    (PbrMaterialModel) materialModel;
                processPbrMaterialModelExtensions(pbrMaterialModel);
            }
            else
            {
                // TODO It could be nice if this was capable of handling
                // TechniqueMaterialModel instances
                logger.warning("Unknown material type: " + materialModel);
            }
        }
    }

    /**
     * Create the extension models for the given {@link PbrMaterialModel}.
     * 
     * @param pbrMaterialModel The {@link PbrMaterialModel}
     */
    private void processPbrMaterialModelExtensions(
        PbrMaterialModel pbrMaterialModel)
    {
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel = 
            pbrMaterialModel.getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel != null)
        {
            ExtensionModels.createExtensionModels(gltfModel, 
                pbrMetallicRoughnessModel, 
                PbrMetallicRoughnessModel.class);
            
            TextureInfoModel baseColorTextureInfoModel = 
                pbrMetallicRoughnessModel.getBaseColorTextureInfoModel();
            if (baseColorTextureInfoModel != null)
            {
                ExtensionModels.createExtensionModels(gltfModel, 
                    baseColorTextureInfoModel, 
                    TextureInfoModel.class);
            }
            
            TextureInfoModel metallicRoughnessTextureInfoModel =
                pbrMetallicRoughnessModel.getMetallicRoughnessTextureInfoModel();
            if (metallicRoughnessTextureInfoModel != null)
            {
                ExtensionModels.createExtensionModels(gltfModel, 
                    metallicRoughnessTextureInfoModel, 
                    TextureInfoModel.class);
            }
        }
        
        NormalTextureInfoModel normalTextureInfoModel = 
            pbrMaterialModel.getNormalTextureInfoModel();
        if (normalTextureInfoModel != null)
        {
            ExtensionModels.createExtensionModels(gltfModel, 
                normalTextureInfoModel, 
                TextureInfoModel.class);
        }
        OcclusionTextureInfoModel occlusionTextureInfoModel = 
            pbrMaterialModel.getOcclusionTextureInfoModel();
        if (occlusionTextureInfoModel != null)
        {
            ExtensionModels.createExtensionModels(gltfModel, 
                occlusionTextureInfoModel, 
                TextureInfoModel.class);
        }
        TextureInfoModel emissiveTextureInfoModel = 
            pbrMaterialModel.getEmissiveTextureInfoModel();
        if (emissiveTextureInfoModel != null)
        {
            ExtensionModels.createExtensionModels(gltfModel, 
                emissiveTextureInfoModel, 
                TextureInfoModel.class);
        }
    }
    
    /**
     * Process the {@link MeshModel} instances
     */
    private void processMeshModelsExtensions()
    {
        List<MeshModel> meshModels = gltfModel.getMeshModels();
        for (MeshModel meshModel : meshModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, meshModel, MeshModel.class);

            List<MeshPrimitiveModel> meshPrimitiveModels =
                meshModel.getMeshPrimitiveModels();
            for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitiveModels)
            {
                ExtensionModels.createExtensionModels(
                    gltfModel, meshPrimitiveModel, MeshPrimitiveModel.class);
            }
        }
    }

    /**
     * Process the {@link NodeModel} instances
     */
    private void processNodeModelsExtensions()
    {
        List<NodeModel> nodeModels = gltfModel.getNodeModels();
        for (NodeModel nodeModel : nodeModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, nodeModel, NodeModel.class);
        }
    }

    /**
     * Process the {@link SceneModel} instances
     */
    private void processSceneModelsExtensions()
    {
        List<SceneModel> sceneModels = gltfModel.getSceneModels();
        for (SceneModel sceneModel : sceneModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, sceneModel, SceneModel.class);
        }
    }

    /**
     * Process the {@link SkinModel} instances
     */
    private void processSkinModelsExtensions()
    {
        List<SkinModel> skinModels = gltfModel.getSkinModels();
        for (SkinModel skinModel : skinModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, skinModel, SkinModel.class);
        }
    }

    /**
     * Process the {@link TextureModel} instances
     */
    private void processTextureModelsExtensions()
    {
        List<TextureModel> textureModels = gltfModel.getTextureModels();
        for (TextureModel textureModel : textureModels)
        {
            ExtensionModels.createExtensionModels(
                gltfModel, textureModel, TextureModel.class);
        }
    }
}
