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
import java.util.function.Supplier;

import de.javagl.jgltf.model.MathUtils;

/**
 * The view configuration for a single {@link RenderedGltfModel}. It allows
 * setting the {@link #setRenderedCamera(RenderedCamera) current camera}
 * that should be used for rendering, and offers the viewport, view matrix
 * and projection matrix. 
 */
final class ViewConfiguration
{
    /**
     * The {@link RenderedCamera} that should be used for rendering.  
     */
    private RenderedCamera renderedCamera;

    /**
     * The supplier for the viewport, as an array [x, y, width, height]
     */
    private final Supplier<float[]> viewportSupplier;
    
    /**
     * The supplier for the view matrix
     */
    private final Supplier<float[]> viewMatrixSupplier;
    
    /**
     * The supplier for the projection matrix
     */
    private final Supplier<float[]> projectionMatrixSupplier;
    
    /**
     * Creates a new view configuration
     *  
     * @param viewportSupplier A supplier that supplies the viewport, 
     * as 4 float elements, [x, y, width, height]
     */
    ViewConfiguration(
        Supplier<float[]> viewportSupplier)
    {
        this.viewportSupplier = Objects.requireNonNull(
            viewportSupplier, "The viewportSupplier may not be null");
        this.viewMatrixSupplier = 
            createViewMatrixSupplier();
        this.projectionMatrixSupplier = 
            createProjectionMatrixSupplier();
    }
    
    /**
     * Set the {@link RenderedCamera} that should be used for rendering. 
     * <br>
     * @param renderedCamera The {@link RenderedCamera} 
     */
    public void setRenderedCamera(RenderedCamera renderedCamera)
    {
        this.renderedCamera = renderedCamera;
    }
    
    /**
     * Returns the {@link RenderedCamera}
     * 
     * @return The {@link RenderedCamera}
     */
    public RenderedCamera getRenderedCamera()
    {
        return renderedCamera;
    }
    
    /**
     * Create a supplier for the view matrix, as a float array with 16 
     * elements, containing the view matrix in column-major order.<br>
     * <br>
     * The resulting supplier will supply a view matrix as follows:
     * <ul>
     *   <li> 
     *     If a non-<code>null</code> {@link #setRenderedCamera
     *     current camera} has been set, then the view matrix from
     *     this {@link RenderedCamera} will be returned
     *   </li>
     *   <li>
     *     Otherwise, an identity matrix will be returned
     *   </li>
     * </ul>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     * 
     * @return The view matrix supplier
     */
    private Supplier<float[]> createViewMatrixSupplier()
    {
        float defaultViewMatrix[] = MathUtils.createIdentity4x4();
        return () ->
        {
            if (renderedCamera == null)
            {
                MathUtils.setIdentity4x4(defaultViewMatrix);
                return defaultViewMatrix;
            }
            return renderedCamera.getViewMatrix();
        };
    }
    
    /**
     * Create a supplier for the projection matrix, as a float array with 16 
     * elements, containing the projection matrix in column-major order.<br>
     * <br>
     * The resulting supplier will supply a projection matrix as follows:
     * <ul>
     *   <li> 
     *     If a non-<code>null</code> {@link #setRenderedCamera
     *     current camera} has been set, then the projection matrix from
     *     this {@link RenderedCamera} will be returned
     *   </li>
     *   <li>
     *     Otherwise, an identity matrix will be returned
     *   </li>
     * </ul>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array.
     *  
     * @return The projection matrix supplier
     */
    private Supplier<float[]> createProjectionMatrixSupplier()
    {
        float defaultProjectionMatrix[] = MathUtils.createIdentity4x4();
        return () ->
        {
            if (renderedCamera == null)
            {
                MathUtils.setIdentity4x4(defaultProjectionMatrix);
                return defaultProjectionMatrix;
            }
            return renderedCamera.getProjectionMatrix();
        };
    }
    
    /**
     * Returns the viewport that is used for rendering, as an array
     * [x, y, width, height]
     * 
     * @return The viewport
     */
    public float[] getViewport()
    {
        return viewportSupplier.get();
    }
    
    /**
     * Returns the view matrix, as an array with 16 elements, containing 
     * the matrix in column-major order.<br>
     * <br> 
     * This method MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array.
     * 
     * @return The view matrix
     */
    public float[] getViewMatrix()
    {
        return viewMatrixSupplier.get();
    }
    
    /**
     * Returns the projection matrix, as an array with 16 elements, containing 
     * the matrix in column-major order.<br>
     * <br> 
     * This method MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array.
     * 
     * @return The view matrix
     */
    public float[] getProjectionMatrix()
    {
        return projectionMatrixSupplier.get();
    }
}
