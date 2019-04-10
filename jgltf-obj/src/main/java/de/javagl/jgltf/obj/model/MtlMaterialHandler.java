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
package de.javagl.jgltf.obj.model;

import de.javagl.jgltf.model.MaterialModel;
import de.javagl.obj.Mtl;
import de.javagl.obj.ReadableObj;

/**
 * Package-private interface for classes that can generate 
 * {@link MaterialModel} instances from the information 
 * that is contained in OBJ- and MTL files. 
 */
interface MtlMaterialHandler
{
    /**
     * Create a {@link MaterialModel} for the given OBJ and MTL
     * 
     * @param obj The OBJ
     * @param mtl The MTL
     * @return The {@link MaterialModel}
     */
    MaterialModel createMaterial(ReadableObj obj, Mtl mtl);

    /**
     * Create a {@link MaterialModel} that represents a material with
     * the given properties
     * 
     * @param withNormals Whether the rendered object has normals
     * @param r The red component, in [0,1]
     * @param g The green component, in [0,1]
     * @param b The blue component, in [0,1]
     * @return The {@link MaterialModel}
     */
    MaterialModel createMaterialWithColor(
        boolean withNormals, float r, float g, float b);

}
