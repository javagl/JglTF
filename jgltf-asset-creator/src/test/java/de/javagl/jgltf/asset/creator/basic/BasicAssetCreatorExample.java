/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.asset.creator.basic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import de.javagl.jgltf.asset.creator.utilities.CartesianProducts;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.GltfModelWriter;

/**
 * An example for the {@link BasicAssetCreator}
 */
public class BasicAssetCreatorExample
{
    /**
     * The output directory
     */
    private static final String OUTPUT_DIR = "./data/output/";
    
    /**
     * The entry point
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        createSingle();
        createCartesianProduct();
        createSingleDimension();
    }

    /**
     * Create a single asset with a manually defined configuration
     * 
     * @throws IOException If an IO error occurs
     */
    private static void createSingle() throws IOException
    {
        Config config = new Config();

        config.numMeshPrimitives = 16;
        config.pointSizes = new int[][]
        {
            { 64, 64 },
            { 128, 128 } 
        };
        config.numTextures = 16;
        config.pixelSizes = new int[][]
        {
            { 256, 256 },
            { 512, 512 } 
        };
        config.numMaterials = 16;
        config.numMeshes = 256;
        config.numMeshPrimitivesPerMesh = 1;
        config.numNodes = 512;

        config.noiseGeometry = true;
        config.noiseTextures = true;
        config.gridDimensions = 3;

        BasicAssetCreator c = new BasicAssetCreator();
        DefaultGltfModel model = c.create(config);

        Files.createDirectories(Paths.get(OUTPUT_DIR));
        GltfModelWriter w = new GltfModelWriter();
        w.writeBinary(model, new File(OUTPUT_DIR + "Created.glb"));
    }

    /**
     * Creates a set of assets from the cartesian product of values
     * 
     * @throws IOException If an IO error occurs
     */
    private static void createCartesianProduct() throws IOException
    {
        Object pointSizes = new int[][]
        {
            { 64, 64 },
            { 128, 128 } 
        };
        Object pixelSizes = new int[][]
        {
            { 256, 256 },
            { 512, 512 } 
        };

        List<Config> configs = CartesianProducts.create()
            .with("numMeshPrimitives", 8, 16)
            .with("pointSizes", pointSizes)
            .with("numTextures", 8, 16)
            .with("pixelSizes", pixelSizes)
            .with("numMaterials", 8, 16)
            .with("numMeshes", 8, 16)
            .with("numMeshPrimitivesPerMesh", 1)
            .with("numNodes", 4 * 4 * 4, 8 * 8 * 8, 16 * 16 * 16, 32 * 32 * 32)
            .with("noiseGeometry", true)
            .with("noiseTextures", true)
            .with("gridDimensions", 3)
            .buildAs(Config.class);

        Files.createDirectories(Paths.get(OUTPUT_DIR));
        GltfModelWriter w = new GltfModelWriter();

        BasicAssetCreator c = new BasicAssetCreator();
        for (int i = 0; i < configs.size(); i++)
        {
            Config config = configs.get(i);
            System.out.println("Configuration " + i + " of " + configs.size()
                + ":\n" + Configs.createString(config));

            String fileName = "Created-";
            fileName += "numMeshPrimitives-" + config.numMeshPrimitives + "_";
            fileName += "numTextures-" + config.numTextures + "_";
            fileName += "numMaterials-" + config.numMaterials + "_";
            fileName += "numMeshes-" + config.numMeshes + "_";
            fileName += "numNodes-" + config.numNodes;
            fileName += ".glb";

            System.out.println("Writing " + fileName);
            DefaultGltfModel model = c.create(config);
            w.writeBinary(model, new File(OUTPUT_DIR + fileName));
        }

    }

    /**
     * Creates a set of assets that varies along a single dimension
     * 
     * @throws IOException If an IO error occurs
     */
    private static void createSingleDimension() throws IOException
    {
        Config config = new Config();

        // A single (effective) geometry, i.e. one mesh primitive with its
        // accessors, rendered with different materials that use up to 
        // 'max' textures.
        int max = 128;
        
        config.numMeshPrimitives = 1;
        config.pointSizes = new int[][] { { 128, 128 } };
        config.numTextures = -1; // Varied in the loop below;
        config.pixelSizes = new int[][] { { 512, 512 } };
        config.numMaterials = max;
        config.numMeshes = max; 
        config.numMeshPrimitivesPerMesh = 1;
        config.numNodes = 16 * 16 * 16;

        config.noiseGeometry = true;
        config.noiseTextures = true;
        config.gridDimensions = 3;

        Files.createDirectories(Paths.get(OUTPUT_DIR));
        GltfModelWriter w = new GltfModelWriter();

        BasicAssetCreator c = new BasicAssetCreator();
        for (int numTextures = 1; numTextures <= max; numTextures *= 2)
        {
            config.numTextures = numTextures;
            System.out
                .println("Configuration:\n" + Configs.createString(config));

            String fileName = "Created-";
            fileName += "numTextures-" + numTextures;
            fileName += ".glb";

            System.out.println("Writing " + fileName);
            DefaultGltfModel model = c.create(config);
            w.writeBinary(model, new File(OUTPUT_DIR + fileName));
        }

    }

}
