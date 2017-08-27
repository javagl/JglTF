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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.javagl.jgltf.impl.v2.Buffer;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.impl.UriStrings;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v2.GltfModelV2;

/**
 * A class for converting {@link GltfModelV1} to a {@link GltfAssetV2} with
 * a default data representation, where data elements are referred to via
 * URIs. The {@link Buffer} and {@link Image} objects that used the binary 
 * glTF or data URIs will be converted to refer to their data using URIs.
 */
public class DefaultAssetCreatorV2
{
    /**
     * The {@link GltfAssetV2} that is currently being created
     */
    private GltfAssetV2 gltfAsset;
    
    /**
     * The set of {@link Buffer} URI strings that are already used
     */
    private Set<String> existingBufferUriStrings;

    /**
     * The set of {@link Image} URI strings that are already used
     */
    private Set<String> existingImageUriStrings;

    /**
     * Creates a new glTF model to default converter
     */
    public DefaultAssetCreatorV2()
    {
        // Default constructor
    }

    /**
     * Create a default {@link GltfAssetV2} from the given {@link GltfModelV2}.
     *  
     * @param gltfModel The input {@link GltfModelV2}
     * @return The default {@link GltfAssetV2}
     */
    public GltfAssetV2 create(GltfModelV2 gltfModel)
    {
        GlTF inputGltf = gltfModel.getGltf();
        GlTF convertedGltf = GltfUtilsV2.copy(inputGltf);
        
        existingBufferUriStrings = collectUriStrings(
            Optionals.of(inputGltf.getBuffers()),
            Buffer::getUri);
        existingImageUriStrings = collectUriStrings(
            Optionals.of(inputGltf.getImages()),
            Image::getUri);

        gltfAsset = new GltfAssetV2(convertedGltf, null);
        
        List<Buffer> buffers = Optionals.of(convertedGltf.getBuffers());
        for (int i = 0; i < buffers.size(); i++)
        {
            Buffer buffer = buffers.get(i);
            convertBufferToDefault(gltfModel, i, buffer);
        }
        List<Image> images = Optionals.of(convertedGltf.getImages());
        for (int i = 0; i < images.size(); i++)
        {
            Image image = images.get(i);
            convertImageToDefault(gltfModel, i, image);
        }

        return gltfAsset;
    }

    /**
     * Collect all strings that are obtained from the given elements by 
     * applying the given function, if these strings are not <code>null</code>
     * and no data URI strings
     * 
     * @param elements The elements
     * @param uriFunction The function to obtain the string
     * @return The strings
     */
    private static <T> Set<String> collectUriStrings(Collection<T> elements, 
        Function<? super T, ? extends String> uriFunction)
    {
        return elements.stream()
            .map(uriFunction)
            .filter(Objects::nonNull)
            .filter(uriString -> !IO.isDataUriString(uriString))
            .collect(Collectors.toSet());
    }
    

    /**
     * Convert the given {@link Buffer} from the given {@link GltfModelV2} into
     * a default buffer: If its URI is a data URI, or it does not have a URI
     * (indicating that it is the binary glTF buffer), it will receive a new 
     * URI. This URI refers to the buffer data, which is then stored as 
     * {@link GltfAsset#getReferenceData(String) reference data} in the
     * asset. 
     * 
     * @param gltfModel The {@link GltfModelV2}
     * @param index The index of the {@link Buffer}
     * @param buffer The {@link Buffer}
     */
    private void convertBufferToDefault(
        GltfModelV2 gltfModel, int index, Buffer buffer)
    {
        String uriString = buffer.getUri();
        if (uriString == null ||
            IO.isDataUriString(uriString))
        {
            BufferModel bufferModel = gltfModel.getBufferModels().get(index);
            String bufferUriString = UriStrings.createBufferUriString(
                existingBufferUriStrings);
            buffer.setUri(bufferUriString);
            existingBufferUriStrings.add(bufferUriString);
            
            ByteBuffer bufferData = bufferModel.getBufferData();
            gltfAsset.putReferenceData(bufferUriString, bufferData);
        }
    }

    /**
     * Convert the given {@link Image} from the given {@link GltfModelV2} into
     * an default image. If its URI is a data URI, or it refers to data via
     * a buffer view, it will receive a new URI. This URI refers to the image 
     * data, which is then stored as 
     * {@link GltfAsset#getReferenceData(String) reference data} in the
     * asset. 
     * 
     * @param gltfModel The {@link GltfModelV2}
     * @param index The index of the {@link Image}
     * @param image The {@link Image}
     * @throws GltfException If the image format (and thus, the MIME type)
     * can not be determined from the image data  
     */
    private void convertImageToDefault(
        GltfModelV2 gltfModel, int index, Image image)
    {
        String uriString = image.getUri();
        if (image.getBufferView() != null ||
            IO.isDataUriString(uriString))
        {
            ImageModel imageModel = gltfModel.getImageModels().get(index);
            
            String imageUriString = UriStrings.createImageUriString(
                imageModel, existingImageUriStrings);
            image.setUri(imageUriString);
            existingImageUriStrings.add(imageUriString);
            
            ByteBuffer imageData = imageModel.getImageData();
            gltfAsset.putReferenceData(imageUriString, imageData);
            
            return;
        }
    }

}
