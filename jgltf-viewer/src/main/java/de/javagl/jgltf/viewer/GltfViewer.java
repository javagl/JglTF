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

import java.awt.Component;

import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.model.GltfData;

/**
 * Interface describing a simple glTF viewer
 */
public interface GltfViewer
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
    Component getRenderComponent();

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

}