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
package de.javagl.jgltf.model.khr.draco_mesh_compression;

import de.javagl.jgltf.impl.v2.khr.draco_mesh_compression.MeshPrimitiveDracoMeshCompression;
import de.javagl.jgltf.model.ExtensionsModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.impl.DefaultExtensionsModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;

/**
 * Implementation of an {@link ExtensionHandler} for
 * <code>KHR_draco_mesh_compression</code>
 */
public class MeshPrimitiveDracoMeshCompressionExtensionHandler
    implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_draco_mesh_compression";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MeshPrimitiveModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MeshPrimitiveDracoMeshCompression.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return Object.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        int XXX; // TODO:
        System.out.println("Draco: Convert to model");

        MeshPrimitiveDracoMeshCompression impl =
            (MeshPrimitiveDracoMeshCompression) object;
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            (DefaultMeshPrimitiveModel) owningModelObject;

        DracoDecoding.decode(gltfModel, meshPrimitiveModel, impl);

        int XXX0; // TODO: Check if this removal is correct...
        meshPrimitiveModel.removeExtension("KHR_draco_mesh_compression");
        ExtensionsModel extensionsModel = gltfModel.getExtensionsModel();
        DefaultExtensionsModel defaultExtensionsModel =
            (DefaultExtensionsModel) extensionsModel;
        defaultExtensionsModel
            .removeExtensionUsed("KHR_draco_mesh_compression");

        return null;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        int XXX; // TODO:
        System.out.println("Draco: Convert to impl");
        return null;
    }
}
