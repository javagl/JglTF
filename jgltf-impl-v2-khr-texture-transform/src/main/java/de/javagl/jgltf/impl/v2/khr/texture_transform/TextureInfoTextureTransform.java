/*
 * glTF KHR_texture_transform JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.texture_transform;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * glTF extension that enables shifting and scaling UV coordinates on a 
 * per-texture basis 
 * 
 * Auto-generated for textureInfo.KHR_texture_transform.schema.json 
 * 
 */
public class TextureInfoTextureTransform
    extends GlTFProperty
{

    /**
     * The offset of the UV coordinate origin as a factor of the texture 
     * dimensions. (optional)<br> 
     * Default: [0.0,0.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private double[] offset;
    /**
     * Rotate the UVs by this many radians counter-clockwise around the 
     * origin. (optional)<br> 
     * Default: 0.0 
     * 
     */
    private Double rotation;
    /**
     * The scale factor applied to the components of the UV coordinates. 
     * (optional)<br> 
     * Default: [1.0,1.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private double[] scale;
    /**
     * Overrides the textureInfo texCoord value if supplied, and if this 
     * extension is supported. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer texCoord;

    /**
     * The offset of the UV coordinate origin as a factor of the texture 
     * dimensions. (optional)<br> 
     * Default: [0.0,0.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param offset The offset to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setOffset(double[] offset) {
        if (offset == null) {
            this.offset = offset;
            return ;
        }
        if (offset.length< 2) {
            throw new IllegalArgumentException("Number of offset elements is < 2");
        }
        if (offset.length > 2) {
            throw new IllegalArgumentException("Number of offset elements is > 2");
        }
        this.offset = offset;
    }

    /**
     * The offset of the UV coordinate origin as a factor of the texture 
     * dimensions. (optional)<br> 
     * Default: [0.0,0.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The offset
     * 
     */
    public double[] getOffset() {
        return this.offset;
    }

    /**
     * Returns the default value of the offset<br> 
     * @see #getOffset 
     * 
     * @return The default offset
     * 
     */
    public double[] defaultOffset() {
        return new double[] { 0.0D, 0.0D };
    }

    /**
     * Rotate the UVs by this many radians counter-clockwise around the 
     * origin. (optional)<br> 
     * Default: 0.0 
     * 
     * @param rotation The rotation to set
     * 
     */
    public void setRotation(Double rotation) {
        if (rotation == null) {
            this.rotation = rotation;
            return ;
        }
        this.rotation = rotation;
    }

    /**
     * Rotate the UVs by this many radians counter-clockwise around the 
     * origin. (optional)<br> 
     * Default: 0.0 
     * 
     * @return The rotation
     * 
     */
    public Double getRotation() {
        return this.rotation;
    }

    /**
     * Returns the default value of the rotation<br> 
     * @see #getRotation 
     * 
     * @return The default rotation
     * 
     */
    public Double defaultRotation() {
        return  0.0D;
    }

    /**
     * The scale factor applied to the components of the UV coordinates. 
     * (optional)<br> 
     * Default: [1.0,1.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param scale The scale to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setScale(double[] scale) {
        if (scale == null) {
            this.scale = scale;
            return ;
        }
        if (scale.length< 2) {
            throw new IllegalArgumentException("Number of scale elements is < 2");
        }
        if (scale.length > 2) {
            throw new IllegalArgumentException("Number of scale elements is > 2");
        }
        this.scale = scale;
    }

    /**
     * The scale factor applied to the components of the UV coordinates. 
     * (optional)<br> 
     * Default: [1.0,1.0]<br> 
     * Number of items: 2<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The scale
     * 
     */
    public double[] getScale() {
        return this.scale;
    }

    /**
     * Returns the default value of the scale<br> 
     * @see #getScale 
     * 
     * @return The default scale
     * 
     */
    public double[] defaultScale() {
        return new double[] { 1.0D, 1.0D };
    }

    /**
     * Overrides the textureInfo texCoord value if supplied, and if this 
     * extension is supported. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param texCoord The texCoord to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setTexCoord(Integer texCoord) {
        if (texCoord == null) {
            this.texCoord = texCoord;
            return ;
        }
        if (texCoord< 0) {
            throw new IllegalArgumentException("texCoord < 0");
        }
        this.texCoord = texCoord;
    }

    /**
     * Overrides the textureInfo texCoord value if supplied, and if this 
     * extension is supported. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The texCoord
     * 
     */
    public Integer getTexCoord() {
        return this.texCoord;
    }

}
