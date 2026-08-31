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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;

/**
 * A code creator for the meshes code
 */
class MeshesCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(MeshesCodeCreator.class.getName());

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
    MeshesCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<MeshModel> meshModels = gltfModel.getMeshModels();
        if (meshModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Meshes (" + meshModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < meshModels.size(); i++)
        {
            block.directStatement("// Mesh " + i + " of " + meshModels.size());
            MeshModel meshModel = meshModels.get(i);
            createMesh(block, meshModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given mesh, and add it to the given
     * block
     * 
     * @param block The block
     * @param meshModel The mesh
     * @param meshIndex The index of the mesh
     */
    private void createMesh(JBlock block, MeshModel meshModel, int meshIndex)
    {
        JClass defaultMeshModelClass = findClass(DefaultMeshModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultMeshModelClass,
            "meshModel" + meshIndex);

        JMethod method = createMeshCreationMethod(meshModel, meshIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given mesh model
     * 
     * @param meshModel The mesh model
     * @param meshIndex The mesh index
     * @return The method
     */
    private JMethod createMeshCreationMethod(MeshModel meshModel, int meshIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createMeshModel" + meshIndex);
        Comments.add(method, "Create the specified mesh model");

        JBlock block = method.body();
        createMeshCreationCode(block, meshModel, meshIndex);
        return method;
    }

    /**
     * Create the code that creates the given mesh model and add it to the given
     * block
     * 
     * @param block The block
     * @param meshModel The mesh model
     * @param meshIndex The mesh index
     */
    private void createMeshCreationCode(JBlock block, MeshModel meshModel,
        int meshIndex)
    {
        // Collect the required types
        JClass defaultMeshModelClass = findClass(DefaultMeshModel.class);

        // this.meshModelX = new DefaultMeshModel()
        JFieldRef meshVar = JExpr._this().ref("meshModel" + meshIndex);
        block.assign(meshVar, JExpr._new(defaultMeshModelClass));

        createMeshPrimitives(block, meshVar, meshModel, meshIndex);

        // meshModelX.setWeights(new double[] { ... });
        double[] weights = meshModel.getWeights();
        if (weights != null)
        {
            block.add(
                meshVar.invoke("setWeights").arg(newDoubleArrayWith(weights)));
        }
    }

    /**
     * Create the code for creating the primitives of the specified mesh of the
     * given glTF model, and add it to the given block
     * 
     * @param block The block
     * @param meshVar The meshModelX variable
     * @param meshModel The mesh model
     * @param meshIndex The mesh index
     */
    private void createMeshPrimitives(JBlock block, JAssignmentTarget meshVar,
        MeshModel meshModel, int meshIndex)
    {
        List<MeshPrimitiveModel> meshPrimitiveModels =
            meshModel.getMeshPrimitiveModels();
        block.directStatement("// Primitives of mesh " + meshIndex + " ("
            + meshPrimitiveModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < meshPrimitiveModels.size(); i++)
        {
            block.directStatement("// Primitive " + i + " of "
                + meshPrimitiveModels.size() + " of mesh " + meshIndex);
            MeshPrimitiveModel meshPrimitiveModel = meshPrimitiveModels.get(i);
            createMeshPrimitive(block, meshVar, meshModel, meshIndex,
                meshPrimitiveModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the specified mesh primitive, and add it to
     * the given block
     * 
     * @param block The block
     * @param meshVar The meshModelX variable
     * @param meshModel The mesh model
     * @param meshIndex The mesh index
     * @param meshPrimitiveModel The mesh primitive model
     * @param meshPrimitiveIndex The mesh primitive index
     */
    private void createMeshPrimitive(JBlock block, JAssignmentTarget meshVar,
        MeshModel meshModel, int meshIndex,
        MeshPrimitiveModel meshPrimitiveModel, int meshPrimitiveIndex)
    {

        // Collect the required types
        JClass defaultMeshPrimitiveModelClass =
            findClass(DefaultMeshPrimitiveModel.class);
        JClass gltfConstantsClass = findClass(GltfConstants.class);

        // The mode has to be passed to the DefaultMeshPrimitiveModel
        // constructor
        int mode = meshPrimitiveModel.getMode();
        String modeString = GltfConstants.stringFor(mode);

        // DefaultMeshPrimitiveModel meshModelX_Y =
        // new DefaultMeshPrimitiveModel(GltfConstants.GL_TRIANGLES)
        JVar meshPrimitiveVar = block.decl(defaultMeshPrimitiveModelClass,
            "meshPrimitiveModel" + meshIndex + "_" + meshPrimitiveIndex,
            JExpr._new(defaultMeshPrimitiveModelClass)
                .arg(gltfConstantsClass.staticRef(modeString)));

        // Indices
        AccessorModel indices = meshPrimitiveModel.getIndices();
        if (indices != null)
        {
            int indicesIndex = gltfModel.getAccessorModels().indexOf(indices);
            if (indicesIndex == -1)
            {
                logger.severe("Could not find accessor model for "
                    + " indices of mesh primitive " + meshPrimitiveIndex
                    + " in mesh " + meshIndex);
                return;
            }
            // meshPrimitiveModelX_Y.setIndices(accessorModelZ);
            block.add(meshPrimitiveVar.invoke("setIndices")
                .arg(JExpr._this().ref("accessorModel" + indicesIndex)));
        }

        // Attributes
        Map<String, AccessorModel> attributes =
            meshPrimitiveModel.getAttributes();
        for (Entry<String, AccessorModel> entry : attributes.entrySet())
        {
            String attributeName = entry.getKey();
            AccessorModel attributeAccessor = entry.getValue();

            int attributeIndex =
                gltfModel.getAccessorModels().indexOf(attributeAccessor);
            if (attributeIndex == -1)
            {
                logger.severe("Could not find accessor model for "
                    + " attribute " + attributeName + " of mesh primitive "
                    + meshPrimitiveIndex + " in mesh " + meshIndex);
                return;
            }
            // meshPrimitiveModelX_Y.putAttribute("POSITION", accessorModelZ);
            block.add(meshPrimitiveVar.invoke("putAttribute")
                .arg(JExpr.lit(attributeName))
                .arg(JExpr._this().ref("accessorModel" + attributeIndex)));
        }

        MaterialModel materialModel = meshPrimitiveModel.getMaterialModel();
        if (materialModel != null)
        {
            int materialIndex =
                gltfModel.getMaterialModels().indexOf(materialModel);
            if (materialIndex == -1)
            {
                logger.severe("Could not find material model for "
                    + " material of mesh primitive " + meshPrimitiveIndex
                    + " in mesh " + meshIndex);
                return;
            }
            // meshPrimitiveModelX_Y.setMaterialModel(materialModelZ);
            block.add(meshPrimitiveVar.invoke("setMaterialModel")
                .arg(JExpr._this().ref("materialModel" + materialIndex)));
        }

        createMeshPrimitiveMorphTargets(block, meshIndex, meshPrimitiveVar,
            meshPrimitiveModel, meshPrimitiveIndex);

        // meshModelX.addMeshPrimitiveModel(meshPrimitiveModelX_Y);
        block
            .add(meshVar.invoke("addMeshPrimitiveModel").arg(meshPrimitiveVar));
    }

    /**
     * Create the code for creating the morph targets of the given primitive of
     * the given mesh of the given glTF model, and add it to the given block
     * 
     * @param block The block
     * @param meshIndex The mesh index
     * @param meshPrimitiveVar The meshPrimitiveX_Y variable
     * @param meshPrimitiveModel The mesh primitive model
     * @param meshPrimitiveIndex The mesh primitive index
     */
    private void createMeshPrimitiveMorphTargets(JBlock block, int meshIndex,
        JVar meshPrimitiveVar, MeshPrimitiveModel meshPrimitiveModel,
        int meshPrimitiveIndex)
    {
        List<Map<String, AccessorModel>> targets =
            meshPrimitiveModel.getTargets();
        if (targets.isEmpty())
        {
            return;
        }
        block.directStatement("// Targets of primitive " + meshPrimitiveIndex
            + " of mesh " + meshIndex + " (" + targets.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < targets.size(); i++)
        {
            block.directStatement("// Target " + i + " of " + targets.size()
                + " of mesh primitive " + meshPrimitiveIndex + " of mesh "
                + meshIndex);

            Map<String, AccessorModel> target = targets.get(i);
            createMeshPrimitiveMorphTarget(block, meshIndex, meshPrimitiveVar,
                meshPrimitiveIndex, target, i);
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the specified mesh primitive morph target,
     * and add it to the given block
     * 
     * @param block The block
     * @param meshIndex The mesh index
     * @param meshPrimitiveVar The meshPrimitiveModelX_Y variable
     * @param meshPrimitiveIndex The mesh primitive index
     * @param target The morph target
     * @param targetIndex The morph target index
     */
    private void createMeshPrimitiveMorphTarget(JBlock block, int meshIndex,
        JVar meshPrimitiveVar, int meshPrimitiveIndex,
        Map<String, AccessorModel> target, int targetIndex)
    {
        JClass mapClass = findClass(Map.class);
        JClass linkedHashMapClass = findClass(LinkedHashMap.class);
        JClass mapType = mapClass.narrow(String.class, AccessorModel.class);
        JClass linkedHashMapType =
            linkedHashMapClass.narrow(String.class, AccessorModel.class);

        // Map<String, AccessorModel> targetX_Y_Z =
        // new LinkedHashMap<String, AccessorModel>();
        JVar targetVar = block.decl(mapType,
            "target" + meshIndex + "_" + meshPrimitiveIndex + "_" + targetIndex,
            JExpr._new(linkedHashMapType));

        for (Entry<String, AccessorModel> entry : target.entrySet())
        {
            String attributeName = entry.getKey();
            AccessorModel attributeValue = entry.getValue();

            int attributeIndex =
                gltfModel.getAccessorModels().indexOf(attributeValue);
            if (attributeIndex == -1)
            {
                logger.severe("Could not find accessor model for "
                    + " attribute " + attributeName + " of morph target "
                    + targetIndex + " of mesh primitive " + meshPrimitiveIndex
                    + " in mesh " + meshIndex);
                return;
            }
            // targetX_Y_Z.putIndices("POSITION", accessorModelW);
            block.add(targetVar.invoke("put").arg(JExpr.lit(attributeName))
                .arg(JExpr._this().ref("accessorModel" + attributeIndex)));
        }

        // meshPrimitiveModelX_Y.addTarget(targetX_Y_Z);
        block.add(meshPrimitiveVar.invoke("addTarget").arg(targetVar));

    }

}
