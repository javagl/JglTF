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
    private TextureInfoModel clearcoatTextureInfoModel;
    
    /**
     * The clearcoat layer roughness.
     */
    private Double clearcoatRoughnessFactor;

    /**
     * The clearcoat layer roughness texture info
     */
    private TextureInfoModel clearcoatRoughnessTextureInfoModel;

    /**
     * The clearcoat normal map texture info
     */
    private NormalTextureInfoModel clearcoatNormalTextureInfoModel;

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
    public void setClearcoatTextureInfoModel(
        TextureInfoModel clearcoatTextureInfoModel)
    {
        this.clearcoatTextureInfoModel = clearcoatTextureInfoModel;
    }

    @Override
    public TextureInfoModel getClearcoatTextureInfoModel()
    {
        return this.clearcoatTextureInfoModel;
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
    public void setClearcoatRoughnessTextureInfoModel(
        TextureInfoModel clearcoatRoughnessTextureInfoModel)
    {
        this.clearcoatRoughnessTextureInfoModel = 
            clearcoatRoughnessTextureInfoModel;
    }

    @Override
    public TextureInfoModel getClearcoatRoughnessTextureInfoModel()
    {
        return this.clearcoatRoughnessTextureInfoModel;
    }

    @Override
    public void setClearcoatNormalTextureInfoModel(
        NormalTextureInfoModel clearcoatNormalTextureInfoModel)
    {
        this.clearcoatNormalTextureInfoModel = clearcoatNormalTextureInfoModel;
    }

    @Override
    public NormalTextureInfoModel getClearcoatNormalTextureInfoModel()
    {
        return this.clearcoatNormalTextureInfoModel;
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        if (clearcoatTextureInfoModel != null)
        {
            modelElements.add(clearcoatTextureInfoModel);
        }
        if (clearcoatRoughnessTextureInfoModel != null)
        {
            modelElements.add(clearcoatRoughnessTextureInfoModel);
        }
        if (clearcoatNormalTextureInfoModel != null)
        {
            modelElements.add(clearcoatNormalTextureInfoModel);
        }
        return modelElements;
    }
    
    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove) 
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(clearcoatTextureInfoModel)) 
        {
            setClearcoatTextureInfoModel(null);
        }
        if (modelElementsToRemove.contains(clearcoatRoughnessTextureInfoModel)) 
        {
            setClearcoatRoughnessTextureInfoModel(null);
        }
        if (modelElementsToRemove.contains(clearcoatNormalTextureInfoModel)) 
        {
            setClearcoatNormalTextureInfoModel(null);
        }
        return false;
    }
    
    
}
