/*
 * www.javagl.de - JglTF
 *
 * Copyright 2025 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.khr.materials_clearcoat;

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsClearcoatModel}
 */
public class DefaultMaterialsClearcoatModel 
    extends AbstractModelElement 
    implements MaterialsClearcoatModel
{
    /**
     * The clearcoat layer intensity.
     */
    private Double clearcoatFactor;

    /**
     * The clearcoat texture info model
     */
    private TextureInfoModel clearcoatTexture;
    
    /**
     * The clearcoat layer roughness.
     */
    private Double clearcoatRoughnessFactor;

    /**
     * The clearcoat layer roughness texture info
     */
    private TextureInfoModel clearcoatRoughnessTexture;

    /**
     * The clearcoat normal map texture info
     */
    private NormalTextureInfoModel clearcoatNormalTexture;

    @Override
    public void setClearcoatFactor(Double clearcoatFactor)
    {
        this.clearcoatFactor = clearcoatFactor;
    }

    @Override
    public Double getClearcoatFactor()
    {
        return this.clearcoatFactor;
    }

    @Override
    public void setClearcoatTexture(
        TextureInfoModel clearcoatTexture)
    {
        this.clearcoatTexture = clearcoatTexture;
    }

    @Override
    public TextureInfoModel getClearcoatTexture()
    {
        return this.clearcoatTexture;
    }

    @Override
    public void setClearcoatRoughnessFactor(Double clearcoatRoughnessFactor)
    {
        this.clearcoatRoughnessFactor = clearcoatRoughnessFactor;
    }

    @Override
    public Double getClearcoatRoughnessFactor()
    {
        return this.clearcoatRoughnessFactor;
    }

    @Override
    public void setClearcoatRoughnessTexture(
        TextureInfoModel clearcoatRoughnessTexture)
    {
        this.clearcoatRoughnessTexture = 
            clearcoatRoughnessTexture;
    }

    @Override
    public TextureInfoModel getClearcoatRoughnessTexture()
    {
        return this.clearcoatRoughnessTexture;
    }

    @Override
    public void setClearcoatNormalTexture(
        NormalTextureInfoModel clearcoatNormalTexture)
    {
        this.clearcoatNormalTexture = clearcoatNormalTexture;
    }

    @Override
    public NormalTextureInfoModel getClearcoatNormalTexture()
    {
        return this.clearcoatNormalTexture;
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        if (clearcoatTexture != null)
        {
            modelElements.add(clearcoatTexture);
        }
        if (clearcoatRoughnessTexture != null)
        {
            modelElements.add(clearcoatRoughnessTexture);
        }
        if (clearcoatNormalTexture != null)
        {
            modelElements.add(clearcoatNormalTexture);
        }
        return modelElements;
    }
    
    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove) 
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(clearcoatTexture)) 
        {
            setClearcoatTexture(null);
        }
        if (modelElementsToRemove.contains(clearcoatRoughnessTexture)) 
        {
            setClearcoatRoughnessTexture(null);
        }
        if (modelElementsToRemove.contains(clearcoatNormalTexture)) 
        {
            setClearcoatNormalTexture(null);
        }
        return false;
    }
    
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_clearcoat";
    }
    
    @Override
    public boolean isRequired()
    {
        return false;
    }
    
}
