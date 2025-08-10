/*
 * glTF KHR_materials_clearcoat JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_clearcoat;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.impl.v2.MaterialNormalTextureInfo;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * glTF extension that defines the clearcoat material layer. 
 * 
 * Auto-generated for material.KHR_materials_clearcoat.schema.json 
 * 
 */
public class MaterialMaterialsClearcoat
    extends GlTFProperty
{

    /**
     * The clearcoat layer intensity. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     */
    private Double clearcoatFactor;
    /**
     * The clearcoat layer intensity texture. (optional) 
     * 
     */
    private TextureInfo clearcoatTexture;
    /**
     * The clearcoat layer roughness. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     */
    private Double clearcoatRoughnessFactor;
    /**
     * The clearcoat layer roughness texture. (optional) 
     * 
     */
    private TextureInfo clearcoatRoughnessTexture;
    /**
     * The clearcoat normal map texture. (optional) 
     * 
     */
    private MaterialNormalTextureInfo clearcoatNormalTexture;

    /**
     * The clearcoat layer intensity. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param clearcoatFactor The clearcoatFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setClearcoatFactor(Double clearcoatFactor) {
        if (clearcoatFactor == null) {
            this.clearcoatFactor = clearcoatFactor;
            return ;
        }
        if (clearcoatFactor > 1.0D) {
            throw new IllegalArgumentException("clearcoatFactor > 1.0");
        }
        if (clearcoatFactor< 0.0D) {
            throw new IllegalArgumentException("clearcoatFactor < 0.0");
        }
        this.clearcoatFactor = clearcoatFactor;
    }

    /**
     * The clearcoat layer intensity. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The clearcoatFactor
     * 
     */
    public Double getClearcoatFactor() {
        return this.clearcoatFactor;
    }

    /**
     * Returns the default value of the clearcoatFactor<br> 
     * @see #getClearcoatFactor 
     * 
     * @return The default clearcoatFactor
     * 
     */
    public Double defaultClearcoatFactor() {
        return  0.0D;
    }

    /**
     * The clearcoat layer intensity texture. (optional) 
     * 
     * @param clearcoatTexture The clearcoatTexture to set
     * 
     */
    public void setClearcoatTexture(TextureInfo clearcoatTexture) {
        if (clearcoatTexture == null) {
            this.clearcoatTexture = clearcoatTexture;
            return ;
        }
        this.clearcoatTexture = clearcoatTexture;
    }

    /**
     * The clearcoat layer intensity texture. (optional) 
     * 
     * @return The clearcoatTexture
     * 
     */
    public TextureInfo getClearcoatTexture() {
        return this.clearcoatTexture;
    }

    /**
     * The clearcoat layer roughness. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param clearcoatRoughnessFactor The clearcoatRoughnessFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setClearcoatRoughnessFactor(Double clearcoatRoughnessFactor) {
        if (clearcoatRoughnessFactor == null) {
            this.clearcoatRoughnessFactor = clearcoatRoughnessFactor;
            return ;
        }
        if (clearcoatRoughnessFactor > 1.0D) {
            throw new IllegalArgumentException("clearcoatRoughnessFactor > 1.0");
        }
        if (clearcoatRoughnessFactor< 0.0D) {
            throw new IllegalArgumentException("clearcoatRoughnessFactor < 0.0");
        }
        this.clearcoatRoughnessFactor = clearcoatRoughnessFactor;
    }

    /**
     * The clearcoat layer roughness. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The clearcoatRoughnessFactor
     * 
     */
    public Double getClearcoatRoughnessFactor() {
        return this.clearcoatRoughnessFactor;
    }

    /**
     * Returns the default value of the clearcoatRoughnessFactor<br> 
     * @see #getClearcoatRoughnessFactor 
     * 
     * @return The default clearcoatRoughnessFactor
     * 
     */
    public Double defaultClearcoatRoughnessFactor() {
        return  0.0D;
    }

    /**
     * The clearcoat layer roughness texture. (optional) 
     * 
     * @param clearcoatRoughnessTexture The clearcoatRoughnessTexture to set
     * 
     */
    public void setClearcoatRoughnessTexture(TextureInfo clearcoatRoughnessTexture) {
        if (clearcoatRoughnessTexture == null) {
            this.clearcoatRoughnessTexture = clearcoatRoughnessTexture;
            return ;
        }
        this.clearcoatRoughnessTexture = clearcoatRoughnessTexture;
    }

    /**
     * The clearcoat layer roughness texture. (optional) 
     * 
     * @return The clearcoatRoughnessTexture
     * 
     */
    public TextureInfo getClearcoatRoughnessTexture() {
        return this.clearcoatRoughnessTexture;
    }

    /**
     * The clearcoat normal map texture. (optional) 
     * 
     * @param clearcoatNormalTexture The clearcoatNormalTexture to set
     * 
     */
    public void setClearcoatNormalTexture(MaterialNormalTextureInfo clearcoatNormalTexture) {
        if (clearcoatNormalTexture == null) {
            this.clearcoatNormalTexture = clearcoatNormalTexture;
            return ;
        }
        this.clearcoatNormalTexture = clearcoatNormalTexture;
    }

    /**
     * The clearcoat normal map texture. (optional) 
     * 
     * @return The clearcoatNormalTexture
     * 
     */
    public MaterialNormalTextureInfo getClearcoatNormalTexture() {
        return this.clearcoatNormalTexture;
    }

}
