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
package de.javagl.jgltf.model.transform;

import java.nio.ByteBuffer;
import java.util.Collection;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.structure.BufferBuilderStrategies;
import de.javagl.jgltf.model.structure.BufferBuilderStrategy;

/**
 * Methods to transform {@link DefaultGltfModel} instances
 */
public class GltfModelTransforms
{
    /**
     * Prune the given glTF model.
     * 
     * This will remove all elements in the model that are not reachable from
     * either the scene- or the animation models.
     * 
     * This will iteratively remove all model elements that became invalid
     * or obsolete due to the removal of the given elements.
     * 
     * @param gltfModel The glTF model
     */
    public static void prune(DefaultGltfModel gltfModel)
    {
        removeAll(gltfModel, null);
    }

    /**
     * Remove the given elements from the given glTF model.
     * 
     * This will iteratively remove all model elements that became invalid
     * or obsolete due to the removal of the given elements.
     * 
     * @param gltfModel The glTF model
     * @param toRemove The elements to remove. If this is <code>null</code>,
     * then only a pruning of the current elements will be performed.
     */
    public static void removeAll(DefaultGltfModel gltfModel,
        Collection<? extends ModelElement> toRemove)
    {
        GltfModelElementCollector c = new GltfModelElementCollector();
        c.process(gltfModel);
        GltfModelPruner.prune(gltfModel, toRemove);
        rebuildBufferStructure(gltfModel);
    }

    /**
     * Revalidate the given glTF model.
     * 
     * This will ensure that the top-level lists of the model and the buffer
     * structure take into account any accessors or images that have been
     * added to model elements.  
     * 
     * @param gltfModel The glTF model
     */
    public static void revalidate(DefaultGltfModel gltfModel)
    {
        GltfModelElementCollector c = new GltfModelElementCollector();
        c.process(gltfModel);
        rebuildBufferStructure(gltfModel);
    }
    
    /**
     * Rebuild the buffer structure for the given glTF model.
     * 
     * This will update all accessor models in the given model to refer
     * to freshly built buffer view- and buffer models.
     * 
     * Some details of this process are intentionally not specified.
     * 
     * @param gltfModel The glTF model
     */
    private static void rebuildBufferStructure(DefaultGltfModel gltfModel)
    {
        BufferBuilderStrategy bbs = BufferBuilderStrategies.createDefault();

        for (AccessorModel am : gltfModel.getAccessorModels())
        {
            DefaultAccessorModel dam = (DefaultAccessorModel) am;
            
            // Reset any byte stride that may have been assigned previously
            dam.setByteStride(dam.getElementSizeInBytes());
            dam.setBufferViewModel(null);
        }
        
        // If any image model referred to a buffer view, then store the
        // buffer view data as the image data, and set the buffer view
        // to null.
        for (ImageModel im : gltfModel.getImageModels())
        {
            DefaultImageModel dim = (DefaultImageModel) im;
            BufferViewModel bufferViewModel = dim.getBufferViewModel();
            if (bufferViewModel != null)
            {
                ByteBuffer bufferViewData = bufferViewModel.getBufferViewData();
                dim.setImageData(bufferViewData);
                dim.setBufferViewModel(null);
            }
        }
        gltfModel.clearBufferModels();
        gltfModel.clearBufferViewModels();

        bbs.process(gltfModel);

        for (ImageModel im : gltfModel.getImageModels())
        {
            DefaultImageModel dim = (DefaultImageModel) im;
            bbs.validateImageModel(dim);
        }
        gltfModel.addBufferViewModels(bbs.getBufferViewModels());
        gltfModel.addBufferModels(bbs.getBufferModels());

    }

    /**
     * Private constructor to prevent instantiation
     */
    private GltfModelTransforms()
    {
        // Private constructor to prevent instantiation
    }
}
