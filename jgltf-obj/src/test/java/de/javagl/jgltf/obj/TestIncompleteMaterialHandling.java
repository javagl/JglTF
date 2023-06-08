/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2022 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.obj;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.io.GltfModelWriter;
import de.javagl.jgltf.obj.model.ObjGltfModelCreator;

/**
 * A basic test for the handling of MTL file names that do not contain
 * certain material properties, and that have to be substituted with
 * default values after reading.
 * 
 * This is mainly addressing the API change of the OBJ library between
 * 0.3.0 and 0.4.0: In the latter, values that have not been set in
 * the MTL are reported by returning <code>null</code> from the
 * respective functions. 
 */
@SuppressWarnings("javadoc")
class TestIncompleteMaterialHandling
{
    public static void main(String[] args) throws IOException
    {
        // The 'twoMaterials.obj' does not contain the 'mtllib' keyword.
        // By default, it should read two materials from 'twoMaterials.mtl'.
        runTest("usingIncompleteMaterial");
    }
    
    private static void runTest(String baseName) throws IOException
    {
        System.out.println("Running test for " + baseName);
        
        Path basePath = Paths.get("src/test/resources");
        Files.createDirectories(basePath.resolve("output"));

        Path objPath = basePath.resolve(baseName+".obj");
        ObjGltfModelCreator gltfModelCreator = new ObjGltfModelCreator();
        GltfModel gltfModel = gltfModelCreator.create(objPath.toUri());

        List<MaterialModel> materialModels = gltfModel.getMaterialModels();
        System.out.println("Read " + materialModels.size() + " materials");

        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        Path glbPath = basePath.resolve("output/"+baseName+".glb");
        gltfModelWriter.writeBinary(gltfModel, glbPath.toFile());        
    }
}
