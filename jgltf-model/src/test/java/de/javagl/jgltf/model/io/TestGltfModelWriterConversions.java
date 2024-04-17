/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io;

import java.io.IOException;
import java.util.logging.Level;

import org.junit.Test;

import de.javagl.jgltf.model.structure._LoggerUtil;

/**
 * Tests for the {@link GltfModelWriter} class for glTF 2.0.<br>
 * <br>
 * The tests read a glTF model using the {@link GltfModelReader}, 
 * in different flavors (default, binary, embedded), write it out
 * in all different flavors, and compare the result to the expected
 * reference files.
 */
@SuppressWarnings("javadoc")
public class TestGltfModelWriterConversions
{
    public static void main(String[] args) throws IOException
    {
        _LoggerUtil.initLogging();
        GltfModelWriterConversions.level = Level.INFO;
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.BINARY;
        GltfFlavor outputFlavor = GltfFlavor.BINARY;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
    }

    //=========================================================================
    // V1
    
    // V1, DEFAULT input
    
    @Test
    public void testGltfToGltfV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.DEFAULT;
        GltfFlavor outputFlavor = GltfFlavor.DEFAULT;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfToGltfBinaryV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.DEFAULT;
        GltfFlavor outputFlavor = GltfFlavor.BINARY;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfToGltfEmbeddedV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.DEFAULT;
        GltfFlavor outputFlavor = GltfFlavor.EMBEDDED;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    // V1, BINARY input
    
    @Test
    public void testGltfBinaryToGltfV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.BINARY;
        GltfFlavor outputFlavor = GltfFlavor.DEFAULT;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltffBinaryToGltfBinaryV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.BINARY;
        GltfFlavor outputFlavor = GltfFlavor.BINARY;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfBinaryToGltfEmbeddedV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.BINARY;
        GltfFlavor outputFlavor = GltfFlavor.EMBEDDED;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    // V1, EMBEDDED input
    
    @Test
    public void testGltfEmbeddedToGltfV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.EMBEDDED;
        GltfFlavor outputFlavor = GltfFlavor.DEFAULT;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfEmbeddedToGltfBinaryV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v1";
        GltfFlavor inputFlavor = GltfFlavor.EMBEDDED;
        GltfFlavor outputFlavor = GltfFlavor.BINARY;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfEmbeddedToGltfEmbeddedV1() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.EMBEDDED;
        GltfFlavor outputFlavor = GltfFlavor.EMBEDDED;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    
    
    //=========================================================================
    // V2
    
    // V2, DEFAULT input
    
    @Test
    public void testGltfToGltfV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.DEFAULT;
        GltfFlavor outputFlavor = GltfFlavor.DEFAULT;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfToGltfBinaryV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.DEFAULT;
        GltfFlavor outputFlavor = GltfFlavor.BINARY;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfToGltfEmbeddedV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.DEFAULT;
        GltfFlavor outputFlavor = GltfFlavor.EMBEDDED;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    // V2, BINARY input
    
    @Test
    public void testGltfBinaryToGltfV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.BINARY;
        GltfFlavor outputFlavor = GltfFlavor.DEFAULT;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltffBinaryToGltfBinaryV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.BINARY;
        GltfFlavor outputFlavor = GltfFlavor.BINARY;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfBinaryToGltfEmbeddedV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.BINARY;
        GltfFlavor outputFlavor = GltfFlavor.EMBEDDED;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    // V2, EMBEDDED input
    
    @Test
    public void testGltfEmbeddedToGltfV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.EMBEDDED;
        GltfFlavor outputFlavor = GltfFlavor.DEFAULT;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfEmbeddedToGltfBinaryV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.EMBEDDED;
        GltfFlavor outputFlavor = GltfFlavor.BINARY;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    
    @Test
    public void testGltfEmbeddedToGltfEmbeddedV2() throws IOException 
    {
        String testModelName = "unitCubeTextured";
        String versionString = "v2";
        GltfFlavor inputFlavor = GltfFlavor.EMBEDDED;
        GltfFlavor outputFlavor = GltfFlavor.EMBEDDED;
        GltfModelWriterConversions.readAndWriteModel(
            versionString, testModelName, inputFlavor, outputFlavor);
        GltfModelWriterConversions.assertFilesEqual(
            versionString, testModelName, inputFlavor, outputFlavor);
    }
    

}

