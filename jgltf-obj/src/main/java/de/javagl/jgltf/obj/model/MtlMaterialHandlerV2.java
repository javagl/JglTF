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
package de.javagl.jgltf.obj.model;

import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.PbrMaterialModel.AlphaMode;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultPbrMetallicRoughnessModel;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.Mtl;
import de.javagl.obj.ReadableObj;

/**
 * A class for providing {@link DefaultPbrMaterialModel} instances that are used when
 * converting an OBJ into a glTF model.
 */
class MtlMaterialHandlerV2 implements MtlMaterialHandler
{
    /**
     * The {@link TextureModelHandler} that will generate the required
     * {@link TextureModel} objects on demand
     */
    private final TextureModelHandler textureModelHandler;

    /**
     * Default constructor
     *  
     * @param baseUri The URI to resolve image URIs against
     */
    MtlMaterialHandlerV2(String baseUri)
    {
        this.textureModelHandler = new TextureModelHandler(baseUri);
    }
    
    @Override
    public MaterialModel createMaterial(ReadableObj obj, Mtl mtl)
    {
        boolean withTexture = obj.getNumTexCoords() > 0 && 
            mtl != null && mtl.getMapKd() != null;
        if (withTexture)
        {
            return createMaterialWithTexture(mtl);
        }
        DefaultPbrMaterialModel material = createMaterialWithColor(
            true, 0.75f, 0.75f, 0.75f);
        if (mtl == null)
        {
            return material;
        }

        // If there is an MTL, try to translate some of the MTL
        // information into reasonable PBR information
        double baseColorFactor[] = new double[] { 1.0f, 1.0f, 1.0f, 1.0f };
        FloatTuple ambientColor = mtl.getKd();
        if (ambientColor != null)
        {
            baseColorFactor[0] = ambientColor.get(0);
            baseColorFactor[1] = ambientColor.get(1);
            baseColorFactor[2] = ambientColor.get(2);
        }
        Float opacity = mtl.getD();
        if (opacity != null)
        {
            baseColorFactor[3] = opacity;
            if (opacity < 1.0f)
            {
                material.setAlphaMode(AlphaMode.BLEND);
            }
        }
        
        DefaultPbrMetallicRoughnessModel metallicRoughness = 
            new DefaultPbrMetallicRoughnessModel();
        metallicRoughness.setBaseColorFactor(baseColorFactor);
        Float shininess = mtl.getNs();
        if (shininess != null)
        {
            metallicRoughness.setMetallicFactor(shininess / 128f);
        }
        material.setPbrMetallicRoughnessModel(metallicRoughness);

        material.setDoubleSided(true);
        return material;
    }

    /**
     * Create a simple {@link MaterialModel} with a texture that will be 
     * taken from the diffuse map of the given MTL. The given MTL and 
     * its diffuse map must be non-<code>null</code>.
     * 
     * @param mtl The MTL
     * @return The {@link MaterialModel}
     */
    private MaterialModel createMaterialWithTexture(Mtl mtl)
    {
        String imageUri = mtl.getMapKd();

        TextureModel textureModel = 
            textureModelHandler.getTextureModel(imageUri);
        
        DefaultPbrMaterialModel material = new DefaultPbrMaterialModel();
        DefaultPbrMetallicRoughnessModel metallicRoughness = 
            new DefaultPbrMetallicRoughnessModel();
        material.setPbrMetallicRoughnessModel(metallicRoughness);
        
        DefaultTextureInfoModel textureInfo = new DefaultTextureInfoModel();
        textureInfo.setTextureModel(textureModel);
        metallicRoughness.setBaseColorTexture(textureInfo);

        metallicRoughness.setMetallicFactor(0.0f);
        metallicRoughness.setRoughnessFactor(1.0f);
        
        material.setDoubleSided(true);
        return material;
    }
    
    @Override
    public DefaultPbrMaterialModel createMaterialWithColor(
        boolean withNormals, double r, double g, double b)
    {
        DefaultPbrMaterialModel material = new DefaultPbrMaterialModel();
        
        DefaultPbrMetallicRoughnessModel metallicRoughness = 
            new DefaultPbrMetallicRoughnessModel();
        material.setPbrMetallicRoughnessModel(metallicRoughness);

        metallicRoughness.setMetallicFactor(0.0f);
        metallicRoughness.setRoughnessFactor(1.0f);
        metallicRoughness.setBaseColorFactor(new double[] { r, g, b, 1.0f });
        material.setDoubleSided(true);
        return material;
    }
    
}
