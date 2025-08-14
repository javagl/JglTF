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
package de.javagl.jgltf.model.khr.texture_transform;

import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link TextureTransformModel}
 */
public class DefaultTextureTransformModel extends AbstractModelElement
    implements TextureTransformModel
{
    /**
     * The offset of the UV coordinate origin as a factor of the texture
     * dimensions.
     */
    private double[] offset;
    
    /**
     * Rotate the UVs by this many radians counter-clockwise around the origin.
     */
    private Double rotation;
    
    /**
     * The scale factor applied to the components of the UV coordinates.
     */
    private double[] scale;
    
    /**
     * Overrides the textureInfo texCoord value if supplied
     */
    private Integer texCoord;

    /**
     * Default constructor
     */
    public DefaultTextureTransformModel()
    {
        // Default constructor
    }

    @Override
    public void setOffset(double[] offset)
    {
        this.offset = offset;
    }

    @Override
    public double[] getOffset()
    {
        return this.offset;
    }

    @Override
    public void setRotation(Double rotation)
    {
        this.rotation = rotation;
    }

    @Override
    public Double getRotation()
    {
        return this.rotation;
    }

    @Override
    public void setScale(double[] scale)
    {
        this.scale = scale;
    }

    @Override
    public double[] getScale()
    {
        return this.scale;
    }

    @Override
    public void setTexCoord(Integer texCoord)
    {
        this.texCoord = texCoord;
    }

    @Override
    public Integer getTexCoord()
    {
        return this.texCoord;
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        return modelElements;
    }
    

}
