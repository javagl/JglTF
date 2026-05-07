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
package de.javagl.jgltf.model.khr.draco_mesh_compression;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.openize.drako.Draco;
import com.openize.drako.DracoMesh;
import com.openize.drako.DrakoException;
import com.openize.drako.PointAttribute;

import de.javagl.jgltf.impl.v2.khr.draco_mesh_compression.MeshPrimitiveDracoMeshCompression;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfException;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Internal utility methods for draco decoding
 */
class DracoDecoding
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(DracoDecoding.class.getName());

    /**
     * The default log level
     */
    private static final Level level = Level.INFO;

    /**
     * Decode the draco extension data of the given mesh primitive model
     * 
     * @param gltfModel The glTF model
     * @param meshPrimitiveModel The mesh primitive model
     * @param impl The implementation object
     */
    public static void decode(GltfModel gltfModel,
        DefaultMeshPrimitiveModel meshPrimitiveModel,
        MeshPrimitiveDracoMeshCompression impl)
    {
        try
        {
            decodeInternal(gltfModel, meshPrimitiveModel, impl);
        }
        catch (GltfException e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Internal method of {@link #decode}
     * 
     * @param gltfModel The glTF model
     * @param meshPrimitiveModel The mesh primitive model
     * @param impl The implementation object
     * @throws GltfException If the data could not be decoded
     */
    private static void decodeInternal(GltfModel gltfModel,
        DefaultMeshPrimitiveModel meshPrimitiveModel,
        MeshPrimitiveDracoMeshCompression impl)
    {
        // Read the draco mesh from the buffer view
        int bufferViewIndex = impl.getBufferView();
        DracoMesh dracoMesh = readDracoMesh(gltfModel, bufferViewIndex);

        // Fill the indices accessor with the indices from the draco mesh
        AccessorModel indicesAccessorModel = meshPrimitiveModel.getIndices();
        int dracoIndices[] = dracoMesh.getIndices().toArray();
        AccessorDataBuffers.fillFromInt(indicesAccessorModel,
            IntBuffer.wrap(dracoIndices));

        // Process all draco attributes. The specification requires them
        // to be a subset of the actual attributes.
        Map<String, Integer> dracoAttributes = impl.getAttributes();
        Map<String, AccessorModel> attributes =
            meshPrimitiveModel.getAttributes();
        for (Entry<String, Integer> entry : dracoAttributes.entrySet())
        {
            String attributeName = entry.getKey();
            logger.log(level, "Decoding draco attribute " + attributeName);

            // Note that the value here is an ID for the decoded draco
            // attribute, and not an accessor index or so...
            int attributeId = entry.getValue();

            PointAttribute pointAttribute =
                obtainAttribute(dracoMesh, attributeName, attributeId);

            logger.log(level, "  attribute " + attributeName);
            logger.log(level, "  attributeId " + attributeId);
            logger.log(level, "  pointAttribute " + pointAttribute);

            // Fill the accessor with the data from the point attribute
            AccessorModel accessorModel = attributes.get(attributeName);
            fill(accessorModel, pointAttribute);

            logger.log(level,
                "Decoding draco attribute " + attributeName + " DONE");
        }
    }

    /**
     * Read the Draco mesh from the specified buffer view data
     * 
     * @param gltfModel The glTF model
     * @param bufferViewIndex The buffer view index
     * @return The draco mesh
     * @throws GltfException If the mesh could not be decoded
     */
    private static DracoMesh readDracoMesh(GltfModel gltfModel,
        int bufferViewIndex)
    {
        logger.log(level,
            "Reading draco mesh from buffer view " + bufferViewIndex);
        List<BufferViewModel> bufferViewModels =
            gltfModel.getBufferViewModels();
        BufferViewModel bufferViewModel = bufferViewModels.get(bufferViewIndex);
        ByteBuffer bufferViewData = bufferViewModel.getBufferViewData();
        byte bufferViewDataArray[] = new byte[bufferViewData.remaining()];
        bufferViewData.slice().get(bufferViewDataArray);
        DracoMesh dracoMesh = null;
        try
        {
            dracoMesh = (DracoMesh) Draco.decode(bufferViewDataArray);
        }
        catch (DrakoException e)
        {
            throw new GltfException("Could not decode draco mesh", e);
        }
        logger.log(level,
            "Reading draco mesh from buffer view " + bufferViewIndex + " DONE");
        return dracoMesh;
    }

    /**
     * Obtains the point attribute with the given ID from the given draco mesh.
     * 
     * @param dracoMesh The draco mesh
     * @param gltfAttribute The glTF attribute name. Only used for potential
     *        error messages.
     * @param id The unique ID of the attribute. This is the value that was
     *        stored as the value in the
     *        {@link MeshPrimitiveDracoMeshCompression#getAttributes()}
     * @return The point attribute
     * @throws GltfException If the attribute is not found
     */
    private static PointAttribute obtainAttribute(DracoMesh dracoMesh,
        String gltfAttribute, int id)
    {
        for (int i = 0; i < dracoMesh.getNumAttributes(); i++)
        {
            PointAttribute attribute = dracoMesh.attribute(i);
            if (attribute.getUniqueId() == id)
            {
                return attribute;
            }
        }
        throw new GltfException("Could not obtain attribute " + gltfAttribute
            + " with unique ID " + id + " from draco mesh");
    }

    /**
     * Fill the given accessor model with the data that is read from the given
     * point attribute
     * 
     * @param accessorModel The accessor model
     * @param pointAttribute The point attribute
     * @throws GltfException If the types are incompatible
     */
    private static void fill(AccessorModel accessorModel,
        PointAttribute pointAttribute)
    {
        Class<?> componentType = accessorModel.getComponentDataType();
        int componentCount = accessorModel.getElementType().getNumComponents();
        int count = accessorModel.getCount();
        if (componentType == byte.class)
        {
            ByteBuffer attributeData =
                readByteDracoAttribute(pointAttribute, count, componentCount);
            AccessorDataBuffers.fill(accessorModel, attributeData);
        }
        else if (componentType == short.class)
        {
            ShortBuffer attributeData =
                readShortDracoAttribute(pointAttribute, count, componentCount);
            AccessorDataBuffers.fill(accessorModel, attributeData);
        }
        else if (componentType == float.class)
        {
            FloatBuffer attributeData =
                readFloatDracoAttribute(pointAttribute, count, componentCount);
            AccessorDataBuffers.fill(accessorModel, attributeData);
        }
        else
        {
            throw new GltfException(
                "Expected component type to be byte, short, or float, but was "
                    + componentType);
        }

    }

    /**
     * Read the data from the given point attribute, as <code>byte</code> values
     * 
     * @param pointAttribute The draco point attribute
     * @param count The accessor count
     * @param componentCount The component count
     * @return The resulting data, as a byte buffer
     */
    private static ByteBuffer readByteDracoAttribute(
        PointAttribute pointAttribute, int count, int componentCount)
    {
        byte p[] = new byte[componentCount];
        ByteBuffer attributeData =
            Buffers.create(count * componentCount * Byte.BYTES);
        for (int i = 0; i < count; i++)
        {
            int j = pointAttribute.mappedIndex(i);
            pointAttribute.getValue(j, p);
            for (int c = 0; c < componentCount; c++)
            {
                attributeData.put(i * componentCount + c, p[c]);
            }
        }
        return attributeData;
    }

    /**
     * Read the data from the given point attribute, as <code>short</code>
     * values
     * 
     * @param pointAttribute The draco point attribute
     * @param count The accessor count
     * @param componentCount The component count
     * @return The resulting data, as a short buffer
     */
    private static ShortBuffer readShortDracoAttribute(
        PointAttribute pointAttribute, int count, int componentCount)
    {
        short p[] = new short[componentCount];
        ByteBuffer attributeData =
            Buffers.create(count * componentCount * Short.BYTES);
        ShortBuffer attributeDataShort = attributeData.asShortBuffer();
        for (int i = 0; i < count; i++)
        {
            int j = pointAttribute.mappedIndex(i);
            pointAttribute.getValue(j, p);
            for (int c = 0; c < componentCount; c++)
            {
                attributeDataShort.put(i * componentCount + c, p[c]);
            }
        }
        return attributeDataShort;
    }

    /**
     * Read the data from the given point attribute, as <code>float</code>
     * values
     * 
     * @param pointAttribute The draco point attribute
     * @param count The accessor count
     * @param componentCount The component count
     * @return The resulting data, as a float buffer
     */
    private static FloatBuffer readFloatDracoAttribute(
        PointAttribute pointAttribute, int count, int componentCount)
    {
        float p[] = new float[componentCount];
        ByteBuffer attributeData =
            Buffers.create(count * componentCount * Float.BYTES);
        FloatBuffer attributeDataFloat = attributeData.asFloatBuffer();
        for (int i = 0; i < count; i++)
        {
            int j = pointAttribute.mappedIndex(i);
            pointAttribute.getValue(j, p);
            int offset0 = i * componentCount;
            for (int c = 0; c < componentCount; c++)
            {
                attributeDataFloat.put(offset0 + c, p[c]);
            }
        }
        return attributeDataFloat;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private DracoDecoding()
    {
        // Private constructor to prevent instantiation
    }

}
