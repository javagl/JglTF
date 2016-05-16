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
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
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
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfDataLoader;
import de.javagl.jgltf.model.JsonError;

/**
 * The main glTF browser application class, containing the main frame 
 * and the top-level UI components  
 */
class GltfBrowserApplication
{
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
        frame.setJMenuBar(menuBar);
        
        openFileChooser = new JFileChooser(".");
        openFileChooser.setFileFilter(
            new FileNameExtensionFilter("glTF Files", "gltf", "glb"));
        
        desktopPane = new JDesktopPane();
        frame.getContentPane().add(desktopPane);
        
        frame.setSize(1024, 768);
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
        String uriString = (String)optionPane.getInputValue();
        if (uriString == null)
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
     * Execute {@link GltfDataLoader#load(URI, java.util.function.Consumer)} 
     * in a background thread, showing a modal dialog. When the data is 
     * loaded, it will be passed to 
     * {@link #createGltfBrowserPanel(String, GltfData)}
     * 
     * @param uri The URI to load from
     */
    void openUriInBackground(URI uri)
    {
        // The Swing worker that executes the loader in a background thread
        class Worker extends SwingWorker<GltfData, Object>
        {
            /**
             * The list of JsonError instances that have occurred during 
             * parsing
             */
            private final List<JsonError> jsonErrors = 
                new ArrayList<JsonError>();
            
            @Override
            public GltfData doInBackground() throws IOException
            {
                return GltfDataLoader.load(uri, e -> jsonErrors.add(e));
            }

            @Override
            protected void done()
            {
                try
                {
                    GltfData gltfData = get();
                    createGltfBrowserPanel(uri.toString(), gltfData);
                } 
                catch (Exception e)
                {
                    e.printStackTrace();

                    StringBuilder sb = new StringBuilder();
                    sb.append("Loading error: " + e.getMessage());
                    if (!jsonErrors.isEmpty())
                    {
                        sb.append("\n");
                        sb.append("JSON errors:\n");
                        sb.append(createString(jsonErrors));
                    }
                    JOptionPane.showMessageDialog(frame,
                        sb.toString(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!jsonErrors.isEmpty())
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("JSON errors:\n");
                    sb.append(createString(jsonErrors));
                    JOptionPane.showMessageDialog(frame,
                        sb.toString(), "Warning",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
            
            /**
             * Create a string representation of the given errors
             * 
             * @param jsonErrors The JsonErrors
             * @return The string
             */
            String createString(Iterable<? extends JsonError> jsonErrors)
            {
                StringBuffer sb = new StringBuffer();
                int counter = 0;
                for (JsonError jsonError : jsonErrors)
                {
                    sb.append(String.valueOf(counter)+ ".:" + 
                        jsonError.getMessage() + ", JSON path: " + 
                        jsonError.getJsonPathString() + "\n");
                    counter++;
                }
                return sb.toString();
            }
            
        }
        
        // A PropertyChangeListener that hides the (modal) dialog
        // when the worker is done
        class WorkerCompletionWaiter implements PropertyChangeListener 
        {
            /**
             * The dialog that will be shown while loading
             */
            private final JDialog dialog;

            /**
             * Create the new work completion waiter
             * 
             * @param dialog The dialog that will be shown while loading
             */
            WorkerCompletionWaiter(JDialog dialog) 
            {
                this.dialog = dialog;
            }

            @Override
            public void propertyChange(PropertyChangeEvent event) 
            {
                if ("state".equals(event.getPropertyName()) && 
                    SwingWorker.StateValue.DONE == event.getNewValue()) 
                {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        }
        Worker worker = new Worker();
        JDialog dialog = new JDialog(frame, true);
        
        // Try to obtain the size of the input file
        String contentLengthString = "";
        try
        {
            URLConnection connection = uri.toURL().openConnection();
            int contentLength = connection.getContentLength();
            if (contentLength > 0)
            {
                contentLengthString = String.valueOf(contentLength)+" bytes ";
            }
        }
        catch (IOException e)
        {
            // Ignored
        }
        
        JLabel label = new JLabel(
            "Loading " + contentLengthString + "from " + uri,
            SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialog.getContentPane().add(label);
        worker.addPropertyChangeListener(new WorkerCompletionWaiter(dialog));
        worker.execute();
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
    
    /**
     * Create the {@link GltfBrowserPanel} for the given {@link GltfData}, 
     * and add it (in an internal frame with the given string as its title)
     * to the desktop pane
     * 
     * @param uriString The URI string, used as the internal frame title
     * @param gltfData The {@link GltfData}
     */
    private void createGltfBrowserPanel(String uriString, GltfData gltfData)
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

    
}