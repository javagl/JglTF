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
 * Feature IDs to be used as indices to property arrays in the property 
 * table. 
 * 
 * Auto-generated for featureIdAttribute.schema.json 
 * 
 */
public class FeatureIdAttribute
    extends GlTFProperty
{

    /**
     * This integer value is used to construct a string in the format 
     * `FEATURE_ID_&lt;set index&gt;` which is a reference to a key in 
     * `mesh.primitives.attributes` (e.g. a value of `0` corresponds to 
     * `FEATURE_ID_0`). (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer attribute;
    /**
     * Initial value for an implicit feature ID range. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer offset;
    /**
     * Number of vertices for which to repeat each feature ID before 
     * incrementing the ID by 1. If `repeat` is undefined, the feature ID for 
     * all vertices is `offset`. (optional)<br> 
     * Minimum: 1 (inclusive) 
     * 
     */
    private Integer repeat;

    /**
     * This integer value is used to construct a string in the format 
     * `FEATURE_ID_&lt;set index&gt;` which is a reference to a key in 
     * `mesh.primitives.attributes` (e.g. a value of `0` corresponds to 
     * `FEATURE_ID_0`). (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param attribute The attribute to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setAttribute(Integer attribute) {
        if (attribute == null) {
            this.attribute = attribute;
            return ;
        }
        if (attribute< 0) {
            throw new IllegalArgumentException("attribute < 0");
        }
        this.attribute = attribute;
    }

    /**
     * This integer value is used to construct a string in the format 
     * `FEATURE_ID_&lt;set index&gt;` which is a reference to a key in 
     * `mesh.primitives.attributes` (e.g. a value of `0` corresponds to 
     * `FEATURE_ID_0`). (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The attribute
     * 
     */
    public Integer getAttribute() {
        return this.attribute;
    }

    /**
     * Initial value for an implicit feature ID range. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param offset The offset to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setOffset(Integer offset) {
        if (offset == null) {
            this.offset = offset;
            return ;
        }
        if (offset< 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        this.offset = offset;
    }

    /**
     * Initial value for an implicit feature ID range. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The offset
     * 
     */
    public Integer getOffset() {
        return this.offset;
    }

    /**
     * Returns the default value of the offset<br> 
     * @see #getOffset 
     * 
     * @return The default offset
     * 
     */
    public Integer defaultOffset() {
        return  0;
    }

    /**
     * Number of vertices for which to repeat each feature ID before 
     * incrementing the ID by 1. If `repeat` is undefined, the feature ID for 
     * all vertices is `offset`. (optional)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @param repeat The repeat to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setRepeat(Integer repeat) {
        if (repeat == null) {
            this.repeat = repeat;
            return ;
        }
        if (repeat< 1) {
            throw new IllegalArgumentException("repeat < 1");
        }
        this.repeat = repeat;
    }

    /**
     * Number of vertices for which to repeat each feature ID before 
     * incrementing the ID by 1. If `repeat` is undefined, the feature ID for 
     * all vertices is `offset`. (optional)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @return The repeat
     * 
     */
    public Integer getRepeat() {
        return this.repeat;
    }

}
