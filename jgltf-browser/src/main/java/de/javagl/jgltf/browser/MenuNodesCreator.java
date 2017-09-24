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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.javagl.jgltf.browser.MenuNodes.MenuNode;

/**
 * For internal purposes only.
 */
class MenuNodesCreator
{
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
        samplesNode.children.add(createKhronosNode20());
        //samplesNode.children.add(createTutorialNode());
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
            "Box",
            "BoxWithoutIndices",
            "BoxTextured",
            "BoxSemantics",
            "Duck",
            "Avocado",
            "BarramundiFish",
            "SmilingFace",
            "2CylinderEngine",
            "ReciprocatingSaw",
            "GearboxAssy",
            "Buggy",
            "BoxAnimated",
            "CesiumMilkTruck",
            "RiggedSimple",
            "RiggedFigure",
            "WalkingLady",
            "CesiumMan",
            "Monster",
            "BrainStem",
            "VC"
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
     * Create the {@link MenuNode} structure for the Khronos sample models,
     * version 2.0
     * 
     * @return The root {@link MenuNode}
     */
    private static MenuNode createKhronosNode20()
    {
        String basePath = "https://raw.githubusercontent.com/KhronosGroup/" + 
            "glTF-Sample-Models/master/2.0";
        
        List<String> names = Arrays.asList(
            "TriangleWithoutIndices",
            "Triangle",
            "AnimatedTriangle",
            "AnimatedMorphCube",
            "AnimatedMorphSphere",
            "SimpleMeshes",
            "Cameras",
            "Box",
            "BoxTextured",
            "Duck",
            "SmilingFace",
            "2CylinderEngine",
            "ReciprocatingSaw",
            "GearboxAssy",
            "Buggy",
            "BoxAnimated",
            "CesiumMilkTruck",
            "RiggedSimple",
            "RiggedFigure",
            "WalkingLady",
            "CesiumMan",
            "Monster",
            "BrainStem",
            "VC",
            "Avocado",
            "BarramundiFish",
            "BoomBox",
            "Corset",
            "Lantern",
            "WaterBottle",
            "MetalRoughSpheres",
            "NormalTangentTest",
            "TwoSidedPlane",
            "Cube",
            "AnimatedCube",
            "Suzanne",
            "SciFiHelmet"
        );
        
        List<String> types = Arrays.asList(
            "glTF",
            "glTF-Embedded",
            "glTF-Binary",
            "glTF-MaterialsCommon"
        );

        MenuNode root = new MenuNode();
        root.label = "Khronos Samples, v2.0";
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
    /*
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
            "SimpleMaterial", "glTF", "glTF-Embedded-buffer"));
        root.children.add(createTutorialNode(basePath, 
            "SimpleMeshes", "glTF", "glTF-Embedded"));
        root.children.add(createTutorialNode(basePath, 
            "AdvancedMaterial", "glTF", "glTF-Embedded-buffer"));
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
    */
    
    /**
     * Create a {@link MenuNode} with the given name as a label and children
     * that have commands created from the given base path and types
     * 
     * @param basePath The base path
     * @param name The name
     * @param types The types
     * @return The {@link MenuNode}
     */
    /*
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
    */
    
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
    
}
