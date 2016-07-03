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
package de.javagl.jgltf.obj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;

/**
 * Utility methods related to buffers
 */
class Buffers
{
    /**
     * Convert the given input buffer to a byte buffer that contains the data
     * of the input buffer, converted to elements with the given element size.
     * If the given element size is 1 or 2, then the resulting buffer will
     * contain the bytes of the elements of the input buffer, casted to 
     * <code>byte</code> or <code>short</code>, respectively. If the element
     * size is 4, then the resulting buffer will contain the bytes of the 
     * elements of the input buffer. 
     * 
     * @param inputBuffer The input buffer
     * @param elementSize The element size
     * @return The byte buffer
     * @throws IllegalStateException If the given element size is neither
     * 1, 2 nor 4
     */
    static ByteBuffer convertToByteBuffer(
        IntBuffer inputBuffer, int elementSize)
    {
        switch (elementSize)
        {
            case 1: return createConvertedByteByteBuffer(inputBuffer);
            case 2: return createConvertedShortByteBuffer(inputBuffer);
            case 4: return createIntByteBuffer(inputBuffer);
            default:
                break;
        }
        throw new IllegalArgumentException(
            "The elementSize must be 1, 2 or 4, but is " + elementSize);
    }
    
    /**
     * Create a direct byte buffer with native byte order that contains the 
     * bytes of the elements of the given input buffer, casted to 
     * <code>byte</code>.
     *  
     * @param inputBuffer The input buffer
     * @return The byte buffer
     */
    private static ByteBuffer createConvertedByteByteBuffer(
        IntBuffer inputBuffer)
    {
        ByteBuffer byteBuffer = 
            ByteBuffer.allocateDirect(inputBuffer.capacity() * Byte.BYTES)
            .order(ByteOrder.nativeOrder());
        for (int i=0; i<inputBuffer.capacity(); i++)
        {
            byteBuffer.put(i, (byte)inputBuffer.get(i));
        }
        return byteBuffer;
    }
    
    /**
     * Create a direct byte buffer with native byte order that contains the 
     * bytes of the elements of the given input buffer, casted to 
     * <code>short</code>.
     *  
     * @param inputBuffer The input buffer
     * @return The byte buffer
     */
    private static ByteBuffer createConvertedShortByteBuffer(
        IntBuffer inputBuffer)
    {
        ByteBuffer byteBuffer = 
            ByteBuffer.allocateDirect(inputBuffer.capacity() * Short.BYTES)
            .order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        for (int i=0; i<inputBuffer.capacity(); i++)
        {
            shortBuffer.put(i, (short)inputBuffer.get(i));
        }
        return byteBuffer;
    }
    
    /**
     * Create a direct byte buffer with native byte order that contains the 
     * bytes of the elements of the given input buffer
     *  
     * @param inputBuffer The input buffer
     * @return The byte buffer
     */
    private static ByteBuffer createIntByteBuffer(IntBuffer inputBuffer)
    {
        ByteBuffer byteBuffer = 
            ByteBuffer.allocateDirect(inputBuffer.capacity() * Integer.BYTES)
            .order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(inputBuffer.slice());
        return byteBuffer;
    }
    
    /**
     * Create a direct byte buffer with native byte order that contains the 
     * bytes of the elements of the given input buffer. 
     *  
     * @param inputBuffer The input buffer
     * @return The byte buffer
     */
    static ByteBuffer createFloatByteBuffer(FloatBuffer inputBuffer)
    {
        ByteBuffer byteBuffer = 
            ByteBuffer.allocateDirect(inputBuffer.capacity() * Float.BYTES)
            .order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.slice().put(inputBuffer.slice());
        return byteBuffer;
    }
    
    /**
     * Create a direct byte buffer with native byte order whose contents is
     * a concatenation of the given byte buffers 
     * 
     * @param byteBuffers The input byte buffers
     * @return The concatenated byte buffer
     */
    static ByteBuffer concat(Collection<? extends ByteBuffer> byteBuffers)
    {
        if (byteBuffers.isEmpty())
        {
            return ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
        }
        int resultCapacity = byteBuffers.stream()
            .mapToInt(ByteBuffer::capacity)
            .reduce(0, (a,b) -> a + b);
        ByteBuffer newByteBuffer = ByteBuffer
            .allocateDirect(resultCapacity)
            .order(ByteOrder.nativeOrder());
        for (ByteBuffer byteBuffer : byteBuffers)
        {
            newByteBuffer.put(byteBuffer.slice());
        }
        newByteBuffer.position(0);
        return newByteBuffer;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Buffers()
    {
        // Private constructor to prevent instantiation
    }

}