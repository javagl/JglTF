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
package de.javagl.jgltf.model.impl;

import java.util.List;

import de.javagl.jgltf.impl.v2.MaterialNormalTextureInfo;
import de.javagl.jgltf.impl.v2.MaterialOcclusionTextureInfo;
import de.javagl.jgltf.impl.v2.TextureInfo;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.OcclusionTextureInfoModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.extensions.ExtensionModels;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Convenience methods to create {@link TextureInfo} objects from
 * {@link TextureInfoModel} objects, including subtypes like
 * {@link NormalTextureInfoModel} and {@link OcclusionTextureInfoModel}.<br>
 * <br>
 * Clients will usually not use this class, except for implementors of
 * extensions.
 */
public class TextureInfos
{
    /**
     * Create a {@link TextureInfo} from the given
     * {@link TextureInfoModel}, or <code>null</code> if the given
     * texture info is <code>null</code>.
     * 
     * @param gltfModel The {@link GltfModel}
     * @param textureInfoModel The {@link TextureInfoModel}
     * @return The {@link TextureInfo}
     */
    public static TextureInfo from(
        GltfModel gltfModel,
        TextureInfoModel textureInfoModel)
    {
        if (textureInfoModel == null)
        {
            return null;
        }
        TextureInfo textureInfo =
            new TextureInfo();
        ModelElementsV2.transferGltfPropertyElementsFromModel(textureInfoModel,
            textureInfo);

        TextureModel textureModel = textureInfoModel.getTextureModel();
        List<TextureModel> textureModels = gltfModel.getTextureModels(); 
        int index = textureModels.indexOf(textureModel);
        textureInfo.setIndex(index);
        textureInfo.setTexCoord(textureInfoModel.getTexCoord());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, textureInfoModel, 
            TextureInfoModel.class, textureInfo);
        
        return textureInfo;
    }

    /**
     * Create a {@link MaterialNormalTextureInfo} from the given
     * {@link NormalTextureInfoModel}, or <code>null</code> if the given
     * texture info is <code>null</code>.
     * 
     * @param gltfModel The {@link GltfModel}
     * @param textureInfoModel The {@link NormalTextureInfoModel}
     * @return The {@link MaterialNormalTextureInfo}
     */
    public static MaterialNormalTextureInfo from(
        GltfModel gltfModel,
        NormalTextureInfoModel textureInfoModel)
    {
        if (textureInfoModel == null)
        {
            return null;
        }
        MaterialNormalTextureInfo textureInfo =
            new MaterialNormalTextureInfo();
        ModelElementsV2.transferGltfPropertyElementsFromModel(textureInfoModel,
            textureInfo);

        TextureModel textureModel = textureInfoModel.getTextureModel();
        List<TextureModel> textureModels = gltfModel.getTextureModels(); 
        int index = textureModels.indexOf(textureModel);
        textureInfo.setIndex(index);
        textureInfo.setTexCoord(textureInfoModel.getTexCoord());
        textureInfo.setScale(textureInfoModel.getScale());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, textureInfoModel, 
            NormalTextureInfoModel.class, textureInfo);
        
        return textureInfo;
    }
    
    /**
     * Create a {@link MaterialOcclusionTextureInfo} from the given
     * {@link OcclusionTextureInfoModel}, or <code>null</code> if the given
     * texture info is <code>null</code>.
     * 
     * @param gltfModel The {@link GltfModel}
     * @param textureInfoModel The {@link OcclusionTextureInfoModel}
     * @return The {@link MaterialOcclusionTextureInfo}
     */
    public static MaterialOcclusionTextureInfo from(
        GltfModel gltfModel,
        OcclusionTextureInfoModel textureInfoModel)
    {
        if (textureInfoModel == null)
        {
            return null;
        }
        MaterialOcclusionTextureInfo textureInfo =
            new MaterialOcclusionTextureInfo();
        ModelElementsV2.transferGltfPropertyElementsFromModel(textureInfoModel,
            textureInfo);

        TextureModel textureModel = textureInfoModel.getTextureModel();
        List<TextureModel> textureModels = gltfModel.getTextureModels(); 
        int index = textureModels.indexOf(textureModel);
        textureInfo.setIndex(index);
        textureInfo.setTexCoord(textureInfoModel.getTexCoord());
        textureInfo.setStrength(textureInfoModel.getStrength());
        
        ExtensionModels.createExtensionImpls(
            gltfModel, textureInfoModel, 
            OcclusionTextureInfoModel.class, textureInfo);
        
        return textureInfo;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private TextureInfos()
    {
        // Private constructor to prevent instantiation
    }
}
