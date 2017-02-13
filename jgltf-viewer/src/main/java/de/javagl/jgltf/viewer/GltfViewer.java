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

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.GltfData;

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
     * found in the {@link GlTF}.<br>
     * <br>
     * Note: This has to be set <b>before</b> any {@link GltfData} is added
     * with {@link #addGltfData(GltfData)}. Otherwise, the external camera
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
     * Add the given {@link GltfData} to this viewer. This will prepare
     * the internal data structures that are required for rendering, and 
     * trigger a new rendering pass. At the beginning of the rendering 
     * pass, the initialization of the rendering structures will be 
     * performed, on the rendering thread.<br>
     * <br>
     * If the {@link GlTF} in the given {@link GltfData} is not valid,
     * then an error message will be printed instead.
     * 
     * @param gltfData The {@link GltfData}
     */
    void addGltfData(GltfData gltfData);

    /**
     * Remove the given {@link GltfData} from this viewer. This will trigger
     * a new rendering pass, and at the beginning of the rendering pass, the
     * internal data structures will be deleted.
     * 
     * @param gltfData The {@link GltfData} to remove
     */
    void removeGltfData(GltfData gltfData);

    /**
     * Returns an unmodifiable list containing all {@link CameraModel}
     * instances that are created from the given {@link GltfData}. If
     * the given {@link GltfData} is <code>null</code>, then all
     * {@link CameraModel} instances will be returned.
     * 
     * @param gltfData The optional {@link GltfData}
     * @return The {@link CameraModel} instances
     * @throws IllegalArgumentException If the given {@link GltfData} is
     * not <code>null</code> and not contained in this viewer
     */
    List<CameraModel> getCameraModels(GltfData gltfData);
    
    /**
     * Set {@link CameraModel} that should be used for rendering the 
     * given {@link GltfData}. If the given {@link GltfData} is 
     * <code>null</code>, then the {@link CameraModel} will be used
     * for rendering all {@link GltfData} instances.
     * If the {@link CameraModel} is <code>null</code>, then the external 
     * camera will be used. See {@link #setExternalCamera(ExternalCamera)}.
     * 
     * @param gltfData The optional {@link GltfData}
     * @param cameraModel The {@link CameraModel}
     * @throws IllegalArgumentException If the given {@link GltfData} is
     * not <code>null</code> and not contained in this viewer
     */
    void setCurrentCameraModel(GltfData gltfData, CameraModel cameraModel);
}
