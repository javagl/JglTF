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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.GltfData;

/**
 * A class for reading binary glTF into a {@link GltfData}. This class 
 * differs from a {@link GltfDataReader} in that it can read the data 
 * from a file or an input stream, and not only from a URI. Therefore,
 * in contrast to a {@link GltfDataReader}, this class assumes that all 
 * data is contained in a single input, and that {@link Buffer}, 
 * {@link Image} and {@link Shader} instances do not contain any URIs 
 * that have to be resolved. (Although it would still be possible to
 * resolve the references of the resulting {@link GltfData}, using
 * a {@link GltfDataResolver} and a given base URI).
 */
public class BinaryGltfDataReader
{
    /**
     * The {@link GltfReader} for the JSON part of the binary glTF
     */
    private final GltfReader gltfReader;
    
    /**
     * Default constructor 
     */
    public BinaryGltfDataReader()
    {
        this.gltfReader = new GltfReader(); 
    }

    /**
     * Read the binary glTF data from the file with the given name
     * 
     * @param fileName The file name
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public GltfData readBinaryGltfData(String fileName) 
        throws IOException
    {
        try (InputStream inputStream = new FileInputStream(fileName))
        {
            return readBinaryGltfData(inputStream);
        }
    }

    /**
     * Read the binary glTF data from the given input stream. The caller is
     * responsible for closing the given stream.
     * 
     * @param inputStream The stream to read from
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public GltfData readBinaryGltfData(InputStream inputStream) 
        throws IOException
    {
        // First, read only the header data, to determine the length (i.e.
        // the total number of bytes to read from the stream)
        int headerLength = BinaryGltfHeader.BINARY_GLTF_HEADER_LENGTH_IN_BYTES;
        byte headerData[] = new byte[headerLength];
        IO.read(inputStream, headerData);
        BinaryGltfHeader binaryGltfHeader = BinaryGltfHeader.read(headerData);
        
        // Read the required number of bytes
        int length = binaryGltfHeader.getLength();
        byte data[] = Arrays.copyOf(headerData, length);
        IO.read(inputStream, data, headerLength, length - headerLength);

        // Create the binary glTF data, and resolve the buffer views 
        GltfData gltfData = BinaryGltfDatas.create(data, gltfReader);
        BufferViews.createBufferViewByteBuffers(gltfData);
        BinaryGltfDatas.createBinaryImageDatas(gltfData);
        BinaryGltfDatas.createBinaryShaderDatas(gltfData);
        return gltfData;
    }
    
}
