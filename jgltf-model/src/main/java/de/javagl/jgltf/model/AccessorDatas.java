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

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.BufferView;

/**
 * Methods to create instances of the {@link Accessor} data utility classes
 * that allow a <i>typed</i> access to the data that is provided by the
 * {@link GltfData#getBufferViewData(String) bufferViews} that
 * the {@link Accessor} refers to.<br>
 * <br>
 * Unless otherwise noted, none of the arguments to these methods may 
 * be <code>null</code>.
 */
public class AccessorDatas
{
    /**
     * Returns whether the given constant is <code>GL_BYTE</code> or
     * <code>GL_UNSIGNED_BYTE</code>. 
     * 
     * @param type The type constant
     * @return Whether the type is a <code>byte</code> type
     */
    public static boolean isByteType(int type)
    {
        return 
            type == GltfConstants.GL_BYTE ||
            type == GltfConstants.GL_UNSIGNED_BYTE;
    }
    
    /**
     * Returns whether the given constant is <code>GL_SHORT</code> or
     * <code>GL_UNSIGNED_SHORT</code>. 
     * 
     * @param type The type constant
     * @return Whether the type is a <code>short</code> type
     */
    public static boolean isShortType(int type)
    {
        return 
            type == GltfConstants.GL_SHORT ||
            type == GltfConstants.GL_UNSIGNED_SHORT;
    }

    /**
     * Returns whether the given constant is <code>GL_INT</code> or
     * <code>GL_UNSIGNED_INT</code>. 
     * 
     * @param type The type constant
     * @return Whether the type is an <code>int</code> type
     */
    public static boolean isIntType(int type)
    {
        return 
            type == GltfConstants.GL_INT ||
            type == GltfConstants.GL_UNSIGNED_INT;
    }

    /**
     * Returns whether the given constant is <code>GL_FLOAT</code>.
     * 
     * @param type The type constant
     * @return Whether the type is a <code>float</code> type
     */
    public static boolean isFloatType(int type)
    {
        return type == GltfConstants.GL_FLOAT;
    }

    /**
     * Returns whether the given constant is <code>GL_UNSIGNED_BYTE</code>,
     * <code>GL_UNSIGNED_SHORT</code> or <code>GL_UNSIGNED_INT</code>.
     * 
     * @param type The type constant
     * @return Whether the type is an unsigned type
     */
    public static boolean isUnsignedType(int type)
    {
        return 
            type == GltfConstants.GL_UNSIGNED_BYTE ||
            type == GltfConstants.GL_UNSIGNED_SHORT ||
            type == GltfConstants.GL_UNSIGNED_INT;
    }
    
    
    /**
     * Make sure that the given type is <code>GL_BYTE</code> or 
     * <code>GL_UNSIGNED_BYTE</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param type The type constant
     * @throws IllegalArgumentException If the given type is not 
     * <code>GL_BYTE</code> or <code>GL_UNSIGNED_BYTE</code>
     */
    public static void validateByteType(int type)
    {
        if (!isByteType(type))
        {
            throw new IllegalArgumentException(
                "The type is not GL_BYTE or GL_UNSIGNED_BYTE, but " + 
                GltfConstants.stringFor(type));
        }
    }

    /**
     * Make sure that the given type is <code>GL_SHORT</code> or 
     * <code>GL_UNSIGNED_SHORT</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param type The type constant
     * @throws IllegalArgumentException If the given type is not 
     * <code>GL_SHORT</code> or <code>GL_UNSIGNED_BYTE</code>
     */
    public static void validateShortType(int type)
    {
        if (!isShortType(type))
        {
            throw new IllegalArgumentException(
                "The type is not GL_SHORT or GL_UNSIGNED_SHORT, but " + 
                GltfConstants.stringFor(type));
        }
    }
    
    /**
     * Make sure that the given type is <code>GL_INT</code> or 
     * <code>GL_UNSIGNED_INT</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param type The type constant
     * @throws IllegalArgumentException If the given type is not 
     * <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code>
     */
    public static void validateIntType(int type)
    {
        if (!isIntType(type))
        {
            throw new IllegalArgumentException(
                "The type is not GL_INT or GL_UNSIGNED_INT, but " + 
                GltfConstants.stringFor(type));
        }
    }

    /**
     * Make sure that the given type is <code>GL_FLOAT</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param type The type constant
     * @throws IllegalArgumentException If the given type is not 
     * <code>GL_FLOAT</code>
     */
    public static void validateFloatType(int type)
    {
        if (!isFloatType(type))
        {
            throw new IllegalArgumentException(
                "The type is not GL_FLOAT, but " + 
                GltfConstants.stringFor(type));
        }
    }
    
    
    /**
     * Returns whether the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_BYTE</code> or
     * <code>GL_UNSIGNED_BYTE</code>. 
     * 
     * 
     * @param accessor The {@link Accessor}
     * @return Whether the {@link Accessor} has <code>byte</code> components
     */
    public static boolean hasByteComponents(Accessor accessor)
    {
        return isByteType(accessor.getComponentType());
    }

    /**
     * Make sure that the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_BYTE</code> or 
     * <code>GL_UNSIGNED_BYTE</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param accessor The {@link Accessor}
     * @throws IllegalArgumentException If the given accessor has a 
     * {@link Accessor#getComponentType() component type} type that is not 
     * <code>GL_BYTE</code> or <code>GL_UNSIGNED_BYTE</code>
     */
    public static void validateByteComponents(Accessor accessor)
    {
        validateByteType(accessor.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorByteData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param gltfData The {@link GltfData} that contains the byte buffer of
     * the {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorByteData}
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_BYTE</code> or <code>GL_UNSIGNED_BYTE</code>
     * @throws GltfException If the {@link Accessor#getBufferView()} refers
     * to a {@link BufferView} that does not exist 
     */
    public static AccessorByteData createByte(
        Accessor accessor, GltfData gltfData)
    {
        String bufferViewId = accessor.getBufferView();
        ByteBuffer bufferViewData = gltfData.getBufferViewData(bufferViewId);
        if (bufferViewData == null)
        {
            throw new GltfException("The accessor buffer view with ID " + 
                bufferViewId + " does not exist");
        }
        return createByte(accessor, bufferViewData);
    }
    
    /**
     * Creates an {@link AccessorByteData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The  byte buffer of the 
     * {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorByteData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_BYTE</code> or <code>GL_UNSIGNED_BYTE</code>
     */
    public static AccessorByteData createByte(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorByteData(bufferViewByteBuffer, 
            accessor.getByteOffset(),
            accessor.getByteStride(),
            accessor.getComponentType(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getCount());
    }
    
    
    
    /**
     * Returns whether the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_SHORT</code> or
     * <code>GL_UNSIGNED_SHORT</code>. 
     * 
     * 
     * @param accessor The {@link Accessor}
     * @return Whether the {@link Accessor} has <code>short</code> components
     */
    public static boolean hasShortComponents(Accessor accessor)
    {
        return isShortType(accessor.getComponentType());
    }

    /**
     * Make sure that the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_SHORT</code> or 
     * <code>GL_UNSIGNED_SHORT</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param accessor The {@link Accessor}
     * @throws IllegalArgumentException If the given accessor has a 
     * {@link Accessor#getComponentType() component type} type that is not 
     * <code>GL_SHORT</code> or <code>GL_UNSIGNED_SHORT</code>
     */
    public static void validateShortComponents(Accessor accessor)
    {
        validateShortType(accessor.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorShortData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param gltfData The {@link GltfData} that contains the byte buffer of
     * the {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorShortData}
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_SHORT</code> or <code>GL_UNSIGNED_SHORT</code> 
     * @throws GltfException If the {@link Accessor#getBufferView()} refers
     * to a {@link BufferView} that does not exist 
     */
    public static AccessorShortData createShort(
        Accessor accessor, GltfData gltfData)
    {
        String bufferViewId = accessor.getBufferView();
        ByteBuffer bufferViewData = gltfData.getBufferViewData(bufferViewId);
        if (bufferViewData == null)
        {
            throw new GltfException("The accessor buffer view with ID " + 
                bufferViewId + " does not exist");
        }
        return createShort(accessor, bufferViewData);
    }
    
    /**
     * Creates an {@link AccessorShortData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The  byte buffer of the 
     * {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorShortData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_SHORT</code> or <code>GL_UNSIGNED_SHORT</code>
     */
    public static AccessorShortData createShort(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorShortData(bufferViewByteBuffer, 
            accessor.getByteOffset(),
            accessor.getByteStride(),
            accessor.getComponentType(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getCount());
    }
    

    /**
     * Returns whether the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_INT</code> or
     * <code>GL_UNSIGNED_INT</code>. 
     * 
     * 
     * @param accessor The {@link Accessor}
     * @return Whether the {@link Accessor} has <code>int</code> components
     */
    public static boolean hasIntComponents(Accessor accessor)
    {
        return isIntType(accessor.getComponentType());
    }

    /**
     * Make sure that the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_INT</code> or 
     * <code>GL_UNSIGNED_INT</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param accessor The {@link Accessor}
     * @throws IllegalArgumentException If the given accessor has a 
     * {@link Accessor#getComponentType() component type} type that is not 
     * <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code>
     */
    public static void validateIntComponents(Accessor accessor)
    {
        validateIntType(accessor.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorIntData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param gltfData The {@link GltfData} that contains the byte buffer of
     * the {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorIntData}
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code> 
     * @throws GltfException If the {@link Accessor#getBufferView()} refers
     * to a {@link BufferView} that does not exist 
     */
    public static AccessorIntData createInt(
        Accessor accessor, GltfData gltfData)
    {
        String bufferViewId = accessor.getBufferView();
        ByteBuffer bufferViewData = gltfData.getBufferViewData(bufferViewId);
        if (bufferViewData == null)
        {
            throw new GltfException("The accessor buffer view with ID " + 
                bufferViewId + " does not exist");
        }
        return createInt(accessor, bufferViewData);
    }
    
    /**
     * Creates an {@link AccessorIntData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The  byte buffer of the 
     * {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorIntData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code>
     */
    public static AccessorIntData createInt(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorIntData(bufferViewByteBuffer, 
            accessor.getByteOffset(),
            accessor.getByteStride(),
            accessor.getComponentType(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getCount());
    }
    
    
    /**
     * Returns whether the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_FLOAT</code>
     * 
     * @param accessor The {@link Accessor}
     * @return Whether the {@link Accessor} has <code>float</code> components
     */
    public static boolean hasFloatComponents(Accessor accessor)
    {
        return isFloatType(accessor.getComponentType());
    }

    /**
     * Make sure that the {@link Accessor#getComponentType() component type}
     * of the given {@link Accessor} is <code>GL_FLOAT</code>, and throw an
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param accessor The {@link Accessor}
     * @throws IllegalArgumentException If the given accessor has a 
     * {@link Accessor#getComponentType() component type} type that is not 
     * <code>GL_FLOAT</code>
     */
    public static void validateFloatComponents(Accessor accessor)
    {
        validateFloatType(accessor.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorFloatData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param gltfData The {@link GltfData} that contains the byte buffer of
     * the {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorFloatData}
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_FLOAT</code>
     * @throws GltfException If the {@link Accessor#getBufferView()} refers
     * to a {@link BufferView} that does not exist 
     */
    public static AccessorFloatData createFloat(
        Accessor accessor, GltfData gltfData)
    {
        String bufferViewId = accessor.getBufferView();
        ByteBuffer bufferViewData = gltfData.getBufferViewData(bufferViewId);
        if (bufferViewData == null)
        {
            throw new GltfException("The accessor buffer view with ID " + 
                bufferViewId + " does not exist");
        }
        return createFloat(accessor, bufferViewData);
    }
    
    /**
     * Creates an {@link AccessorFloatData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The  byte buffer of the 
     * {@link BufferView} referenced by the {@link Accessor}
     * @return The {@link AccessorFloatData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_FLOAT</code>
     */
    public static AccessorFloatData createFloat(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorFloatData(bufferViewByteBuffer, 
            accessor.getByteOffset(),
            accessor.getByteStride(),
            accessor.getComponentType(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getCount());
    }

    /**
     * Validate that the given {@link Accessor} parameters are valid for
     * accessing a buffer with the given capacity
     * 
     * @param byteOffset The byte offset 
     * @param numElements The number of elements
     * @param byteStridePerElement The byte stride
     * @param bufferCapacity The buffer capacity
     * @throws IllegalArgumentException If the given byte buffer does not
     * have a sufficient capacity
     */
    public static void validateCapacity(int byteOffset, int numElements,
        int byteStridePerElement, int bufferCapacity)
    {
        int expectedCapacity = numElements * byteStridePerElement;
        if (expectedCapacity > bufferCapacity)
        {
            throw new IllegalArgumentException(
                "The accessor has an offset of " + byteOffset + " and " + 
                numElements + " elements with a byte stride of " + 
                byteStridePerElement + ", requiring " + expectedCapacity + 
                " bytes, but the buffer view has only " + 
                bufferCapacity + " bytes");
        }
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorDatas()
    {
        // Private constructor to prevent instantiation
    }

}
