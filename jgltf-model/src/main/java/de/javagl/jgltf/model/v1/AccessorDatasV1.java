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
package de.javagl.jgltf.model.v1;

import java.nio.ByteBuffer;

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.GltfConstants;

/**
 * Convenience functions to create {@link AccessorData} instances directly
 * from a glTF 1.0 {@link Accessor} and a byte buffer
 */
public class AccessorDatasV1
{
    /**
     * Creates an {@link AccessorData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The byte buffer of the buffer view
     * @return The {@link AccessorData}
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not a valid type
     */
    public static AccessorData create(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        if (AccessorDatas.isByteType(accessor.getComponentType()))
        {
            return createByte(accessor, bufferViewByteBuffer);
        }
        if (AccessorDatas.isShortType(accessor.getComponentType()))
        {
            return createShort(accessor, bufferViewByteBuffer);
        }
        if (AccessorDatas.isIntType(accessor.getComponentType()))
        {
            return createInt(accessor, bufferViewByteBuffer);
        }
        if (AccessorDatas.isFloatType(accessor.getComponentType()))
        {
            return createFloat(accessor, bufferViewByteBuffer);
        }
        throw new IllegalArgumentException("Invalid accessor component type: "
            + GltfConstants.stringFor(accessor.getComponentType()));
    }
    
    
    /**
     * Creates an {@link AccessorByteData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The byte buffer of the buffer view
     * @return The {@link AccessorByteData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_BYTE</code> or 
     * <code>GL_UNSIGNED_BYTE</code>
     */
    public static AccessorByteData createByte(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorByteData(accessor.getComponentType(), 
            bufferViewByteBuffer,
            accessor.getByteOffset(),
            accessor.getCount(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getByteStride());
    }
    
    
    /**
     * Creates an {@link AccessorShortData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The byte buffer of the buffer view
     * @return The {@link AccessorShortData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_SHORT</code> or 
     * <code>GL_UNSIGNED_SHORT</code>
     */
    public static AccessorShortData createShort(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorShortData(accessor.getComponentType(), 
            bufferViewByteBuffer,
            accessor.getByteOffset(),
            accessor.getCount(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getByteStride());
    }
    
    
    /**
     * Creates an {@link AccessorIntData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The byte buffer of the buffer view
     * @return The {@link AccessorIntData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the given
     * accessor is not <code>GL_INT</code> or <code>GL_UNSIGNED_INT</code>
     */
    public static AccessorIntData createInt(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorIntData(accessor.getComponentType(), 
            bufferViewByteBuffer,
            accessor.getByteOffset(),
            accessor.getCount(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getByteStride());
    }
    
    /**
     * Creates an {@link AccessorFloatData} for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @param bufferViewByteBuffer The byte buffer of the buffer view
     * @return The {@link AccessorFloatData}
     * @throws NullPointerException If any argument is <code>null</code>
     * @throws IllegalArgumentException If the 
     */
    public static AccessorFloatData createFloat(
        Accessor accessor, ByteBuffer bufferViewByteBuffer)
    {
        return new AccessorFloatData(
            accessor.getComponentType(), 
            bufferViewByteBuffer,
            accessor.getByteOffset(),
            accessor.getCount(),
            Accessors.getNumComponentsForAccessorType(accessor.getType()),
            accessor.getByteStride());
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AccessorDatasV1() 
    {
        // Private constructor to prevent instantiation
    }
}
