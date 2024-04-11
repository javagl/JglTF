/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;

/**
 * Tests for creating glTF assets with morph targets<br>
 */
@SuppressWarnings("javadoc")
public class MorphTargetsBuildingTest
{
    @Test
    public void testWriteTargets() throws IOException
    {
        // Regression test for https://github.com/javagl/JglTF/issues/98

        // Create a mesh primitive with two morph targets
        DefaultMeshPrimitiveModel inputMeshPrimitiveModel = createMeshPrimitive();

        // Create a scene with a node with a mesh with the given primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(inputMeshPrimitiveModel);
        meshModel.setWeights(new float[]
        { 0.0f, 0.25f });
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);

        // Pass the scene to the model builder, and build the model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();

        // The resulting mesh primitive model should contain
        // the morph targets as they have been given in the
        // input
        DefaultMeshModel mesh = gltfModel.getMeshModel(0);
        List<MeshPrimitiveModel> meshPrimitiveModels =
            mesh.getMeshPrimitiveModels();
        MeshPrimitiveModel resultMeshPrimitiveModel =
            meshPrimitiveModels.get(0);
        List<Map<String, AccessorModel>> targets =
            resultMeshPrimitiveModel.getTargets();

        assertNotNull(targets);
        assertNotEquals(0, targets.size());

        // Each accessor model that is defined in the morph targets
        // should appear in the list of accessor models of the 
        // model (this is the actual regression test for 
        // https://github.com/javagl/JglTF/issues/98 )
        List<AccessorModel> accessorModels = gltfModel.getAccessorModels();
        for (Map<String, AccessorModel> target : targets)
        {
            for (AccessorModel targetAcessorModel : target.values())
            {
                assertTrue(accessorModels.contains(targetAcessorModel));
            }
        }
    }

    // Create a mesh primitive model with two morph targets for the tests
    private static DefaultMeshPrimitiveModel createMeshPrimitive()
    {
        int indices[] =
            { 0, 1, 2, };
        float positions[] =
            { 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.0f, };

        DefaultMeshPrimitiveModel meshPrimitiveModel =
            MeshPrimitiveModels.create(indices, positions, null, null);

        float positionsDispacements0[] =
            { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, };
        MeshPrimitiveModels.addMorphTarget(meshPrimitiveModel, 
            0, "POSITION", positionsDispacements0);
        
        float positionsDispacements1[] =
            { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, };
        MeshPrimitiveModels.addMorphTarget(meshPrimitiveModel, 
            1, "POSITION", positionsDispacements1);

        return meshPrimitiveModel;
    }

}
