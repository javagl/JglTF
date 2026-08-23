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
package de.javagl.jgltf.model.khr.materials_volume;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_volume</code> extension that
 * is found in the material of a glTF
 */
public interface MaterialsVolumeModel extends ModelElement, ExtensionModel
{
    /**
     * Thickness of the volume. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     * @param thicknessFactor The thicknessFactor to set
     */
    void setThicknessFactor(Double thicknessFactor);

    /**
     * Thickness of the volume. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     * @return The thicknessFactor
     */
    Double getThicknessFactor();

    /**
     * Texture that defines the thickness of the volume, stored in the G
     * channel. (optional)
     * 
     * @param thicknessTexture The thicknessTexture to set
     * 
     */
    void setThicknessTexture(TextureInfoModel thicknessTexture);

    /**
     * Texture that defines the thickness of the volume, stored in the G
     * channel. (optional)
     * 
     * @return The thicknessTexture
     * 
     */
    TextureInfoModel getThicknessTexture();

    /**
     * Average distance that light travels in the medium before interacting with
     * a particle. (optional)
     * 
     * @param attenuationDistance The attenuationDistance to set
     * 
     */
    void setAttenuationDistance(Double attenuationDistance);

    /**
     * Average distance that light travels in the medium before interacting with
     * a particle. (optional)
     * 
     * @return The attenuationDistance
     * 
     */
    Double getAttenuationDistance();

    /**
     * Color that white light turns into due to absorption when reaching the
     * attenuation distance. (optional)<br>
     * Default: [1.0,1.0,1.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br>
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive)
     * 
     * @param attenuationColor The attenuationColor to set
     */
    void setAttenuationColor(double[] attenuationColor);

    /**
     * Color that white light turns into due to absorption when reaching the
     * attenuation distance. (optional)<br>
     * Default: [1.0,1.0,1.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br>
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive)
     * 
     * @return The attenuationColor
     */
    double[] getAttenuationColor();
}
