/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Utility methods. These should not be considered as part of the public API.
 */
public class Utils
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(Utils.class.getName());
    
    /**
     * Obtains the value for the given ID from the given map, and throws
     * a <code>GltfException</code> with an appropriate error message
     * if the given map is <code>null</code>, or there is no 
     * non-<code>null</code> value found for the given ID.
     * 
     * @param map The map
     * @param id The ID
     * @param description A description of what was looked up in the map.
     * This will be part of the possible exception message
     * @return The value that was found in the map
     * @throws GltfException If there was no value found in the map
     */
    public static <T> T getChecked(
        Map<String, T> map, String id, String description)
    {
        if (map == null)
        {
            throw new GltfException(
                "No map for looking up " + description + " with ID " + id);
        }
        T result = map.get(id);
        if (result == null)
        {
            throw new GltfException(
                "The " + description + " with ID " + id + " does not exist");
        }
        return result;
    }

    /**
     * Obtains the value for the given ID from the given map. If the given 
     * ID is <code>null</code>, or the map is <code>null</code>, or there 
     * is no non-<code>null</code> value found for the given ID, then a 
     * warning will be printed, and <code>null</code> will be returned.
     * 
     * @param map The map
     * @param id The ID
     * @param description A description of what was looked up in the map.
     * This will be part of the possible log message
     * @return The value that was found in the map
     */
    public static <T> T getExpected(
        Map<String, T> map, String id, String description)
    {
        if (id == null)
        {
            logger.warning("The ID of " + description + " is null");
            return null;
        }
        if (map == null)
        {
            logger.warning( 
                "No map for looking up " + description + " with ID " + id);
            return null;
        }
        T result = map.get(id);
        if (result == null)
        {
            logger.warning( 
                "The " + description + " with ID " + id + " does not exist");
        }
        return result;
    }
    
    
    /**
     * Validate that the given array is not <code>null</code> and has the
     * given length. If this is not the case, return a new array with the
     * specified length.
     * 
     * @param array The array
     * @param length The length
     * @return The array, or a new array with the desired length
     */
    static float[] validate(float array[], int length)
    {
        if (array != null && array.length == length)
        {
            return array;
        }
        return new float[length];
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Utils()
    {
        // Private constructor to prevent instantiation
    }
    
}
