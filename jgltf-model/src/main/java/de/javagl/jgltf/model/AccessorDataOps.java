/*
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
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import de.javagl.jgltf.model.io.Buffers;

/**
 * Bulk operations for reading or writing accessor data to and from buffers.
 * <br>
 * Unless otherwise noted, none of the arguments to these methods may be
 * <code>null</code>.
 */
public class AccessorDataOps
{
    /**
     * Read the data from the given accessor data, write it into the given
     * target as integer values, and return the target.
     * 
     * If the target is null or its size does not match the total number of
     * components of the accessor data, then a new buffer will be created and
     * returned.
     * 
     * For accessor data that contains 'byte' or 'short' data, this will take
     * into account whether the data is 'unsigned': If the data is unsigned, the
     * value will be converted into an unsigned integer value.
     * 
     * @param accessorData The accessor
     * @param target The optional target
     * @return The result
     * @throws IllegalArgumentException If the given accessor data does not have
     *         a 'byte', 'short', or 'int' component type.
     */
    public static IntBuffer readAsInts(AccessorData accessorData,
        IntBuffer target)
    {
        Class<?> componentType = accessorData.getComponentType();
        if (componentType == byte.class)
        {
            AccessorByteData accessorByteData = (AccessorByteData) accessorData;
            return readAsInts(accessorByteData, target);
        }
        if (componentType == short.class)
        {
            AccessorShortData accessorShortData =
                (AccessorShortData) accessorData;
            return readAsInts(accessorShortData, target);
        }
        if (componentType == int.class)
        {
            AccessorIntData accessorIntData = (AccessorIntData) accessorData;
            return readInts(accessorIntData, target);
        }
        throw new IllegalArgumentException(
            "Expected component type to be byte, short, or int, but was "
                + componentType);
    }

    /**
     * Internal method for reading integer values from byte accessor data.
     * 
     * See {@link #readAsInts(AccessorData, IntBuffer)}.
     * 
     * @param accessorData The accessor data
     * @param target The target
     * @return The result
     */
    private static IntBuffer readAsInts(AccessorByteData accessorData,
        IntBuffer target)
    {
        IntBuffer result = target;
        int n = accessorData.getTotalNumComponents();
        if (result == null || result.capacity() != n)
        {
            result = Buffers.create(n * Integer.BYTES).asIntBuffer();
        }
        for (int i = 0; i < n; i++)
        {
            int v = accessorData.getInt(i);
            result.put(i, v);
        }
        return result;
    }

    /**
     * Internal method for reading integer values from short accessor data.
     * 
     * See {@link #readAsInts(AccessorData, IntBuffer)}.
     * 
     * @param accessorData The accessor data
     * @param target The target
     * @return The result
     */
    private static IntBuffer readAsInts(AccessorShortData accessorData,
        IntBuffer target)
    {
        IntBuffer result = target;
        int n = accessorData.getTotalNumComponents();
        if (result == null || result.capacity() != n)
        {
            result = Buffers.create(n * Integer.BYTES).asIntBuffer();
        }
        for (int i = 0; i < n; i++)
        {
            int v = accessorData.getInt(i);
            result.put(i, v);
        }
        return result;
    }

    /**
     * Read the data from the given accessor data and write it into the given
     * target buffer.
     * 
     * If the target is null or its size does not match the total number of
     * components of the accessor data, then a new buffer will be created and
     * returned.
     * 
     * @param accessorData The accessor data
     * @param target The target buffer
     * @return The result
     */
    public static ByteBuffer readBytes(AccessorByteData accessorData,
        ByteBuffer target)
    {
        ByteBuffer result = target;
        int n = accessorData.getTotalNumComponents();
        if (result == null || result.capacity() != n)
        {
            result = Buffers.create(n);
        }
        for (int i = 0; i < n; i++)
        {
            byte v = accessorData.get(i);
            result.put(i, v);
        }
        return result;
    }

    /**
     * Read the data from the given accessor data and write it into the given
     * target buffer.
     * 
     * If the target is null or its size does not match the total number of
     * components of the accessor data, then a new buffer will be created and
     * returned.
     * 
     * @param accessorData The accessor data
     * @param target The target buffer
     * @return The result
     */
    public static ShortBuffer readShorts(AccessorShortData accessorData,
        ShortBuffer target)
    {
        ShortBuffer result = target;
        int n = accessorData.getTotalNumComponents();
        if (result == null || result.capacity() != n)
        {
            result = Buffers.create(n * Short.BYTES).asShortBuffer();
        }
        for (int i = 0; i < n; i++)
        {
            short v = accessorData.get(i);
            result.put(i, v);
        }
        return result;
    }

    /**
     * Read the data from the given accessor data and write it into the given
     * target buffer.
     * 
     * If the target is null or its size does not match the total number of
     * components of the accessor data, then a new buffer will be created and
     * returned.
     * 
     * @param accessorData The accessor data
     * @param target The target buffer
     * @return The result
     */
    public static IntBuffer readInts(AccessorIntData accessorData,
        IntBuffer target)
    {
        IntBuffer result = target;
        int n = accessorData.getTotalNumComponents();
        if (result == null || result.capacity() != n)
        {
            result = Buffers.create(n * Integer.BYTES).asIntBuffer();
        }
        for (int i = 0; i < n; i++)
        {
            int v = accessorData.get(i);
            result.put(i, v);
        }
        return result;
    }

    /**
     * Read the data from the given accessor data and write it into the given
     * target buffer.
     * 
     * If the target is null or its size does not match the total number of
     * components of the accessor data, then a new buffer will be created and
     * returned.
     * 
     * @param accessorData The accessor data
     * @param target The target buffer
     * @return The result
     */
    public static FloatBuffer readFloats(AccessorFloatData accessorData,
        FloatBuffer target)
    {
        FloatBuffer result = target;
        int n = accessorData.getTotalNumComponents();
        if (result == null || result.capacity() != n)
        {
            result = Buffers.create(n * Float.BYTES).asFloatBuffer();
        }
        for (int i = 0; i < n; i++)
        {
            float v = accessorData.get(i);
            result.put(i, v);
        }
        return result;
    }

    /**
     * Read the data from the given accessor data and write it into the given
     * target buffer.
     * 
     * If the target is null or its size does not match the total number of
     * components of the accessor data, then a new buffer will be created and
     * returned.
     * 
     * @param accessorData The accessor data
     * @param target The target buffer
     * @return The result
     */
    public static DoubleBuffer readDoubles(AccessorDoubleData accessorData,
        DoubleBuffer target)
    {
        DoubleBuffer result = target;
        int n = accessorData.getTotalNumComponents();
        if (result == null || result.capacity() != n)
        {
            result = Buffers.create(n * Double.BYTES).asDoubleBuffer();
        }
        for (int i = 0; i < n; i++)
        {
            double v = accessorData.get(i);
            result.put(i, v);
        }
        return result;
    }

    /**
     * Write the data from the given source buffer into the given acccessor
     * data.
     * 
     * The number of elements written will be the minimum of the buffer capacity
     * and the total number of components in the accessor data.
     * 
     * @param source The source buffer
     * @param accessorData The accessor data
     */
    public static void writeBytes(ByteBuffer source,
        AccessorByteData accessorData)
    {
        int n0 = source.capacity();
        int n1 = accessorData.getTotalNumComponents();
        int n = Math.min(n0, n1);
        for (int i = 0; i < n; i++)
        {
            byte v = source.get(i);
            accessorData.set(i, v);
        }
    }

    /**
     * Write the data from the given source buffer into the given acccessor
     * data.
     * 
     * The number of elements written will be the minimum of the buffer capacity
     * and the total number of components in the accessor data.
     * 
     * @param source The source buffer
     * @param accessorData The accessor data
     */
    public static void writeShorts(ShortBuffer source,
        AccessorShortData accessorData)
    {
        int n0 = source.capacity();
        int n1 = accessorData.getTotalNumComponents();
        int n = Math.min(n0, n1);
        for (int i = 0; i < n; i++)
        {
            short v = source.get(i);
            accessorData.set(i, v);
        }
    }

    /**
     * Write the data from the given source buffer into the given acccessor
     * data.
     * 
     * The number of elements written will be the minimum of the buffer capacity
     * and the total number of components in the accessor data.
     * 
     * @param source The source buffer
     * @param accessorData The accessor data
     */
    public static void writeInts(IntBuffer source, AccessorIntData accessorData)
    {
        int n0 = source.capacity();
        int n1 = accessorData.getTotalNumComponents();
        int n = Math.min(n0, n1);
        for (int i = 0; i < n; i++)
        {
            int v = source.get(i);
            accessorData.set(i, v);
        }
    }

    /**
     * Write the data from the given source buffer into the given acccessor
     * data.
     * 
     * The number of elements written will be the minimum of the buffer capacity
     * and the total number of components in the accessor data.
     * 
     * @param source The source buffer
     * @param accessorData The accessor data
     */
    public static void writeFloats(FloatBuffer source,
        AccessorFloatData accessorData)
    {
        int n0 = source.capacity();
        int n1 = accessorData.getTotalNumComponents();
        int n = Math.min(n0, n1);
        for (int i = 0; i < n; i++)
        {
            float v = source.get(i);
            accessorData.set(i, v);
        }
    }

    /**
     * Write the data from the given source buffer into the given acccessor
     * data.
     * 
     * The number of elements written will be the minimum of the buffer capacity
     * and the total number of components in the accessor data.
     * 
     * @param source The source buffer
     * @param accessorData The accessor data
     */
    public static void writeDoubles(DoubleBuffer source,
        AccessorDoubleData accessorData)
    {
        int n0 = source.capacity();
        int n1 = accessorData.getTotalNumComponents();
        int n = Math.min(n0, n1);
        for (int i = 0; i < n; i++)
        {
            double v = source.get(i);
            accessorData.set(i, v);
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AccessorDataOps()
    {
        // Private constructor to prevent instantiation
    }

}
