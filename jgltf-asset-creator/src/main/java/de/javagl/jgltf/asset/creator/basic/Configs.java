/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.asset.creator.basic;

/**
 * Utility methods related to the {@link Config} class of this package.
 */
public class Configs
{
    /**
     * Create a string of the given config
     * 
     * @param config The config
     * @return The string
     */
    public static String createString(Config config)
    {
        return createString(config, ": ", "\n");
    }

    /**
     * Create a string of the given config
     * 
     * @param config The config
     * @param valueSeparator The value separator
     * @param lineSeparator The line separator
     * @return The string
     */
    static String createString(Config config, String valueSeparator,
        String lineSeparator)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("numMeshPrimitives" + valueSeparator
            + config.numMeshPrimitives + lineSeparator);
        sb.append("pointSizes" + valueSeparator
            + createString(config.pointSizes) + lineSeparator);
        sb.append("numTextures" + valueSeparator + config.numTextures
            + lineSeparator);
        sb.append("pixelSizes" + valueSeparator
            + createString(config.pixelSizes) + lineSeparator);
        sb.append("numMaterials" + valueSeparator + config.numMaterials
            + lineSeparator);
        sb.append(
            "numMeshes" + valueSeparator + config.numMeshes + lineSeparator);
        sb.append("numMeshPrimitivesPerMesh" + valueSeparator
            + config.numMeshPrimitivesPerMesh + lineSeparator);
        sb.append(
            "numNodes" + valueSeparator + config.numNodes + lineSeparator);
        sb.append("gridDimensions" + valueSeparator + config.gridDimensions
            + lineSeparator);
        sb.append("noiseGeometry" + valueSeparator + config.noiseGeometry
            + lineSeparator);
        sb.append("noiseTextures" + valueSeparator + config.noiseTextures);
        return sb.toString();
    }

    /**
     * Create a string of the given array
     * 
     * @param array The array
     * @return The string
     */
    private static String createString(int array[][])
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++)
        {
            if (i > 0)
            {
                sb.append(", ");
            }
            sb.append(createString(array[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Create a string of the given array
     * 
     * @param array The array
     * @return The string
     */
    private static String createString(int array[])
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++)
        {
            if (i > 0)
            {
                sb.append(", ");
            }
            sb.append(array[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Configs()
    {
        // Private constructor to prevent instantiation
    }

}
