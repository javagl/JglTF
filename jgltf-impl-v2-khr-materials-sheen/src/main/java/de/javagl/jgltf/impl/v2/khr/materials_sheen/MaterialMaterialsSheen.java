/*
 * glTF KHR_materials_sheen JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2025 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_sheen;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * glTF extension that defines the sheen material model. 
 * 
 * Auto-generated for material.KHR_materials_sheen.schema.json 
 * 
 */
public class MaterialMaterialsSheen
    extends GlTFProperty
{

    /**
     * Color of the sheen layer (in linear space). (optional)<br> 
     * Default: [0,0,0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     */
    private double[] sheenColorFactor;
    /**
     * The sheen color (RGB) texture. (optional) 
     * 
     */
    private TextureInfo sheenColorTexture;
    /**
     * The sheen layer roughness. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     */
    private Double sheenRoughnessFactor;
    /**
     * The sheen roughness (Alpha) texture. (optional) 
     * 
     */
    private TextureInfo sheenRoughnessTexture;

    /**
     * Color of the sheen layer (in linear space). (optional)<br> 
     * Default: [0,0,0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @param sheenColorFactor The sheenColorFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setSheenColorFactor(double[] sheenColorFactor) {
        if (sheenColorFactor == null) {
            this.sheenColorFactor = sheenColorFactor;
            return ;
        }
        if (sheenColorFactor.length< 3) {
            throw new IllegalArgumentException("Number of sheenColorFactor elements is < 3");
        }
        if (sheenColorFactor.length > 3) {
            throw new IllegalArgumentException("Number of sheenColorFactor elements is > 3");
        }
        for (double sheenColorFactorElement: sheenColorFactor) {
            if (sheenColorFactorElement > 1.0D) {
                throw new IllegalArgumentException("sheenColorFactorElement > 1.0");
            }
            if (sheenColorFactorElement< 0.0D) {
                throw new IllegalArgumentException("sheenColorFactorElement < 0.0");
            }
        }
        this.sheenColorFactor = sheenColorFactor;
    }

    /**
     * Color of the sheen layer (in linear space). (optional)<br> 
     * Default: [0,0,0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @return The sheenColorFactor
     * 
     */
    public double[] getSheenColorFactor() {
        return this.sheenColorFactor;
    }

    /**
     * Returns the default value of the sheenColorFactor<br> 
     * @see #getSheenColorFactor 
     * 
     * @return The default sheenColorFactor
     * 
     */
    public double[] defaultSheenColorFactor() {
        return new double[] { 0.0D, 0.0D, 0.0D };
    }

    /**
     * The sheen color (RGB) texture. (optional) 
     * 
     * @param sheenColorTexture The sheenColorTexture to set
     * 
     */
    public void setSheenColorTexture(TextureInfo sheenColorTexture) {
        if (sheenColorTexture == null) {
            this.sheenColorTexture = sheenColorTexture;
            return ;
        }
        this.sheenColorTexture = sheenColorTexture;
    }

    /**
     * The sheen color (RGB) texture. (optional) 
     * 
     * @return The sheenColorTexture
     * 
     */
    public TextureInfo getSheenColorTexture() {
        return this.sheenColorTexture;
    }

    /**
     * The sheen layer roughness. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param sheenRoughnessFactor The sheenRoughnessFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setSheenRoughnessFactor(Double sheenRoughnessFactor) {
        if (sheenRoughnessFactor == null) {
            this.sheenRoughnessFactor = sheenRoughnessFactor;
            return ;
        }
        if (sheenRoughnessFactor > 1.0D) {
            throw new IllegalArgumentException("sheenRoughnessFactor > 1.0");
        }
        if (sheenRoughnessFactor< 0.0D) {
            throw new IllegalArgumentException("sheenRoughnessFactor < 0.0");
        }
        this.sheenRoughnessFactor = sheenRoughnessFactor;
    }

    /**
     * The sheen layer roughness. (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The sheenRoughnessFactor
     * 
     */
    public Double getSheenRoughnessFactor() {
        return this.sheenRoughnessFactor;
    }

    /**
     * Returns the default value of the sheenRoughnessFactor<br> 
     * @see #getSheenRoughnessFactor 
     * 
     * @return The default sheenRoughnessFactor
     * 
     */
    public Double defaultSheenRoughnessFactor() {
        return  0.0D;
    }

    /**
     * The sheen roughness (Alpha) texture. (optional) 
     * 
     * @param sheenRoughnessTexture The sheenRoughnessTexture to set
     * 
     */
    public void setSheenRoughnessTexture(TextureInfo sheenRoughnessTexture) {
        if (sheenRoughnessTexture == null) {
            this.sheenRoughnessTexture = sheenRoughnessTexture;
            return ;
        }
        this.sheenRoughnessTexture = sheenRoughnessTexture;
    }

    /**
     * The sheen roughness (Alpha) texture. (optional) 
     * 
     * @return The sheenRoughnessTexture
     * 
     */
    public TextureInfo getSheenRoughnessTexture() {
        return this.sheenRoughnessTexture;
    }

}
