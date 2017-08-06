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
package de.javagl.jgltf.model.io.v2;

import java.nio.ByteBuffer;
import java.util.Objects;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A low-level representation of a glTF asset. It summarizes the {@link GlTF}
 * and the (optional) binary data.
 */
public final class GltfAssetV2
{
    /**
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * The optional binary data
     */
    private final ByteBuffer binaryData;
    
    /**
     * Creates a new instance
     * 
     * @param gltf The {@link GlTF}
     * @param binaryData The optional binary data
     */
    public GltfAssetV2(GlTF gltf, ByteBuffer binaryData)
    {
        this.gltf = Objects.requireNonNull(gltf, "The gltf may not be null");
        this.binaryData = binaryData;
    }
    
    /**
     * Returns the {@link GlTF} of this asset
     * 
     * @return The {@link GlTF}
     */
    public GlTF getGltf()
    {
        return gltf;
    }
    
    /**
     * Returns the binary data of this asset, or <code>null</code> if this
     * asset does not have associated binary data.<br>
     * <br>
     * The returned buffer will be a slice of the data that is stored 
     * internally. So changes of the contents of the buffer will affect
     * this asset, but changes of the limit or position of the buffer
     * will not affect this asset.
     *  
     * @return the optional binary data
     */
    public ByteBuffer getBinaryData()
    {
        return Buffers.createSlice(binaryData);
    }
    
}
