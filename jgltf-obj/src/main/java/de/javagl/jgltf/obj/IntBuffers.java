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
import java.nio.IntBuffer;

import de.javagl.jgltf.model.io.Buffers;

/**
 * Utility methods related to <code>IntBuffer</code> instances
 * 
 * @deprecated This class will be removed in a future version
 */
public class IntBuffers
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
    public static ByteBuffer convertToByteBuffer(
        IntBuffer inputBuffer, int elementSize)
    {
        switch (elementSize)
        {
            case 1: return Buffers.castToByteBuffer(inputBuffer);
            case 2: return Buffers.castToShortByteBuffer(inputBuffer);
            case 4: return Buffers.createByteBufferFrom(inputBuffer);
            default:
                break;
        }
        throw new IllegalArgumentException(
            "The elementSize must be 1, 2 or 4, but is " + elementSize);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private IntBuffers()
    {
        // Private constructor to prevent instantiation
    }

}