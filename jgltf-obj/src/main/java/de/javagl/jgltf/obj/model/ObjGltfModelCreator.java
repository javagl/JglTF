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
package de.javagl.jgltf.obj.model;

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

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.creation.MeshPrimitiveBuilder;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.io.IO;
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
 * A class for creating {@link GltfModel} objects from OBJ files 
 */
public class ObjGltfModelCreator
{
    
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ObjGltfModelCreator.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.INFO;
    
    /**
     * The {@link MtlMaterialHandler}
     */
    private MtlMaterialHandler mtlMaterialHandler;
    
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
     * Whether technique-based materials should be used
     */
    private boolean techniqueBasedMaterials = false;
    
    /**
     * Default constructor
     */
    public ObjGltfModelCreator()
    {
        setIndicesComponentType(GltfConstants.GL_UNSIGNED_SHORT);
    }
    
    /**
     * Set whether technique-based (glTF 1.0) materials should be created
     * 
     * @param techniqueBasedMaterials Whether technique-based materials should
     * be created
     */
    public void setTechniqueBasedMaterials(boolean techniqueBasedMaterials)
    {
        this.techniqueBasedMaterials = techniqueBasedMaterials;
    }
    
    /**
     * Set the component type for the indices of the {@link MeshPrimitiveModel}
     * objects
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
     * Create a {@link GltfModel} from the OBJ file with the given URI
     * 
     * @param objUri The OBJ URI
     * @return The {@link GltfModel}
     * @throws IOException If an IO error occurs
     */
    public GltfModel create(URI objUri) throws IOException
    {
        logger.log(level, "Creating glTF");
        
        // Obtain the relative path information and file names
        logger.log(level, "Resolving paths from " + objUri);
        URI baseUri = IO.getParent(objUri);
        String objFileName = IO.extractFileName(objUri);
        String baseName = stripFileNameExtension(objFileName);
        String mtlFileName = baseName + ".mtl";
        URI mtlUri = IO.makeAbsolute(baseUri, mtlFileName);

        if (techniqueBasedMaterials)
        {
            mtlMaterialHandler = new MtlMaterialHandlerV1(baseUri.toString());
        }
        else
        {
            mtlMaterialHandler = new MtlMaterialHandlerV2(baseUri.toString());
        }
        
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
     * Convert the given OBJ into a {@link GltfModel}.
     *   
     * @param obj The OBJ
     * @param mtlsMap The mapping from material names to MTL instances
     * @param baseName The base name for the glTF
     * @param baseUri The base URI to resolve external data against
     * @return The {@link GltfModel}
     */
    GltfModel convert(
        ReadableObj obj, Map<String, Mtl> mtlsMap, String baseName, URI baseUri)
    {
        // Create the MeshPrimitives from the OBJ and MTL data
        Map<String, Mtl> mtls = Optionals.of(mtlsMap);
        List<DefaultMeshPrimitiveModel> meshPrimitives = 
            createMeshPrimitives(obj, mtls);
        
        DefaultMeshModel mesh = new DefaultMeshModel();
        for (MeshPrimitiveModel meshPrimitive : meshPrimitives)
        {
            mesh.addMeshPrimitiveModel(meshPrimitive);
        }
        
        DefaultNodeModel node = new DefaultNodeModel();
        node.addMeshModel(mesh);

        DefaultSceneModel scene = new DefaultSceneModel();
        scene.addNode(node);
        
        GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        gltfModelBuilder.addSceneModel(scene);
        
        if (techniqueBasedMaterials)
        {
            return gltfModelBuilder.buildV1();
        }
        return gltfModelBuilder.build();
    }
    
    
    /**
     * Create the {@link MeshPrimitiveModel} objects for the given OBJ- and 
     * MTL data
     * 
     * @param obj The OBJ
     * @param mtls The MTLs
     * @return The {@link MeshPrimitiveModel} objects
     */
    private List<DefaultMeshPrimitiveModel> createMeshPrimitives(
        ReadableObj obj, Map<String, Mtl> mtls)
    {
        // When there are no materials, create the MeshPrimitives for the OBJ
        int numMaterialGroups = obj.getNumMaterialGroups();
        if (numMaterialGroups == 0 || mtls.isEmpty())
        {
            return createMeshPrimitives(obj);
        }
        
        // Create the MeshPrimitives for the material groups
        List<DefaultMeshPrimitiveModel> meshPrimitives = 
            new ArrayList<DefaultMeshPrimitiveModel>();
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
            List<DefaultMeshPrimitiveModel> subMeshPrimitives = 
                createPartMeshPrimitives(materialObj);
            
            assignMaterial(subMeshPrimitives, obj, mtl);
            meshPrimitives.addAll(subMeshPrimitives);
        }
        return meshPrimitives;
    }

    
    
    /**
     * Create simple {@link MeshPrimitiveModel} objects for the given OBJ
     * 
     * @param obj The OBJ
     * @return The {@link MeshPrimitiveModel} objects
     */
    private List<DefaultMeshPrimitiveModel> createMeshPrimitives(
        ReadableObj obj)
    {
        logger.log(level, "Creating MeshPrimitives for OBJ");
        
        List<DefaultMeshPrimitiveModel> meshPrimitives = 
            createPartMeshPrimitives(obj);
        
        boolean withNormals = obj.getNumNormals() > 0;
        if (assigningRandomColorsToParts)
        {
            assignRandomColorMaterials(meshPrimitives, withNormals);
        }
        else
        {
            assignDefaultMaterial(meshPrimitives, withNormals);
        }
        return meshPrimitives;
    }

    
    /**
     * Create a {@link MaterialModel} for the given OBJ and MTL, and assign it
     * to all the given {@link MeshPrimitiveModel} instances
     * 
     * @param meshPrimitives The {@link MeshPrimitiveModel} instances
     * @param obj The OBJ
     * @param mtl The MTL
     */
    private void assignMaterial(
        Iterable<? extends DefaultMeshPrimitiveModel> meshPrimitives, 
        ReadableObj obj, Mtl mtl)
    {
        MaterialModel material = mtlMaterialHandler.createMaterial(obj, mtl);
        for (DefaultMeshPrimitiveModel meshPrimitive : meshPrimitives)
        {
            meshPrimitive.setMaterialModel(material);
        }
    }
    
    /**
     * Create a default {@link MaterialModel}, and assign it to all the given 
     * {@link MeshPrimitiveModel} instances
     * 
     * @param meshPrimitives The {@link MeshPrimitiveModel} instances
     * @param withNormals Whether the {@link MeshPrimitiveModel} instances have
     * normal information
     */
    private void assignDefaultMaterial(
        Iterable<? extends DefaultMeshPrimitiveModel> meshPrimitives, 
        boolean withNormals)
    {
        MaterialModel material = mtlMaterialHandler.createMaterialWithColor(
            withNormals, 0.75f, 0.75f, 0.75f);
        for (DefaultMeshPrimitiveModel meshPrimitive : meshPrimitives)
        {
            meshPrimitive.setMaterialModel(material);
        }
    }

    /**
     * Create {@link MaterialModel} instances with random colors, and assign 
     * them to the given {@link MeshPrimitiveModel} instances
     * 
     * @param meshPrimitives The {@link MeshPrimitiveModel} instances
     * @param withNormals Whether the {@link MeshPrimitiveModel} instances have
     * normal information
     */
    private void assignRandomColorMaterials(
        Iterable<? extends DefaultMeshPrimitiveModel> meshPrimitives, 
        boolean withNormals)
    {
        Random random = new Random(0);
        for (DefaultMeshPrimitiveModel meshPrimitive : meshPrimitives)
        {
            float r = random.nextFloat(); 
            float g = random.nextFloat(); 
            float b = random.nextFloat(); 
            MaterialModel material = 
                mtlMaterialHandler.createMaterialWithColor(
                    withNormals, r, g, b);
            meshPrimitive.setMaterialModel(material);
        }
    }
    
    


    /**
     * Create the {@link MeshPrimitiveModel} objects from the given OBJ data.
     * 
     * @param obj The OBJ
     * @return The {@link MeshPrimitiveModel} list
     */
    private List<DefaultMeshPrimitiveModel> createPartMeshPrimitives(
        ReadableObj obj)
    {
        List<? extends ReadableObj> parts = objSplitter.apply(obj);
        
        MeshPrimitiveBuilder meshPrimitiveBuilder = 
            MeshPrimitiveBuilder.create();
        
        List<DefaultMeshPrimitiveModel> meshPrimitives = 
            new ArrayList<DefaultMeshPrimitiveModel>();
        for (int i = 0; i < parts.size(); i++)
        {
            ReadableObj part = parts.get(i);
            DefaultMeshPrimitiveModel meshPrimitive =
                createMeshPrimitive(meshPrimitiveBuilder, part);
            meshPrimitives.add(meshPrimitive);
        }
        return meshPrimitives;
    }
    
    /**
     * Create the {@link MeshPrimitiveModel} from the given OBJ data.
     * 
     * @param meshPrimitiveBuilder The {@link MeshPrimitiveBuilder}
     * @param part The OBJ
     * @return The {@link MeshPrimitiveModel}
     */
    private DefaultMeshPrimitiveModel createMeshPrimitive(
        MeshPrimitiveBuilder meshPrimitiveBuilder, ReadableObj part)
    {
        meshPrimitiveBuilder.setTriangles();

        // Set the indices 
        int numVerticesPerFace = 3;
        IntBuffer objIndices = 
            ObjData.getFaceVertexIndices(part, numVerticesPerFace);
        meshPrimitiveBuilder.setIndicesAs(objIndices, indicesComponentType);
        
        // Add the vertices (positions) from the OBJ
        FloatBuffer objVertices = ObjData.getVertices(part);
        meshPrimitiveBuilder.addPositions3D(objVertices);

        // Add the texture coordinates from the OBJ
        boolean flipY = true;
        FloatBuffer objTexCoords = ObjData.getTexCoords(part, 2, flipY);
        if (objTexCoords.capacity() > 0)
        {
            meshPrimitiveBuilder.addTexCoords02D(objTexCoords);
        }
        
        // Add the normals from the OBJ
        FloatBuffer objNormals = ObjData.getNormals(part);
        if (objNormals.capacity() > 0)
        {
            ObjNormals.normalize(objNormals);
            meshPrimitiveBuilder.addNormals3D(objNormals);
        }

        return meshPrimitiveBuilder.build();
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

