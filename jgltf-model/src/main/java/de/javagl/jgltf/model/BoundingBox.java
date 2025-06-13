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
package de.javagl.jgltf.model;

import java.util.Objects;

/**
 * A very simple (package-private!) bounding box implementation
 */
class BoundingBox
{
    /**
     * The minimum x coordinate
     */
    private double minX;
    
    /**
     * The minimum y coordinate
     */
    private double minY;
    
    /**
     * The minimum z coordinate
     */
    private double minZ;
    
    /**
     * The maximum x coordinate
     */
    private double maxX;
    
    /**
     * The maximum y coordinate
     */
    private double maxY;
    
    /**
     * The maximum z coordinate
     */
    private double maxZ;

    /**
     * Creates a bounding box  
     */
    BoundingBox()
    {
        minX = Float.MAX_VALUE;
        minY = Float.MAX_VALUE;
        minZ = Float.MAX_VALUE;
        maxX = -Float.MAX_VALUE;
        maxY = -Float.MAX_VALUE;
        maxZ = -Float.MAX_VALUE;
    }
    
    /**
     * Combine this bounding box with the given point
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     */
    void combine(double x, double y, double z)
    {
        minX = Math.min(minX, x);
        minY = Math.min(minY, y);
        minZ = Math.min(minZ, z);
        maxX = Math.max(maxX, x);
        maxY = Math.max(maxY, y);
        maxZ = Math.max(maxZ, z);
    }

    /**
     * Combine this bounding box with the given one
     * 
     * @param other The other bounding box
     */
    void combine(BoundingBox other)
    {
        Objects.requireNonNull(other, "The other bounding box may not be null");
        minX = Math.min(minX, other.getMinX());
        minY = Math.min(minY, other.getMinY());
        minZ = Math.min(minZ, other.getMinZ());
        maxX = Math.max(maxX, other.getMaxX());
        maxY = Math.max(maxY, other.getMaxY());
        maxZ = Math.max(maxZ, other.getMaxZ());
    }

    /**
     * Returns the x-coordinate of the center
     *
     * @return The x-coordinate of the center
     */
    double getCenterX()
    {
        return getMinX() + getSizeX() * 0.5;
    }

    /**
     * Returns the y-coordinate of the center
     *
     * @return The y-coordinate of the center
     */
    double getCenterY()
    {
        return getMinY() + getSizeY() * 0.5;
    }

    /**
     * Returns the z-coordinate of the center
     *
     * @return The z-coordinate of the center
     */
    double getCenterZ()
    {
        return getMinZ() + getSizeZ() * 0.5;
    }

    /**
     * Returns the size in x-direction
     *
     * @return The size in x-direction
     */
    double getSizeX()
    {
        return getMaxX() - getMinX();
    }

    /**
     * Returns the size in y-direction
     *
     * @return The size in y-direction
     */
    double getSizeY()
    {
        return getMaxY() - getMinY();
    }

    /**
     * Returns the size in z-direction
     *
     * @return The size in z-direction
     */
    double getSizeZ()
    {
        return getMaxZ() - getMinZ();
    }

    /**
     * Returns the minimum x coordinate
     *
     * @return The minimum x coordinate
     */
    double getMinX()
    {
        return minX;
    }

    /**
     * Returns the minimum y coordinate
     *
     * @return The minimum y coordinate
     */
    double getMinY()
    {
        return minY;
    }

    /**
     * Returns the minimum z coordinate
     *
     * @return The minimum z coordinate
     */
    double getMinZ()
    {
        return minZ;
    }

    /**
     * Returns the maximum x coordinate
     *
     * @return The maximum x coordinate
     */
    double getMaxX()
    {
        return maxX;
    }

    /**
     * Returns the maximum y coordinate
     *
     * @return The maximum y coordinate
     */
    double getMaxY()
    {
        return maxY;
    }

    /**
     * Returns the maximum z coordinate
     *
     * @return The maximum z coordinate
     */
    double getMaxZ()
    {
        return maxZ;
    }
    
    @Override
    public String toString()
    {
        return "[(" + 
            getMinX() + "," + getMinY() + "," + getMinZ() + ")-(" + 
            getMaxX() + "," + getMaxY() + "," + getMaxZ() + ")]";
    }
}