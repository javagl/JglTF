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

/**
 * Some common OpenGL constants that are used in glTF
 */
public class GltfConstants
{
    /**
     * The GL_BGR constant
     */
    public static final int GL_BGR = 32992;
    
    /**
     * The GL_RGB constant
     */
    public static final int GL_RGB = 6407;
    
    /**
     * The GL_RGBA constant
     */
    public static final int GL_RGBA = 6408;
    
    /**
     * The GL_BGRA constant
     */
    public static final int GL_BGRA = 32993;
    

    
    /**
     * The GL_BYTE constant
     */
    public static final int GL_BYTE = 5120;
    
    /**
     * The GL_UNSIGNED_BYTE constant
     */
    public static final int GL_UNSIGNED_BYTE = 5121;

    /**
     * The GL_SHORT constant
     */
    public static final int GL_SHORT = 5122;
    
    /**
     * The GL_UNSIGNED_SHORT constant
     */
    public static final int GL_UNSIGNED_SHORT = 5123;
    
    /**
     * The GL_INT constant
     */
    public static final int GL_INT = 5124;

    /**
     * The GL_UNSIGNED_INT constant
     */
    public static final int GL_UNSIGNED_INT = 5125;
    
    /**
     * The GL_FLOAT constant
     */
    public static final int GL_FLOAT = 5126;
    
    
    
    /**
     * The GL_FLOAT_VEC2 constant
     */
    public static final int GL_FLOAT_VEC2 = 35664;

    /**
     * The GL_FLOAT_VEC3 constant
     */
    public static final int GL_FLOAT_VEC3 = 35665;

    /**
     * The GL_FLOAT_VEC4 constant
     */
    public static final int GL_FLOAT_VEC4 = 35666;

    /**
     * The GL_INT_VEC2 constant
     */
    public static final int GL_INT_VEC2 = 35667;

    /**
     * The GL_INT_VEC3 constant
     */
    public static final int GL_INT_VEC3 = 35668;

    /**
     * The GL_INT_VEC4 constant
     */
    public static final int GL_INT_VEC4 = 35669;

    /**
     * The GL_BOOL constant
     */
    public static final int GL_BOOL = 35670;

    /**
     * The GL_BOOL_VEC2 constant
     */
    public static final int GL_BOOL_VEC2 = 35671;

    /**
     * The GL_BOOL_VEC3 constant
     */
    public static final int GL_BOOL_VEC3 = 35672;

    /**
     * The GL_BOOL_VEC4 constant
     */
    public static final int GL_BOOL_VEC4 = 35673;

    /**
     * The GL_FLOAT_MAT2 constant
     */
    public static final int GL_FLOAT_MAT2 = 35674;

    /**
     * The GL_FLOAT_MAT3 constant
     */
    public static final int GL_FLOAT_MAT3 = 35675;

    /**
     * The GL_FLOAT_MAT4 constant
     */
    public static final int GL_FLOAT_MAT4 = 35676;

    /**
     * The GL_SAMPLER_2D constant
     */
    public static final int GL_SAMPLER_2D = 35678;    
    
    
    
    /**
     * The GL_POINTS constant
     */
    public static final int GL_POINTS = 0;

    /**
     * The GL_LINES constant
     */
    public static final int GL_LINES = 1;

    /**
     * The GL_LINE_LOOP constant
     */
    public static final int GL_LINE_LOOP = 2;

    /**
     * The GL_LINE_STRIP constant
     */
    public static final int GL_LINE_STRIP = 3;

    /**
     * The GL_TRIANGLES constant
     */
    public static final int GL_TRIANGLES = 4;    

    /**
     * The GL_TRIANGLE_STRIP constant
     */
    public static final int GL_TRIANGLE_STRIP = 5;

    /**
     * The GL_TRIANGLE_FAN constant
     */
    public static final int GL_TRIANGLE_FAN = 6;
    
    /**
     * The GL_VERTEX_SHADER constant
     */
    public static final int GL_VERTEX_SHADER = 35633; 
    
    /**
     * The GL_VERTEX_SHADER constant
     */
    public static final int GL_FRAGMENT_SHADER = 35632; 
    
    /**
     * Returns the String representation of the given constant
     * 
     * @param constant The constant
     * @return The String for the constant
     */
    public static String stringFor(int constant)
    {
        switch (constant)
        {
            case GL_BGR : return "GL_BGR";
            case GL_RGB : return "GL_RGB";
            case GL_RGBA : return "GL_RGBA";
            case GL_BGRA : return "GL_BGRA";

            case GL_BYTE : return "GL_BYTE";
            case GL_UNSIGNED_BYTE : return "GL_UNSIGNED_BYTE";
            case GL_SHORT : return "GL_SHORT";
            case GL_UNSIGNED_SHORT : return "GL_UNSIGNED_SHORT";
            case GL_INT : return "GL_INT";
            case GL_UNSIGNED_INT : return "GL_UNSIGNED_INT";
            case GL_FLOAT : return "GL_FLOAT";
            
            case GL_FLOAT_VEC2 : return "GL_FLOAT_VEC2";
            case GL_FLOAT_VEC3 : return "GL_FLOAT_VEC3";
            case GL_FLOAT_VEC4 : return "GL_FLOAT_VEC4";
            case GL_INT_VEC2 : return "GL_INT_VEC2";
            case GL_INT_VEC3 : return "GL_INT_VEC3";
            case GL_INT_VEC4 : return "GL_INT_VEC4";
            case GL_BOOL : return "GL_BOOL";
            case GL_BOOL_VEC2 : return "GL_BOOL_VEC2";
            case GL_BOOL_VEC3 : return "GL_BOOL_VEC3";
            case GL_BOOL_VEC4 : return "GL_BOOL_VEC4";
            case GL_FLOAT_MAT2 : return "GL_FLOAT_MAT2";
            case GL_FLOAT_MAT3 : return "GL_FLOAT_MAT3";
            case GL_FLOAT_MAT4 : return "GL_FLOAT_MAT4";
            case GL_SAMPLER_2D : return "GL_SAMPLER_2D";
            
            case GL_POINTS: return "GL_POINTS";
            case GL_LINES: return "GL_LINES";
            case GL_LINE_LOOP: return "GL_LINE_LOOP";
            case GL_LINE_STRIP: return "GL_LINE_STRIP";
            case GL_TRIANGLES: return "GL_TRIANGLES";
            case GL_TRIANGLE_STRIP: return "GL_TRIANGLE_STRIP";

            case GL_VERTEX_SHADER: return "GL_VERTEX_SHADER";
            case GL_FRAGMENT_SHADER: return "GL_FRAGMENT_SHADER";
            
            default:
                return "UNKNOWN_GL_CONSTANT["+constant+"]";
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfConstants()
    {
        // Private constructor to prevent instantiation
    }
    
    
}
