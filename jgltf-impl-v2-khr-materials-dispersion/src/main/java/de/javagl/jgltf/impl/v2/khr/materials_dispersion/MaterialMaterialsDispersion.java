/*
 * glTF KHR_materials_dispersion JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2025 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_dispersion;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension that defines the strength of dispersion. 
 * 
 * Auto-generated for material.KHR_materials_dispersion.schema.json 
 * 
 */
public class MaterialMaterialsDispersion
    extends GlTFProperty
{

    /**
     * This parameter defines dispersion in terms of the 20/Abbe number 
     * formulation. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     */
    private Double dispersion;

    /**
     * This parameter defines dispersion in terms of the 20/Abbe number 
     * formulation. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @param dispersion The dispersion to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setDispersion(Double dispersion) {
        if (dispersion == null) {
            this.dispersion = dispersion;
            return ;
        }
        if (dispersion< 0.0D) {
            throw new IllegalArgumentException("dispersion < 0.0");
        }
        this.dispersion = dispersion;
    }

    /**
     * This parameter defines dispersion in terms of the 20/Abbe number 
     * formulation. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The dispersion
     * 
     */
    public Double getDispersion() {
        return this.dispersion;
    }

    /**
     * Returns the default value of the dispersion<br> 
     * @see #getDispersion 
     * 
     * @return The default dispersion
     * 
     */
    public Double defaultDispersion() {
        return  0.0D;
    }

}
