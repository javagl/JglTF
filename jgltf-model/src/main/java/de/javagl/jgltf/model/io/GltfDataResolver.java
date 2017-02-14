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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.BinaryGltf;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.Optionals;

/**
 * A class for resolving the references of a {@link GlTF}. This class 
 * offers methods to resolve the {@link Buffer}, {@link Image} and 
 * {@link Shader} references that are given via the 
 * {@link Buffer#getUri() buffer URIs}, {@link Image#getUri() image URIs}
 * and {@link Shader#getUri() shader URIs}, and place the loaded data 
 * into a {@link GltfData}
 */
public class GltfDataResolver
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfDataReader.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.FINE;

    /**
     * The {@link GltfData} that will be filled with the loaded data
     */
    private final GltfData gltfData;

    /**
     * The function that will resolve {@link Buffer#getUri() buffer URI},
     * {@link Image#getUri() image URI} and {@link Shader#getUri() shader URI}
     * strings, and provide input streams for reading the data from these
     * URIs.
     */
    private final Function<? super String, ? extends InputStream> 
        uriStringResolver; 
    
    /**
     * Creates a new glTF data resolver
     * 
     * @param gltfData The {@link GltfData} that will be filled with the
     * loaded data
     * @param baseUri The base URI against which all other URIs will be 
     * resolved
     */
    public GltfDataResolver(GltfData gltfData, URI baseUri)
    {
        this(gltfData, createBaseUriResolver(baseUri));
    }

    /**
     * Creates a new glTF data resolver
     * 
     * @param gltfData The {@link GltfData} that will be filled with the
     * loaded data
     * @param uriStringResolver The function that will be used to resolve
     * {@link Buffer#getUri() buffer URI}, {@link Image#getUri() image URI} 
     * and {@link Shader#getUri() shader URI} strings, and provide input 
     * streams for reading the data from these URIs. If the function 
     * returns <code>null</code>, then it means that the URI string can not
     * be resolved, and a warning will be printed for this URI. 
     */
    public GltfDataResolver(GltfData gltfData, 
        Function<? super String, ? extends InputStream> uriStringResolver)
    {
        Objects.requireNonNull(gltfData, "The gltfData may not be null");
        Objects.requireNonNull(uriStringResolver, 
            "The uriStringResolver may not be null");
        this.gltfData = gltfData;
        this.uriStringResolver = uriStringResolver;
    }
    
    /**
     * Creates a function that resolves URI strings against the given base
     * URI, and returns an input stream for reading the data from the
     * resulting URI
     * 
     * @param baseUri The base URI to resolve against
     * @return The function
     */
    private static Function<? super String, ? extends InputStream> 
        createBaseUriResolver(URI baseUri)
    {
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
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
    
    /**
     * Resolve all {@link Buffer#getUri() buffer URIs}, load the data from 
     * these URIs, and place the results into the current {@link GltfData}
     */
    public void resolveBuffers()
    {
        GlTF gltf = gltfData.getGltf();
        Optionals.of(gltf.getBuffers()).forEach(this::readBuffer);
    }
    
    /**
     * Read the data for the specified {@link Buffer} (if the given ID is 
     * not the binary glTF buffer ID, <code>"binary_glTF"</code>) and store 
     * it in the current {@link GltfData}. 
     *  
     * @param id The ID of the {@link Buffer}
     * @param buffer The {@link Buffer}
     */
    private void readBuffer(String id, Buffer buffer)
    {
        if (BinaryGltf.isBinaryGltfBufferId(id))
        {
            return;
        }
        read("buffer " + id, buffer.getUri(), 
            byteBuffer -> gltfData.putBufferData(id, byteBuffer));
    }
    

    /**
     * Resolve all {@link Image#getUri() image URIs}, load the data from 
     * these URIs, and place the results into the current {@link GltfData}
     */
    public void resolveImages()
    {
        GlTF gltf = gltfData.getGltf();
        Optionals.of(gltf.getImages()).forEach(this::readImage);
    }
    
    /**
     * Read the data for the specified {@link Image} from its 
     * {@link Image#getUri() URI}, and store it in the current 
     * {@link GltfData}.
     *  
     * @param id The ID of the {@link Image}
     * @param image The {@link Image}
     */
    private void readImage(String id, Image image)
    {
        if (BinaryGltf.hasBinaryGltfExtension(image))
        {
            return;
        }
        read("image " + id, image.getUri(), 
            byteBuffer -> gltfData.putImageData(id, byteBuffer));
    }

    /**
     * Resolve all {@link Shader#getUri() shader URIs}, load the data from 
     * these URIs, and place the results into the current {@link GltfData}
     */
    public void resolveShaders()
    {
        GlTF gltf = gltfData.getGltf();
        Optionals.of(gltf.getShaders()).forEach(this::readShader);
    }
    
    /**
     * Read the data for the specified {@link Shader} and store it in the 
     * current {@link GltfData}. 
     *  
     * @param id The ID of the {@link Shader}
     * @param shader The {@link Shader}
     */
    private void readShader(String id, Shader shader)
    {
        if (BinaryGltf.hasBinaryGltfExtension(shader))
        {
            return;
        }
        read("shader " + id, shader.getUri(), 
            byteBuffer -> gltfData.putShaderData(id, byteBuffer));
    }
    
    /**
     * Read the data from the given URI into a byte buffer, and pass
     * this buffer to the given consumer.
     * 
     * @param name The name of the resource to read. Only used for logging.
     * @param uri The URI to read from
     * @param consumer The consumer for the resulting byte buffer
     */
    private void read(String name, 
        String uri, Consumer<ByteBuffer> consumer)
    {
        InputStream inputStream = null;
        try
        {
            logger.log(level, "Reading " + name);
            
            inputStream = uriStringResolver.apply(uri);
            if (inputStream != null)
            {
                byte data[] = IO.readStream(inputStream);
                ByteBuffer byteBuffer = Buffers.create(data);
                consumer.accept(byteBuffer);
                logger.log(level, "Reading " + name + " DONE");
            }
            else
            {
                throw new IOException(
                    "Could not resolve URI of " + name + ": " + uri);
            }
        }
        catch (IOException e)
        {
            logger.warning("Reading " + name + " FAILED: " + 
                e.getMessage());
        }
        finally
        {
            tryClose(inputStream);
        }
    }
    
    /**
     * Try to close the given closeable, printing a warning when an
     * IO exception is thrown
     * 
     * @param closeable The closeable
     */
    private static void tryClose(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (IOException e)
            {
                logger.warning("Could not close " + closeable + ": " + 
                    e.getMessage());
            }
        }
    }
    
}
