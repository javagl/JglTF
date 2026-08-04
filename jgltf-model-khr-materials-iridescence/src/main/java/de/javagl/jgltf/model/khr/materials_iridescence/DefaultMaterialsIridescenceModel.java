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
package de.javagl.jgltf.model.khr.materials_iridescence;

import java.util.Collection;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsIridescenceModel}
 */
public class DefaultMaterialsIridescenceModel extends AbstractModelElement
    implements MaterialsIridescenceModel
{
    /**
     * The iridescence intensity factor. (optional)<br>
     * Default: 0.0<br>
     * Minimum: 0.0 (inclusive)<br>
     * Maximum: 1.0 (inclusive)
     * 
     */
    private Double iridescenceFactor;
    /**
     * The iridescence intensity texture. (optional)
     * 
     */
    private TextureInfoModel iridescenceTexture;
    /**
     * The index of refraction of the dielectric thin-film layer. (optional)<br>
     * Default: 1.3<br>
     * Minimum: 1.0 (inclusive)
     * 
     */
    private Double iridescenceIor;
    /**
     * The minimum thickness of the thin-film layer given in nanometers.
     * (optional)<br>
     * Default: 100.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     */
    private Double iridescenceThicknessMinimum;
    /**
     * The maximum thickness of the thin-film layer given in nanometers.
     * (optional)<br>
     * Default: 400.0<br>
     * Minimum: 0.0 (inclusive)
     * 
     */
    private Double iridescenceThicknessMaximum;
    /**
     * The thickness texture of the thin-film layer. (optional)
     * 
     */
    private TextureInfoModel iridescenceThicknessTexture;

    @Override
    public void setIridescenceFactor(Double iridescenceFactor)
    {
        this.iridescenceFactor = iridescenceFactor;
    }

    @Override
    public Double getIridescenceFactor()
    {
        return this.iridescenceFactor;
    }

    @Override
    public void setIridescenceTexture(TextureInfoModel iridescenceTexture)
    {
        this.iridescenceTexture = iridescenceTexture;
    }

    @Override
    public TextureInfoModel getIridescenceTexture()
    {
        return this.iridescenceTexture;
    }

    @Override
    public void setIridescenceIor(Double iridescenceIor)
    {
        this.iridescenceIor = iridescenceIor;
    }

    @Override
    public Double getIridescenceIor()
    {
        return this.iridescenceIor;
    }

    @Override
    public void
        setIridescenceThicknessMinimum(Double iridescenceThicknessMinimum)
    {
        this.iridescenceThicknessMinimum = iridescenceThicknessMinimum;
    }

    @Override
    public Double getIridescenceThicknessMinimum()
    {
        return this.iridescenceThicknessMinimum;
    }

    @Override
    public void
        setIridescenceThicknessMaximum(Double iridescenceThicknessMaximum)
    {
        this.iridescenceThicknessMaximum = iridescenceThicknessMaximum;
    }

    @Override
    public Double getIridescenceThicknessMaximum()
    {
        return this.iridescenceThicknessMaximum;
    }

    @Override
    public void setIridescenceThicknessTexture(
        TextureInfoModel iridescenceThicknessTexture)
    {
        this.iridescenceThicknessTexture = iridescenceThicknessTexture;
    }

    @Override
    public TextureInfoModel getIridescenceThicknessTexture()
    {
        return this.iridescenceThicknessTexture;
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (iridescenceTexture != null)
        {
            modelElements.add(iridescenceTexture);
        }
        if (iridescenceThicknessTexture != null)
        {
            modelElements.add(iridescenceThicknessTexture);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(iridescenceTexture))
        {
            setIridescenceTexture(null);
        }
        if (modelElementsToRemove.contains(iridescenceThicknessTexture))
        {
            setIridescenceThicknessTexture(null);
        }
        return false;
    }

    @Override
    public String getExtensionName()
    {
        return "KHR_materials_iridescence";
    }

    @Override
    public boolean isRequired()
    {
        return false;
    }

}
