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
package de.javagl.jgltf.viewer;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import de.javagl.jgltf.model.gl.TechniqueModel;

/**
 * Default implementation of a {@link RenderedMaterial}
 */
class DefaultRenderedMaterial implements RenderedMaterial
{
    /**
     * The {@link TechniqueModel}
     */
    private final TechniqueModel techniqueModel;
    
    /**
     * The parameter values for the technique
     */
    private final Map<String, Object> values;
    
    /**
     * Creates a new instance
     * 
     * @param techniqueModel The {@link TechniqueModel}
     * @param values The parameter values for the technique
     */
    DefaultRenderedMaterial(TechniqueModel techniqueModel,
        Map<String, Object> values)
    {
        this.techniqueModel = Objects.requireNonNull(
            techniqueModel, "The techniqueModel may not be null");
        this.values = Objects.requireNonNull(
            values, "The values may not be null");
    }
    
    /**
     * Returns the {@link TechniqueModel}
     * 
     * @return The {@link TechniqueModel}
     */
    @Override
    public TechniqueModel getTechniqueModel()
    {
        return techniqueModel;
    }
    
    /**
     * Returns an unmodifiable view on the parameter values
     * 
     * @return The parameter values
     */
    @Override
    public Map<String, Object> getValues()
    {
        return Collections.unmodifiableMap(values);
    }

}
