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
package de.javagl.jgltf.obj;

import java.util.LinkedHashMap;
import java.util.Map;

import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Texture;
import de.javagl.jgltf.model.GltfConstants;

/**
 * A class for managing the {@link Texture}s of a {@link GlTF}. It allows
 * obtaining a {@link Texture} ID for a given {@link Image#getUri() image URI},
 * creating the {@link Image} and the {@link Texture} if they do not exist
 * yet. 
 */
class TextureHandler
{
    /**
     * The {@link GlTF} which contains the {@link Image}s and {@link Texture}s
     */
    private final GlTF gltf;
    
    /**
     * A mapping from {@link Image#getUri() image URIs} to {@link Texture} IDs
     */
    private final Map<String, String> imageUriToTextureId;
    
    /**
     * Creates a new texture handler that creates {@link Image}s and
     * {@link Texture}s in the given {@link GlTF} as necessary
     * 
     * @param gltf The {@link GlTF}
     */
    TextureHandler(GlTF gltf)
    {
        this.gltf = gltf;
        this.imageUriToTextureId = new LinkedHashMap<String, String>();
    }
    
    /**
     * Returns the ID of the {@link Texture} that contains the {@link Image}
     * with the given {@link Image#getUri() image URI}. If such a 
     * {@link Texture} did not exist yet, it is created (together with the
     * {@link Image}) and stored in the {@link GlTF}
     *     
     * @param imageUri The {@link Image#getUri() image URI}
     * @return The {@link Texture} ID
     */
    String getTextureId(String imageUri)
    {
        String textureId = imageUriToTextureId.get(imageUri);
        if (textureId != null)
        {
            return textureId;
        }
        
        // Create the image 
        Image image = new Image();
        image.setUri(imageUri);
        String imageId = Gltfs.addImage(gltf, image);
        
        // Create the texture that refers to the image
        Texture texture = new Texture();
        texture.setFormat(GltfConstants.GL_RGBA);
        texture.setInternalFormat(GltfConstants.GL_RGBA);
        texture.setSource(imageId);
        texture.setTarget(GltfConstants.GL_TEXTURE_2D);
        texture.setType(GltfConstants.GL_UNSIGNED_BYTE);
        textureId = Gltfs.addTexture(gltf, texture);

        imageUriToTextureId.put(imageUri, textureId);
        return textureId;
    }
    
    
}
