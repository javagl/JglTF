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

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;

/**
 * A code creator for the skins code
 */
class SkinsCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(SkinsCodeCreator.class.getName());

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
    SkinsCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<SkinModel> skinModels = gltfModel.getSkinModels();
        if (skinModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Skins (" + skinModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < skinModels.size(); i++)
        {
            block.directStatement("// Skin " + i + " of " + skinModels.size());
            SkinModel skinModel = skinModels.get(i);
            createSkin(block, skinModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given skin, and add it to the given
     * block
     * 
     * @param block The block
     * @param skinModel The skin
     * @param skinIndex The index of the skin
     */
    private void createSkin(JBlock block, SkinModel skinModel, int skinIndex)
    {
        JClass defaultSkinModelClass = findClass(DefaultSkinModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultSkinModelClass,
            "skinModel" + skinIndex);

        JMethod method = createSkinCreationMethod(skinModel, skinIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given skin model
     * 
     * @param skinModel The skin model
     * @param skinIndex The skin index
     * @return The method
     */
    private JMethod createSkinCreationMethod(SkinModel skinModel, int skinIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createSkinModel" + skinIndex);
        Comments.add(method, "Create the specified skin model");

        JBlock block = method.body();
        createSkinCreationCode(block, skinModel, skinIndex);
        return method;
    }

    /**
     * Create the code that creates the given skin model and add it to the given
     * block
     * 
     * @param block The block
     * @param skinModel The skin model
     * @param skinIndex The skin index
     */
    private void createSkinCreationCode(JBlock block, SkinModel skinModel,
        int skinIndex)
    {
        // Collect the required types
        JClass defaultSkinModelClass = findClass(DefaultSkinModel.class);

        // this.skinModelX = new DefaultSkinModel()
        JFieldRef skinVar = JExpr._this().ref("skinModel" + skinIndex);
        block.assign(skinVar, JExpr._new(defaultSkinModelClass));

        // Inverse bind matrices
        AccessorModel ibm = skinModel.getInverseBindMatrices();
        int ibmIndex = gltfModel.getAccessorModels().indexOf(ibm);
        if (ibmIndex == -1)
        {
            logger.severe("Could not find accessor model for inverse bind "
                + "matrix of skin " + skinIndex);
            return;
        }
        // skinModelX.setInverseBindMatrices(accessorModelY);
        block.add(skinVar.invoke("setInverseBindMatrices")
            .arg(JExpr._this().ref("accessorModel" + ibmIndex)));

        // Joints
        List<NodeModel> joints = skinModel.getJoints();
        for (int i = 0; i < joints.size(); i++)
        {
            NodeModel joint = joints.get(i);
            int jointIndex = gltfModel.getNodeModels().indexOf(joint);
            if (jointIndex == -1)
            {
                logger.severe("Could not find node model for " + " joint " + i
                    + " of skin " + skinIndex);
                return;
            }
            // skinModelX.addJoint(nodeModelY);
            block.add(skinVar.invoke("addJoint")
                .arg(JExpr._this().ref("nodeModel" + jointIndex)));
        }

        // Skeleton
        NodeModel skeleton = skinModel.getSkeleton();
        if (skeleton != null)
        {
            int skeletonIndex = gltfModel.getNodeModels().indexOf(skeleton);
            if (skeletonIndex == -1)
            {
                logger.severe("Could not find node model for "
                    + " skeleton of skin " + skinIndex);
                return;
            }
            // skinModelX.setSkeleton(nodeModelY);
            block.add(skinVar.invoke("setSkeleton")
                .arg(JExpr._this().ref("nodeModel" + skeletonIndex)));
        }
    }

}
