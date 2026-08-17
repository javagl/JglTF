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
package de.javagl.jgltf.model.khr.texture_basisu;

import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.khr.texture_basisu.TextureTextureBasisu;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Implementation of an {@link ExtensionHandler} for the
 * <code>KHR_texture_basisu</code> extension
 */
public class TextureBasisuExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_texture_basisu";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return TextureModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return TextureTextureBasisu.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return TextureBasisuModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultTextureBasisuModel model = new DefaultTextureBasisuModel();
        TextureTextureBasisu impl = (TextureTextureBasisu) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);

        int source = impl.getSource();
        List<ImageModel> imageModels = gltfModel.getImageModels();
        ImageModel imageModel = imageModels.get(source);
        model.setSource(imageModel);
        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultTextureBasisuModel model =
            (DefaultTextureBasisuModel) modelObject;
        TextureTextureBasisu impl = new TextureTextureBasisu();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);

        List<ImageModel> imageModels = gltfModel.getImageModels();
        ImageModel imageModel = model.getSource();
        int source = imageModels.indexOf(imageModel);
        impl.setSource(source);
        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        TextureBasisuModel inputModel = (TextureBasisuModel) modelObject;
        DefaultTextureBasisuModel outputModel = new DefaultTextureBasisuModel();
        modelElementMap.put(inputModel, outputModel);

        ImageModel inputSource = inputModel.getSource();
        ImageModel outputSource = (ImageModel) modelElementMap.get(inputSource);
        outputModel.setSource(outputSource);
        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);
        return outputModel;
    }

}
