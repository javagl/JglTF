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

import java.util.List;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;

/**
 * Interface for classes that can collect the data from elements of a 
 * {@link GltfModel} and arrange it into {@link AccessorModel},
 * {@link BufferViewModel} and {@link BufferModel} instances, with
 * the exact strategy depending on the implementation.<br>
 * <br>
 * This interface is only supposed to be used internally, and not part of
 * the public API!
 */
public interface BufferBuilderStrategy
{
    /**
     * Process all {@link AccessorModel} instances that are referred to
     * by the given {@link GltfModel}
     * 
     * @param gltfModel {@link GltfModel}
     */
    void process(GltfModel gltfModel);

    /**
     * Returns a list containing all {@link AccessorModel} instances that
     * have been created.
     * 
     * @return The {@link AccessorModel} instances
     */
    List<DefaultAccessorModel> getAccessorModels();

    /**
     * Returns a list containing all {@link BufferViewModel} instances that
     * have been created.
     * 
     * @return The {@link BufferViewModel} instances
     */
    List<DefaultBufferViewModel> getBufferViewModels();

    /**
     * Returns a list containing all {@link BufferModel} instances that
     * have been created.
     * 
     * @return The {@link BufferModel} instances
     */
    List<DefaultBufferModel> getBufferModels();

    /**
     * Update the given image model based on the structures that have 
     * been built by this class.<br>
     * <br>
     * This means that the {@link ImageModel#getBufferViewModel()} and
     * the {@link ImageModel#getUri()} will be updated depending on whether
     * the image was stored in a buffer view or not:<br>
     * <br>
     * When the image data is stored in a buffer view, then it will set
     * the corresponding buffer view for the given model, and set its
     * URI to <code>null</code>.<br>
     * <br>
     * Otherwise, it will set the buffer view to <code>null</code>, and
     * the URI to either its original value, or an auto-generated or
     * disambiguated URI.
     * 
     * @param imageModel The {@link ImageModel} to update
     */
    void validateImageModel(DefaultImageModel imageModel);
}
