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
package de.javagl.jgltf.model.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Shader;
import de.javagl.jgltf.model.GltfException;

/**
 * Utility methods related to {@link GlTF}s
 */
class GltfUtils
{
    /**
     * Creates a deep copy of the given {@link GlTF}.<br>
     * <br>
     * Note: Some details about the copy are not specified. E.g. whether
     * values that are mapped to <code>null</code> are still contained
     * in the copy. The goal of this method is to create a copy that is,
     * as far as reasonably possible, "structurally equivalent" to the
     * given input.
     * 
     * @param gltf The input 
     * @return The copy
     * @throws GltfException If the copy can not be created
     */
    static GlTF copy(GlTF gltf)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            objectMapper.writeValue(baos, gltf);
            return objectMapper.readValue(baos.toByteArray(), GlTF.class);
        } 
        catch (IOException e)
        {
            throw new GltfException("Could not copy glTF", e);
        }
    }

    /**
     * Creates a shallow copy of the given {@link BufferView}
     * 
     * @param bufferView The {@link BufferView}
     * @return The copy
     */
    static BufferView copy(BufferView bufferView)
    {
        BufferView copy = new BufferView();
        copy.setExtensions(bufferView.getExtensions());
        copy.setExtras(bufferView.getExtras());
        copy.setName(bufferView.getName());
        copy.setBuffer(bufferView.getBuffer());
        copy.setByteOffset(bufferView.getByteOffset());
        copy.setByteLength(bufferView.getByteLength());
        return copy;
    }
    
    
    /**
     * Creates a shallow copy of the given {@link Image}
     * 
     * @param image The {@link Image}
     * @return The copy
     */
    static Image copy(Image image)
    {
        Image copy = new Image();
        copy.setExtensions(image.getExtensions());
        copy.setExtras(image.getExtras());
        copy.setName(image.getName());
        copy.setUri(image.getUri());
        return copy;
    }
    
    /**
     * Creates a shallow copy of the given {@link Shader}
     * 
     * @param shader The {@link Shader}
     * @return The copy
     */
    static Shader copy(Shader shader)
    {
        Shader copy = new Shader();
        copy.setExtensions(shader.getExtensions());
        copy.setExtras(shader.getExtras());
        copy.setName(shader.getName());
        copy.setType(shader.getType());
        copy.setUri(shader.getUri());
        return copy;
    }

    
    /**
     * Tries to detect the format of the given image and its data and 
     * return the corresponding string of the <code>"image/..."</code> MIME 
     * type. This may, for example, be <code>"png"</code> or <code>"gif"</code>
     * or <code>"jpeg"</code> (<b>not</b> <code>"jpg"</code>!).<br>
     * <br>
     * This method will do an (unspecified) best-effort approach to detect 
     * the mime type, either from the image or from the image data (which
     * are both optional). If the type can not be determined, then 
     * <code>null</code> will be returned. 
     *  
     * @param image The {@link Image}
     * @param imageData The image data
     * @return The image format string, or <code>null</code> if it can not
     * be detected.
     */
    static String guessImageMimeTypeString(Image image, ByteBuffer imageData)
    {
        if (image != null)
        {
            String uriString = image.getUri();
            if (uriString != null)
            {
                String imageMimeTypeString = 
                    guessImageMimeTypeString(uriString);
                if (imageMimeTypeString != null)
                {
                    return imageMimeTypeString;
                }
            }
        }
        if (imageData != null)
        {
            try
            {
                return guessImageMimeTypeString(imageData);
            } 
            catch (IOException e)
            {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Tries to detect the format of the image data from the given URI, and 
     * return the corresponding string of the <code>"image/..."</code> MIME 
     * type. This may, for example, be <code>"png"</code> or <code>"gif"</code>
     * or <code>"jpeg"</code> (<b>not</b> <code>"jpg"</code>!)
     *  
     * @param uriString The image data
     * @return The image format string, or <code>null</code> if it can not
     * be detected.
     */
    private static String guessImageMimeTypeString(String uriString)
    {
        try
        {
            URI uri = new URI(uriString);
            if ("data".equalsIgnoreCase(uri.getScheme()))
            {
                String raw = uri.getRawSchemeSpecificPart().toLowerCase();
                return getStringBetween(raw, "image/", ";base64");
            }
        } 
        catch (URISyntaxException e)
        {
            return null;
        }
        int lastDotIndex = uriString.lastIndexOf('.');
        if (lastDotIndex == -1)
        {
            return null;
        }
        String end = uriString.substring(lastDotIndex).toLowerCase();
        if (end.equals("jpg") || end.equals("jpeg"))
        {
            return "jpeg";
        }
        return end;
    }
    
    /**
     * Returns the part of the input string between the given "before" and
     * "after" part, or <code>null</code> if either of the given parts are
     * not contained in the input string, or the "after" part appears 
     * before the "before" part.
     * 
     * @param input The input string
     * @param before The "before" part
     * @param after The "after" part
     * @return The string between "before" and "after"
     */
    private static String getStringBetween(
        String input, String before, String after)
    {
        int beforeIndex = input.indexOf(before);
        if (beforeIndex < 0)
        {
            return null;
        }
        int afterIndex = input.indexOf(after);
        if (afterIndex < beforeIndex)
        {
            return null;
        }
        return input.substring(beforeIndex + before.length(), afterIndex);
    }
    
    
    /**
     * Tries to detect the format of the given image data, and return the
     * corresponding string of the <code>"image/..."</code> MIME type.
     * This may, for example, be <code>"png"</code> or <code>"gif"</code>
     * or <code>"jpeg"</code> (**not** <code>"jpg"</code>!)
     *  
     * @param imageData The image data
     * @return The image format string
     * @throws IOException If the image format can not be detected
     */
    private static String guessImageMimeTypeString(ByteBuffer imageData) 
        throws IOException
    {
        InputStream inputStream = 
            Buffers.createByteBufferInputStream(imageData.slice());
        ImageInputStream imageInputStream = 
            ImageIO.createImageInputStream(inputStream);
        Iterator<ImageReader> imageReaders = 
            ImageIO.getImageReaders(imageInputStream);
        while (imageReaders.hasNext())
        {
            ImageReader imageReader = imageReaders.next();
            return imageReader.getFormatName();
        }
        throw new IOException("Could not detect format of image data");
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfUtils()
    {
        // Private constructor to prevent instantiation
    }
}
