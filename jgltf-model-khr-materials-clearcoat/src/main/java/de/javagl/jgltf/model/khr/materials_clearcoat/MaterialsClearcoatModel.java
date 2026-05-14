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
package de.javagl.jgltf.model.khr.materials_clearcoat;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_clearcoat</code> extension
 * that is found in the material of a glTF
 */
public interface MaterialsClearcoatModel extends ModelElement, ExtensionModel
{
    /**
     * The clearcoat layer intensity.
     * 
     * @param clearcoatFactor The clearcoatFactor to set
     */
    void setClearcoatFactor(Double clearcoatFactor);

    /**
     * The clearcoat layer intensity.
     * 
     * @return The clearcoatFactor
     */
    Double getClearcoatFactor();

    /**
     * The clearcoat layer intensity texture info.
     * 
     * @param clearcoatTextureInfoModel The texture info
     */
    void setClearcoatTextureInfoModel(
        TextureInfoModel clearcoatTextureInfoModel);

    /**
     * The clearcoat layer intensity texture info
     * 
     * @return The texture info
     */
    TextureInfoModel getClearcoatTextureInfoModel();

    /**
     * The clearcoat layer roughness.
     * 
     * @param clearcoatRoughnessFactor The clearcoatRoughnessFactor to set
     */
    void setClearcoatRoughnessFactor(Double clearcoatRoughnessFactor);

    /**
     * The clearcoat layer roughness.
     * 
     * @return The clearcoatRoughnessFactor
     */
    Double getClearcoatRoughnessFactor();

    /**
     * The clearcoat layer roughness texture info
     * 
     * @param clearcoatRoughnessTextureInfoModel The texture info
     */
    void setClearcoatRoughnessTextureInfoModel(
        TextureInfoModel clearcoatRoughnessTextureInfoModel);

    /**
     * The clearcoat layer roughness texture info.
     * 
     * @return The clearcoatRoughnessTexture info
     */
    TextureInfoModel getClearcoatRoughnessTextureInfoModel();

    /**
     * The clearcoat normal map texture info. (optional)
     * 
     * @param clearcoatNormalTextureInfoModel The texture info
     */
    void setClearcoatNormalTextureInfoModel(
        NormalTextureInfoModel clearcoatNormalTextureInfoModel);

    /**
     * The clearcoat normal map texture info. (optional)
     * 
     * @return The texture info
     * 
     */
    NormalTextureInfoModel getClearcoatNormalTextureInfoModel();
}
