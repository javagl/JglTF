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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v2.GltfModelV2;

/**
 * A class for reading a {@link GltfModel} from a URI.
 */
public final class GltfModelReader
{
    /**
     * The consumer for {@link JsonError} instances
     */
    private Consumer<? super JsonError> jsonErrorConsumer = 
        JsonErrorConsumers.createLogging();
    
    /**
     * Default constructor
     */
    public GltfModelReader()
    {
        // Default constructor
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
        this.jsonErrorConsumer = jsonErrorConsumer;
    }
    
    /**
     * Read the {@link GltfModel} from the given URI
     * 
     * @param uri The URI
     * @return The {@link GltfModel}
     * @throws IOException If an IO error occurs
     */
    public GltfModel read(URI uri) throws IOException
    {
        try (InputStream inputStream = uri.toURL().openStream())
        {
            GltfModel gltfModel = readWithoutReferences(inputStream);
            URI baseUri = IO.getParent(uri);
            resolveReferences(gltfModel, baseUri);
            return gltfModel;
        }
    }
    
    /**
     * Resolve all external references of the given {@link GltfModel},
     * reading the buffer data, image data and possibly shader data,
     * and storing the resulting data in the model classes.
     * 
     * @param gltfModel The {@link GltfModel}
     * @param baseUri The base URI to resolve references against.
     */
    private void resolveReferences(GltfModel gltfModel, URI baseUri)
    {
        Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
        Objects.requireNonNull(baseUri, "The baseUri may not be null");
        
        Function<String, InputStream> uriResolver = 
            UriResolvers.createBaseUriResolver(baseUri);
        GltfReferenceLoader.loadAll(gltfModel.getReferences(), uriResolver);
    }

    /**
     * Read the {@link GltfModel} from the given URI. In contrast to the 
     * {@link #read(URI)} method, this method will not resolve any 
     * references that are contained in the {@link GltfModel}. <br>
     * <br>
     * This is mainly intended for binary- or embedded glTF assets that do not
     * have external references.
     * 
     * @param uri The URI
     * @return The {@link GltfModel}
     * @throws IOException If an IO error occurs
     */
    public GltfModel readWithoutReferences(URI uri) throws IOException
    {
        try (InputStream inputStream = uri.toURL().openStream())
        {
            GltfModel gltfModel = readWithoutReferences(inputStream);
            return gltfModel;
        }
    }
    
    /**
     * Read the {@link GltfModel} from the given input stream. In contrast
     * to the {@link #read(URI)} method, this method will not resolve any 
     * references that are contained in the {@link GltfModel}. <br>
     * <br>
     * This is mainly intended for binary- or embedded glTF assets that do not
     * have external references.
     * 
     * @param inputStream The input stream to read from
     * @return The {@link GltfModel}
     * @throws IOException If an IO error occurs
     */
    public GltfModel readWithoutReferences(InputStream inputStream) 
        throws IOException
    {
        GltfAssetReader gltfAssetReader = new GltfAssetReader();
        gltfAssetReader.setJsonErrorConsumer(jsonErrorConsumer);
        gltfAssetReader.read(inputStream);
        int majorVersion = gltfAssetReader.getMajorVersion();
        if (majorVersion == 1)
        {
            GltfAssetV1 gltfAsset = 
                gltfAssetReader.getAsGltfAssetV1();
            GltfModelV1 gltfModel = new GltfModelV1(
                gltfAsset.getGltf(), gltfAsset.getBinaryData());
            return gltfModel;
        }
        if (majorVersion == 2)
        {
            GltfAssetV2 gltfAsset = 
                gltfAssetReader.getAsGltfAssetV2();
            GltfModelV2 gltfModel = new GltfModelV2(
                gltfAsset.getGltf(), gltfAsset.getBinaryData());
            return gltfModel;
        }
        throw new IOException(
            "Unsupported major version: " + majorVersion);
    }
    
}
