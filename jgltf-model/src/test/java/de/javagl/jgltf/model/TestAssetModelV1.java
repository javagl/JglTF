/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import de.javagl.jgltf.impl.v1.Asset;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.v1.GltfCreatorV1;
import de.javagl.jgltf.model.v1.GltfModelCreatorV1;

/**
 * Tests for the basic handling of the {@link AssetModel} for glTF 1.0
 */
@SuppressWarnings("javadoc")
public class TestAssetModelV1
{
    @Test
    public void testAssetRoundtripV1() throws IOException
    {
        Map<String, Object> extras = new LinkedHashMap<String, Object>();
        extras.put("extrasKey0", "extrasValue0");

        Map<String, Object> extensions = new LinkedHashMap<String, Object>();
        extensions.put("extensionsKey0", "extensionsValue0");

        GlTF inputGltf = new GlTF();
        Asset inputAsset = new Asset();
        inputAsset.setExtras(extras);
        inputAsset.setExtensions(extensions);
        inputAsset.setCopyright("testCopyright");
        inputAsset.setGenerator("testGenerator");
        // The input version will be ignored. The result will be version 1.0.
        inputAsset.setVersion("NOT_USED");
        inputGltf.setAsset(inputAsset);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV1.create(new GltfAssetV1(inputGltf, null));

        GlTF outputGltf = GltfCreatorV1.create(gltfModel);
        Asset outputAsset = outputGltf.getAsset();
        assertEquals(inputAsset.getExtras(), outputAsset.getExtras());
        assertEquals(inputAsset.getExtensions(), outputAsset.getExtensions());
        assertEquals(inputAsset.getGenerator(), outputAsset.getGenerator());
        assertEquals(inputAsset.getCopyright(), outputAsset.getCopyright());
        assertEquals("1.0", outputAsset.getVersion());
    }

    @Test
    public void testAssetDefaultV1() throws IOException
    {
        GlTF inputGltf = new GlTF();
        Asset inputAsset = new Asset();
        inputGltf.setAsset(inputAsset);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV1.create(new GltfAssetV1(inputGltf, null));

        GlTF outputGltf = GltfCreatorV1.create(gltfModel);
        Asset outputAsset = outputGltf.getAsset();
        assertEquals(inputAsset.getExtras(), outputAsset.getExtras());
        assertEquals(inputAsset.getExtensions(), outputAsset.getExtensions());
        assertEquals(inputAsset.getCopyright(), outputAsset.getCopyright());

        String expectedGenerator = "JglTF from https://github.com/javagl/JglTF";
        assertEquals(expectedGenerator, outputAsset.getGenerator());
        assertEquals("1.0", outputAsset.getVersion());
    }

    @Test
    public void testAssetModificationV1() throws IOException
    {
        GlTF inputGltf = new GlTF();
        Asset inputAsset = new Asset();
        inputGltf.setAsset(inputAsset);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV1.create(new GltfAssetV1(inputGltf, null));

        String expectedCopyright = "testCopyright";
        gltfModel.getAssetModel().setCopyright(expectedCopyright);

        Map<String, Object> expectedExtensions =
            new LinkedHashMap<String, Object>();
        expectedExtensions.put("testExtension", "testExtensionValue");
        gltfModel.getAssetModel().setExtensions(expectedExtensions);

        GlTF outputGltf = GltfCreatorV1.create(gltfModel);
        Asset outputAsset = outputGltf.getAsset();
        assertEquals(inputAsset.getExtras(), outputAsset.getExtras());
        assertEquals(expectedExtensions, outputAsset.getExtensions());
        assertEquals(expectedCopyright, outputAsset.getCopyright());

        String expectedGenerator = "JglTF from https://github.com/javagl/JglTF";
        assertEquals(expectedGenerator, outputAsset.getGenerator());
        assertEquals("1.0", outputAsset.getVersion());
    }

}
