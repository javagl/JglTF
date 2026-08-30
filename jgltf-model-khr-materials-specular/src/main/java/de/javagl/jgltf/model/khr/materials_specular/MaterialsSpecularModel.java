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
package de.javagl.jgltf.model.khr.materials_specular;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_specular</code> extension
 * that is found in the material of a glTF
 */
public interface MaterialsSpecularModel extends ModelElement, ExtensionModel
{
    /**
     * The strength of the specular reflection. (optional)<br>
     * Default: 1.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @param specularFactor The specularFactor to set
     */
    void setSpecularFactor(Double specularFactor);

    /**
     * The strength of the specular reflection. (optional)<br>
     * Default: 1.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @return The specularFactor
     */
    Double getSpecularFactor();

    /**
     * A texture that defines the specular factor in the alpha channel.
     * (optional)
     * 
     * @param specularTexture The specularTexture to set
     */
    void setSpecularTexture(TextureInfoModel specularTexture);

    /**
     * A texture that defines the specular factor in the alpha channel.
     * (optional)
     * 
     * @return The specularTexture
     */
    TextureInfoModel getSpecularTexture();

    /**
     * The F0 RGB color of the specular reflection. (optional)<br>
     * Default: [1.0,1.0,1.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)
     * 
     * @param specularColorFactor The specularColorFactor to set
     */
    void setSpecularColorFactor(double[] specularColorFactor);

    /**
     * The F0 RGB color of the specular reflection. (optional)<br>
     * Default: [1.0,1.0,1.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)
     * 
     * @return The specularColorFactor
     */
    double[] getSpecularColorFactor();

    /**
     * A texture that defines the F0 color of the specular reflection.
     * (optional)
     * 
     * @param specularColorTexture The specularColorTexture to set
     */
    void setSpecularColorTexture(TextureInfoModel specularColorTexture);

    /**
     * A texture that defines the F0 color of the specular reflection.
     * (optional)
     * 
     * @return The specularColorTexture
     */
    TextureInfoModel getSpecularColorTexture();
}
