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
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;

/**
 * A class for reading a glTF asset in a version-agnostic form. It allows
 * reading the asset data from an input stream. After the asset has been
 * read, the {@link #getMajorVersion() major version} of the asset may
 * be queried. Depending on the version, the asset may be obtained as a
 * {@link #getAsGltfAssetV1() glTF 1.0 asset} or a 
 * {@link #getAsGltfAssetV2() glTF 2.0 asset}.  
 */
final class GltfAssetReader
{
    /**
     * The {@link GltfReader} for the JSON part
     */
    private final GltfReader gltfReader;
    
    /**
     * The {@link RawGltfData} that was read from the input stream 
     */
    private RawGltfData rawGltfData;
    
    /**
     * Creates a new instance
     */
    GltfAssetReader()
    {
        this.gltfReader = new GltfReader();
    }
    
    /**
     * Set the given consumer to receive {@link JsonError}s that may 
     * occur when a glTF is read
     * 
     * @param jsonErrorConsumer The {@link JsonError} consumer
     */
    public void setJsonErrorConsumer(
        Consumer<? super JsonError> jsonErrorConsumer)
    {
        gltfReader.setJsonErrorConsumer(jsonErrorConsumer);
    }
    
    /**
     * Read the glTF asset from the given input stream. The caller is 
     * responsible for closing the given stream. After this method
     * has been called, the version of the glTF may be obtained with
     * {@link #getMajorVersion()}, and the actual asset may be obtained
     * with {@link #getAsGltfAssetV1()} or {@link #getAsGltfAssetV2()}.
     * 
     * @param inputStream The input stream
     * @throws IOException If an IO error occurred
     */
    void read(InputStream inputStream) throws IOException
    {
        rawGltfData = RawGltfDataReader.read(inputStream);
        ByteBuffer jsonData = rawGltfData.getJsonData();
        try (InputStream jsonInputStream =
            Buffers.createByteBufferInputStream(jsonData))
        {
            gltfReader.read(jsonInputStream);
        }
    }
    
    /**
     * Returns the major version of the glTF, or 0 of no glTF was read yet.
     * 
     * @return The major version number
     */
    int getMajorVersion()
    {
        return gltfReader.getMajorVersion();
    }
    
    /**
     * Returns the {@link GltfAssetV1} with a 
     * {@link de.javagl.jgltf.impl.v1.GlTF version 1.0 glTF}, 
     * or <code>null</code> if no asset has been read yet. 
     * 
     * @return The {@link GltfAssetV1}
     */
    GltfAssetV1 getAsGltfAssetV1()
    {
        de.javagl.jgltf.impl.v1.GlTF gltf = gltfReader.getAsGltfV1();
        if (gltf == null)
        {
            return null;
        }
        return new GltfAssetV1(gltf, rawGltfData.getBinaryData());
    }
    
    /**
     * Returns the {@link GltfAssetV2} with a 
     * {@link de.javagl.jgltf.impl.v2.GlTF version 2.0 glTF}, 
     * or <code>null</code> if no asset has been read yet. 
     * 
     * @return The {@link GltfAssetV1}
     */
    GltfAssetV2 getAsGltfAssetV2()
    {
        de.javagl.jgltf.impl.v2.GlTF gltf = gltfReader.getAsGltfV2();
        if (gltf == null)
        {
            return null;
        }
        
        return new GltfAssetV2(gltf, rawGltfData.getBinaryData());
    }
    
    
}
