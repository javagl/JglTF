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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorData;
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
 * Internal class for processing a {@link BufferStructure} to generate 
 * the actual buffer data that will eventually be passed to 
 * {@link DefaultBufferModel#setBufferData(ByteBuffer)}.
 * 
 */
class BufferStructureProcessor
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BufferStructureProcessor.class.getName());
    
    /**
     * The {@link BufferStructure}
     */
    private final BufferStructure bufferStructure;

    /**
     * The mapping from buffer view objects that do not have associated 
     * accessors to their data
     */
    private final Map<DefaultBufferViewModel, ByteBuffer> 
        standaloneBufferViewDataMap;
    
    /**
     * Creates a new instance 
     * 
     * @param bufferStructure The {@link BufferStructure}
     * @param standaloneBufferViewDataMap The mapping from buffer view objects
     * that to not have associated accessors, to their data
     */
    BufferStructureProcessor(BufferStructure bufferStructure, 
        Map<DefaultBufferViewModel, ByteBuffer> standaloneBufferViewDataMap)
    {
        this.bufferStructure = Objects.requireNonNull(
            bufferStructure, "The bufferStructure may not be null");
        this.standaloneBufferViewDataMap = Objects.requireNonNull(
            standaloneBufferViewDataMap, 
            "The standaloneBufferViewDataMap may not be null");
    }
    
    /**
     * Internal method to finalize the elements of the current 
     * {@link BufferStructure}: This will compute the byte offsets and 
     * paddings, and create the actual {@link BufferModel#getBufferData()
     * buffer data}. This does a default construction. A method for
     * interleaved construction may be added later, in some form. 
     */
    void process()
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
        // byte buffers that contain their data in compact form.
        // These byte buffers may have to be combined, reordered 
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
                Alignment.computeAlignmentBytes(accessorModels);
            int paddingBytesForBuffer = Alignment.computePadding(
                accumulatedBufferBytes, bufferViewAlignmnentBytes);
            
            if (paddingBytesForBuffer > 0)
            {
                bufferStructure.addPaddingByteIndices(
                    bufferModel, accumulatedBufferBytes, paddingBytesForBuffer);
                accumulatedBufferBytes += paddingBytesForBuffer;
                bufferElements.add(ByteBuffer.allocate(paddingBytesForBuffer));
            }
            
            bufferViewModel.setByteOffset(accumulatedBufferBytes);

            Integer commonByteStride = null;
            Integer target = bufferViewModel.getTarget();
            
            // When the target is a vertex attribute, then the elements
            // of the accessor must be aligned to a multiple of 4. 
            boolean targetIsVertexAttribute = Objects.equals(
                GltfConstants.GL_ARRAY_BUFFER, target);
            if (targetIsVertexAttribute)
            {
                commonByteStride = 
                    Alignment.computeCommonVertexAttributeByteStride(
                        accessorModels);
                for (DefaultAccessorModel accessorModel : accessorModels)
                {
                    int oldByteStride = accessorModel.getByteStride();
                    if (oldByteStride != commonByteStride)
                    {
                        accessorModel.setByteStride(commonByteStride);
                        bufferViewModel.setByteStride(commonByteStride);
                    }
                }
                
                // When there are multiple vertex attribute accessors that refer 
                // to the same buffer view, then the byte stride must be defined
                if (accessorModels.size() > 1)
                {
                    bufferViewModel.setByteStride(commonByteStride);
                }
            }
            
            // Handle buffer view models that do not have associated
            // accessors (used for images and in extensions)
            if (accessorModels.isEmpty())
            {
                ByteBuffer bufferViewData = 
                    standaloneBufferViewDataMap.get(bufferViewModel);
                bufferViewModel.setByteLength(bufferViewData.capacity());
                accumulatedBufferBytes += bufferViewData.capacity();
                bufferElements.add(bufferViewData);
            } 
            else
            {
                int accumulatedBufferViewBytes = 0;
                for (DefaultAccessorModel accessorModel : accessorModels)
                {
                    // Handle the padding that may have to be inserted 
                    // into the buffer view before the start of the 
                    // accessor: 
                    // The accessor has to be aligned to its component size.  
                    int accessorAlignmentBytes = 
                        Alignment.computeAlignmentBytes(accessorModel);
                    int paddingBytesForBufferView = Alignment.computePadding(
                        accumulatedBufferViewBytes, accessorAlignmentBytes);
                    if (paddingBytesForBufferView > 0)
                    {
                        // Note: The padding here should always be 0,
                        // because the buffer view was already padded                        
                        logger.warning("Inserting " + paddingBytesForBufferView
                            + " padding bytes for buffer view, due to accessor "
                            + accessorModel);
                        
                        bufferStructure.addPaddingByteIndices(
                            bufferModel, accumulatedBufferBytes, 
                            paddingBytesForBufferView);
                        accumulatedBufferViewBytes += paddingBytesForBufferView;
                    }
                    
                    accessorModel.setByteOffset(
                        accumulatedBufferViewBytes);
                    
                    int targetByteStride =  
                        accessorModel.getPaddedElementSizeInBytes(); 
                    if (commonByteStride == null)
                    {
                        accessorModel.setByteStride(targetByteStride);
                    } 
                    else 
                    {
                        targetByteStride = commonByteStride;
                    }
                    
                    // Compute the byte buffer for the accessor data. This
                    // may have to be restructured by inserting padding bytes
                    ByteBuffer rawAccessorByteBuffer =
                        rawAccessorModelByteBuffers.get(accessorModel);
                    ByteBuffer accessorByteBuffer = rawAccessorByteBuffer;
                    
                    int count = accessorModel.getCount();
                    ElementType elementType = accessorModel.getElementType();
                    int componentType = accessorModel.getComponentType();
                    accessorByteBuffer = applyPadding(
                        rawAccessorByteBuffer, count, elementType, 
                        componentType, targetByteStride);
                    
                    accumulatedBufferViewBytes += accessorByteBuffer.capacity();
    
                    if (paddingBytesForBufferView > 0)
                    {
                        // Note: The padding here should always be 0,
                        // because the buffer view was already padded                        
                        logger.warning("Inserting " + paddingBytesForBufferView
                            + " padding bytes for buffer view, due to accessor "
                            + accessorModel);
                        
                        accumulatedBufferBytes += paddingBytesForBufferView;
                        bufferElements.add(
                            ByteBuffer.allocate(paddingBytesForBufferView));
                    }
                    accumulatedBufferBytes += accessorByteBuffer.capacity();
                    bufferElements.add(accessorByteBuffer);
    
                }
                bufferViewModel.setByteLength(
                    accumulatedBufferViewBytes);
            }
        }
        
        validatePadding(bufferModel);

        // Create the buffer data, and assign it to the buffer
        ByteBuffer bufferData = Buffers.concat(bufferElements);
        bufferModel.setBufferData(bufferData);
    }
    
    /**
     * Read the data from the byte buffer that was created with 
     * {@link AccessorData#createByteBuffer()} and that contains
     * the data in packed form, and write it into a new buffer,
     * with the padding bytes that are required according to the
     * specification.
     * 
     * @param packedByteBuffer The packed byte buffer
     * @param count The number of elements in the accessor
     * @param elementType The accessor element type
     * @param componentType The component type
     * @param byteStride The target byte stride (that must at least
     * be equal to the {@link ElementType#getByteStride(int)}, but may be 
     * larger)
     * @return The buffer, with the padding applied
     */
    private static ByteBuffer applyPadding(
        ByteBuffer packedByteBuffer, int count, ElementType elementType, 
        int componentType, int byteStride)
    {
        ByteBuffer newByteBuffer = Buffers.create(count * byteStride);
        int numComponents = elementType.getNumComponents();
        int numBytesPerComponent = 
            Accessors.getNumBytesForAccessorComponentType(componentType);
        int sourceIndex = 0;
        int targetIndex = 0;
        int padddingForByteStride = 
            byteStride - elementType.getByteStride(componentType);
        for (int i = 0; i < count; i++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                for (int b = 0; b < numBytesPerComponent; b++)
                {
                    byte value = packedByteBuffer.get(sourceIndex);
                    newByteBuffer.put(targetIndex, value);
                    sourceIndex++;
                    targetIndex++;
                }
                int padding = computePaddingBytesAfterComponent(elementType,
                    componentType, c);
                targetIndex += padding;
            }
            targetIndex += padddingForByteStride;
        }
        return newByteBuffer;
    }
    
    /**
     * Compute the number of padding bytes that have to be inserted after the
     * specified component.
     * 
     * https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html#data-alignment
     * 
     * @param elementType The element type
     * @param componentType The component type
     * @param componentIndex The component index
     * @return The padding bytes
     */
    private static int computePaddingBytesAfterComponent(
        ElementType elementType, int componentType, int componentIndex)
    {
        int n = Accessors.getNumBytesForAccessorComponentType(componentType);
        if (n == 1)
        {
            if (elementType == ElementType.MAT2)
            {
                if (componentIndex == 1)
                {
                    return 2;
                }
                if (componentIndex == 3)
                {
                    return 2;
                }
            }
            if (elementType == ElementType.MAT3)
            {
                if (componentIndex == 2)
                {
                    return 1;
                }
                if (componentIndex == 5)
                {
                    return 1;
                }
                if (componentIndex == 9)
                {
                    return 1;
                }
            }
        }
        if (n == 2)
        {
            if (elementType == ElementType.MAT3)
            {
                if (componentIndex == 2)
                {
                    return 2;
                }
                if (componentIndex == 5)
                {
                    return 2;
                }
                if (componentIndex == 9)
                {
                    return 2;
                }
            }
        }
        return 0;
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
        int alignmentBytes = Alignment.computeAlignmentBytes(accessorModel);

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
