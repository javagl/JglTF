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
package de.javagl.jgltf.obj.model;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.gl.ProgramModel;
import de.javagl.jgltf.model.gl.Semantic;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.gl.ShaderModel.ShaderType;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.gl.TechniqueParametersModel;
import de.javagl.jgltf.model.gl.TechniqueStatesModel;
import de.javagl.jgltf.model.gl.impl.DefaultProgramModel;
import de.javagl.jgltf.model.gl.impl.DefaultShaderModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueParametersModel;
import de.javagl.jgltf.model.gl.impl.TechniqueStatesModels;
import de.javagl.jgltf.model.io.UriResolvers;

/**
 * A class containing the default {@link TechniqueModel} instances that
 * are required for rendering OBJs 
 */
class ObjTechniqueModels
{
    /**
     * The name for the <code>"ambient"</code> technique parameter 
     */
    static final String AMBIENT_NAME = "ambient";

    /**
     * The name for the <code>"diffuse"</code> technique parameter
     */
    static final String DIFFUSE_NAME = "diffuse";

    /**
     * The name for the <code>"specular"</code> technique parameter
     */
    static final String SPECULAR_NAME = "specular";

    /**
     * The name for the <code>"shininess"</code> technique parameter
     */
    static final String SHININESS_NAME = "shininess";
    
    /**
     * The {@link TechniqueModel} without textures and normals 
     */
    static final TechniqueModel TECHNIQUE_MODEL_NONE;
    
    /**
     * The {@link TechniqueModel} with texture and without normals 
     */
    static final TechniqueModel TECHNIQUE_MODEL_TEXTURE;
    
    /**
     * The {@link TechniqueModel} without texture and with normals 
     */
    static final TechniqueModel TECHNIQUE_MODEL_NORMALS;
    
    /**
     * The {@link TechniqueModel} with texture and with normals 
     */
    static final TechniqueModel TECHNIQUE_MODEL_TEXTURE_NORMALS;

    /**
     * Static initialization
     */
    static
    {
        TECHNIQUE_MODEL_NONE = createTechniqueModel(
            false, false, "none.vert", "none.frag");
        
        TECHNIQUE_MODEL_TEXTURE = createTechniqueModel(
            true, false, "texture.vert", "texture.frag");
        
        TECHNIQUE_MODEL_NORMALS = createTechniqueModel(
            false, true, "normals.vert", "normals.frag");
        
        TECHNIQUE_MODEL_TEXTURE_NORMALS = createTechniqueModel(
            true, true, "texture_normals.vert", "texture_normals.frag");
    }
    
    /**
     * Create the specified {@link TechniqueModel}, together with its 
     * {@link ProgramModel} and {@link ShaderModel} objects  
     * 
     * @param withTexture Whether the technique should support a texture
     * @param withNormals Whether the technique should support normals
     * @param vertexShaderUri The vertex shader URI
     * @param fragmentShaderUri The fragment shader URI
     * @return The {@link TechniqueModel}
     */
    private static TechniqueModel createTechniqueModel( 
        boolean withTexture, boolean withNormals,
        String vertexShaderUri, String fragmentShaderUri) 
    {
        String programName = "none";
        if (withTexture && withNormals)
        {
            programName = "texture_normals";
        }
        else if (withTexture)
        {
            programName = "texture";
        }
        else if (withNormals)
        {
            programName = "normals";
        }

        Function<String, ByteBuffer> shaderUriResolver = 
            UriResolvers.createResourceUriResolver(
                ObjTechniqueModels.class);
        
        // Create the vertex shader
        DefaultShaderModel vertexShaderModel = new DefaultShaderModel(
            vertexShaderUri, ShaderType.VERTEX_SHADER);
        vertexShaderModel.setName("vertex_shader_" + programName);
        
        ByteBuffer vertexShaderData = 
            shaderUriResolver.apply(vertexShaderUri);
        vertexShaderModel.setShaderData(vertexShaderData);
        
        // Create the fragment shader
        DefaultShaderModel fragmentShaderModel = new DefaultShaderModel(
            fragmentShaderUri, ShaderType.FRAGMENT_SHADER);
        fragmentShaderModel.setName("fragment_shader_" + programName);
        
        ByteBuffer fragmentShaderData = 
            shaderUriResolver.apply(fragmentShaderUri);
        fragmentShaderModel.setShaderData(fragmentShaderData);
        
        // Create the program
        DefaultProgramModel programModel = new DefaultProgramModel();
        programModel.setName(programName);
        programModel.setVertexShaderModel(vertexShaderModel);
        programModel.setFragmentShaderModel(fragmentShaderModel);
        programModel.addAttribute("a_position");
        if (withTexture)
        {
            programModel.addAttribute("a_texcoord0");
        }
        if (withNormals)
        {
            programModel.addAttribute("a_normal");
        }
        
        // Create the technique
        DefaultTechniqueModel techniqueModel = new DefaultTechniqueModel();
        techniqueModel.setProgramModel(programModel);

        Map<String, String> techniqueAttributes =
            createTechniqueAttributes(withTexture, withNormals);
        for (Entry<String, String> entry : techniqueAttributes.entrySet())
        {
            String k = entry.getKey();
            String v = entry.getValue();
            techniqueModel.addAttribute(k, v);
        }

        Map<String, String> techniqueUniforms = 
            createTechniqueUniforms(withNormals);
        for (Entry<String, String> entry : techniqueUniforms.entrySet())
        {
            String k = entry.getKey();
            String v = entry.getValue();
            techniqueModel.addUniform(k, v);
        }
        
        Map<String, TechniqueParametersModel> techniqueParameters =
            createTechniqueParameters(withTexture, withNormals);
        for (Entry<String, TechniqueParametersModel> entry : 
            techniqueParameters.entrySet())
        {
            String k = entry.getKey();
            TechniqueParametersModel v = entry.getValue();
            techniqueModel.addParameter(k, v);
        }
        
        TechniqueStatesModel states = TechniqueStatesModels.createDefault();
        techniqueModel.setTechniqueStatesModel(states);
        return techniqueModel;
    }


    /**
     * Creates the mapping from attribute names to 
     * technique parameter names 
     * 
     * @param withTexture Whether a texture is present
     * @param withNormals Whether normals are present
     * @return The uniform mapping
     */
    private static Map<String, String> createTechniqueAttributes(
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
     * technique parameter names 
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
     * Create the {@link TechniqueParametersModel} mapping for a 
     * {@link TechniqueModel} with the specified configuration
     * 
     * @param withTexture Whether a texture is present
     * @param withNormals Whether normals are present
     * @return The {@link TechniqueParametersModel} mapping
     */
    private static Map<String, TechniqueParametersModel> 
        createTechniqueParameters(
            boolean withTexture, boolean withNormals)
    {
        Map<String, TechniqueParametersModel> techniqueParameters = 
            new LinkedHashMap<String, TechniqueParametersModel>();
        
        techniqueParameters.put("position", 
            createTechniqueParametersModel(
                GltfConstants.GL_FLOAT_VEC3, "POSITION"));
        
        if (withTexture)
        {
            techniqueParameters.put("texcoord0", 
                createTechniqueParametersModel(
                    GltfConstants.GL_FLOAT_VEC2, "TEXCOORD_0"));
        }
        if (withNormals)
        {
            techniqueParameters.put("normal", 
                createTechniqueParametersModel(
                    GltfConstants.GL_FLOAT_VEC3, "NORMAL"));
        }
        
        techniqueParameters.put("modelViewMatrix", 
            createTechniqueParametersModel(
                GltfConstants.GL_FLOAT_MAT4, 
                Semantic.MODELVIEW.name()));
        if (withNormals)
        {
            techniqueParameters.put("normalMatrix", 
                createTechniqueParametersModel(
                    GltfConstants.GL_FLOAT_MAT3, 
                    Semantic.MODELVIEWINVERSETRANSPOSE.name()));
        }
        techniqueParameters.put("projectionMatrix", 
            createTechniqueParametersModel(
                GltfConstants.GL_FLOAT_MAT4, 
                Semantic.PROJECTION.name()));

        techniqueParameters.put(AMBIENT_NAME, 
            createTechniqueParametersModel(
                GltfConstants.GL_FLOAT_VEC4));
        if (withTexture)
        {
            techniqueParameters.put(DIFFUSE_NAME, 
                createTechniqueParametersModel(
                    GltfConstants.GL_SAMPLER_2D));
        }
        else
        {
            techniqueParameters.put(DIFFUSE_NAME, 
                createTechniqueParametersModel(
                    GltfConstants.GL_FLOAT_VEC4));
        }
        techniqueParameters.put(SPECULAR_NAME, 
            createTechniqueParametersModel(
                GltfConstants.GL_FLOAT_VEC4));
        techniqueParameters.put(SHININESS_NAME, 
            createTechniqueParametersModel(
                GltfConstants.GL_FLOAT));
        return techniqueParameters;
    }
    
    /**
     * Create a {@link TechniqueParametersModel} object that has the given type
     * 
     * @param type The type
     * @return The {@link TechniqueParametersModel}
     */
    private static TechniqueParametersModel createTechniqueParametersModel(
        Integer type)
    {
        DefaultTechniqueParametersModel techniqueParametersModel = 
            new DefaultTechniqueParametersModel(type, 1, null, null, null);
        return techniqueParametersModel;
    }
    
    /**
     * Create a {@link TechniqueParametersModel} object that has the given 
     * type and semantic
     * 
     * @param type The type
     * @param semantic The semantic
     * @return The {@link TechniqueParametersModel}
     */
    private static TechniqueParametersModel createTechniqueParametersModel(
        Integer type, String semantic)
    {
        DefaultTechniqueParametersModel techniqueParametersModel = 
            new DefaultTechniqueParametersModel(type, 1, semantic, null, null);
        return techniqueParametersModel;
    }

    
    /**
     * Private constructor to prevent instantiation
     */
    private ObjTechniqueModels()
    {
        // Private constructor to prevent instantiation
    }
}

