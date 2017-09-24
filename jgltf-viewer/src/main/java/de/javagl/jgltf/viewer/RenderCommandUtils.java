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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Program;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.model.io.IO;

/**
 * Utility methods, solely intended for debugging and fine-grained logging
 * of rendering commands.
 */
class RenderCommandUtils
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(RenderCommandUtils.class.getName());
    
    /**
     * Create a wrapper around the given delegate that prints 
     * the value that is set for the specified uniform
     * 
     * @param delegate The delegate command
     * @param uniformName The uniform name
     * @param uniformValueSupplier The supplier for the uniform value
     * @return The wrapping command
     */
    static Runnable debugUniformSettingCommand(
        Runnable delegate, String uniformName, Supplier<?> uniformValueSupplier)
    {
        Runnable command = new Runnable()
        {
            @Override
            public void run()
            {
                Level level = Level.FINE;
                if (logger.isLoggable(level))
                {
                    String valueString = 
                        debugString(uniformValueSupplier.get());
                    logger.log(level,
                        "For uniform " + uniformName + 
                        " setting " + valueString);
                }
                delegate.run();
            }
            
            @Override
            public String toString()
            {
                String valueString = 
                    debugString(uniformValueSupplier.get());
                return "For uniform " + uniformName + 
                    " setting " + valueString;
            }
        };
        return command;
    }
    
    /**
     * Create a debugging string for the given uniform value
     * 
     * @param value The value
     * @return The debug string
     */
    private static String debugString(Object value)
    {
        if (value instanceof int[])
        {
            return Arrays.toString((int[])value);
        }
        if (value instanceof float[])
        {
            float array[] = (float[])value;
            if (array.length % 16 == 0)
            {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < array.length / 16; i++)
                {
                    sb.append("\nMatrix " + i + "\n");
                    sb.append(createMatrixString(array, i * 16, 4, 4));
                }
                return sb.toString();
            }
            if (array.length % 9 == 0)
            {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < array.length / 9; i++)
                {
                    sb.append("\nMatrix " + i + "\n");
                    sb.append(createMatrixString(array, i * 9, 3, 3));
                }
                return sb.toString();
            }
            return Arrays.toString(array);
        }
        if (value instanceof Object[])
        {
            Object array[] = (Object[])value;
            return Arrays.toString(array);
        }
        return String.valueOf(value);
    }
    
    /**
     * Create an unspecified string for the given matrix, suitable for debug
     * output.
     * 
     * @param matrix The matrix
     * @param rows The number of rows
     * @param columns The number of columns
     * @param offset The offset inside the matrix array
     * @return The matrix string
     */
    private static String createMatrixString(
        float matrix[], int offset, int rows, int columns)
    {
        String format = "%8.3f";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int r = 0; r < rows; r++)
        {
            if (r > 0)
            {
                sb.append("\n");
                sb.append(" ");
            }
            for (int c = 0; c < columns; c++)
            {
                if (c > 0)
                {
                    sb.append("  ");
                }
                int index = r + c * rows;
                float value = matrix[offset + index];
                sb.append(String.format(Locale.ENGLISH, format, value));
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    
    /**
     * Create an elaborate info string for this render command,
     * solely intended for debugging
     * 
     * @param gltf The {@link GlTF}
     * @param meshPrimitiveName The name
     * @param techniqueId The technique ID
     * @param uniformSettingCommands The uniform setting commands 
     * @return The info string
     */
    static String createInfoString(
        GlTF gltf, String meshPrimitiveName, String techniqueId, 
        List<?> uniformSettingCommands)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RenderCommand for " + meshPrimitiveName + "\n");

        sb.append("    technique: " + techniqueId + "\n");
        
        Technique technique = gltf.getTechniques().get(techniqueId);
        String programId = technique.getProgram();
        Program program = gltf.getPrograms().get(programId);
        String vertexShaderId = program.getVertexShader();
        String fragmentShaderId = program.getFragmentShader();
        Shader vertexShader = gltf.getShaders().get(vertexShaderId);
        Shader fragmentShader = gltf.getShaders().get(fragmentShaderId);
        String vertexShaderUriString = vertexShader.getUri();
        String fragmentShaderUriString = fragmentShader.getUri();
        String shaderNames = "";
        if (IO.isDataUriString(vertexShaderUriString))
        {
            shaderNames += "v: (data URI), ";
        }
        else
        {
            shaderNames += "v: " + vertexShaderUriString + ", ";
        }
        if (IO.isDataUriString(fragmentShaderUriString))
        {
            shaderNames += "f: (data URI)";
        }
        else
        {
            shaderNames += "f: " + fragmentShaderUriString;
        }
        sb.append("    program: " + programId + 
            " (" + shaderNames + ")\n");
        
//        List<Integer> enabledStates = 
//            GltfTechniqueModel.obtainEnabledStates(technique);
//        sb.append("    enabledStates: ");
//        for (Integer enabledState : enabledStates)
//        {
//            sb.append(GltfConstants.stringFor(enabledState)+", ");
//        }
//        sb.append("\n");
        sb.append("    uniforms:\n");
        for (Object uniformSettingCommand : uniformSettingCommands)
        {
            sb.append("        " + uniformSettingCommand + "\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Private constructor to prevent instantiation
     */
    private RenderCommandUtils()
    {
        // Private constructor to prevent instantiation
    }
}
