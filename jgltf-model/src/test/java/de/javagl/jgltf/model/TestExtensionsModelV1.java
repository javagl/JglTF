/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.model.impl.DefaultExtensionsModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.v1.GltfModelCreatorV1;
import de.javagl.jgltf.model.v1.GltfCreatorV1;

/**
 * Tests for the extensions handling in the {@link GltfModelV1}<br>
 */
@SuppressWarnings("javadoc")
public class TestExtensionsModelV1
{
    @Test
    public void testModelFromGltfV1() throws IOException
    {
        // Note that glTF 1.0 did not have 'extensionsRequired'
        List<String> expectedExtensionsUsed =
            Arrays.asList("TEST_extension_A", "TEST_extension_B");

        GlTF gltf = new GlTF();
        gltf.setExtensionsUsed(expectedExtensionsUsed);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV1.create(new GltfAssetV1(gltf, null));

        ExtensionsModel extensionsModel = gltfModel.getExtensionsModel();

        List<String> actualExtensionsUsed = extensionsModel.getExtensionsUsed();
        assertEquals(expectedExtensionsUsed, actualExtensionsUsed);
    }

    @Test
    public void tesGltfFromModelV1() throws IOException
    {
        // Note that glTF 1.0 did not have 'extensionsRequired'
        List<String> expectedExtensionsUsed =
            Arrays.asList("TEST_extension_A", "TEST_extension_B");

        DefaultGltfModel gltfModel = new DefaultGltfModel();
        DefaultExtensionsModel extensionsModel = gltfModel.getExtensionsModel();
        extensionsModel.addExtensionsUsed(expectedExtensionsUsed);

        GlTF gltf = GltfCreatorV1.create(gltfModel);

        List<String> actualExtensionsUsed = gltf.getExtensionsUsed();
        assertEquals(expectedExtensionsUsed, actualExtensionsUsed);

    }

}
