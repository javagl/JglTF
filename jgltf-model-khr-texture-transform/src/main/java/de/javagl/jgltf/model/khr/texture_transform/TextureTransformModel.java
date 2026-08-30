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
package de.javagl.jgltf.model.khr.texture_transform;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.extensions.ExtensionModel;

/**
 * Interface for a model of the <code>KHR_texture_transform</code> extension
 */
public interface TextureTransformModel extends ModelElement, ExtensionModel
{
    /**
     * The offset of the UV coordinate origin as a factor of the texture 
     * dimensions. (optional)<br> 
     * Default: [0.0,0.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param offset The offset to set
     */
    void setOffset(double[] offset);

    /**
     * The offset of the UV coordinate origin as a factor of the texture 
     * dimensions. (optional)<br> 
     * Default: [0.0,0.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The offset
     */
    double[] getOffset();

    /**
     * Rotate the UVs by this many radians counter-clockwise around the 
     * origin. (optional)<br> 
     * Default: 0.0 
     * 
     * @param rotation The rotation to set
     */
    void setRotation(Double rotation);

    /**
     * Rotate the UVs by this many radians counter-clockwise around the 
     * origin. (optional)<br> 
     * Default: 0.0 
     * 
     * @return The rotation
     */
    Double getRotation();

    /**
     * The scale factor applied to the components of the UV coordinates. 
     * (optional)<br> 
     * Default: [1.0,1.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param scale The scale to set
     */
    void setScale(double[] scale);

    /**
     * The scale factor applied to the components of the UV coordinates. 
     * (optional)<br> 
     * Default: [1.0,1.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The scale
     */
    double[] getScale();

    /**
     * Overrides the textureInfo texCoord value if supplied, and if this 
     * extension is supported. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param texCoord The texCoord to set
     */
    void setTexCoord(Integer texCoord);

    /**
     * Overrides the textureInfo texCoord value if supplied, and if this 
     * extension is supported. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The texCoord
     */
    Integer getTexCoord();

}
