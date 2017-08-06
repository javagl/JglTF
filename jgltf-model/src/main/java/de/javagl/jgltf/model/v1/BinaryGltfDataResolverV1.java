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
package de.javagl.jgltf.model.v1;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.impl.v1.Shader;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A class to resolve the data of {@link Image}- and {@link Shader} objects
 * in a glTF that use the binary glTF extension. This class will read the
 * extension information, extract the parts of the binary glTF buffer that
 * contain the image- and shader data, and put these data blocks into
 * the corresponding {@link ImageModel} and {@link ShaderModel} objects.
 */
class BinaryGltfDataResolverV1
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BinaryGltfDataResolverV1.class.getName());
    
    /**
     * The {@link GltfModelV1}
     */
    private final GltfModelV1 gltfModel;

    /**
     * Creates a new resolver for the given {@link GltfModelV1}
     * 
     * @param gltfModel The {@link GltfModelV1}
     */
    BinaryGltfDataResolverV1(GltfModelV1 gltfModel)
    {
        this.gltfModel = Objects.requireNonNull(
            gltfModel, "The gltfModel may not be null");
    }
    
    /**
     * Resolve the image- and shader data objects that contain the 
     * binary glTF extension.
     */
    void resolve()
    {
        resolveBinaryImageDatas();
        resolveBinaryShaderDatas();
    }
    
    
    /**
     * For each {@link Image} in the current glTF that has the binary glTF
     * extension, resolve the corresponding buffer view that is referred
     * to in the extension, and set the corresponding data as the
     * {@link ImageModel#setImageData(ByteBuffer) ImageModel image data}
     */
    private void resolveBinaryImageDatas() 
    {
        GlTF gltf = gltfModel.getGltf();
        Optionals.of(gltf.getImages()).forEach(this::resolveBinaryImageData);
    }
    
    /**
     * If the given {@link Image} has the binary glTF extension, resolve the 
     * corresponding buffer view that is referred to in the extension, and 
     * set the corresponding data as the 
     * {@link ImageModel#setImageData(ByteBuffer) ImageModel image data}
     * 
     * @param id The {@link Image} ID
     * @param image The {@link Image}
     */
    private void resolveBinaryImageData(String id, Image image)
    {
        if (!BinaryGltfV1.hasBinaryGltfExtension(image))
        {
            return;
        }
        String bufferViewId = 
            BinaryGltfV1.getBinaryGltfBufferViewId(image);
        ByteBuffer bufferViewData = 
            getBufferViewData(gltfModel, bufferViewId);
        if (bufferViewData == null)
        {
            logger.warning("Could not resolve the data for image " + id);
        }
        ImageModel imageModel = gltfModel.getImageModelById(id);
        imageModel.setImageData(bufferViewData);
    }
    
    /**
     * For each {@link Shader} in the current glTF that has the binary glTF
     * extension, resolve the corresponding buffer view that is referred
     * to in the extension, and set the corresponding data as the
     * {@link ShaderModel#setShaderData(ByteBuffer) ShaderModel shader data}
     */
    private void resolveBinaryShaderDatas() 
    {
        GlTF gltf = gltfModel.getGltf();
        Optionals.of(gltf.getShaders()).forEach(this::resolveBinaryShaderData);
    }
    
    /**
     * If the given {@link Shader} has the binary glTF extension, resolve the 
     * corresponding buffer view that is referred to in the extension, and 
     * set the corresponding data as the 
     * {@link ShaderModel#setShaderData(ByteBuffer) ShaderModel shader data}
     * 
     * @param id The {@link Shader} ID
     * @param shader The {@link Shader}
     */
    private void resolveBinaryShaderData(String id, Shader shader)
    {
        if (!BinaryGltfV1.hasBinaryGltfExtension(shader))
        {
            return;
        }
        String bufferViewId = 
            BinaryGltfV1.getBinaryGltfBufferViewId(shader);
        ByteBuffer bufferViewData = 
            getBufferViewData(gltfModel, bufferViewId);
        if (bufferViewData == null)
        {
            logger.warning("Could not resolve the data for shader " + id);
        }
        ShaderModel shaderModel = gltfModel.getShaderModelById(id);
        shaderModel.setShaderData(bufferViewData);
    }

    
    /**
     * Returns a byte buffer for the specified buffer view. The buffer view
     * refers to a buffer. There exists a byte buffer for this buffer in the
     * given {@link GltfModelV1}. This method returns a view on the part of 
     * this byte buffer - namely, the part that the buffer view refers to.<br>
     * <br>
     * If the buffer view refers to an invalid buffer, or the
     * {@link BufferView#getByteOffset() byte offset} or 
     * {@link BufferView#getByteLength() byte length} in the 
     * {@link BufferView} are not valid for the given byte buffer,
     * (or for short: when anything is wrong), then a warning is 
     * printed and <code>null</code> is returned. 
     * 
     * @param gltfModel The {@link GltfModelV1}
     * @param bufferViewId The {@link BufferView} ID
     * @return The byte buffer  
     */
    private static ByteBuffer getBufferViewData(
        GltfModelV1 gltfModel, String bufferViewId)
    {
        GlTF gltf = gltfModel.getGltf();
        Map<String, BufferView> bufferViews = gltf.getBufferViews();
        if (bufferViews == null)
        {
            logger.warning("No bufferViews found in glTF");
            return null;
        }
        BufferView bufferView = bufferViews.get(bufferViewId);
        if (bufferView == null)
        {
            logger.warning("Could not find bufferView with ID " + bufferViewId);
            return null;
        }
        String bufferId = bufferView.getBuffer();
        BufferModel bufferModel = gltfModel.getBufferModelById(bufferId);
        if (bufferModel == null)
        {
            logger.warning("Could not find bufferModel with ID " + bufferId);
            return null;
        }
        Integer byteOffset = bufferView.getByteOffset();
        if (byteOffset < 0)
        {
            logger.warning("The byteOffset in bufferView " + bufferViewId
                + " is negative: " + byteOffset);
            return null;
        }
        Integer byteLength = bufferView.getByteLength();
        if (byteLength == null)
        {
            // This was actually allowed in glTF 1.0, but does not make sense
            logger.warning("The byteLength in bufferView " + bufferViewId
                + " is null");
            return null;
        }
        if (byteLength < 0)
        {
            logger.warning("The byteLength in bufferView " + bufferViewId
                + " is negative: " + byteLength);
            return null;
        }
        ByteBuffer bufferData = bufferModel.getBufferData();
        if (bufferData == null)
        {
            logger.warning("The bufferData for buffer " + bufferId
                + " has not been set");
            return null;
        }
        if (byteOffset + byteLength > bufferData.capacity())
        {
            logger.warning("In bufferView " + bufferView
                + ", the byteOffset is " + byteOffset
                + " and the byteLength is " + byteLength + ", "
                + " but the buffer capacity is only " + bufferData.capacity());
            return null;
        }
        ByteBuffer bufferViewData = Buffers.createSlice(
            bufferData, byteOffset, byteLength);
        return bufferViewData;
    }
    
}
