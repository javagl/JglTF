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
package de.javagl.jgltf.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.GlTFProperty;

/**
 * Utility methods related to glTF extensions
 */
public class GltfExtensions
{
    /**
     * If the {@link GlTF#getExtensionsUsed() used extensions} of the given 
     * {@link GlTF} is <code>null</code> or does not contain the given
     * extension name, then it will be set to a new list that contains all
     * previous extension names, and the given one.
     * 
     * @param gltf The {@link GlTF}
     * @param extensionName The name of the extension to add
     */
    public static void addExtensionUsed(GlTF gltf, String extensionName)
    {
        List<String> oldExtensionsUsed = gltf.getExtensionsUsed();
        if (oldExtensionsUsed == null || 
            !oldExtensionsUsed.contains(extensionName))
        {
            List<String> newExtensionsUsed = new ArrayList<String>();
            if (oldExtensionsUsed != null)
            {
                newExtensionsUsed.addAll(oldExtensionsUsed);
            }
            newExtensionsUsed.add(extensionName);
            gltf.setExtensionsUsed(newExtensionsUsed);
        }
    }
    
    
    /**
     * Return the key-value mapping that is stored as the 
     * {@link GlTFProperty#getExtensions() extension} with the given name 
     * in the given {@link GlTFProperty}, or <code>null</code> if no such 
     * entry can be found.
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @param extensionName The extension name
     * @return The extension property mapping, or <code>null</code>
     */
    static Map<String, Object> getExtensionMap(
        GlTFProperty gltfProperty, String extensionName)
    {
        Map<String, Object> extensions = gltfProperty.getExtensions();
        if (extensions == null)
        {
            return null;
        }
        Object value = extensions.get(extensionName);
        if (value == null)
        {
            return null;
        }
        if (value instanceof Map<?, ?>)
        {
            Map<?, ?> map = (Map<?, ?>)value;
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>)map;
            return result; 
        }
        return null;
    }
    
    /**
     * Create a key-value mapping, and store it under the given name in the
     * {@link GlTFProperty#getExtensions() extensions} of the given 
     * {@link GlTFProperty}. If the extensions already contained a value 
     * with the given name, then this value will be overwritten. 
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @param extensionName The extension name
     * @return The newly created extension mapping
     */
    static Map<String, Object> createExtensionMap(
        GlTFProperty gltfProperty, String extensionName)
    {
        Map<String, Object> newExtensions = new LinkedHashMap<String, Object>();
        Map<String, Object> oldExtensions = gltfProperty.getExtensions();
        if (oldExtensions != null)
        {
            newExtensions.putAll(oldExtensions);
        }
        gltfProperty.setExtensions(newExtensions);
        LinkedHashMap<String, Object> extensionMap = 
            new LinkedHashMap<String, Object>();
        newExtensions.put(extensionName, extensionMap);
        return extensionMap;
    }
    
    
    /**
     * Returns whether a non-<code>null</code> key-value mapping is stored 
     * as the {@link GlTFProperty#getExtensions() extension} with the
     * given name in the given glTF property.

     * @param gltfProperty The {@link GlTFProperty}
     * @param extensionName The extension name
     * @return Whether the specified extension mapping exists
     */
    static boolean hasExtension(
        GlTFProperty gltfProperty, String extensionName)
    {
        return getExtensionMap(gltfProperty, extensionName) != null;
    }
    
    /**
     * Returns the value of the specified property in the key-value mapping 
     * that is stored as the {@link GlTFProperty#getExtensions() extension} 
     * with the given name in the given glTF property. If the specified
     * extension does not exist, or does not have the specified property,
     * then <code>null</code> is returned.
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @param extensionName The extension name
     * @param propertyName The property name
     * @return The value, as a string.
     */
    static String getExtensionPropertyValueAsString(
        GlTFProperty gltfProperty, String extensionName, String propertyName)
    {
        Map<String, Object> extensionMap = 
            getExtensionMap(gltfProperty, extensionName);
        if (extensionMap == null)
        {
            return null;
        }
        Object value = extensionMap.get(propertyName);
        if (value == null)
        {
            return null;
        }
        return String.valueOf(value);
    }
    
    /**
     * Stores the given property value under the given property key in the
     * key-value mapping that is stored under the given extension name in
     * the {@link GlTFProperty#getExtensions() extensions} of the given 
     * {@link GlTFProperty}. If the mapping for the given extension name
     * did not exist, it will be created. If it already existed but was
     * no key-value mapping, then its old value will be overwritten.
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @param extensionName The extension name
     * @param propertyName The property name
     * @param propertyValue The value
     */
    static void setExtensionPropertyValue(
        GlTFProperty gltfProperty, String extensionName, 
        String propertyName, Object propertyValue)
    {
        Map<String, Object> extensionMap = 
            getExtensionMap(gltfProperty, extensionName);
        if (extensionMap == null)
        {
            extensionMap = createExtensionMap(gltfProperty, extensionName);
        }
        extensionMap.put(propertyName, propertyValue);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfExtensions()
    {
        // Private constructor to prevent instantiation
    }

}
