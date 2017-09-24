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
package de.javagl.jgltf.viewer;

import java.util.Objects;
import java.util.function.Supplier;

import de.javagl.jgltf.model.GltfConstants;

/**
 * A class that may be used to create commands for setting uniform values
 * in a {@link GlContext}
 */
class UniformSetterFactory
{
    /**
     * The {@link GlContext} which will be used for actually setting the
     * uniforms values
     */
    private final GlContext glContext;
    
    /**
     * Default constructor 
     * 
     * @param glContext The {@link GlContext}
     */
    UniformSetterFactory(GlContext glContext)
    {
        this.glContext = glContext;
    }
    
    /**
     * Create a command for setting the specified uniform to the value
     * that is obtained from the given supplier.<br>
     * <br>
     * It is assumed that the given supplier provides an object that 
     * matches the required type for the uniform. That is, the return
     * type is assumed to be the one described in this table:  
     * <pre><code>
     *  GL_INT          : int[count]
     *  GL_UNSIGNED_INT : int[count]
     *  GL_INT_VEC2     : int[2*count]
     *  GL_INT_VEC3     : int[3*count]
     *  GL_INT_VEC4     : int[4*count]
     *  GL_FLOAT        : float[count]
     *  GL_FLOAT_VEC2   : float[2*count]
     *  GL_FLOAT_VEC3   : float[3*count]
     *  GL_FLOAT_VEC4   : float[4*count]
     *  GL_FLOAT_MAT2   : float[2*2*count]
     *  GL_FLOAT_MAT3   : float[3*3*count]
     *  GL_FLOAT_MAT3   : float[4*4*count]
     * </code></pre>
     * <br>
     * Note that this does <b>not</b> support the <code>GL_SAMPLER_2D</code>
     * type, as for this type, the texture index also has to be specified.
     * 
     * @param location The OpenGL location for the uniform, as obtained with
     * <code>glGetUniformLocation</code> for the associated GL program 
     * @param type The type of the uniform, as described in the table above
     * @param count The count, that is, the number of elements that the 
     * uniform consists of
     * @param uniformValueSupplier The supplier of the value to set for
     * the uniform 
     * @return The command that, when executed, sets the value that is
     * supplied by the given supplier, as a uniform value for the current
     * {@link GlContext}
     * @throws NullPointerException If the supplier is <code>null</code>
     * @throws IllegalArgumentException If the parameter type is not one
     * of the valid types mentioned above
     */
    Runnable createUniformSettingCommand(
        int location, int type, int count, Supplier<?> uniformValueSupplier)
    {
        Objects.requireNonNull(uniformValueSupplier,
            "The uniformValueSupplier may not be null");
        switch (type)
        {
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            case GltfConstants.GL_INT_VEC2:
            case GltfConstants.GL_INT_VEC3:
            case GltfConstants.GL_INT_VEC4:   
            {
                Supplier<int[]> supplier =
                    cast(uniformValueSupplier, int[].class);
                return () ->
                {
                    int value[] = supplier.get();
                    if (value != null)
                    {
                        glContext.setUniformiv(type, location, count, value);
                    }
                };
            }

            case GltfConstants.GL_FLOAT:
            case GltfConstants.GL_FLOAT_VEC2:   
            case GltfConstants.GL_FLOAT_VEC3:
            case GltfConstants.GL_FLOAT_VEC4:
            {
                Supplier<float[]> supplier =
                    cast(uniformValueSupplier, float[].class);
                return () ->
                {
                    float value[] = supplier.get();
                    if (value != null)
                    {
                        glContext.setUniformfv(type, location, count, value);
                    }
                };
            }
                
            case GltfConstants.GL_FLOAT_MAT2:   
            case GltfConstants.GL_FLOAT_MAT3:   
            case GltfConstants.GL_FLOAT_MAT4:   
            {
                Supplier<float[]> supplier =
                    cast(uniformValueSupplier, float[].class);
                return () ->
                {
                    float value[] = supplier.get();
                    if (value != null)
                    {
                        glContext.setUniformMatrixfv(
                            type, location, count, value);
                    }
                };
            }
            
            
            // These types are not supported as uniform types in OpenGL
            case GltfConstants.GL_BOOL: 
            case GltfConstants.GL_BYTE:
            case GltfConstants.GL_UNSIGNED_BYTE:
            case GltfConstants.GL_SHORT:
            case GltfConstants.GL_UNSIGNED_SHORT:
            case GltfConstants.GL_BOOL_VEC2:   
            case GltfConstants.GL_BOOL_VEC3:   
            case GltfConstants.GL_BOOL_VEC4:
                throw new IllegalArgumentException(
                    "Uniform parameter type not supported: "+
                    GltfConstants.stringFor(type));
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid uniform parameter type: "+ GltfConstants.stringFor(type));
    }
    
    
    /**
     * Create a supplier that casts the values returned by the given supplier
     * to the given type.
     * 
     * @param supplier The delegate supplier
     * @param type The target type
     * @return The casting supplier
     */
    private static <T> Supplier<T> cast(Supplier<?> supplier, Class<T> type)
    {
        return () ->
        {
            Object object = supplier.get();
            return type.cast(object);
        };
    }
}
