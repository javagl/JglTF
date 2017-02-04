/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf2.impl;



/**
 * A buffer points to binary geometry, animation, or skins. 
 * 
 * Auto-generated for buffer.schema.json 
 * 
 */
public class Buffer
    extends GlTFChildOfRootProperty
{

    /**
     * The uri of the buffer. (required) 
     * 
     */
    private String uri;
    /**
     * The length of the buffer in bytes. (required)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer byteLength = 0;

    /**
     * The uri of the buffer. (required) 
     * 
     * @param uri The uri to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setUri(String uri) {
        if (uri == null) {
            throw new NullPointerException((("Invalid value for uri: "+ uri)+", may not be null"));
        }
        this.uri = uri;
    }

    /**
     * The uri of the buffer. (required) 
     * 
     * @return The uri
     * 
     */
    public String getUri() {
        return this.uri;
    }

    /**
     * The length of the buffer in bytes. (required)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
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
        if (byteLength< 0) {
            throw new IllegalArgumentException("byteLength < 0");
        }
        this.byteLength = byteLength;
    }

    /**
     * The length of the buffer in bytes. (required)<br> 
     * Default: 0<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The byteLength
     * 
     */
    public Integer getByteLength() {
        return this.byteLength;
    }

}
