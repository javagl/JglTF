/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.javagl.jgltf.model.ExtensionsModel;

/**
 * Default implementation of an {@link ExtensionsModel}.
 * 
 * This implementation ensures a certain degree of consistency for
 * the extension information:
 * <ul>
 *   <li>
 *     The extension names will always be unique
 *   </li>
 *   <li>
 *     Adding an extension as "required" will also add it as "used"
 *   </li>
 *   <li>
 *     Removing an extension as "used" will also remove it as "required"
 *   </li>
 * </ul>
 * (Of course, adding a "used" extension will not add it as a "required" one,
 * and <i>removing</i> a "required" extension will <i>not</i> remove it as 
 * a "used" extension) 
 */
public class DefaultExtensionsModel implements ExtensionsModel
{
    /**
     * The used extensions
     */
    private final Set<String> extensionsUsed;

    /**
     * The required extensions
     */
    private final Set<String> extensionsRequired;

    /**
     * Default constructor
     */
    public DefaultExtensionsModel()
    {
        this.extensionsUsed = new LinkedHashSet<String>();
        this.extensionsRequired = new LinkedHashSet<String>();
    }

    /**
     * Add the given extension name to the "used" extensions.
     * 
     * @param extension The extension name.
     */
    public void addExtensionUsed(String extension)
    {
        this.extensionsUsed.add(extension);
    }

    /**
     * Remove the given extension name from the "used" extensions and
     * from the list of "required" extensions.
     * 
     * @param extension The extension name.
     */
    public void removeExtensionUsed(String extension)
    {
        this.extensionsUsed.remove(extension);
        removeExtensionRequired(extension);
    }

    /**
     * Add the given extension names to the "used" extensions.
     * 
     * @param extensions The extension names
     */
    public void addExtensionsUsed(Collection<String> extensions)
    {
        if (extensions != null) 
        {
            this.extensionsUsed.addAll(extensions);
        }
    }

    /**
     * Clear the list of "used" extensions and the list of "required" extensions
     */
    public void clearExtensionUsed()
    {
        this.extensionsUsed.clear();
        clearExtensionRequired();
    }

    @Override
    public List<String> getExtensionsUsed()
    {
        return Collections.unmodifiableList(
            new ArrayList<String>(extensionsUsed));
    }

    
    /**
     * Add the given extension name to the "required" extensions and to
     * the "used" extensions.
     * 
     * @param extension The extension name.
     */
    public void addExtensionRequired(String extension)
    {
        this.extensionsRequired.add(extension);
        addExtensionUsed(extension);
    }

    /**
     * Remove the given extension name from the "required" extensions.
     * 
     * (Note that this will <i>not</i> remove the given extension name
     * from the "used" extensions!)
     * 
     * @param extension The extension name.
     */
    public void removeExtensionRequired(String extension)
    {
        this.extensionsRequired.remove(extension);
    }

    /**
     * Add the given extension names to the "required" extensions and to
     * the "used" extensions.
     * 
     * @param extensions The extension names
     */
    public void addExtensionsRequired(Collection<String> extensions)
    {
        if (extensions != null)
        {
            this.extensionsRequired.addAll(extensions);
            addExtensionsUsed(extensions);
        }
    }

    /**
     * Clear the list of "required" extensions.
     */
    public void clearExtensionRequired()
    {
        this.extensionsRequired.clear();
    }
    
    @Override
    public List<String> getExtensionsRequired()
    {
        return Collections.unmodifiableList(
            new ArrayList<String>(extensionsRequired));
    }

}
