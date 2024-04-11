package de.javagl.jgltf.model.creation.example;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.creation.AccessorModels;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.ImageModels;
import de.javagl.jgltf.model.creation.MaterialBuilder;
import de.javagl.jgltf.model.creation.MeshPrimitiveBuilder;
import de.javagl.jgltf.model.creation.TextureModels;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetsV2;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * A basic example for the glTF model creation.<br>
 * <br>
 * This example uses some of the builder classes for creating elements of
 * the glTF model (mesh primitives and materials), and assembles the other 
 * elements manually (meshes, nodes and scenes)
 */
@SuppressWarnings("javadoc")
public class GltfModelCreationExample
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createGltfModel();
        printEmbedded(gltfModel);
    }
    
    private static GltfModel createGltfModel()
    {
        // Create a material
        MaterialModelV2 materialModel = createMaterial();

        // Create a mesh primitive
        DefaultMeshPrimitiveModel meshPrimitiveModel = createMeshPrimitive();
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Create a mesh with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create a node with the mesh
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);
        
        // Create a scene with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);
        
        // Pass the scene to the model builder. It will take care
        // of the other model elements that are contained in the scene.
        // (I.e. the mesh primitive and its accessors, and the material 
        // and its textures)
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        GltfModel gltfModel = gltfModelBuilder.build();

        return gltfModel;
    }
    
    private static DefaultMeshPrimitiveModel createMeshPrimitive()
    {
        int indices[] =
        {
            0, 1, 2,
            1, 3, 2
        };
        float positions[] =
        {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
        };
        float normals[] =
        {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f
        };
        float texCoords0[] =
        {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
        };
        
        // Create a mesh primitive using the MeshPrimitiveBuilder class. 
        // This allows adding the individual attributes one by one, 
        // with the option to handle non-standard attributes
        MeshPrimitiveBuilder meshPrimitiveBuilder = 
            MeshPrimitiveBuilder.create(); 
        meshPrimitiveBuilder.setIntIndicesAsShort(IntBuffer.wrap(indices));
        meshPrimitiveBuilder.addPositions3D(FloatBuffer.wrap(positions));
        meshPrimitiveBuilder.addNormals3D(FloatBuffer.wrap(normals));
        meshPrimitiveBuilder.addTexCoords02D(FloatBuffer.wrap(texCoords0));
        meshPrimitiveBuilder.addAttribute("TEXCOORD_1", 
            AccessorModels.createFloat2D(FloatBuffer.wrap(texCoords0)));
        DefaultMeshPrimitiveModel meshPrimitiveModel = 
            meshPrimitiveBuilder.build();
        
        return meshPrimitiveModel;
    }
    
    private static MaterialModelV2 createMaterial()
    {
        // Create a material using the MaterialBuilder class.
        // This allows configuring all elements of the resulting
        // material, with some convenience functions for common
        // use cases
        
        MaterialBuilder materialBuilder =  MaterialBuilder.create();

        materialBuilder.setBaseColorTexture(
            "./src/test/resources/testTexture.jpg", "testTexture.jpg", null);
        
        ImageModel emissiveImageModel = ImageModels.create(
            "./src/test/resources/testTexture.jpg",
            "testTexture.jpg", "image/jpeg");

        TextureModel emissiveTexture = 
            TextureModels.createFromImage(emissiveImageModel);
        materialBuilder.setEmissiveTexture(
            emissiveTexture, 1.0f, 1.0f, 1.0f, 1);
        
        MaterialModelV2 materialModel = materialBuilder.build();
        return materialModel;
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
