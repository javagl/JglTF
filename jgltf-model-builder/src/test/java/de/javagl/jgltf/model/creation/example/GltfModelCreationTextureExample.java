package de.javagl.jgltf.model.creation.example;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.MaterialModels;
import de.javagl.jgltf.model.creation.MeshPrimitiveModels;
import de.javagl.jgltf.model.creation.SceneModels;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetsV2;
import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * A basic example for the glTF model creation.
 */
@SuppressWarnings("javadoc")
public class GltfModelCreationTextureExample
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createGltfModel();
        printEmbedded(gltfModel);
    }
    
    private static GltfModel createGltfModel()
    {
        // Create a simple mesh primitive of a unit square
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
        float texCoords[] =
        {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
        };
        DefaultMeshPrimitiveModel meshPrimitiveModel = 
            MeshPrimitiveModels.create(indices, positions, normals, texCoords);
        
        // Create a material from a buffered image
        BufferedImage bufferedImage = createTextureImage();
        MaterialModelV2 materialModel = MaterialModels.createFromBufferedImage(
            bufferedImage, "texture.jpg", "image/jpeg");

        // Assign the material to the mesh primitive
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
        GltfModel gltfModel = gltfModelBuilder.build();

        return gltfModel;
    }
    
    
    private static BufferedImage createTextureImage()
    {
        int w = 256;
        int h = 256;
        BufferedImage bufferedImage = new BufferedImage(
            w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("Runtime-generated texture", 10, 20);
        g.setColor(Color.RED);
        g.fill(new RoundRectangle2D.Double(50, 50, 50, 50, 10, 10));
        g.setColor(Color.GREEN);
        g.fill(new RoundRectangle2D.Double(70, 70, 50, 50, 10, 10));
        g.setColor(Color.BLUE);
        g.fill(new RoundRectangle2D.Double(90, 90, 50, 50, 10, 10));
        g.dispose();
        return bufferedImage;
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
