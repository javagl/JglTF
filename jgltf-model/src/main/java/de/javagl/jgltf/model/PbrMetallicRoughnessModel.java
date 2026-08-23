/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.model;

/**
 * Interface for the metallic-roughness texture information that is part of a
 * {@link PbrMaterialModel}.
 */
public interface PbrMetallicRoughnessModel extends ModelElement
{
    /**
     * The factors for the base color of the material. (optional)<br> 
     * Default: [1.0,1.0,1.0,1.0]<br> 
     * Number of items: 4<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     *
     * @return The base color factor
     */
    double[] getBaseColorFactor();
    
    /**
     * The factors for the base color of the material. (optional)<br> 
     * Default: [1.0,1.0,1.0,1.0]<br> 
     * Number of items: 4<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @param baseColorFactor The baseColorFactor to set
     */
    void setBaseColorFactor(double[] baseColorFactor);

    /**
     * The base color texture. (optional) 
     *
     * @return The base color texture
     */
    TextureInfoModel getBaseColorTexture();

    /**
     * The base color texture. (optional) 
     * 
     * @param baseColorTexture The baseColorTexture to set
     */
    void setBaseColorTexture(TextureInfoModel baseColorTexture);
    
    /**
     * The factor for the metalness of the material. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     *
     * @return The metallic factor
     */
    Double getMetallicFactor();
    
    /**
     * The factor for the metalness of the material. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param metallicFactor The metallicFactor to set
     */
    void setMetallicFactor(Double metallicFactor);
    
    /**
     * The factor for the roughness of the material. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return roughnessFactor The roughnessFactor
     */
    Double getRoughnessFactor();

    /**
     * The factor for the roughness of the material. (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @param roughnessFactor The roughnessFactor to set
     */
    void setRoughnessFactor(Double roughnessFactor);
    
    /**
     * The metallic-roughness texture. (optional) 
     *
     * @return The metallic-roughness texture info
     */
    TextureInfoModel getMetallicRoughnessTexture();
    
    /**
     * The metallic-roughness texture. (optional) 
     * 
     * @param metallicRoughnessTexture The metallic-roughness texture to set
     */
    void setMetallicRoughnessTexture(TextureInfoModel metallicRoughnessTexture);
    

}
