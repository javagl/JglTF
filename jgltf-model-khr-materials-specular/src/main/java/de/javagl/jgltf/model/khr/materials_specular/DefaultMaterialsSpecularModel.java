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
package de.javagl.jgltf.model.khr.materials_specular;

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsSpecularModel}
 */
public class DefaultMaterialsSpecularModel extends AbstractModelElement
    implements MaterialsSpecularModel
{
    /**
     * The strength of the specular reflection. (optional)<br>
     * Default: 1.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     */
    private Double specularFactor;

    /**
     * A texture that defines the specular factor in the alpha channel.
     * (optional)
     */
    private TextureInfoModel specularTexture;

    /**
     * The F0 RGB color of the specular reflection. (optional)<br>
     * Default: [1.0,1.0,1.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)
     */
    private double[] specularColorFactor;

    /**
     * A texture that defines the F0 color of the specular reflection.
     * (optional)
     */
    private TextureInfoModel specularColorTexture;

    @Override
    public void setSpecularFactor(Double specularFactor)
    {
        this.specularFactor = specularFactor;
    }

    @Override
    public Double getSpecularFactor()
    {
        return this.specularFactor;
    }

    @Override
    public void setSpecularTexture(TextureInfoModel specularTexture)
    {
        this.specularTexture = specularTexture;
    }

    @Override
    public TextureInfoModel getSpecularTexture()
    {
        return this.specularTexture;
    }

    @Override
    public void setSpecularColorFactor(double[] specularColorFactor)
    {
        this.specularColorFactor = specularColorFactor;
    }

    @Override
    public double[] getSpecularColorFactor()
    {
        return this.specularColorFactor;
    }

    @Override
    public void setSpecularColorTexture(TextureInfoModel specularColorTexture)
    {
        this.specularColorTexture = specularColorTexture;
    }

    @Override
    public TextureInfoModel getSpecularColorTexture()
    {
        return this.specularColorTexture;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (specularTexture != null)
        {
            modelElements.add(specularTexture);
        }
        if (specularColorTexture != null)
        {
            modelElements.add(specularColorTexture);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(specularTexture))
        {
            setSpecularTexture(null);
        }
        if (modelElementsToRemove.contains(specularColorTexture))
        {
            setSpecularColorTexture(null);
        }
        return false;
    }

    @Override
    public String getExtensionName()
    {
        return "KHR_materials_specular";
    }

    @Override
    public boolean isRequired()
    {
        return false;
    }

}
