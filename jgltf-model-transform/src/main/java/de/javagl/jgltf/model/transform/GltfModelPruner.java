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
package de.javagl.jgltf.model.transform;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.transform.graph.Graphs;
import de.javagl.jgltf.model.transform.graph.model.ModelGraph;
import de.javagl.jgltf.model.transform.graph.model.ModelGraphs;

/**
 * A class with methods for pruning a {@link DefaultGltfModel}.
 */
class GltfModelPruner
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GltfModelPruner.class.getName());

    /**
     * Prune the given glTF model.
     * 
     * This will remove all elements in the model that are not reachable from
     * either the scene- or the animation models, and it will remove the given
     * elements.
     * 
     * This will iteratively remove all model elements that became invalid or
     * obsolete due to the removal of the given elements.
     * 
     * @param gltfModel The glTF model
     * @param toRemove The elements to remove. If this is <code>null</code>,
     *        then only a pruning of the current elements will be performed.
     */
    static void prune(DefaultGltfModel gltfModel,
        Collection<? extends ModelElement> toRemove)
    {
        Level level = Level.INFO;

        logger.info("Building model graph...");

        ModelGraph g = ModelGraphs.build(gltfModel);

        logger.info("Building model graph DONE");
        if (logger.isLoggable(level))
        {
            //logger.log(level, Graphs.createString(g));
        }
        
        // During traversal and reachability computations, ignore the
        // GltfModel itself (because all elements are reachable from it),
        // and the AssetModel (because it can never be pruned, and does
        // not refer to other elements)
        Set<ModelElement> ignored = new LinkedHashSet<ModelElement>();
        ignored.add(gltfModel);
        ignored.add(gltfModel.getAssetModel());

        // Start the traversal for the reachability computations at the scene-
        // and animation models
        Set<ModelElement> starting = new LinkedHashSet<ModelElement>();
        starting.addAll(gltfModel.getSceneModels());
        starting.addAll(gltfModel.getAnimationModels());

        // The set of elements that have to be removed in the next iteration
        Set<ModelElement> nextToRemove = new LinkedHashSet<ModelElement>();
        if (toRemove != null)
        {
            nextToRemove.addAll(toRemove);
        }
        
        // All elements that have been removed so far
        Set<ModelElement> allRemoved = new LinkedHashSet<ModelElement>();

        // Iterate the removal until there are no more unreachable or "invalid"
        // elements
        int iteration = 0;
        while (true)
        {
            logger.fine("Iteration " + iteration + "...");

            // Compute the reachable and unreachable elements
            Set<ModelElement> reachable =
                Graphs.computeReachableForward(g, starting, ignored);
            Set<ModelElement> unreachable =
                new LinkedHashSet<ModelElement>(g.vertices());
            unreachable.removeAll(ignored);
            unreachable.removeAll(reachable);

            if (logger.isLoggable(level))
            {
                logger.log(level, createString("Reachable", reachable));
                logger.log(level, createString("Unreachable", unreachable));
            }

            // Determine the elements to remove. These are the unreachable
            // elements, as well as the ones that have to be removed
            // explicitly from the previous iteration
            Set<ModelElement> currentToRemove =
                new LinkedHashSet<ModelElement>();
            currentToRemove.addAll(unreachable);
            if (!nextToRemove.isEmpty())
            {
                if (logger.isLoggable(level))
                {
                    logger.log(level, createString("Manual or implied removal",
                        nextToRemove));
                }
                currentToRemove.addAll(nextToRemove);
                nextToRemove.clear();
            }

            // If there is nothing to remove, stop the iteration
            if (currentToRemove.isEmpty())
            {
                logger.info("No more elements to remove after iteration "
                    + iteration + ", DONE");
                break;
            }

            if (logger.isLoggable(level))
            {
                logger.log(level,
                    createString("Total elements to removel", currentToRemove));
            }

            allRemoved.addAll(currentToRemove);
            
            // Remove the elements from each other model element
            for (ModelElement v : g.vertices())
            {
                // If the model element becomes invalid or obsolete due to
                // the removal (and it was not already removed), then track 
                // it as an element to remove in the next iteration
                boolean impliedRemoval = v.removeModelElements(currentToRemove);
                if (impliedRemoval)
                {
                    if (!allRemoved.contains(v))
                    {
                        if (logger.isLoggable(level))
                        {
                            logger.log(level, "Implied removal of " + v
                                + " due to removal of " + currentToRemove);
                        }
                        nextToRemove.add(v);
                    }
                }
            }

            // Update the graph by removing the vertices (model elements)
            for (ModelElement v : currentToRemove)
            {
                g.removeVertex(v);
            }

            logger.info("Iteration " + iteration + " DONE");
            iteration++;
        }
    }
    
    /**
     * Create an unspecified debug string from the given elements
     * 
     * @param name The name of the elements
     * @param elements The elements
     * @return The result
     */
    private static String createString(String name, Collection<?> elements)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " (" + elements.size() + ")");
        for (Object element : elements)
        {
            sb.append("\n" + "  " + element);
        }
        return sb.toString();
    }

    /**
     * Private constructor to prevent instantiation
     */
    private GltfModelPruner()
    {
        // Private constructor to prevent instantiation
    }

}
