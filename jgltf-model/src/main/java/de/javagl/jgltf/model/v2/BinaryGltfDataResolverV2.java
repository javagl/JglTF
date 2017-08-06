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
package de.javagl.jgltf.model.v2;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;

/**
 * A class to resolve the data of {@link Image} objects in a glTF that uses 
 * the binary glTF data. This class will extract the parts of the binary 
 * glTF buffer that contain the image data, and put these data blocks into
 * the corresponding {@link ImageModel}.
 */
class BinaryGltfDataResolverV2
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BinaryGltfDataResolverV2.class.getName());
    
    /**
     * The {@link GltfModelV2}
     */
    private final GltfModelV2 gltfModel;

    /**
     * Creates a new resolver for the given {@link GltfModelV2}
     * 
     * @param gltfModel The {@link GltfModelV2}
     */
    BinaryGltfDataResolverV2(GltfModelV2 gltfModel)
    {
        this.gltfModel = Objects.requireNonNull(
            gltfModel, "The gltfModel may not be null");
    }
    
    /**
     * Resolve the image data objects that refer to the binary glTF buffer
     */
    void resolve()
    {
        resolveBinaryImageDatas();
    }
    
    /**
     * For each {@link Image} in the current glTF that refers to a buffer view, 
     * set the corresponding data as the
     * {@link ImageModel#setImageData(ByteBuffer) ImageModel image data}
     */
    private void resolveBinaryImageDatas() 
    {
        GlTF gltf = gltfModel.getGltf();
        List<Image> images = Optionals.of(gltf.getImages());
        for (int i = 0; i < images.size(); i++)
        {
            resolveBinaryImageData(i);
        }
    }
    
    /**
     * If the specified {@link Image} has the binary glTF extension, resolve 
     * the corresponding buffer view that is referred to in the extension, 
     * and set the corresponding data as the 
     * {@link ImageModel#setImageData(ByteBuffer) ImageModel image data}
     * 
     * @param imageIndex The {@link Image} index
     */
    private void resolveBinaryImageData(int imageIndex)
    {
        GlTF gltf = gltfModel.getGltf();
        Image image = gltf.getImages().get(imageIndex);
        Integer bufferViewIndex = image.getBufferView();
        if (bufferViewIndex == null)
        {
            return;
        }
        BufferViewModel bufferViewModel = 
            gltfModel.getBufferViewModels().get(bufferViewIndex);
        ByteBuffer bufferViewData = bufferViewModel.getBufferViewData();
        if (bufferViewData == null)
        {
            logger.warning(
                "Could not resolve the data for image " + imageIndex);
        }
        ImageModel imageModel = gltfModel.getImageModels().get(imageIndex);
        imageModel.setImageData(bufferViewData);
    }
    
}
