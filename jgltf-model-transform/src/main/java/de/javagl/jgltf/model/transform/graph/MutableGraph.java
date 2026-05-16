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

/**
 * Interface for a {@link Graph} that may be modified
 * 
 * This class is not part of the public API.
 * 
 * @param <V> The vertex type
 * @param <E> The edge type
 */
public interface MutableGraph<V, E extends Edge<V>> extends Graph<V, E>
{

    /**
     * Add the given vertex to this graph
     * 
     * @param v The vertex
     */
    void addVertex(V v);

    /**
     * Add the specified edge to this graph
     * 
     * @param v0 The first vertex
     * @param v1 The second vertex
     */
    void addEdge(V v0, V v1);
    
    /**
     * Remove the given vertex from this graph.
     * 
     * This will also cause the removal of all incoming and outgoing edges
     * of the given vertex.
     * 
     * @param v The vertex
     */
    void removeVertex(V v);
    
    /**
     * Remove the specified edge from this graph
     * 
     * @param v0 The first vertex
     * @param v1 The second vertex
     */
    void removeEdge(V v0, V v1);

}