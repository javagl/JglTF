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
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.javagl.swing.tasks.SwingTaskView;
import de.javagl.swing.tasks.executors.ExecutorObserver;
import de.javagl.swing.tasks.executors.ObservableExecutorPanel;
import de.javagl.swing.tasks.executors.ObservableExecutorService;
import de.javagl.swing.tasks.executors.TaskViewHandlers;
import de.javagl.swing.tasks.executors.TaskViewListCellRenderers;

/**
 * A panel that will be used as the accessory component of the 
 * {@link SwingTaskView} that will be shown while the
 * {@link GltfLoadingWorker} does its work. It will show the
 * progress of the loading tasks, and possible error messages.
 */
class GltfLoaderPanel extends JPanel
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = -5837299242789042944L;

    /**
     * The text area showing status messages and loading errors
     */
    private final JTextArea statusTextArea;
    
    /**
     * Creates a new instance that shows the progress of the given
     * {@link ObservableExecutorService}
     *  
     * @param observableExecutorService The {@link ObservableExecutorService}
     */
    GltfLoaderPanel(ObservableExecutorService observableExecutorService)
    {
        super(new BorderLayout());
        setBorder(
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
        add(observableExecutorPanel, BorderLayout.CENTER);
        
        // Create a text area that will show possible exception stack
        // traces and error messages 
        statusTextArea = new JTextArea();
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
        
        JScrollPane statusScrollPane = new JScrollPane(statusTextArea);
        statusScrollPane.setPreferredSize(new Dimension(600, 200));
        JPanel statusPanel = new JPanel(new GridLayout(1,1));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Messages:"));
        statusPanel.add(statusScrollPane);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Append the given message to the message text area. This may be called
     * on any thread, and will put the work to append the text on the
     * Event Dispatch Thread
     * 
     * @param message The message to append
     */
    void appendMessage(String message)
    {
        SwingUtilities.invokeLater(() ->
            statusTextArea.append(message));
    }
}
