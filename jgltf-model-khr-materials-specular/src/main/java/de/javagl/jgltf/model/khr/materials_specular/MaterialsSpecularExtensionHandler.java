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
package de.javagl.jgltf.model.khr.materials_specular;

import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.impl.v2.khr.materials_specular.MaterialMaterialsSpecular;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.Optionals;
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
 * <code>KHR_materials_specular</code> extension
 */
public class MaterialsSpecularExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_specular";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsSpecular.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsSpecularModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsSpecularModel model =
            new DefaultMaterialsSpecularModel();
        MaterialMaterialsSpecular impl = (MaterialMaterialsSpecular) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        List<TextureModel> textureModels = gltfModel.getTextureModels();

        model.setSpecularFactor(impl.getSpecularFactor());
        TextureInfo specularTextureInfo = impl.getSpecularTexture();
        if (specularTextureInfo != null)
        {
            DefaultTextureInfoModel specularTextureInfoModel =
                TextureInfoModels.from(textureModels, specularTextureInfo);
            model.setSpecularTexture(specularTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                specularTextureInfoModel, TextureInfoModel.class);
        }

        model.setSpecularColorFactor(
            Optionals.clone(impl.getSpecularColorFactor()));
        TextureInfo specularColorTextureInfo = impl.getSpecularColorTexture();
        if (specularColorTextureInfo != null)
        {
            DefaultTextureInfoModel specularColorTextureInfoModel =
                TextureInfoModels.from(textureModels, specularColorTextureInfo);
            model.setSpecularColorTexture(specularColorTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                specularColorTextureInfoModel, TextureInfoModel.class);
        }

        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMaterialsSpecularModel model =
            (DefaultMaterialsSpecularModel) modelObject;
        MaterialMaterialsSpecular impl = new MaterialMaterialsSpecular();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        impl.setSpecularFactor(model.getSpecularFactor());

        TextureInfoModel specularTextureInfoModel = model.getSpecularTexture();
        TextureInfo specularTextureInfo =
            TextureInfos.from(gltfModel, specularTextureInfoModel);
        impl.setSpecularTexture(specularTextureInfo);

        impl.setSpecularColorFactor(
            Optionals.clone(model.getSpecularColorFactor()));

        TextureInfoModel specularColorTextureInfoModel =
            model.getSpecularColorTexture();
        TextureInfo specularColorTextureInfo =
            TextureInfos.from(gltfModel, specularColorTextureInfoModel);
        impl.setSpecularColorTexture(specularColorTextureInfo);

        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        MaterialsSpecularModel inputModel =
            (MaterialsSpecularModel) modelObject;
        DefaultMaterialsSpecularModel outputModel =
            new DefaultMaterialsSpecularModel();
        modelElementMap.put(inputModel, outputModel);

        outputModel.setSpecularFactor(inputModel.getSpecularFactor());

        TextureInfoModel inputSpecularTextureInfoModel =
            inputModel.getSpecularTexture();
        TextureInfoModel outputSpecularTextureInfoModel = TextureInfoModels
            .copy(gltfModel, inputSpecularTextureInfoModel, modelElementMap);
        outputModel.setSpecularTexture(outputSpecularTextureInfoModel);

        outputModel.setSpecularColorFactor(
            Optionals.clone(inputModel.getSpecularColorFactor()));

        TextureInfoModel inputSpecularColorTextureInfoModel =
            inputModel.getSpecularColorTexture();
        TextureInfoModel outputSpecularColorTextureInfoModel =
            TextureInfoModels.copy(gltfModel,
                inputSpecularColorTextureInfoModel, modelElementMap);
        outputModel
            .setSpecularColorTexture(outputSpecularColorTextureInfoModel);

        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);

        return outputModel;
    }

}
