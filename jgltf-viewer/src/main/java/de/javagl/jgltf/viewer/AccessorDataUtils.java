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
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorShortData;

/**
 * Utility methods for extracting raw data from {@link AccessorData}
 */
class AccessorDataUtils
{
    /**
     * Returns the values that are stored in the given {@link AccessorData}.
     * 
     * This assumes the given data to be either a {@link AccessorByteData},
     * {@link AccessorShortData} or {@link AccessorIntData}.
     * 
     * This method writes all components from the given data into an array.
     * 
     * @param accessorData The {@link AccessorData}
     * @return The int values
     * @throws IllegalArgumentException If the given data does not have one
     * of the valid types.
     */
    static int[] readInts(AccessorData accessorData)
    {
        int numElements = accessorData.getNumElements();
        int numComponents = accessorData.getNumComponentsPerElement();        
        if (accessorData instanceof AccessorByteData) 
        {
            AccessorByteData accessorByteData = 
                (AccessorByteData) accessorData;
            return readIntsFromBytes(
                accessorByteData, numElements, numComponents);
        }
        if (accessorData instanceof AccessorShortData) 
        {
            AccessorShortData accessorShortData = 
                (AccessorShortData) accessorData;
            return readIntsFromShorts(
                accessorShortData, numElements, numComponents);
        }
        if (accessorData instanceof AccessorIntData) 
        {
            AccessorIntData accessorIntData = 
                (AccessorIntData) accessorData;
            return readIntsFromInts(
                accessorIntData, numElements, numComponents);
        }
        throw new IllegalArgumentException(
            "Not a valid index type: " + accessorData);
    }
    
    /**
     * Implementation of {@link #readInts(AccessorData)} for bytes
     * 
     * @param accessorByteData The input data
     * @param numElements The number of elements
     * @param numComponents The number of components per element
     * @return The indices
     */
    private static int[] readIntsFromBytes(
        AccessorByteData accessorByteData, int numElements, int numComponents)
    {
        int n = numElements * numComponents;
        int result[] = new int[n];
        int index = 0;
        for (int e = 0; e < numElements; e++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                result[index] = accessorByteData.getInt(e, c);
                index++;
            }
        }
        return result;
    }

    /**
     * Implementation of {@link #readInts(AccessorData)} for shorts
     * 
     * @param accessorShortData The input data
     * @param numElements The number of elements
     * @param numComponents The number of components per element
     * @return The indices
     */
    private static int[] readIntsFromShorts(
        AccessorShortData accessorShortData, int numElements, int numComponents)
    {
        int n = numElements * numComponents;
        int result[] = new int[n];
        int index = 0;
        for (int e = 0; e < numElements; e++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                result[index] = accessorShortData.getInt(e, c);
                index++;
            }
        }
        return result;
    }

    /**
     * Implementation of {@link #readInts(AccessorData)} for ints
     * 
     * @param accessorIntData The input data
     * @param numElements The number of elements
     * @param numComponents The number of components per element
     * @return The indices
     */
    private static int[] readIntsFromInts(
        AccessorIntData accessorIntData, int numElements, int numComponents)
    {
        int n = numElements * numComponents;
        int result[] = new int[n];
        int index = 0;
        for (int e = 0; e < numElements; e++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                result[index] = accessorIntData.get(e, c);
                index++;
            }
        }
        return result;
    }    

    /**
     * Reads the raw data from the given {@link AccessorFloatData}.
     * 
     * This reads the specified number of components for each element
     * from the input, and writes them into a result array, without
     * any padding. This means that the given number of components
     * may be smaller than the number of components that the accessor
     * data actually has. 
     * 
     * @param accessorData The input data
     * @param numElements The number of elements
     * @param numComponents The number of components per element
     * @return The indices
     */
    static float[] readFloats(
        AccessorFloatData accessorData, int numElements, int numComponents)
    {
        int n = numElements * numComponents;
        float result[] = new float[n];
        int index = 0;
        for (int e = 0; e < numElements; e++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                result[index] = accessorData.get(e, c);
                index++;
            }
        }
        return result;
    }
    
    /**
     * Writes the given raw data into the given {@link AccessorFloatData}.
     * 
     * This reads the specified number of components for each element
     * from the data, and writes them into a the given accessor data. 
     * This means that the given number of components may be smaller 
     * than the number of components that the accessor data actually has. 
     * 
     * @param accessorData The input data
     * @param numElements The number of elements
     * @param numComponents The number of components per element
     * @param data The raw data
     */
    static void writeFloats(
        AccessorFloatData accessorData, int numElements, int numComponents, 
        float data[])
    {
        int index = 0;
        for (int e = 0; e < numElements; e++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                accessorData.set(e, c, data[index]);
                index++;
            }
        }
    }
    
    
    /**
     * Set the values of the given target {@link AccessorData} to the same
     * values as in the given source {@link AccessorData}. If either of
     * them has fewer elements (or fewer components per element) than the
     * other, then the minimum of both will be used, respectively.
     * 
     * @param target The target {@link AccessorData}
     * @param source The source {@link AccessorData}
     */
    static void copyFloats(
        AccessorFloatData target,
        AccessorFloatData source)
    {
        int numElements =
            Math.min(target.getNumElements(), source.getNumElements());
        int numComponents = Math.min(
            target.getNumComponentsPerElement(),
            source.getNumComponentsPerElement());
        for (int e = 0; e < numElements; e++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                float value = source.get(e, c);
                target.set(e, c, value);
            }
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorDataUtils()
    {
        // Private constructor to prevent instantiation
    }

    
}
