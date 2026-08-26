/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.asset.creator.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * From https://github.com/javagl/Combinatorics. (Yes, it's just a Cartesian 
 * product...)
 * 
 * A class providing an iterator over all combinations of a certain number
 * of elements, where the valid ranges may be different for each element
 * of the combination. For a set S = { S0, S1, ... Sn } there will be
 * |S0| * |S1| * ... * |Sn| possible combinations. Example:<br />
 * <pre>
 * S0 = {A,B,C}, |S0| = 3
 * S1 = {D,E}  , |S1| = 2
 * S2 = {A,E}  , |S2| = 2
 * S = { S0, S1, S2 }
 * m = |S0| * |S1| * |S0| = 3 * 2 * 2 = 12 combinations
 * 
 * Combinations:
 * [A, D, A]
 * [A, D, E]
 * [A, E, A]
 * [A, E, E]
 * [B, D, A]
 * [B, D, E]
 * [B, E, A]
 * [B, E, E]
 * [C, D, A]
 * [C, D, E]
 * [C, E, A]
 * [C, E, E]
 * </pre>
 *  
 * @param <T> The type of the elements
 */
class MixedRangeCombinationIterable<T> implements Iterable<List<T>>
{
    /**
     * The input elements
     */
    private List<? extends Collection<? extends T>> sets;
    
    /**
     * The total number of elements that the iterator will provide
     */
    private final int numElements;
 
    /**
     * Creates an iterable over all combinations of one element
     * of each of the given sets.
     *  
     * @param sets The input sets
     */
    public MixedRangeCombinationIterable(
        List<? extends Collection<? extends T>> sets)
    {
        this.sets = sets;
        int m = 0;
        if (sets.size() > 0)
        {
            m = 1;
        }
        for (Collection<? extends T> set : sets)
        {
            m *= set.size();
        }
        this.numElements = m;
    }
 
    @Override
    public Iterator<List<T>> iterator()
    {
        return new Iterator<List<T>>()
        {
            /**
             * The element counter
             */
            private int current = 0;

            /**
             * The current combination
             */
            private List<T> currentCombination = new ArrayList<T>();
            
            /**
             * The iterators over the individual sets
             */
            private List<Iterator<? extends T>> subIterators = 
                new ArrayList<Iterator<? extends T>>(
                    Collections.<Iterator<? extends T>>nCopies(
                        sets.size(), null));

            // Initialize the sub-iterators and the first combination
            {
                if (numElements > 0)
                {
                    for (int i=0; i<sets.size(); i++)
                    {
                        Iterator<? extends T> subIterator = 
                            sets.get(i).iterator();
                        subIterators.set(i, subIterator);
                        currentCombination.add(subIterator.next());
                    }
                }
            }
            
            @Override
            public boolean hasNext()
            {
                return current < numElements;
            }
 
            @Override
            public List<T> next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException("No more elements");
                }
                
                List<T> result = new ArrayList<T>(currentCombination);
                increase(sets.size()-1);
                current++;
                return result;
            }
 
            /**
             * Increases the selection of elements by one.
             * 
             * @param index The index to increase
             */
            private void increase(int index)
            {
                if (index < 0)
                {
                    return;
                }
                Iterator<? extends T> subIterator = subIterators.get(index);
                if (subIterator.hasNext())
                {
                    currentCombination.set(index, subIterator.next());
                }
                else
                {
                    subIterator = sets.get(index).iterator();
                    subIterators.set(index, subIterator);
                    currentCombination.set(index, subIterator.next());
                    increase(index-1);
                }
                
            }
 
            @Override
           public void remove()
            {
                throw new UnsupportedOperationException(
                    "May not remove elements from a combination");
            }
        };
    }
}
