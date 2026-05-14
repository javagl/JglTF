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
package de.javagl.jgltf.model.khr.draco_mesh_compression;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractNamedModelElement;

/**
 * Default implementation of a {@link DracoMeshCompressionModel}
 */
public class DefaultDracoMeshCompressionModel extends AbstractNamedModelElement
    implements DracoMeshCompressionModel
{
    /**
     * The {@link DracoOptions}
     */
    private DracoOptions dracoOptions;

    /**
     * The set of attributes that should be draco-compressed
     */
    private final Set<String> attributes;

    /**
     * After the {@link DracoMeshCompressionExtensionHandler#preprocess}
     * function was run, this will store the buffer view model that contains the
     * Draco-encoded data
     */
    private BufferViewModel dracoBufferViewModel;

    /**
     * After the {@link DracoMeshCompressionExtensionHandler#preprocess}
     * function was run, this will contain the mapping from attribute names to
     * draco attribute IDs (that has to go into the JSON)
     */
    private final Map<String, Integer> dracoAttributeIds;

    /**
     * Creates a new instance
     */
    public DefaultDracoMeshCompressionModel()
    {
        this.dracoOptions = new DracoOptions();
        this.attributes = new LinkedHashSet<String>();
        this.dracoAttributeIds = new LinkedHashMap<String, Integer>();
    }

    @Override
    public DracoOptions getDracoOptions()
    {
        return dracoOptions;
    }

    /**
     * Set the {@link DracoOptions} for this instance
     * 
     * @param dracoOptions The {@link DracoOptions}
     */
    public void setDracoOptions(DracoOptions dracoOptions)
    {
        this.dracoOptions = Objects.requireNonNull(dracoOptions,
            "The dracoOptions may not be null");
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        return Collections.emptySet();
    }

    /**
     * Add the given attribute to be draco-encoded
     * 
     * @param attribute The attribute (e.g. "POSITION")
     */
    public void addAttribute(String attribute)
    {
        this.attributes.add(attribute);
    }

    @Override
    public Set<String> getAttributes()
    {
        return Collections.unmodifiableSet(attributes);
    }

    /**
     * Return the mapping between attribute names and the draco attribute
     * IDs (that has to go into the JSON)
     * 
     * @return The mapping
     */
    Map<String, Integer> getDracoAttributeIds()
    {
        return Collections.unmodifiableMap(dracoAttributeIds);
    }

    /**
     * Set the draco attribute ID for the given attribute name
     * 
     * @param attributeName The attribute name
     * @param id The ID
     */
    void setDracoAttributeId(String attributeName, int id)
    {
        this.dracoAttributeIds.put(attributeName, id);
    }

    /**
     * Returns the buffer view model that should contain the draco-encoded
     * data, after the {@link DracoMeshCompressionExtensionHandler#preprocess}
     * function was run.
     * 
     * @return The buffer view model
     */
    BufferViewModel getDracoBufferViewModel()
    {
        return dracoBufferViewModel;
    }

    /**
     * Called in the {@link DracoMeshCompressionExtensionHandler#preprocess}
     * function, to set the buffer view model for the draco-encoded data
     * 
     * @param dracoBufferViewModel The buffer view model
     */
    void setDracoBufferViewModel(BufferViewModel dracoBufferViewModel)
    {
        this.dracoBufferViewModel = dracoBufferViewModel;
    }
    
    @Override
    public String getExtensionName()
    {
        return "KHR_draco_mesh_compression";
    }
    
    @Override
    public boolean isRequired()
    {
        return true;
    }
    
}
