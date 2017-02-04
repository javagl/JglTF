/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;

import java.util.ArrayList;
import java.util.List;


/**
 * Joints and matrices defining a skin. 
 * 
 * Auto-generated for skin.schema.json 
 * 
 */
public class Skin
    extends GlTFChildOfRootProperty
{

    /**
     * Floating-point 4x4 transformation matrix stored in column-major order. 
     * (optional)<br> 
     * Default: 
     * [1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0]<br> 
     * Number of items: 16<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private float[] bindShapeMatrix;
    /**
     * The index of the accessor containing the floating-point 4x4 
     * inverse-bind matrices. The default is that each matrix is a 4x4 
     * identity matrix, which implies that inverse-bind matrices were 
     * pre-applied. (optional) 
     * 
     */
    private Integer inverseBindMatrices;
    /**
     * Joint names of the joints (nodes with a `jointName` property) in this 
     * skin. (required)<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0 (inclusive) 
     * 
     */
    private List<Integer> jointNames;

    /**
     * Floating-point 4x4 transformation matrix stored in column-major order. 
     * (optional)<br> 
     * Default: 
     * [1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0]<br> 
     * Number of items: 16<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param bindShapeMatrix The bindShapeMatrix to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setBindShapeMatrix(float[] bindShapeMatrix) {
        if (bindShapeMatrix == null) {
            this.bindShapeMatrix = bindShapeMatrix;
            return ;
        }
        if (bindShapeMatrix.length< 16) {
            throw new IllegalArgumentException("Number of bindShapeMatrix elements is < 16");
        }
        if (bindShapeMatrix.length > 16) {
            throw new IllegalArgumentException("Number of bindShapeMatrix elements is > 16");
        }
        this.bindShapeMatrix = bindShapeMatrix;
    }

    /**
     * Floating-point 4x4 transformation matrix stored in column-major order. 
     * (optional)<br> 
     * Default: 
     * [1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0]<br> 
     * Number of items: 16<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The bindShapeMatrix
     * 
     */
    public float[] getBindShapeMatrix() {
        return this.bindShapeMatrix;
    }

    /**
     * Returns the default value of the bindShapeMatrix<br> 
     * @see #getBindShapeMatrix 
     * 
     * @return The default bindShapeMatrix
     * 
     */
    public float[] defaultBindShapeMatrix() {
        return new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F };
    }

    /**
     * The index of the accessor containing the floating-point 4x4 
     * inverse-bind matrices. The default is that each matrix is a 4x4 
     * identity matrix, which implies that inverse-bind matrices were 
     * pre-applied. (optional) 
     * 
     * @param inverseBindMatrices The inverseBindMatrices to set
     * 
     */
    public void setInverseBindMatrices(Integer inverseBindMatrices) {
        if (inverseBindMatrices == null) {
            this.inverseBindMatrices = inverseBindMatrices;
            return ;
        }
        this.inverseBindMatrices = inverseBindMatrices;
    }

    /**
     * The index of the accessor containing the floating-point 4x4 
     * inverse-bind matrices. The default is that each matrix is a 4x4 
     * identity matrix, which implies that inverse-bind matrices were 
     * pre-applied. (optional) 
     * 
     * @return The inverseBindMatrices
     * 
     */
    public Integer getInverseBindMatrices() {
        return this.inverseBindMatrices;
    }

    /**
     * Joint names of the joints (nodes with a `jointName` property) in this 
     * skin. (required)<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0 (inclusive) 
     * 
     * @param jointNames The jointNames to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setJointNames(List<Integer> jointNames) {
        if (jointNames == null) {
            throw new NullPointerException((("Invalid value for jointNames: "+ jointNames)+", may not be null"));
        }
        for (Integer jointNamesElement: jointNames) {
            if (jointNamesElement< 0) {
                throw new IllegalArgumentException("jointNamesElement < 0");
            }
        }
        this.jointNames = jointNames;
    }

    /**
     * Joint names of the joints (nodes with a `jointName` property) in this 
     * skin. (required)<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0 (inclusive) 
     * 
     * @return The jointNames
     * 
     */
    public List<Integer> getJointNames() {
        return this.jointNames;
    }

    /**
     * Add the given jointNames. The jointNames of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addJointNames(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.jointNames;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.jointNames = newList;
    }

    /**
     * Remove the given jointNames. The jointNames of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeJointNames(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.jointNames;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.jointNames = newList;
    }

}
