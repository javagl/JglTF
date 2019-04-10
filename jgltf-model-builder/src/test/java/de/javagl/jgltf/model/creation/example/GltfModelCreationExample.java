package de.javagl.jgltf.model.creation.example;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.creation.AccessorModels;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.ImageModels;
import de.javagl.jgltf.model.creation.MaterialBuilder;
import de.javagl.jgltf.model.creation.MeshPrimitiveBuilder;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.v2.GltfCreatorV2;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * Basic tests and examples for the glTF model creation
 */
@SuppressWarnings("javadoc")
public class GltfModelCreationExample
{
    public static void main(String[] args) throws Exception
    {
        //createMaterial();
        //createMeshPrimitive();
        createGltf();
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
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
        };
        
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
        MaterialBuilder materialBuilder =  MaterialBuilder.create();

        materialBuilder.setBaseColorTexture(
            "./data/testTexture.png", "testTexture.png", null);
        
        DefaultImageModel emissiveImageModel = ImageModels.create(
            "./data/testTexture.png", "testTexture.jpg", "image/jpeg");
        DefaultTextureModel emissiveTexture = new DefaultTextureModel();
        emissiveTexture.setImageModel(emissiveImageModel);
        materialBuilder.setEmissiveTexture(emissiveTexture, 1, 1, 1, null);
        
        MaterialModelV2 materialModel = materialBuilder.build();
        return materialModel;
    }
    
    private static void createGltf() throws Exception
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
        DefaultGltfModel gltfModel = gltfModelBuilder.build();

        // Print the glTF to the console.
        GlTF gltf = GltfCreatorV2.create(gltfModel);
        GltfWriter gltfWriter = new GltfWriter();
        gltfWriter.setIndenting(true);
        gltfWriter.write(gltf, System.out);
    }
}
