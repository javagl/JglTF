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
package de.javagl.jgltf.model.khr.materials_emissive_strength;

import java.util.Map;

import de.javagl.jgltf.impl.v2.khr.materials_emissive_strength.MaterialMaterialsEmissiveStrength;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.extensions.ExtensionHandler;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Implementation of an {@link ExtensionHandler} for the
 * <code>KHR_materials_emissive_strength</code> extension
 */
public class MaterialsEmissiveStrengthExtensionHandler
    implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_emissive_strength";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MaterialModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return MaterialMaterialsEmissiveStrength.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsEmissiveStrengthModel.class;
    }

    @Override
    public Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object object)
    {
        DefaultMaterialsEmissiveStrengthModel model =
            new DefaultMaterialsEmissiveStrengthModel();
        MaterialMaterialsEmissiveStrength impl =
            (MaterialMaterialsEmissiveStrength) object;
        ModelElementsV2.transferGltfPropertyElementsToModel(impl, model);
        model.setEmissiveStrength(impl.getEmissiveStrength());
        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMaterialsEmissiveStrengthModel model =
            (DefaultMaterialsEmissiveStrengthModel) modelObject;
        MaterialMaterialsEmissiveStrength impl =
            new MaterialMaterialsEmissiveStrength();
        ModelElementsV2.transferGltfPropertyElementsFromModel(model, impl);
        impl.setEmissiveStrength(model.getEmissiveStrength());
        return impl;
    }

    @Override
    public Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        MaterialsEmissiveStrengthModel inputModel =
            (MaterialsEmissiveStrengthModel) modelObject;
        DefaultMaterialsEmissiveStrengthModel outputModel =
            new DefaultMaterialsEmissiveStrengthModel();
        modelElementMap.put(inputModel, outputModel);
        outputModel.setEmissiveStrength(inputModel.getEmissiveStrength());
        ExtensionModels.copyExtensionModels(gltfModel, inputModel, outputModel,
            modelElementMap);
        return outputModel;
    }
}
