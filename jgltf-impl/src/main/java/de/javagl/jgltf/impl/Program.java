/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl;

import java.util.List;


/**
 * A shader program, including its vertex and fragment shader, and names 
 * of vertex shader attributes. 
 * 
 * Auto-generated for program.schema.json 
 * 
 */
public class Program
    extends GlTFChildOfRootProperty
{

    /**
     * Names of GLSL vertex shader attributes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<String> attributes;
    /**
     * The ID of the fragment shader. (required) 
     * 
     */
    private String fragmentShader;
    /**
     * The ID of the vertex shader. (required) 
     * 
     */
    private String vertexShader;

    /**
     * Names of GLSL vertex shader attributes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param attributes The attributes to set
     * 
     */
    public void setAttributes(List<String> attributes) {
        if (attributes == null) {
            this.attributes = attributes;
            return ;
        }
        this.attributes = attributes;
    }

    /**
     * Names of GLSL vertex shader attributes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The attributes
     * 
     */
    public List<String> getAttributes() {
        return this.attributes;
    }

    /**
     * The ID of the fragment shader. (required) 
     * 
     * @param fragmentShader The fragmentShader to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setFragmentShader(String fragmentShader) {
        if (fragmentShader == null) {
            throw new NullPointerException((("Invalid value for fragmentShader: "+ fragmentShader)+", may not be null"));
        }
        this.fragmentShader = fragmentShader;
    }

    /**
     * The ID of the fragment shader. (required) 
     * 
     * @return The fragmentShader
     * 
     */
    public String getFragmentShader() {
        return this.fragmentShader;
    }

    /**
     * The ID of the vertex shader. (required) 
     * 
     * @param vertexShader The vertexShader to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setVertexShader(String vertexShader) {
        if (vertexShader == null) {
            throw new NullPointerException((("Invalid value for vertexShader: "+ vertexShader)+", may not be null"));
        }
        this.vertexShader = vertexShader;
    }

    /**
     * The ID of the vertex shader. (required) 
     * 
     * @return The vertexShader
     * 
     */
    public String getVertexShader() {
        return this.vertexShader;
    }

}
