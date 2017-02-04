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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.model.BinaryGltf;
import de.javagl.jgltf.model.GltfData;

/**
 * A class for writing {@link GltfData} as a binary glTF. 
 */
public class BinaryGltfDataWriter
{
    /**
     * The binary glTF version that is written by this writer
     */
    private static final int BINARY_GLTF_VERSION = 1;
    
    /**
     * The writer for the JSON part of the {@link GlTF}
     */
    private final GltfWriter gltfWriter;
    
    /**
     * Default constructor
     */
    public BinaryGltfDataWriter()
    {
        this.gltfWriter = new GltfWriter();
        this.gltfWriter.setIndenting(false);
    }
    
    /**
     * Write the given {@link GltfData} to a file with the given name.<br>
     * <br>
     * This method assumes that the given {@link GltfData} is actually a 
     * "binary glTF" data: It expects the byte buffer of a {@link Buffer} with 
     * the ID <code>"binary_glTF"</code> to be present in the given 
     * {@link GltfData}, and only this data will be written. If no such buffer 
     * data can be found, then an <code>IOException</code> will be thrown.
     * 
     * @param gltfData The {@link GltfData}
     * @param fileName The file name for the binary glTF file
     * @throws IOException If an IO error occurred
     */
    public void writeBinaryGltfData(GltfData gltfData, String fileName) 
        throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(fileName))
        {
            writeBinaryGltfData(gltfData, outputStream);
        }
    }
    
    /**
     * Write the given {@link GltfData} to the given output stream. The caller
     * is responsible for closing the given stream.<br>
     * <br>
     * This method assumes that the given {@link GltfData} is actually a 
     * "binary glTF" data: It expects the byte buffer of a {@link Buffer} with 
     * the ID <code>"binary_glTF"</code> to be present in the given 
     * {@link GltfData}, and only this data will be written. If no such buffer 
     * data can be found, then an <code>IOException</code> will be thrown.
     * 
     * @param gltfData The {@link GltfData}
     * @param outputStream The output stream
     * @throws IOException If an IO error occurred
     */
    public void writeBinaryGltfData(
        GltfData gltfData, OutputStream outputStream) 
        throws IOException
    {
        // Obtain the binary glTF buffer
        ByteBuffer binaryGltfBuffer = gltfData.getBufferData(
            BinaryGltf.getBinaryGltfBufferId());
        if (binaryGltfBuffer == null)
        {
            throw new IOException(
                "The GltfData does not have a buffer " + 
                "with the binary glTF buffer ID (" + 
                BinaryGltf.getBinaryGltfBufferId() + ")");
        }

        // Write the JSON representation of the glTF, creating the scene data
        GlTF gltf = gltfData.getGltf();
        byte sceneData[];
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
            gltfWriter.writeGltf(gltf, baos);
            sceneData = baos.toByteArray();
        }
        
        // Create the header data and fill it with a BinaryGltfHeader
        byte headerData[] = new byte[20];
        int version = BINARY_GLTF_VERSION;
        int sceneLength = sceneData.length;
        int length = headerData.length + sceneLength + 
            binaryGltfBuffer.capacity();
        int sceneFormat = BinaryGltfDatas.SCENE_FORMAT_JSON; 
        BinaryGltfHeader binaryGltfHeader = 
            new BinaryGltfHeader(version, length, sceneLength, sceneFormat);
        BinaryGltfHeader.write(binaryGltfHeader, 
            ByteBuffer.wrap(headerData).order(ByteOrder.LITTLE_ENDIAN));
        
        // Finally, write the header, scene and binary glTF buffer 
        @SuppressWarnings("resource")
        WritableByteChannel writableByteChannel = 
            Channels.newChannel(outputStream);
        writableByteChannel.write(ByteBuffer.wrap(headerData));
        writableByteChannel.write(ByteBuffer.wrap(sceneData));
        writableByteChannel.write(binaryGltfBuffer.slice());
    }
    
}
