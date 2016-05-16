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
package de.javagl.jgltf.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 * IO utility methods
 */
class IO
{
    /**
     * Convert the given URI string into an absolute URI, resolving it
     * against the given base URI if necessary
     * 
     * @param baseUri The base URI
     * @param uriString The URI string
     * @return The absolute URI
     * @throws IOException If the URI string is not valid
     */
    static URI makeAbsolute(URI baseUri, String uriString) throws IOException
    {
        try
        {
            URI uri = new URI(uriString);
            if (uri.isAbsolute())
            {
                return uri;
            }
            return baseUri.resolve(uriString);
        }
        catch (URISyntaxException e)
        {
            throw new IOException("Invalid URI string: "+uriString, e);
        }
    }
    
    /**
     * Returns the URI describing the parent of the given URI. If the 
     * given URI describes a file, this will return the URI of the 
     * directory. If the given URI describes a directory, this will
     * return the URI of the parent directory
     *  
     * @param uri The URI
     * @return The parent URI
     */
    static URI getParent(URI uri)
    {
        if (uri.getPath().endsWith("/"))
        {
            return uri.resolve("..");        
        }
        return uri.resolve(".");
    }

    /**
     * Read the data from the given URI as a byte array. The data may either
     * be an actual URI, or a data URI with base64 encoded data.
     * 
     * @param uri The URI
     * @return The byte array
     * @throws IOException If an IO error occurs
     */
    static byte[] read(URI uri) throws IOException
    {
        if ("data".equalsIgnoreCase(uri.getScheme()))
        {
            return readDataUri(uri.toString());
        }
        try (InputStream inputStream = uri.toURL().openStream())
        {
            byte data[] = readStream(inputStream);
            return data;
        }
    }

    /**
     * Read the base 64 encoded data from the given data URI string.
     * The data is assumed to start after the <code>base64,</code> part
     * of the URI string, which must have the form 
     * <code>data:...;base64,...</code>
     * 
     * @param uriString The URI string
     * @return The data
     */
    private static byte[] readDataUri(String uriString)
    {
        String encoding = "base64,";
        int contentStartIndex = uriString.indexOf(encoding) + encoding.length();
        byte data[] = Base64.getDecoder().decode(
            uriString.substring(contentStartIndex));
        return data;
    }
    
    /**
     * Reads the data from the given inputStream and returns it as
     * a byte array. The caller is responsible for closing the stream.
     * 
     * @param inputStream The input stream to read
     * @return The data from the inputStream
     * @throws IOException If an IO error occurs
     */
    private static byte[] readStream(InputStream inputStream) throws IOException
    {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
            byte buffer[] = new byte[8192];
            while (true)
            {
                int read = inputStream.read(buffer);
                if (read == -1)
                {
                    break;
                }
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return baos.toByteArray();
        }
    }
    
    /**
     * Read the data from the given URI as a direct byte buffer with
     * native byte order
     * 
     * @param uri The URI
     * @return The byte buffer
     * @throws IOException If an IO error occurs
     */
    static ByteBuffer readAsByteBuffer(
        URI uri) throws IOException 
    {
        byte data[] = read(uri);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.put(data);
        byteBuffer.position(0);
        return byteBuffer;
    }
    
    /**
     * Read the data from the given URI as a string (using the platform
     * default character set)
     * 
     * @param uri The URI
     * @return The string
     * @throws IOException If an IO error occurs
     */
    static String readAsString(URI uri) throws IOException
    {
        return new String(read(uri));
    }
    
    /**
     * Read the data from the given URI as a BufferedImage
     * 
     * @param uri The URI
     * @return The buffered image
     * @throws IOException If an IO error occurs
     */
    static BufferedImage readAsBufferedImage(
        URI uri) throws IOException
    {
        try (InputStream inputStream = new ByteArrayInputStream(read(uri)))
        {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            return bufferedImage;
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private IO()
    {
        // Private constructor to prevent instantiation
    }
    
}