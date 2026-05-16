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
package de.javagl.jgltf.model.transform.graph.model;

import java.util.Set;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ModelElement;

/**
 * Methods related to {@link ModelGraph} objects.
 * 
 * This class is not part of the public API.
 */
public class ModelGraphs
{
    /**
     * Build a {@link ModelGraph} from the given model
     * 
     * @param model The mode
     * @return The {@link ModelGraph}
     */
    public static ModelGraph build(GltfModel model)
    {
        ModelGraph g = new ModelGraph();
        build(g, model);
        return g;
    }
    
    /**
     * Fill the given model graph with the data from the given model element,
     * called recursively
     * 
     * @param g The graph
     * @param current The current model element
     */
    static void build(ModelGraph g, ModelElement current)
    {
        //System.out.println("buildGraph from "+current);
        if (g.vertices().contains(current))
        {
            return;
        }
        g.addVertex(current);
        Set<ModelElement> nexts = current.getReferencedModelElements();
        for (ModelElement next : nexts)
        {
            g.addEdge(current, next);
            build(g, next);
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ModelGraphs()
    {
        // Private constructor to prevent instantiation
    }

}
