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
package de.javagl.jgltf.model.creation;

import java.awt.image.BufferedImage;

import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;

/**
 * Methods related to {@link TextureModel} instances
 */
public class TextureModels
{
    /**
     * Creates a {@link TextureModel} from the given image model.
     * 
     * @param imageModel The image model
     * @return The texture model
     */
    public static DefaultTextureModel createFromImage(
        ImageModel imageModel) 
    {
        DefaultTextureModel result = new DefaultTextureModel();
        result.setImageModel(imageModel);
        return result;
    }

    /**
     * Creates a {@link TextureModel} with an image that was created from the 
     * specified image file.
     * 
     * @param imageFileName The image file name
     * @param uri The URI to set for the {@link ImageModel}
     * @return The texture model
     */
    public static DefaultTextureModel createFromImageFile(
        String imageFileName, String uri) 
    {
        ImageModel imageModel = ImageModels.create(imageFileName, uri);
        return createFromImage(imageModel);
    }

    /**
     * Creates a {@link TextureModel} from the given buffered image.
     * 
     * @param bufferedImage The buffered image
     * @param uri The URI for the underlying image model
     * @param mimeType The MIME type for the image
     * @return The texture model
     */
    public static DefaultTextureModel createFromBufferedImage(
        BufferedImage bufferedImage, String uri, String mimeType) 
    {
        ImageModel imageModel = ImageModels.createFromBufferedImage(
            uri, mimeType, bufferedImage);
        return createFromImage(imageModel);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private TextureModels()
    {
        // Private constructor to prevent instantiation
    }

}
