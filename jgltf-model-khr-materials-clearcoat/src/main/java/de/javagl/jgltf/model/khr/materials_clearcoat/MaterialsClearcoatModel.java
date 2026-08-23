/*
 * www.javagl.de - JglTF
 *
 * Copyright 2025 Marco Hutter - http://www.javagl.de
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
     * The clearcoat layer intensity. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @param clearcoatFactor The clearcoatFactor to set
     */
    void setClearcoatFactor(Double clearcoatFactor);

    /**
     * The clearcoat layer intensity. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @return The clearcoatFactor
     */
    Double getClearcoatFactor();

    /**
     * The clearcoat layer intensity texture. (optional)
     * 
     * @param clearcoatTexture The texture info
     */
    void setClearcoatTexture(TextureInfoModel clearcoatTexture);

    /**
     * The clearcoat layer intensity texture. (optional)
     * 
     * @return The texture info
     */
    TextureInfoModel getClearcoatTexture();

    /**
     * The clearcoat layer roughness. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @param clearcoatRoughnessFactor The clearcoatRoughnessFactor to set
     */
    void setClearcoatRoughnessFactor(Double clearcoatRoughnessFactor);

    /**
     * The clearcoat layer roughness. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @return The clearcoatRoughnessFactor
     */
    Double getClearcoatRoughnessFactor();

    /**
     * The clearcoat layer roughness texture. (optional)
     * 
     * @param clearcoatRoughnessTexture The texture info
     */
    void setClearcoatRoughnessTexture(
        TextureInfoModel clearcoatRoughnessTexture);

    /**
     * The clearcoat layer roughness texture. (optional)
     * 
     * @return The clearcoatRoughnessTexture info
     */
    TextureInfoModel getClearcoatRoughnessTexture();

    /**
     * The clearcoat normal map texture. (optional)
     * 
     * @param clearcoatNormalTexture The texture info
     */
    void setClearcoatNormalTexture(
        NormalTextureInfoModel clearcoatNormalTexture);

    /**
     * The clearcoat normal map texture. (optional)
     * 
     * @return The texture info
     */
    NormalTextureInfoModel getClearcoatNormalTexture();
}
