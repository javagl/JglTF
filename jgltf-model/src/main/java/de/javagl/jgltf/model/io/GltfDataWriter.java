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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.Maps;

/**
 * A class for writing {@link GltfData}. <br>
 * <br>
 * This class will write the {@link GltfData} as-it-is. If the {@link GltfData} 
 * contains embedded data (as data URIs), then they will be written as such. 
 * If the {@link GltfData} contains URIs for {@link Buffer}, {@link Image} 
 * or {@link Shader} objects, then these {@link Buffer#getUri() buffer URIs},
 * {@link Image#getUri() image URIs} and {@link Shader#getUri() shader URIs}
 * will be interpreted as file names, and the data will be written into a 
 * file with the respective name.
 */
public class GltfDataWriter
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfDataWriter.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.FINE;
    
    /**
     * The {@link GltfWriter} for the JSON part
     */
    private final GltfWriter gltfWriter;

    /**
     * Default constructor
     */
    public GltfDataWriter()
    {
        this.gltfWriter = new GltfWriter();
    }
    
    /**
     * Write the given {@link GltfData} to a file with the given name. 
     * 
     * @param gltfData The {@link GltfData}
     * @param fileName The file name for the JSON file
     * @throws IOException If an IO error occurred
     */
    public void writeGltfData(GltfData gltfData, String fileName) 
        throws IOException
    {
        GlTF gltf = gltfData.getGltf();

        try (OutputStream outputStream = new FileOutputStream(fileName))
        {
            gltfWriter.writeGltf(gltf, outputStream);
        }
        String path = Paths.get(fileName).getParent().toString();
        Maps.forEachEntry(gltf.getBuffers(), (id, buffer) -> 
            writeBuffer(gltfData, id, buffer, path));
        Maps.forEachEntry(gltf.getImages(), (id, image) -> 
            writeImage(gltfData, id, image, path));
        Maps.forEachEntry(gltf.getShaders(), (id, shader) -> 
            writeShader(gltfData, id, shader, path));
    }

    /**
     * Write the {@link Buffer} with the given ID from the given 
     * {@link GltfData} to a file that is created by appending
     * the {@link Buffer#getUri() buffer URI} to the given path,
     * if this URI is not a data URI 
     * 
     * @param gltfData The {@link GltfData} 
     * @param id The ID
     * @param buffer The {@link Buffer}
     * @param path The path to write to
     */ 
    private static void writeBuffer(
        GltfData gltfData, String id, Buffer buffer, String path)
    {
        String uriString = buffer.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        String name = "buffer " + id;
        ByteBuffer data = gltfData.getBufferData(id);
        String fileName = Paths.get(path, uriString).toString();
        write(name, data, fileName);
    }

    /**
     * Write the {@link Image} with the given ID from the given 
     * {@link GltfData} to a file that is created by appending
     * the {@link Image#getUri() buffer URI} to the given path,
     * if this URI is not a data URI 
     * 
     * @param gltfData The {@link GltfData} 
     * @param id The ID
     * @param image The {@link Image}
     * @param path The path to write to
     */ 
    private static void writeImage(
        GltfData gltfData, String id, Image image, String path)
    {
        String uriString = image.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        String name = "image " + id;
        ByteBuffer data = gltfData.getImageData(id);
        String fileName = Paths.get(path, uriString).toString();
        write(name, data, fileName);
    }
    
    /**
     * Write the {@link Shader} with the given ID from the given 
     * {@link GltfData} to a file that is created by appending
     * the {@link Shader#getUri() buffer URI} to the given path,
     * if this URI is not a data URI 
     * 
     * @param gltfData The {@link GltfData} 
     * @param id The ID
     * @param shader The {@link Shader}
     * @param path The path to write to
     */ 
    private static void writeShader(
        GltfData gltfData, String id, Shader shader, String path)
    {
        String uriString = shader.getUri();
        if (IO.isDataUriString(uriString))
        {
            return;
        }
        String name = "shader " + id;
        ByteBuffer data = gltfData.getShaderData(id);
        String fileName = Paths.get(path, uriString).toString();
        write(name, data, fileName);
    }
    
    
    /**
     * Write the given byte buffer to a file with the given name. If the
     * given data is <code>null</code>, then a warning will be printed
     * and nothing will be written.
     * 
     * @param name The name of the written data. Only used for logging.
     * @param data The data to write
     * @param fileName The file name
     */
    private static void write(String name, ByteBuffer data, String fileName)
    {
        if (data == null)
        {
            logger.warning("Writing " + name + " FAILED: No  data found");
            return;
        }
        try (@SuppressWarnings("resource")
            WritableByteChannel writableByteChannel = 
            Channels.newChannel(new FileOutputStream(fileName)))
        {
            logger.log(level, "Writing " + name + " to " + fileName);
            writableByteChannel.write(data.slice());
            logger.log(level, "Writing " + name + " DONE");
        }
        catch (IOException e)
        {
            logger.warning("Writing " + name + " FAILED: " + e.getMessage());
        }
    }
    
    
}
