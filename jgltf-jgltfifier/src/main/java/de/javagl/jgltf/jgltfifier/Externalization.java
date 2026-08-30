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
package de.javagl.jgltf.jgltfifier;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * A class handling the "externalization" of data elements. It decides whether a
 * given element should be written to an external file, and allows tracking
 * whether external files have been written.
 * 
 * @param <T> The element type
 */
class Externalization<T>
{
    /**
     * The base path that data will be written to
     */
    private final Path path;

    /**
     * The predicate that decides whether elements are externalized
     */
    private BiPredicate<T, Integer> predicate;

    /**
     * Whether the externalization was actually applied to an element
     */
    private boolean applied;

    /**
     * Creates a new instance
     * 
     * @param path The base path that data will be written to
     * @param predicate The predicate that decides about externalization
     */
    Externalization(Path path, BiPredicate<T, Integer> predicate)
    {
        this.path = Objects.requireNonNull(path, "The path may not be null");
        this.predicate =
            Objects.requireNonNull(predicate, "The predicate may not be null");
    }

    /**
     * Returns the base path for externalized data
     * 
     * @return The base path
     */
    Path getPath()
    {
        return path;
    }

    /**
     * Whether the specified element should be externalized
     * 
     * @param element The element
     * @param index The index of the element
     * @return Whether the externalization should be applied
     */
    boolean shouldApply(T element, int index)
    {
        return predicate.test(element, index);
    }

    /**
     * Set whether externalization was actually applied
     * 
     * @param applied Whether externalization was applied
     */
    void setApplied(boolean applied)
    {
        this.applied = applied;
    }

    /**
     * Returns whether externalization was applied
     * 
     * @return Whether externalization was applied
     */
    boolean isApplied()
    {
        return applied;
    }

}
