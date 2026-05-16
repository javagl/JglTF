/*
 * glTF KHR_draco_mesh_compression JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.draco_mesh_compression;

import java.util.LinkedHashMap;
import java.util.Map;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * Auto-generated for 
 * mesh.primitive.KHR_draco_mesh_compression.schema.json 
 * 
 */
public class MeshPrimitiveDracoMeshCompression
    extends GlTFProperty
{

    /**
     * The index of the bufferView. (required) 
     * 
     */
    private Integer bufferView;
    /**
     * A dictionary object, where each key corresponds to an attribute and 
     * its unique attribute id stored in the compressed geometry. (required) 
     * 
     */
    private Map<String, Integer> attributes;

    /**
     * The index of the bufferView. (required) 
     * 
     * @param bufferView The bufferView to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setBufferView(Integer bufferView) {
        if (bufferView == null) {
            throw new NullPointerException((("Invalid value for bufferView: "+ bufferView)+", may not be null"));
        }
        this.bufferView = bufferView;
    }

    /**
     * The index of the bufferView. (required) 
     * 
     * @return The bufferView
     * 
     */
    public Integer getBufferView() {
        return this.bufferView;
    }

    /**
     * A dictionary object, where each key corresponds to an attribute and 
     * its unique attribute id stored in the compressed geometry. (required) 
     * 
     * @param attributes The attributes to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setAttributes(Map<String, Integer> attributes) {
        if (attributes == null) {
            throw new NullPointerException((("Invalid value for attributes: "+ attributes)+", may not be null"));
        }
        this.attributes = attributes;
    }

    /**
     * A dictionary object, where each key corresponds to an attribute and 
     * its unique attribute id stored in the compressed geometry. (required) 
     * 
     * @return The attributes
     * 
     */
    public Map<String, Integer> getAttributes() {
        return this.attributes;
    }

    /**
     * Add the given attributes. The attributes of this instance will be 
     * replaced with a map that contains all previous mappings, and 
     * additionally the new mapping. 
     * 
     * @param key The key
     * @param value The value
     * @throws NullPointerException If the given key or value is <code>null</code>
     * 
     */
    public void addAttributes(String key, Integer value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, Integer> oldMap = this.attributes;
        Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.put(key, value);
        this.attributes = newMap;
    }

    /**
     * Remove the given attributes. The attributes of this instance will be 
     * replaced with a map that contains all previous mappings, except for 
     * the one with the given key. 
     * 
     * @param key The key
     * @throws NullPointerException If the given key is <code>null</code>
     * 
     */
    public void removeAttributes(String key) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        Map<String, Integer> oldMap = this.attributes;
        Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.remove(key);
        this.attributes = newMap;
    }

}
