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
package de.javagl.jgltf.model.io;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility methods related to JSON string processing
 */
public class JsonUtils
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(JsonUtils.class.getName());

    /**
     * Creates a formatted (pretty-printed, indented) representation of
     * the given JSON string. The details of the formatting are not 
     * specified. If there is any error during this process, then a 
     * warning will be printed and the given string will be returned.
     * 
     * @param jsonString The input JSON string
     * @return The formatted JSON string
     */
    public static String format(String jsonString)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Object object = mapper.readValue(jsonString, Object.class);
            String formattedJsonString = mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
            return formattedJsonString;
        }
        catch (IOException e)
        {
            logger.warning(e.getMessage());
            return jsonString;
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private JsonUtils()
    {
        // Private constructor to prevent instantiation
    }
}
