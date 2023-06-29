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
package de.javagl.jgltf.viewer;

import java.nio.ByteBuffer;

import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Methods to create {@link AccessorModel} instances programmatically
 */
class AccessorModelCreation
{
    /**
     * Create a new {@link AccessorModel} that describes the same data as
     * the given {@link AccessorModel}, but in a compact form. The returned
     * {@link AccessorModel} will refer to a newly created 
     * {@link BufferViewModel} and a newly created {@link BufferModel} that
     * contain exactly the data for the accessor.<br>
     * <br>
     * The given {@link AccessorModel} is assumed to have a <code>float</code>
     * component type.
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferUriString The URI string for the {@link BufferModel}
     * @return The new {@link AccessorModel} instance.
     */
    static AccessorModel instantiate(
        AccessorModel accessorModel, String bufferUriString)
    {
        AccessorModel instantiatedAccessorModel = createAccessorModel(
            accessorModel.getComponentType(), accessorModel.getCount(),
            accessorModel.getElementType(), bufferUriString);

        AccessorData accessorData = accessorModel.getAccessorData();
        AccessorFloatData accessorFloatData = (AccessorFloatData)accessorData;
        
        AccessorData instantiatedAccessorData =
            instantiatedAccessorModel.getAccessorData();
        AccessorFloatData instantiatedAccessorFloatData =
            (AccessorFloatData)instantiatedAccessorData;

        AccessorDataUtils.copyFloats(
            instantiatedAccessorFloatData, accessorFloatData);

        return instantiatedAccessorModel;
    }

    /**
     * Creates a new {@link AccessorModel} from the given parameters. It will
     * refer to a newly created {@link BufferViewModel}, which in turn refers to
     * a newly created {@link BufferModel}, each containing exactly the data
     * required for the accessor.
     * 
     * @param componentType The component type
     * @param count The count
     * @param elementType The element type
     * @param bufferUriString The URI string for the {@link BufferModel}
     * @return The {@link AccessorModel}
     */
    static AccessorModel createAccessorModel(int componentType,
        int count, ElementType elementType, String bufferUriString)
    {
        DefaultAccessorModel accessorModel =
            new DefaultAccessorModel(componentType, count, elementType);
        int elementSize = accessorModel.getPaddedElementSizeInBytes();
        accessorModel.setByteOffset(0);
        
        ByteBuffer bufferData = Buffers.create(count * elementSize);
        accessorModel.setBufferViewModel(
            createBufferViewModel(bufferUriString, bufferData));
        return accessorModel;
    }

    /**
     * Create a new {@link BufferViewModel} with an associated
     * {@link BufferModel} that serves as the basis for a sparse accessor, or 
     * an accessor that does not refer to a {@link BufferView})
     * 
     * @param uriString The URI string that will be assigned to the
     * {@link BufferModel} that is created internally. This string is not
     * strictly required, but helpful for debugging, at least
     * @param bufferData The buffer data
     * @return The new {@link BufferViewModel}
     */
    private static DefaultBufferViewModel createBufferViewModel(
        String uriString, ByteBuffer bufferData)
    {
        DefaultBufferModel bufferModel = new DefaultBufferModel();
        bufferModel.setUri(uriString);
        bufferModel.setBufferData(bufferData);

        DefaultBufferViewModel bufferViewModel =
            new DefaultBufferViewModel(null);
        bufferViewModel.setByteOffset(0);
        bufferViewModel.setByteLength(bufferData.capacity());
        bufferViewModel.setBufferModel(bufferModel);

        return bufferViewModel;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorModelCreation()
    {
        // Private constructor to prevent instantiation
    }
    
    
}
