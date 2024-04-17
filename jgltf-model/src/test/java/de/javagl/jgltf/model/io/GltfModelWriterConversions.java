/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;

/**
 * Utilities for the tests for the {@link GltfModelWriter} class.<br>
 * <br>
 */
class GltfModelWriterConversions
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModelWriterConversions.class.getName());
    
    /**
     * A log level for debug output
     */
    static Level level = Level.FINE;
    
    /**
     * The directory that contains the test models
     */
    private static final String TEST_MODELS_DIRECTORY = 
        "./src/test/resources/testModels/";

    /**
     * Read the specified flavor of the specified model, and write it with the
     * specified output flavor.<br>
     * <br>
     * The input will be read from a directory like<br>
     * <code>TEST_MODELS_DIRECTORY/versionString/testModelName/glTF-Binary/testModelName.glb</code><br>
     * and be written into a directory like<br>
     * <code>TEST_MODELS_DIRECTORY/versionString/testModelName/output-glTF-Binary-to-glTF-Embedded/testModelName.gltf</code><br>
     * <br>
     * The output can then be compared to a "golden" reference output.
     * 
     * @param versionString The version string, "v1" or "v2"
     * @param testModelName The test model
     * @param inputFlavor The flavor of the model that should be read
     * @param outputFlavor The flavor of the model that should be written
     * @throws IOException If something goes wrong
     */
    static void readAndWriteModel(
        String versionString, 
        String testModelName, 
        GltfFlavor inputFlavor, 
        GltfFlavor outputFlavor) throws IOException
    {
        logger.log(level, "Reading " + testModelName + ", " + versionString);
        logger.log(level, "  Input : " + inputFlavor);
        logger.log(level, "  Output: " + inputFlavor);
        
        String basePath = TEST_MODELS_DIRECTORY + versionString.toLowerCase(); 

        Path inputPath = Paths.get(basePath, 
            testModelName + "/" + inputFlavor);
        Path outputPath = Paths.get(basePath,
            testModelName + "/output-" + inputFlavor + "-to-" + outputFlavor);
        Files.createDirectories(outputPath);

        String inputFileName = 
            testModelName + "." + inputFlavor.getExtension();
        String outputFileName = 
            testModelName + "." + outputFlavor.getExtension();

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        if (logger.isLoggable(level))
        {
            logger.log(level, "Input model:");
            logger.log(level, 
                "\n" + createStructureInfoString(gltfModel));
        }
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        if (outputFlavor == GltfFlavor.DEFAULT)
        {
            gltfModelWriter.write(gltfModel, outputFile.toFile());
        }
        else if (outputFlavor == GltfFlavor.BINARY)
        {
            gltfModelWriter.writeBinary(gltfModel, outputFile.toFile());
        }
        else if (outputFlavor == GltfFlavor.EMBEDDED)
        {
            gltfModelWriter.writeEmbedded(gltfModel, outputFile.toFile());
        }
        
        if (logger.isLoggable(level))
        {
            GltfModel outputGltfModel = 
                gltfModelReader.read(outputFile.toUri());
            logger.log(level, "Output model:");
            logger.log(level, 
                "\n" + createStructureInfoString(outputGltfModel));
        }
    }

    
    /**
     * Assert that the files in the output directory of the specified conversion
     * are the same as in the golden reference directory.<br>
     * <br>
     * This will compare the files in a directory like<br>
     * <code>TEST_MODELS_DIRECTORY/versionString/testModelName/output-glTF-Binary-to-glTF-Embedded/</code><br>
     * to the files in a directory like
     * <code>TEST_MODELS_DIRECTORY/versionString/testModelName/golden-glTF-Binary-to-glTF-Embedded/</code><br>
     * 
     * @param versionString The version string, "v1" or "v2"
     * @param testModelName The test model
     * @param inputFlavor The flavor of the model that should be read
     * @param outputFlavor The flavor of the model that should be written
     * @throws IOException If something goes wrong
     * @throws AssertionError If the files are not equal
     */
    static void assertFilesEqual(
        String versionString, 
        String testModelName, 
        GltfFlavor inputFlavor, 
        GltfFlavor outputFlavor) throws IOException
    {
        String basePath = TEST_MODELS_DIRECTORY + versionString.toLowerCase(); 
        Path actualPath = Paths.get(basePath,
            testModelName + "/output-" + inputFlavor + "-to-" + outputFlavor);
        Path expectedPath = Paths.get(basePath,
            testModelName + "/golden-" + inputFlavor + "-to-" + outputFlavor);
        TestUtils.assertDirectoriesEqual(
            expectedPath.toString(), actualPath.toString());
    }
    
    /**
     * Creates an unspecified debug string for the given model summarizing
     * aspects about its structure (i.e. the number of buffers and the
     * storage type of images)
     * 
     * @param gltfModel The glTF model
     * @return The info string
     */
    private static String createStructureInfoString(GltfModel gltfModel)
    {
        StringBuilder sb = new StringBuilder();

        List<BufferModel> bufferModels = gltfModel.getBufferModels();
        sb.append("Buffers (").append(bufferModels.size()).append("): \n");
        for (int i = 0; i < bufferModels.size(); i++)
        {
            BufferModel bufferModel = bufferModels.get(i);
            ByteBuffer bufferData = bufferModel.getBufferData();
            sb.append("  buffer ").append(i).append(": ")
                .append(bufferData.capacity()).append(" bytes").append("\n");
        }
        
        List<ImageModel> imageModels = gltfModel.getImageModels();
        sb.append("Images (").append(imageModels.size()).append("): \n");
        for (int i = 0; i < imageModels.size(); i++)
        {
            ImageModel imageModel = imageModels.get(i);
            ByteBuffer imageData = imageModel.getImageData();
            BufferViewModel imageBufferViewModel = 
                imageModel.getBufferViewModel();
            boolean external = imageBufferViewModel == null;
            sb.append("  image ").append(i).append(": ")
                .append(imageData.capacity()).append(" bytes, ")
                .append("MIME type: " + imageModel.getMimeType())
                .append(external ? ", external" : ", via buffer view")
                .append("\n");
        }
        return sb.toString();
    }
}

