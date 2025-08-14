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
package de.javagl.jgltf.model.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import de.javagl.jgltf.model.ModelElement;

/**
 * Abstract base implementation of the {@link ModelElement} interface.<br>
 * <br>
 * Clients should usually not use this class. It is mainly intended for
 * convenience of implementing model elements as part of the core 
 * implementation or extensions.
 */
public abstract class AbstractModelElement implements ModelElement
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(AbstractModelElement.class.getName());
    
    /**
     * The extras
     */
    private Object extras;
    
    /**
     * The extensions
     */
    private Map<String, Object> extensions;
    
    /**
     * The extension models
     */
    private Map<String, Object> extensionModels;

    /**
     * Set the extras
     * 
     * @param extras The extras
     */
    public void setExtras(Object extras)
    {
        this.extras = extras;
    }
    
    @Override
    public Object getExtras()
    {
        return extras;
    }
    
    /**
     * Set the extensions to be a reference to the given map.
     * 
     * A reference to the map will be stored internally. Clients should 
     * usually call this method directly. Library developers may call
     * this function, but are then responsible for ensuring consistency. 
     * 
     * @param extensions The extensions
     */
    public void setExtensions(Map<String, Object> extensions)
    {
        this.extensions = extensions;
    }

    @Override
    public Map<String, Object> getExtensions()
    {
        return extensions;
    }

    /**
     * Add the given extension to this object.
     * 
     * This will add the given key-value pair to the extensions map,
     * creating it if it was <code>null</code>.
     *  
     * @param name The name of the extension
     * @param extension The extension object
     */
    public void addExtension(String name, Object extension)
    {
        Objects.requireNonNull(name, "The name may not be null");
        if (this.extensions == null)
        {
            this.extensions = new LinkedHashMap<String, Object>();
        }
        this.extensions.put(name, extension);
    }

    /**
     * Remove the specified extension from this object.
     * 
     * If the extension map is empty after this call, then it will be
     * set to <code>null</code>.
     * 
     * @param name The name of the extension
     */
    public void removeExtension(String name)
    {
        if (this.extensions != null)
        {
            this.extensions.remove(name);
            if (this.extensions.isEmpty())
            {
                this.extensions = null;
            }
        }
    }
    
    @Override
    public Map<String, Object> getExtensionModels()
    {
        if (this.extensionModels == null)
        {
            return null;
        }
        return Collections.unmodifiableMap(extensionModels);
    }

    /**
     * Add the given extension model to this object.
     * 
     * This will add the given key-value pair to the extension models map,
     * creating it if it was <code>null</code>.
     *  
     * @param name The name of the extension
     * @param extensionModel The extension model object
     */
    public void addExtensionModel(String name, Object extensionModel)
    {
        Objects.requireNonNull(name, "The name may not be null");
        if (this.extensionModels == null)
        {
            this.extensionModels = new LinkedHashMap<String, Object>();
        }
        this.extensionModels.put(name, extensionModel);
    }

    /**
     * Remove the specified extension model from this object.
     * 
     * If the extension model map is empty after this call, then it will be
     * set to <code>null</code>.
     * 
     * @param name The name of the extension model
     */
    public void removeExtensionModel(String name)
    {
        if (this.extensionModels != null)
        {
            this.extensionModels.remove(name);
            if (this.extensionModels.isEmpty())
            {
                this.extensionModels = null;
            }
        }
    }
    
    @Override
    public <T> T getExtensionModel(String extensionName,
        Class<? extends T> type)
    {
        if (this.extensionModels == null)
        {
            return null;
        }
        Object value = extensionModels.get(extensionName);
        if (value == null)
        {
            return null;
        }
        if (type.isInstance(value)) 
        {
            @SuppressWarnings("unchecked")
            T result = (T)value;
            return result;
        }
        logger.warning("Extension model for " + extensionName
            + " does not have type " + type);
        return null;
    }
    
//    @Override
//    public List<? extends ModelElement> getReferencedModelElements()
//    {
//        return getReferencedExtensionModelElements();
//    }
    
    /**
     * Returns a set containing all {@link ModelElement} objects that are
     * found as values in the {@link #getExtensionModels()} map.
     * 
     * This serves as the basis for {@link #getReferencedModelElements()}
     * implementations.
     * 
     * @return The set
     */
    protected final Set<ModelElement> getReferencedExtensionModelElements()
    {
        Set<ModelElement> modelElements = new LinkedHashSet<ModelElement>();
        Map<String, Object> extensionModels = getExtensionModels();
        if (extensionModels != null)
        {
            for (Object object : extensionModels.values()) 
            {
                if (object instanceof ModelElement) 
                {
                    ModelElement modelElement = (ModelElement) object;
                    modelElements.add(modelElement);
                }
            }
        }
        return modelElements;
    }
    

}
