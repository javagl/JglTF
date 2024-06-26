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
package de.javagl.jgltf.model.creation;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Methods to create to {@link AccessorModel} instances.<br>
 * <br>
 * The instances created by these methods do have an associated 
 * {@link AccessorModel#getAccessorData() accessor data} that
 * simply represents the data that was given at construction time.
 * The instances do <b>not</b> have an associated {@link BufferViewModel}
 * instance.<br>
 * <br>
 * The instances of accessor models that are created by this class
 * are supposed to be used during the construction of a glTF model,
 * using the {@link GltfModelBuilder}: They hold the data of the
 * model elements (for example, attributes in a {@link MeshPrimitiveModel}).<br>
 * <br>
 * The {@link GltfModelBuilder} will use the accessor data internally,
 * in order to build the associated buffers and buffer views.<br>
 * <br>
 * This class offers some convenience methods for commonly used accessor
 * model types - for example, 3D floating point vectors (positions and
 * normals), or unsigned int scalar values (for indices).<br>
 * <br>
 * Arbitrary (more specific) accessor model types can be constructed with
 * the {@link #create(int, String, boolean, ByteBuffer)} method, which
 * receives all information that is required for the accessor model.
 * This method expects the data to be given as a byte buffer. When the
 * input data is a <code>FloatBuffer</code>, then the byte buffer can be
 * created with the utility methods in the {@link Buffers} class - for
 * example, {@link Buffers#createByteBufferFrom(FloatBuffer)}.
 */
public class AccessorModels 
{
    /**
     * Creates a copy of the given accessor model, only based on its
     * {@link AccessorData}.<br>
     * <br>
     * This will return a model that has the same data type and data
     * as the given model, but without an associated buffer view.<br>
     * <br>
     * This may be used to create a copy of an accessor model from
     * one {@link GltfModel}, and pass it to a {@link GltfModelBuilder}
     * for the construction of a new {@link GltfModel}.
     *
     * @param input The input
     * @return The copy
     */
    public static DefaultAccessorModel copy(AccessorModel input)
    {
        AccessorData inputAccessorData = input.getAccessorData();
        ByteBuffer byteBuffer = inputAccessorData.createByteBuffer();
        DefaultAccessorModel copy = AccessorModels.create(
            input.getComponentType(),
            input.getElementType().toString(),
            input.isNormalized(),
            byteBuffer);
        return copy;
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_INT</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedIntScalar(IntBuffer data)
    {
        return create(
            GltfConstants.GL_UNSIGNED_INT, "SCALAR", false,
            Buffers.createByteBufferFrom(data));
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_INT</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createIntScalar(IntBuffer data)
    {
        return create(
            GltfConstants.GL_INT, "SCALAR", false,
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_BYTE</code> and the type <code>"SCALAR"</code>.<br>
     * <br>
     * The elements of the given buffer will be cast to <code>byte</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedByteScalar(IntBuffer data)
    {
        return create(  
            GltfConstants.GL_UNSIGNED_BYTE, "SCALAR", false,
            Buffers.castToByteBuffer(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_BYTE</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedByteScalar(
        ByteBuffer data)
    {
        return create(  
            GltfConstants.GL_UNSIGNED_BYTE, "SCALAR", false, data);
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_BYTE</code> and the type <code>"SCALAR"</code>.<br>
     * <br>
     * The elements of the given buffer will be cast to <code>byte</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createByteScalar(IntBuffer data)
    {
        return create(  
            GltfConstants.GL_BYTE, "SCALAR", false,
            Buffers.castToByteBuffer(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_BYTE</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createByteScalar(
        ByteBuffer data)
    {
        return create(  
            GltfConstants.GL_BYTE, "SCALAR", false, data);
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_SHORT</code> and the type <code>"SCALAR"</code>.<br>
     * <br>
     * The elements of the given buffer will be cast to <code>short</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedShortScalar(IntBuffer data)
    {
        return create(  
            GltfConstants.GL_UNSIGNED_SHORT, "SCALAR", false,
            Buffers.castToShortByteBuffer(data));
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_SHORT</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedShortScalar(
        ShortBuffer data)
    {
        return create(  
            GltfConstants.GL_UNSIGNED_SHORT, "SCALAR", false,
            Buffers.createByteBufferFrom(data));
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_SHORT</code> and the type <code>"SCALAR"</code>.<br>
     * <br>
     * The elements of the given buffer will be cast to <code>short</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createShortScalar(IntBuffer data)
    {
        return create(  
            GltfConstants.GL_SHORT, "SCALAR", false,
            Buffers.castToShortByteBuffer(data));
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_SHORT</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createShortScalar(
        ShortBuffer data)
    {
        return create(  
            GltfConstants.GL_SHORT, "SCALAR", false,
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_FLOAT</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createFloatScalar(FloatBuffer data)
    {
        return create(
            GltfConstants.GL_FLOAT, "SCALAR", false,
            Buffers.createByteBufferFrom(data));
    }
    
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_FLOAT</code> and the type <code>"VEC2"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createFloat2D(FloatBuffer data)
    {
        return create(
            GltfConstants.GL_FLOAT, "VEC2", false,
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_FLOAT</code> and the type <code>"VEC3"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createFloat3D(FloatBuffer data)
    {
        return create(
            GltfConstants.GL_FLOAT, "VEC3", false,
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_FLOAT</code> and the type <code>"VEC4"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createFloat4D(FloatBuffer data)
    {
        return create(
            GltfConstants.GL_FLOAT, "VEC4", false,
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Create an {@link AccessorModel} from the given data.
     * 
     * The byte buffer containing the data can be created from buffers
     * with the respective component type by using the utility methods
     * in the {@link Buffers} class. For example, when the input data
     * is a <code>FloatBuffer</code>, then the byte buffer can be 
     * created with <code>Buffers.createByteBufferFrom(floatBuffer)</code>.
     * 
     * @param componentType The component type, as a GL constant
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @param normalized Whether the data is normalized
     * @param byteBuffer The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * is not divisible by the element size that is implied by the given
     * type and component type
     */
    public static DefaultAccessorModel create(
        int componentType, String type, boolean normalized, 
        ByteBuffer byteBuffer)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numBytesPerElement = elementType.getByteStride(componentType);
        if (byteBuffer.capacity() % numBytesPerElement != 0)
        {
            throw new IllegalArgumentException(
                "Invalid data for type " + type + " accessor with "
                + Accessors.getDataTypeForAccessorComponentType(componentType)
                + " components: The data length is " + byteBuffer.capacity()
                + " which is not divisble by " + numBytesPerElement);
        }
        int count = byteBuffer.capacity() / numBytesPerElement;
        DefaultAccessorModel accessorModel = new DefaultAccessorModel(
            componentType, count, elementType);
        accessorModel.setNormalized(normalized);
        accessorModel.setAccessorData(
            AccessorDatas.create(accessorModel, byteBuffer));
        return accessorModel;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorModels()
    {
        // Private constructor to prevent instantiation
    }
    
    
}