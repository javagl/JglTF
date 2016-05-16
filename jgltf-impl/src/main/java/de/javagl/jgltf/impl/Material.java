/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl;

import java.util.Map;


/**
 * The material appearance of a primitive. 
 * 
 * Auto-generated for material.schema.json 
 * 
 */
public class Material
    extends GlTFChildOfRootProperty
{

    /**
     * The ID of the technique. (optional) 
     * 
     */
    private String technique;
    /**
     * A dictionary object of parameter values. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Object> values;

    /**
     * The ID of the technique. (optional) 
     * 
     * @param technique The technique to set
     * 
     */
    public void setTechnique(String technique) {
        if (technique == null) {
            this.technique = technique;
            return ;
        }
        this.technique = technique;
    }

    /**
     * The ID of the technique. (optional) 
     * 
     * @return The technique
     * 
     */
    public String getTechnique() {
        return this.technique;
    }

    /**
     * A dictionary object of parameter values. (optional)<br> 
     * Default: {} 
     * 
     * @param values The values to set
     * 
     */
    public void setValues(Map<String, Object> values) {
        if (values == null) {
            this.values = values;
            return ;
        }
        this.values = values;
    }

    /**
     * A dictionary object of parameter values. (optional)<br> 
     * Default: {} 
     * 
     * @return The values
     * 
     */
    public Map<String, Object> getValues() {
        return this.values;
    }

}
