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
 * A test to make sure that creating a model that contains the same mesh
 * multiple times can be constructed without errors 
 */
@SuppressWarnings("javadoc")
public class DuplicateMeshesTest
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createGltfModel();
        printEmbedded(gltfModel);
    }
    

    @Test
    public void testDuplicateMeshes()
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
        
        // Create a single mesh with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);
        
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        
        // Add the same mesh to the scene twice
        DefaultNodeModel nodeModel0 = new DefaultNodeModel();
        nodeModel0.setTranslation(new double[] { -1.0f, 0, 0 });
        nodeModel0.addMeshModel(meshModel);
        sceneModel.addNode(nodeModel0);

        DefaultNodeModel nodeModel1 = new DefaultNodeModel();
        nodeModel1.setTranslation(new double[] { 1.0f, 0, 0 });
        nodeModel1.addMeshModel(meshModel);
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
