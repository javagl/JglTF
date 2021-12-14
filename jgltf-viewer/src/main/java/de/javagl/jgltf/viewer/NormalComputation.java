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

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;

/**
 * Methods to create default normals
 */
public class NormalComputation
{
    /**
     * Create the default (flat) normals for the given vertex positions.
     * 
     * If the given indices are <code>null</code>, then default indices
     * will be used.
     * 
     * @param positionsAccessorModel The positions {@link AccessorModel}
     * @param indicesAccessorModel The indices {@link AccessorModel}
     * @return The normals {@link AccessorModel}
     */
    static AccessorModel createDefaultNormals(
        AccessorModel positionsAccessorModel, 
        AccessorModel indicesAccessorModel)
    {
        int numPositions = positionsAccessorModel.getCount();
        int numTriangles = numPositions / 3;

        int indices[] = null;
        if (indicesAccessorModel != null)
        {
            AccessorData indicesAccessorData = 
                AccessorDatas.create(indicesAccessorModel);
            indices = AccessorDataUtils.readInts(indicesAccessorData);
        } 
        else
        {
            indices = createDefaultIndices(numTriangles);
        }
        AccessorFloatData positionsAccessorData = 
            AccessorDatas.createFloat(positionsAccessorModel);
        
        AccessorModel normalsAccessorModel = 
            AccessorModelCreation.createAccessorModel(
                GltfConstants.GL_FLOAT, numPositions, ElementType.VEC3, "");
        AccessorFloatData normalsAccessorData = 
            AccessorDatas.createFloat(normalsAccessorModel); 
        
        float vertex0[] = new float[3];
        float vertex1[] = new float[3];
        float vertex2[] = new float[3];
        float edge01[] = new float[3];
        float edge02[] = new float[3];
        float cross[] = new float[3];
        float normal[] = new float[3];
        for (int i = 0; i < numTriangles; i++)
        {
            int index0 = indices[i * 3 + 0];
            int index1 = indices[i * 3 + 1];
            int index2 = indices[i * 3 + 2];
            getVector3D(positionsAccessorData, index0, vertex0);
            getVector3D(positionsAccessorData, index1, vertex1);
            getVector3D(positionsAccessorData, index2, vertex2);
            subtract(vertex1, vertex0, edge01);
            subtract(vertex2, vertex0, edge02);
            cross(edge01, edge02, cross);
            normalize(cross, normal);
            setVector3D(normalsAccessorData, index0, normal);
            setVector3D(normalsAccessorData, index1, normal);
            setVector3D(normalsAccessorData, index2, normal);
        }
        return normalsAccessorModel;
    }
    
    /**
     * Create default indices for the given number of triangles
     * 
     * @param numTriangles The number of triangles
     * @return The indices
     */
    static int[] createDefaultIndices(int numTriangles)
    {
        int n = numTriangles * 3;
        int result[] = new int[n];
        for (int i = 0; i < n; i++)
        {
            result[i] = i;
        }
        return result;
    }
    
    /**
     * Computes a0-a1, and stores the result in the given array.
     * 
     * This assumes that the given arrays are non-<code>null</code> 
     * and have equal lengths.
     *   
     * @param a0 The first array
     * @param a1 The second array
     * @param result The array that stores the result
     */
    private static void subtract(float[] a0, float[] a1, float[] result)
    {
        for (int i = 0; i < a0.length; i++)
        {
            result[i] = a0[i] - a1[i];
        }
    }    

    
    /**
     * Computes the cross product of a0 and a1, and stores the result in 
     * the given array.
     * 
     * This assumes that the given arrays are non-<code>null</code> 
     * and have length 3.
     *   
     * @param a0 The first array
     * @param a1 The second array
     * @param result The array that stores the result
     */
    private static void cross(float a0[], float a1[], float result[])
    {
        result[0] = a0[1] * a1[2] - a0[2] * a1[1];
        result[1] = a0[2] * a1[0] - a0[0] * a1[2];
        result[2] = a0[0] * a1[1] - a0[1] * a1[0];
    }
    
    /**
     * Compute the length of the given vector
     * 
     * @param a The vector
     * @return The length
     */
    private static float computeLength(float a[])
    {
        float sum = 0;
        for (int i=0; i<a.length; i++)
        {
            sum += a[i] * a[i];
        }
        float r = (float) Math.sqrt(sum);
        return r;
    }
    
    /**
     * Normalizes the given array, and stores the result in the given array.
     * 
     * This assumes that the given arrays are non-<code>null</code> 
     * and have the same length.
     *   
     * @param a The array
     * @param result The array that stores the result
     */
    private static void normalize(float a[], float result[])
    {
        float scaling = 1.0f / computeLength(a);
        scale(a, scaling, result);
    }
    
    /**
     * Scales the given vector with the given factor, and stores the result
     * in the given array.
     * 
     * This assumes that the given arrays are non-<code>null</code> 
     * and have equal lengths.
     * 
     * @param a The vector
     * @param factor The scaling factor
     * @param result The array that will store the result
     */
    private static void scale(float a[], float factor, float result[])
    {
        for (int i = 0; i < a.length; i++)
        {
            result[i] = a[i] * factor;
        }
    }
    
    /**
     * Obtain a 3D vector from the given {@link AccessorFloatData}
     * 
     * @param accessorFloatData The data
     * @param index The index of the vector (element)
     * @param result The array that will store the result
     */
    private static void getVector3D(AccessorFloatData accessorFloatData,
        int index, float result[])
    {
        result[0] = accessorFloatData.get(index, 0);
        result[1] = accessorFloatData.get(index, 1);
        result[2] = accessorFloatData.get(index, 2);
    }    
    
    /**
     * Set a 3D vector in the given {@link AccessorFloatData}
     * 
     * @param accessorFloatData The data
     * @param index The index of the vector (element)
     * @param vector The vector to set
     */
    private static void setVector3D(AccessorFloatData accessorFloatData,
        int index, float vector[])
    {
        accessorFloatData.set(index, 0, vector[0]);
        accessorFloatData.set(index, 1, vector[1]);
        accessorFloatData.set(index, 2, vector[2]);
    }    

    /**
     * Private constructor to prevent instantiation
     */
    private NormalComputation()
    {
        // Private constructor to prevent instantiation
    }
    
}
