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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Package-private class for handling the menu entry containing the 
 * most recently used URIs 
 */
class RecentUrisMenu
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(RecentUrisMenu.class.getName());

    /**
     * The number of recently used entries
     */
    private static final int NUM_RECENT_URI_ENTRIES = 8;
    
    /**
     * The menu showing the most recently used URIs
     */
    private final JMenu recentUrisMenu;
    
    /**
     * The recent URIs
     */
    private final Deque<URI> recentUris;
    
    /**
     * A prefix for the file storing the recent URIs, for disambiguation
     */
    private final String fileNamePrefix;
    
    /**
     * The consumer that will receive the URIs that are selected in the menu
     */
    private final Consumer<URI> uriConsumer;
    
    /**
     * Default constructor
     * 
     * @param fileNamePrefix A prefix for the file storing the recent URIs
     * @param uriConsumer The consumer that will receive the URI when one
     * of the menu entries was selected
     */
    RecentUrisMenu(String fileNamePrefix, Consumer<URI> uriConsumer)
    {
        this.fileNamePrefix = fileNamePrefix;
        this.uriConsumer = Objects.requireNonNull(
            uriConsumer, "The uriConsumer may not be null");
        this.recentUris = new LinkedList<URI>();
        this.recentUrisMenu = new JMenu("Recent");
        updateRecentUrisMenu();
    }

    /**
     * Returns the file that stores the recently used URIs
     * 
     * @return The file
     */
    private File getRecentUrisFile()
    {
        return Paths.get(System.getProperty("java.io.tmpdir"), 
            fileNamePrefix + "_recentUris.txt").toFile();
    }
    
    /**
     * Returns the menu that shows the recent URIs
     * 
     * @return the menu
     */
    JMenuItem getMenu()
    {
        return recentUrisMenu;
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
            menuItem.addActionListener(e -> uriConsumer.accept(uri));
            recentUrisMenu.add(menuItem);
        }
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
    void update(URI uri)
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
    
}
