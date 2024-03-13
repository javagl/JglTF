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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.image.ImageUtils;
import de.javagl.jgltf.model.image.PixelData;
import de.javagl.jgltf.model.image.PixelDatas;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.MimeTypes;

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
     * @param uri The URI that will be assigned to the {@link ImageModel}
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
            logger.log(Level.SEVERE, e.toString(), e);
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
     * @param uri The URI that will be assigned to the {@link ImageModel}
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
        imageModel.setMimeType(mimeType);
        return imageModel;
    }
    
    /**
     * Creates a new {@link ImageModel} with the given URI, that contains
     * a buffer containing the image data that was read from the given 
     * input URI.<br>
     * <br>
     * The MIME type will be detected from the input file name (i.e. from
     * its extension). If this is not possible, it will be detected from
     * the image data. If this is not possible, then an error message is
     * displayed and <code>null</code> is returned. 
     * <br>
     * If the source URI cannot be read, then an error message is
     * printed and <code>null</code> is returned. If the source URI
     * is not absolute, it will be assumed to be a path description
     * that is resolved against the default file system.
     * 
     * @param inputUri The input file name
     * @param uri The URI that will be assigned to the {@link ImageModel}
     * @return The instance
     */
    public static DefaultImageModel create(
        String inputUri, String uri)
    {
        byte data[] = null;
        try
        {
            URI localInputUri = URI.create(inputUri);
            if (!localInputUri.isAbsolute())
            {
                localInputUri = Paths.get(inputUri).toUri().normalize();
            }
            data = IO.read(localInputUri);
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
        ByteBuffer imageData = Buffers.create(data);

        String mimeType = MimeTypes.guessImageMimeTypeString(
            inputUri, imageData);
        if (mimeType == null)
        {
            logger.severe("Could not detect MIME type of " + inputUri);
            return null;
        }
        DefaultImageModel imageModel = new DefaultImageModel();
        imageModel.setImageData(imageData);
        imageModel.setUri(uri);
        imageModel.setMimeType(mimeType);
        return imageModel;
    }
    
    /**
     * Create a simple "label" image with the given size that just shows
     * the given text.
     * 
     * The given text may be a HTML string.
     * 
     * @param uri The URI for the image model
     * @param sizeX The size of the image in x-direction
     * @param sizeY The size of the image in y-direction
     * @param fontSize The font size
     * @param text The text
     * @return The image model
     */
    public static DefaultImageModel createLabel(
        String uri, int sizeX, int sizeY, int fontSize, String text) 
    {
        BufferedImage image = createImageWithText(sizeX, sizeY, fontSize, text);
        PixelData pixelData = PixelDatas.create(image);
        DefaultImageModel imageModel = create(uri, "image/png", pixelData);
        return imageModel;
    }

    /**
     * Creates a buffered image with the given size that shows the given
     * text.<br>
     * <br>
     * The given text may be HTML. Further details about the image are
     * not specified.
     *
     * @param sizeX The size of the image in x-direction
     * @param sizeY The size of the image in x-direction
     * @param fontSize The size of the font
     * @param text The text
     * @return The buffered image
     */
    private static BufferedImage createImageWithText(
        int sizeX, int sizeY,
        int fontSize, String text)
    {
        Font font = new Font("Monospaced", Font.PLAIN, fontSize);
        return createImageWithText(sizeX, sizeY, font, text,
            Color.BLACK, Color.WHITE);
    }

    /**
     * Creates a buffered image with the given size that shows the given
     * text.<br>
     * <br>
     * The given text may be HTML. Further details about the image are
     * not specified.
     *
     * @param sizeX The size of the image in x-direction
     * @param sizeY The size of the image in x-direction
     * @param font The font
     * @param text The text
     * @param foregroundColor The foreground color
     * @param backgroundColor The background color
     * @return The buffered image
     */
    private static BufferedImage createImageWithText(
        int sizeX, int sizeY,
        Font font, String text,
        Color foregroundColor,
        Color backgroundColor)
    {
        JLabel label = new JLabel(text);
        label.setBackground(backgroundColor);
        label.setForeground(foregroundColor);
        label.setFont(font);
        label.setOpaque(true);
        label.setSize(sizeX, sizeY);
        BufferedImage image = new BufferedImage(
            sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        label.paint(g);
        g.dispose();
        return image;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ImageModels()
    {
        // Private constructor to prevent instantiation
    }
}

