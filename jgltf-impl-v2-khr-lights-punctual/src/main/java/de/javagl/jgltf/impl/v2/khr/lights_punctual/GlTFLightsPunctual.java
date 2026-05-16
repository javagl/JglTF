/*
 * glTF KHR_lights_punctual JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.lights_punctual;

import java.util.ArrayList;
import java.util.List;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * Auto-generated for glTF.KHR_lights_punctual.schema.json 
 * 
 */
public class GlTFLightsPunctual
    extends GlTFProperty
{

    /**
     * The lights of this GlTFLightsPunctual (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A directional, point, or spot light. (optional) 
     * 
     */
    private List<Light> lights;

    /**
     * The lights of this GlTFLightsPunctual (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A directional, point, or spot light. (optional) 
     * 
     * @param lights The lights to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setLights(List<Light> lights) {
        if (lights == null) {
            throw new NullPointerException((("Invalid value for lights: "+ lights)+", may not be null"));
        }
        if (lights.size()< 1) {
            throw new IllegalArgumentException("Number of lights elements is < 1");
        }
        this.lights = lights;
    }

    /**
     * The lights of this GlTFLightsPunctual (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A directional, point, or spot light. (optional) 
     * 
     * @return The lights
     * 
     */
    public List<Light> getLights() {
        return this.lights;
    }

    /**
     * Add the given lights. The lights of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addLights(Light element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Light> oldList = this.lights;
        List<Light> newList = new ArrayList<Light>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.lights = newList;
    }

    /**
     * Remove the given lights. The lights of this instance will be replaced 
     * with a list that contains all previous elements, except for the 
     * removed one. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeLights(Light element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Light> oldList = this.lights;
        List<Light> newList = new ArrayList<Light>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.lights = newList;
    }

}
