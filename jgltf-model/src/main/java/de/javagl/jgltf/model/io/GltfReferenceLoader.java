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
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for loading the external data of {@link GltfReference} objects
 * that are obtained from a {@link GltfAsset}
 */
public class GltfReferenceLoader
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfReferenceLoader.class.getName());

    /**
     * The log level
     */
    private static final Level level = Level.FINE;
    
    /**
     * Calls {@link #load(GltfReference, Function)} with each 
     * {@link GltfReference} of the given list, resolving the
     * URIs of the references against the given base URI
     * 
     * @param references The {@link GltfReference} objects
     * @param baseUri The base URI that references will be resolved against
     */
    public static void loadAll(
        Iterable<? extends GltfReference> references, URI baseUri)
    {
        Objects.requireNonNull(references, "The references may not be null");
        Objects.requireNonNull(baseUri, "The baseUri may not be null");
        Function<String, InputStream> uriResolver = 
            UriResolvers.createBaseUriResolver(baseUri);
        loadAll(references, uriResolver);
    }
    
    /**
     * Calls {@link #load(GltfReference, Function)} with each 
     * {@link GltfReference} of the given list
     * 
     * @param references The {@link GltfReference} objects
     * @param uriResolver The function for resolving a URI string
     * into an input stream
     */
    public static void loadAll(
        Iterable<? extends GltfReference> references, 
        Function<? super String, ? extends InputStream> uriResolver)
    {
        Objects.requireNonNull(references, "The references may not be null");
        Objects.requireNonNull(uriResolver, "The uriResolver may not be null");

        for (GltfReference reference : references) 
        {
            load(reference, uriResolver);
        }
    }
    
    /**
     * Load the data of the given {@link GltfReference}, and pass the resulting
     * byte buffer to its {@link GltfReference#getTarget() target}. The 
     * {@link GltfReference#getUri() URI} will be resolved into an input
     * stream using the given resolver function. After the data has been
     * read, the stream will be closed.<br>
     * <br>
     * If the data for the reference cannot be loaded, then a warning will
     * be printed.
     * 
     * @param reference The {@link GltfReference}
     * @param uriResolver The function for resolving a URI string
     * into an input stream
     */
    public static void load(GltfReference reference, 
        Function<? super String, ? extends InputStream> uriResolver)
    {
        Objects.requireNonNull(reference, "The reference may not be null");
        Objects.requireNonNull(uriResolver, "The uriResolver may not be null");

        String name = reference.getName();
        String uri = reference.getUri();
        
        try (InputStream inputStream = uriResolver.apply(uri)) 
        {
            if (inputStream == null)
            {
                logger.warning("Could not resolve URI of " + name + ": " + uri);
            }
            else
            {
                load(reference, inputStream);
            }
        }
        catch (IOException e)
        {
            logger.warning("Could not close stream: " + e.getMessage());
        }
    }
    
    /**
     * Load the data of the given {@link GltfReference} from the given 
     * input stream. If an IO error occurs, a warning will be printed.
     * The caller is responsible for closing the given stream.
     * 
     * @param reference The {@link GltfReference}
     * @param inputStream The input stream
     */
    public static void load(GltfReference reference, InputStream inputStream)
    {
        String name = reference.getName();
        Consumer<ByteBuffer> target = reference.getTarget();
        try
        {
            logger.log(level, "Reading " + name);
            byte data[] = IO.readStream(inputStream);
            ByteBuffer byteBuffer = Buffers.create(data);
            logger.log(level, "Reading " + name + " DONE");
            target.accept(byteBuffer);
        }
        catch (IOException e)
        {
            logger.warning("Reading " + name + " FAILED: " + e.getMessage());
        }
        
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfReferenceLoader()
    {
        // Private constructor to prevent instantiation
    }
    
}
