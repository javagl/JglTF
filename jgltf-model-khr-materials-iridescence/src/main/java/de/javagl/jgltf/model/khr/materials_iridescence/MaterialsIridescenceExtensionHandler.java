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
package de.javagl.jgltf.model.khr.materials_iridescence;

import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.impl.v2.khr.materials_iridescence.MaterialMaterialsIridescence;
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
 * <code>KHR_materials_iridescence</code> extension
 */
public class MaterialsIridescenceExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_iridescence";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsIridescence.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsIridescenceModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsIridescenceModel model =
            new DefaultMaterialsIridescenceModel();
        MaterialMaterialsIridescence impl =
            (MaterialMaterialsIridescence) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        List<TextureModel> textureModels = gltfModel.getTextureModels();

        model.setIridescenceFactor(impl.getIridescenceFactor());
        model.setIridescenceIor(impl.getIridescenceIor());

        TextureInfo iridescenceTextureInfo = impl.getIridescenceTexture();
        if (iridescenceTextureInfo != null)
        {
            DefaultTextureInfoModel iridescenceTextureInfoModel =
                TextureInfoModels.from(textureModels, iridescenceTextureInfo);
            model.setIridescenceTexture(iridescenceTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                iridescenceTextureInfoModel, TextureInfoModel.class);
        }

        model.setIridescenceThicknessMinimum(
            impl.getIridescenceThicknessMinimum());
        model.setIridescenceThicknessMaximum(
            impl.getIridescenceThicknessMaximum());

        TextureInfo iridescenceThicknessTextureInfo =
            impl.getIridescenceThicknessTexture();
        if (iridescenceThicknessTextureInfo != null)
        {
            DefaultTextureInfoModel iridescenceThicknessTextureInfoModel =
                TextureInfoModels.from(textureModels,
                    iridescenceThicknessTextureInfo);
            model.setIridescenceThicknessTexture(
                iridescenceThicknessTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                iridescenceThicknessTextureInfoModel, TextureInfoModel.class);
        }

        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMaterialsIridescenceModel model =
            (DefaultMaterialsIridescenceModel) modelObject;
        MaterialMaterialsIridescence impl = new MaterialMaterialsIridescence();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        impl.setIridescenceFactor(model.getIridescenceFactor());
        impl.setIridescenceIor(model.getIridescenceIor());

        TextureInfoModel iridescenceTextureInfoModel =
            model.getIridescenceTexture();
        TextureInfo iridescenceTextureInfo =
            TextureInfos.from(gltfModel, iridescenceTextureInfoModel);
        impl.setIridescenceTexture(iridescenceTextureInfo);

        impl.setIridescenceThicknessMinimum(
            model.getIridescenceThicknessMinimum());
        impl.setIridescenceThicknessMaximum(
            model.getIridescenceThicknessMaximum());

        TextureInfoModel iridescenceThicknessTextureInfoModel =
            model.getIridescenceThicknessTexture();
        TextureInfo iridescenceThicknessTextureInfo =
            TextureInfos.from(gltfModel, iridescenceThicknessTextureInfoModel);
        impl.setIridescenceThicknessTexture(iridescenceThicknessTextureInfo);

        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        MaterialsIridescenceModel inputModel =
            (MaterialsIridescenceModel) modelObject;
        DefaultMaterialsIridescenceModel outputModel =
            new DefaultMaterialsIridescenceModel();
        modelElementMap.put(inputModel, outputModel);

        outputModel.setIridescenceFactor(inputModel.getIridescenceFactor());
        outputModel.setIridescenceIor(inputModel.getIridescenceIor());

        TextureInfoModel inputIridescenceTextureInfoModel =
            inputModel.getIridescenceTexture();
        TextureInfoModel outputIridescenceTextureInfoModel = TextureInfoModels
            .copy(gltfModel, inputIridescenceTextureInfoModel, modelElementMap);
        outputModel.setIridescenceTexture(outputIridescenceTextureInfoModel);

        outputModel.setIridescenceThicknessMinimum(
            inputModel.getIridescenceThicknessMinimum());
        outputModel.setIridescenceThicknessMaximum(
            inputModel.getIridescenceThicknessMaximum());

        TextureInfoModel inputIridescenceThicknessTextureInfoModel =
            inputModel.getIridescenceThicknessTexture();
        TextureInfoModel outputIridescenceThicknessRoughnessTextureInfoModel =
            TextureInfoModels.copy(gltfModel,
                inputIridescenceThicknessTextureInfoModel, modelElementMap);
        outputModel.setIridescenceThicknessTexture(
            outputIridescenceThicknessRoughnessTextureInfoModel);

        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);

        return outputModel;
    }

}
