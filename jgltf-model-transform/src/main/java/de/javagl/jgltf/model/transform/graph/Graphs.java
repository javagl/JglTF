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
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Utility methods related to {@link Graph} instances
 * 
 * This class is not part of the public API.
 */
public class Graphs
{
    /**
     * Prints the given graph to the console, only for debugging.
     * 
     * @param <V> The vertex type
     * @param <E> The edge type
     * @param g The graph
     */
    public static <V, E extends Edge<V>> void print(Graph<V, E> g)
    {
        System.out.println(createString(g));
    }

    /**
     * Creates an unspecified string representation of the given graph, only for
     * debugging.
     * 
     * @param <V> The vertex type
     * @param <E> The edge type
     * @param g The graph
     * @return The string
     */
    public static <V, E extends Edge<V>> String createString(Graph<V, E> g)
    {
        StringBuilder sb = new StringBuilder();
        for (V v : g.vertices())
        {
            Collection<E> outgoing = g.outgoingEdgesOf(v);
            Collection<E> incoming = g.incomingEdgesOf(v);

            sb.append("For " + v + ": " + "\n");
            sb.append("  outgoing:" + "\n");
            if (outgoing.isEmpty())
            {
                sb.append("    " + "(none)" + "\n");
            }
            for (E e : outgoing)
            {
                sb.append("    " + e.v1() + "\n");
            }
            sb.append("  incoming:" + "\n");
            if (incoming.isEmpty())
            {
                sb.append("    " + "(none)" + "\n");
            }
            for (E e : incoming)
            {
                sb.append("    " + e.v0() + "\n");
            }
        }
        return sb.toString();
    }

    /**
     * Compute all vertices that are reachable from the given starting vertices
     * using only outgoing edges.
     * 
     * @param <V> The vertex type
     * @param <E> The edge type
     * @param g The graph
     * @param startingVertices The starting vertices
     * @param ignoredVertices Vertices that should be ignored
     * @return The reachable vertices
     */
    public static <V, E extends Edge<V>> Set<V> computeReachableForward(
        Graph<V, E> g, Set<V> startingVertices, Set<V> ignoredVertices)
    {
        Set<V> visited = new LinkedHashSet<V>();
        visited.addAll(ignoredVertices);
        for (V v : startingVertices)
        {
            traverseForward(g, v, visited);
        }
        visited.removeAll(ignoredVertices);
        return visited;
    }

    /**
     * Traverse the given graph, in unspecified order, starting at the given
     * root vertex, using only outgoing edges.
     * 
     * @param <V> The vertex type
     * @param <E> The edge type
     * @param g The graph
     * @param root The root vertex
     * @param visited The visited vertices
     */
    private static <V, E extends Edge<V>> void traverseForward(Graph<V, E> g,
        V root, Set<V> visited)
    {
        Deque<V> queue = new LinkedList<V>();
        visited.add(root);
        queue.add(root);
        while (!queue.isEmpty())
        {
            V v = queue.removeFirst();
            Collection<E> outgoingEdges = g.outgoingEdgesOf(v);
            for (E e : outgoingEdges)
            {
                V w = e.v1();
                if (!visited.contains(w))
                {
                    visited.add(w);
                    queue.add(w);
                }
            }
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Graphs()
    {
        // Private constructor to prevent instantiation
    }
}
