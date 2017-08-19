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
import java.util.List;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorDatas;
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
        logger.severe(
            "AccessorModel is not a DefaultAccessorModel: " + accessorModel);
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
     * @return The instance that has been created and added to the
     * {@link BufferStructure}
     */
    public AccessorModel createAccessorModel(
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
        return createAccessorModel(id, componentType, type, byteBuffer);
    }

    /**
     * Create an {@link AccessorModel} in the {@link BufferStructure} that 
     * is currently being built.
     * 
     * @param id The ID for the {@link AccessorModel} 
     * @param data The actual data
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @return The instance that has been created and added to the
     * {@link BufferStructure}
     */
    public AccessorModel createAccessorModel(
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
        return createAccessorModel(id, componentType, type, byteBuffer);
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
     * @return The instance that has been created and added to the
     * {@link BufferStructure}
     */
    public AccessorModel createAccessorModel(String id,
        int componentType, String type, ByteBuffer byteBuffer)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        int numBytesPerComponent = 
            Accessors.getNumBytesForAccessorComponentType(componentType);
        int numBytesPerElement = numComponents * numBytesPerComponent;
        int byteStride = numBytesPerElement;
        int count = byteBuffer.capacity() / numBytesPerElement;
        DefaultAccessorModel accessorModel = new DefaultAccessorModel(
            componentType, count, elementType, byteStride);
        accessorModel.setAccessorData(
            AccessorDatas.create(accessorModel, byteBuffer));
        bufferStructure.addAccessorModel(accessorModel, id, byteBuffer);
        currentAccessorModels.add(accessorModel);
        
        return accessorModel;
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. The {@link BufferViewModel#getTarget()
     * target} will be the GL constant for <code>GL_ARRAY_BUFFER</code>.
     * 
     * @param id The ID for the {@link BufferViewModel}
     * @return The instance that has been created and added to the
     * {@link BufferStructure}
     */
    public BufferViewModel createArrayBufferViewModel(String id)
    {
        return createBufferViewModel(
            id, GltfConstants.GL_ARRAY_BUFFER);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. The {@link BufferViewModel#getTarget()
     * target} will be the GL constant for <code>GL_ELEMENT_ARRAY_BUFFER</code>.
     * 
     * @param id The ID for the {@link BufferViewModel}
     * @return The instance that has been created and added to the
     * {@link BufferStructure}
     */
    public BufferViewModel createArrayElementBufferViewModel(String id)
    {
        return createBufferViewModel(
            id, GltfConstants.GL_ELEMENT_ARRAY_BUFFER);
    }
    
    /**
     * Create a {@link BufferViewModel} in the {@link BufferStructure} that 
     * is currently being built. 
     * 
     * @param id The ID for the {@link BufferViewModel}
     * @param target The {@link BufferViewModel#getTarget()}
     * @return The instance that has been created and added to the
     * {@link BufferStructure}
     */
    public BufferViewModel createBufferViewModel(String id, Integer target)
    {
        Integer byteStride = null;
        DefaultBufferViewModel bufferViewModel = new DefaultBufferViewModel(
            byteStride, target);
        for (DefaultAccessorModel accessorModel : currentAccessorModels)
        {
            accessorModel.setBufferViewModel(bufferViewModel);
        }
        bufferStructure.addBufferViewModel(
            bufferViewModel, id, currentAccessorModels);
        currentBufferViewModels.add(bufferViewModel);
        currentAccessorModels.clear();
        
        return bufferViewModel;
    }

    
    
    /**
     * Create a {@link BufferModel} in the {@link BufferStructure} that 
     * is currently being built. 
     * 
     * @param id The ID for the {@link BufferModel}
     * @param uri The {@link BufferModel#getUri()}
     * @return The instance that has been created and added to the
     * {@link BufferStructure}
     */
    public DefaultBufferModel createBufferModel(String id, String uri) 
    {
        DefaultBufferModel bufferModel = new DefaultBufferModel(uri);
        int accumulatedBufferBytes = 0;
        for (DefaultBufferViewModel bufferViewModel : currentBufferViewModels)
        {
            bufferViewModel.setBufferModel(bufferModel);
            
            List<AccessorModel> accessorModels = 
                bufferStructure.getAccessorModels(bufferViewModel);
            
            int bufferViewAlignmnentBytes =
                AccessorModels.computeAlignmentBytes(accessorModels);

            int paddingBytesForBuffer = Utils.computePadding(
                accumulatedBufferBytes, bufferViewAlignmnentBytes);
            for (int i=0; i<paddingBytesForBuffer; i++)
            {
                bufferStructure.addPaddingByteIndex(
                    bufferModel, accumulatedBufferBytes + i);
            }
            accumulatedBufferBytes += paddingBytesForBuffer;
            
            bufferViewModel.setByteOffset(accumulatedBufferBytes);

            int accumulatedBufferViewBytes = 0;
            for (AccessorModel accessorModel : accessorModels)
            {
                int accessorAlignmentBytes = 
                    AccessorModels.computeAlignmentBytes(accessorModel);
                int paddingBytesForBufferView = Utils.computePadding(
                    accumulatedBufferViewBytes, accessorAlignmentBytes);

                for (int i=0; i<paddingBytesForBufferView; i++)
                {
                    bufferStructure.addPaddingByteIndex(
                        bufferModel, accumulatedBufferBytes + i);
                }
                
                accumulatedBufferViewBytes += paddingBytesForBufferView;
                
                DefaultAccessorModel defaultAccessorModel =
                    getDefaultAccessorModel(accessorModel);
                defaultAccessorModel.setByteOffset(accumulatedBufferViewBytes);
                
                ByteBuffer accessorByteBuffer =
                    bufferStructure.getAccessorByteBuffer(accessorModel);
                accumulatedBufferViewBytes += accessorByteBuffer.capacity();

                accumulatedBufferBytes += paddingBytesForBufferView;
                accumulatedBufferBytes += accessorByteBuffer.capacity();
            }
            
            bufferViewModel.setByteLength(accumulatedBufferViewBytes);
        }
        
        validatePadding(bufferModel);
        
        bufferStructure.addBufferModel(bufferModel, id,
            currentBufferViewModels);
        currentBufferViewModels.clear();
        
        ByteBuffer byteBuffer = generateByteBuffer(
            bufferModel, accumulatedBufferBytes);
        bufferModel.setBufferData(byteBuffer);
        
        return bufferModel;
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
    
    /**
     * Generate the byte buffer that contains all the data for the given
     * {@link BufferModel}
     * 
     * @param bufferModel The {@link BufferModel}
     * @param accumulatedBytes The total number of bytes that the byte buffer
     * will have 
     * @return The byte buffer
     */
    private ByteBuffer generateByteBuffer(
        BufferModel bufferModel, int accumulatedBytes)
    {
        ByteBuffer bufferByteBuffer = Buffers.create(accumulatedBytes);
        
        List<BufferViewModel> bufferViewModels = 
            bufferStructure.getBufferViewModels(bufferModel);
        
        for (BufferViewModel bufferViewModel : bufferViewModels)
        {
            int bufferViewByteOffset = bufferViewModel.getByteOffset();
            
            List<AccessorModel> accessorModels = 
                bufferStructure.getAccessorModels(bufferViewModel);
            
            for (AccessorModel accessorModel : accessorModels)
            {
                ByteBuffer accessorByteBuffer = 
                    bufferStructure.getAccessorByteBuffer(accessorModel);
                int accessorByteOffset = accessorModel.getByteOffset();
                
                int totalByteOffset = bufferViewByteOffset + accessorByteOffset;
                bufferByteBuffer.position(totalByteOffset);
                
                bufferByteBuffer.put(accessorByteBuffer.slice());
            }
        }
        bufferByteBuffer.position(0);
        return bufferByteBuffer;
    }

    /**
     * Return the {@link BufferStructure} instance that was created with
     * this builder.
     * 
     * @return The {@link BufferStructure}
     */
    public BufferStructure build()
    {
        return bufferStructure;
    }
    
}
