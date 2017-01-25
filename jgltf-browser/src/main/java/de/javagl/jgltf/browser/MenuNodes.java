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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for creating JMenu instances from JSON data
 */
class MenuNodes
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(MenuNodes.class.getName());
    
    /**
     * Private class representing one node in the menu structure
     */
    static class MenuNode
    {
        /**
         * The label (text) of the node. (Note that for nodes that also
         * have a command, the actual text in the menu may be extended
         * with the command, but the details here are unspecified)
         */
        public String label;
        
        /**
         * The command that will be passed as the action command to 
         * all listeners. This will usually be <code>null</code>
         * for sub-menus that have {@link #children}.
         */
        public String command;
        
        /**
         * The list of children. This will usually be <code>null</code>
         * for nodes that have a {@link #command}
         */
        public List<MenuNode> children;
    }
    
    /**
     * Create the JMenus for the menu node structure that is contained in
     * the JSON data provided by the given input stream.
     * The caller is responsible for closing the given stream.
     * 
     * @param inputStream The input stream.
     * @return The menus
     * @throws IOException If an IO error occurs
     */
    static List<JMenu> createMenus(InputStream inputStream) 
            throws IOException
    {
        List<? extends MenuNode> menuNodes = read(inputStream);
        List<JMenuItem> menuItems = createMenuItems(menuNodes);
        return menuItems.stream()
            .filter(e -> JMenu.class.isInstance(e))
            .map(e -> JMenu.class.cast(e))
            .collect(Collectors.toList());
    }
    
    /**
     * Read the list of {@link MenuNode} objects from the JSON that is 
     * provided by the given input stream. The caller is responsible
     * for closing the given stream.
     * 
     * @param inputStream The input stream
     * @return The list of {@link MenuNode} instances
     * @throws IOException If an IO error occurs
     */
    private static List<? extends MenuNode> read(InputStream inputStream) 
        throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<MenuNode>>typeReference = 
            new TypeReference<List<MenuNode>>()
        {
            // Empty class
        };
        List<? extends MenuNode> menuNodes = 
            objectMapper.readValue(inputStream, typeReference);
        return menuNodes;
    }
    
    /**
     * Create a list of menu items (which may be JMenu instances) for the 
     * given {@link MenuNode} instances.  
     * 
     * @param menuNodes The {@link MenuNode}s
     * @return The menus
     */
    private static List<JMenuItem> createMenuItems(
        List<? extends MenuNode> menuNodes)
    {
        List<JMenuItem> menuItems = new ArrayList<JMenuItem>();
        
        for (MenuNode menuNode : menuNodes)
        {
            if (menuNode.children != null)
            {
                JMenu menu = new JMenu();
                menu.setText(menuNode.label);
                
                List<JMenuItem> childMenuItems = 
                    createMenuItems(menuNode.children);
                for (JMenuItem childMenuItem : childMenuItems)
                {
                    menu.add(childMenuItem);
                }
                menuItems.add(menu);
            }
            else
            {
                if (menuNode.command == null)
                {
                    logger.warning("Empty menu node - skipping");
                    continue;
                }
                JMenuItem menuItem = new JMenuItem();
                String label = 
                    "<html>" + menuNode.label + 
                    " <font size=-2>(" + menuNode.command + ")</font>" + 
                    "</html>";
                menuItem.setText(label);
                menuItem.setActionCommand(menuNode.command);
                menuItems.add(menuItem);
            }

        }
        return menuItems;        
    }    
}
