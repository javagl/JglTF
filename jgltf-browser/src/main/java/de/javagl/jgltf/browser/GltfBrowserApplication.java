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
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
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
import javax.swing.MenuElement;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.GltfModels;
import de.javagl.jgltf.model.io.GltfModelWriter;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.obj.BufferStrategy;
import de.javagl.jgltf.obj.v2.ObjGltfAssetCreatorV2;
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
     * The Action for importing an OBJ file
     * 
     * @see #importObjFile()
     */
    private final Action importObjFileAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 6442563220376147803L;


        // Initialization
        {
            putValue(NAME, "Import OBJ file...");
            putValue(SHORT_DESCRIPTION, "Import an OBJ file");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_I));
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            importObjFile();
        }
    };
    
    /**
     * The Action for saving a glTF
     * 
     * @see #saveAs(Consumer)
     * @see #saveChecked(File)
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
            saveAs(f -> saveChecked(f));
        }
    };
    
    /**
     * The Action for saving a glTF as a binary glTF
     * 
     * @see #saveAs(Consumer)
     * @see #saveBinary(File)
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
            saveAs(f -> saveBinary(f));
        }
    };
    
    /**
     * The Action for saving a glTF as an embedded glTF
     * 
     * @see #saveAs(Consumer)
     * @see #saveEmbedded(File)
     */
    private final Action saveAsEmbeddedAction = new AbstractAction()
    {
        /**
         * Serial UID
         */
        private static final long serialVersionUID = 8683961991846940413L;

        // Initialization
        {
            putValue(NAME, "Save as embedded...");
            putValue(SHORT_DESCRIPTION, 
                "Save the selected glTF to an embedded glTF");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
            setEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            saveAs(f -> saveEmbedded(f));
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
     * The menu bar of the application
     */
    private final JMenuBar menuBar;
    
    /**
     * The FileChooser for opening glTF files
     */
    private final JFileChooser openFileChooser;

    /**
     * The FileChooser for importing OBJ files
     */
    private final JFileChooser importObjFileChooser;
    
    /**
     * The FileChooser for saving glTF files
     */
    private final JFileChooser saveFileChooser;

    /**
     * The {@link RecentUrisMenu} handler for the most recent URIs menu
     */
    private final RecentUrisMenu recentUrisMenu;
    
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
     * The {@link ObjImportAccessoryPanel}
     */
    private final ObjImportAccessoryPanel objImportAccessoryPanel;
    
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
                        saveAsAction.setEnabled(true);
                        saveAsBinaryAction.setEnabled(true);
                        saveAsEmbeddedAction.setEnabled(true);
                    }
                }
            }
        };
    
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

        recentUrisMenu = new RecentUrisMenu(
            GltfBrowserApplication.class.getSimpleName(),
            uri -> openUriInBackground(uri));
        
        menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        frame.setJMenuBar(menuBar);
        
        openFileChooser = new JFileChooser(".");
        openFileChooser.setFileFilter(
            new FileNameExtensionFilter(
                "glTF Files (.gltf, .glb)", "gltf", "glb"));
        
        importObjFileChooser = new JFileChooser(".");
        
        objImportAccessoryPanel = new ObjImportAccessoryPanel();
        importObjFileChooser.setAccessory(objImportAccessoryPanel);
        importObjFileChooser.setFileFilter(
            new FileNameExtensionFilter(
                "OBJ files (.obj)", "obj"));
        
        saveFileChooser = new JFileChooser(".");
        
        desktopPane = new JDesktopPane();
        frame.getContentPane().add(desktopPane);
        
        frame.setSize(1000,700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    
    /**
     * Returns an action listener that may will interpret the action 
     * command as a URI that should be loaded in a background thread
     * 
     * @return The action listener
     */
    private ActionListener getSampleModelsMenuActionListener()
    {
        ActionListener actionListener = event ->
        {
            String actionCommand = event.getActionCommand();
            URI uri = null;
            try
            {
                uri = new URI(actionCommand);
            } 
            catch (URISyntaxException e)
            {
                JOptionPane.showMessageDialog(
                    frame, "Invalid URI: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            openUriInBackground(uri);
        };
        return actionListener;
    }
    
    /**
     * Add the given sample model menus to the menu bar of this application
     * 
     * @param menus The menus
     */
    void addSampleModelMenus(Iterable<? extends JMenu> menus)
    {
        ActionListener actionListener = getSampleModelsMenuActionListener();
        for (JMenu menu : menus)
        {
            attachActionListener(menu, actionListener);
            menuBar.add(menu);
        }
    }
    
    /**
     * Recursively attach the given action listener to all items in the
     * given menu that have a non-<code>null</code> action command
     *  
     * @param menuElement The menu element
     * @param actionListener The action listener
     */
    private static void attachActionListener(
        MenuElement menuElement, ActionListener actionListener)
    {
        if (menuElement instanceof JMenuItem)
        {
            JMenuItem menuItem = (JMenuItem)menuElement;
            if (menuItem.getActionCommand() != null)
            {
                menuItem.addActionListener(actionListener);
            }
        }
        MenuElement[] subElements = menuElement.getSubElements();
        for (MenuElement subElement : subElements)
        {
            attachActionListener(subElement, actionListener);
        }
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

        fileMenu.add(recentUrisMenu.getMenu());
        fileMenu.add(new JSeparator());

        fileMenu.add(new JMenuItem(importObjFileAction));
        fileMenu.add(new JSeparator());
        
        fileMenu.add(new JMenuItem(saveAsAction));
        fileMenu.add(new JMenuItem(saveAsBinaryAction));
        fileMenu.add(new JMenuItem(saveAsEmbeddedAction));
        fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem(exitAction));
        return fileMenu;
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
     * Execute the task of loading the {@link GltfModel} in a background 
     * thread, showing a modal dialog. When the data is loaded, it will 
     * be passed to {@link #createGltfBrowserPanel(String, GltfModel, Object)}
     * 
     * @param uri The URI to load from
     */
    void openUriInBackground(URI uri)
    {
        logger.info("Loading " + uri);
        
        recentUrisMenu.update(uri);
        
        GltfLoadingWorker gltfLoadingWorker = 
            new GltfLoadingWorker(this, frame, uri);
        gltfLoadingWorker.load();
    }
    
    
    /**
     * Open the file chooser to select an OBJ file which will be 
     * imported upon confirmation
     */
    private void importObjFile()
    {
        int returnState = importObjFileChooser.showOpenDialog(frame);
        if (returnState == JFileChooser.APPROVE_OPTION) 
        {
            File file = importObjFileChooser.getSelectedFile();
            importObjUriInBackground(file.toURI());
        }        
    }
    
    /**
     * Import the data from the specified OBJ URI in a background thread
     *  
     * @param uri The URI
     */
    private void importObjUriInBackground(URI uri)
    {
        BufferStrategy bufferStrategy = 
            objImportAccessoryPanel.getSelectedBufferStrategy();
        Integer indicesComponentType = 
            objImportAccessoryPanel.getSelectedIndicesComponentType();
        boolean assigningRandomColorsToParts =
            objImportAccessoryPanel.isAssigningRandomColorsToParts();
        SwingTask<Entry<GltfModel, Object>, ?> swingTask = 
            new SwingTask<Entry<GltfModel, Object>, Void>()
        {
            @Override
            protected Entry<GltfModel, Object> doInBackground() throws Exception
            {
                ObjGltfAssetCreatorV2 objGltfAssetCreator = 
                    new ObjGltfAssetCreatorV2(bufferStrategy);
                objGltfAssetCreator.setIndicesComponentType(
                    indicesComponentType);
                objGltfAssetCreator.setAssigningRandomColorsToParts(
                    assigningRandomColorsToParts);
                GltfAssetV2 gltfAsset = objGltfAssetCreator.create(uri);

                GltfModel gltfModel = GltfModels.create(gltfAsset);
                Object gltf = gltfAsset.getGltf();
                Entry<GltfModel, Object> result = 
                    new SimpleEntry<GltfModel, Object>(gltfModel, gltf);
                return result;
                
            }
        };
        swingTask.addDoneCallback(task -> 
        {
            try
            {
                Entry<GltfModel, Object> result = task.get();
                GltfModel gltfModel = result.getKey();
                Object gltf = result.getValue();
                String frameTitle =
                    "glTF for " + IO.extractFileName(uri);
                createGltfBrowserPanel(frameTitle, gltfModel, gltf);
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
        });
        SwingTaskExecutors.create(swingTask)
            .build()
            .execute();            
    }

    /**
     * Create the {@link GltfBrowserPanel} for the given {@link GltfModel}, 
     * and add it (in an internal frame with the given string as its title)
     * to the desktop pane
     * 
     * @param frameTitle The internal frame title
     * @param gltfModel The {@link GltfModel}
     * @param gltf The glTF object
     */
    void createGltfBrowserPanel(
        String frameTitle, GltfModel gltfModel, Object gltf)
    {
        final boolean resizable = true;
        final boolean closable = true;
        final boolean maximizable = true;
        final boolean iconifiable = true;
        JInternalFrame internalFrame = new JInternalFrame(
            frameTitle, resizable, closable , maximizable, iconifiable);
        internalFrame.addPropertyChangeListener(selectedInternalFrameTracker);
        GltfBrowserPanel gltfBrowserPanel = 
            new GltfBrowserPanel(gltfModel, gltf);
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
        
        internalFrame.addInternalFrameListener(new InternalFrameAdapter()
        {
            @Override
            public void internalFrameClosed(InternalFrameEvent e)
            {
                gltfBrowserPanel.disposeGltfViewer();
            }
        });
    }
    
    /**
     * Returns the {@link GltfModel} of the {@link GltfBrowserPanel} of 
     * the currently selected, or <code>null</code> if there is no
     * selected {@link GltfModel}
     * 
     * @return The selected {@link GltfModel}, or <code>null</code>
     */
    private GltfModel getSelectedGltfModel()
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
        GltfModel gltfModel =  gltfBrowserPanel.getGltfModel();
        return gltfModel;
    }

    
    /**
     * Open a file chooser to let the user select the file that the
     * {@link GltfModel} of the {@link GltfBrowserPanel} of the currently 
     * selected internal frame should be written to. If the file
     * already exists, the user will be asked for confirmation to
     * overwrite the file.
     * 
     * @param fileConsumer The consumer for the selected file
     */
    private void saveAs(Consumer<? super File> fileConsumer)
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
                    // The file should NOT be overwritten. 
                    // Let the user select a new one.
                    saveAs(fileConsumer);
                    return;
                }
            }
            fileConsumer.accept(file);
        }        
    }
    
    
    
    /**
     * Save the currently selected {@link GltfModel} to the given file. 
     * If the target directory is not empty, then the user will be 
     * asked for confirmation, because existing files might be 
     * overwritten.
     * 
     * @param file The target file
     */
    private void saveChecked(File file)
    {
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
                saveAs(f -> saveChecked(f));
            }
        }
        save(file);
    }
    
    /**
     * Save the currently selected {@link GltfModel} to the given file
     * 
     * @param file The target file
     */
    private void save(File file)
    {
        GltfModel gltfModel = getSelectedGltfModel();
        if (gltfModel == null)
        {
            return;
        }
        try
        {
            GltfModelWriter gltfModelWriter = new GltfModelWriter();
            gltfModelWriter.write(gltfModel, file);
        } 
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(frame, 
                "Could not save file to " + file + ": " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Save the currently selected {@link GltfModel} to the given file
     * as a binary glTF
     * 
     * @param file The target file
     */
    private void saveBinary(File file)
    {
        GltfModel gltfModel = getSelectedGltfModel();
        if (gltfModel == null)
        {
            return;
        }
        try
        {
            GltfModelWriter gltfModelWriter = new GltfModelWriter();
            gltfModelWriter.writeBinary(gltfModel, file);
        } 
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(frame, 
                "Could not save file to " + file + ": " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Save the currently selected {@link GltfModel} to the given file
     * as a embedded glTF
     * 
     * @param file The target file
     */
    private void saveEmbedded(File file)
    {
        GltfModel gltfModel = getSelectedGltfModel();
        if (gltfModel == null)
        {
            return;
        }
        try
        {
            GltfModelWriter gltfModelWriter = new GltfModelWriter();
            gltfModelWriter.writeEmbedded(gltfModel, file);
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

   
}