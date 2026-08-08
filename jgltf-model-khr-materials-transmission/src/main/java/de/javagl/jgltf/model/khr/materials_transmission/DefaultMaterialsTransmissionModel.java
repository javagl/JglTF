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

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsTransmissionModel}
 */
public class DefaultMaterialsTransmissionModel extends AbstractModelElement
    implements MaterialsTransmissionModel
{
    /**
     * The base percentage of light transmitted through the surface.
     * (optional)<br>
     */
    private Double transmissionFactor;

    /**
     * A texture that defines the transmission percentage of the surface,
     * sampled from the R channel. These values are linear, and will be
     * multiplied by transmissionFactor. (optional)
     */
    private TextureInfoModel transmissionTexture;

    @Override
    public void setTransmissionFactor(Double transmissionFactor)
    {
        this.transmissionFactor = transmissionFactor;
    }

    @Override
    public Double getTransmissionFactor()
    {
        return this.transmissionFactor;
    }

    @Override
    public void setTransmissionTexture(TextureInfoModel transmissionTexture)
    {
        this.transmissionTexture = transmissionTexture;
    }

    @Override
    public TextureInfoModel getTransmissionTexture()
    {
        return this.transmissionTexture;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (transmissionTexture != null)
        {
            modelElements.add(transmissionTexture);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(transmissionTexture))
        {
            setTransmissionTexture(null);
        }
        return false;
    }

    @Override
    public String getExtensionName()
    {
        return "KHR_materials_transmission";
    }

    @Override
    public boolean isRequired()
    {
        return false;
    }

}
