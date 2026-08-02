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
package de.javagl.jgltf.model.khr.materials_sheen;

import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.impl.v2.khr.materials_sheen.MaterialMaterialsSheen;
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
 * <code>KHR_materials_sheen</code> extension
 */
public class MaterialsSheenExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_sheen";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsSheen.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsSheenModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsSheenModel model = new DefaultMaterialsSheenModel();
        MaterialMaterialsSheen impl = (MaterialMaterialsSheen) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        List<TextureModel> textureModels = gltfModel.getTextureModels();

        model.setSheenColorFactor(Optionals.clone(impl.getSheenColorFactor()));
        TextureInfo sheenColorTextureInfo = impl.getSheenColorTexture();
        if (sheenColorTextureInfo != null)
        {
            DefaultTextureInfoModel sheenColorTextureInfoModel =
                TextureInfoModels.from(textureModels, sheenColorTextureInfo);
            model.setSheenColorTextureInfoModel(sheenColorTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                sheenColorTextureInfoModel, TextureInfoModel.class);
        }

        model.setSheenRoughnessFactor(impl.getSheenRoughnessFactor());
        TextureInfo sheenRoughnessTextureInfo = impl.getSheenRoughnessTexture();
        if (sheenRoughnessTextureInfo != null)
        {
            DefaultTextureInfoModel sheenRoughnessTextureInfoModel =
                TextureInfoModels.from(textureModels,
                    sheenRoughnessTextureInfo);
            model.setSheenRoughnessTextureInfoModel(
                sheenRoughnessTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                sheenRoughnessTextureInfoModel, TextureInfoModel.class);
        }
        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMaterialsSheenModel model =
            (DefaultMaterialsSheenModel) modelObject;
        MaterialMaterialsSheen impl = new MaterialMaterialsSheen();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        impl.setSheenColorFactor(Optionals.clone(model.getSheenColorFactor()));
        impl.setSheenRoughnessFactor(model.getSheenRoughnessFactor());

        TextureInfoModel sheenColorTextureInfoModel =
            model.getSheenColorTextureInfoModel();
        TextureInfo sheenColorTextureInfo =
            TextureInfos.from(gltfModel, sheenColorTextureInfoModel);
        impl.setSheenColorTexture(sheenColorTextureInfo);

        TextureInfoModel sheenRoughnessTextureInfoModel =
            model.getSheenRoughnessTextureInfoModel();
        TextureInfo sheenRoughnessTextureInfo =
            TextureInfos.from(gltfModel, sheenRoughnessTextureInfoModel);
        impl.setSheenRoughnessTexture(sheenRoughnessTextureInfo);

        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        MaterialsSheenModel inputModel = (MaterialsSheenModel) modelObject;
        DefaultMaterialsSheenModel outputModel =
            new DefaultMaterialsSheenModel();
        modelElementMap.put(inputModel, outputModel);

        outputModel.setSheenColorFactor(
            Optionals.clone(inputModel.getSheenColorFactor()));
        outputModel
            .setSheenRoughnessFactor(inputModel.getSheenRoughnessFactor());

        TextureInfoModel inputSheenColorTextureInfoModel =
            inputModel.getSheenColorTextureInfoModel();
        TextureInfoModel outputSheenColorTextureInfoModel = TextureInfoModels
            .copy(gltfModel, inputSheenColorTextureInfoModel, modelElementMap);
        outputModel
            .setSheenColorTextureInfoModel(outputSheenColorTextureInfoModel);

        TextureInfoModel inputSheenRoughnessTextureInfoModel =
            inputModel.getSheenRoughnessTextureInfoModel();
        TextureInfoModel outputSheenRoughnessTextureInfoModel =
            TextureInfoModels.copy(gltfModel,
                inputSheenRoughnessTextureInfoModel, modelElementMap);
        outputModel.setSheenRoughnessTextureInfoModel(
            outputSheenRoughnessTextureInfoModel);

        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);

        return outputModel;
    }

}
