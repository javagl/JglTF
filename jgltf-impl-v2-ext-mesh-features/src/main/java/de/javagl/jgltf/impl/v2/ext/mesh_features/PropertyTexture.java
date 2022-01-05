/*
 * 3D Tiles EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * Features conforming to a class, organized as property values stored in 
 * texture channels. 
 * 
 * Auto-generated for propertyTexture.schema.json 
 * 
 */
public class PropertyTexture
    extends TextureInfo
{

    /**
     * The name of the property texture, e.g. for display purposes. 
     * (optional) 
     * 
     */
    private String name;
    /**
     * The class this property texture conforms to. The value must be a class 
     * ID declared in the `classes` dictionary. (required) 
     * 
     */
    private String classProperty;
    /**
     * A dictionary, where each key corresponds to a property ID in the 
     * class' `properties` dictionary and each value describes the texture 
     * channels containing property values. (required) 
     * 
     */
    private Map<String, List<Integer>> properties;

    /**
     * The name of the property texture, e.g. for display purposes. 
     * (optional) 
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
     * The name of the property texture, e.g. for display purposes. 
     * (optional) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * The class this property texture conforms to. The value must be a class 
     * ID declared in the `classes` dictionary. (required) 
     * 
     * @param classProperty The classProperty to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setClassProperty(String classProperty) {
        if (classProperty == null) {
            throw new NullPointerException((("Invalid value for classProperty: "+ classProperty)+", may not be null"));
        }
        this.classProperty = classProperty;
    }

    /**
     * The class this property texture conforms to. The value must be a class 
     * ID declared in the `classes` dictionary. (required) 
     * 
     * @return The classProperty
     * 
     */
    public String getClassProperty() {
        return this.classProperty;
    }

    /**
     * A dictionary, where each key corresponds to a property ID in the 
     * class' `properties` dictionary and each value describes the texture 
     * channels containing property values. (required) 
     * 
     * @param properties The properties to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setProperties(Map<String, List<Integer>> properties) {
        if (properties == null) {
            throw new NullPointerException((("Invalid value for properties: "+ properties)+", may not be null"));
        }
        this.properties = properties;
    }

    /**
     * A dictionary, where each key corresponds to a property ID in the 
     * class' `properties` dictionary and each value describes the texture 
     * channels containing property values. (required) 
     * 
     * @return The properties
     * 
     */
    public Map<String, List<Integer>> getProperties() {
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
    public void addProperties(String key, List<Integer> value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, List<Integer>> oldMap = this.properties;
        Map<String, List<Integer>> newMap = new LinkedHashMap<String, List<Integer>>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.put(key, value);
        this.properties = newMap;
    }

    /**
     * Remove the given properties. The properties of this instance will be 
     * replaced with a map that contains all previous mappings, except for 
     * the one with the given key. 
     * 
     * @param key The key
     * @throws NullPointerException If the given key is <code>null</code>
     * 
     */
    public void removeProperties(String key) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        Map<String, List<Integer>> oldMap = this.properties;
        Map<String, List<Integer>> newMap = new LinkedHashMap<String, List<Integer>>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.remove(key);
        this.properties = newMap;
    }

}
