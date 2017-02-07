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
package de.javagl.jgltf.model.io;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.Maps;

/**
 * Utility methods related to {@link BufferView} buffers in {@link GltfData}
 */
public class BufferViews
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BufferViews.class.getName());
    
    /**
     * Create the {@link BufferView} byte buffers in the given {@link GltfData}.
     * This method assumes that the {@link Buffer} byte buffers are already
     * contained in the given {@link GltfData}. It will create the appropriate
     * slices of the {@link Buffer} byte buffers, so that they may afterwards
     * be obtained via {@link GltfData#getBufferViewData(String)} for
     * each {@link BufferView} ID. If a {@link BufferView} refers to a 
     * {@link Buffer} for which no byte buffer can be found, or it contains
     * invalid {@link BufferView#getByteOffset() byte offset} or 
     * {@link BufferView#getByteLength() byte length} values, then a warning
     * will be printed and the creation of the respective byte buffer will
     * be skipped. 
     *  
     * @param gltfData The {@link GltfData}
     */
    public static void createBufferViewByteBuffers(GltfData gltfData)
    {
        Map<String, BufferView> bufferViews = 
            gltfData.getGltf().getBufferViews();
        Maps.forEachEntry(bufferViews, (bufferViewId, bufferView) ->
            createBufferViewByteBuffer(gltfData, bufferViewId, bufferView));
    }
    
    /**
     * Create the byte buffer slice for the specified {@link BufferView} in
     * the given {@link GltfData}, from the {@link Buffer} byte buffer.
     * If the required byte buffer can not be found, or the {@link BufferView}
     * contains invalid {@link BufferView#getByteOffset() byte offset} or 
     * {@link BufferView#getByteLength() byte length} values, then a warning 
     * will be printed, and the creation of the slice will be skipped.
     * 
     * @param gltfData The {@link GltfData}
     * @param bufferViewId The {@link BufferView} ID
     * @param bufferView The {@link BufferView}
     */
    private static void createBufferViewByteBuffer(
        GltfData gltfData, String bufferViewId, BufferView bufferView)
    {
        String bufferId = bufferView.getBuffer();
        ByteBuffer bufferData = gltfData.getBufferData(bufferId);
        if (bufferData == null)
        {
            logger.warning("Could not find buffer with ID " + bufferId);
            return;
        }
        ByteBuffer bufferViewData = 
            createBufferViewData(bufferData, bufferView);
        if (bufferViewData != null)
        {
            gltfData.putBufferViewData(bufferViewId, bufferViewData);
        }
    }
    
    /**
     * Creates a byte buffer for the given {@link BufferView}, which is
     * a view on the {@link Buffer} that the given byte buffer belongs to.
     * Returns <code>null</code> if the 
     * {@link BufferView#getByteOffset() byte offset} or 
     * {@link BufferView#getByteLength() byte length} in the 
     * {@link BufferView} are not valid for the given byte buffer. 
     * 
     * @param bufferData The {@link Buffer} byte buffer
     * @param bufferView The {@link BufferView}
     * @return The byte buffer
     */
    public static ByteBuffer createBufferViewData(
        ByteBuffer bufferData, BufferView bufferView)  
    {
        Integer byteOffset = bufferView.getByteOffset();
        Integer byteLength = bufferView.getByteLength();
        if (byteLength == null)
        {
            // TODO The default value should be 0, but this simply
            // does not make sense. 
            // Updated: In glTF 1.0.1, the byte length will be required:
            // https://github.com/KhronosGroup/glTF/issues/560
            byteLength = bufferData.capacity() - byteOffset;
        }
        
        if (byteOffset < 0)
        {
            logger.warning("Negative byteOffset in bufferView: "+byteOffset);
            return null;
        }
        if (byteLength < 0)
        {
            logger.warning("Negative byteLength in bufferView: "+byteLength);
            return null;
        }
        if (byteOffset + byteLength > bufferData.capacity())
        {
            logger.warning(
                "The bufferView byteOffset is " + byteOffset + 
                " and the byteLength is " + byteLength + ", " +
                " but the buffer capacity is only " + 
                bufferData.capacity());
            return null;
        }
        ByteBuffer bufferViewData = 
            Buffers.createSlice(
                bufferData, byteOffset, byteLength);
        return bufferViewData;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private BufferViews()
    {
        // Private constructor to prevent instantiation 
    }
}
