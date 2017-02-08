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
package de.javagl.jgltf.viewer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueStates;
import de.javagl.jgltf.impl.v1.TechniqueStatesFunctions;
import de.javagl.jgltf.model.GltfConstants;

/**
 * A class representing the techniques that are used for rendering a glTF.
 * This is based on the material- and technique concept of glTF 1.0, and
 * will be implemented with custom techniques for glTF 2.0.
 */
class GltfTechniqueModel
{
    /**
     * The glTF which contains the techniques and materials
     */
    private final GlTF gltf;
    
    /**
     * Create a new technique model for the given {@link GlTF}
     * 
     * @param gltf The {@link GlTF}. May not be <code>null</code>.
     */
    GltfTechniqueModel(GlTF gltf)
    {
        this.gltf = Objects.requireNonNull(gltf, "The gltf may not be null");
    }
    
    /**
     * Obtain the {@link Material} with the given ID from the {@link GlTF},
     * or return the {@link GltfDefaults#getDefaultMaterial() default
     * material} if the given ID is <code>null</code> or the 
     * {@link GltfDefaults#isDefaultMaterialId(String) default material ID}.
     * 
     * @param materialId The {@link Material} ID
     * @return The {@link Material}
     */
    Material obtainMaterial(String materialId)
    {
        if (materialId == null ||
            GltfDefaults.isDefaultMaterialId(materialId))
        {
            return GltfDefaults.getDefaultMaterial();
        }
        return gltf.getMaterials().get(materialId);
    }

    /**
     * Obtain the {@link Technique} with the given ID from the {@link GlTF},
     * or return the {@link GltfDefaults#getDefaultTechnique() default
     * technique} if the given ID is <code>null</code> or the 
     * {@link GltfDefaults#isDefaultTechniqueId(String) default technique ID}.
     * 
     * @param techniqueId The {@link Technique} ID
     * @return The {@link Technique}
     */
    Technique obtainTechnique(String techniqueId)
    {
        if (techniqueId == null ||
            GltfDefaults.isDefaultTechniqueId(techniqueId))
        {
            return GltfDefaults.getDefaultTechnique();
        }
        return gltf.getTechniques().get(techniqueId);
    }
    
    /**
     * Returns the set of states that should be enabled for the given 
     * {@link Technique}
     * 
     * @param technique The {@link Technique}
     * @return The enabled states
     */
    static List<Integer> getEnabledStates(Technique technique)
    {
        TechniqueStates states = obtainTechniqueStates(technique);
        List<Integer> enable = states.getEnable();
        if (enable == null)
        {
            return states.defaultEnable();
        }
        return enable;
    }
    
    /**
     * Return the {@link TechniqueStates} from the given {@link Technique},
     * or the {@link TechniqueStates} from the 
     * {@link GltfDefaults#getDefaultTechnique() default technique} if
     * the given {@link Technique} does not contain any {@link TechniqueStates}
     *  
     * @param technique The {@link Technique}
     * @return The {@link TechniqueStates}
     */
    private static TechniqueStates obtainTechniqueStates(Technique technique)
    {
        TechniqueStates states = technique.getStates();
        if (states == null)
        {
            return GltfDefaults.getDefaultTechnique().getStates();
        }
        return states;
    }
    
    /**
     * Return the {@link TechniqueStatesFunctions} from the 
     * {@link TechniqueStates} of the given {@link Technique},
     * or the {@link TechniqueStatesFunctions} from the 
     * {@link GltfDefaults#getDefaultTechnique() default technique} if
     * the given {@link Technique} does not contain any 
     * {@link TechniqueStates} or {@link TechniqueStatesFunctions}
     *  
     * @param technique The {@link Technique}
     * @return The {@link TechniqueStatesFunctions}
     */
    static TechniqueStatesFunctions obtainTechniqueStatesFunctions(
        Technique technique)
    {
        TechniqueStates states = obtainTechniqueStates(technique);
        TechniqueStatesFunctions functions = states.getFunctions();
        if (functions == null)
        {
            TechniqueStates defaultStates = 
                GltfDefaults.getDefaultTechnique().getStates();
            return defaultStates.getFunctions();
        }
        return functions;
    }
    
    /**
     * Returns a list containing all possible states that may be contained
     * in a <code>technique.states.enable</code> list.
     * 
     * @return All possible states
     */
    static List<Integer> getAllStates()
    {
        List<Integer> allStates = Arrays.asList(
            GltfConstants.GL_BLEND,
            GltfConstants.GL_CULL_FACE,
            GltfConstants.GL_DEPTH_TEST,
            GltfConstants.GL_POLYGON_OFFSET_FILL,
            GltfConstants.GL_SAMPLE_ALPHA_TO_COVERAGE,
            GltfConstants.GL_SCISSOR_TEST
        );
        return allStates;
    }
    
    

}
