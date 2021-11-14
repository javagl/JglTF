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

import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorShortData;

/**
 * Utility methods related to indices to be extracted from {@link AccessorData}
 */
class AccessorIndexUtils
{
    /**
     * Returns the indices that are stored in the given {@link AccessorData}.
     * 
     * This assumes the given data to be either a {@link AccessorByteData},
     * {@link AccessorShortData} or {@link AccessorIntData}.
     * 
     * This method writes all components from the given data into an array.
     * This assumes that {@link AccessorData#getNumComponentsPerElement()} 
     * is 1, but this is not checked explicitly. 
     * 
     * @param accessorData The {@link AccessorData}
     * @return The indices
     * @throws IllegalArgumentException If the given data does not have one
     * of the valid types.
     */
    static int[] extractIndices(AccessorData accessorData)
    {
        if (accessorData instanceof AccessorByteData) 
        {
            AccessorByteData accessorByteData = 
                (AccessorByteData) accessorData;
            return extractIndicesFromBytes(accessorByteData);
        }
        if (accessorData instanceof AccessorShortData) 
        {
            AccessorShortData accessorShortData = 
                (AccessorShortData) accessorData;
            return extractIndicesFromShorts(accessorShortData);
        }
        if (accessorData instanceof AccessorIntData) 
        {
            AccessorIntData accessorIntData = 
                (AccessorIntData) accessorData;
            return extractIndicesFromInts(accessorIntData);
        }
        throw new IllegalArgumentException(
            "Not a valid index type: " + accessorData);
    }
    
    /**
     * Implementation of {@link #extractIndices(AccessorData)} for bytes
     * 
     * @param accessorByteData The input data
     * @return The indices
     */
    private static int[] extractIndicesFromBytes(
        AccessorByteData accessorByteData)
    {
        int n = accessorByteData.getNumElements();
        int result[] = new int[n];
        for (int i = 0; i < n; i++)
        {
            result[i] = accessorByteData.getInt(i);
        }
        return result;
    }

    /**
     * Implementation of {@link #extractIndices(AccessorData)} for shorts
     * 
     * @param accessorShortData The input data
     * @return The indices
     */
    private static int[] extractIndicesFromShorts(
        AccessorShortData accessorShortData)
    {
        int n = accessorShortData.getNumElements();
        int result[] = new int[n];
        for (int i = 0; i < n; i++)
        {
            result[i] = accessorShortData.getInt(i);
        }
        return result;
    }

    /**
     * Implementation of {@link #extractIndices(AccessorData)} for ints
     * 
     * @param accessorIntData The input data
     * @return The indices
     */
    private static int[] extractIndicesFromInts(
        AccessorIntData accessorIntData)
    {
        int n = accessorIntData.getNumElements();
        int result[] = new int[n];
        for (int i = 0; i < n; i++)
        {
            result[i] = accessorIntData.get(i);
        }
        return result;
    }
    
    /**
     * Create default indices for the given number of triangles
     * 
     * @param numTriangles The number of triangles
     * @return The indices
     */
    static int[] createDefaultIndices(int numTriangles)
    {
        int n = numTriangles * 3;
        int result[] = new int[n];
        for (int i = 0; i < n; i++)
        {
            result[i] = i;
        }
        return result;
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private AccessorIndexUtils()
    {
        // Private constructor to prevent instantiation
    }

    
}
