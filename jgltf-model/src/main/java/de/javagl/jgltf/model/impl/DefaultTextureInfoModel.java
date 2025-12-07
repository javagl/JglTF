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

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;

/**
 * Implementation of a {@link TextureInfoModel}
 */
public class DefaultTextureInfoModel extends AbstractNamedModelElement
    implements TextureInfoModel
{
    /**
     * The {@link ImageModel}
     */
    private TextureModel textureModel;
    
    /**
     * The texture coordinate set index
     */
    private Integer texCoord;
    
    /**
     * Creates a new instance
     */
    public DefaultTextureInfoModel()
    {
        // Default constructor
    }
    
    /**
     * Set the {@link TextureModel}
     * 
     * @param textureModel The {@link TextureModel}
     */
    public void setTextureModel(TextureModel textureModel)
    {
        this.textureModel = textureModel;
    }
    
    @Override
    public TextureModel getTextureModel()
    {
        return textureModel;
    }

    /**
     * Set the texture coordinate set index
     * 
     * @param texCoord The set index
     */
    public void setTexCoord(Integer texCoord)
    {
        this.texCoord = texCoord;
    }

    @Override
    public Integer getTexCoord() 
    {
        return texCoord;
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        if (textureModel != null)
        {
            modelElements.add(textureModel);
        }
        return modelElements;
    }
    
    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove) 
    {
        removeExtensionModelElements(modelElementsToRemove);
        if (modelElementsToRemove.contains(textureModel))
        {
            setTextureModel(null);
            return true;
        }
        return false;
    }
    
}
