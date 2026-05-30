/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.transform.test;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * Tests for this package
 */
@SuppressWarnings("javadoc")
public class TestGltfModelTransforms
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(TestGltfModelTransforms.class.getName());
    
    /**
     * The directory that contains the test models
     */
    private static final String TEST_MODELS_DIRECTORY =
        "./src/test/resources/testModels/";

    private static final boolean CREATE_GOLDEN = false;

    @Test
    public void testTexturedSquareRemoveTexture() throws IOException
    {
        String basePath = TEST_MODELS_DIRECTORY;

        String expectedBasePath = basePath + "golden";
        if (CREATE_GOLDEN)
        {
            logger.info("Creating reference output");
            GltfModelTransformsTest.setBaseDirectory(expectedBasePath);
            GltfModelTransformsTest.testRemoveTexture();
        }

        String actualBasePath = basePath + "output";
        GltfModelTransformsTest.setBaseDirectory(actualBasePath);
        GltfModelTransformsTest.testRemoveTexture();

        String name = "TexturedSquare";
        String modifiedName = name + "-removedTexture";
        assertDirectoriesEqual(expectedBasePath, actualBasePath, name,
            modifiedName);
    }

    private static void assertDirectoriesEqual(String expectedBasePath,
        String actualBasePath, String... names) throws IOException
    {
        for (String name : names)
        {
            String expectedPath = expectedBasePath + "/" + name;
            String actualPath = actualBasePath + "/" + name;
            TestUtils.assertDirectoriesEqual(expectedPath, actualPath);
        }
    }

}
