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

/**
 * GL constants that are required for the <code>techniqe.states</code>
 * and <code>techniqe.states.functions</code>
 */
@SuppressWarnings("javadoc")
class GlConstants
{
    // glEnable for techniqe.states
    static final int GL_BLEND = 3042;
    static final int GL_CULL_FACE = 2884;
    static final int GL_DEPTH_TEST = 2929;
    static final int GL_POLYGON_OFFSET_FILL = 32823;
    static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 32926;
    static final int GL_SCISSOR_TEST = 3089;

    // glBlendEquationSeparate
    static final int GL_FUNC_ADD = 32774;
    static final int GL_FUNC_SUBTRACT = 32778;
    static final int GL_FUNC_REVERSE_SUBTRACT = 32779;

    // glBlendFuncSeparate
    static final int GL_ZERO = 0;
    static final int GL_ONE = 1;
    static final int GL_SRC_COLOR = 768;
    static final int GL_ONE_MINUS_SRC_COLOR = 769;
    static final int GL_DST_COLOR = 774;
    static final int GL_ONE_MINUS_DST_COLOR = 775;
    static final int GL_SRC_ALPHA = 770;
    static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    static final int GL_DST_ALPHA = 772;
    static final int GL_ONE_MINUS_DST_ALPHA = 773;
    static final int GL_CONSTANT_COLOR = 32769;
    static final int GL_ONE_MINUS_CONSTANT_COLOR = 32770;
    static final int GL_CONSTANT_ALPHA = 32771;
    static final int GL_ONE_MINUS_CONSTANT_ALPHA = 32772;
    static final int GL_SRC_ALPHA_SATURATE = 776;

    // glCullFace
    static final int GL_FRONT = 1028;
    static final int GL_BACK = 1029;
    static final int GL_FRONT_AND_BACK = 1032;

    // glDepthFunc
    static final int GL_NEVER = 512;
    static final int GL_LESS = 513;
    static final int GL_LEQUAL = 515;
    static final int GL_EQUAL = 514;
    static final int GL_GREATER = 516;
    static final int GL_NOTEQUAL = 517;
    static final int GL_GEQUAL = 518;
    static final int GL_ALWAYS = 519;

    // glFrontFace
    static final int GL_CW = 2304;
    static final int GL_CCW = 2305;    

    /**
     * Private constructor to prevent instantiation
     */
    private GlConstants()
    {
        // Private constructor to prevent instantiation
    }
}
