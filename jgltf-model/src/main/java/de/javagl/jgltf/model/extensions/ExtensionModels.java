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
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Methods related to handling extension implementation- and model
 * objects.
 * 
 * This class is not part of the public API.
 *
 */
public class ExtensionModels
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ExtensionModels.class.getName());
    
    
    public static void process(
        GltfModel gltfModel, ModelElement modelElement, Class<?> modelClass)
    {
        if (!(modelElement instanceof AbstractModelElement))
        {
            logger.warning("Could not process extensions of " + modelElement
                + ": The object is not an AbstractModelElement");
            return;
        }
        AbstractModelElement abstractModelElement = 
            (AbstractModelElement) modelElement;
        
        Map<String, Object> extensions = modelElement.getExtensions();
        if (extensions == null || extensions.isEmpty())
        {
            return;
        }
        ExtensionHandlerRegistry extensionHandlerRegistry = 
            ExtensionHandlerRegistries.get();
        for (Entry<String, Object> entry : extensions.entrySet())
        {
            String extensionName = entry.getKey();
            
            Object jsonObject = entry.getValue();
            ExtensionHandler extensionHandler = 
                extensionHandlerRegistry.get(
                    modelClass, extensionName);
            
            logger.info("Found extension " + extensionName
                + " with extension handler " + extensionHandler);            
            
            if (extensionHandler == null)
            {
                continue;
            }
            Class<?> implClass = extensionHandler.getImplClass();
            
            // Try to convert the object, printing a warning when
            // the conversion fails
            Object impl = GltfExtensions.convertValueOptional(
                jsonObject, implClass);
            if (impl != null)
            {
                Object extensionModel = extensionHandler.convertToModel(
                    gltfModel, modelElement, impl);
                abstractModelElement.addExtensionModel(
                    extensionName, extensionModel);
            }
        }
    }

    
    /**
     * Private constructor to prevent instantiation
     */
    private ExtensionModels()
    {
        // Private constructor to prevent instantiation
    }
}
