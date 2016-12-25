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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main class of the glTF browser
 */
public class GltfBrowser
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfBrowser.class.getName());
    
    /**
     * The entry point of this application
     * 
     * @param args The command line arguments
     */
    public static void main(String[] args)
    {
        initLogging();
        setPlatformLookAndFeel();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        SwingUtilities.invokeLater(() -> createAndShowGui(args));
    }
    
    
    /**
     * Create and show the GUI, to be called on the event dispatch thread
     * 
     * @param args The command line arguments
     */
    private static void createAndShowGui(String args[])
    {
        GltfBrowserApplication application = new GltfBrowserApplication();
        initSampleModelsMenus(application, args);
    }

    /**
     * Initialize the menus for the sample models in the given application
     * 
     * @param application The application
     * @param args The command line arguments
     */
    private static void initSampleModelsMenus(
        GltfBrowserApplication application, String args[])
    {
        Map<String, String> argsMap = parseArguments(args);

        // If no "-sampleModels" argument was given, then try to load
        // the default sampleModels, either as a file or a resource
        if (!argsMap.containsKey("-sampleModels"))
        {
            initDefaultSampleModelsMenu(application);
            return;
        }

        String value = argsMap.get("-sampleModels");
        if (value == null)
        {
            logger.warning(
                "No value was given for the sampleModels parameter");
            return;
        }

        List<String> sampleModelMenuFileNames = 
            Arrays.asList(value.split(","));
        for (String fileName : sampleModelMenuFileNames)
        {
            logger.info("Loading sample model menu from " + fileName);
            List<JMenu> menus = loadMenuFile(fileName);
            if (menus != null)
            {
                application.addSampleModelMenus(menus);
            }
        }
    }
    
    /**
     * Try to load the "sampleModels.config" as a local file. If this file 
     * does not exist, load it as a resource. Add the menus that are 
     * defined in this file to the given application. 
     * 
     * @param application The application
     */
    private static void initDefaultSampleModelsMenu(
        GltfBrowserApplication application)
    {
        // Try to load the "sampleModels.config" as a local file.
        // If this file does not exist, load it as a resource.
        File defaultSampleModelsMenuFile = 
            new File("./sampleModels.config");
        if (defaultSampleModelsMenuFile.exists())
        {
            logger.info("Loading sample model menu from " + 
                defaultSampleModelsMenuFile);
            List<JMenu> menus = 
                loadMenuFile("./sampleModels.config");
            if (menus != null)
            {
                application.addSampleModelMenus(menus);
            }
        }
        else 
        {
            //logger.info("Loading sample model menu from resource");
            List<JMenu> menus = 
                loadMenuResource("/sampleModels.config");
            if (menus != null)
            {
                application.addSampleModelMenus(menus);
            }
        }
    }


    /**
     * Initialize the logging based on the logging properties resource
     */
    private static void initLogging()
    {
        try (InputStream inputStream = 
            GltfBrowser.class.getResourceAsStream("/logging.properties"))
        {
            LogManager.getLogManager().readConfiguration(inputStream);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Try to set the default platform look and feel
     */
    private static void setPlatformLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            // Ignored
        }
        catch (ClassNotFoundException e)
        {
            // Ignored
        }
        catch (InstantiationException e)
        {
            // Ignored
        }
        catch (IllegalAccessException e)
        {
            // Ignored
        }
    }

    
    /**
     * Parse the given command line arguments into a map. If an argument
     * contains a <code>'='</code>, then it will be split into a key and
     * a value part and stored in the map. Otherwise, the argument will
     * be stored as a key that is mapped to <code>null</code>.
     * 
     * @param args The command line arguments
     * @return The map
     */
    private static Map<String, String> parseArguments(String args[])
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String arg : args)
        {
            int equalsIndex = arg.indexOf('=');
            if (equalsIndex != -1)
            {
                String key = arg.substring(0, equalsIndex);
                String value = arg.substring(equalsIndex + 1, arg.length());
                map.put(key, value);
            }
            else
            {
                map.put(arg, null);
            }
        }
        return map;
    }    
    
    
    /**
     * Load the menus from the specified path. If any error occurs,
     * then a warning is printed and <code>null</code> is returned.
     *  
     * @param path The path
     * @return The menus
     */
    private static List<JMenu> loadMenuFile(String path)
    {
        try (InputStream inputStream = new FileInputStream(path))
        {
            return MenuNodes.createMenus(inputStream);
        }
        catch (IOException e)
        {
            logger.warning("Could not load menus: " + e.getMessage());
            return null;
        }
    }

    
    /**
     * Load the menus from the specified resource. If any error occurs,
     * then a warning is printed and <code>null</code> is returned.
     *  
     * @param resource The resource
     * @return The menus
     */
    private static List<JMenu> loadMenuResource(String resource)
    {
        try (InputStream inputStream =
            GltfBrowser.class.getResourceAsStream(resource))
        {
            if (inputStream == null)
            {
                logger.warning("Could not find resource: " + resource);
                return null;
            }
            return MenuNodes.createMenus(inputStream);
        }
        catch (IOException e)
        {
            logger.warning("Could not load menus: " + e.getMessage());
            return null;
        }
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private GltfBrowser()
    {
        // Private constructor to prevent instantiation
    }
}
