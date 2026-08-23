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
package de.javagl.jgltf.model.khr.materials_volume;

import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.impl.v2.khr.materials_volume.MaterialMaterialsVolume;
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
 * <code>KHR_materials_volume</code> extension
 */
public class MaterialsVolumeExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_volume";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsVolume.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsVolumeModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsVolumeModel model = new DefaultMaterialsVolumeModel();
        MaterialMaterialsVolume impl = (MaterialMaterialsVolume) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        List<TextureModel> textureModels = gltfModel.getTextureModels();

        model.setThicknessFactor(impl.getThicknessFactor());
        model.setAttenuationDistance(impl.getAttenuationDistance());
        model.setAttenuationColor(Optionals.clone(impl.getAttenuationColor()));
        TextureInfo thicknessTextureInfo = impl.getThicknessTexture();
        if (thicknessTextureInfo != null)
        {
            DefaultTextureInfoModel thicknessTextureInfoModel =
                TextureInfoModels.from(textureModels, thicknessTextureInfo);
            model.setThicknessTexture(thicknessTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                thicknessTextureInfoModel, TextureInfoModel.class);
        }

        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMaterialsVolumeModel model =
            (DefaultMaterialsVolumeModel) modelObject;
        MaterialMaterialsVolume impl = new MaterialMaterialsVolume();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        impl.setThicknessFactor(model.getThicknessFactor());
        impl.setAttenuationDistance(model.getAttenuationDistance());
        impl.setAttenuationColor(Optionals.clone(model.getAttenuationColor()));

        TextureInfoModel thicknessTextureInfoModel =
            model.getThicknessTexture();
        TextureInfo thicknessTextureInfo =
            TextureInfos.from(gltfModel, thicknessTextureInfoModel);
        impl.setThicknessTexture(thicknessTextureInfo);

        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        MaterialsVolumeModel inputModel = (MaterialsVolumeModel) modelObject;
        DefaultMaterialsVolumeModel outputModel =
            new DefaultMaterialsVolumeModel();
        modelElementMap.put(inputModel, outputModel);

        outputModel.setThicknessFactor(inputModel.getThicknessFactor());
        outputModel.setAttenuationDistance(inputModel.getAttenuationDistance());
        outputModel.setAttenuationColor(
            Optionals.clone(inputModel.getAttenuationColor()));

        TextureInfoModel inputThicknessTextureInfoModel =
            inputModel.getThicknessTexture();
        TextureInfoModel outputThicknessTextureInfoModel = TextureInfoModels
            .copy(gltfModel, inputThicknessTextureInfoModel, modelElementMap);
        outputModel.setThicknessTexture(outputThicknessTextureInfoModel);

        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);

        return outputModel;
    }

}
