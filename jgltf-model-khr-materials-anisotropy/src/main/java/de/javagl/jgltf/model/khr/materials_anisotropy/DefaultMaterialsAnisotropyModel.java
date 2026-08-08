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
package de.javagl.jgltf.model.khr.materials_anisotropy;

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsAnisotropyModel}
 */
public class DefaultMaterialsAnisotropyModel extends AbstractModelElement
    implements MaterialsAnisotropyModel
{
    /**
     * The anisotropy strength. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     */
    private Double anisotropyStrength;
    /**
     * The rotation of the anisotropy. (optional)<br>
     * Default: 0.0
     * 
     */
    private Double anisotropyRotation;
    /**
     * The anisotropy texture. (optional)
     * 
     */
    private TextureInfoModel anisotropyTexture;

    @Override
    public void setAnisotropyStrength(Double anisotropyStrength)
    {
        this.anisotropyStrength = anisotropyStrength;
    }

    @Override
    public Double getAnisotropyStrength()
    {
        return this.anisotropyStrength;
    }

    @Override
    public void setAnisotropyRotation(Double anisotropyRotation)
    {
        this.anisotropyRotation = anisotropyRotation;
    }

    @Override
    public Double getAnisotropyRotation()
    {
        return this.anisotropyRotation;
    }

    @Override
    public void setAnisotropyTexture(TextureInfoModel anisotropyTexture)
    {
        this.anisotropyTexture = anisotropyTexture;
    }

    @Override
    public TextureInfoModel getAnisotropyTexture()
    {
        return this.anisotropyTexture;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (anisotropyTexture != null)
        {
            modelElements.add(anisotropyTexture);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(anisotropyTexture))
        {
            setAnisotropyTexture(null);
        }
        return false;
    }

    @Override
    public String getExtensionName()
    {
        return "KHR_materials_anisotropy";
    }

    @Override
    public boolean isRequired()
    {
        return false;
    }

}
