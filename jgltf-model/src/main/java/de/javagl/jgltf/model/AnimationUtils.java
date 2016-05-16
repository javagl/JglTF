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
package de.javagl.jgltf.model;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Animation;
import de.javagl.jgltf.impl.GlTF;

/**
 * Utility methods related to {@link Animation}s
 */
public class AnimationUtils
{
    /**
     * Returns whether the {@link Accessor} that is referred to by the
     * sampler parameter with the given ID in the given {@link Animation}
     * has <code>float</code> components
     * 
     * @param gltf The {@link GlTF}
     * @param animation The {@link Animation}
     * @param animationSamplerParameterId The {@link Animation#getParameters()
     * animation parameter} ID
     * @return Whether the parameter is a parameter with <code>float</code>
     * components
     */
    public static boolean isFloatParameter(
        GlTF gltf, Animation animation, String animationSamplerParameterId)
    {
        String animationSamplerParameterAccessorId = 
            animation.getParameters().get(animationSamplerParameterId);
        Accessor animationSamplerParameterAccessor = 
            gltf.getAccessors().get(animationSamplerParameterAccessorId);
        return AccessorDatas.hasFloatComponents(
            animationSamplerParameterAccessor);
    }
    
    /**
     * Returns the {@link AccessorFloatData} for the data {@link Accessor}
     * that is referred to by the sampler parameter with the given ID in
     * the given {@link Animation}
     * 
     * @param gltfData The {@link GltfData}
     * @param animation The {@link Animation}
     * @param animationSamplerParameterId The {@link Animation#getParameters()
     * animation parameter} ID
     * @return The {@link AccessorFloatData} for the {@link Animation}
     * parameter
     * @throws IllegalArgumentException If the 
     * {@link Accessor#getComponentType() component type} of the 
     * accessor that is used for the data is not <code>GL_FLOAT</code>
     */
    public static AccessorFloatData getFloatParameterData(
        GltfData gltfData, Animation animation, 
        String animationSamplerParameterId)
    {
        String animationSamplerParameterAccessorId = 
            animation.getParameters().get(animationSamplerParameterId);
        GlTF gltf = gltfData.getGltf();
        Accessor animationSamplerParameterAccessor = 
            gltf.getAccessors().get(animationSamplerParameterAccessorId);
        return AccessorDatas.createFloat(
            animationSamplerParameterAccessor, gltfData);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private AnimationUtils()
    {
        // Private constructor to prevent instantiation
    }
}
