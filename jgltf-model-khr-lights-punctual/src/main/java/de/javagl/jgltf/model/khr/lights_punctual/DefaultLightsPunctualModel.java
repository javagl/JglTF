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
package de.javagl.jgltf.model.khr.lights_punctual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link LightsPunctualModel}
 */
public class DefaultLightsPunctualModel extends AbstractModelElement 
    implements LightsPunctualModel
{
    /**
     * The {@link LightModel} instances
     */
    private final List<LightModel> lightModels;
    
    /**
     * Creates a new instance
     */
    public DefaultLightsPunctualModel()
    {
        this.lightModels = new ArrayList<LightModel>();
    }
    
    @Override
    public List<LightModel> getLightModels()
    {
        return Collections.unmodifiableList(lightModels);
    }
    
    /**
     * Add the given {@link LightModel} to this instance
     * 
     * @param lightModel The {@link LightModel}
     */
    public void addLightModel(LightModel lightModel) 
    {
        this.lightModels.add(lightModel);
    }

    /**
     * Remove the given {@link LightModel} from this instance
     * 
     * @param lightModel The {@link LightModel}
     */
    public void removeLightModel(LightModel lightModel) 
    {
        this.lightModels.remove(lightModel);
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        modelElements.addAll(lightModels);
        return modelElements;
    }
    
}
