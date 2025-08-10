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

import de.javagl.jgltf.model.gl.TechniqueModel;

/**
 * Interface for a {@link MaterialModel} that uses the {@link TechniqueModel}
 * based material definition of glTF 1.0.
 */
public interface TechniqueMaterialModel extends MaterialModel
{
    /**
     * Returns the {@link TechniqueModel}
     * 
     * @return The {@link TechniqueModel}
     */
    TechniqueModel getTechniqueModel();

    /**
     * Returns the parameter values of this material. Note that if any 
     * parameter value of the original material is the texture ID 
     * for a parameter of type GL_SAMPLER2D, then the respective value 
     * will be the appropriate {@link TextureModel} instance.
     * 
     * @return The values
     */
    Map<String, Object> getValues();
}