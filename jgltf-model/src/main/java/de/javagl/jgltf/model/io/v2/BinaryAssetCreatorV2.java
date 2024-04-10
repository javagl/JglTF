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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v2.Buffer;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.MimeTypes;
import de.javagl.jgltf.model.v2.GltfCreatorV2;

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
     * Create a binary {@link GltfAssetV2} from the given {@link GltfModel}.
     * The resulting asset will have a {@link GlTF} that uses references to
     * {@link BufferView} objects in its {@link Image} elements. 
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The {@link GltfAssetV2}
     */
    GltfAssetV2 create(GltfModel gltfModel)
    {
        GlTF outputGltf = GltfCreatorV2.create(gltfModel);
        
        // The data from all buffer views and all images has to be put
        // into a single Buffer (namely, the buffer whose ByteBuffer
        // will become the GltfAssetV2#getBinaryData)
        List<ByteBuffer> bufferViewDatas = new ArrayList<ByteBuffer>();
        int byteOffset = 0;
        
        // For each old buffer view: Create a new buffer view that
        // is updated to refer to the new binary glTF buffer,
        // and collect its data
        List<BufferView> oldBufferViews = 
            copy(outputGltf.getBufferViews());
        List<BufferView> newBufferViews = 
            new ArrayList<BufferView>();
        List<BufferViewModel> bufferViewModels = 
            gltfModel.getBufferViewModels();
        for (int i = 0; i < oldBufferViews.size(); i++)
        {
            BufferView oldBufferView = oldBufferViews.get(i);
            BufferView newBufferView = GltfUtilsV2.copy(oldBufferView);

            newBufferView.setBuffer(0);
            newBufferView.setByteOffset(byteOffset);
            newBufferViews.add(newBufferView);
            
            BufferViewModel bufferViewModel = bufferViewModels.get(i);
            ByteBuffer bufferViewData = bufferViewModel.getBufferViewData();
            bufferViewDatas.add(bufferViewData);
            byteOffset += bufferViewData.capacity();
        }
        
        // For all existing images: If the image does not yet refer to a 
        // buffer view, then create a new buffer view and update the
        // image accordingly. If the image already referred to a buffer view,
        // then its data has already been collected, and the image can
        // be kept as-it-is.
        List<Image> oldImages = copy(outputGltf.getImages());
        List<Image> newImages = new ArrayList<Image>();
        List<ImageModel> imageModels = gltfModel.getImageModels();
        for (int i = 0; i < oldImages.size(); i++)
        {
            Image oldImage = oldImages.get(i);
            Image newImage = GltfUtilsV2.copy(oldImage);
            
            ImageModel imageModel = imageModels.get(i);
            ByteBuffer imageData = imageModel.getImageData();

            // If the image does not already refer to a buffer view,
            // then create a new buffer view, update the image, and
            // collect the data of the new buffer view
            BufferViewModel imageBufferViewModel = 
                imageModel.getBufferViewModel();
            if (imageBufferViewModel == null)
            {
                bufferViewDatas.add(imageData);
                byteOffset += imageData.capacity();
                
                BufferView imageBufferView = new BufferView();
                imageBufferView.setBuffer(0);
                imageBufferView.setByteOffset(byteOffset);
                imageBufferView.setByteLength(imageData.capacity());
                
                int newBufferViewIndex = newBufferViews.size();
                newImage.setBufferView(newBufferViewIndex);
                newImage.setUri(null);
                newBufferViews.add(imageBufferView);
            }
            
            String imageMimeTypeString =
                MimeTypes.guessImageMimeTypeString(
                    oldImage.getUri(), imageData);
            if (imageMimeTypeString == null)
            {
                logger.warning("Could not detect MIME type of image");
            }
            else
            {
                newImage.setMimeType(imageMimeTypeString);
            }
            newImages.add(newImage);
        }
        
        // Create the binary Buffer, combining the data of all buffer views
        // that have been collected or newly created for the images
        Buffer binaryGltfBuffer = new Buffer();
        ByteBuffer binaryGltfByteBuffer = Buffers.concat(bufferViewDatas);
        binaryGltfBuffer.setByteLength(binaryGltfByteBuffer.capacity());
        outputGltf.setBuffers(Collections.singletonList(binaryGltfBuffer));

        // Place the newly created lists into the output glTF,
        // if there have been non-null lists for them in the input
        if (!newImages.isEmpty())
        {
            outputGltf.setImages(newImages);
        }
        if (!newBufferViews.isEmpty())
        {
            outputGltf.setBufferViews(newBufferViews);
        }
        return new GltfAssetV2(outputGltf, binaryGltfByteBuffer);
    }

    /**
     * Creates a copy of the given list. If the given list is <code>null</code>, 
     * then an unmodifiable empty list will be returned
     * 
     * @param list The input list
     * @return The copy
     */
    private static <T> List<T> copy(List<T> list)
    {
        if (list == null)
        {
            return Collections.emptyList();
        }
        return new ArrayList<T>(list);
    }
    

}
