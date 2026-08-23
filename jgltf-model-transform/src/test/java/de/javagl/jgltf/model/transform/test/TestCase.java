/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.transform.test;

import java.util.function.Consumer;

import de.javagl.jgltf.model.impl.DefaultGltfModel;

/**
 * Internal class for a single test case of the glTF model transforms
 */
class TestCase
{
    /**
     * The original name of the model
     */
    String name;
    
    /**
     * The name for the modified model
     */
    String modifiedName;
    
    /**
     * The model
     */
    DefaultGltfModel gltfModel;
    
    /**
     * The operation to apply to the model
     */
    Consumer<DefaultGltfModel> op;

    /**
     * Creates a new instance
     * 
     * @param name The original name of the model
     * @param modifiedName The name for the modified model
     * @param gltfModel The model
     * @param op The operation to apply to the model
     * @return The test case
     */
    static TestCase create(String name, String modifiedName,
        DefaultGltfModel gltfModel, Consumer<DefaultGltfModel> op)
    {
        TestCase t = new TestCase();
        t.name = name;
        t.modifiedName = modifiedName;
        t.gltfModel = gltfModel;
        t.op = op;
        return t;
    }
}