/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2026 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.transform.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.PbrMetallicRoughnessModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.ext.mesh_gpu_instancing.DefaultMeshGpuInstancingModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;
import de.javagl.jgltf.model.io.GltfModelWriter;
import de.javagl.jgltf.model.khr.draco_mesh_compression.DefaultDracoMeshCompressionModel;
import de.javagl.jgltf.model.khr.materials_clearcoat.DefaultMaterialsClearcoatModel;
import de.javagl.jgltf.model.khr.materials_clearcoat.MaterialsClearcoatModel;
import de.javagl.jgltf.model.khr.texture_transform.DefaultTextureTransformModel;
import de.javagl.jgltf.model.transform.GltfModelTransforms;

/**
 * Basic tests for this package
 */
public class GltfModelTransformsTests
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GltfModelTransformsTests.class.getName());

    /**
     * The log level
     */
    private static Level level = Level.FINE;

    /**
     * The base directory for the files that are written
     */
    private static Path basePath = Paths.get("./data/");

    /**
     * The entry point
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        LoggerUtil.initLogging();
        level = Level.INFO;

        runTest(createTestRemoveTexture());
        runTest(createTestRemoveTexCoordAccessor());
        runTest(createTestRemoveMaterial());
        runTest(createTestAddTexture());
        runTest(createTestRemoveClearcoatTexture());
        runTest(createTestRemoveClearcoatTextureInfo());
        runTest(createTestRemoveClearcoatTextureInfoTexture());
        runTest(createTestRemoveAnimationValuesAccessor());
        runTest(createTestRemoveSingleAnimationValuesAccessor());
        runTest(createTestRemoveSkinAnimationTimesAccessor());
        runTest(createTestRemoveSkinAttributes());
        runTest(createTestAddAnimation());
        runTest(createTestRemoveInstancingAccessor());
        runTest(createTestRemoveMorphAnimationTimesAccessor());
        runTest(createTestRemoveMorphTargetAccessor());
        runTest(createTestAddInstancing());
        runTest(createTestAddDraco());
        runTest(createTestRemoveDraco());
        runTest(createTestRemoveMaterialVariants());
        runTest(createTestRemoveSingleVariantMaterial());
        runTest(createTestRemoveTextureTransform());
        runTest(createTestAddClearcoatTextureTransform());
        runTest(createTestRemoveAnisotropy());
    }

    /**
     * Set the base directory for writing the files.
     * 
     * Only called from tests!
     * 
     * @param directory The directory
     */
    static void setBaseDirectory(String directory)
    {
        basePath = Paths.get(directory);
    }

    /**
     * Create a test to remove a texture
     * 
     * @return The test
     */
    static TestCase createTestRemoveTexture()
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-removedTexture";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            ModelElement texture0 = m.getTextureModel(0);

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(texture0);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove a texture coordinate accessor
     * 
     * @return The test
     */
    static TestCase createTestRemoveTexCoordAccessor()
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-removedTexCoordAccessor";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            MeshModel mesh0 = m.getMeshModel(0);
            MeshPrimitiveModel primitive0 =
                mesh0.getMeshPrimitiveModels().get(0);
            AccessorModel texCoords =
                primitive0.getAttributes().get("TEXCOORD_0");

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(texCoords);

            GltfModelTransforms.removeAll(m, toRemove);
            GltfModelTransforms.prune(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove a material
     * 
     * @return The test
     */
    static TestCase createTestRemoveMaterial()
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-removedMaterial";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            ModelElement material0 = m.getMaterialModel(0);

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(material0);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove a texture
     * 
     * @return The test
     */
    static TestCase createTestAddTexture()
    {
        String name = "SquareWithTexcoords";
        String modifiedName = name + "-addedTexture";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createSquareWithTexcoords();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            DefaultPbrMaterialModel materialModel = GltfTestModelCreation
                .createBaseColorTextureMaterialModel("baseColor.png");

            MeshModel mesh0 = m.getMeshModel(0);
            MeshPrimitiveModel primitive0 =
                mesh0.getMeshPrimitiveModels().get(0);
            DefaultMeshPrimitiveModel defaultPrimitive0 =
                (DefaultMeshPrimitiveModel) primitive0;
            defaultPrimitive0.setMaterialModel(materialModel);

            GltfModelTransforms.revalidate(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove a texture
     * 
     * @return The test
     */
    static TestCase createTestRemoveClearcoatTexture()
    {
        String name = "TexturedSquareWithClearcoat";
        String modifiedName = name + "-removedClearcoatTexture";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareWithClearcoat();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            ModelElement texture0 = m.getTextureModel(0);

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(texture0);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove a texture by setting the clearcoat texture info
     * texture to <code>null</code>
     * 
     * @return The test
     */
    static TestCase createTestRemoveClearcoatTextureInfoTexture()
    {
        String name = "TexturedSquareWithClearcoat";
        String modifiedName = name + "-removedClearcoatTextureInfoTexture";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareWithClearcoat();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            MaterialModel material0 = m.getMaterialModel(0);

            MaterialsClearcoatModel clearcoat0 = material0.getExtensionModel(
                "KHR_materials_clearcoat", MaterialsClearcoatModel.class);
            TextureInfoModel textureInfo0 =
                clearcoat0.getClearcoatTextureInfoModel();
            DefaultTextureInfoModel defaultTextureInfo0 =
                (DefaultTextureInfoModel) textureInfo0;
            defaultTextureInfo0.setTextureModel(null);

            GltfModelTransforms.prune(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove a texture by setting the clearcoat texture info
     * to <code>null</code>
     * 
     * @return The test
     */
    static TestCase createTestRemoveClearcoatTextureInfo()
    {
        String name = "TexturedSquareWithClearcoat";
        String modifiedName = name + "-removedClearcoatTextureInfo";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareWithClearcoat();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            MaterialModel material0 = m.getMaterialModel(0);

            DefaultMaterialsClearcoatModel defaultClearcoat0 =
                material0.getExtensionModel("KHR_materials_clearcoat",
                    DefaultMaterialsClearcoatModel.class);
            defaultClearcoat0.setClearcoatTextureInfoModel(null);

            GltfModelTransforms.prune(gltfModel);

        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove an animation values accessor from the animated
     * square
     * 
     * @return The test
     */
    static TestCase createTestRemoveAnimationValuesAccessor()
    {
        String name = "AnimatedSquare";
        String modifiedName = name + "-removedAnimationValues";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createAnimatedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            AnimationModel animation0 = m.getAnimationModel(0);
            Channel channel0 = animation0.getChannels().get(0);
            Sampler sampler0 = channel0.getSampler();
            AccessorModel values0 = sampler0.getOutput();

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(values0);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove an animation values accessor from the animated
     * squares
     * 
     * @return The test
     */
    static TestCase createTestRemoveSingleAnimationValuesAccessor()
    {
        String name = "TwoAnimatedSquares";
        String modifiedName = name + "-removedSingleAnimationValues";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTwoAnimatedSquares();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            AnimationModel animation0 = m.getAnimationModel(0);
            Channel channel1 = animation0.getChannels().get(1);
            Sampler sampler1 = channel1.getSampler();
            AccessorModel values1 = sampler1.getOutput();

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(values1);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove an animation values accessor from the simple skin
     * 
     * @return The test
     */
    static TestCase createTestRemoveSkinAnimationTimesAccessor()
    {
        String name = "SimpleSkin";
        String modifiedName = name + "-removedAnimationTimes";
        DefaultGltfModel gltfModel = GltfTestModelCreation.createSimpleSkin();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            AnimationModel animation0 = m.getAnimationModel(0);
            Channel channel0 = animation0.getChannels().get(0);
            Sampler sampler0 = channel0.getSampler();
            AccessorModel times0 = sampler0.getInput();

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(times0);

            // Optional: Remove the attributes for the skin (would cause
            // a validation warning otherwise)
            MeshModel mesh0 = m.getMeshModel(0);
            MeshPrimitiveModel primitive0 =
                mesh0.getMeshPrimitiveModels().get(0);
            DefaultMeshPrimitiveModel defaultPrimitive0 =
                (DefaultMeshPrimitiveModel) primitive0;
            defaultPrimitive0.removeAttribute("JOINTS_0");
            defaultPrimitive0.removeAttribute("WEIGHTS_0");

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove the skin attribute accessors from the simple skin
     * 
     * @return The test
     */
    static TestCase createTestRemoveSkinAttributes()
    {
        String name = "SimpleSkin";
        String modifiedName = name + "-removedSkinAttributes";
        DefaultGltfModel gltfModel = GltfTestModelCreation.createSimpleSkin();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            MeshModel mesh0 = m.getMeshModel(0);
            MeshPrimitiveModel primitive0 =
                mesh0.getMeshPrimitiveModels().get(0);
            DefaultMeshPrimitiveModel defaultPrimitive0 =
                (DefaultMeshPrimitiveModel) primitive0;
            defaultPrimitive0.removeAttribute("JOINTS_0");
            defaultPrimitive0.removeAttribute("WEIGHTS_0");

            GltfModelTransforms.prune(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to add an animation
     * 
     * @return The test
     */
    static TestCase createTestAddAnimation()
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-addedAnimation";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            NodeModel node0 = m.getNodeModel(0);
            DefaultAnimationModel animationModel =
                GltfTestModelCreation.createSimpleRotationAnimation(node0);
            m.addAnimationModel(animationModel);

            GltfModelTransforms.revalidate(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove an accessor that is used for instancing
     * 
     * @return The test
     */
    static TestCase createTestRemoveInstancingAccessor()
    {
        String name = "TexturedSquareInstanced";
        String modifiedName = name + "-removedInstancingAccessor";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareInstanced();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            ModelElement material0 = m.getMaterialModel(0);

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(material0);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to add draco to a mesh primitive
     * 
     * @return The test
     */
    static TestCase createTestAddDraco()
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-addedDraco";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            MeshModel material0 = m.getMeshModel(0);
            MeshPrimitiveModel primitive0 =
                material0.getMeshPrimitiveModels().get(0);
            DefaultMeshPrimitiveModel defaultPrimitive0 =
                (DefaultMeshPrimitiveModel) primitive0;

            // Assign draco mesh compression to the mesh primitive
            DefaultDracoMeshCompressionModel dracoMeshCompressionModel =
                new DefaultDracoMeshCompressionModel();
            dracoMeshCompressionModel.addAttribute("POSITION");
            dracoMeshCompressionModel.addAttribute("TEXCOORD_0");
            defaultPrimitive0.addExtensionModel("KHR_draco_mesh_compression",
                dracoMeshCompressionModel);

            GltfModelTransforms.revalidate(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove draco from a mesh primitive
     * 
     * @return The test
     */
    static TestCase createTestRemoveDraco()
    {
        String name = "TexturedSquareInstancedDraco";
        String modifiedName = name + "-removedDraco";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareInstancedDraco();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            MeshModel mesh0 = m.getMeshModel(0);
            MeshPrimitiveModel primitive0 =
                mesh0.getMeshPrimitiveModels().get(0);
            DefaultMeshPrimitiveModel defaultPrimitive0 =
                (DefaultMeshPrimitiveModel) primitive0;

            // Remove draco mesh compression from the mesh primitive
            defaultPrimitive0
                .removeExtensionModel("KHR_draco_mesh_compression");

            GltfModelTransforms.revalidate(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove an accessor that is used for morphing animation
     * 
     * @return The test
     */
    static TestCase createTestRemoveMorphAnimationTimesAccessor()
    {
        String name = "MorphedSquare";
        String modifiedName = name + "-removedMorphAnimationTimesAccessor";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createMorphedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            AnimationModel animation0 = m.getAnimationModel(0);
            Channel channel0 = animation0.getChannels().get(0);
            Sampler sampler0 = channel0.getSampler();
            AccessorModel times0 = sampler0.getInput();

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(times0);

            // Set some arbitrary weights to show a deformed state
            DefaultNodeModel n0 = m.getNodeModel(0);
            n0.setWeights(new double[]
            { 0.12, 0.34 });

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove an accessor that is used for morphing animation
     * 
     * @return The test
     */
    static TestCase createTestRemoveMorphTargetAccessor()
    {
        String name = "MorphedSquare";
        String modifiedName = name + "-removedMorphTargetAccessor";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createMorphedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            AnimationModel animation0 = m.getAnimationModel(0);
            Channel channel0 = animation0.getChannels().get(0);
            Sampler sampler0 = channel0.getSampler();
            AccessorModel values0 = sampler0.getOutput();

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(values0);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to add GPU instancing to a node
     * 
     * @return The test
     */
    static TestCase createTestAddInstancing()
    {
        String name = "TexturedSquare";
        String modifiedName = name + "-addedInstancing";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquare();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            DefaultNodeModel n0 = m.getNodeModel(0);

            // Assign the instancing extension to the node
            DefaultMeshGpuInstancingModel meshGpuInstancing =
                GltfTestModelCreation.createMeshGpuInstancing();
            n0.addExtensionModel("EXT_mesh_gpu_instancing", meshGpuInstancing);

            GltfModelTransforms.revalidate(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove material variants
     * 
     * @return The test
     */
    static TestCase createTestRemoveMaterialVariants()
    {
        String name = "MaterialVariants";
        String modifiedName = name + "-removedVariants";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createMaterialVariants();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            // Remove the material variants from the primitive model
            MeshModel mesh0 = m.getMeshModel(0);
            MeshPrimitiveModel primitive0 =
                mesh0.getMeshPrimitiveModels().get(0);
            DefaultMeshPrimitiveModel defaultPrimitive0 =
                (DefaultMeshPrimitiveModel) primitive0;
            defaultPrimitive0.removeExtensionModel("KHR_materials_variants");

            // Remove the material variants from the glTF model
            m.removeExtensionModel("KHR_materials_variants");

            GltfModelTransforms.prune(m);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove a single material that is used in material
     * variants
     * 
     * @return The test
     */
    static TestCase createTestRemoveSingleVariantMaterial()
    {
        String name = "MaterialVariants";
        String modifiedName = name + "-removedSingleVariantMaterial";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createMaterialVariants();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            MaterialModel material1 = m.getMaterialModel(1);

            Set<ModelElement> toRemove = new LinkedHashSet<ModelElement>();
            toRemove.add(material1);

            GltfModelTransforms.removeAll(m, toRemove);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }

    /**
     * Create a test to remove texture transform from a texture
     * 
     * @return The test
     */
    static TestCase createTestRemoveTextureTransform()
    {
        String name = "TexturedSquareWithTextureTransform";
        String modifiedName = name + "-removedTextureTransform";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareWithTextureTransform();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            PbrMaterialModel material0 =
                (PbrMaterialModel) m.getMaterialModel(0);
            PbrMetallicRoughnessModel pbr =
                material0.getPbrMetallicRoughnessModel();
            DefaultTextureInfoModel textureInfo =
                (DefaultTextureInfoModel) pbr.getBaseColorTextureInfoModel();
            textureInfo.removeExtensionModel("KHR_texture_transform");
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }
    
    /**
     * Create a test to add a texture transform to a clearcoat texture
     * 
     * @return The test
     */
    static TestCase createTestAddClearcoatTextureTransform()
    {
        String name = "TexturedSquareWithClearcoat";
        String modifiedName = name + "-addedClearcoatTextureTransform";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareWithClearcoat();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            PbrMaterialModel material0 =
                (PbrMaterialModel) m.getMaterialModel(0);
            MaterialsClearcoatModel clearcoat = material0.getExtensionModel(
                "KHR_materials_clearcoat", MaterialsClearcoatModel.class);
            DefaultTextureInfoModel textureInfo = (DefaultTextureInfoModel)clearcoat.getClearcoatTextureInfoModel();
            DefaultTextureTransformModel textureTransform =
                new DefaultTextureTransformModel();
            textureTransform.setOffset(new double[]
            { 0.25, 0.25 });
            textureTransform.setScale(new double[]
            { 0.5, 0.5 });
            textureTransform.setRotation(Math.toRadians(45.0));
            textureInfo.addExtensionModel("KHR_texture_transform",
                textureTransform);

            GltfModelTransforms.revalidate(gltfModel);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }
    

    /**
     * Create a test to remove anisotropy
     * 
     * @return The test
     */
    static TestCase createTestRemoveAnisotropy()
    {
        String name = "TexturedSquareWithAnisotropy";
        String modifiedName = name + "-removedAnisotropy";
        DefaultGltfModel gltfModel =
            GltfTestModelCreation.createTexturedSquareWithAnisotropy();
        Consumer<DefaultGltfModel> op = (m) ->
        {
            DefaultPbrMaterialModel material0 =
                (DefaultPbrMaterialModel) m.getMaterialModel(0);
            material0.removeExtensionModel("KHR_materials_anisotropy");
            GltfModelTransforms.revalidate(gltfModel);
        };
        return TestCase.create(name, modifiedName, gltfModel, op);
    }
    
    
    // =========================================================================
    // Utility functions for the tests

    /**
     * Run the given test, writing the given model to a file with the given
     * name, execute the given operation, and write the modified result to
     * another file
     * 
     * @param t The test
     * @throws IOException If an IO error occurs
     */
    static void runTest(TestCase t) throws IOException
    {
        runTest(t.name, t.modifiedName, t.gltfModel, t.op);
    }

    /**
     * Write the given model to a file with the given name, execute the given
     * operation, and write the modified result to another file
     * 
     * @param name The original name
     * @param modifiedName The modified name
     * @param gltfModel The glTF model
     * @param op The operation
     * @throws IOException If an IO error occurs
     */
    private static void runTest(String name, String modifiedName,
        DefaultGltfModel gltfModel, Consumer<DefaultGltfModel> op)
        throws IOException
    {
        File originalFile = prepareOutput(name, ".glb");
        File modifiedFile = prepareOutput(modifiedName, ".gltf");

        logger.log(level, "Transforming " + name);
        logger.log(level, "        into " + modifiedName);

        GltfModelWriter w = new GltfModelWriter();
        w.writeBinary(gltfModel, originalFile);
        op.accept(gltfModel);
        w.write(gltfModel, modifiedFile);
    }

    /**
     * Prepare the specified output file for the test.
     * 
     * This will be of the form './data/name/name.extension'
     * 
     * @param name The name of the subdirectory and the file
     * @param extensionWithDot The file extension
     * @return The file
     * @throws IOException If an IO error occurs
     */
    private static File prepareOutput(String name, String extensionWithDot)
        throws IOException
    {
        Path dir = basePath.resolve(name);
        Files.createDirectories(dir);
        String fileName = name + extensionWithDot;
        File file = dir.resolve(fileName).toFile();
        return file;
    }

}
