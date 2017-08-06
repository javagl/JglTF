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
package de.javagl.jgltf.model.impl;

import java.nio.ByteBuffer;

import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Implementation of a {@link BufferViewModel}
 */
public final class DefaultBufferViewModel implements BufferViewModel
{
    /**
     * The {@link BufferModel} for this model
     */
    private BufferModel bufferModel;
    
    /**
     * The byte offset
     */
    private final int byteOffset;
    
    /**
     * The byte length
     */
    private final int byteLength;
    
    /**
     * The byte stride
     */
    private final Integer byteStride;
    
    /**
     * The optional target
     */
    private final Integer target;
    
    /**
     * Creates a new instance
     * 
     * @param byteOffset The byte offset
     * @param byteLength The byte length
     * @param byteStride The optional byte stride
     * @param target The optional target
     */
    public DefaultBufferViewModel(
        int byteOffset, int byteLength, Integer byteStride, Integer target)
    {
        this.byteOffset = byteOffset;
        this.byteLength = byteLength;
        this.byteStride = byteStride;
        this.target = target;
    }
    
    /**
     * Set the {@link BufferModel} for this model
     * 
     * @param bufferModel The {@link BufferModel}
     */
    public void setBufferModel(BufferModel bufferModel)
    {
        this.bufferModel = bufferModel;
    }
    
    @Override
    public ByteBuffer getBufferViewData()
    {
        ByteBuffer bufferData = bufferModel.getBufferData();
        ByteBuffer bufferViewData = 
            Buffers.createSlice(bufferData, getByteOffset(), getByteLength());
        return bufferViewData;
    }

    @Override
    public BufferModel getBufferModel()
    {
        return bufferModel;
    }

    @Override
    public int getByteOffset()
    {
        return byteOffset;
    }

    @Override
    public int getByteLength()
    {
        return byteLength;
    }

    @Override
    public Integer getByteStride()
    {
        return byteStride;
    }

    @Override
    public Integer getTarget()
    {
        return target;
    }

}
