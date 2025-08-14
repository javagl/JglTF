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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Methods related to {@link ModelElement} objects.
 * 
 * The methods in this class are not part of the public API. Clients should not
 * use this class. It is only used by implementors of extensions.
 */
public class ModelElements
{
    /**
     * Returns a set containing all {@link ModelElement} instances that
     * have the given type and that are directly or indirectly referred
     * to by the given {@link ModelElement}, as of the 
     * {@link ModelElement#getReferencedModelElements()} method of this
     * object or its referenced objects.
     * 
     * @param <T> The element type
     * @param modelElement The {@link ModelElement}
     * @param type The type of the model elements
     * @return The set
     */
    public static <T> Set<T> collectReferencedModelElements(
        ModelElement modelElement, Class<? extends T> type)
    {
        Set<T> result = new LinkedHashSet<T>();
        Set<ModelElement> processed = new LinkedHashSet<ModelElement>();
        collectReferencedModelElements(modelElement, type, processed, result);
        return result;
    }

    /**
     * Implementation of 
     * {@link #collectReferencedModelElements(ModelElement, Class)}, to be
     * called recursively.
     * 
     * @param <T> The element type
     * @param modelElement The {@link ModelElement}
     * @param type The type of the model elements
     * @param processed The elements that have already been processed
     * @param result The result
     */
    private static <T> void collectReferencedModelElements(
        ModelElement modelElement, Class<? extends T> type, 
        Set<ModelElement> processed, Set<T> result)
    {
        if (processed.contains(modelElement))
        {
            return;
        }
        processed.add(modelElement);
        if (type.isInstance(modelElement))
        {
            result.add(type.cast(modelElement));
        }
        Set<ModelElement> next = modelElement.getReferencedModelElements();
        for (ModelElement m : next)
        {
            collectReferencedModelElements(m, type, processed, result);
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ModelElements()
    {
        // Private constructor to prevent instantiation
    }
}
