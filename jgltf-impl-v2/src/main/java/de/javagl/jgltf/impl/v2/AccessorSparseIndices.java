/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;



/**
 * Indices of those attributes that deviate from their initialization 
 * value. 
 * 
 * Auto-generated for accessor.sparse.indices.schema.json 
 * 
 */
public class AccessorSparseIndices
    extends GlTFProperty
{

    /**
     * The index of the bufferView with sparse indices. Referenced bufferView 
     * can't have ARRAY_BUFFER or ELEMENT_ARRAY_BUFFER target. (required) 
     * 
     */
    private Integer bufferView;
    /**
     * The offset relative to the start of the bufferView in bytes. Must be 
     * aligned. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer byteOffset;
    /**
     * The indices data type. (required)<br> 
     * Valid values: [5121, 5123, 5125] 
     * 
     */
    private Integer componentType;

    /**
     * The index of the bufferView with sparse indices. Referenced bufferView 
     * can't have ARRAY_BUFFER or ELEMENT_ARRAY_BUFFER target. (required) 
     * 
     * @param bufferView The bufferView to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setBufferView(Integer bufferView) {
        if (bufferView == null) {
            throw new NullPointerException((("Invalid value for bufferView: "+ bufferView)+", may not be null"));
        }
        this.bufferView = bufferView;
    }

    /**
     * The index of the bufferView with sparse indices. Referenced bufferView 
     * can't have ARRAY_BUFFER or ELEMENT_ARRAY_BUFFER target. (required) 
     * 
     * @return The bufferView
     * 
     */
    public Integer getBufferView() {
        return this.bufferView;
    }

    /**
     * The offset relative to the start of the bufferView in bytes. Must be 
     * aligned. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param byteOffset The byteOffset to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setByteOffset(Integer byteOffset) {
        if (byteOffset == null) {
            this.byteOffset = byteOffset;
            return ;
        }
        if (byteOffset< 0) {
            throw new IllegalArgumentException("byteOffset < 0");
        }
        this.byteOffset = byteOffset;
    }

    /**
     * The offset relative to the start of the bufferView in bytes. Must be 
     * aligned. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The byteOffset
     * 
     */
    public Integer getByteOffset() {
        return this.byteOffset;
    }

    /**
     * Returns the default value of the byteOffset<br> 
     * @see #getByteOffset 
     * 
     * @return The default byteOffset
     * 
     */
    public Integer defaultByteOffset() {
        return  0;
    }

    /**
     * The indices data type. (required)<br> 
     * Valid values: [5121, 5123, 5125] 
     * 
     * @param componentType The componentType to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setComponentType(Integer componentType) {
        if (componentType == null) {
            throw new NullPointerException((("Invalid value for componentType: "+ componentType)+", may not be null"));
        }
        if (((componentType!= 5121)&&(componentType!= 5123))&&(componentType!= 5125)) {
            throw new IllegalArgumentException((("Invalid value for componentType: "+ componentType)+", valid: [5121, 5123, 5125]"));
        }
        this.componentType = componentType;
    }

    /**
     * The indices data type. (required)<br> 
     * Valid values: [5121, 5123, 5125] 
     * 
     * @return The componentType
     * 
     */
    public Integer getComponentType() {
        return this.componentType;
    }

}
