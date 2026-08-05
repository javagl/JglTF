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
package de.javagl.jgltf.model.khr.materials_sheen;

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsSheenModel}
 */
public class DefaultMaterialsSheenModel extends AbstractModelElement
    implements MaterialsSheenModel
{
    /**
     * Color of the sheen layer (in linear space). (optional)<br>
     */
    private double[] sheenColorFactor;

    /**
     * The sheen color (RGB) texture. (optional)
     */
    private TextureInfoModel sheenColorTexture;

    /**
     * The sheen layer roughness. (optional)<br>
     */
    private Double sheenRoughnessFactor;

    /**
     * The sheen roughness (Alpha) texture. (optional)
     */
    private TextureInfoModel sheenRoughnessTexture;

    @Override
    public void setSheenColorFactor(double[] sheenColorFactor)
    {
        this.sheenColorFactor = sheenColorFactor;
    }

    @Override
    public double[] getSheenColorFactor()
    {
        return this.sheenColorFactor;
    }

    @Override
    public void setSheenColorTexture(TextureInfoModel sheenColorTexture)
    {
        this.sheenColorTexture = sheenColorTexture;
    }

    @Override
    public TextureInfoModel getSheenColorTexture()
    {
        return this.sheenColorTexture;
    }

    @Override
    public void setSheenRoughnessFactor(Double sheenRoughnessFactor)
    {
        this.sheenRoughnessFactor = sheenRoughnessFactor;
    }

    @Override
    public Double getSheenRoughnessFactor()
    {
        return this.sheenRoughnessFactor;
    }

    @Override
    public void setSheenRoughnessTexture(TextureInfoModel sheenRoughnessTexture)
    {
        this.sheenRoughnessTexture = sheenRoughnessTexture;
    }

    @Override
    public TextureInfoModel getSheenRoughnessTexture()
    {
        return this.sheenRoughnessTexture;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (sheenColorTexture != null)
        {
            modelElements.add(sheenColorTexture);
        }
        if (sheenRoughnessTexture != null)
        {
            modelElements.add(sheenRoughnessTexture);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(sheenColorTexture))
        {
            setSheenColorTexture(null);
        }
        if (modelElementsToRemove.contains(sheenRoughnessTexture))
        {
            setSheenRoughnessTexture(null);
        }
        return false;
    }

    @Override
    public String getExtensionName()
    {
        return "KHR_materials_sheen";
    }

    @Override
    public boolean isRequired()
    {
        return false;
    }

}
