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
     * 
     * @param thicknessFactor The thicknessFactor to set
     */
    void setThicknessFactor(Double thicknessFactor);

    /**
     * Thickness of the volume. (optional)<br>
     * 
     * @return The thicknessFactor
     */
    Double getThicknessFactor();

    /**
     * Texture that defines the thickness of the volume, stored in the G
     * channel. (optional)
     * 
     * @param thicknessTextureInfoModel The thicknessTexture to set
     * 
     */
    void setThicknessTextureInfoModel(
        TextureInfoModel thicknessTextureInfoModel);

    /**
     * Texture that defines the thickness of the volume, stored in the G
     * channel. (optional)
     * 
     * @return The thicknessTexture
     * 
     */
    TextureInfoModel getThicknessTextureInfoModel();

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
     * 
     * @param attenuationColor The attenuationColor to set
     */
    void setAttenuationColor(double[] attenuationColor);

    /**
     * Color that white light turns into due to absorption when reaching the
     * attenuation distance. (optional)<br>
     * 
     * @return The attenuationColor
     * 
     */
    double[] getAttenuationColor();
}
