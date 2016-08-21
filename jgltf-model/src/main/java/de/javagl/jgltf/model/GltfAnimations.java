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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Animation;
import de.javagl.jgltf.impl.AnimationChannel;
import de.javagl.jgltf.impl.AnimationChannelTarget;
import de.javagl.jgltf.impl.AnimationSampler;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Node;
import de.javagl.jgltf.model.animation.AnimationListener;
import de.javagl.jgltf.model.animation.AnimationManager;
import de.javagl.jgltf.model.animation.AnimationManager.AnimationPolicy;
import de.javagl.jgltf.model.animation.Interpolators.InterpolatorType;

/**
 * Utility methods to create {@link AnimationManager} instances that
 * contain {@link de.javagl.jgltf.model.animation.Animation} instances
 * that correspond to the {@link Animation} instances in a {@link GlTF}. 
 */
public class GltfAnimations
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfAnimations.class.getName());
    
    /**
     * Create a new {@link AnimationManager} for the given {@link GltfData},
     * using the given {@link AnimationPolicy}, that contains all 
     * {@link Animation} instances created from the {@link GltfData}.
     * 
     * @param gltfData The {@link GltfData}
     * @param animationPolicy The {@link AnimationPolicy}
     * @return The {@link AnimationManager}
     */
    public static AnimationManager createAnimationManager(
        GltfData gltfData, AnimationPolicy animationPolicy)
    {
        AnimationManager animationManager = 
            new AnimationManager(animationPolicy);
        List<de.javagl.jgltf.model.animation.Animation> modelAnimations =
            createModelAnimations(gltfData);
        animationManager.addAnimations(modelAnimations);
        return animationManager;
    }
    
    /**
     * Create a new {@link AnimationManager} using the given 
     * {@link AnimationPolicy}
     * 
     * @param animationPolicy The {@link AnimationPolicy}
     * @return The {@link AnimationManager}
     */
    public static AnimationManager createAnimationManager(
        AnimationPolicy animationPolicy)
    {
        AnimationManager animationManager = 
            new AnimationManager(animationPolicy);
        return animationManager;
    }
    
    /**
     * Create all model {@link de.javagl.jgltf.model.animation.Animation} 
     * instances from the {@link Animation}s in the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @return The model animations
     */
    public static List<de.javagl.jgltf.model.animation.Animation> 
        createModelAnimations(GltfData gltfData)
    {
        Objects.requireNonNull(gltfData, 
            "The gltfData may not be null");
        List<de.javagl.jgltf.model.animation.Animation> allModelAnimations =
            new ArrayList<de.javagl.jgltf.model.animation.Animation>();
        GlTF gltf = gltfData.getGltf();
        Map<String, Animation> animations = gltf.getAnimations();
        if (animations != null)
        {
            for (Entry<String, Animation> entry : animations.entrySet())
            {
                String animationId = entry.getKey();
                Animation animation = entry.getValue();
                List<de.javagl.jgltf.model.animation.Animation> 
                    modelAnimations = createModelAnimations(
                        gltfData, animationId, animation);
                allModelAnimations.addAll(modelAnimations);
            }
        }
        return allModelAnimations;
    }
    
    /**
     * Create one {@link de.javagl.jgltf.model.animation.Animation} for each
     * {@link AnimationChannel} of the given {@link Animation}.
     * If there is any error or inconsistency in the given data, then a 
     * warning will be printed and the respective animation will be
     * skipped.
     * 
     * @param gltfData The {@link GltfData}
     * @param animationId The ID of the {@link Animation} (only for logging)
     * @param animation The {@link Animation}
     * @return The list of model animations
     */
    private static List<de.javagl.jgltf.model.animation.Animation> 
        createModelAnimations(
            GltfData gltfData, String animationId, Animation animation)
    {
        List<de.javagl.jgltf.model.animation.Animation> modelAnimations =
            new ArrayList<de.javagl.jgltf.model.animation.Animation>();
        for (AnimationChannel animationChannel : animation.getChannels())
        {
            de.javagl.jgltf.model.animation.Animation modelAnimation = 
                createModelAnimation(gltfData, animationId, 
                    animation, animationChannel);
            if (modelAnimation != null)
            {
                modelAnimations.add(modelAnimation);
            }
        }
        return modelAnimations;
    }
    
    
    /**
     * Create the {@link de.javagl.jgltf.model.animation.Animation} for 
     * the given {@link AnimationChannel} of the specified {@link GlTF}
     * {@link Animation}. If there is any error or inconsistency in
     * the given data, then a warning will be printed and <code>null</code> 
     * will be returned.
     * 
     * @param gltfData The {@link GltfData}
     * @param animationId The ID of the {@link Animation} (only for logging)
     * @param animation The {@link Animation}
     * @param animationChannel The {@link AnimationChannel}
     * @return The {@link de.javagl.jgltf.model.animation.Animation},
     * or <code>null</code>.
     */
    private static de.javagl.jgltf.model.animation.Animation 
        createModelAnimation(
            GltfData gltfData, String animationId, Animation animation,
            AnimationChannel animationChannel)
    {
        GlTF gltf = gltfData.getGltf();

        String animationChannelSamplerId = animationChannel.getSampler();
        AnimationSampler animationSampler =
            GltfModel.getChecked(animation.getSamplers(), 
                animationChannelSamplerId, "animation channel sampler");

        // Do basic sanity checks of whether the sampler input is valid
        String inputParameterId = animationSampler.getInput();
        if (!isFloatParameter(gltf, animation, inputParameterId))
        {
            logger.warning("Animation channel sampler with ID " + 
                animationChannelSamplerId + " of animation with ID " + 
                animationId + " refers to invalid input parameter " +
                "with ID " + inputParameterId + ". It may only refer " + 
                "to parameters with GL_FLOAT component type.");
            return null;
        }
        
        // Do basic sanity checks of whether the sampler output is valid
        String outputParameterId = animationSampler.getOutput();
        if (!isFloatParameter(gltf, animation, outputParameterId))
        {
            logger.warning("Animation channel sampler with ID " + 
                animationChannelSamplerId + " of animation with ID " + 
                animationId + " refers to invalid output parameter " +
                "with ID " + outputParameterId + ". It may only refer " + 
                "to parameters with GL_FLOAT component type.");
            return null;
        }
        
        // Obtain the interpolation type
        String animationSamplerInterpolation = 
            animationSampler.getInterpolation();
        if (animationSamplerInterpolation == null)
        {
            animationSamplerInterpolation = "LINEAR";
        }
        if (!"LINEAR".equals(animationSamplerInterpolation))
        {
            logger.warning("Animation sampler with ID " + 
                animationChannelSamplerId + " of animation with ID " + 
                animationId + " uses invalid interpolation type " +
                animationSamplerInterpolation + 
                ". Only LINEAR is supported");
            return null;
        }
        
        // Examine the animation channel target 
        AnimationChannelTarget animationChannelTarget = 
            animationChannel.getTarget();
        String animationChannelTargetPath = 
            animationChannelTarget.getPath();
        String animationChannelTargetNodeId = 
            animationChannelTarget.getId();
        
        // Depending on the animation channel target, set the
        // type of the interpolator to use, and create the
        // listener that will forward the animation data to
        // the node (translation, rotation or scale)
        final InterpolatorType interpolatorType;
        AnimationListener animationListener = null;
        if (animationChannelTargetPath.equals("translation"))
        {
            interpolatorType = InterpolatorType.LINEAR;
            animationListener = 
                createTranslationAnimationListener(
                    gltf, animationChannelTargetNodeId);
        }
        else if (animationChannelTargetPath.equals("rotation"))
        {
            interpolatorType = InterpolatorType.SLERP;
            animationListener = 
                createRotationAnimationListener(
                    gltf, animationChannelTargetNodeId);
        }
        else if (animationChannelTargetPath.equals("scale"))
        {
            interpolatorType = InterpolatorType.LINEAR;
            animationListener = 
                createScaleAnimationListener(
                    gltf, animationChannelTargetNodeId);
        }
        else
        {
            logger.warning("Animation channel target path must be "+
                "\"translation\", \"rotation\" or \"scale\", but is " + 
                animationSamplerInterpolation);
            return null;
        }
        
        if (animationListener == null)
        {
            return null;
        }
        
        // If everything went well create the actual model animation
        AccessorFloatData inputParameterData =
            getFloatParameterData(
                gltfData, animation, inputParameterId);
        AccessorFloatData outputParameterData =
            getFloatParameterData(
                gltfData, animation, outputParameterId);

        de.javagl.jgltf.model.animation.Animation modelAnimation = 
            createAnimation(inputParameterData, outputParameterData, 
                interpolatorType);
        modelAnimation.addAnimationListener(animationListener);
        return modelAnimation;
    }

    
    /**
     * Creates a new {@link de.javagl.jgltf.model.animation.Animation} from 
     * the given input data
     * 
     * @param timeData The (1D) {@link AccessorFloatData} containing the
     * time key frames
     * @param outputData The output data that contains the value key frames
     * @param interpolatorType The {@link InterpolatorType} that should
     * be used
     * @return The {@link de.javagl.jgltf.model.animation.Animation}
     */
    static de.javagl.jgltf.model.animation.Animation createAnimation(
        AccessorFloatData timeData,
        AccessorFloatData outputData, 
        InterpolatorType interpolatorType)
    {
        int numElements = timeData.getNumElements();
        float keys[] = new float[numElements];
        for (int e=0; e<numElements; e++)
        {
            keys[e] = timeData.get(e);
        }
        
        int numComponents = outputData.getNumComponentsPerElement();
        float values[][] = new float[numElements][numComponents];
        for (int c=0; c<numComponents; c++)
        {
            for (int e=0; e<numElements; e++)
            {
                values[e][c] = outputData.get(e, c);
            }
        }
        return new de.javagl.jgltf.model.animation.Animation(
            keys, values, interpolatorType);
    }
    
    
    /**
     * Creates an {@link AnimationListener} that writes the animation data
     * into the {@link Node#getTranslation() translation} of the {@link Node}
     * with the given ID in the given {@link GlTF}. If the specified 
     * {@link Node} can not be found, then a warning will be printed
     * and <code>null</code> will be returned.
     *  
     * @param gltf The {@link GlTF}
     * @param nodeId The {@link Node} ID
     * @return The {@link AnimationListener}
     */
    private static AnimationListener createTranslationAnimationListener(
        GlTF gltf, String nodeId)
    {
        Node node = GltfModel.getExpected(
            gltf.getNodes(), nodeId, "animated node");
        if (node == null)
        {
            return null;
        }
        return (animation, timeS, values) ->
        {
            float t[] = node.getTranslation();
            System.arraycopy(values, 0, t, 0, values.length);
        };
    }
    
    /**
     * Creates an {@link AnimationListener} that writes the animation data
     * into the {@link Node#getRotation() rotation} of the {@link Node}
     * with the given ID in the given {@link GlTF}. If the specified 
     * {@link Node} can not be found, then a warning will be printed
     * and <code>null</code> will be returned.
     *  
     * @param gltf The {@link GlTF}
     * @param nodeId The {@link Node} ID
     * @return The {@link AnimationListener}
     */
    private static AnimationListener createRotationAnimationListener(
        GlTF gltf, String nodeId)
    {
        Node node = GltfModel.getExpected(
            gltf.getNodes(), nodeId, "animated node");
        if (node == null)
        {
            return null;
        }
        return (animation, timeS, values) ->
        {
            float t[] = node.getRotation();
            System.arraycopy(values, 0, t, 0, values.length);
        };
    }
    
    /**
     * Creates an {@link AnimationListener} that writes the animation data
     * into the {@link Node#getScale() scale} of the {@link Node}
     * with the given ID in the given {@link GlTF}. If the specified 
     * {@link Node} can not be found, then a warning will be printed
     * and <code>null</code> will be returned.
     *  
     * @param gltf The {@link GlTF}
     * @param nodeId The {@link Node} ID
     * @return The {@link AnimationListener}
     */
    private static AnimationListener createScaleAnimationListener(
        GlTF gltf, String nodeId)
    {
        Node node = GltfModel.getExpected(
            gltf.getNodes(), nodeId, "animated node");
        if (node == null)
        {
            return null;
        }
        return (animation, timeS, values) ->
        {
            float t[] = node.getScale();
            System.arraycopy(values, 0, t, 0, values.length);
        };
    }
    
    
    
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
    private static boolean isFloatParameter(
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
    private static AccessorFloatData getFloatParameterData(
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
    private GltfAnimations()
    {
        // Private constructor to prevent instantiation
    }
}
