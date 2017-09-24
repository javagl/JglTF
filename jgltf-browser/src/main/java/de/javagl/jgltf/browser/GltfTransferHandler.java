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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.TransferHandler;

/**
 * Implementation of a transfer handler that allows transferring files or
 * links into the {@link GltfBrowserApplication} using drag-and-drop 
 */
class GltfTransferHandler extends TransferHandler 
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = 8076904632972988378L;
    
    /**
     * The URL data flavor
     */
    private static final DataFlavor URL_FLAVOR;
    static
    {
        DataFlavor flavor = null;
        try
        {
            flavor = new DataFlavor(
                "application/x-java-url; class=java.net.URL");
        }
        catch (ClassNotFoundException e)
        {
            // Ignored
        }
        URL_FLAVOR = flavor;
    }
    
    /**
     * The owning {@link GltfBrowserApplication}
     */
    private final GltfBrowserApplication owner;
    
    /**
     * Default constructor
     * 
     * @param owner The owning {@link GltfBrowserApplication} 
     */
    GltfTransferHandler(GltfBrowserApplication owner)
    {
        this.owner = owner;
    }
    
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) 
    {
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) &&
            !support.isDataFlavorSupported(URL_FLAVOR)) 
        {
            return false;
        }
        support.setDropAction(COPY);
        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) 
    {
        if (!canImport(support)) 
        {
            return false;
        }
        Transferable transferable = support.getTransferable();
        
        DataFlavor transferDataFlavors[] = 
            transferable.getTransferDataFlavors();
        
        int fileListIndex = Arrays.asList(transferDataFlavors)
            .indexOf(DataFlavor.javaFileListFlavor);
        if (fileListIndex == -1)
        {
            fileListIndex = Integer.MAX_VALUE;
        }
        int urlIndex = Arrays.asList(transferDataFlavors).indexOf(URL_FLAVOR);
        if (urlIndex == -1)
        {
            urlIndex = Integer.MAX_VALUE;
        }
        if (urlIndex < fileListIndex)
        {
            URL url = getUrlOptional(transferable);
            if (url != null)
            {
                try
                {
                    owner.openUriInBackground(url.toURI());
                } 
                catch (URISyntaxException e)
                {
                    return false;
                }
                return true;
            }
            return false;
        }
        
        List<File> fileList = getFileListOptional(transferable);
        if (fileList != null)
        {
            for (File file : fileList)
            {
                owner.openUriInBackground(file.toURI());
            }
            return true;
        }
        return false;
    }
    
    /**
     * Obtains the transfer data from the given Transferable as a file list,
     * or returns <code>null</code> if the data can not be obtained
     * 
     * @param transferable The transferable
     * @return The file list, or <code>null</code>
     */
    private static List<File> getFileListOptional(Transferable transferable)
    {
        try
        {
            Object transferData = 
                transferable.getTransferData(DataFlavor.javaFileListFlavor);
            @SuppressWarnings("unchecked")
            List<File> fileList = (List<File>)transferData;
            return fileList;
        } 
        catch (UnsupportedFlavorException e)
        {
            return null;
        } 
        catch (IOException e)
        {
            return null;
        }
    }
    
    /**
     * Obtains the transfer data from the given Transferable as a URL,
     * or returns <code>null</code> if the data can not be obtained
     * 
     * @param transferable The transferable
     * @return The URL, or <code>null</code>
     */
    private static URL getUrlOptional(Transferable transferable)
    {
        try
        {
            Object transferData = 
                transferable.getTransferData(URL_FLAVOR);
            URL url = (URL)transferData;
            return url;
        } 
        catch (UnsupportedFlavorException e)
        {
            return null;
        } 
        catch (IOException e)
        {
            return null;
        }

    }
    
}