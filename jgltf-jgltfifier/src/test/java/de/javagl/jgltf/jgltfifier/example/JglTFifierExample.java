/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.jgltfifier.example;

import java.io.IOException;
import java.net.URI;

import de.javagl.jgltf.jgltfifier.Config;
import de.javagl.jgltf.jgltfifier.JglTFifier;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;

/**
 * An example of JglTFifier
 */
public class JglTFifierExample
{
    /**
     * The entry point of the example
     * 
     * @param args Not uses
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        LoggerUtil.initLogging();


        // Read one of the glTF sample models
        String baseDir = "https://raw.githubusercontent.com/KhronosGroup/"
            + "glTF-Sample-Assets/main/Models/";
        String modelName = "BoomBox";
        String modelUrl =
            baseDir + modelName + "/glTF-Binary/" + modelName + ".glb";
        URI modelUri = URI.create(modelUrl);

        GltfModelReader r = new GltfModelReader();
        GltfModel gltfModel = r.read(modelUri);

        // Create a JglTFifier
        JglTFifier g = new JglTFifier();
        
        // Some preliminary configuration settings for this example...:
        Config config = new Config();
        config.className = "Generate_" + modelName;
        config.packageName = "de.javagl.jgltf.jgltfifier.generated";
        config.sourceCodeRootDirectory = "src/test/java";
        config.outputGltfFileName = "data/Generated_" + modelName + ".glb";
        config.generatedDataDirectory = "data/for_" + modelName;
        
        // Generate the class. This may write accessor or image data
        // into the 'generatedDataDirectory'
        g.generate(gltfModel, config);
        
        // Write the actual class into the specified package
        g.writeGeneratedClass();
        
        // The class called "Generate_<modelName>" that is now located
        // in the package "de.javagl.jgltf.jgltfifier.generated" contains
        // a main method. Running it will (read the accessor- and image
        // data that has been extracted, and) write the original input model
        // to "./data/Generated_<modelName>.glb".
    }
}
