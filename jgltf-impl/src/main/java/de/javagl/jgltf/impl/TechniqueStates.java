/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl;

import java.util.Set;


/**
 * Fixed-function rendering states. 
 * 
 * Auto-generated for technique.states.schema.json 
 * 
 */
public class TechniqueStates
    extends GlTFProperty
{

    /**
     * WebGL states to enable. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Valid values: [3042, 2884, 2929, 32823, 32926, 3089] 
     * 
     */
    private Set<Integer> enable;
    /**
     * Arguments for fixed-function rendering state functions other than 
     * `enable()`/`disable()`. (optional) 
     * 
     */
    private TechniqueStatesFunctions functions;

    /**
     * WebGL states to enable. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Valid values: [3042, 2884, 2929, 32823, 32926, 3089] 
     * 
     * @param enable The enable to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setEnable(Set<Integer> enable) {
        if (enable == null) {
            this.enable = enable;
            return ;
        }
        for (Integer enableElement: enable) {
            if ((((((enableElement!= 3042)&&(enableElement!= 2884))&&(enableElement!= 2929))&&(enableElement!= 32823))&&(enableElement!= 32926))&&(enableElement!= 3089)) {
                throw new IllegalArgumentException((("Invalid value for enableElement: "+ enableElement)+", valid: [3042, 2884, 2929, 32823, 32926, 3089]"));
            }
        }
        this.enable = enable;
    }

    /**
     * WebGL states to enable. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Valid values: [3042, 2884, 2929, 32823, 32926, 3089] 
     * 
     * @return The enable
     * 
     */
    public Set<Integer> getEnable() {
        return this.enable;
    }

    /**
     * Arguments for fixed-function rendering state functions other than 
     * `enable()`/`disable()`. (optional) 
     * 
     * @param functions The functions to set
     * 
     */
    public void setFunctions(TechniqueStatesFunctions functions) {
        if (functions == null) {
            this.functions = functions;
            return ;
        }
        this.functions = functions;
    }

    /**
     * Arguments for fixed-function rendering state functions other than 
     * `enable()`/`disable()`. (optional) 
     * 
     * @return The functions
     * 
     */
    public TechniqueStatesFunctions getFunctions() {
        return this.functions;
    }

}
