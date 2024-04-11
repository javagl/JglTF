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
import java.util.Map;
import java.util.Set;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;

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
     * Whether the vertex attribute accessors should all refer to
     * a single buffer view
     */
    private final boolean useSingleVertexAttributesBufferView = false;
    
    /**
     * The set of {@link MeshModel} instances that have already been 
     * processed
     */
    private final Set<MeshModel> processedMeshModels;
    
    /**
     * The set of {@link MeshPrimitiveModel} instances that have already been 
     * processed
     */
    private final Set<MeshPrimitiveModel> processedMeshPrimitiveModels;
    
    /**
     * The set of {@link ImageModel} instances that have already been 
     * processed
     */
    private final Set<ImageModel> processedImageModels;
    
    /**
     * The set of {@link AnimationModel} instances that have already been 
     * processed
     */
    private final Set<AnimationModel> processedAnimationModels;
    
    /**
     * The set of {@link SkinModel} instances that have already been 
     * processed
     */
    private final Set<SkinModel> processedSkinModels;
    
    /**
     * The set of {@link AccessorModel} instances that have already been 
     * processed
     */
    private final Set<AccessorModel> processedAccessorModels;
    
    
    /**
     * Default constructor
     */
    DefaultBufferBuilderStrategy()
    {
        bufferStructureBuilder = new BufferStructureBuilder(); 
        processedMeshModels = new LinkedHashSet<MeshModel>();
        processedMeshPrimitiveModels = new LinkedHashSet<MeshPrimitiveModel>();
        processedImageModels = new LinkedHashSet<ImageModel>();
        processedAnimationModels = new LinkedHashSet<AnimationModel>();
        processedSkinModels = new LinkedHashSet<SkinModel>();
        processedAccessorModels = new LinkedHashSet<AccessorModel>();
    }
    
    @Override
    public void processMeshModels(
        Collection<? extends DefaultMeshModel> meshModels)
    {
        for (DefaultMeshModel meshModel : meshModels)
        {
            processMeshModel(meshModel);
        }
    }

    /**
     * Process the given {@link MeshModel}
     * 
     * @param meshModel The {@link MeshModel}
     */
    private void processMeshModel(MeshModel meshModel)
    {
        if (processedMeshModels.contains(meshModel))
        {
            return;
        }
        processedMeshModels.add(meshModel);
        
        List<MeshPrimitiveModel> meshPrimitives = 
            meshModel.getMeshPrimitiveModels();
        for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitives)
        {
            processMeshPrimitiveModel(meshPrimitiveModel);
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
        if (processedMeshPrimitiveModels.contains(meshPrimitiveModel))
        {
            return;
        }
        processedMeshPrimitiveModels.add(meshPrimitiveModel);
        
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
            if (bufferStructureBuilder.getNumCurrentAccessorModels() > 0) 
            {
                if (!useSingleVertexAttributesBufferView)
                {
                    bufferStructureBuilder.createArrayBufferViewModel(
                        "attribute");
                }
            }
        }
        if (bufferStructureBuilder.getNumCurrentAccessorModels() > 0)
        {
            if (useSingleVertexAttributesBufferView)
            {
                bufferStructureBuilder.createArrayBufferViewModel("attributes");
            }
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
    }
    
    @Override
    public void processImageModels(
        Collection<? extends DefaultImageModel> imageModels)
    {
        for (DefaultImageModel imageModel : imageModels)
        {
            processImageModel(imageModel);
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
        // By default, the data of each image will be stored under its URI
    }

    @Override
    public void processAnimationModels(
        Collection<? extends DefaultAnimationModel> animationModels)
    {
         for (DefaultAnimationModel animationModel : animationModels)
         {
             processAnimationModel(animationModel);
         }
    }
    
    /**
     * Process the given {@link AnimationModel} 
     * 
     * @param animationModel The {@link AnimationModel}
     */
    private void processAnimationModel(AnimationModel animationModel)
    {
        if (processedAnimationModels.contains(animationModel))
        {
            return;
        }
        processedAnimationModels.add(animationModel);
        
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
    }
    

    @Override
    public void processSkinModels(
        Collection<? extends DefaultSkinModel> skinModels)
    {
        for (DefaultSkinModel skinModel : skinModels)
        {
            processSkinModel(skinModel);
        }
    }
    
    /**
     * Process the given {@link SkinModel} 
     * 
     * @param skinModel The {@link SkinModel}
     */
    private void processSkinModel(SkinModel skinModel)
    {
        if (processedSkinModels.contains(skinModel))
        {
            return;
        }
        processedSkinModels.add(skinModel);
        
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
    }
    
    @Override
    public void processAccessorModels(
        Collection<? extends DefaultAccessorModel> accessorModels)
    {
        for (DefaultAccessorModel accessorModel : accessorModels)
        {
            if (!processedAccessorModels.contains(accessorModel))
            {
                bufferStructureBuilder.addAccessorModel(
                    "additional", accessorModel);
                processedAccessorModels.add(accessorModel);
                bufferStructureBuilder.createArrayBufferViewModel(
                    "additional");
            }
        }
    }
    
    @Override
    public void commitBuffer(String uri)
    {
        if (bufferStructureBuilder.getNumCurrentBufferViewModels() > 0)
        {
            bufferStructureBuilder.createBufferModel("buffer", uri);
        }
    }
    
    @Override
    public void finish()
    {
        bufferStructure = bufferStructureBuilder.build();
    }

    @Override
    public List<DefaultAccessorModel> getAccessorModels()
    {
        return bufferStructure.getAccessorModels();
    }

    @Override
    public List<DefaultBufferViewModel> getBufferViewModels()
    {
        return bufferStructure.getBufferViewModels();
    }

    @Override
    public List<DefaultBufferModel> getBufferModels()
    {
        return bufferStructure.getBufferModels();
    }
    
    
}