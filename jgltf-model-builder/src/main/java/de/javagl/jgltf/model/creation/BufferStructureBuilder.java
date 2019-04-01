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
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class for creating {@link BufferStructure} instances.<br>
 * <br>
 * The class offers methods to create the accessors, buffer views and buffers
 * that are part of a glTF asset. It allows to create these elements 
 * hierarchically: A sequence of accessors may be created, which are then
 * combined into a buffer view. The sequence of buffer views that are created
 * can then be combined into a buffer: 
 * <pre><code>
 * BufferStructureBuilder b = new BufferStructureBuilder();
 * 
 * b.createAccessorModel("indices accessor", indices, "SCALAR");
 * b.createArrayElementBufferViewModel("indices bufferView");
 *       
 * b.createAccessorModel("positions accessor", positions, "VEC3");
 * b.createAccessorModel("normals accessor", normals, "VEC3");
 * b.createArrayBufferViewModel("attributes bufferView");
 *       
 * b.createBufferModel("geometry buffer", "simpleTriangle.bin");
 * BufferStructure bufferStructure = b.build();
 * </code></pre>
 * <br>
 * <b>Note:</b> Although the methods for creating the models always return
 * the respective model, e.g. an <code>AccessorModel</code> instance, these
 * instances will not be fully initialized until the {@link #build()} 
 * method is called.
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
        this.currentBufferViewModels = new ArrayList<DefaultBufferViewModel>();
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
        DefaultAccessorModel accessorModel = AccessorModels.create(
            componentType, type, byteBuffer);
        addAccessorModel(idPrefix, accessorModel);
        return accessorModel;
    }
    
    /**
     * Add the given {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param idPrefix The ID prefix of the {@link AccessorModel}
     * @param accessorModel The {@link AccessorModel}
     */
    public void addAccessorModel(
        String idPrefix, DefaultAccessorModel accessorModel)
    {
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
     * Create a {@link BufferModel} in the {@link BufferStructure} that 
     * is currently being built. 
     * 
     * @param idPrefix The ID prefix for the {@link BufferModel}
     * @param uri The {@link BufferModel#getUri()}
     * @return The {@link BufferModel} 
     */
    public BufferModel createBufferModel(String idPrefix, String uri) 
    {
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
        List<DefaultBufferModel> bufferModels = 
            bufferStructure.getBufferModels();
        for (DefaultBufferModel bufferModel : bufferModels)
        {
            processBufferModel(bufferModel);
        }
    }

    /**
     * Process the given {@link BufferModel} during the build process.
     * This will compute the properties of the model, as well as the
     * properties of its {@link AccessorModel} and {@link BufferViewModel}
     * instances, that can only be determined when the buffer is about
     * to be finalized (e.g. the byte stride, byte offsets and lengths). 
     * 
     * @param bufferModel The {@link BufferModel}
     */
    private void processBufferModel(DefaultBufferModel bufferModel)
    {
        // Compute the mapping from AccessorModel instances to the
        // byte buffers that contain their data, as given when they have 
        // been added. These byte buffers may have to be combined, reordered 
        // or padded in order to create the actual buffer data
        Map<DefaultAccessorModel, ByteBuffer> rawAccessorModelByteBuffers =
            new LinkedHashMap<DefaultAccessorModel, ByteBuffer>();
        for (DefaultAccessorModel accessorModel : 
            bufferStructure.getAccessorModels())
        {
            AccessorData accessorData = accessorModel.getAccessorData();
            ByteBuffer byteBuffer = accessorData.createByteBuffer();
            rawAccessorModelByteBuffers.put(accessorModel, byteBuffer);
        }
        
        // The sequence of accessor data buffers and paddings that
        // eventually will be combined to create the buffer data
        List<ByteBuffer> bufferElements = new ArrayList<ByteBuffer>();
        
        List<DefaultBufferViewModel> bufferViewModels = 
            bufferStructure.getBufferViewModels(bufferModel);
        
        int accumulatedBufferBytes = 0;
        for (DefaultBufferViewModel bufferViewModel : bufferViewModels)
        {
            List<DefaultAccessorModel> accessorModels = 
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
            
            bufferViewModel.setByteOffset(accumulatedBufferBytes);

            Integer commonByteStride = null;
            Integer target = bufferViewModel.getTarget();
            
            boolean targetIsElementArrayBuffer = 
                Objects.equals(GltfConstants.GL_ELEMENT_ARRAY_BUFFER, target);
            if (!targetIsElementArrayBuffer && accessorModels.size() > 1)
            {
                // Compute the byte stride based on the element sizes of
                // all accessors, and assign it to the accessors as well
                // as the buffer view
                commonByteStride = 
                    AccessorModels.computeCommonByteStride(accessorModels);
                for (DefaultAccessorModel accessorModel : accessorModels)
                {
                    accessorModel.setByteStride(commonByteStride);
                }
                
                // The byte stride only has to be set in the buffer view
                // when more than one accessor refers to the buffer view.
                bufferViewModel.setByteStride(commonByteStride);
            }
            
            
            int accumulatedBufferViewBytes = 0;
            for (DefaultAccessorModel accessorModel : accessorModels)
            {
                // Handle the padding that may have to be inserted 
                // into the buffer view before the start of the 
                // accessor: 
                // The accessor has to be aligned to its component size.  
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
                
                accessorModel.setByteOffset(
                    accumulatedBufferViewBytes);
                
                // Compute the byte buffer for the accessor data. This
                // may have to be restructured by inserting padding bytes,
                // if the element size of the accessor does not match the
                // common byte stride
                ByteBuffer rawAccessorByteBuffer =
                    rawAccessorModelByteBuffers.get(accessorModel);
                ByteBuffer accessorByteBuffer = rawAccessorByteBuffer;
                
                if (commonByteStride != null)
                {
                    int elementSizeInBytes = 
                        accessorModel.getElementSizeInBytes();
                    if (elementSizeInBytes != commonByteStride) 
                    {
                        accessorByteBuffer = applyByteStride(
                            rawAccessorByteBuffer, elementSizeInBytes, 
                            commonByteStride);
                    }
                } 
                else
                {
                    int elementSize = accessorModel.getElementSizeInBytes();
                    accessorModel.setByteStride(elementSize);
                }
                
                accumulatedBufferViewBytes += accessorByteBuffer.capacity();

                accumulatedBufferBytes += paddingBytesForBufferView;
                bufferElements.add(
                    ByteBuffer.allocate(paddingBytesForBufferView));
                accumulatedBufferBytes += accessorByteBuffer.capacity();
                bufferElements.add(accessorByteBuffer);

            }
            bufferViewModel.setByteLength(
                accumulatedBufferViewBytes);
        }
        
        validatePadding(bufferModel);

        // Create the buffer data, and assign it to the buffer
        ByteBuffer bufferData = Buffers.concat(bufferElements);
        bufferModel.setBufferData(bufferData);
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
        List<DefaultBufferViewModel> bufferViewModels =
            bufferStructure.getBufferViewModels();

        for (BufferViewModel bufferViewModel : bufferViewModels)
        {
            List<DefaultAccessorModel> accessorModels =
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
