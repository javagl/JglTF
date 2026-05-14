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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import de.javagl.jgltf.model.ModelElements;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.extensions.ExtensionProcessing;
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
     * The set of {@link ImageModel} instances that have already been processed
     */
    private final Set<ImageModel> processedImageModels;

    /**
     * The set of {@link AccessorModel} instances that have already been
     * processed
     */
    private final Set<AccessorModel> processedAccessorModels;

    /**
     * A mapping from image models to buffer views that may have been created
     * for them.
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
     * The configuration settings
     */
    private final BufferBuilderConfig config;
    
    /**
     * Default constructor
     * 
     * @param config The configuration
     */
    DefaultBufferBuilderStrategy(BufferBuilderConfig config)
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
        ExtensionProcessing extensionProcessing = new ExtensionProcessing()
        {
            @Override
            public void acceptAccessorEncoding(
                List<? extends AccessorModel> accessorModels,
                DefaultBufferViewModel bufferViewModel,
                ByteBuffer bufferViewData)
            {
                bufferStructureBuilder.addStandaloneBufferViewModel("extension",
                    bufferViewModel, bufferViewData);
                processedAccessorModels.addAll(accessorModels);
            }
        };
        ExtensionModels.preprocess(gltfModel, extensionProcessing);
        
        processMeshModels(gltfModel.getMeshModels());
        processAnimationModels(gltfModel.getAnimationModels());
        processSkinModels(gltfModel.getSkinModels());
        processImageModels(gltfModel.getImageModels());

        Set<AccessorModel> extensionAccessorModels = ModelElements
            .collectReferencedModelElements(gltfModel, AccessorModel.class);
        extensionAccessorModels.removeAll(processedAccessorModels);
        processAccessorModels(extensionAccessorModels);
        
        commitBuffer();
        bufferStructure = bufferStructureBuilder.build();
    }

    /**
     * Process the given {@link MeshModel} instances
     * 
     * @param meshModels The {@link MeshModel} instances
     */
    private void processMeshModels(Collection<? extends MeshModel> meshModels)
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
    private void
        processMeshPrimitiveModel(MeshPrimitiveModel meshPrimitiveModel)
    {
        if (config.bufferPerMeshPrimitive)
        {
            commitBuffer();
        }
        AccessorModel indices = meshPrimitiveModel.getIndices();
        if (indices != null)
        {
            processAccessorModelOnce("indices", indices);
            commitArrayElementBufferView("indices");
        }
        Map<String, AccessorModel> attributes =
            meshPrimitiveModel.getAttributes();
        for (Entry<String, AccessorModel> entry : attributes.entrySet())
        {
            AccessorModel attribute = entry.getValue();
            processAccessorModelOnce("attribute", attribute);
            if (config.bufferViewPerAttributeAccessor)
            {
                commitArrayBufferView("attributes");
            }
        }
        commitArrayBufferView("attributes");

        List<Map<String, AccessorModel>> targets =
            meshPrimitiveModel.getTargets();
        if (!targets.isEmpty())
        {
            for (Map<String, AccessorModel> target : targets)
            {
                for (AccessorModel targetValue : target.values())
                {
                    processAccessorModelOnce("target", targetValue);
                }
            }
            commitArrayBufferView("targets");
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
            processAccessorModelOnce("animation input", input);
            processAccessorModelOnce("animation output", output);
        }
        commitBufferView("animation");
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
    private void processSkinModels(Collection<? extends SkinModel> skinModels)
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
                processAccessorModelOnce("inverse bind matrices", ibm);
                commitBufferView("skin");        
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
    private void
        processImageModels(Collection<? extends ImageModel> imageModels)
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
            BufferViewModel bufferViewModel = bufferStructureBuilder
                .createStandaloneBufferViewModel("image", imageData);
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
        processAccessorModelOnce("additional", accessorModel);
        commitBufferView("additional");        
    }

    /**
     * Process the given accessor model once.
     * 
     * This will add the given accessor model to the buffer structure builder
     * if it has not been added yet.
     * 
     * @param idPrefix The ID prefix
     * @param accessorModel The accessor model
     */
    private void processAccessorModelOnce(String idPrefix,
        AccessorModel accessorModel)
    {
        if (processedAccessorModels.contains(accessorModel))
        {
            return;
        }
        bufferStructureBuilder.addAccessorModel(idPrefix,
            (DefaultAccessorModel) accessorModel);
        processedAccessorModels.add(accessorModel);
    }

    /**
     * Commit the currently pending accessor models into an array element
     * buffer view (for indices)
     * 
     * @param idPrefix The ID prefix
     */
    private void commitArrayElementBufferView(String idPrefix)
    {
        if (bufferStructureBuilder.getNumCurrentAccessorModels() > 0)
        {
            bufferStructureBuilder.createArrayElementBufferViewModel(idPrefix);
        }
    }
    
    /**
     * Commit the currently pending accessor models into an array buffer view
     * (for vertex attributes)
     * 
     * @param idPrefix The ID prefix
     */
    private void commitArrayBufferView(String idPrefix)
    {
        if (bufferStructureBuilder.getNumCurrentAccessorModels() > 0)
        {
            bufferStructureBuilder.createArrayBufferViewModel(idPrefix);
        }
    }
    
    /**
     * Commit the currently pending accessor models into a buffer view
     * (for a buffer view without a target)
     * 
     * @param idPrefix The ID prefix
     */
    private void commitBufferView(String idPrefix)
    {
        if (bufferStructureBuilder.getNumCurrentAccessorModels() > 0)
        {
            bufferStructureBuilder.createBufferViewModel(idPrefix, null);
        }
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
        List<DefaultAccessorModel> allAccessorModels =
            new ArrayList<DefaultAccessorModel>();
        for (AccessorModel accessorModel : processedAccessorModels)
        {
            allAccessorModels.add((DefaultAccessorModel) accessorModel);
        }
        return allAccessorModels;
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
        BufferViewModel imageBufferViewModel = imageBufferViews.get(imageModel);
        if (imageBufferViewModel == null)
        {
            String oldUriString = imageUriStrings.get(imageModel);
            String newUriString = oldUriString;
            if (oldUriString == null)
            {
                newUriString = UriStrings.createImageUriString(imageModel,
                    existingImageUriStrings);
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