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
package de.javagl.jgltf.browser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import de.javagl.jgltf.model.Optionals;

/**
 * Utility class for resolving entities inside a glTF based on
 * a JSON path description
 */
class Resolver
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(Resolver.class.getName());
    
    /**
     * A class encapsulating a resolved entity
     */
    static class ResolvedEntity
    {
        /**
         * The key that was used for the lookup
         */
        private final Object key;
        
        /**
         * The value that was found in the map
         */
        private final Object value;
        
        /**
         * If the lookup failed, this contains an informative message
         * of why the lookup failed 
         */
        private final String message;
        
        /**
         * Creates a new instance
         * 
         * @param context A context describing where the entity was looked up. 
         * This is only used for a descriptive output, and can, for example, 
         * be a human-readable description like <code>"the glTF file"</code> 
         * or <code"the animation samplers"</code>.
         * @param pathString The path string describing the path to the key, 
         * for example, <code>"glTF.accessors.accessor01.bufferView"</code>
         * @param key The key that is used for the lookup. This will usually be
         * an ID that is used as the key for the map
         * @param map The map that will be used for the lookup. This may be
         * <code>null</code>, causing an appropriate {@link #getMessage()}
         * to be generated
         */
        private ResolvedEntity(String context, String pathString, 
            Object key, Map<?, ?> map)
        {
            this.key = key;
            if (map == null)
            {
                value = null;
                message = "No map in " + context + 
                    " for looking up " + key + " from " + pathString;
            }
            else
            {
                value = map.get(key);
                if (value == null)
                {
                    message =
                        "No entry found in " + context + 
                        " map for " + key + " in " + pathString;
                }
                else
                {
                    message = null;
                }
            }
        }
        
        /**
         * Returns the key that was used for the lookup
         * 
         * @return The key
         */
        Object getKey()
        {
            return key;
        }

        /**
         * Returns the value that was found during the lookup. This may
         * be <code>null</code> if the lookup failed. In this case,
         * the {@link #getMessage() message} will contain more detailed
         * information
         * 
         * @return The value that was found during the lookup
         */
        Object getValue()
        {
            return value;
        }

        /**
         * If the lookup failed, this returns a message describing why
         * the lookup failed
         * 
         * @return The message
         */
        String getMessage()
        {
            return message;
        }

    }
    
    /**
     * The glTF
     */
    private final Object gltf;
    
    /**
     * Creates a new resolver for the given glTF
     * 
     * @param gltf The glTF
     */
    Resolver(Object gltf)
    {
        this.gltf = gltf;
    }
    
    /**
     * Resolve an entity in the current glTF. The path string is assumed to
     * describe the JSON path, like 
     * <code>"glTF.accessors.accessor01.bufferView"</code>. The key is
     * the corresponding property value - for example, the ID of a glTF 
     * bufferView - which will be used as the key for the lookup in the
     * appropriate map.
     *  
     * @param pathString The path string
     * @param key The key that is is used for the lookup  
     * @return The {@link ResolvedEntity}, which may be <code>null</code> if
     * the entry could not be resolved
     */
    ResolvedEntity resolve(String pathString, Object key)
    {
        // Create the map from simple path expressions (see the 
        // "RegEx.matches(String, String)" method) to the maps that
        // map the selected element to a glTF entity 
        Map<String, Map<?, ?>> m = 
            new LinkedHashMap<String, Map<?,?>>();
        m.put("glTF.scene", getMap(gltf, "scenes"));
        m.put("glTF.scenes.*.nodes.*", getMap(gltf, "nodes"));
        m.put("glTF.bufferViews.*.buffer", getMap(gltf, "buffers"));
        m.put("glTF.images.*.bufferView", getMap(gltf, "bufferViews"));
        m.put("glTF.nodes.*.camera", getMap(gltf, "cameras"));
        m.put("glTF.nodes.*.meshes.*", getMap(gltf, "meshes"));
        m.put("glTF.nodes.*.children.*", getMap(gltf, "nodes"));
        m.put("glTF.nodes.*.skeletons.*", getMap(gltf, "nodes"));
        m.put("glTF.nodes.*.skin", getMap(gltf, "skins"));
        m.put("glTF.meshes.*.primitives.*.material", getMap(gltf, "materials"));
        m.put("glTF.meshes.*.primitives.*.attributes.*", 
            getMap(gltf, "accessors"));
        m.put("glTF.meshes.*.primitives.*.targets.*.*", 
            getMap(gltf, "accessors"));
        m.put("glTF.accessors.*.bufferView", getMap(gltf, "bufferViews"));
        m.put("glTF.programs.*.fragmentShader", getMap(gltf, "shaders"));
        m.put("glTF.programs.*.vertexShader", getMap(gltf, "shaders"));
        m.put("glTF.materials.*.technique", getMap(gltf, "techniques"));
        m.put("glTF.materials.*.pbrMetallicRoughness.baseColorTexture.index", 
            getMap(gltf, "textures"));
        m.put("glTF.materials.*.emissiveTexture.index", 
            getMap(gltf, "textures"));
        m.put("glTF.materials.*.normalTexture.index", 
            getMap(gltf, "textures"));
        m.put("glTF.materials.*.occlusionTexture.index", 
            getMap(gltf, "textures"));
        m.put("glTF.techniques.*.program", getMap(gltf, "programs"));
        m.put("glTF.techniques.*.parameters.*.node", getMap(gltf, "nodes"));
        m.put("glTF.animations.*.channels.*.target.id", getMap(gltf, "nodes"));
        m.put("glTF.animations.*.samplers.*.input", getMap(gltf, "accessors"));
        m.put("glTF.animations.*.samplers.*.output", getMap(gltf, "accessors"));
        m.put("glTF.animations.*.parameters.*", getMap(gltf, "accessors"));
        m.put("glTF.textures.*.sampler", getMap(gltf, "samplers"));
        m.put("glTF.textures.*.source", getMap(gltf, "images"));
        m.put("glTF.skins.*.jointNames.*", getMap(gltf, "nodes"));
        m.put("glTF.skins.*.inverseBindMatrices", getMap(gltf, "accessors"));
        m.put("glTF.*.*.extensions.KHR_binary_glTF.bufferView", 
            getMap(gltf, "bufferViews"));
        
        // Try to resolve the top level glTF entities 
        // based on the path string
        for (Entry<String, Map<?, ?>> entry : m.entrySet())
        {
            String simpleExpression = entry.getKey();
            Map<?, ?> map = entry.getValue();
            if (RegEx.matches(pathString, simpleExpression))
            {
                return new ResolvedEntity(
                    "glTF", pathString, key, map);
            }
        }
        
        // Try to resolve the sampler of an animation
        if (RegEx.matches(pathString, "glTF.animations.*.channels.*.sampler"))
        {
            String animationId = extractId(pathString, "glTF.animations.");
            Object animation = 
                Optionals.get(animationId, getMap(gltf, "animations"));
            if (animation != null)
            {  
                Map<?, ?> samplers = getMap(animation, "samplers");
                return new ResolvedEntity(
                    "animation samplers", pathString, 
                    key, samplers);
            }
        }
        
        // Try to resolve the parameter of an animation sampler input or output
        if (RegEx.matches(pathString, "glTF.animations.*.samplers.*.input") ||
            RegEx.matches(pathString, "glTF.animations.*.samplers.*.output"))
        {
            String animationId = extractId(pathString, "glTF.animations.");
            Object animation = 
                Optionals.get(animationId, getMap(gltf, "animations"));
            if (animation != null)
            {
                Map<?, ?> parameters = 
                    getMap(animation, "parameters"); 
                return new ResolvedEntity(
                    "animation parameters", pathString, 
                    key, parameters);
            }
        }
        
        
        // Try to resolve the texture of a material value. 
        // (Some guesswork involved here...)
        if (RegEx.matches(pathString, "glTF.materials.*.values.*"))
        {
            String valueString = String.valueOf(key);
            Map<?, ?> textures = getMap(gltf, "textures");
            if (textures != null)
            {
                Object texture = textures.get(valueString);
                if (texture != null)
                {
                    return new ResolvedEntity(
                        "material values", pathString, 
                        key, textures);
                }
            }
        }
        
        // Try to resolve the parameter of a technique attribute
        if (RegEx.matches(pathString, "glTF.techniques.*.attributes.*"))
        {
            String techniqueId = extractId(pathString, "glTF.techniques.");
            Object technique = 
                Optionals.get(techniqueId, getMap(gltf, "techniques"));
            if (technique != null)
            {
                Map<?, ?> parameters = getMap(technique, "parameters");
                return new ResolvedEntity(
                    "technique parameters", pathString, 
                    key, parameters);
            }
        }
        return null;
    }
    
    /**
     * Tries to extract an ID from the given input string. This is assumed
     * to be the part after the given prefix, until the next "." dot.
     * If no ID can be extracted, then <code>null</code> is returned.
     * 
     * @param input The input
     * @param prefix The prefix
     * @return The ID
     */
    private static String extractId(String input, String prefix)
    {
        if (input.length() < prefix.length())
        {
            return null;
        }
        String part = input.substring(prefix.length());
        int dotIndex = part.indexOf(".");
        if (dotIndex < 0)
        {
            return null;
        }
        String id = part.substring(0, dotIndex);
        return id;
    }

    /**
     * Returns the map that is obtained by calling a getter method for the
     * property with the given name. For example, when the name is 
     * <code>"property"</code>, then the method <code>"getProperty"</code>
     * is called. If no such method exists, or it cannot be invoked,
     * then an empty map will be returned.<br>
     * <br>
     * If the method returns a List, then this method will return a Map
     * that is a read-only view on the list. 
     *  
     * @param object The object
     * @param name The property name
     * @return The map
     */
    private static Map<?, ?> getMap(Object object, String name) 
    {
        Class<?> c = object.getClass();
        try
        {
            Method method = c.getMethod("get" + capitalizeFirstLetter(name));
            Object result = method.invoke(object);
            if (result == null)
            {
                return null;
            }
            if (result instanceof List<?>)
            {
                List<?> list = (List<?>)result;
                return listAsMap(list);
            }
            if (!(result instanceof Map<?,?>))
            {
                logger.info("Result is not a map: " + result.getClass());
                return Collections.emptyMap();
            }
            Map<?, ?> map = (Map<?, ?>)result;
            return map;
        }
        catch (NoSuchMethodException | 
               SecurityException | 
               IllegalAccessException | 
               IllegalArgumentException | 
               InvocationTargetException e)
        {
            logger.fine("Could not access " + name + ": " + e.getMessage());
            return Collections.emptyMap();
        }
    }
    
    
    /**
     * Returns a map that is a read-only view on the given list
     * 
     * @param list The list
     * @return The map
     */
    private static <V> Map<Integer, V> listAsMap(List<? extends V> list)
    {
        return new AbstractMap<Integer, V>()
        {
            @Override
            public V get(Object key)
            {
                if (!(key instanceof Integer))
                {
                    return null;
                }
                Integer index = (Integer)key;
                if (index < 0 || index >= list.size())
                {
                    return null;
                }
                return list.get(index);
            }
            
            @Override
            public Set<Entry<Integer, V>> entrySet()
            {
                return new AbstractSet<Entry<Integer,V>>()
                {
                    @Override
                    public Iterator<Entry<Integer, V>> iterator()
                    {
                        return new Iterator<Entry<Integer,V>>()
                        {
                            int index = 0;
                            
                            @Override
                            public boolean hasNext()
                            {
                                return index < list.size();
                            }

                            @Override
                            public Entry<Integer, V> next()
                            {
                                if (index >= list.size())
                                {
                                    throw new NoSuchElementException(
                                        "No more elements");
                                }
                                Entry<Integer, V> result =
                                    new SimpleEntry<Integer, V>(
                                        index, list.get(index));
                                index++;
                                return result;
                            }
                        };
                    }

                    @Override
                    public int size()
                    {
                        return list.size();
                    }
                };
            }
        };
    }
    
    /**
     * Capitalize the first letter of the given string
     * 
     * @param s The string
     * @return The capitalized string
     */
    private static String capitalizeFirstLetter(String s)
    {
        if (s == null || s.length() == 0)
        {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    
}
