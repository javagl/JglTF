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
package de.javagl.jgltf.model.impl.creation;

import de.javagl.jgltf.model.AccessorModel;

/**
 * Utility methods related to {@link AccessorModel} instances
 */
class AccessorModels 
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
            alignmentBytes = Utils.computeLeastCommonMultiple(alignmentBytes, 
                AccessorModels.computeAlignmentBytes(accessorModel));
        }
        return alignmentBytes;
    }
    
    /**
     * Compute the byte stride that is common for the given 
     * {@link AccessorModel} instances. This is the maximum of the
     * {@link AccessorModel#getElementSizeInBytes() element sizes}
     * of the given models.
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The common byte stride
     */
    static int computeCommonByteStride(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int commonByteStride = 1;
        for (AccessorModel accessorModel : accessorModels)
        {
            int elementSize = accessorModel.getElementSizeInBytes();
            commonByteStride = Math.max(commonByteStride, elementSize);
        }
        return commonByteStride;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorModels()
    {
        // Private constructor to prevent instantiation
    }
    
    
}