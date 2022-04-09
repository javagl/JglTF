/*
 * EXT_structural_metadata JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.structural_metadata;

import java.util.LinkedHashMap;
import java.util.Map;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * Properties conforming to a class, organized as property values stored 
 * in attributes. 
 * 
 * Auto-generated for propertyAttribute.schema.json 
 * 
 */
public class PropertyAttribute
    extends GlTFProperty
{

    /**
     * The name of the property attribute, e.g. for display purposes. 
     * (optional) 
     * 
     */
    private String name;
    /**
     * The class that property values conform to. The value must be a class 
     * ID declared in the `classes` dictionary. (required) 
     * 
     */
    private String classProperty;
    /**
     * A dictionary, where each key corresponds to a property ID in the 
     * class' `properties` dictionary and each value is an object describing 
     * where property values are stored. Required properties must be included 
     * in this dictionary. (optional) 
     * 
     */
    private Map<String, PropertyAttributeProperty> properties;

    /**
     * The name of the property attribute, e.g. for display purposes. 
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
     * The name of the property attribute, e.g. for display purposes. 
     * (optional) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * The class that property values conform to. The value must be a class 
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
     * The class that property values conform to. The value must be a class 
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
     * class' `properties` dictionary and each value is an object describing 
     * where property values are stored. Required properties must be included 
     * in this dictionary. (optional) 
     * 
     * @param properties The properties to set
     * 
     */
    public void setProperties(Map<String, PropertyAttributeProperty> properties) {
        if (properties == null) {
            this.properties = properties;
            return ;
        }
        this.properties = properties;
    }

    /**
     * A dictionary, where each key corresponds to a property ID in the 
     * class' `properties` dictionary and each value is an object describing 
     * where property values are stored. Required properties must be included 
     * in this dictionary. (optional) 
     * 
     * @return The properties
     * 
     */
    public Map<String, PropertyAttributeProperty> getProperties() {
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
    public void addProperties(String key, PropertyAttributeProperty value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, PropertyAttributeProperty> oldMap = this.properties;
        Map<String, PropertyAttributeProperty> newMap = new LinkedHashMap<String, PropertyAttributeProperty>();
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
        Map<String, PropertyAttributeProperty> oldMap = this.properties;
        Map<String, PropertyAttributeProperty> newMap = new LinkedHashMap<String, PropertyAttributeProperty>();
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
