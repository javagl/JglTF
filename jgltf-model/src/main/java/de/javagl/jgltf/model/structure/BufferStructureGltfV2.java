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
package de.javagl.jgltf.model.structure;

import java.util.ArrayList;
import java.util.List;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.Buffer;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.v2.GltfCreatorV2;

/**
 * Utility methods for creating the glTF 2.0 elements that correspond to
 * a {@link BufferStructure}.<br>
 * <br>
 * This class is only intended for internal use, and may be removed in
 * the future.
 */
public class BufferStructureGltfV2
{
    /**
     * Create the {@link Accessor} objects from the given 
     * {@link BufferStructure}
     * 
     * @param bufferStructure The {@link BufferStructure}
     * @return The {@link Accessor} objects
     */
    public static List<Accessor> createAccessors(
        BufferStructure bufferStructure)
    {
        List<DefaultAccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        List<Accessor> accessors = new ArrayList<Accessor>();
        for (AccessorModel accessorModel : accessorModels) 
        {
            BufferViewModel bufferViewModel = 
                accessorModel.getBufferViewModel();
            int bufferViewIndex =
                bufferStructure.getBufferViewIndex(bufferViewModel);
            accessors.add(GltfCreatorV2.createAccessor(
                accessorModel, bufferViewIndex));
        }
        return accessors;
    }
    
    /**
     * Create the {@link BufferView} objects from the given 
     * {@link BufferStructure}
     * 
     * @param bufferStructure The {@link BufferStructure}
     * @return The {@link BufferView} objects
     */
    public static List<BufferView> createBufferViews(
        BufferStructure bufferStructure)
    {
        List<DefaultBufferViewModel> bufferViewModels = 
            bufferStructure.getBufferViewModels();
        List<BufferView> bufferViews = new ArrayList<BufferView>();
        for (BufferViewModel bufferViewModel : bufferViewModels)
        {
            BufferModel bufferModel = bufferViewModel.getBufferModel();
            int bufferIndex = bufferStructure.getBufferIndex(bufferModel);
            bufferViews.add(GltfCreatorV2.createBufferView(
                bufferViewModel, bufferIndex));
        }
        return bufferViews;
    }

    /**
     * Create the {@link Buffer} objects from the given 
     * {@link BufferStructure}
     * 
     * @param bufferStructure The {@link BufferStructure}
     * @return The {@link Buffer} objects
     */
    public static List<Buffer> createBuffers(BufferStructure bufferStructure)
    {
        List<DefaultBufferModel> bufferModels = 
            bufferStructure.getBufferModels();
        List<Buffer> buffers = new ArrayList<Buffer>();
        for (BufferModel bufferModel : bufferModels) 
        {
            buffers.add(GltfCreatorV2.createBuffer(bufferModel));
        }
        return buffers;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private BufferStructureGltfV2()
    {
        // Private constructor to prevent instantiation
    }
}
