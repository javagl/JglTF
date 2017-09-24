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

import java.util.List;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Mesh;
import de.javagl.jgltf.impl.v1.MeshPrimitive;

/**
 * A class for validating {@link Mesh}es
 */
class MeshValidator extends AbstractGltfValidator
{
    /**
     * The {@link MeshPrimitiveValidator}
     */
    private final MeshPrimitiveValidator meshPrimitiveValidator;
    
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} that contains the elements to validate
     */
    MeshValidator(GlTF gltf)
    {
        super(gltf);
        this.meshPrimitiveValidator = new MeshPrimitiveValidator(gltf);
    }

    /**
     * Validate the given {@link Mesh} ID, and return the
     * {@link ValidatorResult}
     * 
     * @param meshId The {@link Mesh} ID
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    ValidatorResult validateMesh(
        String meshId, ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext)
            .with("meshes[" + meshId + "]");
        ValidatorResult validatorResult = new ValidatorResult();
        
        // Validate the ID
        validatorResult.add(validateMapEntry(
            getGltf().getMeshes(), meshId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        Mesh mesh = getGltf().getMeshes().get(meshId);
        
        // Validate the mesh.primitives
        List<MeshPrimitive> primitives = mesh.getPrimitives();
        if (primitives != null)
        {
            for (int i = 0; i < primitives.size(); i++)
            {
                MeshPrimitive meshPrimitive = primitives.get(i);
                validatorResult.add(
                    meshPrimitiveValidator.validateMeshPrimitive(meshPrimitive, 
                        context.with("meshPrimitives[" + i + "]")));
                if (validatorResult.hasErrors())
                {
                    return validatorResult;
                }
            }
        }
        
        return validatorResult;
    }
    
}
