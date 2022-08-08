/*
 * EXT_structural_metadata JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.structural_metadata;

import java.util.ArrayList;
import java.util.List;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * Structural metadata about a glTF primitive. 
 * 
 * Auto-generated for mesh.primitive.EXT_structural_metadata.schema.json 
 * 
 */
public class MeshPrimitiveStructuralMetadata
    extends GlTFProperty
{

    /**
     * An array of indexes of property textures in the root 
     * `EXT_structural_metadata` object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<Integer> propertyTextures;
    /**
     * An array of indexes of property attributes in the root 
     * `EXT_structural_metadata` object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<Integer> propertyAttributes;

    /**
     * An array of indexes of property textures in the root 
     * `EXT_structural_metadata` object. (optional)<br> 
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
     * An array of indexes of property textures in the root 
     * `EXT_structural_metadata` object. (optional)<br> 
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

    /**
     * An array of indexes of property attributes in the root 
     * `EXT_structural_metadata` object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param propertyAttributes The propertyAttributes to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setPropertyAttributes(List<Integer> propertyAttributes) {
        if (propertyAttributes == null) {
            this.propertyAttributes = propertyAttributes;
            return ;
        }
        if (propertyAttributes.size()< 1) {
            throw new IllegalArgumentException("Number of propertyAttributes elements is < 1");
        }
        this.propertyAttributes = propertyAttributes;
    }

    /**
     * An array of indexes of property attributes in the root 
     * `EXT_structural_metadata` object. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The propertyAttributes
     * 
     */
    public List<Integer> getPropertyAttributes() {
        return this.propertyAttributes;
    }

    /**
     * Add the given propertyAttributes. The propertyAttributes of this 
     * instance will be replaced with a list that contains all previous 
     * elements, and additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addPropertyAttributes(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.propertyAttributes;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.propertyAttributes = newList;
    }

    /**
     * Remove the given propertyAttributes. The propertyAttributes of this 
     * instance will be replaced with a list that contains all previous 
     * elements, except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removePropertyAttributes(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.propertyAttributes;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.propertyAttributes = null;
        } else {
            this.propertyAttributes = newList;
        }
    }

}
