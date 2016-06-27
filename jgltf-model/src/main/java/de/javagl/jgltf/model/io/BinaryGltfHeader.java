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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * A class representing the data of a binary glTF header. For details of
 * the fields of this class, see the binary glTF specification.
 */
class BinaryGltfHeader
{
    /**
     * The length of the binary glTF header, in bytes
     */
    static final int BINARY_GLTF_HEADER_LENGTH_IN_BYTES = 20;
    
    /**
     * The string whose 4 bytes are the magic header of a binary glTF.
     */
    private static final String BINARY_GLTF_MAGIC_HEADER = "glTF";
    
    /**
     * The version
     */
    private final int version;
    
    /**
     * The length
     */
    private final int length;
    
    /**
     * The scene length
     */
    private final int sceneLength;
    
    /**
     * The scene format
     */
    private final int sceneFormat;
    
    /**
     * Creates a new header instance
     * 
     * @param version The version
     * @param length The length 
     * @param sceneLength The scene length
     * @param sceneFormat The scene format
     */
    BinaryGltfHeader(int version, int length, int sceneLength, int sceneFormat)
    {
        this.version = version;
        this.length = length;
        this.sceneLength = sceneLength;
        this.sceneFormat = sceneFormat;
    }

    /**
     * Read a binary glTF header from the given data. 
     * 
     * @param data The data to read from
     * @return The binary glTF header
     * @throws IOException If the header could not be read because there
     * was insufficient data, or the data did not start with the magic
     * header bytes.
     */
    static BinaryGltfHeader read(byte data[]) throws IOException
    {
        return read(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN));
    }
    
    /**
     * Read a binary glTF header from the given little-endian data. The 
     * position of the given data will be advanced by the binary glTF 
     * header size.
     * 
     * @param data The data to read from
     * @return The binary glTF header
     * @throws IOException If the header could not be read because there
     * was insufficient data, or the data did not start with the magic
     * header bytes.
     */
    static BinaryGltfHeader read(ByteBuffer data) throws IOException
    {
        if (data.remaining() < BINARY_GLTF_HEADER_LENGTH_IN_BYTES)
        {
            throw new IOException(
                "Expected " + BINARY_GLTF_HEADER_LENGTH_IN_BYTES + 
                " bytes for header, but only found " + data.remaining());
        }
        if (!startsWithBinaryGltfMagicHeader(data))
        {
            byte temp[] = new byte[4];
            data.slice().get(temp);
            throw new IOException(
                "Expected \"" + BINARY_GLTF_MAGIC_HEADER + "\" header, but " + 
                "found " + new String(temp) + ", as ASCII: " + 
                Arrays.toString(temp));
        }
        IntBuffer intData = data.asIntBuffer();
        int version = intData.get(1);
        int length = intData.get(2);
        int sceneLength = intData.get(3);
        int sceneFormat = intData.get(4);
        return new BinaryGltfHeader(version, length, sceneLength, sceneFormat);
    }
    
    /**
     * Write the given binary glTF header into the given little-endian buffer. 
     * The position of the given data will be advanced by the binary glTF 
     * header size.
     *  
     * @param binaryGltfHeader The header to write
     * @param data The data to write to 
     * @throws IOException If there was not enough space remaining in the
     * given target data buffer
     */
    static void write(BinaryGltfHeader binaryGltfHeader, ByteBuffer data) 
        throws IOException
    {
        if (data.remaining() < BINARY_GLTF_HEADER_LENGTH_IN_BYTES)
        {
            throw new IOException(
                "Required " + BINARY_GLTF_HEADER_LENGTH_IN_BYTES + 
                " bytes for header, but only have " + data.remaining());
        }
        data.put((byte) 'g');
        data.put((byte) 'l');
        data.put((byte) 'T');
        data.put((byte) 'F');
        
        IntBuffer intData = data.asIntBuffer(); // Position is 1 already
        intData.put(binaryGltfHeader.getVersion());
        intData.put(binaryGltfHeader.getLength());
        intData.put(binaryGltfHeader.getSceneLength());
        intData.put(binaryGltfHeader.getSceneFormat());
    }

    /**
     * Returns whether the given data starts with the bytes of 
     * <code>"glTF"</code>, the binary glTF magic header
     * 
     * @param data The data
     * @return Whether the data starts with the magic header
     */
    private static boolean startsWithBinaryGltfMagicHeader(ByteBuffer data)
    {
        if (data.remaining() < 4)
        {
            return false;
        }
        int position = data.position();
        if (data.get(position + 0) != 'g')
        {
            return false;
        }
        if (data.get(position + 1) != 'l')
        {
            return false;
        }
        if (data.get(position + 2) != 'T')
        {
            return false;
        }
        if (data.get(position + 3) != 'F')
        {
            return false;
        }
        return true;
    }
    
    /**
     * Returns the version
     * 
     * @return The version
     */
    int getVersion()
    {
        return version;
    }
    
    /**
     * Returns the length
     * 
     * @return The length
     */
    int getLength()
    {
        return length;
    }
    
    /**
     * Returns the scene length
     * 
     * @return The scene length
     */
    int getSceneLength()
    {
        return sceneLength;
    }
    
    /**
     * Returns the scene format 
     * 
     * @return The scene format
     */
    int getSceneFormat()
    {
        return sceneFormat;
    }
}
