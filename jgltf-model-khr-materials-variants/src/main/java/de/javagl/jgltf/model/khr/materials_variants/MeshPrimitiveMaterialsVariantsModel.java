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

import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.ModelElement;

/**
 * Interface for a model of the <code>KHR_materials_variants</code> extension
 * that is found in a mesh primitive
 */
public interface MeshPrimitiveMaterialsVariantsModel extends ModelElement
{
    /**
     * Returns the material model that should be used for the mesh primitive
     * for the specified variant, or <code>null</code> if the variant is
     * not one of the valid variants as of the top-level 
     * {@link MaterialsVariantsModel} of the glTF.
     * 
     * @param variantName The variant name
     * @return The material model
     */
    MaterialModel getMaterialModel(String variantName);
    
    /**
     * Returns the name of the mapping for the given variant name, or 
     * <code>null</code> if the given variant name is not valid or
     * this mapping does not have a name.
     * 
     * @param variantName The variant name
     * @return The mapping name
     */
    String getName(String variantName);
}
