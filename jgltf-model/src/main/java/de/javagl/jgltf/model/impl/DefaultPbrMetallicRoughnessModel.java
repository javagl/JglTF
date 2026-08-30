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
package de.javagl.jgltf.model.impl;

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.PbrMetallicRoughnessModel;
import de.javagl.jgltf.model.TextureInfoModel;

/**
 * Default implementation of a {@link PbrMetallicRoughnessModel}
 */
public class DefaultPbrMetallicRoughnessModel extends AbstractModelElement
    implements PbrMetallicRoughnessModel
{
    /**
     * The base color factor
     */
    private double[] baseColorFactor;

    /**
     * The base color texture info
     */
    private TextureInfoModel baseColorTexture;

    /**
     * The metallic factor
     */
    private Double metallicFactor;

    /**
     * The roughness factor
     */
    private Double roughnessFactor;

    /**
     * The metallic-roughness texture info
     */
    private TextureInfoModel metallicRoughnessTexture;

    /**
     * Creates a new instance
     */
    public DefaultPbrMetallicRoughnessModel()
    {
        baseColorFactor = null;
        baseColorTexture = null;
        metallicFactor = null;
        roughnessFactor = null;
        metallicRoughnessTexture = null;
    }

    @Override
    public double[] getBaseColorFactor()
    {
        return baseColorFactor;
    }

    @Override
    public void setBaseColorFactor(double[] baseColorFactor)
    {
        this.baseColorFactor = baseColorFactor;
    }

    @Override
    public TextureInfoModel getBaseColorTexture()
    {
        return baseColorTexture;
    }

    @Override
    public void setBaseColorTexture(TextureInfoModel baseColorTexture)
    {
        this.baseColorTexture = baseColorTexture;
    }

    @Override
    public Double getMetallicFactor()
    {
        return metallicFactor;
    }

    @Override
    public void setMetallicFactor(Double metallicFactor)
    {
        this.metallicFactor = metallicFactor;
    }

    @Override
    public Double getRoughnessFactor()
    {
        return roughnessFactor;
    }

    @Override
    public void setRoughnessFactor(Double roughnessFactor)
    {
        this.roughnessFactor = roughnessFactor;
    }

    @Override
    public TextureInfoModel getMetallicRoughnessTexture()
    {
        return metallicRoughnessTexture;
    }

    @Override
    public void
        setMetallicRoughnessTexture(TextureInfoModel metallicRoughnessTexture)
    {
        this.metallicRoughnessTexture = metallicRoughnessTexture;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (baseColorTexture != null)
        {
            modelElements.add(baseColorTexture);
        }
        if (metallicRoughnessTexture != null)
        {
            modelElements.add(metallicRoughnessTexture);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(baseColorTexture))
        {
            setBaseColorTexture(null);
        }
        if (modelElementsToRemove.contains(metallicRoughnessTexture))
        {
            setBaseColorTexture(null);
        }
        return false;
    }

}
