/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Geometry to be rendered with the given material. 
 * 
 * Auto-generated for mesh.primitive.schema.json 
 * 
 */
public class MeshPrimitive
    extends GlTFProperty
{

    /**
     * A dictionary object, where each key corresponds to mesh attribute 
     * semantic and each value is the index of the accessor containing 
     * attribute's data. (required) 
     * 
     */
    private Map<String, Integer> attributes;
    /**
     * The index of the accessor that contains the indices. (optional) 
     * 
     */
    private Integer indices;
    /**
     * The index of the material to apply to this primitive when rendering. 
     * (optional) 
     * 
     */
    private Integer material;
    /**
     * The type of primitives to render. (optional)<br> 
     * Default: 4<br> 
     * Valid values: [0, 1, 2, 3, 4, 5, 6] 
     * 
     */
    private Integer mode;

    /**
     * A dictionary object, where each key corresponds to mesh attribute 
     * semantic and each value is the index of the accessor containing 
     * attribute's data. (required) 
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
     * A dictionary object, where each key corresponds to mesh attribute 
     * semantic and each value is the index of the accessor containing 
     * attribute's data. (required) 
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

    /**
     * The index of the accessor that contains the indices. (optional) 
     * 
     * @param indices The indices to set
     * 
     */
    public void setIndices(Integer indices) {
        if (indices == null) {
            this.indices = indices;
            return ;
        }
        this.indices = indices;
    }

    /**
     * The index of the accessor that contains the indices. (optional) 
     * 
     * @return The indices
     * 
     */
    public Integer getIndices() {
        return this.indices;
    }

    /**
     * The index of the material to apply to this primitive when rendering. 
     * (optional) 
     * 
     * @param material The material to set
     * 
     */
    public void setMaterial(Integer material) {
        if (material == null) {
            this.material = material;
            return ;
        }
        this.material = material;
    }

    /**
     * The index of the material to apply to this primitive when rendering. 
     * (optional) 
     * 
     * @return The material
     * 
     */
    public Integer getMaterial() {
        return this.material;
    }

    /**
     * The type of primitives to render. (optional)<br> 
     * Default: 4<br> 
     * Valid values: [0, 1, 2, 3, 4, 5, 6] 
     * 
     * @param mode The mode to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setMode(Integer mode) {
        if (mode == null) {
            this.mode = mode;
            return ;
        }
        if (((((((mode!= 0)&&(mode!= 1))&&(mode!= 2))&&(mode!= 3))&&(mode!= 4))&&(mode!= 5))&&(mode!= 6)) {
            throw new IllegalArgumentException((("Invalid value for mode: "+ mode)+", valid: [0, 1, 2, 3, 4, 5, 6]"));
        }
        this.mode = mode;
    }

    /**
     * The type of primitives to render. (optional)<br> 
     * Default: 4<br> 
     * Valid values: [0, 1, 2, 3, 4, 5, 6] 
     * 
     * @return The mode
     * 
     */
    public Integer getMode() {
        return this.mode;
    }

    /**
     * Returns the default value of the mode<br> 
     * @see #getMode 
     * 
     * @return The default mode
     * 
     */
    public Integer defaultMode() {
        return  4;
    }

}
