/*
 * glTF EXT_texture_webp JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.texture_webp;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension to specify textures using the WebP image format. 
 * 
 * Auto-generated for glTF.EXT_texture_webp.schema.json 
 * 
 */
public class GlTFTextureWebp
    extends GlTFProperty
{

    /**
     * The index of the WebP image. (optional) 
     * 
     */
    private Integer source;

    /**
     * The index of the WebP image. (optional) 
     * 
     * @param source The source to set
     * 
     */
    public void setSource(Integer source) {
        if (source == null) {
            this.source = source;
            return ;
        }
        this.source = source;
    }

    /**
     * The index of the WebP image. (optional) 
     * 
     * @return The source
     * 
     */
    public Integer getSource() {
        return this.source;
    }

}
