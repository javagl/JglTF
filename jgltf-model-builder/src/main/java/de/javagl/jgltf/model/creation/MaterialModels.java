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

import java.awt.image.BufferedImage;

import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * Methods related to {@link MaterialModel} instances
 */
public class MaterialModels
{
    /**
     * Creates a simple, double-sided material with a roughness value of 1.0
     * and a metallic value of 0.0, with the given base color factors
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     * @param a The alpha component
     * @return The material model
     */
    public static MaterialModelV2 createFromBaseColor(
        float r, float g, float b, float a)
    {
        MaterialBuilder builder = MaterialBuilder.create();
        builder.setBaseColorFactor(r, g, b, a);
        builder.setDoubleSided(true);
        builder.setMetallicRoughnessFactors(0.0f, 1.0f);
        MaterialModelV2 result = builder.build();
        return result;
    }

    /**
     * Creates a simple, double-sided material with a roughness value of 1.0
     * and a metallic value of 0.0, with the given base color texture
     * 
     * @param baseColorTexture The base color texture
     * @param texCoord The optional texture coordinate index
     * @return The material model
     */
    public static MaterialModelV2 createFromBaseColorTexture(
        TextureModel baseColorTexture, Integer texCoord)
    {
        MaterialBuilder builder = MaterialBuilder.create();
        builder.setBaseColorTexture(baseColorTexture, texCoord);
        builder.setDoubleSided(true);
        builder.setMetallicRoughnessFactors(0.0f, 1.0f);
        MaterialModelV2 result = builder.build();
        return result;
    }
    
    /**
     * Creates a simple, double-sided material with a roughness value of 1.0
     * and a metallic value of 0.0, with a base color texture that was
     * created from the specified image file.
     * 
     * @param imageFileName The image file name
     * @param uri The URI for the image
     * @return The material model
     */
    public static MaterialModelV2 createFromImageFile(
        String imageFileName, String uri) 
    {
        MaterialBuilder builder = MaterialBuilder.create();
        builder.setBaseColorTexture(imageFileName, uri, null);
        builder.setDoubleSided(true);
        builder.setMetallicRoughnessFactors(0.0f, 1.0f);
        MaterialModelV2 result = builder.build();
        return result;
    }
    
    /**
     * Creates a simple, double-sided material with a roughness value of 1.0
     * and a metallic value of 0.0, with a base color texture that was
     * created from the given image.
     * 
     * @param bufferedImage The buffered image
     * @param uri The URI for the image model
     * @param mimeType The MIME type of the image
     * @return The material model
     */
    public static MaterialModelV2 createFromBufferedImage(
        BufferedImage bufferedImage, String uri, String mimeType) 
    {
        DefaultTextureModel baseColorTexture = 
            TextureModels.createFromBufferedImage(bufferedImage, uri, mimeType);
        MaterialBuilder builder = MaterialBuilder.create();
        builder.setBaseColorTexture(baseColorTexture, null);
        builder.setDoubleSided(true);
        builder.setMetallicRoughnessFactors(0.0f, 1.0f);
        MaterialModelV2 result = builder.build();
        return result;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private MaterialModels()
    {
        // Private constructor to prevent instantiation
    }

}
