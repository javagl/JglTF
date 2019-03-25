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

/**
 * Utility methods of the buffer structure creation
 */
class Utils
{
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
    static int computeLeastCommonMultiple(int a, int b)
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
     * Private constructor to prevent instantiation
     */
    private Utils()
    {
        // Private constructor to prevent instantiation
    }
    
}
