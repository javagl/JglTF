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
package de.javagl.jgltf.model.khr.texture_transform;

import de.javagl.jgltf.model.ModelElement;

/**
 * Interface for a model of the <code>KHR_texture_transform</code> extension
 */
public interface TextureTransformModel extends ModelElement
{
    /**
     * Set the offset to be a <b>reference</b> to the given array. 
     * 
     * @param offset The offset to set
     */
    void setOffset(double[] offset);

    /**
     * Returns a <b>reference</b> to the array storing the offset, 
     * or <code>null</code> if no color was set.
     * 
     * @return The offset
     */
    double[] getOffset();

    /**
     * Set the counter-clockwise rotation around the origin, in radians
     * 
     * @param rotation The rotation to set
     */
    void setRotation(Double rotation);

    /**
     * Return the counter-clockwise rotation around the origin, in radians,
     * or <code>null</code> if the rotation was not set.
     * 
     * @return The rotation
     */
    Double getRotation();

    /**
     * Set the scale to be a <b>reference</b> to the given array. 
     * 
     * @param scale The scale to set
     */
    void setScale(double[] scale);

    /**
     * Returns a <b>reference</b> to the array storing the scale, 
     * or <code>null</code> if no color was set.
     * 
     * @return The scale
     */
    double[] getScale();

    /**
     * Set the optional override of the 'texCoord' value
     * 
     * @param texCoord The texCoord to set
     */
    void setTexCoord(Integer texCoord);

    /**
     * Returns the optional 'texCoord' value if supplied, or <code>null</code>
     * if it was not provided.
     * 
     * @return The texCoord
     */
    Integer getTexCoord();

}
