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
package de.javagl.jgltf.model.khr.draco_mesh_compression;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.GltfException;

/**
 * Internal utility methods for this package, for interoperation between
 * accessor models and buffers
 */
class AccessorDataBuffers
{
    /**
     * Fill the given accessor model with the given data, casting all elements
     * to the required target type
     * 
     * @param accessorModel The accessor model
     * @param values The values
     * @throws GltfException If the component type is not 'byte', 'short', or
     *         'int'
     */
    static void fillFromInt(AccessorModel accessorModel, IntBuffer values)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        Class<?> componentType = accessorData.getComponentType();
        if (componentType == byte.class)
        {
            AccessorByteData accessorByteData = (AccessorByteData) accessorData;
            fill(accessorByteData, values);
        }
        else if (componentType == short.class)
        {
            AccessorShortData accessorShortData =
                (AccessorShortData) accessorData;
            fill(accessorShortData, values);
        }
        else if (componentType == int.class)
        {
            AccessorIntData accessorIntData = (AccessorIntData) accessorData;
            fill(accessorIntData, values);
        }
        else
        {
            throw new GltfException(
                "Expected component type to be byte, short, or int, but was "
                    + componentType);
        }
    }

    /**
     * Fill the given accessor model with the given data
     * 
     * @param accessorModel The accessor model
     * @param values The values
     * @throws GltfException If the component type is not 'byte'
     */
    static void fill(AccessorModel accessorModel, ByteBuffer values)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        Class<?> componentType = accessorData.getComponentType();
        if (componentType == byte.class)
        {
            AccessorByteData accessorByteData = (AccessorByteData) accessorData;
            fill(accessorByteData, values);
        }
        else
        {
            throw new GltfException(
                "Expected component type to be byte, but was " + componentType);
        }
    }

    /**
     * Fill the given accessor model with the given data
     * 
     * @param accessorModel The accessor model
     * @param values The values
     * @throws GltfException If the component type is not 'short'
     */
    static void fill(AccessorModel accessorModel, ShortBuffer values)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        Class<?> componentType = accessorData.getComponentType();
        if (componentType == short.class)
        {
            AccessorShortData accessorShortData =
                (AccessorShortData) accessorData;
            fill(accessorShortData, values);
        }
        else
        {
            throw new GltfException(
                "Expected component type to be short, but was "
                    + componentType);
        }
    }

    /**
     * Fill the given accessor model with the given data
     * 
     * @param accessorModel The accessor model
     * @param values The values
     * @throws GltfException If the component type is not 'float'
     */
    static void fill(AccessorModel accessorModel, FloatBuffer values)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        Class<?> componentType = accessorData.getComponentType();
        if (componentType == float.class)
        {
            AccessorFloatData accessorFloatData =
                (AccessorFloatData) accessorData;
            fill(accessorFloatData, values);
        }
        else
        {
            throw new GltfException(
                "Expected component type to be float, but was "
                    + componentType);
        }
    }

    /**
     * Fill the given accessor data with the given values
     * 
     * @param accessorData The accessor data
     * @param values The values
     */
    private static void fill(AccessorByteData accessorData, ByteBuffer values)
    {
        int n =
            Math.min(values.capacity(), accessorData.getTotalNumComponents());
        for (int i = 0; i < n; i++)
        {
            accessorData.set(i, values.get(i));
        }
    }

    /**
     * Fill the given accessor data with the given values
     * 
     * @param accessorData The accessor data
     * @param values The values
     */
    private static void fill(AccessorShortData accessorData, ShortBuffer values)
    {
        int n =
            Math.min(values.capacity(), accessorData.getTotalNumComponents());
        for (int i = 0; i < n; i++)
        {
            accessorData.set(i, values.get(i));
        }
    }

    /**
     * Fill the given accessor data with the given values
     * 
     * @param accessorData The accessor data
     * @param values The values
     */
    private static void fill(AccessorByteData accessorData, IntBuffer values)
    {
        int n =
            Math.min(values.capacity(), accessorData.getTotalNumComponents());
        for (int i = 0; i < n; i++)
        {
            accessorData.set(i, (byte) values.get(i));
        }
    }

    /**
     * Fill the given accessor data with the given values
     * 
     * @param accessorData The accessor data
     * @param values The values
     */
    private static void fill(AccessorShortData accessorData, IntBuffer values)
    {
        int n =
            Math.min(values.capacity(), accessorData.getTotalNumComponents());
        for (int i = 0; i < n; i++)
        {
            accessorData.set(i, (short) values.get(i));
        }
    }

    /**
     * Fill the given accessor data with the given values
     * 
     * @param accessorData The accessor data
     * @param values The values
     */
    private static void fill(AccessorIntData accessorData, IntBuffer values)
    {
        int n =
            Math.min(values.capacity(), accessorData.getTotalNumComponents());
        for (int i = 0; i < n; i++)
        {
            accessorData.set(i, values.get(i));
        }
    }

    /**
     * Fill the given accessor data with the given values
     * 
     * @param accessorData The accessor data
     * @param values The values
     */
    private static void fill(AccessorFloatData accessorData, FloatBuffer values)
    {
        int n =
            Math.min(values.capacity(), accessorData.getTotalNumComponents());
        for (int i = 0; i < n; i++)
        {
            accessorData.set(i, values.get(i));
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AccessorDataBuffers()
    {
        // Private constructor to prevent instantiation
    }

}
