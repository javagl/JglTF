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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.javagl.jgltf.model.GltfData;

/**
 * The main glTF browser application class, containing the main frame 
 * and the top-level UI components  
 */
class GltfBrowserApplication
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfBrowserApplication.class.getName());
    
    /**
     * The Action for opening a glTF file
     * 
     * @see #openFile()
     */
    private final Action openFileAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = -5125243029591873126L;

        // Initialization
        {
            putValue(NAME, "Open file...");
            putValue(SHORT_DESCRIPTION, "Open a glTF file");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_F));
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            openFile();
        }
    };

    /**
     * The Action for opening a glTF URI
     * 
     * @see #openUri()
     */
    private final Action openUriAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = -512524309591873126L;

        // Initialization
        {
            putValue(NAME, "Open URI...");
            putValue(SHORT_DESCRIPTION, "Open a glTF URI");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            openUri();
        }
    };
    
    /**
     * The Action for exiting the application
     * 
     * @see #exitApplication()
     */
    private final Action exitAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = -51252430991873126L;

        // Initialization
        {
            putValue(NAME, "Exit");
            putValue(SHORT_DESCRIPTION, "Exit the application");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            exitApplication();
        }

    };

    
    /**
     * The main frame of the application
     */
    private final JFrame frame;

    /**
     * The FileChooser for opening glTF files
     */
    private final JFileChooser openFileChooser;
    
    /**
     * The desktop pane that will contain the internal frames with
     * the glTF browser panels 
     */
    private final JDesktopPane desktopPane;
    
    /**
     * Default constructor
     */
    GltfBrowserApplication()
    {
        frame = new JFrame("GltfBrowser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GltfTransferHandler transferHandler = 
            new GltfTransferHandler(this);
        frame.setTransferHandler(transferHandler);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createSampleModelsMenu());
        frame.setJMenuBar(menuBar);
        
        openFileChooser = new JFileChooser(".");
        openFileChooser.setFileFilter(
            new FileNameExtensionFilter(
                "glTF Files (.gltf, .glb)", "gltf", "glb"));
        
        desktopPane = new JDesktopPane();
        frame.getContentPane().add(desktopPane);
        
        frame.setSize(1000,700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Create the file menu
     * 
     * @return The file menu
     */
    private JMenu createFileMenu()
    {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(openFileAction));
        fileMenu.add(new JMenuItem(openUriAction));
        fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem(exitAction));
        return fileMenu;
    }
    
    /**
     * Create the menu containing shortcuts for loading the sample models
     * 
     * @return The menu
     */
    private JMenu createSampleModelsMenu()
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
                    frame, "Invalid URI: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            openUriInBackground(uri);
        });
        return menuItem;
    }
    
    
    /**
     * Open the file chooser to select a file which will be 
     * loaded upon confirmation
     */
    private void openFile()
    {
        int returnState = openFileChooser.showOpenDialog(frame);
        if (returnState == JFileChooser.APPROVE_OPTION) 
        {
            File file = openFileChooser.getSelectedFile();
            openUriInBackground(file.toURI());
        }        
    }

    /**
     * Open a dialog to enter a URI which will be loaded upon confirmation
     */
    private void openUri()
    {
        JOptionPane optionPane = new JOptionPane(new JLabel("URI:"), 
            JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        optionPane.setWantsInput(true);
        JDialog dialog = optionPane.createDialog(frame, "Enter URI");
        dialog.setResizable(true);
        dialog.setVisible(true);
        Object value = optionPane.getValue();
        if (value == null)
        {
            return;
        }
        if (!value.equals(JOptionPane.OK_OPTION))
        {
            return;
        }
        String uriString = (String)optionPane.getInputValue();
        if (uriString == null || uriString.trim().isEmpty())
        {
            return;
        }
        URI uri = null;
        try
        {
            uri = new URI(uriString);
        } 
        catch (URISyntaxException e)
        {
            JOptionPane.showMessageDialog(
                frame, "Invalid URI: "+e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        openUriInBackground(uri);
    }
    
    /**
     * Execute the task of loading the {@link GltfData} in a background 
     * thread, showing a modal dialog. When the data is loaded, it will 
     * be passed to {@link #createGltfBrowserPanel(String, GltfData)}
     * 
     * @param uri The URI to load from
     */
    void openUriInBackground(URI uri)
    {
        GltfLoadingWorker gltfLoadingWorker = 
            new GltfLoadingWorker(this, frame, uri);
        gltfLoadingWorker.load();
    }
    
    /**
     * Create the {@link GltfBrowserPanel} for the given {@link GltfData}, 
     * and add it (in an internal frame with the given string as its title)
     * to the desktop pane
     * 
     * @param uriString The URI string, used as the internal frame title
     * @param gltfData The {@link GltfData}
     */
    void createGltfBrowserPanel(String uriString, GltfData gltfData)
    {
        final boolean resizable = true;
        final boolean closable = true;
        final boolean maximizable = true;
        final boolean iconifiable = true;
        JInternalFrame internalFrame = new JInternalFrame(
            uriString, resizable, closable , maximizable, iconifiable);
        Component gltfBrowserPanel = new GltfBrowserPanel(gltfData);
        internalFrame.getContentPane().add(gltfBrowserPanel);
        internalFrame.setSize(gltfBrowserPanel.getPreferredSize());
        internalFrame.setLocation(0, 0);
        desktopPane.add(internalFrame);
        try
        {
            internalFrame.setMaximum(true);
        } 
        catch (PropertyVetoException e)
        {
            // Ignored
        }
        internalFrame.setVisible(true);
    }
    
    /**
     * Exit the application
     */
    private void exitApplication()
    {
        frame.setVisible(false);
        frame.dispose();
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