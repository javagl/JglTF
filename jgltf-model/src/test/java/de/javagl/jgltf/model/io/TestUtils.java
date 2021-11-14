/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods for the test package
 */
class TestUtils
{
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
