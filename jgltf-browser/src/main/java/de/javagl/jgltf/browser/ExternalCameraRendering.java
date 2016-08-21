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
package de.javagl.jgltf.browser;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

import javax.vecmath.Matrix4f;

import de.javagl.jgltf.viewer.ExternalCamera;
import de.javagl.rendering.core.view.Camera;
import de.javagl.rendering.core.view.CameraListener;
import de.javagl.rendering.core.view.CameraUtils;
import de.javagl.rendering.core.view.Rectangles;
import de.javagl.rendering.core.view.View;
import de.javagl.rendering.core.view.Views;
import de.javagl.rendering.interaction.Control;
import de.javagl.rendering.interaction.camera.CameraControls;

/**
 * Implementation of an {@link ExternalCamera}, based on the classes
 * from the https://github.com/javagl/Rendering library
 */
class ExternalCameraRendering implements ExternalCamera
{
    /**
     * The {@link View}
     */
    private final View view;
    
    /**
     * The view matrix
     */
    private final float viewMatrix[];
    
    /**
     * The projection matrix
     */
    private final float projectionMatrix[];
    
    /**
     * Create a new external camera. The viewport and aspect ratio will
     * be taken from the given component, and the default (mouse) controls
     * for interaction will be attached to the given component.
     *  
     * @param component The rendering component
     */
    public ExternalCameraRendering(Component component)
    {
        Objects.requireNonNull(component, "The component may not be null");
        
        this.view = Views.create();
        this.viewMatrix = new float[16];
        this.projectionMatrix = new float[16];
        
        component.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                int w = component.getWidth();
                int h = component.getHeight();
                view.setViewport(Rectangles.create(0, 0, w, h));
                view.setAspect((float)w/h);
            }
        });
        view.getCamera().addCameraListener(new CameraListener()
        {
            @Override
            public void cameraChanged(Camera camera)
            {
                component.repaint();
            }
        });
        Control control = CameraControls.createDefaultTrackballControl(view);
        control.attachTo(component);
    }

    @Override
    public float[] getViewMatrix()
    {
        Matrix4f m = CameraUtils.computeViewMatrix(view.getCamera());
        writeMatrixToArrayColumnMajor4f(m, viewMatrix, 0);
        return viewMatrix;
    }
    
    @Override
    public float[] getProjectionMatrix()
    {
        Matrix4f m = view.getProjectionMatrix();
        writeMatrixToArrayColumnMajor4f(m, projectionMatrix, 0);
        return projectionMatrix;
    }

    /**
     * Writes the given matrix into the given array, in column-major order.
     * Neither the matrix nor the array may be <code>null</code>. The given
     * array must have a length of at least <code>offset+16</code>.
     * 
     * @param m The matrix
     * @param a The array
     * @param offset The offset where to start writing into the array
     */
    static void writeMatrixToArrayColumnMajor4f(
        Matrix4f m, float a[], int offset)
    {
        int i = offset;
        a[i++] = m.m00;
        a[i++] = m.m10;
        a[i++] = m.m20;
        a[i++] = m.m30;
        a[i++] = m.m01;
        a[i++] = m.m11;
        a[i++] = m.m21;
        a[i++] = m.m31;
        a[i++] = m.m02;
        a[i++] = m.m12;
        a[i++] = m.m22;
        a[i++] = m.m32;
        a[i++] = m.m03;
        a[i++] = m.m13;
        a[i++] = m.m23;
        a[i++] = m.m33;
    }
    
}
