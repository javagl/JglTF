package de.javagl.jgltf.model.creation.example;

import java.io.IOException;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.MaterialModels;
import de.javagl.jgltf.model.creation.MeshPrimitiveModels;
import de.javagl.jgltf.model.creation.SceneModels;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetsV2;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * A basic example for the glTF model creation.<br>
 * <br>
 * This example uses some of the convenience functions for creating
 * the elements of the glTF model (e.g. the {@link MeshPrimitiveModels}
 * and {@link MaterialModels} functions). 
 */
@SuppressWarnings("javadoc")
public class GltfModelCreationBasicExample
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
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            MeshPrimitiveModels.create(indices, positions, null, null);
        
        // Create a material, and assign it to the mesh primitive
        MaterialModelV2 materialModel = 
            MaterialModels.createFromBaseColor(1.0f, 0.0f, 0.0f, 1.0f);
        meshPrimitiveModel.setMaterialModel(materialModel);
        
        // Create a scene that only contains the given mesh primitive
        SceneModel sceneModel = 
            SceneModels.createFromMeshPrimitive(meshPrimitiveModel);
        
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
