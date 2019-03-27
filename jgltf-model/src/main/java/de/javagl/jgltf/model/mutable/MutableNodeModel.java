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
package de.javagl.jgltf.model.mutable;

import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;

/**
 * Interface for a {@link NodeModel} that can be modified
 */
public interface MutableNodeModel extends NodeModel
{
    /**
     * Set the parent of this node
     * 
     * @param parent The parent
     */
    void setParent(MutableNodeModel parent);
    
    /**
     * Add the given child to this node
     * 
     * @param child The child
     */
    void addChild(MutableNodeModel child);
    
    /**
     * Add the given {@link MeshModel} to this node
     * 
     * @param meshModel The {@link MeshModel}
     */
    void addMeshModel(MeshModel meshModel);
    
    /**
     * Set the {@link SkinModel}
     * 
     * @param skinModel The {@link SkinModel}
     */
    void setSkinModel(SkinModel skinModel);
    
    /**
     * Set the {@link CameraModel}
     * 
     * @param cameraModel The {@link CameraModel}
     */
    void setCameraModel(CameraModel cameraModel);
    
}
