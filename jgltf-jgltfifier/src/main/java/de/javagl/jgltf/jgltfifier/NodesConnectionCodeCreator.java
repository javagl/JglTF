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
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;

/**
 * A code creator for the code that connects the nodes, after the skins have
 * been created
 */
class NodesConnectionCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(NodesConnectionCodeCreator.class.getName());

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
    NodesConnectionCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
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
            + ") - only children or skin information");
        block.directStatement(" ");

        for (int i = 0; i < nodeModels.size(); i++)
        {
            block.directStatement("// Node " + i + " of " + nodeModels.size());
            NodeModel nodeModel = nodeModels.get(i);
            connectNode(block, nodeModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for connecting the given node, and add it to the given
     * block
     * 
     * @param block The block
     * @param nodeModel The node
     * @param nodeIndex The index of the node
     */
    private void connectNode(JBlock block, NodeModel nodeModel, int nodeIndex)
    {
        JMethod method = createNodeConnectionMethod(nodeModel, nodeIndex);
        block.invoke(method);
    }

    /**
     * Create the method that connects the given node model
     * 
     * @param nodeModel The node model
     * @param nodeIndex The node index
     * @return The method
     */
    private JMethod createNodeConnectionMethod(NodeModel nodeModel,
        int nodeIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "connectNodeModel" + nodeIndex);
        Comments.add(method, "Connect the specified node model");

        JBlock block = method.body();
        createNodeConnectionCode(block, nodeModel, nodeIndex);
        return method;
    }

    /**
     * Create the code that connects the given node model and add it to the
     * given block
     * 
     * @param block The block
     * @param nodeModel The node model
     * @param nodeIndex The node index
     */
    private void createNodeConnectionCode(JBlock block, NodeModel nodeModel,
        int nodeIndex)
    {
        JExpression nodeVar = JExpr._this().ref("nodeModel" + nodeIndex);

        // Children
        List<NodeModel> children = nodeModel.getChildren();
        for (int i = 0; i < children.size(); i++)
        {
            NodeModel child = children.get(i);
            int childIndex = gltfModel.getNodeModels().indexOf(child);
            if (childIndex == -1)
            {
                logger.severe("Could not find node model for " + " child " + i
                    + " of node " + nodeIndex);
                return;
            }
            // nodeModelX.addChild(nodeModelY);
            block.add(nodeVar.invoke("addChild")
                .arg(JExpr._this().ref("nodeModel" + childIndex)));
        }

        // Skin
        SkinModel skinModel = nodeModel.getSkinModel();
        if (skinModel != null)
        {
            int skinIndex = gltfModel.getSkinModels().indexOf(skinModel);
            if (skinIndex == -1)
            {
                logger.severe("Could not find skin model for "
                    + " skin of node " + nodeIndex);
                return;
            }
            // nodeModelX.setSkinModel(skinModelY);
            block.add(nodeVar.invoke("setSkinModel")
                .arg(JExpr._this().ref("skinModel" + skinIndex)));

        }
    }

}
