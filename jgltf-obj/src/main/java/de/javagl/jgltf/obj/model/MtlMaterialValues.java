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
package de.javagl.jgltf.obj.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.v1.MaterialModelV1;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.FloatTuples;
import de.javagl.obj.Mtl;

/**
 * Methods to create the {@link MaterialModelV1#getValues() material values} 
 * from an MTL
 */
class MtlMaterialValues
{
    /**
     * Create the {@link MaterialModelV1#getValues() material values} for the 
     * given MTL data, matching to the {@link TechniqueModel} instances that
     * are contained in the {@link ObjTechniqueModels}
     * 
     * @param mtl The MTL
     * @param textureModel The {@link TextureModel}. If this is 
     * <code>null</code>, then the diffuse color from the given 
     * MTL will be used
     * @return The material values
     */
    static Map<String, Object> createMaterialValues(
        Mtl mtl, TextureModel textureModel)
    {
        FloatTuple ka = FloatTuples.create(0, 0, 0);
        FloatTuple kd = FloatTuples.create(0, 0, 0);
        FloatTuple ks = FloatTuples.create(0, 0, 0);
        float d = 1.0f;
        float ns = 100.0f;
        if (mtl.getKa() != null)
        {
            ka = mtl.getKa();
        }
        if (mtl.getKd() != null)
        {
            kd = mtl.getKd();
        }
        if (mtl.getKs() != null)
        {
            ks = mtl.getKs();
        }
        if (mtl.getD() != null)
        {
            d = mtl.getD();
        }
        if (mtl.getNs() != null)
        {
            ns = mtl.getNs();
        }
        Map<String, Object> materialValues = 
            new LinkedHashMap<String, Object>();
        materialValues.put(ObjTechniqueModels.AMBIENT_NAME, 
            createMaterialValue(ka));
        if (textureModel == null)
        {
            materialValues.put(ObjTechniqueModels.DIFFUSE_NAME, 
                createMaterialValue(kd, d));
        }
        else
        {
            materialValues.put(ObjTechniqueModels.DIFFUSE_NAME, textureModel);
        }
        materialValues.put(ObjTechniqueModels.SPECULAR_NAME, 
            createMaterialValue(ks));
        materialValues.put(ObjTechniqueModels.SHININESS_NAME, 
            Collections.singletonList(ns));
        return materialValues;
    }

    /**
     * Create a material value from the given float tuple. This will be a 
     * list of float values, containing the <code>(x,y,z)</code> components 
     * of the given tuple, and 1.0f as the fourth element
     * 
     * @param t The input tuple
     * @return The values
     */
    private static List<Float> createMaterialValue(FloatTuple t)
    {
        return createMaterialValue(t, 1.0f);
    }

    /**
     * Create a material value from the given float tuple. This will be a 
     * list of float values, containing the <code>(x,y,z)</code> components 
     * of the given tuple, and the given <code>z</code> value as the fourth 
     * element
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
     * Create the (unspecified) material value for a "default" material 
     * with the given diffuse RGB color
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     * @return The values
     */
    static Map<String, Object> createDefaultMaterialValues(
        float r, float g, float b)
    {
        Map<String, Object> materialValues = 
            new LinkedHashMap<String, Object>();
        materialValues.put(ObjTechniqueModels.AMBIENT_NAME, 
            Arrays.asList(0.0f, 0.0f, 0.0f, 1.0f));
        materialValues.put(ObjTechniqueModels.DIFFUSE_NAME, 
            Arrays.asList(r, g, b, 1.0f));
        materialValues.put(ObjTechniqueModels.SPECULAR_NAME, 
            Arrays.asList(0.0f, 0.0f, 0.0f, 1.0f));
        materialValues.put(ObjTechniqueModels.SHININESS_NAME, 30.0f);
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
