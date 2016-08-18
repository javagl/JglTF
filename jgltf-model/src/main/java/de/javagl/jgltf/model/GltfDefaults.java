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
import java.util.Base64;

import de.javagl.jgltf.impl.Material;
import de.javagl.jgltf.impl.Program;
import de.javagl.jgltf.impl.Shader;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;
import de.javagl.jgltf.impl.TechniqueStates;

// TODO: This class is not public yet.

/**
 * Methods to create default instances of glTF objects.<br>
 * <br>
 * These methods currently mainly cover the default material that should
 * be used when the {@link Material#getTechnique()} is not given.
 * <br> 
 * See https://github.com/KhronosGroup/glTF/blob/master/specification/README.md#appendix-a
 */
class GltfDefaults
{
    /**
     * The source code of the default vertex shader
     */
    private static final String DEFAULT_VERTEX_SHADER_CODE = 
        "#ifdef GL_ES"  + "\n" +
        "  precision highp float;" + "\n" +
        "#endif"+ "\n" + "\n" +
        "uniform mat4 u_modelViewMatrix;" + "\n" + 
        "uniform mat4 u_projectionMatrix;" + "\n" +
        "attribute vec3 a_position;"  + "\n" +
        "void main(void)" + "\n" +
        "{" + "\n" +
        "    gl_Position = u_projectionMatrix * u_modelViewMatrix *" + "\n" +
        "        vec4(a_position,1.0);" + "\n" +
        "}" + "\n" +
        "\n";
    
    /**
     * The source code of the default fragment shader
     */
    private static final String DEFAULT_FRAGMENT_SHADER_CODE =
        "#ifdef GL_ES"  + "\n" +
        "  precision highp float;" + "\n" +
        "#endif"+ "\n" + "\n" +
        "uniform vec4 u_emission;" + "\n" +
        "void main(void)" + "\n" +
        "{" + "\n" +
        "    gl_FragColor = u_emission;" + "\n" +
        "}" + "\n" +
        "\n";
    
    
    /**
     * Creates a default vertex {@link Shader}, with an embedded 
     * representation of the source code in form of a data URI.
     * 
     * @return The default {@link Shader}
     */
    static Shader createDefaultVertexShader()
    {
        Shader shader = new Shader();
        shader.setType(GltfConstants.GL_VERTEX_SHADER);
        String encodedCode = 
            Base64.getEncoder().encodeToString(
                DEFAULT_VERTEX_SHADER_CODE.getBytes());
        String dataUriString = "data:text/plain;base64," + encodedCode;
        shader.setUri(dataUriString);
        return shader;
    }

    /**
     * Creates a default fragment {@link Shader}, with an embedded 
     * representation of the source code in form of a data URI.
     * 
     * @return The default {@link Shader}
     */
    static Shader createDefaultFragmentShader()
    {
        Shader shader = new Shader();
        shader.setType(GltfConstants.GL_FRAGMENT_SHADER);
        String encodedCode = 
            Base64.getEncoder().encodeToString(
                DEFAULT_FRAGMENT_SHADER_CODE.getBytes());
        String dataUriString = "data:text/plain;base64," + encodedCode;
        shader.setUri(dataUriString);
        return shader;
    }
    
    /**
     * Creates a default {@link Program} with the given vertex- and 
     * fragment {@link Shader} IDs, which are assumed to refer to
     * the {@link #createDefaultVertexShader() default vertex shader}
     * and {@link #createDefaultFragmentShader() default fragment shader}.
     * 
     * @param vertexShaderId The vertex {@link Shader} ID
     * @param fragmentShaderId The fragment {@link Shader} ID
     * @return The default {@link Program}
     */
    static Program createDefaultProgram(
        String vertexShaderId, String fragmentShaderId)
    {
        Program program = new Program();
        program.setVertexShader(vertexShaderId);
        program.setFragmentShader(fragmentShaderId);
        program.addAttributes("a_position");
        return program;
    }
    
    /**
     * Create a default {@link Material} with the given {@link Technique} ID,
     * that is assumed to refer to a {@link #createDefaultTechnique(String)
     * default technique}
     * 
     * @param techniqueId The {@link Technique} ID
     * @return The default {@link Material}
     */
    static Material createDefaultMaterial(String techniqueId)
    {
        Material material = new Material();
        material.addValues("emission", Arrays.asList(0.5, 0.5, 0.5, 1.0));
        material.setTechnique(techniqueId);
        return material;
    }
    
    /**
     * Create a default {@link Technique} with the given {@link Program} ID,
     * which is assumed to refer to a {@link #createDefaultProgram(
     * String, String) default program}.
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
                "MODELVIEW", GltfConstants.GL_FLOAT_MAT4));
        technique.addParameters("projectionMatrix", 
            createDefaultTechniqueParameters(
                "PROJECTION", GltfConstants.GL_FLOAT_MAT4));
        technique.addParameters("emission", 
            createDefaultTechniqueParameters(
                null, GltfConstants.GL_FLOAT_VEC4));
        technique.addParameters("position", 
            createDefaultTechniqueParameters(
                "POSITION", GltfConstants.GL_FLOAT_VEC3));
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
     * Create default {@link TechniqueParameters} with the given semantic
     * and type
     * 
     * @param semantic The semantic
     * @param type The type
     * @return The default {@link TechniqueParameters}
     */
    private static TechniqueParameters createDefaultTechniqueParameters(
        String semantic, Integer type)
    {
        TechniqueParameters techniqueParameters = new TechniqueParameters();
        techniqueParameters.setSemantic(semantic);
        techniqueParameters.setType(type);
        return techniqueParameters;
    }
}
