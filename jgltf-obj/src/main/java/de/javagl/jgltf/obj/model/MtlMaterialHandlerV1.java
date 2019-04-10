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

import java.util.Map;

import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.v1.MaterialModelV1;
import de.javagl.obj.Mtl;
import de.javagl.obj.ReadableObj;

/**
 * Implementation of a {@link MtlMaterialHandler} that generates 
 * {@link MaterialModelV1} instances
 * @author User
 *
 */
class MtlMaterialHandlerV1 implements MtlMaterialHandler
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
    MtlMaterialHandlerV1(String baseUri)
    {
        this.textureModelHandler = new TextureModelHandler(baseUri);
    }
    
    @Override
    public MaterialModel createMaterial(ReadableObj obj, Mtl mtl)
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
     * Create a {@link MaterialModel} for the texture that is described by
     * the given MTL
     * 
     * @param withNormals Whether the rendered object has normals
     * @param mtl The MTL
     * @return The {@link MaterialModel}
     */
    private MaterialModel createMaterialWithTexture(
        boolean withNormals, Mtl mtl)
    {
        MaterialModelV1 materialModelV1 = new MaterialModelV1();
        if (withNormals)
        {
            materialModelV1.setTechniqueModel(
                ObjTechniqueModels.TECHNIQUE_MODEL_TEXTURE_NORMALS);
        }
        else
        {
            materialModelV1.setTechniqueModel(
                ObjTechniqueModels.TECHNIQUE_MODEL_TEXTURE);
        }
        
        String imageUri = mtl.getMapKd();
        TextureModel textureModel = 
            textureModelHandler.getTextureModel(imageUri);
        Map<String, Object> materialValues = 
            MtlMaterialValues.createMaterialValues(mtl, textureModel);
        materialModelV1.setValues(materialValues);
        return materialModelV1;
    }
    

    @Override
    public MaterialModel createMaterialWithColor(boolean withNormals, 
        float r, float g, float b)
    {
        MaterialModelV1 materialModelV1 = new MaterialModelV1();
        if (withNormals)
        {
            materialModelV1.setTechniqueModel(
                ObjTechniqueModels.TECHNIQUE_MODEL_NORMALS);
        }
        else
        {
            materialModelV1.setTechniqueModel(
                ObjTechniqueModels.TECHNIQUE_MODEL_NONE);
        }
        materialModelV1.setValues(
            MtlMaterialValues.createDefaultMaterialValues(r, g, b));
        return materialModelV1;
    }
    
}
