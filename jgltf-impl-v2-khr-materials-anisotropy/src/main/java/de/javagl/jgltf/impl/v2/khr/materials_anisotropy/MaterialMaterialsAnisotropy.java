/*
 * glTF KHR_materials_anisotropy JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_anisotropy;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * glTF extension that defines anisotropy. 
 * 
 * Auto-generated for material.KHR_materials_anisotropy.schema.json 
 * 
 */
public class MaterialMaterialsAnisotropy
    extends GlTFProperty
{

    /**
     * The anisotropy strength. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     */
    private Double anisotropyStrength;
    /**
     * The rotation of the anisotropy. (optional)<br> 
     * Default: 0.0 
     * 
     */
    private Double anisotropyRotation;
    /**
     * The anisotropy texture. (optional) 
     * 
     */
    private TextureInfo anisotropyTexture;

    /**
     * The anisotropy strength. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param anisotropyStrength The anisotropyStrength to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setAnisotropyStrength(Double anisotropyStrength) {
        if (anisotropyStrength == null) {
            this.anisotropyStrength = anisotropyStrength;
            return ;
        }
        if (anisotropyStrength > 1.0D) {
            throw new IllegalArgumentException("anisotropyStrength > 1.0");
        }
        if (anisotropyStrength< 0.0D) {
            throw new IllegalArgumentException("anisotropyStrength < 0.0");
        }
        this.anisotropyStrength = anisotropyStrength;
    }

    /**
     * The anisotropy strength. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The anisotropyStrength
     * 
     */
    public Double getAnisotropyStrength() {
        return this.anisotropyStrength;
    }

    /**
     * Returns the default value of the anisotropyStrength<br> 
     * @see #getAnisotropyStrength 
     * 
     * @return The default anisotropyStrength
     * 
     */
    public Double defaultAnisotropyStrength() {
        return  0.0D;
    }

    /**
     * The rotation of the anisotropy. (optional)<br> 
     * Default: 0.0 
     * 
     * @param anisotropyRotation The anisotropyRotation to set
     * 
     */
    public void setAnisotropyRotation(Double anisotropyRotation) {
        if (anisotropyRotation == null) {
            this.anisotropyRotation = anisotropyRotation;
            return ;
        }
        this.anisotropyRotation = anisotropyRotation;
    }

    /**
     * The rotation of the anisotropy. (optional)<br> 
     * Default: 0.0 
     * 
     * @return The anisotropyRotation
     * 
     */
    public Double getAnisotropyRotation() {
        return this.anisotropyRotation;
    }

    /**
     * Returns the default value of the anisotropyRotation<br> 
     * @see #getAnisotropyRotation 
     * 
     * @return The default anisotropyRotation
     * 
     */
    public Double defaultAnisotropyRotation() {
        return  0.0D;
    }

    /**
     * The anisotropy texture. (optional) 
     * 
     * @param anisotropyTexture The anisotropyTexture to set
     * 
     */
    public void setAnisotropyTexture(TextureInfo anisotropyTexture) {
        if (anisotropyTexture == null) {
            this.anisotropyTexture = anisotropyTexture;
            return ;
        }
        this.anisotropyTexture = anisotropyTexture;
    }

    /**
     * The anisotropy texture. (optional) 
     * 
     * @return The anisotropyTexture
     * 
     */
    public TextureInfo getAnisotropyTexture() {
        return this.anisotropyTexture;
    }

}
