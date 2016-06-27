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

import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTFProperty;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Shader;

/**
 * Utility methods related to the binary glTF extension
 */
public class BinaryGltf
{
    /**
     * The name of the KHR_binary_glTF extension
     */
    private static final String KHRONOS_BINARY_GLTF_EXTENSION_NAME = 
        "KHR_binary_glTF";
    
    /**
     * The unique, universal buffer ID for the binary data that is stored
     * in a binary glTF file
     */
    private static final String BINARY_GLTF_BUFFER_ID = "binary_glTF";

    /**
     * Returns whether the given {@link GlTFProperty} has the 
     * <code>"KHR_binary_glTF"</code> extension. This is applicable
     * to {@link Image} and {@link Shader} objects that may contain
     * an extension object with a {@link BufferView} ID instead of 
     * a URI. The {@link BufferView} ID may be obtained with 
     * {@link BinaryGltf#getBinaryGltfBufferViewId(GlTFProperty)}.
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @return Whether the extension exists
     */
    public static boolean hasBinaryGltfExtension(GlTFProperty gltfProperty)
    {
        return GltfExtensions.hasExtension(gltfProperty, 
            KHRONOS_BINARY_GLTF_EXTENSION_NAME);
    }

    /**
     * Returns the value of the <code>"bufferView"</code> property in 
     * the <code>"KHR_binary_glTF"</code> extension object of the
     * given {@link GlTFProperty}, or <code>null</code> if either the
     * extension object or the property does not exist. 
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @return The property value
     */
    public static String getBinaryGltfBufferViewId(GlTFProperty gltfProperty)
    {
        return GltfExtensions.getExtensionPropertyValueAsString(
            gltfProperty, KHRONOS_BINARY_GLTF_EXTENSION_NAME, "bufferView");
    }

    /**
     * Set the value of the <code>"bufferView"</code> property in 
     * the <code>"KHR_binary_glTF"</code> extension object of the
     * given {@link GlTFProperty}. If no (or an invalid) extension
     * object already existed, it will be created or overwritten,
     * respectively. 
     * 
     * @param gltfProperty The {@link GlTFProperty}
     * @param bufferViewId The {@link BufferView} ID
     */
    public static void setBinaryGltfBufferViewId(
        GlTFProperty gltfProperty, String bufferViewId)
    {
        GltfExtensions.setExtensionPropertyValue(
            gltfProperty, KHRONOS_BINARY_GLTF_EXTENSION_NAME, 
            "bufferView", bufferViewId);
    }

    /**
     * Returns the name of the Khronos binary glTF extension
     * (<code>"KHR_binary_glTF"</code>)
     * 
     * @return The name of the Khronos binary glTF extension
     */
    public static String getBinaryGltfExtensionName()
    {
        return KHRONOS_BINARY_GLTF_EXTENSION_NAME;
    }

    /**
     * Returns unique ID of the binary glTF {@link Buffer} 
     * (<code>"binary_glTF"</code>)
     * 
     * @return The binary glTF {@link Buffer} ID
     */
    public static String getBinaryGltfBufferId()
    {
        return BINARY_GLTF_BUFFER_ID;
    }

    /**
     * Returns whether the given ID is the unique ID of the binary glTF
     * {@link Buffer} (<code>"binary_glTF"</code>)
     * 
     * @param id The ID
     * @return Whether the given ID is the binary glTF {@link Buffer} ID
     */
    public static boolean isBinaryGltfBufferId(String id)
    {
        return BINARY_GLTF_BUFFER_ID.equals(id);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private BinaryGltf()
    {
        // Private constructor to prevent instantiation
    }
    
}
