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
package de.javagl.jgltf.browser.io;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * Methods to create {@link ProgressInputStream} instances
 */
class ProgressInputStreams
{
    /**
     * Creates an input stream that wraps the given input stream, and informs
     * the given consumer about the number of bytes that have been read from 
     * the returned stream.
     * 
     * @param inputStream The delegate stream
     * @param totalNumBytesReadConsumer A consumer for the total
     * number of bytes that have been read from the input.
     * @return The input stream
     */
    static InputStream createProgressInputStream(
        InputStream inputStream, LongConsumer totalNumBytesReadConsumer) 
    {
        Objects.requireNonNull(totalNumBytesReadConsumer, 
            "The totalNumBytesReadConsumer may not be null");
        ProgressInputStream progressInputStream = 
            new ProgressInputStream(inputStream);
        PropertyChangeListener listener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent event)
            {
                if (event.getPropertyName().equals("totalNumBytesRead"))
                {
                    Object newValue = event.getNewValue();
                    Number number = (Number)newValue;
                    totalNumBytesReadConsumer.accept(
                        number.longValue());
                }
            }
        };
        progressInputStream.addPropertyChangeListener(listener);
        return progressInputStream;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ProgressInputStreams()
    {
        // Private constructor to prevent instantiation
    }
}
