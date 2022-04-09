/*
 * glTF EXT_mesh_gpu_instancing JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_gpu_instancing;

import java.util.LinkedHashMap;
import java.util.Map;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension defines instance attributes for a node with a mesh. 
 * 
 * Auto-generated for glTF.EXT_mesh_gpu_instancing.schema.json 
 * 
 */
public class GlTFMeshGpuInstancing
    extends GlTFProperty
{

    /**
     * A dictionary object, where each key corresponds to instance attribute 
     * and each value is the index of the accessor containing attribute's 
     * data. Attributes TRANSLATION, ROTATION, SCALE define instance 
     * transformation. For "TRANSLATION" the values are FLOAT_VEC3's 
     * specifying translation along the x, y, and z axes. For "ROTATION" the 
     * values are VEC4's specifying rotation as a quaternion in the order (x, 
     * y, z, w), where w is the scalar, with component type `FLOAT` or 
     * normalized integer. For "SCALE" the values are FLOAT_VEC3's specifying 
     * scaling factors along the x, y, and z axes. (optional) 
     * 
     */
    private Map<String, Integer> attributes;

    /**
     * A dictionary object, where each key corresponds to instance attribute 
     * and each value is the index of the accessor containing attribute's 
     * data. Attributes TRANSLATION, ROTATION, SCALE define instance 
     * transformation. For "TRANSLATION" the values are FLOAT_VEC3's 
     * specifying translation along the x, y, and z axes. For "ROTATION" the 
     * values are VEC4's specifying rotation as a quaternion in the order (x, 
     * y, z, w), where w is the scalar, with component type `FLOAT` or 
     * normalized integer. For "SCALE" the values are FLOAT_VEC3's specifying 
     * scaling factors along the x, y, and z axes. (optional) 
     * 
     * @param attributes The attributes to set
     * 
     */
    public void setAttributes(Map<String, Integer> attributes) {
        if (attributes == null) {
            this.attributes = attributes;
            return ;
        }
        this.attributes = attributes;
    }

    /**
     * A dictionary object, where each key corresponds to instance attribute 
     * and each value is the index of the accessor containing attribute's 
     * data. Attributes TRANSLATION, ROTATION, SCALE define instance 
     * transformation. For "TRANSLATION" the values are FLOAT_VEC3's 
     * specifying translation along the x, y, and z axes. For "ROTATION" the 
     * values are VEC4's specifying rotation as a quaternion in the order (x, 
     * y, z, w), where w is the scalar, with component type `FLOAT` or 
     * normalized integer. For "SCALE" the values are FLOAT_VEC3's specifying 
     * scaling factors along the x, y, and z axes. (optional) 
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
     * the one with the given key.<br> 
     * If this new map would be empty, then it will be set to 
     * <code>null</code>. 
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
        if (newMap.isEmpty()) {
            this.attributes = null;
        } else {
            this.attributes = newMap;
        }
    }

}
