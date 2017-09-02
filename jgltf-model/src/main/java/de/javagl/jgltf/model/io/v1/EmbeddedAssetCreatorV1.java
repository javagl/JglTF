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
import java.util.Base64;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.MimeTypes;
import de.javagl.jgltf.model.v1.BinaryGltfV1;
import de.javagl.jgltf.model.v1.GltfModelV1;

/**
 * A class for creating a {@link GltfAssetV1} with an "embedded" data 
 * representation from a {@link GltfModelV1}.<br>
 * <br>
 * In the "embedded" data representation, the data of elements like 
 * {@link Buffer}, {@link Image} or {@link Shader} objects is stored 
 * in data URIs.
 */
final class EmbeddedAssetCreatorV1
{
    /**
     * Creates a new asset creator
     */
    EmbeddedAssetCreatorV1()
    {
        // Default constructor
    }

    /**
     * Create a {@link GltfAssetV1} with "embedded" data representation from 
     * the given {@link GltfModelV1}.<br>
     * <br>
     * The returned {@link GltfAssetV1} will contain a {@link GlTF} where the
     * the URIs that appear in {@link Buffer}, {@link Image} or {@link Shader}
     * instances are replaced with data URIs that contain the corresponding 
     * data. Its {@link GltfAsset#getBinaryData() binary data} will be
     * <code>null</code>, and its {@link GltfAsset#getReferenceDatas() 
     * reference data elements} will be empty.
     *  
     * @param gltfModel The input {@link GltfModelV1}
     * @return The embedded {@link GltfAssetV1}
     */
    GltfAssetV1 create(GltfModelV1 gltfModel)
    {
        GlTF inputGltf = gltfModel.getGltf();
        GlTF outputGltf = GltfUtilsV1.copy(inputGltf);

        Optionals.of(outputGltf.getBuffers()).forEach((id, value) -> 
            convertBufferToEmbedded(gltfModel, id, value));
        Optionals.of(outputGltf.getImages()).forEach((id, value) -> 
            convertImageToEmbedded(gltfModel, id, value));
        Optionals.of(outputGltf.getShaders()).forEach((id, value) -> 
            convertShaderToEmbedded(gltfModel, id, value));

        return new GltfAssetV1(outputGltf, null);
    }

    /**
     * Convert the given {@link Buffer} into an embedded buffer, by replacing 
     * its URI with a data URI, if the URI is not already a data URI
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param id The ID of the {@link Buffer}
     * @param buffer The {@link Buffer}
     */
    private static void convertBufferToEmbedded(
        GltfModelV1 gltfModel, String id, Buffer buffer)
    {
        String uriString = buffer.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        BufferModel bufferModel = gltfModel.getBufferModelById(id);
        ByteBuffer bufferData = bufferModel.getBufferData();
        
        byte data[] = new byte[bufferData.capacity()];
        bufferData.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString = 
            "data:application/gltf-buffer;base64," + encodedData;
        
        buffer.setUri(dataUriString);
    }

    /**
     * Convert the given {@link Image} into an embedded image, by replacing 
     * its URI with a data URI, if the URI is not already a data URI
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param id The ID of the {@link Image}
     * @param image The {@link Image}
     * @throws GltfException If the image format (and thus, the MIME type)
     * can not be determined from the image data  
     */
    private static void convertImageToEmbedded(
        GltfModelV1 gltfModel, String id, Image image)
    {
        String uriString = image.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        ImageModel imageModel = gltfModel.getImageModelById(id);
        ByteBuffer imageData = imageModel.getImageData();
        
        String uri = image.getUri();
        String imageMimeTypeString =
            MimeTypes.guessImageMimeTypeString(uri, imageData);
        if (imageMimeTypeString == null)
        {
            throw new GltfException(
                "Could not detect MIME type of image " + id);
        }

        byte data[] = new byte[imageData.capacity()];
        imageData.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString =
            "data:" + imageMimeTypeString + ";base64," + encodedData;
        
        image.removeExtensions(BinaryGltfV1.getBinaryGltfExtensionName());
        image.setUri(dataUriString);
    }

    /**
     * Convert the given {@link Shader} into an embedded shader, by replacing 
     * its URI with a data URI, if the URI is not already a data URI
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param id The ID of the {@link Shader}
     * @param shader The {@link Shader}
     */
    private static void convertShaderToEmbedded(
        GltfModelV1 gltfModel, String id, Shader shader)
    {
        String uriString = shader.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        ShaderModel shaderModel = gltfModel.getShaderModelById(id);
        ByteBuffer shaderData = shaderModel.getShaderData();
        
        byte data[] = new byte[shaderData.capacity()];
        shaderData.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString = 
            "data:text/plain;base64," + encodedData;
        
        shader.removeExtensions(BinaryGltfV1.getBinaryGltfExtensionName());
        shader.setUri(dataUriString);
    }


}
