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
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Shader;
import de.javagl.jgltf.model.BinaryGltf;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.Maps;

/**
 * A class for resolving the references of a {@link GlTF}. This class 
 * offers methods to resolve the {@link Buffer}, {@link Image} and 
 * {@link Shader} references that are given via the 
 * {@link Buffer#getUri() buffer URIs}, {@link Image#getUri() image URIs}
 * and {@link Shader#getUri() shader URIs} against a base URI, and place 
 * the loaded data into a {@link GltfData}
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
     * The base URI against which all other URIs will be resolved
     */
    private URI baseUri;
    
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
        Objects.requireNonNull(gltfData, "The gltfData may not be null");
        Objects.requireNonNull(baseUri, "The baseUri may not be null");
        this.gltfData = gltfData;
        this.baseUri = baseUri;
    }
    
    /**
     * Resolve all {@link Buffer#getUri() buffer URIs} against the base URI
     * that was given in the constructor, load the data from these URIs, 
     * and place the results into the current {@link GltfData}
     */
    public void resolveBuffers()
    {
        GlTF gltf = gltfData.getGltf();
        Maps.forEachEntry(gltf.getBuffers(), this::readBuffer);
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
        try
        {
            logger.log(level, "Reading buffer " + id);
            
            URI absoluteUri = IO.makeAbsolute(baseUri, buffer.getUri());
            byte data[] = IO.read(absoluteUri);
            ByteBuffer byteBuffer = Buffers.create(data);
            gltfData.putBufferData(id, byteBuffer);
            
            logger.log(level, "Reading buffer " + id + " DONE");
        }
        catch (IOException e)
        {
            logger.log(level, "Reading buffer " + id + " FAILED: " + 
                e.getMessage());
        }
    }

    /**
     * Resolve all {@link Image#getUri() image URIs} against the base URI
     * that was given in the constructor, load the data from these URIs, 
     * and place the results into the current {@link GltfData}
     */
    public void resolveImages()
    {
        GlTF gltf = gltfData.getGltf();
        Maps.forEachEntry(gltf.getImages(), this::readImage);
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
        try
        {
            logger.log(level, "Reading image " + id);
            
            String uriString = image.getUri();
            URI absoluteUri = IO.makeAbsolute(baseUri, uriString);
            byte[] data = IO.read(absoluteUri);
            gltfData.putImageData(id, Buffers.create(data));
            
            logger.log(level, "Reading image " + id + " DONE");
        }
        catch (IOException e)
        {
            logger.log(level, "Reading image " + id + " FAILED: " + 
                e.getMessage());
        }
    }

    /**
     * Resolve all {@link Shader#getUri() shader URIs} against the base URI
     * that was given in the constructor, load the data from these URIs, 
     * and place the results into the current {@link GltfData}
     */
    public void resolveShaders()
    {
        GlTF gltf = gltfData.getGltf();
        Maps.forEachEntry(gltf.getShaders(), this::readShader);
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
        try
        {
            logger.log(level, "Reading shader " + id);

            String uriString = shader.getUri();
            URI absoluteUri = IO.makeAbsolute(baseUri, uriString);
            byte[] data = IO.read(absoluteUri);
            gltfData.putShaderData(id, Buffers.create(data));
            
            logger.log(level, "Reading shader " + id + " DONE");
        }
        catch (IOException e)
        {
            logger.log(level, "Reading shader " + id + " FAILED: " + 
                e.getMessage());
        }
    }
    
}
