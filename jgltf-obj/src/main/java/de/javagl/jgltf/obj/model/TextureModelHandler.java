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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.creation.ImageModels;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.io.IO;

/**
 * A class for managing the {@link TextureModel} objects. It allows obtaining 
 * a {@link TextureModel} for a given image URI, creating the 
 * {@link ImageModel} and the {@link TextureModel} if they do not exist yet. 
 */
class TextureModelHandler
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(TextureModelHandler.class.getName());
    
    /**
     * The URI to resolve image URIs against
     */
    private final String baseUri;
    
    /**
     * A mapping from URIs to {@link TextureModel} objects
     */
    private final Map<String, TextureModel> imageUriToTextureModel;
    
    /**
     * Creates a new texture handler that creates {@link ImageModel} and
     * {@link TextureModel} objects as necessary
     * 
     * @param baseUri The URI to resolve image URIs against
     */
    TextureModelHandler(String baseUri)
    {
        this.baseUri = baseUri;
        this.imageUriToTextureModel = new LinkedHashMap<String, TextureModel>();
    }
    
    /**
     * Returns the {@link TextureModel} that contains the {@link ImageModel}
     * with the given URI. If such a {@link TextureModel} did not exist yet, 
     * it is created (together with the {@link ImageModel})
     *     
     * @param imageUri The image URI
     * @return The {@link TextureModel} 
     */
    TextureModel getTextureModel(String imageUri)
    {
        TextureModel textureModel = imageUriToTextureModel.get(imageUri);
        if (textureModel != null)
        {
            return textureModel;
        }
        
        String inputUri = null;
        try
        {
            inputUri = IO.makeAbsolute(URI.create(baseUri), imageUri).toString();
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
        DefaultImageModel imageModel = ImageModels.create(
            inputUri, imageUri);
        DefaultTextureModel newTextureModel = new DefaultTextureModel();
        newTextureModel.setImageModel(imageModel);

        imageUriToTextureModel.put(imageUri, newTextureModel);
        return newTextureModel;
    }
    
    /**
     * Returns the list of all {@link TextureModel} objects that have been
     * created
     * 
     * @return The {@link TextureModel} objects
     */
    List<TextureModel> getTextureModels()
    {
        return new ArrayList<TextureModel>(imageUriToTextureModel.values());
    }
    
    
}
