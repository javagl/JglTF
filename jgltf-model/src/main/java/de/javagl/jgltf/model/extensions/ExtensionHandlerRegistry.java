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

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.TextureModel;

/**
 * Interface for classes that manage {@link ExtensionHandler} instances.
 * 
 * Instances of this class are created with {@link ExtensionHandlerRegistries}.
 */
public interface ExtensionHandlerRegistry
{
    /**
     * Return an unmodifiable list containing all {@link ExtensionHandler}
     * instances
     * 
     * @return The list of {@link ExtensionHandler} instances
     */
    List<ExtensionHandler> getAll();

    /**
     * Returns an {@link ExtensionHandler} for the specified extension when it
     * is attached to an object with the specified model class.
     * 
     * The model class with usually be a class implementing the
     * {@link ModelElement} interface - for example, {@link TextureModel} or
     * {@link AccessorModel}.
     * 
     * @param owningModelClass The owning model class
     * @param extensionName The extension name
     * @return The extension handler, or <code>null</code> if no extension
     *         handler was registered for the specified extension
     */
    ExtensionHandler get(Class<?> owningModelClass, String extensionName);

}