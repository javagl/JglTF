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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * A class for creating a menu that contains the URIs of the official
 * Khronos glTF sample models
 */
class SampleModelsMenuFactory
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(SampleModelsMenuFactory.class.getName());
    
    /**
     * The parent component for possible error dialogs that may be
     * shown due to invalid URIs
     */
    private final Component dialogParentComponent;
    
    /**
     * The consumer for the selected URIs
     */
    private final Consumer<? super URI> selectedUriConsumer;
    
    /**
     * Create a new instance of this factory
     * 
     * @param dialogParentComponent The parent component for possible error 
     * dialogs that may be shown due to invalid URIs 
     * @param selectedUriConsumer The consumer for the selected URIs
     */
    SampleModelsMenuFactory(Component dialogParentComponent, 
        Consumer<? super URI> selectedUriConsumer)
    {
        Objects.requireNonNull(selectedUriConsumer, 
            "The selectedUriConsumer may not be null");
        this.dialogParentComponent = dialogParentComponent;
        this.selectedUriConsumer = selectedUriConsumer;
    }
    
    /**
     * Create the menu containing shortcuts for loading the sample models
     * 
     * @return The menu
     */
    JMenu createSampleModelsMenu()
    {
        JMenu sampleModelsMenu = new JMenu("Sample models");

        Properties sampleModelsProperties = loadProperties(
            "/sampleModels.properties");
        if (sampleModelsProperties == null)
        {
            JMenuItem errorMenuItem = new JMenuItem(
                "Could not load sample model definitions");
            errorMenuItem.setEnabled(false);
            sampleModelsMenu.add(errorMenuItem);
            return sampleModelsMenu;
        }
        
        Object basePathObject = sampleModelsProperties.get("basePath");
        if (basePathObject == null)
        {
            JMenuItem errorMenuItem = new JMenuItem(
                "Could not find sample models base path");
            errorMenuItem.setEnabled(false);
            sampleModelsMenu.add(errorMenuItem);
            return sampleModelsMenu;
        }
        
        String basePath = String.valueOf(basePathObject);
        for (Entry<Object, Object> entry : sampleModelsProperties.entrySet())
        {
            String keyString = String.valueOf(entry.getKey());
            if (keyString.startsWith("sampleModel"))
            {
                Object valueObject = entry.getValue();
                if (valueObject == null)
                {
                    continue;
                }
                String valueString = String.valueOf(valueObject);
                sampleModelsMenu.add(
                    createSampleModelMenu(basePath, valueString));
            }
        }
        return sampleModelsMenu;
    }
    
    /**
     * Create a menu containing the shortcuts for loading the sample model
     * with the given name, in different formats
     * 
     * @param basePath The base path
     * @param name The name of the sample model
     * @return The menu
     */
    private JMenu createSampleModelMenu(String basePath, String name)
    {
        JMenu sampleModelMenu = new JMenu(name);
        
        sampleModelMenu.add(createSampleModelMenuItem(
            basePath, name, "glTF", "gltf"));
        sampleModelMenu.add(createSampleModelMenuItem(
            basePath, name, "glTF-Embedded", "gltf"));
        sampleModelMenu.add(createSampleModelMenuItem(
            basePath, name, "glTF-Binary", "glb"));
        sampleModelMenu.add(createSampleModelMenuItem(
            basePath, name, "glTF-MaterialsCommon", "gltf"));
        return sampleModelMenu;
    }
    
    /**
     * Create a menu item for loading the specified sample model.
     * 
     * @param basePath The base path
     * @param name The name of the sample model
     * @param type The type, e.g. "glTF-Binary"
     * @param extensionWithoutDot The expected file extension, without a dot
     * @return The menu item
     */
    private JMenuItem createSampleModelMenuItem(
        String basePath, String name, String type, String extensionWithoutDot)
    {
        String uriString = 
            basePath + "/" + name + "/" + type + "/" + 
            name + "." + extensionWithoutDot;
        JMenuItem menuItem = new JMenuItem(
            "<html>" + type + " <font size=-2>(" + 
            uriString + ")</font></html>");
        menuItem.addActionListener(event -> 
        {
            URI uri = null;
            try
            {
                uri = new URI(uriString);
            } 
            catch (URISyntaxException e)
            {
                JOptionPane.showMessageDialog(
                    dialogParentComponent, "Invalid URI: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectedUriConsumer.accept(uri);
        });
        return menuItem;
    }

    /**
     * Load the properties from the specified resource. Returns 
     * <code>null</code> if any error occurs.
     *  
     * @param resource The resource
     * @return The properties
     */
    private static Properties loadProperties(String resource)
    {
        Properties properties = new Properties();
        InputStream inputStream =  
            GltfBrowserApplication.class.getResourceAsStream(resource);
        if (inputStream == null)
        {
            return null;
        }
        try 
        {    
            properties.load(inputStream);
        }
        catch (IOException e)
        {
            logger.warning("Could not load properties: " + e.getMessage());
            return null;
        }
        finally
        {
            try
            {
                inputStream.close();
            } 
            catch (IOException e)
            {
                logger.warning("Could not close stream: " + e.getMessage());
            }
        }
        return properties;
    }
    
}
