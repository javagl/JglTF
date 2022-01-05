/*
 * 3D Tiles EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

import java.util.LinkedHashMap;
import java.util.Map;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * A class containing a set of properties. 
 * 
 * Auto-generated for class.schema.json 
 * 
 */
public class MetadataClass
    extends GlTFProperty
{

    /**
     * The name of the class, e.g. for display purposes. (optional) 
     * 
     */
    private String name;
    /**
     * The description of the class. (optional) 
     * 
     */
    private String description;
    /**
     * A dictionary, where each key is a property ID and each value is an 
     * object defining the property. Property IDs may contain only 
     * alphanumeric and underscore characters. (optional) 
     * 
     */
    private Map<String, ClassProperty> properties;

    /**
     * The name of the class, e.g. for display purposes. (optional) 
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
     * The name of the class, e.g. for display purposes. (optional) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * The description of the class. (optional) 
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
     * The description of the class. (optional) 
     * 
     * @return The description
     * 
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * A dictionary, where each key is a property ID and each value is an 
     * object defining the property. Property IDs may contain only 
     * alphanumeric and underscore characters. (optional) 
     * 
     * @param properties The properties to set
     * 
     */
    public void setProperties(Map<String, ClassProperty> properties) {
        if (properties == null) {
            this.properties = properties;
            return ;
        }
        this.properties = properties;
    }

    /**
     * A dictionary, where each key is a property ID and each value is an 
     * object defining the property. Property IDs may contain only 
     * alphanumeric and underscore characters. (optional) 
     * 
     * @return The properties
     * 
     */
    public Map<String, ClassProperty> getProperties() {
        return this.properties;
    }

    /**
     * Add the given properties. The properties of this instance will be 
     * replaced with a map that contains all previous mappings, and 
     * additionally the new mapping. 
     * 
     * @param key The key
     * @param value The value
     * @throws NullPointerException If the given key or value is <code>null</code>
     * 
     */
    public void addProperties(String key, ClassProperty value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, ClassProperty> oldMap = this.properties;
        Map<String, ClassProperty> newMap = new LinkedHashMap<String, ClassProperty>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.put(key, value);
        this.properties = newMap;
    }

    /**
     * Remove the given properties. The properties of this instance will be 
     * replaced with a map that contains all previous mappings, except for 
     * the one with the given key.<br> 
     * If this new map would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param key The key
     * @throws NullPointerException If the given key is <code>null</code>
     * 
     */
    public void removeProperties(String key) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        Map<String, ClassProperty> oldMap = this.properties;
        Map<String, ClassProperty> newMap = new LinkedHashMap<String, ClassProperty>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.remove(key);
        if (newMap.isEmpty()) {
            this.properties = null;
        } else {
            this.properties = newMap;
        }
    }

}
