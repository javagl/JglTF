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
package de.javagl.jgltf.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Methods for creating functions that resolve URI to input streams
 */
public class UriResolvers
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(UriResolvers.class.getName());

    /**
     * Creates a function that resolves URI strings against the given 
     * base URI, and returns an input stream for reading the data from 
     * the resulting URI.<br>
     * <br>
     * The given URI strings may either be standard URI or data URI.<br>
     * <br>
     * If the returned function cannot open a stream, then it will print a
     * warning and return <code>null</code>.
     * 
     * @param baseUri The base URI to resolve against
     * @return The function
     */
    public static Function<String, InputStream> createBaseUriResolver(
        URI baseUri)
    {
        Objects.requireNonNull(baseUri, "The baseUri may not be null");
        return new Function<String, InputStream>()
        {
            @Override
            public InputStream apply(String uriString)
            {
                try
                {
                    URI absoluteUri = IO.makeAbsolute(baseUri, uriString);
                    return IO.createInputStream(absoluteUri);
                }
                catch (IOException e)
                {
                    logger.warning("Could not open input stream for URI "
                        + uriString + ":  " + e.getMessage());
                    return null;
                }
            }
        };
    }
    
    /**
     * Create a function that maps a string to the input stream of a resource
     * of the given class.
     * 
     * @param c The class
     * @return The resolving function
     */
    public static Function<String, InputStream> createResourceUriResolver(
        Class<?> c)
    {
        Objects.requireNonNull(c, "The class may not be null");
        return new Function<String, InputStream>()
        {
            @Override
            public InputStream apply(String uriString)
            {
                InputStream inputStream = 
                    c.getResourceAsStream("/" + uriString);
                if (inputStream == null)
                {
                    logger.warning(
                        "Could not obtain input stream for resource "
                        + "with URI " + uriString);
                }
                return inputStream;
            }
        };
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private UriResolvers()
    {
        // Private constructor to prevent instantiation
    }

}
