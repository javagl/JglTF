/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.extensions;

import java.nio.ByteBuffer;
import java.util.List;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;

/**
 * Highly preliminary interface for classes that want to be informed about
 * extensions that are preprocessing a glTF model, using the
 * {@link ExtensionHandler#preprocess} method.
 * 
 * For now, this interface is tailored for the case of extensions that
 * provide the data of accessors, meaning that the accessors do not have
 * an associated buffer view (i.e. Draco compression).
 */
public interface ExtensionProcessing
{
    /**
     * Inform this instance that the given accessor models do not need an
     * associated buffer view, but their data is encoded in the given
     * buffer view.
     * 
     * @param accessorModels The accessor model
     * @param bufferViewModel The buffer view model
     * @param bufferViewData The buffer view data
     */
    void acceptAccessorEncoding(List<? extends AccessorModel> accessorModels,
        DefaultBufferViewModel bufferViewModel, ByteBuffer bufferViewData);

}
