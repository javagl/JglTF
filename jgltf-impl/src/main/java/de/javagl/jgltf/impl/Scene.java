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
 * The root nodes of a scene. 
 * 
 * Auto-generated for scene.schema.json 
 * 
 */
public class Scene
    extends GlTFChildOfRootProperty
{

    /**
     * The IDs of each root node. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<String> nodes;

    /**
     * The IDs of each root node. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param nodes The nodes to set
     * 
     */
    public void setNodes(List<String> nodes) {
        if (nodes == null) {
            this.nodes = nodes;
            return ;
        }
        this.nodes = nodes;
    }

    /**
     * The IDs of each root node. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The nodes
     * 
     */
    public List<String> getNodes() {
        return this.nodes;
    }

}
