/*
 * glTF KHR_materials_specular JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.materials_specular;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.impl.v2.TextureInfo;


/**
 * glTF extension that defines the strength of the specular reflection. 
 * 
 * Auto-generated for material.KHR_materials_specular.schema.json 
 * 
 */
public class MaterialMaterialsSpecular
    extends GlTFProperty
{

    /**
     * The strength of the specular reflection. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     */
    private Double specularFactor;
    /**
     * A texture that defines the specular factor in the alpha channel. 
     * (optional) 
     * 
     */
    private TextureInfo specularTexture;
    /**
     * The F0 RGB color of the specular reflection. (optional)<br> 
     * Default: [1.0,1.0,1.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive) 
     * 
     */
    private double[] specularColorFactor;
    /**
     * A texture that defines the F0 color of the specular reflection. 
     * (optional) 
     * 
     */
    private TextureInfo specularColorTexture;

    /**
     * The strength of the specular reflection. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param specularFactor The specularFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setSpecularFactor(Double specularFactor) {
        if (specularFactor == null) {
            this.specularFactor = specularFactor;
            return ;
        }
        if (specularFactor > 1.0D) {
            throw new IllegalArgumentException("specularFactor > 1.0");
        }
        if (specularFactor< 0.0D) {
            throw new IllegalArgumentException("specularFactor < 0.0");
        }
        this.specularFactor = specularFactor;
    }

    /**
     * The strength of the specular reflection. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The specularFactor
     * 
     */
    public Double getSpecularFactor() {
        return this.specularFactor;
    }

    /**
     * Returns the default value of the specularFactor<br> 
     * @see #getSpecularFactor 
     * 
     * @return The default specularFactor
     * 
     */
    public Double defaultSpecularFactor() {
        return  1.0D;
    }

    /**
     * A texture that defines the specular factor in the alpha channel. 
     * (optional) 
     * 
     * @param specularTexture The specularTexture to set
     * 
     */
    public void setSpecularTexture(TextureInfo specularTexture) {
        if (specularTexture == null) {
            this.specularTexture = specularTexture;
            return ;
        }
        this.specularTexture = specularTexture;
    }

    /**
     * A texture that defines the specular factor in the alpha channel. 
     * (optional) 
     * 
     * @return The specularTexture
     * 
     */
    public TextureInfo getSpecularTexture() {
        return this.specularTexture;
    }

    /**
     * The F0 RGB color of the specular reflection. (optional)<br> 
     * Default: [1.0,1.0,1.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive) 
     * 
     * @param specularColorFactor The specularColorFactor to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setSpecularColorFactor(double[] specularColorFactor) {
        if (specularColorFactor == null) {
            this.specularColorFactor = specularColorFactor;
            return ;
        }
        if (specularColorFactor.length< 3) {
            throw new IllegalArgumentException("Number of specularColorFactor elements is < 3");
        }
        if (specularColorFactor.length > 3) {
            throw new IllegalArgumentException("Number of specularColorFactor elements is > 3");
        }
        for (double specularColorFactorElement: specularColorFactor) {
            if (specularColorFactorElement< 0.0D) {
                throw new IllegalArgumentException("specularColorFactorElement < 0.0");
            }
        }
        this.specularColorFactor = specularColorFactor;
    }

    /**
     * The F0 RGB color of the specular reflection. (optional)<br> 
     * Default: [1.0,1.0,1.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive) 
     * 
     * @return The specularColorFactor
     * 
     */
    public double[] getSpecularColorFactor() {
        return this.specularColorFactor;
    }

    /**
     * Returns the default value of the specularColorFactor<br> 
     * @see #getSpecularColorFactor 
     * 
     * @return The default specularColorFactor
     * 
     */
    public double[] defaultSpecularColorFactor() {
        return new double[] { 1.0D, 1.0D, 1.0D };
    }

    /**
     * A texture that defines the F0 color of the specular reflection. 
     * (optional) 
     * 
     * @param specularColorTexture The specularColorTexture to set
     * 
     */
    public void setSpecularColorTexture(TextureInfo specularColorTexture) {
        if (specularColorTexture == null) {
            this.specularColorTexture = specularColorTexture;
            return ;
        }
        this.specularColorTexture = specularColorTexture;
    }

    /**
     * A texture that defines the F0 color of the specular reflection. 
     * (optional) 
     * 
     * @return The specularColorTexture
     * 
     */
    public TextureInfo getSpecularColorTexture() {
        return this.specularColorTexture;
    }

}
