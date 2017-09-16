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
import java.util.function.Consumer;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.GltfModels;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;

/**
 * A class for reading a glTF asset in a version-agnostic form. <br>
 * <br>
 * The {@link #read(URI)} method allows reading the asset from a URI. The
 * external references of the asset will be resolved against the parent
 * of the given URI, and loaded automatically. The respective data may
 * then be obtained with {@link GltfAsset#getReferenceData(String)}.<br>
 * <br>
 * The {@link #readWithoutReferences(URI)} and 
 * {@link #readWithoutReferences(InputStream)} methods allow reading an
 * asset from a URI or an input stream, <i>without</i> resolving external 
 * references. This is mainly intended for binary- or embedded glTF assets
 * that do not have external references, or for cases where the external
 * references should be resolved manually.<br>
 * <br>
 * In addition to returning the {@link GltfAsset} that was read, the
 * {@link #getMajorVersion() major version} of the read asset may be
 * queried. Depending on the version, the asset may be obtained as a 
 * {@link #getAsGltfAssetV1() glTF 1.0 asset} or a 
 * {@link #getAsGltfAssetV2() glTF 2.0 asset}.<br> 
 * <br> 
 * Such a {@link GltfAsset} may then be processed further, for example,
 * by creating a {@link GltfModel} using {@link GltfModels#create(GltfAsset)}.
 */
public final class GltfAssetReader
{
    /**
     * The {@link GltfReader} for the JSON part
     */
    private final GltfReader gltfReader;
    
    /**
     * The {@link GltfAsset} that was read
     */
    private GltfAsset gltfAsset;
    
    /**
     * Creates a new instance
     */
    public GltfAssetReader()
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
     * Read the {@link GltfAsset} from the given URI
     * 
     * @param uri The URI
     * @return The {@link GltfModel}
     * @throws IOException If an IO error occurs
     */
    public GltfAsset read(URI uri) throws IOException
    {
        try (InputStream inputStream = uri.toURL().openStream())
        {
            readWithoutReferences(inputStream);
            URI baseUri = IO.getParent(uri);
            GltfReferenceResolver.resolveAll(
                gltfAsset.getReferences(), baseUri);
            return gltfAsset;
        }
    }
    
    /**
     * Read the {@link GltfAsset} from the given URI.<br>
     * <br>
     * In contrast to the {@link #read(URI)} method, this method will
     * not resolve any external references.<br>
     * <br>
     * This is mainly intended for binary- or embedded glTF assets that do not
     * have external references.
     * 
     * @param uri The URI
     * @return The {@link GltfModel}
     * @throws IOException If an IO error occurs
     */
    public GltfAsset readWithoutReferences(URI uri) throws IOException
    {
        try (InputStream inputStream = uri.toURL().openStream())
        {
            readWithoutReferences(inputStream);
            return gltfAsset;
        }
    }
    
    /**
     * Read the glTF asset from the given input stream. The caller is 
     * responsible for closing the given stream. After this method
     * has been called, the version of the glTF may be obtained with
     * {@link #getMajorVersion()}, and the actual asset may be obtained
     * with {@link #getAsGltfAssetV1()} or {@link #getAsGltfAssetV2()}.<br>
     * <br>
     * In contrast to the {@link #read(URI)} method, this method will
     * not resolve any external references.<br>
     * <br>
     * This is mainly intended for binary- or embedded glTF assets that do not
     * have external references.
     * 
     * @param inputStream The input stream
     * @return The {@link GltfAsset}
     * @throws IOException If an IO error occurred
     */
    public GltfAsset readWithoutReferences(InputStream inputStream) 
        throws IOException
    {
        RawGltfData rawGltfData = RawGltfDataReader.read(inputStream);
        ByteBuffer jsonData = rawGltfData.getJsonData();
        try (InputStream jsonInputStream =
            Buffers.createByteBufferInputStream(jsonData))
        {
            gltfReader.read(jsonInputStream);
            int majorVersion = gltfReader.getMajorVersion();
            if (majorVersion == 1)
            {
                de.javagl.jgltf.impl.v1.GlTF gltfV1 = 
                    gltfReader.getAsGltfV1();
                gltfAsset = new GltfAssetV1(gltfV1, 
                    rawGltfData.getBinaryData());
            }
            else if (majorVersion == 2)
            {
                de.javagl.jgltf.impl.v2.GlTF gltfV2 = 
                    gltfReader.getAsGltfV2();
                gltfAsset = new GltfAssetV2(gltfV2, 
                    rawGltfData.getBinaryData());
            }
            else
            {
                throw new IOException(
                    "Unsupported major version: " + majorVersion);
            }
        }
        return gltfAsset;
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
     * or <code>null</code> if no asset has been read yet, or
     * the glTF asset that has been read is not a glTF 1.0 asset. 
     * 
     * @return The {@link GltfAssetV1}
     */
    GltfAssetV1 getAsGltfAssetV1()
    {
        if (gltfAsset instanceof GltfAssetV1)
        {
            return (GltfAssetV1)gltfAsset;
        }
        return null;
    }
    
    /**
     * Returns the {@link GltfAssetV2} with a 
     * {@link de.javagl.jgltf.impl.v2.GlTF version 2.0 glTF}, 
     * or <code>null</code> if no asset has been read yet, or
     * the glTF asset that has been read is not a glTF 2.0 asset. 
     * 
     * @return The {@link GltfAssetV2}
     */
    GltfAssetV2 getAsGltfAssetV2()
    {
        if (gltfAsset instanceof GltfAssetV2)
        {
            return (GltfAssetV2)gltfAsset;
        }
        return null;
    }
    
    
}
