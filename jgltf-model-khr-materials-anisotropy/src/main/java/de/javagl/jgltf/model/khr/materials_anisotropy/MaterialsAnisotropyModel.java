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
package de.javagl.jgltf.model.khr.materials_anisotropy;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_anisotropy</code> extension
 * that is found in the material of a glTF
 */
public interface MaterialsAnisotropyModel extends ModelElement, ExtensionModel
{
    /**
     * The anisotropy strength. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param anisotropyStrength The anisotropy strength to set
     */
    void setAnisotropyStrength(Double anisotropyStrength);

    /**
     * The anisotropy strength. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The anisotropy strength
     */
    Double getAnisotropyStrength();

    /**
     * The rotation of the anisotropy. (optional)<br> 
     * Default: 0.0 
     * 
     * @param anisotropyRotation The anisotropy rotation to set
     */
    void setAnisotropyRotation(Double anisotropyRotation);

    /**
     * The rotation of the anisotropy. (optional)<br> 
     * Default: 0.0 
     * 
     * @return The anisotropy rotation
     */
    Double getAnisotropyRotation();

    /**
     * The anisotropy texture. (optional) 
     * 
     * @param anisotropyTextureInfoModel The The anisotropy texture info model
     */
    void setAnisotropyTextureInfoModel(
        TextureInfoModel anisotropyTextureInfoModel);

    /**
     * The anisotropy texture. (optional) 
     * 
     * @return The anisotropy texture info model
     */
    TextureInfoModel getAnisotropyTextureInfoModel();

}
