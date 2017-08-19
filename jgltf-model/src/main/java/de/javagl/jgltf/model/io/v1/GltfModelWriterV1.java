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
package de.javagl.jgltf.model.io.v1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.List;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.v1.GltfModelV1;

/**
 * A class for writing a {@link GltfModelV1}. The model can be written as
 * a default glTF, consisting of a JSON file and the files that are 
 * referred to via URIs, or as a binary file, or an embedded file where
 * all external references are replaced by data URIs.
 */
public class GltfModelWriterV1
{
    /**
     * Default constructor
     */
    public GltfModelWriterV1()
    {
        // Default constructor
    }
    
    /**
     * Write the given {@link GltfModelV1} to the given file. External
     * references of buffers, images and shaders that are given via 
     * the respective URI string will be resolved against the parent 
     * directory of the given file, and the corresponding data will 
     * be written into the corresponding files. 
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param file The file
     * @throws IOException If an IO error occurs
     */
    public void write(GltfModelV1 gltfModel, File file) 
        throws IOException
    {
        File directory = file.getParentFile();
        directory.mkdirs();
        
        GlTF gltf = gltfModel.getGltf();
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            GltfWriter gltfWriter = new GltfWriter();
            gltfWriter.write(gltf, outputStream);
        }
        

        List<BufferModel> bufferModels = gltfModel.getBufferModels();
        for (BufferModel bufferModel : bufferModels)
        {
            String uri = bufferModel.getUri();
            if (!IO.isDataUriString(uri))
            {
                ByteBuffer bufferData = bufferModel.getBufferData();
                writeData(directory, uri, bufferData);
            }
        }
        
        List<ImageModel> imageModels = gltfModel.getImageModels();
        for (ImageModel imageModel : imageModels)
        {
            String uri = imageModel.getUri();
            if (!IO.isDataUriString(uri))
            {
                ByteBuffer imageData = imageModel.getImageData();
                writeData(directory, uri, imageData);
            }
        }
        
        List<ShaderModel> shaderModels = gltfModel.getShaderModels();
        for (ShaderModel shaderModel : shaderModels)
        {
            String uri = shaderModel.getUri();
            if (!IO.isDataUriString(uri))
            {
                ByteBuffer shaderData = shaderModel.getShaderData();
                writeData(directory, uri, shaderData);
            }
        }
    }
    
    /**
     * Write the given {@link GltfModelV1} as a binary glTF asset to the
     * given file
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param file The file
     * @throws IOException If an IO error occurs
     */
    public void writeBinary(GltfModelV1 gltfModel, File file) 
        throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            writeBinary(gltfModel, outputStream);
        }
    }
    
    /**
     * Write the given {@link GltfModelV1} as a binary glTF asset to the
     * given output stream. The caller is responsible for closing the 
     * given stream.
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param outputStream The output stream
     * @throws IOException If an IO error occurs
     */
    public void writeBinary(GltfModelV1 gltfModel, OutputStream outputStream) 
        throws IOException
    {
        GltfModelToBinaryAssetConverterV1 gltfModelToBinaryAssetConverter =
            new GltfModelToBinaryAssetConverterV1();
        GltfAssetV1 gltfAsset = 
            gltfModelToBinaryAssetConverter.convert(gltfModel);
        GltfAssetWriterV1 gltfAssetWriter = new GltfAssetWriterV1();
        gltfAssetWriter.writeBinary(gltfAsset, outputStream);
    }
    

    /**
     * Write the given {@link GltfModelV1} as an embedded glTF asset to the
     * given file
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param file The file
     * @throws IOException If an IO error occurs
     */
    public void writeEmbedded(GltfModelV1 gltfModel, File file) 
        throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            writeEmbedded(gltfModel, outputStream);
        }
    }
    
    /**
     * Write the given {@link GltfModelV1} as an embedded glTF asset to the
     * given output stream. The caller is responsible for closing the 
     * given stream.
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param outputStream The output stream
     * @throws IOException If an IO error occurs
     */
    public void writeEmbedded(GltfModelV1 gltfModel, OutputStream outputStream) 
        throws IOException
    {
        GltfModelToEmbeddedAssetConverterV1 gltfModelToEmbeddedAssetConverter = 
            new GltfModelToEmbeddedAssetConverterV1();
        GltfAssetV1 gltfAsset = 
            gltfModelToEmbeddedAssetConverter.convert(gltfModel);
        GltfWriter gltfWriter = new GltfWriter();
        GlTF gltf = gltfAsset.getGltf();
        gltfWriter.write(gltf, outputStream);
    }
    
    
    /**
     * Write the given data into a file that is defined by the given directory
     * and the given (possibly relative) URI.
     * 
     * @param directory The directory
     * @param uri The URI
     * @param data The data to write
     * @throws IOException If an IO error occurs
     */
    private static void writeData(File directory, String uri, ByteBuffer data) 
        throws IOException
    {
        Path directoryPath = directory.toPath();
        Path bufferFilePath = directoryPath.resolve(uri);
        File bufferFile = bufferFilePath.toFile();
        try (OutputStream outputStream = new FileOutputStream(bufferFile);
            WritableByteChannel writableByteChannel =
                Channels.newChannel(outputStream))
        {
            writableByteChannel.write(data.slice());
        }
    }
}
