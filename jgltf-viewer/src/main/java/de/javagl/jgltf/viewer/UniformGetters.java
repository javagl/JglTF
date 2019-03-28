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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.gl.TechniqueParametersModel;

/**
 * Utility methods related to obtaining the values of uniform variables from
 * a {@link RenderedMaterial}. Its only non-private method is 
 * {@link #createGenericSupplier(String, RenderedMaterial)}, which is
 * used by the {@link UniformGetterFactory}.
 */
class UniformGetters
{
    /**
     * Creates a supplier that obtains the specified value for a uniform, and
     * supplies this value with a type that matches the 
     * {@link TechniqueParametersModel#getType()}:
     * <pre><code>
     * GL_INT
     * GL_UNSIGNED_INT     
     * GL_INT_VEC2
     * GL_INT_VEC3
     * GL_INT_VEC4       : An int[] array with the appropriate size. If
     *                     technique.parameters.count is null, then this
     *                     will be a single-element array.
     *                     
     * GL_FLOAT           
     * GL_FLOAT_MAT3
     * GL_FLOAT_MAT4
     * GL_FLOAT_VEC2
     * GL_FLOAT_VEC3
     * GL_FLOAT_VEC4     : A float[] array with the appropriate size. If
     *                     technique.parameters.count is null, then this
     *                     will be a single-element array. 
     * 
     * GL_SAMPLER_2D     : A Object[1] containing the texture reference. The
     *                     type of this may be a string (namely, the texture
     *                     ID for glTF 1.0) or an integer (namely, the texture
     *                     index for glTF 2.0)
     * 
     * </code></pre> 
     * <br>
     * The returned suppliers MAY always return the same array instances.
     * So callers MUST NOT store or modify these arrays.<br>
     * <br>
     * If the {@link TechniqueParametersModel} for the specified uniform can
     * not be looked up in the {@link TechniqueModel} of the given 
     * {@link RenderedMaterial} then <code>null</code> is returned.
     * 
     * @param uniformName The uniform name
     * @param renderedMaterial The {@link RenderedMaterial}
     * @return The supplier
     * @throws IllegalArgumentException If the
     * {@link TechniqueParametersModel#getType()} is not one of the supported
     * types for OpenGL uniforms
     */
    static Supplier<?> createGenericSupplier(
        String uniformName, RenderedMaterial renderedMaterial)
    {
        TechniqueModel techniqueModel = renderedMaterial.getTechniqueModel();
        TechniqueParametersModel techniqueParametersModel = 
            techniqueModel.getUniformParameters(uniformName);
        if (techniqueParametersModel == null)
        {
            return null;
        }
        switch (techniqueParametersModel.getType())
        {
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            case GltfConstants.GL_INT_VEC2:   
            case GltfConstants.GL_INT_VEC3:   
            case GltfConstants.GL_INT_VEC4:   
            {
                return createIntArraySupplier(
                    uniformName, renderedMaterial);
            }

            case GltfConstants.GL_FLOAT: 
            case GltfConstants.GL_FLOAT_MAT2:   
            case GltfConstants.GL_FLOAT_MAT3:   
            case GltfConstants.GL_FLOAT_MAT4:   
            case GltfConstants.GL_FLOAT_VEC2:   
            case GltfConstants.GL_FLOAT_VEC3:   
            case GltfConstants.GL_FLOAT_VEC4:   
            {
                return createFloatArraySupplier(
                    uniformName, renderedMaterial);
            }
            
            case GltfConstants.GL_SAMPLER_2D:
            {
                return createObjectArraySupplier(
                    uniformName, renderedMaterial);
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
                    techniqueParametersModel.getType());
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid parameter type: " + techniqueParametersModel.getType());
    }

    /**
     * Obtain the value for the uniform with the given name from the given
     * {@link RenderedMaterial}. If the value for the parameter is not 
     * contained in the given {@link RenderedMaterial}, then return the 
     * default value that is specified by the {@link TechniqueModel} of 
     * the {@link RenderedMaterial}. 
     * (Note that this value may still be <code>null</code>).
     *   
     * @param uniformName The uniform name
     * @param renderedMaterial The {@link RenderedMaterial}
     * @return The uniform value
     */
    private static Object getUniformValueObject(
        String uniformName, RenderedMaterial renderedMaterial)
    {
        TechniqueModel techniqueModel = renderedMaterial.getTechniqueModel();
        Map<String, String> uniforms = techniqueModel.getUniforms();
        String parameterName = uniforms.get(uniformName);
        Map<String, Object> materialValues = renderedMaterial.getValues();
        Object materialValue = materialValues.get(parameterName);
        if (materialValue != null)
        {
            return materialValue;
        }
        TechniqueParametersModel techniqueParametersModel = 
            techniqueModel.getUniformParameters(uniformName);
        if (techniqueParametersModel == null)
        {
            return null;
        }
        return techniqueParametersModel.getValue();
    }

    /**
     * Returns a supplier for the specified uniform value. This supplier
     * will return a Object[1] array, with the only element being the
     * value of the uniform. This value may be <code>null</code>, or
     * the actual value object.
     * 
     * @param uniformName The uniform name
     * @param renderedMaterial The {@link RenderedMaterial}
     * @return The supplier
     */
    private static Supplier<?> createObjectArraySupplier(
        String uniformName, RenderedMaterial renderedMaterial)
    {
        Object value[] = new Object[1];
        return () ->
        {
            Object object = getUniformValueObject(
                uniformName, renderedMaterial);
            if (object == null)
            {
                value[0] = null;
            }
            else if (object instanceof Collection<?>)
            {
                Collection<?> collection = (Collection<?>)object;
                if (collection.size() == 0)
                {
                    value[0] = null;
                }
                else
                {
                    Object element = collection.iterator().next();
                    value[0] = element;
                }
            }
            else
            {
                value[0] = object;
            }
            return value;
        };
    }

    /**
     * Returns a supplier for the specified uniform value, which is assumed
     * to be a single number, an integer array, or a list of numbers  
     * 
     * @param uniformName The uniform name
     * @param renderedMaterial The {@link RenderedMaterial}
     * @return The supplier
     */
    private static Supplier<?> createIntArraySupplier(
        String uniformName, RenderedMaterial renderedMaterial)
    {
        Supplier<int[]> supplier = new Supplier<int[]>()
        {
            private int value[] = null;
            
            @Override
            public int[] get()
            {
                Object object = getUniformValueObject(
                    uniformName, renderedMaterial);
                if (object == null)
                {
                    return null;
                }
                if (object instanceof Number)
                {
                    if (value == null)
                    {
                        value = new int[1];
                    }
                    Number number = (Number)object;
                    value[0] = number.intValue();
                }
                else if (object instanceof int[])
                {
                    return (int[])object;
                }
                else
                {
                    List<? extends Number> list = asNumberList(object);
                    value = toIntArray(list, value);
                }
                return value;
            }
        };
        return supplier;
    }
    
    /**
     * Returns a supplier for the specified uniform value, which is assumed
     * to be a single number, an float array, or a list of numbers  
     * 
     * @param uniformName The uniform name
     * @param renderedMaterial The {@link RenderedMaterial}
     * @return The supplier
     */
    private static Supplier<?> createFloatArraySupplier(
        String uniformName, RenderedMaterial renderedMaterial)
    {
        Supplier<float[]> supplier = new Supplier<float[]>()
        {
            private float value[] = null;
            
            @Override
            public float[] get()
            {
                Object object = getUniformValueObject(
                    uniformName, renderedMaterial);
                if (object == null)
                {
                    return null;
                }
                if (object instanceof Number)
                {
                    if (value == null)
                    {
                        value = new float[1];
                    }
                    Number number = (Number)object;
                    value[0] = number.floatValue();
                }
                else if (object instanceof float[])
                {
                    return (float[])object;
                }
                else
                {
                    List<? extends Number> list = asNumberList(object);
                    value = toFloatArray(list, value);
                }
                return value;
            }
        };
        return supplier;
    }
    

    /**
     * Write the elements of the given list into the given array. The given
     * list may not be <code>null</code> and may not contain <code>null</code>
     * elements. If the given array is <code>null</code>, then a new array
     * will be created and returned. If it is not <code>null</code>, it must
     * be large enough to store the elements of the list.
     * 
     * @param list The list
     * @param result The array that will store the result
     * @return The result array
     */
    private static float[] toFloatArray(
        List<? extends Number> list, float result[])
    {
        float localResult[] = result;
        if (localResult == null)
        {
            localResult = new float[list.size()];            
        }
        for (int i = 0; i < list.size(); i++)
        {
            localResult[i] = list.get(i).floatValue();
        }
        return localResult;
    }
    
    /**
     * Write the elements of the given list into the given array. The given
     * list may not be <code>null</code> and may not contain <code>null</code>
     * elements. If the given array is <code>null</code>, then a new array
     * will be created and returned. If it is not <code>null</code>, it must
     * be large enough to store the elements of the list.
     * 
     * @param list The list
     * @param result The array that will store the result
     * @return The result array
     */
    private static int[] toIntArray(
        List<? extends Number> list, int result[])
    {
        int localResult[] = result;
        if (localResult == null)
        {
            localResult = new int[list.size()];
        }
        for (int i = 0; i < list.size(); i++)
        {
            localResult[i] = list.get(i).intValue();
        }
        return localResult;
    }
    
    
    /**
     * Brutally casts the given object to a list of subtype of numbers.
     * 
     * @param object The object
     * @return The casted object
     */
    private static List<? extends Number> asNumberList(Object object)
    {
        @SuppressWarnings("unchecked")
        List<? extends Number> list = (List<? extends Number>)object;
        return list;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private UniformGetters()
    {
        // Private constructor to prevent instantiation
    }
    

}
