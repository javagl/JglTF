package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AssetModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.ExtensionsModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetsV2;

/**
 * Tests for the {@link GltfModelBuilder} class 
 */
@SuppressWarnings("javadoc")
public class GltfModelBuilderBasicTest
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createBasic();
        printEmbedded(gltfModel);
    }
    
    @Test
    public void testBasic()
    {
        GltfModel gltfModel = createBasic();
        
        List<AccessorModel> accessorModels = gltfModel.getAccessorModels();
        List<AnimationModel> animationModels = gltfModel.getAnimationModels();
        List<BufferModel> bufferModels = gltfModel.getBufferModels();
        List<BufferViewModel> bufferViewModels = gltfModel.getBufferViewModels();
        List<CameraModel> cameraModels = gltfModel.getCameraModels();
        List<ImageModel> imageModels = gltfModel.getImageModels();
        List<MaterialModel> materialModels = gltfModel.getMaterialModels();
        List<MeshModel> meshModels = gltfModel.getMeshModels();
        List<NodeModel> nodeModels = gltfModel.getNodeModels();
        List<SceneModel> sceneModels = gltfModel.getSceneModels();
        List<SkinModel> skinModels = gltfModel.getSkinModels();
        List<TextureModel> textureModels = gltfModel.getTextureModels();
        ExtensionsModel extensionsModel = gltfModel.getExtensionsModel();
        AssetModel assetModel = gltfModel.getAssetModel();
        
        assertEquals(2, accessorModels.size());
        assertEquals(0, animationModels.size());
        assertEquals(1, bufferModels.size());
        assertEquals(2, bufferViewModels.size());
        assertEquals(0, cameraModels.size());
        assertEquals(0, imageModels.size());
        assertEquals(0, materialModels.size());
        assertEquals(1, meshModels.size());
        assertEquals(1, nodeModels.size());
        assertEquals(1, sceneModels.size());
        assertEquals(0, skinModels.size());
        assertEquals(0, textureModels.size());
        assertNotNull(extensionsModel);
        assertNotNull(assetModel);
    }

    private static DefaultGltfModel createBasic()
    {
        int indices[] = { 0, 1, 2 };
        float positions[] =
        {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.5f, 1.0f, 0.0f
        };
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            MeshPrimitiveModels.create(indices, positions, null, null);
        
        DefaultSceneModel sceneModel = 
            SceneModels.createFromMeshPrimitive(meshPrimitiveModel);

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
