/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.techniquewebgl;

import java.util.ArrayList;
import java.util.List;


/**
 * Auto-generated for glTF.KHR_technique_webgl.schema.json 
 * 
 */
public class GlTFKHR_technique_webgl {

    /**
     * An array of shaders. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A vertex or fragment shader. (optional) 
     * 
     */
    private List<Shader> shaders;
    /**
     * An array of techniques. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A template for material appearances. (optional) 
     * 
     */
    private List<Technique> techniques;
    /**
     * An array of programs. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A shader program, including its vertex and fragment 
     * shader, and names of vertex shader attributes. (optional) 
     * 
     */
    private List<Program> programs;

    /**
     * An array of shaders. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A vertex or fragment shader. (optional) 
     * 
     * @param shaders The shaders to set
     * 
     */
    public void setShaders(List<Shader> shaders) {
        if (shaders == null) {
            this.shaders = shaders;
            return ;
        }
        this.shaders = shaders;
    }

    /**
     * An array of shaders. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A vertex or fragment shader. (optional) 
     * 
     * @return The shaders
     * 
     */
    public List<Shader> getShaders() {
        return this.shaders;
    }

    /**
     * Add the given shaders. The shaders of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addShaders(Shader element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Shader> oldList = this.shaders;
        List<Shader> newList = new ArrayList<Shader>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.shaders = newList;
    }

    /**
     * Remove the given shaders. The shaders of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeShaders(Shader element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Shader> oldList = this.shaders;
        List<Shader> newList = new ArrayList<Shader>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.shaders = null;
        } else {
            this.shaders = newList;
        }
    }

    /**
     * Returns the default value of the shaders<br> 
     * @see #getShaders 
     * 
     * @return The default shaders
     * 
     */
    public List<Shader> defaultShaders() {
        return new ArrayList<Shader>();
    }

    /**
     * An array of techniques. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A template for material appearances. (optional) 
     * 
     * @param techniques The techniques to set
     * 
     */
    public void setTechniques(List<Technique> techniques) {
        if (techniques == null) {
            this.techniques = techniques;
            return ;
        }
        this.techniques = techniques;
    }

    /**
     * An array of techniques. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A template for material appearances. (optional) 
     * 
     * @return The techniques
     * 
     */
    public List<Technique> getTechniques() {
        return this.techniques;
    }

    /**
     * Add the given techniques. The techniques of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addTechniques(Technique element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Technique> oldList = this.techniques;
        List<Technique> newList = new ArrayList<Technique>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.techniques = newList;
    }

    /**
     * Remove the given techniques. The techniques of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeTechniques(Technique element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Technique> oldList = this.techniques;
        List<Technique> newList = new ArrayList<Technique>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.techniques = null;
        } else {
            this.techniques = newList;
        }
    }

    /**
     * Returns the default value of the techniques<br> 
     * @see #getTechniques 
     * 
     * @return The default techniques
     * 
     */
    public List<Technique> defaultTechniques() {
        return new ArrayList<Technique>();
    }

    /**
     * An array of programs. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A shader program, including its vertex and fragment 
     * shader, and names of vertex shader attributes. (optional) 
     * 
     * @param programs The programs to set
     * 
     */
    public void setPrograms(List<Program> programs) {
        if (programs == null) {
            this.programs = programs;
            return ;
        }
        this.programs = programs;
    }

    /**
     * An array of programs. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A shader program, including its vertex and fragment 
     * shader, and names of vertex shader attributes. (optional) 
     * 
     * @return The programs
     * 
     */
    public List<Program> getPrograms() {
        return this.programs;
    }

    /**
     * Add the given programs. The programs of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addPrograms(Program element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Program> oldList = this.programs;
        List<Program> newList = new ArrayList<Program>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.programs = newList;
    }

    /**
     * Remove the given programs. The programs of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removePrograms(Program element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Program> oldList = this.programs;
        List<Program> newList = new ArrayList<Program>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.programs = null;
        } else {
            this.programs = newList;
        }
    }

    /**
     * Returns the default value of the programs<br> 
     * @see #getPrograms 
     * 
     * @return The default programs
     * 
     */
    public List<Program> defaultPrograms() {
        return new ArrayList<Program>();
    }

}
