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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.javagl.jgltf.impl.Animation;
import de.javagl.jgltf.impl.AnimationSampler;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Technique;
import de.javagl.jgltf.impl.TechniqueParameters;
import de.javagl.jgltf.impl.Texture;

/**
 * Utility class for resolving entities inside a {@link GlTF} based on
 * a JSON path description
 */
class Resolver
{
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
        ResolvedEntity(String context, String pathString, 
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
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * Creates a new resolver for the given {@link GlTF}
     * 
     * @param gltf The {@link GlTF}
     */
    Resolver(GlTF gltf)
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
        // "matches(String, String)" method) to the maps that
        // map the selected element to a glTF entity 
        Map<String, Map<?, ?>> m = 
            new LinkedHashMap<String, Map<?,?>>();
        m.put("glTF.scene", gltf.getScenes());
        m.put("glTF.scenes.*.nodes.*", gltf.getNodes());
        m.put("glTF.bufferViews.*.buffer", gltf.getBuffers());
        m.put("glTF.nodes.*.camera", gltf.getCameras());
        m.put("glTF.nodes.*.meshes.*", gltf.getMeshes());
        m.put("glTF.nodes.*.children.*", gltf.getNodes());
        m.put("glTF.nodes.*.skeletons.*", gltf.getNodes());
        m.put("glTF.nodes.*.skin", gltf.getSkins());
        m.put("glTF.meshes.*.primitives.*.material", gltf.getMaterials());
        m.put("glTF.meshes.*.primitives.*.attributes.*", gltf.getAccessors());
        m.put("glTF.meshes.*.primitives.*.indices", gltf.getAccessors());
        m.put("glTF.accessors.*.bufferView", gltf.getBufferViews());
        m.put("glTF.programs.*.fragmentShader", gltf.getShaders());
        m.put("glTF.programs.*.vertexShader", gltf.getShaders());
        m.put("glTF.materials.*.technique", gltf.getTechniques());
        m.put("glTF.techniques.*.program", gltf.getPrograms());
        m.put("glTF.techniques.*.parameters.*.node", gltf.getNodes());
        m.put("glTF.animations.*.channels.*.target.id", gltf.getNodes());
        m.put("glTF.animations.*.parameters.*", gltf.getAccessors());
        m.put("glTF.textures.*.sampler", gltf.getSamplers());
        m.put("glTF.textures.*.source", gltf.getImages());
        m.put("glTF.skins.*.jointNames.*", gltf.getNodes());
        m.put("glTF.skins.*.inverseBindMatrices", gltf.getAccessors());
        m.put("*.extensions.KHR_binary_glTF.bufferView", gltf.getBufferViews());
        
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
            if (animationId != null)
            {
                Map<String, Animation> animations = gltf.getAnimations();
                if (animations != null)
                {
                    Animation animation = animations.get(animationId);
                    if (animation != null)
                    {
                        Map<String, AnimationSampler> samplers = 
                            animation.getSamplers();
                        return new ResolvedEntity(
                            "animation samplers", pathString, 
                            key, samplers);
                    }
                }
            }
        }
        
        // Try to resolve the texture of a material value. 
        // (Some guesswork involved here...)
        if (RegEx.matches(pathString, "glTF.materials.*.values.*"))
        {
            String valueString = String.valueOf(key);
            Map<String, Texture> textures = gltf.getTextures();
            if (textures != null)
            {
                Texture texture = textures.get(valueString);
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
            if (techniqueId != null)
            {
                Map<String, Technique> techniques = gltf.getTechniques();
                if (techniques != null)
                {
                    Technique technique = techniques.get(techniqueId);
                    if (technique != null)
                    {
                        Map<String, TechniqueParameters> parameters = 
                            technique.getParameters();
                        return new ResolvedEntity(
                            "technique parameters", pathString, 
                            key, parameters);
                    }
                }
            }
        }
        
        // Try to resolve the parameter of an animation sampler input or output
        if (RegEx.matches(pathString, "glTF.animations.*.samplers.*.input") ||
            RegEx.matches(pathString, "glTF.animations.*.samplers.*.output"))
        {
            String animationId = extractId(pathString, "glTF.animations.");
            if (animationId != null)
            {
                Map<String, Animation> animations = gltf.getAnimations();
                if (animations != null)
                {
                    Animation animation = animations.get(animationId);
                    if (animation != null)
                    {
                        Map<String, String> parameters = 
                            animation.getParameters();
                        return new ResolvedEntity(
                            "animation parameters", pathString, 
                            key, parameters);
                    }
                }
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

}
