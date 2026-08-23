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
package de.javagl.jgltf.model.khr.materials_emissive_strength;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_materials_clearcoat</code> extension
 * that is found in the material of a glTF
 */
public interface MaterialsEmissiveStrengthModel
    extends ModelElement, ExtensionModel
{
    /**
     * The strength adjustment to be multiplied with the material's emissive
     * value. (optional)<br>
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @param emissiveStrength The emissiveStrength to set
     */
    void setEmissiveStrength(Double emissiveStrength);

    /**
     * The strength adjustment to be multiplied with the material's emissive
     * value. (optional)<br>
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The emissiveStrength
     */
    Double getEmissiveStrength();
}
