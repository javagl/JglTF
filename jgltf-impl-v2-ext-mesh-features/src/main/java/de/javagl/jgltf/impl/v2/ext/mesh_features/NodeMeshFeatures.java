/*
 * 3D Tiles EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

import java.util.ArrayList;
import java.util.List;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * An object describing per-instance feature IDs to be used as indices to 
 * property arrays in the property table. 
 * 
 * Auto-generated for node.EXT_mesh_features.schema.json 
 * 
 */
public class NodeMeshFeatures
    extends GlTFProperty
{

    /**
     * (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Feature IDs to be used as indices to property arrays in 
     * the property table. (optional) 
     * 
     */
    private List<FeatureIdAttribute> featureIds;
    /**
     * An array of IDs of property tables from the root `EXT_mesh_features` 
     * object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<Integer> propertyTables;

    /**
     * (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Feature IDs to be used as indices to property arrays in 
     * the property table. (optional) 
     * 
     * @param featureIds The featureIds to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setFeatureIds(List<FeatureIdAttribute> featureIds) {
        if (featureIds == null) {
            throw new NullPointerException((("Invalid value for featureIds: "+ featureIds)+", may not be null"));
        }
        if (featureIds.size()< 1) {
            throw new IllegalArgumentException("Number of featureIds elements is < 1");
        }
        this.featureIds = featureIds;
    }

    /**
     * (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Feature IDs to be used as indices to property arrays in 
     * the property table. (optional) 
     * 
     * @return The featureIds
     * 
     */
    public List<FeatureIdAttribute> getFeatureIds() {
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
    public void addFeatureIds(FeatureIdAttribute element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<FeatureIdAttribute> oldList = this.featureIds;
        List<FeatureIdAttribute> newList = new ArrayList<FeatureIdAttribute>();
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
    public void removeFeatureIds(FeatureIdAttribute element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<FeatureIdAttribute> oldList = this.featureIds;
        List<FeatureIdAttribute> newList = new ArrayList<FeatureIdAttribute>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.featureIds = newList;
    }

    /**
     * An array of IDs of property tables from the root `EXT_mesh_features` 
     * object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param propertyTables The propertyTables to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setPropertyTables(List<Integer> propertyTables) {
        if (propertyTables == null) {
            this.propertyTables = propertyTables;
            return ;
        }
        if (propertyTables.size()< 1) {
            throw new IllegalArgumentException("Number of propertyTables elements is < 1");
        }
        this.propertyTables = propertyTables;
    }

    /**
     * An array of IDs of property tables from the root `EXT_mesh_features` 
     * object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The propertyTables
     * 
     */
    public List<Integer> getPropertyTables() {
        return this.propertyTables;
    }

    /**
     * Add the given propertyTables. The propertyTables of this instance will 
     * be replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addPropertyTables(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.propertyTables;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.propertyTables = newList;
    }

    /**
     * Remove the given propertyTables. The propertyTables of this instance 
     * will be replaced with a list that contains all previous elements, 
     * except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removePropertyTables(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.propertyTables;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.propertyTables = null;
        } else {
            this.propertyTables = newList;
        }
    }

}
