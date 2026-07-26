/*
 * glTF KHR_materials_transmission JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_transmission;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * glTF extension that defines the optical transmission of a material. 
 * 
 * Auto-generated for material.KHR_materials_transmission.schema.json 
 * 
 */
public class MaterialMaterialsTransmission
    extends GlTFProperty
{

    /**
     * The base percentage of light transmitted through the surface. 
     * (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     */
    private Double transmissionFactor;
    /**
     * A texture that defines the transmission percentage of the surface, 
     * sampled from the R channel. These values are linear, and will be 
     * multiplied by transmissionFactor. (optional) 
     * 
     */
    private TextureInfo transmissionTexture;

    /**
     * The base percentage of light transmitted through the surface. 
     * (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param transmissionFactor The transmissionFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setTransmissionFactor(Double transmissionFactor) {
        if (transmissionFactor == null) {
            this.transmissionFactor = transmissionFactor;
            return ;
        }
        if (transmissionFactor > 1.0D) {
            throw new IllegalArgumentException("transmissionFactor > 1.0");
        }
        if (transmissionFactor< 0.0D) {
            throw new IllegalArgumentException("transmissionFactor < 0.0");
        }
        this.transmissionFactor = transmissionFactor;
    }

    /**
     * The base percentage of light transmitted through the surface. 
     * (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The transmissionFactor
     * 
     */
    public Double getTransmissionFactor() {
        return this.transmissionFactor;
    }

    /**
     * Returns the default value of the transmissionFactor<br> 
     * @see #getTransmissionFactor 
     * 
     * @return The default transmissionFactor
     * 
     */
    public Double defaultTransmissionFactor() {
        return  0.0D;
    }

    /**
     * A texture that defines the transmission percentage of the surface, 
     * sampled from the R channel. These values are linear, and will be 
     * multiplied by transmissionFactor. (optional) 
     * 
     * @param transmissionTexture The transmissionTexture to set
     * 
     */
    public void setTransmissionTexture(TextureInfo transmissionTexture) {
        if (transmissionTexture == null) {
            this.transmissionTexture = transmissionTexture;
            return ;
        }
        this.transmissionTexture = transmissionTexture;
    }

    /**
     * A texture that defines the transmission percentage of the surface, 
     * sampled from the R channel. These values are linear, and will be 
     * multiplied by transmissionFactor. (optional) 
     * 
     * @return The transmissionTexture
     * 
     */
    public TextureInfo getTransmissionTexture() {
        return this.transmissionTexture;
    }

}
