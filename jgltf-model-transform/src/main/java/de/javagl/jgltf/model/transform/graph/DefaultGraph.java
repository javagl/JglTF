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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Default implementation of a {@link Graph}
 *
 * This class is not part of the public API.
 *
 * @param <V> The vertex type
 * @param <E> The edge type
 */
public class DefaultGraph<V, E extends Edge<V>> implements MutableGraph<V, E>
{
    /**
     * The vertices
     */
    private final Set<V> vertices;
    
    /**
     * The edges
     */
    private final Set<E> edges;
    
    /**
     * The outgoing edges
     */
    private final Map<V, List<E>> outgoingEdgesOf;
    
    /**
     * The incoming edges
     */
    private final Map<V, List<E>> incomingEdgesOf;
    
    /**
     * The function that creates the edges
     */
    private final BiFunction<V, V, E> edgeFactory;
    
    /**
     * Default constructor
     * 
     * @param edgeFactory The edge factory
     */
    public DefaultGraph(BiFunction<V, V, E> edgeFactory)
    {
        this.vertices = new LinkedHashSet<V>();
        this.edges = new LinkedHashSet<E>();
        this.outgoingEdgesOf = new LinkedHashMap<V, List<E>>();
        this.incomingEdgesOf = new LinkedHashMap<V, List<E>>();
        this.edgeFactory = Objects.requireNonNull(edgeFactory, "The edgeFactory may not be null");
    }
    
    @Override
    public void addVertex(V v)
    {
        Objects.requireNonNull(v, "The vertex may not be null");
        this.vertices.add(v);
    }
    
    @Override
    public void removeVertex(V v)
    {
        this.vertices.remove(v);
        Set<E> edgesToRemove = new LinkedHashSet<E>();
        List<E> outgoingEdges = outgoingEdgesOf.get(v);
        if (outgoingEdges != null)
        {
            edgesToRemove.addAll(outgoingEdges);
        }
        List<E> incomingEdges = incomingEdgesOf.get(v);
        if (incomingEdges != null)
        {
            edgesToRemove.addAll(incomingEdges);
        }
        for (E e : edgesToRemove)
        {
            removeEdge(e.v0(), e.v1());
        }
    }

    @Override
    public Set<V> vertices()
    {
        return Collections.unmodifiableSet(vertices);
    }

    /**
     * Add the specified edge to this graph
     * 
     * @param v0 The first vertex
     * @param v1 The second vertex
     */
    @Override
    public void addEdge(V v0, V v1)
    {
        E e = edgeFactory.apply(v0, v1);
        this.edges.add(e);
        Function<V, List<E>> mappingFunction = (k) -> new ArrayList<E>();
        outgoingEdgesOf.computeIfAbsent(v0, mappingFunction).add(e);
        incomingEdgesOf.computeIfAbsent(v1, mappingFunction).add(e);
    }
    
    @Override
    public void removeEdge(V v0, V v1)
    {
        List<E> outgoingEdges = outgoingEdgesOf.get(v0);
        if (outgoingEdges != null)
        {
            Set<E> edgesToRemove = new LinkedHashSet<E>();
            for (E e : outgoingEdges)
            {
                if (Objects.equals(e.v1(), v1))
                {
                    edgesToRemove.add(e);
                    edges.remove(e);
                }
            }
            outgoingEdges.removeAll(edgesToRemove);
            if (outgoingEdges.isEmpty())
            {
                outgoingEdgesOf.remove(v0);
            }
        }
        List<E> incomingEdges = incomingEdgesOf.get(v1);
        if (incomingEdges != null)
        {
            Set<E> edgesToRemove = new LinkedHashSet<E>();
            for (E e : incomingEdges)
            {
                if (Objects.equals(e.v0(), v0))
                {
                    edgesToRemove.add(e);
                    edges.remove(e);
                }
            }
            incomingEdges.removeAll(edgesToRemove);
            if (incomingEdges.isEmpty())
            {
                incomingEdgesOf.remove(v1);
            }
        }
    }

    @Override
    public Set<E> edges()
    {
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public Collection<E> outgoingEdgesOf(V v)
    {
        List<E> result = outgoingEdgesOf.get(v);
        if (result == null)
        {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(result);
    }

    @Override
    public Collection<E> incomingEdgesOf(V v)
    {
        List<E> result = incomingEdgesOf.get(v);
        if (result == null)
        {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(result);
    }
}
