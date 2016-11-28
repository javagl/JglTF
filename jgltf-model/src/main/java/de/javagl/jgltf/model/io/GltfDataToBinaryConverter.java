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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Shader;
import de.javagl.jgltf.model.BinaryGltf;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfExtensions;

/**
 * A class for converting {@link GltfData} to binary glTF. 
 */
public class GltfDataToBinaryConverter
{
    /**
     * Creates a new glTF data to binary converter
     */
    public GltfDataToBinaryConverter()
    {
        // Default constructor
    }
    
    /**
     * Convert the given {@link GltfData} to binary glTF.
     *  
     * @param inputGltfData The input {@link GltfData}
     * @return The converted {@link GltfData}
     */
    public GltfData convert(GltfData inputGltfData)
    {
        GlTF inputGltf = inputGltfData.getGltf();
        GltfData convertedGltfData = 
            new GltfData(GltfUtils.copy(inputGltf));
        GlTF convertedGltf = convertedGltfData.getGltf();
        
        // Create the new byte buffer for the data of the "binary_glTF" Buffer,
        // and store it in the GltfData
        int binaryGltfBufferSize = 
            computeBinaryGltfBufferSize(inputGltfData);
        ByteBuffer binaryGltfByteBuffer = 
            Buffers.create(binaryGltfBufferSize);
        convertedGltfData.putBufferData(
            BinaryGltf.getBinaryGltfBufferId(), binaryGltfByteBuffer);
        
        // Create the "binary_glTF" Buffer, 
        GltfExtensions.addExtensionUsed(convertedGltf, 
            BinaryGltf.getBinaryGltfExtensionName());
        Buffer binaryGltfBuffer = new Buffer();
        binaryGltfBuffer.setType("arraybuffer");
        binaryGltfBuffer.setUri(
            BinaryGltf.getBinaryGltfBufferId() + ".bin");
        binaryGltfBuffer.setByteLength(binaryGltfBufferSize);
        Map<String, Buffer> newBuffers = Collections.singletonMap(
            BinaryGltf.getBinaryGltfBufferId(), binaryGltfBuffer);
        
        // Create defensive copies of the original maps, as linked maps
        // with a fixed iteration order (!). If the input maps are null,
        // then these will be empty maps. This has to be considered when
        // the new maps are created and put into the glTF!
        Map<String, Buffer> oldBuffers = copy(convertedGltf.getBuffers());
        Map<String, Image> oldImages = copy(convertedGltf.getImages());
        Map<String, Shader> oldShaders = copy(convertedGltf.getShaders());
        
        // Place the data from buffers, images and shaders into the
        // new binary glTF buffer. The mappings from IDs to offsets 
        // inside the resulting buffer will be used to compute the
        // offsets for the buffer views
        Map<String, Integer> bufferOffsets = concatBuffers(oldBuffers, 
            binaryGltfByteBuffer, inputGltfData::getBufferData);
        Map<String, Integer> imageOffsets = concatBuffers(oldImages, 
            binaryGltfByteBuffer, inputGltfData::getImageData);
        Map<String, Integer> shaderOffsets = concatBuffers(oldShaders, 
            binaryGltfByteBuffer, inputGltfData::getShaderData);
        binaryGltfByteBuffer.position(0);
        
        // For all existing BufferViews, create new ones that are updated to 
        // refer to the new binary glTF buffer, with the appropriate offset
        Map<String, BufferView> oldBufferViews = 
            copy(convertedGltf.getBufferViews());
        Map<String, BufferView> newBufferViews = 
            new LinkedHashMap<String, BufferView>();
        for (Entry<String, BufferView> oldEntry : oldBufferViews.entrySet())
        {
            String id = oldEntry.getKey();
            BufferView oldBufferView = oldEntry.getValue();
            BufferView newBufferView = GltfUtils.copy(oldBufferView);
            
            newBufferView.setBuffer(BinaryGltf.getBinaryGltfBufferId());
            String oldBufferId = oldBufferView.getBuffer();
            int oldByteOffset = oldBufferView.getByteOffset();
            int bufferOffset = bufferOffsets.get(oldBufferId);
            int newByteOffset = oldByteOffset + bufferOffset;
            newBufferView.setByteOffset(newByteOffset);
            
            newBufferViews.put(id, newBufferView);
        }
        
        // For all existing Images, create new ones that are updated to 
        // refer to the new binary glTF buffer, using a bufferView ID
        // (in the binary_glTF extension object) that refers to a newly
        // created BufferView
        Map<String, Image> newImages = 
            new LinkedHashMap<String, Image>();
        for (Entry<String, Image> oldEntry : oldImages.entrySet())
        {
            String id = oldEntry.getKey();
            Image oldImage = oldEntry.getValue();
            Image newImage = GltfUtils.copy(oldImage);
            
            // Create the BufferView for the image
            ByteBuffer imageData = inputGltfData.getImageData(id);
            int byteLength = imageData.capacity();
            int byteOffset = imageOffsets.get(id);
            BufferView imageBufferView = new BufferView();
            imageBufferView.setBuffer(BinaryGltf.getBinaryGltfBufferId());
            imageBufferView.setByteOffset(byteOffset);
            imageBufferView.setByteLength(byteLength);

            // Store the BufferView under a newly generated ID
            String generatedBufferViewId = 
                createNewBufferViewId("image_"+id, oldBufferViews.keySet());
            newBufferViews.put(generatedBufferViewId, imageBufferView);
            
            // Let the image refer to the BufferView via its extension object
            BinaryGltf.setBinaryGltfBufferViewId(
                newImage, generatedBufferViewId);
            
            // Set the width, height and mimeType properties for the
            // extension object
            BinaryGltf.setBinaryGltfImageProperties(newImage, imageData);
            
            newImages.put(id, newImage);
        }
        
        // For all existing Shaders, create new ones that are updated to 
        // refer to the new binary glTF buffer using a bufferView ID
        // (in the binary_glTF extension object) that refers to a newly
        // created BufferView
        Map<String, Shader> newShaders = 
            new LinkedHashMap<String, Shader>();
        for (Entry<String, Shader> oldEntry : oldShaders.entrySet())
        {
            String id = oldEntry.getKey();
            Shader oldShader = oldEntry.getValue();
            Shader newShader = GltfUtils.copy(oldShader);
            
            // Create the BufferView for the shader
            ByteBuffer shaderData = inputGltfData.getShaderData(id);
            int byteLength = shaderData.capacity();
            int byteOffset = shaderOffsets.get(id);
            BufferView shaderBufferView = new BufferView();
            shaderBufferView.setBuffer(BinaryGltf.getBinaryGltfBufferId());
            shaderBufferView.setByteOffset(byteOffset);
            shaderBufferView.setByteLength(byteLength);

            // Store the BufferView under a newly generated ID
            String generatedBufferViewId = 
                createNewBufferViewId("shader_"+id, oldBufferViews.keySet());
            newBufferViews.put(generatedBufferViewId, shaderBufferView);
            
            // Let the shader refer to the BufferView via its extension object
            BinaryGltf.setBinaryGltfBufferViewId(
                newShader, generatedBufferViewId);
            
            newShaders.put(id, newShader);
        }
        
        // Place the newly created mappings into the converted glTF,
        // if there have been non-null mappings for them in the input
        if (inputGltf.getBuffers() != null)
        {
            convertedGltf.setBuffers(newBuffers);
        }
        if (inputGltf.getImages() != null)
        {
            convertedGltf.setImages(newImages);
        }
        if (inputGltf.getShaders() != null)
        {
            convertedGltf.setShaders(newShaders);
        }
        if (!newBufferViews.isEmpty())
        {
            convertedGltf.setBufferViews(newBufferViews);
        }
        
        // Fill the GltfData with the appropriate byte buffers
        convertedGltfData.putBufferData(
            BinaryGltf.getBinaryGltfBufferId(), binaryGltfByteBuffer);
        BufferViews.createBufferViewByteBuffers(convertedGltfData);
        
        // Put the image data buffers into the GltfData
        for (Entry<String, Image> entry : newImages.entrySet())
        {
            String id = entry.getKey();
            Image image = entry.getValue();
            String bufferViewId = 
                BinaryGltf.getBinaryGltfBufferViewId(image);
            ByteBuffer bufferViewData = 
                convertedGltfData.getBufferViewData(bufferViewId);
            convertedGltfData.putImageData(id, bufferViewData);
        }
        
        // Put the shader data buffers into the GltfData
        for (Entry<String, Shader> entry : newShaders.entrySet())
        {
            String id = entry.getKey();
            Shader shader = entry.getValue();
            String bufferViewId = 
                BinaryGltf.getBinaryGltfBufferViewId(shader);
            ByteBuffer bufferViewData = 
                convertedGltfData.getBufferViewData(bufferViewId);
            byte data[] = new byte[bufferViewData.capacity()];
            bufferViewData.slice().get(data);
            convertedGltfData.putShaderData(id, bufferViewData);
        }
        
        return convertedGltfData;
    }
    
    
    /**
     * Create an unspecified {@link BufferView} ID that does not exist yet
     * 
     * @param namePart A string that should appear in the ID
     * @param existingBufferViewIds The existing IDs
     * @return The new ID
     */
    private static String createNewBufferViewId(
        String namePart, Set<String> existingBufferViewIds)
    {
        int counter = 0;
        while (true)
        {
            String bufferViewId = "bufferView_for_"+namePart;
            if (counter > 0)
            {
                bufferViewId += "_"+counter;
            }
            if (existingBufferViewIds.contains(bufferViewId))
            {
                counter++;
            }
            else
            {
                return bufferViewId;
            }
        }
    }
    
    
    /**
     * Compute the total size that is required for the binary glTF buffer
     * for the given {@link GltfData}
     * 
     * @param gltfData The {@link GltfData}
     * @return The total size for the binary glTF buffer
     */
    private static int computeBinaryGltfBufferSize(GltfData gltfData)
    {
        int binaryGltfBufferSize = 0;
        GlTF gltf = gltfData.getGltf();
        binaryGltfBufferSize += computeTotalBuffersSize(gltf.getBuffers(), 
            gltfData::getBufferData);
        binaryGltfBufferSize += computeTotalBuffersSize(gltf.getImages(), 
            gltfData::getImageData);
        binaryGltfBufferSize += computeTotalBuffersSize(gltf.getShaders(), 
            gltfData::getShaderData);
        return binaryGltfBufferSize;
    }
    
    /**
     * Compute the total buffer size that is required for the buffers of
     * the given elements. If the given elements are <code>null</code>, 
     * then 0 will be returned.
     * 
     * @param elements The mapping from IDs to elements
     * @param idToByteBuffer The function that provides the byte buffer of
     * the element, based on the ID of the element
     * @return The total buffer size
     */
    private static int computeTotalBuffersSize(
        Map<String, ?> elements, 
        Function<? super String, ? extends ByteBuffer> idToByteBuffer)
    {
        if (elements == null)
        {
            return 0;
        }
        int totalSize = 0;
        for (Entry<String, ?> entry : elements.entrySet())
        {
            String id = entry.getKey();
            ByteBuffer byteBuffer = idToByteBuffer.apply(id);
            totalSize += byteBuffer.capacity();
        }
        return totalSize;
    }
    
    /**
     * Put the contents of all byte buffers that are associated with the
     * given elements into the given target buffer. This method assumes
     * that the target buffer has a sufficient capacity to hold all 
     * buffers. This capacity may be computed with 
     * {@link #computeTotalBuffersSize(Map, Function)}
     * 
     * @param elements The mapping from IDs to elements
     * @param targetBuffer The target buffer
     * @param idToByteBuffer The function that provides the byte buffer of
     * the element, based on the ID of the element
     * @return A mapping from each ID to the offset inside the target buffer
     */
    private static Map<String, Integer> concatBuffers(
        Map<String, ?> elements, 
        ByteBuffer targetBuffer,
        Function<? super String, ? extends ByteBuffer> idToByteBuffer)
    {
        Map<String, Integer> offsets = new LinkedHashMap<String, Integer>();
        for (String oldId : elements.keySet())
        {
            ByteBuffer oldByteBuffer = idToByteBuffer.apply(oldId);
            int offset = targetBuffer.position();
            offsets.put(oldId, offset);
            targetBuffer.put(oldByteBuffer.slice());
        }
        return offsets;
    }
    
    /**
     * Creates a copy of the given map, as a linked hash map. If the given
     * map is <code>null</code>, then an unmodifiable empty map will be
     * returned
     * 
     * @param map The input map
     * @return The copy
     */
    private static <K, V> Map<K, V> copy(Map<K, V> map)
    {
        if (map == null)
        {
            return Collections.emptyMap();
        }
        return new LinkedHashMap<K, V>(map);
    }
    

}
