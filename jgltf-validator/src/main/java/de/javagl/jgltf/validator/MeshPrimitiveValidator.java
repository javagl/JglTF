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

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.MeshPrimitive;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueParameters;

/**
 * A class for validating {@link MeshPrimitive}es
 */
class MeshPrimitiveValidator extends AbstractGltfValidator
{
    /**
     * The {@link MaterialValidator}
     */
    private final MaterialValidator materialValidator;
    
    /**
     * The {@link AccessorValidator}
     */
    private final AccessorValidator accessorValidator;
    
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} that contains the elements to validate
     */
    MeshPrimitiveValidator(GlTF gltf)
    {
        super(gltf);
        this.materialValidator = new MaterialValidator(gltf);
        this.accessorValidator = new AccessorValidator(gltf);
    }

    /**
     * Validate the given {@link MeshPrimitive}, and return the
     * {@link ValidatorResult}
     * 
     * @param meshPrimitive The {@link MeshPrimitive}
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    ValidatorResult validateMeshPrimitive(
        MeshPrimitive meshPrimitive, ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        
        // Validate the meshPrimitive.material
        String materialId = meshPrimitive.getMaterial();
        
        // The material ID is optional as of glTF 1.1
        if (materialId != null)
        {
            validatorResult.add(materialValidator.validateMaterial(
                materialId, context));
            if (validatorResult.hasErrors())
            {
                return validatorResult;
            }
            
            Material material = getGltf().getMaterials().get(materialId);
            
            String techniqueId = material.getTechnique();
            Technique technique = null;
            if (techniqueId == null)
            {
                // TODO Check how this will be handled in glTF 2.0!
                validatorResult.addWarning("No technique ID found in material",
                    context.with("material " + materialId));
                return validatorResult;
            }
            technique = getGltf().getTechniques().get(techniqueId);
            
            // Check that the attributes of the technique are mapped to valid
            // accessors via the meshPrimitive.attributes map
            Map<String, String> attributes = Optional
                .ofNullable(technique.getAttributes())
                .orElse(Collections.emptyMap());
            for (String attributeName : attributes.keySet())
            {
                String techniqueParameterId = attributes.get(attributeName);
                Map<String, TechniqueParameters> parameters = 
                    technique.getParameters();
                TechniqueParameters techniqueParameters =
                    parameters.get(techniqueParameterId);
                String semantic = techniqueParameters.getSemantic();

                Map<String, String> meshPrimitiveAttributes = 
                    meshPrimitive.getAttributes();
                if (meshPrimitiveAttributes == null)
                {
                    validatorResult.addError(
                        "The attributes are null", context);
                    return validatorResult;
                }
                String accessorId = meshPrimitiveAttributes.get(semantic);
                
                validatorResult.add(accessorValidator.validateAccessor(accessorId, 
                    context.with("accessor for attribute " + attributeName)));
                if (validatorResult.hasErrors())
                {
                    return validatorResult;
                }
            }
            
        }
        
        
        // If the meshPrimitive contains indices, validate the 
        // meshPrimitive.indices accessor
        String indicesAccessorId = meshPrimitive.getIndices();
        if (indicesAccessorId != null)
        {
            validatorResult.add(accessorValidator.validateAccessor(
                indicesAccessorId, context.with("accessor for indices")));
            if (validatorResult.hasErrors())
            {
                return validatorResult;
            }
        }
        
        return validatorResult;
    }
    
}
