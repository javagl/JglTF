/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import de.javagl.jgltf.model.io.JacksonUtils;

/**
 * An "integration test" level class that just reads all glTF sample models
 * from the glTF-Sample-Models repository.
 */
public class ReadSampleModels
{
    /**
     * The entry point of this test
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        LoggerUtil.initLogging();

        URI rootUri = URI.create("https://raw.githubusercontent.com/"
            + "KhronosGroup/glTF-Sample-Models/master/");
        
        // For Reading from a local directory: 
        //rootUri = URI.create("file:/C:/glTF-Sample-Models/");

        readModels(rootUri.resolve("1.0/"));
        readModels(rootUri.resolve("2.0/"));

    }

    /**
     * Read all sample models that are found in the 'model-index.json' file
     * of the given directory
     * 
     * @param baseUri The URI indicating the base directory
     * @throws IOException If an IO error occurs
     */
    private static void readModels(URI baseUri) throws IOException
    {
        List<ModelIndexEntry> entries =
            readModelIndex(baseUri.resolve("model-index.json"));
        readModels(baseUri, entries);
    }

    /**
     * Read all sample models from the given base directory, based on the
     * {@link ModelIndexEntry} objects that have been read from the 
     * 'model-index.json' file.
     * 
     * @param baseUri The URI indicating the base directory
     * @param entries The {@link ModelIndexEntry} objects
     * @throws IOException If an IO error occurs
     */
    private static void readModels(URI baseUri, List<ModelIndexEntry> entries)
        throws IOException
    {
        System.out.println("Reading models from " + baseUri);
        
        int modelCounter = 0;
        int variantCounter = 0;
        Set<URI> failedUris = new LinkedHashSet<URI>();
        
        GltfModelReader reader = new GltfModelReader();
        for (ModelIndexEntry entry : entries)
        {
            System.out.println("Reading " + entry.name);
            String entryDirectoryName = entry.name.replaceAll(" ", "%20");

            for (Entry<String, String> variant : entry.variants.entrySet())
            {
                String variantName = variant.getKey();
                System.out.println("    Reading " + variantName);

                String variantFileName =
                    variant.getValue().replaceAll(" ", "%20");
                String variantDirectoryName =
                    variantName.replaceAll(" ", "%20");
                URI variantUri = baseUri
                    .resolve(entryDirectoryName + "/")
                    .resolve(variantDirectoryName + "/")
                    .resolve(variantFileName);
                try
                {
                    GltfModel model = reader.read(variantUri);
                    System.out.println("    Reading " + variantName
                        + " DONE, result " + model);
                }
                catch (IOException e)
                {
                    System.out.println("    Reading " + variantName 
                        + " FAILED!");
                    failedUris.add(variantUri);
                }
                variantCounter++;
            }
            modelCounter++;
        }
        System.out.println("Read " 
            + modelCounter + " models, " 
            + variantCounter + " variants");
        if (failedUris.isEmpty())
        {
            System.out.println("No failures");
        }
        else
        {
            System.out.println("Failures:");
            for (URI failedUri : failedUris)
            {
                System.out.println("    " + failedUri);
            }

        }
    }

    /**
     * Read the file from the given URI, assuming that it contains the data of a
     * 'model-index.json' file from the glTF-Sample-Models repository.
     * 
     * @param modelIndexUri The model index URI
     * @return The {@link ModelIndexEntry} objects
     * @throws IOException If an IO error occurs
     */
    private static List<ModelIndexEntry> readModelIndex(URI modelIndexUri)
        throws IOException
    {
        System.out.println("Reading model index from " + modelIndexUri);
        ObjectMapper objectMapper = JacksonUtils.createObjectMapper();
        ModelIndexEntry[] entries = objectMapper
            .readValue(modelIndexUri.toURL(), ModelIndexEntry[].class);
        return Arrays.asList(entries);
    }

}
