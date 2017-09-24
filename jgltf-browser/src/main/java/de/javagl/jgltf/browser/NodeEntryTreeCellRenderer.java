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
package de.javagl.jgltf.browser;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.javagl.jgltf.browser.ObjectTrees.NodeEntry;

/**
 * A <code>TreeCellRenderer</code> that renders a special string 
 * representation for nodes that are a <code>DefaultMutableTreeNode</code>
 * and contain a user object that is a {@link NodeEntry}
 */
class NodeEntryTreeCellRenderer 
    extends DefaultTreeCellRenderer
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1355228970206763912L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
        Object object, boolean selected, boolean expanded, 
        boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(
            tree, object, selected, expanded, leaf, row, hasFocus);
        if (object instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = 
                (DefaultMutableTreeNode)object;
            Object userObject = node.getUserObject();
            if (userObject instanceof NodeEntry)
            {
                NodeEntry nodeEntry = (NodeEntry)userObject;
                setText(createString(nodeEntry));
            }
        }
        return this;
    }
    
    /**
     * Create a string representation of the given {@link NodeEntry} for
     * the use in a tree cell renderer
     * 
     * @param nodeEntry The {@link NodeEntry}
     * @return The string
     */
    static String createString(NodeEntry nodeEntry)
    {
        String name = nodeEntry.getName();
        Object value = nodeEntry.getValue();
        if (value == null)
        {
            return name + " : " + value;
        }
        if (value instanceof String)
        {
            // Limit the string length to something that is reasonable
            // to be displayed as a tree node label
            String string = (String) value;
            final int maxLength = 80;
            if (string.length() > maxLength)
            {
                string = string.substring(0, maxLength) + "...";
            }
            return name + " : \"" + string + "\"";
        }
        if ((value instanceof Number) ||
            (value instanceof Boolean))
        {
            return name + " : "+value;
        }
        
        if (value instanceof double[])
        {
            double array[] = (double[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof Double[])
        {
            Double array[] = (Double[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof float[])
        {
            float array[] = (float[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof Float[])
        {
            Float array[] = (Float[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof int[])
        {
            int array[] = (int[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof Integer[])
        {
            Integer array[] = (Integer[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof boolean[])
        {
            boolean array[] = (boolean[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof Boolean[])
        {
            Boolean array[] = (Boolean[])value;
            return name + " : " + Arrays.toString(array);
        }
        if (value instanceof Number[])
        {
            Number array[] = (Number[])value;
            return name + " : " + Arrays.toString(array);
        }
        
        if (value instanceof Map<?, ?>)
        {
            return name;
        }
        if (value instanceof Collection<?>)
        {
            return name;
        }
        
        return name;
    }
    
}