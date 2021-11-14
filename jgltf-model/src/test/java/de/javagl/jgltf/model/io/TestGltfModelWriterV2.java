/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.javagl.jgltf.model.GltfModel;

/**
 * Tests for the {@link GltfModelWriter} class for glTF 2.0.<br>
 * <br>
 * The tests read a default glTF 2.0 model using the {@link GltfModelReader}, 
 * write it as default, embededd, and binary glTF, and compare the resulting 
 * files to the files that contain the expected output.
 */
@SuppressWarnings("javadoc")
public class TestGltfModelWriterV2
{
    @Test
    public void testGltfToGltfV2() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v2/"; 

        Path inputPath = Paths.get(basePath, "testBox/glTF");
        Path outputPath = Paths.get(basePath, 
            "testBox/output-glTF-to-glTF");
        Files.createDirectories(outputPath);

        String inputFileName = "Box.gltf";
        String outputFileName = "Box.gltf";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.write(gltfModel, outputFile.toFile());
        
        TestUtils.assertFileEquals(
            inputPath.toString(), "Box.gltf", 
            outputPath.toString(), "Box.gltf");
        
        TestUtils.assertFileEquals(
            inputPath.toString(), "Box0.bin", 
            outputPath.toString(), "Box0.bin");
    }
    
    @Test
    public void testGltfToGltfEmbeddedV2() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v2/"; 

        Path inputPath = Paths.get(basePath, "testBox/glTF");
        Path outputPath = Paths.get(basePath, 
            "testBox/output-glTF-to-glTF-Embedded");
        Files.createDirectories(outputPath);
        Path referencePath = Paths.get(basePath, "testBox/glTF-Embedded");

        String inputFileName = "Box.gltf";
        String outputFileName = "Box.gltf";
        String referenceFileName = "Box.gltf";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);

        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.writeEmbedded(gltfModel, outputFile.toFile());
        
        TestUtils.assertFileEquals(
            referencePath.toString(), referenceFileName, 
            outputPath.toString(), outputFileName);
    }
    
    @Test
    public void testGltfToGltfBinaryV2() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v2/"; 

        Path inputPath = Paths.get(basePath, "testBox/glTF");
        Path outputPath = Paths.get(basePath, 
            "testBox/output-glTF-to-glTF-Binary");
        Files.createDirectories(outputPath);
        Path referencePath = Paths.get(basePath, "testBox/glTF-Binary");

        String inputFileName = "Box.gltf";
        String outputFileName = "Box.glb";
        String referenceFileName = "Box.glb";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.writeBinary(gltfModel, outputFile.toFile());
        
        TestUtils.assertFileEquals(
            referencePath.toString(), referenceFileName, 
            outputPath.toString(), outputFileName);
    }
    
}

