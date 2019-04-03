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
package de.javagl.jgltf.viewer;

import java.util.Objects;

import de.javagl.jgltf.model.v2.MaterialModelV2;

/**
 * A simple (package-private!) class describing the structure of a material.
 * It is only used as a key for map lookups in the 
 * {@link RenderedMaterialHandler}, to decide whether for a certain 
 * structure of a material a new technique is required.
 */
class MaterialStructure
{
    /**
     * The base color texture coordinates semantic string
     */
    private final String baseColorTexCoordSemantic;

    /**
     * The metallic-roughness texture coordinates semantic string
     */
    private final String metallicRoughnessTexCoordSemantic;

    /**
     * The normal texture coordinates semantic string
     */
    private final String normalTexCoordSemantic;

    /**
     * The occlusion texture coordinates semantic string
     */
    private final String occlusionTexCoordSemantic;

    /**
     * The emissive texture coordinates semantic string
     */
    private final String emissiveTexCoordSemantic;

    /**
     * The number of joints
     */
    private final int numJoints;
    
    /**
     * Default constructor 
     * 
     * @param material The {@link MaterialModelV2}
     * @param numJoints The number of joints
     */
    MaterialStructure(MaterialModelV2 material, int numJoints)
    {
        this.baseColorTexCoordSemantic = 
            getTexCoordSemantic(material.getBaseColorTexcoord());
        this.metallicRoughnessTexCoordSemantic =
            getTexCoordSemantic(material.getMetallicRoughnessTexcoord());
        this.normalTexCoordSemantic = 
            getTexCoordSemantic(material.getNormalTexcoord());
        this.occlusionTexCoordSemantic = 
            getTexCoordSemantic(material.getOcclusionTexcoord());
        this.emissiveTexCoordSemantic = 
            getTexCoordSemantic(material.getEmissiveTexcoord());
        
        this.numJoints = numJoints;
    }
    
    /**
     * Obtain the <code>TEXCOORD_n</code> semantic string based on the given
     * texture info, defaulting to <code>TEXCOORD_0</code>
     *  
     * @param texCoord The optional texture coordinate
     * @return The string
     */
    private static String getTexCoordSemantic(Integer texCoord)
    {
        if (texCoord == null)
        {
            return "TEXCOORD_0";
        }
        return "TEXCOORD_" + texCoord;
    }
    
    /**
     * Returns the texture coordinates semantic string
     * 
     * @return The semantic string
     */
    String getBaseColorTexCoordSemantic()
    {
        return baseColorTexCoordSemantic;
    }

    /**
     * Returns the texture coordinates semantic string
     * 
     * @return The semantic string
     */
    String getMetallicRoughnessTexCoordSemantic()
    {
        return metallicRoughnessTexCoordSemantic;
    }

    /**
     * Returns the texture coordinates semantic string
     * 
     * @return The semantic string
     */
    String getNormalTexCoordSemantic()
    {
        return normalTexCoordSemantic;
    }

    /**
     * Returns the texture coordinates semantic string
     * 
     * @return The semantic string
     */
    String getOcclusionTexCoordSemantic()
    {
        return occlusionTexCoordSemantic;
    }

    /**
     * Returns the texture coordinates semantic string
     * 
     * @return The semantic string
     */
    String getEmissiveTexCoordSemantic()
    {
        return emissiveTexCoordSemantic;
    }

    /**
     * Returns the number of joints
     * 
     * @return The number of joints
     */
    int getNumJoints()
    {
        return numJoints;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(
            baseColorTexCoordSemantic, 
            metallicRoughnessTexCoordSemantic,
            normalTexCoordSemantic, 
            occlusionTexCoordSemantic, 
            emissiveTexCoordSemantic, 
            numJoints);
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null)
        {
            return false;
        }
        if (getClass() != object.getClass())
        {
            return false;
        }
        MaterialStructure other = (MaterialStructure) object;
        if (!Objects.equals(baseColorTexCoordSemantic, 
            other.baseColorTexCoordSemantic))
        {
            return false;
        }
        if (!Objects.equals(metallicRoughnessTexCoordSemantic, 
            other.metallicRoughnessTexCoordSemantic))
        {
            return false;
        }
        if (!Objects.equals(normalTexCoordSemantic, 
            other.normalTexCoordSemantic))
        {
            return false;
        }
        if (!Objects.equals(occlusionTexCoordSemantic, 
            other.occlusionTexCoordSemantic))
        {
            return false;
        }
        if (!Objects.equals(emissiveTexCoordSemantic, 
            other.emissiveTexCoordSemantic))
        {
            return false;
        }
        if (numJoints != other.numJoints)
        {
            return false;
        }
       return true;
    }
    
    
}