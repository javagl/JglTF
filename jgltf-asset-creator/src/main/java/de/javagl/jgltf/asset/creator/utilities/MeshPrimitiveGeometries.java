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
package de.javagl.jgltf.asset.creator.utilities;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.javagl.geogen.Geometries;
import de.javagl.geogen.Geometry;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.creation.AccessorModels;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;

/**
 * Methods to create {@link DefaultMeshPrimitiveModel} objects from Geometry
 * objects.
 */
public class MeshPrimitiveGeometries
{
    /**
     * Creates a {@link DefaultMeshPrimitiveModel} from the given Geometry, 
     * only using the indices, positions, and normals (if present)
     *
     * @param geometry The geometry
     * @return The {@link DefaultMeshPrimitiveModel}
     */
    public static DefaultMeshPrimitiveModel createPlain(Geometry geometry)
    {
        Geometry g = Geometries.create(
            geometry.getIndices(), 
            geometry.getPositions3D(),
            geometry.getNormals3D(), 
            null, null, null);
        return createFull(g);
    }

    /**
     * Creates a {@link DefaultMeshPrimitiveModel} from the given Geometry, 
     * only using the indices, positions, and normals, and texture 
     * coordinates (if present)
     *
     * @param geometry The geometry
     * @return The {@link DefaultMeshPrimitiveModel}
     */
    public static DefaultMeshPrimitiveModel createTextured(Geometry geometry)
    {
        Geometry g = Geometries.create(
            geometry.getIndices(), 
            geometry.getPositions3D(),
            geometry.getNormals3D(), 
            geometry.getTexCoords2D(), 
            null, null);
        return createFull(g);
    }
    
    /**
     * Creates a new {@link DefaultMeshPrimitiveModel} with the indices and 
     * attributes that are present in the given {@link Geometry}
     * 
     * @param g The {@link Geometry}
     * @return The {@link MeshPrimitiveModel}
     */
    public static DefaultMeshPrimitiveModel createFull(Geometry g)
    {
        DefaultMeshPrimitiveModel meshPrimitive;
        IntBuffer indices = g.getIndices();
        if (indices == null)
        {
            meshPrimitive = new DefaultMeshPrimitiveModel(
                GltfConstants.GL_POINTS);
        }
        else
        {
            meshPrimitive = new DefaultMeshPrimitiveModel(
                GltfConstants.GL_TRIANGLES);
            meshPrimitive.setIndices(
                AccessorModels.createUnsignedShortScalar(indices));
        }
        FloatBuffer positions = g.getPositions3D();
        FloatBuffer normals = g.getNormals3D();
        FloatBuffer texCoords = g.getTexCoords2D();
        FloatBuffer colors = g.getColors4D();
        FloatBuffer tangents = g.getTangents4D();

        meshPrimitive.putAttribute("POSITION", 
            AccessorModels.createFloat3D(positions));

        if (normals != null)
        {
            meshPrimitive.putAttribute("NORMAL", 
                AccessorModels.createFloat3D(normals));
        }
        if (texCoords != null)
        {
            meshPrimitive.putAttribute("TEXCOORD_0", 
                AccessorModels.createFloat2D(texCoords));
        }
        if (colors != null)
        {
            meshPrimitive.putAttribute("COLOR", 
                AccessorModels.createFloat4D(colors));
        }
        if (tangents != null)
        {
            meshPrimitive.putAttribute("TANGENT", 
                AccessorModels.createFloat4D(tangents));
        }
        return meshPrimitive;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private MeshPrimitiveGeometries()
    {
        // Private constructor to prevent instantiation
    }

    
}
