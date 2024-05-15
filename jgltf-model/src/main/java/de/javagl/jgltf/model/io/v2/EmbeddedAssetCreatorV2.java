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

import de.javagl.jgltf.impl.v2.Buffer;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.v2.GltfCreatorV2;

/**
 * A class for creating a {@link GltfAssetV2} with an "embedded" data 
 * representation from a {@link GltfModel}.<br>
 * <br>
 * In the "embedded" data representation, the data of {@link Buffer} and
 * {@link Image} objects is stored as data URIs in the {@link Buffer#getUri()}
 * and {@link Image#getUri()}, respectively.
 */
final class EmbeddedAssetCreatorV2
{
    /**
     * Creates a new asset creator
     */
    EmbeddedAssetCreatorV2()
    {
        // Default constructor
    }

    /**
     * Create a {@link GltfAssetV2} with "embedded" data representation from 
     * the given {@link GltfModel}.<br>
     * <br>
     * The returned {@link GltfAssetV2} will contain a {@link GlTF} where the
     * the URIs that appear in {@link Buffer} and {@link Image} instances are 
     * replaced with data URIs that contain the corresponding data.<br>
     * <br> 
     * Its {@link GltfAsset#getBinaryData() binary data} will be
     * <code>null</code>, and its {@link GltfAsset#getReferenceDatas() 
     * reference data elements} will be empty.
     *  
     * @param gltfModel The input {@link GltfModel}
     * @return The embedded {@link GltfAssetV2}
     */
    GltfAssetV2 create(GltfModel gltfModel)
    {
        GlTF outputGltf = GltfCreatorV2.create(gltfModel);

        List<Buffer> buffers = Optionals.of(outputGltf.getBuffers());
        List<BufferModel> bufferModels = gltfModel.getBufferModels();
        for (int i = 0; i < buffers.size(); i++)
        {
            Buffer buffer = buffers.get(i);
            BufferModel bufferModel = bufferModels.get(i);
            String dataUri = DataUris.createBufferDataUri(bufferModel);
            buffer.setUri(dataUri);
        }
        
        List<Image> images = Optionals.of(outputGltf.getImages());
        List<ImageModel> imageModels = gltfModel.getImageModels();
        for (int i = 0; i < images.size(); i++)
        {
            Image image = images.get(i);
            ImageModel imageModel = imageModels.get(i);
            
            // If the image refers to a buffer view, then its data is
            // already part of the data URI that has been written for
            // one of the buffers. Otherwise, its URI will be set
            // to be a data URI for its image data.
            if (imageModel.getBufferViewModel() == null)
            {
                String currentUri = image.getUri();
                String dataUri = DataUris.createImageDataUri(
                    currentUri, imageModel);
                image.setUri(dataUri);
            } 
            else 
            {
                image.setUri(null);
            }
        }
        return new GltfAssetV2(outputGltf, null);
    }


}
