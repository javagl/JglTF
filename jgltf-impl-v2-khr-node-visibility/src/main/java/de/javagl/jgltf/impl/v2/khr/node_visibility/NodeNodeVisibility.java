/*
 * glTF KHR_node_visibility JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2025 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.node_visibility;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension that defines node's visibility. 
 * 
 * Auto-generated for node.KHR_node_visibility.schema.json 
 * 
 */
public class NodeNodeVisibility
    extends GlTFProperty
{

    /**
     * Specifies whether the node is visible. (optional)<br> 
     * Default: true 
     * 
     */
    private Boolean visible;

    /**
     * Specifies whether the node is visible. (optional)<br> 
     * Default: true 
     * 
     * @param visible The visible to set
     * 
     */
    public void setVisible(Boolean visible) {
        if (visible == null) {
            this.visible = visible;
            return ;
        }
        this.visible = visible;
    }

    /**
     * Specifies whether the node is visible. (optional)<br> 
     * Default: true 
     * 
     * @return The visible
     * 
     */
    public Boolean isVisible() {
        return this.visible;
    }

    /**
     * Returns the default value of the visible<br> 
     * @see #isVisible 
     * 
     * @return The default visible
     * 
     */
    public Boolean defaultVisible() {
        return true;
    }

}
