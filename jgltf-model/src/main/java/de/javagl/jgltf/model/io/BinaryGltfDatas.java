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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.BinaryGltf;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.Optionals;

/**
 * Methods related to binary {@link GltfData} instances
 */
public class BinaryGltfDatas
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BinaryGltfDatas.class.getName());
    
    /**
     * The constant indicating JSON scene format
     */
    static final int SCENE_FORMAT_JSON = 0;

    /**
     * The highest version of binary glTF data that is supported
     */
    private static final int MAX_SUPPORTED_BINARY_GLTF_VERSION = 1;
    
    /**
     * Create the {@link GltfData} from the given data that was read from
     * a binary glTF file
     * 
     * @param data The actual data
     * @param gltfReader The {@link GltfReader}
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public static GltfData create(byte data[], GltfReader gltfReader) 
        throws IOException
    {
        Objects.requireNonNull(gltfReader, "The gltfReader may not be null");
        
        // Read the binary glTF header, and perform some sanity checks
        BinaryGltfHeader binaryGltfHeader = BinaryGltfHeader.read(data);
        int version = binaryGltfHeader.getVersion();
        int length = binaryGltfHeader.getLength();
        int sceneLength = binaryGltfHeader.getSceneLength();
        int sceneFormat = binaryGltfHeader.getSceneFormat();
        if (version > MAX_SUPPORTED_BINARY_GLTF_VERSION)
        {
            logger.warning("Found binary glTF version " + version + ", only " + 
                MAX_SUPPORTED_BINARY_GLTF_VERSION + " is supported");
        }
        if (length != data.length)
        {
            throw new IOException(
                "The length field indicated " + length + " bytes, " + 
                "but only found " + data.length + " have been read");
        }
        if (sceneFormat != SCENE_FORMAT_JSON)
        {
            throw new IOException(
                "The scene format is " + sceneFormat + ", but only " + 
                "JSON (" + SCENE_FORMAT_JSON + ") is supported");
        }

        // Read the glTF JSON from the input data
        GltfData gltfData;
        int headerLength = BinaryGltfHeader.BINARY_GLTF_HEADER_LENGTH_IN_BYTES;
        try (InputStream sceneInputStream = 
            new ByteArrayInputStream(data, headerLength, sceneLength))
        {
            GlTF gltf = gltfReader.readGltf(sceneInputStream);
            gltfData = new GltfData(gltf);
        }
        
        // Create the main binary glTF buffer from the input data
        int binaryDataOffset = sceneLength + headerLength;
        int binaryDataLength = length - sceneLength - headerLength;
        ByteBuffer binaryDataBuffer =
            Buffers.create(data, binaryDataOffset, binaryDataLength);
        gltfData.putBufferData(
            BinaryGltf.getBinaryGltfBufferId(), binaryDataBuffer);
        
        return gltfData;
    }
    
    /**
     * Extract the JSON part with the scene description of the given binary 
     * glTF data
     * 
     * @param data The binary glTF data
     * @return The JSON string
     * @throws IOException If the JSON string could not be extracted
     */
    public static String extractJsonString(byte data[]) 
        throws IOException
    {
        // Read the binary glTF header, and perform some sanity checks
        BinaryGltfHeader binaryGltfHeader = BinaryGltfHeader.read(data);
        int length = binaryGltfHeader.getLength();
        int sceneLength = binaryGltfHeader.getSceneLength();
        int sceneFormat = binaryGltfHeader.getSceneFormat();
        if (length != data.length)
        {
            throw new IOException(
                "The length field indicated " + length + " bytes, " + 
                "but only found " + data.length + " have been read");
        }
        if (sceneFormat != SCENE_FORMAT_JSON)
        {
            throw new IOException(
                "The scene format is " + sceneFormat + ", but only " + 
                "JSON (" + SCENE_FORMAT_JSON + ") is supported");
        }
        int headerLength = BinaryGltfHeader.BINARY_GLTF_HEADER_LENGTH_IN_BYTES;
        String jsonString = new String(data, headerLength, sceneLength);
        return jsonString;
    }
    
    /**
     * Create the mapping from IDs to byte buffers containing the data for 
     * all {@link Image}s in the given {@link GltfData} that have the 
     * binary glTF extension object.
     * 
     * @param gltfData The {@link GltfData}
     */
    public static void createBinaryImageDatas(GltfData gltfData) 
    {
        GlTF gltf = gltfData.getGltf();
        Optionals.of(gltf.getImages()).forEach(
            (id, image) -> createBinaryImageData(gltfData, id, image));
    }
    
    /**
     * Create the mapping from the given ID to the byte buffer containing the 
     * data for the specified {@link Image} in the given {@link GltfData}, if
     * the {@link Image} has the binary glTF extension object
     * 
     * @param gltfData The {@link GltfData}
     * @param id The {@link Image} ID
     * @param image The {@link Image}
     */
    private static void createBinaryImageData(
        GltfData gltfData, String id, Image image)
    {
        if (!BinaryGltf.hasBinaryGltfExtension(image))
        {
            return;
        }
        String bufferViewId = 
            BinaryGltf.getBinaryGltfBufferViewId(image);
        ByteBuffer bufferViewData =
            gltfData.getBufferViewData(bufferViewId);
        gltfData.putImageData(id, bufferViewData.slice());
    }
    
    /**
     * Create the mapping from IDs to byte buffers containing the data for 
     * all {@link Shader}s in the given {@link GltfData} that have the 
     * binary glTF extension object
     * 
     * @param gltfData The {@link GltfData}
     */
    public static void createBinaryShaderDatas(GltfData gltfData) 
    {
        GlTF gltf = gltfData.getGltf();
        Optionals.of(gltf.getShaders()).forEach(
            (id, shader) -> createBinaryShaderData(gltfData, id, shader));
    }
    
    /**
     * Create the mapping from the given ID to the byte buffer containing the 
     * data for the specified {@link Shader} in the given {@link GltfData}, if
     * the {@link Shader} has the binary glTF extension object
     * 
     * @param gltfData The {@link GltfData}
     * @param id The {@link Shader} ID
     * @param shader The {@link Shader}
     */
    private static void createBinaryShaderData(
        GltfData gltfData, String id, Shader shader)
    {
        if (!BinaryGltf.hasBinaryGltfExtension(shader))
        {
            return;
        }
        String bufferViewId = 
            BinaryGltf.getBinaryGltfBufferViewId(shader);
        ByteBuffer bufferViewData =
            gltfData.getBufferViewData(bufferViewId);
        gltfData.putShaderData(id, bufferViewData.slice());
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private BinaryGltfDatas()
    {
        // Private constructor to prevent instantiation
    }

}
