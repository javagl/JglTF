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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MeshPrimitiveMaterialsVariantsModel}
 */
public class DefaultMeshPrimitiveMaterialsVariantsModel 
    extends AbstractModelElement
    implements MeshPrimitiveMaterialsVariantsModel
{
    /**
     * The mapping from variant names to material models 
     */
    private final Map<String, MaterialModel> materials;
    
    /**
     * The mapping from variant names to mapping names
     */
    private final Map<String, String> names;
    
    /**
     * Default constructor
     */
    public DefaultMeshPrimitiveMaterialsVariantsModel()
    {
        this.materials = new LinkedHashMap<String, MaterialModel>();
        this.names = new LinkedHashMap<String, String>();
    }
    
    @Override
    public MaterialModel getMaterialModel(String variantName)
    {
        return materials.get(variantName);
    }
    
    @Override
    public String getName(String variantName)
    {
        return names.get(variantName);
    }
    
    /**
     * Set the material model that should be used for the mesh primitive for 
     * the given variant name.
     * 
     * If the given material model is <code>null</code>, then the mapping
     * for the given variant name will be removed.
     * 
     * The caller is responsible for passing in a variant name that actually
     * appears in the {@link MaterialsVariantsModel} of the top-level glTF. 
     * 
     * @param variantName The variant name
     * @param materialModel The material model
     * @param mappingName The optional mapping name
     */
    public void setMaterialForVariant(String variantName, 
        MaterialModel materialModel, String mappingName)
    {
        if (materialModel == null)
        {
            this.materials.remove(variantName);
            this.names.remove(variantName);
        }
        else
        {
            this.materials.put(variantName, materialModel);
            this.names.put(variantName, mappingName);
        }
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        modelElements.addAll(materials.values());
        return modelElements;
    }
    
    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove) 
    {
        removeExtensionModelElements(modelElementsToRemove);
        Set<String> keysToRemove = new LinkedHashSet<String>();
        for (Entry<String, MaterialModel> entry : materials.entrySet())
        {
            String key = entry.getKey();
            MaterialModel value = entry.getValue();
            if (modelElementsToRemove.contains(value))
            {
                keysToRemove.add(key);
            }
        }
        for (String keyToRemove : keysToRemove)
        {
            materials.remove(keyToRemove);
        }
        return materials.isEmpty();
    }
    
    
}