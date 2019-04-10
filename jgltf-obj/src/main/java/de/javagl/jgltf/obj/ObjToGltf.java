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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelWriter;
import de.javagl.jgltf.obj.model.ObjGltfModelCreator;

/**
 * A class for converting OBJ files to glTF
 */
public class ObjToGltf
{
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
        int indicesComponentType = GltfConstants.GL_UNSIGNED_SHORT;
        int version = 2;
        for (String a : args)
        {
            String arg = a.trim();
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
            else if (arg.startsWith("-c"))
            {
                String s = arg.substring(3);
                if (s.equals("GL_UNSIGNED_BYTE"))
                {
                    indicesComponentType = GltfConstants.GL_UNSIGNED_BYTE;
                }
                else if (s.equals("GL_UNSIGNED_SHORT"))
                {
                    indicesComponentType = GltfConstants.GL_UNSIGNED_SHORT;
                }
                else if (s.equals("GL_UNSIGNED_INT"))
                {
                    indicesComponentType = GltfConstants.GL_UNSIGNED_INT;
                }
                else
                {
                    System.err.println("Invalid indices component type: " + s);
                    displayHelp();
                    return;
                }
            }
            else if (arg.startsWith("-v="))
            {
                String s = arg.substring(3);
                try
                {
                    version = Integer.parseInt(s);
                }
                catch (NumberFormatException e)
                {
                    System.err.println("Invalid argument: " + arg);
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

        GltfModel gltfModel = null;
        ObjGltfModelCreator gltfModelCreator = new ObjGltfModelCreator();
        gltfModelCreator.setIndicesComponentType(indicesComponentType);
        if (version == 1)
        {
            gltfModelCreator.setTechniqueBasedMaterials(true);
        }
        gltfModel = gltfModelCreator.create(objUri);

        GltfModelWriter gltfModelWriter = new GltfModelWriter();
        File outputFile = new File(outputFileName);
        File parentFile = outputFile.getParentFile();
        if (parentFile != null)
        {
            parentFile.mkdirs();
        }
        if (binary)
        {
            logger.info("Writing binary glTF to " + outputFileName);
            gltfModelWriter.writeBinary(gltfModel, outputFile);
        }
        else if (embedded)
        {
            logger.info("Writing embedded glTF to " + outputFileName);
            gltfModelWriter.writeEmbedded(gltfModel, outputFile);
        }
        else
        {
            logger.info("Writing glTF to " + outputFileName);
            gltfModelWriter.write(gltfModel, outputFile);
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
        System.out.println("  -i=<file>   : The input file name");
        System.out.println("  -o=<file>   : The output file name");
        System.out.println("  -e          : Write the output as embedded glTF");
        System.out.println("  -b          : Write the output as binary glTF");
        System.out.println("  -c=<type>   : The indices component type. "
            + "The <type> may be GL_UNSIGNED_BYTE, GL_UNSIGNED_SHORT "
            + "or GL_UNSIGNED_INT. The default is GL_UNSIGNED_SHORT");
        System.out.println("  -h          : Print this message.");
        System.out.println("  -v=<number> : The target glTF version. "
            + "The <number> may be 1 or 2. The default is 2.");
        System.out.println("");
        System.out.println("Example: ");
        System.out.println("========");
        System.out.println("");
        System.out.println("  ObjToGltf -b -s=part -i=C:/Input/Example.obj " 
            + "-o=C:/Output/Example.glb");
    }
}
