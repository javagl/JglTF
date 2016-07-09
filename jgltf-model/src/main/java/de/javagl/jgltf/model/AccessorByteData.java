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
package de.javagl.jgltf.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.BufferView;

/**
 * A class for accessing the data that is described by an {@link Accessor}.
 * It allows accessing the byte buffer of the {@link BufferView} of the
 * {@link Accessor}, depending on the {@link Accessor} parameters.<br>
 * <br> 
 * This data consists of several elements (for example, 3D byte vectors),
 * which consist of several components (for example, the 3 byte values).  
 */
public final class AccessorByteData
{
    /**
     * The number of bytes that each component of this data consists of
     */
    private static final int NUM_BYTES_PER_COMPONENT = Byte.BYTES;
    
    /**
     * The byte buffer of the {@link BufferView} that the {@link Accessor}
     * refers to
     */
    private final ByteBuffer bufferViewByteBuffer;

    /**
     * The number of elements
     */
    private final int numElements;
    
    /**
     * The number of components per element
     */
    private final int numComponentsPerElement;
    
    /**
     * The offset for the {@link Accessor} inside the byte buffer of
     * the {@link BufferView}
     */
    private final int byteOffset;
    
    /**
     * The stride, in number of bytes, between two consecutive elements 
     */
    private final int byteStridePerElement;
    
    /**
     * Whether the data should be interpreted as unsigned values
     */
    private final boolean unsigned;
    
    /**
     * Creates a new instance for accessing the data in the given 
     * {@link BufferView} byte buffer, according to the rules 
     * described by the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The byte buffer of the {@link BufferView}
     * @param unsigned Whether the data should be interpreted as unsigned
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_BYTE</code> or <code>GL_UNSIGEND_BYTE</code>
     * @throws IllegalArgumentException If the given byte buffer does not
     * have a sufficient capacity to provide the data for the given 
     * {@link Accessor}
     */
    AccessorByteData(Accessor accessor,
        ByteBuffer bufferViewByteBuffer, boolean unsigned)
    {
        Objects.requireNonNull(accessor, "The accessor is null");
        Objects.requireNonNull(bufferViewByteBuffer, 
            "The bufferViewByteBuffer is null");
        AccessorDatas.validateByteComponents(accessor);

        this.bufferViewByteBuffer = bufferViewByteBuffer;
        this.unsigned = unsigned;

        // Obtain the basic size information for the accessor
        this.numElements = accessor.getCount();
        this.numComponentsPerElement = 
            Accessors.getNumComponentsForAccessorType(accessor.getType());

        // Obtain the byte offset and stride
        this.byteOffset = accessor.getByteOffset();
        Integer byteStride = accessor.getByteStride();
        if (byteStride == null || byteStride == 0)
        {
            this.byteStridePerElement = 
                numComponentsPerElement * NUM_BYTES_PER_COMPONENT;
        }
        else
        {
            this.byteStridePerElement = byteStride;
        }
        AccessorDatas.validateCapacity(byteOffset, numElements, 
            byteStridePerElement, bufferViewByteBuffer.capacity());
    }
    
    /**
     * Returns whether the data should be interpreted as unsigned
     * 
     * @return Whether the data should be interpreted as unsigned
     */
    public boolean isUnsigned()
    {
        return unsigned;
    }
    
    /**
     * Returns the number of elements in this data (for example, the number
     * of 3D vectors)
     * 
     * @return The number of elements
     */
    public int getNumElements()
    {
        return numElements;
    }
    
    /**
     * Returns the number of components per element (for example, 3 if the
     * elements are 3D vectors)
     * 
     * @return The number of components per element
     */
    public int getNumComponentsPerElement()
    {
        return numComponentsPerElement;
    }
    
    /**
     * Returns the total number of components (that is, the number of elements
     * multiplied with the number of components per element)
     * 
     * @return The total number of components
     */
    public int getTotalNumComponents()
    {
        return numElements * numComponentsPerElement;
    }
    
    /**
     * Returns the value of the specified component of the specified element
     * 
     * @param elementIndex The element index
     * @param componentIndex The component index
     * @return The value
     * @throws IndexOutOfBoundsException If the given indices cause the
     * underlying buffer to be accessed out of bounds
     */
    public byte get(int elementIndex, int componentIndex)
    {
        int byteIndex = byteOffset + 
            elementIndex * byteStridePerElement + 
            componentIndex * NUM_BYTES_PER_COMPONENT;
        return bufferViewByteBuffer.get(byteIndex);
    }
    
    /**
     * Returns the value of the specified component
     * 
     * @param globalComponentIndex The global component index
     * @return The value
     * @throws IndexOutOfBoundsException If the given index causes the
     * underlying buffer to be accessed out of bounds
     */
    public byte get(int globalComponentIndex)
    {
        int elementIndex = globalComponentIndex / numComponentsPerElement;
        int componentIndex = globalComponentIndex % numComponentsPerElement;
        return get(elementIndex, componentIndex);
    }

    
    /**
     * Returns the value of the specified component of the specified element, 
     * taking into account whether the data {@link #isUnsigned()}: If the data 
     * is unsigned, the returned byte value will be converted into an 
     * unsigned integer value.
     * 
     * @param elementIndex The element index
     * @param componentIndex The component index
     * @return The value
     * @throws IndexOutOfBoundsException If the given indices cause the
     * underlying buffer to be accessed out of bounds
     */
    public int getInt(int elementIndex, int componentIndex)
    {
        byte value = get(elementIndex, componentIndex);
        return unsigned ? Byte.toUnsignedInt(value) : value;
    }
    
    /**
     * Returns the value of the specified component, taking into account
     * whether the data {@link #isUnsigned()}: If the data is unsigned,
     * the returned byte value will be converted into an unsigned integer
     * value.
     * 
     * @param globalComponentIndex The global component index
     * @return The value
     * @throws IndexOutOfBoundsException If the given index causes the
     * underlying buffer to be accessed out of bounds
     */
    public int getInt(int globalComponentIndex)
    {
        byte value = get(globalComponentIndex);
        return unsigned ? Byte.toUnsignedInt(value) : value;
    }
    
    /**
     * Returns an array containing the minimum component values of all elements 
     * of this accessor data. This will be an array whose length is the 
     * {@link #getNumComponentsPerElement() number of components per element}.
     * 
     * @return The minimum values
     */
    public byte[] getMin()
    {
        byte result[] = new byte[getNumComponentsPerElement()];
        Arrays.fill(result, Byte.MAX_VALUE);
        for (int e = 0; e < getNumElements(); e++)
        {
            for (int c = 0; c < getNumComponentsPerElement(); c++)
            {
                result[c] = (byte) Math.min(result[c], get(e, c));
            }
        }
        return result;
    }

    /**
     * Returns an array containing the maximum component values of all elements 
     * of this accessor data. This will be an array whose length is the 
     * {@link #getNumComponentsPerElement() number of components per element}.
     * 
     * @return The minimum values
     */
    public byte[] getMax()
    {
        byte result[] = new byte[getNumComponentsPerElement()];
        Arrays.fill(result, Byte.MIN_VALUE);
        for (int e = 0; e < getNumElements(); e++)
        {
            for (int c = 0; c < getNumComponentsPerElement(); c++)
            {
                result[c] = (byte) Math.max(result[c], get(e, c));
            }
        }
        return result;
    }
    
    /**
     * Returns an array containing the minimum component values of all elements 
     * of this accessor data. This will be an array whose length is the 
     * {@link #getNumComponentsPerElement() number of components per element}.
     * These values are computed based on {@link #getInt(int, int)}.
     * 
     * @return The minimum values
     */
    public int[] getMinInt()
    {
        int result[] = new int[getNumComponentsPerElement()];
        Arrays.fill(result, Integer.MAX_VALUE);
        for (int e = 0; e < getNumElements(); e++)
        {
            for (int c = 0; c < getNumComponentsPerElement(); c++)
            {
                result[c] = Math.min(result[c], getInt(e, c));
            }
        }
        return result;
    }

    /**
     * Returns an array containing the maximum component values of all elements 
     * of this accessor data. This will be an array whose length is the 
     * {@link #getNumComponentsPerElement() number of components per element}.
     * These values are computed based on {@link #getInt(int, int)}.
     * 
     * @return The minimum values
     */
    public int[] getMaxInt()
    {
        int result[] = new int[getNumComponentsPerElement()];
        Arrays.fill(result, Integer.MIN_VALUE);
        for (int e = 0; e < getNumElements(); e++)
        {
            for (int c = 0; c < getNumComponentsPerElement(); c++)
            {
                result[c] = Math.max(result[c], getInt(e, c));
            }
        }
        return result;
    }
    
    /**
     * Creates a new, direct byte buffer (with native byte order) that
     * contains the data for the {@link Accessor}, in a compact form,
     * without any offset, and without any additional stride (that is,
     * all elements will be tightly packed).  
     * 
     * @return The byte buffer
     */
    public ByteBuffer createByteBuffer()
    {
        int totalNumComponents = getTotalNumComponents();
        int totalBytes = totalNumComponents * NUM_BYTES_PER_COMPONENT;
        ByteBuffer result = ByteBuffer.allocateDirect(totalBytes)
            .order(ByteOrder.nativeOrder());
        for (int i=0; i<totalNumComponents; i++)
        {
            byte component = get(i);
            result.put(component);
        }
        result.position(0);
        return result;
    }
    
    /**
     * Creates a (potentially large!) string representation of the data
     * 
     * @param locale The locale used for number formatting
     * @param format The number format string
     * @param elementsPerRow The number of elements per row. If this
     * is not greater than 0, then all elements will be in a single row.
     * @return The data string
     */
    public String createString(
        Locale locale, String format, int elementsPerRow)
    {
        StringBuilder sb = new StringBuilder();
        int nc = getNumComponentsPerElement();
        sb.append("[");
        for (int e = 0; e < getNumElements(); e++)
        {
            if (e > 0)
            {
                sb.append(", ");
                if (elementsPerRow > 0 && (e % elementsPerRow) == 0)
                {
                    sb.append("\n ");
                }
            }
            if (nc > 1)
            {
                sb.append("(");
            }
            for (int c = 0; c < nc; c++)
            {
                if (c > 0)
                {
                    sb.append(", ");
                }
                int component = getInt(e, c);
                sb.append(String.format(locale, format, component));
            }
            if (nc > 1)
            {
                sb.append(")");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
}