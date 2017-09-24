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
package de.javagl.jgltf.validator;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Asset;
import de.javagl.jgltf.impl.v1.GlTF;

/**
 * A basic validator for glTF data. Note that this class is only intended 
 * for internal use. It does NOT do a full-fledged, systematic validation, 
 * but only offers basic sanity checks. Do NOT rely on this.
 */
public class Validator extends AbstractGltfValidator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(Validator.class.getName());
    
    /**
     * The log level for errors
     */
    private final Level errorLogLevel = Level.SEVERE;
    
    /**
     * The {@link SceneValidator}
     */
    private final SceneValidator sceneValidator;

    /**
     * The {@link NodeValidator}
     */
    private final NodeValidator nodeValidator;

    /**
     * The {@link MeshValidator}
     */
    private final MeshValidator meshValidator;
    
    /**
     * The {@link AccessorValidator}
     */
    private final AccessorValidator accessorValidator;
    
    /**
     * The {@link BufferViewValidator}
     */
    private final BufferViewValidator bufferViewValidator;
    
    /**
     * The {@link MaterialValidator}
     */
    private final MaterialValidator materialValidator;
    
    /**
     * The {@link TechniqueValidator}
     */
    private final TechniqueValidator techniqueValidator;
    
    /**
     * The {@link ProgramValidator}
     */
    private final ProgramValidator programValidator;

    /**
     * The {@link ShaderValidator}
     */
    private final ShaderValidator shaderValidator;
    
    /**
     * The {@link TextureValidator}
     */
    private final TextureValidator textureValidator;
    
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} whose elements should be validated
     */
    public Validator(GlTF gltf)
    {
        super(gltf);
        this.sceneValidator = new SceneValidator(gltf);
        this.meshValidator = new MeshValidator(gltf);
        this.nodeValidator = new NodeValidator(gltf);
        this.accessorValidator = new AccessorValidator(gltf);
        this.bufferViewValidator = new BufferViewValidator(gltf);
        this.materialValidator = new MaterialValidator(gltf);
        this.techniqueValidator = new TechniqueValidator(gltf);
        this.programValidator = new ProgramValidator(gltf);
        this.shaderValidator = new ShaderValidator(gltf);
        this.textureValidator = new TextureValidator(gltf);
    }
    
    /**
     * Returns whether the {@link GlTF} that was given in the constructor
     * is valid. If it is not valid, then the errors will be printed,
     * and <code>false</code> will be returned.
     * 
     * @return Whether the {@link GlTF} is valid
     */
    public boolean isValid()
    {
        ValidatorResult validatorResult = validate();
        if (validatorResult.hasErrors())
        {
            validatorResult.logErrors(logger, errorLogLevel);
            return false;
        }
        return true;
    }

    /**
     * Validate the {@link GlTF}, and return the {@link ValidatorResult}
     * 
     * @return The {@link ValidatorResult}
     */
    public ValidatorResult validate()
    {
        validateAsset();
        
        ValidatorContext context = new ValidatorContext("glTF");
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateScenes(context));
        validatorResult.add(validateNodes(context));
        validatorResult.add(validateMeshes(context));
        validatorResult.add(validateAccessors(context));
        validatorResult.add(validateBufferViews(context));
        validatorResult.add(validateMaterials(context));
        validatorResult.add(validateTechniques(context));
        validatorResult.add(validatePrograms(context));
        validatorResult.add(validateShaders(context));
        validatorResult.add(validateTextures(context));
        
        return validatorResult;
    }
    
    /**
     * Validate the {@link Asset}. Until now, this only prints log messages
     * with version information.
     */
    private void validateAsset()
    {
        Asset asset = getGltf().getAsset();
        if (asset == null)
        {
            logger.info("No <asset> information found. " + 
                "Assuming glTF version 1.0.0");
        }
        else
        {
            String version = asset.getVersion();
            if (version == null)
            {
                logger.info("No 'version' property found in <asset>. " + 
                    "Assuming glTF version 1.0.0");
            }
            else
            {
                logger.info("Validating glTF with version " + version);
            }
        }
        
    }

    /**
     * Validate the {@link GlTF#getScene()} and {@link GlTF#getScenes()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateScenes(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        String defaultSceneId = getGltf().getScene();
        if (defaultSceneId != null)
        {
            validatorResult.add(validateMapEntry(
                getGltf().getScenes(), defaultSceneId, context));
            if (validatorResult.hasErrors())
            {
                return validatorResult;
            }
        }
        
        validatorResult.add(validateElements(
            getGltf().getScenes(), "scenes", true, context, 
            sceneValidator::validateScene));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        return validatorResult;
    }
    
    
    /**
     * Validate the {@link GlTF#getNodes()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateNodes(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();

        validatorResult.add(validateElements(
            getGltf().getNodes(), "nodes", true, context, 
            nodeValidator::validateNode));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }
    

    /**
     * Validate the {@link GlTF#getMeshes()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateMeshes(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getMeshes(), "meshes", true, context, 
            meshValidator::validateMesh));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }

    /**
     * Validate the {@link GlTF#getAccessors()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateAccessors(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getAccessors(), "accessors", true, context, 
            accessorValidator::validateAccessor));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }

    /**
     * Validate the {@link GlTF#getBufferViews()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateBufferViews(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getBufferViews(), "bufferViews", true, context, 
            bufferViewValidator::validateBufferView));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }

    /**
     * Validate the {@link GlTF#getMaterials()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateMaterials(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getMaterials(), "materials", false, context, 
            materialValidator::validateMaterial));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }

    
    /**
     * Validate the {@link GlTF#getTechniques()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateTechniques(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getTechniques(), "techniques", false, context, 
            techniqueValidator::validateTechnique));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }

    /**
     * Validate the {@link GlTF#getPrograms()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validatePrograms(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getPrograms(), "programs", false, context, 
            programValidator::validateProgram));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }

    /**
     * Validate the {@link GlTF#getShaders()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateShaders(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getShaders(), "shaders", false, context, 
            shaderValidator::validateShader));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }

    /**
     * Validate the {@link GlTF#getTextures()}
     * 
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateTextures(ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        validatorResult.add(validateElements(
            getGltf().getTextures(), "textures", false, context, 
            textureValidator::validateTexture));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        return validatorResult;
    }
    
    
    /**
     * Validate all elements of the given map. If the given map is 
     * <code>null</code> or empty, and <code>expected==true</code>, then
     * a warning will be added to the {@link ValidatorResult}. Otherwise,
     * the give validator function will be applied to each key of the
     * map, and the given context.
     * 
     * @param map The map
     * @param mapName The name of the map, for messages
     * @param expected Whether the map is expected to be non-<code>null</code>
     * and not empty
     * @param currentContext The optional {@link ValidatorContext} of the glTF
     * @param validatorFunction The validator function
     * @return The {@link ValidatorResult}
     */
    private <T> ValidatorResult validateElements(
        Map<String, ? extends T> map, String mapName, boolean expected, 
        ValidatorContext currentContext, 
        BiFunction<String, ValidatorContext, ValidatorResult> validatorFunction)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        if (map == null || map.isEmpty())
        {
            if (expected)
            {
                validatorResult.addWarning(
                    "No '"+mapName+"' found in glTF", context);
            }
        }
        else
        {
            for (String id : map.keySet())
            {
                validatorResult.add(validatorFunction.apply(id, context));
                if (validatorResult.hasErrors())
                {
                    return validatorResult;
                }
            }
        }
        return validatorResult;
    }
    
    
    
    


    
    

    

    
    

    
}
