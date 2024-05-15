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
package de.javagl.jgltf.model.io.v1;

import java.util.List;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.structure.GltfModelStructures;
import de.javagl.jgltf.model.v1.GltfModelV1;

/**
 * A class for creating a {@link GltfAssetV1} with a default data 
 * representation from a {@link GltfModelV1}.<br>
 * <br>
 * In the default data representation, elements are referred to via URIs. 
 * Data elements like {@link Buffer}, {@link Image} or {@link Shader}
 * objects that used the binary glTF extension or data URIs will be 
 * converted to refer to their data using URIs.
 */
final class DefaultAssetCreatorV1
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(DefaultAssetCreatorV1.class.getName());
    
    /**
     * Creates a new asset creator
     */
    DefaultAssetCreatorV1()
    {
        // Default constructor
    }

    /**
     * Create a default {@link GltfAssetV1} from the given {@link GltfModel}.
     *  
     * @param gltfModel The input {@link GltfModel}
     * @return The default {@link GltfAssetV1}
     */
    GltfAssetV1 create(GltfModelV1 gltfModel)
    {
        // When the model already has a structure that is suitable for
        // writing it as a default glTF, then create that default glTF
        // directly from the model
        boolean hasDefaultStructure = hasDefaultStructure(gltfModel);
        if (hasDefaultStructure) 
        {
            logger.fine("The model has a default structure - creating asset");
            DirectAssetCreatorV1 delegate = new DirectAssetCreatorV1();
            return delegate.create(gltfModel);
        }

        logger.fine("Converting model into default structure");
        
        // Otherwise, convert the structure of the model, so that it
        // is suitable to be written as a default glTF
        GltfModelStructures g = new GltfModelStructures();
        g.prepare(gltfModel);
        DefaultGltfModel defaultGltfModel = g.createDefault();

        DirectAssetCreatorV1 delegate = new DirectAssetCreatorV1();
        return delegate.create((GltfModelV1) defaultGltfModel);
    }
    
    /**
     * Check if the given model has a structure that is suitable for 
     * writing it as a default glTF. This is the case when none of 
     * the existing images refers to a buffer view.
     * 
     * @param gltfModel The {@link GltfModel}
     * @return Whether the model has a structure suitable for a default glTF
     */
    private static boolean hasDefaultStructure(GltfModel gltfModel)
    {
        List<ImageModel> imageModels = gltfModel.getImageModels();
        for (ImageModel imageModel : imageModels)
        {
            BufferViewModel imageBufferViewModel = 
                imageModel.getBufferViewModel();
            if (imageBufferViewModel != null)
            {
                return false;
            }
        }
        return true;
    }


}
