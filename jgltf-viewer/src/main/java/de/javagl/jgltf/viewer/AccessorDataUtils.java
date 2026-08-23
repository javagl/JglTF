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

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;

/**
 * Utility methods related to {@link AccessorData}
 */
class AccessorDataUtils
{
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
