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
 * Interface for a {@link MaterialModel} that is tailored for Physically
 * Based Rendering (PBR), as defined in glTF 2.0.
 */
public interface PbrMaterialModel extends MaterialModel
{
    /**
     * Alpha modes
     */
    public static enum AlphaMode
    {
        /**
         * Opaque mode
         */
        OPAQUE,
        
        /**
         * Masking mode
         */
        MASK,
        
        /**
         * Blend mode
         */
        BLEND        
    }
    
    /**
     * Returns the {@link PbrMetallicRoughnessModel} of this material
     * 
     * @return The {@link PbrMetallicRoughnessModel}
     */
    PbrMetallicRoughnessModel getPbrMetallicRoughnessModel();

    /**
     * Returns the base color {@link TextureModel} (optional)
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getBaseColorTextureModel()
    {
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel == null)
        {
            return null;
        }
        TextureInfoModel textureInfo =
            pbrMetallicRoughnessModel.getBaseColorTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }

    /**
     * Returns the base color texture coordinate index (optional)
     * 
     * @return The index
     */
    default Integer getBaseColorTexcoord()
    {
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel == null)
        {
            return null;
        }
        TextureInfoModel textureInfo =
            pbrMetallicRoughnessModel.getBaseColorTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }

    /**
     * Returns the metallic-roughness {@link TextureModel} (optional)
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getMetallicRoughnessTextureModel()
    {
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel == null)
        {
            return null;
        }
        TextureInfoModel textureInfo =
            pbrMetallicRoughnessModel.getMetallicRoughnessTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the metallic-roughness texture coordinate index (optional)
     * 
     * @return The index
     */
    default Integer getMetallicRoughnessTexcoord()
    {
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel == null)
        {
            return null;
        }
        TextureInfoModel textureInfo =
            pbrMetallicRoughnessModel.getMetallicRoughnessTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * Returns the {@link NormalTextureInfoModel} of this material (optional)
     * 
     * @return The {@link NormalTextureInfoModel}
     */
    NormalTextureInfoModel getNormalTexture();
    
    /**
     * Set the {@link NormalTextureInfoModel} of this material (optional)
     * 
     * @param normalTexture The {@link NormalTextureInfoModel}
     */
    void setNormalTexture(NormalTextureInfoModel normalTexture);

    /**
     * Returns the normal {@link TextureModel} (optional)
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getNormalTextureModel()
    {
        TextureInfoModel textureInfo = getNormalTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the normal texture coordinate index (optional)
     * 
     * @return The index
     */
    default Integer getNormalTexcoord()
    {
        TextureInfoModel textureInfo = getNormalTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * The scalar parameter applied to each normal vector of the normal 
     * texture. (optional)<br> 
     * Default: 1.0 
     * 
     * @return The normal scale
     */
    default Double getNormalScale()
    {
        NormalTextureInfoModel textureInfo = getNormalTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getScale();
    }
    
    /**
     * Returns the {@link OcclusionTextureInfoModel} of this material (optional)
     * 
     * @return The {@link OcclusionTextureInfoModel}
     */
    OcclusionTextureInfoModel getOcclusionTexture();

    /**
     * Set the {@link OcclusionTextureInfoModel} of this material (optional)
     * 
     * @param occlusionTexture The {@link OcclusionTextureInfoModel}
     */
    void setOcclusionTexture(OcclusionTextureInfoModel occlusionTexture);
    
    /**
     * Returns the occlusion {@link TextureModel} (optional)
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getOcclusionTextureModel()
    {
        TextureInfoModel textureInfo = getOcclusionTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the occlusion texture coordinate index (optional)
     * 
     * @return The index
     */
    default Integer getOcclusionTexcoord()
    {
        TextureInfoModel textureInfo = getOcclusionTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * A scalar multiplier controlling the amount of occlusion applied. 
     * (optional)<br> 
     * Default: 1.0<br> 
     * Minimum: 0.0 (inclusive)<br> 
     * Maximum: 1.0 (inclusive) 
     * 
     * @return The occlusion strength
     */
    default Double getOcclusionStrength()
    {
        OcclusionTextureInfoModel textureInfo = getOcclusionTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getStrength();
    }
    
    /**
     * Returns the {@link TextureInfoModel} for the emissive texture (optional)
     * 
     * @return The {@link TextureInfoModel}
     */
    TextureInfoModel getEmissiveTexture();

    /**
     * Set the {@link TextureInfoModel} for the emissive texture (optional)
     * 
     * @param emissiveTexture The {@link TextureInfoModel}
     */
    void setEmissiveTexture(TextureInfoModel emissiveTexture);

    /**
     * Returns the emissive {@link TextureModel} (optional)
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getEmissiveTextureModel()
    {
        TextureInfoModel textureInfo = getEmissiveTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the emissive texture coordinate index (optional)
     * 
     * @return The index
     */
    default Integer getEmissiveTexcoord()
    {
        TextureInfoModel textureInfo = getEmissiveTexture();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * The factors for the emissive color of the material. (optional)<br> 
     * Default: [0.0,0.0,0.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @return emissiveFactor The emissiveFactor
     */
    double[] getEmissiveFactor();

    /**
     * The factors for the emissive color of the material. (optional)<br> 
     * Default: [0.0,0.0,0.0]<br> 
     * Number of items: 3<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Minimum: 0.0 (inclusive)<br> 
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive) 
     * 
     * @param emissiveFactor The emissiveFactor to set
     */
    void setEmissiveFactor(double emissiveFactor[]);

    /**
     * The alpha rendering mode of the material. (optional)<br> 
     * Default: "OPAQUE"<br> 
     * Valid values: [OPAQUE, MASK, BLEND] 
     * 
     * @return The alpha mode
     */
    AlphaMode getAlphaMode();

    /**
     * The alpha rendering mode of the material. (optional)<br> 
     * Default: "OPAQUE"<br> 
     * Valid values: [OPAQUE, MASK, BLEND] 
     * 
     * @param alphaMode The alpha mode
     */
    void setAlphaMode(AlphaMode alphaMode);
    

    /**
     * The alpha cutoff value of the material. (optional)<br> 
     * Default: 0.5<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @return The alpha cutoff
     */
    Double getAlphaCutoff();
    
    /**
     * The alpha cutoff value of the material. (optional)<br> 
     * Default: 0.5<br> 
     * Minimum: 0.0 (inclusive) 
     * 
     * @param alphaCutoff The alpha cutoff
     */
    void setAlphaCutoff(Double alphaCutoff);

    /**
     * Specifies whether the material is double sided. (optional)<br> 
     * Default: false 
     *
     * @return Whether the material is double sided
     */
    Boolean isDoubleSided();
    
    /**
     * Specifies whether the material is double sided. (optional)<br> 
     * Default: false 
     * 
     * @param doubleSided Whether the material is double sided
     */
    void setDoubleSided(Boolean doubleSided);
    
}