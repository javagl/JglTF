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
package de.javagl.jgltf.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that informs property change listeners about the
 * number of bytes that are read.
 */
class ProgressInputStream extends FilterInputStream {
    
    // Adapted from http://stackoverflow.com/a/1339589
    
    /**
     * The property change support
     */
    private final PropertyChangeSupport propertyChangeSupport;
    
    /**
     * The total number of bytes that have been read
     */
    private volatile long totalNumBytesRead;

    /**
     * Creates a new progress input stream that reads from the given input
     * stream
     * 
     * @param inputStream The input stream
     */
    ProgressInputStream(InputStream inputStream)
    {
        super(inputStream);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Returns the total number of bytes that already have been read
     * from the stream
     * 
     * @return The number of bytes read
     */
    long getTotalNumBytesRead()
    {
        return totalNumBytesRead;
    }

    /**
     * Add the given listener to be informed when the number of bytes
     * that have been read from this stream changes
     * 
     * @param listener The listener to add
     */
    void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove the given listener from this stream
     * 
     * @param listener The listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public int read() throws IOException
    {
        int b = super.read();
        updateProgress(1);
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        return (int) updateProgress(super.read(b));
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        return (int) updateProgress(super.read(b, off, len));
    }

    @Override
    public long skip(long n) throws IOException
    {
        return updateProgress(super.skip(n));
    }

    @Override
    public void mark(int readlimit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported()
    {
        return false;
    }

    /**
     * Update the progress of this stream, based on the given number of
     * bytes that have been read, and inform all registered listeners
     * 
     * @param numBytesRead The number of bytes that have been read 
     * @return The number of bytes read
     */
    private long updateProgress(long numBytesRead)
    {
        if (numBytesRead > 0)
        {
            long oldTotalNumBytesRead = this.totalNumBytesRead;
            this.totalNumBytesRead += numBytesRead;
            propertyChangeSupport.firePropertyChange("totalNumBytesRead",
                oldTotalNumBytesRead, this.totalNumBytesRead);
        }
        return numBytesRead;
    }
}