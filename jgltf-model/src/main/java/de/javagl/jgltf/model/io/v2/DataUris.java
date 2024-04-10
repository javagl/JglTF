/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2024 Marco Hutter - http://www.javagl.de
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
import java.util.Base64;

import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.io.MimeTypes;

/**
 * Methods to create data URIs from model objects.
 * 
 * This is not part of the public API.
 */
public class DataUris
{
    /**
     * Create a data URI for the data of the given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The data URI string
     */
    public static String createBufferDataUri(BufferModel bufferModel)
    {
        ByteBuffer bufferData = bufferModel.getBufferData();
        byte data[] = new byte[bufferData.capacity()];
        bufferData.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString = 
            "data:application/gltf-buffer;base64," + encodedData;
        return dataUriString;
    }
    
    /**
     * Create the data URI for the data of the given {@link ImageModel}
     *
     * @param currentUri The current URI of the image
     * @param imageModel The {@link ImageModel}
     * @return The data URI string
     * @throws GltfException If the image format (and thus, the MIME type)
     * can not be determined from the image data  
     */
    public static String createImageDataUri(
        String currentUri, ImageModel imageModel)
    {
        ByteBuffer imageData = imageModel.getImageData();
        
        String imageMimeTypeString =
            MimeTypes.guessImageMimeTypeString(currentUri, imageData);
        if (imageMimeTypeString == null)
        {
            throw new GltfException(
                "Could not detect MIME type of image");
        }

        byte data[] = new byte[imageData.capacity()];
        imageData.slice().get(data);
        String encodedData = Base64.getEncoder().encodeToString(data);
        String dataUriString =
            "data:" + imageMimeTypeString + ";base64," + encodedData;
        
        return dataUriString;
    }
    
    
}
