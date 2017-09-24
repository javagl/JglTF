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
package de.javagl.jgltf.obj.v1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Program;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueParameters;
import de.javagl.jgltf.impl.v1.TechniqueStates;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.gl.Semantic;
import de.javagl.jgltf.model.v1.GltfIds;

/**
 * A class for managing the {@link Technique}s that may be required for
 * rendering data that is contained in an OBJ file. It allows obtaining
 * the {@link Technique}s for elements with and without textures and
 * normals, and adds the required {@link Program}s and {@link Shader}s
 * to a {@link GlTF} when the IDs of the corresponding {@link Technique}s
 * are requested 
 */
class TechniqueHandler
{
    /**
     * The ID of the {@link Technique} that has a texture and normals
     */
    private static final String TECHNIQUE_TEXTURE_NORMALS_ID = 
        "techniqueTextureNormals";
    
    /**
     * The ID of the {@link Technique} that has a texture
     */
    private static final String TECHNIQUE_TEXTURE_ID = 
        "techniqueTexture";
    
    /**
     * The ID of the {@link Technique} that has normals
     */
    private static final String TECHNIQUE_NORMALS_ID =
        "techniqueNormals";
    
    /**
     * The ID of the {@link Technique} that has neither a texture nor normals
     */
    private static final String TECHNIQUE_NONE_ID = 
        "techniqueNone";

    /**
     * The name for the <code>"ambient"</code> 
     * {@link Technique#getParameters() technique parameter}
     */
    static final String AMBIENT_NAME = "ambient";

    /**
     * The name for the <code>"diffuse"</code> 
     * {@link Technique#getParameters() technique parameter}
     */
    static final String DIFFUSE_NAME = "diffuse";

    /**
     * The name for the <code>"specular"</code> 
     * {@link Technique#getParameters() technique parameter}
     */
    static final String SPECULAR_NAME = "specular";

    /**
     * The name for the <code>"shininess"</code> 
     * {@link Technique#getParameters() technique parameter}
     */
    static final String SHININESS_NAME = "shininess";
    
    /**
     * The {@link GlTF} that will receive the elements that are created by
     * this instance
     */
    private final GlTF gltf;
    
    /**
     * Create a new technique handler
     * @param gltf The {@link GlTF} that will receive the {@link Technique}s, 
     * {@link Program}s and {@link Shader}s that are created by this instance
     * upon request 
     */
    TechniqueHandler(GlTF gltf)
    {
        this.gltf = gltf;
    }
    
    /**
     * Returns the ID of the {@link Technique} with the given properties.
     * If the corresponding {@link Technique} was not created yet, it will
     * be created, and will be added to the {@link GlTF} that was given
     * in the constructor, together with the {@link Program} and 
     * {@link Shader} instances
     * 
     * @param withTexture Whether the {@link Technique} should support a texture
     * @param withNormals Whether the {@link Technique} should support normals
     * @return The {@link Technique} ID
     */
    String getTechniqueId(boolean withTexture, boolean withNormals)
    {
        if (withTexture && withNormals)
        {
            String techniqueId = TECHNIQUE_TEXTURE_NORMALS_ID;
            String vertexShaderUri = "texture_normals.vert";
            String fragmentShaderUri = "texture_normals.frag";
            createTechnique(techniqueId,
                withTexture, withNormals, 
                vertexShaderUri, fragmentShaderUri);
            return techniqueId;
        }
        if (withTexture && !withNormals)
        {
            String techniqueId = TECHNIQUE_TEXTURE_ID;
            String vertexShaderUri = "texture.vert";
            String fragmentShaderUri = "texture.frag";
            createTechnique(techniqueId,
                withTexture, withNormals, 
                vertexShaderUri, fragmentShaderUri);
            return techniqueId;
        }
        if (!withTexture && withNormals)
        {
            String techniqueId = TECHNIQUE_NORMALS_ID;
            String vertexShaderUri = "normals.vert";
            String fragmentShaderUri = "normals.frag";
            createTechnique(techniqueId,
                withTexture, withNormals, 
                vertexShaderUri, fragmentShaderUri);
            return techniqueId;
        }
        String techniqueId = TECHNIQUE_NONE_ID;
        String vertexShaderUri = "none.vert";
        String fragmentShaderUri = "none.frag";
        createTechnique(techniqueId,
            withTexture, withNormals, 
            vertexShaderUri, fragmentShaderUri);
        return techniqueId;
    }

    /**
     * Create the specified {@link Technique}, if it does not exist
     * yet, and add it to the the {@link GlTF} that was given in 
     * the constructor, together with its {@link Program} and 
     * {@link Shader}s  
     * 
     * @param techniqueId The {@link Technique} ID
     * @param withTexture Whether the {@link Technique} should support a texture
     * @param withNormals Whether the {@link Technique} should support normals
     * @param vertexShaderUri The {@link Shader#getUri() vertex shader URI}
     * @param fragmentShaderUri The {@link Shader#getUri() fragment shader URI}
     */
    private void createTechnique(String techniqueId, 
        boolean withTexture, boolean withNormals,
        String vertexShaderUri, String fragmentShaderUri) 
    {
        Map<String, Technique> techniques = gltf.getTechniques();
        if (techniques != null)
        {
            Technique technique = techniques.get(techniqueId);
            if (technique != null)
            {
                return;
            }
        }
        String programId = GltfIds.generateId("program", gltf.getPrograms()); 
        
        Shader vertexShader = new Shader();
        vertexShader.setUri(vertexShaderUri);
        vertexShader.setType(GltfConstants.GL_VERTEX_SHADER);
        String vertexShaderId = GltfIds.generateId(
            "vertexShader_for_" + programId, gltf.getShaders());
        gltf.addShaders(vertexShaderId, vertexShader);
        
        Shader fragmentShader = new Shader();
        fragmentShader.setUri(fragmentShaderUri);
        fragmentShader.setType(GltfConstants.GL_FRAGMENT_SHADER);
        String fragmentShaderId = GltfIds.generateId(
            "fragmentShader_for_" + programId, gltf.getShaders());
        gltf.addShaders(fragmentShaderId, fragmentShader);
        
        Program program = createProgram(
            withTexture, withNormals, vertexShaderId, fragmentShaderId);
        gltf.addPrograms(programId, program);
        
        
        Technique technique = new Technique();
        technique.setProgram(programId);

        Map<String, String> techniqueAttributes =
            createTechniqueAttributes(withTexture, withNormals);
        technique.setAttributes(techniqueAttributes);

        Map<String, TechniqueParameters> techniqueParameters =
            createTechniqueParameters(withTexture, withNormals);
        technique.setParameters(techniqueParameters);
        
        Map<String, String> techniqueUniforms = 
            createTechniqueUniforms(withNormals);
        technique.setUniforms(techniqueUniforms);
        
        TechniqueStates states = new TechniqueStates();
        states.addEnable(GltfConstants.GL_DEPTH_TEST);
        technique.setStates(states);
        
        gltf.addTechniques(techniqueId, technique);
    }


    /**
     * Create a {@link Program} for the given configuration
     * 
     * @param withTexture Whether a texture is present
     * @param withNormals Whether normals are present
     * @param vertexShaderId The vertex {@link Shader} ID
     * @param fragmentShaderId The fragment {@link Shader} ID
     * @return The {@link Program}
     */
    private Program createProgram(boolean withTexture, boolean withNormals,
        String vertexShaderId, String fragmentShaderId)
    {
        Program program = new Program();
        program.setVertexShader(vertexShaderId);
        program.setFragmentShader(fragmentShaderId);
        List<String> programAttributes = new ArrayList<String>();
        programAttributes.add("a_position");
        if (withTexture)
        {
            programAttributes.add("a_texcoord0");
        }
        if (withNormals)
        {
            programAttributes.add("a_normal");
        }
        program.setAttributes(programAttributes);
        return program;
    }

    /**
     * Creates the mapping from attribute names to 
     * {@link Technique#getParameters() technique parameters} names 
     * 
     * @param withTexture Whether a texture is present
     * @param withNormals Whether normals are present
     * @return The uniform mapping
     */
    private Map<String, String> createTechniqueAttributes(
        boolean withTexture, boolean withNormals)
    {
        Map<String, String> techniqueAttributes = 
            new LinkedHashMap<String, String>();
        techniqueAttributes.put("a_position", "position");
        if (withTexture)
        {
            techniqueAttributes.put("a_texcoord0", "texcoord0");
        }
        if (withNormals)
        {
            techniqueAttributes.put("a_normal", "normal");
        }
        return techniqueAttributes;
    }
    
    /**
     * Creates the mapping from uniform names to 
     * {@link Technique#getParameters() technique parameters} names 
     * 
     * @param withNormals Whether normals are present
     * @return The uniform mapping
     */
    private static Map<String, String> createTechniqueUniforms(
        boolean withNormals)
    {
        Map<String, String> techniqueUniforms = 
            new LinkedHashMap<String, String>();
        techniqueUniforms.put("u_ambient", AMBIENT_NAME);
        techniqueUniforms.put("u_diffuse", DIFFUSE_NAME);
        techniqueUniforms.put("u_specular", SPECULAR_NAME);
        techniqueUniforms.put("u_shininess", SHININESS_NAME);
        techniqueUniforms.put("u_modelViewMatrix", "modelViewMatrix");
        if (withNormals)
        {
            techniqueUniforms.put("u_normalMatrix", "normalMatrix");
        }
        techniqueUniforms.put("u_projectionMatrix", "projectionMatrix");
        return techniqueUniforms;
    }

    /**
     * Create the {@link TechniqueParameters} mapping for a {@link Technique} 
     * with the specified configuration
     * 
     * @param withTexture Whether a texture is present
     * @param withNormals Whether normals are present
     * @return The {@link TechniqueParameters} mapping
     */
    private static Map<String, TechniqueParameters> createTechniqueParameters(
        boolean withTexture, boolean withNormals)
    {
        Map<String, TechniqueParameters> techniqueParameters = 
            new LinkedHashMap<String, TechniqueParameters>();
        
        techniqueParameters.put("position", 
            createTechniqueParameters(
                GltfConstants.GL_FLOAT_VEC3, "POSITION"));
        
        if (withTexture)
        {
            techniqueParameters.put("texcoord0", 
                createTechniqueParameters(
                    GltfConstants.GL_FLOAT_VEC2, "TEXCOORD_0"));
        }
        if (withNormals)
        {
            techniqueParameters.put("normal", 
                createTechniqueParameters(
                    GltfConstants.GL_FLOAT_VEC3, "NORMAL"));
        }
        
        techniqueParameters.put("modelViewMatrix", 
            createTechniqueParameters(
                GltfConstants.GL_FLOAT_MAT4, 
                Semantic.MODELVIEW.name()));
        if (withNormals)
        {
            techniqueParameters.put("normalMatrix", 
                createTechniqueParameters(
                    GltfConstants.GL_FLOAT_MAT3, 
                    Semantic.MODELVIEWINVERSETRANSPOSE.name()));
        }
        techniqueParameters.put("projectionMatrix", 
            createTechniqueParameters(
                GltfConstants.GL_FLOAT_MAT4, 
                Semantic.PROJECTION.name()));

        techniqueParameters.put(AMBIENT_NAME, 
            createTechniqueParameters(
                GltfConstants.GL_FLOAT_VEC4));
        if (withTexture)
        {
            techniqueParameters.put(DIFFUSE_NAME, 
                createTechniqueParameters(
                    GltfConstants.GL_SAMPLER_2D));
        }
        else
        {
            techniqueParameters.put(DIFFUSE_NAME, 
                createTechniqueParameters(
                    GltfConstants.GL_FLOAT_VEC4));
        }
        techniqueParameters.put(SPECULAR_NAME, 
            createTechniqueParameters(
                GltfConstants.GL_FLOAT_VEC4));
        techniqueParameters.put(SHININESS_NAME, 
            createTechniqueParameters(
                GltfConstants.GL_FLOAT));
        return techniqueParameters;
    }
    
    /**
     * Create a {@link TechniqueParameters} object that has the given 
     * {@link TechniqueParameters#getType() type}
     * 
     * @param type The type
     * @return The {@link TechniqueParameters}
     */
    private static TechniqueParameters createTechniqueParameters(
        Integer type)
    {
        TechniqueParameters techniqueParameters = new TechniqueParameters();
        techniqueParameters.setType(type);
        return techniqueParameters;
    }
    
    /**
     * Create a {@link TechniqueParameters} object that has the given 
     * {@link TechniqueParameters#getType() type} and
     * {@link TechniqueParameters#getSemantic() semantic}
     * 
     * @param type The type
     * @param semantic The semantic
     * @return The {@link TechniqueParameters}
     */
    private static TechniqueParameters createTechniqueParameters(
        Integer type, String semantic)
    {
        TechniqueParameters techniqueParameters = new TechniqueParameters();
        techniqueParameters.setType(type);
        techniqueParameters.setSemantic(semantic);
        return techniqueParameters;
    }
    
}

