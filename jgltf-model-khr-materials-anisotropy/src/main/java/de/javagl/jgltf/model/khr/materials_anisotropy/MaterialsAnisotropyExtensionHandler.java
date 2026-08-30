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
package de.javagl.jgltf.model.khr.materials_anisotropy;

import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.impl.v2.khr.materials_anisotropy.MaterialMaterialsAnisotropy;
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
 * <code>KHR_materials_anisotropy</code> extension
 */
public class MaterialsAnisotropyExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_anisotropy";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsAnisotropy.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsAnisotropyModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsAnisotropyModel model =
            new DefaultMaterialsAnisotropyModel();
        MaterialMaterialsAnisotropy impl = (MaterialMaterialsAnisotropy) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        List<TextureModel> textureModels = gltfModel.getTextureModels();

        model.setAnisotropyStrength(impl.getAnisotropyStrength());
        model.setAnisotropyRotation(impl.getAnisotropyRotation());

        TextureInfo anisotropyTextureInfo = impl.getAnisotropyTexture();
        if (anisotropyTextureInfo != null)
        {
            DefaultTextureInfoModel anisotropyTextureInfoModel =
                TextureInfoModels.from(textureModels, anisotropyTextureInfo);
            model.setAnisotropyTexture(anisotropyTextureInfoModel);
            ExtensionModels.createExtensionModels(gltfModel,
                anisotropyTextureInfoModel, TextureInfoModel.class);
        }

        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMaterialsAnisotropyModel model =
            (DefaultMaterialsAnisotropyModel) modelObject;
        MaterialMaterialsAnisotropy impl = new MaterialMaterialsAnisotropy();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        impl.setAnisotropyStrength(model.getAnisotropyStrength());
        impl.setAnisotropyRotation(model.getAnisotropyRotation());

        TextureInfoModel anisotropyTextureInfoModel =
            model.getAnisotropyTexture();
        TextureInfo anisotropyTextureInfo =
            TextureInfos.from(gltfModel, anisotropyTextureInfoModel);
        impl.setAnisotropyTexture(anisotropyTextureInfo);

        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        MaterialsAnisotropyModel inputModel =
            (MaterialsAnisotropyModel) modelObject;
        DefaultMaterialsAnisotropyModel outputModel =
            new DefaultMaterialsAnisotropyModel();
        modelElementMap.put(inputModel, outputModel);

        outputModel.setAnisotropyStrength(inputModel.getAnisotropyStrength());
        outputModel.setAnisotropyRotation(inputModel.getAnisotropyRotation());

        TextureInfoModel inputAnisotropyTextureInfoModel =
            inputModel.getAnisotropyTexture();
        TextureInfoModel outputAnisotropyTextureInfoModel = TextureInfoModels
            .copy(gltfModel, inputAnisotropyTextureInfoModel, modelElementMap);
        outputModel.setAnisotropyTexture(outputAnisotropyTextureInfoModel);

        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);

        return outputModel;
    }

}
