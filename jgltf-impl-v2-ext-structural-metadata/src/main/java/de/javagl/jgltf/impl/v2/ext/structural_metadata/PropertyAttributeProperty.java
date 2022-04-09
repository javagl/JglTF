/*
 * EXT_structural_metadata JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.structural_metadata;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * An attribute containing property values. 
 * 
 * Auto-generated for propertyAttribute.property.schema.json 
 * 
 */
public class PropertyAttributeProperty
    extends GlTFProperty
{

    /**
     * The name of the attribute containing property values. (required) 
     * 
     */
    private String attribute;
    /**
     * An offset to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `offset` if both are 
     * defined. (optional) 
     * 
     */
    private Object offset;
    /**
     * A scale to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `scale` if both are 
     * defined. (optional) 
     * 
     */
    private Object scale;
    /**
     * Maximum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the maximum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     */
    private Object max;
    /**
     * Minimum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the minimum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     */
    private Object min;

    /**
     * The name of the attribute containing property values. (required) 
     * 
     * @param attribute The attribute to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setAttribute(String attribute) {
        if (attribute == null) {
            throw new NullPointerException((("Invalid value for attribute: "+ attribute)+", may not be null"));
        }
        this.attribute = attribute;
    }

    /**
     * The name of the attribute containing property values. (required) 
     * 
     * @return The attribute
     * 
     */
    public String getAttribute() {
        return this.attribute;
    }

    /**
     * An offset to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `offset` if both are 
     * defined. (optional) 
     * 
     * @param offset The offset to set
     * 
     */
    public void setOffset(Object offset) {
        if (offset == null) {
            this.offset = offset;
            return ;
        }
        this.offset = offset;
    }

    /**
     * An offset to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `offset` if both are 
     * defined. (optional) 
     * 
     * @return The offset
     * 
     */
    public Object getOffset() {
        return this.offset;
    }

    /**
     * A scale to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `scale` if both are 
     * defined. (optional) 
     * 
     * @param scale The scale to set
     * 
     */
    public void setScale(Object scale) {
        if (scale == null) {
            this.scale = scale;
            return ;
        }
        this.scale = scale;
    }

    /**
     * A scale to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `scale` if both are 
     * defined. (optional) 
     * 
     * @return The scale
     * 
     */
    public Object getScale() {
        return this.scale;
    }

    /**
     * Maximum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the maximum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @param max The max to set
     * 
     */
    public void setMax(Object max) {
        if (max == null) {
            this.max = max;
            return ;
        }
        this.max = max;
    }

    /**
     * Maximum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the maximum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @return The max
     * 
     */
    public Object getMax() {
        return this.max;
    }

    /**
     * Minimum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the minimum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @param min The min to set
     * 
     */
    public void setMin(Object min) {
        if (min == null) {
            this.min = min;
            return ;
        }
        this.min = min;
    }

    /**
     * Minimum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the minimum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @return The min
     * 
     */
    public Object getMin() {
        return this.min;
    }

}
