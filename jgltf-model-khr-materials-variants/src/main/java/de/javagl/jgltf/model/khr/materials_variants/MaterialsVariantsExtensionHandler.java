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
package de.javagl.jgltf.model.khr.materials_variants;

import java.util.List;

import de.javagl.jgltf.impl.v2.khr.materials_variants.GlTFMaterialsVariants;
import de.javagl.jgltf.impl.v2.khr.materials_variants.GlTFMaterialsVariantsPropertiesVariantsItems;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;

/**
 * Implementation of an {@link ExtensionHandler} for the 
 * <code>KHR_materials_variants</code> extension
 * that appears in a {@link GltfModel}.
 */
public class MaterialsVariantsExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_variants";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return GltfModel.class;
    }
    
    @Override
    public Class<?> getImplClass()
    {
        return GlTFMaterialsVariants.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MaterialsVariantsModel.class;
    }

    @Override
    public Object convertToModel(
        GltfModel gltfModel, Object owningModelObject, Object object)
    {
        DefaultMaterialsVariantsModel model = 
            new DefaultMaterialsVariantsModel();
        GlTFMaterialsVariants impl = (GlTFMaterialsVariants) object;
        List<GlTFMaterialsVariantsPropertiesVariantsItems> variants = 
            impl.getVariants();
        for (GlTFMaterialsVariantsPropertiesVariantsItems variant : variants)
        {
            model.addName(variant.getName());
        }
        return model;
    }

}
