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

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.impl.DefaultExtensionsModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.v2.GltfModelCreatorV2;
import de.javagl.jgltf.model.v2.GltfCreatorV2;

/**
 * Tests for the extensions handling in the {@link GltfModelV2}<br>
 */
@SuppressWarnings("javadoc")
public class TestExtensionsModelV2
{
    @Test
    public void testModelFromGltfV2() throws IOException
    {
        List<String> expectedExtensionsUsed =
            Arrays.asList("TEST_extension_A", "TEST_extension_B");
        List<String> expectedExtensionsRequired =
            Arrays.asList("TEST_extension_A");

        GlTF gltf = new GlTF();
        gltf.setExtensionsUsed(expectedExtensionsUsed);
        gltf.setExtensionsRequired(expectedExtensionsRequired);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV2.create(new GltfAssetV2(gltf, null));

        ExtensionsModel extensionsModel = gltfModel.getExtensionsModel();

        List<String> actualExtensionsUsed = extensionsModel.getExtensionsUsed();
        assertEquals(expectedExtensionsUsed, actualExtensionsUsed);

        List<String> actualExtensionsRequired =
            extensionsModel.getExtensionsRequired();
        assertEquals(expectedExtensionsRequired, actualExtensionsRequired);

    }

    @Test
    public void tesGltfFromModelV2() throws IOException
    {
        List<String> expectedExtensionsUsed =
            Arrays.asList("TEST_extension_A", "TEST_extension_B");
        List<String> expectedExtensionsRequired =
            Arrays.asList("TEST_extension_A");

        DefaultGltfModel gltfModel = new DefaultGltfModel();
        DefaultExtensionsModel extensionsModel = gltfModel.getExtensionsModel();
        extensionsModel.addExtensionsUsed(expectedExtensionsUsed);
        extensionsModel.addExtensionsRequired(expectedExtensionsRequired);

        GlTF gltf = GltfCreatorV2.create(gltfModel);

        List<String> actualExtensionsUsed = gltf.getExtensionsUsed();
        assertEquals(expectedExtensionsUsed, actualExtensionsUsed);

        List<String> actualExtensionsRequired =
            gltf.getExtensionsRequired();
        assertEquals(expectedExtensionsRequired, actualExtensionsRequired);
    }

}
