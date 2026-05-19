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
package de.javagl.jgltf.model.creation;

import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.MathUtils;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultChannel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultSampler;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class for building {@link DefaultAnimationModel} instances.<br>
 * <br>
 * This class exposes a few static classes that will usually not directly be
 * used by most clients. These classes are only intended for handling the
 * different interpolation types (linear, step, and spline) for the different
 * node properties (translation, rotation, scale) as transparently and easily as
 * possible.
 * 
 * Usually the process for building an animation will be as follows:
 * 
 * <ul>
 * <li>Create an animation builder instance with
 * {@link AnimationBuilder#create()}</li>
 * <li>Create a channel builder with
 * {@link AnimationBuilder#beginChannelLinear(NodeModel)}.<br>
 * Different flavors of this function exist, one for each interpolation type.
 * Additionally, there is a generic form of that, which allows passing in the
 * interpolation types manually, for example:<br>
 * 
 * <pre>
 * <code>
 *     cb = ab.beginChannel(nodeModel, 
 *       TranslationInterpolation.LINEAR, 
 *       RotationInterpolation.STEP, 
 *       ScaleInterpolation.SPLINE);
 *     </code>
 * </pre>
 * 
 * </li>
 * <li>Use the {@link ChannelBuilder#translations()},
 * {@link ChannelBuilder#rotations()}, and {@link ChannelBuilder#scales()}
 * handles to pass in key frames.</li>
 * <li><b>Important:</b> Mark the end of the channel, by calling
 * {@link ChannelBuilder#end()}</li>
 * 
 * </ul>
 * 
 * A complete example for building a linear translation animation is shown here:
 * 
 * <pre>
 * <code>
 * AnimationBuilder ab = AnimationBuilder.create();
 * ChannelBuilder<LinearT, LinearR, LinearS> cb = ab.beginChannelLinear(nodeModel);
 * 
 * cb.translations().add(0.5, 0.0, 0.0, 0.0);
 * cb.translations().add(1.5, 1.0, 0.0, 0.0);
 * cb.translations().add(2.5, 2.0, 0.0, 0.0);
 * cb.translations().add(4.5, 0.0, 0.0, 0.0);
 * 
 * cb.end();
 * 
 * DefaultAnimationModel animationModel = ab.build();
 * </code>
 * </pre>
 */
public class AnimationBuilder
{
    // TODO: There should be a warning when the animation is built while
    // a channel has not been "end()"ed.
    // TODO: All rotation handles should offer "axis-angle" functions
    
    /**
     * A class containing constants for translation interpolation
     *
     * @param <T> The interpolation type
     */
    public static class TranslationInterpolation<T>
    {
        /**
         * Linear translation interpolation
         */
        public static final TranslationInterpolation<LinearT> LINEAR =
            new TranslationInterpolation<LinearT>(LinearT::new);

        /**
         * Step translation interpolation
         */
        public static final TranslationInterpolation<StepT> STEP =
            new TranslationInterpolation<StepT>(StepT::new);

        /**
         * Spline translation interpolation
         */
        public static final TranslationInterpolation<SplineT> SPLINE =
            new TranslationInterpolation<SplineT>(SplineT::new);

        /**
         * The factory
         */
        private final Function<NodeModel, T> factory;

        /**
         * Create the interpolation instance
         * 
         * @param nodeModel The node model
         * @return The instance
         */
        T create(NodeModel nodeModel)
        {
            return factory.apply(nodeModel);
        }

        /**
         * Default constructor
         * 
         * @param factory The factory
         */
        private TranslationInterpolation(Function<NodeModel, T> factory)
        {
            this.factory = factory;
        }
    }

    /**
     * A class containing constants for rotation interpolation
     *
     * @param <R> The interpolation type
     */
    public static class RotationInterpolation<R>
    {
        /**
         * Linear rotation interpolation
         */
        public static final RotationInterpolation<LinearR> LINEAR =
            new RotationInterpolation<LinearR>(LinearR::new);

        /**
         * Step rotation interpolation
         */
        public static final RotationInterpolation<StepR> STEP =
            new RotationInterpolation<StepR>(StepR::new);

        /**
         * Spline rotation interpolation
         */
        public static final RotationInterpolation<SplineR> SPLINE =
            new RotationInterpolation<SplineR>(SplineR::new);

        /**
         * The factory
         */
        private final Function<NodeModel, R> factory;

        /**
         * Create the interpolation instance
         * 
         * @param nodeModel The node model
         * @return The instance
         */
        R create(NodeModel nodeModel)
        {
            return factory.apply(nodeModel);
        }

        /**
         * Default constructor
         * 
         * @param factory The factory
         */
        private RotationInterpolation(Function<NodeModel, R> factory)
        {
            this.factory = factory;
        }
    }

    /**
     * A class containing constants for scale interpolation
     *
     * @param <S> The interpolation type
     */
    public static class ScaleInterpolation<S>
    {
        /**
         * Linear scale interpolation
         */
        public static final ScaleInterpolation<LinearS> LINEAR =
            new ScaleInterpolation<LinearS>(LinearS::new);

        /**
         * Step scale interpolation
         */
        public static final ScaleInterpolation<StepS> STEP =
            new ScaleInterpolation<StepS>(StepS::new);

        /**
         * Spline scale interpolation
         */
        public static final ScaleInterpolation<SplineS> SPLINE =
            new ScaleInterpolation<SplineS>(SplineS::new);

        /**
         * The factory
         */
        private final Function<NodeModel, S> factory;

        /**
         * Create the interpolation instance
         * 
         * @param nodeModel The node model
         * @return The instance
         */
        S create(NodeModel nodeModel)
        {
            return factory.apply(nodeModel);
        }

        /**
         * Default constructor
         * 
         * @param factory The factory
         */
        private ScaleInterpolation(Function<NodeModel, S> factory)
        {
            this.factory = factory;
        }
    }

    /**
     * Interface for interpolation channel component handles
     */
    private static interface Handle
    {
        /**
         * Returns all times for the animation channel
         * 
         * @return The times
         */
        Set<Double> getTimes();

        /**
         * Creates the channel
         * 
         * @param timesAccessorModel The accessor model for the times
         * @return The channel
         */
        DefaultChannel createChannel(AccessorModel timesAccessorModel);
    }

    /**
     * A base class for linear and step animations
     */
    private static class SimpleHandle implements Handle
    {
        /**
         * The node model for the animation channel
         */
        private final NodeModel nodeModel;

        /**
         * The interpolation (STEP or LINEAR)
         */
        private final Interpolation interpolation;

        /**
         * The number of components.
         * 
         * This is 3 for translation and scale, and 4 for rotations
         */
        private final int components;

        /**
         * The channel path, "translation", "rotation", or "scale"
         */
        private final String path;

        /**
         * The mapping from key frame times to values
         */
        private final Map<Double, double[]> map;

        /**
         * Creates a new instance
         * 
         * @param nodeModel The node model
         * @param components The number of components
         * @param interpolation The interpolation
         * @param path The path
         */
        private SimpleHandle(NodeModel nodeModel, int components,
            Interpolation interpolation, String path)
        {
            this.nodeModel = nodeModel;
            this.components = components;
            this.interpolation = interpolation;
            this.path = path;
            this.map = new TreeMap<Double, double[]>();
        }

        /**
         * Add the given key frame
         * 
         * @param time The time
         * @param values The values
         */
        protected final void addInternal(double time, double values[])
        {
            if (values.length != components)
            {
                throw new IllegalArgumentException(
                    "The values must have a length of " + components
                        + ", but have a length of " + values.length);
            }
            map.put(time, values);
        }

        @Override
        public Set<Double> getTimes()
        {
            return map.keySet();
        }

        @Override
        public DefaultChannel createChannel(AccessorModel timesAccessorModel)
        {
            FloatBuffer buffer = flatten1D(map.values(), components);
            AccessorModel accessorModel;
            if (components == 3)
            {
                accessorModel = AccessorModels.createFloat3D(buffer);
            }
            else
            {
                accessorModel = AccessorModels.createFloat4D(buffer);
            }

            Sampler sampler = new DefaultSampler(timesAccessorModel,
                interpolation, accessorModel);

            DefaultChannel channel =
                new DefaultChannel(sampler, nodeModel, path);
            return channel;
        }

    }

    /**
     * A base class for spline animations
     */
    private static class SplineHandle implements Handle
    {
        /**
         * The node model for the animation channel
         */
        private final NodeModel nodeModel;

        /**
         * The number of components.
         * 
         * This is 3 for translation and scale, and 4 for rotations
         */
        private final int components;

        /**
         * The channel path, "translation", "rotation", or "scale"
         */
        private final String path;

        /**
         * The mapping from key frame times to values
         */
        private final Map<Double, double[][]> map;

        /**
         * Creates a new instance
         * 
         * @param nodeModel The node model
         * @param components The number of components
         * @param path The path
         */
        private SplineHandle(NodeModel nodeModel, int components, String path)
        {
            this.nodeModel = nodeModel;
            this.components = components;
            this.path = path;
            this.map = new TreeMap<Double, double[][]>();
        }

        @Override
        public Set<Double> getTimes()
        {
            return map.keySet();
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param values The values
         */
        protected final void addInternal(double time, double values[][])
        {
            if (values.length != 3)
            {
                throw new IllegalArgumentException(
                    "The values must have a length of " + 3
                        + ", but have a length of " + values.length);
            }
            for (double value[] : values)
            {
                if (value.length != components)
                {
                    throw new IllegalArgumentException(
                        "Each of the values must have a length of " + components
                            + ", but have a length of " + value.length);
                }
            }
            map.put(time, values);
        }

        @Override
        public DefaultChannel createChannel(AccessorModel timesAccessorModel)
        {
            FloatBuffer buffer = flatten2D(map.values(), 3 * components);

            AccessorModel accessorModel;
            if (components == 3)
            {
                accessorModel = AccessorModels.createFloat3D(buffer);
            }
            else
            {
                accessorModel = AccessorModels.createFloat4D(buffer);
            }

            Sampler sampler = new DefaultSampler(timesAccessorModel,
                Interpolation.CUBICSPLINE, accessorModel);

            DefaultChannel channel =
                new DefaultChannel(sampler, nodeModel, path);
            return channel;
        }
    }

    /**
     * A class for defining linear translation animations
     */
    public static class LinearT extends SimpleHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private LinearT(NodeModel nodeModel)
        {
            super(nodeModel, 3, Interpolation.LINEAR, "translation");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @return This instance
         */
        public LinearT add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param value The value
         * @return This instance
         */
        public LinearT add(double time, double value[])
        {
            addInternal(time, value.clone());
            return this;
        }

    }

    /**
     * A class for defining linear translation animations
     */
    public static class StepT extends SimpleHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private StepT(NodeModel nodeModel)
        {
            super(nodeModel, 3, Interpolation.STEP, "translation");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @return This instance
         */
        public StepT add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param value The value
         * @return This instance
         */
        public StepT add(double time, double value[])
        {
            addInternal(time, value.clone());
            return this;
        }

    }

    /**
     * A class for defining spline translation animations
     */
    public static class SplineT extends SplineHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private SplineT(NodeModel nodeModel)
        {
            super(nodeModel, 3, "translation");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param xIn The x-component of the in-tangent
         * @param yIn The y-component of the in-tangent
         * @param zIn The z-component of the in-tangent
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @param xOut The x-component of the out-tangent
         * @param yOut The y-component of the out-tangent
         * @param zOut The z-component of the out-tangent
         * @return This instance
         */
        public SplineT add(double time, double xIn, double yIn, double zIn,
            double x, double y, double z, double xOut, double yOut, double zOut)
        {
            double v[][] = new double[3][3];
            v[0][0] = xIn;
            v[0][1] = yIn;
            v[0][2] = zIn;

            v[1][0] = x;
            v[1][1] = y;
            v[1][2] = z;

            v[2][0] = xOut;
            v[2][1] = yOut;
            v[2][2] = zOut;
            addInternal(time, v);
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param in The in-tangent
         * @param v The key frame
         * @param out The out-tangent
         * @return This instance
         */
        public SplineT add(double time, double in[], double v[], double out[])
        {
            double vs[][] = new double[3][];
            vs[0] = in.clone();
            vs[1] = v.clone();
            vs[2] = out.clone();
            addInternal(time, vs);
            return this;
        }

    }

    /**
     * A class for defining linear rotation animations
     */
    public static class LinearR extends SimpleHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private LinearR(NodeModel nodeModel)
        {
            super(nodeModel, 4, Interpolation.LINEAR, "rotation");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @param w The w-component
         * @return This instance
         */
        public LinearR add(double time, double x, double y, double z, double w)
        {
            addInternal(time, new double[]
            { x, y, z, w });
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param value The value
         */
        public void add(double time, double value[])
        {
            addInternal(time, value.clone());
        }

        /**
         * Add the given rotation at the given time stamp
         * 
         * @param time The time stamp.
         * 
         * @param x The x-component of the rotation axis
         * @param y The y-component of the rotation axis
         * @param z The z-component of the rotation axis
         * @param angleRad The rotation angle in radians
         * @return This instance
         */
        public LinearR addAxisAngle(double time, double x, double y, double z,
            double angleRad)
        {
            double q[] = new double[4];
            MathUtils.axisAngleRadToQuaternion(x, y, z, angleRad, q);
            addInternal(time, q);
            return this;
        }

        /**
         * Add the given rotation at the given time stamp
         * 
         * @param time The time stamp.
         * @param axisAngle The axis and angle
         * @return This instance
         */
        public LinearR addAxisAngle(double time, double axisAngle[])
        {
            return addAxisAngle(time, axisAngle[0], axisAngle[1], axisAngle[2],
                axisAngle[3]);
        }

    }

    /**
     * A class for defining linear rotation animations
     */
    public static class StepR extends SimpleHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private StepR(NodeModel nodeModel)
        {
            super(nodeModel, 4, Interpolation.STEP, "rotation");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @param w The w-component
         * @return This instance
         */
        public StepR add(double time, double x, double y, double z, double w)
        {
            addInternal(time, new double[]
            { x, y, z, w });
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param value The value
         * @return This instance
         */
        public StepR add(double time, double value[])
        {
            addInternal(time, value.clone());
            return this;
        }

    }

    /**
     * A class for defining spline rotation animations
     */
    public static class SplineR extends SplineHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private SplineR(NodeModel nodeModel)
        {
            super(nodeModel, 4, "rotation");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param xIn The x-component of the in-tangent
         * @param yIn The y-component of the in-tangent
         * @param zIn The z-component of the in-tangent
         * @param wIn The w-component of the in-tangent
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @param w The w-component
         * @param xOut The x-component of the out-tangent
         * @param yOut The y-component of the out-tangent
         * @param zOut The z-component of the out-tangent
         * @param wOut The w-component of the out-tangent
         * @return This instance
         */
        SplineR add(double time, double xIn, double yIn, double zIn, double wIn,
            double x, double y, double z, double w, double xOut, double yOut,
            double zOut, double wOut)
        {
            double v[][] = new double[3][4];
            v[0][0] = xIn;
            v[0][1] = yIn;
            v[0][2] = zIn;
            v[0][3] = wIn;

            v[1][0] = x;
            v[1][1] = y;
            v[1][2] = z;
            v[1][3] = w;

            v[2][0] = xOut;
            v[2][1] = yOut;
            v[2][2] = zOut;
            v[2][3] = wOut;
            addInternal(time, v);
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param in The in-tangent
         * @param v The key frame
         * @param out The out-tangent
         * @return This instance
         */
        public SplineR add(double time, double in[], double v[], double out[])
        {
            double vs[][] = new double[3][];
            vs[0] = in.clone();
            vs[1] = v.clone();
            vs[2] = out.clone();
            addInternal(time, vs);
            return this;
        }

    }

    /**
     * A class for defining linear scale animations
     */
    public static class LinearS extends SimpleHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private LinearS(NodeModel nodeModel)
        {
            super(nodeModel, 3, Interpolation.LINEAR, "scale");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @return This instance
         */
        public LinearS add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param value The value
         * @return This instance
         */
        public LinearS add(double time, double value[])
        {
            addInternal(time, value.clone());
            return this;
        }

    }

    /**
     * A class for defining linear scale animations
     */
    public static class StepS extends SimpleHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private StepS(NodeModel nodeModel)
        {
            super(nodeModel, 3, Interpolation.STEP, "scale");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @return This instance
         */
        public StepS add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param value The value
         * @return This instance
         */
        public StepS add(double time, double value[])
        {
            addInternal(time, value.clone());
            return this;
        }

    }

    /**
     * A class for defining spline scale animations
     */
    public static class SplineS extends SplineHandle
    {
        /**
         * Default constructor
         * 
         * @param nodeModel The node model
         */
        private SplineS(NodeModel nodeModel)
        {
            super(nodeModel, 3, "scale");
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param xIn The x-component of the in-tangent
         * @param yIn The y-component of the in-tangent
         * @param zIn The z-component of the in-tangent
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         * @param xOut The x-component of the out-tangent
         * @param yOut The y-component of the out-tangent
         * @param zOut The z-component of the out-tangent
         * @return This instance
         */
        public SplineS add(double time, double xIn, double yIn, double zIn,
            double x, double y, double z, double xOut, double yOut, double zOut)
        {
            double v[][] = new double[3][3];
            v[0][0] = xIn;
            v[0][1] = yIn;
            v[0][2] = zIn;

            v[1][0] = x;
            v[1][1] = y;
            v[1][2] = z;

            v[2][0] = xOut;
            v[2][1] = yOut;
            v[2][2] = zOut;
            addInternal(time, v);
            return this;
        }

        /**
         * Add the specified key frame
         * 
         * @param time The time
         * @param in The in-tangent
         * @param v The key frame
         * @param out The out-tangent
         * @return This instance
         */
        public SplineS add(double time, double in[], double v[], double out[])
        {
            double vs[][] = new double[3][];
            vs[0] = in.clone();
            vs[1] = v.clone();
            vs[2] = out.clone();
            addInternal(time, vs);
            return this;
        }

    }

    /**
     * A class for building animation channels
     * 
     * @param <T> The translation interpolation type
     * @param <R> The rotation interpolation type
     * @param <S> The scale interpolation type
     */
    public static class ChannelBuilder<T extends Handle, R extends Handle, S extends Handle>
    {
        /**
         * The owner of this instance
         */
        private final AnimationBuilder owner;

        /**
         * The animation model that is built in the owner
         */
        private final DefaultAnimationModel animationModel;

        /**
         * The translation animation handler
         */
        private final T translations;

        /**
         * The rotation animation handler
         */
        private final R rotations;

        /**
         * The scale animation handler
         */
        private final S scales;

        /**
         * A lookup from key frame times to accessor models
         */
        private final Map<Set<Double>, AccessorModel> timesAccessorModels;

        /**
         * Creates a new instance
         * 
         * @param owner The owner
         * @param animationModel The animation model
         * @param translations The translation handle
         * @param rotations The rotation handle
         * @param scales The scale handle
         */
        ChannelBuilder(AnimationBuilder owner,
            DefaultAnimationModel animationModel, T translations, R rotations,
            S scales)
        {
            this.owner = owner;
            this.animationModel = animationModel;
            this.translations = translations;
            this.rotations = rotations;
            this.scales = scales;
            this.timesAccessorModels =
                new LinkedHashMap<Set<Double>, AccessorModel>();
        }

        /**
         * Returns the handle for adding translation key frames
         * 
         * @return The handle
         */
        public T translations()
        {
            return translations;
        }

        /**
         * Returns the handle for adding rotation key frames
         * 
         * @return The handle
         */
        public R rotations()
        {
            return rotations;
        }

        /**
         * Returns the handle for adding scale key frames
         * 
         * @return The handle
         */
        public S scales()
        {
            return scales;
        }

        /**
         * Obtain an accessor model for the given key frame times.
         * 
         * This will re-use existing accessor models that already represent the
         * same key frame times
         * 
         * @param times The times
         * @return The accessor model
         */
        private AccessorModel fetchTimesAccessorModel(Set<Double> times)
        {
            AccessorModel accessorModel = timesAccessorModels.get(times);
            if (accessorModel != null)
            {
                return accessorModel;
            }
            FloatBuffer timesBuffer = floats(times);
            accessorModel = AccessorModels.createFloatScalar(timesBuffer);
            timesAccessorModels.put(times, accessorModel);
            return accessorModel;
        }

        /**
         * End building the animation channel
         * 
         * @return The animation builder
         * @throws IllegalStateException If no key frames have been added after
         *         this instance was created, or this instance contains key
         *         frames for an animation channel that was already created
         */
        public AnimationBuilder end()
        {
            boolean created = false;

            Set<Double> translationTimes = translations.getTimes();
            if (!translationTimes.isEmpty())
            {
                AccessorModel timesAccessorModel =
                    fetchTimesAccessorModel(translationTimes);
                DefaultChannel translationChannel =
                    translations.createChannel(timesAccessorModel);
                ensureChannelPathIsNew(animationModel, "translation");
                animationModel.addChannel(translationChannel);
                created = true;
            }

            Set<Double> rotationTimes = rotations.getTimes();
            if (!rotationTimes.isEmpty())
            {
                AccessorModel timesAccessorModel =
                    fetchTimesAccessorModel(rotationTimes);
                DefaultChannel rotationChannel =
                    rotations.createChannel(timesAccessorModel);
                ensureChannelPathIsNew(animationModel, "rotation");
                animationModel.addChannel(rotationChannel);
                created = true;
            }

            Set<Double> scaleTimes = scales.getTimes();
            if (!scaleTimes.isEmpty())
            {
                AccessorModel timesAccessorModel =
                    fetchTimesAccessorModel(scaleTimes);
                DefaultChannel scaleChannel =
                    scales.createChannel(timesAccessorModel);
                ensureChannelPathIsNew(animationModel, "scale");
                animationModel.addChannel(scaleChannel);
                created = true;
            }

            if (!created)
            {
                throw new IllegalStateException(
                    "No key frames have been added to the current channel.");
            }
            return owner;
        }
    }

    /**
     * Ensure that the given animation model does not contain a channel with the
     * given path name, and throw an exception of this is not the case.
     * 
     * @param animationModel The animation model
     * @param path The path
     * @throws IllegalStateException If the path already exists
     */
    private static void ensureChannelPathIsNew(AnimationModel animationModel,
        String path)
    {
        List<Channel> channels = animationModel.getChannels();
        for (Channel channel : channels)
        {
            if (channel.getPath().equals(path))
            {
                throw new IllegalStateException(
                    "The animation already has a '" + path + "' channel.");
            }
        }
    }

    /**
     * Creates a new instance
     * 
     * @return The {@link AnimationBuilder}
     */
    public static AnimationBuilder create()
    {
        return new AnimationBuilder();
    }

    /**
     * The animation model that is currently being built
     */
    private DefaultAnimationModel animationModel;

    /**
     * Private constructor to be called from {@link #create()}.
     */
    private AnimationBuilder()
    {
        this.animationModel = new DefaultAnimationModel();
    }

    /**
     * Begin a new channel.
     * 
     * This will use LINEAR interpolation for translation, rotation, and scale
     * 
     * @param nodeModel The node model that should be affected
     * @return The channel builder
     */
    public ChannelBuilder<LinearT, LinearR, LinearS>
        beginChannelLinear(NodeModel nodeModel)
    {
        return beginChannel(nodeModel, TranslationInterpolation.LINEAR,
            RotationInterpolation.LINEAR, ScaleInterpolation.LINEAR);
    }

    /**
     * Begin a new channel.
     * 
     * This will use STEP interpolation for translation, rotation, and scale
     * 
     * @param nodeModel The node model that should be affected
     * @return The channel builder
     */
    public ChannelBuilder<StepT, StepR, StepS>
        beginChannelStep(NodeModel nodeModel)
    {
        return beginChannel(nodeModel, TranslationInterpolation.STEP,
            RotationInterpolation.STEP, ScaleInterpolation.STEP);
    }

    /**
     * Begin a new channel.
     * 
     * This will use SPLINE interpolation for translation, rotation, and scale
     * 
     * @param nodeModel The node model that should be affected
     * @return The channel builder
     */
    public ChannelBuilder<SplineT, SplineR, SplineS>
        beginChannelSpline(NodeModel nodeModel)
    {
        return beginChannel(nodeModel, TranslationInterpolation.SPLINE,
            RotationInterpolation.SPLINE, ScaleInterpolation.SPLINE);
    }

    /**
     * Begins a new channel with the given interpolation types
     * 
     * @param <T> The translation interpolation type
     * @param <R> The rotation interpolation type
     * @param <S> The scale interpolation type
     * 
     * @param nodeModel The node model
     * @param translationInterpolation The translation interpolation
     * @param rotationInterpolation The rotation interpolation
     * @param scaleInterpolation The scale interpolation
     * @return The channel builder
     */
    public <T extends Handle, R extends Handle, S extends Handle>
        ChannelBuilder<T, R, S> beginChannel(NodeModel nodeModel,
            TranslationInterpolation<T> translationInterpolation,
            RotationInterpolation<R> rotationInterpolation,
            ScaleInterpolation<S> scaleInterpolation)
    {
        Objects.requireNonNull(nodeModel, "The nodeModel may not be null");
        Objects.requireNonNull(translationInterpolation,
            "The translationInterpolation may not be null");
        Objects.requireNonNull(rotationInterpolation,
            "The rotationInterpolation may not be null");
        Objects.requireNonNull(scaleInterpolation,
            "The scaleInterpolation may not be null");

        T translations = translationInterpolation.create(nodeModel);
        R rotations = rotationInterpolation.create(nodeModel);
        S scales = scaleInterpolation.create(nodeModel);
        return new ChannelBuilder<T, R, S>(this, animationModel, translations,
            rotations, scales);
    }

    /**
     * Build the {@link DefaultAnimationModel} with the current state
     * 
     * @return The {@link DefaultAnimationModel}
     */
    public DefaultAnimationModel build()
    {
        DefaultAnimationModel result = animationModel;
        if (animationModel.getChannels().isEmpty())
        {
            throw new IllegalStateException("No channels have been created. "
                + "Use 'animationBuilder.beginChannel(...)' to create a "
                + "channel builder, and 'channelBuilder.end()' to "
                + "end the channel");
        }
        animationModel = new DefaultAnimationModel();
        return result;
    }

    /**
     * Convert the given collection into a float buffer.
     * 
     * The given list may not contain <code>null</code> elements.
     * 
     * @param collection The collection
     * @return The float buffer
     */
    private static FloatBuffer floats(Collection<? extends Number> collection)
    {
        FloatBuffer buffer = FloatBuffer.allocate(collection.size());
        int index = 0;
        for (Number n : collection)
        {
            buffer.put(index, n.floatValue());
            index++;
        }
        return buffer;
    }

    /**
     * Flatten the given collection of arrays into a float buffer.
     * 
     * @param values The values
     * @param valueLength The length of each value
     * @return The buffer
     */
    private static FloatBuffer flatten1D(Collection<double[]> values,
        int valueLength)
    {
        int totalSize = values.size() * valueLength;
        FloatBuffer buffer = FloatBuffer.allocate(totalSize);
        for (double v[] : values)
        {
            for (int i = 0; i < v.length; i++)
            {
                buffer.put((float) v[i]);
            }
        }
        Buffers.position(buffer, 0);
        return buffer;
    }

    /**
     * Flatten the given collection of arrays into a float buffer.
     * 
     * @param values The values
     * @param valueLength The length of each value
     * @return The buffer
     */
    private static FloatBuffer flatten2D(Collection<double[][]> values,
        int valueLength)
    {
        int totalSize = values.size() * valueLength;
        FloatBuffer buffer = FloatBuffer.allocate(totalSize);
        for (double v[][] : values)
        {
            for (int i = 0; i < v.length; i++)
            {
                for (int j = 0; j < v[i].length; j++)
                {
                    buffer.put((float) v[i][j]);
                }
            }
        }
        Buffers.position(buffer, 0);
        return buffer;
    }

}
