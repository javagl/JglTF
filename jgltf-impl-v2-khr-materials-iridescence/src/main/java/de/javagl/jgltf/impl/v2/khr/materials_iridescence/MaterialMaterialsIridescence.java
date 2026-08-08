/*
 * glTF KHR_materials_iridescence JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_iridescence;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * glTF extension that defines an iridescence effect. 
 * 
 * Auto-generated for material.KHR_materials_iridescence.schema.json 
 * 
 */
public class MaterialMaterialsIridescence
    extends GlTFProperty
{

    /**
     * The iridescence intensity factor. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     */
    private Double iridescenceFactor;
    /**
     * The iridescence intensity texture. (optional) 
     * 
     */
    private TextureInfo iridescenceTexture;
    /**
     * The index of refraction of the dielectric thin-film layer. 
     * (optional)<br> 
     * Default: 1.3<br> 
     * Minimum: 1.0 (inclusive) 
     * 
     */
    private Double iridescenceIor;
    /**
     * The minimum thickness of the thin-film layer given in nanometers. 
     * (optional)<br> 
     * Default: 100.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     */
    private Double iridescenceThicknessMinimum;
    /**
     * The maximum thickness of the thin-film layer given in nanometers. 
     * (optional)<br> 
     * Default: 400.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     */
    private Double iridescenceThicknessMaximum;
    /**
     * The thickness texture of the thin-film layer. (optional) 
     * 
     */
    private TextureInfo iridescenceThicknessTexture;

    /**
     * The iridescence intensity factor. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param iridescenceFactor The iridescenceFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setIridescenceFactor(Double iridescenceFactor) {
        if (iridescenceFactor == null) {
            this.iridescenceFactor = iridescenceFactor;
            return ;
        }
        if (iridescenceFactor > 1.0D) {
            throw new IllegalArgumentException("iridescenceFactor > 1.0");
        }
        if (iridescenceFactor< 0.0D) {
            throw new IllegalArgumentException("iridescenceFactor < 0.0");
        }
        this.iridescenceFactor = iridescenceFactor;
    }

    /**
     * The iridescence intensity factor. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The iridescenceFactor
     * 
     */
    public Double getIridescenceFactor() {
        return this.iridescenceFactor;
    }

    /**
     * Returns the default value of the iridescenceFactor<br> 
     * @see #getIridescenceFactor 
     * 
     * @return The default iridescenceFactor
     * 
     */
    public Double defaultIridescenceFactor() {
        return  0.0D;
    }

    /**
     * The iridescence intensity texture. (optional) 
     * 
     * @param iridescenceTexture The iridescenceTexture to set
     * 
     */
    public void setIridescenceTexture(TextureInfo iridescenceTexture) {
        if (iridescenceTexture == null) {
            this.iridescenceTexture = iridescenceTexture;
            return ;
        }
        this.iridescenceTexture = iridescenceTexture;
    }

    /**
     * The iridescence intensity texture. (optional) 
     * 
     * @return The iridescenceTexture
     * 
     */
    public TextureInfo getIridescenceTexture() {
        return this.iridescenceTexture;
    }

    /**
     * The index of refraction of the dielectric thin-film layer. 
     * (optional)<br> 
     * Default: 1.3<br> 
     * Minimum: 1.0 (inclusive) 
     * 
     * @param iridescenceIor The iridescenceIor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setIridescenceIor(Double iridescenceIor) {
        if (iridescenceIor == null) {
            this.iridescenceIor = iridescenceIor;
            return ;
        }
        if (iridescenceIor< 1.0D) {
            throw new IllegalArgumentException("iridescenceIor < 1.0");
        }
        this.iridescenceIor = iridescenceIor;
    }

    /**
     * The index of refraction of the dielectric thin-film layer. 
     * (optional)<br> 
     * Default: 1.3<br> 
     * Minimum: 1.0 (inclusive) 
     * 
     * @return The iridescenceIor
     * 
     */
    public Double getIridescenceIor() {
        return this.iridescenceIor;
    }

    /**
     * Returns the default value of the iridescenceIor<br> 
     * @see #getIridescenceIor 
     * 
     * @return The default iridescenceIor
     * 
     */
    public Double defaultIridescenceIor() {
        return  1.3D;
    }

    /**
     * The minimum thickness of the thin-film layer given in nanometers. 
     * (optional)<br> 
     * Default: 100.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @param iridescenceThicknessMinimum The iridescenceThicknessMinimum to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setIridescenceThicknessMinimum(Double iridescenceThicknessMinimum) {
        if (iridescenceThicknessMinimum == null) {
            this.iridescenceThicknessMinimum = iridescenceThicknessMinimum;
            return ;
        }
        if (iridescenceThicknessMinimum< 0.0D) {
            throw new IllegalArgumentException("iridescenceThicknessMinimum < 0.0");
        }
        this.iridescenceThicknessMinimum = iridescenceThicknessMinimum;
    }

    /**
     * The minimum thickness of the thin-film layer given in nanometers. 
     * (optional)<br> 
     * Default: 100.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The iridescenceThicknessMinimum
     * 
     */
    public Double getIridescenceThicknessMinimum() {
        return this.iridescenceThicknessMinimum;
    }

    /**
     * Returns the default value of the iridescenceThicknessMinimum<br> 
     * @see #getIridescenceThicknessMinimum 
     * 
     * @return The default iridescenceThicknessMinimum
     * 
     */
    public Double defaultIridescenceThicknessMinimum() {
        return  100.0D;
    }

    /**
     * The maximum thickness of the thin-film layer given in nanometers. 
     * (optional)<br> 
     * Default: 400.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @param iridescenceThicknessMaximum The iridescenceThicknessMaximum to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setIridescenceThicknessMaximum(Double iridescenceThicknessMaximum) {
        if (iridescenceThicknessMaximum == null) {
            this.iridescenceThicknessMaximum = iridescenceThicknessMaximum;
            return ;
        }
        if (iridescenceThicknessMaximum< 0.0D) {
            throw new IllegalArgumentException("iridescenceThicknessMaximum < 0.0");
        }
        this.iridescenceThicknessMaximum = iridescenceThicknessMaximum;
    }

    /**
     * The maximum thickness of the thin-film layer given in nanometers. 
     * (optional)<br> 
     * Default: 400.0<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The iridescenceThicknessMaximum
     * 
     */
    public Double getIridescenceThicknessMaximum() {
        return this.iridescenceThicknessMaximum;
    }

    /**
     * Returns the default value of the iridescenceThicknessMaximum<br> 
     * @see #getIridescenceThicknessMaximum 
     * 
     * @return The default iridescenceThicknessMaximum
     * 
     */
    public Double defaultIridescenceThicknessMaximum() {
        return  400.0D;
    }

    /**
     * The thickness texture of the thin-film layer. (optional) 
     * 
     * @param iridescenceThicknessTexture The iridescenceThicknessTexture to set
     * 
     */
    public void setIridescenceThicknessTexture(TextureInfo iridescenceThicknessTexture) {
        if (iridescenceThicknessTexture == null) {
            this.iridescenceThicknessTexture = iridescenceThicknessTexture;
            return ;
        }
        this.iridescenceThicknessTexture = iridescenceThicknessTexture;
    }

    /**
     * The thickness texture of the thin-film layer. (optional) 
     * 
     * @return The iridescenceThicknessTexture
     * 
     */
    public TextureInfo getIridescenceThicknessTexture() {
        return this.iridescenceThicknessTexture;
    }

}
