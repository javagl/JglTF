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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility methods for creating cartesian products
 */
public class CartesianProducts
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(CartesianProducts.class.getName());

    /**
     * A builder for cartesian products
     */
    public static class Builder
    {
        /**
         * The mapping from property names to lists of values
         */
        private final Map<String, List<Object>> valuesMap;

        /**
         * Private constructor
         */
        private Builder()
        {
            this.valuesMap = new LinkedHashMap<String, List<Object>>();
        }

        /**
         * Uses the given mapping from a name to an array of values for the
         * respective name in the cartesian product.
         * 
         * @param name The name
         * @param values The values
         * @return This builder
         */
        public Builder with(String name, Object... values)
        {
            valuesMap.put(name, Arrays.asList(values));
            return this;
        }

        /**
         * Creates a list containing maps that represent all elements of the
         * cartesian product that has been created by this builder.
         * 
         * @return The list
         */
        public List<Map<String, Object>> build()
        {
            List<Map<String, Object>> result =
                new ArrayList<Map<String, Object>>();
            List<String> keys = new ArrayList<String>(valuesMap.keySet());
            List<List<Object>> collections =
                new ArrayList<List<Object>>(valuesMap.values());
            MixedRangeCombinationIterable<Object> iterable =
                new MixedRangeCombinationIterable<Object>(collections);
            Iterator<List<Object>> iterator = iterable.iterator();
            while (iterator.hasNext())
            {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                List<Object> next = iterator.next();
                for (int i = 0; i < keys.size(); i++)
                {
                    String key = keys.get(i);
                    Object value = next.get(i);
                    map.put(key, value);
                }
                result.add(map);
            }
            return result;
        }

        /**
         * Create a list containing all elements of the cartesian product, as
         * the given type.<br>
         * <br>
         * This internally uses Jackson value conversion, and does not do any
         * sanity checks.
         * 
         * @param <T> The element type
         * @param type The type
         * @return The resulting list
         */
        public <T> List<T> buildAs(Class<T> type)
        {
            List<T> result = new ArrayList<T>();
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> maps = build();
            for (Map<String, Object> map : maps)
            {
                logger.fine("Creating " + type + " from map:");
                for (Entry<String, Object> entry : map.entrySet())
                {
                    logger.fine("  Key  : " + entry.getKey());
                    logger.fine("  Value: " + entry.getValue());
                }
                T t = mapper.convertValue(map, type);
                result.add(t);
            }
            return result;
        }
    }

    /**
     * Create a new builder for cartesian products
     * 
     * @return The builder
     */
    public static Builder create()
    {
        return new Builder();
    }

    /**
     * Read the specified file, and use it as the input for creating a list of
     * objects that represent the cartesian product that was created from the
     * input, converted to the given target type.
     * 
     * Yeah, SO many things are underspecified here....
     * 
     * @param <T> The target type
     * @param file The file
     * @param type The type
     * @return The list
     * @throws IOException If an IO error occurs
     */
    public static <T> List<T> readFrom(File file, Class<T> type)
        throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> map = mapper.readValue(file, Map.class);

        if (logger.isLoggable(Level.FINE))
        {
            logger.fine("Creating cartesian products from input:");
            for (Entry<?, ?> entry : map.entrySet())
            {
                String key = String.valueOf(entry.getKey());
                Object value = entry.getValue();
                logger.fine("Key   : " + key);
                logger.fine(
                    "Value : " + value + " (type " + value.getClass() + ")");
            }
        }

        Builder builder = CartesianProducts.create();
        for (Entry<?, ?> entry : map.entrySet())
        {
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            builder.with(key, asArray(value));
        }
        List<T> result = builder.buildAs(type);
        return result;
    }

    /**
     * If the given value is a collection, returns an array from that
     * collection. Otherwise, casts the given value to an array.
     * 
     * @param value The value
     * @return The array
     */
    private static Object[] asArray(Object value)
    {
        if (value instanceof Collection<?>)
        {
            Collection<?> collection = (Collection<?>) value;
            return collection.toArray();
        }
        return (Object[]) value;
    }

    /**
     * Write the given list to the specified file.
     * 
     * Yeah, SO many things are underspecified here....
     * 
     * @param file The file
     * @param list The list
     * @throws IOException If an IO error occurs
     */
    public static void writeTo(File file, List<?> list) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private CartesianProducts()
    {
        // Private constructor to prevent instantiation
    }

}
