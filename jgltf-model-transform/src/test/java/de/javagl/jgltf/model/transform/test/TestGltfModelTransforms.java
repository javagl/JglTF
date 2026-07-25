/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.transform.test;

import java.io.IOException;
import java.util.logging.Level;
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
     * The log level
     */
    private static Level level = Level.INFO;

    /**
     * The directory that contains the test models
     */
    private static final String TEST_MODELS_DIRECTORY =
        "./src/test/resources/testModels/";

    /**
     * Whether the golden reference files should be created
     */
    private static boolean createReference = false;

    /**
     * The entry point for creating the golden reference files
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        LoggerUtil.initLogging();
        TestGltfModelTransforms t = new TestGltfModelTransforms();

        logger.warning("Creating reference data");
        TestGltfModelTransforms.createReference = true;

        t.testRemoveTexture();
        t.testRemoveTexCoordAccessor();
        t.testRemoveMaterial();
        t.testAddTexture();
        t.testRemoveClearcoatTexture();
        t.testRemoveClearcoatTextureInfoTexture();
        t.testRemoveClearcoatTextureInfo();
        t.testRemoveAnimationValuesAccessor();
        t.testRemoveSingleAnimationValuesAccessor();
        t.testRemoveSkinAnimationTimesAccessor();
        t.testRemoveSkinAttributes();
        t.testAddAnimation();
        t.testRemoveInstancingAccessor();
        t.testAddDraco();
        t.testRemoveDraco();
        t.testRemoveMorphAnimationTimesAccessor();
        t.testRemoveMorphTargetAccessor();
        t.testAddInstancing();
        t.testRemoveTextureTransform();
        t.testAddClearcoatTextureTransform();
    }

    @Test
    public void testRemoveTexture() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestRemoveTexture();
        runTestCase(t);
    }

    @Test
    public void testRemoveTexCoordAccessor() throws IOException
    {
        TestCase t =
            GltfModelTransformsTests.createTestRemoveTexCoordAccessor();
        runTestCase(t);
    }

    @Test
    public void testRemoveMaterial() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestRemoveMaterial();
        runTestCase(t);
    }

    @Test
    public void testAddTexture() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestAddTexture();
        runTestCase(t);
    }

    @Test
    public void testRemoveClearcoatTexture() throws IOException
    {
        TestCase t =
            GltfModelTransformsTests.createTestRemoveClearcoatTexture();
        runTestCase(t);
    }

    @Test
    public void testRemoveClearcoatTextureInfoTexture() throws IOException
    {
        TestCase t = GltfModelTransformsTests
            .createTestRemoveClearcoatTextureInfoTexture();
        runTestCase(t);
    }

    @Test
    public void testRemoveClearcoatTextureInfo() throws IOException
    {
        TestCase t =
            GltfModelTransformsTests.createTestRemoveClearcoatTextureInfo();
        runTestCase(t);
    }

    @Test
    public void testRemoveAnimationValuesAccessor() throws IOException
    {
        TestCase t =
            GltfModelTransformsTests.createTestRemoveAnimationValuesAccessor();
        runTestCase(t);
    }

    @Test
    public void testRemoveSingleAnimationValuesAccessor() throws IOException
    {
        TestCase t = GltfModelTransformsTests
            .createTestRemoveSingleAnimationValuesAccessor();
        runTestCase(t);
    }

    @Test
    public void testRemoveSkinAnimationTimesAccessor() throws IOException
    {
        TestCase t = GltfModelTransformsTests
            .createTestRemoveSkinAnimationTimesAccessor();
        runTestCase(t);
    }

    @Test
    public void testRemoveSkinAttributes() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestRemoveSkinAttributes();
        runTestCase(t);
    }

    @Test
    public void testAddAnimation() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestAddAnimation();
        runTestCase(t);
    }

    @Test
    public void testRemoveInstancingAccessor() throws IOException
    {
        TestCase t =
            GltfModelTransformsTests.createTestRemoveInstancingAccessor();
        runTestCase(t);
    }

    @Test
    public void testAddDraco() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestAddDraco();
        runTestCase(t);
    }

    @Test
    public void testRemoveDraco() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestRemoveDraco();
        runTestCase(t);
    }

    @Test
    public void testRemoveMorphAnimationTimesAccessor() throws IOException
    {
        TestCase t = GltfModelTransformsTests
            .createTestRemoveMorphAnimationTimesAccessor();
        runTestCase(t);
    }

    @Test
    public void testRemoveMorphTargetAccessor() throws IOException
    {
        TestCase t =
            GltfModelTransformsTests.createTestRemoveMorphTargetAccessor();
        runTestCase(t);
    }

    @Test
    public void testAddInstancing() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestAddInstancing();
        runTestCase(t);
    }
    
    @Test
    public void testRemoveTextureTransform() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestRemoveTextureTransform();
        runTestCase(t);
    }

    @Test
    public void testAddClearcoatTextureTransform() throws IOException
    {
        TestCase t = GltfModelTransformsTests.createTestAddClearcoatTextureTransform();
        runTestCase(t);
    }

    /**
     * Run the given test case
     * 
     * @param t The test case
     * @throws IOException If an IO error occurs
     */
    private static void runTestCase(TestCase t) throws IOException
    {
        GltfModelTransformsTests.setBaseDirectory(getActualBasePath());
        GltfModelTransformsTests.runTest(t);

        String message = "Running " + t.name + " to " + t.modifiedName;
        logger.log(level, message);

        TestUtils.assertDirectoriesEqual(getExpectedBasePath(),
            getActualBasePath(), t.name, t.modifiedName);
    }

    /**
     * Returns the base path for the "golden" output
     * 
     * @return The base path
     */
    private static String getExpectedBasePath()
    {
        String basePath = TEST_MODELS_DIRECTORY;
        String expectedBasePath = basePath + "golden";
        return expectedBasePath;
    }

    /**
     * Returns the base path for the actual output.
     * 
     * If {@link #createReference} is <code>true</code>, then this will be the
     * base path for the "golden" output.
     * 
     * @return The actual base path
     */
    private static String getActualBasePath()
    {
        if (createReference)
        {
            return getExpectedBasePath();
        }
        String basePath = TEST_MODELS_DIRECTORY;
        String expectedBasePath = basePath + "output";
        return expectedBasePath;
    }

}
