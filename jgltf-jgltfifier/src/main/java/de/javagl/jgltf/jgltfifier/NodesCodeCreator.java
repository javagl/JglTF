/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.jgltfifier;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;

/**
 * A code creator for the nodes code
 */
class NodesCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(NodesCodeCreator.class.getName());

    /**
     * The glTF model
     */
    private final GltfModel gltfModel;

    /**
     * Creates a new instance
     * 
     * @param codeModel The code model
     * @param definedClass The defined class
     * @param gltfModel The glTF model
     */
    NodesCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<NodeModel> nodeModels = gltfModel.getNodeModels();
        if (nodeModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Nodes (" + nodeModels.size()
            + ") - no children or skin information");
        block.directStatement(" ");

        for (int i = 0; i < nodeModels.size(); i++)
        {
            block.directStatement("// Node " + i + " of " + nodeModels.size());
            NodeModel nodeModel = nodeModels.get(i);
            createNode(block, nodeModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given node, and add it to the given
     * block
     * 
     * @param block The block
     * @param nodeModel The node
     * @param nodeIndex The index of the node
     */
    private void createNode(JBlock block, NodeModel nodeModel, int nodeIndex)
    {
        JClass defaultNodeModelClass = findClass(DefaultNodeModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultNodeModelClass,
            "nodeModel" + nodeIndex);

        JMethod method = createNodeCreationMethod(nodeModel, nodeIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given node model
     * 
     * @param nodeModel The node model
     * @param nodeIndex The node index
     * @return The method
     */
    private JMethod createNodeCreationMethod(NodeModel nodeModel, int nodeIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createNodeModel" + nodeIndex);
        Comments.add(method, "Create the specified node model");

        JBlock block = method.body();
        createNodeCreationCode(block, nodeModel, nodeIndex);
        return method;
    }

    /**
     * Create the code that creates the given node model and add it to the given
     * block
     * 
     * @param block The block
     * @param nodeModel The node model
     * @param nodeIndex The node index
     */
    private void createNodeCreationCode(JBlock block, NodeModel nodeModel,
        int nodeIndex)
    {
        // Collect the required types
        JClass defaultNodeModelClass = findClass(DefaultNodeModel.class);

        // this.nodeModelX = new DefaultNodeModel()
        JFieldRef nodeVar = JExpr._this().ref("nodeModel" + nodeIndex);
        block.assign(nodeVar, JExpr._new(defaultNodeModelClass));

        // Call the required setters

        double[] translation = nodeModel.getTranslation();
        if (translation != null)
        {
            block.add(nodeVar.invoke("setTranslation")
                .arg(newDoubleArrayWith(translation)));
        }

        double[] rotation = nodeModel.getRotation();
        if (rotation != null)
        {
            block.add(nodeVar.invoke("setRotation")
                .arg(newDoubleArrayWith(rotation)));
        }

        double[] scale = nodeModel.getScale();
        if (scale != null)
        {
            block
                .add(nodeVar.invoke("setScale").arg(newDoubleArrayWith(scale)));
        }

        double[] matrix = nodeModel.getMatrix();
        if (matrix != null)
        {
            block.add(
                nodeVar.invoke("setMatrix").arg(newDoubleArrayWith(matrix)));
        }

        // Camera
        CameraModel cameraModel = nodeModel.getCameraModel();
        if (cameraModel != null)
        {
            int cameraIndex = gltfModel.getCameraModels().indexOf(cameraModel);
            if (cameraIndex == -1)
            {
                logger.severe("Could not find camera model for "
                    + " camera of node " + nodeIndex);
                return;
            }
            block.add(nodeVar.invoke("setCameraModel")
                .arg(JExpr._this().ref("cameraModel" + cameraIndex)));
        }

        // Meshes
        List<MeshModel> meshModels = nodeModel.getMeshModels();
        if (!meshModels.isEmpty())
        {
            if (meshModels.size() > 1)
            {
                // This was possible in glTF 1.0 ...
                logger.severe("Only one mesh per node is handled");
            }
            MeshModel meshModel = meshModels.get(0);
            int meshIndex = gltfModel.getMeshModels().indexOf(meshModel);
            if (meshIndex == -1)
            {
                logger.severe("Could not find mesh model for "
                    + " mesh of node " + nodeIndex);
                return;
            }
            // nodeModelX.addMeshModel(meshModelY);
            block.add(nodeVar.invoke("addMeshModel")
                .arg(JExpr._this().ref("meshModel" + meshIndex)));
        }

    }

}
