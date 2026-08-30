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

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ModelElement;

/**
 * Interface for classes that convert between implementation- and model classes.
 */
public interface ExtensionHandler
{
    /**
     * Returns the name of the extension that is handled by this class
     * 
     * @return The extension name
     */
    String getExtensionName();

    /**
     * Returns the model class that contains this extension object. This will
     * usually be a class that is a {@link ModelElement}.
     * 
     * @return The model class
     */
    Class<?> getOwningModelClass();

    /**
     * Returns the class that describes the structure of the implementation.
     * 
     * @return The implementation class.
     */
    Class<?> getImplClass();

    /**
     * Returns the class that represents the model.
     * 
     * @return The model class
     */
    Class<?> getModelClass();

    /**
     * Convert the given implementation object into the model representation of
     * the extension.
     * 
     * 
     * @param gltfModel The {@link GltfModel} in which the extension was found
     * @param owningModelObject The model object (usually an instance of
     *        {@link ModelElement}) that contained the extension
     * @param impl The implementation object
     * @return The model object
     */
    Object convertToModel(GltfModel gltfModel, Object owningModelObject,
        Object impl);

    /**
     * Convert the given model representation of the extension into the
     * low-level implementation object (i.e. serialized JSON).
     * 
     * @param gltfModel The {@link GltfModel} in which the extension was found
     * @param modelObject The model object (usually an instance of
     *        {@link ModelElement}) that is the model representation of the
     *        extension.
     * @return The implementation object
     */
    Object convertToImpl(GltfModel gltfModel, Object modelObject);

    /**
     * Copy the given model object.
     * 
     * This will create a copy of the given model object. The given model
     * element map can be used for looking up existing copies for all
     * model elements that exist in the source, in order to construct
     * the target from these copies.
     * 
     * The copy that is returned should also be placed into this map.
     * 
     * @param gltfModel The model that contains the object
     * @param modelObject The model object
     * @param modelElementMap The model element map
     * @return The copy
     */
    Object copy(GltfModel gltfModel, Object modelObject,
        Map<ModelElement, ModelElement> modelElementMap);
    
    /**
     * A preliminary function that can be used for preprocessing a glTF model.
     * 
     * For most implementations, this will be a no-op. For the draco mesh
     * compression extension, this will inform the given 
     * {@link ExtensionProcessing} about the accessor models that are
     * draco-encoded.
     * 
     * @param gltfModel The model
     * @param extensionProcessing The {@link ExtensionProcessing}
     */
    default void preprocess(GltfModel gltfModel,
        ExtensionProcessing extensionProcessing) 
    {
        // Empty default implementation
    }
}
