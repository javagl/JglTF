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
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;

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
    private final Map<DefaultAccessorModel, String> accessorIds;

    /**
     * The set of values in the {@link #accessorIds}
     */
    private final Set<String> accessorIdValues;
    
    /**
     * The mapping from {@link AccessorModel} instances to their indices
     */
    private final Map<DefaultAccessorModel, Integer> accessorIndices;
    
    /**
     * The list of all {@link BufferViewModel} instances
     */
    private final List<DefaultBufferViewModel> bufferViewModels;
    
    /**
     * The mapping from {@link BufferViewModel} instances to their IDs
     */
    private final Map<DefaultBufferViewModel, String> bufferViewIds;

    /**
     * The set of values in the {@link #bufferViewIds}
     */
    private final Set<String> bufferViewIdValues;
    
    /**
     * The mapping from {@link BufferViewModel} instances to their indices
     */
    private final Map<DefaultBufferViewModel, Integer> bufferViewIndices;
    
    /**
     * The mapping from {@link BufferViewModel} instances to the list
     * of {@link AccessorModel} instances that refer to them
     */
    private final Map<DefaultBufferViewModel, List<DefaultAccessorModel>> 
        bufferViewAccessorModels;

    /**
     * The list of all {@link BufferModel} instances
     */
    private final List<DefaultBufferModel> bufferModels;
    
    /**
     * The mapping from {@link BufferModel} instances to their IDs
     */
    private final Map<DefaultBufferModel, String> bufferIds;
    
    /**
     * The set of values in the {@link #bufferIds}
     */
    private final Set<String> bufferIdValues;
    
    
    /**
     * The mapping from {@link BufferModel} instances to their indices
     */
    private final Map<DefaultBufferModel, Integer> bufferIndices;
    
    /**
     * The mapping from {@link BufferModel} instances to the list
     * of {@link BufferViewModel} instances that refer to them
     */
    private final Map<DefaultBufferModel, List<DefaultBufferViewModel>> 
        bufferBufferViewModels;

    /**
     * A mapping from {@link BufferModel} instances to the sets of
     * byte indices that have been inserted for padding and alignment
     */
    private final Map<DefaultBufferModel, Set<Integer>> paddingByteIndices;
    
    /**
     * A mapping from prefixes for IDs to the next number that can be appended
     * to a given prefix in order to create a new ID. 
     */
    private final Map<String, Integer> idSuffixes;
    
    /**
     * Default constructor
     */
    public BufferStructure()
    {
        this.accessorIds = new LinkedHashMap<DefaultAccessorModel, String>();
        this.accessorIdValues = new LinkedHashSet<String>();
        this.accessorIndices = 
            new LinkedHashMap<DefaultAccessorModel, Integer>();

        this.bufferViewModels = new ArrayList<DefaultBufferViewModel>();
        this.bufferViewIds = 
            new LinkedHashMap<DefaultBufferViewModel, String>();
        this.bufferViewIdValues = new LinkedHashSet<String>();
        this.bufferViewIndices = 
            new LinkedHashMap<DefaultBufferViewModel, Integer>();
        this.bufferViewAccessorModels = 
            new LinkedHashMap<DefaultBufferViewModel, 
                List<DefaultAccessorModel>>();
        
        this.bufferModels = new ArrayList<DefaultBufferModel>();
        this.bufferIds = new LinkedHashMap<DefaultBufferModel, String>();
        this.bufferIdValues = new LinkedHashSet<String>();
        this.bufferIndices = 
            new LinkedHashMap<DefaultBufferModel, Integer>();
        this.bufferBufferViewModels = 
            new LinkedHashMap<DefaultBufferModel, 
                List<DefaultBufferViewModel>>();
        
        this.paddingByteIndices = 
            new LinkedHashMap<DefaultBufferModel, Set<Integer>>();
        
        this.idSuffixes = new LinkedHashMap<String, Integer>();
    }
    
    /**
     * Add the specified {@link AccessorModel} to this structure
     * 
     * @param accessorModel The {@link AccessorModel}
     * @param idPrefix The ID prefix
     */
    public void addAccessorModel(
        DefaultAccessorModel accessorModel, String idPrefix)
    {
        String id = createId(idPrefix, accessorIdValues);
        this.accessorIndices.put(accessorModel, accessorIndices.size());
        this.accessorIds.put(accessorModel, id);
        this.accessorIdValues.add(id);
    }
    
    /**
     * Add the given {@link BufferViewModel} to this structure
     * 
     * @param bufferViewModel The {@link BufferViewModel} to add
     * @param idPrefix The ID prefix
     * @param accessorModels The {@link AccessorModel} instances that 
     * refer to the given {@link BufferViewModel}. A copy of the 
     * given collection will be stored internally.
     */
    public void addBufferViewModel(
        DefaultBufferViewModel bufferViewModel, String idPrefix, 
        Collection<? extends DefaultAccessorModel> accessorModels)
    {
        this.bufferViewModels.add(bufferViewModel);
        String id = createId(idPrefix, bufferViewIdValues);
        this.bufferViewIds.put(bufferViewModel, id);
        this.bufferViewIdValues.add(id);
        this.bufferViewIndices.put(bufferViewModel, bufferViewIndices.size());
        this.bufferViewAccessorModels.put(bufferViewModel, 
            new ArrayList<DefaultAccessorModel>(accessorModels));
    }
    
    /**
     * Add the given {@link BufferModel} to this structure
     * 
     * @param bufferModel The {@link BufferModel} to add
     * @param idPrefix The ID prefix
     * @param bufferViewModels The {@link BufferViewModel} instances
     * that refer to the given {@link BufferModel}.  A copy of the 
     * given collection will be stored internally.
     */
    public void addBufferModel(DefaultBufferModel bufferModel, String idPrefix,
        Collection<? extends DefaultBufferViewModel> bufferViewModels)
    {
        this.bufferModels.add(bufferModel);
        String id = createId(idPrefix, bufferIdValues);
        this.bufferIds.put(bufferModel, id);
        this.bufferIdValues.add(id);
        this.bufferIndices.put(bufferModel, bufferIndices.size());
        this.bufferBufferViewModels.put(bufferModel, 
            new ArrayList<DefaultBufferViewModel>(bufferViewModels));
    }
    
    /**
     * Add the given index as a padding byte index for the given 
     * {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @param index The index
     */
    private void addPaddingByteIndex(DefaultBufferModel bufferModel, int index)
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
        DefaultBufferModel bufferModel, int startIndex, int count)
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
    public List<DefaultBufferModel> getBufferModels()
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
     * Returns the index that was assigned to the given {@link BufferModel}
     * when it was added
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The index
     */
    public Integer getBufferIndex(BufferModel bufferModel)
    {
        return bufferIndices.get(bufferModel);
    }
    
    /**
     * Returns an unmodifiable list containing the {@link BufferViewModel}
     * instances that refer to the given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The list
     */
    public List<DefaultBufferViewModel> getBufferViewModels(
        BufferModel bufferModel)
    {
        List<DefaultBufferViewModel> bufferViewModels = 
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
    public List<DefaultBufferViewModel> getBufferViewModels()
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
     * Returns the index that was assigned to the given {@link BufferViewModel}
     * when it was added
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The index
     */
    public Integer getBufferViewIndex(BufferViewModel bufferViewModel)
    {
        return bufferViewIndices.get(bufferViewModel);
    }
    
    /**
     * Returns an unmodifiable list containing the {@link AccessorModel}
     * instances that refer to the given {@link BufferViewModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The list
     */
    public List<DefaultAccessorModel> getAccessorModels(
        BufferViewModel bufferViewModel)
    {
        List<DefaultAccessorModel> accessorModels = 
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
    public List<DefaultAccessorModel> getAccessorModels()
    {
        List<DefaultAccessorModel> allAccessorModels = 
            new ArrayList<DefaultAccessorModel>(accessorIds.keySet());
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
     * Returns the index that was assigned to the given {@link AccessorModel}
     * when it was added
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The index
     */
    public Integer getAccessorIndex(AccessorModel accessorModel)
    {
        return accessorIndices.get(accessorModel);
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

    
    /**
     * Create an unspecified ID string with the given prefix, that is not 
     * contained in the given collection
     * 
     * @param prefix The prefix for the ID
     * @param existingIds The existing ID strings
     * @return The new ID string
     */
    private String createId(String prefix,
        Collection<? extends String> existingIds)
    {
        int counter = idSuffixes.getOrDefault(prefix, 0);
        String id = prefix;
        while (existingIds.contains(id)) 
        {
            id = prefix + "_" + counter;
            counter++;
        }
        idSuffixes.put(prefix, counter);
        return id;
    }
    

    
}
