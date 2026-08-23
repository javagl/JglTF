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
package de.javagl.jgltf.model.khr.mesh_quantization;

import java.util.Map;
import java.util.Map.Entry;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.extensions.ExtensionModels;

/**
 * Implementation of an {@link ExtensionHandler} for the
 * <code>KHR_mesh_quantization</code> extension
 */
public class MeshQuantizationExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_mesh_quantization";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MeshPrimitiveModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        // This has no implementation-side representation. It only allows
        // additional accessor types.
        return Void.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MeshQuantizationModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        MeshPrimitiveModel primitive = (MeshPrimitiveModel) owningModelObject;
        Map<String, AccessorModel> attributes = primitive.getAttributes();
        for (Entry<String, AccessorModel> entry : attributes.entrySet())
        {
            String attributeName = entry.getKey();
            AccessorModel attribute = entry.getValue();
            if (isRequiredFor(attributeName, attribute))
            {
                return new DefaultMeshQuantizationModel();
            }
        }
        return null;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        return null;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        if (modelObject == null)
        {
            return null;
        }
        MeshQuantizationModel inputModel = (MeshQuantizationModel) modelObject;
        DefaultMeshQuantizationModel outputModel =
            new DefaultMeshQuantizationModel();
        modelElementMap.put(inputModel, outputModel);
        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);
        return outputModel;

    }

    /**
     * Returns whether the KHR_mesh_quantization is required for the given
     * attribute in a mesh primitive.
     * 
     * See
     * https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html#meshes-overview
     * 
     * @param attributeName The attribute name
     * @param attribute The attribute
     * @return Whether the extension is required
     */
    private static boolean isRequiredFor(String attributeName,
        AccessorModel attribute)
    {
        int componentType = attribute.getComponentType();
        if ("POSITION".equals(attributeName))
        {
            return componentType != GltfConstants.GL_FLOAT;
        }
        if ("NORMAL".equals(attributeName))
        {
            return componentType != GltfConstants.GL_FLOAT;
        }
        if ("TANGENT".equals(attributeName))
        {
            return componentType != GltfConstants.GL_FLOAT;
        }

        if (attributeName.startsWith("TEXCOORD_"))
        {
            if (componentType == GltfConstants.GL_FLOAT)
            {
                return true;
            }
            if (componentType == GltfConstants.GL_UNSIGNED_BYTE)
            {
                return attribute.isNormalized();
            }
            if (componentType == GltfConstants.GL_UNSIGNED_SHORT)
            {
                return attribute.isNormalized();
            }
            return true;
        }
        return false;
    }

}
