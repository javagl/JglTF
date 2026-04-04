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

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import de.javagl.jgltf.model.AnimationModel.Interpolation;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.creation.AccessorModels;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultPbrMetallicRoughnessModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.khr.materials_clearcoat.DefaultMaterialsClearcoatModel;

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
            createBaseColorTextureMaterialModel();
        meshPrimitiveModel.setMaterialModel(materialModel);

        DefaultSceneModel sceneModel = createSceneWith(meshPrimitiveModel);

        // Create the glTF model
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(sceneModel);
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
            createBaseColorTextureMaterialModel();
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
            createBaseColorTextureMaterialModel();
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
     * Create the accessor model for the square indices
     * 
     * @return The accessor model
     */
    private static DefaultAccessorModel craeteSquareIndices()
    {
        short[] indices = new short[]
        { 
            0, 1, 2, 
            1, 3, 2
        };
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
        float[] positions = new float[]
        {   
            0.0f, 0.0f, 0.0f, 
            1.0f, 0.0f, 0.0f, 
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f 
        };
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
        float[] texCoords = new float[]
        { 
            0.0f, 1.0f, 
            1.0f, 1.0f, 
            0.0f, 0.0f, 
            1.0f, 0.0f 
        };
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
    private static DefaultAnimationModel
        createSimpleRotationAnimation(NodeModel nodeModel)
    {
        // Create the times accessor
        float[] times = new float[]
        { 
            0.0f, 1.0f, 2.0f, 3.0f, 4.0f 
        };
        DefaultAccessorModel timesAccessorModel =
            AccessorModels.createFloatScalar(FloatBuffer.wrap(times));

        // Create the rotation accessor
        float[] rotations = new float[]
        { 
            0.0f, 0.0f, 0.0f, 1.0f, 
            0.0f, 0.0f, 0.707f, 0.707f, 
            0.0f, 0.0f, 1.0f,  0.0f, 
            0.0f, 0.0f, 0.707f, -0.707f, 
            0.0f, 0.0f, 0.0f, 1.0f 
        };
        DefaultAccessorModel rotationsAccessorModel =
            AccessorModels.createFloat4D(FloatBuffer.wrap(rotations));

        // Create the animation model with one channel and sampler for rotation
        DefaultAnimationModel animationModel = new DefaultAnimationModel();
        DefaultAnimationModel.DefaultSampler sampler =
            new DefaultAnimationModel.DefaultSampler(timesAccessorModel,
                Interpolation.LINEAR, rotationsAccessorModel);
        DefaultAnimationModel.DefaultChannel channel =
            new DefaultAnimationModel.DefaultChannel(sampler, nodeModel,
                "rotation");
        animationModel.addChannel(channel);

        return animationModel;
    }

    /**
     * Create a simple material model with a base color texture for tests
     * 
     * @return The material model
     */
    static DefaultPbrMaterialModel createBaseColorTextureMaterialModel()
    {
        DefaultPbrMaterialModel materialModel = new DefaultPbrMaterialModel();

        DefaultPbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            new DefaultPbrMetallicRoughnessModel();
        pbrMetallicRoughnessModel.setMetallicFactor(0.0);
        DefaultTextureInfoModel baseColorTextureInfoModel =
            new DefaultTextureInfoModel();

        DefaultTextureModel textureModel =
            createSimpleTextureModel("baseColor.png");
        baseColorTextureInfoModel.setTextureModel(textureModel);
        pbrMetallicRoughnessModel
            .setBaseColorTextureInfoModel(baseColorTextureInfoModel);

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
        clearcoatModel.setClearcoatTextureInfoModel(clearcoatTextureInfoModel);
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
        DefaultTextureModel textureModel = new DefaultTextureModel();
        String imageText = createImageText(uri);
        int fontSize = 12;
        Color foreground = new Color(255, 0, 0);
        Color background = new Color(0, 0, 255);
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
     * Private constructor to prevent instantiation
     */
    private GltfTestModelCreation()
    {
        // Private constructor to prevent instantiation
    }

}
