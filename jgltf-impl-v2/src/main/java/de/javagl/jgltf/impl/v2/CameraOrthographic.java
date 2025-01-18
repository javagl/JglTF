/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;



/**
 * An orthographic camera containing properties to create an orthographic 
 * projection matrix. 
 * 
 * Auto-generated for camera.orthographic.schema.json 
 * 
 */
public class CameraOrthographic
    extends GlTFProperty
{

    /**
     * The floating-point horizontal magnification of the view. This value 
     * **MUST NOT** be equal to zero. This value **SHOULD NOT** be negative. 
     * (required) 
     * 
     */
    private Double xmag;
    /**
     * The floating-point vertical magnification of the view. This value 
     * **MUST NOT** be equal to zero. This value **SHOULD NOT** be negative. 
     * (required) 
     * 
     */
    private Double ymag;
    /**
     * The floating-point distance to the far clipping plane. This value 
     * **MUST NOT** be equal to zero. `zfar` **MUST** be greater than 
     * `znear`. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     */
    private Double zfar;
    /**
     * The floating-point distance to the near clipping plane. (required)<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     */
    private Double znear;

    /**
     * The floating-point horizontal magnification of the view. This value 
     * **MUST NOT** be equal to zero. This value **SHOULD NOT** be negative. 
     * (required) 
     * 
     * @param xmag The xmag to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setXmag(Double xmag) {
        if (xmag == null) {
            throw new NullPointerException((("Invalid value for xmag: "+ xmag)+", may not be null"));
        }
        this.xmag = xmag;
    }

    /**
     * The floating-point horizontal magnification of the view. This value 
     * **MUST NOT** be equal to zero. This value **SHOULD NOT** be negative. 
     * (required) 
     * 
     * @return The xmag
     * 
     */
    public Double getXmag() {
        return this.xmag;
    }

    /**
     * The floating-point vertical magnification of the view. This value 
     * **MUST NOT** be equal to zero. This value **SHOULD NOT** be negative. 
     * (required) 
     * 
     * @param ymag The ymag to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setYmag(Double ymag) {
        if (ymag == null) {
            throw new NullPointerException((("Invalid value for ymag: "+ ymag)+", may not be null"));
        }
        this.ymag = ymag;
    }

    /**
     * The floating-point vertical magnification of the view. This value 
     * **MUST NOT** be equal to zero. This value **SHOULD NOT** be negative. 
     * (required) 
     * 
     * @return The ymag
     * 
     */
    public Double getYmag() {
        return this.ymag;
    }

    /**
     * The floating-point distance to the far clipping plane. This value 
     * **MUST NOT** be equal to zero. `zfar` **MUST** be greater than 
     * `znear`. (required)<br> 
     * Minimum: 0.0 (exclusive) 
     * 
     * @param zfar The zfar to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setZfar(Double zfar) {
        if (zfar == null) {
            throw new NullPointerException((("Invalid value for zfar: "+ zfar)+", may not be null"));
        }
        if (zfar<= 0.0D) {
            throw new IllegalArgumentException("zfar <= 0.0");
        }
        this.zfar = zfar;
    }

    /**
     * The floating-point distance to the far clipping plane. This value 
     * **MUST NOT** be equal to zero. `zfar` **MUST** be greater than 
     * `znear`. (required)<br> 
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
     * Minimum: 0.0 (inclusive) 
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
        if (znear< 0.0D) {
            throw new IllegalArgumentException("znear < 0.0");
        }
        this.znear = znear;
    }

    /**
     * The floating-point distance to the near clipping plane. (required)<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The znear
     * 
     */
    public Double getZnear() {
        return this.znear;
    }

}
