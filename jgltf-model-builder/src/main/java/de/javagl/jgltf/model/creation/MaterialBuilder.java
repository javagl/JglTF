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
package de.javagl.jgltf.model.creation;

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.v2.MaterialModelV2;
import de.javagl.jgltf.model.v2.MaterialModelV2.AlphaMode;

/**
 * A class for building {@link MaterialModelV2} instances
 */
public class MaterialBuilder
{
    /**
     * Creates a new instance
     * 
     * @return The {@link MaterialBuilder}
     */
    public static MaterialBuilder create()
    {
        return new MaterialBuilder();
    }
    
    /**
     * The {@link MaterialModelV2} that is currently being built
     */
    private MaterialModelV2 materialModel;
    
    /**
     * Private constructor
     */
    private MaterialBuilder()
    {
        materialModel = new MaterialModelV2();
    }
    
    /**
     * Set the base color factors
     * 
     * @param r The red factor
     * @param g The green factor
     * @param b The blue factor
     * @param a The alpha factor
     * @return This builder
     */
    public MaterialBuilder setBaseColorFactor(
        float r, float g, float b, float a)
    {
        materialModel.setBaseColorFactor(new float[] { r, g, b, a });
        return this;
    }
    
    /**
     * Convenience method to set the base color texture to the image that
     * is read from the specified file.
     * 
     * @param fileName The file name
     * @param uri The URI that will be assigned to the {@link ImageModel}
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setBaseColorTexture(
        String fileName, String uri, Integer texCoord)
    {
        DefaultImageModel imageModel = ImageModels.create(fileName, uri);
        DefaultTextureModel textureModel = new DefaultTextureModel();
        textureModel.setImageModel(imageModel);
        return setBaseColorTexture(textureModel, texCoord);
    }
    
    /**
     * Set the base color texture
     * 
     * @param baseColorTexture The base color texture
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setBaseColorTexture(
        TextureModel baseColorTexture, Integer texCoord)
    {
        materialModel.setBaseColorTexture(baseColorTexture);
        materialModel.setBaseColorTexcoord(texCoord);
        return this;
    }
    
    /**
     * Set the metallic and roughness factors
     * 
     * @param metallicFactor The metallic factor
     * @param roughnessFactor The roughness factor
     * @return This builder
     */
    public MaterialBuilder setMetallicRoughnessFactors(
        float metallicFactor, float roughnessFactor)
    {
        materialModel.setMetallicFactor(metallicFactor);
        materialModel.setRoughnessFactor(roughnessFactor);
        return this;
    }
    
    /**
     * Convenience method to set the metallic-roughness texture to the image 
     * that is read from the specified file.
     * 
     * @param fileName The file name
     * @param uri The URI that will be assigned to the {@link ImageModel}
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setMetallicRoughnessTexture(
        String fileName, String uri, Integer texCoord)
    {
        DefaultImageModel imageModel = ImageModels.create(fileName, uri);
        DefaultTextureModel textureModel = new DefaultTextureModel();
        textureModel.setImageModel(imageModel);
        return setMetallicRoughnessTexture(textureModel, texCoord);
    }
    
    /**
     * Set the metallic-roughness texture
     * 
     * @param metallicRoughnessTexture The metallic-roughness texture
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setMetallicRoughnessTexture(
        TextureModel metallicRoughnessTexture, Integer texCoord)
    {
        materialModel.setMetallicRoughnessTexture(metallicRoughnessTexture);
        materialModel.setMetallicRoughnessTexcoord(texCoord);
        return this;
    }

    /**
     * Convenience method to set the normal texture to the image 
     * that is read from the specified file.
     * 
     * @param fileName The file name
     * @param uri The URI that will be assigned to the {@link ImageModel}
     * @param scale The normal scale
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setNormalTexture(
        String fileName, String uri, 
        float scale, Integer texCoord)
    {
        DefaultImageModel imageModel = ImageModels.create(fileName, uri);
        DefaultTextureModel textureModel = new DefaultTextureModel();
        textureModel.setImageModel(imageModel);
        return setNormalTexture(textureModel, scale, texCoord);
    }
    
    /**
     * Set the normal texture
     * 
     * @param normalTexture The normal texture
     * @param scale The scaling
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setNormalTexture(
        TextureModel normalTexture, float scale, Integer texCoord)
    {
        materialModel.setNormalTexture(normalTexture);
        materialModel.setNormalScale(scale);
        materialModel.setNormalTexcoord(texCoord);
        return this;
    }

    /**
     * Convenience method to set the occlusion texture to the image 
     * that is read from the specified file.
     * 
     * @param fileName The file name
     * @param uri The URI that will be assigned to the {@link ImageModel}
     * @param strength The occlusion strength
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setOcclusionTexture(
        String fileName, String uri, 
        float strength, Integer texCoord)
    {
        DefaultImageModel imageModel = ImageModels.create(fileName, uri);
        DefaultTextureModel textureModel = new DefaultTextureModel();
        textureModel.setImageModel(imageModel);
        return setOcclusionTexture(textureModel, strength, texCoord);
    }
    
    /**
     * Set the occlusion texture
     * 
     * @param occlusionTexture The occlusion texture
     * @param strength The occlusion strength
     * @param texCoord The optional texture coordinate set 
     * @return This builder
     */
    public MaterialBuilder setOcclusionTexture(
        TextureModel occlusionTexture, float strength, Integer texCoord)
    {
        materialModel.setOcclusionTexture(occlusionTexture);
        materialModel.setOcclusionStrength(strength);
        materialModel.setOcclusionTexcoord(texCoord);
        return this;
    }

    /**
     * Convenience method to set the occlusion texture to the image 
     * that is read from the specified file.
     * 
     * @param fileName The file name
     * @param uri The URI that will be assigned to the {@link ImageModel}
     * @param r The red factor
     * @param g The green factor
     * @param b The blue factor
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setEmissiveTexture(
        String fileName, String uri, 
        float r, float g, float b, Integer texCoord)
    {
        DefaultImageModel imageModel = ImageModels.create(fileName, uri);
        DefaultTextureModel textureModel = new DefaultTextureModel();
        textureModel.setImageModel(imageModel);
        return setEmissiveTexture(textureModel, r, g, b, texCoord);
    }
    
    /**
     * Set the emissive texture
     * 
     * @param emissiveTexture The emissive texture
     * @param r The red factor
     * @param g The green factor
     * @param b The blue factor
     * @param texCoord The optional texture coordinate set
     * @return This builder
     */
    public MaterialBuilder setEmissiveTexture(TextureModel emissiveTexture, 
        float r, float g, float b, Integer texCoord)
    {
        materialModel.setEmissiveTexture(emissiveTexture);
        materialModel.setEmissiveFactor(new float[] { r, g, b });
        materialModel.setEmissiveTexcoord(texCoord);
        return this;
    }
    
    /**
     * Set the alpha mode
     * 
     * @param alphaMode The alpha mode
     * @return This builder
     */
    public MaterialBuilder setAlphaMode(AlphaMode alphaMode)
    {
        materialModel.setAlphaMode(alphaMode);
        return this;
    }

    /**
     * Set the alpha cutoff
     * 
     * @param alphaCutoff The alpha cutoff
     * @return This builder
     */
    public MaterialBuilder setAlphaCutoff(float alphaCutoff)
    {
        materialModel.setAlphaCutoff(alphaCutoff);
        return this;
    }

    /**
     * Set whether the material is double sided
     * 
     * @param doubleSided Whether the material is double sided
     * @return This builder
     */
    public MaterialBuilder setDoubleSided(boolean doubleSided)
    {
        materialModel.setDoubleSided(doubleSided);
        return this;
    }

    /**
     * Create the {@link MaterialModelV2} instance with the current state
     * 
     * @return The {@link MaterialModelV2}
     */
    public MaterialModelV2 build()
    {
        MaterialModelV2 result = materialModel;
        materialModel = new MaterialModelV2();
        return result;
    }
}
