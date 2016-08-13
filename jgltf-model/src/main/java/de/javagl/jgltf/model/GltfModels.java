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
package de.javagl.jgltf.model;

import java.util.List;
import java.util.function.Supplier;

import de.javagl.jgltf.impl.Material;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;

/**
 * Utility methods related to the glTF data model. 
 */
public class GltfModels
{
    /**
     * Obtain the value for the uniform with the given name from the given
     * {@link Material}. If the value is not contained in the given 
     * {@link Material}, then return the default value that is specified 
     * by the {@link Technique}. (Note that this value may still be 
     * <code>null</code>).
     *   
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @return The uniform value
     */
    static Object getUniformValueObject(
        String uniformName, Technique technique, Material material)
    {
        String uniformParameterId = technique.getUniforms().get(uniformName);
        if (material.getValues() != null)
        {
            Object materialValue = 
                material.getValues().get(uniformParameterId);
            if (materialValue != null)
            {
                return materialValue;
            }
        }
        TechniqueParameters uniformParameter = 
            technique.getParameters().get(uniformParameterId);
        return uniformParameter.getValue();
    }
    
    /**
     * Creates a supplier that obtains the specified value for a uniform, and
     * supplies this value with a type that matches the 
     * {@link TechniqueParameters#getType()}:
     * <pre><code>
     * GL_INT
     * GL_UNSIGNED_INT   : Integer if technique.parameters.count is null.
     *                     An int[] array with the appropriate size if 
     *                     technique.parameters.count is non-null    
     * 
     * GL_FLOAT          : Float if technique.parameters.count is null.
     *                     A float[] array with the appropriate size if 
     *                     technique.parameters.count is non-null    
     * 
     * GL_FLOAT_MAT3
     * GL_FLOAT_MAT4
     * GL_FLOAT_VEC2
     * GL_FLOAT_VEC3
     * GL_FLOAT_VEC4     : A float[] array of appropriate size
     * 
     * GL_INT_VEC2
     * GL_INT_VEC3
     * GL_INT_VEC4       : An int[] array of appropriate size
     * 
     * GL_SAMPLER_2D     : A String containing the texture ID
     * </code></pre> 
     * <br>
     * The returned suppliers MAY always return the same array instances.
     * So callers MUST NOT store or modify these arrays.
     * 
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @return The supplier
     * @throws IllegalArgumentException If the
     * {@link TechniqueParameters#getType()} is not one of the supported
     * types for OpenGL uniforms
     */
    public static Supplier<?> createGenericSupplier(
        String uniformName, Technique technique, Material material)
    {
        String uniformParameterId = technique.getUniforms().get(uniformName);
        TechniqueParameters techniqueParameters = 
            technique.getParameters().get(uniformParameterId);
        switch (techniqueParameters.getType())
        {
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            {
                if (techniqueParameters.getCount() == null)
                {
                    return createSingleIntSupplier(
                        uniformName, technique, material);
                }
                return createIntArraySupplier(
                    uniformName, technique, material);
            }

            case GltfConstants.GL_FLOAT: 
            {
                if (techniqueParameters.getCount() == null)
                {
                    return createSingleFloatSupplier(
                        uniformName, technique, material);
                }
                return createFloatArraySupplier(
                    uniformName, technique, material);
            }

            case GltfConstants.GL_FLOAT_MAT3:   
            case GltfConstants.GL_FLOAT_MAT4:   
            case GltfConstants.GL_FLOAT_VEC2:   
            case GltfConstants.GL_FLOAT_VEC3:   
            case GltfConstants.GL_FLOAT_VEC4:   
            {
                return createFloatArraySupplier(
                    uniformName, technique, material);
            }

            case GltfConstants.GL_INT_VEC2:   
            case GltfConstants.GL_INT_VEC3:   
            case GltfConstants.GL_INT_VEC4:   
            {
                return createIntArraySupplier(
                    uniformName, technique, material);
            }
            
            case GltfConstants.GL_SAMPLER_2D:
            {
                return () ->
                {
                    Object object = getUniformValueObject(
                        uniformName, technique, material);
                    return object == null ? null : String.valueOf(object);
                };
            }
            
            // These types are not supported as uniform types in OpenGL
            case GltfConstants.GL_FLOAT_MAT2:   
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
                    techniqueParameters.getType());
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid parameter type: "+ techniqueParameters.getType());
    }




    /**
     * Returns a supplier for the specified uniform value, which is assumed
     * to be a single (signed or unsigned) integer. 
     * 
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @return The supplier
     */
    private static Supplier<?> createSingleIntSupplier(
        String uniformName, Technique technique, Material material)
    {
        return () ->
        {
            Object object = getUniformValueObject(
                uniformName, technique, material);
            if (object == null)
            {
                return null;
            }
            Number number = (Number)object;
            Integer value = number.intValue();
            return value;
        };
    }

    /**
     * Returns a supplier for the specified uniform value, which is assumed
     * to be a an integer array. 
     * 
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @return The supplier
     */
    private static Supplier<?> createIntArraySupplier(
        String uniformName, Technique technique, Material material)
    {
        Supplier<int[]> supplier = new Supplier<int[]>()
        {
            private int value[] = null;
            
            @Override
            public int[] get()
            {
                Object object = getUniformValueObject(
                    uniformName, technique, material);
                if (object == null)
                {
                    return null;
                }
                List<? extends Number> list = asNumberList(object);
                value = toIntArray(list, value);
                return value;
            }
        };
        return supplier;
    }
    
    /**
     * Returns a supplier for the specified uniform value, which is assumed
     * to be a single float value. 
     * 
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @return The supplier
     */
    private static Supplier<?> createSingleFloatSupplier(String uniformName,
        Technique technique, Material material)
    {
        return () ->
        {
            Object object = getUniformValueObject(
                uniformName, technique, material);
            if (object == null)
            {
                return null;
            }
            Number number = (Number)object;
            Float value = number.floatValue();
            return value;
        };
    }
    
    /**
     * Returns a supplier for the specified uniform value, which is assumed
     * to be a float array 
     * 
     * @param uniformName The uniform name
     * @param technique The {@link Technique}
     * @param material The {@link Material}
     * @return The supplier
     */
    private static Supplier<?> createFloatArraySupplier(String uniformName,
        Technique technique, Material material)
    {
        Supplier<float[]> supplier = new Supplier<float[]>()
        {
            private float value[] = null;
            
            @Override
            public float[] get()
            {
                Object object = getUniformValueObject(
                    uniformName, technique, material);
                if (object == null)
                {
                    return null;
                }
                List<? extends Number> list = asNumberList(object);
                value = toFloatArray(list, value);
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
        for (int i=0; i<list.size(); i++)
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
        for (int i=0; i<list.size(); i++)
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
    private GltfModels()
    {
        // Private constructor to prevent instantiation
    }
    

}
