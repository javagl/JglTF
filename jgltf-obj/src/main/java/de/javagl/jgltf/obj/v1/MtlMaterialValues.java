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
package de.javagl.jgltf.obj.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.Mtl;

/**
 * Methods to create the {@link Material#getValues() material values} from
 * an MTL
 */
class MtlMaterialValues
{
    /**
     * Create the {@link Material#getValues() material values} for the given
     * MTL data, matching to the {@link Technique#getParameters() technique
     * parameters} of the {@link Technique}s that are created by the
     * {@link TechniqueHandler}
     * 
     * @param mtl The MTL
     * @param textureId The texture ID. If this is <code>null</code>, then
     * the diffuse color from the given MTL will be used
     * @return The {@link Material#getValues() material values}
     */
    static Map<String, Object> createMaterialValues(Mtl mtl, String textureId)
    {
        Map<String, Object> materialValues = 
            new LinkedHashMap<String, Object>();
        materialValues.put(TechniqueHandler.AMBIENT_NAME, 
            createMaterialValue(mtl.getKa()));
        if (textureId == null)
        {
            materialValues.put(TechniqueHandler.DIFFUSE_NAME, 
                createMaterialValue(mtl.getKd(), mtl.getD()));
        }
        else
        {
            materialValues.put(TechniqueHandler.DIFFUSE_NAME, 
                Collections.singletonList(textureId));
        }
        materialValues.put(TechniqueHandler.SPECULAR_NAME, 
            createMaterialValue(mtl.getKs()));
        materialValues.put(TechniqueHandler.SHININESS_NAME, 
            Collections.singletonList(mtl.getNs()));
        return materialValues;
    }

    /**
     * Create a {@link Material#getValues() material value} from the given
     * float tuple. This will be a list of float values, containing the 
     * <code>(x,y,z)</code> components of the given tuple, and 1.0f as 
     * the fourth element
     * 
     * @param t The input tuple
     * @return The values
     */
    private static List<Float> createMaterialValue(FloatTuple t)
    {
        return createMaterialValue(t, 1.0f);
    }

    /**
     * Create a {@link Material#getValues() material value} from the given
     * float tuple. This will be a list of float values, containing the 
     * <code>(x,y,z)</code> components of the given tuple, and the given 
     * <code>z</code> value as the fourth element
     * 
     * @param t The input tuple
     * @param z The z-value
     * @return The values
     */
    private static List<Float> createMaterialValue(FloatTuple t, float z)
    {
        List<Float> list = new ArrayList<Float>();
        list.add(t.getX());
        list.add(t.getY());
        list.add(t.getZ());
        list.add(z);
        return list;
    }

    
    /**
     * Create the (unspecified) {@link Material#getValues() material values} 
     * for a "default" material with the given diffuse RGB color
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     * @return The {@link Material#getValues() material values}
     */
    static Map<String, Object> createDefaultMaterialValues(
        float r, float g, float b)
    {
        Map<String, Object> materialValues = 
            new LinkedHashMap<String, Object>();
        materialValues.put(TechniqueHandler.AMBIENT_NAME, 
            Arrays.asList(0.0f, 0.0f, 0.0f, 1.0f));
        materialValues.put(TechniqueHandler.DIFFUSE_NAME, 
            Arrays.asList(r, g, b, 1.0f));
        materialValues.put(TechniqueHandler.SPECULAR_NAME, 
            Arrays.asList(0.0f, 0.0f, 0.0f, 1.0f));
        materialValues.put(TechniqueHandler.SHININESS_NAME, 30.0f);
        return materialValues;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private MtlMaterialValues()
    {
        // Private constructor to prevent instantiation
    }
}
