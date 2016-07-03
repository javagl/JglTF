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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.Buffer;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Material;
import de.javagl.jgltf.impl.Mesh;
import de.javagl.jgltf.impl.Node;
import de.javagl.jgltf.impl.Program;
import de.javagl.jgltf.impl.Scene;
import de.javagl.jgltf.impl.Shader;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.Texture;

/**
 * Utility methods for {@link GlTF}s.
 */
class Gltfs
{
    /*
     * Implementation note: These methods could be omitted and be replaced
     * with a more object-oriented implementation in the GlTF class. 
     * But the GlTF class is auto-generated from the JSON schema, and
     * adding the specific functionality that is covered by these methods
     * for the particular case of glTF raises some questions, e.g. about
     * the generation of keys for the maps, or the treatment of empty
     * maps. So they are for now summarized in this (package private) class.   
     */
    
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(Gltfs.class.getName());

    /**
     * Add the {@link Buffer} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addBuffer(GlTF gltf, String id, Buffer value)
    {
        add(gltf::setBuffers, gltf::getBuffers, id, value);
    }
    
    /**
     * Add the given {@link Buffer} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addBuffer(GlTF gltf, Buffer value)
    {
        String id = generateId("buffer", gltf.getBuffers());
        add(gltf::setBuffers, gltf::getBuffers, id, value);
        return id;
    }


    /**
     * Add the {@link BufferView} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addBufferView(GlTF gltf, String id, BufferView value)
    {
        add(gltf::setBufferViews, gltf::getBufferViews, id, value);
    }
    
    /**
     * Add the given {@link BufferView} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addBufferView(GlTF gltf, BufferView value)
    {
        String id = generateId("bufferview", gltf.getBufferViews());
        add(gltf::setBufferViews, gltf::getBufferViews, id, value);
        return id;
    }


    /**
     * Add the {@link Accessor} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addAccessor(GlTF gltf, String id, Accessor value)
    {
        add(gltf::setAccessors, gltf::getAccessors, id, value);
    }
    
    /**
     * Add the given {@link Accessor} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addAccessor(GlTF gltf, Accessor value)
    {
        String id = generateId("accessor", gltf.getAccessors());
        add(gltf::setAccessors, gltf::getAccessors, id, value);
        return id;
    }


    /**
     * Add the {@link Technique} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addTechnique(GlTF gltf, String id, Technique value)
    {
        add(gltf::setTechniques, gltf::getTechniques, id, value);
    }
    
    /**
     * Add the given {@link Technique} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addTechnique(GlTF gltf, Technique value)
    {
        String id = generateId("technique", gltf.getTechniques());
        add(gltf::setTechniques, gltf::getTechniques, id, value);
        return id;
    }


    /**
     * Add the {@link Shader} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addShader(GlTF gltf, String id, Shader value)
    {
        add(gltf::setShaders, gltf::getShaders, id, value);
    }
    
    /**
     * Add the given {@link Shader} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addShader(GlTF gltf, Shader value)
    {
        String id = generateId("shader", gltf.getShaders());
        add(gltf::setShaders, gltf::getShaders, id, value);
        return id;
    }


    /**
     * Add the {@link Program} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addProgram(GlTF gltf, String id, Program value)
    {
        add(gltf::setPrograms, gltf::getPrograms, id, value);
    }
    
    /**
     * Add the given {@link Program} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addProgram(GlTF gltf, Program value)
    {
        String id = generateId("program", gltf.getPrograms());
        add(gltf::setPrograms, gltf::getPrograms, id, value);
        return id;
    }


    /**
     * Add the {@link Material} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addMaterial(GlTF gltf, String id, Material value)
    {
        add(gltf::setMaterials, gltf::getMaterials, id, value);
    }

    /**
     * Add the given {@link Material} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addMaterial(GlTF gltf, Material value)
    {
        String id = generateId("material", gltf.getMaterials());
        add(gltf::setMaterials, gltf::getMaterials, id, value);
        return id;
    }

    /**
     * Add the {@link Image} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addImage(GlTF gltf, String id, Image value)
    {
        add(gltf::setImages, gltf::getImages, id, value);
    }

    /**
     * Add the given {@link Image} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addImage(GlTF gltf, Image value)
    {
        String id = generateId("image", gltf.getImages());
        add(gltf::setImages, gltf::getImages, id, value);
        return id;
    }

    /**
     * Add the {@link Texture} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addTexture(GlTF gltf, String id, Texture value)
    {
        add(gltf::setTextures, gltf::getTextures, id, value);
    }

    /**
     * Add the given {@link Texture} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addTexture(GlTF gltf, Texture value)
    {
        String id = generateId("texture", gltf.getTextures());
        add(gltf::setTextures, gltf::getTextures, id, value);
        return id;
    }

    /**
     * Add the {@link Node} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addNode(GlTF gltf, String id, Node value)
    {
        add(gltf::setNodes, gltf::getNodes, id, value);
    }

    /**
     * Add the given {@link Node} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addNode(GlTF gltf, Node value)
    {
        String id = generateId("node", gltf.getNodes());
        add(gltf::setNodes, gltf::getNodes, id, value);
        return id;
    }

    /**
     * Add the {@link Scene} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addScene(GlTF gltf, String id, Scene value)
    {
        add(gltf::setScenes, gltf::getScenes, id, value);
    }

    /**
     * Add the given {@link Scene} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addScene(GlTF gltf, Scene value)
    {
        String id = generateId("scene", gltf.getScenes());
        add(gltf::setScenes, gltf::getScenes, id, value);
        return id;
    }

    /**
     * Add the {@link Mesh} with the given ID to the given {@link GlTF}.
     *
     * @param gltf The {@link GlTF}
     * @param id The ID
     * @param value The value
     */
    static void addMesh(GlTF gltf, String id, Mesh value)
    {
        add(gltf::setMeshes, gltf::getMeshes, id, value);
    }

    /**
     * Add the given {@link Mesh} to the given {@link GlTF}, and return
     * the ID that has been generated for it.
     *
     * @param gltf The {@link GlTF}
     * @param value The value
     * @return The generated ID
     */
    static String addMesh(GlTF gltf, Mesh value)
    {
        String id = generateId("mesh", gltf.getMeshes());
        add(gltf::setMeshes, gltf::getMeshes, id, value);
        return id;
    }
    
    
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
     * Returns the size of the given map, or 0 if the given map is 
     * <code>null</code>
     * 
     * @param map The map
     * @return The size of the map
     */
    static int getSize(Map<?, ?> map)
    {
        return map == null ? 0 : map.size();
    }

    /**
     * Returns a new, modifiable map that has the same contents as the one 
     * that is provided by the given supplier. The result is an empty map
     * if the supplier supplies a <code>null</code> map.
     * 
     * @param supplier The supplier
     * @return the new map
     */
    private static <K, V> Map<K, V> getNew(
        Supplier<? extends Map<K, V>> supplier)
    {
        Map<K, V> newMap = new LinkedHashMap<K, V>();
        Map<K, V> oldMap = supplier.get();
        if (oldMap != null)
        {
            newMap.putAll(oldMap);
        }
        return newMap;
    } 

    /**
     * Create a new map that has the same entries as the map that is provided
     * by the given supplier, add the given key-value mapping, and pass the
     * new map to the given consumer. The supplier is allowed to provide
     * a <code>null</code> map.<br>
     * <br>
     * If the old map already contains the given key, then a warning will
     * be printed.
     * 
     * @param consumer The consumer for the new map
     * @param supplier The supplier for the old map
     * @param key The key for the new entry
     * @param value The value for the new entry
     */
    private static <K, V> void add(
        Consumer<? super Map<K, V>> consumer, 
        Supplier<? extends Map<K, V>> supplier, 
        K key, V value)
    {
        Map<K, V> newMap = getNew(supplier);
        if (newMap.containsKey(key))
        {
            logger.warning(
                "Entry with " + key + " and value " + value + "already exists");
        }
        newMap.put(key,  value);
        consumer.accept(newMap);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Gltfs()
    {
        // Private constructor to prevent instantiation
    }

}