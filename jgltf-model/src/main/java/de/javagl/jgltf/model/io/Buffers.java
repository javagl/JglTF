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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

/**
 * Utility methods related to buffers
 */
public class Buffers
{
    /**
     * Returns the contents of the given byte buffer as a string, using
     * the platform's default charset, or <code>null</code> if the given 
     * buffer is <code>null</code>.
     * 
     * @param byteBuffer The byte buffer
     * @return The data as a string
     */
    public static String readAsString(ByteBuffer byteBuffer)
    {
        if (byteBuffer == null)
        {
            return null;
        }
        byte array[] = new byte[byteBuffer.capacity()];
        byteBuffer.slice().get(array);
        return new String(array);
    }
    
    /**
     * Returns the contents of the given buffer as a <code>BufferedImage</code>,
     * or <code>null</code> if the given buffer is <code>null</code>, or
     * the data can not be converted into a buffered image.
     * 
     * TODO TODO_ANDROID This may be moved to a different class
     * 
     * @param byteBuffer The byte buffer
     * @return The buffered image
     */
    public static BufferedImage readAsBufferedImage(ByteBuffer byteBuffer)
    {
        if (byteBuffer == null)
        {
            return null;
        }
        try (InputStream inputStream = 
            createByteBufferInputStream(byteBuffer.slice()))
        {
            return ImageIO.read(inputStream);
        }
        catch (IOException e)
        {
            return null;
        }
    }
    
    /**
     * Create a slice of the given byte buffer, in the specified range.
     * The returned buffer will have the same byte order as the given
     * buffer.
     * 
     * @param byteBuffer The byte buffer
     * @param position The position where the slice should start
     * @param length The length of the slice
     * @return The slice
     * @throws IllegalArgumentException If the range that is specified
     * by the position and length are not valid for the given buffer
     */
    public static ByteBuffer createSlice(
        ByteBuffer byteBuffer, int position, int length)
    {
        int oldPosition = byteBuffer.position();
        int oldLimit = byteBuffer.limit();
        byteBuffer.limit(position + length);
        byteBuffer.position(position);
        ByteBuffer slice = byteBuffer.slice();
        slice.order(byteBuffer.order());
        byteBuffer.limit(oldLimit);
        byteBuffer.position(oldPosition);
        return slice;
    }
    
    /**
     * Creates a new, direct byte buffer that contains the given data,
     * with little-endian byte order
     *  
     * @param data The data
     * @return The byte buffer
     */
    public static ByteBuffer create(byte data[])
    {
        return create(data, 0, data.length);
    }
    
    /**
     * Creates a new, direct byte buffer that contains the specified range
     * of the given data, with little-endian byte order
     *  
     * @param data The data
     * @param offset The offset in the data array
     * @param length The length of the range
     * @return The byte buffer
     */
    public static ByteBuffer create(byte data[], int offset, int length)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(length);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(data, offset, length);
        byteBuffer.position(0);
        return byteBuffer;
    }
    
    /**
     * Create a new direct byte buffer with the given size, and native byte
     * order.
     * 
     * @param size The size of the buffer
     * @return The byte buffer
     * @throws IllegalArgumentException If the given size is negative
     */
    public static ByteBuffer create(int size)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer;
    }
    
    
    /**
     * Create an input stream from the given byte buffer, starting at its
     * current position, up to its current limit. Reading the returned
     * stream will advance the position of the buffer. If this is not 
     * desired, a slice of the buffer may be passed to this method.
     * 
     * @param byteBuffer The buffer
     * @return The input stream
     */
    public static InputStream createByteBufferInputStream(
        ByteBuffer byteBuffer)
    {
        return new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                if (!byteBuffer.hasRemaining())
                {
                    return -1;
                }
                return byteBuffer.get() & 0xFF;
            }
    
            @Override
            public int read(byte[] bytes, int off, int len) throws IOException
            {
                if (!byteBuffer.hasRemaining())
                {
                    return -1;
                }
                int readLength = Math.min(len, byteBuffer.remaining());
                byteBuffer.get(bytes, off, readLength);
                return readLength;
            }
        };
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private Buffers()
    {
        // Private constructor to prevent instantiation
    }
}
