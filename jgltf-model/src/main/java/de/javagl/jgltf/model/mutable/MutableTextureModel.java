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

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.TextureModel;

/**
 * Interface for a {@link TextureModel} that can be modified
 */
public interface MutableTextureModel extends TextureModel
{
    /**
     * Set the magnification filter constant
     * 
     * @param magFilter The constant
     */
    void setMagFilter(Integer magFilter);

    /**
     * Set the minification filter constant
     * 
     * @param minFilter The constant
     */
    void setMinFilter(Integer minFilter);

    /**
     * Set the wrapping constant for S-direction
     * 
     * @param wrapS The constant
     */
    void setWrapS(Integer wrapS);

    /**
     * Set the wrapping constant for T-direction
     * 
     * @param wrapT The constant
     */
    void setWrapT(Integer wrapT);
    
    /**
     * Set the {@link ImageModel} that backs this texture
     * 
     * @param imageModel The {@link ImageModel}
     */
    void setImageModel(ImageModel imageModel);
}