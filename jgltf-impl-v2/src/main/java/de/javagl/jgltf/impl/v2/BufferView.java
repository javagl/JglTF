/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;



/**
 * A view into a buffer generally representing a subset of the buffer. 
 * 
 * Auto-generated for bufferView.schema.json 
 * 
 */
public class BufferView
    extends GlTFChildOfRootProperty
{

    /**
     * The index of the buffer. (required) 
     * 
     */
    private Integer buffer;
    /**
     * The offset into the buffer in bytes. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer byteOffset;
    /**
     * The length of the bufferView in bytes. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     */
    private Integer byteLength;
    /**
     * The stride, in bytes. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive)<br> 
     * Maximum: 255 (inclusive) 
     * 
     */
    private Integer byteStride;
    /**
     * The target that the GPU buffer should be bound to. (optional) 
     * 
     */
    private Integer target;

    /**
     * The index of the buffer. (required) 
     * 
     * @param buffer The buffer to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setBuffer(Integer buffer) {
        if (buffer == null) {
            throw new NullPointerException((("Invalid value for buffer: "+ buffer)+", may not be null"));
        }
        this.buffer = buffer;
    }

    /**
     * The index of the buffer. (required) 
     * 
     * @return The buffer
     * 
     */
    public Integer getBuffer() {
        return this.buffer;
    }

    /**
     * The offset into the buffer in bytes. (optional)<br> 
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
     * The offset into the buffer in bytes. (optional)<br> 
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
     * The length of the bufferView in bytes. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @param byteLength The byteLength to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setByteLength(Integer byteLength) {
        if (byteLength == null) {
            throw new NullPointerException((("Invalid value for byteLength: "+ byteLength)+", may not be null"));
        }
        if (byteLength< 1) {
            throw new IllegalArgumentException("byteLength < 1");
        }
        this.byteLength = byteLength;
    }

    /**
     * The length of the bufferView in bytes. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @return The byteLength
     * 
     */
    public Integer getByteLength() {
        return this.byteLength;
    }

    /**
     * The stride, in bytes. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive)<br> 
     * Maximum: 255 (inclusive) 
     * 
     * @param byteStride The byteStride to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setByteStride(Integer byteStride) {
        if (byteStride == null) {
            this.byteStride = byteStride;
            return ;
        }
        if (byteStride > 255) {
            throw new IllegalArgumentException("byteStride > 255");
        }
        if (byteStride< 0) {
            throw new IllegalArgumentException("byteStride < 0");
        }
        this.byteStride = byteStride;
    }

    /**
     * The stride, in bytes. (optional)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive)<br> 
     * Maximum: 255 (inclusive) 
     * 
     * @return The byteStride
     * 
     */
    public Integer getByteStride() {
        return this.byteStride;
    }

    /**
     * Returns the default value of the byteStride<br> 
     * @see #getByteStride 
     * 
     * @return The default byteStride
     * 
     */
    public Integer defaultByteStride() {
        return  0;
    }

    /**
     * The target that the GPU buffer should be bound to. (optional) 
     * 
     * @param target The target to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setTarget(Integer target) {
        if (target == null) {
            this.target = target;
            return ;
        }
        if ((target!= 34962)&&(target!= 34963)) {
            throw new IllegalArgumentException((("Invalid value for target: "+ target)+", valid: [34962, 34963]"));
        }
        this.target = target;
    }

    /**
     * The target that the GPU buffer should be bound to. (optional) 
     * 
     * @return The target
     * 
     */
    public Integer getTarget() {
        return this.target;
    }

}
