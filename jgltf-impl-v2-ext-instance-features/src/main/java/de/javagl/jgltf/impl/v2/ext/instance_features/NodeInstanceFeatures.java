/*
 * EXT_instance_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.instance_features;

import java.util.ArrayList;
import java.util.List;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * An object describing per-instance feature IDs. 
 * 
 * Auto-generated for node.EXT_instance_features.schema.json 
 * 
 */
public class NodeInstanceFeatures
    extends GlTFProperty
{

    /**
     * An array of feature ID sets. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Feature IDs stored in a GPU mesh instancing attribute 
     * (optional) 
     * 
     */
    private List<FeatureId> featureIds;

    /**
     * An array of feature ID sets. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Feature IDs stored in a GPU mesh instancing attribute 
     * (optional) 
     * 
     * @param featureIds The featureIds to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setFeatureIds(List<FeatureId> featureIds) {
        if (featureIds == null) {
            throw new NullPointerException((("Invalid value for featureIds: "+ featureIds)+", may not be null"));
        }
        if (featureIds.size()< 1) {
            throw new IllegalArgumentException("Number of featureIds elements is < 1");
        }
        this.featureIds = featureIds;
    }

    /**
     * An array of feature ID sets. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Feature IDs stored in a GPU mesh instancing attribute 
     * (optional) 
     * 
     * @return The featureIds
     * 
     */
    public List<FeatureId> getFeatureIds() {
        return this.featureIds;
    }

    /**
     * Add the given featureIds. The featureIds of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addFeatureIds(FeatureId element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<FeatureId> oldList = this.featureIds;
        List<FeatureId> newList = new ArrayList<FeatureId>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.featureIds = newList;
    }

    /**
     * Remove the given featureIds. The featureIds of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeFeatureIds(FeatureId element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<FeatureId> oldList = this.featureIds;
        List<FeatureId> newList = new ArrayList<FeatureId>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.featureIds = newList;
    }

}
