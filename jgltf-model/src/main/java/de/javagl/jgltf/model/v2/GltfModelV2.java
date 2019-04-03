/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.v2;

import java.util.Objects;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;

/**
 * Implementation of a {@link GltfModel}, based on a {@link GlTF glTF 2.0}.<br>
 */
public final class GltfModelV2 extends DefaultGltfModel implements GltfModel
{
    /**
     * Creates a new model for the given glTF
     * 
     * @param gltfAsset The {@link GltfAssetV2}
     */
    public GltfModelV2(GltfAssetV2 gltfAsset)
    {
        Objects.requireNonNull(gltfAsset, 
            "The gltfAsset may not be null");
        setGltf(gltfAsset.getGltf());

        GltfModelCreatorV2 gltfModelCreatorV2 = 
            new GltfModelCreatorV2(gltfAsset, this);
        gltfModelCreatorV2.create();
    }
    
}
