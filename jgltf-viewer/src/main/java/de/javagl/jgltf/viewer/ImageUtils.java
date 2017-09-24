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
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import de.javagl.jgltf.model.io.Buffers;

/**
 * Utility methods related to images
 */
class ImageUtils
{
    /**
     * Returns the contents of the given buffer as a <code>BufferedImage</code>,
     * or <code>null</code> if the given buffer is <code>null</code>, or
     * the data can not be converted into a buffered image.
     * 
     * @param byteBuffer The byte buffer
     * @return The buffered image
     */
    static BufferedImage readAsBufferedImage(ByteBuffer byteBuffer)
    {
        if (byteBuffer == null)
        {
            return null;
        }
        try (InputStream inputStream = 
            Buffers.createByteBufferInputStream(byteBuffer.slice()))
        {
            return ImageIO.read(inputStream);
        }
        catch (IOException e)
        {
            return null;
        }
    }

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
        
        // Note: The byte order is BIG_ENDIAN by default. This order
        // is kept here, to keep the ARGB order, and not convert them
        // to BGRA implicitly.
        ByteBuffer outputByteBuffer = ByteBuffer
            .allocateDirect(imageBuffer.remaining() * Integer.BYTES)
            .order(ByteOrder.BIG_ENDIAN);
        IntBuffer output = outputByteBuffer.asIntBuffer();
        output.put(imageBuffer.slice());
        return outputByteBuffer;
    }

    /**
     * Interpret the given byte buffer as ARGB pixels, and convert it into
     * a direct byte buffer containing the corresponding RGBA pixels
     * 
     * @param pixels The input pixels
     * @return The output pixels
     */
    static ByteBuffer swizzleARGBtoRGBA(ByteBuffer pixels)
    {
        return swizzle(pixels, 16, 8, 0, 24);
    }
    
    /**
     * Interpret the given byte buffer as pixels, swizzle the bytes
     * of these pixels according to the given shifts, and return a 
     * a direct byte buffer containing the corresponding new pixels
     * 
     * @param pixels The input pixels
     * @param s0 The right-shift for byte 0 
     * @param s1 The right-shift for byte 1 
     * @param s2 The right-shift for byte 2
     * @param s3 The right-shift for byte 3
     * @return The output pixels
     */
    private static ByteBuffer swizzle(ByteBuffer pixels,
        int s0, int s1, int s2, int s3)
    {
        IntBuffer iBuffer = pixels.asIntBuffer();
        ByteBuffer oByteBuffer = ByteBuffer
            .allocateDirect(iBuffer.capacity() * Integer.BYTES)
            .order(pixels.order());
        IntBuffer oBuffer = oByteBuffer.asIntBuffer();
        for (int i = 0; i < iBuffer.capacity(); i++)
        {
            int input = iBuffer.get(i);
            int output = swizzle(input, s0, s1, s2, s3);
            oBuffer.put(i, output);
        }
        return oByteBuffer;
    }
    
    /**
     * Swizzle the bytes of the given input according to the given shifts
     * 
     * @param input The input 
     * @param s0 The right-shift for byte 0 
     * @param s1 The right-shift for byte 1 
     * @param s2 The right-shift for byte 2
     * @param s3 The right-shift for byte 3
     * @return The output
     */
    private static int swizzle(int input, int s0, int s1, int s2, int s3)
    {
        int b0 = (input >> s0) & 0xFF; 
        int b1 = (input >> s1) & 0xFF; 
        int b2 = (input >> s2) & 0xFF; 
        int b3 = (input >> s3) & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
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
