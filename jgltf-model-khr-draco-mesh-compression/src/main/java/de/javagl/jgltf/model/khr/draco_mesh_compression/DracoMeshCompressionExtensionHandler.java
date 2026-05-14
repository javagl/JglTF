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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.javagl.jgltf.impl.v2.khr.draco_mesh_compression.MeshPrimitiveDracoMeshCompression;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.extensions.ExtensionProcessing;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultBufferViewModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Implementation of an {@link ExtensionHandler} for
 * <code>KHR_draco_mesh_compression</code>
 */
public class DracoMeshCompressionExtensionHandler implements ExtensionHandler
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
        return DracoMeshCompressionModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            (DefaultMeshPrimitiveModel) owningModelObject;
        DefaultDracoMeshCompressionModel model =
            new DefaultDracoMeshCompressionModel();
        MeshPrimitiveDracoMeshCompression impl =
            (MeshPrimitiveDracoMeshCompression) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        DracoDecoding.Result result =
            DracoDecoding.decode(gltfModel, meshPrimitiveModel, impl);
        meshPrimitiveModel.setIndices(result.indices);
        for (Entry<String, DefaultAccessorModel> entry : result.attributes
            .entrySet())
        {
            String attributeName = entry.getKey();
            AccessorModel attributeValue = entry.getValue();
            meshPrimitiveModel.putAttribute(attributeName, attributeValue);

            model.addAttribute(attributeName);
        }
        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultDracoMeshCompressionModel model =
            (DefaultDracoMeshCompressionModel) modelObject;
        MeshPrimitiveDracoMeshCompression impl =
            new MeshPrimitiveDracoMeshCompression();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        List<BufferViewModel> bufferViewModels =
            gltfModel.getBufferViewModels();
        BufferViewModel bufferViewModel = model.getDracoBufferViewModel();
        int bufferViewIndex = bufferViewModels.indexOf(bufferViewModel);

        impl.setBufferView(bufferViewIndex);
        impl.setAttributes(model.getDracoAttributeIds());
        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        DefaultDracoMeshCompressionModel inputModel =
            (DefaultDracoMeshCompressionModel) modelObject;
        DefaultDracoMeshCompressionModel outputModel =
            new DefaultDracoMeshCompressionModel();
        modelElementMap.put(inputModel, outputModel);
        ModelElementsV2.transferGltfPropertyElements(inputModel, outputModel);

        for (String attribute : inputModel.getAttributes())
        {
            outputModel.addAttribute(attribute);
        }
        outputModel
            .setDracoOptions(DracoOptions.copy(inputModel.getDracoOptions()));
        return outputModel;
    }

    @Override
    public void preprocess(GltfModel gltfModel,
        ExtensionProcessing extensionProcessing)
    {
        List<MeshModel> meshModels = gltfModel.getMeshModels();
        for (MeshModel meshModel : meshModels)
        {
            List<MeshPrimitiveModel> meshPrimitiveModels =
                meshModel.getMeshPrimitiveModels();
            for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitiveModels)
            {
                DefaultDracoMeshCompressionModel dracoMeshCompressionModel =
                    meshPrimitiveModel.getExtensionModel(
                        "KHR_draco_mesh_compression",
                        DefaultDracoMeshCompressionModel.class);
                if (dracoMeshCompressionModel != null)
                {
                    preprocess(gltfModel, meshPrimitiveModel,
                        dracoMeshCompressionModel, extensionProcessing);
                }
            }
        }
    }

    /**
     * Preprocess the given mesh primitive model with its associated draco
     * compression
     * 
     * @param gltfModel The glTF model
     * @param meshPrimitiveModel The mesh primitive model
     * @param dracoMeshCompressionModel The {@link DracoMeshCompressionModel}
     * @param extensionProcessing The {@link ExtensionProcessing}
     */
    private void preprocess(GltfModel gltfModel,
        MeshPrimitiveModel meshPrimitiveModel,
        DefaultDracoMeshCompressionModel dracoMeshCompressionModel,
        ExtensionProcessing extensionProcessing)
    {
        DracoEncoding.Result result = DracoEncoding.encode(gltfModel,
            meshPrimitiveModel, dracoMeshCompressionModel);

        // Collect all accessor models that have been draco-encoded
        List<AccessorModel> encodedAccessorModels =
            new ArrayList<AccessorModel>();
        encodedAccessorModels.add(meshPrimitiveModel.getIndices());

        // Collect the accessor models from the attributes that have
        // been draco-encoded, and assign the draco attribute IDs
        // to the draco mesh compression model
        Map<String, AccessorModel> attributes =
            meshPrimitiveModel.getAttributes();
        Set<String> dracoAttributeNames = result.dracoAttributeIds.keySet();
        for (String attributeName : dracoAttributeNames)
        {
            AccessorModel attribute = attributes.get(attributeName);
            encodedAccessorModels.add(attribute);
            Integer id = result.dracoAttributeIds.get(attributeName);
            dracoMeshCompressionModel.setDracoAttributeId(attributeName, id);
        }
        
        // Create the buffer view model that will contain the draco data 
        DefaultBufferViewModel bufferViewModel =
            new DefaultBufferViewModel(null);
        dracoMeshCompressionModel.setDracoBufferViewModel(bufferViewModel);
        
        // Pass the draco data to the extension processing callback
        ByteBuffer bufferViewData = ByteBuffer.wrap(result.dracoData);
        extensionProcessing.acceptAccessorEncoding(encodedAccessorModels,
            bufferViewModel, bufferViewData);
    }

}
