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

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.GlTF;

/**
 * A class for validating {@link Accessor}s
 */
class AccessorValidator extends AbstractGltfValidator
{
    /**
     * The {@link BufferViewValidator}
     */
    private final BufferViewValidator bufferViewValidator;
    
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} that contains the elements to validate
     */
    AccessorValidator(GlTF gltf)
    {
        super(gltf);
        this.bufferViewValidator = new BufferViewValidator(gltf);
    }

    /**
     * Validate the given {@link Accessor} ID, and return the
     * {@link ValidatorResult}
     * 
     * @param accessorId The {@link Accessor} ID
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    ValidatorResult validateAccessor(
        String accessorId, ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext)
            .with("accessors[" + accessorId + "]");
        ValidatorResult validatorResult = new ValidatorResult();

        // Validate the ID
        validatorResult.add(validateMapEntry(
            getGltf().getAccessors(), accessorId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        Accessor accessor = getGltf().getAccessors().get(accessorId);
        
        // Validate the accessor.bufferView
        String bufferViewId = accessor.getBufferView();
        validatorResult.add(bufferViewValidator.validateBufferView(
            bufferViewId, context.with("bufferView")));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        return validatorResult;
    }
    
    
}
