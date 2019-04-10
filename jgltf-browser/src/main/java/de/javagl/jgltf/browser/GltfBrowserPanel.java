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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.javagl.common.ui.tree.filtered.FilteredTree;
import de.javagl.common.ui.tree.filtered.TreeModelFilter;
import de.javagl.jgltf.browser.ObjectTrees.NodeEntry;
import de.javagl.jgltf.browser.Resolver.ResolvedEntity;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.viewer.GltfViewer;

/**
 * A panel for browsing through the glTF of a {@link GltfModel}
 * and displaying information about the glTF entities. 
 */
class GltfBrowserPanel extends JPanel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfBrowserPanel.class.getName());

    /**
     *  Serial UID
     */
    private static final long serialVersionUID = 8959452050508861357L;

    /**
     * The {@link GltfModel} that is displayed in this panel
     */
    private final GltfModel gltfModel;
    
    /**
     * The tree that displays the glTF structure
     */
    private JTree tree;
    
    /**
     * The history of tree paths that have been selected
     */
    private final Deque<TreePath> selectionPathHistory;
    
    /**
     * The last path that was selected in the tree with the mouse, or 
     * via the popup menu. This is the full path, and may contain elements 
     * that are not contained in the current (filtered) tree model. 
     */
    private TreePath currentSelectionPath;

    /**
     * The factory for the info components of selected elements
     */
    private final InfoComponentFactory infoComponentFactory;
    
    /**
     * The {@link Resolver} that is used for resolving glTF
     * entities that are clicked in the tree
     */
    private final Resolver resolver;
    
    /**
     * The Action for going back in the selection history
     * 
     * @see #backToPreviousSelection()
     */
    private final Action backAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = -515243029591873126L;

        // Initialization
        {
            putValue(NAME, "Back");
            putValue(SHORT_DESCRIPTION, "Go back to previous selection");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
            setEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            backToPreviousSelection();
        }
    };
    
    /**
     * The main tabbed pane showing the info components and the 
     * {@link GltfViewerPanel}
     */
    private final JTabbedPane mainTabbedPane;
    
    /**
     * The {@link GltfViewerPanel}
     */
    private final GltfViewerPanel gltfViewerPanel;

    /**
     * A container for the panels that show information about the
     * entities that are selected in the tree
     */
    private final JPanel infoPanelContainer;
    
    /**
     * Creates a new browser panel for the given {@link GltfModel}
     * 
     * @param gltfModel The {@link GltfModel}
     * @param gltf The glTF object associated with the model
     */
    GltfBrowserPanel(GltfModel gltfModel, Object gltf)
    {
        super(new BorderLayout());
        
        this.gltfModel = Objects.requireNonNull(
            gltfModel, "The gltfModel may not be null");
        Objects.requireNonNull(
            gltf, "The gltf may not be null");

        this.selectionPathHistory = new LinkedList<TreePath>();
        
        this.infoComponentFactory = new InfoComponentFactory(gltfModel, gltf);
        this.resolver = new Resolver(gltf);
        
        add(createControlPanel(), BorderLayout.NORTH);
        
        JSplitPane mainSplitPane = new JSplitPane();
        add(mainSplitPane, BorderLayout.CENTER);
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mainSplitPane.setDividerLocation(0.3);
            }
        });
        
        mainSplitPane.setLeftComponent(
            createTreePanel(gltf));
        
        mainTabbedPane = new JTabbedPane();
        mainSplitPane.setRightComponent(mainTabbedPane);
        
        infoPanelContainer = new JPanel(new GridLayout(1,1));
        mainTabbedPane.addTab("Info", infoPanelContainer);
        
        gltfViewerPanel = new GltfViewerPanel(gltfModel);
        mainTabbedPane.addTab("View", gltfViewerPanel);
    }
    
    /**
     * Dispose the current {@link GltfViewer}. This will stop all animations,
     * remove the {@link GltfModel} from the viewer, and set the viewer to
     * <code>null</code>
     */
    void disposeGltfViewer()
    {
        gltfViewerPanel.disposeGltfViewer();
    }
    
    /**
     * Returns the {@link GltfModel} that is displayed in this panel
     * 
     * @return The {@link GltfModel}
     */
    GltfModel getGltfModel()
    {
        return gltfModel;
    }
    
    /**
     * Create the panel containing the (filterable, browsable) tree 
     * showing the structure of the given glTF
     *  
     * @param gltf The glTF
     * @return The tree panel
     */
    private JPanel createTreePanel(Object gltf)
    {
        JPanel treePanel = new JPanel(new BorderLayout());

        TreeModel originalTreeModel = 
            ObjectTrees.createTreeModel("glTF", gltf);
        FilteredTree filteredTree = 
            FilteredTree.create(originalTreeModel);
        tree = filteredTree.getTree();
        tree.setCellRenderer(new NodeEntryTreeCellRenderer());
        tree.addTreeSelectionListener(e -> treeSelectionChanged());
        
        JPopupMenu popupMenu = new JPopupMenu();
        MouseListener popupMenuMouseListener = new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                currentSelectionPath = 
                    tree.getPathForLocation(e.getX(), e.getY());
                tree.setSelectionPath(currentSelectionPath);
                if (SwingUtilities.isRightMouseButton(e))
                {
                    boolean menuContainsEntries = 
                        preparePopupMenu(popupMenu, currentSelectionPath);
                    if (menuContainsEntries)
                    {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        };
        tree.addMouseListener(popupMenuMouseListener);        
        treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        

        JTextField filterTextField = new JTextField();
        treePanel.add(filterTextField, BorderLayout.NORTH);
        filterTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                update();
            }
            
            private void update()
            {
                TreePath oldSelectionPath = currentSelectionPath;
                
                String s = filterTextField.getText();
                if (s == null || s.trim().length() == 0)
                {
                    filteredTree.setFilter(null);
                }
                else
                {
                    filteredTree.setFilter(
                        createNodeEntryModelFilter(s));
                }
                
                if (oldSelectionPath != null)
                {
                    TreeModel filteredModel = filteredTree.getFilteredModel();
                    TreePath newSelectionPath = translatePathPartial(
                        filteredModel, oldSelectionPath);
                    tree.setSelectionPath(newSelectionPath);
                }
            }
        });        
        
        return treePanel;
    }
    
    /**
     * Creates a tree model filter that accepts a node when it is a  
     * <code>DefaultMutableTreeNode</code> that contains a 
     * <code>NodeEntry</code> as its user object, whose
     * {@link NodeEntryTreeCellRenderer#createString(NodeEntry) string
     * representation} case-insensitively contains the given string,
     * or when it has a descendant to which this property applies
     * 
     * @param string The string to look for
     * @return The filter
     */
    private TreeModelFilter createNodeEntryModelFilter(String string)
    {
        return new TreeModelFilter()
        {
            @Override
            public boolean acceptNode(TreeModel treeModel, TreeNode node)
            {
                if (!(node instanceof DefaultMutableTreeNode))
                {
                    return false;
                }
                DefaultMutableTreeNode n = (DefaultMutableTreeNode)node;
                Object userObject = n.getUserObject();
                if (!(userObject instanceof NodeEntry))
                {
                    return false;
                }
                NodeEntry nodeEntry = (NodeEntry)userObject;
                String nodeEntryString = 
                    NodeEntryTreeCellRenderer.createString(nodeEntry);
                if (nodeEntryString.toLowerCase().contains(
                    string.toLowerCase())) 
                {
                    return true;
                }
                for (int i=0; i<node.getChildCount(); i++)
                {
                    if (acceptNode(treeModel, node.getChildAt(i)))
                    {
                        return true;
                    }
                }
                return false;
            }
        };        
    }
    
    
    /**
     * Create the control panel
     * 
     * @return The control panel
     */
    private JPanel createControlPanel()
    {
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton(backAction);
        controlPanel.add(backButton);
        return controlPanel;
    }
    
    /**
     * Go back to the previous selection in the tree
     */
    private void backToPreviousSelection()
    {
        if (!selectionPathHistory.isEmpty())
        {
            TreePath treePath = selectionPathHistory.removeLast();
            tree.setSelectionPath(treePath);
            tree.expandPath(treePath);
            scrollToTop(tree, getParentPath(treePath));
            
        }
        if (selectionPathHistory.isEmpty())
        {
            backAction.setEnabled(false);
        }
    }
    
    /**
     * Will be called whenever the tree selection changed, and update
     * the info panel based on the selected entity
     */
    private void treeSelectionChanged()
    {
        infoPanelContainer.removeAll();
        JComponent infoComponent = 
            infoComponentFactory.createInfoComponent(
                tree.getSelectionPath());
        if (infoComponent != null)
        {
            infoPanelContainer.add(infoComponent);
        }
        infoPanelContainer.validate();
        infoPanelContainer.repaint();
    }
    
    
    

    /**
     * Prepare the given popup menu to contain the items that are 
     * appropriate for the given selection path
     *  
     * @param popupMenu The popup menu
     * @param treePath The selection path
     * @return Whether the menu contains entries
     */
    private boolean preparePopupMenu(
        JPopupMenu popupMenu, TreePath treePath)
    {
        popupMenu.removeAll();
        Object nodeEntryValue = ObjectTrees.getNodeEntryValue(treePath);
        if (nodeEntryValue == null)
        {
            return false;
        }
        
        String pathString = ObjectTrees.createPathString(treePath);
        ResolvedEntity resolvedEntity = 
            resolver.resolve(pathString, nodeEntryValue);
        if (resolvedEntity == null)
        {
            return false;
        }
        if (resolvedEntity.getMessage() != null)
        {
            popupMenu.add(createMessageMenuItem(resolvedEntity.getMessage()));
        }
        else
        {
            popupMenu.add(createTreeNodeSelectionMenuItem(
                resolvedEntity.getKey(), resolvedEntity.getValue()));
        }
        return true;
    }
    

    /**
     * Create a disabled menu item with the given text
     *  
     * @param text The text
     * @return The menu item
     */
    private static JMenuItem createMessageMenuItem(String text)
    {
        JMenuItem item = new JMenuItem(text);
        item.setEnabled(false);
        return item;
    }


    /**
     * Create a menu item for selecting the node in the tree that contains 
     * the given value.
     * <br>
     * The node is determined as the first of the nodes returned by 
     * {@link ObjectTrees#findNodesWithNodeEntryValue}.
     * If there is no tree node that contains the resulting value, then
     * an "error message" menu item will be created and returned. 
     *  
     * @param key The key
     * @param value The value
     * @return The menu item
     */
    private JMenuItem createTreeNodeSelectionMenuItem(
        Object key, Object value)
    {
        List<DefaultMutableTreeNode> nodes = 
            ObjectTrees.findNodesWithNodeEntryValue(tree.getModel(), value);
        if (nodes.isEmpty())
        {
            return createMessageMenuItem("Not found: "+key);
        }
        if (nodes.size() > 1)
        {
            logger.warning("Found " + nodes.size() + " nodes for " + key);
        }
        DefaultMutableTreeNode node = nodes.get(0);
        JMenuItem item = new JMenuItem("Select "+key);
        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TreePath oldTreePath = tree.getSelectionPath();
                if (oldTreePath != null)
                {
                    selectionPathHistory.add(oldTreePath);
                    backAction.setEnabled(true);
                }
                
                TreePath treePath = ObjectTrees.createPath(node);
                tree.setSelectionPath(treePath);
                tree.expandPath(treePath);
                scrollToTop(tree, treePath);
                
                currentSelectionPath = treePath;
            }
        });
        return item;
    }
    
    
 
    
    
    /**
     * Returns the tree path of the parent of the given tree path. 
     * 
     * @param treePath The tree path
     * @return The parent tree path
     */
    private static TreePath getParentPath(TreePath treePath)
    {
        Object[] array = treePath.getPath();
        if (array.length == 0)
        {
            return treePath;
        }
        return new TreePath(Arrays.copyOf(array,  array.length-1));
    }
 
    /**
     * Scroll the given tree so that the given tree path is at the upper
     * border of the containing scroll pane
     * 
     * @param tree The tree
     * @param treePath The tree path
     */
    private static void scrollToTop(JTree tree, TreePath treePath)
    {
        Rectangle bounds = tree.getPathBounds(treePath);
        if (bounds == null)
        {
            return;
        }
        Container parent = tree.getParent();
        if (parent instanceof JViewport)
        {
            JViewport viewport = (JViewport)parent;
            Rectangle viewRect = viewport.getViewRect();
            Rectangle rectangle = new Rectangle(
                0, bounds.y, viewRect.width, viewRect.height);
            tree.scrollRectToVisible(rectangle);
        }
    }
    
    /**
     * Translates one TreePath to a new TreeModel. This methods assumes 
     * DefaultMutableTreeNodes, and identifies the path based on the
     * user objects.
     * 
     * This method is similar to 
     * {@link de.javagl.common.ui.JTrees#translatePath(TreeModel, TreePath)},
     * but returns a "partial" path if the new tree model only contains a
     * subset of the given path.
     * 
     * @param newTreeModel The new tree model
     * @param oldPath The old tree path
     * @return The new tree path
     */
    private static TreePath translatePathPartial(
        TreeModel newTreeModel, TreePath oldPath)
    {
        Object newRoot = newTreeModel.getRoot();
        List<Object> newPath = new ArrayList<Object>();
        newPath.add(newRoot);
        Object newPreviousElement = newRoot;
        for (int i=1; i<oldPath.getPathCount(); i++)
        {
            Object oldElement = oldPath.getPathComponent(i);
            DefaultMutableTreeNode oldElementNode = 
                (DefaultMutableTreeNode)oldElement;
            Object oldUserObject = oldElementNode.getUserObject();
            
            Object newElement = getChildWith(newPreviousElement, oldUserObject);
            if (newElement == null)
            {
                break;
            }
            newPath.add(newElement);
            newPreviousElement = newElement;
        }
        return new TreePath(newPath.toArray());
    }
    
    
    /**
     * Returns the child with the given user object in the given tree
     * node. Assumes DefaultMutableTreeNodes.
     * 
     * @param node The node
     * @param userObject The user object
     * @return The child with the given user object, or <code>null</code>
     * if no such child can be found.
     */
    private static Object getChildWith(Object node, Object userObject)
    {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node;
        for (int j=0; j<treeNode.getChildCount(); j++)
        {
            TreeNode child = treeNode.getChildAt(j);
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)child;
            Object childUserObject = childNode.getUserObject();
            if (Objects.equals(userObject, childUserObject))
            {
                return child;
            }
        }
        return null;
    }
    
    
    
}