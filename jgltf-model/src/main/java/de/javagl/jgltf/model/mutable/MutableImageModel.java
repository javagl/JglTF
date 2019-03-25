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
package de.javagl.jgltf.model.mutable;

import java.nio.ByteBuffer;

import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ImageModel;

/**
 * Interface for an {@link ImageModel} that can be modified
 */
public interface MutableImageModel extends ImageModel
{
    /**
     * Set the URI of the image data (optional)
     * 
     * @param uri The URI
     */
    void setUri(String uri);
    
    /**
     * Set the MIME type of the image data that is contained in 
     * the buffer view
     * 
     * @param mimeType The MIME type
     */
    void setMimeType(String mimeType);
    
    /**
     * Set the (optional) {@link BufferViewModel} that contains
     * the image data
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     */
    void setBufferViewModel(BufferViewModel bufferViewModel);
    
    /**
     * Set the actual image data. If the given data is <code>null</code>,
     * then calls to {@link #getImageData()} will return the data of the
     * {@link BufferViewModel} that was set with {@link #setBufferViewModel}
     * 
     * @param imageData The image data
     */
    void setImageData(ByteBuffer imageData);
}
