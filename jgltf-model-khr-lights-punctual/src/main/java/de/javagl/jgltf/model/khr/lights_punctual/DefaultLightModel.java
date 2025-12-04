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

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractNamedModelElement;

/**
 * Default implementation of a {@link LightModel}
 */
public class DefaultLightModel 
    extends AbstractNamedModelElement
    implements LightModel
{
    /**
     * The color
     */
    private double color[];

    /**
     * The intensity
     */
    private Double intensity;

    /**
     * The spot
     */
    private LightSpotModel spot;

    /**
     * The range
     */
    private Double range;

    @Override
    public void setColor(double[] color)
    {
        this.color = color;
    }

    @Override
    public double[] getColor()
    {
        return this.color;
    }

    @Override
    public void setIntensity(Double intensity)
    {
        this.intensity = intensity;
    }

    @Override
    public Double getIntensity()
    {
        return this.intensity;
    }

    @Override
    public void setSpot(LightSpotModel spot)
    {
        this.spot = spot;
    }

    @Override
    public LightSpotModel getSpot()
    {
        return this.spot;
    }

    @Override
    public void setRange(Double range)
    {
        this.range = range;
    }

    @Override
    public Double getRange()
    {
        return this.range;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        return modelElements;
    }
    
    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove) 
    {
        removeExtensionModelElements(modelElementsToRemove);
        return false;
    }
    
}
