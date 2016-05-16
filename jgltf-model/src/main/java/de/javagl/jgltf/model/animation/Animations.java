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
package de.javagl.jgltf.model.animation;

import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.animation.Interpolators.InterpolatorType;

/**
 * Methods to create {@link Animation} instances
 */
public class Animations
{
    /**
     * Creates a new {@link Animation} from the given input data
     * 
     * @param timeData The (1D) {@link AccessorFloatData} containing the
     * time key frames
     * @param outputData The output data that contains the value key frames
     * @param interpolatorType The {@link InterpolatorType} that should
     * be used
     * @return The {@link Animation}
     */
    public static Animation create(
        AccessorFloatData timeData,
        AccessorFloatData outputData, 
        InterpolatorType interpolatorType)
    {
        int numElements = timeData.getNumElements();
        float keys[] = new float[numElements];
        for (int e=0; e<numElements; e++)
        {
            keys[e] = timeData.get(e);
        }
        
        int numComponents = outputData.getNumComponentsPerElement();
        float values[][] = new float[numElements][numComponents];
        for (int c=0; c<numComponents; c++)
        {
            for (int e=0; e<numElements; e++)
            {
                values[e][c] = outputData.get(e, c);
            }
        }
        return new Animation(keys, values, interpolatorType);
    }
 
    /**
     * Private constructor to prevent instantiation
     */
    private Animations()
    {
        // Private constructor to prevent instantiation
    }
    
}