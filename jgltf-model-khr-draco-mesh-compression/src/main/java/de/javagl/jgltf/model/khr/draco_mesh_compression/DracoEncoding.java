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
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.openize.drako.AttributeType;
import com.openize.drako.DataBuffer;
import com.openize.drako.DataType;
import com.openize.drako.Draco;
import com.openize.drako.DracoCompressionLevel;
import com.openize.drako.DracoEncodeOptions;
import com.openize.drako.DracoMesh;
import com.openize.drako.DrakoException;
import com.openize.drako.PointAttribute;

import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;

/**
 * Utility methods for draco encoding
 */
class DracoEncoding
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
     * Internal class for the encoded draco data of one mesh primitive
     */
    static class Result
    {
        /**
         * The encoded draco data
         */
        byte dracoData[];

        /**
         * The draco attribute IDs
         */
        final Map<String, Integer> dracoAttributeIds =
            new LinkedHashMap<String, Integer>();
    }

    /**
     * Encode the data from the given mesh primitive using draco.
     * 
     * @param gltfModel The glTF model
     * @param meshPrimitiveModel The mesh primitive model
     * @param model The draco mesh compression model
     * @return The result
     */
    static Result encode(GltfModel gltfModel,
        MeshPrimitiveModel meshPrimitiveModel, DracoMeshCompressionModel model)
    {
        Result result = new Result();

        DracoMesh mesh = new DracoMesh();
        AccessorModel indicesAccessorModel = meshPrimitiveModel.getIndices();
        int indices[] = toIntArray(indicesAccessorModel);
        mesh.getIndices().addRange(indices);

        Set<String> encodedAttributes = model.getAttributes();
        Map<String, AccessorModel> attributes =
            meshPrimitiveModel.getAttributes();
        for (String attributeName : encodedAttributes)
        {
            DefaultAccessorModel accessorModel =
                (DefaultAccessorModel) attributes.get(attributeName);
            if (accessorModel == null)
            {
                logger.warning("Could not find attribute " + attributeName
                    + " in mesh primitive - skipping");
                continue;
            }
            int attributeType = determineAttributeType(attributeName);
            if (attributeType == AttributeType.INVALID)
            {
                continue;
            }
            PointAttribute pointAttribute =
                toAttribute(attributeType, accessorModel);
            mesh.addAttribute(pointAttribute);
            int attributeId = mesh.getNamedAttributeId(attributeType);
            result.dracoAttributeIds.put(attributeName, attributeId);
        }

        AccessorModel positionAccessorModel = attributes.get("POSITION");
        mesh.setNumPoints(positionAccessorModel.getCount());

        DracoOptions dracoOptions = model.getDracoOptions();
        DracoEncodeOptions dracoEncodeOptions = translate(dracoOptions);
        try
        {
            result.dracoData = Draco.encode(mesh, dracoEncodeOptions);
            logger.log(level,
                "Draco encoding created " + result.dracoData.length + " bytes");
        }
        catch (DrakoException e)
        {
            logger.log(Level.SEVERE, "Could not encode draco mesh", e);
            return null;
        }
        return result;
    }

    /**
     * Translate the given {@link DracoOptions} into 'Drako' options
     * 
     * @param dracoOptions The {@link DracoOptions}
     * @return The drako options
     */
    private static DracoEncodeOptions translate(DracoOptions dracoOptions)
    {
        DracoEncodeOptions dracoEncodeOptions = new DracoEncodeOptions();
        dracoEncodeOptions.setPositionBits(dracoOptions.getPositionBits());
        dracoEncodeOptions.setNormalBits(dracoOptions.getNormalBits());
        dracoEncodeOptions
            .setTextureCoordinateBits(dracoOptions.getTextureCoordinateBits());
        dracoEncodeOptions.setColorBits(dracoOptions.getColorBits());
        switch (dracoOptions.getCompressionLevel())
        {
            case FAST:
                dracoEncodeOptions
                    .setCompressionLevel(DracoCompressionLevel.FAST);
                break;
            case NO_COMPRESSION:
                dracoEncodeOptions
                    .setCompressionLevel(DracoCompressionLevel.NO_COMPRESSION);
                break;
            case OPTIMAL:
                dracoEncodeOptions
                    .setCompressionLevel(DracoCompressionLevel.OPTIMAL);
                break;
            case STANDARD:
                dracoEncodeOptions
                    .setCompressionLevel(DracoCompressionLevel.STANDARD);
                break;
            default:
                break;
        }
        return dracoEncodeOptions;
    }

    /**
     * Determine the 'Drako' AttributeType value for the given attribute name,
     * defaulting to INVALID (-1) for unknown attributes.
     * 
     * @param attributeName The attribute name
     * @return The attribute type
     */
    private static int determineAttributeType(String attributeName)
    {
        if (attributeName == null)
        {
            return AttributeType.INVALID;
        }
        if (attributeName.equals("POSITION"))
        {
            return AttributeType.POSITION;
        }
        if (attributeName.equals("NORMAL"))
        {
            return AttributeType.NORMAL;
        }
        if (attributeName.startsWith("TEXCOORD_"))
        {
            return AttributeType.TEX_COORD;
        }
        if (attributeName.equals("TANGENT"))
        {
            return AttributeType.GENERIC;
        }
        logger.warning("Invalid attribute name for draco: " + attributeName);
        return AttributeType.INVALID;
    }

    /**
     * Create a 'Drako' PointAttribute with the given AttributeType from the
     * given accessor model
     * 
     * @param type The AttributeType value
     * @param accessorModel The accessor model
     * @return The PointAttribute
     */
    private static PointAttribute toAttribute(int type,
        AccessorModel accessorModel)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        AccessorFloatData accessorFloatData = (AccessorFloatData) accessorData;
        ElementType elementType = accessorModel.getElementType();
        int numComponents = elementType.getNumComponents();
        return wrap(type, numComponents, accessorFloatData);
    }

    /**
     * Wrap the given float accessor data into a 'Drako' PointAttribute
     * 
     * @param type The AttributeType
     * @param componentsPerElement The number of components per element
     * @param accessorFloatData The accessor data
     * @return The point attribute
     */
    private static PointAttribute wrap(int type, int componentsPerElement,
        AccessorFloatData accessorFloatData)
    {
        int n = accessorFloatData.getTotalNumComponents();
        byte[] bytes = new byte[n * 4];
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < n; i++)
        {
            float fv = accessorFloatData.get(i);
            bb.putFloat(fv);
        }
        return new PointAttribute(type, DataType.FLOAT32, componentsPerElement,
            false, -1, 0, new DataBuffer(bytes));
    }

    /**
     * Return the data from the given byte, short, or int accessor model as an
     * integer array.
     * 
     * If the given accessor has an invalid type, an error wil be printed and
     * <code>null</code> will be returned.
     * 
     * @param accessorModel The accessor mode
     * @return The array
     */
    private static int[] toIntArray(AccessorModel accessorModel)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        Class<?> componentType = accessorData.getComponentType();
        if (componentType == byte.class)
        {
            AccessorByteData accessorByteData = (AccessorByteData) accessorData;
            return toIntArray(accessorByteData);
        }
        else if (componentType == short.class)
        {
            AccessorShortData accessorShortData =
                (AccessorShortData) accessorData;
            return toIntArray(accessorShortData);
        }
        else if (componentType == int.class)
        {
            AccessorIntData accessorIntData = (AccessorIntData) accessorData;
            return toIntArray(accessorIntData);
        }
        logger.severe("Invalid component type: " + componentType);
        return null;
    }

    /**
     * Return the data from the given accessor data as an integer array
     * 
     * @param accessorByteData The accessor data
     * @return The array
     */
    private static int[] toIntArray(AccessorByteData accessorByteData)
    {
        int count = accessorByteData.getNumElements();
        int indices[] = new int[count];
        for (int i = 0; i < count; i++)
        {
            indices[i] = accessorByteData.getInt(i);
        }
        return indices;
    }

    /**
     * Return the data from the given accessor data as an integer array
     * 
     * @param accessorShortData The accessor data
     * @return The array
     */
    private static int[] toIntArray(AccessorShortData accessorShortData)
    {
        int count = accessorShortData.getNumElements();
        int indices[] = new int[count];
        for (int i = 0; i < count; i++)
        {
            indices[i] = accessorShortData.getInt(i);
        }
        return indices;
    }

    /**
     * Return the data from the given accessor data as an integer array
     * 
     * @param accessorIntData The accessor data
     * @return The array
     */
    private static int[] toIntArray(AccessorIntData accessorIntData)
    {
        int count = accessorIntData.getNumElements();
        int indices[] = new int[count];
        for (int i = 0; i < count; i++)
        {
            indices[i] = accessorIntData.get(i);
        }
        return indices;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private DracoEncoding()
    {
        // Private constructor to prevent instantiation
    }
}
