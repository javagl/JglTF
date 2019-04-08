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
package de.javagl.jgltf.model.gl.impl.v1;

import java.util.Objects;

import de.javagl.jgltf.impl.v1.TechniqueStatesFunctions;
import de.javagl.jgltf.model.gl.TechniqueStatesFunctionsModel;

/**
 * Default implementation of a {@link TechniqueStatesFunctionsModel}, 
 * based on glTF 1.0 {@link TechniqueStatesFunctions}
 */
public class DefaultTechniqueStatesFunctionsModelV1 
    implements TechniqueStatesFunctionsModel
{
    /**
     * The {@link TechniqueStatesFunctions}
     */
    private final TechniqueStatesFunctions functions;
    
    /**
     * Default constructor 
     * 
     * @param functions The {@link TechniqueStatesFunctions}
     */
    public DefaultTechniqueStatesFunctionsModelV1(
        TechniqueStatesFunctions functions)
    {
        this.functions = Objects.requireNonNull(
            functions, "The functions may not be null");
    }

    @Override
    public float[] getBlendColor()
    {
        return functions.getBlendColor();
    }

    @Override
    public int[] getBlendEquationSeparate()
    {
        return functions.getBlendEquationSeparate();
    }

    @Override
    public int[] getBlendFuncSeparate()
    {
        return functions.getBlendFuncSeparate();
    }

    @Override
    public boolean[] getColorMask()
    {
        return functions.getColorMask();
    }

    @Override
    public int[] getCullFace()
    {
        return functions.getCullFace();
    }

    @Override
    public int[] getDepthFunc()
    {
        return functions.getDepthFunc();
    }

    @Override
    public boolean[] getDepthMask()
    {
        return functions.getDepthMask();
    }

    @Override
    public float[] getDepthRange()
    {
        return functions.getDepthRange();
    }

    @Override
    public int[] getFrontFace()
    {
        return functions.getFrontFace();
    }

    @Override
    public float[] getLineWidth()
    {
        return functions.getLineWidth();
    }

    @Override
    public float[] getPolygonOffset()
    {
        return functions.getPolygonOffset();
    }

}
