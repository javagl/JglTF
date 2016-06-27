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
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.model.GltfData;

/**
 * A class for reading {@link GltfData} from an URI.
 */
public class GltfDataReader
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
     * The reader for the {@link GlTF}
     */
    private final GltfReader gltfReader;
    
    /**
     * The URI that the data is read from
     */
    private URI uri;
    
    /**
     * The {@link GltfData} that is currently being read
     */
    private GltfData gltfData;
    
    /**
     * Creates a new glTF data reader
     */
    public GltfDataReader()
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
     * Read the {@link GltfData} from the given URI
     * 
     * @param uri The URI
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public GltfData readGltfData(URI uri) throws IOException
    {
        Objects.requireNonNull(uri, "The URI may not be null");
        
        this.uri = uri;
        this.gltfData = readGltfData();
        
        GltfDataResolver gltfDataResolver =
            new GltfDataResolver(gltfData, IO.getParent(uri));
        gltfDataResolver.resolveBuffers();
        
        BufferViews.createBufferViewByteBuffers(gltfData);
        BinaryGltfDatas.createBinaryImageDatas(gltfData);
        BinaryGltfDatas.createBinaryShaderDatas(gltfData);
        
        gltfDataResolver.resolveImages();
        gltfDataResolver.resolveShaders();
        
        GltfData result = this.gltfData;
        this.gltfData = null;
        this.uri = null;
        return result;
    }
    
    /**
     * Read the {@link GltfData} from the current URI
     * 
     * @return The {@link GltfData} 
     * @throws IOException If an IO error occurs
     */
    private GltfData readGltfData() throws IOException
    {
        if (uri.toString().toLowerCase().endsWith(".glb"))
        {
            try (InputStream inputStream = uri.toURL().openStream())
            {
                logger.log(level, "Reading binary glTF data");
                byte data[] = IO.readStream(inputStream);
                GltfData gltfData = BinaryGltfDatas.create(data, gltfReader);
                logger.log(level, "Reading binary glTF data DONE");
                return gltfData;
            }
        }
        try (InputStream inputStream = uri.toURL().openStream())
        {
            logger.log(level, "Reading glTF data");
            GlTF gltf = gltfReader.readGltf(inputStream);
            GltfData gltfData = new GltfData(gltf);
            logger.log(level, "Reading glTF data DONE");
            return gltfData;
        }
    }

}
