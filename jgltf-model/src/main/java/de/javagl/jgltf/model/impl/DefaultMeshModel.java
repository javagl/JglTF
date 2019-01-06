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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.Optionals;

/**
 * Implementation of a {@link MeshModel}
 */
public final class DefaultMeshModel extends AbstractNamedModelElement
    implements MeshModel
{
    /**
     * The {@link MeshPrimitiveModel} instances 
     */
    private final List<MeshPrimitiveModel> meshPrimitiveModels;
    
    /**
     * The morph target weights
     */
    private float weights[];
    
    /**
     * Creates a new instance
     */
    public DefaultMeshModel()
    {
        this.meshPrimitiveModels = new ArrayList<MeshPrimitiveModel>();
    }

    /**
     * Add a {@link MeshPrimitiveModel}
     * 
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel} to add
     */
    public void addMeshPrimitiveModel(MeshPrimitiveModel meshPrimitiveModel)
    {
        this.meshPrimitiveModels.add(meshPrimitiveModel);
    }
    
    /**
     * Set the default morph target weights to be a copy of the given array,
     * or <code>null</code> if the given array is <code>null</code>.
     * 
     * @param weights the default morph target weights
     */
    public void setWeights(float[] weights)
    {
        this.weights = Optionals.clone(weights);
    }
    
    @Override
    public List<MeshPrimitiveModel> getMeshPrimitiveModels()
    {
        return Collections.unmodifiableList(meshPrimitiveModels);
    }
    
    @Override
    public float[] getWeights()
    {
        return Optionals.clone(weights);
    }

}
