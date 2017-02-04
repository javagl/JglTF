/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.techniquewebgl;

import de.javagl.jgltf.impl.v2.GlTFChildOfRootProperty;

/**
 * A vertex or fragment shader. 
 * 
 * Auto-generated for shader.schema.json 
 * 
 */
public class Shader
    extends GlTFChildOfRootProperty
{

    /**
     * The uri of the GLSL source. (optional) 
     * 
     */
    private String uri;
    /**
     * The shader stage. (required)<br> 
     * Valid values: [35632, 35633] 
     * 
     */
    private Integer type;
    /**
     * The index of the bufferView that contains the shader source. Use this 
     * instead of the shader's uri property. (optional) 
     * 
     */
    private Integer bufferView;

    /**
     * The uri of the GLSL source. (optional) 
     * 
     * @param uri The uri to set
     * 
     */
    public void setUri(String uri) {
        if (uri == null) {
            this.uri = uri;
            return ;
        }
        this.uri = uri;
    }

    /**
     * The uri of the GLSL source. (optional) 
     * 
     * @return The uri
     * 
     */
    public String getUri() {
        return this.uri;
    }

    /**
     * The shader stage. (required)<br> 
     * Valid values: [35632, 35633] 
     * 
     * @param type The type to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setType(Integer type) {
        if (type == null) {
            throw new NullPointerException((("Invalid value for type: "+ type)+", may not be null"));
        }
        if ((type!= 35632)&&(type!= 35633)) {
            throw new IllegalArgumentException((("Invalid value for type: "+ type)+", valid: [35632, 35633]"));
        }
        this.type = type;
    }

    /**
     * The shader stage. (required)<br> 
     * Valid values: [35632, 35633] 
     * 
     * @return The type
     * 
     */
    public Integer getType() {
        return this.type;
    }

    /**
     * The index of the bufferView that contains the shader source. Use this 
     * instead of the shader's uri property. (optional) 
     * 
     * @param bufferView The bufferView to set
     * 
     */
    public void setBufferView(Integer bufferView) {
        if (bufferView == null) {
            this.bufferView = bufferView;
            return ;
        }
        this.bufferView = bufferView;
    }

    /**
     * The index of the bufferView that contains the shader source. Use this 
     * instead of the shader's uri property. (optional) 
     * 
     * @return The bufferView
     * 
     */
    public Integer getBufferView() {
        return this.bufferView;
    }

}
