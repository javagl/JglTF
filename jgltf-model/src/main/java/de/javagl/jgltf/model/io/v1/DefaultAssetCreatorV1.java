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

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.impl.UriStrings;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.v1.BinaryGltfV1;
import de.javagl.jgltf.model.v1.GltfExtensionsV1;
import de.javagl.jgltf.model.v1.GltfModelV1;

/**
 * A class for converting {@link GltfModelV1} to a {@link GltfAssetV1} with
 * a default data representation, where data elements are referred to via
 * URIs. Data elements like {@link Buffer}, {@link Image} or {@link Shader}
 * objects that used the binary glTF extension or data URIs will be converted
 * to refer to their data using URIs.
 */
public class DefaultAssetCreatorV1
{
    /**
     * The {@link GltfAssetV1} that is currently being created
     */
    private GltfAssetV1 gltfAsset;
    
    /**
     * The set of {@link Buffer} URI strings that are already used
     */
    private Set<String> existingBufferUriStrings;

    /**
     * The set of {@link Image} URI strings that are already used
     */
    private Set<String> existingImageUriStrings;

    /**
     * The set of {@link Shader} URI strings that are already used
     */
    private Set<String> existingShaderUriStrings;
    
    /**
     * Creates a new glTF model to default converter
     */
    public DefaultAssetCreatorV1()
    {
        // Default constructor
    }

    /**
     * Create a default {@link GltfAssetV1} from the given {@link GltfModelV1}.
     *  
     * @param gltfModel The input {@link GltfModelV1}
     * @return The default {@link GltfAssetV1}
     */
    public GltfAssetV1 create(GltfModelV1 gltfModel)
    {
        GlTF inputGltf = gltfModel.getGltf();
        GlTF convertedGltf = GltfUtilsV1.copy(inputGltf);
        
        // Remove the binary glTF extension, if it was used
        GltfExtensionsV1.removeExtensionUsed(convertedGltf, 
            BinaryGltfV1.getBinaryGltfExtensionName());

        existingBufferUriStrings = collectUriStrings(
            Optionals.of(inputGltf.getBuffers()).values(),
            Buffer::getUri);
        existingImageUriStrings = collectUriStrings(
            Optionals.of(inputGltf.getImages()).values(),
            Image::getUri);
        existingShaderUriStrings = collectUriStrings(
            Optionals.of(inputGltf.getShaders()).values(),
            Shader::getUri);

        gltfAsset = new GltfAssetV1(convertedGltf, null);
        
        Optionals.of(convertedGltf.getBuffers()).forEach((id, value) -> 
            convertBufferToDefault(gltfModel, id, value));
        Optionals.of(convertedGltf.getImages()).forEach((id, value) -> 
            convertImageToDefault(gltfModel, id, value));
        Optionals.of(convertedGltf.getShaders()).forEach((id, value) -> 
            convertShaderToDefault(gltfModel, id, value));

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
     * Convert the given {@link Buffer} from the given {@link GltfModelV1} into
     * a default buffer: If its URI is a data URI, it will receive a new URI.
     * This URI refers to the buffer data, which is then stored as 
     * {@link GltfAsset#getReferenceData(String) reference data} in the
     * asset. 
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param id The ID of the {@link Buffer}
     * @param buffer The {@link Buffer}
     */
    private void convertBufferToDefault(
        GltfModelV1 gltfModel, String id, Buffer buffer)
    {
        String uriString = buffer.getUri();
        if (BinaryGltfV1.isBinaryGltfBufferId(id) ||
            IO.isDataUriString(uriString))
        {
            BufferModel bufferModel = gltfModel.getBufferModelById(id);
            String bufferUriString = UriStrings.createBufferUriString(
                existingBufferUriStrings);
            buffer.setUri(bufferUriString);
            existingBufferUriStrings.add(bufferUriString);
            
            ByteBuffer bufferData = bufferModel.getBufferData();
            gltfAsset.putReferenceData(bufferUriString, bufferData);
        }
    }

    /**
     * Convert the given {@link Image} from the given {@link GltfModelV1} into
     * an default image. If its URI is a data URI, or it refers to data via
     * the binary glTF extension, it will receive a new URI. This URI refers 
     * to the image data, which is then stored as 
     * {@link GltfAsset#getReferenceData(String) reference data} in the
     * asset. 
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param id The ID of the {@link Image}
     * @param image The {@link Image}
     * @throws GltfException If the image format (and thus, the MIME type)
     * can not be determined from the image data  
     */
    private void convertImageToDefault(
        GltfModelV1 gltfModel, String id, Image image)
    {
        String uriString = image.getUri();
        if (BinaryGltfV1.hasBinaryGltfExtension(image) ||
            IO.isDataUriString(uriString))
        {
            ImageModel imageModel = gltfModel.getImageModelById(id);
            
            String imageUriString = UriStrings.createImageUriString(
                imageModel, existingImageUriStrings);
            image.setUri(imageUriString);
            existingImageUriStrings.add(imageUriString);
            
            ByteBuffer imageData = imageModel.getImageData();
            gltfAsset.putReferenceData(imageUriString, imageData);
            
            return;
        }
    }

    /**
     * Convert the given {@link Shader} from the given {@link GltfModelV1} into
     * a default shader. If its URI is a data URI, or it refers to data via
     * the binary glTF extension, it will receive a new URI. This URI refers 
     * to the shader data, which is then stored as 
     * {@link GltfAsset#getReferenceData(String) reference data} in the
     * asset. 
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param id The ID of the {@link Shader}
     * @param shader The {@link Shader}
     */
    private void convertShaderToDefault(
        GltfModelV1 gltfModel, String id, Shader shader)
    {
        String uriString = shader.getUri();
        if (BinaryGltfV1.hasBinaryGltfExtension(shader) ||
            IO.isDataUriString(uriString))
        {
            ShaderModel shaderModel = gltfModel.getShaderModelById(id);
            
            String shaderUriString = UriStrings.createShaderUriString(
                shaderModel, existingShaderUriStrings);
            shader.setUri(shaderUriString);
            existingShaderUriStrings.add(shaderUriString);
            
            ByteBuffer shaderData = shaderModel.getShaderData();
            gltfAsset.putReferenceData(shaderUriString, shaderData);
            
            return;
        }
    }


}
