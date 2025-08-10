/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.khr.lights_punctual;

import de.javagl.jgltf.model.NamedModelElement;

/**
 * Interface for a light in a glTF model
 */
public interface LightModel extends NamedModelElement
{
    /**
     * Set the color of this light to be a <b>reference</b> to the given
     * array. 
     * 
     * @param color The color
     */
    void setColor(double color[]);
    
    /**
     * Returns a <b>reference</b> to the array storing the color of this 
     * light, or <code>null</code> if no color was set.
     * 
     * @return The color
     */
    double[] getColor();
    
    /**
     * Set the intensity of the light source.
     * 
     * Point and spot lights use luminous intensity in candela (lm/sr) 
     * while directional lights use illuminance in lux (lm/m^2). 
     *  
     * @param intensity The intensity
     */
    void setIntensity(Double intensity);
    
    /**
     * Returns the intensity of the light source, or <code>null</code> if
     * no intensity was set.
     * 
     * @return The intensity
     */
    Double getIntensity();

    /**
     * Set the spot of this Light 
     * 
     * @param spot The spot
     */
    void setSpot(LightSpotModel spot);
    
    /**
     * Returns the spot of this light, or <code>null</code> if no spot
     * was set.
     * 
     * @return The spot
     */
    LightSpotModel getSpot();
    
    /**
     * Set a distance cutoff at which the light's intensity may be considered 
     * to have reached zero.
     * 
     *  @param range The range
     */
    void setRange(Double range);
    
    /**
     * Returns the range at which the light's intensity may be considered 
     * to have reached zero, or <code>null</code> if no range was set.
     * 
     * @return The range
     */
    Double getRange();
}
