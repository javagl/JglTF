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

import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;

/**
 * Methods related to {@link SceneModel} objects
 */
public class SceneModels
{
    /**
     * Convenience function to create a {@link SceneModel} with a single
     * node with a single mesh with the given primitive.
     * 
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     * @return The {@link SceneModel}
     */
    public static DefaultSceneModel createFromMeshPrimitive(
        MeshPrimitiveModel meshPrimitiveModel) 
    {
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);
        return createFromMesh(meshModel);
    }

    /**
     * Convenience function to create a {@link SceneModel} with a single
     * node with the given mesh.
     * 
     * @param meshModel The {@link MeshModel}
     * @return The {@link SceneModel}
     */
    public static DefaultSceneModel createFromMesh(MeshModel meshModel) 
    {
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);
        return createFromNode(nodeModel);
    }

    /**
     * Convenience function to create a {@link SceneModel} with the given
     * node.
     * 
     * @param nodeModel The {@link NodeModel}
     * @return The {@link SceneModel}
     */
    public static DefaultSceneModel createFromNode(NodeModel nodeModel) 
    {
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);
        return sceneModel;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private SceneModels()
    {
        // Private constructor to prevent instantiation
    }
    
}
