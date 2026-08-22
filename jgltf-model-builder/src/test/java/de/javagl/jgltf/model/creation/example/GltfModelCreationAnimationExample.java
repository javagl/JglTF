package de.javagl.jgltf.model.creation.example;

import java.io.File;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.creation.AnimationBuilder;
import de.javagl.jgltf.model.creation.AnimationBuilder.ChannelBuilder;
import de.javagl.jgltf.model.creation.AnimationBuilder.CubicSplineScaleBuilder;
import de.javagl.jgltf.model.creation.AnimationBuilder.LinearRotationBuilder;
import de.javagl.jgltf.model.creation.AnimationBuilder.StepTranslationBuilder;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.MaterialModels;
import de.javagl.jgltf.model.creation.MeshPrimitiveModels;
import de.javagl.jgltf.model.creation.NodeModels;
import de.javagl.jgltf.model.creation.SceneModels;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.GltfModelWriter;

/**
 * An example for creating animations.<br>
 * <br>
 */
@SuppressWarnings("javadoc")
public class GltfModelCreationAnimationExample
{
    public static void main(String[] args) throws Exception
    {
        GltfModel gltfModel = createGltfModel();
        GltfModelWriter w = new GltfModelWriter();
        w.writeEmbedded(gltfModel, new File("./data/animation.gltf"));
    }

    private static GltfModel createGltfModel()
    {
        DefaultMeshPrimitiveModel meshPrimitiveModel = createUnitSquare();

        NodeModel nodeModel =
            NodeModels.createFromMeshPrimitive(meshPrimitiveModel);

        AnimationBuilder ab = AnimationBuilder.create();
        ChannelBuilder cb = ab.beginChannel(nodeModel);

        // Define the 'scale' animation. This is using a CUBICSPLINE
        // interpolation, meaning that each time stamp is associated with
        // the in-tangent, key frame, and out-tangent.
        // Here, the in- and out-tangents are very large, to emphasize the
        // effect of the interpolation, causing the scaling to "overshoot"
        // and "undershoot" the scaling of 3.0 and 1.0, respectively
        // @formatter:off
        CubicSplineScaleBuilder sb = cb.beginCubicSplineScale();
        sb.add(0.5, 
             0.0,  0.0,  0.0, 
             1.0,  1.0,  1.0, 
             5.0,  5.0,  5.0);
        sb.add(1.5, 
            -5.0, -5.0, -5.0, 
             3.0,  3.0,  3.0, 
             0.0,  0.0,  0.0);
        sb.add(2.5, 
             0.0,  0.0,  0.0, 
             3.0,  3.0,  3.0, 
            -5.0, -5.0, -5.0);
        sb.add(3.5, 
             5.0,  5.0,  5.0, 
             1.0,  1.0,  1.0,
             0.0,  0.0 , 0.0);
        // @formatter:off

        // Add a rotation around (0,0,1), with LINEAR interpolation, 
        // using the "axis-angle" convenience function
        LinearRotationBuilder rb = cb.beginLinearRotation();
        rb.addAxisAngle(5.0, 0.0, 0.0, 1.0, 0.0);
        rb.addAxisAngle(5.5, 0.0, 0.0, 1.0, Math.toRadians(90.0));
        rb.addAxisAngle(6.0, 0.0, 0.0, 1.0, Math.toRadians(90.0));
        rb.addAxisAngle(6.5, 0.0, 0.0, 1.0, 0.0);
        
        // Add a translation, using STEP interpolation
        StepTranslationBuilder tb = cb.beginStepTranslation();
        tb.add(7.0, 0.0, 0.0, 0.0);
        tb.add(7.5, 1.0, 0.0, 0.0);
        tb.add(8.0, 1.0, 1.0, 0.0);
        tb.add(8.5, 0.0, 1.0, 0.0);
        tb.add(9.0, 0.0, 0.0, 0.0);

        cb.endChannel();
        
        // Build the animation
        DefaultAnimationModel animationModel = ab.build();
        
        // Create a scene that only contains the given node
        SceneModel sceneModel =  SceneModels.createFromNode(nodeModel);
        
        // Pass the scene to the model builder. It will take care
        // of the other model elements that are contained in the scene.
        // (I.e. the mesh primitive and its accessors, and the material)
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        gltfModelBuilder.addAnimationModel(animationModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();

        return gltfModel;
    }

    /**
     * Create a simple unit square mesh primitive
     * 
     * @return The mesh primitive
     */
    private static DefaultMeshPrimitiveModel createUnitSquare()
    {
        // Create a mesh primitive
        int indices[] = { 0, 1, 2, 1, 3, 2 };
        // @formatter:off
        float[] positions = new float[]
        {   
            0.0f, 0.0f, 0.0f, 
            1.0f, 0.0f, 0.0f, 
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f 
        };
        // @formatter:on

        DefaultMeshPrimitiveModel meshPrimitiveModel =
            MeshPrimitiveModels.create(indices, positions, null, null);

        // Create a material, and assign it to the mesh primitive
        PbrMaterialModel materialModel =
            MaterialModels.createFromBaseColor(1.0f, 0.0f, 0.0f, 1.0f);
        meshPrimitiveModel.setMaterialModel(materialModel);

        return meshPrimitiveModel;
    }
}
