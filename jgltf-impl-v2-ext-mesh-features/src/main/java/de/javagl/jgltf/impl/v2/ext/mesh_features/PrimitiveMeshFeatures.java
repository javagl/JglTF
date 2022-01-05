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
 * `EXT_mesh_features` extension for a primitive in a glTF model, to 
 * associate it with the root `EXT_mesh_features` object. 
 * 
 * Auto-generated for primitive.EXT_mesh_features.schema.json 
 * 
 */
public class PrimitiveMeshFeatures
    extends GlTFProperty
{

    /**
     * An array of feature IDs. A property table at index `i` corresponds to 
     * the `featureIds` entry at the same index. Additional feature ID 
     * entries may be present, so the length of the `featureIds` array must 
     * be greater than or equal to the length of the `propertyTables` array. 
     * (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<Object> featureIds;
    /**
     * An array of IDs of property tables from the root `EXT_mesh_features` 
     * object. A property table at index `i` corresponds to the `featureIds` 
     * entry at the same index. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<Integer> propertyTables;
    /**
     * An array of IDs of property textures from the root `EXT_mesh_features` 
     * object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<Integer> propertyTextures;

    /**
     * An array of feature IDs. A property table at index `i` corresponds to 
     * the `featureIds` entry at the same index. Additional feature ID 
     * entries may be present, so the length of the `featureIds` array must 
     * be greater than or equal to the length of the `propertyTables` array. 
     * (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param featureIds The featureIds to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setFeatureIds(List<Object> featureIds) {
        if (featureIds == null) {
            this.featureIds = featureIds;
            return ;
        }
        if (featureIds.size()< 1) {
            throw new IllegalArgumentException("Number of featureIds elements is < 1");
        }
        this.featureIds = featureIds;
    }

    /**
     * An array of feature IDs. A property table at index `i` corresponds to 
     * the `featureIds` entry at the same index. Additional feature ID 
     * entries may be present, so the length of the `featureIds` array must 
     * be greater than or equal to the length of the `propertyTables` array. 
     * (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The featureIds
     * 
     */
    public List<Object> getFeatureIds() {
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
    public void addFeatureIds(Object element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Object> oldList = this.featureIds;
        List<Object> newList = new ArrayList<Object>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.featureIds = newList;
    }

    /**
     * Remove the given featureIds. The featureIds of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeFeatureIds(Object element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Object> oldList = this.featureIds;
        List<Object> newList = new ArrayList<Object>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.featureIds = null;
        } else {
            this.featureIds = newList;
        }
    }

    /**
     * An array of IDs of property tables from the root `EXT_mesh_features` 
     * object. A property table at index `i` corresponds to the `featureIds` 
     * entry at the same index. (optional)<br> 
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
     * object. A property table at index `i` corresponds to the `featureIds` 
     * entry at the same index. (optional)<br> 
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

    /**
     * An array of IDs of property textures from the root `EXT_mesh_features` 
     * object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param propertyTextures The propertyTextures to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setPropertyTextures(List<Integer> propertyTextures) {
        if (propertyTextures == null) {
            this.propertyTextures = propertyTextures;
            return ;
        }
        if (propertyTextures.size()< 1) {
            throw new IllegalArgumentException("Number of propertyTextures elements is < 1");
        }
        this.propertyTextures = propertyTextures;
    }

    /**
     * An array of IDs of property textures from the root `EXT_mesh_features` 
     * object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The propertyTextures
     * 
     */
    public List<Integer> getPropertyTextures() {
        return this.propertyTextures;
    }

    /**
     * Add the given propertyTextures. The propertyTextures of this instance 
     * will be replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addPropertyTextures(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.propertyTextures;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.propertyTextures = newList;
    }

    /**
     * Remove the given propertyTextures. The propertyTextures of this 
     * instance will be replaced with a list that contains all previous 
     * elements, except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removePropertyTextures(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.propertyTextures;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.propertyTextures = null;
        } else {
            this.propertyTextures = newList;
        }
    }

}
