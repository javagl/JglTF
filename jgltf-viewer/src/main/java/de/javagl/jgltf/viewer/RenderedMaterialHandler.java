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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.gl.ProgramModel;
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
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * A class for creating the {@link RenderedMaterial} instances for glTF 2.0
 * {@link MaterialModelV2} objects.<br>
 * <br>
 * It will lazily create the internal {@link TechniqueModel}, 
 * {@link ProgramModel} and {@link ShaderModel} instances that 
 * are required for rendering.
 */
class RenderedMaterialHandler
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(RenderedMaterialHandler.class.getName());
    
    /**
     * The mapping from joint counts to vertex {@link ShaderModel} instances
     */
    private final Map<Integer, ShaderModel> vertexShaderModels;
    
    /**
     * The fragment {@link ShaderModel}
     */
    private ShaderModel fragmentShaderModel;
    
    /**
     * The mapping from joint count to {@link ProgramModel} instances
     */
    private final Map<Integer, ProgramModel> programModels;
    
    /**
     * The mapping from {@link MaterialStructure} descriptions to the
     * matching {@link TechniqueModel} instances
     */
    private final Map<MaterialStructure, TechniqueModel> techniqueModels;
    
    /**
     * Default constructor
     * 
     * @param textureIndexLookup The lookup for {@link TextureModel} indices
     */
    RenderedMaterialHandler(
        Function<? super TextureModel, Integer> textureIndexLookup)
    {
        this.vertexShaderModels = 
            new LinkedHashMap<Integer, ShaderModel>();
        this.programModels = 
            new LinkedHashMap<Integer, ProgramModel>();
        this.techniqueModels =
            new LinkedHashMap<MaterialStructure, TechniqueModel>();
    }
    
    /**
     * Obtain the vertex {@link ShaderModel} for the given number of joints,
     * creating it if necessary
     * 
     * @param numJoints The number of joints
     * @return The {@link ShaderModel}
     */
    private ShaderModel obtainVertexShaderModel(int numJoints)
    {
        ShaderModel shaderModel = vertexShaderModels.get(numJoints);
        if (shaderModel == null)
        {
            shaderModel = createVertexShaderModel(numJoints);
            vertexShaderModels.put(numJoints, shaderModel);
        }
        return shaderModel;
    }
    
    /**
     * Create the vertex {@link ShaderModel} for the given number of joints
     * 
     * @param numJoints The number of joints
     * @return The {@link ShaderModel}
     */
    private ShaderModel createVertexShaderModel(int numJoints)
    {
        String vertexShaderDefines = "";
        if (numJoints > 0)
        {
            vertexShaderDefines += "#define NUM_JOINTS " + numJoints + "\n";
        }
        ShaderModel vertexShaderModel = createDefaultShaderModel(
            "pbr.vert", "pbr" + numJoints + ".vert", 
            ShaderType.VERTEX_SHADER, vertexShaderDefines);
        return vertexShaderModel;
    }
    
    /**
     * Obtain the fragment {@link ShaderModel}, creating it if necessary
     * 
     * @return The {@link ShaderModel}
     */
    private ShaderModel obtainFragmentShaderModel()
    {
        if (fragmentShaderModel == null)
        {
            fragmentShaderModel = createDefaultShaderModel(
                "pbr.frag", "pbr.frag", ShaderType.FRAGMENT_SHADER, null);
        }
        return fragmentShaderModel;
    }
    
    
    
    /**
     * Obtain the {@link ProgramModel} for the given number of joints,
     * creating it if necessary
     * 
     * @param numJoints The number of joints
     * @return The {@link ProgramModel}
     */
    private ProgramModel obtainProgramModel(int numJoints)
    {
        ProgramModel programModel = programModels.get(numJoints);
        if (programModel == null)
        {
            programModel = createProgramModel(numJoints);
            programModels.put(numJoints, programModel);
        }
        return programModel;
    }
    
    /**
     * Create the {@link ProgramModel} for the given number of joints
     * 
     * @param numJoints The number of joints
     * @return The {@link ProgramModel}
     */
    private ProgramModel createProgramModel(int numJoints)
    {
        ShaderModel vertexShaderModel = 
            obtainVertexShaderModel(numJoints);
        ShaderModel fragmentShaderModel = 
            obtainFragmentShaderModel();
        
        DefaultProgramModel programModel = new DefaultProgramModel();
        programModel.setVertexShaderModel(vertexShaderModel);
        programModel.setFragmentShaderModel(fragmentShaderModel);
        
        return programModel;
    }

    
    /**
     * Obtain the {@link TechniqueModel} for the given 
     * {@link MaterialStructure}, creating it if necessary
     * 
     * @param materialStructure The {@link MaterialStructure}
     * @return The {@link TechniqueModel}
     */
    private TechniqueModel obtainTechniqueModel(
        MaterialStructure materialStructure)
    {
        TechniqueModel techniqueModel = techniqueModels.get(materialStructure);
        if (techniqueModel == null)
        {
            techniqueModel = createTechniqueModel(materialStructure);
            techniqueModels.put(materialStructure, techniqueModel);
        }
        return techniqueModel;
    }
    

    /**
     * Create the {@link TechniqueModel} for the given 
     * {@link MaterialStructure}
     * 
     * @param materialStructure The {@link MaterialStructure}
     * @return The {@link TechniqueModel}
     */
    private TechniqueModel createTechniqueModel(
        MaterialStructure materialStructure)
    {
        ProgramModel programModel = 
            obtainProgramModel(materialStructure.getNumJoints());
        
        DefaultTechniqueModel techniqueModel = new DefaultTechniqueModel();
        techniqueModel.setProgramModel(programModel);
        
        addParametersForPbrTechnique(techniqueModel, materialStructure);
        
        TechniqueStatesModel techniqueStatesModel =
            TechniqueStatesModels.createDefault();
        techniqueModel.setTechniqueStatesModel(techniqueStatesModel);
        
        return techniqueModel;
    }

    /**
     * Create a {@link RenderedMaterial} instance for the given 
     * {@link MaterialModelV2}
     * 
     * @param material The {@link MaterialModelV2}
     * @param numJoints The number of joints
     * @return The {@link RenderedMaterial}
     */
    RenderedMaterial createRenderedMaterial(
        MaterialModelV2 material, int numJoints)
    {
        MaterialStructure materialStructure = 
            new MaterialStructure(material, numJoints);
        TechniqueModel techniqueModel = 
            obtainTechniqueModel(materialStructure);
        
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        
        if (Boolean.TRUE.equals(material.isDoubleSided()))
        {
            values.put("isDoubleSided", 1);
        }
        else
        {
            values.put("isDoubleSided", 0);
        }
        
        TextureModel baseColorTexture = 
            material.getBaseColorTexture();
        if (baseColorTexture != null)
        {
            values.put("hasBaseColorTexture", 1);
            values.put("baseColorTexCoord", 
                materialStructure.getBaseColorTexCoordSemantic());
            values.put("baseColorTexture", baseColorTexture);
        }
        else
        {
            values.put("hasBaseColorTexture", 0);
        }
        float[] baseColorFactor = material.getBaseColorFactor();
        values.put("baseColorFactor", baseColorFactor);
        
        
        TextureModel metallicRoughnessTexture = 
            material.getMetallicRoughnessTexture();
        if (metallicRoughnessTexture != null)
        {
            values.put("hasMetallicRoughnessTexture", 1);
            values.put("metallicRoughnessTexCoord",
                materialStructure.getMetallicRoughnessTexCoordSemantic());
            values.put("metallicRoughnessTexture", metallicRoughnessTexture);
        }
        else
        {
            values.put("hasMetallicRoughnessTexture", 0);
        }
        float metallicFactor = material.getMetallicFactor();
        values.put("metallicFactor", metallicFactor);
        
        float roughnessFactor = material.getRoughnessFactor();
        values.put("roughnessFactor", roughnessFactor);
        
        
        TextureModel normalTexture = 
            material.getNormalTexture();
        if (normalTexture != null)
        {
            values.put("hasNormalTexture", 1);
            values.put("normalTexCoord", 
                materialStructure.getNormalTexCoordSemantic());
            values.put("normalTexture", normalTexture);
            
            float normalScale = material.getNormalScale();
            values.put("normalScale", normalScale);
        }
        else
        {
            values.put("hasNormalTexture", 0);
            values.put("normalScale", 1.0);
        }

        TextureModel occlusionTexture = 
            material.getOcclusionTexture();
        if (occlusionTexture != null)
        {
            values.put("hasOcclusionTexture", 1);
            values.put("occlusionTexCoord", 
                materialStructure.getOcclusionTexCoordSemantic());
            values.put("occlusionTexture", occlusionTexture);
            
            float occlusionStrength = material.getOcclusionStrength();
            values.put("occlusionStrength", occlusionStrength);
        }
        else
        {
            values.put("hasOcclusionTexture", 0);
            
            // TODO Should this really be 1.0?
            values.put("occlusionStrength", 0.0); 
        }

        TextureModel emissiveTexture = 
            material.getEmissiveTexture();
        if (emissiveTexture != null)
        {
            values.put("hasEmissiveTexture", 1);
            values.put("emissiveTexCoord",
                materialStructure.getEmissiveTexCoordSemantic());
            values.put("emissiveTexture", emissiveTexture);
        }
        else
        {
            values.put("hasEmissiveTexture", 0);
        }
        
        float[] emissiveFactor = material.getEmissiveFactor();
        values.put("emissiveFactor", emissiveFactor);
        
        
        float lightPosition[] = { -800,500,500 };
        values.put("lightPosition", lightPosition);
        
        DefaultRenderedMaterial renderedMaterial = 
            new DefaultRenderedMaterial(techniqueModel, values);
        return renderedMaterial;
    }
    
    
    /**
     * Create a default {@link ShaderModel} instance with the given URI 
     * string and type, by reading the resource that is identified with
     * the given name. If the specified resource cannot be read, then
     * an error message will be printed and the returned shader model
     * will not contain any data. This method is only intended for 
     * internal use!
     * 
     * @param resourceName The name of the resource to read the source code from
     * @param uriString The URI string
     * @param shaderType The shader type
     * @param defines An optional string containing lines of code that 
     * will be prefixed to the shader code, and which will usually 
     * contain preprocessor definitions
     * @return The {@link ShaderModel}
     */
    private static DefaultShaderModel createDefaultShaderModel(
        String resourceName, String uriString, 
        ShaderType shaderType, String defines)
    {
        DefaultShaderModel shaderModel = new DefaultShaderModel(
            uriString, shaderType);
        try (InputStream inputStream = 
            RenderedMaterialHandler.class.getResourceAsStream("/" + resourceName))
        {
            byte[] data = IO.readStream(inputStream);
            String basicShaderString = new String(data);
            String fullShaderString = basicShaderString;
            if (defines != null)
            {
                fullShaderString = defines + "\n" + basicShaderString;
            }
            ByteBuffer shaderData = 
                Buffers.create(fullShaderString.getBytes());
            shaderModel.setShaderData(shaderData);
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, 
                "Could not read shader source code", e);
        }
        return shaderModel;
    }
    
    
    /**
     * Add all {@link TechniqueParametersModel} instances for PBR techniques
     * to the given {@link TechniqueModel}
     * 
     * @param techniqueModel The {@link TechniqueModel}
     * @param materialStructure The {@link MaterialStructure} of the material
     * for which the {@link TechniqueModel} is intended 
     */
    private static void addParametersForPbrTechnique(
        DefaultTechniqueModel techniqueModel, 
        MaterialStructure materialStructure)
    {
        addAttributeParameters(techniqueModel, "a_position", 
            "position", GltfConstants.GL_FLOAT_VEC4, 1, "POSITION"); 
        addAttributeParameters(techniqueModel, "a_normal", 
            "normal", GltfConstants.GL_FLOAT_VEC4, 1, "NORMAL"); 
        addAttributeParameters(techniqueModel, "a_tangent", 
            "tangent", GltfConstants.GL_FLOAT_VEC4, 1, "TANGENT"); 

        addAttributeParameters(techniqueModel, "a_baseColorTexCoord", 
            "baseColorTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, 
            materialStructure.getBaseColorTexCoordSemantic()); 
        addAttributeParameters(techniqueModel, "a_metallicRoughnessTexCoord", 
            "metallicRoughnessTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, 
            materialStructure.getMetallicRoughnessTexCoordSemantic()); 
        addAttributeParameters(techniqueModel, "a_normalTexCoord", 
            "normalTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, 
            materialStructure.getNormalTexCoordSemantic()); 
        addAttributeParameters(techniqueModel, "a_occlusionTexCoord", 
            "occlusionTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, 
            materialStructure.getOcclusionTexCoordSemantic()); 
        addAttributeParameters(techniqueModel, "a_emissiveTexCoord", 
            "emissiveTexCoord", GltfConstants.GL_FLOAT_VEC2, 1, 
            materialStructure.getEmissiveTexCoordSemantic()); 
        
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
        
        addAttributeParameters(techniqueModel, "a_joint", 
            "joint", GltfConstants.GL_FLOAT_VEC4, 1, "JOINTS_0"); 
        addAttributeParameters(techniqueModel, "a_weight", 
            "weight", GltfConstants.GL_FLOAT_VEC4, 1, "WEIGHTS_0");
        
        if (materialStructure.getNumJoints() > 0)
        {
            addUniformParameters(techniqueModel, "u_jointMat", 
                "jointMat", GltfConstants.GL_FLOAT_MAT4, 
                materialStructure.getNumJoints(), "JOINTMATRIX");
        }
        
        // TODO Preliminary uniform for a single point light
        addUniformParameters(techniqueModel, "u_lightPosition", 
            "lightPosition", GltfConstants.GL_FLOAT_VEC3, 1, null);
        
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
    

}
