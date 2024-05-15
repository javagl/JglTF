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
package de.javagl.jgltf.model.io.v2;

import java.util.List;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v2.Buffer;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.structure.GltfModelStructures;

/**
 * A class for creating a binary {@link GltfAssetV2} from a 
 * {@link GltfModel}.<br>
 * <br>
 */
final class BinaryAssetCreatorV2
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(BinaryAssetCreatorV2.class.getName());
    
    /**
     * Creates a new asset creator
     */
    BinaryAssetCreatorV2()
    {
        // Default constructor
    }
    
    /**
     * Create a binary {@link GltfAssetV2} from the given {@link GltfModel}.<br>
     * <br>
     * The resulting asset will have a {@link GlTF} that uses references to
     * {@link BufferView} objects in its {@link Buffer} and {@link Image}
     * elements. 
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The {@link GltfAssetV2}
     */
    GltfAssetV2 create(GltfModel gltfModel)
    {
        // When the model already has a structure that is suitable for
        // writing it as a binary glTF, then create that binary glTF
        // directly from the model
        boolean hasBinaryStructure = hasBinaryStructure(gltfModel);
        if (hasBinaryStructure) 
        {
            logger.fine("The model has a binary structure - creating asset");
            DirectAssetCreatorV2 delegate = new DirectAssetCreatorV2();
            GltfAssetV2 binaryAsset = delegate.createBinary(gltfModel);
            return binaryAsset;
        }

        logger.fine("Converting model into binary structure");
        
        // Otherwise, convert the structure of the model, so that it
        // is suitable to be written as a binary glTF
        GltfModelStructures g = new GltfModelStructures();
        g.prepare(gltfModel);
        DefaultGltfModel binaryGltfModel = g.createBinary();
        DirectAssetCreatorV2 delegate = new DirectAssetCreatorV2();
        GltfAssetV2 binaryAsset = delegate.createBinary(binaryGltfModel);
        return binaryAsset;
    }
    
    /**
     * Check if the given model has a structure that is suitable for 
     * writing it as a binary glTF. This is the case when there is 
     * at most one buffer, and all of the existing images already
     * refer to a buffer view within that buffer.
     * 
     * @param gltfModel The {@link GltfModel}
     * @return Whether the model has a structure suitable for a binary glTF
     */
    private static boolean hasBinaryStructure(GltfModel gltfModel)
    {
        List<BufferModel> bufferModels = gltfModel.getBufferModels();
        if (bufferModels.size() >= 2) 
        {
            return false;
        } 
        List<ImageModel> imageModels = gltfModel.getImageModels();
        for (ImageModel imageModel : imageModels)
        {
            BufferViewModel imageBufferViewModel = 
                imageModel.getBufferViewModel();
            if (imageBufferViewModel == null)
            {
                return false;
            }
        }
        return true;
    }
    

}