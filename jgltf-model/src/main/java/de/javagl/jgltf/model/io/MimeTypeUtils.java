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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Utility methods to detect the MIME type from data URLs or image data
 * @author User
 *
 */
class MimeTypeUtils
{
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
    static String guessImageMimeTypeString(String uriString)
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
        String end = uriString.substring(lastDotIndex + 1).toLowerCase();
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
    static String guessImageMimeTypeString(ByteBuffer imageData) 
        throws IOException
    {
        ImageReader imageReader = null;
        try
        {
            imageReader = findImageReader(imageData);
            return imageReader.getFormatName();
        }
        finally
        {
            if (imageReader != null)
            {
                imageReader.dispose();
            }
        }
        
    }
    
    /**
     * Tries to find an <code>ImageReader</code> that is capable of reading
     * the given image data. The returned image reader will be initialized
     * by passing an ImageInputStream that is created from the given data
     * to its <code>setInput</code> method. The caller is responsible for 
     * disposing the returned image reader.
     *  
     * @param imageData The image data
     * @return The image reader
     * @throws IOException If no matching image reader can be found
     */
    @SuppressWarnings("resource")
    private static ImageReader findImageReader(ByteBuffer imageData) 
        throws IOException
    {
        InputStream inputStream = 
            Buffers.createByteBufferInputStream(imageData.slice());
        ImageInputStream imageInputStream = 
            ImageIO.createImageInputStream(inputStream);
        Iterator<ImageReader> imageReaders = 
            ImageIO.getImageReaders(imageInputStream);
        if (imageReaders.hasNext())
        {
            ImageReader imageReader = imageReaders.next();
            imageReader.setInput(imageInputStream);
            return imageReader;
        }
        throw new IOException("Could not find ImageReader for image data");
    }

}
