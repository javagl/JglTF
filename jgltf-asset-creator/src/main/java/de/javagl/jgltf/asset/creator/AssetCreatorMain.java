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
package de.javagl.jgltf.asset.creator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.javagl.jgltf.asset.creator.basic.BasicAssetCreator;
import de.javagl.jgltf.asset.creator.basic.Config;
import de.javagl.jgltf.asset.creator.utilities.CartesianProducts;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.GltfModelWriter;

/**
 * The main class for the asset creator
 */
public class AssetCreatorMain
{
    /**
     * Simple class for the command line arguments
     */
    private static class Arguments
    {
        /**
         * The name of the JSON file containing the configurations
         */
        private String inputFileName;

        /**
         * The name of the directory to which the output files should be written
         */
        private String outputDirectoryName;
    }

    /**
     * The entry point
     * 
     * Valid arguments: <code>-input</code> : The input JSON file name
     * <code>-output</code> : The output directory name
     * 
     * @param args The arguments
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        Arguments arguments = parse(args);
        if (arguments == null)
        {
            printHelp();
            return;
        }

        String inputFileName = arguments.inputFileName;
        String outputDirectoryName = arguments.outputDirectoryName;

        System.out.println("Running:");
        System.out.println("  input file      : " + inputFileName);
        System.out.println("  output directory: " + outputDirectoryName);

        // Read the input file and create all configurations
        File inputFile = new File(inputFileName);
        List<Config> configs =
            CartesianProducts.readFrom(inputFile, Config.class);

        // Ensure that the output exists
        Path outputPath = Paths.get(outputDirectoryName);
        Files.createDirectories(outputPath);

        // Write the JSON containing all configurations to the output
        File outputFile = outputPath.resolve("allConfigs.json").toFile();
        CartesianProducts.writeTo(outputFile, configs);

        // Create one glTF model from each configuration, and write them
        // to the output directory
        GltfModelWriter w = new GltfModelWriter();
        BasicAssetCreator c = new BasicAssetCreator();
        int counter = 0;
        for (Config config : configs)
        {
            System.out
                .println("Creating model " + counter + " of " + configs.size());
            String n = leftPad(String.valueOf(counter), 5, "0");
            String fileName = "Created-" + n + ".glb";
            DefaultGltfModel model = c.create(config);

            File f = outputPath.resolve(fileName).toFile();
            System.out.println("Writing model " + counter + " to " + f);

            w.writeBinary(model, f);
            counter++;
        }
    }

    /**
     * Parse command line arguments.
     * 
     * Returns <code>null</code> if none can be parsed.
     * 
     * @param args The arguments
     * @return The parsed arguments
     */
    private static Arguments parse(String args[])
    {
        if (args.length < 4)
        {
            System.out.println("No arguments given");
            printHelp();

            System.out.println("Attempting default run");
            return parse(new String[]
            { "-input", "configs.json", "-output", "./output" });
        }

        String inputFileName = null;
        String outputDirectoryName = null;
        for (int i = 0; i < args.length; i++)
        {
            String arg = args[i];
            if (arg.equals("-input"))
            {
                if (i < args.length - 1)
                {
                    inputFileName = args[i + 1];
                }
            }
            if (arg.equals("-output"))
            {
                if (i < args.length - 1)
                {
                    outputDirectoryName = args[i + 1];
                }
            }
        }
        if (inputFileName == null || outputDirectoryName == null)
        {
            return null;
        }
        Arguments arguments = new Arguments();
        arguments.inputFileName = inputFileName;
        arguments.outputDirectoryName = outputDirectoryName;
        return arguments;
    }

    /**
     * Print usage info
     */
    private static void printHelp()
    {
        System.out.println("Usage: ");
        System.out.println(
            "  AssetCreatorMain -input exampleInputFile.json -output ./exampleOutputDirectory");
        System.out.println("Arguments: ");
        System.out
            .println("  -input  : The input JSON file for the configurations");
        System.out.println("  -output : The output directory");
    }

    /**
     * Left-pad the given string with the given padding to achieve the given
     * length.
     * 
     * @param s The input
     * @param length The length
     * @param padding The padding
     * @return The result
     */
    private static String leftPad(String s, int length, String padding)
    {
        String result = s;
        while (result.length() < length)
        {
            result = padding + result;
        }
        return result;
    }

}
