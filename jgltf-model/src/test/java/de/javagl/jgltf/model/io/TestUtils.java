/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Utility methods for the test package
 */
class TestUtils
{
    /**
     * Asserts that the files in the given directories are equal, byte-wise,
     * except for the line separators. <br>
     * <br>
     * This will compare the number of files in the specified directories,
     * and then compare each file with 
     * {@link #assertFileEquals(String, String, String, String)}.
     * 
     * @param directoryExpected The directory of the expected file
     * @param directoryActual The directory of the actual file
     * @throws IOException If any file cannot be read
     * @throws AssertionError If the directories are not equal 
     */
    static void assertDirectoriesEqual(
        String directoryExpected, 
        String directoryActual) throws IOException 
    {
        File[] filesExpected = 
            Paths.get(directoryExpected).toFile().listFiles();
        assertNotNull("The expected files do not exist", filesExpected);
        File[] filesActual = 
            Paths.get(directoryActual).toFile().listFiles();
        assertNotNull("The actual files do not exist", filesActual);
        Arrays.sort(filesExpected);
        Arrays.sort(filesActual);
        assertEquals("Expected " + filesExpected.length + " files but found "
            + filesActual.length, filesExpected.length, filesActual.length);
        
        for (int i = 0; i < filesExpected.length; i++)
        {
            String fileNameExpected = filesExpected[i].getName();
            String fileNameActual = filesActual[i].getName();
            assertFileEquals(
                directoryExpected, fileNameExpected, 
                directoryActual, fileNameActual);
        }
        
    }
    
    /**
     * Asserts that the files at the given locations are equal, byte-wise,
     * except for the line separators. <br>
     * <br>
     * All bytes of the specified files will be read, and the line separators 
     * will be normalized using {@link #normalizeLineSeparators(byte[])}.
     * 
     * @param directoryExpected The directory of the expected file
     * @param fileNameExpected The name of the expected file
     * @param directoryActual The directory of the actual file
     * @param fileNameActual The name of the actual file
     * @throws IOException If any file cannot be read
     * @throws AssertionError If the files are not equal 
     */
    static void assertFileEquals(
        String directoryExpected, String fileNameExpected, 
        String directoryActual, String fileNameActual) throws IOException
    {
        Path pathExpected = Paths.get(directoryExpected, fileNameExpected);
        byte bytesExpected[] = Files.readAllBytes(pathExpected);
        String stringExpected = normalizeLineSeparators(bytesExpected);
        Path pathActual = Paths.get(directoryActual, fileNameActual);
        byte bytesActual[] = Files.readAllBytes(pathActual);
        String stringActual = normalizeLineSeparators(bytesActual);
        assertEquals(stringExpected, stringActual);
    }
    
    /**
     * Normalize the line separators in the given string, by replacing 
     * all <code>"\r\n"</code> and <code>"\r"</code> with <code>"\n"</code>
     *   
     * @param s The input string
     * @return The normalized string
     */
    private static String normalizeLineSeparators(String s)
    {
        String result = s;
        result = result.replaceAll("\\r\\n", "\n");
        result = result.replaceAll("\\r", "\n");
        return result;
    }

    /**
     * Convert the given bytes into a string, using the default charset,
     * and normalize the line separators, by replacing all <code>"\r\n"</code>
     * and <code>"\r"</code> with <code>"\n"</code>
     *   
     * @param bytes The bytes
     * @return The normalized string
     */
    private static String normalizeLineSeparators(byte bytes[])
    {
        return normalizeLineSeparators(new String(bytes));
    }

    /**
     * Private constructor to prevent instantiation
     */
    private TestUtils()
    {
        // Private constructor to prevent instantiation
    }
}
