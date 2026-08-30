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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.io.v1.GltfAssetWriterV1;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetWriterV2;

/**
 * A class for writing a {@link GltfAsset}
 */
public class GltfAssetWriter
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GltfAssetWriter.class.getName());
    
    /**
     * Default constructor
     */
    public GltfAssetWriter()
    {
        // Default constructor
    }

    /**
     * Write the the given {@link GltfAsset} to a file with the given name. 
     * The {@link GltfAsset#getBinaryData() binary data} will be ignored.
     * The {@link GltfAsset#getReferenceDatas() reference data elements} 
     * will be written to files that are determined by resolving the 
     * (relative) URLs of the references against the parent of the specified
     * file.  
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param fileName The file name for the JSON file
     * @throws IOException If an IO error occurred
     */
    public void write(GltfAsset gltfAsset, String fileName) 
        throws IOException
    {
        write(gltfAsset, new File(fileName));
    }
    
    /**
     * Write the the given {@link GltfAsset} to the given file.  
     * The {@link GltfAsset#getBinaryData() binary data} will be ignored.
     * The {@link GltfAsset#getReferenceDatas() reference data elements} 
     * will be written to files that are determined by resolving the 
     * (relative) URLs of the references against the parent of the specified
     * file.  
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param file The file for the JSON part
     * @throws IOException If an IO error occurred
     */
    public void write(GltfAsset gltfAsset, File file) 
        throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            writeJson(gltfAsset, outputStream);
        }
        writeReferenceDatas(gltfAsset, file);
    }

    /**
     * Write the JSON part of the given {@link GltfAsset} to a file with 
     * the given name. The {@link GltfAsset#getBinaryData() binary data}
     * and {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be ignored.
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param fileName The file name for the JSON file
     * @throws IOException If an IO error occurred
     */
    public void writeJson(GltfAsset gltfAsset, String fileName) 
        throws IOException
    {
        writeJson(gltfAsset, new File(fileName));
    }
    
    /**
     * Write the JSON part of the given {@link GltfAsset} to a file with 
     * the given name. The {@link GltfAsset#getBinaryData() binary data}
     * and {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be ignored.
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param file The file for the JSON part
     * @throws IOException If an IO error occurred
     */
    public void writeJson(GltfAsset gltfAsset, File file) 
        throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            writeJson(gltfAsset, outputStream);
        }
    }
    
    /**
     * Write the JSON part of the given {@link GltfAsset} to the given
     * output stream. The {@link GltfAsset#getBinaryData() binary data}
     * and {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be ignored. The caller is responsible for closing the given
     * stream.
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param outputStream The output stream
     * @throws IOException If an IO error occurred
     */
    public void writeJson(GltfAsset gltfAsset, OutputStream outputStream) 
        throws IOException
    {
        Object gltf = gltfAsset.getGltf();
        GltfWriter gltfWriter = new GltfWriter();
        gltfWriter.write(gltf, outputStream);
    }
    
    /**
     * Write the given {@link GltfAsset} as a binary glTF asset to file with 
     * the given name.
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param fileName The file name for the JSON file
     * @throws IOException If an IO error occurred
     */
    public void writeBinary(GltfAsset gltfAsset, String fileName) 
        throws IOException
    {
        writeBinary(gltfAsset, new File(fileName));
    }
    
    /**
     * Write the given {@link GltfAsset} as a binary glTF asset to the given
     * file
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param file The file
     * @throws IOException If an IO error occurred
     */
    public void writeBinary(GltfAsset gltfAsset, File file) 
        throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            writeBinary(gltfAsset, outputStream);
        }
        // Binary glTF assets may still have external references
        writeReferenceDatas(gltfAsset, file);
    }
    
    /**
     * Write the given {@link GltfAsset} as a binary glTF asset to the 
     * given output stream. The caller is responsible for closing the 
     * given stream.
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param outputStream The output stream
     * @throws IOException If an IO error occurred
     */
    public void writeBinary(GltfAsset gltfAsset, OutputStream outputStream) 
        throws IOException
    {
        if (gltfAsset instanceof GltfAssetV1)
        {
            GltfAssetV1 gltfAssetV1 = (GltfAssetV1)gltfAsset;
            GltfAssetWriterV1 gltfAssetWriterV1 = new GltfAssetWriterV1();
            gltfAssetWriterV1.writeBinary(gltfAssetV1, outputStream);
        }
        else if (gltfAsset instanceof GltfAssetV2)
        {
            GltfAssetV2 gltfAssetV2 = (GltfAssetV2)gltfAsset;
            GltfAssetWriterV2 gltfAssetWriterV2 = new GltfAssetWriterV2();
            gltfAssetWriterV2.writeBinary(gltfAssetV2, outputStream);
        }
        else
        {
            throw new IOException(
                "The gltfAsset has an unknown version: " + gltfAsset);
        }
    }

    /**
     * Write all {@link GltfAsset#getReferenceDatas()} to files, relative to the
     * given file.
     * 
     * @param gltfAsset The {@link GltfAsset}
     * @param file The base file
     * @throws IOException If an IO error occurs
     */
    private void writeReferenceDatas(GltfAsset gltfAsset, File file)
        throws IOException
    {
        Map<String, ByteBuffer> referenceDatas = gltfAsset.getReferenceDatas();
        for (Entry<String, ByteBuffer> entry : referenceDatas.entrySet())
        {
            String rawUriString = entry.getKey();

            // The space character is allowed in glTF URIs, but not
            // part of a valid URI - replace it for the checks here
            String uriString = rawUriString.replaceAll(" ", "%20");

            if (!IO.isValidUri(uriString))
            {
                logger.severe("Not a valid URI: '" + uriString + "'");
                continue;
            }

            // A data URI should never be in the referenceDatas!
            if (IO.isDataUriString(uriString))
            {
                logger.severe("Found data URI as reference");
                continue;
            }

            // Absolute URIs cannot be written - print a severe error
            // message, but continue with the remaining entries
            if (IO.isAbsolute(uriString))
            {
                logger.severe(
                    "Found absolute URI as reference: '" + uriString + "'");
                continue;
            }

            // Decode the URI to be used as a file name
            String decodedUriString = IO.decodeUri(uriString);
            // System.out.println("Decoded '"+uriString+"'");
            // System.out.println(" to '"+decodedUriString+"'");

            // Resolve the decoded URI against the containing directory
            Path resolutionPath = file.toPath().getParent();
            Path referenceFilePath = resolutionPath.resolve(decodedUriString);
            File referenceFile = referenceFilePath.toFile();

            // Ensure that the directory of the reference file exists
            Path referenceDirectoryPath = referenceFilePath.getParent();
            Files.createDirectories(referenceDirectoryPath);

            // Write the actual reference file data
            ByteBuffer data = entry.getValue();
            try (@SuppressWarnings("resource")
            WritableByteChannel writableByteChannel =
                Channels.newChannel(new FileOutputStream(referenceFile)))
            {
                writableByteChannel.write(data.slice());
            }
        }
    }

}
