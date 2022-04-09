/*
 * EXT_structural_metadata JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.structural_metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * A texture containing property values. 
 * 
 * Auto-generated for propertyTexture.property.schema.json 
 * 
 */
public class PropertyTextureProperty
    extends TextureInfo
{

    /**
     * Texture channels containing property values, identified by index. The 
     * values may be packed into multiple channels if a single channel does 
     * not have sufficient bit depth. The values are packed in little-endian 
     * order. (optional)<br> 
     * Default: [0]<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0 (inclusive) 
     * 
     */
    private List<Integer> channels;
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
     * Texture channels containing property values, identified by index. The 
     * values may be packed into multiple channels if a single channel does 
     * not have sufficient bit depth. The values are packed in little-endian 
     * order. (optional)<br> 
     * Default: [0]<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0 (inclusive) 
     * 
     * @param channels The channels to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setChannels(List<Integer> channels) {
        if (channels == null) {
            this.channels = channels;
            return ;
        }
        for (Integer channelsElement: channels) {
            if (channelsElement< 0) {
                throw new IllegalArgumentException("channelsElement < 0");
            }
        }
        this.channels = channels;
    }

    /**
     * Texture channels containing property values, identified by index. The 
     * values may be packed into multiple channels if a single channel does 
     * not have sufficient bit depth. The values are packed in little-endian 
     * order. (optional)<br> 
     * Default: [0]<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0 (inclusive) 
     * 
     * @return The channels
     * 
     */
    public List<Integer> getChannels() {
        return this.channels;
    }

    /**
     * Add the given channels. The channels of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addChannels(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.channels;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.channels = newList;
    }

    /**
     * Remove the given channels. The channels of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeChannels(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.channels;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.channels = null;
        } else {
            this.channels = newList;
        }
    }

    /**
     * Returns the default value of the channels<br> 
     * @see #getChannels 
     * 
     * @return The default channels
     * 
     */
    public List<Integer> defaultChannels() {
        return new ArrayList<Integer>(Arrays.asList(0));
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
