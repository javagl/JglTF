package de.javagl.jgltf.model.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.javagl.jgltf.model.GltfModel;

@SuppressWarnings("javadoc")
public class TestIO
{
    @Test
    public void testGltfToGltfV1() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v1/"; 

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
        
        assertFileEquals(
            inputPath.toString(), "Box.gltf", 
            outputPath.toString(), "Box.gltf");
        
        assertFileEquals(
            inputPath.toString(), "Box.bin", 
            outputPath.toString(), "Box.bin");
        
        assertFileEquals(
            inputPath.toString(), "Box0VS.glsl", 
            outputPath.toString(), "Box0VS.glsl");
        
        assertFileEquals(
            inputPath.toString(), "Box0FS.glsl", 
            outputPath.toString(), "Box0FS.glsl");
    }
    
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
        
        assertFileEquals(
            inputPath.toString(), "Box.gltf", 
            outputPath.toString(), "Box.gltf");
        
        assertFileEquals(
            inputPath.toString(), "Box0.bin", 
            outputPath.toString(), "Box0.bin");
    }
    
    
    @Test
    public void testGltfToGltfEmbeddedV1() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v1/"; 

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
        
        assertFileEquals(
            referencePath.toString(), referenceFileName, 
            outputPath.toString(), outputFileName);
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
        
        assertFileEquals(
            referencePath.toString(), referenceFileName, 
            outputPath.toString(), outputFileName);
    }
    
    
    @Test
    public void testGltfToGltfBinaryV1() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/v1/"; 

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
        
        assertFileEquals(
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
        
        assertFileEquals(
            referencePath.toString(), referenceFileName, 
            outputPath.toString(), outputFileName);
    }
    
    private static void assertFileEquals(
        String directoryExpected, String fileNameExpected, 
        String directoryActual, String fileNameActual) throws IOException
    {
        Path pathExpected = Paths.get(directoryExpected, fileNameExpected);
        byte bytesExpected[] = Files.readAllBytes(pathExpected);
        String stringExpected = normalizeLineSeparators(bytesExpected);
        Path pathActual = Paths.get(directoryActual, fileNameActual);
        byte bytesActual[] = Files.readAllBytes(pathActual);
        String stringActual = normalizeLineSeparators(bytesActual);
        assertEquals(stringExpected, stringActual);
    }
    
    /**
     * Convert the given bytes into a string, using the default charset,
     * and normalize the line separators, by replacing all <code>"\r\n"</code>
     * and <code>"\r"</code> with <code>"\n"</code>
     *   
     * @param bytes The bytes
     * @return The normalized string
     */
    private static String normalizeLineSeparators(byte bytes[])
    {
        String s = new String(bytes);
        s = s.replaceAll("\\r\\n", "\n");
        s = s.replaceAll("\\r", "\n");
        return s;
        
    }
    
    
}

