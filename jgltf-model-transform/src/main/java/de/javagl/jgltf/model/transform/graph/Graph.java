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

import java.util.Collection;
import java.util.Set;

/**
 * Interface for a basic graph.
 * 
 * Unless otherwise noted, all methods in this interface have to be assumed
 * to return unmodifiable collections, and possibly unmodifiable views on
 * an internal state.
 * 
 * This class is not part of the public API.
 * 
 * @param <V> The vertex type
 * @param <E> The edge type
 */
public interface Graph<V, E extends Edge<V>>
{
    /**
     * Returns the vertices.
     * 
     * @return The vertices
     */
    Set<V> vertices();

    /**
     * Returns the edges.
     * 
     * @return The edges.
     */
    Set<E> edges();
    
    /**
     * Returns the outgoing edges of the given vertex.
     * 
     * Returns an empty collection if the given vertex is not part of this
     * graph.
     * 
     * @param v The vertex
     * @return The edges
     */
    Collection<E> outgoingEdgesOf(V v);

    /**
     * Returns the incoming edges of the given vertex.
     * 
     * Returns an empty collection if the given vertex is not part of this
     * graph.
     * 
     * @param v The vertex
     * @return The edges
     */
    Collection<E> incomingEdgesOf(V v);
}
