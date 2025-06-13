/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import de.javagl.jgltf.model.io.GltfModelReader;
import de.javagl.jgltf.model.io.GltfModelWriter;

/**
 * Tests for the {@link GltfModel} class.<br>
 */
@SuppressWarnings("javadoc")
public class TestInterpolationTest
{
    @Test
    public void testWriteInterpolationTest() throws IOException 
    {
        // Regression test for https://github.com/javagl/JglTF/issues/129
        String inputPath = "./src/test/resources/testModels/" + 
            "InterpolationTest/glTF/InterpolationTest.gltf"; 

        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(Paths.get(inputPath));
        
        GltfModelWriter w = new GltfModelWriter();
        w.writeBinary(gltfModel, new ByteArrayOutputStream());
    }
    
    
}

