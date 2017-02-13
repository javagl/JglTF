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
package de.javagl.jgltf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Camera;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Node;

/**
 * A class that serves as a data model for a {@link GlTF}.<br>
 */
public final class GltfModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfModel.class.getName());

    /**
     * The {@link GlTF} of this model
     */
    private final GlTF gltf;

    /**
     * The mapping from {@link Node} IDs to {@link NodeModel} instances
     */
    private final Map<String, NodeModel> nodeIdToNodeModel;
    
    /**
     * The {@link CameraModel} instances that have been created from
     * the {@link Camera} references of {@link Node} instances
     */
    private final List<CameraModel> cameraModels;
    
    /**
     * Creates a new model for the given glTF
     * 
     * @param gltf The {@link GlTF}
     */
    public GltfModel(GlTF gltf)
    {
        Objects.requireNonNull(gltf, 
            "The gltf may not be null");

        this.gltf = gltf;
        this.nodeIdToNodeModel = createNodeIdToNodeModel();
        this.cameraModels = createCameraModels();
    }

    /**
     * Compute the mapping from {@link Node} IDs to {@link NodeModel} instances
     * 
     * @return The mapping
     */
    private Map<String, NodeModel> createNodeIdToNodeModel()
    {
        Map<String, NodeModel> map = new LinkedHashMap<String, NodeModel>();
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        for (String nodeId : nodes.keySet())
        {
            Node node = nodes.get(nodeId);
            NodeModel nodeModel = new NodeModel(node);
            map.put(nodeId, nodeModel);
        }
        for (String nodeId : map.keySet())
        {
            NodeModel nodeModel = map.get(nodeId);
            Node node = nodeModel.getNode();
            List<String> childIds = Optionals.of(node.getChildren());
            for (String childId : childIds)
            {
                NodeModel child = map.get(childId);
                if (child == null)
                {
                    logger.severe("Node with ID " + childId + " not found");
                }
                else
                {
                    nodeModel.addChild(child);
                }
            }
        }
        return map;
    }
    
    /**
     * Create the {@link CameraModel} instances, based on the {@link Node} 
     * objects of the glTF that refer to a {@link Camera}.
     * 
     * @return The {@link CameraModel} instances
     */
    private List<CameraModel> createCameraModels()
    {
        List<CameraModel> list = new ArrayList<CameraModel>();
        Map<String, Node> nodes = Optionals.of(gltf.getNodes());
        Map<String, Camera> cameras = Optionals.of(gltf.getCameras());
        for (String nodeId : nodes.keySet())
        {
            Node node = nodes.get(nodeId);
            String cameraId = node.getCamera();
            if (cameraId != null)
            {
                Camera camera = cameras.get(cameraId);
                if (camera == null)
                {
                    logger.severe("Camera with ID " + cameraId + " not found");
                }
                else
                {
                    NodeModel nodeModel = nodeIdToNodeModel.get(nodeId);
                    String name = nodeId + "." + cameraId;
                    CameraModel cameraModel = 
                        new CameraModel(name, camera, nodeModel);
                    list.add(cameraModel);
                }
            }
        }
        return list;
    }

    
    /**
     * Returns an unmodifiable view on the list of {@link CameraModel} 
     * instances that have been created for the glTF.
     * 
     * @return The {@link CameraModel} instances
     */
    public List<CameraModel> getCameraModels()
    {
        return Collections.unmodifiableList(cameraModels);
    }
    
    /**
     * Returns an unmodifiable view on the list of {@link NodeModel} 
     * instances that have been created for the glTF.
     * 
     * @return The {@link NodeModel} instances
     */
    public List<NodeModel> getNodeModels()
    {
        // TODO: This should return the internal list after
        // the update to glTF 2.0 is completed
        return Collections.unmodifiableList(
            new ArrayList<NodeModel>(nodeIdToNodeModel.values()));
    }
    
    /**
     * Returns an unmodifiable view on the map from node IDs to 
     * {@link NodeModel} instances that have been created for the glTF.
     * 
     * @return The mapping from IDs to {@link NodeModel} instances
     * 
     * @deprecated This will be a list in glTF 2.0
     */
    public Map<String, NodeModel> getNodeModelsMap()
    {
        return Collections.unmodifiableMap(nodeIdToNodeModel);
    }
    
    
    
    
}
