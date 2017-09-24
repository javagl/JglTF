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

import java.util.LinkedHashMap;
import java.util.Map;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Sampler;
import de.javagl.jgltf.impl.v1.Texture;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.v1.GltfIds;

/**
 * A class for managing the {@link Texture}s of a {@link GlTF}. It allows
 * obtaining a {@link Texture} ID for a given {@link Image#getUri() image URI},
 * creating the {@link Image} and the {@link Texture} if they do not exist
 * yet. 
 */
class TextureHandlerV1
{
    /**
     * The {@link GlTF} which contains the {@link Image}s and {@link Texture}s
     */
    private final GlTF gltf;
    
    /**
     * The ID of the common {@link Sampler} that will be used for all 
     * {@link Texture}s. The {@link Sampler} will be created on demand
     * and added to the {@link GlTF}
     */
    private String samplerId;
    
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
    TextureHandlerV1(GlTF gltf)
    {
        this.gltf = gltf;
        this.samplerId = null;
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
        // Create the common (default) sampler if it was not created yet
        if (samplerId == null)
        {
            Sampler sampler = new Sampler();
            samplerId = GltfIds.generateId("sampler", gltf.getSamplers());
            gltf.addSamplers(samplerId, sampler);
        }
        
        String textureId = imageUriToTextureId.get(imageUri);
        if (textureId != null)
        {
            return textureId;
        }
        
        // Create the image 
        Image image = new Image();
        image.setUri(imageUri);
        String imageId = GltfIds.generateId("image", gltf.getImages());
        gltf.addImages(imageId, image);
        
        // Create the texture that refers to the image
        Texture texture = new Texture();
        texture.setFormat(GltfConstants.GL_RGBA);
        texture.setInternalFormat(GltfConstants.GL_RGBA);
        texture.setSource(imageId);
        texture.setTarget(GltfConstants.GL_TEXTURE_2D);
        texture.setType(GltfConstants.GL_UNSIGNED_BYTE);
        texture.setSampler(samplerId);
        textureId = GltfIds.generateId("texture", gltf.getTextures()); 
        gltf.addTextures(textureId, texture);

        imageUriToTextureId.put(imageUri, textureId);
        return textureId;
    }
    
    
}
