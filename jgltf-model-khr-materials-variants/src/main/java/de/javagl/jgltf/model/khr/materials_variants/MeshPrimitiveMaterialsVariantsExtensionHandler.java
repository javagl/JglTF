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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.javagl.jgltf.impl.v2.khr.materials_variants.MeshPrimitiveMaterialsVariants;
import de.javagl.jgltf.impl.v2.khr.materials_variants.MeshPrimitiveMaterialsVariantsPropertiesMappingsItems;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.extensions.ExtensionHandler;

/**
 * Implementation of an {@link ExtensionHandler} for the 
 * <code>KHR_materials_variants</code> extension
 * that appears in a {@link MeshPrimitiveModel}.
 */
public class MeshPrimitiveMaterialsVariantsExtensionHandler 
    implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_materials_variants";
    }

    @Override
    public Class<?> getOwningModelClass()
    {
        return MeshPrimitiveModel.class;
    }
    
    @Override
    public Class<?> getImplClass()
    {
        return MeshPrimitiveMaterialsVariants.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return MeshPrimitiveMaterialsVariantsModel.class;
    }

    @Override
    public Object convertToModel(
        GltfModel gltfModel, Object owningModelObject, Object object)
    {
        DefaultMeshPrimitiveMaterialsVariantsModel model =
            new DefaultMeshPrimitiveMaterialsVariantsModel();
        MeshPrimitiveMaterialsVariants impl =
            (MeshPrimitiveMaterialsVariants) object;

        Map<String, Object> extensionModels = gltfModel.getExtensionModels();
        MaterialsVariantsModel materialVariantsModel =
            (MaterialsVariantsModel) extensionModels
                .get("KHR_materials_variants");
        List<String> variantNames = materialVariantsModel.getNames();

        List<MaterialModel> materialModels = gltfModel.getMaterialModels();

        List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> mappings =
            impl.getMappings();
        for (MeshPrimitiveMaterialsVariantsPropertiesMappingsItems mapping : 
            mappings)
        {
            String mappingName = mapping.getName();
            int material = mapping.getMaterial();
            MaterialModel materialModel = materialModels.get(material);

            List<Integer> variants = mapping.getVariants();

            for (int variant : variants)
            {
                String variantName = variantNames.get(variant);
                model.setMaterialForVariant(
                    variantName, materialModel, mappingName);
            }
        }
        return model;
    }

    @Override
    public Object convertToImpl(GltfModel gltfModel, Object modelObject)
    {
        DefaultMeshPrimitiveMaterialsVariantsModel model =
            (DefaultMeshPrimitiveMaterialsVariantsModel)modelObject;
        MeshPrimitiveMaterialsVariants impl =
            new MeshPrimitiveMaterialsVariants();
        
        Map<String, Object> extensionModels = gltfModel.getExtensionModels();
        MaterialsVariantsModel materialVariantsModel =
            (MaterialsVariantsModel) extensionModels
                .get("KHR_materials_variants");
        List<String> variantNames = materialVariantsModel.getNames();
        
        List<MaterialModel> materialModels = gltfModel.getMaterialModels();        
        Map<MaterialModel, List<Integer>> variantIndicesPerMaterial = 
            new LinkedHashMap<MaterialModel, List<Integer>>();
        Map<MaterialModel, String> namesPerMaterial =  
            new LinkedHashMap<MaterialModel, String>();
        
        for (int variantIndex = 0; 
            variantIndex < variantNames.size(); variantIndex++)
        {
            String variantName = variantNames.get(variantIndex);
            MaterialModel materialModel = model.getMaterialModel(variantName);

            List<Integer> variantIndices = 
                variantIndicesPerMaterial.get(materialModel);
            if (variantIndices == null)
            {
                variantIndices = new ArrayList<Integer>();
                variantIndicesPerMaterial.put(materialModel, variantIndices);
            }
            String name = model.getName(variantName);
            namesPerMaterial.put(materialModel, name);
            variantIndices.add(variantIndex);
        }

        for (Entry<MaterialModel, List<Integer>> entry : 
            variantIndicesPerMaterial.entrySet())
        {
            MeshPrimitiveMaterialsVariantsPropertiesMappingsItems element = 
                new MeshPrimitiveMaterialsVariantsPropertiesMappingsItems();
            
            MaterialModel materialModel = entry.getKey();
            String name = namesPerMaterial.get(materialModel);
            
            int materialIndex = materialModels.indexOf(materialModel);
            List<Integer> variantIndices = entry.getValue();
            
            element.setName(name);
            element.setMaterial(materialIndex);
            element.setVariants(variantIndices);
            impl.addMappings(element);
        }
        return impl;
    }
    
}
