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
 * Auto-generated for 
 * mesh.primitive.KHR_materials_variants.schema.json#/properties/mappings/items 
 * 
 */
public class MeshPrimitiveMaterialsVariantsPropertiesMappingsItems
    extends GlTFProperty
{

    /**
     * An array of variant index values. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<Integer> variants;
    /**
     * The material associated with the set of variants. (required) 
     * 
     */
    private Integer material;
    /**
     * The user-defined name of this variant material mapping. (optional) 
     * 
     */
    private String name;

    /**
     * An array of variant index values. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param variants The variants to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setVariants(List<Integer> variants) {
        if (variants == null) {
            throw new NullPointerException((("Invalid value for variants: "+ variants)+", may not be null"));
        }
        if (variants.size()< 1) {
            throw new IllegalArgumentException("Number of variants elements is < 1");
        }
        this.variants = variants;
    }

    /**
     * An array of variant index values. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The variants
     * 
     */
    public List<Integer> getVariants() {
        return this.variants;
    }

    /**
     * Add the given variants. The variants of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addVariants(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.variants;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.variants = newList;
    }

    /**
     * Remove the given variants. The variants of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeVariants(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.variants;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.variants = newList;
    }

    /**
     * The material associated with the set of variants. (required) 
     * 
     * @param material The material to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setMaterial(Integer material) {
        if (material == null) {
            throw new NullPointerException((("Invalid value for material: "+ material)+", may not be null"));
        }
        this.material = material;
    }

    /**
     * The material associated with the set of variants. (required) 
     * 
     * @return The material
     * 
     */
    public Integer getMaterial() {
        return this.material;
    }

    /**
     * The user-defined name of this variant material mapping. (optional) 
     * 
     * @param name The name to set
     * 
     */
    public void setName(String name) {
        if (name == null) {
            this.name = name;
            return ;
        }
        this.name = name;
    }

    /**
     * The user-defined name of this variant material mapping. (optional) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

}
