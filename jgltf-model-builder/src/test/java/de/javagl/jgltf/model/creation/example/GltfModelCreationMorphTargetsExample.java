package de.javagl.jgltf.model.creation.example;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.MaterialModels;
import de.javagl.jgltf.model.creation.MeshPrimitiveBuilder;
import de.javagl.jgltf.model.creation.SceneModels;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetsV2;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * Basic tests and examples for the glTF model creation with morph targets
 */
@SuppressWarnings("javadoc")
public class GltfModelCreationMorphTargetsExample
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createGltfModel();
        printEmbedded(gltfModel);
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
        
        MeshPrimitiveBuilder meshPrimitiveBuilder = 
            MeshPrimitiveBuilder.create(); 
        meshPrimitiveBuilder.setIntIndicesAsShort(IntBuffer.wrap(indices));
        meshPrimitiveBuilder.addPositions3D(FloatBuffer.wrap(positions));
        
        // Add the first morph target (position displacements)
        float positionsDispacements0[] =
        { 
             0.0f, 0.0f, 0.0f, 
             0.0f, 0.0f, 0.0f, 
            -1.0f, 0.0f, 0.0f
        };
        meshPrimitiveBuilder.addMorphTarget(0, "POSITION", 
            Buffers.createByteBufferFrom(
                FloatBuffer.wrap(positionsDispacements0)));

        // Add the second morph target (position displacements)
        float positionsDispacements1[] =
        { 
            0.0f, 0.0f, 0.0f, 
            0.0f, 0.0f, 0.0f, 
            1.0f, 0.0f, 0.0f
        };
        meshPrimitiveBuilder.addMorphTarget(1, "POSITION", 
            Buffers.createByteBufferFrom(
                FloatBuffer.wrap(positionsDispacements1)));
        
        DefaultMeshPrimitiveModel meshPrimitiveModel = 
            meshPrimitiveBuilder.build();

        // Create a material, and assign it to the mesh primitive
        MaterialModelV2 materialModel = 
            MaterialModels.createFromBaseColor(1.0f, 0.9f, 0.9f, 1.0f);
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Create a mesh with the mesh primitive, assigning
        // the morph target weights
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.setWeights(new double[] { 0.0f, 0.25f } );
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);
        
        // Create a scene that only contains the given mesh
        SceneModel sceneModel = SceneModels.createFromMesh(meshModel);
        
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
