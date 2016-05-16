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
 * Auto-generated for glTFProperty.schema.json 
 * 
 */
public class GlTFProperty {

    /**
     * Dictionary object with extension-specific objects. (optional) 
     * 
     */
    private Map<String, Object> extensions;
    /**
     * Application-specific data. (optional) 
     * 
     */
    private Object extras;

    /**
     * Dictionary object with extension-specific objects. (optional) 
     * 
     * @param extensions The extensions to set
     * 
     */
    public void setExtensions(Map<String, Object> extensions) {
        if (extensions == null) {
            this.extensions = extensions;
            return ;
        }
        this.extensions = extensions;
    }

    /**
     * Dictionary object with extension-specific objects. (optional) 
     * 
     * @return The extensions
     * 
     */
    public Map<String, Object> getExtensions() {
        return this.extensions;
    }

    /**
     * Application-specific data. (optional) 
     * 
     * @param extras The extras to set
     * 
     */
    public void setExtras(Object extras) {
        if (extras == null) {
            this.extras = extras;
            return ;
        }
        this.extras = extras;
    }

    /**
     * Application-specific data. (optional) 
     * 
     * @return The extras
     * 
     */
    public Object getExtras() {
        return this.extras;
    }

}
