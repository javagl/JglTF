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

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Program;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.GltfConstants;

/**
 * A class for validating {@link Program} objects
 */
class ProgramValidator extends AbstractGltfValidator
{
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} that contains the elements to validate
     */
    protected ProgramValidator(GlTF gltf)
    {
        super(gltf);
    }

    /**
     * Validate the given {@link Program} ID, and return the
     * {@link ValidatorResult}
     * 
     * @param programId The {@link Program} ID
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    ValidatorResult validateProgram(
        String programId, ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext)
            .with("programs[" + programId + "]");
        ValidatorResult validatorResult = new ValidatorResult();
        
        // Validate the program ID
        validatorResult.add(validateMapEntry(
            getGltf().getPrograms(), programId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        Program program = getGltf().getPrograms().get(programId);

        // Validate the program.vertexShader
        String vertexShaderId = program.getVertexShader();
        validatorResult.add(validateMapEntry(
            getGltf().getShaders(), vertexShaderId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        Shader vertexShader = getGltf().getShaders().get(vertexShaderId);
        Integer vertexShaderType = vertexShader.getType();
        if (vertexShaderType != GltfConstants.GL_VERTEX_SHADER)
        {
            validatorResult.addError(
                "type should be " + GltfConstants.GL_VERTEX_SHADER + 
                ", but is " + vertexShaderType, 
                context.with("vertexShader"));
            return validatorResult;
        }
        
        // Validate the program.fragmentShader
        String fragmentShaderId = program.getFragmentShader();
        validatorResult.add(validateMapEntry(
            getGltf().getShaders(), fragmentShaderId, context));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        Shader fragmentShader = getGltf().getShaders().get(fragmentShaderId);
        Integer fragmentShaderType = fragmentShader.getType();
        if (fragmentShaderType != GltfConstants.GL_FRAGMENT_SHADER)
        {
            validatorResult.addError(
                "type should be " + GltfConstants.GL_FRAGMENT_SHADER + 
                ", but is " + fragmentShaderType, 
                context.with("fragmentShader"));
            return validatorResult;
        }
        
        return validatorResult;
    }
    
}
