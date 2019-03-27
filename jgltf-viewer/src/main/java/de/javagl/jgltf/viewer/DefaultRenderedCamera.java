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

import java.util.Objects;
import java.util.function.DoubleSupplier;

import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.MathUtils;
import de.javagl.jgltf.model.NodeModel;

/**
 * Default implementation of a {@link RenderedCamera}
 */
public class DefaultRenderedCamera implements RenderedCamera
{
    /**
     * The {@link NodeModel} that the view matrix will be computed from
     */
    private final NodeModel nodeModel;
    
    /**
     * The {@link CameraModel} that will be used to compute the projection
     * matrix
     */
    private final CameraModel cameraModel;
    
    /**
     * The view matrix
     */
    private final float viewMatrix[];
    
    /**
     * The projection matrix
     */
    private final float projectionMatrix[];
    
    /**
     * An optional supplier for the aspect ratio. If this is <code>null</code>, 
     * then the aspect ratio of the camera will be used     
     */
    private final DoubleSupplier aspectRatioSupplier;
    
    /**
     * A human-readable string, name identifying this camera
     */
    private final String name;
    
    /**
     * Default constructor
     * 
     * @param name The name
     * @param nodeModel The {@link NodeModel}
     * @param cameraModel The {@link CameraModel}
     * @param aspectRatioSupplier The optional supplier for the aspect ratio
     */
    DefaultRenderedCamera(String name,
        NodeModel nodeModel, CameraModel cameraModel,
        DoubleSupplier aspectRatioSupplier)
    {
        this.name = name;
        this.nodeModel = Objects.requireNonNull(
            nodeModel, "The nodeModel may not be null");
        this.cameraModel = Objects.requireNonNull(
            cameraModel, "The cameraModel may not be null");
        this.aspectRatioSupplier = aspectRatioSupplier;
        
        this.viewMatrix = new float[16];
        this.projectionMatrix = new float[16];
    }

    @Override
    public float[] getViewMatrix()
    {
        nodeModel.computeGlobalTransform(viewMatrix);
        MathUtils.invert4x4(viewMatrix, viewMatrix);
        return viewMatrix;
    }

    @Override
    public float[] getProjectionMatrix()
    {
        Float aspectRatio = null;
        if (aspectRatioSupplier != null)
        {
            aspectRatio = (float)aspectRatioSupplier.getAsDouble();
        }
        cameraModel.computeProjectionMatrix(projectionMatrix, aspectRatio);
        return projectionMatrix;
    }
    
    @Override
    public String toString()
    {
        return name;
    }

}

