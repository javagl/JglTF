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
package de.javagl.jgltf.model.v1.gl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Program;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueParameters;
import de.javagl.jgltf.impl.v1.TechniqueStates;
import de.javagl.jgltf.impl.v1.TechniqueStatesFunctions;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.gl.Semantic;

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
     * default {@link Material}, as described in "Appendix A" of the 
     * glTF 1.0 specification. 
     * 
     * @param programId The {@link Program} ID
     * @return The default {@link Technique}
     */
    static Technique createDefaultTechnique(String programId)
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
        techniqueStates.setEnable(
            new ArrayList<Integer>(techniqueStates.defaultEnable()));
        techniqueStates.setFunctions(createDefaultTechniqueStatesFunctions());
        return techniqueStates;
    }
    
    /**
     * Create the default {@link TechniqueStatesFunctions}
     *  
     * @return The default {@link TechniqueStatesFunctions}
     */
    private static TechniqueStatesFunctions 
        createDefaultTechniqueStatesFunctions()
    {
        TechniqueStatesFunctions techniqueStatesFunctions = 
            new TechniqueStatesFunctions();
        techniqueStatesFunctions.setBlendColor(
            techniqueStatesFunctions.defaultBlendColor());
        techniqueStatesFunctions.setBlendEquationSeparate(
            techniqueStatesFunctions.defaultBlendEquationSeparate());
        techniqueStatesFunctions.setBlendFuncSeparate(
            techniqueStatesFunctions.defaultBlendFuncSeparate());
        techniqueStatesFunctions.setColorMask(
            techniqueStatesFunctions.defaultColorMask());
        techniqueStatesFunctions.setCullFace(
            techniqueStatesFunctions.defaultCullFace());
        techniqueStatesFunctions.setDepthFunc(
            techniqueStatesFunctions.defaultDepthFunc());
        techniqueStatesFunctions.setDepthMask(
            techniqueStatesFunctions.defaultDepthMask());
        techniqueStatesFunctions.setDepthRange(
            techniqueStatesFunctions.defaultDepthRange());
        techniqueStatesFunctions.setFrontFace(
            techniqueStatesFunctions.defaultFrontFace());
        techniqueStatesFunctions.setLineWidth(
            techniqueStatesFunctions.defaultLineWidth());
        techniqueStatesFunctions.setPolygonOffset(
            techniqueStatesFunctions.defaultPolygonOffset());
        techniqueStatesFunctions.setScissor(
            techniqueStatesFunctions.defaultScissor());
        return techniqueStatesFunctions;
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
     * Returns the set of states that should be enabled for the given 
     * {@link Technique}
     * 
     * @param technique The {@link Technique}
     * @return The enabled states
     */
    public static List<Integer> obtainEnabledStates(Technique technique)
    {
        TechniqueStates states = obtainTechniqueStates(technique);
        List<Integer> enable = states.getEnable();
        if (enable == null)
        {
            return states.defaultEnable();
        }
        return enable;
    }

    /**
     * Return the {@link TechniqueStates} from the given {@link Technique},
     * or the {@link TechniqueStates} from the 
     * {@link GltfDefaults#getDefaultTechnique() default technique} if
     * the given {@link Technique} is <code>null</code> or does not 
     * contain any {@link TechniqueStates}
     *  
     * @param technique The {@link Technique}
     * @return The {@link TechniqueStates}
     */
    public static TechniqueStates obtainTechniqueStates(Technique technique)
    {
        TechniqueStates states = technique.getStates();
        if (states == null)
        {
            return GltfDefaults.getDefaultTechnique().getStates();
        }
        return states;
    }

    /**
     * Return the {@link TechniqueStatesFunctions} from the 
     * {@link TechniqueStates} of the given {@link Technique}, or the 
     * {@link TechniqueStatesFunctions} from the 
     * {@link GltfDefaults#getDefaultTechnique() default technique} if
     * the given {@link Technique} is <code>null</code> or does not 
     * contain any {@link TechniqueStates} or {@link TechniqueStatesFunctions}
     *  
     * @param technique The {@link Technique}
     * @return The {@link TechniqueStatesFunctions}
     */
    public static TechniqueStatesFunctions obtainTechniqueStatesFunctions(
        Technique technique)
    {
        TechniqueStates states = obtainTechniqueStates(technique);
        TechniqueStatesFunctions functions = states.getFunctions();
        if (functions == null)
        {
            TechniqueStates defaultStates = 
                GltfDefaults.getDefaultTechnique().getStates();
            return defaultStates.getFunctions();
        }
        return functions;
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
    
    /**
     * Private constructor to prevent instantiation
     */
    private Techniques()
    {
        // Private constructor to prevent instantiation
    }
    
}
