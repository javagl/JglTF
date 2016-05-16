/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl;

import java.util.Map;


/**
 * A template for material appearances. 
 * 
 * Auto-generated for technique.schema.json 
 * 
 */
public class Technique
    extends GlTFChildOfRootProperty
{

    /**
     * A dictionary object of technique.parameters objects. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, TechniqueParameters> parameters;
    /**
     * A dictionary object of strings that maps GLSL attribute names to 
     * technique parameter IDs. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, String> attributes;
    /**
     * The ID of the program. (required) 
     * 
     */
    private String program;
    /**
     * A dictionary object of strings that maps GLSL uniform names to 
     * technique parameter IDs. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, String> uniforms;
    /**
     * Fixed-function rendering states. (optional)<br> 
     * Default: {} 
     * 
     */
    private TechniqueStates states;

    /**
     * A dictionary object of technique.parameters objects. (optional)<br> 
     * Default: {} 
     * 
     * @param parameters The parameters to set
     * 
     */
    public void setParameters(Map<String, TechniqueParameters> parameters) {
        if (parameters == null) {
            this.parameters = parameters;
            return ;
        }
        this.parameters = parameters;
    }

    /**
     * A dictionary object of technique.parameters objects. (optional)<br> 
     * Default: {} 
     * 
     * @return The parameters
     * 
     */
    public Map<String, TechniqueParameters> getParameters() {
        return this.parameters;
    }

    /**
     * A dictionary object of strings that maps GLSL attribute names to 
     * technique parameter IDs. (optional)<br> 
     * Default: {} 
     * 
     * @param attributes The attributes to set
     * 
     */
    public void setAttributes(Map<String, String> attributes) {
        if (attributes == null) {
            this.attributes = attributes;
            return ;
        }
        this.attributes = attributes;
    }

    /**
     * A dictionary object of strings that maps GLSL attribute names to 
     * technique parameter IDs. (optional)<br> 
     * Default: {} 
     * 
     * @return The attributes
     * 
     */
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    /**
     * The ID of the program. (required) 
     * 
     * @param program The program to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setProgram(String program) {
        if (program == null) {
            throw new NullPointerException((("Invalid value for program: "+ program)+", may not be null"));
        }
        this.program = program;
    }

    /**
     * The ID of the program. (required) 
     * 
     * @return The program
     * 
     */
    public String getProgram() {
        return this.program;
    }

    /**
     * A dictionary object of strings that maps GLSL uniform names to 
     * technique parameter IDs. (optional)<br> 
     * Default: {} 
     * 
     * @param uniforms The uniforms to set
     * 
     */
    public void setUniforms(Map<String, String> uniforms) {
        if (uniforms == null) {
            this.uniforms = uniforms;
            return ;
        }
        this.uniforms = uniforms;
    }

    /**
     * A dictionary object of strings that maps GLSL uniform names to 
     * technique parameter IDs. (optional)<br> 
     * Default: {} 
     * 
     * @return The uniforms
     * 
     */
    public Map<String, String> getUniforms() {
        return this.uniforms;
    }

    /**
     * Fixed-function rendering states. (optional)<br> 
     * Default: {} 
     * 
     * @param states The states to set
     * 
     */
    public void setStates(TechniqueStates states) {
        if (states == null) {
            this.states = states;
            return ;
        }
        this.states = states;
    }

    /**
     * Fixed-function rendering states. (optional)<br> 
     * Default: {} 
     * 
     * @return The states
     * 
     */
    public TechniqueStates getStates() {
        return this.states;
    }

}
