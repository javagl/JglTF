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
package de.javagl.jgltf.model.khr.materials_transmission;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_transmission</code>
 * extension that is found in the material of a glTF
 */
public interface MaterialsTransmissionModel extends ModelElement, ExtensionModel
{
    /**
     * The base percentage of light transmitted through the surface.
     * (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @param transmissionFactor The transmissionFactor to set
     */
    void setTransmissionFactor(Double transmissionFactor);

    /**
     * The base percentage of light transmitted through the surface.
     * (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     * @return The transmissionFactor
     */
    Double getTransmissionFactor();

    /**
     * A texture that defines the transmission percentage of the surface,
     * sampled from the R channel. These values are linear, and will be
     * multiplied by transmissionFactor. (optional)
     * 
     * @param transmissionTexture The transmissionTexture to set
     */
    void setTransmissionTexture(TextureInfoModel transmissionTexture);

    /**
     * A texture that defines the transmission percentage of the surface,
     * sampled from the R channel. These values are linear, and will be
     * multiplied by transmissionFactor. (optional)
     * 
     * @return The transmissionTexture
     */
    TextureInfoModel getTransmissionTexture();
}
