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
 * glTF extension that defines a material variations for mesh primitives 
 * 
 * Auto-generated for glTF.KHR_materials_variants.schema.json 
 * 
 */
public class GlTFMaterialsVariants
    extends GlTFProperty
{

    /**
     * The variants of this GlTFMaterialsVariants (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An object defining a valid material variant (optional) 
     * 
     */
    private List<GlTFMaterialsVariantsPropertiesVariantsItems> variants;

    /**
     * The variants of this GlTFMaterialsVariants (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An object defining a valid material variant (optional) 
     * 
     * @param variants The variants to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setVariants(List<GlTFMaterialsVariantsPropertiesVariantsItems> variants) {
        if (variants == null) {
            throw new NullPointerException((("Invalid value for variants: "+ variants)+", may not be null"));
        }
        if (variants.size()< 1) {
            throw new IllegalArgumentException("Number of variants elements is < 1");
        }
        this.variants = variants;
    }

    /**
     * The variants of this GlTFMaterialsVariants (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An object defining a valid material variant (optional) 
     * 
     * @return The variants
     * 
     */
    public List<GlTFMaterialsVariantsPropertiesVariantsItems> getVariants() {
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
    public void addVariants(GlTFMaterialsVariantsPropertiesVariantsItems element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<GlTFMaterialsVariantsPropertiesVariantsItems> oldList = this.variants;
        List<GlTFMaterialsVariantsPropertiesVariantsItems> newList = new ArrayList<GlTFMaterialsVariantsPropertiesVariantsItems>();
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
    public void removeVariants(GlTFMaterialsVariantsPropertiesVariantsItems element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<GlTFMaterialsVariantsPropertiesVariantsItems> oldList = this.variants;
        List<GlTFMaterialsVariantsPropertiesVariantsItems> newList = new ArrayList<GlTFMaterialsVariantsPropertiesVariantsItems>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.variants = newList;
    }

}
