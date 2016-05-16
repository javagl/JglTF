/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl;

import java.util.List;
import java.util.Map;


/**
 * A keyframe animation. 
 * 
 * Auto-generated for animation.schema.json 
 * 
 */
public class Animation
    extends GlTFChildOfRootProperty
{

    /**
     * An array of channels, each of which targets an animation's sampler at 
     * a node's property. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Targets an animation's sampler at a node's property. 
     * (optional) 
     * 
     */
    private List<AnimationChannel> channels;
    /**
     * A dictionary object of strings whose values are IDs of accessors with 
     * keyframe data, e.g., time, translation, rotation, etc. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, String> parameters;
    /**
     * A dictionary object of samplers that combines input and output 
     * parameters with an interpolation algorithm to define a keyframe graph 
     * (but not its target). (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, AnimationSampler> samplers;

    /**
     * An array of channels, each of which targets an animation's sampler at 
     * a node's property. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Targets an animation's sampler at a node's property. 
     * (optional) 
     * 
     * @param channels The channels to set
     * 
     */
    public void setChannels(List<AnimationChannel> channels) {
        if (channels == null) {
            this.channels = channels;
            return ;
        }
        this.channels = channels;
    }

    /**
     * An array of channels, each of which targets an animation's sampler at 
     * a node's property. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Targets an animation's sampler at a node's property. 
     * (optional) 
     * 
     * @return The channels
     * 
     */
    public List<AnimationChannel> getChannels() {
        return this.channels;
    }

    /**
     * A dictionary object of strings whose values are IDs of accessors with 
     * keyframe data, e.g., time, translation, rotation, etc. (optional)<br> 
     * Default: {} 
     * 
     * @param parameters The parameters to set
     * 
     */
    public void setParameters(Map<String, String> parameters) {
        if (parameters == null) {
            this.parameters = parameters;
            return ;
        }
        this.parameters = parameters;
    }

    /**
     * A dictionary object of strings whose values are IDs of accessors with 
     * keyframe data, e.g., time, translation, rotation, etc. (optional)<br> 
     * Default: {} 
     * 
     * @return The parameters
     * 
     */
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    /**
     * A dictionary object of samplers that combines input and output 
     * parameters with an interpolation algorithm to define a keyframe graph 
     * (but not its target). (optional)<br> 
     * Default: {} 
     * 
     * @param samplers The samplers to set
     * 
     */
    public void setSamplers(Map<String, AnimationSampler> samplers) {
        if (samplers == null) {
            this.samplers = samplers;
            return ;
        }
        this.samplers = samplers;
    }

    /**
     * A dictionary object of samplers that combines input and output 
     * parameters with an interpolation algorithm to define a keyframe graph 
     * (but not its target). (optional)<br> 
     * Default: {} 
     * 
     * @return The samplers
     * 
     */
    public Map<String, AnimationSampler> getSamplers() {
        return this.samplers;
    }

}
