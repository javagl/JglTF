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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
    private static class MenuNode
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
     * Write the given list of {@link MenuNode} objects as JSON to the
     * given output stream. The caller is responsible for closing the 
     * given stream.
     * 
     * @param menuNodes The menu nodes
     * @param outputStream The output stream
     * @throws IOException If an IO error occurs
     */
    private static void write(
        List<? extends MenuNode> menuNodes, OutputStream outputStream) 
            throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(outputStream, menuNodes);
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
    

    //=========================================================================
    
    /**
     * For internal purposes only
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        MenuNode samplesNode = new MenuNode();
        samplesNode.label = "Samples";
        samplesNode.children = new ArrayList<MenuNode>();
        samplesNode.children.add(createKhronosNode10());
        samplesNode.children.add(createTutorialNode());
        write(Arrays.asList(samplesNode), System.out);
    }
    
    /**
     * Create the {@link MenuNode} structure for the Khronos sample models,
     * version 1.0
     * 
     * @return The root {@link MenuNode}
     */
    private static MenuNode createKhronosNode10()
    {
        String basePath = "https://raw.githubusercontent.com/KhronosGroup/" + 
            "glTF-Sample-Models/master/1.0";
        
        List<String> names = Arrays.asList(
            "2CylinderEngine",
            "Box",
            "BoxAnimated",
            "BoxSemantics",
            "BoxTextured",
            "BoxWithoutIndices",
            "BrainStem",
            "Buggy",
            "CesiumMan",
            "CesiumMilkTruck",
            "Duck",
            "GearboxAssy",
            "Monster",
            "ReciprocatingSaw",
            "RiggedFigure",
            "RiggedSimple",
            "VC",
            "WalkingLady"
        );
        
        List<String> types = Arrays.asList(
            "glTF",
            "glTF-Embedded",
            "glTF-Binary",
            "glTF-MaterialsCommon"
        );

        MenuNode root = new MenuNode();
        root.label = "Khronos Samples, v1.0";
        root.children = new ArrayList<MenuNode>();
        
        for (String name : names)
        {
            MenuNode modelNode = new MenuNode();
            modelNode.label = name;
            modelNode.children = new ArrayList<MenuNode>();
            
            for (String type : types)
            {
                MenuNode typeNode = new MenuNode();
                typeNode.label = type;
                
                String extensionWithoutDot = "gltf";
                if (type.equals("glTF-Binary"))
                {
                    extensionWithoutDot = "glb";
                }
                typeNode.command = basePath + "/" + name + "/" + type + "/" + 
                    name + "." + extensionWithoutDot;
                modelNode.children.add(typeNode);
            }
            root.children.add(modelNode);
        }
        
        return root;
    }
    
    
    /**
     * Create the {@link MenuNode} structure for the tutorial sample models
     * 
     * @return The root {@link MenuNode}
     */
    private static MenuNode createTutorialNode()
    {
        String basePath = "https://raw.githubusercontent.com/" + 
            "javagl/gltfTutorialModels/master";
        
        MenuNode root = new MenuNode();
        root.label = "Tutorial Samples";
        root.children = new ArrayList<MenuNode>();
        
        root.children.add(createTutorialNode(basePath, 
            "TriangleWithoutIndices", "glTF", "glTF-Embedded"));
        root.children.add(createTutorialNode(basePath, 
            "Triangle", "glTF", "glTF-Embedded"));
        root.children.add(createTutorialNode(basePath, 
            "AnimatedTriangle", "glTF", "glTF-Embedded"));
        root.children.add(createTutorialNode(basePath, 
            "TriangleWithSimpleMaterial", "glTF", "glTF-Embedded-buffer"));
        root.children.add(createTutorialNode(basePath, 
            "SimpleMeshes", "glTF", "glTF-Embedded"));
        root.children.add(createTutorialNode(basePath, 
            "SimpleMeshesWithAdvancedMaterial", 
            "glTF", "glTF-Embedded-buffer"));
        root.children.add(createTutorialNode(basePath, 
            "SimpleOpacity", "glTF", "glTF-Embedded-buffer"));
        root.children.add(createTutorialNode(basePath, 
            "SimpleTexture", "glTF", "glTF-Embedded-buffer"));
        root.children.add(createTutorialNode(basePath, 
            "Cameras", "glTF", "glTF-Embedded"));
        root.children.add(createTutorialNode(basePath, 
            "SimpleSkin", "glTF", "glTF-Embedded-buffers"));
        
        return root;
    }
    
    /**
     * Create a {@link MenuNode} with the given name as a label and children
     * that have commands created from the given base path and types
     * 
     * @param basePath The base path
     * @param name The name
     * @param types The types
     * @return The {@link MenuNode}
     */
    private static MenuNode createTutorialNode(
        String basePath, String name, String ... types)
    {
        MenuNode modelNode = new MenuNode();
        modelNode.label = name;
        modelNode.children = new ArrayList<MenuNode>();
        
        for (String type : types)
        {
            MenuNode typeNode = new MenuNode();
            typeNode.label = type;

            String extensionWithoutDot = "gltf";
            if (type.equals("glTF-Binary"))
            {
                extensionWithoutDot = "glb";
            }
            typeNode.command = basePath + "/" + name + "/" + type + "/" + 
                name + "." + extensionWithoutDot;
            modelNode.children.add(typeNode);
        }
        
        return modelNode;
    }
    
    
}
