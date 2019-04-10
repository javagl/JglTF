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

import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.v1.GltfCreatorV1;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v2.GltfCreatorV2;
import de.javagl.jgltf.model.v2.GltfModelCreatorV2;

/**
 * Methods to create {@link GltfModel} instances from a {@link GltfAsset}
 */
public class GltfModels
{
    /**
     * Creates a {@link GltfModel} instance from the given {@link GltfAsset}
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @return The {@link GltfModel}
     * @throws IllegalArgumentException If the given asset has an 
     * unknown version
     */
    public static GltfModel create(GltfAsset gltfAsset)
    {
        if (gltfAsset instanceof GltfAssetV1)
        {
            GltfAssetV1 gltfAssetV1 = (GltfAssetV1)gltfAsset;
            return new GltfModelV1(gltfAssetV1);
        }
        if (gltfAsset instanceof GltfAssetV2)
        {
            GltfAssetV2 gltfAssetV2 = (GltfAssetV2)gltfAsset;
            return GltfModelCreatorV2.create(gltfAssetV2);
        }
        throw new IllegalArgumentException(
            "The glTF asset has an unknown version: " + gltfAsset);
    }
    
    /**
     * Obtains the raw glTF object for the given {@link GltfModel}.<br>
     * <br>
     * If the given {@link GltfModel#getGltf()} method returns a 
     * non-<code>null</code> value, then this value will be returned.
     * This may either be a {@link de.javagl.jgltf.impl.v1.GlTF glTF 1.0}
     * or a {@link de.javagl.jgltf.impl.v2.GlTF glTF 2.0} object.<br>
     * <br>
     * Otherwise a {@link de.javagl.jgltf.impl.v2.GlTF glTF 2.0} object
     * for the given model will be created.
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The glTF object
     */
    public static Object getGltf(GltfModel gltfModel)
    {
        Object gltf = gltfModel.getGltf();
        if (gltf != null)
        {
            return gltf;
        }
        return getGltfV2(gltfModel);
    }
    
    /**
     * Obtains a {@link de.javagl.jgltf.impl.v1.GlTF glTF 1.0} object for
     * the given {@link GltfModel}.<br>
     * <br>
     * If the given {@link GltfModel#getGltf()} method returns a 
     * non-<code>null</code> value, and this value already is a 
     * glTF 1.0 object, it will be returned. Otherwise, a new 
     * glTF 1.0 object will be created from the given model 
     * and returned.
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The glTF object
     */
    public static de.javagl.jgltf.impl.v1.GlTF getGltfV1(GltfModel gltfModel)
    {
        Object gltf = gltfModel.getGltf();
        if (gltf != null && gltf instanceof de.javagl.jgltf.impl.v1.GlTF)
        {
            de.javagl.jgltf.impl.v1.GlTF gltfV1 = 
                (de.javagl.jgltf.impl.v1.GlTF)gltf;
            return gltfV1;
        }
        GltfCreatorV1 gltfCreator = new GltfCreatorV1(gltfModel);
        return gltfCreator.create();
    }
    
    /**
     * Obtains a {@link de.javagl.jgltf.impl.v1.GlTF glTF 2.0} object for
     * the given {@link GltfModel}.<br>
     * <br>
     * If the given {@link GltfModel#getGltf()} method returns a 
     * non-<code>null</code> value, and this value already is a 
     * glTF 2.0 object, it will be returned. Otherwise, a new 
     * glTF 2.0 object will be created from the given model 
     * and returned.
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The glTF object
     */
    public static de.javagl.jgltf.impl.v2.GlTF getGltfV2(GltfModel gltfModel)
    {
        Object gltf = gltfModel.getGltf();
        if (gltf != null && gltf instanceof de.javagl.jgltf.impl.v2.GlTF)
        {
            de.javagl.jgltf.impl.v2.GlTF gltfV2 = 
                (de.javagl.jgltf.impl.v2.GlTF)gltf;
            return gltfV2;
        }
        GltfCreatorV2 gltfCreator = new GltfCreatorV2(gltfModel);
        return gltfCreator.create();
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfModels()
    {
        // Private constructor to prevent instantiation
    }
}
