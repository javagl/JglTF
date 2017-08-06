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
package de.javagl.jgltf.model.io.v2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.RawGltfData;
import de.javagl.jgltf.model.io.RawGltfDataReader;

/**
 * A class for reading a glTF 2.0 asset
 */
final class GltfAssetReaderV2
{
    /**
     * The {@link GltfReaderV2} for the JSON part
     */
    private final GltfReaderV2 gltfReader;
    
    /**
     * The {@link RawGltfData} that was read from the input stream 
     */
    private RawGltfData rawGltfData;
    
    /**
     * Creates a new instance
     */
    GltfAssetReaderV2()
    {
        this.gltfReader = new GltfReaderV2();
    }
    
    /**
     * Read the glTF asset from the given input stream. The caller is 
     * responsible for closing the given stream. 
     * 
     * @param inputStream The input stream
     * @return The asset that was read
     * @throws IOException If an IO error occurred
     */
    GltfAssetV2 read(InputStream inputStream) throws IOException
    {
        rawGltfData = RawGltfDataReader.read(inputStream);
        ByteBuffer jsonData = rawGltfData.getJsonData();
        try (InputStream jsonInputStream =
            Buffers.createByteBufferInputStream(jsonData))
        {
            GlTF gltf = gltfReader.read(jsonInputStream);
            return new GltfAssetV2(gltf, rawGltfData.getBinaryData());
        }
    }
}
