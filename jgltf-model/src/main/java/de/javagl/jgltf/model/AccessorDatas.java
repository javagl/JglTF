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

/**
 * Methods to create instances of the accessor data utility classes
 * that allow a <i>typed</i> access to the data that is contained in the
 * buffer view that the accessor refers to.<br>
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
     * Returns whether the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_BYTE</code> or
     * <code>GL_UNSIGNED_BYTE</code>. 
     * 
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return Whether the {@link AccessorModel} has <code>byte</code> 
     * components
     */
    public static boolean hasByteComponents(AccessorModel accessorModel)
    {
        return isByteType(accessorModel.getComponentType());
    }

    /**
     * Make sure that the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_BYTE</code> or 
     * <code>GL_UNSIGNED_BYTE</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param accessorModel The {@link AccessorModel}
     * @throws IllegalArgumentException If the given accessorModel has a 
     * {@link AccessorModel#getComponentType() component type} type that is 
     * not <code>GL_BYTE</code> or <code>GL_UNSIGNED_BYTE</code>
     */
    public static void validateByteComponents(AccessorModel accessorModel)
    {
        validateByteType(accessorModel.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorByteData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The {@link AccessorByteData}
     * @throws IllegalArgumentException If the 
     * {@link AccessorModel#getComponentType() component type} of the given
     * accessor is not <code>GL_BYTE</code> or <code>GL_UNSIGNED_BYTE</code>
     */
    public static AccessorByteData createByte(AccessorModel accessorModel)
    {
        BufferViewModel bufferViewModel = accessorModel.getBufferViewModel();
        return createByte(accessorModel, bufferViewModel.getBufferViewData());
    }
    
    /**
     * Creates an {@link AccessorByteData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewByteBuffer The byte buffer of the 
     * {@link BufferViewModel} referenced by the {@link AccessorModel}
     * @return The {@link AccessorByteData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link AccessorModel#getComponentType() component type} of the given
     * accessorModel is not <code>GL_BYTE</code> or 
     * <code>GL_UNSIGNED_BYTE</code>
     */
    public static AccessorByteData createByte(
        AccessorModel accessorModel, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorByteData(accessorModel.getComponentType(), 
            bufferViewByteBuffer,
            accessorModel.getByteOffset(),
            accessorModel.getCount(),
            accessorModel.getElementType().getNumComponents(),
            accessorModel.getByteStride());
    }
    
    
    /**
     * Returns whether the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_SHORT</code> or
     * <code>GL_UNSIGNED_SHORT</code>. 
     * 
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return Whether the {@link AccessorModel} has <code>short</code> 
     * components
     */
    public static boolean hasShortComponents(AccessorModel accessorModel)
    {
        return isShortType(accessorModel.getComponentType());
    }

    /**
     * Make sure that the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_SHORT</code> or 
     * <code>GL_UNSIGNED_SHORT</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param accessorModel The {@link AccessorModel}
     * @throws IllegalArgumentException If the given accessorModel has a 
     * {@link AccessorModel#getComponentType() component type} type that is not 
     * <code>GL_SHORT</code> or <code>GL_UNSIGNED_SHORT</code>
     */
    public static void validateShortComponents(AccessorModel accessorModel)
    {
        validateShortType(accessorModel.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorShortData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The {@link AccessorShortData}
     * @throws IllegalArgumentException If the 
     * {@link AccessorModel#getComponentType() component type} of the given
     * accessorModel is not <code>GL_SHORT</code> or 
     * <code>GL_UNSIGNED_SHORT</code> 
     */
    public static AccessorShortData createShort(AccessorModel accessorModel)
    {
        BufferViewModel bufferViewModel = accessorModel.getBufferViewModel();
        return createShort(accessorModel, bufferViewModel.getBufferViewData());
    }
    
    /**
     * Creates an {@link AccessorShortData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewByteBuffer The byte buffer of the 
     * {@link BufferViewModel} referenced by the {@link AccessorModel}
     * @return The {@link AccessorShortData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link AccessorModel#getComponentType() component type} of the given
     * accessorModel is not <code>GL_SHORT</code> or 
     * <code>GL_UNSIGNED_SHORT</code>
     */
    public static AccessorShortData createShort(
        AccessorModel accessorModel, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorShortData(accessorModel.getComponentType(), 
            bufferViewByteBuffer,
            accessorModel.getByteOffset(),
            accessorModel.getCount(),
            accessorModel.getElementType().getNumComponents(),
            accessorModel.getByteStride());
    }
    

    /**
     * Returns whether the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_INT</code> or
     * <code>GL_UNSIGNED_INT</code>. 
     * 
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return Whether the {@link AccessorModel} has <code>int</code> components
     */
    public static boolean hasIntComponents(AccessorModel accessorModel)
    {
        return isIntType(accessorModel.getComponentType());
    }

    /**
     * Make sure that the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_INT</code> or 
     * <code>GL_UNSIGNED_INT</code>, and throw an 
     * <code>IllegalArgumentException</code> if this is not the case.
     * 
     * @param accessorModel The {@link AccessorModel}
     * @throws IllegalArgumentException If the given accessorModel has a 
     * {@link AccessorModel#getComponentType() component type} type that is not 
     * <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code>
     */
    public static void validateIntComponents(AccessorModel accessorModel)
    {
        validateIntType(accessorModel.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorIntData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The {@link AccessorIntData}
     * @throws IllegalArgumentException If the 
     * {@link AccessorModel#getComponentType() component type} of the given
     * accessorModel is not <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code> 
     */
    public static AccessorIntData createInt(AccessorModel accessorModel)
    {
        BufferViewModel bufferViewModel = accessorModel.getBufferViewModel();
        return createInt(accessorModel, bufferViewModel.getBufferViewData());
    }
    
    /**
     * Creates an {@link AccessorIntData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewByteBuffer The byte buffer of the 
     * {@link BufferViewModel} referenced by the {@link AccessorModel}
     * @return The {@link AccessorIntData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link AccessorModel#getComponentType() component type} of the given
     * accessorModel is not <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code>
     */
    public static AccessorIntData createInt(
        AccessorModel accessorModel, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorIntData(accessorModel.getComponentType(), 
            bufferViewByteBuffer,
            accessorModel.getByteOffset(),
            accessorModel.getCount(),
            accessorModel.getElementType().getNumComponents(),
            accessorModel.getByteStride());
    }
    
    
    /**
     * Returns whether the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_FLOAT</code>
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return Whether the {@link AccessorModel} has <code>float</code> 
     * components
     */
    public static boolean hasFloatComponents(AccessorModel accessorModel)
    {
        return isFloatType(accessorModel.getComponentType());
    }

    /**
     * Make sure that the {@link AccessorModel#getComponentType() component 
     * type} of the given {@link AccessorModel} is <code>GL_FLOAT</code>, 
     * and throw an <code>IllegalArgumentException</code> if this is not the 
     * case.
     * 
     * @param accessorModel The {@link AccessorModel}
     * @throws IllegalArgumentException If the given accessorModel has a 
     * {@link AccessorModel#getComponentType() component type} type that is not 
     * <code>GL_FLOAT</code>
     */
    public static void validateFloatComponents(AccessorModel accessorModel)
    {
        validateFloatType(accessorModel.getComponentType());
    }
    
    /**
     * Creates an {@link AccessorFloatData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The {@link AccessorFloatData}
     * @throws IllegalArgumentException If the 
     * {@link AccessorModel#getComponentType() component type} of the given
     * accessorModel is not <code>GL_FLOAT</code>
     */
    public static AccessorFloatData createFloat(AccessorModel accessorModel)
    {
        BufferViewModel bufferViewModel = accessorModel.getBufferViewModel();
        return createFloat(accessorModel, bufferViewModel.getBufferViewData());
    }
    
    /**
     * Creates an {@link AccessorFloatData} for the given {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewByteBuffer The byte buffer of the 
     * {@link BufferViewModel} referenced by the {@link AccessorModel}
     * @return The {@link AccessorFloatData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     */
    public static AccessorFloatData createFloat(
        AccessorModel accessorModel, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorFloatData(accessorModel.getComponentType(), 
            bufferViewByteBuffer,
            accessorModel.getByteOffset(),
            accessorModel.getCount(),
            accessorModel.getElementType().getNumComponents(),
            accessorModel.getByteStride());
    }

    /**
     * Validate that the given {@link AccessorModel} parameters are valid for
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
                "The accessorModel has an offset of " + byteOffset + " and " + 
                numElements + " elements with a byte stride of " + 
                byteStridePerElement + ", requiring " + expectedCapacity + 
                " bytes, but the buffer view has only " + 
                bufferCapacity + " bytes");
        }
    }
    
    
    /**
     * Compute the the minimum component values of the given 
     * {@link AccessorData}
     * 
     * @param accessorData The {@link AccessorData}
     * @return The minimum values
     * @throws IllegalArgumentException If the given model has an unknown type
     */
    public static Number[] computeMin(AccessorData accessorData)
    {
        if (accessorData instanceof AccessorByteData) 
        {
            AccessorByteData accessorByteData = 
                (AccessorByteData) accessorData;
            return NumberArrays.asNumbers(
                accessorByteData.computeMinInt());
        }
        if (accessorData instanceof AccessorShortData) 
        {
            AccessorShortData accessorShortData = 
                (AccessorShortData) accessorData;
            return NumberArrays.asNumbers(
                accessorShortData.computeMinInt());
        }
        if (accessorData instanceof AccessorIntData) 
        {
            AccessorIntData accessorIntData = 
                (AccessorIntData) accessorData;
            return NumberArrays.asNumbers(
                accessorIntData.computeMinLong());
        }
        if (accessorData instanceof AccessorFloatData) 
        {
            AccessorFloatData accessorFloatData = 
                (AccessorFloatData) accessorData;
            return NumberArrays.asNumbers(
                accessorFloatData.computeMin());
        }
        throw new IllegalArgumentException(
            "Invalid data type: " + accessorData);
    }
    
    /**
     * Compute the the maximum component values of the given 
     * {@link AccessorData}
     * 
     * @param accessorData The {@link AccessorData}
     * @return The maximum values
     * @throws IllegalArgumentException If the given model has an unknown type
     */
    public static Number[] computeMax(AccessorData accessorData)
    {
        if (accessorData instanceof AccessorByteData) 
        {
            AccessorByteData accessorByteData = 
                (AccessorByteData) accessorData;
            return NumberArrays.asNumbers(
                accessorByteData.computeMaxInt());
        }
        if (accessorData instanceof AccessorShortData) 
        {
            AccessorShortData accessorShortData = 
                (AccessorShortData) accessorData;
            return NumberArrays.asNumbers(
                accessorShortData.computeMaxInt());
        }
        if (accessorData instanceof AccessorIntData) 
        {
            AccessorIntData accessorIntData = 
                (AccessorIntData) accessorData;
            return NumberArrays.asNumbers(
                accessorIntData.computeMaxLong());
        }
        if (accessorData instanceof AccessorFloatData) 
        {
            AccessorFloatData accessorFloatData = 
                (AccessorFloatData) accessorData;
            return NumberArrays.asNumbers(
                accessorFloatData.computeMax());
        }
        throw new IllegalArgumentException(
            "Invalid data type: " + accessorData);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorDatas()
    {
        // Private constructor to prevent instantiation
    }
    

}
