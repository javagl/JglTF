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
package de.javagl.jgltf.viewer;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;

/**
 * Internal utility methods for handling the CESIUM_RTC extension
 */
class CesiumRtcUtils
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(CesiumRtcUtils.class.getName());

    
    /**
     * The string that represents the RTC model view semantic for the
     * CESIUM_RTC extension.
     */
    private static final String CESIUM_RTC_MODELVIEW_SEMANTIC_STRING = 
        "CESIUM_RTC_MODELVIEW";

    /**
     * Returns whether the given string indicates the 
     * <code>CESIUM_RTC_MODELVIEW</code> semantic for
     * the CESIUM_RTC extension
     * 
     * @param semanticString The semantic string
     * @return Whether the string is the CESIUM_RTC_MODELVIEW semantic
     */
    static boolean isCesiumRtcModelViewSemantic(String semanticString)
    {
        return CESIUM_RTC_MODELVIEW_SEMANTIC_STRING.equals(semanticString);
    }
    
    /**
     * Returns a supplier for a model-view matrix that takes the CESIUM_RTC
     * extension into account, by adding the RTC center to the translation
     * component.
     * 
     * @param nodeModel The node model
     * @param viewMatrixSupplier The view matrix supplier
     * @param rtcCenter The RTC center
     * @return The supplier
     */
    static Supplier<float[]> createCesiumRtcModelViewMatrixSupplier(
        NodeModel nodeModel, Supplier<float[]> viewMatrixSupplier, 
        float rtcCenter[])
    {
        Supplier<float[]> modelMatrixSupplier = 
            nodeModel.createGlobalTransformSupplier();
        return MatrixOps
            .create4x4()
            .multiply4x4(viewMatrixSupplier)
            .multiply4x4(modelMatrixSupplier)
            .translate(rtcCenter[0], rtcCenter[1], rtcCenter[2])
            .log(CESIUM_RTC_MODELVIEW_SEMANTIC_STRING, Level.FINE)
            .build();
        
    }
    
    /**
     * Extract the 3D float array from the given glTF model that is stored
     * as the <code>"center"</code> property in the <code>"CESIUM_RTC"</code>
     * extension. If no such property is found, then <code>null</code> is
     * returned.
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The RTC center
     */
    static float[] extractRtcCenterFromModel(GltfModel gltfModel)
    {
        Map<String, Object> extensions = gltfModel.getExtensions();
        if (extensions == null) 
        {
            return null;
        }
        Object rtcExtension = extensions.get("CESIUM_RTC");
        return extractRtcCenterFromExtensionObbject(rtcExtension);
    }

    /**
     * Extract the 3D float array from the given <code>"CESIUM_RTC"</code>
     * extension object. This will be an array containing the numbers that
     * are stored in the list under the <code>"center"</code> property.
     * 
     * @param extensionObject The extension object
     * @return The RTC center
     */
    private static float[] extractRtcCenterFromExtensionObbject(
        Object extensionObject)
    {
        // NOTE: This is very pragmatic and involves some manual fiddling.
        // One could create dedicated classes for that from the JSON schema
        // and rely on the Jackson conversion functions, but it's only a 
        // single property, so there is no need for dedicated structures.
        if (extensionObject == null)
        {
            return null;
        }
        if (!(extensionObject instanceof Map<?, ?>)) 
        {
            logger.warning("CESIUM_RTC extension object has "
                + "invalid type. Expected Map<?, ?>. "
                + "Found " + extensionObject.getClass());
            return null;
        }
        Map<?, ?> extension = 
            (Map<?, ? >) extensionObject;
        Object center = extension.get("center");
        if (!(center instanceof List<?>))
        {
            logger.warning("CESIUM_RTC extension center value has "
                + "invalid type. Expected List<?>. "
                + "Found " + center.getClass());
            return null;
        }
        List<?> list = (List<?>) center;
        if (list.size() != 3)
        {
            logger.warning("CESIUM_RTC extension center value must "
                + "have size 3, but has " + list.size());
            return null;
        }
        float result[] = new float[3];
        for (int i = 0; i < list.size(); i++)
        {
            Object value = list.get(i);
            if (!(value instanceof Number))
            {
                logger.warning("CESIUM_RTC extension center value at "
                    + "index " + i +" is not a number, "
                    + "but " + value.getClass());
                return null;
            }
            Number number = (Number) value;
            result[i] = number.floatValue();
        }
        return result;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private CesiumRtcUtils()
    {
        // Private constructor to prevent instantiation
    }
    
}
