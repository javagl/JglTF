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
package de.javagl.jgltf.model.khr.draco_mesh_compression;

/**
 * A class summarizing draco compression options.
 * 
 * This class is preliminary and may change in future releases.
 */
public class DracoOptions
{
    /**
     * Enumeration of the available compression levels 
     */
    public static enum CompressionLevel
    {
        /**
         * No compression
         */
        NO_COMPRESSION,

        /**
         * Fast compression
         */
        FAST,

        /**
         * Standard compression
         */
        STANDARD,
        /**
         * Optimal compression
         */
        OPTIMAL;
    }
    
    /**
     * Creates a copy of the given options
     * 
     * @param input The input 
     * @return The copy
     */
    static DracoOptions copy(DracoOptions input)
    {
        DracoOptions copy = new DracoOptions();
        copy.setPositionBits(input.getPositionBits());
        copy.setTextureCoordinateBits(input.getTextureCoordinateBits());
        copy.setNormalBits(input.getNormalBits());
        copy.setColorBits(input.getColorBits());
        copy.setCompressionLevel(input.getCompressionLevel());
        return copy;
    }

    /**
     * The position quantization bits
     */
    private int positionBits;
    
    /**
     * The normal quantization bits
     */
    private int normalBits;
    
    /**
     * The texture coordinate quantization bits
     */
    private int textureCoordinateBits;
    
    /**
     * The color quantization bits
     */
    private int colorBits;
    
    /**
     * The compression level
     */
    private CompressionLevel compressionLevel;

    /**
     * Creates a new instance with unspecified defaults
     */
    public DracoOptions()
    {
        this.setPositionBits(11);
        this.setTextureCoordinateBits(12);
        this.setNormalBits(10);
        this.setColorBits(10);
        this.setCompressionLevel(CompressionLevel.STANDARD);
    }


    /**
     * Returns the quantization bits for the position
     *
     * @return The value
     */
    public int getPositionBits()
    {
        return this.positionBits;
    }

    /**
     * Set the quantization bits for the position
     *
     * @param value The value
     */
    public void setPositionBits(int value)
    {
        this.positionBits = value;
    }

    /**
     * Returns the quantization bits for the texture coordinates
     *
     * @return The value
     */
    public int getTextureCoordinateBits()
    {
        return this.textureCoordinateBits;
    }

    /**
     * Set the quantization bits for the texture coordinates
     *
     * @param value The value
     */
    public void setTextureCoordinateBits(int value)
    {
        this.textureCoordinateBits = value;
    }

    /**
     * Returns the quantization bits for the color
     *
     * @return The value
     */
    public int getColorBits()
    {
        return this.colorBits;
    }

    /**
     * Set the quantization bits for the color
     *
     * @param value The value
     */
    public void setColorBits(int value)
    {
        this.colorBits = value;
    }

    /**
     * Returns the quantization bits for the normal
     *
     * @return The value
     */
    public int getNormalBits()
    {
        return this.normalBits;
    }

    /**
     * Set the quantization bits for the normal
     *
     * @param value The value
     */
    public void setNormalBits(int value)
    {
        this.normalBits = value;
    }

    /**
     * Returns the {@link CompressionLevel}
     *
     * @return the value
     */
    public CompressionLevel getCompressionLevel()
    {
        return this.compressionLevel;
    }

    /**
     * Set the {@link CompressionLevel}
     *
     * @param value The value
     */
    public void setCompressionLevel(CompressionLevel value)
    {
        this.compressionLevel = value;
    }
}
