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
package de.javagl.jgltf.model.animation;

/**
 * An {@link Interpolator} that performs a spherical linear interpolation
 * (SLERP) between quaternions, given as arrays of length 4
 */
class SlerpQuaternionInterpolator implements Interpolator
{

    @Override
    public void interpolate(double[] a, double[] b, double alpha, double[] result)
    {
        // Adapted from javax.vecmath.Quat4f
        double ax = a[0];
        double ay = a[1];
        double az = a[2];
        double aw = a[3];
        double bx = b[0];
        double by = b[1];
        double bz = b[2];
        double bw = b[3];

        double dot = ax * bx + ay * by + az * bz + aw * bw;
        if (dot < 0)
        {
            bx = -bx;
            by = -by;
            bz = -bz;
            bw = -bw;
            dot = -dot;
        }
        double epsilon = 1e-8;
        double s0, s1;
        if ((1.0 - dot) > epsilon)
        {
            double omega = Math.acos(dot);
            double invSinOmega = 1.0 / Math.sin(omega);
            s0 = Math.sin((1.0 - alpha) * omega) * invSinOmega;
            s1 = Math.sin(alpha * omega) * invSinOmega;
        } 
        else
        {
            s0 = 1.0f - alpha;
            s1 = alpha;
        }
        double rx = s0 * ax + s1 * bx;
        double ry = s0 * ay + s1 * by;
        double rz = s0 * az + s1 * bz;
        double rw = s0 * aw + s1 * bw;
        result[0] = rx;
        result[1] = ry;
        result[2] = rz;
        result[3] = rw;
    }

}
