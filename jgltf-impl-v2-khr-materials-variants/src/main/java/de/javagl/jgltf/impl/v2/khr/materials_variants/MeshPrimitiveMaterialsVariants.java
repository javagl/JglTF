/*
 * KHR_materials_variants JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_variants;

import java.util.ArrayList;
import java.util.List;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * Auto-generated for mesh.primitive.KHR_materials_variants.schema.json 
 * 
 */
public class MeshPrimitiveMaterialsVariants
    extends GlTFProperty
{

    /**
     * A list of material to variant mappings (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> mappings;

    /**
     * A list of material to variant mappings (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param mappings The mappings to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setMappings(List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> mappings) {
        if (mappings == null) {
            throw new NullPointerException((("Invalid value for mappings: "+ mappings)+", may not be null"));
        }
        if (mappings.size()< 1) {
            throw new IllegalArgumentException("Number of mappings elements is < 1");
        }
        this.mappings = mappings;
    }

    /**
     * A list of material to variant mappings (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The mappings
     * 
     */
    public List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> getMappings() {
        return this.mappings;
    }

    /**
     * Add the given mappings. The mappings of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addMappings(MeshPrimitiveMaterialsVariantsPropertiesMappingsItems element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> oldList = this.mappings;
        List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> newList = new ArrayList<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.mappings = newList;
    }

    /**
     * Remove the given mappings. The mappings of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeMappings(MeshPrimitiveMaterialsVariantsPropertiesMappingsItems element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> oldList = this.mappings;
        List<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems> newList = new ArrayList<MeshPrimitiveMaterialsVariantsPropertiesMappingsItems>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.mappings = newList;
    }

}
