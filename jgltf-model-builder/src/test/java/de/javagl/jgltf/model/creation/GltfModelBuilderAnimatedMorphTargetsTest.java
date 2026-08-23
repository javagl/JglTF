package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.AnimationModel.Sampler;
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
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultChannel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultSampler;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.io.v2.GltfAssetsV2;

/**
 * Tests for the {@link GltfModelBuilder} class 
 */
@SuppressWarnings("javadoc")
public class GltfModelBuilderAnimatedMorphTargetsTest
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createWithAnimatedMorphTargets();
        printEmbedded(gltfModel);
    }

    @Test
    public void testWithAnimatedMorphTargets()
    {
        GltfModel gltfModel = createWithAnimatedMorphTargets();
        
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
        
        // 2 base geometry, 2 morph targets, 2 for animation times/values
        assertEquals(6, accessorModels.size());
        assertEquals(1, animationModels.size());
        assertEquals(1, bufferModels.size());
        assertEquals(4, bufferViewModels.size());
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

    private static DefaultGltfModel createWithAnimatedMorphTargets()
    {
        
        int indices[] = { 0, 1, 2 };
        float positions[] =
        {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.5f, 1.0f, 0.0f
        };
        
        // Create the basic mesh primitive
        MeshPrimitiveBuilder builder = MeshPrimitiveBuilder.create();
        builder.setTriangles();
        builder.setIntIndicesAsShort(IntBuffer.wrap(indices));
        builder.addPositions3D(FloatBuffer.wrap(positions));

        // Create two morph targets for the mesh primitive
        float positionDisplacements0[] = 
        {
             0.0f, 0.0f, 0.0f,
             0.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f
        };
        builder.addMorphTarget(0, "POSITION", Buffers
            .createByteBufferFrom(FloatBuffer.wrap(positionDisplacements0)));

        float positionDisplacements1[] = 
        {
             0.0f, 0.0f, 0.0f,
             0.0f, 0.0f, 0.0f,
             1.0f, 0.0f, 0.0f
        };
        builder.addMorphTarget(1, "POSITION", Buffers
            .createByteBufferFrom(FloatBuffer.wrap(positionDisplacements1)));
        
        // Create a mesh, node, and scene from the mesh primitive
        DefaultMeshPrimitiveModel meshPrimitiveModel = builder.build();
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);

        // Create the the animation sampler that will contain the 
        // times- and values accessors
        float[] times = { 0.0f, 1.0f, 2.0f };
        float[] values = { 0.0f, 1.0f, 0.0f };

        Sampler sampler = new DefaultSampler(
            AccessorModels.createFloatScalar(FloatBuffer.wrap(times)), 
            Interpolation.LINEAR, 
            AccessorModels.createFloatScalar(FloatBuffer.wrap(values)));

        // Create the animation channel from the sampler and the node
        Channel channel = new DefaultChannel(sampler, nodeModel, "weights");

        // Create the actual animation
        DefaultAnimationModel animationModel = new DefaultAnimationModel();
        animationModel.addChannel(channel);

        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        
        // The animation model has to be added explicitly. It is not
        // referred to by any other scene element.
        gltfModelBuilder.addAnimationModel(animationModel);
        
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
