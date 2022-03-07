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
 * A basic test for the handling of MTL file names that are stored in
 * OBJ files, for the use in the ObjGltfModelCreator
 * 
 * Reported at https://github.com/javagl/Obj/issues/20
 */
@SuppressWarnings("javadoc")
class TestMtlFileHandling
{
    public static void main(String[] args) throws IOException
    {
        // The 'twoMaterials.obj' does not contain the 'mtllib' keyword.
        // By default, it should read two materials from 'twoMaterials.mtl'.
        runTest("twoMaterials");
        
        // The 'usingTwoMaterials.obj' refers to 'twoMaterials.mtl'. 
        // Before the issue was fixed, this reference was not resolved,
        // and a default material was used. Now, it uses the materials 
        // from 'twoMaterials.mtl'
        runTest("usingTwoMaterials");
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
