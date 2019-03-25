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
package de.javagl.jgltf.model.impl.creation;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.io.GltfReference;

/**
 * Utility methods related to {@link BufferStructure} instances.
 * Only intended for internal use.
 */
public class BufferStructures
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BufferStructures.class.getName());
    
    /**
     * Resolve the given {@link GltfReference} instances against the given
     * {@link BufferStructure}. This will look up the {@link BufferModel} 
     * instances in the given structure, based on the  
     * {@link GltfReference#getUri() reference URI}, and pass the buffer
     * data to the {@link GltfReference#getTarget() reference target}.
     * If there is no {@link BufferModel} for a URI, then a warning will
     * be printed.
     * 
     * @param bufferReferences The {@link GltfReference} instances
     * @param bufferStructure The {@link BufferStructure}
     */
    public static void resolve(
        Iterable<? extends GltfReference> bufferReferences, 
        BufferStructure bufferStructure)
    {
        List<BufferModel> bufferModels = bufferStructure.getBufferModels();
        Map<String, BufferModel> uriToBufferModel = 
            new LinkedHashMap<String, BufferModel>();
        for (BufferModel bufferModel : bufferModels)
        {
            String uri = bufferModel.getUri();
            uriToBufferModel.put(uri, bufferModel);
        }
        for (GltfReference bufferReference : bufferReferences)
        {
            String uri = bufferReference.getUri();
            BufferModel bufferModel = uriToBufferModel.get(uri);
            if (bufferModel == null)
            {
                logger.warning("Could not resolve buffer model for URI " + uri
                    + " in buffer structure");
            }
            else
            {
                Consumer<ByteBuffer> target = bufferReference.getTarget();
                target.accept(bufferModel.getBufferData());
            }
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private BufferStructures()
    {
        // Private constructor to prevent instantiation
    }
}
