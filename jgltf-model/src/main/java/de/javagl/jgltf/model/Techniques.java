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

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import de.javagl.jgltf.impl.Material;
import de.javagl.jgltf.impl.Program;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;
import de.javagl.jgltf.impl.TechniqueStates;

/**
 * Utility methods related to {@link Technique}s
 */
public class Techniques
{
    /**
     * Create a default {@link Technique} with the given {@link Program} ID,
     * which is assumed to refer to a {@link Programs#createDefaultProgram(
     * String, String) default program}.<br>
     * <br>
     * The returned {@link Technique} is the {@link Technique} for the 
     * default {@link Material}, as described in 
     * https://github.com/KhronosGroup/glTF/blob/master/specification/README.md#appendix-a
     * 
     * @param programId The {@link Program} ID
     * @return The default {@link Technique}
     */
    public static Technique createDefaultTechnique(String programId)
    {
        Technique technique = new Technique();
        technique.addAttributes("a_position", "position");
        technique.addParameters("modelViewMatrix", 
            createDefaultTechniqueParameters(
                "MODELVIEW", GltfConstants.GL_FLOAT_MAT4, null));
        technique.addParameters("projectionMatrix", 
            createDefaultTechniqueParameters(
                "PROJECTION", GltfConstants.GL_FLOAT_MAT4, null));
        technique.addParameters("emission", 
            createDefaultTechniqueParameters(
                null, GltfConstants.GL_FLOAT_VEC4, 
                Arrays.asList(0.5f, 0.5f, 0.5f, 1.0f)));
        technique.addParameters("position", 
            createDefaultTechniqueParameters(
                "POSITION", GltfConstants.GL_FLOAT_VEC3, null));
        technique.setStates(createDefaultTechniqueStates());
        technique.setProgram(programId);
        
        technique.addUniforms("u_modelViewMatrix", "modelViewMatrix");
        technique.addUniforms("u_projectionMatrix", "projectionMatrix");
        technique.addUniforms("u_emission", "emission");
        
        return technique;
    }
    
    /**
     * Create the default {@link TechniqueStates}
     * 
     * @return The default {@link TechniqueStates}
     */
    private static TechniqueStates createDefaultTechniqueStates()
    {
        TechniqueStates techniqueStates = new TechniqueStates();
        techniqueStates.addEnable(2884); // GL_CULL_FACE
        techniqueStates.addEnable(2929); // GL_DEPTH_TEST
        return techniqueStates;
    }

    /**
     * Create default {@link TechniqueParameters} with the given semantic,
     * type and value
     * 
     * @param semantic The semantic
     * @param type The type
     * @param value The value
     * @return The default {@link TechniqueParameters}
     */
    private static TechniqueParameters createDefaultTechniqueParameters(
        String semantic, Integer type, Object value)
    {
        TechniqueParameters techniqueParameters = new TechniqueParameters();
        techniqueParameters.setSemantic(semantic);
        techniqueParameters.setType(type);
        techniqueParameters.setValue(value);
        return techniqueParameters;
    }
    
    
    /**
     * Return the {@link TechniqueParameters} for the uniform with the
     * given name from the given {@link Technique}. If there are no
     * uniforms or parameters in the given {@link Technique}, or the
     * matching parameter is not found, then <code>null</code> is returned.
     * 
     * @param technique The {@link Technique}
     * @param uniformName The uniform name
     * @return The {@link TechniqueParameters}
     */
    public static TechniqueParameters 
        getOptionalUniformTechniqueParameters(
            Technique technique, String uniformName)
    {
        // The technique.uniforms map the uniform names to the
        // technique parameter IDs. The technique.parameters map
        // the technique parameter IDs to the TechniqueParameters.
        Map<String, String> uniforms = technique.getUniforms();
        if (uniforms == null)
        {
            return null;
        }
        String techniqueParameterId = uniforms.get(uniformName);
        if (techniqueParameterId == null)
        {
            return null;
        }
        Map<String, TechniqueParameters> parameters = technique.getParameters();
        if (parameters == null)
        {
            return null;
        }
        return parameters.get(techniqueParameterId);
    }
    
    /**
     * Return the {@link TechniqueParameters} for the program attribute with 
     * the given name from the given {@link Technique}. If there are no
     * attributes or parameters in the given {@link Technique}, or the
     * matching parameter is not found, then <code>null</code> is returned.
     *  
     * @param technique The {@link Technique}
     * @param attributeName The program attribute name
     * @return The {@link TechniqueParameters}
     */
    public static TechniqueParameters 
        getOptionalAttributeTechniqueParameters(
            Technique technique, String attributeName)
    {
        // The technique.attributes map GLSL program attribute names 
        // to technique parameter IDs. The technique.parameters
        // map technique parameter IDs to TechniqueParameters.
        Map<String, String> attributes = technique.getAttributes();
        if (attributes == null)
        {
            return null;
        }
        String techniqueParameterId = attributes.get(attributeName);
        if (techniqueParameterId == null)
        {
            return null;
        }
        Map<String, TechniqueParameters> parameters = technique.getParameters();
        if (parameters == null)
        {
            return null;
        }
        return parameters.get(techniqueParameterId);
    }
    

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
        Map<String, String> uniforms = Optional
            .ofNullable(technique.getUniforms())
            .orElse(Collections.emptyMap());
        String techniqueParameterId = uniforms.get(uniformName);
        if (techniqueParameterId == null)
        {
            throw new GltfException(
                "No technique parameter ID for uniform with name " + 
                uniformName + " found in technique");
        }
        Map<String, TechniqueParameters> parameters = Optional
            .ofNullable(technique.getParameters())
            .orElse(Collections.emptyMap());
        TechniqueParameters techniqueParameters = 
            parameters.get(techniqueParameterId);
        if (techniqueParameters == null)
        {
            throw new GltfException(
                "No technique parameters for technique parameter ID " +
                techniqueParameterId + " for uniform with name " +
                uniformName + " found in technique");
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
        Map<String, String> attributes = Optional
            .ofNullable(technique.getAttributes())
            .orElse(Collections.emptyMap());
        String techniqueParameterId = attributes.get(attributeName);
        if (techniqueParameterId == null)
        {
            throw new GltfException(
                "No technique parameter ID for attribue with name " + 
                 attributeName + " found in technique");
        }
        Map<String, TechniqueParameters> parameters = Optional
            .ofNullable(technique.getParameters())
            .orElse(Collections.emptyMap());
        TechniqueParameters techniqueParameters = 
            parameters.get(techniqueParameterId);
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
     *  "JOINTMATRIX"                 FLOAT_MAT4
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
        if (!Semantic.contains(semantic))
        {
            throw new IllegalArgumentException(
                "Not a valid semantic: " + semantic);
        }
        Semantic s = Semantic.valueOf(semantic);
        switch (s)
        {
            case LOCAL: 
            case MODEL:
            case VIEW:
            case PROJECTION:
            case MODELVIEW:
            case MODELVIEWPROJECTION:
            case MODELINVERSE:
            case VIEWINVERSE:
            case PROJECTIONINVERSE:
            case MODELVIEWINVERSE:
            case MODELVIEWPROJECTIONINVERSE:
            case JOINTMATRIX:
                return GltfConstants.GL_FLOAT_MAT4;
    
            case MODELINVERSETRANSPOSE:
            case MODELVIEWINVERSETRANSPOSE:
                return GltfConstants.GL_FLOAT_MAT3;
    
            case VIEWPORT:
                return GltfConstants.GL_FLOAT_VEC4;
                
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Invalid technique parameters semantic: " + semantic);
    }

//    // TODO Really using "Optional" here does not make things better ...
//    /**
//     * Return the {@link TechniqueParameters} for the uniform with the
//     * given name from the given {@link Technique}. If there are no
//     * uniforms or parameters in the given {@link Technique}, or the
//     * matching parameter is not found, then the optional will be empty.
//     * 
//     * @param technique The {@link Technique}
//     * @param uniformName The uniform name
//     * @return The optional {@link TechniqueParameters}
//     */
//    private static Optional<TechniqueParameters> 
//    getOptionalUniformTechniqueParameters(
//        Technique technique, String uniformName)
//    {
//        // The technique.uniforms map the uniform names to the
//        // technique parameter IDs. The technique.parameters map
//        // the technique parameter IDs to the TechniqueParameters.
//        Optional<String> techniqueParameterId = 
//            Optional.ofNullable(technique.getUniforms())
//            .map(u -> u.get(uniformName));
//        Optional<TechniqueParameters> techniqueParameters = 
//            Optional.ofNullable(technique.getParameters())
//            .flatMap(p -> techniqueParameterId.map(t -> p.get(t)));
//        return techniqueParameters;
//    }
//
//    /**
//     * Return the {@link TechniqueParameters} for the program attribute with 
//     * the given name from the given {@link Technique}. If there are no
//     * attributes or parameters in the given {@link Technique}, or the
//     * matching parameter is not found, then the optional will be empty.
//     *  
//     * @param technique The {@link Technique}
//     * @param attributeName The program attribute name
//     * @return The {@link TechniqueParameters}
//     */
//    private static Optional<TechniqueParameters> 
//    getOptionalAttributeTechniqueParameters(
//        Technique technique, String attributeName)
//    {
//        // The technique.attributes map GLSL program attribute names 
//        // to technique parameter IDs. The technique.parameters
//        // map technique parameter IDs to TechniqueParameters.
//        Optional<String> techniqueParameterId = 
//            Optional.ofNullable(technique.getAttributes())
//            .map(u -> u.get(attributeName));
//        Optional<TechniqueParameters> techniqueParameters = 
//            Optional.ofNullable(technique.getParameters())
//            .flatMap(p -> techniqueParameterId.map(t -> p.get(t)));
//        return techniqueParameters;
//    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Techniques()
    {
        // Private constructor to prevent instantiation
    }
    
}
