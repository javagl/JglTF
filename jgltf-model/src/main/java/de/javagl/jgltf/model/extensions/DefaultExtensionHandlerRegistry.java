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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Default implementation of an {@link ExtensionHandlerRegistry}
 */
class DefaultExtensionHandlerRegistry implements ExtensionHandlerRegistry
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(DefaultExtensionHandlerRegistry.class.getName());

    /**
     * The list of extension handlers
     */
    private List<ExtensionHandler> extensionHandlers;

    /**
     * Package-private default constructor. This will store a reference to the
     * given list.
     * 
     * @param extensionHandlers The extension handlers
     */
    DefaultExtensionHandlerRegistry(List<ExtensionHandler> extensionHandlers)
    {
        this.extensionHandlers = Objects.requireNonNull(extensionHandlers,
            "The extensionsHandlers may not be null");
    }

    /**
     * Return an unmodifiable list containing all {@link ExtensionHandler}
     * instances
     * 
     * @return The list of {@link ExtensionHandler} instances
     */
    @Override
    public List<ExtensionHandler> getAll()
    {
        return Collections.unmodifiableList(extensionHandlers);
    }

    @Override
    public ExtensionHandler get(Class<?> owningModelClass, String extensionName)
    {
        // TODO When there are many extension handlers and this is called
        // frequently, it might be better to store them in a Map that
        // maps Entry<String, Class> objects to the instances.
        for (ExtensionHandler extensionHandler : extensionHandlers)
        {
            if (Objects.equals(extensionName,
                extensionHandler.getExtensionName()))
            {
                if (Objects.equals(owningModelClass,
                    extensionHandler.getOwningModelClass()))
                {
                    return extensionHandler;
                }
            }
        }
        logger.fine("No extension handler found for " + extensionName
            + " owned by class " + owningModelClass);
        return null;
    }

}
