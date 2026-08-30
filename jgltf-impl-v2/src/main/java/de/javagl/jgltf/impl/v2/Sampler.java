/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;



/**
 * Texture sampler properties for filtering and wrapping modes. 
 * 
 * Auto-generated for sampler.schema.json 
 * 
 */
public class Sampler
    extends GlTFChildOfRootProperty
{

    /**
     * Magnification filter. (optional)<br> 
     * Valid values: [9728, 9729] 
     * 
     */
    private Integer magFilter;
    /**
     * Minification filter. (optional)<br> 
     * Valid values: [9728, 9729, 9984, 9985, 9986, 9987] 
     * 
     */
    private Integer minFilter;
    /**
     * S (U) wrapping mode. (optional)<br> 
     * Default: 10497<br> 
     * Valid values: [33071, 33648, 10497] 
     * 
     */
    private Integer wrapS;
    /**
     * T (V) wrapping mode. (optional)<br> 
     * Default: 10497<br> 
     * Valid values: [33071, 33648, 10497] 
     * 
     */
    private Integer wrapT;

    /**
     * Magnification filter. (optional)<br> 
     * Valid values: [9728, 9729] 
     * 
     * @param magFilter The magFilter to set
     * 
     */
    public void setMagFilter(Integer magFilter) {
        if (magFilter == null) {
            this.magFilter = magFilter;
            return ;
        }
        this.magFilter = magFilter;
    }

    /**
     * Magnification filter. (optional)<br> 
     * Valid values: [9728, 9729] 
     * 
     * @return The magFilter
     * 
     */
    public Integer getMagFilter() {
        return this.magFilter;
    }

    /**
     * Minification filter. (optional)<br> 
     * Valid values: [9728, 9729, 9984, 9985, 9986, 9987] 
     * 
     * @param minFilter The minFilter to set
     * 
     */
    public void setMinFilter(Integer minFilter) {
        if (minFilter == null) {
            this.minFilter = minFilter;
            return ;
        }
        this.minFilter = minFilter;
    }

    /**
     * Minification filter. (optional)<br> 
     * Valid values: [9728, 9729, 9984, 9985, 9986, 9987] 
     * 
     * @return The minFilter
     * 
     */
    public Integer getMinFilter() {
        return this.minFilter;
    }

    /**
     * S (U) wrapping mode. (optional)<br> 
     * Default: 10497<br> 
     * Valid values: [33071, 33648, 10497] 
     * 
     * @param wrapS The wrapS to set
     * 
     */
    public void setWrapS(Integer wrapS) {
        if (wrapS == null) {
            this.wrapS = wrapS;
            return ;
        }
        this.wrapS = wrapS;
    }

    /**
     * S (U) wrapping mode. (optional)<br> 
     * Default: 10497<br> 
     * Valid values: [33071, 33648, 10497] 
     * 
     * @return The wrapS
     * 
     */
    public Integer getWrapS() {
        return this.wrapS;
    }

    /**
     * Returns the default value of the wrapS<br> 
     * @see #getWrapS 
     * 
     * @return The default wrapS
     * 
     */
    public Integer defaultWrapS() {
        return  10497;
    }

    /**
     * T (V) wrapping mode. (optional)<br> 
     * Default: 10497<br> 
     * Valid values: [33071, 33648, 10497] 
     * 
     * @param wrapT The wrapT to set
     * 
     */
    public void setWrapT(Integer wrapT) {
        if (wrapT == null) {
            this.wrapT = wrapT;
            return ;
        }
        this.wrapT = wrapT;
    }

    /**
     * T (V) wrapping mode. (optional)<br> 
     * Default: 10497<br> 
     * Valid values: [33071, 33648, 10497] 
     * 
     * @return The wrapT
     * 
     */
    public Integer getWrapT() {
        return this.wrapT;
    }

    /**
     * Returns the default value of the wrapT<br> 
     * @see #getWrapT 
     * 
     * @return The default wrapT
     * 
     */
    public Integer defaultWrapT() {
        return  10497;
    }

}
