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
package de.javagl.jgltf.model.khr.materials_sheen;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_sheen</code> extension that
 * is found in the material of a glTF
 */
public interface MaterialsSheenModel extends ModelElement, ExtensionModel
{
    /**
     * Color of the sheen layer (in linear space). (optional)<br>
     * Default: [0,0,0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br>
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive)
     * 
     * @param sheenColorFactor The sheenColorFactor to set
     */
    void setSheenColorFactor(double[] sheenColorFactor);

    /**
     * Color of the sheen layer (in linear space). (optional)<br>
     * Default: [0,0,0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br>
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive)
     * 
     * @return The sheenColorFactor
     */
    double[] getSheenColorFactor();

    /**
     * The sheen color (RGB) texture. (optional)
     * 
     * @param sheenColorTexture The sheenColorTexture to set
     */
    void setSheenColorTexture(TextureInfoModel sheenColorTexture);

    /**
     * The sheen color (RGB) texture. (optional)
     * 
     * @return The sheenColorTexture
     * 
     */
    TextureInfoModel getSheenColorTexture();

    /**
     * The sheen layer roughness. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @param sheenRoughnessFactor The sheenRoughnessFactor to set
     */
    void setSheenRoughnessFactor(Double sheenRoughnessFactor);

    /**
     * The sheen layer roughness. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @return The sheenRoughnessFactor
     */
    Double getSheenRoughnessFactor();

    /**
     * The sheen roughness (Alpha) texture. (optional)
     * 
     * @param sheenRoughnessTexture The sheenRoughnessTexture to set
     */
    void setSheenRoughnessTexture(TextureInfoModel sheenRoughnessTexture);

    /**
     * The sheen roughness (Alpha) texture. (optional)
     * 
     * @return The sheenRoughnessTexture
     */
    TextureInfoModel getSheenRoughnessTexture();

}
