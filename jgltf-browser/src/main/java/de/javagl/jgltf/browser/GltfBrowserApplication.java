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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
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
import de.javagl.jgltf.model.io.BinaryGltfDataWriter;
import de.javagl.jgltf.model.io.GltfDataToBinaryConverter;
import de.javagl.jgltf.model.io.GltfDataToEmbeddedConverter;
import de.javagl.jgltf.model.io.GltfDataWriter;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.obj.ObjGltfDataCreator;
import de.javagl.swing.tasks.SwingTask;
import de.javagl.swing.tasks.SwingTaskExecutors;

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
     * The Action for importing a file
     * 
     * @see #importFile()
     */
    private final Action importFileAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 6442563220376147803L;


        // Initialization
        {
            putValue(NAME, "Import file...");
            putValue(SHORT_DESCRIPTION, "Import a non-glTF file");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_I));
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            importFile();
        }
    };
    
    /**
     * The Action for saving a glTF
     * 
     * @see #saveAs()
     */
    private final Action saveAsAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 6442563220376147803L;

        // Initialization
        {
            putValue(NAME, "Save as...");
            putValue(SHORT_DESCRIPTION, 
                "Save the selected glTF to a directory");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
            setEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            saveAs();
        }
    };
    
    /**
     * The Action for saving a glTF as a binary glTF
     * 
     * @see #saveAsBinary()
     */
    private final Action saveAsBinaryAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 8683961991846940413L;

        // Initialization
        {
            putValue(NAME, "Save as binary...");
            putValue(SHORT_DESCRIPTION, 
                "Save the selected glTF to a binary glTF");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
            setEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            saveAsBinary();
        }
    };
    
    
    /**
     * The Action for converting the {@link GltfData} of the currently 
     * selected frame into an embedded glTF
     * 
     * @see #convertToEmbeddedGltf()
     */
    private final Action convertToEmbeddedGltfAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 8683961991846940413L;

        // Initialization
        {
            putValue(NAME, "Convert to embedded glTF");
            putValue(SHORT_DESCRIPTION, 
                "Convert the currently selected glTF into an embedded glTF");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
            setEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            convertToEmbeddedGltf();
        }
    };
    
    /**
     * The Action for converting the {@link GltfData} of the currently 
     * selected frame into a binary glTF
     * 
     * @see #convertToBinaryGltf()
     */
    private final Action convertToBinaryGltfAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 8683961991846940413L;

        // Initialization
        {
            putValue(NAME, "Convert to binary glTF");
            putValue(SHORT_DESCRIPTION, 
                "Convert the currently selected glTF into a binary glTF");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
            setEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            convertToBinaryGltf();
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
     * The menu showing the most recently used URIs
     */
    private JMenu recentUrisMenu;
    
    /**
     * The number of recently used entries
     */
    private static final int NUM_RECENT_URI_ENTRIES = 8;
    
    /**
     * The recent URIs
     */
    private Deque<URI> recentUris;
    
    /**
     * The main frame of the application
     */
    private final JFrame frame;

    /**
     * The FileChooser for opening glTF files
     */
    private final JFileChooser openFileChooser;

    /**
     * The FileChooser for importing files
     */
    private final JFileChooser importFileChooser;
    
    /**
     * The FileChooser for saving glTF files
     */
    private final JFileChooser saveFileChooser;

    /**
     * The FileChooser for saving binary glTF files
     */
    private final JFileChooser saveBinaryFileChooser;
    
    /**
     * The desktop pane that will contain the internal frames with
     * the glTF browser panels 
     */
    private final JDesktopPane desktopPane;

    /**
     * The currently selected internal frame, that contains the 
     * {@link GltfBrowserPanel}
     */
    private JInternalFrame selectedInternalFrame;
    
    /**
     * The property change listener that will be added to all internal frames, 
     * and track when a frame is selected, and stores which of them is the 
     * current {@link #selectedInternalFrame}
     */
    private final PropertyChangeListener selectedInternalFrameTracker =
        new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent event)
            {
                if (JInternalFrame.IS_SELECTED_PROPERTY.equals(
                    event.getPropertyName()))
                {
                    if (Boolean.TRUE.equals(event.getNewValue()))
                    {
                        selectedInternalFrame = 
                            (JInternalFrame)event.getSource();
                        convertToEmbeddedGltfAction.setEnabled(true);
                        convertToBinaryGltfAction.setEnabled(true);
                        saveAsAction.setEnabled(true);
                        saveAsBinaryAction.setEnabled(true);
                    }
                }
            }
        };
    
    /**
     * Default constructor
     */
    GltfBrowserApplication()
    {
        recentUris = new LinkedList<URI>();
        
        frame = new JFrame("GltfBrowser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GltfTransferHandler transferHandler = 
            new GltfTransferHandler(this);
        frame.setTransferHandler(transferHandler);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createConvertMenu());
        menuBar.add(createSampleModelsMenu());
        frame.setJMenuBar(menuBar);
        
        openFileChooser = new JFileChooser(".");
        openFileChooser.setFileFilter(
            new FileNameExtensionFilter(
                "glTF Files (.gltf, .glb)", "gltf", "glb"));
        
        importFileChooser = new JFileChooser(".");
        importFileChooser.setFileFilter(
            new FileNameExtensionFilter(
                "Importable Files (.obj)", "obj"));
        
        saveFileChooser = new JFileChooser(".");
        saveBinaryFileChooser = new JFileChooser(".");
        
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

        recentUrisMenu = new JMenu("Recent");
        updateRecentUrisMenu();
        fileMenu.add(recentUrisMenu);
        fileMenu.add(new JSeparator());

        fileMenu.add(new JMenuItem(importFileAction));
        fileMenu.add(new JSeparator());
        
        fileMenu.add(new JMenuItem(saveAsAction));
        fileMenu.add(new JMenuItem(saveAsBinaryAction));
        fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem(exitAction));
        return fileMenu;
    }
    
    /**
     * Update the {@link #recentUrisMenu} based on the contents of the
     * current recent URIs file
     */
    private void updateRecentUrisMenu()
    {
        readRecentUris();
        recentUrisMenu.removeAll();
        for (URI uri : recentUris)
        {
            JMenuItem menuItem = new JMenuItem(uri.toString());
            menuItem.addActionListener(
                e -> openUriInBackground(uri));
            recentUrisMenu.add(menuItem);
        }
    }
    
    /**
     * Returns the file that stores the recently used URIs
     * 
     * @return The file
     */
    private File getRecentUrisFile()
    {
        return Paths.get(System.getProperty("java.io.tmpdir"), 
            this.getClass().getSimpleName()+"_recentUris.txt").toFile();
    }
    
    /**
     * Read the {@link #recentUris} from the temporary file
     */
    private void readRecentUris()
    {
        recentUris.clear();
        File recentUrisFile = getRecentUrisFile();
        if (recentUrisFile.exists())
        {
            try
            {
                List<String> uriStrings = 
                    Files.readAllLines(recentUrisFile.toPath());
                for (String uriString : uriStrings)
                {
                    try
                    {
                        URI uri = new URI(uriString);
                        recentUris.add(uri);
                    } 
                    catch (URISyntaxException e)
                    {
                        // Should never happen here, unless the user
                        // messed around in the file manually...
                        logger.warning("Invalid URI string: " + uriString);
                    }
                }
            }
            catch (IOException e)
            {
                logger.warning(
                    "Could not read recent URIs: " + e.getMessage());
            }
        }
    }
    
    /**
     * Update the recently used URIs with the given URI. This will update
     * the list, the file, and the menu.
     * 
     * @param uri The most recent URI
     */
    private void updateRecentUris(URI uri)
    {
        recentUris.remove(uri);
        recentUris.addFirst(uri);
        while (recentUris.size() > NUM_RECENT_URI_ENTRIES)
        {
            recentUris.removeLast();
        }
        writeRecentUris();
        updateRecentUrisMenu();
    }
    
    /**
     * Write the current {@link #recentUris} to the recent URIs file
     */
    private void writeRecentUris()
    {
        File recentUrisFile = getRecentUrisFile();
        try (BufferedWriter bufferedWriter = 
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(recentUrisFile))))
        {
            for (URI uri : recentUris)
            {
                bufferedWriter.write(uri.toString()+System.lineSeparator());
            }
        } 
        catch (IOException e)
        {
            logger.warning(
                "Could not write recent URIs: " + e.getMessage());
        }
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
     * Create the conversions menu
     * 
     * @return The conversions menu
     */
    private JMenu createConvertMenu()
    {
        JMenu convertMenu = new JMenu("Convert");
        convertMenu.add(new JMenuItem(convertToEmbeddedGltfAction));
        convertMenu.add(new JMenuItem(convertToBinaryGltfAction));
        return convertMenu;
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
        updateRecentUris(uri);
        GltfLoadingWorker gltfLoadingWorker = 
            new GltfLoadingWorker(this, frame, uri);
        gltfLoadingWorker.load();
    }
    
    
    /**
     * Open the file chooser to select a file which will be 
     * imported upon confirmation
     */
    private void importFile()
    {
        int returnState = importFileChooser.showOpenDialog(frame);
        if (returnState == JFileChooser.APPROVE_OPTION) 
        {
            File file = importFileChooser.getSelectedFile();
            importUriInBackground(file.toURI());
        }        
    }
    
    /**
     * Import the data from the specified URI in a background thread
     *  
     * @param uri The URI
     */
    private void importUriInBackground(URI uri)
    {
        // TODO Improve this crude "file type detection"...  
        if (uri.toString().toLowerCase().endsWith(".obj"))
        {
            SwingTask<GltfData, ?> swingTask = new SwingTask<GltfData, Void>()
            {
                @Override
                protected GltfData doInBackground() throws Exception
                {
                    return new ObjGltfDataCreator().create(uri);
                }
                
                @Override
                protected void done()
                {
                    try
                    {
                        GltfData gltfData = get();
                        String frameTitle =
                            "glTF for " + IO.extractFileName(uri);
                        createGltfBrowserPanel(frameTitle, gltfData);
                    } 
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                    catch (ExecutionException e)
                    {
                        JOptionPane.showMessageDialog(frame, 
                            "Could not read " + uri + ": " + e.getMessage(), 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            SwingTaskExecutors.create(swingTask)
                .build()
                .execute();            
        }
        else
        {
            logger.warning("Unknown file format: " + uri);
        }
    }

    /**
     * Create the {@link GltfBrowserPanel} for the given {@link GltfData}, 
     * and add it (in an internal frame with the given string as its title)
     * to the desktop pane
     * 
     * @param frameTitle The internal frame title
     * @param gltfData The {@link GltfData}
     */
    void createGltfBrowserPanel(String frameTitle, GltfData gltfData)
    {
        final boolean resizable = true;
        final boolean closable = true;
        final boolean maximizable = true;
        final boolean iconifiable = true;
        JInternalFrame internalFrame = new JInternalFrame(
            frameTitle, resizable, closable , maximizable, iconifiable);
        internalFrame.addPropertyChangeListener(selectedInternalFrameTracker);
        Component gltfBrowserPanel = new GltfBrowserPanel(gltfData);
        internalFrame.getContentPane().add(gltfBrowserPanel);
        internalFrame.setSize(
            desktopPane.getWidth(), 
            desktopPane.getHeight());
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
     * Returns the {@link GltfData} of the {@link GltfBrowserPanel} of 
     * the currently selected, or <code>null</code> if there is no
     * selected {@link GltfData}
     * 
     * @return The selected {@link GltfData}, or <code>null</code>
     */
    private GltfData getSelectedGltfData()
    {
        if (selectedInternalFrame == null)
        {
            return null;
        }
        Component component = 
            selectedInternalFrame.getContentPane().getComponent(0);
        if (!(component instanceof GltfBrowserPanel))
        {
            logger.severe("Unexpected type in internal frame: " + 
                component.getClass());
            return null;
        }
        GltfBrowserPanel gltfBrowserPanel = (GltfBrowserPanel)component;
        GltfData gltfData = gltfBrowserPanel.getGltfData();
        return gltfData;
    }

    
    /**
     * Convert the {@link GltfData} of the {@link GltfBrowserPanel} of 
     * the currently selected internal frame into an embedded glTF,
     * and open the result in a new frame
     */
    private void convertToEmbeddedGltf()
    {
        GltfData gltfData = getSelectedGltfData();
        if (gltfData == null)
        {
            return;
        }
        GltfData convertedGltfData = 
            new GltfDataToEmbeddedConverter().convert(gltfData);
        createGltfBrowserPanel(
            "Embedded of " + selectedInternalFrame.getTitle(), 
            convertedGltfData);
    }
    

    /**
     * Convert the {@link GltfData} of the {@link GltfBrowserPanel} of 
     * the currently selected internal frame into a binary glTF,
     * and open the result in a new frame
     */
    private void convertToBinaryGltf()
    {
        GltfData gltfData = getSelectedGltfData();
        if (gltfData == null)
        {
            return;
        }
        GltfData convertedGltfData = 
            new GltfDataToBinaryConverter().convert(gltfData);
        createGltfBrowserPanel(
            "Binary of " + selectedInternalFrame.getTitle(), 
            convertedGltfData);
    }
    
    /**
     * Open a file chooser to let the user select the file that the
     * {@link GltfData} of the {@link GltfBrowserPanel} of the currently 
     * selected internal frame should be written to.
     */
    void saveAs()
    {
        int returnState = saveFileChooser.showSaveDialog(frame);
        if (returnState == JFileChooser.APPROVE_OPTION) 
        {
            File file = saveFileChooser.getSelectedFile();
            if (file.exists())
            {
                int confirmState = 
                    JOptionPane.showConfirmDialog(frame, 
                        "File already exists, do you want to overwrite it?", 
                        "Confirm", JOptionPane.YES_NO_OPTION, 
                        JOptionPane.WARNING_MESSAGE);
                if (confirmState != JOptionPane.YES_OPTION)
                {
                    saveAs();
                }
            }
            
            // TODO Could check the buffer/shader/image URIs here, 
            // to see whether files would *really* be overridden.
            // Also, this check does not cover the case of relative
            // paths (subdirectories in URIs) where the files may
            // be stored.
            File siblingFiles[] = file.getParentFile().listFiles();
            if (siblingFiles.length != 0)
            {
                int confirmState = 
                    JOptionPane.showConfirmDialog(frame, 
                        "The directory of the target file is not empty. " + 
                        "Existing files may be overwritten. Do you want " +
                        "to continue?", "Confirm", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirmState != JOptionPane.YES_OPTION)
                {
                    saveAs();
                }
            }
            save(file);
        }        
    }
    
    
    /**
     * Save the currently selected {@link GltfData} to the given file
     * 
     * @param file The target file
     */
    private void save(File file)
    {
        GltfData gltfData = getSelectedGltfData();
        if (gltfData == null)
        {
            return;
        }
        try
        {
            new GltfDataWriter().writeGltfData(gltfData, file.getAbsolutePath());
        } 
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(frame, 
                "Could not save file to " + file + ": " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    /**
     * Open a file chooser to let the user select the file that the
     * {@link GltfData} of the {@link GltfBrowserPanel} of the currently 
     * selected internal frame should be written to as a binary file
     */
    void saveAsBinary()
    {
        int returnState = saveBinaryFileChooser.showSaveDialog(frame);
        if (returnState == JFileChooser.APPROVE_OPTION) 
        {
            File file = saveBinaryFileChooser.getSelectedFile();
            if (file.exists())
            {
                int confirmState = 
                    JOptionPane.showConfirmDialog(frame, 
                        "File already exists, do you want to overwrite it?", 
                        "Confirm", JOptionPane.YES_NO_OPTION, 
                        JOptionPane.WARNING_MESSAGE);
                if (confirmState != JOptionPane.YES_OPTION)
                {
                    saveAsBinary();
                }
            }
            saveBinary(file);
        }        
    }

    /**
     * Save the currently selected {@link GltfData} to the given file
     * as a binary glTF
     * 
     * @param file The target file
     */
    private void saveBinary(File file)
    {
        GltfData gltfData = getSelectedGltfData();
        if (gltfData == null)
        {
            return;
        }
        try
        {
            new BinaryGltfDataWriter().writeBinaryGltfData(
                gltfData, file.getAbsolutePath());
        } 
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(frame, 
                "Could not save file to " + file + ": " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
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