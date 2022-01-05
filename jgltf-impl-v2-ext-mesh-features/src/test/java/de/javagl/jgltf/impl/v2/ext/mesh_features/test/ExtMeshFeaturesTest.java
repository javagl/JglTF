/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.impl.v2.ext.mesh_features.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.ext.mesh_features.GlTFMeshFeatures;
import de.javagl.jgltf.model.extensions.GltfExtensions;
import de.javagl.jgltf.model.io.JacksonUtils;
import de.javagl.jgltf.model.io.v2.GltfReaderV2;

/**
 * A basic test for reading a glTF with EXT_mesh_features
 */
public class ExtMeshFeaturesTest
{
    /**
     * The entry point of this test
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        String dir = "data/";
        String fileName = "EmptyAssetWithExtMeshFeatures.gltf";

        try (InputStream inputStream = new FileInputStream(
            new File(dir + fileName)))
        {
            GltfReaderV2 reader = new GltfReaderV2();
            GlTF gltf = reader.read(inputStream);
            GlTFMeshFeatures gltfMeshFeatures = GltfExtensions.obtain(
                gltf, "EXT_mesh_features", GlTFMeshFeatures.class);
            System.out.println("gltfMeshFeatures: " + gltfMeshFeatures);
            System.out.println("JSON string:");
            System.out.println(createJsonString(gltfMeshFeatures));
        }
    }
    
    /**
     * Create a formatted JSON string representation of the given object.
     * 
     * @param object The object
     * @return The JSON string
     */
    static String createJsonString(Object object)
    {
        try
        {
            ObjectMapper objectMapper = JacksonUtils.createObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(object);
        }
        catch (IOException e)
        {
            return String.valueOf(object);
        }
    }

    
}
