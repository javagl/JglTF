package de.javagl.jgltf.model.creation.example;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.creation.AnimationBuilder;
import de.javagl.jgltf.model.creation.AnimationBuilder.ChannelBuilder;
import de.javagl.jgltf.model.creation.AnimationBuilder.LinearTranslationBuilder;
import de.javagl.jgltf.model.creation.AnimationBuilder.LinearWeightsBuilder;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.MeshPrimitiveBuilder;
import de.javagl.jgltf.model.creation.NodeModels;
import de.javagl.jgltf.model.creation.SceneModels;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.GltfModelWriter;

/**
 * An example for creating a mesh primitive with animated morph targets
 */
public class GltfModelCreationMorphAnimationExample
{
    /**
     * The entry point
     * 
     * @param args Not used
     * @throws IOException Hopefully not...
     */
    public static void main(String[] args) throws IOException
    {
        GltfModel gltfModel = createGltfModel();
        GltfModelWriter w = new GltfModelWriter();
        w.writeEmbedded(gltfModel, new File("./data/morphAnimation.gltf"));
    }

    /**
     * Create the glTF model for this test
     * 
     * @return The model
     */
    public static DefaultGltfModel createGltfModel()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createMorphedSquareMeshPrimitive();

        // Create a node model with a mesh from the mesh primitive
        DefaultNodeModel nodeModel =
            NodeModels.createFromMeshPrimitive(meshPrimitiveModel);

        // Create a morph target animation targeting the "weights" of the node
        DefaultAnimationModel animationModel =
            createMorphAnimationModel(nodeModel);

        // Create a scene model with the node
        DefaultSceneModel sceneModel = SceneModels.createFromNode(nodeModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        gltfModelBuilder.addAnimationModel(animationModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a basic square mesh primitive with two morph targets
     * 
     * @return The mesh primitive
     */
    private static DefaultMeshPrimitiveModel createMorphedSquareMeshPrimitive()
    {
        // The raw data for a morphed square, with indices, positions,
        // and two morph target displacements for the positions
        // @formatter:off
        short[] indices = new short[]
        { 
            0, 1, 2, 
            1, 3, 2
        };
        float[] positions = new float[]
        {   
            0.0f, 0.0f, 0.0f, 
            1.0f, 0.0f, 0.0f, 
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f 
        };
        float[] displacements0 = new float[]
        { 
             0.0f, 0.0f, 0.0f, 
             0.0f, 0.0f, 0.0f, 
            -1.0f, 1.0f, 0.0f, 
            -0.5f, 0.5f, 0.0f 
        };
        float[] displacements1 = new float[]
        { 
            0.0f, 0.0f, 0.0f, 
            0.0f, 0.0f, 0.0f, 
            0.5f, 0.5f, 0.0f, 
            1.0f, 1.0f, 0.0f 
        };
        // @formatter:on

        // Assemble the raw data into a mesh primitive
        MeshPrimitiveBuilder mpb = MeshPrimitiveBuilder.create();
        mpb.setShortIndices(ShortBuffer.wrap(indices));
        mpb.addPositions3D(FloatBuffer.wrap(positions));
        mpb.addMorphTarget(0, "POSITION",
            Buffers.createByteBufferFrom(FloatBuffer.wrap(displacements0)));
        mpb.addMorphTarget(1, "POSITION",
            Buffers.createByteBufferFrom(FloatBuffer.wrap(displacements1)));
        DefaultMeshPrimitiveModel meshPrimitiveModel = mpb.build();
        return meshPrimitiveModel;
    }

    /**
     * Create an animation of two morph target weights (and the translation)
     * for the given node
     * 
     * @param nodeModel The node model
     * @return The animation model
     */
    private static DefaultAnimationModel
        createMorphAnimationModel(NodeModel nodeModel)
    {
        AnimationBuilder ab = AnimationBuilder.create();
        ChannelBuilder cb = ab.beginChannel(nodeModel);

        // Add the weights animation to the channel
        double tw = 0.0;
        LinearWeightsBuilder wb = cb.beginLinearWeights();
        wb.add(tw++, 0.0, 0.0);
        wb.add(tw++, 1.0, 0.0);
        wb.add(tw++, 1.0, 1.0);
        wb.add(tw++, 0.0, 1.0);
        wb.add(tw++, 0.0, 0.0);

        // For illustration only: Also add a translation 
        // animation to the same channel
        double tt = 0.0;
        LinearTranslationBuilder tb = cb.beginLinearTranslation();
        tb.add(tt++, 0.0, 0.0, 0.0);
        tb.add(tt++, 1.0, 0.0, 0.0);
        tb.add(tt++, 1.0, 1.0, 0.0);
        tb.add(tt++, 0.0, 1.0, 0.0);
        tb.add(tt++, 0.0, 0.0, 0.0);

        cb.endChannel();
        DefaultAnimationModel animationModel = ab.build();
        return animationModel;
    }
}
