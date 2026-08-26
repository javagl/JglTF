/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.asset.creator.basic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;

import de.javagl.geogen.Geometries;
import de.javagl.geogen.Geometry;
import de.javagl.geogen.Normals;
import de.javagl.geogen.generation.procedural.ProceduralGeometries;
import de.javagl.geogen.generation.procedural.ProceduralTextures;
import de.javagl.jgltf.asset.creator.utilities.MeshPrimitiveGeometries;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.MaterialBuilder;
import de.javagl.jgltf.model.creation.MaterialModels;
import de.javagl.jgltf.model.creation.TextureModels;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;

/**
 * A class for creating basic glTF assets with different numbers of elements, to
 * be used as stress tests.
 * 
 * Some details are intentionally not (yet) specified here.
 */
public class BasicAssetCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(BasicAssetCreator.class.getName());

    /**
     * The default log level
     */
    private final Level level = Level.FINE;

    /**
     * The spacing between nodes in the grid of nodes, in x-direction
     */
    private static final double GRID_SPACING_X = 1.1f;

    /**
     * The spacing between nodes in the grid of nodes, in y-direction
     */
    private static final double GRID_SPACING_Y = 1.1f;

    /**
     * The spacing between nodes in the grid of nodes, in z-direction
     */
    private static final double GRID_SPACING_Z = 1.1f;

    /**
     * The {@link DefaultMeshPrimitiveModel} objects
     */
    private List<DefaultMeshPrimitiveModel> meshPrimitives;

    /**
     * The {@link DefaultTextureModel} objects
     */
    private List<DefaultTextureModel> textures;

    /**
     * The {@link DefaultPbrMaterialModel} objects
     */
    private List<DefaultPbrMaterialModel> materials;

    /**
     * The {@link DefaultMeshModel} objects
     */
    private List<DefaultMeshModel> meshes;

    /**
     * The {@link DefaultNodeModel} objects
     */
    private List<DefaultNodeModel> nodes;

    /**
     * Creates a new instance
     */
    public BasicAssetCreator()
    {
        // Nothing to do here
    }

    /**
     * Create a {@link DefaultGltfModel} with the given configuration.
     * 
     * @param config The configuration
     * @return The {@link DefaultGltfModel}
     */
    public DefaultGltfModel create(Config config)
    {
        logger.log(level, "Configuration:\n" + Configs.createString(config));

        int numMeshPrimitives = config.numMeshPrimitives;
        int pointSizes[][] = config.pointSizes;

        int numTextures = config.numTextures;
        int pixelSizes[][] = config.pixelSizes;

        int numMaterials = config.numMaterials;
        int numMeshes = config.numMeshes;
        int numMeshPrimitivesPerMesh = config.numMeshPrimitivesPerMesh;
        int numNodes = config.numNodes;

        int gridDimensions = config.gridDimensions;
        boolean noiseGeometry = config.noiseGeometry;
        boolean noiseTextures = config.noiseTextures;

        this.meshPrimitives = new ArrayList<DefaultMeshPrimitiveModel>();
        this.textures = new ArrayList<DefaultTextureModel>();
        this.materials = new ArrayList<DefaultPbrMaterialModel>();
        this.meshes = new ArrayList<DefaultMeshModel>();
        this.nodes = new ArrayList<DefaultNodeModel>();

        logger.log(level, "Creating " + numTextures + " textures...");
        createTextures(numTextures, pixelSizes, noiseTextures);

        logger.log(level, "Creating " + numMaterials + " materials...");
        createMaterials(numMaterials);

        logger.log(level,
            "Creating " + numMeshPrimitives + " mesh primitives...");
        createMeshPrimitives(numMeshPrimitives, pointSizes, noiseGeometry);

        logger.log(level, "Creating " + numMeshes + " meshes...");
        createMeshes(numMeshes, numMeshPrimitivesPerMesh);

        logger.log(level, "Creating " + numNodes + " nodes...");
        if (gridDimensions == 3)
        {
            createFlatNodesGrid3D(numNodes);
        }
        else if (gridDimensions == 2)
        {
            createFlatNodesGrid2D(numNodes);
        }
        else
        {
            createFlatNodesGrid1D(numNodes);
        }

        DefaultSceneModel scene = new DefaultSceneModel();
        for (DefaultNodeModel node : nodes)
        {
            scene.addNode(node);
        }
        GltfModelBuilder b = GltfModelBuilder.create();
        b.addSceneModel(scene);
        DefaultGltfModel model = b.build();

        logger.log(level, "Done.");
        return model;
    }

    /**
     * Create the {@link DefaultMeshPrimitiveModel} objects
     * 
     * @param numMeshPrimitives The number of instances to create
     * @param pointSizes The point sizes for the instances
     * @param noiseGeometry Whether the geometry should be noisy
     */
    private void createMeshPrimitives(int numMeshPrimitives, int pointSizes[][],
        boolean noiseGeometry)
    {
        for (int i = 0; i < numMeshPrimitives; i++)
        {
            int pointSize[] = pointSizes[i % pointSizes.length];
            int numPointsX = pointSize[0];
            int numPointsY = pointSize[1];

            if (numPointsX * numPointsY > 65500)
            {
                logger.warning(
                    "Too many vertices in mesh: " + (numPointsX * numPointsY));
            }

            Geometry geometry = null;
            if (noiseGeometry)
            {
                geometry = ProceduralGeometries.createPerlinNoise(numPointsX,
                    numPointsY, 3, i);
                Geometries.transform(geometry, FloatBuffer.wrap(new float[]
                { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0.1f, 0, 0, 0, 0, 1, }));
                Normals.computeSmoothNormals(4, geometry.getIndices(),
                    geometry.getPositions3D(), geometry.getNormals3D());
            }
            else
            {
                geometry =
                    Geometries.createPlaneGeometry(numPointsX, numPointsY);
            }

            Geometries.flipTexCoordsY(geometry);
            DefaultMeshPrimitiveModel meshPrimitive =
                MeshPrimitiveGeometries.createTextured(geometry);
            meshPrimitives.add(meshPrimitive);
        }
    }

    /**
     * Create the {@link DefaultTextureModel} objects
     * 
     * @param numTextures The number of instances to create
     * @param pixelSizes The pixel sizes for the instances
     * @param noiseTextures Whether the textures should contain noise
     */
    private void createTextures(int numTextures, int pixelSizes[][],
        boolean noiseTextures)
    {
        if (numTextures != 0 && pixelSizes.length == 0)
        {
            throw new GltfException("Cannot generate " + numTextures
                + " textures when 0 pixelSizes are given");
        }
        for (int i = 0; i < numTextures; i++)
        {
            int pixelSize[] = pixelSizes[i % pixelSizes.length];
            int sizeX = pixelSize[0];
            int sizeY = pixelSize[1];
            int fontSizePx = Math.round(sizeY / 8);
            String labelText = "<html>&nbsp;Texture " + i + "<br>&nbsp;" + sizeX
                + "x" + sizeY + "<html>";

            BufferedImage image;
            if (noiseTextures)
            {
                float rel = (float) i / numTextures;
                float h = rel;
                float s0 = 0.4f;
                float s1 = 0.8f;
                float b0 = 0.4f;
                float b1 = 0.8f;
                Color c0 = new Color(Color.HSBtoRGB(h, s0, b0));
                Color c1 = new Color(Color.HSBtoRGB(h, s1, b1));
                image = ProceduralTextures.createPerlinNoise(sizeX, sizeY, 4, i,
                    c0, c1);
            }
            else
            {
                float rel = (float) i / numTextures;
                float h = rel;
                float s = 0.8f;
                float b = 0.8f;
                Color c = new Color(Color.HSBtoRGB(h, s, b));
                image = new BufferedImage(sizeX, sizeY,
                    BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();
                g.setColor(c);
                g.fillRect(0, 0, sizeX, sizeY);
                g.dispose();
            }

            JLabel label = new JLabel(labelText);
            label.setForeground(Color.BLACK);
            label.setFont(new Font("Monospaced", Font.BOLD, fontSizePx));
            label.setOpaque(false);
            label.setSize(sizeX, sizeY);
            Graphics g = image.getGraphics();
            label.paint(g);
            g.dispose();
            DefaultTextureModel texture = TextureModels
                .createFromBufferedImage(image, "image.png", "image/png");
            textures.add(texture);
        }
    }

    /**
     * Create the {@link DefaultPbrMaterialModel} objects
     * 
     * @param numMaterials The number of instances to create
     */
    private void createMaterials(int numMaterials)
    {
        for (int i = 0; i < numMaterials; i++)
        {
            if (textures.size() == 0)
            {
                DefaultPbrMaterialModel materialModel =
                    MaterialModels.createFromBaseColor(0.5f, 0.5f, 0.5f, 0.5f);
                materials.add(materialModel);
            }
            else
            {
                DefaultTextureModel texture = textures.get(i % textures.size());
                logger.finer(
                    "Material " + i + " using texture " + (i % textures.size())
                        + " id " + System.identityHashCode(texture));
                MaterialBuilder builder = MaterialBuilder.create();
                builder.setDoubleSided(true);
                builder.setMetallicRoughnessFactors(0.0f, 1.0f);
                builder.setBaseColorTexture(texture, null);
                DefaultPbrMaterialModel material = builder.build();
                materials.add(material);
            }
        }
    }

    /**
     * Create the {@link DefaultMeshModel} objects
     * 
     * @param numMeshes The number of instances to create
     * @param numMeshPrimitivesPerMesh The number of primitives per mesh
     */
    private void createMeshes(int numMeshes, int numMeshPrimitivesPerMesh)
    {
        if (numMeshes != 0 && meshPrimitives.size() == 0)
        {
            throw new GltfException("Cannot generate " + numMeshes
                + " meshes when 0 meshPrimitiveGeometries are given");
        }

        int index = 0;
        for (int i = 0; i < numMeshes; i++)
        {
            DefaultMeshModel mesh = new DefaultMeshModel();
            for (int j = 0; j < numMeshPrimitivesPerMesh; j++)
            {
                DefaultMeshPrimitiveModel meshPrimitive =
                    meshPrimitives.get(index % meshPrimitives.size());
                logger.finer("Mesh " + i + " primitive " + j + " is "
                    + (index % meshPrimitives.size()) + " with material "
                    + meshPrimitive.getMaterialModel());

                DefaultMeshPrimitiveModel m =
                    new DefaultMeshPrimitiveModel(meshPrimitive.getMode());
                m.setIndices(meshPrimitive.getIndices());
                Map<String, AccessorModel> attributes =
                    meshPrimitive.getAttributes();
                m.putAttribute("POSITION", attributes.get("POSITION"));
                m.putAttribute("NORMAL", attributes.get("NORMAL"));
                m.putAttribute("TEXCOORD_0", attributes.get("TEXCOORD_0"));
                if (materials.size() > 0)
                {
                    PbrMaterialModel material =
                        materials.get(index % materials.size());
                    m.setMaterialModel(material);
                }

                mesh.addMeshPrimitiveModel(m);
                index++;
            }
            meshes.add(mesh);
        }
    }

    /**
     * Create the {@link NodeModel} objects
     * 
     * @param numNodes The number of instances to create
     */
    private void createFlatNodesGrid3D(int numNodes)
    {
        double sx = BasicAssetCreator.GRID_SPACING_X;
        double sy = BasicAssetCreator.GRID_SPACING_Y;
        double sz = BasicAssetCreator.GRID_SPACING_Z;

        int gridSize = (int) Math.ceil(Math.cbrt(numNodes));
        int index = 0;
        for (int x = 0; x < gridSize; x++)
        {
            for (int y = 0; y < gridSize; y++)
            {
                for (int z = 0; z < gridSize; z++)
                {
                    if (index < numNodes)
                    {
                        DefaultNodeModel node = new DefaultNodeModel();
                        node.setTranslation(new double[]
                        { x * sx, y * sy, z * sz });

                        if (meshes.size() > 0)
                        {
                            DefaultMeshModel mesh =
                                meshes.get(index % meshes.size());
                            logger.finer("Node " + index + " using mesh "
                                + (index % meshes.size()));
                            node.addMeshModel(mesh);
                        }
                        nodes.add(node);
                    }
                    index++;
                }
            }
        }
    }

    /**
     * Create the {@link NodeModel} objects
     * 
     * @param numNodes The number of instances to create
     */
    private void createFlatNodesGrid2D(int numNodes)
    {
        double sx = BasicAssetCreator.GRID_SPACING_X;
        double sy = BasicAssetCreator.GRID_SPACING_Y;

        int gridSize = (int) Math.ceil(Math.sqrt(numNodes));
        int index = 0;
        for (int x = 0; x < gridSize; x++)
        {
            for (int y = 0; y < gridSize; y++)
            {
                if (index < numNodes)
                {
                    DefaultNodeModel node = new DefaultNodeModel();
                    node.setTranslation(new double[]
                    { x * sx, y * sy, 0.0f });

                    if (meshes.size() > 0)
                    {
                        DefaultMeshModel mesh =
                            meshes.get(index % meshes.size());
                        logger.finer("Node " + index + " using mesh "
                            + (index % meshes.size()));
                        node.addMeshModel(mesh);
                    }
                    nodes.add(node);
                }
                index++;
            }
        }
    }

    /**
     * Create the {@link NodeModel} objects
     * 
     * @param numNodes The number of instances to create
     */
    private void createFlatNodesGrid1D(int numNodes)
    {
        double sx = BasicAssetCreator.GRID_SPACING_X;

        int gridSize = numNodes;
        int index = 0;
        for (int x = 0; x < gridSize; x++)
        {
            if (index < numNodes)
            {
                DefaultNodeModel node = new DefaultNodeModel();
                node.setTranslation(new double[]
                { x * sx, 0.0f, 0.0f });

                if (meshes.size() > 0)
                {
                    DefaultMeshModel mesh = meshes.get(index % meshes.size());
                    logger.finer("Node " + index + " using mesh "
                        + (index % meshes.size()));
                    node.addMeshModel(mesh);
                }
                nodes.add(node);
            }
            index++;
        }
    }
}
