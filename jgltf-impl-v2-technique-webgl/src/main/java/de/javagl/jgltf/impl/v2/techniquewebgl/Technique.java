/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.techniquewebgl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v2.GlTFChildOfRootProperty;


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
     * An array of technique.parameters objects. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An attribute or uniform input to a technique, and an 
     * optional semantic and value. (optional) 
     * 
     */
    private List<TechniqueParameters> parameters;
    /**
     * A dictionary that maps GLSL attribute names to technique parameter 
     * indices. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Integer> attributes;
    /**
     * The index of the program. (required) 
     * 
     */
    private Integer program;
    /**
     * A dictionary that maps GLSL uniform names to technique parameter 
     * indices. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Integer> uniforms;
    /**
     * Fixed-function rendering states. (optional)<br> 
     * Default: {} 
     * 
     */
    private TechniqueStates states;

    /**
     * An array of technique.parameters objects. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An attribute or uniform input to a technique, and an 
     * optional semantic and value. (optional) 
     * 
     * @param parameters The parameters to set
     * 
     */
    public void setParameters(List<TechniqueParameters> parameters) {
        if (parameters == null) {
            this.parameters = parameters;
            return ;
        }
        this.parameters = parameters;
    }

    /**
     * An array of technique.parameters objects. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An attribute or uniform input to a technique, and an 
     * optional semantic and value. (optional) 
     * 
     * @return The parameters
     * 
     */
    public List<TechniqueParameters> getParameters() {
        return this.parameters;
    }

    /**
     * Add the given parameters. The parameters of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addParameters(TechniqueParameters element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<TechniqueParameters> oldList = this.parameters;
        List<TechniqueParameters> newList = new ArrayList<TechniqueParameters>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.parameters = newList;
    }

    /**
     * Remove the given parameters. The parameters of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeParameters(TechniqueParameters element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<TechniqueParameters> oldList = this.parameters;
        List<TechniqueParameters> newList = new ArrayList<TechniqueParameters>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.parameters = null;
        } else {
            this.parameters = newList;
        }
    }

    /**
     * Returns the default value of the parameters<br> 
     * @see #getParameters 
     * 
     * @return The default parameters
     * 
     */
    public List<TechniqueParameters> defaultParameters() {
        return new ArrayList<TechniqueParameters>();
    }

    /**
     * A dictionary that maps GLSL attribute names to technique parameter 
     * indices. (optional)<br> 
     * Default: {} 
     * 
     * @param attributes The attributes to set
     * 
     */
    public void setAttributes(Map<String, Integer> attributes) {
        if (attributes == null) {
            this.attributes = attributes;
            return ;
        }
        this.attributes = attributes;
    }

    /**
     * A dictionary that maps GLSL attribute names to technique parameter 
     * indices. (optional)<br> 
     * Default: {} 
     * 
     * @return The attributes
     * 
     */
    public Map<String, Integer> getAttributes() {
        return this.attributes;
    }

    /**
     * Add the given attributes. The attributes of this instance will be 
     * replaced with a map that contains all previous mappings, and 
     * additionally the new mapping. 
     * 
     * @param key The key
     * @param value The value
     * @throws NullPointerException If the given key or value is <code>null</code>
     * 
     */
    public void addAttributes(String key, Integer value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, Integer> oldMap = this.attributes;
        Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.put(key, value);
        this.attributes = newMap;
    }

    /**
     * Remove the given attributes. The attributes of this instance will be 
     * replaced with a map that contains all previous mappings, except for 
     * the one with the given key.<br> 
     * If this new map would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param key The key
     * @throws NullPointerException If the given key is <code>null</code>
     * 
     */
    public void removeAttributes(String key) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        Map<String, Integer> oldMap = this.attributes;
        Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.remove(key);
        if (newMap.isEmpty()) {
            this.attributes = null;
        } else {
            this.attributes = newMap;
        }
    }

    /**
     * Returns the default value of the attributes<br> 
     * @see #getAttributes 
     * 
     * @return The default attributes
     * 
     */
    public Map<String, Integer> defaultAttributes() {
        return new LinkedHashMap<String, Integer>();
    }

    /**
     * The index of the program. (required) 
     * 
     * @param program The program to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setProgram(Integer program) {
        if (program == null) {
            throw new NullPointerException((("Invalid value for program: "+ program)+", may not be null"));
        }
        this.program = program;
    }

    /**
     * The index of the program. (required) 
     * 
     * @return The program
     * 
     */
    public Integer getProgram() {
        return this.program;
    }

    /**
     * A dictionary that maps GLSL uniform names to technique parameter 
     * indices. (optional)<br> 
     * Default: {} 
     * 
     * @param uniforms The uniforms to set
     * 
     */
    public void setUniforms(Map<String, Integer> uniforms) {
        if (uniforms == null) {
            this.uniforms = uniforms;
            return ;
        }
        this.uniforms = uniforms;
    }

    /**
     * A dictionary that maps GLSL uniform names to technique parameter 
     * indices. (optional)<br> 
     * Default: {} 
     * 
     * @return The uniforms
     * 
     */
    public Map<String, Integer> getUniforms() {
        return this.uniforms;
    }

    /**
     * Add the given uniforms. The uniforms of this instance will be replaced 
     * with a map that contains all previous mappings, and additionally the 
     * new mapping. 
     * 
     * @param key The key
     * @param value The value
     * @throws NullPointerException If the given key or value is <code>null</code>
     * 
     */
    public void addUniforms(String key, Integer value) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("The value may not be null");
        }
        Map<String, Integer> oldMap = this.uniforms;
        Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.put(key, value);
        this.uniforms = newMap;
    }

    /**
     * Remove the given uniforms. The uniforms of this instance will be 
     * replaced with a map that contains all previous mappings, except for 
     * the one with the given key.<br> 
     * If this new map would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param key The key
     * @throws NullPointerException If the given key is <code>null</code>
     * 
     */
    public void removeUniforms(String key) {
        if (key == null) {
            throw new NullPointerException("The key may not be null");
        }
        Map<String, Integer> oldMap = this.uniforms;
        Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
        if (oldMap!= null) {
            newMap.putAll(oldMap);
        }
        newMap.remove(key);
        if (newMap.isEmpty()) {
            this.uniforms = null;
        } else {
            this.uniforms = newMap;
        }
    }

    /**
     * Returns the default value of the uniforms<br> 
     * @see #getUniforms 
     * 
     * @return The default uniforms
     * 
     */
    public Map<String, Integer> defaultUniforms() {
        return new LinkedHashMap<String, Integer>();
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

    /**
     * Returns the default value of the states<br> 
     * @see #getStates 
     * 
     * @return The default states
     * 
     */
    public TechniqueStates defaultStates() {
        return new TechniqueStates();
    }

}
