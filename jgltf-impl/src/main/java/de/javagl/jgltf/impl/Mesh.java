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
 * A set of primitives to be rendered. A node can contain one or more 
 * meshes. A node's transform places the mesh in the scene. 
 * 
 * Auto-generated for mesh.schema.json 
 * 
 */
public class Mesh
    extends GlTFChildOfRootProperty
{

    /**
     * An array of primitives, each defining geometry to be rendered with a 
     * material. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Geometry to be rendered with the given material. 
     * (optional) 
     * 
     */
    private List<MeshPrimitive> primitives;

    /**
     * An array of primitives, each defining geometry to be rendered with a 
     * material. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Geometry to be rendered with the given material. 
     * (optional) 
     * 
     * @param primitives The primitives to set
     * 
     */
    public void setPrimitives(List<MeshPrimitive> primitives) {
        if (primitives == null) {
            this.primitives = primitives;
            return ;
        }
        this.primitives = primitives;
    }

    /**
     * An array of primitives, each defining geometry to be rendered with a 
     * material. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Geometry to be rendered with the given material. 
     * (optional) 
     * 
     * @return The primitives
     * 
     */
    public List<MeshPrimitive> getPrimitives() {
        return this.primitives;
    }

}
