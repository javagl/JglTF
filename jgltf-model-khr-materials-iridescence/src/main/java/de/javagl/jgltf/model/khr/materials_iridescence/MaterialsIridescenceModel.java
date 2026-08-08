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
package de.javagl.jgltf.model.khr.materials_iridescence;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_iridescence</code> extension
 * that is found in the material of a glTF
 */
public interface MaterialsIridescenceModel extends ModelElement, ExtensionModel
{
    /**
     * The iridescence intensity factor. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @param iridescenceFactor The iridescenceFactor to set
     */
    void setIridescenceFactor(Double iridescenceFactor);

    /**
     * The iridescence intensity factor. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @return The iridescenceFactor
     */
    Double getIridescenceFactor();

    /**
     * The iridescence intensity texture. (optional)
     * 
     * @param iridescenceTexture The iridescenceTexture to set
     */
    void setIridescenceTexture(TextureInfoModel iridescenceTexture);

    /**
     * The iridescence intensity texture. (optional)
     * 
     * @return The iridescenceTexture
     */
    TextureInfoModel getIridescenceTexture();

    /**
     * The index of refraction of the dielectric thin-film layer. (optional)<br>
     * Default: 1.3<br>
     * Minimum: 1.0 (inclusive)
     * 
     * @param iridescenceIor The iridescenceIor to set
     */
    void setIridescenceIor(Double iridescenceIor);

    /**
     * The index of refraction of the dielectric thin-film layer. (optional)<br>
     * Default: 1.3<br>
     * Minimum: 1.0 (inclusive)
     * 
     * @return The iridescenceIor
     */
    Double getIridescenceIor();

    /**
     * The minimum thickness of the thin-film layer given in nanometers.
     * (optional)<br>
     * Default: 100.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     * @param iridescenceThicknessMinimum The iridescenceThicknessMinimum to set
     */
    void setIridescenceThicknessMinimum(Double iridescenceThicknessMinimum);

    /**
     * The minimum thickness of the thin-film layer given in nanometers.
     * (optional)<br>
     * Default: 100.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     * @return The iridescenceThicknessMinimum
     */
    Double getIridescenceThicknessMinimum();

    /**
     * The maximum thickness of the thin-film layer given in nanometers.
     * (optional)<br>
     * Default: 400.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     * @param iridescenceThicknessMaximum The iridescenceThicknessMaximum to set
     */
    void setIridescenceThicknessMaximum(Double iridescenceThicknessMaximum);

    /**
     * The maximum thickness of the thin-film layer given in nanometers.
     * (optional)<br>
     * Default: 400.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     * @return The iridescenceThicknessMaximum
     */
    Double getIridescenceThicknessMaximum();

    /**
     * The thickness texture of the thin-film layer. (optional)
     * 
     * @param iridescenceThicknessTexture The iridescenceThicknessTexture to set
     */
    void setIridescenceThicknessTexture(
        TextureInfoModel iridescenceThicknessTexture);

    /**
     * The thickness texture of the thin-film layer. (optional)
     * 
     * @return The iridescenceThicknessTexture
     */
    TextureInfoModel getIridescenceThicknessTexture();

}
