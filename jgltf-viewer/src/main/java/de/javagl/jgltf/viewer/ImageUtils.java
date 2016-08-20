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
package de.javagl.jgltf.viewer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Utility methods related to images
 */
class ImageUtils
{
    /**
     * Returns a direct byte buffer that contains the ARGB pixel values of
     * the given image. <br>
     * <br>
     * The given image might become unmanaged/untrackable by this operation.
     * 
     * @param inputImage The input image
     * @param flipVertically Whether the contents of the image should be
     * flipped vertically. This is always a hassle.
     * @return The byte buffer containing the ARGB pixel values
     */
    static ByteBuffer getImagePixelsARGB(
        BufferedImage inputImage, boolean flipVertically)
    {
        BufferedImage image = inputImage;
        if (flipVertically)
        {
            image = flipVertically(image);
        }
        if (image.getType() != BufferedImage.TYPE_INT_ARGB)
        {
            image = convertToARGB(image);
        }
        IntBuffer imageBuffer = getBuffer(image);
        int numBytes = image.getWidth() * image.getHeight() * 4;
        ByteBuffer byteBuffer = ByteBuffer
            .allocateDirect(numBytes)
            .order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(imageBuffer);
        return byteBuffer;
    }
    
    /**
     * Convert the given image into a buffered image with the type
     * <code>TYPE_INT_ARGB</code>.
     * 
     * @param image The input image
     * @return The converted image
     */
    private static BufferedImage convertToARGB(BufferedImage image)
    {
        BufferedImage newImage = new BufferedImage(
            image.getWidth(), image.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
    
    /**
     * Create a vertically flipped version of the given image. 
     * 
     * @param image The input image
     * @return The flipped image
     */
    private static BufferedImage flipVertically(BufferedImage image)
    {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage newImage = new BufferedImage(
            w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, h, w, -h, null);
        g.dispose();
        return newImage;
    }
    
    /**
     * Returns the data buffer of the given image as an IntBuffer. The given
     * image will become unmanaged/untrackable by this call.
     * 
     * @param image The image
     * @return The data from the image as an IntBuffer
     * @throws IllegalArgumentException If the given image is not
     * backed by a DataBufferInt
     */
    private static IntBuffer getBuffer(BufferedImage image)
    {
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        if (!(dataBuffer instanceof DataBufferInt))
        {
            throw new IllegalArgumentException(
                "Invalid buffer type in image, " + 
                "only TYPE_INT_* is allowed");
        }
        DataBufferInt dataBufferInt = (DataBufferInt)dataBuffer;
        int data[] = dataBufferInt.getData();
        IntBuffer intBuffer = IntBuffer.wrap(data);
        return intBuffer;
    }

}
