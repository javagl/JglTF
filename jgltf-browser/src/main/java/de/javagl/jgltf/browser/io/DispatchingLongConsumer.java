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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.LongConsumer;

/**
 * A <code>LongConsumer</code> that dispatches the consumed values to
 * other <code>LongConsumer</code>s
 */
class DispatchingLongConsumer implements LongConsumer
{
    /**
     * The list of consumers
     */
    private final List<LongConsumer> consumers;
    
    /**
     * Default constructor
     */
    DispatchingLongConsumer()
    {
        this.consumers = new CopyOnWriteArrayList<LongConsumer>();
    }
    
    /**
     * Add the given consumer to this consumer
     * 
     * @param consumer The consumer
     */
    void addConsumer(LongConsumer consumer)
    {
        Objects.requireNonNull(consumer, "The consumer may not be null");
        consumers.add(consumer);
    }
    
    /**
     * Remove the given consumer from this consumer
     * 
     * @param consumer The consumer
     */
    void removeConsumer(LongConsumer consumer)
    {
        consumers.remove(consumer);
    }
    
    @Override
    public void accept(long value)
    {
        for (LongConsumer consumer : consumers)
        {
            consumer.accept(value);
        }
    }
}