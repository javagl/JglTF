/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.obj;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Asset;
import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Material;
import de.javagl.jgltf.impl.Mesh;
import de.javagl.jgltf.impl.MeshPrimitive;
import de.javagl.jgltf.impl.Node;
import de.javagl.jgltf.impl.Scene;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.Texture;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.io.BufferViews;
import de.javagl.jgltf.model.io.GltfDataResolver;
import de.javagl.jgltf.model.io.IO;
import de.javagl.obj.Mtl;
import de.javagl.obj.MtlReader;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjGroup;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import de.javagl.obj.ReadableObj;

/**
 * A class for creating {@link GltfData} objects from OBJ files 
 */
public class ObjGltfDataCreator
{
    // TODO The configuration, consisting of the BufferStrategy,
    // the indicesComponentType and the OBJ splitter, should be
    // summarized in a dedicated class, to ensure consistency:
    // Currently, the splitter does not consider the indices
    // component type, but always splits assuming that the 
    // component type should be GL_UNSIGNED_SHORT
    
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ObjGltfDataCreator.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.FINE;
    
    /**
     * The {@link GlTF} that is currently being built
     */
    private GlTF gltf;

    /**
     * The {@link GltfData} that is currently being built
     */
    private GltfData gltfData;
    
    /**
     * An enumeration of strategies for the creation of {@link Buffer}
     * instances
     */
    public enum BufferStrategy
    {
        /**
         * Create one {@link Buffer} for the whole OBJ file
         */
        BUFFER_PER_FILE,
        
        /**
         * Create one {@link Buffer} for each MTL group
         */
        BUFFER_PER_GROUP,
        
        /**
         * Create one {@link Buffer} for each part (for the case that
         * the geometry data was split into multiple parts to obey 
         * the 16 bit index constraint)
         */
        BUFFER_PER_PART,
    }
    
    /**
     * The {@link BufferStrategy} that should be used
     */
    private BufferStrategy bufferStrategy;
    
    /**
     * The {@link BufferCreator} that will receive the geometry information
     * from the OBJ, and build the {@link Accessor}s, {@link BufferView}s,
     * {@link MeshPrimitive}s and {@link Buffer}s.
     */
    private BufferCreator bufferCreator;
    
    /**
     * The {@link TechniqueHandler} that maintains the {@link Technique}s
     * that are required for the {@link GlTF}
     */
    private TechniqueHandler techniqueHandler;
    
    /**
     * The {@link TextureHandler} that maintains the {@link Image}s and
     * {@link Texture}s that are required for the {@link GlTF}
     */
    private TextureHandler textureHandler;
    
    /**
     * The component type for the indices of the resulting glTF. 
     * For glTF 1.0, this may at most be GL_UNSIGNED_SHORT.  
     */
    private int indicesComponentType = GltfConstants.GL_UNSIGNED_SHORT;
    
    /**
     * A function that will receive all OBJ groups that should be processed,
     * and may (or may not) split them into multiple parts. 
     */
    private final Function<? super ReadableObj, List<? extends ReadableObj>> 
        objSplitter;
    
    /**
     * For testing: Assign a material with a random color to each 
     * mesh primitive that was created during the splitting process
     */
    private boolean assigningRandomColorsToParts = false;
    
    /**
     * Default constructor
     */
    public ObjGltfDataCreator()
    {
        this(BufferStrategy.BUFFER_PER_FILE);
    }

    /**
     * Creates a new glTF data creator
     * 
     * @param bufferStrategy The {@link BufferStrategy} to use
     */
    public ObjGltfDataCreator(BufferStrategy bufferStrategy)
    {
        this.bufferStrategy = bufferStrategy;
        this.objSplitter = obj -> ObjSplitting.split(obj);
    }
    
    /**
     * Set the {@link BufferStrategy} to use
     * 
     * @param bufferStrategy The {@link BufferStrategy}
     */
    public void setBufferStrategy(BufferStrategy bufferStrategy)
    {
        this.bufferStrategy = bufferStrategy;
    }
    
    /**
     * Set the component type for the indices of the {@link MeshPrimitive}s
     * 
     * @param indicesComponentType The component type
     * @throws IllegalArgumentException If the given type is not
     * GL_UNSIGNED_BYTE, GL_UNSIGNED_SHORT or GL_UNSIGNED_INT
     */
    public void setIndicesComponentType(int indicesComponentType)
    {
        List<Integer> validTypes = Arrays.asList(
            GltfConstants.GL_UNSIGNED_BYTE,
            GltfConstants.GL_UNSIGNED_SHORT,
            GltfConstants.GL_UNSIGNED_INT);
        if (!validTypes.contains(indicesComponentType))
        {
            throw new IllegalArgumentException(
                "The indices component type must be GL_UNSIGNED_BYTE," + 
                "GL_UNSIGNED_SHORT or GL_UNSIGNED_INT, but is " +
                GltfConstants.stringFor(indicesComponentType));
        }
        this.indicesComponentType = indicesComponentType;
    }
    
    /**
     * For testing and debugging: Assign random colors to the parts that
     * are created when splitting the OBJ data
     * 
     * @param assigningRandomColorsToParts The flag
     */
    public void setAssigningRandomColorsToParts(
        boolean assigningRandomColorsToParts)
    {
        this.assigningRandomColorsToParts = assigningRandomColorsToParts;
    }
    
    /**
     * Create a {@link GltfData} from the OBJ file with the given URI
     * 
     * @param objUri The OBJ URI
     * @return The {@link GltfData}
     * @throws IOException If an IO error occurs
     */
    public GltfData create(URI objUri) throws IOException
    {
        logger.log(level, "Creating glTF with " + bufferStrategy + 
            " buffer strategy");
        
        // Basic setup 
        gltf = new GlTF();
        gltf.setAsset(createAsset());
        gltfData = new GltfData(gltf);
        techniqueHandler = new TechniqueHandler(gltf);
        textureHandler = new TextureHandler(gltf);
        
        // Obtain the relative path information and file names
        logger.log(level, "Resolving paths from " + objUri);
        URI baseUri = IO.getParent(objUri);
        String objFileName = IO.extractFileName(objUri);
        String baseName = stripFileNameExtension(objFileName);
        String mtlFileName = baseName + ".mtl";
        URI mtlUri = IO.makeAbsolute(baseUri, mtlFileName);
        
        // Read the input data
        Obj obj = readObj(objUri);
        Map<String, Mtl> mtls = Collections.emptyMap();
        if (IO.existsUnchecked(mtlUri))
        {
            mtls = readMtls(mtlUri);
        }
        
        // Prepare creating a Buffer containing the data that the
        // BufferViews of the Accessors of the MeshPrimitives of
        // the Mesh will refer to. Yeah, it's a bit fiddly.
        bufferCreator = new BufferCreator(gltf, baseName);
        
        // Create the MeshPrimitives from the OBJ and MTL data
        List<MeshPrimitive> meshPrimitives = 
            createMeshPrimitives(obj, mtls);
        
        if (bufferStrategy == BufferStrategy.BUFFER_PER_FILE)
        {
            bufferCreator.commitBuffer();
        }
        
        bufferCreator.getBufferDatas().entrySet().forEach(
            e -> gltfData.putBufferData(e.getKey(), e.getValue()));
        
        // Create the basic scene graph structure, consisting of a single Mesh 
        // (containing the MeshPrimitives) in a single Node in a single Scene
        Mesh mesh = new Mesh();
        mesh.setPrimitives(meshPrimitives);
        String meshId = Gltfs.generateId("mesh", gltf.getMeshes());
        gltf.addMeshes(meshId, mesh);

        Node node = new Node();
        node.setMeshes(Collections.singletonList(meshId));
        String nodeId = Gltfs.generateId("node", gltf.getNodes());
        gltf.addNodes(nodeId, node);
        
        Scene scene = new Scene();
        scene.setNodes(Collections.singletonList(nodeId));
        String sceneId = Gltfs.generateId("scene", gltf.getScenes());
        gltf.addScenes(sceneId, scene);
        
        gltf.setScene(sceneId);

        // Finally assemble the GltfData
        BufferViews.createBufferViewByteBuffers(gltfData);
        
        // Resolve the image data, by reading the data from the URIs in
        // the Images, resolved against the root path of the input OBJ
        logger.log(level, "Resolving Image data");
        GltfDataResolver imageDataResolver = 
            new GltfDataResolver(gltfData, baseUri);
        imageDataResolver.resolveImages();
        
        // Resolve the shader data, by reading the resources that are
        // referenced via the Shader URIs
        logger.log(level, "Resolving Shader data");
        GltfDataResolver shaderDataResolver = 
            new GltfDataResolver(gltfData, createResourceResolver());
        shaderDataResolver.resolveShaders();

        return gltfData;
    }
    
    /**
     * Create the {@link Asset} for the generated {@link GlTF} 
     * 
     * @return The {@link Asset}
     */
    private static Asset createAsset()
    {
        Asset asset = new Asset();
        asset.setGenerator("jgltf-obj from https://github.com/javagl/JglTF");
        asset.setVersion("1.0.0");
        return asset;
    }

    /**
     * Create the {@link MeshPrimitive}s for the given OBJ- and MTL data
     * 
     * @param obj The OBJ
     * @param mtls The MTLs
     * @return The {@link MeshPrimitive}s
     */
    private List<MeshPrimitive> createMeshPrimitives(
        Obj obj, Map<String, Mtl> mtls)
    {
        // When there are no materials, create the MeshPrimitives for the OBJ
        int numMaterialGroups = obj.getNumMaterialGroups();
        if (numMaterialGroups == 0 || mtls.isEmpty())
        {
            return createMeshPrimitives(obj);
        }
        
        // Create the MeshPrimitives for the material groups
        List<MeshPrimitive> meshPrimitives = new ArrayList<MeshPrimitive>();
        for (int i = 0; i < numMaterialGroups; i++)
        {
            ObjGroup materialGroup = obj.getMaterialGroup(i);
            String materialGroupName = materialGroup.getName();
            Obj materialObj = ObjUtils.groupToObj(obj, materialGroup, null);
            Mtl mtl = mtls.get(materialGroupName);
            
            logger.log(level, "Creating MeshPrimitive for material " + 
                materialGroupName);

            String idSuffix = String.valueOf(i);
            List<MeshPrimitive> subMeshPrimitives = createPartMeshPrimitives(
                materialObj, idSuffix);

            // Create the material that will be assigned to all mesh primitives
            boolean withTexture = obj.getNumTexCoords() > 0 && 
                mtl != null && mtl.getMapKd() != null;
            boolean withNormals = obj.getNumNormals() > 0;
            Material material = null;
            if (withTexture)
            {
                material = createMaterialWithTexture(withNormals, mtl);
            }
            else
            {
                material = createMaterialWithColor(
                    withNormals, 0.75f, 0.75f, 0.75f);
            }
            
            String materialId = 
                Gltfs.generateId("material", gltf.getMaterials());
            gltf.addMaterials(materialId, material);
            for (MeshPrimitive subMeshPrimitive : subMeshPrimitives)
            {
                subMeshPrimitive.setMaterial(materialId);
            }
            meshPrimitives.addAll(subMeshPrimitives);
            
            if (bufferStrategy == BufferStrategy.BUFFER_PER_GROUP)
            {
                bufferCreator.commitBuffer();
            }
            
        }
        return meshPrimitives;
    }

    
    /**
     * Create simple {@link MeshPrimitive}s for the given OBJ
     * 
     * @param obj The OBJ
     * @return The {@link MeshPrimitive}s
     */
    private List<MeshPrimitive> createMeshPrimitives(Obj obj)
    {
        logger.log(level, "Creating MeshPrimitives for OBJ");
        
        List<MeshPrimitive> meshPrimitives = 
            createPartMeshPrimitives(obj, "");
        
        if (assigningRandomColorsToParts)
        {
            Random random = new Random(0);
            boolean withNormals = obj.getNumNormals() > 0;
            for (MeshPrimitive meshPrimitive : meshPrimitives)
            {
                float r = random.nextFloat(); 
                float g = random.nextFloat(); 
                float b = random.nextFloat(); 
                Material material = 
                    createMaterialWithColor(withNormals, r, g, b);
                String materialId = 
                    Gltfs.generateId("material", gltf.getMaterials());
                gltf.addMaterials(materialId, material);
                meshPrimitive.setMaterial(materialId);
            }
        }
        else
        {
            // Default case: Assign a single, gray material to all 
            // mesh primitives
            boolean withNormals = obj.getNumNormals() > 0;
            Material material = createMaterialWithColor(
                withNormals, 0.75f, 0.75f, 0.75f);
            String materialId = 
                Gltfs.generateId("material", gltf.getMaterials());
            gltf.addMaterials(materialId, material);
            for (MeshPrimitive meshPrimitive : meshPrimitives)
            {
                meshPrimitive.setMaterial(materialId);
            }
        }
        if (bufferStrategy == BufferStrategy.BUFFER_PER_GROUP)
        {
            bufferCreator.commitBuffer();
        }
        return meshPrimitives;
    }

    /**
     * Create a simple {@link Material} with a texture that will be taken from 
     * the diffuse map of the given MTL. The given MTL and its diffuse
     * map must be non-<code>null</code>.
     * 
     * @param withNormals Whether the {@link Material} should support normals
     * @param mtl The MTL
     * @return The {@link Material}
     */
    private Material createMaterialWithTexture(
        boolean withNormals, Mtl mtl)
    {
        boolean withTexture = true;
        String techniqueId = 
            techniqueHandler.getTechniqueId(
                withTexture, withNormals);
        
        // Create the material for the MeshPrimitive
        Material material = new Material();
        material.setTechnique(techniqueId);
        String imageUri = mtl.getMapKd();
        String textureId = textureHandler.getTextureId(imageUri);
        Map<String, Object> materialValues = 
            MtlMaterialValues.createMaterialValues(mtl, textureId);
        material.setValues(materialValues);
        return material;
    }
    
    /**
     * Create a simple {@link Material} with the given diffuse color
     * 
     * @param withNormals Whether the {@link Material} should support normals
     * @param r The red component of the diffuse color
     * @param g The green component of the diffuse color
     * @param b The blue component of the diffuse color
     * @return The {@link Material}
     */
    private Material createMaterialWithColor(
        boolean withNormals, float r, float g, float b)
    {
        boolean withTexture = false;
        String techniqueId = 
            techniqueHandler.getTechniqueId(withTexture, withNormals);
        Material material = new Material();
        material.setTechnique(techniqueId);
        Map<String, Object> materialValues = 
            MtlMaterialValues.createDefaultMaterialValues(r, g, b);
        material.setValues(materialValues);
        return material;
    }
    
    /**
     * Read the OBJ from the given URI, and return it as a "renderable" OBJ,
     * which contains only triangles, has unique vertex coordinates and
     * normals, and is single-indexed 
     * 
     * @param objUri The OBJ URI
     * @return The OBJ
     * @throws IOException If an IO error occurs
     */
    private static Obj readObj(URI objUri) throws IOException
    {
        logger.log(level, "Reading OBJ from " + objUri);
        
        try (InputStream objInputStream =  objUri.toURL().openStream())
        {
            Obj obj = ObjReader.read(objInputStream);
            return ObjUtils.convertToRenderable(obj);
        }
    }

    /**
     * Read a mapping from material names to MTL objects from the given URI
     * 
     * @param mtlUri The MTL URI
     * @return The mapping
     * @throws IOException If an IO error occurs
     */
    private static Map<String, Mtl> readMtls(URI mtlUri) throws IOException
    {
        logger.log(level, "Reading MTL from " + mtlUri);

        try (InputStream mtlInputStream = mtlUri.toURL().openStream())
        {
            List<Mtl> mtlList = MtlReader.read(mtlInputStream);
            Map<String, Mtl> mtls = mtlList.stream().collect(
                LinkedHashMap::new, 
                (map, mtl) -> map.put(mtl.getName(), mtl), 
                (map0, map1) -> map0.putAll(map1));
            return mtls;
        }
    }


    /**
     * Create the {@link MeshPrimitive}s from the given OBJ data.
     * 
     * @param obj The OBJ
     * @param idSuffix The suffix that will be appended to the IDs that
     * are created for {@link BufferView} and {@link Accessor} instances
     * @return The {@link MeshPrimitive}
     */
    private List<MeshPrimitive> createPartMeshPrimitives(
        Obj obj, String idSuffix)
    {
        List<MeshPrimitive> meshPrimitives = new ArrayList<MeshPrimitive>();
        List<? extends ReadableObj> parts = objSplitter.apply(obj);
        for (int i = 0; i < parts.size(); i++)
        {
            ReadableObj part = parts.get(i);
            String partIdSuffix = idSuffix;
            if (parts.size() > 1)
            {
                partIdSuffix += "_"+i;
            }
            
            // Obtain the data from the OBJ
            int numVerticesPerFace = 3;
            IntBuffer objIndices = 
                ObjData.getFaceVertexIndices(part, numVerticesPerFace);
            FloatBuffer objVertices = ObjData.getVertices(part);
            FloatBuffer objTexCoords = ObjData.getTexCoords(part, 2);
            for (int j = 1; j < objTexCoords.capacity(); j += 2)
            {
                objTexCoords.put(j, 1.0f - objTexCoords.get(j));
            }
            FloatBuffer objNormals = ObjData.getNormals(part);
            
            // Generate a MeshPrimitive for the current OBJ data
            MeshPrimitive meshPrimitive = 
                bufferCreator.createMeshPrimitive(partIdSuffix, 
                    objIndices, indicesComponentType, 
                    objVertices, objTexCoords, objNormals);
            meshPrimitives.add(meshPrimitive);
            
            if (bufferStrategy == BufferStrategy.BUFFER_PER_PART)
            {
                bufferCreator.commitBuffer();
            }
            
        }
        return meshPrimitives;
    }
    
    
    
    /**
     * Create a function that maps a string to the input stream of a resource
     * with the same name of this class

     * @return The resolving function
     */
    private static Function<String, InputStream> createResourceResolver()
    {
        return new Function<String, InputStream>()
        {
            @Override
            public InputStream apply(String uriString)
            {
                InputStream inputStream = 
                    ObjGltfDataCreator.class.getResourceAsStream(
                        "/" + uriString);
                return inputStream;
            }
        };
    }
    
    /**
     * Remove the extension from the given file name. That is, the part 
     * starting with the last <code>'.'</code> dot. If the given file name
     * does not contain a dot, it will be returned unmodified
     * 
     * @param fileName The file name
     * @return The file name without the extension
     */
    private static String stripFileNameExtension(String fileName)
    {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0)
        {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }
}
