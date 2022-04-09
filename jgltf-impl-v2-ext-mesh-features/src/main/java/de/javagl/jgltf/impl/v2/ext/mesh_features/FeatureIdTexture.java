/*
 * EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * A texture containing feature IDs 
 * 
 * Auto-generated for featureIdTexture.schema.json 
 * 
 */
public class FeatureIdTexture
    extends TextureInfo
{

    /**
     * Texture channels containing feature IDs, identified by index. Feature 
     * IDs may be packed into multiple channels if a single channel does not 
     * have sufficient bit depth to represent all feature ID values. The 
     * values are packed in little-endian order. (optional)<br> 
     * Default: [0]<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0 (inclusive) 
     * 
     */
    private List<Integer> channels;

    /**
     * Texture channels containing feature IDs, identified by index. Feature 
     * IDs may be packed into multiple channels if a single channel does not 
     * have sufficient bit depth to represent all feature ID values. The 
     * values are packed in little-endian order. (optional)<br> 
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
     * Texture channels containing feature IDs, identified by index. Feature 
     * IDs may be packed into multiple channels if a single channel does not 
     * have sufficient bit depth to represent all feature ID values. The 
     * values are packed in little-endian order. (optional)<br> 
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

}
