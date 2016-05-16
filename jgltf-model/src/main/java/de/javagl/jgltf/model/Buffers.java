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
package de.javagl.jgltf.model;

import java.nio.ByteBuffer;

/**
 * Utility methods related to buffers
 */
class Buffers
{
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
     * Private constructor to prevent instantiation
     */
    private Buffers()
    {
        // Private constructor to prevent instantiation
    }

}
