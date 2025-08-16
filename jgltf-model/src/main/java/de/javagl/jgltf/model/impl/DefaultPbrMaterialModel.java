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
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.OcclusionTextureInfoModel;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.PbrMetallicRoughnessModel;
import de.javagl.jgltf.model.TextureInfoModel;

/**
 * Default implementation of a {@link PbrMaterialModel} for glTF 2.0.<br>
 */
public final class DefaultPbrMaterialModel extends AbstractNamedModelElement 
    implements PbrMaterialModel
{
    /**
     * The {@link PbrMetallicRoughnessModel}
     */
    private PbrMetallicRoughnessModel pbrMetallicRoughnessModel;
    
    /**
     * THe {@link NormalTextureInfoModel}
     */
    private NormalTextureInfoModel normalTextureInfoModel;
    
    /**
     * The {@link OcclusionTextureInfoModel}
     */
    private OcclusionTextureInfoModel occlusionTextureInfoModel;
    
    /**
     * The emissive {@link TextureInfoModel}
     */
    private TextureInfoModel emissiveTextureInfoModel;
    
    /**
     * The emissive factor
     */
    private double[] emissiveFactor;

    /**
     * The alpha mode
     */
    private AlphaMode alphaMode;

    /**
     * The alpha cutoff
     */
    private Double alphaCutoff;

    /**
     * Whether the material is double sided
     */
    private boolean doubleSided;

    /**
     * Creates a new instance with default values
     */
    public DefaultPbrMaterialModel()
    {
        pbrMetallicRoughnessModel = null;
        normalTextureInfoModel = null;
        occlusionTextureInfoModel = null;
        emissiveTextureInfoModel = null;
        emissiveFactor = null;
        alphaMode = null;
        alphaCutoff = null;
        doubleSided = false;
    }

    @Override
    public PbrMetallicRoughnessModel getPbrMetallicRoughnessModel()
    {
        return pbrMetallicRoughnessModel;
    }
    
    /**
     * Set the {@link PbrMetallicRoughnessModel}
     * 
     * @param pbrMetallicRoughnessModel The {@link PbrMetallicRoughnessModel}
     */
    public void setPbrMetallicRoughnessModel(
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel)
    {
        this.pbrMetallicRoughnessModel = pbrMetallicRoughnessModel;
    }

    @Override
    public NormalTextureInfoModel getNormalTextureInfoModel()
    {
        return normalTextureInfoModel;
    }
    
    /**
     * Set the {@link NormalTextureInfoModel}
     * @param normalTextureInfoModel The {@link NormalTextureInfoModel}
     */
    public void
        setNormalTextureInfoModel(NormalTextureInfoModel normalTextureInfoModel)
    {
        this.normalTextureInfoModel = normalTextureInfoModel;
    }

    @Override
    public OcclusionTextureInfoModel getOcclusionTextureInfoModel()
    {
        return occlusionTextureInfoModel;
    }
    
    /**
     * Set the {@link OcclusionTextureInfoModel}
     * 
     * @param occlusionTextureInfoModel The {@link OcclusionTextureInfoModel}
     */
    public void setOcclusionTextureInfoModel(
        OcclusionTextureInfoModel occlusionTextureInfoModel)
    {
        this.occlusionTextureInfoModel = occlusionTextureInfoModel;
    }

    @Override
    public TextureInfoModel getEmissiveTextureInfoModel()
    {
        return emissiveTextureInfoModel;
    }
    
    /**
     * Set the emissive {@link TextureInfoModel}
     * 
     * @param emissiveTextureInfoModel The {@link TextureInfoModel}
     */
    public void
        setEmissiveTextureInfoModel(TextureInfoModel emissiveTextureInfoModel)
    {
        this.emissiveTextureInfoModel = emissiveTextureInfoModel;
    }

    @Override
    public double[] getEmissiveFactor()
    {
        return emissiveFactor;
    }

    /**
     * Set the emissive factor
     *
     * @param emissiveFactor The emissive factor
     */
    public void setEmissiveFactor(double[] emissiveFactor)
    {
        this.emissiveFactor = emissiveFactor;
    }

    @Override
    public AlphaMode getAlphaMode()
    {
        return alphaMode;
    }

    /**
     * Set the alpha mode
     *
     * @param alphaMode The alpha mode
     */
    public void setAlphaMode(AlphaMode alphaMode)
    {
        this.alphaMode = alphaMode;
    }

    @Override
    public Double getAlphaCutoff()
    {
        return alphaCutoff;
    }

    /**
     * Set the alpha cutoff
     *
     * @param alphaCutoff The alpha cutoff
     */
    public void setAlphaCutoff(Double alphaCutoff)
    {
        this.alphaCutoff = alphaCutoff;
    }

    @Override
    public Boolean isDoubleSided()
    {
        return doubleSided;
    }

    /**
     * Set whether the material is double sided
     *
     * @param doubleSided Whether the material is double sided
     */
    public void setDoubleSided(Boolean doubleSided)
    {
        this.doubleSided = doubleSided;
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        if (pbrMetallicRoughnessModel != null)
        {
            modelElements.add(pbrMetallicRoughnessModel);
        }
        if (normalTextureInfoModel != null)
        {
            modelElements.add(normalTextureInfoModel);
        }
        if (occlusionTextureInfoModel != null)
        {
            modelElements.add(occlusionTextureInfoModel);
        }
        if (emissiveTextureInfoModel != null)
        {
            modelElements.add(emissiveTextureInfoModel);
        }
        return modelElements;
    }
    
}
