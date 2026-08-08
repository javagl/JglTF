/*
 * glTF KHR_materials_volume JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_volume;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * glTF extension that defines the parameters for the volume of a 
 * material. 
 * 
 * Auto-generated for material.KHR_materials_volume.schema.json 
 * 
 */
public class MaterialMaterialsVolume
    extends GlTFProperty
{

    /**
     * Thickness of the volume. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     */
    private Double thicknessFactor;
    /**
     * Texture that defines the thickness of the volume, stored in the G 
     * channel. (optional) 
     * 
     */
    private TextureInfo thicknessTexture;
    /**
     * Average distance that light travels in the medium before interacting 
     * with a particle. (optional) 
     * 
     */
    private Double attenuationDistance;
    /**
     * Color that white light turns into due to absorption when reaching the 
     * attenuation distance. (optional)<br> 
     * Default: [1.0,1.0,1.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     */
    private double[] attenuationColor;

    /**
     * Thickness of the volume. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @param thicknessFactor The thicknessFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setThicknessFactor(Double thicknessFactor) {
        if (thicknessFactor == null) {
            this.thicknessFactor = thicknessFactor;
            return ;
        }
        if (thicknessFactor< 0.0D) {
            throw new IllegalArgumentException("thicknessFactor < 0.0");
        }
        this.thicknessFactor = thicknessFactor;
    }

    /**
     * Thickness of the volume. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The thicknessFactor
     * 
     */
    public Double getThicknessFactor() {
        return this.thicknessFactor;
    }

    /**
     * Returns the default value of the thicknessFactor<br> 
     * @see #getThicknessFactor 
     * 
     * @return The default thicknessFactor
     * 
     */
    public Double defaultThicknessFactor() {
        return  0.0D;
    }

    /**
     * Texture that defines the thickness of the volume, stored in the G 
     * channel. (optional) 
     * 
     * @param thicknessTexture The thicknessTexture to set
     * 
     */
    public void setThicknessTexture(TextureInfo thicknessTexture) {
        if (thicknessTexture == null) {
            this.thicknessTexture = thicknessTexture;
            return ;
        }
        this.thicknessTexture = thicknessTexture;
    }

    /**
     * Texture that defines the thickness of the volume, stored in the G 
     * channel. (optional) 
     * 
     * @return The thicknessTexture
     * 
     */
    public TextureInfo getThicknessTexture() {
        return this.thicknessTexture;
    }

    /**
     * Average distance that light travels in the medium before interacting 
     * with a particle. (optional) 
     * 
     * @param attenuationDistance The attenuationDistance to set
     * 
     */
    public void setAttenuationDistance(Double attenuationDistance) {
        if (attenuationDistance == null) {
            this.attenuationDistance = attenuationDistance;
            return ;
        }
        this.attenuationDistance = attenuationDistance;
    }

    /**
     * Average distance that light travels in the medium before interacting 
     * with a particle. (optional) 
     * 
     * @return The attenuationDistance
     * 
     */
    public Double getAttenuationDistance() {
        return this.attenuationDistance;
    }

    /**
     * Color that white light turns into due to absorption when reaching the 
     * attenuation distance. (optional)<br> 
     * Default: [1.0,1.0,1.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @param attenuationColor The attenuationColor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setAttenuationColor(double[] attenuationColor) {
        if (attenuationColor == null) {
            this.attenuationColor = attenuationColor;
            return ;
        }
        if (attenuationColor.length< 3) {
            throw new IllegalArgumentException("Number of attenuationColor elements is < 3");
        }
        if (attenuationColor.length > 3) {
            throw new IllegalArgumentException("Number of attenuationColor elements is > 3");
        }
        for (double attenuationColorElement: attenuationColor) {
            if (attenuationColorElement > 1.0D) {
                throw new IllegalArgumentException("attenuationColorElement > 1.0");
            }
            if (attenuationColorElement< 0.0D) {
                throw new IllegalArgumentException("attenuationColorElement < 0.0");
            }
        }
        this.attenuationColor = attenuationColor;
    }

    /**
     * Color that white light turns into due to absorption when reaching the 
     * attenuation distance. (optional)<br> 
     * Default: [1.0,1.0,1.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @return The attenuationColor
     * 
     */
    public double[] getAttenuationColor() {
        return this.attenuationColor;
    }

    /**
     * Returns the default value of the attenuationColor<br> 
     * @see #getAttenuationColor 
     * 
     * @return The default attenuationColor
     * 
     */
    public double[] defaultAttenuationColor() {
        return new double[] { 1.0D, 1.0D, 1.0D };
    }

}
