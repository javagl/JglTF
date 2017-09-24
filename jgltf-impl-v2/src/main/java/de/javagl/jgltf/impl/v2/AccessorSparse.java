/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;



/**
 * Sparse storage of attributes that deviate from their initialization 
 * value. 
 * 
 * Auto-generated for accessor.sparse.schema.json 
 * 
 */
public class AccessorSparse
    extends GlTFProperty
{

    /**
     * Number of entries stored in the sparse array. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     */
    private Integer count;
    /**
     * Index array of size `count` that points to those accessor attributes 
     * that deviate from their initialization value. Indices must strictly 
     * increase. (required) 
     * 
     */
    private AccessorSparseIndices indices;
    /**
     * Array of size `count` times number of components, storing the 
     * displaced accessor attributes pointed by `indices`. Substituted values 
     * must have the same `componentType` and number of components as the 
     * base accessor. (required) 
     * 
     */
    private AccessorSparseValues values;

    /**
     * Number of entries stored in the sparse array. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @param count The count to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setCount(Integer count) {
        if (count == null) {
            throw new NullPointerException((("Invalid value for count: "+ count)+", may not be null"));
        }
        if (count< 1) {
            throw new IllegalArgumentException("count < 1");
        }
        this.count = count;
    }

    /**
     * Number of entries stored in the sparse array. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @return The count
     * 
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * Index array of size `count` that points to those accessor attributes 
     * that deviate from their initialization value. Indices must strictly 
     * increase. (required) 
     * 
     * @param indices The indices to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setIndices(AccessorSparseIndices indices) {
        if (indices == null) {
            throw new NullPointerException((("Invalid value for indices: "+ indices)+", may not be null"));
        }
        this.indices = indices;
    }

    /**
     * Index array of size `count` that points to those accessor attributes 
     * that deviate from their initialization value. Indices must strictly 
     * increase. (required) 
     * 
     * @return The indices
     * 
     */
    public AccessorSparseIndices getIndices() {
        return this.indices;
    }

    /**
     * Array of size `count` times number of components, storing the 
     * displaced accessor attributes pointed by `indices`. Substituted values 
     * must have the same `componentType` and number of components as the 
     * base accessor. (required) 
     * 
     * @param values The values to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setValues(AccessorSparseValues values) {
        if (values == null) {
            throw new NullPointerException((("Invalid value for values: "+ values)+", may not be null"));
        }
        this.values = values;
    }

    /**
     * Array of size `count` times number of components, storing the 
     * displaced accessor attributes pointed by `indices`. Substituted values 
     * must have the same `componentType` and number of components as the 
     * base accessor. (required) 
     * 
     * @return The values
     * 
     */
    public AccessorSparseValues getValues() {
        return this.values;
    }

}
