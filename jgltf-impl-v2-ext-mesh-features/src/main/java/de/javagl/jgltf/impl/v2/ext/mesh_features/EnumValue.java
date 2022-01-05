/*
 * 3D Tiles EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * An enum value. 
 * 
 * Auto-generated for enum.value.schema.json 
 * 
 */
public class EnumValue
    extends GlTFProperty
{

    /**
     * The name of the enum value. (required) 
     * 
     */
    private String name;
    /**
     * The description of the enum value. (optional) 
     * 
     */
    private String description;
    /**
     * The integer enum value. (required) 
     * 
     */
    private Integer value;

    /**
     * The name of the enum value. (required) 
     * 
     * @param name The name to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException((("Invalid value for name: "+ name)+", may not be null"));
        }
        this.name = name;
    }

    /**
     * The name of the enum value. (required) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * The description of the enum value. (optional) 
     * 
     * @param description The description to set
     * 
     */
    public void setDescription(String description) {
        if (description == null) {
            this.description = description;
            return ;
        }
        this.description = description;
    }

    /**
     * The description of the enum value. (optional) 
     * 
     * @return The description
     * 
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * The integer enum value. (required) 
     * 
     * @param value The value to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setValue(Integer value) {
        if (value == null) {
            throw new NullPointerException((("Invalid value for value: "+ value)+", may not be null"));
        }
        this.value = value;
    }

    /**
     * The integer enum value. (required) 
     * 
     * @return The value
     * 
     */
    public Integer getValue() {
        return this.value;
    }

}
