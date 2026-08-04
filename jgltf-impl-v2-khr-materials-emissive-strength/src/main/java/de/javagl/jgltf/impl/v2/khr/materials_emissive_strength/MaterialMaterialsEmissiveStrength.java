/*
 * glTF KHR_materials_emissive_strength JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_emissive_strength;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension that adjusts the strength of emissive material 
 * properties. 
 * 
 * Auto-generated for 
 * material.KHR_materials_emissive_strength.schema.json 
 * 
 */
public class MaterialMaterialsEmissiveStrength
    extends GlTFProperty
{

    /**
     * The strength adjustment to be multiplied with the material's emissive 
     * value. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     */
    private Double emissiveStrength;

    /**
     * The strength adjustment to be multiplied with the material's emissive 
     * value. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @param emissiveStrength The emissiveStrength to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setEmissiveStrength(Double emissiveStrength) {
        if (emissiveStrength == null) {
            this.emissiveStrength = emissiveStrength;
            return ;
        }
        if (emissiveStrength< 0.0D) {
            throw new IllegalArgumentException("emissiveStrength < 0.0");
        }
        this.emissiveStrength = emissiveStrength;
    }

    /**
     * The strength adjustment to be multiplied with the material's emissive 
     * value. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The emissiveStrength
     * 
     */
    public Double getEmissiveStrength() {
        return this.emissiveStrength;
    }

    /**
     * Returns the default value of the emissiveStrength<br> 
     * @see #getEmissiveStrength 
     * 
     * @return The default emissiveStrength
     * 
     */
    public Double defaultEmissiveStrength() {
        return  1.0D;
    }

}
