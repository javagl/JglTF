package de.javagl.jgltf.model;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class AccessorDataTests
{
    @Test
    public void testMatrixPaddingMat2Byte()
    {
        // Make sure that the AccessorData for MAT2 with byte components
        // takes into account the padding bytes that have to be inserted
        // after each column (indicated by a -1 here)
        byte mat[] = new byte[] 
        {
            1, 2, -1, -1,
            3, 4, -1, -1,
            2, 3, -1, -1,
            4, 5, -1, -1,
        };
        int numElements = 2;
        int componentType = GltfConstants.GL_BYTE;
        ElementType elementType = ElementType.MAT2;
        AccessorByteData ad = (AccessorByteData) AccessorDatas.create(
            componentType, ByteBuffer.wrap(mat), 0, 
            numElements, elementType, null);
        //System.out.println(AccessorDatas.createString(ad, 1));
        
        assertEquals(1, ad.get(0, 0));
        assertEquals(2, ad.get(0, 1));
        assertEquals(3, ad.get(0, 2));
        assertEquals(4, ad.get(0, 3));

        assertEquals(2, ad.get(1, 0));
        assertEquals(3, ad.get(1, 1));
        assertEquals(4, ad.get(1, 2));
        assertEquals(5, ad.get(1, 3));
    }
    
    @Test
    public void testMatrixPaddingMat3Byte()
    {
        // Make sure that the AccessorData for MAT3 with byte components
        // takes into account the padding bytes that have to be inserted
        // after each column (indicated by a -1 here)
        byte mat[] = new byte[] 
        {
            1, 2, 3, -1,
            4, 5, 6, -1,
            7, 8, 9, -1,
            2, 3, 4, -1,
            5, 6, 7, -1,
            8, 9, 0, -1,
        };
        int numElements = 2;
        int componentType = GltfConstants.GL_BYTE;
        ElementType elementType = ElementType.MAT3;
        AccessorByteData ad = (AccessorByteData)AccessorDatas.create(
            componentType, ByteBuffer.wrap(mat), 0, 
            numElements, elementType, null);
        //System.out.println(AccessorDatas.createString(ad, 1));
        
        assertEquals(1, ad.get(0, 0));
        assertEquals(2, ad.get(0, 1));
        assertEquals(3, ad.get(0, 2));
        assertEquals(4, ad.get(0, 3));
        assertEquals(5, ad.get(0, 4));
        assertEquals(6, ad.get(0, 5));
        assertEquals(7, ad.get(0, 6));
        assertEquals(8, ad.get(0, 7));
        assertEquals(9, ad.get(0, 8));

        assertEquals(2, ad.get(1, 0));
        assertEquals(3, ad.get(1, 1));
        assertEquals(4, ad.get(1, 2));
        assertEquals(5, ad.get(1, 3));
        assertEquals(6, ad.get(1, 4));
        assertEquals(7, ad.get(1, 5));
        assertEquals(8, ad.get(1, 6));
        assertEquals(9, ad.get(1, 7));
        assertEquals(0, ad.get(1, 8));
    }
    
    @Test
    public void testMatrixPaddingMat3Short()
    {
        // Make sure that the AccessorData for MAT3 with short components
        // takes into account the padding bytes that have to be inserted
        // after each column (indicated by a -1 here)
        short mat[] = new short[] 
        {
            1, 2, 3, -1,
            4, 5, 6, -1,
            7, 8, 9, -1,
            2, 3, 4, -1,
            5, 6, 7, -1,
            8, 9, 0, -1,
        };
        ByteBuffer bb = ByteBuffer.allocate(mat.length * 2);
        bb.asShortBuffer().put(mat);
        int numElements = 2;
        int componentType = GltfConstants.GL_SHORT;
        ElementType elementType = ElementType.MAT3;
        AccessorShortData ad = (AccessorShortData)AccessorDatas.create(
            componentType, bb, 0, 
            numElements, elementType, null);
        //System.out.println(AccessorDatas.createString(ad, 1));
        
        assertEquals(1, ad.get(0, 0));
        assertEquals(2, ad.get(0, 1));
        assertEquals(3, ad.get(0, 2));
        assertEquals(4, ad.get(0, 3));
        assertEquals(5, ad.get(0, 4));
        assertEquals(6, ad.get(0, 5));
        assertEquals(7, ad.get(0, 6));
        assertEquals(8, ad.get(0, 7));
        assertEquals(9, ad.get(0, 8));

        assertEquals(2, ad.get(1, 0));
        assertEquals(3, ad.get(1, 1));
        assertEquals(4, ad.get(1, 2));
        assertEquals(5, ad.get(1, 3));
        assertEquals(6, ad.get(1, 4));
        assertEquals(7, ad.get(1, 5));
        assertEquals(8, ad.get(1, 6));
        assertEquals(9, ad.get(1, 7));
        assertEquals(0, ad.get(1, 8));
    }
    
    @Test
    public void testVec2Byte()
    {
        // Make sure that the AccessorData for VEC2 with byte components
        // computes the right byte offsets, fixing the issue reported
        // at https://github.com/javagl/JglTF/issues/60
        byte vec[] = new byte[] 
        {
            1, 2,
            3, 4,
            2, 3,
            4, 5,
        };
        int numElements = 4;
        int componentType = GltfConstants.GL_BYTE;
        ElementType elementType = ElementType.VEC2;
        AccessorByteData ad = (AccessorByteData) AccessorDatas.create(
            componentType, ByteBuffer.wrap(vec), 0, 
            numElements, elementType, null);
        //System.out.println(AccessorDatas.createString(ad, 1));
        
        assertEquals(1, ad.get(0, 0));
        assertEquals(2, ad.get(0, 1));

        assertEquals(3, ad.get(1, 0));
        assertEquals(4, ad.get(1, 1));
        
        assertEquals(2, ad.get(2, 0));
        assertEquals(3, ad.get(2, 1));

        assertEquals(4, ad.get(3, 0));
        assertEquals(5, ad.get(3, 1));
    }
    
    @Test
    public void testVec2Short()
    {
        // Make sure that the AccessorData for VEC2 with short components
        // computes the right byte offsets, fixing the issue reported
        // at https://github.com/javagl/JglTF/issues/60
        short vec[] = new short[] 
        {
            1, 2,
            3, 4,
            2, 3,
            4, 5,
        };
        ByteBuffer bb = ByteBuffer.allocate(vec.length * 2);
        bb.asShortBuffer().put(vec);
        int numElements = 4;
        int componentType = GltfConstants.GL_SHORT;
        ElementType elementType = ElementType.VEC2;
        AccessorShortData ad = (AccessorShortData) AccessorDatas.create(
            componentType, bb, 0, 
            numElements, elementType, null);
        //System.out.println(AccessorDatas.createString(ad, 1));
        
        assertEquals(1, ad.get(0, 0));
        assertEquals(2, ad.get(0, 1));
        
        assertEquals(3, ad.get(1, 0));
        assertEquals(4, ad.get(1, 1));

        assertEquals(2, ad.get(2, 0));
        assertEquals(3, ad.get(2, 1));
        
        assertEquals(4, ad.get(3, 0));
        assertEquals(5, ad.get(3, 1));
    }
    
    
    
    
}
