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
package de.javagl.jgltf.model.khr.materials_variants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.AbstractModelElement;

/**
 * Default implementation of a {@link MaterialsVariantsModel}
 */
public class DefaultMaterialsVariantsModel 
    extends AbstractModelElement
    implements MaterialsVariantsModel
{
    /**
     * The variant names
     */
    private final List<String> names;
    
    /**
     * Default constructor
     */
    public DefaultMaterialsVariantsModel()
    {
        this.names = new ArrayList<String>();
    }

    @Override
    public List<String> getNames()
    {
        return Collections.unmodifiableList(names);
    }
    
    /**
     * Add the given variant name
     * 
     * @param name The name
     */
    public void addName(String name)
    {
        this.names.add(name);
    }
    
    /**
     * Remove the given variant name
     * 
     * @param name The name
     */
    public void removeName(String name)
    {
        this.names.remove(name);
    }
    
    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = 
            getReferencedExtensionModelElements();
        return modelElements;
    }
    
    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove) 
    {
        removeExtensionModelElements(modelElementsToRemove);
        return false;
    }
    
}

