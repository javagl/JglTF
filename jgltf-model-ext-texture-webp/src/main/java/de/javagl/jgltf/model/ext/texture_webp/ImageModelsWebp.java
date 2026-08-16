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
package de.javagl.jgltf.model.ext.texture_webp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.luciad.imageio.webp.WebPWriteParam;

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.image.ImageUtils;
import de.javagl.jgltf.model.impl.DefaultImageModel;

/**
 * Convenience methods for converting between buffered images and image models
 * that use WebP compression.
 */
public class ImageModelsWebp
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(ImageModelsWebp.class.getName());

    /**
     * Reads the data from the given image model as a buffered image.<br>
     * <br>
     * Returns <code>null</code> if the given buffer is <code>null</code>. If
     * the data can not be converted into a buffered image, then an error
     * message is printed and <code>null</code> is returned.<br>
     * <br>
     * 
     * @param imageModel The image model
     * @return The buffered image
     */
    static BufferedImage readAsBufferedImage(ImageModel imageModel)
    {
        return ImageUtils.readAsBufferedImage(imageModel.getImageData());
    }

    /**
     * Create a simple WebP image model from the given buffered image.<br>
     * <br>
     * If the image cannot be created, an error message is printed and
     * <code>null</code> is returned.
     * 
     * @param uri The URI for the image model
     * @param bufferedImage The buffered image
     * @return The image model
     */
    public static DefaultImageModel createFromBufferedImage(String uri,
        BufferedImage bufferedImage)
    {
        try
        {
            return createFromBufferedImageInternal(uri, bufferedImage);
        }
        catch (IOException e)
        {
            logger.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Create a simple WebP image model from the given buffered image.
     * 
     * @param uri The URI for the image model
     * @param bufferedImage The buffered image
     * @return The image model
     * @throws IOException If an IO error occurs
     */
    private static DefaultImageModel createFromBufferedImageInternal(String uri,
        BufferedImage bufferedImage) throws IOException
    {
        ByteBuffer imageData = createImageDataBuffer(bufferedImage);
        DefaultImageModel imageModel = new DefaultImageModel();
        imageModel.setImageData(imageData);
        imageModel.setUri(uri);
        imageModel.setMimeType("image/webp");
        return imageModel;
    }

    /**
     * Create a byte buffer containing the given image encoded as a lossless
     * WebP image
     * 
     * @param image The image
     * @return The data
     * @throws IOException If an IO error occurs
     */
    private static ByteBuffer createImageDataBuffer(BufferedImage image)
        throws IOException
    {
        // Create a lossless WEBP writer
        ImageWriter writer =
            ImageIO.getImageWritersByMIMEType("image/webp").next();
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        String[] compressionTypes = writeParam.getCompressionTypes();
        String compressionType =
            compressionTypes[WebPWriteParam.LOSSLESS_COMPRESSION];
        writeParam.setCompressionType(compressionType);

        // Write the image
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream imageOutputStream =
                ImageIO.createImageOutputStream(baos))
        {
            writer.setOutput(imageOutputStream);
            writer.write(null, new IIOImage(image, null, null), writeParam);
            imageOutputStream.flush();
            baos.close();
            return ByteBuffer.wrap(baos.toByteArray());
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ImageModelsWebp()
    {
        // Private constructor to prevent instantiation
    }
}
