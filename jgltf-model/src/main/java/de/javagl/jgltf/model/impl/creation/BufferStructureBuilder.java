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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class for creating {@link BufferStructure} instances.
 * <br>
 * <b>This class is only intended for internal use!</b> 
 */
public final class BufferStructureBuilder 
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BufferStructureBuilder.class.getName());
    
    /**
     * The {@link BufferStructure} that is created by this instance
     */
    private final BufferStructure bufferStructure;

    /**
     * The set of {@link AccessorModel} instances that have been created
     * by calling {@link #createAccessorModel}, and which will be used
     * to create a {@link BufferViewModel} when calling 
     * {@link #createBufferViewModel}
     */
    private final List<DefaultAccessorModel> currentAccessorModels;
    
    /**
     * The mapping from {@link AccessorModel} instances to the byte buffers
     * that contain their data, as given during the construction. These
     * byte buffers may later be combined, reordered or padded in order
     * to create the actual buffer data!
     */
    private final Map<DefaultAccessorModel, ByteBuffer> 
        rawAccessorModelByteBuffers;

    /**
     * The set of {@link BufferViewModel} instances that have been created
     * by calling {@link #createBufferViewModel}, and which will be used
     * to create a {@link BufferModel} when calling 
     * {@link #createBufferModel}
     */
    private final List<DefaultBufferViewModel> currentBufferViewModels;
    
    /**
     * Default constructor
     */
    public BufferStructureBuilder()
    {
        this.bufferStructure = new BufferStructure();
        this.currentAccessorModels = new ArrayList<DefaultAccessorModel>();
        this.rawAccessorModelByteBuffers = 
            new LinkedHashMap<DefaultAccessorModel, ByteBuffer>();
        this.currentBufferViewModels = new ArrayList<DefaultBufferViewModel>();
    }
    
    /**
     * If the given {@link AccessorModel} is a {@link DefaultAccessorModel},
     * it will be returned. Otherwise, an error message is printed, and
     * <code>null</code> is returned
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The given instance, casted to {@link DefaultAccessorModel}
     */
    private DefaultAccessorModel getDefaultAccessorModel(
        AccessorModel accessorModel)
    {
        if (accessorModel instanceof DefaultAccessorModel)
        {
            return (DefaultAccessorModel)accessorModel;
        }
        logger.severe("AccessorModel is not a DefaultAccessorModel: " 
            + accessorModel);
        return null;
    }
    
    /**
     * If the given {@link BufferViewModel} is a {@link DefaultBufferViewModel},
     * it will be returned. Otherwise, an error message is printed, and
     * <code>null</code> is returned
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The given instance, casted to {@link DefaultBufferViewModel}
     */
    private DefaultBufferViewModel getDefaultBufferViewModel(
        BufferViewModel bufferViewModel)
    {
        if (bufferViewModel instanceof DefaultBufferViewModel)
        {
            return (DefaultBufferViewModel)bufferViewModel;
        }
        logger.severe("BufferViewModel is not a DefaultBufferViewModel: " 
            + bufferViewModel);
        return null;
    }
    
    /**
     * If the given {@link BufferModel} is a {@link DefaultBufferModel},
     * it will be returned. Otherwise, an error message is printed, and
     * <code>null</code> is returned
     * 
     * @param bufferModel The {@link BufferModel}
     * @return The given instance, casted to {@link DefaultBufferModel}
     */
    private DefaultBufferModel getDefaultBufferModel(
        BufferModel bufferModel)
    {
        if (bufferModel instanceof DefaultBufferModel)
        {
            return (DefaultBufferModel)bufferModel;
        }
        logger.severe("BufferModel is not a DefaultBufferModel: " 
            + bufferModel);
        return null;
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
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param id The ID of the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     */
    public void createAccessorModel(
        String id, float data[], String type)
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
        createAccessorModel(id, componentType, type, byteBuffer);
    }

    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param id The ID for the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     */
    public void createAccessorModel(
        String id, short data[], String type)
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
        createAccessorModel(id, componentType, type, byteBuffer);
    }

    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param id The ID of the {@link AccessorModel}
     * @param componentType The component type, as a GL constant
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @param byteBuffer The actual data
     */
    public void createAccessorModel(String id,
        int componentType, String type, ByteBuffer byteBuffer)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        int numBytesPerComponent = 
            Accessors.getNumBytesForAccessorComponentType(componentType);
        int numBytesPerElement = numComponents * numBytesPerComponent;
        int count = byteBuffer.capacity() / numBytesPerElement;
        DefaultAccessorModel accessorModel = new DefaultAccessorModel(
            componentType, count, elementType);
        bufferStructure.addAccessorModel(accessorModel, id);
        currentAccessorModels.add(accessorModel);
        rawAccessorModelByteBuffers.put(accessorModel, byteBuffer);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. The {@link BufferViewModel#getTarget()
     * target} will be the GL constant for <code>GL_ARRAY_BUFFER</code>.
     * 
     * @param id The ID for the {@link BufferViewModel}
     */
    public void createArrayBufferViewModel(String id)
    {
        createBufferViewModel(
            id, GltfConstants.GL_ARRAY_BUFFER);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. The {@link BufferViewModel#getTarget()
     * target} will be the GL constant for <code>GL_ELEMENT_ARRAY_BUFFER</code>.
     * 
     * @param id The ID for the {@link BufferViewModel}
     */
    public void createArrayElementBufferViewModel(String id)
    {
        createBufferViewModel(
            id, GltfConstants.GL_ELEMENT_ARRAY_BUFFER);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. 
     * 
     * @param id The ID for the {@link BufferViewModel}
     * @param target The {@link BufferViewModel#getTarget()}
     */
    public void createBufferViewModel(String id, Integer target)
    {
        DefaultBufferViewModel bufferViewModel = 
            new DefaultBufferViewModel(target);
        for (DefaultAccessorModel accessorModel : currentAccessorModels)
        {
            accessorModel.setBufferViewModel(bufferViewModel);
        }
        bufferStructure.addBufferViewModel(
            bufferViewModel, id, currentAccessorModels);
        currentBufferViewModels.add(bufferViewModel);
        currentAccessorModels.clear();
    }

    
    
    /**
     * Create a {@link BufferModel} in the {@link BufferStructure} that 
     * is currently being built. 
     * 
     * @param id The ID for the {@link BufferModel}
     * @param uri The {@link BufferModel#getUri()}
     */
    public void createBufferModel(String id, String uri) 
    {
        DefaultBufferModel bufferModel = new DefaultBufferModel();
        bufferModel.setUri(uri);
        for (DefaultBufferViewModel bufferViewModel : currentBufferViewModels)
        {
            bufferViewModel.setBufferModel(bufferModel);
        }
        bufferStructure.addBufferModel(bufferModel, id,
            currentBufferViewModels);
        currentBufferViewModels.clear();
    }

    /**
     * Return the {@link BufferStructure} instance that was created with
     * this builder.
     * 
     * @return The {@link BufferStructure}
     */
    public BufferStructure build()
    {
        buildDefault();
        return bufferStructure;
    }
    
    /**
     * Internal method to finalize the elements of the current 
     * {@link BufferStructure}: This will compute the byte offsets and 
     * paddings, and create the actual {@link BufferModel#getBufferData()
     * buffer data}. This does a default construction. A method for
     * interleaved construction may be added later, in some form. 
     */
    private void buildDefault()
    {
        for (BufferModel bufferModel : bufferStructure.getBufferModels())
        {
            // The sequence of accessor data buffers and paddings that
            // eventually will be combined to create the buffer data
            List<ByteBuffer> bufferElements = new ArrayList<ByteBuffer>();
            
            List<BufferViewModel> bufferViewModels = 
                bufferStructure.getBufferViewModels(bufferModel);
            
            int accumulatedBufferBytes = 0;
            for (BufferViewModel bufferViewModel : bufferViewModels)
            {
                List<AccessorModel> accessorModels = 
                    bufferStructure.getAccessorModels(bufferViewModel);
                
                // Handle the padding that may have to be inserted into
                // the buffer, before the start of the buffer view: 
                // The buffer view has to be aligned to the least 
                // common multiple of the component size of all accessors
                int bufferViewAlignmnentBytes =
                    AccessorModels.computeAlignmentBytes(accessorModels);
                int paddingBytesForBuffer = Utils.computePadding(
                    accumulatedBufferBytes, bufferViewAlignmnentBytes);
                bufferStructure.addPaddingByteIndices(
                    bufferModel, accumulatedBufferBytes, paddingBytesForBuffer);
                accumulatedBufferBytes += paddingBytesForBuffer;
                bufferElements.add(ByteBuffer.allocate(paddingBytesForBuffer));
                
                DefaultBufferViewModel defaultBufferViewModel = 
                    getDefaultBufferViewModel(bufferViewModel);
                defaultBufferViewModel.setByteOffset(accumulatedBufferBytes);

                // Compute the byte stride based on the element sizes of
                // all accessors, and assign it to the accessors as well
                // as the buffer view
                int commonByteStride = 
                    AccessorModels.computeCommonByteStride(accessorModels);
                for (AccessorModel accessorModel : accessorModels)
                {
                    DefaultAccessorModel defaultAccessorModel =
                        getDefaultAccessorModel(accessorModel);
                    defaultAccessorModel.setByteStride(commonByteStride);
                }
                
                // The byte stride only has to be set in the buffer view
                // when more than one accessor refers to the buffer view.
                if (accessorModels.size() > 1)
                {
                    defaultBufferViewModel.setByteStride(commonByteStride);
                }
                
                int accumulatedBufferViewBytes = 0;
                for (AccessorModel accessorModel : accessorModels)
                {
                    // Handle the padding that may have to be inserted 
                    // into the buffer view before the start of the 
                    // accessor: 
                    // The accessor has to be aligned to its component
                    // size.  
                    int accessorAlignmentBytes = 
                        AccessorModels.computeAlignmentBytes(accessorModel);
                    int paddingBytesForBufferView = Utils.computePadding(
                        accumulatedBufferViewBytes, accessorAlignmentBytes);
                    if (paddingBytesForBufferView != 0)
                    {
                        // Note: The padding here should always be 0,
                        // because the buffer view was already padded                        
                        logger.warning("Inserting " + paddingBytesForBufferView
                            + " padding bytes for buffer view, due to accessor "
                            + accessorModel);
                    }
                    bufferStructure.addPaddingByteIndices(
                        bufferModel, accumulatedBufferBytes, 
                        paddingBytesForBufferView);
                    accumulatedBufferViewBytes += paddingBytesForBufferView;
                    
                    DefaultAccessorModel defaultAccessorModel =
                        getDefaultAccessorModel(accessorModel);
                    defaultAccessorModel.setByteOffset(
                        accumulatedBufferViewBytes);
                    
                    // Compute the byte buffer for the accessor data. This
                    // may have to be restructured by inserting padding bytes,
                    // if the element size of the accessor does not match the
                    // common byte stride
                    ByteBuffer rawAccessorByteBuffer =
                        rawAccessorModelByteBuffers.get(accessorModel);
                    ByteBuffer accessorByteBuffer = rawAccessorByteBuffer;
                    int elementSizeInBytes = 
                        accessorModel.getElementSizeInBytes();
                    if (elementSizeInBytes != commonByteStride) 
                    {
                        accessorByteBuffer = applyByteStride(
                            rawAccessorByteBuffer, elementSizeInBytes, 
                            commonByteStride);
                    }
                    
                    accumulatedBufferViewBytes += accessorByteBuffer.capacity();

                    accumulatedBufferBytes += paddingBytesForBufferView;
                    bufferElements.add(
                        ByteBuffer.allocate(paddingBytesForBufferView));
                    accumulatedBufferBytes += accessorByteBuffer.capacity();
                    bufferElements.add(accessorByteBuffer);

                }
                defaultBufferViewModel.setByteLength(
                    accumulatedBufferViewBytes);
            }
            
            validatePadding(bufferModel);

            // Create the buffer data, and assign it to the buffer
            ByteBuffer bufferData = Buffers.concat(bufferElements);
            DefaultBufferModel defaultBufferModel = 
                getDefaultBufferModel(bufferModel);
            defaultBufferModel.setBufferData(bufferData);
        }
    }
    
    /**
     * Apply the given byte stride to the given buffer and return the result
     * as a new buffer. The directness or byte order of the result are not
     * specified.<br>
     * <br>
     * This is supposed to be applied to accessor byte buffers. For example:
     * For an an accessor with 2D float elements, the old byte stride will 
     * be 2 * 4, and the old byte buffer may contain 24 bytes. Calling this
     * method with a new byte stride of 3 * 4 will cause padding bytes to
     * be inserted in the returned buffer: 
     * <pre><code>
     * oldByteBuffer: |X0|Y0|X1|Y1|X2|Y2|
     * newByteBuffer: |X0|Y0|..|X1|Y1|..|X2|Y2|..|
     * </code></pre>
     *  
     * @param oldByteBuffer The old byte buffer 
     * @param oldByteStride The old byte stride
     * @param newByteStride The new byte stride
     * @return The new byte buffer
     */
    private static ByteBuffer applyByteStride(
        ByteBuffer oldByteBuffer, int oldByteStride, int newByteStride)
    {
        int count = oldByteBuffer.capacity() / oldByteStride;
        ByteBuffer newByteBuffer = ByteBuffer.allocate(count * newByteStride);
        for (int i = 0; i < count; i++)
        {
            int srcPos = i * oldByteStride;
            int dstPos = i * newByteStride;
            Buffers.bufferCopy(oldByteBuffer, srcPos, 
                newByteBuffer, dstPos, oldByteStride);
        }
        return newByteBuffer;
    }
    
    
    /**
     * Perform a basic validation of the padding/alignment requirements
     * of the given {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     */
    private void validatePadding(BufferModel bufferModel)
    {
        List<BufferViewModel> bufferViewModels =
            bufferStructure.getBufferViewModels();

        for (BufferViewModel bufferViewModel : bufferViewModels)
        {
            List<AccessorModel> accessorModels =
                bufferStructure.getAccessorModels(bufferViewModel);
            for (AccessorModel accessorModel : accessorModels)
            {
                validatePadding(bufferViewModel, accessorModel);
            }
        }
    }

    /**
     * Perform a basic validation of the padding/alignment requirements
     * of the given {@link BufferViewModel} and {@link AccessorModel}
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @param accessorModel The {@link AccessorModel}
     */
    private void validatePadding(
        BufferViewModel bufferViewModel, AccessorModel accessorModel)
    {
        int alignmentBytes =
            AccessorModels.computeAlignmentBytes(accessorModel);

        int bufferViewByteOffset = bufferViewModel.getByteOffset();
        int accessorByteOffset = accessorModel.getByteOffset();
        int totalByteOffset = bufferViewByteOffset + accessorByteOffset;
        if (accessorByteOffset % alignmentBytes != 0)
        {
            logger.severe("Error: accessor.byteOffset is " + accessorByteOffset
                + " for alignment " + alignmentBytes + " in accessor "
                + accessorModel);
        }
        if (totalByteOffset % alignmentBytes != 0)
        {
            logger.severe("Error: bufferView.byteOffset+accessor.byteOffset is "
                + totalByteOffset + " for alignment " + alignmentBytes
                + " in accessor " + accessorModel);
        }
    }    
}
