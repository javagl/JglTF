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
package de.javagl.jgltf.model.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for writing byte buffers to files
 */
public class BufferWriter
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(BufferWriter.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.FINE;
    
    /**
     * Write the given byte buffer to a file with the given name. If the
     * given data is <code>null</code>, then a warning will be printed
     * and nothing will be written. If an IO error occurs, then a 
     * warning will be printed.
     * 
     * @param name The name of the data element
     * @param data The data to write
     * @param fileName The file name
     */
    public static void write(
        String name, ByteBuffer data, String fileName)
    {
        if (data == null)
        {
            logger.warning("Writing " + name + " FAILED: No  data found");
            return;
        }
        try (@SuppressWarnings("resource")
            WritableByteChannel writableByteChannel = 
            Channels.newChannel(new FileOutputStream(fileName)))
        {
            logger.log(level, "Writing " + name + " to " + fileName);
            writableByteChannel.write(data.slice());
            logger.log(level, "Writing " + name + " DONE");
        }
        catch (IOException e)
        {
            logger.warning("Writing " + name + " FAILED: " + e.getMessage());
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private BufferWriter()
    {
        // Private constructor to prevent instantiation
    }
}
