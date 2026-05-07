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
     * Returns the base color {@link TextureModel}
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getBaseColorTexture()
    {
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel == null)
        {
            return null;
        }
        TextureInfoModel textureInfo =
            pbrMetallicRoughnessModel.getBaseColorTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }

    /**
     * Returns the base color texture coordinate index
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
            pbrMetallicRoughnessModel.getBaseColorTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }

    /**
     * Returns the metallic-roughness {@link TextureModel}
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getMetallicRoughnessTexture()
    {
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel == null)
        {
            return null;
        }
        TextureInfoModel textureInfo =
            pbrMetallicRoughnessModel.getMetallicRoughnessTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the metallic-roughness texture coordinate index
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
            pbrMetallicRoughnessModel.getMetallicRoughnessTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * Returns the {@link NormalTextureInfoModel} of this material
     * 
     * @return The {@link NormalTextureInfoModel}
     */
    NormalTextureInfoModel getNormalTextureInfoModel();

    /**
     * Returns the normal {@link TextureModel}
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getNormalTexture()
    {
        TextureInfoModel textureInfo = getNormalTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the normal texture coordinate index
     * 
     * @return The index
     */
    default Integer getNormalTexcoord()
    {
        TextureInfoModel textureInfo = getNormalTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * Returns the normal scale
     * 
     * @return The normal scale
     */
    default Double getNormalScale()
    {
        NormalTextureInfoModel textureInfo = getNormalTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getScale();
    }
    
    /**
     * Returns the {@link OcclusionTextureInfoModel} of this material
     * 
     * @return The {@link OcclusionTextureInfoModel}
     */
    OcclusionTextureInfoModel getOcclusionTextureInfoModel();

    /**
     * Returns the occlusion {@link TextureModel}
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getOcclusionTexture()
    {
        TextureInfoModel textureInfo = getOcclusionTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the occlusion texture coordinate index
     * 
     * @return The index
     */
    default Integer getOcclusionTexcoord()
    {
        TextureInfoModel textureInfo = getOcclusionTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * Returns the occlusion strength
     * 
     * @return The occlusion strength
     */
    default Double getOcclusionStrength()
    {
        OcclusionTextureInfoModel textureInfo = getOcclusionTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getStrength();
    }
    
    /**
     * Returns the {@link TextureInfoModel} for the emissive texture
     * 
     * @return The {@link TextureInfoModel}
     */
    TextureInfoModel getEmissiveTextureInfoModel();

    /**
     * Returns the emissive {@link TextureModel}
     * 
     * @return The {@link TextureModel}
     */
    default TextureModel getEmissiveTexture()
    {
        TextureInfoModel textureInfo = getEmissiveTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTextureModel();
    }
    
    /**
     * Returns the emissive texture coordinate index
     * 
     * @return The index
     */
    default Integer getEmissiveTexcoord()
    {
        TextureInfoModel textureInfo = getEmissiveTextureInfoModel();
        if (textureInfo == null)
        {
            return null;
        }
        return textureInfo.getTexCoord();
    }
    
    /**
     * Returns the emissive factor
     * 
     * @return The emissive factor
     */
    double[] getEmissiveFactor();

    /**
     * Returns the alpha mode
     * 
     * @return The alpha mode
     */
    AlphaMode getAlphaMode();

    /**
     * Returns the alpha cutoff
     * 
     * @return The alpha cutoff
     */
    double getAlphaCutoff();

    /**
     * Returns whether the material is double sided
     *
     * @return Whether the material is double sided
     */
    boolean isDoubleSided();
    
}