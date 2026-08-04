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
    private NormalTextureInfoModel normalTexture;
    
    /**
     * The {@link OcclusionTextureInfoModel}
     */
    private OcclusionTextureInfoModel occlusionTexture;
    
    /**
     * The emissive {@link TextureInfoModel}
     */
    private TextureInfoModel emissiveTexture;
    
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
    private Boolean doubleSided;

    /**
     * Creates a new instance with default values
     */
    public DefaultPbrMaterialModel()
    {
        pbrMetallicRoughnessModel = null;
        normalTexture = null;
        occlusionTexture = null;
        emissiveTexture = null;
        emissiveFactor = null;
        alphaMode = null;
        alphaCutoff = null;
        doubleSided = null;
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
    public NormalTextureInfoModel getNormalTexture()
    {
        return normalTexture;
    }
    
    @Override
    public void
        setNormalTexture(NormalTextureInfoModel normalTexture)
    {
        this.normalTexture = normalTexture;
    }

    @Override
    public OcclusionTextureInfoModel getOcclusionTexture()
    {
        return occlusionTexture;
    }
    
    @Override
    public void setOcclusionTexture(
        OcclusionTextureInfoModel occlusionTexture)
    {
        this.occlusionTexture = occlusionTexture;
    }

    @Override
    public TextureInfoModel getEmissiveTexture()
    {
        return emissiveTexture;
    }
    
    @Override
    public void
        setEmissiveTexture(TextureInfoModel emissiveTexture)
    {
        this.emissiveTexture = emissiveTexture;
    }

    @Override
    public double[] getEmissiveFactor()
    {
        return emissiveFactor;
    }

    @Override
    public void setEmissiveFactor(double[] emissiveFactor)
    {
        this.emissiveFactor = emissiveFactor;
    }

    @Override
    public AlphaMode getAlphaMode()
    {
        return alphaMode;
    }

    @Override
    public void setAlphaMode(AlphaMode alphaMode)
    {
        this.alphaMode = alphaMode;
    }

    @Override
    public Double getAlphaCutoff()
    {
        return alphaCutoff;
    }

    @Override
    public void setAlphaCutoff(Double alphaCutoff)
    {
        this.alphaCutoff = alphaCutoff;
    }

    @Override
    public Boolean isDoubleSided()
    {
        return doubleSided;
    }

    @Override
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
        if (normalTexture != null)
        {
            modelElements.add(normalTexture);
        }
        if (occlusionTexture != null)
        {
            modelElements.add(occlusionTexture);
        }
        if (emissiveTexture != null)
        {
            modelElements.add(emissiveTexture);
        }
        return modelElements;
    }
    
    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(pbrMetallicRoughnessModel)) 
        {
            setPbrMetallicRoughnessModel(null);
        }
        if (modelElementsToRemove.contains(normalTexture)) 
        {
            setNormalTexture(null);
        }
        if (modelElementsToRemove.contains(occlusionTexture)) 
        {
            setOcclusionTexture(null);
        }
        if (modelElementsToRemove.contains(emissiveTexture)) 
        {
            setEmissiveFactor(null);
        }
        return false;
    }
    
}
