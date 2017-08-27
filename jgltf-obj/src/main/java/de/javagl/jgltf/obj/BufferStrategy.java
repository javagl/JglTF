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
package de.javagl.jgltf.obj;

import de.javagl.jgltf.impl.v1.Buffer;

/**
 * An enumeration of strategies for the creation of {@link Buffer}
 * instances when converting an OBJ file into a glTF asset.
 */
public enum BufferStrategy
{
    /**
     * Create one {@link Buffer} for the whole OBJ file
     */
    BUFFER_PER_FILE,
    
    /**
     * Create one {@link Buffer} for each MTL group
     */
    BUFFER_PER_GROUP,
    
    /**
     * Create one {@link Buffer} for each part (for the case that
     * the geometry data was split into multiple parts to obey 
     * the 16 bit index constraint)
     */
    BUFFER_PER_PART,
}

