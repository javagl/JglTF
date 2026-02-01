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
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import de.javagl.jgltf.model.io.Buffers;

/**
 * Utility methods for quantization.
 */
public class Quantization
{
    /**
     * Returns the data from the given accessor model as a float buffer, tightly
     * packed, applying dequantization as necessary.
     * 
     * If the component type of the given accessor model is
     * <code>GL_FLOAT</code>, then the data will be returned directly.
     * 
     * If the component type is <code>GL_BYTE</code>,
     * <code>GL_UNSIGNED_BYTE</code>, <code>GL_SHORT</code>, or
     * <code>GL_UNSIGNED_SHORT</code>, then the data will be dequantized into
     * float values.
     * 
     * Note: This does not check whether the accessor model is indeed normalized
     * as of {@link AccessorModel#isNormalized()}.
     *
     * @param accessorModel The accessor model
     * @return The buffer
     * @throws IllegalArgumentException If the component type of the given
     *         accessor model is neither float, nor signed/unsigned byte/short.
     */
    static FloatBuffer readAsFloatBuffer(AccessorModel accessorModel)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        int componentType = accessorModel.getComponentType();
        if (componentType == GltfConstants.GL_FLOAT)
        {
            ByteBuffer inputByteBuffer = accessorData.createByteBuffer();
            return inputByteBuffer.asFloatBuffer();
        }
        if (componentType == GltfConstants.GL_BYTE)
        {
            ByteBuffer inputByteBuffer = accessorData.createByteBuffer();
            return dequantizeByteBuffer(inputByteBuffer);
        }
        if (componentType == GltfConstants.GL_UNSIGNED_BYTE)
        {
            ByteBuffer inputByteBuffer = accessorData.createByteBuffer();
            return dequantizeUnsignedByteBuffer(inputByteBuffer);
        }
        if (componentType == GltfConstants.GL_SHORT)
        {
            ByteBuffer inputByteBuffer = accessorData.createByteBuffer();
            ShortBuffer shortBuffer = inputByteBuffer.asShortBuffer();
            return dequantizeShortBuffer(shortBuffer);
        }
        if (componentType == GltfConstants.GL_UNSIGNED_SHORT)
        {
            ByteBuffer inputByteBuffer = accessorData.createByteBuffer();
            ShortBuffer shortBuffer = inputByteBuffer.asShortBuffer();
            return dequantizeUnsignedShortBuffer(shortBuffer);
        }
        throw new IllegalArgumentException(
            "Component type " + GltfConstants.stringFor(componentType)
                + " cannot be converted to float");
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as a signed byte.
     *
     * @param byteBuffer The input buffer
     * @return The result
     */
    public static FloatBuffer dequantizeByteBuffer(ByteBuffer byteBuffer)
    {
        FloatBuffer floatBuffer = FloatBuffer.allocate(byteBuffer.capacity());
        dequantizeByteBuffer(byteBuffer, floatBuffer);
        return floatBuffer;
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as a signed byte.
     *
     * @param byteBuffer The input buffer
     * @param floatBuffer The output buffer
     */
    private static void dequantizeByteBuffer(ByteBuffer byteBuffer,
        FloatBuffer floatBuffer)
    {
        for (int i = 0; i < byteBuffer.capacity(); i++)
        {
            byte c = byteBuffer.get(i);
            float f = dequantizeByte(c);
            floatBuffer.put(i, f);
        }
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as an unsigned byte.
     *
     * @param byteBuffer The input buffer
     * @return The result
     */
    public static FloatBuffer
        dequantizeUnsignedByteBuffer(ByteBuffer byteBuffer)
    {
        FloatBuffer floatBuffer = FloatBuffer.allocate(byteBuffer.capacity());
        dequantizeUnsignedByteBuffer(byteBuffer, floatBuffer);
        return floatBuffer;
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as an unsigned byte.
     *
     * @param byteBuffer The input buffer
     * @param floatBuffer The output buffer
     */
    private static void dequantizeUnsignedByteBuffer(ByteBuffer byteBuffer,
        FloatBuffer floatBuffer)
    {
        for (int i = 0; i < byteBuffer.capacity(); i++)
        {
            byte c = byteBuffer.get(i);
            float f = dequantizeUnsignedByte(c);
            floatBuffer.put(i, f);
        }
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as a signed short.
     *
     * @param shortBuffer The input buffer
     * @return The result
     */
    public static FloatBuffer dequantizeShortBuffer(ShortBuffer shortBuffer)
    {
        FloatBuffer floatBuffer = FloatBuffer.allocate(shortBuffer.capacity());
        dequantizeShortBuffer(shortBuffer, floatBuffer);
        return floatBuffer;
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as a signed short.
     *
     * @param shortBuffer The input buffer
     * @param floatBuffer The output buffer
     */
    private static void dequantizeShortBuffer(ShortBuffer shortBuffer,
        FloatBuffer floatBuffer)
    {
        for (int i = 0; i < shortBuffer.capacity(); i++)
        {
            short c = shortBuffer.get(i);
            float f = dequantizeShort(c);
            floatBuffer.put(i, f);
        }
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as an unsigned short.
     *
     * @param shortBuffer The input buffer
     * @return The result
     */
    public static FloatBuffer
        dequantizeUnsignedShortBuffer(ShortBuffer shortBuffer)
    {
        FloatBuffer floatBuffer = FloatBuffer.allocate(shortBuffer.capacity());
        dequantizeUnsignedShortBuffer(shortBuffer, floatBuffer);
        return floatBuffer;
    }

    /**
     * Dequantize the given buffer into a float buffer, treating each element of
     * the input as an unsigned short.
     *
     * @param shortBuffer The input buffer
     * @param floatBuffer The output buffer
     */
    private static void dequantizeUnsignedShortBuffer(ShortBuffer shortBuffer,
        FloatBuffer floatBuffer)
    {
        for (int i = 0; i < shortBuffer.capacity(); i++)
        {
            short c = shortBuffer.get(i);
            float f = dequantizeUnsignedShort(c);
            floatBuffer.put(i, f);
        }
    }

    /**
     * Dequantize the given signed byte into a floating point value
     *
     * @param c The input
     * @return The result
     */
    private static float dequantizeByte(byte c)
    {
        float f = Math.max(c / 127.0f, -1.0f);
        return f;
    }

    /**
     * Dequantize the given unsigned byte into a floating point value
     *
     * @param c The input
     * @return The result
     */
    private static float dequantizeUnsignedByte(byte c)
    {
        int i = Byte.toUnsignedInt(c);
        float f = i / 255.0f;
        return f;
    }

    /**
     * Dequantize the given signed short into a floating point value
     *
     * @param c The input
     * @return The result
     */
    private static float dequantizeShort(short c)
    {
        float f = Math.max(c / 32767.0f, -1.0f);
        return f;
    }

    /**
     *
     * Dequantize the given unsigned byte into a floating point value
     *
     * @param c The input
     * @return The result
     */
    private static float dequantizeUnsignedShort(short c)
    {
        int i = Short.toUnsignedInt(c);
        float f = i / 65535.0f;
        return f;
    }

    /**
     * Quantize the given float buffer into a buffer with signed bytes
     *
     * @param floatBuffer The input buffer
     * @return The result
     */
    public static ByteBuffer quantizeToByteBuffer(FloatBuffer floatBuffer)
    {
        ByteBuffer byteBuffer = Buffers.create(floatBuffer.capacity());
        quantizeToByteBuffer(floatBuffer, byteBuffer);
        return byteBuffer;
    }

    /**
     * Quantize the given float buffer into a buffer with signed bytes
     *
     * @param floatBuffer The input buffer
     * @param byteBuffer The output buffer
     */
    private static void quantizeToByteBuffer(FloatBuffer floatBuffer,
        ByteBuffer byteBuffer)
    {
        for (int i = 0; i < floatBuffer.capacity(); i++)
        {
            float f = floatBuffer.get(i);
            byte c = quantizeByte(f);
            byteBuffer.put(i, c);
        }
    }

    /**
     * Quantize the given float buffer into a buffer with unsigned bytes
     *
     * @param floatBuffer The input buffer
     * @return The result
     */
    public static ByteBuffer
        quantizeToUnsignedByteBuffer(FloatBuffer floatBuffer)
    {
        ByteBuffer byteBuffer = Buffers.create(floatBuffer.capacity());
        quantizeToUnsignedByteBuffer(floatBuffer, byteBuffer);
        return byteBuffer;
    }

    /**
     * Quantize the given float buffer into a buffer with unsigned bytes
     *
     * @param floatBuffer The input buffer
     * @param byteBuffer The output buffer
     */
    private static void quantizeToUnsignedByteBuffer(FloatBuffer floatBuffer,
        ByteBuffer byteBuffer)
    {
        for (int i = 0; i < floatBuffer.capacity(); i++)
        {
            float f = floatBuffer.get(i);
            byte c = quantizeUnsignedByte(f);
            byteBuffer.put(i, c);
        }
    }

    /**
     * Quantize the given float buffer into a buffer with signed shorts
     *
     * @param floatBuffer The input buffer
     * @return The result
     */
    public static ShortBuffer quantizeToShortBuffer(FloatBuffer floatBuffer)
    {
        ShortBuffer shortBuffer = ShortBuffer.allocate(floatBuffer.capacity());
        quantizeToShortBuffer(floatBuffer, shortBuffer);
        return shortBuffer;
    }

    /**
     * Quantize the given float buffer into a buffer with signed shorts
     *
     * @param floatBuffer The input buffer
     * @param shortBuffer The output buffer
     */
    private static void quantizeToShortBuffer(FloatBuffer floatBuffer,
        ShortBuffer shortBuffer)
    {
        for (int i = 0; i < floatBuffer.capacity(); i++)
        {
            float f = floatBuffer.get(i);
            short c = quantizeShort(f);
            shortBuffer.put(i, c);
        }
    }

    /**
     * Quantize the given float buffer into a buffer with unsigned shorts
     *
     * @param floatBuffer The input buffer
     * @return The result
     */
    public static ShortBuffer
        quantizeToUnsignedShortBuffer(FloatBuffer floatBuffer)
    {
        ShortBuffer shortBuffer = ShortBuffer.allocate(floatBuffer.capacity());
        quantizeToUnsignedShortBuffer(floatBuffer, shortBuffer);
        return shortBuffer;
    }

    /**
     * Quantize the given float buffer into a buffer with unsigned shorts
     *
     * @param floatBuffer The input buffer
     * @param shortBuffer The output buffer
     */
    private static void quantizeToUnsignedShortBuffer(FloatBuffer floatBuffer,
        ShortBuffer shortBuffer)
    {
        for (int i = 0; i < floatBuffer.capacity(); i++)
        {
            float f = floatBuffer.get(i);
            short c = quantizeUnsignedShort(f);
            shortBuffer.put(i, c);
        }
    }

    /**
     * Quantize the given floating point value into a signed byte
     *
     * @param f The input
     * @return The result
     */
    private static byte quantizeByte(float f)
    {
        byte c = (byte) Math.round(f * 127.0f);
        return c;
    }

    /**
     * Quantize the given floating point value into an unsigned byte
     *
     * @param f The input
     * @return The result
     */
    private static byte quantizeUnsignedByte(float f)
    {
        byte c = (byte) Math.round(f * 255.0f);
        return c;
    }

    /**
     * Quantize the given floating point value into a signed short
     *
     * @param f The input
     * @return The result
     */
    private static short quantizeShort(float f)
    {
        short c = (short) Math.round(f * 32767.0f);
        return c;
    }

    /**
     * Quantize the given floating point value into an unsigned short
     *
     * @param f The input
     * @return The result
     */
    private static short quantizeUnsignedShort(float f)
    {
        short c = (short) Math.round(f * 65535.0f);
        return c;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Quantization()
    {
        // Private constructor to prevent instantiation
    }

}
