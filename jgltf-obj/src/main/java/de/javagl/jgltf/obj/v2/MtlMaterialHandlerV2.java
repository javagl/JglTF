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
package de.javagl.jgltf.obj.v2;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.impl.v2.Material;
import de.javagl.jgltf.impl.v2.MaterialPbrMetallicRoughness;
import de.javagl.jgltf.impl.v2.Texture;
import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.Mtl;
import de.javagl.obj.ReadableObj;

/**
 * A class for providing {@link Material} instances that are used when
 * converting an OBJ into a glTF asset.
 */
public class MtlMaterialHandlerV2
{
    /**
     * The {@link TextureHandlerV2} that maintains the {@link Image}s and
     * {@link Texture}s that are required for the {@link GlTF}
     */
    private final TextureHandlerV2 textureHandler;

    /**
     * Default constructor 
     * 
     * @param gltf The {@link GlTF}
     */
    MtlMaterialHandlerV2(GlTF gltf)
    {
        this.textureHandler = new TextureHandlerV2(gltf);
    }
    
    /**
     * Create a simple {@link Material} for the glTF that is created from
     * the given OBJ and MTL.
     * 
     * @param obj The {@link ReadableObj}
     * @param mtl The {@link Mtl}
     * @return The {@link Material}
     */
    Material createMaterial(ReadableObj obj, Mtl mtl)
    {
        boolean withTexture = obj.getNumTexCoords() > 0 && 
            mtl != null && mtl.getMapKd() != null;
        boolean withNormals = obj.getNumNormals() > 0;
        if (withTexture)
        {
            return createMaterialWithTexture(withNormals, mtl);
        }
        Material material = createMaterialWithColor(
            withNormals, 0.75f, 0.75f, 0.75f);
        if (mtl == null)
        {
            return material;
        }

        // If there is an MTL, try to translate some of the MTL
        // information into reasonable PBR information

        FloatTuple ambientColor = mtl.getKd();
        float r = ambientColor.get(0);
        float g = ambientColor.get(1);
        float b = ambientColor.get(2);
        float opacity = mtl.getD();
        if (opacity < 1.0f)
        {
            material.setAlphaMode("BLEND");
        }
        MaterialPbrMetallicRoughness pbr = material.getPbrMetallicRoughness();
        float[] baseColorFactor = new float[] { r, g, b, opacity };
        pbr.setBaseColorFactor(baseColorFactor);

        float shininess = mtl.getNs();
        pbr.setMetallicFactor(shininess / 128f);

        material.setDoubleSided(true);
        return material;
    }

    /**
     * Create a simple {@link Material} with a texture that will be taken from 
     * the diffuse map of the given MTL. The given MTL and its diffuse
     * map must be non-<code>null</code>.
     * 
     * @param withNormals Whether the {@link Material} should support normals
     * @param mtl The MTL
     * @return The {@link Material}
     */
    private Material createMaterialWithTexture(
        boolean withNormals, Mtl mtl)
    {
        String imageUri = mtl.getMapKd();

        int textureIndex = textureHandler.getTextureIndex(imageUri);
        TextureInfo baseColorTexture = new TextureInfo();
        baseColorTexture.setIndex(textureIndex);
        
        MaterialPbrMetallicRoughness pbrMetallicRoughness = 
            new MaterialPbrMetallicRoughness();
        pbrMetallicRoughness.setBaseColorTexture(baseColorTexture);
        pbrMetallicRoughness.setMetallicFactor(0.0f);

        Material material = new Material();
        material.setPbrMetallicRoughness(pbrMetallicRoughness);
        
        return material;
    }
    
    /**
     * Create a simple {@link Material} with the given diffuse color
     * 
     * @param withNormals Whether the {@link Material} should support normals
     * @param r The red component of the diffuse color
     * @param g The green component of the diffuse color
     * @param b The blue component of the diffuse color
     * @return The {@link Material}
     */
    Material createMaterialWithColor(
        boolean withNormals, float r, float g, float b)
    {
        MaterialPbrMetallicRoughness pbrMetallicRoughness = 
            new MaterialPbrMetallicRoughness();
        float[] baseColorFactor = new float[] { r, g, b, 1.0f };
        pbrMetallicRoughness.setBaseColorFactor(baseColorFactor);
        pbrMetallicRoughness.setMetallicFactor(0.0f);
        
        Material material = new Material();
        material.setPbrMetallicRoughness(pbrMetallicRoughness);

        return material;
    }
    
}
