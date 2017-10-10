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

import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Implementation of a {@link ImageModel}
 */
public final class DefaultImageModel extends AbstractNamedModelElement
    implements ImageModel
{
    /**
     * The URI of the image
     */
    private String uri;
    
    /**
     * The MIME type of the image data in the buffer view model
     */
    private final String mimeType;
    
    /**
     * The {@link BufferViewModel}
     */
    private BufferViewModel bufferViewModel;
    
    /**
     * The image data
     */
    private ByteBuffer imageData;
    
    /**
     * Creates a new instance
     * 
     * @param mimeType The MIME type
     * @param bufferViewModel The {@link BufferViewModel}
     */
    public DefaultImageModel(
        String mimeType, BufferViewModel bufferViewModel)
    {
        this.mimeType = mimeType;
        this.bufferViewModel = bufferViewModel;
    }
    
    /**
     * Set the URI string of this image
     * 
     * @param uri The URI
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    /**
     * Set the {@link BufferViewModel} that this image refers to
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     */
    public void setBufferViewModel(BufferViewModel bufferViewModel)
    {
        this.bufferViewModel = bufferViewModel;
    }
    
    /**
     * Set the data of this image. If the given data is <code>null</code>,
     * then calls to {@link #getImageData()} will return the data of the
     * {@link BufferViewModel} that was set with {@link #setBufferViewModel}
     * 
     * @param imageData The image data
     */
    public void setImageData(ByteBuffer imageData)
    {
        this.imageData = imageData;
    }
    
    @Override
    public String getUri()
    {
        return uri;
    }
    
    @Override
    public String getMimeType()
    {
        return mimeType;
    }
    
    @Override
    public BufferViewModel getBufferViewModel()
    {
        return bufferViewModel;
    }
    
    @Override
    public ByteBuffer getImageData()
    {
        if (imageData == null)
        {
            return bufferViewModel.getBufferViewData();
        }
        return Buffers.createSlice(imageData);
    }

    
}
