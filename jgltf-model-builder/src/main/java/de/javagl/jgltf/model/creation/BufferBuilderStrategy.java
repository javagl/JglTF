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

import java.util.Collection;
import java.util.List;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultBufferModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;

/**
 * Interface for classes that can collect the data from elements of a 
 * {@link GltfModel} and arrange it into {@link AccessorModel},
 * {@link BufferViewModel} and {@link BufferModel} instances, with
 * the exact strategy depending on the implementation. 
 */
interface BufferBuilderStrategy
{
    /**
     * Process all {@link AccessorModel} instances that are referred to
     * by the given {@link MeshModel} instances
     * 
     * @param meshModels The {@link MeshModel} instances
     */
    void processMeshModels(
        Collection<? extends DefaultMeshModel> meshModels);

    /**
     * Process all data blocks that are referred to
     * by the given {@link ImageModel} instances
     * 
     * @param imageModels The {@link ImageModel} instances
     */
    void processImageModels(
        Collection<? extends DefaultImageModel> imageModels);

    /**
     * Process all {@link AccessorModel} instances that are referred to
     * by the given {@link AnimationModel} instances
     * 
     * @param animationModels The {@link AnimationModel} instances
     */
    void processAnimationModels(
        Collection<? extends DefaultAnimationModel> animationModels);

    /**
     * Process all {@link AccessorModel} instances that are referred to
     * by the given {@link SkinModel} instances
     * 
     * @param skinModels The {@link SkinModel} instances
     */
    void processSkinModels(
        Collection<? extends DefaultSkinModel> skinModels);
    
    /**
     * Finish the creation of the buffer structure, so that the results
     * may be obtained with {@link #getAccessorModels()}, 
     * {@link #getBufferViewModels()} and {@link #getBufferModels()}
     */
    void finish();
    
    /**
     * Returns a list containing all {@link AccessorModel} instances that
     * have been created. This method may only be called after the 
     * {@link #finish()} method was called.
     * 
     * @return The {@link AccessorModel} instances
     */
    List<DefaultAccessorModel> getAccessorModels();

    /**
     * Returns a list containing all {@link BufferViewModel} instances that
     * have been created. This method may only be called after the 
     * {@link #finish()} method was called.
     * 
     * @return The {@link BufferViewModel} instances
     */
    List<DefaultBufferViewModel> getBufferViewModels();

    /**
     * Returns a list containing all {@link BufferModel} instances that
     * have been created. This method may only be called after the 
     * {@link #finish()} method was called.
     * 
     * @return The {@link BufferModel} instances
     */
    List<DefaultBufferModel> getBufferModels();
}
