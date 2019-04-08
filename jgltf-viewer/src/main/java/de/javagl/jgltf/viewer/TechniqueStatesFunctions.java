/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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

import java.util.ArrayList;
import java.util.List;

import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.gl.TechniqueStatesFunctionsModel;

/**
 * Methods for creating rendering commands from a 
 * {@link TechniqueStatesFunctionsModel}
 */
class TechniqueStatesFunctions
{

    /**
     * The default BlendColor
     */
    private static final float[] DEFAULT_BLENDCOLOR = 
        new float[] { 0.0F, 0.0F, 0.0F, 0.0F };

    /**
     * The default BlendEquationSeparate
     */
    private static final int[] DEFAULT_BLEND_EQUATION_SEPARATE = 
        new int[] { 32774, 32774 };

    /**
     * The default BlendFuncSeparate
     */
    private static final int[] DEFAULT_BLEND_FUNC_SEPARATE = 
        new int[] { 1, 0, 1, 0 };

    /**
     * The default ColorMask
     */
    private static final boolean[] DEFAULT_COLOR_MASK = 
        new boolean[] {true, true, true, true };

    /**
     * The default CullFace
     */
    private static final int[] DEFAULT_CULL_FACE = 
        new int[] { 1029 };

    /**
     * The default DepthFunc
     */
    private static final int[] DEFAULT_DEPTH_FUNC = 
        new int[] { 513 };

    /**
     * The default DepthMask
     */
    private static final boolean[] DEFAULT_DEPTH_MASK = 
        new boolean[] {true };

    /**
     * The default DepthRange
     */
    private static final float[] DEFAULT_DEPTH_RANGE = 
        new float[] { 0.0F, 1.0F };

    /**
     * The default FrontFace
     */
    private static final int[] DEFAULT_FRONT_FACE = 
        new int[] { 2305 };

    /**
     * The default LineWidth
     */
    private static final float[] DEFAULT_LINE_WIDTH = 
        new float[] { 1.0F };

    /**
     * The default PolygonOffset
     */
    private static final float[] DEFAULT_POLYGON_OFFSET = 
        new float[] { 0.0F, 0.0F };

    /**
     * Create the functions that, when executed, call the functions
     * in the {@link GlContext} for setting the states that have been 
     * defined in the {@link TechniqueStatesFunctionsModel}. When any 
     * information is missing, the default values will be set.
     * 
     * @param glContext The {@link GlContext}
     * @param techniqueStatesFunctionsModel The 
     * {@link TechniqueStatesFunctionsModel} 
     * @return The list of commands
     */
    static List<Runnable> createTechniqueStatesFunctionsSettingCommands(
        GlContext glContext, 
        TechniqueStatesFunctionsModel techniqueStatesFunctionsModel)
    {
        List<Runnable> commands = new ArrayList<Runnable>();
        
        float[] blendColor = Optionals.of(
            techniqueStatesFunctionsModel.getBlendColor(),
            DEFAULT_BLENDCOLOR);
        commands.add(() ->
        {
            glContext.setBlendColor(
                blendColor[0], blendColor[1], 
                blendColor[2], blendColor[3]);
        });
        
        int[] blendEquationSeparate = Optionals.of( 
            techniqueStatesFunctionsModel.getBlendEquationSeparate(),
            DEFAULT_BLEND_EQUATION_SEPARATE);
        commands.add(() ->
        {
            glContext.setBlendEquationSeparate(
                blendEquationSeparate[0], blendEquationSeparate[1]);
        });
        
        int[] blendFuncSeparate = Optionals.of(
            techniqueStatesFunctionsModel.getBlendFuncSeparate(),
            DEFAULT_BLEND_FUNC_SEPARATE);
        commands.add(() ->
        {
            glContext.setBlendFuncSeparate(
                blendFuncSeparate[0], blendFuncSeparate[1],
                blendFuncSeparate[2], blendFuncSeparate[3]);
        });
        
        boolean[] colorMask = Optionals.of( 
            techniqueStatesFunctionsModel.getColorMask(),
            DEFAULT_COLOR_MASK);
        commands.add(() ->
        {
            glContext.setColorMask(
                colorMask[0], colorMask[1],
                colorMask[2], colorMask[3]);
        });
        
        int[] cullFace = Optionals.of(
            techniqueStatesFunctionsModel.getCullFace(),
            DEFAULT_CULL_FACE);
        commands.add(() ->
        {
            glContext.setCullFace(cullFace[0]);
        });
        
        int[] depthFunc = Optionals.of(
            techniqueStatesFunctionsModel.getDepthFunc(),
            DEFAULT_DEPTH_FUNC);
        commands.add(() ->
        {
            glContext.setDepthFunc(depthFunc[0]);
        });
        
        boolean[] depthMask = Optionals.of(
            techniqueStatesFunctionsModel.getDepthMask(),
            DEFAULT_DEPTH_MASK);
        commands.add(() ->
        {
            glContext.setDepthMask(depthMask[0]);
        });
        
        float[] depthRange = Optionals.of(
            techniqueStatesFunctionsModel.getDepthRange(),
            DEFAULT_DEPTH_RANGE);
        commands.add(() ->
        {
            glContext.setDepthRange(depthRange[0], depthRange[1]);
        });
        
        int[] frontFace = Optionals.of(
            techniqueStatesFunctionsModel.getFrontFace(),
            DEFAULT_FRONT_FACE);
        commands.add(() ->
        {
            glContext.setFrontFace(frontFace[0]);
        });
        
        float[] lineWidth = Optionals.of(
            techniqueStatesFunctionsModel.getLineWidth(),
            DEFAULT_LINE_WIDTH);
        commands.add(() ->
        {
            glContext.setLineWidth(lineWidth[0]);
        });
        
        float[] polygonOffset = Optionals.of(
            techniqueStatesFunctionsModel.getPolygonOffset(),
            DEFAULT_POLYGON_OFFSET);
        commands.add(() ->
        {
            glContext.setPolygonOffset(
                polygonOffset[0], polygonOffset[1]);
        });
        
        return commands;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private TechniqueStatesFunctions()
    {
        // Private constructor to prevent instantiation
    }

}
