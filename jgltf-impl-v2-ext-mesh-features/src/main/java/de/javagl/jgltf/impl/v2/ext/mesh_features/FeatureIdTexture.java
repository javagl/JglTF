/*
 * 3D Tiles EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * An object describing a texture used for storing per-texel feature IDs. 
 * 
 * Auto-generated for featureIdTexture.schema.json 
 * 
 */
public class FeatureIdTexture
    extends TextureInfo
{

    /**
     * Single channel index storing per-texel feature IDs. (required)<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer channel;

    /**
     * Single channel index storing per-texel feature IDs. (required)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param channel The channel to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setChannel(Integer channel) {
        if (channel == null) {
            throw new NullPointerException((("Invalid value for channel: "+ channel)+", may not be null"));
        }
        if (channel< 0) {
            throw new IllegalArgumentException("channel < 0");
        }
        this.channel = channel;
    }

    /**
     * Single channel index storing per-texel feature IDs. (required)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The channel
     * 
     */
    public Integer getChannel() {
        return this.channel;
    }

}
