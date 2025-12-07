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
package de.javagl.jgltf.model.transform.graph;

import java.util.Objects;

/**
 * Default implementation of an {@link Edge}
 *
 * @param <V> The vertex type
 */
public class DefaultEdge<V> implements Edge<V>
{
    /**
     * The first vertex
     */
    private final V v0;
    
    /**
     * The second vertex
     */
    private final V v1;
    
    /**
     * Creates a new instance
     * 
     * @param v0 The first vertex
     * @param v1 The second vertex
     */
    public DefaultEdge(V v0, V v1)
    {
        this.v0 = Objects.requireNonNull(v0, "Vertex 0 may not be null");
        this.v1 = Objects.requireNonNull(v1, "Vertex 1 may not be null");
    }
    
    @Override
    public V v0()
    {
        return v0;
    }
    
    @Override
    public V v1()
    {
        return v1;
    }
}
