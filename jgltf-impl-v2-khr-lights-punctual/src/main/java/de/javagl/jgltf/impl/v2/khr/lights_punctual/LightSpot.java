/*
 * glTF KHR_lights_punctual JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.lights_punctual;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * Auto-generated for light.spot.schema.json 
 * 
 */
public class LightSpot
    extends GlTFProperty
{

    /**
     * Angle in radians from centre of spotlight where falloff begins. 
     * (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0 (inclusive)<br> 
     * Maximum: 1.5707963267948966 (exclusive) 
     * 
     */
    private Double innerConeAngle;
    /**
     * Angle in radians from centre of spotlight where falloff ends. 
     * (optional)<br> 
     * Default: 0.7853981633974483<br> 
     * Minimum: 1.5707963267948966 (exclusive)<br> 
     * Maximum: 1.5707963267948966 (inclusive) 
     * 
     */
    private Double outerConeAngle;

    /**
     * Angle in radians from centre of spotlight where falloff begins. 
     * (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0 (inclusive)<br> 
     * Maximum: 1.5707963267948966 (exclusive) 
     * 
     * @param innerConeAngle The innerConeAngle to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setInnerConeAngle(Double innerConeAngle) {
        if (innerConeAngle == null) {
            this.innerConeAngle = innerConeAngle;
            return ;
        }
        if (innerConeAngle >= 1.5707963267948966D) {
            throw new IllegalArgumentException("innerConeAngle >= 1.5707963267948966");
        }
        if (innerConeAngle< 0.0D) {
            throw new IllegalArgumentException("innerConeAngle < 0.0");
        }
        this.innerConeAngle = innerConeAngle;
    }

    /**
     * Angle in radians from centre of spotlight where falloff begins. 
     * (optional)<br> 
     * Default: 0.0<br> 
     * Minimum: 0 (inclusive)<br> 
     * Maximum: 1.5707963267948966 (exclusive) 
     * 
     * @return The innerConeAngle
     * 
     */
    public Double getInnerConeAngle() {
        return this.innerConeAngle;
    }

    /**
     * Returns the default value of the innerConeAngle<br> 
     * @see #getInnerConeAngle 
     * 
     * @return The default innerConeAngle
     * 
     */
    public Double defaultInnerConeAngle() {
        return  0.0D;
    }

    /**
     * Angle in radians from centre of spotlight where falloff ends. 
     * (optional)<br> 
     * Default: 0.7853981633974483<br> 
     * Minimum: 1.5707963267948966 (exclusive)<br> 
     * Maximum: 1.5707963267948966 (inclusive) 
     * 
     * @param outerConeAngle The outerConeAngle to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setOuterConeAngle(Double outerConeAngle) {
        if (outerConeAngle == null) {
            this.outerConeAngle = outerConeAngle;
            return ;
        }
        if (outerConeAngle > 1.5707963267948966D) {
            throw new IllegalArgumentException("outerConeAngle > 1.5707963267948966");
        }
        if (outerConeAngle<= 1.5707963267948966D) {
            throw new IllegalArgumentException("outerConeAngle <= 1.5707963267948966");
        }
        this.outerConeAngle = outerConeAngle;
    }

    /**
     * Angle in radians from centre of spotlight where falloff ends. 
     * (optional)<br> 
     * Default: 0.7853981633974483<br> 
     * Minimum: 1.5707963267948966 (exclusive)<br> 
     * Maximum: 1.5707963267948966 (inclusive) 
     * 
     * @return The outerConeAngle
     * 
     */
    public Double getOuterConeAngle() {
        return this.outerConeAngle;
    }

    /**
     * Returns the default value of the outerConeAngle<br> 
     * @see #getOuterConeAngle 
     * 
     * @return The default outerConeAngle
     * 
     */
    public Double defaultOuterConeAngle() {
        return  0.7853981633974483D;
    }

}
