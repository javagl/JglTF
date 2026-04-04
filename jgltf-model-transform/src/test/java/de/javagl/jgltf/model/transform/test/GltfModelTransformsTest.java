/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2026 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.transform.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.io.GltfModelWriter;
import de.javagl.jgltf.model.transform.GltfModelTransforms;

/**
 * Basic tests for this package
 */
public class GltfModelTransformsTest
{
    /**
     * The base directory for the files that are written
     */
    private static final Path BASE_DIR = Paths.get("./data/");

    /**
     * The entry point
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        LoggerUtil.initLogging();
        //testRemoveTexture();
        //testRemoveTexCoordAccessor();
        testAddTexture();
    }

    /**
     * Run a test to remove a texture
     * 
     * @throws IOException If an IO error occurs
     */
    private static void testRemoveTexture() throws IOException
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-removedTexture";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();

        runTest(gltfModel, name, modifiedName, () -> 
        {
            DefaultTextureModel tm = gltfModel.getTextureModel(0);
            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(tm);
            GltfModelTransforms.removeAll(gltfModel, toRemove);
        });
    }
    
    /**
     * Run a test to remove a texture coordinate accessor
     *  
     * @throws IOException If an IO error occurs
     */
    private static void testRemoveTexCoordAccessor() throws IOException
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-removedTexCoordAccessor";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();

        runTest(gltfModel, name, modifiedName, () -> 
        {
            // Removing the TEXCOORDS accessor will make the assed invalid
            // because the material requires texture coordinates. Set the
            // material to null explicitly.
            DefaultMeshModel m0 = gltfModel.getMeshModel(0);
            DefaultMeshPrimitiveModel m0p0 = 
                (DefaultMeshPrimitiveModel) m0.getMeshPrimitiveModels().get(0);
            m0p0.setMaterialModel(null);
            
            DefaultAccessorModel am2 = gltfModel.getAccessorModel(2);
            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(am2);
            GltfModelTransforms.removeAll(gltfModel, toRemove);
        });
    }

    

    /**
     * Run a test to remove a texture
     * 
     * @throws IOException If an IO error occurs
     */
    private static void testAddTexture() throws IOException
    {
        String name = "SquareWithTexcoords";
        String modifiedName = name + "-addedTexture";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createSquareWithTexcoords();

        runTest(gltfModel, name, modifiedName, () -> 
        {
            DefaultPbrMaterialModel materialModel = 
                GltfTestModelCreation.createBaseColorTextureMaterialModel();
            DefaultMeshModel m0 = gltfModel.getMeshModel(0);
            DefaultMeshPrimitiveModel m0p0 = 
                (DefaultMeshPrimitiveModel) m0.getMeshPrimitiveModels().get(0);
            m0p0.setMaterialModel(materialModel);
            
            GltfModelTransforms.revalidate(gltfModel);
        });
    }
    
    
    
    
    
    //=========================================================================
    // Utility functions for the tests
    
    /**
     * Write the given model to a file with the given name, execute the given
     * operation, and write the modified result to another file
     * 
     * @param gltfModel The glTF model
     * @param name The original name
     * @param modifiedName The modified name
     * @param op The operation
     * @throws IOException If an IO error occurs
     */
    private static void runTest(DefaultGltfModel gltfModel, String name,
        String modifiedName, Runnable op) throws IOException
    {
        File originalFile = prepareOutput(name, ".glb");
        File modifiedFile = prepareOutput(modifiedName, ".gltf");

        GltfModelWriter w = new GltfModelWriter();
        w.writeBinary(gltfModel, originalFile);
        op.run();
        w.write(gltfModel, modifiedFile);
    }

    /**
     * Prepare the specified output file for the test.
     * 
     * This will be of the form './data/name/name.extension'
     * 
     * @param name The name of the subdirectory and the file
     * @param extensionWithDot The file extension
     * @return The file
     * @throws IOException If an IO error occurs
     */
    private static File prepareOutput(String name, String extensionWithDot)
        throws IOException
    {
        Path dir = BASE_DIR.resolve(name);
        Files.createDirectories(dir);
        String fileName = name + extensionWithDot;
        File file = dir.resolve(fileName).toFile();
        return file;
    }

}
