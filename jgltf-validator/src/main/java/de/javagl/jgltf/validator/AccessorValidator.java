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

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.GltfConstants;

/**
 * A class for validating {@link Accessor} objects
 */
class AccessorValidator extends AbstractGltfValidator
{
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} that contains the elements to validate
     */
    AccessorValidator(GlTF gltf)
    {
        super(gltf);
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

        // Validate the accessorId
        validatorResult.add(validateMapEntry(
            getGltf().getAccessors(), accessorId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }

        Accessor accessor = getGltf().getAccessors().get(accessorId);

        // Validate the accessor.bufferView
        String bufferViewId = accessor.getBufferView();
        validatorResult.add(validateMapEntry(
            getGltf().getBufferViews(), bufferViewId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        // Validate the alignment constraints of the accessor, for the
        // components (e.g. float), and the elements (e.g. 3D float vectors),
        // referring to the byte offset (in its buffer view), and the total 
        // byte offset (in the buffer)
        
        Integer componentType = accessor.getComponentType();
        if (componentType == null)
        {
            validatorResult.addError("The componentType is null", context);
            return validatorResult;
        }
            
        String componentTypeName = 
            GltfConstants.stringFor(accessor.getComponentType());
        int componentSizeInBytes =
            Accessors.getNumBytesForAccessorComponentType(componentType);
        int numComponents = 
            Accessors.getNumComponentsForAccessorType(accessor.getType());
        int elementSizeInBytes = numComponents * componentSizeInBytes;

        BufferView bufferView = getGltf().getBufferViews().get(bufferViewId);
        int accessorByteOffset = accessor.getByteOffset();
        int totalByteOffset = bufferView.getByteOffset() + accessorByteOffset;

        if (accessorByteOffset % componentSizeInBytes != 0)
        {
            validatorResult.addError(
                "The byteOffset is " + accessorByteOffset + ", but must be " + 
                "aligned to the size of the component type (" + 
                componentTypeName + "), which is "+componentSizeInBytes, 
                context);
            return validatorResult;
        }
        if (totalByteOffset % componentSizeInBytes != 0)
        {
            validatorResult.addError(
                "The total byte offset from the beginning of buffer " + 
                bufferView.getBuffer() + " is " + totalByteOffset + 
                ", but must be aligned to the size of the component type (" + 
                componentTypeName + "), which is "+componentSizeInBytes, 
                context);
            return validatorResult;
        }

        if (!isVersionGreaterThanOrEqual("1.1"))
        {
            // In glTF 1.0, the accessors had to be aligned to the size of 
            // the FULL type (called "elements" here). But nobody obeyed 
            // this constraint. Issue only a warning in this case.
            
            if (accessorByteOffset % elementSizeInBytes != 0)
            {
                validatorResult.addWarning(
                    "The byteOffset is " + accessorByteOffset + 
                    ", but must be aligned to the size of the elements (" + 
                    accessor.getType() + " with " + componentTypeName + "), " + 
                    "which is "+elementSizeInBytes, 
                    context);
                return validatorResult;
            }
            if (totalByteOffset % elementSizeInBytes != 0)
            {
                validatorResult.addWarning(
                    "The total byte offset from the beginning of buffer " + 
                    bufferView.getBuffer() + " is " + totalByteOffset + 
                    ", but must be aligned to the size of the elements (" + 
                    accessor.getType() + " with " + componentTypeName + "), " + 
                    "which is "+elementSizeInBytes, 
                    context);
                return validatorResult;
            }
            
        }
        
        return validatorResult;
    }
    
    
}
