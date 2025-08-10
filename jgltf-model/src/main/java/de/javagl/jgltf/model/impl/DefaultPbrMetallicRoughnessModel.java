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

import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.OcclusionTextureInfoModel;
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
    private double metallicFactor;

    /**
     * The roughness factor
     */
    private double roughnessFactor;

    /**
     * The metallic-roughness texture info
     */
    private TextureInfoModel metallicRoughnessTextureInfoModel;

    /**
     * The normal texture info
     */
    private NormalTextureInfoModel normalTextureInfoModel;

    /**
     * The occlusion texture info
     */
    private OcclusionTextureInfoModel occlusionTextureInfoModel;

    /**
     * The emissive texture info
     */
    private TextureInfoModel emissiveTextureInfoModel;

    /**
     * Creates a new instance with default values
     */
    public DefaultPbrMetallicRoughnessModel()
    {
        baseColorFactor = new double[]
        { 1.0, 1.0, 1.0, 1.0 };
        baseColorTextureInfoModel = null;
        metallicFactor = 1.0;
        roughnessFactor = 1.0;
        metallicRoughnessTextureInfoModel = null;
        normalTextureInfoModel = null;
        occlusionTextureInfoModel = null;
        emissiveTextureInfoModel = null;
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
    public double getMetallicFactor()
    {
        return metallicFactor;
    }

    /**
     * Set the metallic factor
     *
     * @param metallicFactor The metallic factor
     */
    public void setMetallicFactor(double metallicFactor)
    {
        this.metallicFactor = metallicFactor;
    }

    @Override
    public double getRoughnessFactor()
    {
        return roughnessFactor;
    }

    /**
     * Set the roughness factor
     *
     * @param roughnessFactor The roughness factor
     */
    public void setRoughnessFactor(double roughnessFactor)
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

    /**
     * Returns the normal texture info model
     *
     * @return The normal texture info model
     */
    public NormalTextureInfoModel getNormalTextureInfoModel()
    {
        return normalTextureInfoModel;
    }

    /**
     * Set the normal texture info model
     *
     * @param normalTextureInfoModel The normal texture info model
     */
    public void
        setNormalTextureInfoModel(NormalTextureInfoModel normalTextureInfoModel)
    {
        this.normalTextureInfoModel = normalTextureInfoModel;
    }

    /**
     * Returns the occlusion texture info model
     *
     * @return The occlusion texture info model
     */
    public OcclusionTextureInfoModel getOcclusionTextureInfoModel()
    {
        return occlusionTextureInfoModel;
    }

    /**
     * Set the occlusion texture info model
     *
     * @param occlusionTextureInfoModel The occlusion texture info model
     */
    public void setOcclusionTextureInfoModel(
        OcclusionTextureInfoModel occlusionTextureInfoModel)
    {
        this.occlusionTextureInfoModel = occlusionTextureInfoModel;
    }

    /**
     * Returns the emissive texture info model
     *
     * @return The emissive texture info model
     */
    public TextureInfoModel getEmissiveTextureInfoModel()
    {
        return emissiveTextureInfoModel;
    }

    /**
     * Set the emissive texture info model
     *
     * @param emissiveTextureInfoModel The emissive texture info model
     */
    public void setEmissiveTexture(TextureInfoModel emissiveTextureInfoModel)
    {
        this.emissiveTextureInfoModel = emissiveTextureInfoModel;
    }
}
