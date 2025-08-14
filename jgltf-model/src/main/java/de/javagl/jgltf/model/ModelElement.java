/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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

import java.util.Map;
import java.util.Set;

/**
 * Interface for all classes of the model package. This is corresponds to
 * the <code>GlTFProperty</code> of the original glTF asset.
 */
public interface ModelElement
{
    /**
     * Returns the extensions of this element. This is a mapping from
     * property names (extension names) to the JSON objects.
     * 
     * This will be a reference to the map that is stored internally.
     * Clients should usually not access or modify this map. Library
     * developers may access this map, for example, to remove extension
     * information, but are then responsible for ensuring consistency
     * by updating other model elements, or the extension model of
     * the root glTF model. 
     * 
     * @return The extensions
     */
    Map<String, Object> getExtensions();
    
    /**
     * Returns the extras of this element. 
     * 
     * @return The extras
     */
    Object getExtras();
    
    /**
     * Returns an unmodifiable map containing the extension models of this 
     * element, or <code>null</code> if this object does not have any
     * extension models.
     * 
     * This is a mapping from property names (extension names)
     * to the model objects that have been created from the extension 
     * information.
     * 
     * @return The extension models
     */
    Map<String, Object> getExtensionModels();
    
    /**
     * Returns the model object that is stored for the given extension name.
     * 
     * If this object does not have an object for the specified extension,
     * then <code>null</code> is returned.
     * 
     * If the extension object is not an instance of the given type, then
     * a warning is printed and <code>null</code> is returned.
     * 
     * @param <T> The model object type
     * @param extensionName The extension name
     * @param type The expected model type
     * @return The model object
     */
    <T> T getExtensionModel(String extensionName, Class<? extends T> type);
    
    /**
     * Returns a possibly unmodifiable and possibly empty set of all 
     * {@link ModelElement} objects that are directly referred to
     * by this {@link ModelElement}.
     * 
     * @return The list
     */
    Set<ModelElement> getReferencedModelElements();
}
