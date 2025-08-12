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
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.OcclusionTextureInfoModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.v2.ModelElementsV2;

/**
 * Convenience methods to create {@link TextureInfoModel} objects from
 * {@link TextureInfo} objects, including subtypes like
 * {@link NormalTextureInfoModel} and {@link OcclusionTextureInfoModel}.<br>
 * <br>
 * Clients will usually not use this class, except for implementors of
 * extensions.
 */
public class TextureInfoModels
{
    /**
     * Create a {@link DefaultTextureInfoModel} from the given
     * {@link TextureInfo}, or <code>null</code> if the given
     * texture info is <code>null</code>.
     * 
     * @param textureModels The {@link TextureModel} list
     * @param textureInfo The {@link TextureInfo}
     * @return The {@link DefaultTextureInfoModel}
     */
    public static DefaultTextureInfoModel from(
        List<? extends TextureModel> textureModels, TextureInfo textureInfo)
    {
        if (textureInfo == null)
        {
            return null;
        }
        DefaultTextureInfoModel textureInfoModel =
            new DefaultTextureInfoModel();
        ModelElementsV2.transferGltfPropertyElementsToModel(textureInfo,
            textureInfoModel);

        int index = textureInfo.getIndex();
        TextureModel textureModel = textureModels.get(index);
        textureInfoModel.setTextureModel(textureModel);
        textureInfoModel.setTexCoord(textureInfo.getTexCoord());
        return textureInfoModel;
    }

    /**
     * Create a {@link DefaultNormalTextureInfoModel} from the given
     * {@link MaterialNormalTextureInfo}, or <code>null</code> if the given
     * texture info is <code>null</code>.
     * 
     * @param textureModels The {@link TextureModel} list
     * @param textureInfo The {@link TextureInfo}
     * @return The {@link DefaultTextureInfoModel}
     */
    public static DefaultNormalTextureInfoModel from(
        List<? extends TextureModel> textureModels, 
        MaterialNormalTextureInfo textureInfo)
    {
        if (textureInfo == null)
        {
            return null;
        }
        DefaultNormalTextureInfoModel textureInfoModel =
            new DefaultNormalTextureInfoModel();
        ModelElementsV2.transferGltfPropertyElementsToModel(textureInfo,
            textureInfoModel);

        int index = textureInfo.getIndex();
        TextureModel textureModel = textureModels.get(index);
        textureInfoModel.setTextureModel(textureModel);
        textureInfoModel.setTexCoord(textureInfo.getTexCoord());
        textureInfoModel.setScale(textureInfo.getScale());
        return textureInfoModel;
    }

    /**
     * Create a {@link DefaultOcclusionTextureInfoModel} from the given
     * {@link MaterialOcclusionTextureInfo}, or <code>null</code> if the given
     * texture info is <code>null</code>.
     * 
     * @param textureModels The {@link TextureModel} list
     * @param textureInfo The {@link TextureInfo}
     * @return The {@link DefaultTextureInfoModel}
     */
    public static DefaultOcclusionTextureInfoModel from(
        List<? extends TextureModel> textureModels, 
        MaterialOcclusionTextureInfo textureInfo)
    {
        if (textureInfo == null)
        {
            return null;
        }
        DefaultOcclusionTextureInfoModel textureInfoModel =
            new DefaultOcclusionTextureInfoModel();
        ModelElementsV2.transferGltfPropertyElementsToModel(textureInfo,
            textureInfoModel);

        int index = textureInfo.getIndex();
        TextureModel textureModel = textureModels.get(index);
        textureInfoModel.setTextureModel(textureModel);
        textureInfoModel.setTexCoord(textureInfo.getTexCoord());
        textureInfoModel.setStrength(textureInfo.getStrength());
        return textureInfoModel;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private TextureInfoModels()
    {
        // Private constructor to prevent instantiation
    }
}
