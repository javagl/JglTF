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

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;

/**
 * A code creator for the scenes code
 */
class ScenesCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(ScenesCodeCreator.class.getName());

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
    ScenesCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<SceneModel> sceneModels = gltfModel.getSceneModels();
        if (sceneModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Scenes (" + sceneModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < sceneModels.size(); i++)
        {
            block
                .directStatement("// Scene " + i + " of " + sceneModels.size());
            SceneModel sceneModel = sceneModels.get(i);
            createScene(block, sceneModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given scene, and add it to the given
     * block
     * 
     * @param block The block
     * @param sceneModel The scene
     * @param sceneIndex The index of the scene
     */
    private void createScene(JBlock block, SceneModel sceneModel,
        int sceneIndex)
    {
        JClass defaultSceneModelClass = findClass(DefaultSceneModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultSceneModelClass,
            "sceneModel" + sceneIndex);

        JMethod method = createSceneCreationMethod(sceneModel, sceneIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given scene model
     * 
     * @param sceneModel The scene model
     * @param sceneIndex The scene index
     * @return The method
     */
    private JMethod createSceneCreationMethod(SceneModel sceneModel,
        int sceneIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createSceneModel" + sceneIndex);
        Comments.add(method, "Create the specified scene model");

        JBlock block = method.body();
        createSceneCreationCode(block, sceneModel, sceneIndex);
        return method;
    }

    /**
     * Create the code that creates the given scene model and add it to the
     * given block
     * 
     * @param block The block
     * @param sceneModel The scene model
     * @param sceneIndex The scene index
     */
    private void createSceneCreationCode(JBlock block, SceneModel sceneModel,
        int sceneIndex)
    {
        // Collect the required types
        JClass defaultSceneModelClass = findClass(DefaultSceneModel.class);

        // this.sceneModelX = new DefaultSceneModel()
        JFieldRef sceneVar = JExpr._this().ref("sceneModel" + sceneIndex);
        block.assign(sceneVar, JExpr._new(defaultSceneModelClass));

        // Nodes
        List<NodeModel> nodeModels = sceneModel.getNodeModels();
        for (int i = 0; i < nodeModels.size(); i++)
        {
            NodeModel nodeModel = nodeModels.get(i);
            int nodeIndex = gltfModel.getNodeModels().indexOf(nodeModel);
            if (nodeIndex == -1)
            {
                logger.severe("Could not find node model for " + " node " + i
                    + " of scene " + sceneIndex);
                return;
            }
            // sceneModelX.addNode(nodeModelY);
            block.add(sceneVar.invoke("addNode")
                .arg(JExpr._this().ref("nodeModel" + nodeIndex)));
        }
    }

}
