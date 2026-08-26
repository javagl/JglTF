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

/**
 * The configuration for a {@link BasicAssetCreator}.<br>
 * <br>
 * This is only a "parameter object": All its properties are public, and it may
 * be used for serialization/deserialization internally.
 */
public class Config
{
    /**
     * The total number of mesh primitives to create.<br>
     * <br>
     * These mesh primitives can be considered as "templates" for the ones that
     * are put into the actual meshes. These mesh primitive will determine the
     * number of accessors that are created. And each mesh will contain NEW mesh
     * primitives that are created from these templates, potentially sharing
     * accessors.
     */
    public int numMeshPrimitives;

    /**
     * The number of points that should be contained in the mesh primitives.<br>
     * <br>
     * This is an array of Mx2 elements. The mesh primitive with index m will
     * have <code>pointSizes[m][0]</code> vertices in x-direction and
     * <code>pointSizes[m][1]</code> vertices in y-direction, where t will be
     * wrapped to be in the range [0,M).
     */
    public int pointSizes[][];

    /**
     * The total number of textures to create.
     */
    public int numTextures;

    /**
     * The total number of pixels that each texture image should have.<br>
     * <br>
     * This is an array of Tx2 elements. The texture with index t will have
     * <code>pixelSizes[t][0]</code> pixels in x-direction and
     * <code>pixelSizes[t][1]</code> pixels in y-direction, where t will be
     * wrapped to be in the range [0,T).
     */
    public int pixelSizes[][];

    /**
     * The total number of materials to create.<br>
     * <br>
     * When a mesh is created, and all its mesh primitives are created, then
     * each mesh primitive will receive a material. The index for accessing
     * the material will be counted up, e.g. the p'th mesh primitive that
     * is created will receive <code>material[p]</code>, wrapping p to the
     * range [0, numMaterials). 
     */
    public int numMaterials;

    /**
     * The total number of meshes to create.<br>
     * <br>
     * Node n will contain the mesh <code>meshes[n]</code>, with n being wrapped
     * to be in [0, numMeshes).
     */
    public int numMeshes;

    /**
     * The number of mesh primitives per mesh.<br>
     * <br>
     * This will usually be 1, to avoid drawing multiple primitives at the same
     * place.
     */
    public int numMeshPrimitivesPerMesh;

    /**
     * The total number of nodes to create.
     */
    public int numNodes;

    /**
     * The number of dimensions of the grid in which the nodes should be
     * arranged. Must be in [1,3].
     */
    public int gridDimensions;

    /**
     * Whether the geometry should be noisy (with unspecified perlin noise)
     */
    public boolean noiseGeometry;

    /**
     * Whether the textures should be noisy (with unspecified perlin noise)
     */
    public boolean noiseTextures;

}
