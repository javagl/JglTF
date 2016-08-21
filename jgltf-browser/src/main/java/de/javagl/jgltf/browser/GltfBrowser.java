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
import java.io.InputStream;
import java.util.logging.LogManager;

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
     * The entry point of this application
     * 
     * @param args Not used
     */
    public static void main(String[] args)
    {
        initLogging();
        setPlatformLookAndFeel();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        SwingUtilities.invokeLater(() -> new GltfBrowserApplication());
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
     * Private constructor to prevent instantiation
     */
    private GltfBrowser()
    {
        // Private constructor to prevent instantiation
    }
}
