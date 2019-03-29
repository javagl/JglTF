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
package de.javagl.jgltf.model.creation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.viewer.ImageUtils;
import de.javagl.jgltf.viewer.PixelData;
import de.javagl.jgltf.viewer.PixelDatas;

/**
 * Methods to create {@link ImageModel} instances
 */
public class ImageModels 
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ImageModels.class.getName());
    
    /**
     * Creates a new {@link ImageModel} with the given URI, that contains
     * a buffer containing the image data that was read from the given 
     * source file, encoded with the given MIME type.<br>
     * <br>
     * The MIME type must be <code>"image/png"</code> or 
     * <code>"image/gif"</code> or <code>"image/jpeg"</code> (<b>not</b> 
     * <code>"image/jpg"</code>!).<br> 
     * <br>
     * If the source URI cannot be read, then an error message is
     * printed and <code>null</code> is returned.
     * 
     * @param fileName The source file name
     * @param uri The URI
     * @param mimeType The MIME type
     * @return The instance
     * @throws IllegalArgumentException If the MIME type is not one of the
     * types listed above
     */
    public static DefaultImageModel create(
        String fileName, String uri, String mimeType)
    {
        try (InputStream inputStream = new FileInputStream(fileName))
        {
            byte data[] = IO.readStream(inputStream);
            PixelData pixelData = PixelDatas.create(ByteBuffer.wrap(data));
            return create(uri, mimeType, pixelData);
        }
        catch (IOException e)
        {
            logger.severe(e.toString());
            return null;
        }
    }
    
    /**
     * Creates a new {@link ImageModel} with the given URI, that contains
     * a buffer containing the image data for the given {@link PixelData},
     * encoded with the given MIME type.<br>
     * <br>
     * The MIME type must be <code>"image/png"</code> or 
     * <code>"image/gif"</code> or <code>"image/jpeg"</code> (<b>not</b> 
     * <code>"image/jpg"</code>!).<br> 
     * <br>
     * 
     * @param uri The URI
     * @param mimeType The MIME type
     * @param pixelData The {@link PixelData}
     * @return The instance
     * @throws IllegalArgumentException If the MIME type is not one of the
     * types listed above
     */
    public static DefaultImageModel create(
        String uri, String mimeType, PixelData pixelData)
    {
        ByteBuffer imageData = ImageUtils.createImageDataBuffer(
            pixelData, mimeType);
        DefaultImageModel imageModel = new DefaultImageModel();
        imageModel.setImageData(imageData);
        imageModel.setUri(uri);
        return imageModel;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ImageModels()
    {
        // Private constructor to prevent instantiation
    }
}

