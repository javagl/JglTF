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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjGroup;
import de.javagl.obj.ObjUtils;
import de.javagl.obj.ReadableObj;

/**
 * Methods for splitting an OBJ into multiple parts. This class is package
 * private and only used internally. Many details about the behavior of
 * this class are intentionally not specified.
 */
class ObjSplitting
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ObjGltfDataCreator.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.INFO;

    /**
     * The maximum number of vertices that may be contained in one OBJ
     */
    private static final int MAX_NUM_VERTICES = 65534; 
    
    /**
     * The predicate that says whether an OBJ has to be split
     */
    private static Predicate<? super ReadableObj> OBJ_SPLIT_PREDICATE =
        obj -> obj.getNumVertices() > MAX_NUM_VERTICES;

    /**
     * Split the given OBJ into multiple parts.
     * 
     * @param obj The input OBJ
     * @return The list of resulting OBJs
     */
    static List<? extends ReadableObj> split(ReadableObj obj)
    {
        List<ReadableObj> currentObjs = Collections.singletonList(obj);
        List<ReadableObj> nextObjs = new ArrayList<ReadableObj>();
        boolean didSplit = false;
        while (true)
        {
            didSplit = false;
            for (int i=0; i<currentObjs.size(); i++)
            {
                ReadableObj currentObj = currentObjs.get(i);
                if (OBJ_SPLIT_PREDICATE.test(currentObj))
                {
                    List<Obj> parts = splitSingle(currentObj);
                    nextObjs.addAll(parts);
                    didSplit = true;
                }
                else
                {
                    nextObjs.add(currentObj);
                }
            }
            currentObjs = nextObjs;
            if (!didSplit)
            {
                break;
            }
            nextObjs = new ArrayList<ReadableObj>();
        }
        return currentObjs;
    }
    
    /**
     * Split a single OBJ into two parts
     * 
     * @param obj The input OBJ
     * @return The list containing the parts
     */
    private static List<Obj> splitSingle(ReadableObj obj)
    {
        logger.log(level,
            "Splitting OBJ with " + obj.getNumVertices() + " vertices");

        Predicate<ObjFace> facePredicate = computeFacePredicate(obj);
        List<ObjFace> faces0 = new ArrayList<ObjFace>();
        List<ObjFace> faces1 = new ArrayList<ObjFace>();
        for (int i=0; i<obj.getNumFaces(); i++)
        {
            ObjFace face = obj.getFace(i);
            if (facePredicate.test(face))
            {
                faces0.add(face);
            }
            else
            {
                faces1.add(face);
            }
        }
        Obj obj0 = ObjUtils.groupToObj(obj, asGroup(faces0), null);
        Obj obj1 = ObjUtils.groupToObj(obj, asGroup(faces1), null);
        return Arrays.asList(obj0, obj1);
        
    }
    
    /**
     * Returns a new OBJ group that is a view on the given list
     * 
     * @param faces The list of faces
     * @return The OBJ group
     */
    private static ObjGroup asGroup(List<? extends ObjFace> faces)
    {
        return new ObjGroup()
        {
            @Override
            public String getName()
            {
                return "default";
            }

            @Override
            public int getNumFaces()
            {
                return faces.size();
            }
            
            @Override
            public ObjFace getFace(int index)
            {
                return faces.get(index);
            }
        };
    }
    
    /**
     * Compute the {@link BoundingBox} of the given OBJ
     * 
     * @param obj The OBJ
     * @return The bounding box
     */
    private static BoundingBox computeBoundingBox(ReadableObj obj)
    {
        BoundingBox boundingBox = new BoundingBox();
        for (int i=0; i<obj.getNumVertices(); i++)
        {
            FloatTuple vertex = obj.getVertex(i);
            boundingBox.combine(vertex.getX(), vertex.getY(), vertex.getZ());
        }
        return boundingBox;
    }
    
    /**
     * Compute the specified component of the center position of the given 
     * OBJ face
     * 
     * @param obj The OBJ
     * @param face The face
     * @param component The component, 0,1 or 2 for x, y or z.
     * @return The center
     */
    private static float computeFaceCenter(
        ReadableObj obj, ObjFace face, int component)
    {
        float sum = 0;
        int n = face.getNumVertices();
        for (int i=0; i<n; i++)
        {
            int vertexIndex = face.getVertexIndex(i);
            FloatTuple vertex = obj.getVertex(vertexIndex);
            sum += vertex.get(component);
        }
        return sum / n;
    }
    
    
    /**
     * Create a predicate that divides the set of faces of the given OBJ
     * into two parts. Further details are not specified.
     * 
     * @param obj The OBJ
     * @return The predicate
     */
    private static Predicate<ObjFace> computeFacePredicate(
        ReadableObj obj)
    {
        BoundingBox boundingBox = computeBoundingBox(obj);
        
        if (boundingBox.getSizeX() > boundingBox.getSizeY())
        {
            if (boundingBox.getSizeX() > boundingBox.getSizeZ())
            {
                float centerX = boundingBox.getCenterX();
                return objFace -> 
                {
                    float faceCenterX = computeFaceCenter(obj, objFace, 0);
                    return faceCenterX > centerX;
                };
            }
            float centerZ = boundingBox.getCenterZ();
            return objFace -> 
            {
                float faceCenterZ = computeFaceCenter(obj, objFace, 2);
                return faceCenterZ > centerZ;
            };
        }
        if (boundingBox.getSizeY() > boundingBox.getSizeZ())
        {
            float centerY = boundingBox.getCenterY();
            return objFace -> 
            {
                float faceCenterY = computeFaceCenter(obj, objFace, 1);
                return faceCenterY > centerY;
            };
        }
        float centerZ = boundingBox.getCenterZ();
        return objFace -> 
        {
            float faceCenterZ = computeFaceCenter(obj, objFace, 2);
            return faceCenterZ > centerZ;
        };
    }
    
    /**
     * A very simple bounding box implementation - yes, with noisy JavaDocs
     */
    private static class BoundingBox
    {
        /**
         * The minimum x coordinate
         */
        private float minX;
        
        /**
         * The minimum y coordinate
         */
        private float minY;
        
        /**
         * The minimum z coordinate
         */
        private float minZ;
        
        /**
         * The maximum x coordinate
         */
        private float maxX;
        
        /**
         * The maximum y coordinate
         */
        private float maxY;
        
        /**
         * The maximum z coordinate
         */
        private float maxZ;

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
        void combine(float x, float y, float z)
        {
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
        }

        /**
         * Returns the x-coordinate of the center
         *
         * @return The x-coordinate of the center
         */
        float getCenterX()
        {
            return getMinX() + getSizeX() * 0.5f;
        }

        /**
         * Returns the y-coordinate of the center
         *
         * @return The y-coordinate of the center
         */
        float getCenterY()
        {
            return getMinY() + getSizeY() * 0.5f;
        }

        /**
         * Returns the z-coordinate of the center
         *
         * @return The z-coordinate of the center
         */
        float getCenterZ()
        {
            return getMinZ() + getSizeZ() * 0.5f;
        }

        /**
         * Returns the size in x-direction
         *
         * @return The size in x-direction
         */
        float getSizeX()
        {
            return getMaxX() - getMinX();
        }

        /**
         * Returns the size in y-direction
         *
         * @return The size in y-direction
         */
        float getSizeY()
        {
            return getMaxY() - getMinY();
        }

        /**
         * Returns the size in z-direction
         *
         * @return The size in z-direction
         */
        float getSizeZ()
        {
            return getMaxZ() - getMinZ();
        }

        /**
         * Returns the minimum x coordinate
         *
         * @return The minimum x coordinate
         */
        float getMinX()
        {
            return minX;
        }

        /**
         * Returns the minimum y coordinate
         *
         * @return The minimum y coordinate
         */
        float getMinY()
        {
            return minY;
        }

        /**
         * Returns the minimum z coordinate
         *
         * @return The minimum z coordinate
         */
        float getMinZ()
        {
            return minZ;
        }

        /**
         * Returns the maximum x coordinate
         *
         * @return The maximum x coordinate
         */
        float getMaxX()
        {
            return maxX;
        }

        /**
         * Returns the maximum y coordinate
         *
         * @return The maximum y coordinate
         */
        float getMaxY()
        {
            return maxY;
        }

        /**
         * Returns the maximum z coordinate
         *
         * @return The maximum z coordinate
         */
        float getMaxZ()
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
    
    /**
     * Private constructor to prevent instantiation
     */
    private ObjSplitting()
    {
        // Private constructor to prevent instantiation
    }

}
