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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.javagl.jgltf.browser.io.GltfDataReaderThreaded;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.io.IO;
import de.javagl.jgltf.model.io.JsonError;
import de.javagl.swing.tasks.ProgressListener;
import de.javagl.swing.tasks.SwingTask;
import de.javagl.swing.tasks.SwingTaskExecutors;
import de.javagl.swing.tasks.SwingTaskViews;
import de.javagl.swing.tasks.executors.ExecutorObserver;
import de.javagl.swing.tasks.executors.ObservableExecutorPanel;
import de.javagl.swing.tasks.executors.ObservableExecutorService;
import de.javagl.swing.tasks.executors.TaskViewHandlers;
import de.javagl.swing.tasks.executors.TaskViewListCellRenderers;

/**
 * The worker class for loading glTF data from a URI. This is a swing task
 * that performs the loading in a background thread, while showing a modal
 * dialog with progress information. 
 */
class GltfLoadingWorker extends SwingTask<GltfData, Object>
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(GltfBrowserApplication.class.getName());
    
    /**
     * The application which created this worker
     */
    private final GltfBrowserApplication owner;
    
    /**
     * The main frame of the application, used as parent for dialogs
     */
    private final JFrame frame;
    
    /**
     * The URI that the data is loaded from
     */
    private final URI uri;

    /**
     * The reader that will execute the background tasks
     */
    private final GltfDataReaderThreaded gltfDataReaderThreaded;

    /**
     * The list of JsonError instances that have occurred during 
     * parsing
     */
    private final List<JsonError> jsonErrors = new ArrayList<JsonError>();

    /**
     * Creates a new worker
     * 
     * @param owner The application which created this worker
     * @param frame The parent frame for dialogs
     * @param uri The URI to load the data from
     */
    GltfLoadingWorker(GltfBrowserApplication owner, JFrame frame, URI uri)
    {
        this.owner = owner;
        this.frame = frame;
        this.uri = uri;
        
        this.gltfDataReaderThreaded = new GltfDataReaderThreaded(-1);
    }
    
    /**
     * Load the data from the URI that was given in the constructor,
     * showing a modal dialog with progress information.
     */
    void load()
    {
        JComponent accessory = createSwingTaskViewAccessory(
            gltfDataReaderThreaded.getObservableExecutorService());
        
        SwingTaskExecutors.create(this)
            .setTitle("Loading")
            .setMillisToPopup(0)
            .setSwingTaskViewFactory(c -> 
                SwingTaskViews.create(c, accessory, false))
            .setCancelable(true)
            .build()
            .execute();
    }
        
    @Override
    protected GltfData doInBackground() throws IOException
    {
        String message = "Loading glTF from " + IO.extractFileName(uri);
        long contentLength = IO.getContentLength(uri);
        if (contentLength >= 0)
        {
            String contentLengthString = 
                NumberFormat.getNumberInstance().format(contentLength);
            message += " (" + contentLengthString + " bytes)";
        }
        setMessage(message);
        
        ProgressListener progressListener = new ProgressListener()
        {
            @Override
            public void messageChanged(String message)
            {
                setMessage(message);
            }
            @Override
            public void progressChanged(double progress)
            {
                setProgress(progress);
            }
        }; 
        gltfDataReaderThreaded.addProgressListener(progressListener);
        gltfDataReaderThreaded.setJsonErrorConsumer(
            e -> jsonErrors.add(e));
        return gltfDataReaderThreaded.readGltfData(uri);
    }

    @Override
    protected void done()
    {
        try
        {
            GltfData gltfData = get();
            owner.createGltfBrowserPanel(uri.toString(), gltfData);
        } 
        catch (CancellationException e)
        {
            logger.info("Cancelled loading " + uri + 
                " (" + e.getMessage() + ")");
            gltfDataReaderThreaded.cancel();
            //e.printStackTrace();
            return;
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
    private String createString(Iterable<? extends JsonError> jsonErrors)
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

    /**
     * Create the accessory component that displays the list of active tasks
     * in the dialog that is shown while a glTF is loaded
     * 
     * @param observableExecutorService The {@link ObservableExecutorService}
     * that is processing the loading tasks
     * @return The accessory component
     */
    private JComponent createSwingTaskViewAccessory(
        ObservableExecutorService observableExecutorService)
    {
        JComponent accessory = new JPanel(new BorderLayout());
        accessory.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 0), 
                BorderFactory.createTitledBorder("Active tasks:")));

        // Create the ObservableExecutorPanel that shows the currently
        // running tasks
        ObservableExecutorPanel observableExecutorPanel = 
            new ObservableExecutorPanel();
        observableExecutorPanel.setObservableExecutorService(
            observableExecutorService);
        observableExecutorPanel.setPreferredSize(new Dimension(600, 300));
        observableExecutorPanel.setTaskViewHandler(
            TaskViewHandlers.createDefault(false));
        observableExecutorPanel.setCellRenderer(
            TaskViewListCellRenderers.createBasic());
        accessory.add(observableExecutorPanel, BorderLayout.CENTER);
        
        // Create a text area that will show possible exception stack
        // traces of selected tasks
        JTextArea statusTextArea = new JTextArea();
        statusTextArea.setTabSize(2);
        statusTextArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        observableExecutorService.addExecutorObserver(new ExecutorObserver()
        {
            @Override
            public void tasksFinished()
            {
                // Nothing to do here
            }
            
            @Override
            public void scheduled(Runnable r)
            {
                // Nothing to do here
            }
            
            @Override
            public void beforeExecute(Thread t, Runnable r)
            {
                // Nothing to do here
            }
            
            @Override
            public void afterExecute(Runnable r, Throwable t)
            {
                if (t != null)
                {
                    StringWriter stringWriter = new StringWriter();
                    t.printStackTrace(new PrintWriter(stringWriter));
                    String string = stringWriter.toString();
                    String message = "\n=== Error in " + r + ":\n" + string;
                    SwingUtilities.invokeLater(() ->
                        statusTextArea.append(message));
                }
            }
        });
        
        /*
        observableExecutorPanel.addListSelectionListener(
            new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                JList<?> list = (JList<?>)e.getSource();
                Object value = list.getSelectedValue();
                TaskView taskView = (TaskView)value;
                Throwable throwable = taskView.getThrowable();
                if (throwable != null)
                {
                    StringWriter stringWriter = new StringWriter();
                    throwable.printStackTrace(new PrintWriter(stringWriter));
                    statusTextArea.setText(stringWriter.toString());
                }
                else
                {
                    statusTextArea.setText("");
                }
            }
        });
        */
        
        JScrollPane statusScrollPane = new JScrollPane(statusTextArea);
        statusScrollPane.setPreferredSize(new Dimension(600, 200));
        JPanel statusPanel = new JPanel(new GridLayout(1,1));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Messages:"));
        statusPanel.add(statusScrollPane);
        accessory.add(statusPanel, BorderLayout.SOUTH);
        
        return accessory;
    }
    
    
}
