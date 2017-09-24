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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueParameters;
import de.javagl.jgltf.model.GltfConstants;

/**
 * A class for validating {@link Technique} objects
 */
class TechniqueValidator extends AbstractGltfValidator
{
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} that contains the elements to validate
     */
    TechniqueValidator(GlTF gltf)
    {
        super(gltf);
    }
    
    /**
     * Validate the given {@link Technique} ID, and return the
     * {@link ValidatorResult}
     * 
     * @param techniqueId The {@link Technique} ID
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    ValidatorResult validateTechnique(
        String techniqueId, ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext)
            .with("techniques[" + techniqueId + "]");
        ValidatorResult validatorResult = new ValidatorResult();
        
        // Validate the ID
        validatorResult.add(validateMapEntry(
            getGltf().getTechniques(), techniqueId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        Technique technique = getGltf().getTechniques().get(techniqueId);
        
        // Validate the technique.uniform entries
        validatorResult.add(validateTechniqueUniforms(
            techniqueId, technique, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        // Validate the technique.attribute entries
        validatorResult.add(validateTechniqueAttributes(
            techniqueId, technique, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        // Validate the technique.program
        String programId = technique.getProgram();
        validatorResult.add(validateMapEntry(
            getGltf().getPrograms(), programId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        return validatorResult;
    }

    /**
     * Validate the {@link Technique#getUniforms() uniforms} of the
     * given {@link Technique}
     * 
     * @param techniqueId The {@link Technique} ID
     * @param technique The {@link Technique}
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateTechniqueUniforms(
        String techniqueId, Technique technique, 
        ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        Map<String, String> uniforms = Optional
            .ofNullable(technique.getUniforms())
            .orElse(Collections.emptyMap());
        for (String uniformName : uniforms.keySet())
        {
            String techniqueParametersId = uniforms.get(uniformName);
            if (techniqueParametersId == null)
            {
                validatorResult.addError(
                    "The techniqueParameters ID is null for uniform " + 
                    uniformName, context);
                return validatorResult;
            }
            
            // Validate the technique parameters for the uniform
            validatorResult.add(validateTechniqueParameters(
                technique, techniqueParametersId, 
                context.with("uniform " + uniformName)));
            if (validatorResult.hasErrors())
            {
                return validatorResult;
            }
            
            
        }
        return validatorResult;
    }
    
    
    /**
     * Validate the {@link Technique#getAttributes() attributes} of the
     * given {@link Technique}
     * 
     * @param techniqueId The {@link Technique} ID
     * @param technique The {@link Technique}
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateTechniqueAttributes(
        String techniqueId, Technique technique, 
        ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        Map<String, String> attributes = Optional
            .ofNullable(technique.getAttributes())
            .orElse(Collections.emptyMap());
        for (String attributeName : attributes.keySet())
        {
            String techniqueParametersId = attributes.get(attributeName);
            if (techniqueParametersId == null)
            {
                validatorResult.addError(
                    "The techniqueParameters ID is null for attribute " + 
                    attributeName, context);
                return validatorResult;
            }
            
            // Validate the technique parameters for the attribute
            validatorResult.add(validateTechniqueParameters(
                technique, techniqueParametersId, 
                context.with("attribute " + attributeName)));
            if (validatorResult.hasErrors())
            {
                return validatorResult;
            }
        }
        return validatorResult;
    }
    
    /**
     * Validate the {@link TechniqueParameters} with the given ID in the
     * given {@link Technique}
     * 
     * @param technique The {@link Technique}
     * @param techniqueParametersId The {@link TechniqueParameters} ID
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    private ValidatorResult validateTechniqueParameters(
        Technique technique, String techniqueParametersId, 
        ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext)
            .with("technique.parameters[" + techniqueParametersId + "]");
        ValidatorResult validatorResult = new ValidatorResult();
        
        // Validate the ID
        validatorResult.add(validateMapEntry(
            technique.getParameters(), techniqueParametersId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        TechniqueParameters techniqueParameters = 
            technique.getParameters().get(techniqueParametersId);
        
        // Validate the techniqueParameters.type
        Integer type = techniqueParameters.getType();
        if (type == null)
        {
            validatorResult.addError("The type is null", context);
            return validatorResult;
        }

        // NO! The semantics may be arbitrary strings!
//        // Validate the techniqueParameters.semantic
//        String semantic = techniqueParameters.getSemantic();
//        if (semantic != null)
//        {
//            if (!Semantic.contains(semantic))
//            {
//                validatorResult.addError(
//                    "The techniqueParameters " + techniqueParametersId + 
//                    " have an invalid semantic: " + semantic, context); 
//                return validatorResult;
//            }
//        }
        
        // If the techniqueParameters.type is GL_SAMPLER_2D, then
        // validate the referenced texture
        if (type == GltfConstants.GL_SAMPLER_2D)
        {
            Object value = techniqueParameters.getValue();
            if (value != null)
            {
                String textureId = null;
                if (value instanceof String)
                {
                    textureId = (String)value;
                }
                else if (value instanceof Collection<?>)
                {
                    Collection<?> collection = (Collection<?>)value;
                    if (collection.size() == 0)
                    {
                        validatorResult.addError(
                            "The value of techniqueParameters " 
                            + techniqueParametersId + " is empty", context);
                        return validatorResult;
                    }
                    textureId = String.valueOf(collection.iterator().next());
                }
                else
                {
                    validatorResult.addWarning(
                        "The value of techniqueParameters "
                        + techniqueParametersId + " is "
                        + value.getClass().getSimpleName()
                        + ", but should be String or an array of strings", 
                        context);
                }
                
                validatorResult.add(validateMapEntry(
                    getGltf().getTextures(), textureId, context));
                if (validatorResult.hasErrors())
                {
                    return validatorResult;
                }
            }
        }
        
        return validatorResult;
    }
    
}
