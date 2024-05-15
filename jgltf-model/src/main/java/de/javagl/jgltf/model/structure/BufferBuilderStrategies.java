/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.structure;

/**
 * Methods for creating {@link BufferBuilderStrategy} instances.<br>
 * <br>
 * This class is only supposed to be used internally, and not part of
 * the public API!
 */
public class BufferBuilderStrategies
{
    /**
     * Create an unspecified default {@link BufferBuilderStrategy}
     * 
     * @return The {@link BufferBuilderStrategy}
     */
    public static BufferBuilderStrategy createDefault() 
    {
        DefaultBufferBuilderStrategy.Config config = 
            new DefaultBufferBuilderStrategy.Config();
        return new DefaultBufferBuilderStrategy(config);
    }
    
    /**
     * Create a default {@link BufferBuilderStrategy} with the given 
     * configuration.
     * 
     * @param config The configuration
     * @return The {@link BufferBuilderStrategy}
     */
    static BufferBuilderStrategy create(
        DefaultBufferBuilderStrategy.Config config) 
    {
        return new DefaultBufferBuilderStrategy(config);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private BufferBuilderStrategies()
    {
        // Private constructor to prevent instantiation
    }
    
    
}
