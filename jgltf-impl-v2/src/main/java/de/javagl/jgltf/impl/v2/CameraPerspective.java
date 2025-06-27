/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;



/**
 * A perspective camera containing properties to create a perspective 
 * projection matrix. 
 * 
 * Auto-generated for camera.perspective.schema.json 
 * 
 */
public class CameraPerspective
    extends GlTFProperty
{

    /**
     * The floating-point aspect ratio of the field of view. (optional)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     */
    private Double aspectRatio;
    /**
     * The floating-point vertical field of view in radians. This value 
     * **SHOULD** be less than π. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     */
    private Double yfov;
    /**
     * The floating-point distance to the far clipping plane. (optional)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     */
    private Double zfar;
    /**
     * The floating-point distance to the near clipping plane. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     */
    private Double znear;

    /**
     * The floating-point aspect ratio of the field of view. (optional)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @param aspectRatio The aspectRatio to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setAspectRatio(Double aspectRatio) {
        if (aspectRatio == null) {
            this.aspectRatio = aspectRatio;
            return ;
        }
        if (aspectRatio<= 0.0D) {
            throw new IllegalArgumentException("aspectRatio <= 0.0");
        }
        this.aspectRatio = aspectRatio;
    }

    /**
     * The floating-point aspect ratio of the field of view. (optional)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @return The aspectRatio
     * 
     */
    public Double getAspectRatio() {
        return this.aspectRatio;
    }

    /**
     * The floating-point vertical field of view in radians. This value 
     * **SHOULD** be less than π. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @param yfov The yfov to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setYfov(Double yfov) {
        if (yfov == null) {
            throw new NullPointerException((("Invalid value for yfov: "+ yfov)+", may not be null"));
        }
        if (yfov<= 0.0D) {
            throw new IllegalArgumentException("yfov <= 0.0");
        }
        this.yfov = yfov;
    }

    /**
     * The floating-point vertical field of view in radians. This value 
     * **SHOULD** be less than π. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @return The yfov
     * 
     */
    public Double getYfov() {
        return this.yfov;
    }

    /**
     * The floating-point distance to the far clipping plane. (optional)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @param zfar The zfar to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setZfar(Double zfar) {
        if (zfar == null) {
            this.zfar = zfar;
            return ;
        }
        if (zfar<= 0.0D) {
            throw new IllegalArgumentException("zfar <= 0.0");
        }
        this.zfar = zfar;
    }

    /**
     * The floating-point distance to the far clipping plane. (optional)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @return The zfar
     * 
     */
    public Double getZfar() {
        return this.zfar;
    }

    /**
     * The floating-point distance to the near clipping plane. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @param znear The znear to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setZnear(Double znear) {
        if (znear == null) {
            throw new NullPointerException((("Invalid value for znear: "+ znear)+", may not be null"));
        }
        if (znear<= 0.0D) {
            throw new IllegalArgumentException("znear <= 0.0");
        }
        this.znear = znear;
    }

    /**
     * The floating-point distance to the near clipping plane. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @return The znear
     * 
     */
    public Double getZnear() {
        return this.znear;
    }

}
