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
package de.javagl.jgltf.validator;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.jgltf.impl.v1.GlTF;

/**
 * Abstract base class for classes that can validate glTF elements
 */
abstract class AbstractGltfValidator
{
    /**
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} that contains the elements to validate
     */
    protected AbstractGltfValidator(GlTF gltf)
    {
        this.gltf = Objects.requireNonNull(gltf, "The gltf may not be null");
    }
    
    /**
     * Returns the {@link GlTF} for which this validator was created
     * 
     * @return The {@link GlTF}
     */
    protected final GlTF getGltf()
    {
        return gltf;
    }
    
    /**
     * Returns whether the version of the {@link #getGltf() glTF} of this
     * instance is greater than or equal to the given semantic version 
     * string. Details about the validity of the given string are not
     * specified, but it should be a "usual" semantic version string,
     * like "1.2" or "1.1.0-beta". 
     * 
     * @param versionString The version string
     * @return Whether the version is greater than or equal to the given one
     */
    protected final boolean isVersionGreaterThanOrEqual(String versionString)
    {
        return GltfUtils.compareVersions(
            GltfUtils.getVersion(gltf), versionString) >= 0;
    }
    
    /**
     * Generic method to validate a map entry. It checks whether the given
     * map and the given key are not <code>null</code>, and the value that
     * is obtained from the map for the given key is not <code>null</code>.
     * If any condition does not hold, the {@link ValidatorResult} will
     * contain the appropriate error {@link ValidatorMessage}.
     * 
     * @param map The map
     * @param key The key
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    static ValidatorResult validateMapEntry(
        Map<String, ?> map, String key, ValidatorContext currentContext)
    {
        ValidatorContext context = new ValidatorContext(currentContext);
        ValidatorResult validatorResult = new ValidatorResult();
        if (key == null)
        {
            validatorResult.addError(
                "The key is null", context);
        }
        if (map == null)
        {
            validatorResult.addError(
                "The map is null when looking up key " + key, context);
        }
        if (key != null && map != null)
        {
            Object result = map.get(key);
            if (result == null)
            {
                validatorResult.addError(
                    "The value is null when looking up key " + key, context);
            }
        }
        return validatorResult;
    }
    
    /**
     * Call {@link #validateMapEntry(Map, String, ValidatorContext)} for all
     * keys in the given list. If the given list is <code>null</code> or
     * empty, and <code>expected==true</code>, then a warning will be created.
     * 
     * @param map The map to pass to {@link #validateMapEntry}
     * @param keys The keys to validate
     * @param keysName The name of the keys array (used in messages)
     * @param expected Whether the given list is expected to be 
     * non-<code>null</code> and not empty.
     * @param currentContext The optional {@link ValidatorContext} describing 
     * where the given object appeared
     * @return The {@link ValidatorResult}
     */
    static ValidatorResult validateMapEntries(
        Map<String, ?> map, List<String> keys, 
        String keysName, boolean expected, ValidatorContext currentContext)
    {
        ValidatorResult validatorResult = new ValidatorResult();
        ValidatorContext context = new ValidatorContext(currentContext);
        
        if (keys == null || keys.isEmpty())
        {
            if (expected)
            {
                validatorResult.addWarning(
                    "Empty '" + keysName + "' array", context);
            }
        }
        else
        {
            for (int i = 0; i < keys.size(); i++)
            {
                String key = keys.get(i);
                validatorResult.add(validateMapEntry(map, key,
                    context.with(keysName + "[" + i + "]")));
                if (validatorResult.hasErrors())
                {
                    return validatorResult;
                }
            }
        }
        return validatorResult;
    }
}
