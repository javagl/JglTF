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
package de.javagl.jgltf.model;

import de.javagl.jgltf.impl.v1.Camera;
import de.javagl.jgltf.impl.v1.Node;

/**
 * An instance of a {@link Camera} that is attached to a {@link Node}.
 */
public final class CameraModel
{
    /**
     * The {@link Camera} of this instance
     */
    private final Camera camera;
    
    /**
     * The {@link NodeModel} of the {@link Node} that the {@link Camera} 
     * is attached to 
     */
    private final NodeModel nodeModel;
    
    /**
     * Creates a new instance for the given {@link Camera}, attached to
     * the given {@link NodeModel}
     * 
     * @param camera The {@link Camera}
     * @param nodeModel The {@link NodeModel} that the camera is attached to 
     */
    CameraModel(Camera camera, NodeModel nodeModel)
    {
        this.camera = camera;
        this.nodeModel = nodeModel;
    }
    
    /**
     * Returns the {@link Camera} of this instance
     * 
     * @return The {@link Camera}
     */
    public Camera getCamera()
    {
        return camera;
    }
    
    /**
     * Returns the {@link NodeModel} that the {@link Camera} is attached to
     * 
     * @return The {@link NodeModel}
     */
    public NodeModel getNodeModel()
    {
        return nodeModel;
    }
    
    /**
     * Compute the view matrix for this camera. This is the inverse of the
     * global transform of the {@link Node} that the camera is attached to.<br>
     * <br>
     * The result will be written to the given array, as a 4x4 matrix in 
     * column major order. If the given array is <code>null</code>, then
     * a new array with length 16 will be created and returned. Otherwise,
     * the given array must at least have a length of 16.
     * 
     * @param result The result array
     * @return The result array
     */
    float[] computeViewMatrix(float result[])
    {
        float localResult[] = Utils.validate(result, 16);
        nodeModel.computeGlobalTransform(localResult);
        MathUtils.invert4x4(localResult, localResult);
        return localResult;
    }
    
    /**
     * Compute the projection matrix for this camera.<br>
     * <br>
     * The result will be written to the given array, as a 4x4 matrix in 
     * column major order. If the given array is <code>null</code>, then
     * a new array with length 16 will be created and returned. Otherwise,
     * the given array must at least have a length of 16.
     * 
     * @param result The result array
     * @param aspectRatio An optional aspect ratio that should be used. 
     * If this is <code>null</code>, then the aspect ratio of the 
     * camera will be used.
     * @return The result array
     */
    float[] computeProjectionMatrix(float result[], Float aspectRatio)
    {
        float localResult[] = Utils.validate(result, 16);
        Cameras.computeProjectionMatrix(
            camera, aspectRatio, localResult);
        return localResult;
    }
    
}
