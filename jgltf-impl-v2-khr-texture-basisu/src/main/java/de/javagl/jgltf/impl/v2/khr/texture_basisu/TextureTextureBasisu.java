/*
 * glTF KHR_texture_basisu JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2025 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.texture_basisu;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension to specify textures using the KTX v2 images with Basis 
 * Universal supercompression. 
 * 
 * Auto-generated for texture.KHR_texture_basisu.schema.json 
 * 
 */
public class TextureTextureBasisu
    extends GlTFProperty
{

    /**
     * The index of the image which points to a KTX v2 resource with Basis 
     * Universal supercompression. (optional) 
     * 
     */
    private Integer source;

    /**
     * The index of the image which points to a KTX v2 resource with Basis 
     * Universal supercompression. (optional) 
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
     * The index of the image which points to a KTX v2 resource with Basis 
     * Universal supercompression. (optional) 
     * 
     * @return The source
     * 
     */
    public Integer getSource() {
        return this.source;
    }

}
