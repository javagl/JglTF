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

import de.javagl.jgltf.impl.v2.Asset;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.v2.GltfCreatorV2;
import de.javagl.jgltf.model.v2.GltfModelCreatorV2;

/**
 * Tests for the basic handling of the {@link AssetModel} for glTF 2.0
 */
@SuppressWarnings("javadoc")
public class TestAssetModelV2
{
    @Test
    public void testAssetRoundtripV2() throws IOException
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
        // The input version will be ignored. The result will be version 2.0.
        inputAsset.setVersion("NOT_USED"); 
        inputGltf.setAsset(inputAsset);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV2.create(new GltfAssetV2(inputGltf, null));

        GlTF outputGltf = GltfCreatorV2.create(gltfModel);
        Asset outputAsset = outputGltf.getAsset();
        assertEquals(inputAsset.getExtras(), outputAsset.getExtras());
        assertEquals(inputAsset.getExtensions(), outputAsset.getExtensions());
        assertEquals(inputAsset.getGenerator(), outputAsset.getGenerator());
        assertEquals(inputAsset.getCopyright(), outputAsset.getCopyright());
        assertEquals("2.0", outputAsset.getVersion());
    }
    
    @Test
    public void testAssetDefaultV2() throws IOException
    {
        GlTF inputGltf = new GlTF();
        Asset inputAsset = new Asset();
        inputGltf.setAsset(inputAsset);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV2.create(new GltfAssetV2(inputGltf, null));

        GlTF outputGltf = GltfCreatorV2.create(gltfModel);
        Asset outputAsset = outputGltf.getAsset();
        assertEquals(inputAsset.getExtras(), outputAsset.getExtras());
        assertEquals(inputAsset.getExtensions(), outputAsset.getExtensions());
        assertEquals(inputAsset.getCopyright(), outputAsset.getCopyright());
        
        String expectedGenerator = "JglTF from https://github.com/javagl/JglTF";
        assertEquals(expectedGenerator, outputAsset.getGenerator());
        assertEquals("2.0", outputAsset.getVersion());
    }
    
    @Test
    public void testAssetModificationV2() throws IOException
    {
        GlTF inputGltf = new GlTF();
        Asset inputAsset = new Asset();
        inputGltf.setAsset(inputAsset);

        DefaultGltfModel gltfModel =
            GltfModelCreatorV2.create(new GltfAssetV2(inputGltf, null));

        String expectedCopyright = "testCopyright";
        gltfModel.getAssetModel().setCopyright(expectedCopyright);

        Map<String, Object> expectedExtensions =
            new LinkedHashMap<String, Object>();
        expectedExtensions.put("testExtension", "testExtensionValue");
        gltfModel.getAssetModel().setExtensions(expectedExtensions);

        GlTF outputGltf = GltfCreatorV2.create(gltfModel);
        Asset outputAsset = outputGltf.getAsset();
        assertEquals(inputAsset.getExtras(), outputAsset.getExtras());
        assertEquals(expectedExtensions, outputAsset.getExtensions());
        assertEquals(expectedCopyright, outputAsset.getCopyright());

        String expectedGenerator = "JglTF from https://github.com/javagl/JglTF";
        assertEquals(expectedGenerator, outputAsset.getGenerator());
        assertEquals("2.0", outputAsset.getVersion());
    }
    

}
