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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.UriStrings;

/**
 * Default implementation of a {@link BufferBuilderStrategy}
 */
class DefaultBufferBuilderStrategy implements BufferBuilderStrategy
{
    // TODO Try to retain the names of the model elements, if they are present
    
    /**
     * The {@link BufferStructureBuilder} that is used internally
     */
    private final BufferStructureBuilder bufferStructureBuilder;
    
    /**
     * The {@link BufferStructure} that stores the results
     */
    private BufferStructure bufferStructure;
    
    /**
     * The set of {@link ImageModel} instances that have already been 
     * processed
     */
    private final Set<ImageModel> processedImageModels;
    
    /**
     * The set of {@link AccessorModel} instances that have already been 
     * processed
     */
    private final Set<AccessorModel> processedAccessorModels;
    
    /**
     * A mapping from image models to buffer views that may have been
     * created for them.
     */
    private final Map<ImageModel, BufferViewModel> imageBufferViews;
    
    /**
     * A mapping from image models to the URI that they had originally.
     */
    private final Map<ImageModel, String> imageUriStrings;
    
    /**
     * A mapping from image models to the URI that they had originally.
     */
    private final Set<String> existingImageUriStrings;

    /**
     * A <b>package-private</b> class storing the configuration settings
     */
    static class Config
    {
        /**
         * Whether to create one buffer per mesh primitive
         */
        boolean bufferPerMeshPrimitive = false;
        
        /**
         * Whether to create one buffer per mesh
         */
        boolean bufferPerMesh = false;
        
        /**
         * Whether to create one buffer for all meshes
         */
        boolean bufferForMeshes = false;

        /**
         * Whether to create one buffer per animation
         */
        boolean bufferPerAnimation = false;
        
        /**
         * Whether to create one buffer for all animations
         */
        boolean bufferForAnimations = false;
        
        /**
         * Whether to create one buffer per skin
         */
        boolean bufferPerSkin = false;
        
        /**
         * Whether to create one buffer for all skins
         */
        boolean bufferForSkins = false;
        
        /**
         * Whether images should be stored in buffer views
         */
        boolean imagesInBufferViews = false;
        
        /**
         * Whether to create one buffer per image
         */
        boolean bufferPerImage = false;
        
        /**
         * Whether to create one buffer for all images
         */
        boolean bufferForImages = false;

        /**
         * Whether to create one buffer for all additional accessors
         */
        boolean bufferForAdditionalAccessors = false;
    }
    
    /**
     * The configuration settings
     */
    private final Config config;
    
    /**
     * Default constructor
     * 
     * @param config The configuration 
     */
    DefaultBufferBuilderStrategy(Config config)
    {
        this.config = config;

        bufferStructureBuilder = new BufferStructureBuilder(); 
        processedImageModels = new LinkedHashSet<ImageModel>();
        processedAccessorModels = new LinkedHashSet<AccessorModel>();
        imageBufferViews = new LinkedHashMap<ImageModel, BufferViewModel>();
        imageUriStrings = new LinkedHashMap<ImageModel, String>();
        existingImageUriStrings = new LinkedHashSet<String>();
    }
    
    
    @Override
    public void process(GltfModel gltfModel)
    {
        processMeshModels(gltfModel.getMeshModels());
        processAnimationModels(gltfModel.getAnimationModels());
        processSkinModels(gltfModel.getSkinModels());
        processImageModels(gltfModel.getImageModels());
        processAccessorModels(gltfModel.getAccessorModels());
        commitBuffer();
        bufferStructure = bufferStructureBuilder.build();
    }
    
    
    /**
     * Process the given {@link MeshModel} instances
     * 
     * @param meshModels The {@link MeshModel} instances
     */
    private void processMeshModels(
        Collection<? extends MeshModel> meshModels)
    {
        if (config.bufferForMeshes)
        {
            commitBuffer();
        }
        for (MeshModel meshModel : meshModels)
        {
            processMeshModel(meshModel);
        }
        if (config.bufferForMeshes)
        {
            commitBuffer();
        }
    }

    /**
     * Process the given {@link MeshModel}
     * 
     * @param meshModel The {@link MeshModel}
     */
    private void processMeshModel(MeshModel meshModel)
    {
        if (config.bufferPerMesh)
        {
            commitBuffer();
        }
        List<MeshPrimitiveModel> meshPrimitives = 
            meshModel.getMeshPrimitiveModels();
        for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitives)
        {
            processMeshPrimitiveModel(meshPrimitiveModel);
        }
        if (config.bufferPerMesh)
        {
            commitBuffer();
        }
    }
    
    /**
     * Process the given {@link MeshPrimitiveModel}
     * 
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     */
    private void processMeshPrimitiveModel(
        MeshPrimitiveModel meshPrimitiveModel)
    {
        if (config.bufferPerMeshPrimitive)
        {
            commitBuffer();
        }
        AccessorModel indices = meshPrimitiveModel.getIndices();
        if (indices != null)
        {
            if (!processedAccessorModels.contains(indices))
            {
                bufferStructureBuilder.addAccessorModel("indices", 
                    (DefaultAccessorModel)indices);
                processedAccessorModels.add(indices);
                bufferStructureBuilder.createArrayElementBufferViewModel(
                    "indices");
            }
        }
        Collection<AccessorModel> attributes = 
            meshPrimitiveModel.getAttributes().values();
        for (AccessorModel attribute : attributes)
        {
            if (!processedAccessorModels.contains(attribute))
            {
                bufferStructureBuilder.addAccessorModel("attribute", 
                    (DefaultAccessorModel)attribute);
                processedAccessorModels.add(attribute);
            }
        }
        if (bufferStructureBuilder.getNumCurrentAccessorModels() > 0)
        {
            bufferStructureBuilder.createArrayBufferViewModel("attributes");
        }
        
        List<Map<String, AccessorModel>> targets =
            meshPrimitiveModel.getTargets();
        if (!targets.isEmpty())
        {
            for (Map<String, AccessorModel> target : targets)
            {
                for (AccessorModel targetValue : target.values())
                {
                    if (!processedAccessorModels.contains(targetValue))
                    {
                        bufferStructureBuilder.addAccessorModel("target",
                            (DefaultAccessorModel) targetValue);
                        processedAccessorModels.add(targetValue);
                    }
                }
            }
            if (bufferStructureBuilder.getNumCurrentAccessorModels() > 0)
            {
                bufferStructureBuilder.createArrayBufferViewModel("targets");
            }
        }
        if (config.bufferPerMeshPrimitive)
        {
            commitBuffer();
        }
    }
    
    /**
     * Process the given {@link AnimationModel} instances
     * 
     * @param animationModels The {@link AnimationModel} instances
     */
    private void processAnimationModels(
        Collection<? extends AnimationModel> animationModels)
    {
        if (config.bufferForAnimations)
        {
            commitBuffer();
        }
         for (AnimationModel animationModel : animationModels)
         {
             processAnimationModel(animationModel);
         }
         if (config.bufferForAnimations)
         {
             commitBuffer();
         }
    }
    
    /**
     * Process the given {@link AnimationModel} 
     * 
     * @param animationModel The {@link AnimationModel}
     */
    private void processAnimationModel(AnimationModel animationModel)
    {
        if (config.bufferPerAnimation)
        {
            commitBuffer();
        }
        for (Channel channel : animationModel.getChannels())
        {
            Sampler sampler = channel.getSampler();
            AccessorModel input = sampler.getInput();
            AccessorModel output = sampler.getOutput();
            if (!processedAccessorModels.contains(input))
            {
                bufferStructureBuilder.addAccessorModel(
                    "animation input", (DefaultAccessorModel) input);
                processedAccessorModels.add(input);
            }
            if (!processedAccessorModels.contains(output))
            {
                bufferStructureBuilder.addAccessorModel(
                    "animation output", (DefaultAccessorModel) output);
                processedAccessorModels.add(output);
            }
        }
        if (!animationModel.getChannels().isEmpty())
        {
            bufferStructureBuilder.createBufferViewModel("animation", null);
        }
        if (config.bufferPerAnimation)
        {
            commitBuffer();
        }
    }
    

    /**
     * Process the given {@link SkinModel} instances
     * 
     * @param skinModels The {@link SkinModel} instances
     */
    private void processSkinModels(
        Collection<? extends SkinModel> skinModels)
    {
        if (config.bufferForSkins)
        {
            commitBuffer();
        }
        for (SkinModel skinModel : skinModels)
        {
            processSkinModel(skinModel);
        }
        if (config.bufferForSkins)
        {
            commitBuffer();
        }
    }
    
    /**
     * Process the given {@link SkinModel} 
     * 
     * @param skinModel The {@link SkinModel}
     */
    private void processSkinModel(SkinModel skinModel)
    {
        if (config.bufferPerSkin)
        {
            commitBuffer();
        }
        AccessorModel ibm = skinModel.getInverseBindMatrices();
        if (ibm != null) 
        {
            if (!processedAccessorModels.contains(ibm))
            {
                bufferStructureBuilder.addAccessorModel(
                    "inverse bind matrices", (DefaultAccessorModel) ibm);
                processedAccessorModels.add(ibm);
                bufferStructureBuilder.createBufferViewModel("skin", null);
            }
        }
        if (config.bufferPerSkin)
        {
            commitBuffer();
        }
    }

    /**
     * Process the given {@link ImageModel} instances
     * 
     * @param imageModels The {@link ImageModel} instances
     */
    private void processImageModels(
        Collection<? extends ImageModel> imageModels)
    {
        if (config.bufferForImages)
        {
            commitBuffer();
        }
        for (ImageModel imageModel : imageModels)
        {
            processImageModel(imageModel);
        }
        if (config.bufferForImages)
        {
            commitBuffer();
        }
    }
    
    /**
     * Process the given {@link ImageModel} 
     * 
     * @param imageModel The {@link ImageModel}
     */
    private void processImageModel(ImageModel imageModel)
    {
        if (processedImageModels.contains(imageModel))
        {
            return;
        }
        processedImageModels.add(imageModel);
        
        String uri = imageModel.getUri();
        if (uri != null)
        {
            imageUriStrings.put(imageModel, uri);
            existingImageUriStrings.add(uri);
        }

        if (config.bufferPerImage) 
        {
            commitBuffer();
        }
        if (config.imagesInBufferViews)
        {
            ByteBuffer imageData = imageModel.getImageData();
            BufferViewModel bufferViewModel = 
                bufferStructureBuilder.createImageBufferViewModel(
                    "image", imageData);
            imageBufferViews.put(imageModel, bufferViewModel);
        }
        if (config.bufferPerImage) 
        {
            commitBuffer();
        }
    }
    
    
    /**
     * Process the given {@link AccessorModel} instances
     * 
     * @param accessorModels The {@link AccessorModel} instances
     */
    private void processAccessorModels(
        Collection<? extends AccessorModel> accessorModels)
    {
        if (config.bufferForAdditionalAccessors)
        {
            commitBuffer();
        }
        for (AccessorModel accessorModel : accessorModels)
        {
            processAccessorModel(accessorModel);
        }
        if (config.bufferForAdditionalAccessors)
        {
            commitBuffer();
        }
    }

    /**
     * Process the given {@link AccessorModel} 
     * 
     * @param accessorModel The {@link AccessorModel}
     */
    private void processAccessorModel(AccessorModel accessorModel)
    {
        if (processedAccessorModels.contains(accessorModel))
        {
            return;
        }
        processedAccessorModels.add(accessorModel);
        bufferStructureBuilder.addAccessorModel("additional", 
            (DefaultAccessorModel) accessorModel);
        bufferStructureBuilder.createBufferViewModel("additional", null); 
    }
    
    
    /**
     * Commit the currently pending buffer view models into a buffer
     */
    private void commitBuffer()
    {
        if (bufferStructureBuilder.getNumCurrentBufferViewModels() > 0)
        {
            int index = bufferStructureBuilder.getNumBufferModels();
            String uri = "buffer" + index + ".bin";
            bufferStructureBuilder.createBufferModel("buffer", uri);
        }
    }
    
    
    
    @Override
    public List<DefaultAccessorModel> getAccessorModels()
    {
        if (bufferStructure == null)
        {
            throw new IllegalStateException(
                "No input model has been processed");
        }
        return bufferStructure.getAccessorModels();
    }

    @Override
    public List<DefaultBufferViewModel> getBufferViewModels()
    {
        if (bufferStructure == null)
        {
            throw new IllegalStateException(
                "No input model has been processed");
        }
        return bufferStructure.getBufferViewModels();
    }

    @Override
    public List<DefaultBufferModel> getBufferModels()
    {
        if (bufferStructure == null)
        {
            throw new IllegalStateException(
                "No input model has been processed");
        }
        return bufferStructure.getBufferModels();
    }
    
    @Override
    public void validateImageModel(DefaultImageModel imageModel)
    {
        BufferViewModel imageBufferViewModel = 
            imageBufferViews.get(imageModel);
        if (imageBufferViewModel == null)
        {
            String oldUriString = imageUriStrings.get(imageModel);
            String newUriString = oldUriString;
            if (oldUriString == null) 
            {
                newUriString = UriStrings.createImageUriString(
                    imageModel, existingImageUriStrings);
                existingImageUriStrings.add(newUriString);
                imageUriStrings.put(imageModel, newUriString);
            }
            imageModel.setUri(newUriString);
            imageModel.setBufferViewModel(null);
        } 
        else 
        {
            imageModel.setBufferViewModel(imageBufferViewModel);
            imageModel.setUri(null);
        }
    }

    
   
    
}