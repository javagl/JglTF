/*
 * glTF KHR_materials_ior JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2025 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_ior;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension that defines the index of refraction of a material. 
 * 
 * Auto-generated for material.KHR_materials_ior.schema.json 
 * 
 */
public class MaterialMaterialsIor
    extends GlTFProperty
{

    /**
     * The index of refraction. (optional)<br> 
     * Default: 1.5 
     * 
     */
    private Double ior;

    /**
     * The index of refraction. (optional)<br> 
     * Default: 1.5 
     * 
     * @param ior The ior to set
     * 
     */
    public void setIor(Double ior) {
        if (ior == null) {
            this.ior = ior;
            return ;
        }
        this.ior = ior;
    }

    /**
     * The index of refraction. (optional)<br> 
     * Default: 1.5 
     * 
     * @return The ior
     * 
     */
    public Double getIor() {
        return this.ior;
    }

    /**
     * Returns the default value of the ior<br> 
     * @see #getIor 
     * 
     * @return The default ior
     * 
     */
    public Double defaultIor() {
        return  1.5D;
    }

}
