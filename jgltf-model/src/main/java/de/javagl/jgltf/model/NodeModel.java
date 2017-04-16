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
package de.javagl.jgltf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import de.javagl.jgltf.impl.v1.Node;

/**
 * A model representing a {@link Node} of a glTF asset 
 */
public final class NodeModel
{
    /**
     * A thread-local, temporary 16-element matrix
     */
    private static final ThreadLocal<float[]> TEMP_MATRIX_4x4 =
        ThreadLocal.withInitial(() -> new float[16]);
    
    /**
     * The parent of this node. This is <code>null</code> for the root node.
     */
    private NodeModel parent;
    
    /**
     * The actual glTF {@link Node}
     */
    private final Node node;
    
    /**
     * The children of this node
     */
    private final List<NodeModel> children;
    
    /**
     * Default constructor. 
     * 
     * @param node The glTF {@link Node}
     */
    NodeModel(Node node)
    {
        this.node = Objects.requireNonNull(node, "The node may not be null");
        this.children = new ArrayList<NodeModel>();
    }
    
    /**
     * Package-private method to add the given child to this node
     * 
     * @param child The child
     */
    void addChild(NodeModel child)
    {
        Objects.requireNonNull(child, "The child may not be null");
        children.add(child);
        child.parent = this;
    }
    
    /**
     * Returns the actual glTF {@link Node}
     * 
     * @return The actual glTF {@link Node}
     */
    Node getNode()
    {
        return node;
    }
    
    /**
     * Returns an unmodifiable view on the list of children of this node
     * 
     * @return The children
     */
    List<NodeModel> getChildren()
    {
        return Collections.unmodifiableList(children);
    }
    
    /**
     * Computes the local transform of this node.<br>
     * <br>
     * The result will be written to the given array, as a 4x4 matrix in 
     * column major order. If the given array is <code>null</code> or does
     * not have a length of 16, then a new array with length 16 will be 
     * created and returned. 
     * 
     * @param result The result array
     * @return The result array
     */
    float[] computeLocalTransform(float result[])
    {
        return Nodes.computeLocalTransform(node, result);
    }
    
    /**
     * Computes the global transform of this node.<br>
     * <br>
     * The result will be written to the given array, as a 4x4 matrix in 
     * column major order. If the given array is <code>null</code> or does
     * not have a length of 16, then a new array with length 16 will be 
     * created and returned. 
     * 
     * @param result The result array
     * @return The result array
     */
    float[] computeGlobalTransform(float result[])
    {
        float localResult[] = Utils.validate(result, 16);
        float tempLocalTransform[] = TEMP_MATRIX_4x4.get();
        NodeModel currentNode = this;
        MathUtils.setIdentity4x4(localResult);
        while (currentNode != null)
        {
            currentNode.computeLocalTransform(tempLocalTransform);
            MathUtils.mul4x4(
                tempLocalTransform, localResult, localResult);
            currentNode = currentNode.parent;
        }
        return localResult;
    }
    
    /**
     * Creates a supplier for the global transform matrix of this node 
     * model.<br>
     * <br> 
     * The matrix will be provided as a float array with 16 elements, 
     * storing the matrix entries in column-major order.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     * 
     * @return The supplier
     */
    public Supplier<float[]> createGlobalTransformSupplier()
    {
        return Suppliers.createTransformSupplier(this, 
            (n, t) -> n.computeGlobalTransform(t));
    }
    
    /**
     * Creates a supplier for the local transform matrix of this node model.<br>
     * <br> 
     * The matrix will be provided as a float array with 16 elements, 
     * storing the matrix entries in column-major order.<br>
     * <br>
     * Note: The supplier MAY always return the same array instance.
     * Callers MUST NOT store or modify the returned array. 
     * 
     * @return The supplier
     */
    public Supplier<float[]> createLocalTransformSupplier()
    {
        return Suppliers.createTransformSupplier(node, 
            (n, t) -> Nodes.computeLocalTransform(n, t));
    }

    
}
