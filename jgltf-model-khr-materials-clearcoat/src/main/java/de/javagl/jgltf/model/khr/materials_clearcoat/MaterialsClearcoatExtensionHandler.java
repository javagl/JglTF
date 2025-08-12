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
package de.javagl.jgltf.model.khr.materials_clearcoat;

import java.util.List;

import de.javagl.jgltf.impl.v2.MaterialNormalTextureInfo;
import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.impl.v2.khr.materials_clearcoat.MaterialMaterialsClearcoat;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.impl.DefaultNormalTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;
import de.javagl.jgltf.model.impl.TextureInfoModels;

/**
 * Implementation of an {@link ExtensionHandler} for the
 * <code>KHR_materials_clearcoat</code> extension
 */
public class MaterialsClearcoatExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_clearcoat";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsClearcoat.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsClearcoatModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsClearcoatModel model =
            new DefaultMaterialsClearcoatModel();
        MaterialMaterialsClearcoat impl = (MaterialMaterialsClearcoat) object;

        List<TextureModel> textureModels = gltfModel.getTextureModels();

        model.setClearcoatFactor(Optionals.of(impl.getClearcoatFactor(),
            impl.defaultClearcoatFactor()));
        TextureInfo clearcoatTextureInfo = impl.getClearcoatTexture();
        if (clearcoatTextureInfo != null)
        {
            DefaultTextureInfoModel clearcoatTextureInfoModel = 
                TextureInfoModels.from(textureModels, clearcoatTextureInfo);
            model.setClearcoatTextureInfoModel(clearcoatTextureInfoModel);
            ExtensionModels.process(gltfModel, clearcoatTextureInfoModel,
                TextureInfoModel.class);
        }

        model.setClearcoatRoughnessFactor(
            Optionals.of(impl.getClearcoatRoughnessFactor(),
                impl.defaultClearcoatRoughnessFactor()));
        TextureInfo clearcoatRoughnessTextureInfo =
            impl.getClearcoatRoughnessTexture();
        if (clearcoatRoughnessTextureInfo != null)
        {
            DefaultTextureInfoModel clearcoatRoughnessTextureInfoModel =
                TextureInfoModels.from(textureModels, clearcoatRoughnessTextureInfo);
            model.setClearcoatRoughnessTextureInfoModel(
                clearcoatRoughnessTextureInfoModel);
            ExtensionModels.process(gltfModel,
                clearcoatRoughnessTextureInfoModel, TextureInfoModel.class);
        }

        MaterialNormalTextureInfo clearcoatNormalTextureInfo =
            impl.getClearcoatNormalTexture();
        if (clearcoatNormalTextureInfo != null)
        {
            DefaultNormalTextureInfoModel clearcoatNormalTextureInfoModel =
                TextureInfoModels.from(textureModels, clearcoatNormalTextureInfo);
            model.setClearcoatNormalTextureInfoModel(
                clearcoatNormalTextureInfoModel);
            ExtensionModels.process(gltfModel, clearcoatNormalTextureInfoModel,
                TextureInfoModel.class);
        }
        return model;
    }


}
