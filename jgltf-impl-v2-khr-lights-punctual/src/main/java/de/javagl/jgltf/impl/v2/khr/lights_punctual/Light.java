/*
 * glTF KHR_lights_punctual JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.khr.lights_punctual;

import de.javagl.jgltf.impl.v2.GlTFChildOfRootProperty;


/**
 * A directional, point, or spot light. 
 * 
 * Auto-generated for light.schema.json 
 * 
 */
public class Light
    extends GlTFChildOfRootProperty
{

    /**
     * Color of the light source. (optional)<br> 
     * Default: [1,1,1]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     */
    private double[] color;
    /**
     * Intensity of the light source. `point` and `spot` lights use luminous 
     * intensity in candela (lm/sr) while `directional` lights use 
     * illuminance in lux (lm/m^2) (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Double intensity;
    /**
     * The spot of this Light (optional) 
     * 
     */
    private LightSpot spot;
    /**
     * Specifies the light type. (required)<br> 
     * Valid values: [directional, point, spot] 
     * 
     */
    private String type;
    /**
     * A distance cutoff at which the light's intensity may be considered to 
     * have reached zero. (optional) 
     * 
     */
    private Double range;

    /**
     * Color of the light source. (optional)<br> 
     * Default: [1,1,1]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @param color The color to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setColor(double[] color) {
        if (color == null) {
            this.color = color;
            return ;
        }
        if (color.length< 3) {
            throw new IllegalArgumentException("Number of color elements is < 3");
        }
        if (color.length > 3) {
            throw new IllegalArgumentException("Number of color elements is > 3");
        }
        for (double colorElement: color) {
            if (colorElement > 1.0D) {
                throw new IllegalArgumentException("colorElement > 1.0");
            }
            if (colorElement< 0.0D) {
                throw new IllegalArgumentException("colorElement < 0.0");
            }
        }
        this.color = color;
    }

    /**
     * Color of the light source. (optional)<br> 
     * Default: [1,1,1]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @return The color
     * 
     */
    public double[] getColor() {
        return this.color;
    }

    /**
     * Returns the default value of the color<br> 
     * @see #getColor 
     * 
     * @return The default color
     * 
     */
    public double[] defaultColor() {
        return new double[] { 1.0D, 1.0D, 1.0D };
    }

    /**
     * Intensity of the light source. `point` and `spot` lights use luminous 
     * intensity in candela (lm/sr) while `directional` lights use 
     * illuminance in lux (lm/m^2) (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param intensity The intensity to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setIntensity(Double intensity) {
        if (intensity == null) {
            this.intensity = intensity;
            return ;
        }
        if (intensity< 0.0D) {
            throw new IllegalArgumentException("intensity < 0.0");
        }
        this.intensity = intensity;
    }

    /**
     * Intensity of the light source. `point` and `spot` lights use luminous 
     * intensity in candela (lm/sr) while `directional` lights use 
     * illuminance in lux (lm/m^2) (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The intensity
     * 
     */
    public Double getIntensity() {
        return this.intensity;
    }

    /**
     * Returns the default value of the intensity<br> 
     * @see #getIntensity 
     * 
     * @return The default intensity
     * 
     */
    public Double defaultIntensity() {
        return  1.0D;
    }

    /**
     * The spot of this Light (optional) 
     * 
     * @param spot The spot to set
     * 
     */
    public void setSpot(LightSpot spot) {
        if (spot == null) {
            this.spot = spot;
            return ;
        }
        this.spot = spot;
    }

    /**
     * The spot of this Light (optional) 
     * 
     * @return The spot
     * 
     */
    public LightSpot getSpot() {
        return this.spot;
    }

    /**
     * Specifies the light type. (required)<br> 
     * Valid values: [directional, point, spot] 
     * 
     * @param type The type to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException((("Invalid value for type: "+ type)+", may not be null"));
        }
        if (((!"directional".equals(type))&&(!"point".equals(type)))&&(!"spot".equals(type))) {
            throw new IllegalArgumentException((("Invalid value for type: "+ type)+", valid: [directional, point, spot]"));
        }
        this.type = type;
    }

    /**
     * Specifies the light type. (required)<br> 
     * Valid values: [directional, point, spot] 
     * 
     * @return The type
     * 
     */
    public String getType() {
        return this.type;
    }

    /**
     * A distance cutoff at which the light's intensity may be considered to 
     * have reached zero. (optional) 
     * 
     * @param range The range to set
     * 
     */
    public void setRange(Double range) {
        if (range == null) {
            this.range = range;
            return ;
        }
        this.range = range;
    }

    /**
     * A distance cutoff at which the light's intensity may be considered to 
     * have reached zero. (optional) 
     * 
     * @return The range
     * 
     */
    public Double getRange() {
        return this.range;
    }

}
