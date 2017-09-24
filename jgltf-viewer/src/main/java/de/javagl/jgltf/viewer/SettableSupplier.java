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
package de.javagl.jgltf.viewer;

import java.util.function.Supplier;

/**
 * A supplier where the supplied value may be set
 * 
 * @param <T> The type of the value
 */
class SettableSupplier<T> implements Supplier<T>
{
    /**
     * The value 
     */
    private T value;

    /**
     * Create a new supplier that initially supplies <code>null</code>.
     */
    SettableSupplier()
    {
        this(null);
    }
    
    /**
     * Create a new supplier for the given value
     * 
     * @param value The value
     */
    private SettableSupplier(T value)
    {
        this.value = value;
    }
    
    /**
     * Set the value
     * 
     * @param value The value
     */
    void set(T value)
    {
        this.value = value;
    }
    
    @Override
    public T get()
    {
        return value;
    }
    
}