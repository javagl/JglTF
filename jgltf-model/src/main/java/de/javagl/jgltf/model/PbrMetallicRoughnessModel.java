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
package de.javagl.jgltf.model;

/**
 * Interface for the metallic-roughness texture information that is part of a
 * {@link PbrMaterialModel}.
 */
public interface PbrMetallicRoughnessModel extends ModelElement
{
    /**
     * Returns the base color factor
     *
     * @return The base color factor
     */
    double[] getBaseColorFactor();

    /**
     * Returns the base color texture info
     *
     * @return The base color texture info
     */
    TextureInfoModel getBaseColorTextureInfoModel();

    /**
     * Returns the metallic factor
     *
     * @return The metallic factor
     */
    double getMetallicFactor();
    
    /**
     * Returns the roughness factor
     *
     * @return The roughness factor
     */
    double getRoughnessFactor();

    /**
     * Returns the metallic-roughness-texture info
     *
     * @return The metallic-roughness texture info
     */
    TextureInfoModel getMetallicRoughnessTextureInfoModel();

}
