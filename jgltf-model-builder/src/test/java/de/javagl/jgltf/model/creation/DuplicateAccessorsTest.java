package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetsV2;

/**
 * A test to make sure that creating a model that contains the same accessors
 * multiple times (via one mesh primitive that appears multiple times) can
 * be constructed without errors 
 */
@SuppressWarnings("javadoc")
public class DuplicateAccessorsTest
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createGltfModel();
        printEmbedded(gltfModel);
    }
    
    @Test
    public void testDuplicateAccessors()
    {
        GltfModel gltfModel = createGltfModel();
        int expectedNumAccessors = 2;
        assertEquals(expectedNumAccessors, 
            gltfModel.getAccessorModels().size());
    }
    
    private static GltfModel createGltfModel()
    {
        // Create a mesh primitive
        int indices[] = { 0, 1, 2 };
        float positions[] =
        {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.5f, 1.0f, 0.0f
        };
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            MeshPrimitiveModels.create(indices, positions, null, null);
        
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        
        // Use the same mesh primitive model (with the same accessors)
        // in two different meshes, and add them to the scene
        DefaultMeshModel meshModel0 = new DefaultMeshModel();
        meshModel0.addMeshPrimitiveModel(meshPrimitiveModel);
        DefaultNodeModel nodeModel0 = new DefaultNodeModel();
        nodeModel0.addMeshModel(meshModel0);
        sceneModel.addNode(nodeModel0);

        DefaultMeshModel meshModel1 = new DefaultMeshModel();
        meshModel1.addMeshPrimitiveModel(meshPrimitiveModel);
        DefaultNodeModel nodeModel1 = new DefaultNodeModel();
        nodeModel1.addMeshModel(meshModel1);
        sceneModel.addNode(nodeModel1);
        
        // Pass the scene to the model builder. It will take care
        // of the other model elements that are contained in the scene.
        // (I.e. the mesh primitive and its accessors, and the material 
        // and its textures)
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();

        return gltfModel;
    }
    
    private static void printEmbedded(GltfModel gltfModel) throws IOException 
    {
        GltfAssetV2 asset = GltfAssetsV2.createEmbedded(gltfModel);
        GlTF gltf = asset.getGltf();
        GltfWriter gltfWriter = new GltfWriter();
        gltfWriter.setIndenting(true);
        gltfWriter.write(gltf, System.out);
    }
}
