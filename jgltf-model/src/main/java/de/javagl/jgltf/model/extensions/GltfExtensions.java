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
package de.javagl.jgltf.model.extensions;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.javagl.jgltf.model.io.JacksonUtils;

/**
 * Utility methods related to glTF extension objects.
 */
public class GltfExtensions
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfExtensions.class.getName());
    
    /**
     * Obtain the specified extension object from the given glTF property.
     * 
     * If the given glTF object does not have an extension with the
     * given name, or this object cannot be converted to the given
     * target type, then <code>null</code> is returned.
     *  
     * @param <T> The type of the extension object
     * 
     * @param gltfProperty The glTF property
     * @param extensionName The extension name
     * @param extensionType The extension type
     * @return The extension object, or <code>null</code>
     * @throws IllegalArgumentException If the given glTF property is neither
     * a <code>de.javagl.jgltf.impl.v1.GlTFProperty</code> nor a
     * a <code>de.javagl.jgltf.impl.v2.GlTFProperty</code>
     */
    public static <T> T obtain(
        Object gltfProperty, String extensionName, Class<T> extensionType)
    {
        if (gltfProperty instanceof de.javagl.jgltf.impl.v1.GlTFProperty)
        {
            de.javagl.jgltf.impl.v1.GlTFProperty gltfPropertyV1 = 
                (de.javagl.jgltf.impl.v1.GlTFProperty) gltfProperty;
            Map<String, Object> extensions = gltfPropertyV1.getExtensions();
            return obtainInternal(extensions, extensionName, extensionType);
        }
        if (gltfProperty instanceof de.javagl.jgltf.impl.v2.GlTFProperty)
        {
            de.javagl.jgltf.impl.v2.GlTFProperty gltfPropertyV2 = 
                (de.javagl.jgltf.impl.v2.GlTFProperty) gltfProperty;
            Map<String, Object> extensions = gltfPropertyV2.getExtensions();
            return obtainInternal(extensions, extensionName, extensionType);
        }
        throw new IllegalArgumentException(
            "Not a valid glTF property: " + gltfProperty);
    }
    
    /**
     * Obtain the specified extension object from the given map of 
     * raw (unprocessed) extension information.
     * 
     * If the given extensions are <code>null</code>, or do not contain
     * an extension with the given name, or the object cannot be converted 
     * to the given target type, then <code>null</code> is returned.
     *  
     * @param <T> The type of the extension object
     * 
     * @param extensions The extensions
     * @param extensionName The extension name
     * @param extensionType The extension type
     * @return The extension object, or <code>null</code>
     */
    public static <T> T obtainExtension(
        Map<String, Object> extensions, 
        String extensionName, Class<T> extensionType)
    {
        return obtainInternal(extensions, extensionName, extensionType);
    }

    /**
     * Internal method to obtain the extension object from the given map
     * 
     * @param <T> The type of the extension object
     * @param extensions The optional extensions map
     * @param extensionName The extension name
     * @param extensionType The extension type
     * @return The extension object, or <code>null</code> if it cannot be
     * obtained
     */
    private static <T> T obtainInternal(
        Map<String, Object> extensions,
        String extensionName, Class<T> extensionType)
    {
        if (extensions == null)
        {
            return null;
        }
        Object object = extensions.get(extensionName);
        if (object == null)
        {
            return null;
        }
        return convertValueOptional(object, extensionType);
    }
    
    /**
     * Convert the given object to the given target type
     * 
     * @param <T> The target type
     * @param object The object
     * @param type The target type
     * @return The result
     * @throws IllegalArgumentException If the conversion failed
     */
    private static <T> T convertValue(Object object, Class<T> type)
        throws IllegalArgumentException
    {
        ObjectMapper objectMapper = JacksonUtils.createObjectMapper();
        return objectMapper.convertValue(object, type);
    }
    
    /**
     * Convert the given object to the given target type, returning 
     * <code>null</code> if the conversion failed.
     * 
     * This method is not part of the public API.
     * 
     * @param <T> The target type
     * @param object The object
     * @param type The target type
     * @return The result
     */
    public static <T> T convertValueOptional(Object object, Class<T> type)
    {
        try
        {
            return convertValue(object, type);
        }
        catch (IllegalArgumentException e)
        {
            logger.log(Level.WARNING,
                "Could not convert " + object + " to " + type, e);
            return null;
        }
    }
    

    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfExtensions()
    {
        // Private constructor to prevent instantiation
    }
}
