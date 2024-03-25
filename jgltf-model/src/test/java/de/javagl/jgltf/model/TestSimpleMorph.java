/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.model.io.GltfModelReader;

/**
 * Tests for the {@link GltfModel} class.<br>
 * <br>
 * This tests whether the standard model reading functionality properly
 * reads the "SimpleMorph" sample asset.
 */
@SuppressWarnings("javadoc")
public class TestSimpleMorph
{
    @Test
    public void testReadWeights() throws IOException 
    {
        // Regression test for https://github.com/javagl/JglTF/issues/101
        String inputPath = "./src/test/resources/testModels/" + 
            "SimpleMorph/glTF-Embedded/SimpleMorph.gltf"; 

        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(Paths.get(inputPath));
        
        List<MeshModel> meshes = gltfModel.getMeshModels();
        MeshModel mesh = meshes.get(0);
        float expected[] = new float[] { 0.5f, 0.5f };
        float actual[] = mesh.getWeights();
        assertArrayEquals(expected,  actual, 0.0f);
    }
    
    
}

