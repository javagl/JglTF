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

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class for creating {@link BufferStructure} instances.<br>
 * <br>
 * The class offers methods to build the structure of accessors, buffer views 
 * and buffers that are part of a glTF asset. It allows to create these 
 * elements hierarchically: A sequence of accessors may be added, which 
 * are then combined into a buffer view. The sequence of buffer views that 
 * are created can then be combined into a buffer.<br>
 * <br>
 * <b>Note:</b> Although the methods for creating the models always return
 * the respective model, e.g. an <code>AccessorModel</code> instance, these
 * instances will not be fully initialized until the {@link #build()} 
 * method is called.
 */
public final class BufferStructureBuilder 
{
    /**
     * The {@link BufferStructure} that is created by this instance
     */
    private final BufferStructure bufferStructure;

    /**
     * The set of {@link AccessorModel} instances that have been added
     * and which will be used to create a {@link BufferViewModel} when 
     * calling {@link #createBufferViewModel}
     */
    private final List<DefaultAccessorModel> currentAccessorModels;
    
    /**
     * The set of {@link BufferViewModel} instances that have been created
     * by calling {@link #createBufferViewModel}, and which will be used
     * to create a {@link BufferModel} when calling 
     * {@link #createBufferModel}
     */
    private final List<DefaultBufferViewModel> currentBufferViewModels;
    
    /**
     * The mapping from buffer view objects that do not have associated 
     * accessors to their data
     */
    private final Map<DefaultBufferViewModel, ByteBuffer> 
        standaloneBufferViewDataMap;
    
    /**
     * Default constructor
     */
    public BufferStructureBuilder()
    {
        this.bufferStructure = new BufferStructure();
        this.currentAccessorModels = new ArrayList<DefaultAccessorModel>();
        this.currentBufferViewModels = new ArrayList<DefaultBufferViewModel>();
        this.standaloneBufferViewDataMap = 
            new LinkedHashMap<DefaultBufferViewModel, ByteBuffer>();
    }
    
    /**
     * Returns the number of {@link AccessorModel} instances that have
     * been created until now
     * 
     * @return The number of {@link AccessorModel} instances
     */
    public int getNumAccessorModels()
    {
        return bufferStructure.getAccessorModels().size();
    }

    /**
     * Returns the number of {@link BufferViewModel} instances that have
     * been created until now
     * 
     * @return The number of {@link BufferViewModel} instances
     */
    public int getNumBufferViewModels()
    {
        return bufferStructure.getBufferViewModels().size();
    }

    /**
     * Returns the number of {@link BufferModel} instances that have
     * been created until now
     * 
     * @return The number of {@link BufferModel} instances
     */
    public int getNumBufferModels()
    {
        return bufferStructure.getBufferModels().size();
    }
    
    /**
     * Returns the number of {@link AccessorModel} instances that have been 
     * added, but for which no {@link BufferViewModel} has been created
     * yet.
     * 
     * @return The number of pending {@link AccessorModel} instances
     */
    public int getNumCurrentAccessorModels() 
    {
        return currentAccessorModels.size();
    }
    
    /**
     * Returns the number of {@link BufferViewModel} instances that have been 
     * added, but for which no {@link BufferModel} has been created
     * yet.
     * 
     * @return The number of pending {@link BufferViewModel} instances
     */
    public int getNumCurrentBufferViewModels() 
    {
        return currentBufferViewModels.size();
    }
    
    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param idPrefix The ID prefix of the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @return The {@link AccessorModel}
     */
    public AccessorModel createAccessorModel(
        String idPrefix, float data[], String type)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        if (data.length % numComponents != 0)
        {
            throw new IllegalArgumentException("Invalid data for type " + type
                + ". The data.length is not divisble by " + numComponents);
        }
        int componentType = GltfConstants.GL_FLOAT;
        ByteBuffer byteBuffer = 
            Buffers.createByteBufferFrom(FloatBuffer.wrap(data));        
        return createAccessorModel(idPrefix, componentType, type, byteBuffer);
    }
    
    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param idPrefix The ID prefix of the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @return The {@link AccessorModel}
     */
    public AccessorModel createAccessorModel(
        String idPrefix, double data[], String type)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        if (data.length % numComponents != 0)
        {
            throw new IllegalArgumentException("Invalid data for type " + type
                + ". The data.length is not divisble by " + numComponents);
        }
        int componentType = GltfConstants.GL_DOUBLE;
        ByteBuffer byteBuffer = 
            Buffers.createByteBufferFrom(DoubleBuffer.wrap(data));        
        return createAccessorModel(idPrefix, componentType, type, byteBuffer);
    }
    
    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built, using the component type
     * <code>GL_UNSIGNED_INT</code>
     * 
     * @param idPrefix The ID prefix of the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @return The {@link AccessorModel}
     */
    public AccessorModel createAccessorModel(
        String idPrefix, int data[], String type)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        if (data.length % numComponents != 0)
        {
            throw new IllegalArgumentException("Invalid data for type " + type
                + ". The data.length is not divisble by " + numComponents);
        }
        int componentType = GltfConstants.GL_UNSIGNED_INT;
        ByteBuffer byteBuffer = 
            Buffers.createByteBufferFrom(IntBuffer.wrap(data));        
        return createAccessorModel(idPrefix, componentType, type, byteBuffer);
    }

    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built, using the component type
     * <code>GL_UNSIGNED_SHORT</code>
     * 
     * @param idPrefix The ID prefix for the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @return The {@link AccessorModel}
     */
    public AccessorModel createAccessorModel(
        String idPrefix, short data[], String type)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        if (data.length % numComponents != 0)
        {
            throw new IllegalArgumentException("Invalid data for type " + type
                + ". The data.length is not divisble by " + numComponents);
        }
        int componentType = GltfConstants.GL_UNSIGNED_SHORT;
        ByteBuffer byteBuffer = 
            Buffers.createByteBufferFrom(ShortBuffer.wrap(data));
        return createAccessorModel(idPrefix, componentType, type, byteBuffer);
    }

    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built, using the component type
     * <code>GL_UNSIGNED_BYTE</code>
     * 
     * @param idPrefix The ID prefix for the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @return The {@link AccessorModel}
     */
    public AccessorModel createAccessorModel(
        String idPrefix, byte data[], String type)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        if (data.length % numComponents != 0)
        {
            throw new IllegalArgumentException("Invalid data for type " + type
                + ". The data.length is not divisble by " + numComponents);
        }
        int componentType = GltfConstants.GL_UNSIGNED_BYTE;
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        return createAccessorModel(idPrefix, componentType, type, byteBuffer);
    }

    
    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param idPrefix The ID prefix of the {@link AccessorModel}
     * @param componentType The component type, as a GL constant
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @param byteBuffer The actual data
     * @return The {@link AccessorModel}
     */
    public AccessorModel createAccessorModel(String idPrefix,
        int componentType, String type, ByteBuffer byteBuffer)
    {
        boolean normalized = false;
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
        addAccessorModel(idPrefix, accessorModel);
        return accessorModel;
    }
        
    
    /**
     * Add the given {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param idPrefix The ID prefix of the {@link AccessorModel}
     * @param accessorModel The {@link AccessorModel}
     * @throws GltfException If the given accessor model already has an
     * associated buffer view
     */
    public void addAccessorModel(
        String idPrefix, DefaultAccessorModel accessorModel)
    {
        BufferViewModel bufferViewModel = accessorModel.getBufferViewModel();
        if (bufferViewModel != null)
        {
            throw new GltfException(
                "The accessor already contains a buffer view");
        }
        bufferStructure.addAccessorModel(accessorModel, idPrefix);
        currentAccessorModels.add(accessorModel);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. The {@link BufferViewModel#getTarget()
     * target} will be the GL constant for <code>GL_ARRAY_BUFFER</code>.
     * 
     * @param idPrefix The ID prefix for the {@link BufferViewModel}
     * @return The {@link BufferViewModel}
     */
    public BufferViewModel createArrayBufferViewModel(String idPrefix)
    {
        return createBufferViewModel(
            idPrefix, GltfConstants.GL_ARRAY_BUFFER);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. The {@link BufferViewModel#getTarget()
     * target} will be the GL constant for <code>GL_ELEMENT_ARRAY_BUFFER</code>.
     * 
     * @param idPrefix The ID prefix for the {@link BufferViewModel}
     * @return The {@link BufferViewModel}
     */
    public BufferViewModel createArrayElementBufferViewModel(String idPrefix)
    {
        return createBufferViewModel(
            idPrefix, GltfConstants.GL_ELEMENT_ARRAY_BUFFER);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. 
     * 
     * @param idPrefix The ID prefix for the {@link BufferViewModel}
     * @param target The {@link BufferViewModel#getTarget()}
     * @return The {@link BufferViewModel}
     */
    public BufferViewModel createBufferViewModel(
        String idPrefix, Integer target)
    {
        DefaultBufferViewModel bufferViewModel =
            new DefaultBufferViewModel(target);
        addBufferViewModel(idPrefix, bufferViewModel);
        return bufferViewModel;
    }

    /**
     * Add the given {@link BufferViewModel} in the {@link BufferStructure} 
     * that is currently being built. All {@link AccessorModel} instances
     * that have been created until now and not yet added to a 
     * {@link BufferViewModel} will be assigned to the given model. 
     * 
     * @param idPrefix The ID prefix for the {@link BufferViewModel}
     * @param bufferViewModel The {@link BufferViewModel} to add
     */
    public void addBufferViewModel(
        String idPrefix, DefaultBufferViewModel bufferViewModel)
    {
        for (DefaultAccessorModel accessorModel : currentAccessorModels)
        {
            accessorModel.setBufferViewModel(bufferViewModel);
        }
        bufferStructure.addBufferViewModel(
            bufferViewModel, idPrefix, currentAccessorModels);
        currentBufferViewModels.add(bufferViewModel);
        currentAccessorModels.clear();
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built.<br>
     * <br>
     * This is intended for buffer view models that do not have associated
     * accessors (e.g. buffer view models that are used for images or
     * by extensions) 
     * 
     * @param idPrefix The ID prefix for the {@link BufferViewModel}
     * @param bufferViewData The buffer view data
     * @return The {@link BufferViewModel}
     * @throws IllegalStateException When there are pending accessor models
     */
    public BufferViewModel createStandaloneBufferViewModel(
        String idPrefix, ByteBuffer bufferViewData)
    {
        Objects.requireNonNull(bufferViewData, 
            "The bufferViewData may not be null");
        if (!currentAccessorModels.isEmpty())
        {
            throw new IllegalStateException(
                "Cannot create an image buffer view model with "
                + currentAccessorModels.size() + " pending accessors");
        }

        DefaultBufferViewModel bufferViewModel =
            new DefaultBufferViewModel(null);
        addStandaloneBufferViewModel(idPrefix, bufferViewModel, bufferViewData);
        return bufferViewModel;
    }

    /**
     * Add a standalone {@link BufferViewModel} in the {@link BufferStructure}
     * that is currently being built.<br>
     * <br>
     * This is intended for buffer view models that do not have associated
     * accessors (e.g. buffer view models that are used for images or by
     * extensions).<br>
     * <br>
     * The byte length of the given buffer view will be set to be the 
     * capacity of the given data buffer.
     * 
     * @param idPrefix The ID prefix for the {@link BufferViewModel}
     * @param bufferViewModel The buffer view model
     * @param bufferViewData The buffer view data
     */
    public void addStandaloneBufferViewModel(String idPrefix,
        DefaultBufferViewModel bufferViewModel, ByteBuffer bufferViewData)
    {
        Objects.requireNonNull(bufferViewModel,
            "The bufferViewModel may not be null");
        Objects.requireNonNull(bufferViewData,
            "The bufferViewData may not be null");
        bufferViewModel.setByteLength(bufferViewData.capacity());
        standaloneBufferViewDataMap.put(bufferViewModel, bufferViewData);
        bufferStructure.addBufferViewModel(bufferViewModel, idPrefix,
            Collections.emptyList());
        currentBufferViewModels.add(bufferViewModel);
    }    
    
    /**
     * Create a {@link BufferModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * If no buffer view models have been created (at all, or after
     * this method has previously been called), then this call will
     * not have any effect, and <code>null</code> will be returned.
     * 
     * @param idPrefix The ID prefix for the {@link BufferModel}
     * @param uri The {@link BufferModel#getUri()}
     * @return The {@link BufferModel} 
     */
    public BufferModel createBufferModel(String idPrefix, String uri) 
    {
        if (currentBufferViewModels.isEmpty())
        {
            return null;
        }
        DefaultBufferModel bufferModel = new DefaultBufferModel();
        bufferModel.setUri(uri);
        addBufferModel(idPrefix, bufferModel);
        return bufferModel;
    }

    /**
     * Add the given {@link BufferModel} in the {@link BufferStructure} 
     * that is currently being built. All {@link BufferViewModel} instances
     * that have been created until now and not yet added to a 
     * {@link BufferModel} will be assigned to the given model.
     * 
     * @param idPrefix The ID prefix for the {@link BufferModel}
     * @param bufferModel The {@link BufferModel}
     */
    public void addBufferModel(String idPrefix, DefaultBufferModel bufferModel) 
    {
        for (DefaultBufferViewModel bufferViewModel : currentBufferViewModels)
        {
            bufferViewModel.setBufferModel(bufferModel);
        }
        bufferStructure.addBufferModel(bufferModel, idPrefix,
            currentBufferViewModels);
        currentBufferViewModels.clear();
    }
    
    /**
     * Return the {@link BufferStructure} instance that was created with
     * this builder.
     * 
     * @return The {@link BufferStructure}
     * @throws IllegalStateException If there are buffer views that have
     * not yet been combined into a buffer
     */
    public BufferStructure build()
    {
        if (!currentBufferViewModels.isEmpty())
        {
            throw new IllegalStateException("There are "
                + currentBufferViewModels.size() + " buffer views for "
                + "which no buffer has been created yet. "
                + "The 'createBufferModel' method must be called before "
                + "building the buffer structure");
        }
        BufferStructureProcessor bufferStructureProcessor = 
            new BufferStructureProcessor(
                bufferStructure, standaloneBufferViewDataMap);
        bufferStructureProcessor.process();
        return bufferStructure;
    }
    
}
