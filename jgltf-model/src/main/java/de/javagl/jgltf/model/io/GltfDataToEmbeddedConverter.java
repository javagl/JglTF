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
package de.javagl.jgltf.model.io;

import java.nio.ByteBuffer;
import java.util.Base64;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.Optionals;

/**
 * A class for converting {@link GltfData} to a {@link GltfData} with
 * an "embedded" data representation, where data elements are not
 * referred to via URIs, but encoded directly in the glTF JSON as
 * data URIs. 
 */
public class GltfDataToEmbeddedConverter
{
    /**
     * Creates a new glTF data to embedded converter
     */
    public GltfDataToEmbeddedConverter()
    {
        // Default constructor
    }
    
    /**
     * Convert the given {@link GltfData} to a {@link GltfData} with 
     * an "embedded" data representation. <br>
     * <br>
     * The returned {@link GltfData} will contain a {@link GlTF} where the
     * the URIs that appear in {@link Buffer}, {@link Image} or {@link Shader}
     * instances are replaced with data URIs that contain the corresponding 
     * data. The respective ID mappings in the returned {@link GltfData} 
     * will be removed, but it will still contain the same instances of 
     * {@link GltfData#getBufferViewData(String) buffer views}
     * as the given input {@link GltfData}.
     * 
     * @param inputGltfData The input {@link GltfData}
     * @return The embedded {@link GltfData}
     */
    public GltfData convert(GltfData inputGltfData)
    {
        GltfData convertedGltfData = 
            new GltfData(GltfUtils.copy(inputGltfData.getGltf()));
        convertedGltfData.copy(inputGltfData);
        
        GlTF gltf = convertedGltfData.getGltf();
        
        Optionals.of(gltf.getBuffers()).forEach((id, value) -> 
            convertBufferToEmbedded(convertedGltfData, id, value));
        Optionals.of(gltf.getImages()).forEach((id, value) -> 
            convertImageToEmbedded(convertedGltfData, id, value));
        Optionals.of(gltf.getShaders()).forEach((id, value) -> 
            convertShaderToEmbedded(convertedGltfData, id, value));
        
        return convertedGltfData;
    }
    
    /**
     * Convert the given {@link Buffer} from the given {@link GltfData} into
     * an embedded buffer, by replacing its URI with a data URI, if the 
     * URI is not already a data URI
     * 
     * @param gltfData The {@link GltfData}
     * @param id The ID of the {@link Shader}
     * @param buffer The {@link Buffer}
     */
    private static void convertBufferToEmbedded(
        GltfData gltfData, String id, Buffer buffer)
    {
        String uriString = buffer.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        ByteBuffer byteBuffer = gltfData.getBufferData(id);
        byte data[] = new byte[byteBuffer.capacity()];
        byteBuffer.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString = 
            "data:application/octet-stream;base64," + encodedData;
        buffer.setUri(dataUriString);
    }

    /**
     * Convert the given {@link Image} from the given {@link GltfData} into
     * an embedded image, by replacing its URI with a data URI, if the 
     * URI is not already a data URI
     * 
     * @param gltfData The {@link GltfData}
     * @param id The ID of the {@link Shader}
     * @param image The {@link Image}
     * @throws GltfException If the image format (and thus, the MIME type)
     * can not be determined from the image data  
     */
    private static void convertImageToEmbedded(
        GltfData gltfData, String id, Image image)
    {
        String uriString = image.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        ByteBuffer imageData = gltfData.getImageData(id);
        
        String imageMimeTypeString = 
            GltfUtils.guessImageMimeTypeString(image, imageData);
        if (imageMimeTypeString == null)
        {
            throw new GltfException(
                "Could not detect MIME type of image " + id);
        }
        
        byte data[] = new byte[imageData.capacity()];
        imageData.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString = 
            "data:image/" + imageMimeTypeString + 
            ";base64," + encodedData;
        image.setUri(dataUriString);
    }

    /**
     * Convert the given {@link Shader} from the given {@link GltfData} into
     * an embedded shader, by replacing its URI with a data URI, if the 
     * URI is not already a data URI
     * 
     * @param gltfData The {@link GltfData}
     * @param id The ID of the {@link Shader}
     * @param shader The {@link Shader}
     */
    private static void convertShaderToEmbedded(
        GltfData gltfData, String id, Shader shader)
    {
        String uriString = shader.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        ByteBuffer shaderData = gltfData.getShaderData(id);
        byte data[] = new byte[shaderData.capacity()];
        shaderData.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString = 
            "data:text/plain;base64," + encodedData;
        shader.setUri(dataUriString);
    }
    
    
}
