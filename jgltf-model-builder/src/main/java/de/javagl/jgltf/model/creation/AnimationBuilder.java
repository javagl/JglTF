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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.MathUtils;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultChannel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultSampler;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class for building {@link AnimationModel} instances.<br>
 * <br>
 * Usually the process for building an animation will be as follows:
 * 
 * <ul>
 * <li>Create an animation builder instance with
 * {@link AnimationBuilder#create()}</li>
 * <li>Create a channel builder with
 * {@link AnimationBuilder#beginChannel(NodeModel)}.</li>
 * <li>Begin adding channels with one of the <code>begin...</code> methods, e.g.
 * {@link ChannelBuilder#beginLinearTranslation()}.</li>
 * <li>Add key frames using the <code>add...</code> methods, e.g.
 * {@link LinearTranslationBuilder#add(double, double, double, double)}</li>
 * <li>End the channel with {@link ChannelBuilder#endChannel()}.</li>
 * <li>Build the animation model with {@link AnimationBuilder#build()}.</li>
 * </ul>
 * 
 * A complete example for building a linear translation animation is shown here:
 * 
 * <pre>
 * <code>
 * AnimationBuilder ab = AnimationBuilder.create();
 * ChannelBuilder cb = ab.beginChannel(nodeModel);
 * LinearTranslationBuilder tb = cb.beginLinearTranslation();
 * tb.add(1.0, 0.1, 0.2, 0.3);
 * tb.add(2.0, 0.2, 0.3, 0.5);
 * tb.add(3.0, 0.3, 0.4, 0.6);
 * cb.endChannel();
 * AnimationModel a = ab.build();
 * </code>
 * </pre>
 */
public class AnimationBuilder
{
    // =========================================================================
    // The public classes for building channels.
    //
    // These are strictly typed, to only allow the specialized methods e.g.
    // for adding 3 key frame values to a "Linear Translation", or the
    // 3x4 key frame values for a "Cubic Spline Rotation", including the
    // convenience methods that use the "axis-angle" representation for the
    // rotations.

    /**
     * Class for building translation animation channels with linear
     * interpolation
     */
    public static class LinearTranslationBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private LinearTranslationBuilder()
        {
            super(Interpolation.LINEAR, 3);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         */
        public void add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
        }
    }

    /**
     * Class for building rotation animation channels with linear interpolation
     */
    public static class LinearRotationBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private LinearRotationBuilder()
        {
            super(Interpolation.LINEAR, 4);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param qx The x-component of the quaternion
         * @param qy The y-component of the quaternion
         * @param qz The z-component of the quaternion
         * @param qw The w-component of the quaternion
         */
        public void add(double time, double qx, double qy, double qz, double qw)
        {
            addInternal(time, new double[]
            { qx, qy, qz, qw });
        }

        /**
         * Add the given key frame at the given time, using the axis-angle
         * representation.
         * 
         * @param time The time
         * @param ax The x-component of the axis
         * @param ay The y-component of the axis
         * @param az The z-component of the axis
         * @param angleRad The angle, in radians
         */
        public void addAxisAngle(double time, double ax, double ay, double az,
            double angleRad)
        {
            double q[] = new double[4];
            MathUtils.axisAngleRadToQuaternion(ax, ay, az, angleRad, q);
            addInternal(time, q);
        }
    }

    /**
     * Class for building scale animation channels with linear interpolation
     */
    public static class LinearScaleBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private LinearScaleBuilder()
        {
            super(Interpolation.LINEAR, 3);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         */
        public void add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
        }
    }

    /**
     * Class for building weight animation channels with linear interpolation
     */
    public static class LinearWeightsBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private LinearWeightsBuilder()
        {
            super(Interpolation.LINEAR, 1);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param weights The weights
         */
        public void add(double time, double... weights)
        {
            addInternal(time, weights.clone());
        }
    }

    /**
     * Class for building translation animation channels with step interpolation
     */
    public static class StepTranslationBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private StepTranslationBuilder()
        {
            super(Interpolation.STEP, 3);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         */
        public void add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
        }
    }

    /**
     * Class for building rotation animation channels with step interpolation
     */
    public static class StepRotationBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private StepRotationBuilder()
        {
            super(Interpolation.STEP, 4);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param qx The x-component of the quaternion
         * @param qy The y-component of the quaternion
         * @param qz The z-component of the quaternion
         * @param qw The w-component of the quaternion
         */
        public void add(double time, double qx, double qy, double qz, double qw)
        {
            addInternal(time, new double[]
            { qx, qy, qz, qw });
        }

        /**
         * Add the given key frame at the given time, using the axis-angle
         * representation.
         * 
         * @param time The time
         * @param ax The x-component of the axis
         * @param ay The y-component of the axis
         * @param az The z-component of the axis
         * @param angleRad The angle, in radians
         */
        public void addAxisAngle(double time, double ax, double ay, double az,
            double angleRad)
        {
            double q[] = new double[4];
            MathUtils.axisAngleRadToQuaternion(ax, ay, az, angleRad, q);
            addInternal(time, q);
        }
    }

    /**
     * Class for building scale animation channels with step interpolation
     */
    public static class StepScaleBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private StepScaleBuilder()
        {
            super(Interpolation.STEP, 3);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param x The x-component
         * @param y The y-component
         * @param z The z-component
         */
        public void add(double time, double x, double y, double z)
        {
            addInternal(time, new double[]
            { x, y, z });
        }
    }

    /**
     * Class for building weight animation channels with step interpolation
     */
    public static class StepWeightsBuilder
        extends DefaultLagrangianInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private StepWeightsBuilder()
        {
            super(Interpolation.STEP, 1);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param weights The weights
         */
        public void add(double time, double... weights)
        {
            addInternal(time, weights.clone());
        }
    }

    /**
     * Class for building translation animation channels with cubic spline
     * interpolation
     */
    public static class CubicSplineTranslationBuilder
        extends DefaultHermiteInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private CubicSplineTranslationBuilder()
        {
            super(Interpolation.CUBICSPLINE, 3);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param ix The x-component of the in-tangent
         * @param iy The y-component of the in-tangent
         * @param iz The z-component of the in-tangent
         * @param kx The x-component of the key frame
         * @param ky The y-component of the key frame
         * @param kz The z-component of the key frame
         * @param ox The x-component of the out-tangent
         * @param oy The y-component of the out-tangent
         * @param oz The z-component of the out-tangent
         */
        public void add(double time, double ix, double iy, double iz, double kx,
            double ky, double kz, double ox, double oy, double oz)
        {
            double i[] =
            { ix, iy, iz };
            double k[] =
            { kx, ky, kz };
            double o[] =
            { ox, oy, oz };
            double values[][] =
            { i, k, o };
            addInternal(time, values);
        }
    }

    /**
     * Class for building rotation animation channels with cubic spline
     * interpolation
     */
    public static class CubicSplineRotationBuilder
        extends DefaultHermiteInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private CubicSplineRotationBuilder()
        {
            super(Interpolation.CUBICSPLINE, 4);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param iqx The x-component of the in-tangent quaternion
         * @param iqy The y-component of the in-tangent quaternion
         * @param iqz The z-component of the in-tangent quaternion
         * @param iqw The w-component of the in-tangent quaternion
         * @param kqx The x-component of the key frame quaternion
         * @param kqy The y-component of the key frame quaternion
         * @param kqz The z-component of the key frame quaternion
         * @param kqw The w-component of the key frame quaternion
         * @param oqx The x-component of the out-tangent quaternion
         * @param oqy The y-component of the out-tangent quaternion
         * @param oqz The z-component of the out-tangent quaternion
         * @param oqw The w-component of the out-tangent quaternion
         */
        public void add(double time, double iqx, double iqy, double iqz,
            double iqw, double kqx, double kqy, double kqz, double kqw,
            double oqx, double oqy, double oqz, double oqw)
        {
            double i[] =
            { iqx, iqy, iqz, iqw };
            double k[] =
            { kqx, kqy, kqz, kqw };
            double o[] =
            { oqx, oqy, oqz, oqw };
            double values[][] =
            { i, k, o };
            addInternal(time, values);
        }

        /**
         * Add the given key frame at the given time, using the axis-angle
         * representation
         * 
         * @param time The time
         * @param iax The x-component of the axis of the in-tangent
         * @param iay The y-component of the axis of the in-tangent
         * @param iaz The z-component of the axis of the in-tangent
         * @param iAngleRad The angle (in radians) of the in-tangent
         * @param kax The x-component of the axis of the key frame
         * @param kay The y-component of the axis of the key frame
         * @param kaz The z-component of the axis of the key frame
         * @param kAngleRad The angle (in radians) of the key frame
         * @param oax The x-component of the axis of the out-tangent
         * @param oay The y-component of the axis of the out-tangent
         * @param oaz The z-component of the axis of the out-tangent
         * @param oAngleRad The angle (in radians) of the out-tangent
         */
        public void addAxisAngle(double time, double iax, double iay,
            double iaz, double iAngleRad, double kax, double kay, double kaz,
            double kAngleRad, double oax, double oay, double oaz,
            double oAngleRad)
        {

            double iq[] = new double[4];
            double kq[] = new double[4];
            double oq[] = new double[4];
            MathUtils.axisAngleRadToQuaternion(iax, iay, iaz, iAngleRad, iq);
            MathUtils.axisAngleRadToQuaternion(kax, kay, kaz, kAngleRad, kq);
            MathUtils.axisAngleRadToQuaternion(oax, oay, oaz, oAngleRad, oq);
            double values[][] =
            { iq, kq, oq };
            addInternal(time, values);
        }
    }

    /**
     * Interface for building scale animation channels with cubic spline
     * interpolation
     */
    public static class CubicSplineScaleBuilder
        extends DefaultHermiteInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private CubicSplineScaleBuilder()
        {
            super(Interpolation.CUBICSPLINE, 3);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param ix The x-component of the in-tangent
         * @param iy The y-component of the in-tangent
         * @param iz The z-component of the in-tangent
         * @param kx The x-component of the key frame
         * @param ky The y-component of the key frame
         * @param kz The z-component of the key frame
         * @param ox The x-component of the out-tangent
         * @param oy The y-component of the out-tangent
         * @param oz The z-component of the out-tangent
         */
        public void add(double time, double ix, double iy, double iz, double kx,
            double ky, double kz, double ox, double oy, double oz)
        {

            double i[] =
            { ix, iy, iz };
            double k[] =
            { kx, ky, kz };
            double o[] =
            { ox, oy, oz };
            double values[][] =
            { i, k, o };
            addInternal(time, values);
        }
    }

    /**
     * Class for building weight animation channels with cubic spline
     * interpolation
     */
    public static class CubicSplineWeightsBuilder
        extends DefaultHermiteInterpolationBuilder
    {
        /**
         * Default constructor
         */
        private CubicSplineWeightsBuilder()
        {
            super(Interpolation.CUBICSPLINE, 1);
        }

        /**
         * Add the given key frame at the given time
         * 
         * @param time The time
         * @param i The weights for the in-tangent
         * @param k The weights for the key frame
         * @param o The weights for the out-tangent
         */
        public void add(double time, double i[], double k[], double o[])
        {
            double values[][] =
            { i.clone(), k.clone(), o.clone() };
            addInternal(time, values);
        }
    }

    /**
     * Interface for interpolation builders
     */
    private static interface InterpolationBuilder
    {
        /**
         * Return the interpolation method that should be used
         * 
         * @return The interpolation method
         */
        Interpolation getInterpolation();

        /**
         * Returns the number of components per element.
         * 
         * This is 3 for translation and scale, 4 for rotation, and 1 for
         * weights.
         * 
         * @return The number of components
         */
        int getNumComponents();

        /**
         * Returns a set represents the key frame times.
         * 
         * This set will contain the elements in ascending order. It may be
         * unmodifiable, and may not be modified.
         * 
         * @return The times
         */
        Set<Double> getTimes();

        /**
         * Returns a set of values that correspond to the key frame times.
         * 
         * This set will be "flat", regardless of the interpolation method. For
         * <code>n</code> key frame times, it will contain <code>n*n</code>
         * elements, where <code>m</code> is the number of components per
         * element (times three, if the interpolation method is CUBICSPLINE).
         * 
         * @return The flat values
         */
        List<Double> createFlatValues();
    }

    /**
     * A class for building animation channels
     */
    public static class ChannelBuilder
    {
        /**
         * The animation model that is currently being built
         */
        private final DefaultAnimationModel animationModel;

        /**
         * The node model that the channel refers to
         */
        private final NodeModel nodeModel;

        /**
         * A lookup from key frame times to accessor models
         */
        private final Map<Set<Double>, AccessorModel> timesAccessorModels;

        /**
         * The current set of animation paths that have already been created or
         * added (i.e. "translation", "rotation", "scale", and "weights")
         */
        private final Set<String> currentPaths;

        /**
         * Internal builder for the "translation" path
         */
        private InterpolationBuilder translationBuilder;

        /**
         * Internal builder for the "rotation" path
         */
        private InterpolationBuilder rotationBuilder;

        /**
         * Internal builder for the "scale" path
         */
        private InterpolationBuilder scaleBuilder;

        /**
         * Internal builder for the "weights" path
         */
        private InterpolationBuilder weightsBuilder;

        /**
         * Create a new instance
         * 
         * @param animationModel The animation model that is being built
         * @param nodeModel The node model that the channel refers to
         */
        private ChannelBuilder(DefaultAnimationModel animationModel,
            NodeModel nodeModel)
        {
            Objects.requireNonNull(animationModel,
                "The animationModel may not be null");
            Objects.requireNonNull(nodeModel, "The nodeModel may not be null");

            this.animationModel = animationModel;
            this.nodeModel = nodeModel;
            this.timesAccessorModels =
                new LinkedHashMap<Set<Double>, AccessorModel>();
            this.currentPaths = new LinkedHashSet<String>();
        }

        /**
         * Returns a builder for a linear translation channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a translation
         *         channel
         */
        public LinearTranslationBuilder beginLinearTranslation()
        {
            activatePath("translation");
            LinearTranslationBuilder result = new LinearTranslationBuilder();
            this.translationBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a linear rotation channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a rotation channel
         */
        public LinearRotationBuilder beginLinearRotation()
        {
            activatePath("rotation");
            LinearRotationBuilder result = new LinearRotationBuilder();
            this.rotationBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a linear scale channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a scale channel
         */
        public LinearScaleBuilder beginLinearScale()
        {
            activatePath("scale");
            LinearScaleBuilder result = new LinearScaleBuilder();
            this.scaleBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a linear weights channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a weights channel
         */
        public LinearWeightsBuilder beginLinearWeights()
        {
            activatePath("weights");
            LinearWeightsBuilder result = new LinearWeightsBuilder();
            this.weightsBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a step translation channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a translation
         *         channel
         */
        public StepTranslationBuilder beginStepTranslation()
        {
            activatePath("translation");
            StepTranslationBuilder result = new StepTranslationBuilder();
            this.translationBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a step rotation channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a rotation channel
         */
        public StepRotationBuilder beginStepRotation()
        {
            activatePath("rotation");
            StepRotationBuilder result = new StepRotationBuilder();
            this.rotationBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a step scale channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a scale channel
         */
        public StepScaleBuilder beginStepScale()
        {
            activatePath("scale");
            StepScaleBuilder result = new StepScaleBuilder();
            this.scaleBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a step weights channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a weights channel
         */
        public StepWeightsBuilder beginStepWeights()
        {
            activatePath("weights");
            StepWeightsBuilder result = new StepWeightsBuilder();
            this.weightsBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a cubic spline translation channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a translation
         *         channel
         */
        public CubicSplineTranslationBuilder beginCubicSplineTranslation()
        {
            activatePath("translation");
            CubicSplineTranslationBuilder result =
                new CubicSplineTranslationBuilder();
            this.translationBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a cubic spline rotation channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a rotation channel
         */
        public CubicSplineRotationBuilder beginCubicSplineRotation()
        {
            activatePath("rotation");
            CubicSplineRotationBuilder result =
                new CubicSplineRotationBuilder();
            this.rotationBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a cubic spline scale channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a scale channel
         */
        public CubicSplineScaleBuilder beginCubicSplineScale()
        {
            activatePath("scale");
            CubicSplineScaleBuilder result = new CubicSplineScaleBuilder();
            this.scaleBuilder = result;
            return result;
        }

        /**
         * Returns a builder for a cubic spline weights channel
         * 
         * @return The builder
         * @throws IllegalStateException If there already is a weights channel
         */
        public CubicSplineWeightsBuilder beginCubicSplineWeights()
        {
            activatePath("weights");
            CubicSplineWeightsBuilder result = new CubicSplineWeightsBuilder();
            this.weightsBuilder = result;
            return result;
        }

        /**
         * Manually add a channel to this builder.<br>
         * <br>
         * The caller is responsible for the consistency of the given data.<br>
         * <br>
         * This mainly means that the given key frame values must match the
         * given path and interpolation method. For example, for a linear
         * translation animation, and <code>n</code> key frame times, the values
         * must contain <code>n*3</code> elements. For a cubic spline rotation
         * animation, the values must contain <code>n*(3*4)</code> elements.
         * 
         * @param path The path, "translation", "rotation", "scale", or
         *        "weights"
         * @param interpolation The {@link Interpolation} method
         * @param times The key frame times
         * @param values The (flat) key frame values.
         * @throws IllegalStateException If the channel already has the given
         *         path
         * @throws IllegalArgumentException If the given path is not
         *         "translation", "rotation", "scale", or "weights"
         * @throws IllegalArgumentException If the given data does not have the
         *         expected layout (as defined above)
         */
        public void add(String path, Interpolation interpolation,
            Collection<? extends Number> times,
            Collection<? extends Number> values)
        {
            FloatBuffer timesBuffer = AnimationBuilder.floats(times);
            FloatBuffer valuesBuffer = AnimationBuilder.floats(values);
            add(path, interpolation, timesBuffer, valuesBuffer);
        }

        /**
         * Manually add a channel to this builder.<br>
         * <br>
         * The caller is responsible for the consistency of the given data.<br>
         * <br>
         * This mainly means that the given key frame values must match the
         * given path and interpolation method. For example, for a linear
         * translation animation, and <code>n</code> key frame times, the values
         * must contain <code>n*3</code> elements. For a cubic spline rotation
         * animation, the values must contain <code>n*(3*4)</code> elements.
         * 
         * @param path The path, "translation", "rotation", "scale", or
         *        "weights"
         * @param interpolation The {@link Interpolation} method
         * @param times The key frame times
         * @param values The (flat) key frame values.
         * @throws IllegalStateException If the channel already has the given
         *         path
         * @throws IllegalArgumentException If the given path is not
         *         "translation", "rotation", "scale", or "weights"
         * @throws IllegalArgumentException If the given data does not have the
         *         expected layout (as defined above)
         */
        public void add(String path, Interpolation interpolation,
            FloatBuffer times, FloatBuffer values)
        {
            Objects.requireNonNull(path, "The path may not be null");
            Objects.requireNonNull(interpolation,
                "The interpolation may not be null");
            activatePath(path);

            int numTimes = times.capacity();
            int numValues = values.capacity();
            validateLayout(path, interpolation, numTimes, numValues);

            int numComponents = computeNumComponents(path);
            AccessorModel timesAccessorModel =
                AccessorModels.createFloatScalar(times);
            AccessorModel valuesAccessorModel =
                createAccessorModel(values, numComponents);

            addChannel(path, interpolation, timesAccessorModel,
                valuesAccessorModel);
        }

        /**
         * Activate the given path.<br>
         * <br>
         * This is called from the respective "begin..." methods, or when a path
         * is added manually. It will ensure that the given path is valid, and
         * that it was NOT already added
         * 
         * @param path The path
         * @throws IllegalArgumentException If the path is invalid
         * @throws IllegalStateException If the path is already active
         */
        private void activatePath(String path)
        {
            List<String> validPaths =
                Arrays.asList("translation", "rotation", "scale", "weights");
            if (!validPaths.contains(path))
            {
                throw new IllegalArgumentException(
                    "The path must be " + validPaths + ", but is " + path);
            }
            if (currentPaths.contains(path))
            {
                throw new IllegalStateException(
                    "The channel already has a '" + path + "' path");
            }
            currentPaths.add(path);
        }

        /**
         * Add a channel with the given data to the animation model
         * 
         * @param path The path
         * @param interpolation The interpolation method
         * @param timesAccessorModel The accessor model for the times
         * @param valuesAccessorModel The accessor model for the values
         */
        private void addChannel(String path, Interpolation interpolation,
            AccessorModel timesAccessorModel, AccessorModel valuesAccessorModel)
        {
            Sampler sampler = new DefaultSampler(timesAccessorModel,
                interpolation, valuesAccessorModel);

            DefaultChannel channel =
                new DefaultChannel(sampler, nodeModel, path);
            animationModel.addChannel(channel);
        }

        /**
         * Create a channel from the given interpolation builder (if it does
         * contain key frames), and add the channel to the animation.
         * 
         * @param builder The builder
         * @param path The path
         */
        private void createChannelOptional(InterpolationBuilder builder,
            String path)
        {
            if (builder == null)
            {
                return;
            }
            Set<Double> times = builder.getTimes();
            if (times.isEmpty())
            {
                return;
            }
            AccessorModel timesAccessorModel = fetchTimesAccessorModel(times);

            List<Double> flatValues = builder.createFlatValues();
            int components = builder.getNumComponents();
            AccessorModel valuesAccessorModel =
                createAccessorModel(flatValues, components);

            addChannel(path, builder.getInterpolation(), timesAccessorModel,
                valuesAccessorModel);
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
            FloatBuffer timesBuffer = AnimationBuilder.floats(times);
            accessorModel = AccessorModels.createFloatScalar(timesBuffer);
            timesAccessorModels.put(times, accessorModel);
            return accessorModel;
        }

        /**
         * Called to indicate that the channel is finished and should be added
         * to the animation model.
         */
        public void endChannel()
        {
            int numChannelsBefore = animationModel.getChannels().size();

            createChannelOptional(translationBuilder, "translation");
            createChannelOptional(rotationBuilder, "rotation");
            createChannelOptional(scaleBuilder, "scale");
            createChannelOptional(weightsBuilder, "weights");

            int numChannelsAfter = animationModel.getChannels().size();
            if (numChannelsBefore == numChannelsAfter)
            {
                throw new IllegalStateException(
                    "No key frames have been added to the current channel.");
            }
        }
    }

    // =========================================================================
    // Private base classes for the interpolation builders

    /**
     * Default implementation of a an {@link InterpolationBuilder} for Lagrange
     * interpolations (that require only the key frame, but no tangents)
     */
    private static class DefaultLagrangianInterpolationBuilder
        implements InterpolationBuilder
    {
        /**
         * The interpolation method
         */
        private final Interpolation interpolation;

        /**
         * The number of components per element (3 for translation and scale, 4
         * for rotation, 1 for weights)
         */
        private final int components;

        /**
         * The mapping from times to key frame data
         */
        private final Map<Double, double[]> data =
            new LinkedHashMap<Double, double[]>();

        /**
         * Creates a new instance
         * 
         * @param interpolation The interpolation
         * @param components The number of components per element (3 for
         *        translation and scale, 4 for rotation, 1 for weights)
         */
        private DefaultLagrangianInterpolationBuilder(
            Interpolation interpolation, int components)
        {
            Objects.requireNonNull(interpolation,
                "The interpolation may not be null");
            this.interpolation = interpolation;
            this.components = components;
        }

        @Override
        public Interpolation getInterpolation()
        {
            return interpolation;
        }

        @Override
        public int getNumComponents()
        {
            return components;
        }

        /**
         * Internal method for for adding a key frame, to be called by
         * subclasses.
         * 
         * @param time The time
         * @param values The values
         */
        protected void addInternal(double time, double... values)
        {
            if (this.components != 1)
            {
                if (values.length != this.components)
                {
                    throw new IllegalArgumentException(
                        "The values must have a length of " + this.components
                            + ", but has a length of " + values.length);
                }
            }
            data.put(time, values);
        }

        @Override
        public Set<Double> getTimes()
        {
            return data.keySet();
        }

        @Override
        public List<Double> createFlatValues()
        {
            return AnimationBuilder.flattenList1D(data.values());
        }

    }

    /**
     * Default implementation of a an {@link InterpolationBuilder} for Hermite
     * interpolations (that require the in-tangent, key frame, and out-tangent)
     */
    private static class DefaultHermiteInterpolationBuilder
        implements InterpolationBuilder
    {
        /**
         * The interpolation method
         */
        private final Interpolation interpolation;

        /**
         * The number of components per element (3 for translation and scale, 4
         * for rotation, 1 for weights)
         */
        private final int components;

        /**
         * The mapping from times to key frame data
         */
        private final Map<Double, double[][]> data =
            new LinkedHashMap<Double, double[][]>();

        /**
         * Creates a new instance
         * 
         * @param interpolation The interpolation
         * @param components The number of components per element (3 for
         *        translation and scale, 4 for rotation, 1 for weights)
         */
        private DefaultHermiteInterpolationBuilder(Interpolation interpolation,
            int components)
        {
            Objects.requireNonNull(interpolation,
                "The interpolation may not be null");
            this.interpolation = interpolation;
            this.components = components;
        }

        @Override
        public Interpolation getInterpolation()
        {
            return interpolation;
        }

        @Override
        public int getNumComponents()
        {
            return components;
        }

        /**
         * Internal method for for adding a key frame, to be called by
         * subclasses.
         * 
         * @param time The time
         * @param values The values
         */
        protected void addInternal(double time, double values[][])
        {
            if (values.length != 3)
            {
                throw new IllegalArgumentException(
                    "The values must have a length of " + 3
                        + ", but have a length of " + values.length);
            }
            if (this.components != 1)
            {
                for (double value[] : values)
                {
                    if (value.length != components)
                    {
                        throw new IllegalArgumentException(
                            "Each of the values must have a length of "
                                + components + ", but have a length of "
                                + value.length);
                    }
                }
            }
            data.put(time, values);
        }

        @Override
        public Set<Double> getTimes()
        {
            return data.keySet();
        }

        @Override
        public List<Double> createFlatValues()
        {
            return AnimationBuilder.flattenList2D(data.values());
        }

    }

    /**
     * Creates a new instance
     * 
     * @return The animation builder
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
     * Private constructor
     */
    private AnimationBuilder()
    {
        this.animationModel = new DefaultAnimationModel();
    }

    /**
     * Begin building a new channel that refers to the given node.<br>
     * <br>
     * When the channel is finished, {@link ChannelBuilder#endChannel()} has to
     * be called.
     * 
     * @param nodeModel The node
     * @return The builder for the channel.
     */
    public ChannelBuilder beginChannel(NodeModel nodeModel)
    {
        return new ChannelBuilder(animationModel, nodeModel);
    }

    /**
     * Build the animation model containing all channels that have been created.
     * 
     * @return The animation model
     * @throws IllegalStateException If no channels have been created
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

    // =========================================================================
    // Internal utility methods

    /**
     * Create an accessor model from the given values
     * 
     * @param flatValues The values
     * @param components The number of components per element (1, 3, or 4)
     * @return The accessor model
     */
    private static AccessorModel createAccessorModel(
        Collection<? extends Number> flatValues, int components)
    {
        FloatBuffer buffer = AnimationBuilder.floats(flatValues);
        return createAccessorModel(buffer, components);
    }

    /**
     * Create an accessor model from the given values
     * 
     * @param buffer The values
     * @param components The number of components per element (1, 3, or 4)
     * @return The accessor model
     */
    private static AccessorModel createAccessorModel(FloatBuffer buffer,
        int components)
    {
        if (components == 3)
        {
            return AccessorModels.createFloat3D(buffer);
        }
        else if (components == 4)
        {
            return AccessorModels.createFloat4D(buffer);
        }
        return AccessorModels.createFloatScalar(buffer);
    }

    /**
     * Returns the element type for the given path.<br>
     * <br>
     * This is VEC3 for translation and scale, VEC4 for rotation, and defaulting
     * to SCALAR (for weights and others)
     * 
     * @param path The path
     * @return The element type
     */
    private static ElementType computeElementType(String path)
    {
        if (path.equals("translation"))
        {
            return ElementType.VEC3;
        }
        if (path.equals("rotation"))
        {
            return ElementType.VEC4;
        }
        if (path.equals("scale"))
        {
            return ElementType.VEC3;
        }
        return ElementType.SCALAR;
    }

    /**
     * Returns the number of components per element for the given path.<br>
     * <br>
     * This is 3 for translation and scale, 4 for rotation, and defaulting to 1
     * (for weights and others)
     * 
     * @param path The path
     * @return The number of components
     */
    private static int computeNumComponents(String path)
    {
        ElementType elementType = computeElementType(path);
        return elementType.getNumComponents();
    }

    /**
     * Validate the layout of the given data for the given path and
     * interpolation method.<br>
     * <br>
     * For example, for a linear translation animation, and <code>n</code> key
     * frame times, the values must contain <code>n*3</code> elements. For a
     * cubic spline rotation animation, the values must contain
     * <code>n*(3*4)</code> elements.
     * 
     * @param path The path
     * @param interpolation The interpolation
     * @param numTimes The number of elements of the key frame times
     * @param numValues The total number of values
     * @throws IllegalArgumentException If the layout is not valid
     */
    private static void validateLayout(String path, Interpolation interpolation,
        int numTimes, int numValues)
    {
        int actualNumValuesPerTime = numValues / numTimes;
        int remainder = numValues - (numTimes * actualNumValuesPerTime);
        if (remainder != 0)
        {
            throw new IllegalArgumentException(
                "The number of values (" + numValues + ") for the '" + path
                    + "' path is not divisible by the number of times ("
                    + numTimes + ")");
        }
        if (path.equals("weights"))
        {
            return;
        }

        int expectedNumComponentsPerTime = computeNumComponents(path);
        int expectedNumValuesPerTime = 0;
        if (interpolation == Interpolation.LINEAR)
        {
            expectedNumValuesPerTime = expectedNumComponentsPerTime;
        }
        else if (interpolation == Interpolation.STEP)
        {
            expectedNumValuesPerTime = expectedNumComponentsPerTime;
        }
        else if (interpolation == Interpolation.CUBICSPLINE)
        {
            expectedNumValuesPerTime = 3 * expectedNumComponentsPerTime;
        }
        if (actualNumValuesPerTime != expectedNumValuesPerTime)
        {
            throw new IllegalArgumentException("The number of values for the '"
                + path + "' path with interpolation type " + interpolation
                + " must be " + expectedNumValuesPerTime + " but is "
                + actualNumValuesPerTime);
        }
    }

    /**
     * Flatten the given collection of arrays into a list
     * 
     * @param values The values
     * @return The buffer
     */
    private static List<Double> flattenList1D(Collection<double[]> values)
    {
        List<Double> result = new ArrayList<Double>();
        for (double v[] : values)
        {
            for (int i = 0; i < v.length; i++)
            {
                result.add(v[i]);
            }
        }
        return result;
    }

    /**
     * Flatten the given collection of arrays into a list
     * 
     * @param values The values
     * @return The buffer
     */
    private static List<Double> flattenList2D(Collection<double[][]> values)
    {
        List<Double> result = new ArrayList<Double>();
        for (double vs[][] : values)
        {
            for (int i = 0; i < vs.length; i++)
            {
                double v[] = vs[i];
                for (int j = 0; j < v.length; j++)
                {
                    result.add(v[j]);
                }
            }
        }
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
    static FloatBuffer flatten1D(Collection<double[]> values, int valueLength)
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
}
