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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Methods and classes related to creating <code>JTree</code> instances for 
 * arbitrary objects.
 */
class ObjectTrees
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ObjectTrees.class.getName());

    /**
     * The class for the user objects in the tree nodes
     */
    static class NodeEntry
    {
        /**
         * The name of the entry
         */
        private final String name;
        
        /**
         * The value of the entry
         */
        private final Object value;
        
        /**
         * Creates a new entry 
         * 
         * @param name The name
         * @param value The value
         */
        private NodeEntry(String name, Object value)
        {
            this.name = name;
            this.value = value;
        }
        
        /**
         * Returns the name of this entry 
         * 
         * @return The name of this entry
         */
        String getName()
        {
            return name;
        }

        /**
         * Returns the value of this entry
         * 
         * @return The value of this entry
         */
        Object getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return getName()+" : "+getValue();
        }
    }
    
    
    /**
     * Create a <code>TreeModel</code> for the given object
     * 
     * @param rootNodeName The root node name
     * @param object The object
     * @return The tree model
     */
    static TreeModel createTreeModel(String rootNodeName, Object object)
    {
        DefaultMutableTreeNode node = createNode(rootNodeName, object);
        TreeModel treeModel = new DefaultTreeModel(node);
        return treeModel;
    }
    
    
    /**
     * Recursively create the tree node for the given object
     * 
     * @param name The name of the {@link NodeEntry} that will be contained
     * in the tree node
     * @param value The value of the {@link NodeEntry} that will be contained
     * in the tree node
     * @return The tree node
     */
    private static DefaultMutableTreeNode createNode(String name, Object value) 
    {
        if (value == null)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                new NodeEntry(name, value));
            return node;
        }

        // Handle the case where the value is a List, Collection or a Map,
        // and the method should be called recursively with the values
        if (value instanceof List<?>)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                new NodeEntry(name, value));
            List<?> list = (List<?>)value;
            for (int i = 0; i < list.size(); i++)
            {
                Object element = list.get(i);
                DefaultMutableTreeNode childNode =
                    createNode("[" + i + "]", element);
                node.add(childNode);
            }
            return node;
        }
        if (value instanceof Collection<?>)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                new NodeEntry(name, value));
            Collection<?> collection = (Collection<?>)value;
            int index = 0;
            for (Object element : collection)
            {
                DefaultMutableTreeNode childNode = 
                    createNode("[" + index + "]", element);
                index++;
                node.add(childNode);
            }
            return node;
        }
        if (value instanceof Map<?, ?>)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                new NodeEntry(name, value));
            Map<?, ?> map = (Map<?, ?>)value;
            for (Entry<?, ?> entry : map.entrySet())
            {
                DefaultMutableTreeNode childNode = createNode(
                    String.valueOf(entry.getKey()), entry.getValue());
                node.add(childNode);
            }
            return node;
        }

        // Handle the "base case" where no recursion is required
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
            new NodeEntry(name, value));
        if ((value instanceof String) ||
            (value instanceof Number) ||
            (value instanceof Boolean)) 
        {
            return node;
        }

        
        // Handle the case where the object is of unknown type, and
        // reflection should be used for the recursive calls
        Map<String, Method> readMethods = 
            getReadMethods(value.getClass(), Comparator.naturalOrder());
        for (Entry<String, Method> entry : readMethods.entrySet())
        {
            String propertyName = entry.getKey();
            Method readMethod = entry.getValue();
            if (propertyName.equals("class"))
            {
                continue;
            }
            Object propertyValue = null;
            try
            {
                propertyValue = readMethod.invoke(value);
            } 
            catch (
                IllegalAccessException | 
                IllegalArgumentException | 
                InvocationTargetException e)
            {
                logger.warning("Could not read "+propertyName);
            }
            DefaultMutableTreeNode propertyNode = 
                createNode(propertyName, propertyValue);
            node.add(propertyNode);
        }
        return node;
    }
    
    
    /**
     * Returns a map of all "read methods" of the given class. A "read method"
     * is meant in the Java Beans sense: 
     * <ul>
     *   <li>The method has no arguments</li>
     *   <li>
     *     The method return type is neither <code>void</code> 
     *     nor <code>Void</code>
     *   </li>
     *   <li>
     *     The name of the method either starts with <code>"get"</code> or 
     *     with <code>"is"</code>. If it starts with <code>"is"</code>, it 
     *     will only be considered as a "read method" if its return type is 
     *     <code>boolean</code> or <code>Boolean</code>
     *   </li>
     * </ul>
     * The keys of the map will be the "property names" that are associated
     * with the respective read method.
     * 
     * @param c The class
     * @param comparator The comparator to use for sorting the keys (property
     * names). If this is <code>null</code>, then the order of the appearance
     * will be determined by the order of the methods when calling 
     * <code>Class#getMethods</code>.
     * @return The read methods
     */
    private static Map<String, Method> getReadMethods(Class<?> c, 
        Comparator<? super String> comparator)
    {
        Map<String, Method> readMethods = new LinkedHashMap<String, Method>();
        Method methods[] = c.getMethods();
        for (Method method : methods)
        {
            if (method.getParameterCount() != 0)
            {
                continue;
            }
            if (method.getReturnType().equals(void.class) ||
                method.getReturnType().equals(Void.class))
            {
                continue;
            }
            String methodName = method.getName();
            if (methodName.startsWith("get") && methodName.length() > 3)
            {
                String s = methodName.substring(3);
                String propertyName = 
                    Character.toLowerCase(s.charAt(0)) + s.substring(1);
                readMethods.put(propertyName, method);
            }
            else if (methodName.startsWith("is") && methodName.length() > 2 &&
                (method.getReturnType().equals(boolean.class) ||
                 method.getReturnType().equals(Boolean.class)))
            {
                String s = methodName.substring(2);
                String propertyName = 
                    Character.toLowerCase(s.charAt(0)) + s.substring(1);
                readMethods.put(propertyName, method);
            }
        }
        if (comparator == null)
        {
            return readMethods;
        }
        
        // Return a new map, with the insertion order depending on the
        // sort order of the keys based on the given comparator
        Map<String, Method> result = new LinkedHashMap<String, Method>();
        Set<String> sortedKeySet = new TreeSet<String>(comparator);
        sortedKeySet.addAll(readMethods.keySet());
        for (String key : sortedKeySet)
        {
            result.put(key, readMethods.get(key));
        }
        return result;
    }

    /**
     * Creates a simple string representation for the given tree path,
     * which is assumed to be a path of a tree created by this class.
     * This string will be of the form
     * <code>rootNodeName.propertyNameA.propertyNameB</code>.<br>
     * <br>
     * For collections, the property name may be an index, represented with
     * a string like <code>"[42]"</code>.<br>
     * <br>
     * If the given path contains elements that are not
     * <code>DefaultMutableTreeNode</code> instances, or contain
     * user objects that are not {@link NodeEntry} instances,
     * then a warning will be printed and an unspecified (possibly
     * <code>null</code>) string will be returned.
     * 
     * @param treePath The path
     * @return The path string
     */
    static String createPathString(TreePath treePath)
    {
        Object lastPathComponent = treePath.getLastPathComponent();
        if (lastPathComponent instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = 
                (DefaultMutableTreeNode)lastPathComponent;
            return createPathString(node);
        }
        logger.warning("No valid path for object tree: "+treePath);
        return null;
    }
    
    /**
     * Recursively create the path string for the given node
     * 
     * @param node The node
     * @return The path string
     * @see #createPathString(TreePath)
     */
    private static String createPathString(DefaultMutableTreeNode node)
    {
        StringBuilder sb = new StringBuilder();
        TreeNode parent = node.getParent();
        if (parent != null)
        {
            if (parent instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode parentNode =
                    (DefaultMutableTreeNode)parent;
                sb.append(createPathString(parentNode));
                sb.append(".");
            }
            else
            {
                logger.warning("Unexpected node type: "+parent.getClass());
            }
        }
        Object userObject = node.getUserObject();
        if (userObject != null)
        {
            if (userObject instanceof NodeEntry)
            {
                NodeEntry nodeEntry = (NodeEntry)userObject;
                sb.append(nodeEntry.getName());
            }
            else
            {
                logger.warning(
                    "Unexpected user object type: "+userObject.getClass());
            }
        }
        return sb.toString();
    }
    
    /**
     * Find the nodes that are <code>DefaultMutableTreeNode</code> with
     * a user object that is a {@link NodeEntry} that has the given value,
     * and return them as a (possibly unmodifiable) list.<br>
     * <br>
     * If the given tree model contains elements that are not
     * <code>DefaultMutableTreeNode</code> instances, or contain
     * user objects that are not {@link NodeEntry} instances,
     * then a warning will be printed, and the respective nodes
     * will be omitted in the returned list.
     * 
     * @param treeModel The tree model
     * @param nodeEntryValue The {@link NodeEntry} value
     * @return The nodes 
     */
    static List<DefaultMutableTreeNode> findNodesWithNodeEntryValue(
        TreeModel treeModel, Object nodeEntryValue)
    {
        Object root = treeModel.getRoot();
        if (!(root instanceof DefaultMutableTreeNode))
        {
            logger.warning("Unexpected node type: "+root.getClass());
            return Collections.emptyList();
        }
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)root;
        List<DefaultMutableTreeNode> result = 
            new ArrayList<DefaultMutableTreeNode>();
        findNodesWithNodeEntryValue(rootNode, nodeEntryValue, result);
        return result;
    }
    
    /**
     * Recursively find the node that has a user object that is a 
     * {@link NodeEntry} with the given value
     * 
     * @param node The current node
     * @param nodeEntryValue The {@link NodeEntry} value
     * @param result The list that stores the result 
     */
    private static void findNodesWithNodeEntryValue(
        DefaultMutableTreeNode node, Object nodeEntryValue, 
        List<DefaultMutableTreeNode> result)
    {
        Object userObject = node.getUserObject();
        if (userObject instanceof NodeEntry)
        {
            NodeEntry nodeEntry = (NodeEntry)userObject;
            if (nodeEntry.getValue() == nodeEntryValue)
            {
                result.add(node);
            }
        }
        else
        {
            logger.warning(
                "Unexpected user object type: "+userObject.getClass());
        }
        int n = node.getChildCount();
        for (int i=0; i<n; i++)
        {
            TreeNode child = node.getChildAt(i);
            if (child instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode childNode = 
                    (DefaultMutableTreeNode)child;    
                findNodesWithNodeEntryValue(childNode, nodeEntryValue, result);
            }
            else
            {
                logger.warning("Unexpected node type: "+child.getClass());
            }
        }
    }
    
    /**
     * Create a <code>TreePath</code> by going up from the given node until
     * the root node is reached
     * 
     * @param node The node 
     * @return The tree path
     */
    static TreePath createPath(TreeNode node) 
    {
        Deque<TreeNode> list = new LinkedList<TreeNode>();
        TreeNode current = node;
        while (current != null)
        {
            list.addFirst(current);
            current = current.getParent();
        }
        return new TreePath(list.toArray(new TreeNode[0]));
    }    
    
    /**
     * Returns the {@link NodeEntry#getValue() value} of the {@link NodeEntry}
     * that is the user object of the last path component of the given tree
     * path. If the given path is <code>null</code>, then <code>null</code>
     * is returned. If last path component is not a 
     * <code>DefaultMutableTreeNode</code>, then a warning is printed. 
     * If the user object in the last path component is not a {@link NodeEntry},
     * then a warning will be printed and <code>null</code> will be returned.
     * 
     * @param treePath The tree path
     * @return The {@link NodeEntry} value
     */
    static Object getNodeEntryValue(TreePath treePath)
    {
        if (treePath == null)
        {
            return null;
        }
        Object lastPathComponent = 
            treePath.getLastPathComponent();
        if (!(lastPathComponent instanceof DefaultMutableTreeNode))
        {
            logger.warning(
                "Unexpected node type in tree path: "+
                lastPathComponent.getClass());
            return null;
        }
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)lastPathComponent;
        Object userObject = node.getUserObject();
        if (!(userObject instanceof NodeEntry))
        {
            logger.warning(
                "Unexpected object type in tree node: "+
                userObject.getClass());
            return null;
        }
        NodeEntry nodeEntry = (NodeEntry)userObject;
        return nodeEntry.getValue();
    }
    
    
}