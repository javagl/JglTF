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
package de.javagl.jgltf.model.v2.gl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.TechniqueStatesFunctions;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.gl.ProgramModel;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.gl.ShaderModel.ShaderType;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.gl.TechniqueParametersModel;
import de.javagl.jgltf.model.gl.TechniqueStatesFunctionsModel;
import de.javagl.jgltf.model.gl.TechniqueStatesModel;
import de.javagl.jgltf.model.gl.impl.DefaultProgramModel;
import de.javagl.jgltf.model.gl.impl.DefaultShaderModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueParametersModel;
import de.javagl.jgltf.model.gl.impl.DefaultTechniqueStatesModel;
import de.javagl.jgltf.model.gl.impl.v1.DefaultTechniqueStatesFunctionsModelV1;
import de.javagl.jgltf.model.impl.DefaultMaterialModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.IO;

/**
 * A class containing the PBR {@link TechniqueModel} and 
 * {@link MaterialModel} instances that correspond to 
 * materials of glTF 2.0.
 */
public class DefaultModels
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(DefaultModels.class.getName());
    
    /**
     * The PBR vertex {@link ShaderModel}
     */
    private static final DefaultShaderModel PBR_VERTEX_SHADER_MODEL;
    
    /**
     * The PBR fragment {@link ShaderModel}
     */
    private static final DefaultShaderModel PBR_FRAGMENT_SHADER_MODEL;
    
    /**
     * The PBR {@link ProgramModel}
     */
    private static final DefaultProgramModel PBR_PROGRAM_MODEL;
    
    /**
     * The PBR {@link TechniqueModel}
     */
    private static final DefaultTechniqueModel PBR_TECHNIQUE_MODEL;
    
    /**
     * The PBR {@link MaterialModel}
     */
    private static final DefaultMaterialModel PBR_MATERIAL_MODEL;
    
    static
    {
        Class<?> c = DefaultModels.class;
        
        // Create a model for the PBR vertex shader
        String vertexShaderUriString = "pbr.vert";
        PBR_VERTEX_SHADER_MODEL = new DefaultShaderModel(
            vertexShaderUriString, ShaderType.VERTEX_SHADER);
        try (InputStream inputStream = 
            c.getResourceAsStream("/" + vertexShaderUriString))
        {
            byte[] data = IO.readStream(inputStream);
            ByteBuffer shaderData = Buffers.create(data);
            PBR_VERTEX_SHADER_MODEL.setShaderData(shaderData);
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, 
                "Could not read PBR vertex shader source code", e);
        }

        // Create a model for the PBR fragment shader
        String fragmentShaderUriString = "pbr.frag";
        PBR_FRAGMENT_SHADER_MODEL = new DefaultShaderModel(
            fragmentShaderUriString, ShaderType.FRAGMENT_SHADER);
        try (InputStream inputStream = 
            c.getResourceAsStream("/" + fragmentShaderUriString))
        {
            byte[] data = IO.readStream(inputStream);
            ByteBuffer shaderData = Buffers.create(data);
            PBR_FRAGMENT_SHADER_MODEL.setShaderData(shaderData);
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, 
                "Could not read PBR fragment shader source code", e);
        }
        
        // Create a model for the PBR program
        PBR_PROGRAM_MODEL = new DefaultProgramModel();
        PBR_PROGRAM_MODEL.setVertexShaderModel(
            PBR_VERTEX_SHADER_MODEL);
        PBR_PROGRAM_MODEL.setFragmentShaderModel(
            PBR_FRAGMENT_SHADER_MODEL);
        
        // Create a model for the PBR technique
        PBR_TECHNIQUE_MODEL = new DefaultTechniqueModel();
        PBR_TECHNIQUE_MODEL.setProgramModel(PBR_PROGRAM_MODEL);
        
        addParametersForPbrTechnique(PBR_TECHNIQUE_MODEL);
        
        List<Integer> enable = Arrays.asList(
            GltfConstants.GL_DEPTH_TEST, 
            GltfConstants.GL_CULL_FACE
        );
        TechniqueStatesFunctions functions = 
            de.javagl.jgltf.model.v1.gl.Techniques
                .createDefaultTechniqueStatesFunctions();
        TechniqueStatesFunctionsModel techniqueStatesFunctionsModel =
            new DefaultTechniqueStatesFunctionsModelV1(functions);
        TechniqueStatesModel techniqueStatesModel = 
            new DefaultTechniqueStatesModel(
                enable, techniqueStatesFunctionsModel);
        PBR_TECHNIQUE_MODEL.setTechniqueStatesModel(techniqueStatesModel);

        // Create a model for the PBR material
        PBR_MATERIAL_MODEL = new DefaultMaterialModel();
        PBR_MATERIAL_MODEL.setTechniqueModel(PBR_TECHNIQUE_MODEL);
        
    }
    
    /**
     * Return the PBR {@link MaterialModel}
     * 
     * @return The PBR {@link MaterialModel}
     */
    public static DefaultMaterialModel getPbrMaterialModel()
    {
        return PBR_MATERIAL_MODEL;
    }
    

    /**
     * Return the PBR {@link TechniqueModel}
     * 
     * @return The PBR {@link TechniqueModel}
     */
    public static TechniqueModel getPbrTechniqueModel()
    {
        return PBR_TECHNIQUE_MODEL;
    }
    
    /**
     * Add all {@link TechniqueParametersModel} instances for PBR technique
     * to the given {@link TechniqueModel}
     * 
     * @param techniqueModel The {@link TechniqueModel}
     */
    private static void addParametersForPbrTechnique(
        DefaultTechniqueModel techniqueModel)
    {
        addAttributeParameters(techniqueModel, "a_position", 
            "position", GltfConstants.GL_FLOAT_VEC4, 1, "POSITION"); 
        addAttributeParameters(techniqueModel, "a_normal", 
            "normal", GltfConstants.GL_FLOAT_VEC4, 1, "NORMAL"); 
        addAttributeParameters(techniqueModel, "a_tangent", 
            "tangent", GltfConstants.GL_FLOAT_VEC4, 1, "TANGENT"); 

        // TODO These TEXCOORD_0 cannot be right...
        addAttributeParameters(techniqueModel, "a_baseColorTexCoord", 
            "baseColorTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, "TEXCOORD_0"); 
        addAttributeParameters(techniqueModel, "a_metallicRoughnessTexCoord", 
            "metallicRoughnessTexCoord", 
            GltfConstants.GL_FLOAT_VEC2, 1, "TEXCOORD_0"); 
        addAttributeParameters(techniqueModel, "a_normalTexCoord", 
            "normalTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, "TEXCOORD_0"); 
        addAttributeParameters(techniqueModel, "a_occlusionTexCoord", 
            "occlusionTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, "TEXCOORD_0"); 
        addAttributeParameters(techniqueModel, "a_emissiveTexCoord", 
            "emissiveTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, "TEXCOORD_0"); 
        
        addUniformParameters(techniqueModel, "u_modelViewMatrix", 
            "modelViewMatrix", GltfConstants.GL_FLOAT_MAT4, 1, "MODELVIEW");
        addUniformParameters(techniqueModel, "u_projectionMatrix", 
            "projectionMatrix", GltfConstants.GL_FLOAT_MAT4, 1, "PROJECTION");
        addUniformParameters(techniqueModel, "u_normalMatrix", 
            "normalMatrix", GltfConstants.GL_FLOAT_MAT3, 1, 
            "MODELVIEWINVERSETRANSPOSE");

        addUniformParameters(techniqueModel, "u_isDoubleSided", 
            "isDoubleSided", GltfConstants.GL_INT, 1, null);
        
        addUniformParameters(techniqueModel, "u_baseColorTexture", 
            "baseColorTexture", GltfConstants.GL_SAMPLER_2D, 1, null);
        addUniformParameters(techniqueModel, "u_metallicRoughnessTexture", 
            "metallicRoughnessTexture", GltfConstants.GL_SAMPLER_2D, 1, null);
        addUniformParameters(techniqueModel, "u_normalTexture", 
            "normalTexture", GltfConstants.GL_SAMPLER_2D, 1, null);
        addUniformParameters(techniqueModel, "u_occlusionTexture", 
            "occlusionTexture", GltfConstants.GL_SAMPLER_2D, 1, null);
        addUniformParameters(techniqueModel, "u_emissiveTexture", 
            "emissiveTexture", GltfConstants.GL_SAMPLER_2D, 1, null);
        
        addUniformParameters(techniqueModel, "u_hasBaseColorTexture", 
            "hasBaseColorTexture", GltfConstants.GL_INT, 1, null);
        addUniformParameters(techniqueModel, "u_hasMetallicRoughnessTexture", 
            "hasMetallicRoughnessTexture", GltfConstants.GL_INT, 1, null);
        addUniformParameters(techniqueModel, "u_hasNormalTexture", 
            "hasNormalTexture", GltfConstants.GL_INT, 1, null);
        addUniformParameters(techniqueModel, "u_hasOcclusionTexture", 
            "hasOcclusionTexture", GltfConstants.GL_INT, 1, null);
        addUniformParameters(techniqueModel, "u_hasEmissiveTexture", 
            "hasEmissiveTexture", GltfConstants.GL_INT, 1, null);
        
        addUniformParameters(techniqueModel, "u_baseColorFactor", 
            "baseColorFactor", GltfConstants.GL_FLOAT_VEC4, 1, null);
        addUniformParameters(techniqueModel, "u_metallicFactor", 
            "metallicFactor", GltfConstants.GL_FLOAT, 1, null);
        addUniformParameters(techniqueModel, "u_roughnessFactor", 
            "roughnessFactor", GltfConstants.GL_FLOAT, 1, null);
        addUniformParameters(techniqueModel, "u_normalScale", 
            "normalScale", GltfConstants.GL_FLOAT, 1, null);
        addUniformParameters(techniqueModel, "u_occlusionStrength",
            "occlusionStrength", GltfConstants.GL_FLOAT, 1, null);
        addUniformParameters(techniqueModel, "u_emissiveFactor", 
            "emissiveFactor", GltfConstants.GL_FLOAT_VEC3, 1, null);
        
    }
    
    /**
     * Add the specified attribute to the given model
     * 
     * @param techniqueModel The {@link TechniqueModel}
     * @param attributeName The attribute name
     * @param parameterName The parameter name
     * @param type The parameter type
     * @param count The count
     * @param semantic The semantic
     */
    private static void addAttributeParameters(
        DefaultTechniqueModel techniqueModel, String attributeName, 
        String parameterName, int type, int count, String semantic)
    {
        techniqueModel.addAttribute(attributeName, parameterName);
        addParameters(techniqueModel, parameterName, type, count, semantic);
    }

    /**
     * Add the specified uniform to the given model
     * 
     * @param techniqueModel The {@link TechniqueModel}
     * @param uniformName The uniform name
     * @param parameterName The parameter name
     * @param type The parameter type
     * @param count The count
     * @param semantic The semantic
     */
    private static void addUniformParameters(
        DefaultTechniqueModel techniqueModel, String uniformName, 
        String parameterName, int type, int count, String semantic)
    {
        techniqueModel.addUniform(uniformName, parameterName);
        addParameters(techniqueModel, parameterName, type, count, semantic);
    }
    
    /**
     * Add a {@link TechniqueParametersModel} with the given parameters to
     * the given {@link TechniqueModel}
     * 
     * @param techniqueModel The {@link TechniqueModel}
     * @param parameterName The parameter name
     * @param type The parameter type
     * @param count The count
     * @param semantic The semantic
     */
    private static void addParameters(DefaultTechniqueModel techniqueModel, 
        String parameterName, int type, int count, String semantic)
    {
        Object value = null;
        NodeModel nodeModel = null;
        TechniqueParametersModel techniqueParametersModel =
            new DefaultTechniqueParametersModel(
                type, count, semantic, value, nodeModel);
        techniqueModel.addParameter(
            parameterName, techniqueParametersModel);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private DefaultModels()
    {
        // Private constructor to prevent instantiation
    }
    
    
    
}
