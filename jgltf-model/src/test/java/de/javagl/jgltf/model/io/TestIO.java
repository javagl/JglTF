package de.javagl.jgltf.model.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.javagl.jgltf.model.GltfData;

@SuppressWarnings("javadoc")
public class TestIO
{
    @Test
    public void testGltfToGltf() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/"; 

        Path inputPath = Paths.get(basePath, "testBox/glTF");
        Path outputPath = Paths.get(basePath, 
            "testBox/output-glTF-to-glTF");
        Files.createDirectories(outputPath);

        String inputFileName = "Box.gltf";
        String outputFileName = "Box.gltf";

        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        Path outputFile = Paths.get(outputPath.toString(), outputFileName);
        
        GltfDataReader r = new GltfDataReader();
        GltfData gltfData = r.readGltfData(inputFile.toUri());
        GltfDataWriter w = new GltfDataWriter();
        w.writeGltfData(gltfData, outputFile.toString());
        
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
    public void testGltfToGltfEmbedded() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/"; 

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
        
        GltfDataReader r = new GltfDataReader();
        GltfData gltfData = r.readGltfData(inputFile.toUri());
        
        gltfData = new GltfDataToEmbeddedConverter().convert(gltfData);
        
        GltfDataWriter w = new GltfDataWriter();
        w.writeGltfData(gltfData, outputFile.toString());
        
        assertFileEquals(
            referencePath.toString(), referenceFileName, 
            outputPath.toString(), outputFileName);
    }
    
    @Test
    public void testGltfToGltfBinary() throws IOException 
    {
        String basePath = "./src/test/resources/testModels/"; 

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
        
        GltfDataReader r = new GltfDataReader();
        GltfData gltfData = r.readGltfData(inputFile.toUri());
        
        gltfData = new GltfDataToBinaryConverter().convert(gltfData);
        
        BinaryGltfDataWriter w = new BinaryGltfDataWriter();
        w.writeBinaryGltfData(gltfData, outputFile.toString());
        
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