/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.jgltfifier;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultChannel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultSampler;

/**
 * A code creator for the animations code
 */
class AnimationsCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(AnimationsCodeCreator.class.getName());

    /**
     * The glTF model
     */
    private final GltfModel gltfModel;

    /**
     * Creates a new instance
     * 
     * @param codeModel The code model
     * @param definedClass The defined class
     * @param gltfModel The glTF model
     */
    AnimationsCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<AnimationModel> animationModels = gltfModel.getAnimationModels();
        if (animationModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Animations (" + animationModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < animationModels.size(); i++)
        {
            block.directStatement(
                "// Animation " + i + " of " + animationModels.size());
            AnimationModel animationModel = animationModels.get(i);
            createAnimation(block, animationModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given animation, and add it to the given
     * block
     * 
     * @param block The block
     * @param animationModel The animation
     * @param animationIndex The index of the animation
     */
    private void createAnimation(JBlock block, AnimationModel animationModel,
        int animationIndex)
    {
        JClass defaultAnimationModelClass =
            findClass(DefaultAnimationModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultAnimationModelClass,
            "animationModel" + animationIndex);

        JMethod method =
            createAnimationCreationMethod(animationModel, animationIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given animation model
     * 
     * @param animationModel The animation model
     * @param animationIndex The animation index
     * @return The method
     */
    private JMethod createAnimationCreationMethod(AnimationModel animationModel,
        int animationIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createAnimationModel" + animationIndex);
        Comments.add(method, "Create the specified animation model");

        JBlock block = method.body();
        createAnimationCreationCode(block, animationModel, animationIndex);
        return method;
    }

    /**
     * Create the code that creates the given animation model and add it to the
     * given block
     * 
     * @param block The block
     * @param animationModel The animation model
     * @param animationIndex The animation index
     */
    private void createAnimationCreationCode(JBlock block,
        AnimationModel animationModel, int animationIndex)
    {
        // Collect the required types
        JClass defaultAnimationModelClass =
            findClass(DefaultAnimationModel.class);

        // this.animationModelX = new DefaultAnimationModel()
        JFieldRef animationVar =
            JExpr._this().ref("animationModel" + animationIndex);
        block.assign(animationVar, JExpr._new(defaultAnimationModelClass));

        createChannels(block, animationVar, animationModel, animationIndex);
    }

    /**
     * Create the code for creating the channels of the specified animation of
     * the current glTF model, and add it to the given block
     * 
     * @param block The block
     * @param animationVar The animationModelX variable
     * @param animationModel The animation model
     * @param animationIndex The animation index
     */
    private void createChannels(JBlock block, JAssignmentTarget animationVar,
        AnimationModel animationModel, int animationIndex)
    {
        List<Channel> channels = animationModel.getChannels();
        block.directStatement("// Channels of animation " + animationIndex
            + " (" + channels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < channels.size(); i++)
        {
            block.directStatement("// Channel " + i + " of " + channels.size()
                + " of animation " + animationIndex);
            Channel channel = channels.get(i);
            createChannel(block, animationVar, animationModel, animationIndex,
                channel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given channels of the specified
     * animation of the current glTF model, and add it to the given block
     * 
     * @param block The block
     * @param animationVar The animationModelX variable
     * @param animationModel The animation model
     * @param animationIndex The animation index
     * @param channel The channel
     * @param channelIndex The channel index
     */
    private void createChannel(JBlock block, JAssignmentTarget animationVar,
        AnimationModel animationModel, int animationIndex, Channel channel,
        int channelIndex)
    {
        // Collect the required types
        JClass defaultChannelClass = findClass(DefaultChannel.class);
        JClass defaultSamplerClass = findClass(DefaultSampler.class);
        JClass interpolationClass = findClass(Interpolation.class);

        // Sampler
        Sampler sampler = channel.getSampler();
        Interpolation interpolation = sampler.getInterpolation();

        // Input
        AccessorModel input = sampler.getInput();
        int inputIndex = gltfModel.getAccessorModels().indexOf(input);
        if (inputIndex == -1)
        {
            logger.severe("Could not find accessor model for "
                + " input of sampler of channel " + channelIndex
                + " in animation " + animationIndex);
            return;
        }

        // Output
        AccessorModel output = sampler.getOutput();
        int outputIndex = gltfModel.getAccessorModels().indexOf(output);
        if (outputIndex == -1)
        {
            logger.severe("Could not find accessor model for "
                + " output of sampler of channel " + channelIndex
                + " in animation " + animationIndex);
            return;
        }

        // DefaultSampler samplerX_Y = new DefaultSampler(
        // accessorModelZ, Interpolation.LINEAR, accessorModelW)
        JVar samplerVar = block.decl(defaultSamplerClass,
            "sampler" + animationIndex + "_" + channelIndex,
            JExpr._new(defaultSamplerClass)
                .arg(JExpr._this().ref("accessorModel" + inputIndex))
                .arg(interpolationClass.staticRef(interpolation.toString()))
                .arg(JExpr._this().ref("accessorModel" + outputIndex)));

        // Node
        NodeModel nodeModel = channel.getNodeModel();
        int nodeIndex = gltfModel.getNodeModels().indexOf(nodeModel);
        if (nodeIndex == -1)
        {
            logger.severe("Could not find node model for " + " channel "
                + channelIndex + " of animation " + animationIndex);
            return;
        }

        // Path
        String path = channel.getPath();

        // DefaultChannel channelX_Y = new DefaultChannel(
        // samplerX_Y, nodeModelZ, "rotation")
        JVar channelVar = block.decl(defaultChannelClass,
            "channel" + animationIndex + "_" + channelIndex,
            JExpr._new(defaultChannelClass).arg(samplerVar)
                .arg(JExpr._this().ref("nodeModel" + nodeIndex))
                .arg(JExpr.lit(path)));

        block.add(animationVar.invoke("addChannel").arg(channelVar));
    }

}
