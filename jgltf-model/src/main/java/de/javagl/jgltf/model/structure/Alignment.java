/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.structure;

import de.javagl.jgltf.model.AccessorModel;

/**
 * Methods related to the alignment and padding of data within the
 * accessor/bufferView/buffer structure of glTF.
 */
class Alignment
{
    /**
     * Compute the number of bytes that the given {@link AccessorModel} data
     * has to be aligned to. 
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The alignment bytes
     */
    static int computeAlignmentBytes(AccessorModel accessorModel)
    {
        return accessorModel.getComponentSizeInBytes();
    }
    
    /**
     * Compute the alignment for the given {@link AccessorModel} instances.
     * This is the least common multiple of the alignment of all instances.
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The alignment
     */
    static int computeAlignmentBytes(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int alignmentBytes = 1;
        for (AccessorModel accessorModel : accessorModels)
        {
            alignmentBytes = computeLeastCommonMultiple(alignmentBytes, 
                computeAlignmentBytes(accessorModel));
        }
        return alignmentBytes;
    }
    
    /**
     * Compute the byte stride that is common for the given 
     * {@link AccessorModel} instances.
     * 
     * This is the maximum of the {@link AccessorModel#getElementSizeInBytes() 
     * element sizes} and the {@link AccessorModel#getByteStride() byte 
     * strides} of the given models. 
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The common byte stride
     */
    private static int computeCommonByteStride(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int commonByteStride = 1;
        for (AccessorModel accessorModel : accessorModels)
        {
            int elementSize = accessorModel.getPaddedElementSizeInBytes();
            commonByteStride = Math.max(commonByteStride, elementSize);
            
            int byteStride = accessorModel.getByteStride();
            commonByteStride = Math.max(commonByteStride, byteStride);
        }
        return commonByteStride;
    }

    /**
     * Compute the byte stride that is common for the given 
     * {@link AccessorModel} instances when they are used as
     * vertex attributes. 
     * 
     * The byte stride for vertex attributes must be a multiple of 4. So the 
     * result of this function is the smallest multiple of 4 that is at least 
     * as large as the {@link #computeCommonByteStride(Iterable) common byte 
     * stride}. 
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The common byte stride
     */
    static int computeCommonVertexAttributeByteStride(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int commonByteStride = computeCommonByteStride(accessorModels);
        return pad(commonByteStride, 4);
    }

    /**
     * Computes the greatest common divisor of the given arguments
     * 
     * @param a The first argument
     * @param b The second argument
     * @return The greatest common divisor
     */
    private static int computeGreatestCommonDivisor(int a, int b) 
    {
        return b == 0 ? a : computeGreatestCommonDivisor(b, a % b);
    }
    
    /**
     * Computes the least common multiple of the given arguments
     * 
     * @param a The first argument
     * @param b The second argument
     * @return The least common multiple
     */
    private static int computeLeastCommonMultiple(int a, int b)
    {
        if (a == 0)
        {
            return b;
        }
        if (b == 0)
        {
            return a;
        }
        return (a * b) / computeGreatestCommonDivisor(a, b);
    }

    /**
     * Compute the padding that has to be added to the given size, in order
     * to achieve the given alignment
     * 
     * @param size The size
     * @param alignment The alignment
     * @return The padding
     */
    static int computePadding(int size, int alignment)
    {
        int remainder = size % alignment;
        if (remainder > 0)
        {
            return alignment - remainder;
        }
        return 0;
    }

    /**
     * Compute the smallest value that is a multiple of the given alignment,
     * and at least as large as the given size.
     * 
     * @param size The size
     * @param alignment The alignment
     * @return The padded size
     */
    static int pad(int size, int alignment)
    {
        return size + computePadding(size, alignment);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Alignment()
    {
        // Private constructor to prevent instantiation
    }
    
}
