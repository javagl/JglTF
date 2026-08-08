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

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsVolumeModel}
 */
public class DefaultMaterialsVolumeModel extends AbstractModelElement
    implements MaterialsVolumeModel
{
    /**
     * Thickness of the volume. (optional)<br>
     */
    private Double thicknessFactor;

    /**
     * Texture that defines the thickness of the volume, stored in the G
     * channel. (optional)
     */
    private TextureInfoModel thicknessTexture;

    /**
     * Average distance that light travels in the medium before interacting with
     * a particle. (optional)
     */
    private Double attenuationDistance;

    /**
     * Color that white light turns into due to absorption when reaching the
     * attenuation distance. (optional)<br>
     */
    private double[] attenuationColor;

    @Override
    public void setThicknessFactor(Double thicknessFactor)
    {
        this.thicknessFactor = thicknessFactor;
    }

    @Override
    public Double getThicknessFactor()
    {
        return this.thicknessFactor;
    }

    @Override
    public void setThicknessTexture(TextureInfoModel thicknessTexture)
    {
        this.thicknessTexture = thicknessTexture;
    }

    @Override
    public TextureInfoModel getThicknessTexture()
    {
        return this.thicknessTexture;
    }

    @Override
    public void setAttenuationDistance(Double attenuationDistance)
    {
        this.attenuationDistance = attenuationDistance;
    }

    @Override
    public Double getAttenuationDistance()
    {
        return this.attenuationDistance;
    }

    @Override
    public void setAttenuationColor(double[] attenuationColor)
    {
        this.attenuationColor = attenuationColor;
    }

    @Override
    public double[] getAttenuationColor()
    {
        return this.attenuationColor;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (thicknessTexture != null)
        {
            modelElements.add(thicknessTexture);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(thicknessTexture))
        {
            setThicknessTexture(null);
        }
        return false;
    }

    @Override
    public String getExtensionName()
    {
        return "KHR_materials_volume";
    }

    @Override
    public boolean isRequired()
    {
        return false;
    }

}
