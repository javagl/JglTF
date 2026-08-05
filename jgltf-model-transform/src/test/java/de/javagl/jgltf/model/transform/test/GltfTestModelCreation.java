/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.model.transform.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel.Channel;
import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.AnimationModel.Sampler;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.PbrMetallicRoughnessModel;
import de.javagl.jgltf.model.creation.AccessorModels;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.ext.mesh_gpu_instancing.DefaultMeshGpuInstancingModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultChannel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel.DefaultSampler;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultPbrMetallicRoughnessModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.khr.draco_mesh_compression.DefaultDracoMeshCompressionModel;
import de.javagl.jgltf.model.khr.materials_anisotropy.DefaultMaterialsAnisotropyModel;
import de.javagl.jgltf.model.khr.materials_clearcoat.DefaultMaterialsClearcoatModel;
import de.javagl.jgltf.model.khr.materials_dispersion.DefaultMaterialsDispersionModel;
import de.javagl.jgltf.model.khr.materials_emissive_strength.DefaultMaterialsEmissiveStrengthModel;
import de.javagl.jgltf.model.khr.materials_ior.DefaultMaterialsIorModel;
import de.javagl.jgltf.model.khr.materials_iridescence.DefaultMaterialsIridescenceModel;
import de.javagl.jgltf.model.khr.materials_sheen.DefaultMaterialsSheenModel;
import de.javagl.jgltf.model.khr.materials_specular.DefaultMaterialsSpecularModel;
import de.javagl.jgltf.model.khr.materials_transmission.DefaultMaterialsTransmissionModel;
import de.javagl.jgltf.model.khr.materials_variants.DefaultMaterialsVariantsModel;
import de.javagl.jgltf.model.khr.materials_variants.DefaultMeshPrimitiveMaterialsVariantsModel;
import de.javagl.jgltf.model.khr.materials_volume.DefaultMaterialsVolumeModel;
import de.javagl.jgltf.model.khr.texture_transform.DefaultTextureTransformModel;

/**
 * Utility methods to create test models for this package
 */
class GltfTestModelCreation
{
    /**
     * Create an animated square
     * 
     * @return The model
     */
    public static DefaultGltfModel createAnimatedSquare()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitive();

        // Create a mesh model with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create a node model with the mesh
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);

        // Create a scene model with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);

        // Create the animation model
        DefaultAnimationModel animationModel =
            createSimpleRotationAnimation(nodeModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        gltfModelBuilder.addAnimationModel(animationModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create two animated squares
     * 
     * @return The model
     */
    public static DefaultGltfModel createTwoAnimatedSquares()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitive();

        // Create a mesh model with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        DefaultNodeModel nodeModelA = new DefaultNodeModel();
        nodeModelA.addMeshModel(meshModel);

        DefaultNodeModel baseNodeModelA = new DefaultNodeModel();
        baseNodeModelA.setTranslation(new double[]
        { -1.5, 0.0, 0.0 });
        baseNodeModelA.addChild(nodeModelA);

        DefaultNodeModel nodeModelB = new DefaultNodeModel();
        nodeModelB.addMeshModel(meshModel);

        DefaultNodeModel baseNodeModelB = new DefaultNodeModel();
        baseNodeModelB.setTranslation(new double[]
        { 1.5, 0.0, 0.0 });
        baseNodeModelB.addChild(nodeModelB);

        DefaultNodeModel rootNodeModel = new DefaultNodeModel();
        rootNodeModel.addChild(baseNodeModelA);
        rootNodeModel.addChild(baseNodeModelB);

        // Create a scene model with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(rootNodeModel);

        // Create the animation model
        DefaultAnimationModel animationModel =
            createSimpleTranslationAnimation(nodeModelA, nodeModelB);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        gltfModelBuilder.addAnimationModel(animationModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a textured square
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquare()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Assign a material
        DefaultPbrMaterialModel materialModel =
            createBaseColorTextureMaterialModel("baseColor.png");
        meshPrimitiveModel.setMaterialModel(materialModel);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a textured square that uses instancing and draco compression
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquareInstancedDraco()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Assign draco mesh compression to the mesh primitive
        DefaultDracoMeshCompressionModel dracoMeshCompressionModel =
            new DefaultDracoMeshCompressionModel();
        dracoMeshCompressionModel.addAttribute("POSITION");
        dracoMeshCompressionModel.addAttribute("TEXCOORD_0");
        meshPrimitiveModel.addExtensionModel("KHR_draco_mesh_compression",
            dracoMeshCompressionModel);

        // Assign a material
        DefaultPbrMaterialModel materialModel =
            createBaseColorTextureMaterialModel("baseColor.png");
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Create a mesh model with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create a node model with the mesh
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);

        // Assign the instancing extension to the node
        DefaultMeshGpuInstancingModel meshGpuInstancing =
            createMeshGpuInstancing();
        nodeModel.addExtensionModel("EXT_mesh_gpu_instancing",
            meshGpuInstancing);

        // Create a scene model with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a textured square
     * 
     * @return The model
     */
    public static DefaultGltfModel createMorphedSquare()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitive();

        // Add the morph targets
        List<Map<String, AccessorModel>> morphTargets =
            createSquareMeshPrimitiveMorphTargets();
        for (Map<String, AccessorModel> morphTarget : morphTargets)
        {
            meshPrimitiveModel.addTarget(morphTarget);
        }

        // Create a mesh model with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create a node model with the mesh
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);

        // Create a scene model with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);

        // Create a morph target animation
        DefaultAnimationModel animationModel =
            createMorphAnimationModel(nodeModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        gltfModelBuilder.addAnimationModel(animationModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a square with texture coordinates, but without a material
     * 
     * @return The model
     */
    public static DefaultGltfModel createSquareWithTexcoords()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create an textured square including a KHR_materials_clearcoat texture
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquareWithClearcoat()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Assign a material
        DefaultPbrMaterialModel materialModel =
            createBaseColorTextureMaterialModel("baseColor.png");
        meshPrimitiveModel.setMaterialModel(materialModel);

        addClearcoatTexture(materialModel);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a textured square including a KHR_materials_anisotropy
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquareWithAnisotropy()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Assign a material
        DefaultPbrMaterialModel materialModel =
            createBaseColorTextureMaterialModel("baseColor.png");

        // Note: For the anisotropy to be visible, the roughness
        // of the material may not be 1.0 (the lower it is, the
        // more visible the effect is).
        PbrMetallicRoughnessModel pbr =
            materialModel.getPbrMetallicRoughnessModel();
        DefaultPbrMetallicRoughnessModel defaultPbr =
            (DefaultPbrMetallicRoughnessModel) pbr;
        defaultPbr.setRoughnessFactor(0.1);
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Assign the anisotropy extension
        DefaultMaterialsAnisotropyModel anisotropyModel =
            new DefaultMaterialsAnisotropyModel();
        anisotropyModel.setAnisotropyStrength(0.25);
        anisotropyModel.setAnisotropyRotation(Math.toRadians(45.0));
        materialModel.addExtensionModel("KHR_materials_anisotropy",
            anisotropyModel);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a "lens" including a KHR_materials_volume and
     * KHR_materials_dispersion and KHR_materials_transmission and
     * KHR_materials_ior and KHR_materials_sheen, and KHR_materials_specular,
     * just to have all of them in one place...
     * 
     * @return The model
     */
    public static DefaultGltfModel createLens()
    {
        // Create a mesh primitive model for the background
        DefaultMeshPrimitiveModel backgroundMeshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();
        DefaultPbrMaterialModel backgroundMaterialModel =
            createBaseColorTextureMaterialModel("background.png");
        backgroundMeshPrimitiveModel.setMaterialModel(backgroundMaterialModel);
        DefaultMeshModel backgroundMeshModel = new DefaultMeshModel();
        backgroundMeshModel.addMeshPrimitiveModel(backgroundMeshPrimitiveModel);

        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createLensMeshPrimitiveModel();

        // Assign a material
        DefaultPbrMaterialModel materialModel = new DefaultPbrMaterialModel();
        DefaultPbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            new DefaultPbrMetallicRoughnessModel();
        pbrMetallicRoughnessModel.setMetallicFactor(0.0);
        pbrMetallicRoughnessModel.setRoughnessFactor(0.1);
        pbrMetallicRoughnessModel.setBaseColorFactor(new double[]
        { 0.9, 0.1, 0.1, 0.1 });
        materialModel.setPbrMetallicRoughnessModel(pbrMetallicRoughnessModel);
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Assign the volume extension
        DefaultMaterialsVolumeModel volumeModel =
            new DefaultMaterialsVolumeModel();
        volumeModel.setThicknessFactor(0.7);
        volumeModel.setAttenuationDistance(0.9);
        volumeModel.setAttenuationColor(new double[]
        { 0.1, 0.9, 0.1 });
        materialModel.addExtensionModel("KHR_materials_volume", volumeModel);

        // Assign the transmission extension
        DefaultMaterialsTransmissionModel transmissionModel =
            new DefaultMaterialsTransmissionModel();
        transmissionModel.setTransmissionFactor(0.9);
        materialModel.addExtensionModel("KHR_materials_transmission",
            transmissionModel);

        // Assign the dispersion extension
        DefaultMaterialsDispersionModel dispersionModel =
            new DefaultMaterialsDispersionModel();
        dispersionModel.setDispersion(0.1);
        materialModel.addExtensionModel("KHR_materials_dispersion",
            dispersionModel);

        // Assign the IOR extension
        DefaultMaterialsIorModel iorModel = new DefaultMaterialsIorModel();
        iorModel.setIor(1.5);
        materialModel.addExtensionModel("KHR_materials_ior", iorModel);

        // Assign the sheen extension
        DefaultMaterialsSheenModel sheenModel =
            new DefaultMaterialsSheenModel();
        sheenModel.setSheenColorFactor(new double[]
        { 1.0, 1.0, 0.0 });
        sheenModel.setSheenRoughnessFactor(0.9);
        materialModel.addExtensionModel("KHR_materials_sheen", sheenModel);

        // Assign the specular extension
        DefaultMaterialsSpecularModel specularModel =
            new DefaultMaterialsSpecularModel();
        specularModel.setSpecularColorFactor(new double[]
        { 0.0, 0.0, 1.0 });
        materialModel.addExtensionModel("KHR_materials_specular",
            specularModel);

        // Arrange the background behind the main model
        DefaultNodeModel backgroundNode = new DefaultNodeModel();
        backgroundNode.addMeshModel(backgroundMeshModel);
        backgroundNode.setScale(new double[]
        { 2.0, 2.0, 2.0 });
        backgroundNode.setTranslation(new double[]
        { -0.5, -0.5, -0.5 });

        // Attach the main model to a node
        DefaultNodeModel node = new DefaultNodeModel();
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);
        node.addMeshModel(meshModel);

        // Build the actual scene
        DefaultNodeModel root = new DefaultNodeModel();
        root.addChild(backgroundNode);
        root.addChild(node);
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(root);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a textured square including a KHR_materials_iridescence
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquareWithIridescence()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Create a material
        DefaultPbrMaterialModel materialModel = new DefaultPbrMaterialModel();
        materialModel.setDoubleSided(true);
        DefaultPbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            new DefaultPbrMetallicRoughnessModel();
        pbrMetallicRoughnessModel.setBaseColorFactor(new double[]
        { 0.0, 0.0, 0.0, 0.0 });
        pbrMetallicRoughnessModel.setMetallicFactor(0.0);
        pbrMetallicRoughnessModel.setRoughnessFactor(1.0);
        materialModel.setPbrMetallicRoughnessModel(pbrMetallicRoughnessModel);

        // Assign the iridescence extension
        DefaultMaterialsIridescenceModel iridescenceModel =
            new DefaultMaterialsIridescenceModel();
        iridescenceModel.setIridescenceFactor(1.0);
        iridescenceModel.setIridescenceIor(1.5);
        DefaultTextureInfoModel iridescenceTextureInfoModel =
            new DefaultTextureInfoModel();
        DefaultTextureModel textureModel =
            createSimpleTextureModel("iridescence.png", Color.BLACK, Color.RED);
        iridescenceTextureInfoModel.setTextureModel(textureModel);
        iridescenceModel.setIridescenceTexture(iridescenceTextureInfoModel);

        materialModel.addExtensionModel("KHR_materials_iridescence",
            iridescenceModel);

        meshPrimitiveModel.setMaterialModel(materialModel);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create an textured square including a KHR_texture_transform transform
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquareWithTextureTransform()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Assign a material
        DefaultPbrMaterialModel materialModel =
            createBaseColorTextureMaterialModel("baseColor.png");
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Obtain the base color texture info
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            materialModel.getPbrMetallicRoughnessModel();
        DefaultTextureInfoModel baseColorTextureInfoModel =
            (DefaultTextureInfoModel) pbrMetallicRoughnessModel
                .getBaseColorTexture();

        // Create the texture transform and assign it to the base color
        DefaultTextureTransformModel baseColorTextureTransform =
            new DefaultTextureTransformModel();
        baseColorTextureTransform.setOffset(new double[]
        { 0.25, 0.25 });
        baseColorTextureTransform.setScale(new double[]
        { 0.5, 0.5 });
        baseColorTextureTransform.setRotation(Math.toRadians(45.0));
        baseColorTextureInfoModel.addExtensionModel("KHR_texture_transform",
            baseColorTextureTransform);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create an textured square that uses EXT_mesh_gpu_instancing
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquareInstanced()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Assign a material
        DefaultPbrMaterialModel materialModel =
            createBaseColorTextureMaterialModel("baseColor.png");
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Create a mesh with the primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create a node with the mesh
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);

        // Assign the instancing extension to the node
        DefaultMeshGpuInstancingModel meshGpuInstancing =
            createMeshGpuInstancing();
        nodeModel.addExtensionModel("EXT_mesh_gpu_instancing",
            meshGpuInstancing);

        // Create a scene with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a simple mesh GPU instancing instance
     * 
     * @return The instance
     */
    static DefaultMeshGpuInstancingModel createMeshGpuInstancing()
    {
        // Create the instancing extension with some translation
        DefaultMeshGpuInstancingModel meshGpuInstancing =
            new DefaultMeshGpuInstancingModel();
        // @formatter:off
        float translations[] =
        { 
            0.0f, 0.0f, 0.0f, 
            1.5f, 0.0f, 0.0f, 
            3.0f, 0.0f, 0.0f 
        };
        // @formatter:on
        DefaultAccessorModel translationAccessorModel =
            AccessorModels.createFloat3D(FloatBuffer.wrap(translations));
        meshGpuInstancing.setAttribute("TRANSLATION", translationAccessorModel);
        return meshGpuInstancing;
    }

    /**
     * Create a textured square including a KHR_materials_emissive_strength
     * 
     * @return The model
     */
    public static DefaultGltfModel createTexturedSquareWithEmissive()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Create a material with an emissive texture
        DefaultPbrMaterialModel materialModel = new DefaultPbrMaterialModel();
        materialModel.setDoubleSided(true);

        // Assign the base color texture to the material
        DefaultPbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            new DefaultPbrMetallicRoughnessModel();
        pbrMetallicRoughnessModel.setMetallicFactor(0.0);
        pbrMetallicRoughnessModel.setRoughnessFactor(1.0);
        DefaultTextureModel baseColorTextureModel =
            createSimpleTextureModel("baseColor.png");
        DefaultTextureInfoModel baseColorTextureInfoModel =
            new DefaultTextureInfoModel();
        baseColorTextureInfoModel.setTextureModel(baseColorTextureModel);
        pbrMetallicRoughnessModel
            .setBaseColorTexture(baseColorTextureInfoModel);
        materialModel.setPbrMetallicRoughnessModel(pbrMetallicRoughnessModel);

        // Assign the emissive texture to the material
        DefaultTextureInfoModel emissiveTextureInfoModel =
            new DefaultTextureInfoModel();
        DefaultTextureModel emissiveTextureModel =
            createSimpleTextureModel("emissive.png", Color.WHITE, Color.BLACK);
        emissiveTextureInfoModel.setTextureModel(emissiveTextureModel);
        materialModel.setEmissiveTexture(emissiveTextureInfoModel);

        meshPrimitiveModel.setMaterialModel(materialModel);

        // Assign the emissive strength extension to the material
        DefaultMaterialsEmissiveStrengthModel emissiveStrengthModel =
            new DefaultMaterialsEmissiveStrengthModel();
        emissiveStrengthModel.setEmissiveStrength(10.0);
        materialModel.setEmissiveFactor(new double[]
        { 1.0, 1.0, 1.0 });
        materialModel.addExtensionModel("KHR_materials_emissive_strength",
            emissiveStrengthModel);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a scene model with a single node with a single mesh with the given
     * mesh primitive model
     * 
     * @param meshPrimitiveModel The mesh primitive model
     * @return The scene models
     */
    private static DefaultSceneModel
        createSceneWith(DefaultMeshPrimitiveModel meshPrimitiveModel)
    {
        // Create a mesh model with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create a node model with the mesh
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);

        // Create a scene model with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);
        return sceneModel;
    }

    /**
     * Create an animated textured square
     * 
     * @return The model
     */
    public static DefaultGltfModel createAnimatedTexturedSquare()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Assign a material
        DefaultPbrMaterialModel materialModel =
            createBaseColorTextureMaterialModel("baseColor.png");
        meshPrimitiveModel.setMaterialModel(materialModel);

        // Create a mesh model with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create a node model with the mesh
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);

        // Create a scene model with the node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);

        // Create the animation model
        DefaultAnimationModel animationModel =
            createSimpleRotationAnimation(nodeModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        gltfModelBuilder.addAnimationModel(animationModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a basic square mesh primitive
     * 
     * @return The mesh primitive
     */
    private static DefaultMeshPrimitiveModel createSquareMeshPrimitive()
    {
        DefaultAccessorModel indicesAccessorModel = craeteSquareIndices();
        DefaultAccessorModel positionsAccessorModel = createSquarePositions();

        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            new DefaultMeshPrimitiveModel(GltfConstants.GL_TRIANGLES);
        meshPrimitiveModel.setIndices(indicesAccessorModel);
        meshPrimitiveModel.putAttribute("POSITION", positionsAccessorModel);

        return meshPrimitiveModel;
    }

    /**
     * Create a square mesh primitive with texture coordinates
     * 
     * @return The mesh primitive
     */
    private static DefaultMeshPrimitiveModel
        createSquareMeshPrimitiveWithTexcoords()
    {
        DefaultAccessorModel indicesAccessorModel = craeteSquareIndices();
        DefaultAccessorModel positionsAccessorModel = createSquarePositions();
        DefaultAccessorModel texCoordsAccessorModel = createSquareTexCoords();

        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            new DefaultMeshPrimitiveModel(GltfConstants.GL_TRIANGLES);
        meshPrimitiveModel.setIndices(indicesAccessorModel);
        meshPrimitiveModel.putAttribute("POSITION", positionsAccessorModel);
        meshPrimitiveModel.putAttribute("TEXCOORD_0", texCoordsAccessorModel);

        return meshPrimitiveModel;
    }

    /**
     * Create morph targets for the square mesh primitive model
     * 
     * @return The morph targets
     */
    private static List<Map<String, AccessorModel>>
        createSquareMeshPrimitiveMorphTargets()
    {
        Map<String, AccessorModel> target0 =
            new LinkedHashMap<String, AccessorModel>();

        // @formatter:off
        float[] displacements0 = new float[]
        { 
             0.0f, 0.0f, 0.0f, 
             0.0f, 0.0f, 0.0f, 
            -1.0f, 1.0f, 0.0f, 
            -0.5f, 0.5f, 0.0f 
        };
        // @formatter:on
        AccessorModel displacementsAccessorModel0 =
            AccessorModels.createFloat3D(FloatBuffer.wrap(displacements0));
        target0.put("POSITION", displacementsAccessorModel0);

        // @formatter:off
        float[] displacements1 = new float[]
        { 
            0.0f, 0.0f, 0.0f, 
            0.0f, 0.0f, 0.0f, 
            0.5f, 0.5f, 0.0f, 
            1.0f, 1.0f, 0.0f 
        };
        // @formatter:on
        AccessorModel displacementsAccessorModel1 =
            AccessorModels.createFloat3D(FloatBuffer.wrap(displacements1));
        Map<String, AccessorModel> target1 =
            new LinkedHashMap<String, AccessorModel>();
        target1.put("POSITION", displacementsAccessorModel1);

        return Arrays.asList(target0, target1);
    }

    /**
     * Create an animation of the TWO morph targets for the given node
     * 
     * @param nodeModel The node model
     * @return The animation model
     */
    private static DefaultAnimationModel
        createMorphAnimationModel(NodeModel nodeModel)
    {
        DefaultAnimationModel animationModel = new DefaultAnimationModel();

        // @formatter:off
        float[] times = new float[]
        { 
            0.0f, 1.0f, 2.0f, 3.0f, 4.0f 
        };
        // @formatter:on
        AccessorModel timesAccessorModel =
            AccessorModels.createFloatScalar(FloatBuffer.wrap(times));

        // @formatter:off
        float[] weights = new float[]
        { 
            0.0f, 0.0f, 
            1.0f, 0.0f, 
            1.0f, 1.0f, 
            0.0f, 1.0f, 
            0.0f, 0.0f 
        };
        // @formatter:on
        AccessorModel weightsAccessorModel =
            AccessorModels.createFloatScalar(FloatBuffer.wrap(weights));

        Sampler sampler = new DefaultSampler(timesAccessorModel,
            Interpolation.LINEAR, weightsAccessorModel);
        Channel channel = new DefaultChannel(sampler, nodeModel, "weights");
        animationModel.addChannel(channel);
        return animationModel;

    }

    /**
     * Create the accessor model for the square indices
     * 
     * @return The accessor model
     */
    private static DefaultAccessorModel craeteSquareIndices()
    {
        // @formatter:off
        short[] indices = new short[]
        { 
            0, 1, 2, 
            1, 3, 2
        };
        // @formatter:on
        DefaultAccessorModel indicesAccessorModel =
            AccessorModels.createUnsignedShortScalar(ShortBuffer.wrap(indices));
        return indicesAccessorModel;
    }

    /**
     * Create the accessor model for the square positions
     * 
     * @return The accessor model
     */
    private static DefaultAccessorModel createSquarePositions()
    {
        // Create the positions accessor
        // @formatter:off
        float[] positions = new float[]
        {   
            0.0f, 0.0f, 0.0f, 
            1.0f, 0.0f, 0.0f, 
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f 
        };
        // @formatter:on
        DefaultAccessorModel positionsAccessorModel =
            AccessorModels.createFloat3D(FloatBuffer.wrap(positions));
        return positionsAccessorModel;
    }

    /**
     * Create the accessor model for the square texture coordinates
     * 
     * @return The accessor model
     */
    private static DefaultAccessorModel createSquareTexCoords()
    {
        // @formatter:off
        float[] texCoords = new float[]
        { 
            0.0f, 1.0f, 
            1.0f, 1.0f, 
            0.0f, 0.0f, 
            1.0f, 0.0f 
        };
        // @formatter:on
        DefaultAccessorModel texCoordsAccessorModel =
            AccessorModels.createFloat2D(FloatBuffer.wrap(texCoords));
        return texCoordsAccessorModel;
    }

    /**
     * Create a simple animation model that rotates the given node
     * 
     * @param nodeModel The node
     * @return The animation model
     */
    static DefaultAnimationModel
        createSimpleRotationAnimation(NodeModel nodeModel)
    {
        // Create the times accessor
        float[] times = new float[]
        { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f };
        DefaultAccessorModel timesAccessorModel =
            AccessorModels.createFloatScalar(FloatBuffer.wrap(times));

        // Create the rotation accessor
        // @formatter:off
        float[] rotations = new float[]
        { 
            0.0f, 0.0f, 0.0f, 1.0f, 
            0.0f, 0.0f, 0.707f, 0.707f, 
            0.0f, 0.0f, 1.0f,  0.0f, 
            0.0f, 0.0f, 0.707f, -0.707f, 
            0.0f, 0.0f, 0.0f, 1.0f 
        };
        // @formatter:on
        DefaultAccessorModel rotationsAccessorModel =
            AccessorModels.createFloat4D(FloatBuffer.wrap(rotations));

        // Create the animation model with one channel and sampler for rotation
        DefaultAnimationModel animationModel = new DefaultAnimationModel();
        Sampler sampler = new DefaultSampler(timesAccessorModel,
            Interpolation.LINEAR, rotationsAccessorModel);
        Channel channel = new DefaultChannel(sampler, nodeModel, "rotation");
        animationModel.addChannel(channel);

        return animationModel;
    }

    /**
     * Create a simple animation model that applies a translation to the given
     * nodes.
     * 
     * @param nodeModelA The first node
     * @param nodeModelB The second node
     * @return The animation model
     */
    static DefaultAnimationModel createSimpleTranslationAnimation(
        NodeModel nodeModelA, NodeModel nodeModelB)
    {
        // Create the times accessor
        float[] times = new float[]
        { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f };
        DefaultAccessorModel timesAccessorModel =
            AccessorModels.createFloatScalar(FloatBuffer.wrap(times));

        // Create the translation accessors
        // @formatter:off
        float[] translationsA = new float[]
        { 
            0.0f, 0.0f, 0.0f, 
            0.0f, 0.5f, 0.0f, 
            0.0f, 1.0f, 0.0f, 
            0.0f, 0.5f, 0.0f, 
            0.0f, 0.0f, 0.0f, 
        };
        float[] translationsB = new float[]
        { 
            0.0f,  0.0f, 0.0f, 
            0.0f, -0.5f, 0.0f, 
            0.0f, -1.0f, 0.0f, 
            0.0f, -0.5f, 0.0f, 
            0.0f,  0.0f, 0.0f, 
        };
        // @formatter:on
        DefaultAccessorModel translationsAccessorModelA =
            AccessorModels.createFloat3D(FloatBuffer.wrap(translationsA));
        DefaultAccessorModel translationsAccessorModelB =
            AccessorModels.createFloat3D(FloatBuffer.wrap(translationsB));

        // Create the animation model
        DefaultAnimationModel animationModel = new DefaultAnimationModel();

        Sampler samplerA = new DefaultSampler(timesAccessorModel,
            Interpolation.LINEAR, translationsAccessorModelA);
        Channel channelA =
            new DefaultChannel(samplerA, nodeModelA, "translation");
        animationModel.addChannel(channelA);

        Sampler samplerB = new DefaultSampler(timesAccessorModel,
            Interpolation.LINEAR, translationsAccessorModelB);
        Channel channelB =
            new DefaultChannel(samplerB, nodeModelB, "translation");
        animationModel.addChannel(channelB);

        return animationModel;
    }

    /**
     * Create a mesh primitive model that resembles a "lens"
     * 
     * @return The mesh primitive model
     */
    private static DefaultMeshPrimitiveModel createLensMeshPrimitiveModel()
    {
        // Come on, criticize this. It looks nice, and serves its purpose.

        // @formatter:off
        short indices[] = new short[] {
            0,    25,    23, 
            5,    38,    52, 
            4,    24,    80, 
            3,    79,   101, 
            2,   100,    54, 
            5,    52,   119, 
            6,    45,   127, 
            8,    87,   135, 
            9,   108,   147, 
           10,   115,   155, 
            5,   119,   128, 
            6,   127,   136, 
            8,   135,   148, 
            9,   147,   156, 
           10,   155,   120, 
          122,   160,   123, 
          121,   161,   122, 
          120,   163,   121, 
          122,   161,   160, 
          161,   162,   160, 
          121,   163,   161, 
          163,   164,   161, 
          161,   164,   162, 
          164,   165,   162, 
          120,   155,   163, 
          155,   154,   163, 
          163,   154,   164, 
          154,   153,   164, 
          164,   153,   165, 
          153,   152,   165, 
          158,   166,   159, 
          157,   167,   158, 
          156,   169,   157, 
          158,   167,   166, 
          167,   168,   166, 
          157,   169,   167, 
          169,   170,   167, 
          167,   170,   168, 
          170,   171,   168, 
          156,   147,   169, 
          147,   146,   169, 
          169,   146,   170, 
          146,   145,   170, 
          170,   145,   171, 
          145,   144,   171, 
          150,   172,   151, 
          149,   173,   150, 
          148,   175,   149, 
          150,   173,   172, 
          173,   174,   172, 
          149,   175,   173, 
          175,   176,   173, 
          173,   176,   174, 
          176,   177,   174, 
          148,   135,   175, 
          135,   134,   175, 
          175,   134,   176, 
          134,   133,   176, 
          176,   133,   177, 
          133,   132,   177, 
          140,   178,   142, 
          138,   179,   140, 
          136,   181,   138, 
          140,   179,   178, 
          179,   180,   178, 
          138,   181,   179, 
          181,   182,   179, 
          179,   182,   180, 
          182,   183,   180, 
          136,   127,   181, 
          127,   126,   181, 
          181,   126,   182, 
          126,   125,   182, 
          182,   125,   183, 
          125,   124,   183, 
          130,   184,   131, 
          129,   185,   130, 
          128,   187,   129, 
          130,   185,   184, 
          185,   186,   184, 
          129,   187,   185, 
          187,   188,   185, 
          185,   188,   186, 
          188,   189,   186, 
          128,   119,   187, 
          119,   118,   187, 
          187,   118,   188, 
          118,   117,   188, 
          188,   117,   189, 
          117,   116,   189, 
          153,   193,   152, 
          154,   197,   153, 
          155,   202,   154, 
          152,   193,   190, 
          193,   194,   190, 
          190,   194,   191, 
          194,   195,   191, 
          191,   195,   192, 
          195,   196,   192, 
          192,   196,   159, 
          196,   158,   159, 
          153,   197,   193, 
          197,   198,   193, 
          193,   198,   194, 
          198,   199,   194, 
          194,   199,   195, 
          199,   200,   195, 
          195,   200,   196, 
          200,   201,   196, 
          196,   201,   158, 
          201,   157,   158, 
          154,   202,   197, 
          202,   203,   197, 
          197,   203,   198, 
          203,   204,   198, 
          198,   204,   199, 
          204,   205,   199, 
          199,   205,   200, 
          205,   206,   200, 
          200,   206,   201, 
          206,   207,   201, 
          201,   207,   157, 
          207,   156,   157, 
          155,   115,   202, 
          115,   114,   202, 
          202,   114,   203, 
          114,   113,   203, 
          203,   113,   204, 
          113,   112,   204, 
          204,   112,   205, 
          112,   111,   205, 
          205,   111,   206, 
          111,   110,   206, 
          206,   110,   207, 
          110,   109,   207, 
          207,   109,   156, 
          109,     9,   156, 
          145,   211,   144, 
          146,   215,   145, 
          147,   220,   146, 
          144,   211,   208, 
          211,   212,   208, 
          208,   212,   209, 
          212,   213,   209, 
          209,   213,   210, 
          213,   214,   210, 
          210,   214,   151, 
          214,   150,   151, 
          145,   215,   211, 
          215,   216,   211, 
          211,   216,   212, 
          216,   217,   212, 
          212,   217,   213, 
          217,   218,   213, 
          213,   218,   214, 
          218,   219,   214, 
          214,   219,   150, 
          219,   149,   150, 
          146,   220,   215, 
          220,   221,   215, 
          215,   221,   216, 
          221,   222,   216, 
          216,   222,   217, 
          222,   223,   217, 
          217,   223,   218, 
          223,   224,   218, 
          218,   224,   219, 
          224,   225,   219, 
          219,   225,   149, 
          225,   148,   149, 
          147,   108,   220, 
          108,   107,   220, 
          220,   107,   221, 
          107,   106,   221, 
          221,   106,   222, 
          106,   105,   222, 
          222,   105,   223, 
          105,   104,   223, 
          223,   104,   224, 
          104,   103,   224, 
          224,   103,   225, 
          103,   102,   225, 
          225,   102,   148, 
          102,     8,   148, 
          133,   229,   132, 
          134,   233,   133, 
          135,   238,   134, 
          132,   229,   226, 
          229,   230,   226, 
          226,   230,   227, 
          230,   231,   227, 
          227,   231,   228, 
          231,   232,   228, 
          228,   232,   143, 
          232,   141,   143, 
          133,   233,   229, 
          233,   234,   229, 
          229,   234,   230, 
          234,   235,   230, 
          230,   235,   231, 
          235,   236,   231, 
          231,   236,   232, 
          236,   237,   232, 
          232,   237,   141, 
          237,   139,   141, 
          134,   238,   233, 
          238,   239,   233, 
          233,   239,   234, 
          239,   240,   234, 
          234,   240,   235, 
          240,   241,   235, 
          235,   241,   236, 
          241,   242,   236, 
          236,   242,   237, 
          242,   243,   237, 
          237,   243,   139, 
          243,   137,   139, 
          135,    87,   238, 
           87,    86,   238, 
          238,    86,   239, 
           86,    85,   239, 
          239,    85,   240, 
           85,    84,   240, 
          240,    84,   241, 
           84,    83,   241, 
          241,    83,   242, 
           83,    82,   242, 
          242,    82,   243, 
           82,    81,   243, 
          243,    81,   137, 
           81,     7,   137, 
          125,   247,   124, 
          126,   251,   125, 
          127,   256,   126, 
          124,   247,   244, 
          247,   248,   244, 
          244,   248,   245, 
          248,   249,   245, 
          245,   249,   246, 
          249,   250,   246, 
          246,   250,   131, 
          250,   130,   131, 
          125,   251,   247, 
          251,   252,   247, 
          247,   252,   248, 
          252,   253,   248, 
          248,   253,   249, 
          253,   254,   249, 
          249,   254,   250, 
          254,   255,   250, 
          250,   255,   130, 
          255,   129,   130, 
          126,   256,   251, 
          256,   257,   251, 
          251,   257,   252, 
          257,   258,   252, 
          252,   258,   253, 
          258,   259,   253, 
          253,   259,   254, 
          259,   260,   254, 
          254,   260,   255, 
          260,   261,   255, 
          255,   261,   129, 
          261,   128,   129, 
          127,    45,   256, 
           45,    44,   256, 
          256,    44,   257, 
           44,    43,   257, 
          257,    43,   258, 
           43,    42,   258, 
          258,    42,   259, 
           42,    41,   259, 
          259,    41,   260, 
           41,    40,   260, 
          260,    40,   261, 
           40,    39,   261, 
          261,    39,   128, 
           39,     5,   128, 
          117,   265,   116, 
          118,   269,   117, 
          119,   274,   118, 
          116,   265,   262, 
          265,   266,   262, 
          262,   266,   263, 
          266,   267,   263, 
          263,   267,   264, 
          267,   268,   264, 
          264,   268,   123, 
          268,   122,   123, 
          117,   269,   265, 
          269,   270,   265, 
          265,   270,   266, 
          270,   271,   266, 
          266,   271,   267, 
          271,   272,   267, 
          267,   272,   268, 
          272,   273,   268, 
          268,   273,   122, 
          273,   121,   122, 
          118,   274,   269, 
          274,   275,   269, 
          269,   275,   270, 
          275,   276,   270, 
          270,   276,   271, 
          276,   277,   271, 
          271,   277,   272, 
          277,   278,   272, 
          272,   278,   273, 
          278,   279,   273, 
          273,   279,   121, 
          279,   120,   121, 
          119,    52,   274, 
           52,    51,   274, 
          274,    51,   275, 
           51,    50,   275, 
          275,    50,   276, 
           50,    49,   276, 
          276,    49,   277, 
           49,    48,   277, 
          277,    48,   278, 
           48,    47,   278, 
          278,    47,   279, 
           47,    46,   279, 
          279,    46,   120, 
           46,    10,   120, 
           66,   115,    10, 
           64,   280,    66, 
           62,   281,    64, 
           60,   283,    62, 
           58,   286,    60, 
           56,   290,    58, 
           54,   295,    56, 
           66,   280,   115, 
          280,   114,   115, 
           64,   281,   280, 
          281,   282,   280, 
          280,   282,   114, 
          282,   113,   114, 
           62,   283,   281, 
          283,   284,   281, 
          281,   284,   282, 
          284,   285,   282, 
          282,   285,   113, 
          285,   112,   113, 
           60,   286,   283, 
          286,   287,   283, 
          283,   287,   284, 
          287,   288,   284, 
          284,   288,   285, 
          288,   289,   285, 
          285,   289,   112, 
          289,   111,   112, 
           58,   290,   286, 
          290,   291,   286, 
          286,   291,   287, 
          291,   292,   287, 
          287,   292,   288, 
          292,   293,   288, 
          288,   293,   289, 
          293,   294,   289, 
          289,   294,   111, 
          294,   110,   111, 
           56,   295,   290, 
          295,   296,   290, 
          290,   296,   291, 
          296,   297,   291, 
          291,   297,   292, 
          297,   298,   292, 
          292,   298,   293, 
          298,   299,   293, 
          293,   299,   294, 
          299,   300,   294, 
          294,   300,   110, 
          300,   109,   110, 
           54,   100,   295, 
          100,    98,   295, 
          295,    98,   296, 
           98,    96,   296, 
          296,    96,   297, 
           96,    94,   297, 
          297,    94,   298, 
           94,    92,   298, 
          298,    92,   299, 
           92,    90,   299, 
          299,    90,   300, 
           90,    88,   300, 
          300,    88,   109, 
           88,     9,   109, 
           89,   108,     9, 
           91,   301,    89, 
           93,   302,    91, 
           95,   304,    93, 
           97,   307,    95, 
           99,   311,    97, 
          101,   316,    99, 
           89,   301,   108, 
          301,   107,   108, 
           91,   302,   301, 
          302,   303,   301, 
          301,   303,   107, 
          303,   106,   107, 
           93,   304,   302, 
          304,   305,   302, 
          302,   305,   303, 
          305,   306,   303, 
          303,   306,   106, 
          306,   105,   106, 
           95,   307,   304, 
          307,   308,   304, 
          304,   308,   305, 
          308,   309,   305, 
          305,   309,   306, 
          309,   310,   306, 
          306,   310,   105, 
          310,   104,   105, 
           97,   311,   307, 
          311,   312,   307, 
          307,   312,   308, 
          312,   313,   308, 
          308,   313,   309, 
          313,   314,   309, 
          309,   314,   310, 
          314,   315,   310, 
          310,   315,   104, 
          315,   103,   104, 
           99,   316,   311, 
          316,   317,   311, 
          311,   317,   312, 
          317,   318,   312, 
          312,   318,   313, 
          318,   319,   313, 
          313,   319,   314, 
          319,   320,   314, 
          314,   320,   315, 
          320,   321,   315, 
          315,   321,   103, 
          321,   102,   103, 
          101,    79,   316, 
           79,    77,   316, 
          316,    77,   317, 
           77,    75,   317, 
          317,    75,   318, 
           75,    73,   318, 
          318,    73,   319, 
           73,    71,   319, 
          319,    71,   320, 
           71,    69,   320, 
          320,    69,   321, 
           69,    67,   321, 
          321,    67,   102, 
           67,     8,   102, 
           68,    87,     8, 
           70,   322,    68, 
           72,   323,    70, 
           74,   325,    72, 
           76,   328,    74, 
           78,   332,    76, 
           80,   337,    78, 
           68,   322,    87, 
          322,    86,    87, 
           70,   323,   322, 
          323,   324,   322, 
          322,   324,    86, 
          324,    85,    86, 
           72,   325,   323, 
          325,   326,   323, 
          323,   326,   324, 
          326,   327,   324, 
          324,   327,    85, 
          327,    84,    85, 
           74,   328,   325, 
          328,   329,   325, 
          325,   329,   326, 
          329,   330,   326, 
          326,   330,   327, 
          330,   331,   327, 
          327,   331,    84, 
          331,    83,    84, 
           76,   332,   328, 
          332,   333,   328, 
          328,   333,   329, 
          333,   334,   329, 
          329,   334,   330, 
          334,   335,   330, 
          330,   335,   331, 
          335,   336,   331, 
          331,   336,    83, 
          336,    82,    83, 
           78,   337,   332, 
          337,   338,   332, 
          332,   338,   333, 
          338,   339,   333, 
          333,   339,   334, 
          339,   340,   334, 
          334,   340,   335, 
          340,   341,   335, 
          335,   341,   336, 
          341,   342,   336, 
          336,   342,    82, 
          342,    81,    82, 
           80,    24,   337, 
           24,    22,   337, 
          337,    22,   338, 
           22,    20,   338, 
          338,    20,   339, 
           20,    18,   339, 
          339,    18,   340, 
           18,    16,   340, 
          340,    16,   341, 
           16,    14,   341, 
          341,    14,   342, 
           14,    12,   342, 
          342,    12,    81, 
           12,     7,    81, 
           46,    65,    10, 
           47,   343,    46, 
           48,   344,    47, 
           49,   346,    48, 
           50,   349,    49, 
           51,   353,    50, 
           52,   358,    51, 
           46,   343,    65, 
          343,    63,    65, 
           47,   344,   343, 
          344,   345,   343, 
          343,   345,    63, 
          345,    61,    63, 
           48,   346,   344, 
          346,   347,   344, 
          344,   347,   345, 
          347,   348,   345, 
          345,   348,    61, 
          348,    59,    61, 
           49,   349,   346, 
          349,   350,   346, 
          346,   350,   347, 
          350,   351,   347, 
          347,   351,   348, 
          351,   352,   348, 
          348,   352,    59, 
          352,    57,    59, 
           50,   353,   349, 
          353,   354,   349, 
          349,   354,   350, 
          354,   355,   350, 
          350,   355,   351, 
          355,   356,   351, 
          351,   356,   352, 
          356,   357,   352, 
          352,   357,    57, 
          357,    55,    57, 
           51,   358,   353, 
          358,   359,   353, 
          353,   359,   354, 
          359,   360,   354, 
          354,   360,   355, 
          360,   361,   355, 
          355,   361,   356, 
          361,   362,   356, 
          356,   362,   357, 
          362,   363,   357, 
          357,   363,    55, 
          363,    53,    55, 
           52,    38,   358, 
           38,    36,   358, 
          358,    36,   359, 
           36,    34,   359, 
          359,    34,   360, 
           34,    32,   360, 
          360,    32,   361, 
           32,    30,   361, 
          361,    30,   362, 
           30,    28,   362, 
          362,    28,   363, 
           28,    26,   363, 
          363,    26,    53, 
           26,     1,    53, 
           11,    45,     6, 
           13,   364,    11, 
           15,   365,    13, 
           17,   367,    15, 
           19,   370,    17, 
           21,   374,    19, 
           23,   379,    21, 
           11,   364,    45, 
          364,    44,    45, 
           13,   365,   364, 
          365,   366,   364, 
          364,   366,    44, 
          366,    43,    44, 
           15,   367,   365, 
          367,   368,   365, 
          365,   368,   366, 
          368,   369,   366, 
          366,   369,    43, 
          369,    42,    43, 
           17,   370,   367, 
          370,   371,   367, 
          367,   371,   368, 
          371,   372,   368, 
          368,   372,   369, 
          372,   373,   369, 
          369,   373,    42, 
          373,    41,    42, 
           19,   374,   370, 
          374,   375,   370, 
          370,   375,   371, 
          375,   376,   371, 
          371,   376,   372, 
          376,   377,   372, 
          372,   377,   373, 
          377,   378,   373, 
          373,   378,    41, 
          378,    40,    41, 
           21,   379,   374, 
          379,   380,   374, 
          374,   380,   375, 
          380,   381,   375, 
          375,   381,   376, 
          381,   382,   376, 
          376,   382,   377, 
          382,   383,   377, 
          377,   383,   378, 
          383,   384,   378, 
          378,   384,    40, 
          384,    39,    40, 
           23,    25,   379, 
           25,    27,   379, 
          379,    27,   380, 
           27,    29,   380, 
          380,    29,   381, 
           29,    31,   381, 
          381,    31,   382, 
           31,    33,   382, 
          382,    33,   383, 
           33,    35,   383, 
          383,    35,   384, 
           35,    37,   384, 
          384,    37,    39, 
           37,     5,    39
        };
        float positions[] = new float[] 
        {
            0.50000f,  0.50000f,  0.12500f, 
            0.50000f,  0.50000f,  0.12500f, 
            0.50000f,  0.50000f,  0.12500f, 
            0.50000f,  0.50000f,  0.12500f, 
            0.50000f,  0.50000f,  0.12500f, 
            0.86180f,  0.76286f,  0.05590f, 
            0.36181f,  0.92532f,  0.05590f, 
            0.36181f,  0.92532f,  0.05590f, 
            0.05279f,  0.50000f,  0.05590f, 
            0.36181f,  0.07468f,  0.05590f, 
            0.86180f,  0.23714f,  0.05590f, 
            0.37103f,  0.89693f,  0.06884f, 
            0.37103f,  0.89693f,  0.06884f, 
            0.38359f,  0.85828f,  0.08219f, 
            0.38359f,  0.85828f,  0.08219f, 
            0.39966f,  0.80883f,  0.09505f, 
            0.39966f,  0.80883f,  0.09505f, 
            0.41877f,  0.75000f,  0.10633f, 
            0.41877f,  0.75000f,  0.10633f, 
            0.43979f,  0.68530f,  0.11512f, 
            0.43979f,  0.68530f,  0.11512f, 
            0.46120f,  0.61943f,  0.12099f, 
            0.46120f,  0.61943f,  0.12099f, 
            0.48158f,  0.55670f,  0.12411f, 
            0.48158f,  0.55670f,  0.12411f, 
            0.54824f,  0.53504f,  0.12411f, 
            0.54824f,  0.53504f,  0.12411f, 
            0.60159f,  0.57381f,  0.12099f, 
            0.60159f,  0.57381f,  0.12099f, 
            0.65763f,  0.61452f,  0.11512f, 
            0.65763f,  0.61452f,  0.11512f, 
            0.71266f,  0.65451f,  0.10633f, 
            0.71266f,  0.65451f,  0.10633f, 
            0.76271f,  0.69087f,  0.09505f, 
            0.76271f,  0.69087f,  0.09505f, 
            0.80477f,  0.72143f,  0.08219f, 
            0.80477f,  0.72143f,  0.08219f, 
            0.83765f,  0.74531f,  0.06884f, 
            0.83765f,  0.74531f,  0.06884f, 
            0.81923f,  0.80202f,  0.05962f, 
            0.76597f,  0.84086f,  0.06279f, 
            0.70250f,  0.87617f,  0.06495f, 
            0.63143f,  0.90451f,  0.06572f, 
            0.55728f,  0.92336f,  0.06495f, 
            0.48518f,  0.93209f,  0.06279f, 
            0.41927f,  0.93198f,  0.05962f, 
            0.88589f,  0.28973f,  0.05962f, 
            0.90636f,  0.35238f,  0.06279f, 
            0.92034f,  0.42365f,  0.06495f, 
            0.92532f,  0.50000f,  0.06572f, 
            0.92034f,  0.57635f,  0.06495f, 
            0.90636f,  0.64762f,  0.06279f, 
            0.88589f,  0.71027f,  0.05962f, 
            0.54824f,  0.46496f,  0.12411f, 
            0.54824f,  0.46496f,  0.12411f, 
            0.60159f,  0.42619f,  0.12099f, 
            0.60159f,  0.42619f,  0.12099f, 
            0.65763f,  0.38548f,  0.11512f, 
            0.65763f,  0.38548f,  0.11512f, 
            0.71266f,  0.34549f,  0.10633f, 
            0.71266f,  0.34549f,  0.10633f, 
            0.76271f,  0.30913f,  0.09505f, 
            0.76271f,  0.30913f,  0.09505f, 
            0.80477f,  0.27857f,  0.08219f, 
            0.80477f,  0.27857f,  0.08219f, 
            0.83765f,  0.25469f,  0.06884f, 
            0.83765f,  0.25469f,  0.06884f, 
            0.08264f,  0.50000f,  0.06884f, 
            0.08264f,  0.50000f,  0.06884f, 
            0.12328f,  0.50000f,  0.08219f, 
            0.12328f,  0.50000f,  0.08219f, 
            0.17527f,  0.50000f,  0.09505f, 
            0.17527f,  0.50000f,  0.09505f, 
            0.23714f,  0.50000f,  0.10633f, 
            0.23714f,  0.50000f,  0.10633f, 
            0.30516f,  0.50000f,  0.11512f, 
            0.30516f,  0.50000f,  0.11512f, 
            0.37443f,  0.50000f,  0.12099f, 
            0.37443f,  0.50000f,  0.12099f, 
            0.44038f,  0.50000f,  0.12411f, 
            0.44038f,  0.50000f,  0.12411f, 
            0.31141f,  0.89693f,  0.05962f, 
            0.25801f,  0.85828f,  0.06279f, 
            0.20482f,  0.80883f,  0.06495f, 
            0.15591f,  0.75000f,  0.06572f, 
            0.11506f,  0.68530f,  0.06495f, 
            0.08447f,  0.61943f,  0.06279f, 
            0.06422f,  0.55670f,  0.05962f, 
            0.37103f,  0.10307f,  0.06884f, 
            0.37103f,  0.10307f,  0.06884f, 
            0.38359f,  0.14172f,  0.08219f, 
            0.38359f,  0.14172f,  0.08219f, 
            0.39966f,  0.19117f,  0.09505f, 
            0.39966f,  0.19117f,  0.09505f, 
            0.41877f,  0.25000f,  0.10633f, 
            0.41877f,  0.25000f,  0.10633f, 
            0.43979f,  0.31470f,  0.11512f, 
            0.43979f,  0.31470f,  0.11512f, 
            0.46120f,  0.38057f,  0.12099f, 
            0.46120f,  0.38057f,  0.12099f, 
            0.48158f,  0.44330f,  0.12411f, 
            0.48158f,  0.44330f,  0.12411f, 
            0.06422f,  0.44330f,  0.05962f, 
            0.08447f,  0.38057f,  0.06279f, 
            0.11506f,  0.31470f,  0.06495f, 
            0.15591f,  0.25000f,  0.06572f, 
            0.20482f,  0.19117f,  0.06495f, 
            0.25801f,  0.14172f,  0.06279f, 
            0.31141f,  0.10307f,  0.05962f, 
            0.41927f,  0.06802f,  0.05962f, 
            0.48518f,  0.06791f,  0.06279f, 
            0.55728f,  0.07664f,  0.06495f, 
            0.63143f,  0.09549f,  0.06572f, 
            0.70250f,  0.12383f,  0.06495f, 
            0.76597f,  0.15914f,  0.06279f, 
            0.81923f,  0.19798f,  0.05962f, 
            0.97553f,  0.65451f,  0.00000f, 
            0.95755f,  0.69087f,  0.01624f, 
            0.93035f,  0.72143f,  0.03139f, 
            0.89727f,  0.74531f,  0.04472f, 
            0.89727f,  0.25469f,  0.04472f, 
            0.93035f,  0.27857f,  0.03139f, 
            0.95755f,  0.30913f,  0.01624f, 
            0.97553f,  0.34549f, -0.00000f, 
            0.50000f,  1.00000f,  0.00000f, 
            0.45986f,  0.99414f,  0.01624f, 
            0.42239f,  0.97771f,  0.03139f, 
            0.38946f,  0.95364f,  0.04472f, 
            0.85608f,  0.80202f,  0.04472f, 
            0.84358f,  0.84086f,  0.03139f, 
            0.82292f,  0.87617f,  0.01624f, 
            0.79389f,  0.90451f, -0.00000f, 
            0.02447f,  0.65451f,  0.00000f, 
            0.01764f,  0.61452f,  0.01624f, 
            0.02169f,  0.57381f,  0.03139f, 
            0.03441f,  0.53504f,  0.04472f, 
            0.32280f,  0.93198f,  0.04472f, 
            0.32280f,  0.93198f,  0.04472f, 
            0.28200f,  0.93209f,  0.03139f, 
            0.28200f,  0.93209f,  0.03139f, 
            0.24203f,  0.92336f,  0.01624f, 
            0.24203f,  0.92336f,  0.01624f, 
            0.20611f,  0.90451f, -0.00000f, 
            0.20611f,  0.90451f, -0.00000f, 
            0.20611f,  0.09549f,  0.00000f, 
            0.24203f,  0.07664f,  0.01624f, 
            0.28200f,  0.06791f,  0.03139f, 
            0.32280f,  0.06802f,  0.04472f, 
            0.03441f,  0.46496f,  0.04472f, 
            0.02169f,  0.42619f,  0.03139f, 
            0.01764f,  0.38548f,  0.01624f, 
            0.02447f,  0.34549f, -0.00000f, 
            0.79389f,  0.09549f,  0.00000f, 
            0.82292f,  0.12383f,  0.01624f, 
            0.84358f,  0.15914f,  0.03139f, 
            0.85608f,  0.19798f,  0.04472f, 
            0.38946f,  0.04636f,  0.04472f, 
            0.42239f,  0.02229f,  0.03139f, 
            0.45986f,  0.00586f,  0.01624f, 
            0.50000f,  0.00000f, -0.00000f, 
            0.94640f,  0.27478f,  0.00000f, 
            0.92265f,  0.24126f,  0.01663f, 
            0.90451f,  0.20611f,  0.00000f, 
            0.89125f,  0.21574f,  0.03174f, 
            0.87668f,  0.17800f,  0.01663f, 
            0.85215f,  0.14505f, -0.00000f, 
            0.42375f,  0.00585f,  0.00000f, 
            0.38453f,  0.01809f,  0.01663f, 
            0.34549f,  0.02447f,  0.00000f, 
            0.35056f,  0.04006f,  0.03174f, 
            0.31016f,  0.04225f,  0.01663f, 
            0.27124f,  0.05540f,  0.00000f, 
            0.00647f,  0.41982f, -0.00000f, 
            0.00599f,  0.46090f,  0.01663f, 
            0.00000f,  0.50000f, -0.00000f, 
            0.01639f,  0.50000f,  0.03174f, 
            0.00599f,  0.53910f,  0.01663f, 
            0.00647f,  0.58018f, -0.00000f, 
            0.27124f,  0.94460f,  0.00000f, 
            0.31016f,  0.95775f,  0.01663f, 
            0.34549f,  0.97553f,  0.00000f, 
            0.35056f,  0.95994f,  0.03174f, 
            0.38453f,  0.98191f,  0.01663f, 
            0.42375f,  0.99415f,  0.00000f, 
            0.85215f,  0.85495f,  0.00000f, 
            0.87668f,  0.82200f,  0.01663f, 
            0.90451f,  0.79389f,  0.00000f, 
            0.89125f,  0.78426f,  0.03174f, 
            0.92264f,  0.75874f,  0.01663f, 
            0.94640f,  0.72522f,  0.00000f, 
            0.72876f,  0.05540f,  0.00000f, 
            0.65451f,  0.02447f,  0.00000f, 
            0.57625f,  0.00585f, -0.00000f, 
            0.76287f,  0.08018f,  0.01705f, 
            0.69160f,  0.04351f,  0.01751f, 
            0.61331f,  0.01807f,  0.01751f, 
            0.53410f,  0.00585f,  0.01705f, 
            0.78961f,  0.11474f,  0.03326f, 
            0.72361f,  0.07468f,  0.03455f, 
            0.64832f,  0.04351f,  0.03501f, 
            0.56910f,  0.02447f,  0.03455f, 
            0.49215f,  0.01809f,  0.03326f, 
            0.80815f,  0.15537f,  0.04761f, 
            0.74851f,  0.11474f,  0.04989f, 
            0.67856f,  0.08018f,  0.05115f, 
            0.60231f,  0.05540f,  0.05115f, 
            0.52541f,  0.04225f,  0.04989f, 
            0.45327f,  0.04006f,  0.04761f, 
            0.14785f,  0.14505f,  0.00000f, 
            0.09549f,  0.20611f,  0.00000f, 
            0.05360f,  0.27478f, -0.00000f, 
            0.18196f,  0.12027f,  0.01705f, 
            0.12506f,  0.17671f,  0.01751f, 
            0.07667f,  0.24331f,  0.01751f, 
            0.04057f,  0.31487f,  0.01705f, 
            0.22309f,  0.10551f,  0.03326f, 
            0.16459f,  0.15590f,  0.03455f, 
            0.11169f,  0.21787f,  0.03501f, 
            0.06910f,  0.28734f,  0.03455f, 
            0.03925f,  0.35855f,  0.03326f, 
            0.26746f,  0.10043f,  0.04761f, 
            0.21039f,  0.14461f,  0.04989f, 
            0.15591f,  0.20045f,  0.05115f, 
            0.10878f,  0.26531f,  0.05115f, 
            0.07250f,  0.33438f,  0.04989f, 
            0.04813f,  0.40231f,  0.04761f, 
            0.05360f,  0.72522f,  0.00000f, 
            0.09549f,  0.79389f, -0.00000f, 
            0.14785f,  0.85495f, -0.00000f, 
            0.04057f,  0.68513f,  0.01705f, 
            0.07667f,  0.75669f,  0.01751f, 
            0.12506f,  0.82329f,  0.01751f, 
            0.18196f,  0.87973f,  0.01705f, 
            0.03925f,  0.64145f,  0.03326f, 
            0.06910f,  0.71266f,  0.03455f, 
            0.11169f,  0.78213f,  0.03501f, 
            0.16459f,  0.84410f,  0.03455f, 
            0.22309f,  0.89449f,  0.03326f, 
            0.04813f,  0.59769f,  0.04761f, 
            0.07250f,  0.66562f,  0.04989f, 
            0.10878f,  0.73469f,  0.05115f, 
            0.15591f,  0.79955f,  0.05115f, 
            0.21039f,  0.85539f,  0.04989f, 
            0.26746f,  0.89957f,  0.04761f, 
            0.57625f,  0.99415f,  0.00000f, 
            0.65451f,  0.97553f, -0.00000f, 
            0.72876f,  0.94460f, -0.00000f, 
            0.53410f,  0.99415f,  0.01705f, 
            0.61331f,  0.98193f,  0.01751f, 
            0.69160f,  0.95649f,  0.01751f, 
            0.76287f,  0.91982f,  0.01705f, 
            0.49215f,  0.98191f,  0.03326f, 
            0.56910f,  0.97553f,  0.03455f, 
            0.64832f,  0.95649f,  0.03501f, 
            0.72361f,  0.92532f,  0.03455f, 
            0.78961f,  0.88526f,  0.03326f, 
            0.45327f,  0.95994f,  0.04761f, 
            0.52541f,  0.95775f,  0.04989f, 
            0.60231f,  0.94460f,  0.05115f, 
            0.67856f,  0.91982f,  0.05115f, 
            0.74851f,  0.88526f,  0.04989f, 
            0.80815f,  0.84463f,  0.04761f, 
            0.99353f,  0.58018f,  0.00000f, 
            1.00000f,  0.50000f, -0.00000f, 
            0.99353f,  0.41982f, -0.00000f, 
            0.98050f,  0.62027f,  0.01705f, 
            0.99336f,  0.54116f,  0.01751f, 
            0.99336f,  0.45884f,  0.01751f, 
            0.98050f,  0.37973f,  0.01705f, 
            0.95590f,  0.65638f,  0.03326f, 
            0.97361f,  0.58123f,  0.03455f, 
            0.97998f,  0.50000f,  0.03501f, 
            0.97361f,  0.41877f,  0.03455f, 
            0.95590f,  0.34362f,  0.03326f, 
            0.92299f,  0.68657f,  0.04761f, 
            0.94320f,  0.61729f,  0.04989f, 
            0.95445f,  0.54009f,  0.05115f, 
            0.95445f,  0.45991f,  0.05115f, 
            0.94320f,  0.38271f,  0.04989f, 
            0.92299f,  0.31343f,  0.04761f, 
            0.78853f,  0.21574f,  0.07329f, 
            0.74850f,  0.24126f,  0.08707f, 
            0.72795f,  0.17800f,  0.07679f, 
            0.69964f,  0.27478f,  0.09982f, 
            0.68090f,  0.20611f,  0.09045f, 
            0.65749f,  0.14505f,  0.07874f, 
            0.64446f,  0.31487f,  0.11036f, 
            0.62668f,  0.24331f,  0.10249f, 
            0.60504f,  0.17671f,  0.09167f, 
            0.58123f,  0.12027f,  0.07874f, 
            0.58707f,  0.35855f,  0.11790f, 
            0.56910f,  0.28734f,  0.11180f, 
            0.54839f,  0.21788f,  0.10249f, 
            0.52639f,  0.15591f,  0.09045f, 
            0.50485f,  0.10551f,  0.07679f, 
            0.53174f,  0.40231f,  0.12233f, 
            0.51270f,  0.33439f,  0.11790f, 
            0.49195f,  0.26531f,  0.11036f, 
            0.47087f,  0.20045f,  0.09982f, 
            0.45104f,  0.14461f,  0.08707f, 
            0.43366f,  0.10044f,  0.07329f, 
            0.31882f,  0.13775f,  0.07329f, 
            0.33072f,  0.18370f,  0.08707f, 
            0.26420f,  0.18370f,  0.07679f, 
            0.34749f,  0.24054f,  0.09982f, 
            0.27639f,  0.23714f,  0.09045f, 
            0.21108f,  0.24054f,  0.07874f, 
            0.36857f,  0.30540f,  0.11036f, 
            0.29502f,  0.30020f,  0.10249f, 
            0.22500f,  0.30020f,  0.09167f, 
            0.16396f,  0.30540f,  0.07874f, 
            0.39238f,  0.37348f,  0.11790f, 
            0.31910f,  0.36857f,  0.11180f, 
            0.24664f,  0.36680f,  0.10249f, 
            0.18090f,  0.36857f,  0.09045f, 
            0.12632f,  0.37348f,  0.07679f, 
            0.41690f,  0.43963f,  0.12233f, 
            0.34642f,  0.43674f,  0.11790f, 
            0.27431f,  0.43513f,  0.11036f, 
            0.20611f,  0.43513f,  0.09982f, 
            0.14687f,  0.43674f,  0.08707f, 
            0.09949f,  0.43963f,  0.07329f, 
            0.09949f,  0.56037f,  0.07329f, 
            0.14687f,  0.56326f,  0.08707f, 
            0.12632f,  0.62652f,  0.07679f, 
            0.20611f,  0.56487f,  0.09982f, 
            0.18090f,  0.63143f,  0.09045f, 
            0.16396f,  0.69460f,  0.07874f, 
            0.27431f,  0.56487f,  0.11036f, 
            0.24664f,  0.63320f,  0.10249f, 
            0.22500f,  0.69980f,  0.09167f, 
            0.21108f,  0.75946f,  0.07874f, 
            0.34642f,  0.56326f,  0.11790f, 
            0.31910f,  0.63143f,  0.11180f, 
            0.29502f,  0.69980f,  0.10249f, 
            0.27639f,  0.76286f,  0.09045f, 
            0.26420f,  0.81630f,  0.07679f, 
            0.41690f,  0.56038f,  0.12233f, 
            0.39238f,  0.62652f,  0.11790f, 
            0.36857f,  0.69460f,  0.11036f, 
            0.34749f,  0.75946f,  0.09982f, 
            0.33072f,  0.81630f,  0.08707f, 
            0.31882f,  0.86225f,  0.07329f, 
            0.85951f,  0.31343f,  0.07329f, 
            0.87668f,  0.38271f,  0.07679f, 
            0.82287f,  0.34362f,  0.08707f, 
            0.88625f,  0.45991f,  0.07874f, 
            0.83541f,  0.41877f,  0.09045f, 
            0.77589f,  0.37973f,  0.09982f, 
            0.88625f,  0.54009f,  0.07874f, 
            0.83992f,  0.50000f,  0.09167f, 
            0.78327f,  0.45884f,  0.10249f, 
            0.72071f,  0.41982f,  0.11036f, 
            0.87668f,  0.61729f,  0.07679f, 
            0.83541f,  0.58123f,  0.09045f, 
            0.78327f,  0.54116f,  0.10249f, 
            0.72361f,  0.50000f,  0.11180f, 
            0.66143f,  0.46090f,  0.11790f, 
            0.85951f,  0.68657f,  0.07329f, 
            0.82287f,  0.65638f,  0.08707f, 
            0.77589f,  0.62027f,  0.09982f, 
            0.72071f,  0.58018f,  0.11036f, 
            0.66143f,  0.53910f,  0.11790f, 
            0.60272f,  0.50000f,  0.12233f, 
            0.43366f,  0.89956f,  0.07329f, 
            0.45104f,  0.85539f,  0.08707f, 
            0.50485f,  0.89449f,  0.07679f, 
            0.47088f,  0.79955f,  0.09982f, 
            0.52640f,  0.84409f,  0.09045f, 
            0.58123f,  0.87973f,  0.07874f, 
            0.49195f,  0.73468f,  0.11036f, 
            0.54839f,  0.78212f,  0.10249f, 
            0.60504f,  0.82329f,  0.09167f, 
            0.65749f,  0.85495f,  0.07874f, 
            0.51270f,  0.66561f,  0.11790f, 
            0.56910f,  0.71266f,  0.11180f, 
            0.62668f,  0.75668f,  0.10249f, 
            0.68090f,  0.79389f,  0.09045f, 
            0.72795f,  0.82200f,  0.07679f, 
            0.53174f,  0.59769f,  0.12233f, 
            0.58707f,  0.64145f,  0.11790f, 
            0.64446f,  0.68513f,  0.11036f, 
            0.69964f,  0.72522f,  0.09982f, 
            0.74851f,  0.75874f,  0.08707f, 
            0.78853f,  0.78426f,  0.07329f
        };
        float normals[] = new float[] 
        {
            0.00002f,  0.00006f,  1.00000f, 
            0.00006f,  0.00000f,  1.00000f, 
            0.00002f, -0.00006f,  1.00000f, 
           -0.00005f, -0.00004f,  1.00000f, 
           -0.00005f,  0.00004f,  1.00000f, 
            0.36181f,  0.26286f,  0.89443f, 
           -0.13813f,  0.42540f,  0.89440f, 
           -0.13828f,  0.42523f,  0.89446f, 
           -0.44721f,  0.00000f,  0.89443f, 
           -0.13820f, -0.42531f,  0.89443f, 
            0.36181f, -0.26286f,  0.89443f, 
           -0.10880f,  0.33529f,  0.93581f, 
           -0.10909f,  0.33519f,  0.93581f, 
           -0.08459f,  0.26093f,  0.96164f, 
           -0.08495f,  0.26082f,  0.96164f, 
           -0.06417f,  0.19820f,  0.97806f, 
           -0.06457f,  0.19807f,  0.97806f, 
           -0.04699f,  0.14528f,  0.98827f, 
           -0.04740f,  0.14515f,  0.98827f, 
           -0.03240f,  0.10042f,  0.99442f, 
           -0.03280f,  0.10029f,  0.99442f, 
           -0.01999f,  0.06215f,  0.99787f, 
           -0.02034f,  0.06204f,  0.99787f, 
           -0.00935f,  0.02927f,  0.99953f, 
           -0.00963f,  0.02918f,  0.99953f, 
            0.02476f,  0.01817f,  0.99953f, 
            0.02494f,  0.01793f,  0.99953f, 
            0.05270f,  0.03852f,  0.99787f, 
            0.05292f,  0.03822f,  0.99787f, 
            0.08525f,  0.06221f,  0.99442f, 
            0.08549f,  0.06187f,  0.99442f, 
            0.12339f,  0.08992f,  0.98828f, 
            0.12365f,  0.08956f,  0.98828f, 
            0.16842f,  0.12262f,  0.97806f, 
            0.16867f,  0.12228f,  0.97806f, 
            0.22181f,  0.16137f,  0.96164f, 
            0.22203f,  0.16107f,  0.96164f, 
            0.28506f,  0.20730f,  0.93582f, 
            0.28524f,  0.20706f,  0.93582f, 
            0.30261f,  0.28810f,  0.90853f, 
            0.24228f,  0.31201f,  0.91867f, 
            0.17976f,  0.33495f,  0.92493f, 
            0.11590f,  0.35665f,  0.92702f, 
            0.05148f,  0.37664f,  0.92493f, 
           -0.01257f,  0.39485f,  0.91866f, 
           -0.07548f,  0.41094f,  0.90853f, 
            0.36752f, -0.19880f,  0.90852f, 
            0.37164f, -0.13398f,  0.91866f, 
            0.37410f, -0.06745f,  0.92493f, 
            0.37499f,  0.00000f,  0.92703f, 
            0.37410f,  0.06745f,  0.92493f, 
            0.37164f,  0.13398f,  0.91866f, 
            0.36752f,  0.19880f,  0.90852f, 
            0.02494f, -0.01793f,  0.99953f, 
            0.02476f, -0.01817f,  0.99953f, 
            0.05292f, -0.03822f,  0.99787f, 
            0.05270f, -0.03852f,  0.99787f, 
            0.08549f, -0.06187f,  0.99442f, 
            0.08525f, -0.06221f,  0.99442f, 
            0.12365f, -0.08956f,  0.98828f, 
            0.12339f, -0.08992f,  0.98828f, 
            0.16867f, -0.12228f,  0.97806f, 
            0.16842f, -0.12262f,  0.97806f, 
            0.22203f, -0.16107f,  0.96164f, 
            0.22181f, -0.16137f,  0.96164f, 
            0.28524f, -0.20706f,  0.93582f, 
            0.28506f, -0.20730f,  0.93582f, 
           -0.35250f, -0.00015f,  0.93581f, 
           -0.35250f,  0.00015f,  0.93581f, 
           -0.27430f, -0.00019f,  0.96164f, 
           -0.27430f,  0.00019f,  0.96164f, 
           -0.20835f, -0.00021f,  0.97806f, 
           -0.20835f,  0.00021f,  0.97806f, 
           -0.15268f, -0.00022f,  0.98828f, 
           -0.15268f,  0.00022f,  0.98828f, 
           -0.10552f, -0.00021f,  0.99442f, 
           -0.10552f,  0.00021f,  0.99442f, 
           -0.06529f, -0.00019f,  0.99787f, 
           -0.06529f,  0.00019f,  0.99787f, 
           -0.03071f, -0.00015f,  0.99953f, 
           -0.03071f,  0.00015f,  0.99953f, 
           -0.18047f,  0.37685f,  0.90852f, 
           -0.22190f,  0.32682f,  0.91867f, 
           -0.26304f,  0.27444f,  0.92493f, 
           -0.30339f,  0.22042f,  0.92702f, 
           -0.34229f,  0.16534f,  0.92493f, 
           -0.37941f,  0.11003f,  0.91866f, 
           -0.41418f,  0.05521f,  0.90852f, 
           -0.10880f, -0.33529f,  0.93581f, 
           -0.10909f, -0.33519f,  0.93581f, 
           -0.08459f, -0.26093f,  0.96164f, 
           -0.08495f, -0.26082f,  0.96164f, 
           -0.06417f, -0.19820f,  0.97806f, 
           -0.06457f, -0.19807f,  0.97806f, 
           -0.04699f, -0.14528f,  0.98827f, 
           -0.04740f, -0.14515f,  0.98827f, 
           -0.03240f, -0.10042f,  0.99442f, 
           -0.03280f, -0.10029f,  0.99442f, 
           -0.01999f, -0.06215f,  0.99787f, 
           -0.02034f, -0.06204f,  0.99787f, 
           -0.00935f, -0.02927f,  0.99953f, 
           -0.00963f, -0.02918f,  0.99953f, 
           -0.41418f, -0.05521f,  0.90852f, 
           -0.37941f, -0.11003f,  0.91866f, 
           -0.34229f, -0.16534f,  0.92493f, 
           -0.30339f, -0.22042f,  0.92702f, 
           -0.26304f, -0.27444f,  0.92493f, 
           -0.22190f, -0.32682f,  0.91867f, 
           -0.18047f, -0.37685f,  0.90852f, 
           -0.07548f, -0.41094f,  0.90853f, 
           -0.01257f, -0.39485f,  0.91866f, 
            0.05148f, -0.37664f,  0.92493f, 
            0.11590f, -0.35665f,  0.92702f, 
            0.17976f, -0.33495f,  0.92493f, 
            0.24228f, -0.31201f,  0.91867f, 
            0.30261f, -0.28810f,  0.90853f, 
            0.92590f,  0.29990f,  0.22970f, 
            0.81908f,  0.34105f,  0.46130f, 
            0.61971f,  0.31792f,  0.71755f, 
            0.46773f,  0.28772f,  0.83573f, 
            0.46773f, -0.28772f,  0.83573f, 
            0.61971f, -0.31792f,  0.71755f, 
            0.81908f, -0.34105f,  0.46130f, 
            0.92590f, -0.29990f,  0.22970f, 
            0.00087f,  0.97326f,  0.22969f, 
           -0.07131f,  0.88436f,  0.46133f, 
           -0.11084f,  0.68762f,  0.71756f, 
           -0.12915f,  0.53372f,  0.83574f, 
            0.41820f,  0.35592f,  0.83572f, 
            0.49389f,  0.49116f,  0.71752f, 
            0.57753f,  0.67353f,  0.46132f, 
            0.57142f,  0.78786f,  0.22970f, 
           -0.92536f,  0.30156f,  0.22971f, 
           -0.86311f,  0.20551f,  0.46131f, 
           -0.68825f,  0.10702f,  0.71754f, 
           -0.54752f,  0.04212f,  0.83573f, 
           -0.20923f,  0.50790f,  0.83562f, 
           -0.20933f,  0.50749f,  0.83584f, 
           -0.31441f,  0.62176f,  0.71733f, 
           -0.31450f,  0.62125f,  0.71773f, 
           -0.46207f,  0.75763f,  0.46096f, 
           -0.46218f,  0.75713f,  0.46168f, 
           -0.57271f,  0.78694f,  0.22962f, 
           -0.57284f,  0.78680f,  0.22977f, 
           -0.57277f, -0.78687f,  0.22970f, 
           -0.46212f, -0.75738f,  0.46132f, 
           -0.31445f, -0.62150f,  0.71754f, 
           -0.20927f, -0.50769f,  0.83574f, 
           -0.54752f, -0.04212f,  0.83573f, 
           -0.68825f, -0.10702f,  0.71754f, 
           -0.86311f, -0.20551f,  0.46131f, 
           -0.92536f, -0.30156f,  0.22971f, 
            0.57142f, -0.78786f,  0.22970f, 
            0.57753f, -0.67353f,  0.46132f, 
            0.49389f, -0.49116f,  0.71752f, 
            0.41820f, -0.35592f,  0.83572f, 
           -0.12915f, -0.53372f,  0.83574f, 
           -0.11084f, -0.68762f,  0.71756f, 
           -0.07131f, -0.88436f,  0.46133f, 
            0.00087f, -0.97326f,  0.22969f, 
            0.86875f, -0.44068f,  0.22599f, 
            0.75567f, -0.46358f,  0.46267f, 
            0.78765f, -0.57230f,  0.22821f, 
            0.56329f, -0.40927f,  0.71777f, 
            0.67441f, -0.57542f,  0.46267f, 
            0.68755f, -0.69007f,  0.22599f, 
           -0.15067f, -0.96241f,  0.22598f, 
           -0.20734f, -0.86195f,  0.46265f, 
           -0.30088f, -0.92595f,  0.22821f, 
           -0.21513f, -0.66222f,  0.71776f, 
           -0.33884f, -0.81923f,  0.46266f, 
           -0.44385f, -0.86714f,  0.22597f, 
           -0.96185f, -0.15417f,  0.22599f, 
           -0.88385f, -0.06910f,  0.46265f, 
           -0.97361f,  0.00000f,  0.22821f, 
           -0.69630f,  0.00000f,  0.71775f, 
           -0.88385f,  0.06910f,  0.46265f, 
           -0.96185f,  0.15417f,  0.22599f, 
           -0.44385f,  0.86714f,  0.22597f, 
           -0.33884f,  0.81923f,  0.46266f, 
           -0.30085f,  0.92587f,  0.22858f, 
           -0.21513f,  0.66222f,  0.71776f, 
           -0.20734f,  0.86195f,  0.46265f, 
           -0.15067f,  0.96241f,  0.22598f, 
            0.68755f,  0.69007f,  0.22599f, 
            0.67441f,  0.57542f,  0.46267f, 
            0.78758f,  0.57225f,  0.22858f, 
            0.56329f,  0.40927f,  0.71777f, 
            0.75567f,  0.46358f,  0.46267f, 
            0.86875f,  0.44068f,  0.22599f, 
            0.44230f, -0.86333f,  0.24297f, 
            0.29954f, -0.92184f,  0.24593f, 
            0.14958f, -0.95843f,  0.24299f, 
            0.46208f, -0.74288f,  0.48437f, 
            0.33559f, -0.80196f,  0.49421f, 
            0.19987f, -0.84605f,  0.49422f, 
            0.06282f, -0.87260f,  0.48437f, 
            0.40200f, -0.53909f,  0.74013f, 
            0.30476f, -0.58280f,  0.75330f, 
            0.20164f, -0.62047f,  0.75786f, 
            0.09600f, -0.65064f,  0.75329f, 
           -0.00834f, -0.67242f,  0.74012f, 
            0.34533f, -0.38971f,  0.85374f, 
            0.26998f, -0.42150f,  0.86571f, 
            0.19117f, -0.45093f,  0.87185f, 
            0.11039f, -0.47719f,  0.87184f, 
            0.02936f, -0.49969f,  0.86571f, 
           -0.05028f, -0.51828f,  0.85373f, 
           -0.68441f, -0.68742f,  0.24298f, 
           -0.78418f, -0.56973f,  0.24592f, 
           -0.86529f, -0.43843f,  0.24298f, 
           -0.56376f, -0.66900f,  0.48436f, 
           -0.65901f, -0.56697f,  0.49421f, 
           -0.74288f, -0.45153f,  0.49422f, 
           -0.81048f, -0.32944f,  0.48435f, 
           -0.38851f, -0.54889f,  0.74012f, 
           -0.46009f, -0.46992f,  0.75332f, 
           -0.52780f, -0.38350f,  0.75786f, 
           -0.58908f, -0.29241f,  0.75331f, 
           -0.64208f, -0.19985f,  0.74013f, 
           -0.26395f, -0.44883f,  0.85375f, 
           -0.31746f, -0.38701f,  0.86570f, 
           -0.36981f, -0.32117f,  0.87183f, 
           -0.41972f, -0.25246f,  0.87184f, 
           -0.46615f, -0.18233f,  0.86571f, 
           -0.50843f, -0.11235f,  0.85374f, 
           -0.86529f,  0.43843f,  0.24298f, 
           -0.78418f,  0.56973f,  0.24592f, 
           -0.68441f,  0.68742f,  0.24298f, 
           -0.81048f,  0.32944f,  0.48435f, 
           -0.74288f,  0.45153f,  0.49422f, 
           -0.65901f,  0.56697f,  0.49421f, 
           -0.56376f,  0.66900f,  0.48436f, 
           -0.64208f,  0.19985f,  0.74013f, 
           -0.58908f,  0.29241f,  0.75331f, 
           -0.52780f,  0.38350f,  0.75786f, 
           -0.46009f,  0.46992f,  0.75332f, 
           -0.38851f,  0.54889f,  0.74012f, 
           -0.50843f,  0.11235f,  0.85374f, 
           -0.46615f,  0.18233f,  0.86571f, 
           -0.41972f,  0.25246f,  0.87184f, 
           -0.36981f,  0.32117f,  0.87183f, 
           -0.31746f,  0.38701f,  0.86570f, 
           -0.26395f,  0.44883f,  0.85375f, 
            0.14958f,  0.95843f,  0.24299f, 
            0.29954f,  0.92184f,  0.24593f, 
            0.44230f,  0.86333f,  0.24297f, 
            0.06282f,  0.87260f,  0.48437f, 
            0.19987f,  0.84605f,  0.49422f, 
            0.33559f,  0.80196f,  0.49421f, 
            0.46208f,  0.74288f,  0.48437f, 
           -0.00834f,  0.67242f,  0.74012f, 
            0.09600f,  0.65064f,  0.75329f, 
            0.20164f,  0.62047f,  0.75786f, 
            0.30476f,  0.58280f,  0.75330f, 
            0.40200f,  0.53909f,  0.74013f, 
           -0.05028f,  0.51828f,  0.85373f, 
            0.02936f,  0.49969f,  0.86571f, 
            0.11039f,  0.47719f,  0.87184f, 
            0.19117f,  0.45093f,  0.87185f, 
            0.26998f,  0.42150f,  0.86571f, 
            0.34533f,  0.38971f,  0.85374f, 
            0.95775f,  0.15385f,  0.24299f, 
            0.96929f, -0.00000f,  0.24592f, 
            0.95775f, -0.15385f,  0.24299f, 
            0.84930f,  0.20992f,  0.48438f, 
            0.86641f,  0.07129f,  0.49421f, 
            0.86641f, -0.07129f,  0.49421f, 
            0.84930f, -0.20992f,  0.48438f, 
            0.63693f,  0.21570f,  0.74013f, 
            0.64846f,  0.10971f,  0.75330f, 
            0.65241f,  0.00000f,  0.75787f, 
            0.64846f, -0.10971f,  0.75330f, 
            0.63693f, -0.21570f,  0.74013f, 
            0.47738f,  0.20796f,  0.85373f, 
            0.48432f,  0.12650f,  0.86570f, 
            0.48792f,  0.04245f,  0.87185f, 
            0.48792f, -0.04245f,  0.87185f, 
            0.48432f, -0.12650f,  0.86570f, 
            0.47738f, -0.20796f,  0.85373f, 
            0.23022f, -0.22848f,  0.94593f, 
            0.17123f, -0.17986f,  0.96867f, 
            0.17493f, -0.24850f,  0.95270f, 
            0.12202f, -0.13909f,  0.98273f, 
            0.12070f, -0.19727f,  0.97289f, 
            0.11851f, -0.26785f,  0.95614f, 
            0.08084f, -0.10481f,  0.99120f, 
            0.07586f, -0.15451f,  0.98507f, 
            0.06960f, -0.21420f,  0.97431f, 
            0.06155f, -0.28637f,  0.95614f, 
            0.04610f, -0.07576f,  0.99606f, 
            0.03857f, -0.11868f,  0.99218f, 
            0.02946f, -0.16962f,  0.98507f, 
            0.01830f, -0.23052f,  0.97290f, 
            0.00457f, -0.30386f,  0.95271f, 
            0.01655f, -0.05099f,  0.99856f, 
            0.00723f, -0.08839f,  0.99606f, 
           -0.00380f, -0.13230f,  0.99120f, 
           -0.01695f, -0.18426f,  0.98273f, 
           -0.03283f, -0.24617f,  0.96867f, 
           -0.05196f, -0.32015f,  0.94594f, 
           -0.14616f, -0.28955f,  0.94594f, 
           -0.11816f, -0.21844f,  0.96867f, 
           -0.18229f, -0.24316f,  0.95270f, 
           -0.09456f, -0.15903f,  0.98273f, 
           -0.15032f, -0.17574f,  0.97289f, 
           -0.21814f, -0.19549f,  0.95614f, 
           -0.07469f, -0.10925f,  0.99120f, 
           -0.12351f, -0.11992f,  0.98507f, 
           -0.18222f, -0.13240f,  0.97430f, 
           -0.25333f, -0.14703f,  0.95614f, 
           -0.05781f, -0.06725f,  0.99606f, 
           -0.10094f, -0.07334f,  0.99219f, 
           -0.15222f, -0.08042f,  0.98507f, 
           -0.21360f, -0.08864f,  0.97289f, 
           -0.28760f, -0.09824f,  0.95270f, 
           -0.04338f, -0.03150f,  0.99856f, 
           -0.08183f, -0.03420f,  0.99606f, 
           -0.12698f, -0.03727f,  0.99120f, 
           -0.18047f, -0.04080f,  0.98273f, 
           -0.24426f, -0.04487f,  0.96867f, 
           -0.32055f, -0.04951f,  0.94594f, 
           -0.32055f,  0.04951f,  0.94594f, 
           -0.24426f,  0.04487f,  0.96867f, 
           -0.28760f,  0.09824f,  0.95270f, 
           -0.18047f,  0.04080f,  0.98273f, 
           -0.21360f,  0.08864f,  0.97289f, 
           -0.25333f,  0.14703f,  0.95614f, 
           -0.12698f,  0.03727f,  0.99120f, 
           -0.15222f,  0.08042f,  0.98507f, 
           -0.18222f,  0.13240f,  0.97430f, 
           -0.21814f,  0.19549f,  0.95614f, 
           -0.08183f,  0.03420f,  0.99606f, 
           -0.10094f,  0.07334f,  0.99219f, 
           -0.12351f,  0.11992f,  0.98507f, 
           -0.15032f,  0.17574f,  0.97289f, 
           -0.18229f,  0.24316f,  0.95270f, 
           -0.04338f,  0.03150f,  0.99856f, 
           -0.05781f,  0.06725f,  0.99606f, 
           -0.07469f,  0.10925f,  0.99120f, 
           -0.09456f,  0.15903f,  0.98273f, 
           -0.11816f,  0.21844f,  0.96867f, 
           -0.14616f,  0.28955f,  0.94594f, 
            0.28845f, -0.14834f,  0.94594f, 
            0.29039f, -0.08956f,  0.95271f, 
            0.22398f, -0.10727f,  0.96867f, 
            0.29137f, -0.02994f,  0.95614f, 
            0.22492f, -0.05382f,  0.97289f, 
            0.17000f, -0.07306f,  0.98273f, 
            0.29137f,  0.02994f,  0.95614f, 
            0.22522f, -0.00000f,  0.97431f, 
            0.17040f, -0.02439f,  0.98507f, 
            0.12464f, -0.04448f,  0.99120f, 
            0.29039f,  0.08956f,  0.95271f, 
            0.22492f,  0.05382f,  0.97289f, 
            0.17040f,  0.02439f,  0.98507f, 
            0.12477f,  0.00000f,  0.99219f, 
            0.08630f, -0.02043f,  0.99606f, 
            0.28845f,  0.14834f,  0.94594f, 
            0.22398f,  0.10727f,  0.96867f, 
            0.17000f,  0.07309f,  0.98273f, 
            0.12464f,  0.04448f,  0.99120f, 
            0.08630f,  0.02043f,  0.99606f, 
            0.05360f,  0.00000f,  0.99856f, 
           -0.05196f,  0.32015f,  0.94594f, 
           -0.03283f,  0.24617f,  0.96867f, 
            0.00457f,  0.30386f,  0.95271f, 
           -0.01695f,  0.18426f,  0.98273f, 
            0.01830f,  0.23052f,  0.97290f, 
            0.06155f,  0.28637f,  0.95614f, 
           -0.00380f,  0.13230f,  0.99120f, 
            0.02946f,  0.16962f,  0.98507f, 
            0.06960f,  0.21420f,  0.97431f, 
            0.11851f,  0.26785f,  0.95614f, 
            0.00723f,  0.08839f,  0.99606f, 
            0.03857f,  0.11868f,  0.99218f, 
            0.07586f,  0.15451f,  0.98507f, 
            0.12070f,  0.19727f,  0.97289f, 
            0.17493f,  0.24850f,  0.95270f, 
            0.01655f,  0.05099f,  0.99856f, 
            0.04610f,  0.07576f,  0.99606f, 
            0.08084f,  0.10481f,  0.99120f, 
            0.12202f,  0.13909f,  0.98273f, 
            0.17123f,  0.17986f,  0.96867f, 
            0.23022f,  0.22848f,  0.94593f
        };
        // @formatter:on

        AccessorModel indicesAccessorModel =
            AccessorModels.createUnsignedShortScalar(ShortBuffer.wrap(indices));
        AccessorModel positionsAccessorModel =
            AccessorModels.createFloat3D(FloatBuffer.wrap(positions));
        AccessorModel normalsAccessorModel =
            AccessorModels.createFloat3D(FloatBuffer.wrap(normals));

        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            new DefaultMeshPrimitiveModel(GltfConstants.GL_TRIANGLES);
        meshPrimitiveModel.setIndices(indicesAccessorModel);
        meshPrimitiveModel.putAttribute("POSITION", positionsAccessorModel);
        meshPrimitiveModel.putAttribute("NORMAL", normalsAccessorModel);
        return meshPrimitiveModel;

    }

    /**
     * Create a simple material model with a base color texture for tests
     * 
     * @param uri The URI
     * @return The material model
     */
    static DefaultPbrMaterialModel
        createBaseColorTextureMaterialModel(String uri)
    {
        DefaultPbrMaterialModel materialModel = new DefaultPbrMaterialModel();

        DefaultPbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            new DefaultPbrMetallicRoughnessModel();
        pbrMetallicRoughnessModel.setMetallicFactor(0.0);

        DefaultTextureInfoModel baseColorTextureInfoModel =
            new DefaultTextureInfoModel();
        DefaultTextureModel textureModel = createSimpleTextureModel(uri);
        baseColorTextureInfoModel.setTextureModel(textureModel);
        pbrMetallicRoughnessModel
            .setBaseColorTexture(baseColorTextureInfoModel);

        materialModel.setPbrMetallicRoughnessModel(pbrMetallicRoughnessModel);
        materialModel.setDoubleSided(true);
        return materialModel;
    }

    /**
     * Create a simple material model with a base color for tests
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     * @param a The alpha component
     * @return The material model
     */
    static DefaultPbrMaterialModel createBaseColorMaterialModel(double r,
        double g, double b, double a)
    {
        DefaultPbrMaterialModel materialModel = new DefaultPbrMaterialModel();

        DefaultPbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            new DefaultPbrMetallicRoughnessModel();
        pbrMetallicRoughnessModel.setMetallicFactor(0.0);
        pbrMetallicRoughnessModel.setBaseColorFactor(new double[]
        { r, g, b, a });

        materialModel.setPbrMetallicRoughnessModel(pbrMetallicRoughnessModel);
        materialModel.setDoubleSided(true);
        return materialModel;
    }

    /**
     * Add a KHR_materials_clearcoat extension with a texture to the given
     * material model
     * 
     * @param materialModel The material model
     */
    private static void
        addClearcoatTexture(DefaultPbrMaterialModel materialModel)
    {
        DefaultTextureModel textureModel =
            createClearcoatTextureModel("clearcoat.png");

        DefaultMaterialsClearcoatModel clearcoatModel =
            new DefaultMaterialsClearcoatModel();
        DefaultTextureInfoModel clearcoatTextureInfoModel =
            new DefaultTextureInfoModel();
        clearcoatTextureInfoModel.setTextureModel(textureModel);
        clearcoatModel.setClearcoatTexture(clearcoatTextureInfoModel);
        clearcoatModel.setClearcoatFactor(1.0);

        materialModel.addExtensionModel("KHR_materials_clearcoat",
            clearcoatModel);
    }

    /**
     * Create a simple texture model for tests
     * 
     * @param uri The URI for the image
     * @return The texture model
     */
    private static DefaultTextureModel createSimpleTextureModel(String uri)
    {
        Color foreground = new Color(255, 0, 0);
        Color background = new Color(0, 0, 255);
        return createSimpleTextureModel(uri, foreground, background);
    }

    /**
     * Create a simple texture model for tests
     * 
     * @param uri The URI for the image
     * @param foreground The foreground color
     * @param background The background color
     * @return The texture model
     */
    private static DefaultTextureModel createSimpleTextureModel(String uri,
        Color foreground, Color background)
    {
        DefaultTextureModel textureModel = new DefaultTextureModel();
        String imageText = createImageText(uri);
        int fontSize = 12;
        DefaultImageModel imageModel =
            createImageModel(uri, imageText, fontSize, foreground, background);
        textureModel.setImageModel(imageModel);
        return textureModel;
    }

    /**
     * Create a clearcoat texture model for tests
     * 
     * @param uri The URI for the image
     * @return The texture model
     */
    private static DefaultTextureModel createClearcoatTextureModel(String uri)
    {
        DefaultTextureModel textureModel = new DefaultTextureModel();
        String imageText = createImageText(uri);
        int fontSize = 24;
        Color foreground = new Color(255, 255, 255);
        Color background = new Color(16, 16, 16);
        DefaultImageModel imageModel =
            createImageModel(uri, imageText, fontSize, foreground, background);
        textureModel.setImageModel(imageModel);
        return textureModel;
    }

    /**
     * Create a simple image model for tests
     * 
     * @param uri The URI for the image
     * @param text The text on the image
     * @param fontSize Font size
     * @param foreground Foreground color
     * @param background Background color
     * @return The image model
     */
    private static DefaultImageModel createImageModel(String uri, String text,
        int fontSize, Color foreground, Color background)
    {
        DefaultImageModel imageModel = new DefaultImageModel();
        imageModel.setUri(uri);
        imageModel.setMimeType("image/png");
        BufferedImage image =
            createBufferedImage(text, fontSize, foreground, background);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
            ImageIO.write(image, "png", baos);
            ByteBuffer imageData = Buffers.create(baos.toByteArray());
            imageModel.setImageData(imageData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return imageModel;
    }

    /**
     * Create a simple buffered image for tests
     * 
     * @param text The text on the image
     * @param fontSize Font size
     * @param foreground Foreground color
     * @param background Background color
     * @return The image
     */
    private static BufferedImage createBufferedImage(String text, int fontSize,
        Color foreground, Color background)
    {
        int sizeX = 256;
        int sizeY = 256;
        JLabel label = new JLabel(text);
        label.setVerticalAlignment(JLabel.TOP);
        label.setForeground(foreground);
        label.setBackground(background);
        label.setFont(new Font("Monospaced", Font.BOLD, fontSize));
        label.setOpaque(true);
        label.setSize(sizeX, sizeY);
        BufferedImage image =
            new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        label.paint(g);
        g.dispose();
        return image;
    }

    /**
     * Create a HTML string to used as the text on an image, repeating the given
     * text many times
     * 
     * @param base The base text
     * @return The string
     */
    private static String createImageText(String base)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        for (int i = 0; i < 50; i++)
        {
            if (i > 0)
            {
                sb.append(" ");
            }
            sb.append(base);
        }
        sb.append("</html>");
        String result = sb.toString();
        return result;
    }

    /**
     * Create a glTF model with a simple skin and animation.
     * 
     * (This corresponds to the 'SimpleSkin' sample asset)
     * 
     * @return The model
     */
    public static DefaultGltfModel createSimpleSkin()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            new DefaultMeshPrimitiveModel(GltfConstants.GL_TRIANGLES);

        // Create the indices, positions, joints, and weights
        // @formatter:off
        short[] indices = new short[] 
        { 
            0, 1, 3, 
            0, 3, 2, 
            2, 3, 5, 
            2, 5, 4,
            4, 5, 7, 
            4, 7, 6, 
            6, 7, 9, 
            6, 9, 8 
        };
        // @formatter:on
        AccessorModel indicesAccessorModel =
            AccessorModels.createUnsignedShortScalar(ShortBuffer.wrap(indices));

        // @formatter:off
        float[] positions = new float[] 
        {
            -0.5F, 0.0F, 0.0F, 
             0.5F, 0.0F, 0.0F, 
            -0.5F, 0.5F, 0.0F, 
             0.5F, 0.5F, 0.0F, 
            -0.5F, 1.0F, 0.0F, 
             0.5F, 1.0F, 0.0F, 
            -0.5F, 1.5F, 0.0F, 
             0.5F, 1.5F, 0.0F, 
            -0.5F, 2.0F, 0.0F, 
             0.5F, 2.0F, 0.0F 
        };
        // @formatter:on
        AccessorModel positionAccessorModel =
            AccessorModels.createFloat3D(FloatBuffer.wrap(positions));

        // @formatter:off
        short[] joints = new short[] 
        { 
            0, 0, 0, 0, 
            0, 0, 0, 0, 
            0, 1, 0, 0, 
            0, 1, 0, 0, 
            0, 1, 0, 0, 
            0, 1, 0, 0, 
            0, 1, 0, 0, 
            0, 1, 0, 0, 
            0, 1, 0, 0, 
            0, 1, 0, 0 
        };
        // @formatter:on
        AccessorModel jointsAccessorModel =
            AccessorModels.create(GltfConstants.GL_UNSIGNED_SHORT, "VEC4",
                false, Buffers.createByteBufferFrom(ShortBuffer.wrap(joints)));

        // @formatter:off
        float[] wights = new float[] 
        { 
            1.0F,  0.0F,  0.0F, 0.0F, 
            1.0F,  0.0F,  0.0F, 0.0F, 
            0.75F, 0.25F, 0.0F, 0.0F,
            0.75F, 0.25F, 0.0F, 0.0F, 
            0.5F,  0.5F,  0.0F, 0.0F, 
            0.5F,  0.5F,  0.0F, 0.0F, 
            0.25F, 0.75F, 0.0F, 0.0F, 
            0.25F, 0.75F, 0.0F, 0.0F, 
            0.0F,  1.0F,  0.0F, 0.0F, 
            0.0F,  1.0F,  0.0F, 0.0F 
        };
        // @formatter:on
        AccessorModel weightsAccessorModel =
            AccessorModels.createFloat4D(FloatBuffer.wrap(wights));

        // Assign indices, positions, joints, and weights to the mesh
        // primitive model
        meshPrimitiveModel.setIndices(indicesAccessorModel);
        meshPrimitiveModel.putAttribute("POSITION", positionAccessorModel);
        meshPrimitiveModel.putAttribute("JOINTS_0", jointsAccessorModel);
        meshPrimitiveModel.putAttribute("WEIGHTS_0", weightsAccessorModel);

        // Create a mesh model with the mesh primitive
        DefaultMeshModel meshModel = new DefaultMeshModel();
        meshModel.addMeshPrimitiveModel(meshPrimitiveModel);

        // Create the skin model
        DefaultSkinModel skinModel = new DefaultSkinModel();
        // @formatter:off
        float[] ibm = new float[] 
        { 
            1.0F,  0.0F, 0.0F, 0.0F, 
            0.0F,  1.0F, 0.0F, 0.0F, 
            0.0F,  0.0F, 1.0F, 0.0F, 
            0.0F,  0.0F, 0.0F, 1.0F, 
            
            1.0F,  0.0F, 0.0F, 0.0F,
            0.0F,  1.0F, 0.0F, 0.0F, 
            0.0F,  0.0F, 1.0F, 0.0F, 
            0.0F, -1.0F, 0.0F, 1.0F 
        };
        // @formatter:on
        AccessorModel ibmAccessorModel =
            AccessorModels.create(GltfConstants.GL_FLOAT, "MAT4", false,
                Buffers.createByteBufferFrom(FloatBuffer.wrap(ibm)));
        skinModel.setInverseBindMatrices(ibmAccessorModel);

        // Create the joint node models and assign them to the skin
        DefaultNodeModel jointNodeModel0 = new DefaultNodeModel();
        DefaultNodeModel jointNodeModel1 = new DefaultNodeModel();
        jointNodeModel1.setTranslation(new double[]
        { 0.0D, 1.0D, 0.0D });
        jointNodeModel0.addChild(jointNodeModel1);
        skinModel.addJoint(jointNodeModel0);
        skinModel.addJoint(jointNodeModel1);

        // Create the main node model and assign the mesh and skin to it
        DefaultNodeModel nodeModel = new DefaultNodeModel();
        nodeModel.addMeshModel(meshModel);
        nodeModel.setSkinModel(skinModel);

        // Create the animation model
        DefaultAnimationModel animationModel = new DefaultAnimationModel();

        // @formatter:off
        float[] times = new float[] 
        { 
            0.0F, 
            0.5F, 
            1.0F, 
            1.5F, 
            2.0F, 
            2.5F, 
            3.0F,
            3.5F, 
            4.0F, 
            4.5F, 
            5.0F, 
            5.5F 
        };
        // @formatter:on
        AccessorModel timesAcessorModel =
            AccessorModels.createFloatScalar(FloatBuffer.wrap(times));

        // @formatter:off
        float[] rotations = new float[] 
        { 
            0.0F, 0.0F,  0.0F,   1.0F, 
            0.0F, 0.0F,  0.383F, 0.924F, 
            0.0F, 0.0F,  0.707F, 0.707F, 
            0.0F, 0.0F,  0.707F, 0.707F, 
            0.0F, 0.0F,  0.383F, 0.924F, 
            0.0F, 0.0F,  0.0F,   1.0F, 
            0.0F, 0.0F,  0.0F,   1.0F, 
            0.0F, 0.0F, -0.383F, 0.924F, 
            0.0F, 0.0F, -0.707F, 0.707F, 
            0.0F, 0.0F, -0.707F, 0.707F, 
            0.0F, 0.0F, -0.383F, 0.924F, 
            0.0F, 0.0F,  0.0F,   1.0F 
        };
        // @formatter:on
        AccessorModel rotationsAccessorModel =
            AccessorModels.createFloat4D(FloatBuffer.wrap(rotations));

        // Let the animation model rotate the jointNodeModel1
        Sampler sampler = new DefaultSampler(timesAcessorModel,
            Interpolation.LINEAR, rotationsAccessorModel);
        Channel channel =
            new DefaultChannel(sampler, jointNodeModel1, "rotation");
        animationModel.addChannel(channel);

        // Create a scene model with the main node and root joint node
        DefaultSceneModel sceneModel = new DefaultSceneModel();
        sceneModel.addNode(nodeModel);
        sceneModel.addNode(jointNodeModel0);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        gltfModelBuilder.addAnimationModel(animationModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();
        return gltfModel;
    }

    /**
     * Create a glTF model with material variants
     * 
     * @return The model
     */
    static DefaultGltfModel createMaterialVariants()
    {
        // Create the mesh primitive model
        DefaultMeshPrimitiveModel meshPrimitiveModel =
            createSquareMeshPrimitiveWithTexcoords();

        // Create the material variants
        DefaultPbrMaterialModel materialModelA =
            createBaseColorTextureMaterialModel("variantA.png");
        DefaultPbrMaterialModel materialModelB =
            createBaseColorTextureMaterialModel("variantB.png");
        DefaultPbrMaterialModel materialModelC =
            createBaseColorTextureMaterialModel("variantC.png");

        // Create the material variants of the mesh primitive
        DefaultMeshPrimitiveMaterialsVariantsModel meshPrimitiveMaterialsVariantsModel =
            new DefaultMeshPrimitiveMaterialsVariantsModel();
        meshPrimitiveMaterialsVariantsModel.setMaterialForVariant("variantA",
            materialModelA, null);
        meshPrimitiveMaterialsVariantsModel.setMaterialForVariant("variantB",
            materialModelB, null);
        meshPrimitiveMaterialsVariantsModel.setMaterialForVariant("variantC",
            materialModelC, null);
        meshPrimitiveModel.addExtensionModel("KHR_materials_variants",
            meshPrimitiveMaterialsVariantsModel);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
        DefaultGltfModel gltfModel = gltfModelBuilder.build();

        // Add the top-level variants model
        DefaultMaterialsVariantsModel materialVariantsModel =
            new DefaultMaterialsVariantsModel();
        materialVariantsModel.addName("variantA");
        materialVariantsModel.addName("variantB");
        materialVariantsModel.addName("variantC");
        gltfModel.addExtensionModel("KHR_materials_variants",
            materialVariantsModel);

        return gltfModel;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private GltfTestModelCreation()
    {
        // Private constructor to prevent instantiation
    }

}
