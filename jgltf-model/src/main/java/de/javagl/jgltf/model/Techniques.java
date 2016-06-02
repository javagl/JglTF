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

import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;

/**
 * Utility methods related to {@link Technique}s
 */
public class Techniques
{

    /**
     * Return the {@link TechniqueParameters} for the uniform with the
     * given name from the given {@link Technique}
     * 
     * @param technique The {@link Technique}
     * @param uniformName The uniform name
     * @return The {@link TechniqueParameters}
     * @throws GltfException If the given technique does not
     * contain a technique parameter ID for the uniform with the given name, 
     * or no technique parameters with the respective ID
     */
    public static TechniqueParameters getUniformTechniqueParameters(
        Technique technique, String uniformName)
    {
        // The technique.uniforms map the uniform names to the
        // technique parameter IDs. The technique.parameters map
        // the technique parameter IDs to the TechniqueParameters. 
        String techniqueParameterId = 
            technique.getUniforms().get(uniformName);
        if (techniqueParameterId == null)
        {
            throw new GltfException(
                "No technique parameter ID for uniform with name " + 
                uniformName + " found in technique");
        }
        TechniqueParameters techniqueParameters = 
            technique.getParameters().get(techniqueParameterId);
        if (techniqueParameters == null)
        {
            throw new GltfException(
                "No technique parameters for technique parameter ID " +
                techniqueParameterId + " found in technique");
        }
        return techniqueParameters;
    }

    /**
     * Return the {@link TechniqueParameters} for the program attribute with 
     * the given name from the given {@link Technique}
     * 
     * @param technique The {@link Technique}
     * @param attributeName The program attribute name
     * @return The {@link TechniqueParameters}
     * @throws GltfException If the given technique does not
     * contain a technique parameter ID for the attribute with the given name, 
     * or no technique parameters with the respective ID
     */
    public static TechniqueParameters getAttributeTechniqueParameters(
        Technique technique, String attributeName)
    {
        // The technique.attributes map GLSL program attribute names 
        // to technique parameter IDs. The technique.parameters
        // map technique parameter IDs to TechniqueParameters.
        String techniqueParameterId = 
            technique.getAttributes().get(attributeName);
        if (techniqueParameterId == null)
        {
            throw new GltfException(
                "No technique parameter ID for attribue with name " + 
                    attributeName + " found in technique");
        }
        TechniqueParameters techniqueParameters = 
            technique.getParameters().get(techniqueParameterId);
        if (techniqueParameters == null)
        {
            throw new GltfException(
                "No technique parameters for technique parameter ID " +
                techniqueParameterId + " found in technique");
        }
        return techniqueParameters;
    }

    /**
     * Returns the component type for the given 
     * {@link TechniqueParameters#getType()}.<br>
     * 
     * The valid parameters and their return values are as follows:
     * <pre><code>
     * GL_BOOL: 
     * GL_BYTE: 
     * GL_UNSIGNED_BYTE:
     * GL_SHORT:
     * GL_UNSIGNED_SHORT:
     * GL_INT:
     * GL_UNSIGNED_INT:
     * GL_FLOAT:
     *     Will be returned directly 
     *
     * GL_FLOAT_VEC2:   
     * GL_FLOAT_VEC3:   
     * GL_FLOAT_VEC4:   
     * GL_FLOAT_MAT2:   
     * GL_FLOAT_MAT3:   
     * GL_FLOAT_MAT4: 
     *     Will return GL_FLOAT
     *       
     * GL_INT_VEC2:   
     * GL_INT_VEC3:   
     * GL_INT_VEC4: 
     *     Will return GL_INT
     *     
     * GL_BOOL_VEC2:   
     * GL_BOOL_VEC3:   
     * GL_BOOL_VEC4: 
     *     Will return GL_BOOL
     * </code></pre>
     * 
     * @param type The {@link TechniqueParameters#getType()}
     * @return The component type
     * @throws IllegalArgumentException If the given type is none of the
     * valid parameters
     */
    public static int getComponentTypeForTechniqueParamtersType(int type)
    {
        switch (type)
        {
            case GltfConstants.GL_BOOL: 
            case GltfConstants.GL_BYTE: 
            case GltfConstants.GL_UNSIGNED_BYTE:
            case GltfConstants.GL_SHORT:
            case GltfConstants.GL_UNSIGNED_SHORT:
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            case GltfConstants.GL_FLOAT: 
                return type;
                
            case GltfConstants.GL_FLOAT_VEC2:   
            case GltfConstants.GL_FLOAT_VEC3:   
            case GltfConstants.GL_FLOAT_VEC4:   
            case GltfConstants.GL_FLOAT_MAT2:   
            case GltfConstants.GL_FLOAT_MAT3:   
            case GltfConstants.GL_FLOAT_MAT4:   
                return GltfConstants.GL_FLOAT;
               
            case GltfConstants.GL_INT_VEC2:   
            case GltfConstants.GL_INT_VEC3:   
            case GltfConstants.GL_INT_VEC4:
                return GltfConstants.GL_INT;
                
            case GltfConstants.GL_BOOL_VEC2:   
            case GltfConstants.GL_BOOL_VEC3:   
            case GltfConstants.GL_BOOL_VEC4:
                return GltfConstants.GL_BOOL;
                
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid technique parameters type: "+type);
    }
    

    /**
     * Returns the number of components for the given
     * {@link TechniqueParameters#getType()}.<br>
     * 
     * The valid parameters and their return values are as follows:
     * <pre><code>
     * GL_BOOL: 
     * GL_BYTE: 
     * GL_UNSIGNED_BYTE:
     * GL_SHORT:
     * GL_UNSIGNED_SHORT:
     * GL_INT:
     * GL_UNSIGNED_INT:
     * GL_FLOAT: 
     *     Will return 1
     *
     * GL_FLOAT_VEC2:   
     * GL_INT_VEC2:   
     * GL_BOOL_VEC2:
     *     Will return 2
     *
     * GL_FLOAT_VEC3:   
     * GL_INT_VEC3:   
     * GL_BOOL_VEC3:
     *     Will return 3
     *
     * GL_FLOAT_VEC4:   
     * GL_BOOL_VEC4:
     * GL_INT_VEC4:
     * GL_FLOAT_MAT2:
     *     Will return 4
     *                
     * GL_FLOAT_MAT3:
     *     Will return 9
     *                
     * GL_FLOAT_MAT4:   
     *     Will return 16
     * </code></pre>
     * 
     * @param type The {@link TechniqueParameters#getType()}
     * @return The number of components
     * @throws IllegalArgumentException If the given type is none of the
     * valid parameters
     */
    public static int getNumComponentsForTechniqueParametersType(int type)
    {
        switch (type)
        {
            case GltfConstants.GL_BOOL: 
            case GltfConstants.GL_BYTE: 
            case GltfConstants.GL_UNSIGNED_BYTE:
            case GltfConstants.GL_SHORT:
            case GltfConstants.GL_UNSIGNED_SHORT:
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            case GltfConstants.GL_FLOAT: 
                return 1;
                
            case GltfConstants.GL_FLOAT_VEC2:   
            case GltfConstants.GL_INT_VEC2:   
            case GltfConstants.GL_BOOL_VEC2:
                return 2;
    
            case GltfConstants.GL_FLOAT_VEC3:   
            case GltfConstants.GL_INT_VEC3:   
            case GltfConstants.GL_BOOL_VEC3:
                return 3;
    
            case GltfConstants.GL_FLOAT_VEC4:   
            case GltfConstants.GL_BOOL_VEC4:
            case GltfConstants.GL_INT_VEC4:
            case GltfConstants.GL_FLOAT_MAT2:
                return 4;
                
            case GltfConstants.GL_FLOAT_MAT3:
                return 9;
                
            case GltfConstants.GL_FLOAT_MAT4:   
                return 16;
    
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid technique parameters type: "+type);
    }

    /**
     * Returns the GL type constant corresponding to the given 
     * {@link TechniqueParameters#getSemantic()}.<br> 
     * <br>
     * If the given semantic is <code>null</code>, then <code>null</code>
     * is returned.<br>
     * <br>
     * Valid parameter types (and their return types) are
     * <pre><code>
     *  "LOCAL"                       FLOAT_MAT4
     *  "MODEL"                       FLOAT_MAT4
     *  "VIEW"                        FLOAT_MAT4
     *  "PROJECTION"                  FLOAT_MAT4
     *  "MODELVIEW"                   FLOAT_MAT4
     *  "MODELVIEWPROJECTION"         FLOAT_MAT4
     *  "MODELINVERSE"                FLOAT_MAT4
     *  "VIEWINVERSE"                 FLOAT_MAT4
     *  "PROJECTIONINVERSE"           FLOAT_MAT4
     *  "MODELVIEWINVERSE"            FLOAT_MAT4
     *  "MODELVIEWPROJECTIONINVERSE"  FLOAT_MAT4
     *  "MODELINVERSETRANSPOSE"       FLOAT_MAT3
     *  "MODELVIEWINVERSETRANSPOSE"   FLOAT_MAT3
     *  "VIEWPORT"                    FLOAT_VEC4    
     * </code></pre>
     * 
     * @param semantic The semantic string
     * @return The type
     * @throws IllegalArgumentException If the given string is none of the
     * valid parameters
     */
    static Integer getTypeForTechniqueParametersSemantic(String semantic)
    {
        if (semantic == null)
        {
            return null;
        }
        switch (semantic)
        {
            case "LOCAL" : 
            case "MODEL" :
            case "VIEW" :
            case "PROJECTION" :
            case "MODELVIEW" :
            case "MODELVIEWPROJECTION" :
            case "MODELINVERSE" :
            case "VIEWINVERSE" :
            case "PROJECTIONINVERSE" :
            case "MODELVIEWINVERSE" :
            case "MODELVIEWPROJECTIONINVERSE" :
                return GltfConstants.GL_FLOAT_MAT4;
    
            case "MODELINVERSETRANSPOSE":
            case "MODELVIEWINVERSETRANSPOSE":
                return GltfConstants.GL_FLOAT_MAT3;
    
            case "VIEWPORT":
                return GltfConstants.GL_FLOAT_VEC4;
                
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid technique parameters semantic: "+semantic);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Techniques()
    {
        // Private constructor to prevent instantiation
    }
    
}
