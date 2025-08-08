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

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * Methods for obtaining {@link ExtensionHandlerRegistry} instances
 */
public class ExtensionHandlerRegistries
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(ExtensionHandlerRegistries.class.getName());

    /**
     * Holder class for the instance
     */
    private static class Holder
    {
        /**
         * The instance
         */
        private static final ExtensionHandlerRegistry INSTANCE =
            createDefault();
    }

    /**
     * Returns the {@link ExtensionHandlerRegistry}
     * 
     * @return The {@link ExtensionHandlerRegistry}
     */
    public static ExtensionHandlerRegistry get()
    {
        return Holder.INSTANCE;
    }

    /**
     * Create a new {@link ExtensionHandlerRegistry} that is filled with all
     * extension handlers for which an implementation can be found on the class
     * path, using a service loader.
     * 
     * @return The {@link ExtensionHandlerRegistry}
     */
    private static ExtensionHandlerRegistry createDefault()
    {
        ServiceLoader<ExtensionHandler> extensionHandlers =
            ServiceLoader.load(ExtensionHandler.class);
        List<ExtensionHandler> result = new ArrayList<ExtensionHandler>();
        for (ExtensionHandler extensionHandler : extensionHandlers)
        {
            logger.info("Found ExtensionHandler: "
                + extensionHandler.getExtensionName() + " for "
                + extensionHandler.getImplClass().getSimpleName() + " as " 
                + extensionHandler.getModelClass().getSimpleName() + " on "
                + extensionHandler.getOwningModelClass().getSimpleName());
            result.add(extensionHandler);
        }
        return new DefaultExtensionHandlerRegistry(result);

    }

    /**
     * Private constructor to prevent instantiation
     */
    private ExtensionHandlerRegistries()
    {
        // Private constructor to prevent instantiation
    }

}
