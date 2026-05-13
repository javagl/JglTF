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
package de.javagl.jgltf.model.ext.mesh_gpu_instancing;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.javagl.jgltf.impl.v2.ext.mesh_gpu_instancing.GlTFMeshGpuInstancing;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Implementation of an {@link ExtensionHandler} for 
 * <code>KHR_texture_transform</code>
 */
public class MeshGpuInstancingExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "EXT_mesh_gpu_instancing";
    }
    
    @Override
    public Class<?> getOwningModelClass()
    {
        return NodeModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return GlTFMeshGpuInstancing.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return DefaultMeshGpuInstancingModel.class;
    }

    @Override
    public Object convertToModel(
        GltfModel gltfModel, Object owningModelObject, Object object)
    {
        GlTFMeshGpuInstancing impl = (GlTFMeshGpuInstancing) object;
        DefaultMeshGpuInstancingModel model = 
            new DefaultMeshGpuInstancingModel();
        ModelElementsV2.transferGltfPropertyElementsToModel(
            impl, model);
        
        Map<String, Integer> attributes = impl.getAttributes();
        if (attributes != null)
        {
            List<AccessorModel> accessorModels = gltfModel.getAccessorModels();
            for (Entry<String, Integer> entry : attributes.entrySet())
            {
                String key = entry.getKey();
                int index = entry.getValue();
                AccessorModel accessorModel = accessorModels.get(index);
                model.setAttribute(key, accessorModel);
            }
        }
        
        return model;
    }

    @Override
    public Object convertToImpl(
        GltfModel gltfModel, Object modelObject)
    {
        GlTFMeshGpuInstancing impl = new GlTFMeshGpuInstancing();
        MeshGpuInstancingModel model = (MeshGpuInstancingModel) modelObject;
        ModelElementsV2.transferGltfPropertyElementsFromModel(
            model, impl);
        
        List<AccessorModel> accessorModels = gltfModel.getAccessorModels();
        Map<String, AccessorModel> attributes = model.getAttributes();
        for (Entry<String, AccessorModel> entry : attributes.entrySet())
        {
            String key = entry.getKey();
            AccessorModel value = entry.getValue();
            int index = accessorModels.indexOf(value);
            impl.addAttributes(key, index);
        }
        
        return impl;
    }

}
