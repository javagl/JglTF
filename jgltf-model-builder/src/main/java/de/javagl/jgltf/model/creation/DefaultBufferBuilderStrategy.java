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
import java.util.List;

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
     * Default constructor
     */
    DefaultBufferBuilderStrategy()
    {
        bufferStructureBuilder = new BufferStructureBuilder(); 
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
        AccessorModel indices = meshPrimitiveModel.getIndices();
        bufferStructureBuilder.addAccessorModel("indices", 
            (DefaultAccessorModel)indices);
        bufferStructureBuilder.createArrayElementBufferViewModel("indices");
        
        Collection<AccessorModel> attributes = 
            meshPrimitiveModel.getAttributes().values();
        for (AccessorModel attribute : attributes)
        {
            bufferStructureBuilder.addAccessorModel("attribute", 
                (DefaultAccessorModel)attribute);
        }
        bufferStructureBuilder.createArrayBufferViewModel("attributes");
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
        for (Channel channel : animationModel.getChannels())
        {
            Sampler sampler = channel.getSampler();
            AccessorModel input = sampler.getInput();
            AccessorModel output = sampler.getOutput();
            bufferStructureBuilder.addAccessorModel(
                "animation input", (DefaultAccessorModel) input);
            bufferStructureBuilder.addAccessorModel(
                "animation output", (DefaultAccessorModel) output);
        }
        if (!animationModel.getChannels().isEmpty())
        {
            bufferStructureBuilder.createArrayBufferViewModel("animation");
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
        AccessorModel ibm = skinModel.getInverseBindMatrices();
        bufferStructureBuilder.addAccessorModel(
            "inverse bind matrices", (DefaultAccessorModel) ibm);
        bufferStructureBuilder.createArrayBufferViewModel("skin");
    }
    
    
    @Override
    public void finish()
    {
        bufferStructureBuilder.createBufferModel("buffer", "buffer.bin");
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