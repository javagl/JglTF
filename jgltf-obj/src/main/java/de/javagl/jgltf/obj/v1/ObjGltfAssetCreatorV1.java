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
package de.javagl.jgltf.obj.v1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
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

import de.javagl.jgltf.impl.v1.Accessor;
import de.javagl.jgltf.impl.v1.Asset;
import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Mesh;
import de.javagl.jgltf.impl.v1.MeshPrimitive;
import de.javagl.jgltf.impl.v1.Node;
import de.javagl.jgltf.impl.v1.Scene;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.creation.BufferStructure;
import de.javagl.jgltf.model.creation.BufferStructureBuilder;
import de.javagl.jgltf.model.creation.BufferStructureGltfV1;
import de.javagl.jgltf.model.creation.BufferStructures;
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.GltfReference;
import de.javagl.jgltf.model.io.GltfReferenceResolver;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.UriResolvers;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.v1.GltfIds;
import de.javagl.jgltf.obj.BufferStrategy;
import de.javagl.jgltf.obj.IntBuffers;
import de.javagl.jgltf.obj.ObjNormals;
import de.javagl.obj.Mtl;
import de.javagl.obj.MtlReader;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjGroup;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjSplitting;
import de.javagl.obj.ObjUtils;
import de.javagl.obj.ReadableObj;

/**
 * A class for creating {@link GltfAssetV1} objects from OBJ files 
 */
public class ObjGltfAssetCreatorV1
{
    // TODO Obviously, there's a lot of code duplication between
    // here and ObjGltfAssetCreatorV2. Try to refactor this.
    
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ObjGltfAssetCreatorV1.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.FINE;
    
    /**
     * The {@link GlTF} that is currently being built
     */
    private GlTF gltf;

    /**
     * The {@link BufferStrategy} that should be used
     */
    private BufferStrategy bufferStrategy;
    
    /**
     * The {@link BufferStructureBuilder}
     */
    private BufferStructureBuilder bufferStructureBuilder;
    
    /**
     * The {@link MtlMaterialHandlerV1}
     */
    private MtlMaterialHandlerV1 mtlMaterialHandler;
    
    /**
     * The component type for the indices of the resulting glTF. 
     * For glTF 1.0, this may at most be GL_UNSIGNED_SHORT.  
     */
    private int indicesComponentType = GltfConstants.GL_UNSIGNED_SHORT;
    
    /**
     * A function that will receive all OBJ groups that should be processed,
     * and may (or may not) split them into multiple parts. 
     */
    private Function<? super ReadableObj, List<? extends ReadableObj>> 
        objSplitter;
    
    /**
     * For testing: Assign a material with a random color to each 
     * mesh primitive that was created during the splitting process
     */
    private boolean assigningRandomColorsToParts = true;
    
    /**
     * Default constructor
     */
    public ObjGltfAssetCreatorV1()
    {
        this(BufferStrategy.BUFFER_PER_FILE);
    }

    /**
     * Creates a new glTF model creator
     * 
     * @param bufferStrategy The {@link BufferStrategy} to use
     */
    public ObjGltfAssetCreatorV1(BufferStrategy bufferStrategy)
    {
        this.bufferStrategy = bufferStrategy;
        setIndicesComponentType(GltfConstants.GL_UNSIGNED_SHORT);
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

        if (indicesComponentType == GltfConstants.GL_UNSIGNED_INT)
        {
            this.objSplitter = obj -> Collections.singletonList(obj);
        }
        else if (indicesComponentType == GltfConstants.GL_UNSIGNED_SHORT)
        {
            int maxNumVertices = 65536 - 3;
            this.objSplitter = 
                obj -> ObjSplitting.splitByMaxNumVertices(obj, maxNumVertices);
        }
        else if (indicesComponentType == GltfConstants.GL_UNSIGNED_BYTE)
        {
            int maxNumVertices = 256 - 3;
            this.objSplitter = 
                obj -> ObjSplitting.splitByMaxNumVertices(obj, maxNumVertices);
        }
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
     * Create a {@link GltfAssetV1} from the OBJ file with the given URI
     * 
     * @param objUri The OBJ URI
     * @return The {@link GltfAssetV1}
     * @throws IOException If an IO error occurs
     */
    public GltfAssetV1 create(URI objUri) throws IOException
    {
        logger.log(level, "Creating glTF with " + bufferStrategy + 
            " buffer strategy");
        
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
        return convert(obj, mtls, baseName, baseUri);
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
     * Convert the given OBJ into a {@link GltfAssetV1}.
     *   
     * @param obj The OBJ
     * @param mtlsMap The mapping from material names to MTL instances
     * @param baseName The base name for the glTF
     * @param baseUri The base URI to resolve shaders and textures against
     * @return The {@link GltfAssetV1}
     */
    GltfAssetV1 convert(
        ReadableObj obj, Map<String, Mtl> mtlsMap, String baseName, URI baseUri)
    {
        // Basic setup 
        gltf = new GlTF();
        gltf.setAsset(createAsset());
        mtlMaterialHandler = new MtlMaterialHandlerV1(gltf);
        bufferStructureBuilder = new BufferStructureBuilder();
        
        // Create the MeshPrimitives from the OBJ and MTL data
        Map<String, Mtl> mtls = 
            mtlsMap == null ? Collections.emptyMap() : mtlsMap;
        List<MeshPrimitive> meshPrimitives = 
            createMeshPrimitives(obj, mtls);
        
        if (bufferStrategy == BufferStrategy.BUFFER_PER_FILE)
        {
            int bufferCounter = bufferStructureBuilder.getNumBufferModels();
            String bufferName = "buffer" + bufferCounter;
            String uri = bufferName + ".bin";
            bufferStructureBuilder.createBufferModel(bufferName, uri);
        }
        
        // Transfer the data from the buffer structure into the glTF
        BufferStructure bufferStructure = bufferStructureBuilder.build();
        
        Map<String, Accessor> accessors = 
            BufferStructureGltfV1.createAccessors(bufferStructure);
        gltf.setAccessors(accessors);

        Map<String, BufferView> bufferViews = 
            BufferStructureGltfV1.createBufferViews(bufferStructure);
        gltf.setBufferViews(bufferViews);

        Map<String, Buffer> buffers = 
            BufferStructureGltfV1.createBuffers(bufferStructure);
        gltf.setBuffers(buffers);
        
        
        // Create the basic scene graph structure, consisting of a single Mesh 
        // (containing the MeshPrimitives) in a single Node in a single Scene
        Mesh mesh = new Mesh();
        mesh.setPrimitives(meshPrimitives);
        String meshId = GltfIds.generateId("mesh", gltf.getMeshes());
        gltf.addMeshes(meshId, mesh);

        Node node = new Node();
        node.setMeshes(Collections.singletonList(meshId));
        String nodeId = GltfIds.generateId("node", gltf.getNodes());
        gltf.addNodes(nodeId, node);
        
        Scene scene = new Scene();
        scene.setNodes(Collections.singletonList(nodeId));
        String sceneId = GltfIds.generateId("scene", gltf.getScenes());
        gltf.addScenes(sceneId, scene);
        
        gltf.setScene(sceneId);

        // Create the GltfAsset from the glTF
        GltfAssetV1 gltfAsset = new GltfAssetV1(gltf, null);
        
        // For each Buffer reference in the GltfAsset, obtain the data from the 
        // BufferModel instance that has been created by the 
        // BufferStructureBuilder.
        List<GltfReference> bufferReferences = gltfAsset.getBufferReferences();
        BufferStructures.resolve(bufferReferences, bufferStructure);
        
        if (baseUri != null)
        {
            // Resolve the image data, by reading the data from the URIs in
            // the Images, resolved against the root path of the input OBJ
            logger.log(level, "Resolving Image data");
            Function<String, ByteBuffer> externalUriResolver = 
                UriResolvers.createBaseUriResolver(baseUri);
            List<GltfReference> imageReferences = 
                gltfAsset.getImageReferences();
            GltfReferenceResolver.resolveAll(
                imageReferences, externalUriResolver);
            
            // Resolve the shader data, by reading the resources that are
            // referenced via the Shader URIs
            logger.log(level, "Resolving Shader data");
            Function<String, ByteBuffer> internalUriResolver = 
                UriResolvers.createResourceUriResolver(
                    ObjGltfAssetCreatorV1.class);
            List<GltfReference> shaderReferences = 
                gltfAsset.getShaderReferences();
            GltfReferenceResolver.resolveAll(
                shaderReferences, internalUriResolver);
        }
        
        return gltfAsset;
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
        asset.setVersion("1.0");
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
        ReadableObj obj, Map<String, Mtl> mtls)
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

            // If the material group is too large, it may have to
            // be split into multiple parts
            String name = "materialGroup_" + String.valueOf(i);
            List<MeshPrimitive> subMeshPrimitives = 
                createPartMeshPrimitives(materialObj, name);

            assignMaterial(subMeshPrimitives, obj, mtl);
            meshPrimitives.addAll(subMeshPrimitives);
            
            if (bufferStrategy == BufferStrategy.BUFFER_PER_GROUP)
            {
                int bufferCounter = bufferStructureBuilder.getNumBufferModels();
                String bufferName = "buffer" + bufferCounter;
                String uri = bufferName + ".bin";
                bufferStructureBuilder.createBufferModel(bufferName, uri);
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
    private List<MeshPrimitive> createMeshPrimitives(ReadableObj obj)
    {
        logger.log(level, "Creating MeshPrimitives for OBJ");
        
        List<MeshPrimitive> meshPrimitives = 
            createPartMeshPrimitives(obj, "obj");
        
        boolean withNormals = obj.getNumNormals() > 0;
        if (assigningRandomColorsToParts)
        {
            assignRandomColorMaterials(meshPrimitives, withNormals);
        }
        else
        {
            assignDefaultMaterial(meshPrimitives, withNormals);
        }
        if (bufferStrategy == BufferStrategy.BUFFER_PER_GROUP)
        {
            int bufferCounter = bufferStructureBuilder.getNumBufferModels();
            String bufferName = "buffer" + bufferCounter;
            String uri = bufferName + ".bin";
            bufferStructureBuilder.createBufferModel(bufferName, uri);
        }
        return meshPrimitives;
    }

    
    /**
     * Create a {@link Material} for the given OBJ and MTL, and assign it
     * to all the given {@link MeshPrimitive} instances
     * 
     * @param meshPrimitives The {@link MeshPrimitive} instances
     * @param obj The OBJ
     * @param mtl The MTL
     */
    private void assignMaterial(
        Iterable<? extends MeshPrimitive> meshPrimitives, 
        ReadableObj obj, Mtl mtl)
    {
        Material material = mtlMaterialHandler.createMaterial(obj, mtl);
        String materialId = 
            GltfIds.generateId("material", gltf.getMaterials());
        gltf.addMaterials(materialId, material);
        for (MeshPrimitive meshPrimitive : meshPrimitives)
        {
            meshPrimitive.setMaterial(materialId);
        }
    }
    
    /**
     * Create a default {@link Material}, and assign it to all the given 
     * {@link MeshPrimitive} instances
     * 
     * @param meshPrimitives The {@link MeshPrimitive} instances
     * @param withNormals Whether the {@link MeshPrimitive} instances have
     * normal information
     */
    private void assignDefaultMaterial(
        Iterable<? extends MeshPrimitive> meshPrimitives, boolean withNormals)
    {
        Material material = mtlMaterialHandler.createMaterialWithColor(
            withNormals, 0.75f, 0.75f, 0.75f);
        String materialId = 
            GltfIds.generateId("material", gltf.getMaterials());
        gltf.addMaterials(materialId, material);
        for (MeshPrimitive meshPrimitive : meshPrimitives)
        {
            meshPrimitive.setMaterial(materialId);
        }
    }

    /**
     * Create {@link Material} instances with random colors, and assign 
     * them to the given {@link MeshPrimitive} instances
     * 
     * @param meshPrimitives The {@link MeshPrimitive} instances
     * @param withNormals Whether the {@link MeshPrimitive} instances have
     * normal information
     */
    private void assignRandomColorMaterials(
        Iterable<? extends MeshPrimitive> meshPrimitives, boolean withNormals)
    {
        Random random = new Random(0);
        for (MeshPrimitive meshPrimitive : meshPrimitives)
        {
            float r = random.nextFloat(); 
            float g = random.nextFloat(); 
            float b = random.nextFloat(); 
            Material material = 
                mtlMaterialHandler.createMaterialWithColor(
                    withNormals, r, g, b);
            String materialId = 
                GltfIds.generateId("material", gltf.getMaterials());
            gltf.addMaterials(materialId, material);
            meshPrimitive.setMaterial(materialId);
        }
    }
    
    


    /**
     * Create the {@link MeshPrimitive}s from the given OBJ data.
     * 
     * @param obj The OBJ
     * @param name A name that will be used as a basis to create the IDs of 
     * the {@link BufferView} and {@link Accessor} instances
     * @return The {@link MeshPrimitive} list
     */
    private List<MeshPrimitive> createPartMeshPrimitives(
        ReadableObj obj, String name)
    {
        List<MeshPrimitive> meshPrimitives = new ArrayList<MeshPrimitive>();
        List<? extends ReadableObj> parts = objSplitter.apply(obj);
        for (int i = 0; i < parts.size(); i++)
        {
            ReadableObj part = parts.get(i);
            String partName = name;
            if (parts.size() > 1)
            {
                partName += "_part_" + i;
            }
            MeshPrimitive meshPrimitive =
                createMeshPrimitive(part, partName);
            meshPrimitives.add(meshPrimitive);
            if (bufferStrategy == BufferStrategy.BUFFER_PER_PART)
            {
                int bufferCounter = bufferStructureBuilder.getNumBufferModels();
                String bufferId = "buffer" + bufferCounter;
                String uri = bufferId + ".bin";
                bufferStructureBuilder.createBufferModel(bufferId, uri);
            }
        }
        return meshPrimitives;
    }
    
    /**
     * Create the {@link MeshPrimitive} from the given OBJ data.
     * 
     * @param part The OBJ
     * @param partName A name that will be used as a basis to create the IDs of 
     * the {@link BufferView} and {@link Accessor} instances
     * @return The {@link MeshPrimitive}
     */
    private MeshPrimitive createMeshPrimitive(ReadableObj part, String partName)
    {
        MeshPrimitive meshPrimitive = 
            new MeshPrimitive();
        meshPrimitive.setMode(GltfConstants.GL_TRIANGLES);

        // Add the indices data from the OBJ to the buffer structure
        String indicesAccessorId = partName + "_indices"; 
        bufferStructureBuilder.createAccessorModel(
            indicesAccessorId, indicesComponentType, "SCALAR", 
            createIndicesByteBuffer(part, indicesComponentType));
        meshPrimitive.setIndices(indicesAccessorId);
        
        bufferStructureBuilder.createArrayElementBufferViewModel(
            partName + "_indices_bufferView");
        
        // Add the vertices (positions) from the OBJ to the buffer structure
        String positionsAccessorId = partName + "_positions";
        FloatBuffer objVertices = ObjData.getVertices(part);
        bufferStructureBuilder.createAccessorModel(
            positionsAccessorId, GltfConstants.GL_FLOAT, "VEC3", 
            Buffers.createByteBufferFrom(objVertices));
        meshPrimitive.addAttributes("POSITION", positionsAccessorId);

        // Add the texture coordinates from the OBJ to the buffer structure
        boolean flipY = true;
        FloatBuffer objTexCoords = ObjData.getTexCoords(part, 2, flipY);
        if (objTexCoords.capacity() > 0)
        {
            String texCoordsAccessorId = partName + "_texcoords0";
            bufferStructureBuilder.createAccessorModel(
                texCoordsAccessorId, GltfConstants.GL_FLOAT, "VEC2", 
                Buffers.createByteBufferFrom(objTexCoords));
            meshPrimitive.addAttributes("TEXCOORD_0", texCoordsAccessorId);
        }
        
        // Add the normals from the OBJ to the buffer structure
        FloatBuffer objNormals = ObjData.getNormals(part);
        if (objNormals.capacity() > 0)
        {
            ObjNormals.normalize(objNormals);
            String normalsAccessorId = partName + "_normals";
            bufferStructureBuilder.createAccessorModel(
                normalsAccessorId, GltfConstants.GL_FLOAT, "VEC3", 
                Buffers.createByteBufferFrom(objNormals));
            meshPrimitive.addAttributes("NORMAL", normalsAccessorId);
        }

        bufferStructureBuilder.createArrayBufferViewModel(
            partName + "_attributes_bufferView");
        
        return meshPrimitive;
    }
    
    /**
     * Create a byte buffer containing the data of the indices for the
     * given OBJ. The face vertex indices of the given OBJ will be 
     * extracted (assuming that it contains only triangles), converted
     * to the given indices component type (which is a GL constant like
     * GL_SHORT), and a byte buffer containing these indices will be returned.
     * 
     * @param obj The OBJ
     * @param indicesComponentType The indices component type
     * @return The byte buffer
     */
    private static ByteBuffer createIndicesByteBuffer(
        ReadableObj obj, int indicesComponentType)
    {
        int numVerticesPerFace = 3;
        IntBuffer objIndices = 
            ObjData.getFaceVertexIndices(obj, numVerticesPerFace);
        int indicesComponentSize =
            Accessors.getNumBytesForAccessorComponentType(
                indicesComponentType);
        ByteBuffer indicesByteBuffer = 
            IntBuffers.convertToByteBuffer(objIndices, indicesComponentSize);
        return indicesByteBuffer;
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

