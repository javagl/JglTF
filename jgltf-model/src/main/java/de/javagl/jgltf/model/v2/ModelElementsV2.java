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
package de.javagl.jgltf.model.v2;

import de.javagl.jgltf.impl.v2.GlTFChildOfRootProperty;
import de.javagl.jgltf.impl.v2.GlTFProperty;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.NamedModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;
import de.javagl.jgltf.model.impl.AbstractNamedModelElement;

/**
 * Internal utility methods related to model elements.
 * 
 * These methods are not part of the public API.
 */
public class ModelElementsV2
{
    /**
     * Transfer the name and extensions and extras from the given model
     * element to the given property
     * 
     * @param modelElement The model element
     * @param property The property
     */
    static void transferGltfChildOfRootPropertyElementsFromModel(
        NamedModelElement modelElement,
        GlTFChildOfRootProperty property)
    {
        property.setName(modelElement.getName());
        transferGltfPropertyElementsFromModel(modelElement, property);
    }

    /**
     * Transfer the extensions and extras from the given model element to
     * the given property
     * 
     * @param modelElement The model element
     * @param property The property
     */
    public static void transferGltfPropertyElementsFromModel(
        ModelElement modelElement, GlTFProperty property)
    {
        property.setExtensions(modelElement.getExtensions());
        property.setExtras(modelElement.getExtras());
    }

    /**
     * Transfer the name and extensions and extras from the given property to
     * the given target
     * 
     * @param property The property
     * @param modelElement The target
     */
    static void transferGltfChildOfRootPropertyElementsToModel(
        GlTFChildOfRootProperty property, 
        AbstractNamedModelElement modelElement)
    {
        modelElement.setName(property.getName());
        transferGltfPropertyElementsToModel(property, modelElement);
    }

    /**
     * Transfer the extensions and extras from the given property to
     * the given target
     * 
     * @param property The property
     * @param modelElement The target
     */
    public static void transferGltfPropertyElementsToModel(
        GlTFProperty property, AbstractModelElement modelElement)
    {
        modelElement.setExtensions(property.getExtensions());
        modelElement.setExtras(property.getExtras());
    }

    /**
     * Transfer the extensions and extras from the given model element to
     * the given target
     * 
     * @param sourceModelElement The source model element
     * @param targetModelElement The target model element
     */
    public static void transferGltfPropertyElements(
        ModelElement sourceModelElement, AbstractModelElement targetModelElement)
    {
        targetModelElement.setExtensions(sourceModelElement.getExtensions());
        targetModelElement.setExtras(sourceModelElement.getExtras());
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ModelElementsV2()
    {
        // Private constructor to prevent instantiation
    }
}
