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
package de.javagl.jgltf.obj.v1;

import java.util.Map;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.Texture;
import de.javagl.obj.Mtl;
import de.javagl.obj.ReadableObj;

/**
 * A class for providing {@link Material} instances that are used when
 * converting an OBJ into a glTF asset.
 */
class MtlMaterialHandlerV1
{
    /**
     * The {@link TechniqueHandler} that maintains the {@link Technique}s
     * that are required for the {@link GlTF}
     */
    private final TechniqueHandler techniqueHandler;
    
    /**
     * The {@link TextureHandlerV1} that maintains the {@link Image}s and
     * {@link Texture}s that are required for the {@link GlTF}
     */
    private final TextureHandlerV1 textureHandler;

    /**
     * Default constructor 
     * 
     * @param gltf The {@link GlTF}
     */
    MtlMaterialHandlerV1(GlTF gltf)
    {
        this.techniqueHandler = new TechniqueHandler(gltf);
        this.textureHandler = new TextureHandlerV1(gltf);
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
        return createMaterialWithColor(
            withNormals, 0.75f, 0.75f, 0.75f);
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
        boolean withTexture = true;
        String techniqueId =
            techniqueHandler.getTechniqueId(withTexture, withNormals);
        
        Material material = new Material();
        material.setTechnique(techniqueId);
        String imageUri = mtl.getMapKd();
        String textureId = textureHandler.getTextureId(imageUri);
        Map<String, Object> materialValues = 
            MtlMaterialValues.createMaterialValues(mtl, textureId);
        material.setValues(materialValues);
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
        boolean withTexture = false;
        String techniqueId = 
            techniqueHandler.getTechniqueId(withTexture, withNormals);

        Material material = new Material();
        material.setTechnique(techniqueId);
        Map<String, Object> materialValues = 
            MtlMaterialValues.createDefaultMaterialValues(r, g, b);
        material.setValues(materialValues);
        return material;
    }
    
}
