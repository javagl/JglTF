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
package de.javagl.jgltf.model.khr.texture_transform;

import java.util.Map;

import de.javagl.jgltf.impl.v2.khr.texture_transform.TextureInfoTextureTransform;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Implementation of an {@link ExtensionHandler} for 
 * <code>KHR_texture_transform</code>
 */
public class TextureTransformExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_texture_transform";
    }
    
    @Override
    public Class<?> getOwningModelClass()
    {
        return TextureInfoModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return TextureInfoTextureTransform.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return TextureTransformModel.class;
    }

    @Override
    public Object convertToModel(
        GltfModel gltfModel, Object owningModelObject, Object object)
    {
        DefaultTextureTransformModel model = 
            new DefaultTextureTransformModel();
        TextureInfoTextureTransform impl = 
            (TextureInfoTextureTransform) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(
            impl, model);
        model.setOffset(Optionals.clone(impl.getOffset()));
        model.setRotation(impl.getRotation());
        model.setScale(Optionals.clone(impl.getScale()));
        model.setTexCoord(impl.getTexCoord());
        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultTextureTransformModel model = 
            (DefaultTextureTransformModel)modelObject;
        TextureInfoTextureTransform impl = 
            new TextureInfoTextureTransform();
        ModelElementsV2.transferGltfPropertyElementsFromModel(
            model, impl);
        impl.setOffset(Optionals.clone(model.getOffset()));
        impl.setRotation(model.getRotation());
        impl.setScale(Optionals.clone(model.getScale()));
        impl.setTexCoord(model.getTexCoord());
        return impl;
    }
    
    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        TextureTransformModel inputModel = 
            (TextureTransformModel)modelObject;
        DefaultTextureTransformModel outputModel = 
            new DefaultTextureTransformModel();
        ModelElementsV2.transferGltfPropertyElements(inputModel, outputModel);
        modelElementMap.put(inputModel, outputModel);
        
        outputModel.setOffset(Optionals.clone(inputModel.getOffset()));
        outputModel.setRotation(inputModel.getRotation());
        outputModel.setScale(Optionals.clone(inputModel.getScale()));
        outputModel.setTexCoord(inputModel.getTexCoord());
        
        return outputModel;
    }
}
