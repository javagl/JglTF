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
public class TestGltfModelWriterV2_binToBin
{
    public static void main(String[] args) throws IOException
    {
        //testGltfBinaryToGltfBinaryV2();
        //testGltfBinaryToGltfV2();
        testGltfBinaryToGltfEmbeddedV2();
    }
    public static void main1(String[] args) throws IOException
    {
        String basePath = "./src/test/resources/testModels/"; 

        Path inputPath = Paths.get(basePath, "");
        Path outputPath = Paths.get(basePath, "");
        Files.createDirectories(outputPath);

        String inputFileName = "unitCubeTextured.glb";
        String outputFileName = "unitCubeTextured-out.glb";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.writeBinary(gltfModel, outputFile.toFile());
        
    }
    public static void main2(String[] args) throws IOException
    {
        String basePath = "./src/test/resources/testModels/v2/"; 

        Path inputPath = Paths.get(basePath, "unitCubeTextured/glTF-Binary");
        Path outputPath = Paths.get(basePath, "unitCubeTextured/glTF-Embedded");
        Files.createDirectories(outputPath);

        String inputFileName = "unitCubeTexturedV2.glb";
        String outputFileName = "unitCubeTexturedV2.gltf";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.writeEmbedded(gltfModel, outputFile.toFile());
        
    }
    
    static void testGltfBinaryToGltfBinaryV2() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v2/"; 

        Path inputPath = Paths.get(basePath, "unitCubeTextured/glTF-Binary");
        Path outputPath = Paths.get(basePath, 
            "unitCubeTextured/output-glTF-Binary-to-glTF-Binary");
        Files.createDirectories(outputPath);

        String inputFileName = "unitCubeTexturedV2.glb";
        String outputFileName = "unitCubeTexturedV2.glb";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.writeBinary(gltfModel, outputFile.toFile());
    }
    
    static void testGltfBinaryToGltfV2() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v2/"; 

        Path inputPath = Paths.get(basePath, "unitCubeTextured/glTF-Binary");
        Path outputPath = Paths.get(basePath, 
            "unitCubeTextured/output-glTF-Binary-to-glTF");
        Files.createDirectories(outputPath);

        String inputFileName = "unitCubeTexturedV2.glb";
        String outputFileName = "unitCubeTexturedV2.gltf";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.write(gltfModel, outputFile.toFile());
    }
    
    static void testGltfBinaryToGltfEmbeddedV2() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v2/"; 

        Path inputPath = Paths.get(basePath, "unitCubeTextured/glTF-Binary");
        Path outputPath = Paths.get(basePath, 
            "unitCubeTextured/output-glTF-Binary-to-glTF-Embedded");
        Files.createDirectories(outputPath);

        String inputFileName = "unitCubeTexturedV2.glb";
        String outputFileName = "unitCubeTexturedV2.gltf";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        gltfModelWriter.writeEmbedded(gltfModel, outputFile.toFile());
    }
    
}

