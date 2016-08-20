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
package de.javagl.jgltf.obj;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import de.javagl.jgltf.impl.GlTF;

/**
 * Utility methods for {@link GlTF}s.
 */
class Gltfs
{
    /**
     * Generate an unspecified ID string with the given prefix that is not
     * yet contained in the key set of the given map
     * 
     * @param prefix The prefix for the ID string
     * @param map The map from the existing IDs. This may be <code>null</code>.
     * @return The new ID
     */
    static String generateId(
        String prefix, Map<? extends String, ?> map)
    {
        Set<? extends String> set = Collections.emptySet();
        if (map != null)
        {
            set = map.keySet();
        }
        int counter = set.size();
        while (true)
        {
            String id = prefix + counter;
            if (!set.contains(id))
            {
                return id;
            }
            counter++;
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Gltfs()
    {
        // Private constructor to prevent instantiation
    }

}