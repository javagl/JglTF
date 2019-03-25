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
package de.javagl.jgltf.model.impl.creation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;

/**
 * Utility methods for creating the glTF 1.0 elements that correspond to
 * a {@link BufferStructure}
 */
public class BufferStructureGltfV1
{
    /**
     * Create the {@link Accessor} objects from the given 
     * {@link BufferStructure}
     * 
     * @param bufferStructure The {@link BufferStructure}
     * @return The {@link Accessor} objects
     */
    public static Map<String, Accessor> createAccessors(
        BufferStructure bufferStructure)
    {
        List<AccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        Map<String, Accessor> accessors = new LinkedHashMap<String, Accessor>();
        for (AccessorModel accessorModel : accessorModels) 
        {
            BufferViewModel bufferViewModel = 
                accessorModel.getBufferViewModel();
            String bufferViewId = 
                bufferStructure.getBufferViewId(bufferViewModel);
            String accessorId = bufferStructure.getAccessorId(accessorModel);
            Accessor accessor = createAccessor(accessorModel, bufferViewId);
            accessors.put(accessorId, accessor);
        }
        return accessors;
    }

    /**
     * Create the {@link Accessor} object from the given 
     * {@link AccessorModel}
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param bufferViewId The {@link BufferView} ID
     * @return The {@link Accessor} object
     */
    private static Accessor createAccessor(
        AccessorModel accessorModel, String bufferViewId)
    {
        Accessor accessor = new Accessor();

        accessor.setBufferView(bufferViewId);
        
        accessor.setByteOffset(accessorModel.getByteOffset());
        accessor.setComponentType(accessorModel.getComponentType());
        accessor.setCount(accessorModel.getCount());
        accessor.setType(accessorModel.getElementType().toString());
        accessor.setByteStride(accessorModel.getByteStride());
        
        AccessorData accessorData = accessorModel.getAccessorData();
        accessor.setMax(AccessorDatas.computeMax(accessorData));
        accessor.setMin(AccessorDatas.computeMin(accessorData));
        
        return accessor;
    }
    
    /**
     * Create the {@link BufferView} objects from the given 
     * {@link BufferStructure}
     * 
     * @param bufferStructure The {@link BufferStructure}
     * @return The {@link BufferView} objects
     */
    public static Map<String, BufferView> createBufferViews(
        BufferStructure bufferStructure)
    {
        List<BufferViewModel> bufferViewModels = 
            bufferStructure.getBufferViewModels();
        Map<String, BufferView> bufferViews = 
            new LinkedHashMap<String, BufferView>();
        for (BufferViewModel bufferViewModel : bufferViewModels)
        {
            BufferModel bufferModel = bufferViewModel.getBufferModel();
            String bufferId = bufferStructure.getBufferId(bufferModel);
            String bufferViewId = 
                bufferStructure.getBufferViewId(bufferViewModel);
            BufferView bufferView = createBufferView(bufferViewModel, bufferId);
            bufferViews.put(bufferViewId, bufferView);
        }
        return bufferViews;
    }
    
    /**
     * Create the {@link BufferView} object from the given 
     * {@link BufferViewModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @param bufferId The {@link Buffer} ID
     * @return The {@link BufferView} objects
     */
    private static BufferView createBufferView(
        BufferViewModel bufferViewModel, String bufferId)
    {
        BufferView bufferView = new BufferView();

        bufferView.setBuffer(bufferId);
        bufferView.setByteOffset(bufferViewModel.getByteOffset());
        bufferView.setByteLength(bufferViewModel.getByteLength());
        bufferView.setTarget(bufferViewModel.getTarget());
        
        return bufferView;
    }
    
    /**
     * Create the {@link Buffer} objects from the given 
     * {@link BufferStructure}
     * 
     * @param bufferStructure The {@link BufferStructure}
     * @return The {@link Buffer} objects
     */
    public static Map<String, Buffer> createBuffers(
        BufferStructure bufferStructure)
    {
        List<BufferModel> bufferModels = 
            bufferStructure.getBufferModels();
        Map<String, Buffer> buffers = new LinkedHashMap<String, Buffer>();
        for (BufferModel bufferModel : bufferModels) 
        {
            String bufferId = bufferStructure.getBufferId(bufferModel);
            Buffer buffer = createBuffer(bufferModel);
            buffers.put(bufferId, buffer);
        }
        return buffers;
    }
  
    /**
     * Create the {@link Buffer} object from the given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The {@link Buffer}
     */
    private static Buffer createBuffer(BufferModel bufferModel)
    {
        Buffer buffer = new Buffer();
        buffer.setUri(bufferModel.getUri());
        buffer.setByteLength(bufferModel.getByteLength());
        return buffer;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private BufferStructureGltfV1()
    {
        // Private constructor to prevent instantiation
    }
}
