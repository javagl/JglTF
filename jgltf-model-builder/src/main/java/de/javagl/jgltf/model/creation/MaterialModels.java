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

import de.javagl.jgltf.model.MaterialModel;
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
    public static MaterialModelV2 createFromColor(
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
     * and a metallic value of 0.0, with a base color texture that was
     * created from the specified image file.
     * 
     * @param imageFileName The image file name
     * @return The material model
     */
    public static MaterialModelV2 createFromImageFile(String imageFileName) 
    {
        MaterialBuilder builder = MaterialBuilder.create();
        builder.setBaseColorTexture(imageFileName, null, null);
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
