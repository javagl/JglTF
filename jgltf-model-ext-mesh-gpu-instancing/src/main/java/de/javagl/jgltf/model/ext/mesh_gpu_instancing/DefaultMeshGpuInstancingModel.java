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
package de.javagl.jgltf.model.ext.mesh_gpu_instancing;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MeshGpuInstancingModel}
 */
public class DefaultMeshGpuInstancingModel extends AbstractModelElement
    implements MeshGpuInstancingModel
{
    /**
     * The instancing attributes
     */
    private final Map<String, AccessorModel> attributes;
    
    /**
     * Default constructor
     */
    public DefaultMeshGpuInstancingModel()
    {
        this.attributes = new LinkedHashMap<String, AccessorModel>();
    }
    
    /**
     * Set the accessor data for the given instancing attribute.
     * 
     * If the given accessor model is <code>null</code>, then the
     * attribute will be removed.
     * 
     * @param name The name
     * @param accessorModel The instancing data accessor model
     */
    public void setAttribute(String name, AccessorModel accessorModel)
    {
        Objects.requireNonNull(name, "The name may not be null");
        if (accessorModel == null)
        {
            attributes.remove(name);
        }
        else
        {
            attributes.put(name, accessorModel);
        }
    }

    @Override
    public Map<String, AccessorModel> getAttributes()
    {
        return Collections.unmodifiableMap(attributes);
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        modelElements.addAll(attributes.values());
        return modelElements;
    }
    
}
