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
    private TextureInfoModel baseColorTextureInfoModel;

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
    private TextureInfoModel metallicRoughnessTextureInfoModel;

    /**
     * Creates a new instance
     */
    public DefaultPbrMetallicRoughnessModel()
    {
        baseColorFactor = null;
        baseColorTextureInfoModel = null;
        metallicFactor = null;
        roughnessFactor = null;
        metallicRoughnessTextureInfoModel = null;
    }

    @Override
    public double[] getBaseColorFactor()
    {
        return baseColorFactor;
    }

    /**
     * Set the base color factor
     *
     * @param baseColorFactor The base color factor
     */
    public void setBaseColorFactor(double[] baseColorFactor)
    {
        this.baseColorFactor = baseColorFactor;
    }

    @Override
    public TextureInfoModel getBaseColorTextureInfoModel()
    {
        return baseColorTextureInfoModel;
    }

    /**
     * Set the base color texture info model
     *
     * @param baseColorTextureInfoModel The base color texture info model
     */
    public void setBaseColorTextureInfoModel(TextureInfoModel baseColorTextureInfoModel)
    {
        this.baseColorTextureInfoModel = baseColorTextureInfoModel;
    }

    @Override
    public Double getMetallicFactor()
    {
        return metallicFactor;
    }

    /**
     * Set the metallic factor
     *
     * @param metallicFactor The metallic factor
     */
    public void setMetallicFactor(Double metallicFactor)
    {
        this.metallicFactor = metallicFactor;
    }

    @Override
    public Double getRoughnessFactor()
    {
        return roughnessFactor;
    }

    /**
     * Set the roughness factor
     *
     * @param roughnessFactor The roughness factor
     */
    public void setRoughnessFactor(Double roughnessFactor)
    {
        this.roughnessFactor = roughnessFactor;
    }

    @Override
    public TextureInfoModel getMetallicRoughnessTextureInfoModel()
    {
        return metallicRoughnessTextureInfoModel;
    }

    /**
     * Set the metallic-roughness-texture info model
     *
     * @param metallicRoughnessTextureInfoModel The metallic-roughness-texture
     *        info model
     */
    public void setMetallicRoughnessTextureInfoModel(
        TextureInfoModel metallicRoughnessTextureInfoModel)
    {
        this.metallicRoughnessTextureInfoModel =
            metallicRoughnessTextureInfoModel;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        if (baseColorTextureInfoModel != null)
        {
            modelElements.add(baseColorTextureInfoModel);
        }
        if (metallicRoughnessTextureInfoModel != null)
        {
            modelElements.add(metallicRoughnessTextureInfoModel);
        }
        return modelElements;
    }
    
}
