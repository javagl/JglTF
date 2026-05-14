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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Methods related to handling extension implementation- and model objects.
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

    /**
     * Call {@link ExtensionHandler#preprocess(GltfModel, ExtensionProcessing)}
     * with the given model and extension processing instance on all known
     * extension handlers.
     * 
     * @param gltfModel The glTF model
     * @param extensionProcessing The {@link ExtensionProcessing}
     */
    public static void preprocess(GltfModel gltfModel,
        ExtensionProcessing extensionProcessing)
    {
        ExtensionHandlerRegistry extensionHandlerRegistry =
            ExtensionHandlerRegistries.get();
        List<ExtensionHandler> extensionHandlers =
            extensionHandlerRegistry.getAll();
        for (ExtensionHandler extensionHandler : extensionHandlers)
        {
            extensionHandler.preprocess(gltfModel, extensionProcessing);
        }
    }    
    
    /**
     * Process the extensions of the given model element (with the given class) 
     * that is contained in the given glTF model.<br>
     * <br>
     * Note: An implementation detail is that this assumes that the given model 
     * element extends the {@link AbstractModelElement} class.<br>
     * <br>
     * This will examine all extension objects that are stored in the given 
     * {@link ModelElement}. For each extension object, it will look up
     * an {@link ExtensionHandler} in the {@link ExtensionHandlerRegistry}.
     * When an extension handler is found, then it will be used for 
     * converting the extension object into its "model" representation,
     * using {@link ExtensionHandler#convertToModel(GltfModel, Object, Object)},
     * and add it to the {@link ModelElement#getExtensionModels()} of the
     * model element.<br>
     * <br>
     * Clients can then obtain the extension model object from the model
     * element, with {@link ModelElement#getExtensionModel(String, Class)}.
     * 
     * @param gltfModel The {@link GltfModel}
     * @param modelElement The {@link ModelElement}
     * @param modelClass The class of the {@link ModelElement}
     */
    public static void createExtensionModels(
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
                extensionHandlerRegistry.get(modelClass, extensionName);

            logger.fine("Found extension " + extensionName
                + " with extension handler " + extensionHandler);

            if (extensionHandler == null)
            {
                continue;
            }
            Class<?> implClass = extensionHandler.getImplClass();

            // Try to convert the object, printing a warning when
            // the conversion fails
            Object impl =
                GltfExtensions.convertValueOptional(jsonObject, implClass);
            if (impl != null)
            {
                Object extensionModel = extensionHandler.convertToModel(
                    gltfModel, modelElement, impl);
                
                logger.info("Found extension implementation for " + extensionName);
                logger.info("  with model class         " + modelClass);
                logger.info("  extension handler        " + extensionHandler);
                logger.info("  converted implementation " + impl);
                logger.info("  to model                 " + extensionModel);
                
                if (extensionModel != null)
                {
                    abstractModelElement.addExtensionModel(extensionName,
                        extensionModel);
                }
            }
        }
    }

    
    /**
     * Create the implementation-level representation of the given model
     * element, to be stored in the {@link GlTFProperty#getExtensions()}
     * of the given glTF property.<br>
     * <br>
     * This will examine all extension model objects that are stored in the 
     * {@link ModelElement#getExtensionModels()} of the given 
     * {@link ModelElement}. For each extension model object, it will look up
     * an {@link ExtensionHandler} in the {@link ExtensionHandlerRegistry}.
     * When an extension handler is found, then it will be used for 
     * converting the extension model object into its "impl" representation,
     * using {@link ExtensionHandler#convertToImpl(GltfModel, Object)},
     * and add it to the {@link GlTFProperty#getExtensions()} of the
     * given {@link GlTFProperty}.<br>
     * <br>
     * 
     * @param gltfModel The {@link GltfModel}
     * @param modelElement The {@link ModelElement}
     * @param modelClass The class of the {@link ModelElement}
     * @param glTFProperty The {@link GlTFProperty} that will store the 
     * implementation-level representation of the extension.
     */
    public static void createExtensionImpls(
        GltfModel gltfModel, ModelElement modelElement, 
        Class<?> modelClass, GlTFProperty glTFProperty)
    {
        Map<String, Object> extensionModels = modelElement.getExtensionModels();
        if (extensionModels == null || extensionModels.isEmpty())
        {
            return;
        }
        ExtensionHandlerRegistry extensionHandlerRegistry =
            ExtensionHandlerRegistries.get();
        for (Entry<String, Object> entry : extensionModels.entrySet())
        {
            String extensionName = entry.getKey();
            Object modelObject = entry.getValue();
            ExtensionHandler extensionHandler =
                extensionHandlerRegistry.get(modelClass, extensionName);

            logger.fine("Found extension " + extensionName
                + " with extension handler " + extensionHandler);

            if (extensionHandler == null)
            {
                continue;
            }
            
            Object impl = extensionHandler.convertToImpl(
                gltfModel, modelObject);

            logger.info("Found extension model for " + extensionName);
            logger.info("  with model class  " + modelClass);
            logger.info("  extension handler " + extensionHandler);
            logger.info("  converted model   " + modelElement);
            logger.info("  to implementation " + impl);
            
            glTFProperty.addExtensions(extensionName, impl);
        }
    }
    

    /**
     * Copy all extension model elements that are contained in the given source
     * element, and add the copies to the given target.<br>
     * <br>
     * Note: An implementation detail is that this assumes that the given target 
     * element extends the {@link AbstractModelElement} class.<br>
     * <br>
     * This will examine all extension objects that are stored in the given 
     * source {@link ModelElement}. For each extension object, it will look up
     * an {@link ExtensionHandler} in the {@link ExtensionHandlerRegistry}.
     * When an extension handler is found, then it will be used for 
     * copying the extension object, using {@link ExtensionHandler#copy},
     * and the copy to the {@link ModelElement#getExtensionModels()} of the
     * model element.<br>
     * <br>
     * 
     * @param gltfModel The {@link GltfModel}
     * @param sourceModelElement The source {@link ModelElement}
     * @param targetModelElement The target {@link ModelElement}
     * @param modelClass The class of the {@link ModelElement}
     * @param modelElementMap The mapping from source to target model elements
     * that will be passed to {@link ExtensionHandler#copy} 
     */
    public static void copyExtensionModels(
        GltfModel gltfModel, 
        ModelElement sourceModelElement, 
        ModelElement targetModelElement,
        Class<?> modelClass,
        Map<ModelElement, ModelElement> modelElementMap)
    {
        if (!(targetModelElement instanceof AbstractModelElement))
        {
            logger
                .warning("Could not process extensions of " + targetModelElement
                    + ": The object is not an AbstractModelElement");
            return;
        }
        AbstractModelElement abstractTargetModelElement =
            (AbstractModelElement) targetModelElement;

        Map<String, Object> sourceExtensionModels =
            sourceModelElement.getExtensionModels();
        if (sourceExtensionModels == null || sourceExtensionModels.isEmpty())
        {
            return;
        }
        ExtensionHandlerRegistry extensionHandlerRegistry =
            ExtensionHandlerRegistries.get();
        for (Entry<String, Object> entry : sourceExtensionModels.entrySet())
        {
            String extensionName = entry.getKey();
            ExtensionHandler extensionHandler =
                extensionHandlerRegistry.get(modelClass, extensionName);
            if (extensionHandler == null)
            {
                continue;
            }
            logger.info(
                "Copy extension " + extensionName + " with extension handler "
                    + extensionHandler + " for " + modelClass);

            Object sourceExtensionModelObject = entry.getValue();
            Object targetExtensionModelObject = extensionHandler.copy(gltfModel,
                sourceExtensionModelObject, modelElementMap);
            abstractTargetModelElement.addExtensionModel(extensionName,
                targetExtensionModelObject);
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
