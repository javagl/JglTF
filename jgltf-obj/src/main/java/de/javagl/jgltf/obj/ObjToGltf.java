/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.obj;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.io.BinaryGltfDataWriter;
import de.javagl.jgltf.model.io.GltfDataToBinaryConverter;
import de.javagl.jgltf.model.io.GltfDataToEmbeddedConverter;
import de.javagl.jgltf.model.io.GltfDataWriter;
import de.javagl.jgltf.obj.ObjGltfDataCreator.BufferStrategy;

/**
 * A class for converting OBJ files to glTF
 */
public class ObjToGltf
{
    // TODO Offer an option to set the indices component type,
    // via ObjGltfDataCreator#setIndicesComponentType
    
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ObjToGltf.class.getName());
    
    /**
     * The entry point of this application
     * 
     * @param args The command line arguments
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        if (Arrays.asList(args).contains("-h"))
        {
            displayHelp();
            return;
        }
        
        logger.info("Reading command line arguments");
        
        String inputFileName = null;
        String outputFileName = null;
        boolean embedded = false;
        boolean binary = false;
        BufferStrategy bufferStrategy = BufferStrategy.BUFFER_PER_FILE;
        for (String arg : args)
        {
            if (arg.startsWith("-i="))
            {
                inputFileName = arg.substring(3);
            }
            else if (arg.startsWith("-o="))
            {
                outputFileName = arg.substring(3);
            }
            else if (arg.equals("-e"))
            {
                embedded = true;
            }
            else if (arg.equals("-b"))
            {
                binary = true;
            }
            else if (arg.startsWith("-s="))
            {
                String s = arg.substring(3);
                if (s.equalsIgnoreCase("file"))
                {
                    bufferStrategy = BufferStrategy.BUFFER_PER_FILE;
                }
                else if (s.equalsIgnoreCase("group"))
                {
                    bufferStrategy = BufferStrategy.BUFFER_PER_GROUP;
                }
                else if (s.equalsIgnoreCase("part"))
                {
                    bufferStrategy = BufferStrategy.BUFFER_PER_PART;
                }
                else
                {
                    System.err.println("Invalid buffer strategy: " + s);
                    displayHelp();
                    return;
                }
            }
            else
            {
                System.err.println("Invalid argument: " + arg);
                displayHelp();
                return;
            }
        }
        if (inputFileName == null)
        {
            System.err.println("No input file name given");
            displayHelp();
            return;
        }
        if (outputFileName == null)
        {
            System.err.println("No output path name given");
            displayHelp();
            return;
        }
        
        logger.info("Reading input file " + inputFileName);
        
        long beforeNs = System.nanoTime();
        
        URI objUri = Paths.get(inputFileName).toUri();
        GltfData gltfData = 
            new ObjGltfDataCreator(bufferStrategy).create(objUri);

        if (binary)
        {
            logger.info("Converting to binary glTF");
            gltfData = new GltfDataToBinaryConverter().convert(gltfData);
            
            logger.info("Writing binary glTF to " + outputFileName);
            new BinaryGltfDataWriter().writeBinaryGltfData(
                gltfData, outputFileName);
        }
        else
        {
            if (embedded)
            {
                logger.info("Converting to embedded glTF");
                gltfData = new GltfDataToEmbeddedConverter().convert(gltfData);
            }
    
            logger.info("Writing glTF to " + outputFileName);
            new GltfDataWriter().writeGltfData(gltfData, outputFileName);
        }
        
        long afterNs = System.nanoTime();
        double durationS = (afterNs - beforeNs) * 1e-9;
        String durationString =
            String.format(Locale.ENGLISH, "%.3fs", durationS);
        logger.info("Done. " + durationString);
    }

    /**
     * Print a short help message to the console
     */
    private static void displayHelp()
    {
        System.out.println("Usage: ");
        System.out.println("======");
        System.out.println("");
        System.out.println("  -i=<file> : The input file name");
        System.out.println("  -o=<file> : The output file name");
        System.out.println("  -e        : Write the output as embedded glTF");
        System.out.println("  -b        : Write the output as binary glTF");
        System.out.println("  -s=       : The buffer strategy:");
        System.out.println("     file   : Create one buffer for the whole file");
        System.out.println("     group  : Create one buffer for each material group");
        System.out.println("     part   : Create one buffer for each part");
        System.out.println("  -h        : Print this message.");
        System.out.println("");
        System.out.println("Example: ");
        System.out.println("========");
        System.out.println("");
        System.out.println("  ObjToGltf -b -s=part -i=C:/Input/Example.obj " + 
            "-o=C:/Output/Example/");
    }
}
