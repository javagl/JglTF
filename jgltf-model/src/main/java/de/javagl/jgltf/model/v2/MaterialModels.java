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
package de.javagl.jgltf.model.v2;

import java.util.LinkedHashMap;
import java.util.Map;

import de.javagl.jgltf.impl.v2.Material;
import de.javagl.jgltf.impl.v2.MaterialNormalTextureInfo;
import de.javagl.jgltf.impl.v2.MaterialOcclusionTextureInfo;
import de.javagl.jgltf.impl.v2.MaterialPbrMetallicRoughness;
import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.impl.DefaultMaterialModel;
import de.javagl.jgltf.model.v2.gl.DefaultModels;
import de.javagl.jgltf.model.v2.gl.Materials;

/**
 * Methods to initialize {@link MaterialModel} instances based on a glTF 2.0
 * {@link Material}.
 */
class MaterialModels
{
    /**
     * Initialize the given {@link MaterialModel} from the values of the
     * given {@link Material}
     * 
     * @param materialModel The {@link MaterialModel}
     * @param material The {@link Material}
     */
    static void initMaterialModel(DefaultMaterialModel materialModel,
        Material material)
    {
        materialModel.setTechniqueModel(
            DefaultModels.getPbrTechniqueModel());
        
        MaterialPbrMetallicRoughness pbrMetallicRoughness = 
            material.getPbrMetallicRoughness();
        if (pbrMetallicRoughness == null)
        {
            pbrMetallicRoughness = 
                Materials.createDefaultMaterialPbrMetallicRoughness();
        }
        
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        
        if (Boolean.TRUE.equals(material.isDoubleSided()))
        {
            values.put("isDoubleSided", 1);
        }
        else
        {
            values.put("isDoubleSided", 0);
        }
        
        TextureInfo baseColorTextureInfo = 
            pbrMetallicRoughness.getBaseColorTexture();
        if (baseColorTextureInfo != null)
        {
            values.put("hasBaseColorTexture", 1);
            values.put("baseColorTexCoord",
                "TEXCOORD_" + baseColorTextureInfo.getTexCoord());
            values.put("baseColorTexture", baseColorTextureInfo.getIndex());
        }
        else
        {
            values.put("hasBaseColorTexture", 0);
        }
        float[] baseColorFactor = Optionals.of(
            pbrMetallicRoughness.getBaseColorFactor(),
            pbrMetallicRoughness.defaultBaseColorFactor());
        values.put("baseColorFactor", baseColorFactor);
        
        
        TextureInfo metallicRoughnessTextureInfo = 
            pbrMetallicRoughness.getMetallicRoughnessTexture();
        if (metallicRoughnessTextureInfo != null)
        {
            values.put("hasMetallicRoughnessTexture", 1);
            values.put("metallicRoughnessTexCoord",
                "TEXCOORD_" + metallicRoughnessTextureInfo.getTexCoord());
            values.put("metallicRoughnessTexture", 
                metallicRoughnessTextureInfo.getIndex());
        }
        else
        {
            values.put("hasMetallicRoughnessTexture", 0);
        }
        float metallicFactor = Optionals.of(
            pbrMetallicRoughness.getMetallicFactor(),
            pbrMetallicRoughness.defaultMetallicFactor());
        values.put("metallicFactor", metallicFactor);
        
        float roughnessFactor = Optionals.of(
            pbrMetallicRoughness.getRoughnessFactor(),
            pbrMetallicRoughness.defaultRoughnessFactor());
        values.put("roughnessFactor", roughnessFactor);
        
        
        MaterialNormalTextureInfo normalTextureInfo = 
            material.getNormalTexture();
        if (normalTextureInfo != null)
        {
            values.put("hasNormalTexture", 1);
            values.put("normalTexCoord",
                "TEXCOORD_" + normalTextureInfo.getTexCoord());
            values.put("normalTexture", 
                normalTextureInfo.getIndex());
            
            float normalScale = Optionals.of(
                normalTextureInfo.getScale(),
                normalTextureInfo.defaultScale());
            values.put("normalScale", normalScale);
        }
        else
        {
            values.put("hasNormalTexture", 0);
            values.put("normalScale", 1.0);
        }

        MaterialOcclusionTextureInfo occlusionTextureInfo = 
            material.getOcclusionTexture();
        if (occlusionTextureInfo != null)
        {
            values.put("hasOcclusionTexture", 1);
            values.put("occlusionTexCoord",
                "TEXCOORD_" + occlusionTextureInfo.getTexCoord());
            values.put("occlusionTexture", 
                occlusionTextureInfo.getIndex());
            
            float occlusionStrength = Optionals.of(
                occlusionTextureInfo.getStrength(),
                occlusionTextureInfo.defaultStrength());
            values.put("occlusionStrength", occlusionStrength);
        }
        else
        {
            values.put("hasOcclusionTexture", 0);
            
            // TODO Should this really be 1.0?
            values.put("occlusionStrength", 0.0); 
        }

        TextureInfo emissiveTextureInfo = 
            material.getEmissiveTexture();
        if (emissiveTextureInfo != null)
        {
            values.put("hasEmissiveTexture", 1);
            values.put("emissiveTexCoord",
                "TEXCOORD_" + emissiveTextureInfo.getTexCoord());
            values.put("emissiveTexture", 
                emissiveTextureInfo.getIndex());
        }
        else
        {
            values.put("hasEmissiveTexture", 0);
        }
        
        float[] emissiveFactor = Optionals.of(
            material.getEmissiveFactor(),
            material.defaultEmissiveFactor());
        values.put("emissiveFactor", emissiveFactor);
        
        materialModel.setValues(values);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private MaterialModels()
    {
        // Private constructor to prevent instantiation
    }
}
