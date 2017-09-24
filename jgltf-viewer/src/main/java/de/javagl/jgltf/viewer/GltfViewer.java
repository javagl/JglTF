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

import java.util.List;

import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.GltfModel;

/**
 * Interface describing a simple glTF viewer
 * 
 * @param <C> The type of the rendering component of this viewer
 */
public interface GltfViewer<C>
{
    /**
     * Set an optional {@link ExternalCamera}. <br>
     * <br>
     * If the given {@link ExternalCamera} is not <code>null</code>, then it 
     * may supply a view- and projection matrix from an external camera that 
     * will be used as an alternative to the camera information that is 
     * found in the {@link GltfModel}.<br>
     * <br>
     * Note: This has to be set <b>before</b> any {@link GltfModel} is added
     * with {@link #addGltfModel(GltfModel)}. Otherwise, the external camera
     * will not affect the rendered glTF.
     * 
     * @param externalCamera The optional {@link ExternalCamera}
     */
    void setExternalCamera(ExternalCamera externalCamera);

    /**
     * Set whether the animations are running
     * 
     * @param running Whether the animations are running
     */
    void setAnimationsRunning(boolean running);

    /**
     * Returns the rendering component of this viewer
     * 
     * @return The rendering component of this viewer
     */
    C getRenderComponent();
    
    /**
     * Returns the width of the render component
     * 
     * @return The width of the render component
     */
    int getWidth();
    
    /**
     * Returns the height of the render component
     * 
     * @return The height of the render component
     */
    int getHeight();

    /**
     * Trigger a rendering pass
     */
    void triggerRendering();

    /**
     * Add the given {@link GltfModel} to this viewer. This will prepare
     * the internal data structures that are required for rendering, and 
     * trigger a new rendering pass. At the beginning of the rendering 
     * pass, the initialization of the rendering structures will be 
     * performed, on the rendering thread.<br>
     * 
     * @param gltfModel The {@link GltfModel}
     */
    void addGltfModel(GltfModel gltfModel);

    /**
     * Remove the given {@link GltfModel} from this viewer. This will trigger
     * a new rendering pass, and at the beginning of the rendering pass, the
     * internal data structures will be deleted.
     * 
     * @param gltfModel The {@link GltfModel} to remove
     */
    void removeGltfModel(GltfModel gltfModel);

    /**
     * Returns an unmodifiable list containing all {@link CameraModel}
     * instances that are contained in the {@link GltfModel} instances
     * that have been added to this viewer.
     * 
     * @return The {@link CameraModel} instances
     */
    List<CameraModel> getCameraModels();
    
    /**
     * Set {@link CameraModel} that should be used for rendering the 
     * given {@link GltfModel}. If the given {@link GltfModel} is 
     * <code>null</code>, then the {@link CameraModel} will be used
     * for rendering all {@link GltfModel} instances.
     * If the {@link CameraModel} is <code>null</code>, then the external 
     * camera will be used. See {@link #setExternalCamera(ExternalCamera)}.
     * 
     * @param gltfModel The optional {@link GltfModel}
     * @param cameraModel The {@link CameraModel}
     * @throws IllegalArgumentException If the given {@link GltfModel} is
     * not <code>null</code> and not contained in this viewer
     */
    void setCurrentCameraModel(GltfModel gltfModel, CameraModel cameraModel);
}
