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
 * An object defining classes and enums. 
 * 
 * Auto-generated for schema.schema.json 
 * 
 */
public class Schema
    extends GlTFProperty
{

    /**
     * Unique identifier for the schema. (optional) 
     * 
     */
    private String id;
    /**
     * The name of the schema, e.g. for display purposes. (optional) 
     * 
     */
    private String name;
    /**
     * The description of the schema. (optional) 
     * 
     */
    private String description;
    /**
     * Application-specific version of the schema. (optional) 
     * 
     */
    private String version;
    /**
     * A dictionary, where each key is a class ID and each value is an object 
     * defining the class. Class IDs may contain only alphanumeric and 
     * underscore characters. (optional) 
     * 
     */
    private Map<String, MetadataClass> classes;
    /**
     * A dictionary, where each key is an enum ID and each value is an object 
     * defining the values for the enum. Enum IDs may contain only 
     * alphanumeric and underscore characters. (optional) 
     * 
     */
    private Map<String, MetadataEnum> enums;

    /**
     * Unique identifier for the schema. (optional) 
     * 
     * @param id The id to set
     * 
     */
    public void setId(String id) {
        if (id == null) {
            this.id = id;
            return ;
        }
        this.id = id;
    }

    /**
     * Unique identifier for the schema. (optional) 
     * 
     * @return The id
     * 
     */
    public String getId() {
        return this.id;
    }

    /**
     * The name of the schema, e.g. for display purposes. (optional) 
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
     * The name of the schema, e.g. for display purposes. (optional) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * The description of the schema. (optional) 
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
     * The description of the schema. (optional) 
     * 
     * @return The description
     * 
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Application-specific version of the schema. (optional) 
     * 
     * @param version The version to set
     * 
     */
    public void setVersion(String version) {
        if (version == null) {
            this.version = version;
            return ;
        }
        this.version = version;
    }

    /**
     * Application-specific version of the schema. (optional) 
     * 
     * @return The version
     * 
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * A dictionary, where each key is a class ID and each value is an object 
     * defining the class. Class IDs may contain only alphanumeric and 
     * underscore characters. (optional) 
     * 
     * @param classes The classes to set
     * 
     */
    public void setClasses(Map<String, MetadataClass> classes) {
        if (classes == null) {
            this.classes = classes;
            return ;
        }
        this.classes = classes;
    }

    /**
     * A dictionary, where each key is a class ID and each value is an object 
     * defining the class. Class IDs may contain only alphanumeric and 
     * underscore characters. (optional) 
     * 
     * @return The classes
     * 
     */
    public Map<String, MetadataClass> getClasses() {
        return this.classes;
    }

    /**
     * Add the given classes. The classes of this instance will be replaced 
     * with a map that contains all previous mappings, and additionally the 
     * new mapping. 
     * 
     * @param key The key
     * @param value The value
     * @throws NullPointerException If the given key or value is <code>null</code>
     * 
     */
    public void addClasses(String key, MetadataClass value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, MetadataClass> oldMap = this.classes;
        Map<String, MetadataClass> newMap = new LinkedHashMap<String, MetadataClass>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.put(key, value);
        this.classes = newMap;
    }

    /**
     * Remove the given classes. The classes of this instance will be 
     * replaced with a map that contains all previous mappings, except for 
     * the one with the given key.<br> 
     * If this new map would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param key The key
     * @throws NullPointerException If the given key is <code>null</code>
     * 
     */
    public void removeClasses(String key) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        Map<String, MetadataClass> oldMap = this.classes;
        Map<String, MetadataClass> newMap = new LinkedHashMap<String, MetadataClass>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.remove(key);
        if (newMap.isEmpty()) {
            this.classes = null;
        } else {
            this.classes = newMap;
        }
    }

    /**
     * A dictionary, where each key is an enum ID and each value is an object 
     * defining the values for the enum. Enum IDs may contain only 
     * alphanumeric and underscore characters. (optional) 
     * 
     * @param enums The enums to set
     * 
     */
    public void setEnums(Map<String, MetadataEnum> enums) {
        if (enums == null) {
            this.enums = enums;
            return ;
        }
        this.enums = enums;
    }

    /**
     * A dictionary, where each key is an enum ID and each value is an object 
     * defining the values for the enum. Enum IDs may contain only 
     * alphanumeric and underscore characters. (optional) 
     * 
     * @return The enums
     * 
     */
    public Map<String, MetadataEnum> getEnums() {
        return this.enums;
    }

    /**
     * Add the given enums. The enums of this instance will be replaced with 
     * a map that contains all previous mappings, and additionally the new 
     * mapping. 
     * 
     * @param key The key
     * @param value The value
     * @throws NullPointerException If the given key or value is <code>null</code>
     * 
     */
    public void addEnums(String key, MetadataEnum value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, MetadataEnum> oldMap = this.enums;
        Map<String, MetadataEnum> newMap = new LinkedHashMap<String, MetadataEnum>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.put(key, value);
        this.enums = newMap;
    }

    /**
     * Remove the given enums. The enums of this instance will be replaced 
     * with a map that contains all previous mappings, except for the one 
     * with the given key.<br> 
     * If this new map would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param key The key
     * @throws NullPointerException If the given key is <code>null</code>
     * 
     */
    public void removeEnums(String key) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        Map<String, MetadataEnum> oldMap = this.enums;
        Map<String, MetadataEnum> newMap = new LinkedHashMap<String, MetadataEnum>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.remove(key);
        if (newMap.isEmpty()) {
            this.enums = null;
        } else {
            this.enums = newMap;
        }
    }

}
