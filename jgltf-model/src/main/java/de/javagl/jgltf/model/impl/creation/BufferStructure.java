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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;

/**
 * A class representing the structure of a set of {@link AccessorModel} 
 * instances that refer to {@link BufferViewModel} instances, which
 * in turn refer to {@link BufferModel} instances.<br>
 * <br>
 * Such a buffer structure may be created with a 
 * {@link BufferStructureBuilder}.<br>
 * <br>
 * <b>This class is only intended for internal use!</b> 
 */
public final class BufferStructure
{
    /**
     * The mapping from {@link AccessorModel} instances to their IDs
     */
    private final Map<AccessorModel, String> accessorIds;
    
    /**
     * The list of all {@link BufferViewModel} instances
     */
    private final List<BufferViewModel> bufferViewModels;
    
    /**
     * The mapping from {@link BufferViewModel} instances to their IDs
     */
    private final Map<BufferViewModel, String> bufferViewIds;
    
    /**
     * The mapping from {@link BufferViewModel} instances to the list
     * of {@link AccessorModel} instances that refer to them
     */
    private final Map<BufferViewModel, List<AccessorModel>> 
        bufferViewAccessorModels;

    /**
     * The list of all {@link BufferModel} instances
     */
    private final List<BufferModel> bufferModels;
    
    /**
     * The mapping from {@link BufferModel} instances to their IDs
     */
    private final Map<BufferModel, String> bufferIds;
    
    /**
     * The mapping from {@link BufferModel} instances to the list
     * of {@link BufferViewModel} instances that refer to them
     */
    private final Map<BufferModel, List<BufferViewModel>> 
        bufferBufferViewModels;

    /**
     * A mapping from {@link BufferModel} instances to the sets of
     * byte indices that have been inserted for padding and alignment
     */
    private final Map<BufferModel, Set<Integer>> paddingByteIndices;
    
    /**
     * Default constructor
     */
    BufferStructure()
    {
        this.accessorIds = new LinkedHashMap<AccessorModel, String>();

        this.bufferViewModels = new ArrayList<BufferViewModel>();
        this.bufferViewIds = 
            new LinkedHashMap<BufferViewModel, String>();
        this.bufferViewAccessorModels = 
            new LinkedHashMap<BufferViewModel, 
                List<AccessorModel>>();
        
        this.bufferModels = new ArrayList<BufferModel>();
        this.bufferIds = new LinkedHashMap<BufferModel, String>();
        this.bufferBufferViewModels = 
            new LinkedHashMap<BufferModel, List<BufferViewModel>>();
        
        this.paddingByteIndices = 
            new LinkedHashMap<BufferModel, Set<Integer>>();
    }
    
    /**
     * Add the specified {@link AccessorModel} to this structure
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param accessorId The ID
     */
    void addAccessorModel(AccessorModel accessorModel, String accessorId)
    {
        this.accessorIds.put(accessorModel, accessorId);
    }
    
    /**
     * Add the given {@link BufferViewModel} to this structure
     * 
     * @param bufferViewModel The {@link BufferViewModel} to add
     * @param bufferViewId The ID
     * @param accessorModels The {@link AccessorModel} instances that 
     * refer to the given {@link BufferViewModel}. A copy of the 
     * given collection will be stored internally.
     */
    void addBufferViewModel(
        BufferViewModel bufferViewModel, String bufferViewId, 
        Collection<? extends AccessorModel> accessorModels)
    {
        this.bufferViewModels.add(bufferViewModel);
        this.bufferViewIds.put(bufferViewModel, bufferViewId);
        this.bufferViewAccessorModels.put(bufferViewModel, 
            new ArrayList<AccessorModel>(accessorModels));
    }
    
    /**
     * Add the given {@link BufferModel} to this structure
     * 
     * @param bufferModel The {@link BufferModel} to add
     * @param bufferId The ID
     * @param bufferViewModels The {@link BufferViewModel} instances
     * that refer to the given {@link BufferModel}.  A copy of the 
     * given collection will be stored internally.
     */
    void addBufferModel(BufferModel bufferModel, String bufferId,
        Collection<? extends BufferViewModel> bufferViewModels)
    {
        this.bufferModels.add(bufferModel);
        this.bufferIds.put(bufferModel, bufferId);
        this.bufferBufferViewModels.put(bufferModel, 
            new ArrayList<BufferViewModel>(bufferViewModels));
    }
    
    /**
     * Add the given index as a padding byte index for the given 
     * {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @param index The index
     */
    private void addPaddingByteIndex(BufferModel bufferModel, int index)
    {
        this.paddingByteIndices.computeIfAbsent(bufferModel, 
            bm -> new LinkedHashSet<Integer>()).add(index);
    }
    
    /**
     * Add the specified indices as a padding byte index for the given 
     * {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @param startIndex The start index
     * @param count The number of bytes
     */
    void addPaddingByteIndices(
        BufferModel bufferModel, int startIndex, int count)
    {
        for (int index = startIndex; index < startIndex + count; index++)
        {
            addPaddingByteIndex(bufferModel, index);
        }
    }
    
    /**
     * Returns an unmodifiable view on the list of all {@link BufferModel}
     * instances that have been added to this structure
     * 
     * @return The {@link BufferModel} instances
     */
    public List<BufferModel> getBufferModels()
    {
        return Collections.unmodifiableList(bufferModels);
    }
    
    /**
     * Returns the ID that was given to the given {@link BufferModel}
     * when it was added 
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The ID
     */
    public String getBufferId(BufferModel bufferModel)
    {
        return bufferIds.get(bufferModel);
    }
    
    /**
     * Returns an unmodifiable list containing the {@link BufferViewModel}
     * instances that refer to the given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The list
     */
    public List<BufferViewModel> getBufferViewModels(BufferModel bufferModel)
    {
        List<BufferViewModel> bufferViewModels = 
            bufferBufferViewModels.get(bufferModel);
        if (bufferViewModels == null)
        {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(bufferViewModels);
    }
    
    /**
     * Returns an unmodifiable view on the {@link BufferViewModel} instances
     * that have been added to this structure
     * 
     * @return The {@link BufferViewModel} instances 
     */
    public List<BufferViewModel> getBufferViewModels()
    {
        return Collections.unmodifiableList(bufferViewModels);
    }
    
    /**
     * Returns the ID that was given to the given {@link BufferViewModel}
     * when it was added
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The ID
     */
    public String getBufferViewId(BufferViewModel bufferViewModel)
    {
        return bufferViewIds.get(bufferViewModel);
    }
    
    /**
     * Returns an unmodifiable list containing the {@link AccessorModel}
     * instances that refer to the given {@link BufferViewModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The list
     */
    public List<AccessorModel> getAccessorModels(
        BufferViewModel bufferViewModel)
    {
        List<AccessorModel> accessorModels = 
            bufferViewAccessorModels.get(bufferViewModel);
        if (accessorModels == null)
        {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(accessorModels);
    }
    
    /**
     * Returns an unmodifiable list containing all {@link AccessorModel} 
     * instances that have been added to this structure
     * 
     * @return The {@link AccessorModel} instances
     */
    public List<AccessorModel> getAccessorModels()
    {
        List<AccessorModel> allAccessorModels = 
            new ArrayList<AccessorModel>(accessorIds.keySet());
        return Collections.unmodifiableList(allAccessorModels);
    }
    
    /**
     * Returns the ID that was given to the given {@link AccessorModel} 
     * when it was added
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The ID
     */
    public String getAccessorId(AccessorModel accessorModel) 
    {
        return accessorIds.get(accessorModel);
    }

    /**
     * Returns whether the given byte index was a padding byte index for the
     * given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @param index The index
     * @return Whether the index is a padding byte index
     */
    public boolean isPaddingByteIndex(BufferModel bufferModel, int index)
    {
        Set<Integer> indices = paddingByteIndices.get(bufferModel);
        if (indices == null)
        {
            return false;
        }
        return indices.contains(index);
    }


    
}
