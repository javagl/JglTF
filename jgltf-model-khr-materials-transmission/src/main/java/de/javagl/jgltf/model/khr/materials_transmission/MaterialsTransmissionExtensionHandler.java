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
package de.javagl.jgltf.model.khr.materials_transmission;

import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.impl.v2.khr.materials_transmission.MaterialMaterialsTransmission;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;
import de.javagl.jgltf.model.impl.TextureInfoModels;
import de.javagl.jgltf.model.impl.TextureInfos;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Implementation of an {@link ExtensionHandler} for the
 * <code>KHR_materials_transmission</code> extension
 */
public class MaterialsTransmissionExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_transmission";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsTransmission.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsTransmissionModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsTransmissionModel model =
            new DefaultMaterialsTransmissionModel();
        MaterialMaterialsTransmission impl =
            (MaterialMaterialsTransmission) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        List<TextureModel> textureModels = gltfModel.getTextureModels();

        model.setTransmissionFactor(impl.getTransmissionFactor());
        TextureInfo transmissionTextureInfo = impl.getTransmissionTexture();
        if (transmissionTextureInfo != null)
        {
            DefaultTextureInfoModel transmissionTextureInfoModel =
                TextureInfoModels.from(textureModels, transmissionTextureInfo);
            model.setTransmissionTexture(transmissionTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                transmissionTextureInfoModel, TextureInfoModel.class);
        }
        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMaterialsTransmissionModel model =
            (DefaultMaterialsTransmissionModel) modelObject;
        MaterialMaterialsTransmission impl =
            new MaterialMaterialsTransmission();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        impl.setTransmissionFactor(model.getTransmissionFactor());

        TextureInfoModel transmissionTextureInfoModel =
            model.getTransmissionTexture();
        TextureInfo transmissionTextureInfo =
            TextureInfos.from(gltfModel, transmissionTextureInfoModel);
        impl.setTransmissionTexture(transmissionTextureInfo);

        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        MaterialsTransmissionModel inputModel =
            (MaterialsTransmissionModel) modelObject;
        DefaultMaterialsTransmissionModel outputModel =
            new DefaultMaterialsTransmissionModel();
        modelElementMap.put(inputModel, outputModel);

        outputModel.setTransmissionFactor(inputModel.getTransmissionFactor());

        TextureInfoModel inputTransmissionTextureInfoModel =
            inputModel.getTransmissionTexture();
        TextureInfoModel outputTransmissionTextureInfoModel =
            TextureInfoModels.copy(gltfModel, inputTransmissionTextureInfoModel,
                modelElementMap);
        outputModel.setTransmissionTexture(outputTransmissionTextureInfoModel);

        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);

        return outputModel;
    }

}
