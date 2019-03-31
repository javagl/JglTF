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
package de.javagl.jgltf.model.image;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

/**
 * Methods to create {@link PixelData} objects from raw image data.<br>
 * <br>
 * This class should not be considered to be part of the public API.
 */
public class PixelDatas
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(PixelDatas.class.getName());
    
    /**
     * Create a {@link PixelData} from the given image data. The image data
     * may for example the the raw data of a JPG or PNG or GIF file. The
     * exact set of supported file formats is not specified. If the given
     * data can not be read, then a warning is printed and <code>null</code>
     * is returned.
     * 
     * @param imageData The image data
     * @return The {@link PixelData}
     */
    public static PixelData create(ByteBuffer imageData)
    {
        BufferedImage textureImage = ImageUtils.readAsBufferedImage(imageData);
        if (textureImage == null)
        {
            logger.warning("Could not read image from image data");
            return null;
        }
            
        ByteBuffer pixelDataARGB = 
            ImageUtils.getImagePixelsARGB(textureImage, false);
        ByteBuffer pixelDataRGBA =
            ImageUtils.swizzleARGBtoRGBA(pixelDataARGB);
        int width = textureImage.getWidth();
        int height = textureImage.getHeight();
        return new DefaultPixelData(width, height, pixelDataRGBA);
    }
    
    /**
     * Create an unspecified {@link PixelData} object that may be used as 
     * a placeholder for image data that could not be read
     * 
     * @return The {@link PixelData} object
     */
    public static PixelData createErrorPixelData()
    {
        // Right now, this is a 2x2 checkerboard of red and white pixels
        ByteBuffer pixelDataRGBA = ByteBuffer.allocateDirect(4 * Integer.SIZE);
        IntBuffer intPixelDataRGBA = 
            pixelDataRGBA.order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        intPixelDataRGBA.put(0, 0xFF0000FF);
        intPixelDataRGBA.put(1, 0xFFFFFFFF);
        intPixelDataRGBA.put(2, 0xFF0000FF);
        intPixelDataRGBA.put(3, 0xFFFFFFFF);
        int width = 2;
        int height = 2;
        return new DefaultPixelData(width, height, pixelDataRGBA);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private PixelDatas()
    {
        // Private constructor to prevent instantiation
    }
}
