package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;

@SuppressWarnings("javadoc")
public class BufferStructureBuilderByteStrideTest
{
    @Test
    public void testByteStrideRemainsForSingleNonVertexAttribute()
    {
        BufferStructureBuilder b = new BufferStructureBuilder();
        
        int numElements = 3;
        byte vec3[] = new byte[3 * numElements];
        Arrays.fill(vec3, (byte)1);
        
        b.createAccessorModel("accessor", vec3, "VEC3");
        b.createArrayElementBufferViewModel("bufferView");
              
        b.createBufferModel("buffer", "example.bin");
        BufferStructure bufferStructure = b.build();

        // For array ELEMENT buffer views, the byte stride for a
        // single accessor should be the element size 
        List<DefaultAccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        AccessorModel amVec3 = accessorModels.get(0);
        assertEquals(3, amVec3.getByteStride());
    }

    @Test
    public void testByteStrideRemainsForMultipleNonVertexAttributes()
    {
        BufferStructureBuilder b = new BufferStructureBuilder();
        
        int numElements = 3;
        byte vec2[] = new byte[2 * numElements];
        byte vec3[] = new byte[3 * numElements];
        Arrays.fill(vec2, (byte)1);
        Arrays.fill(vec3, (byte)1);
        
        b.createAccessorModel("accessor", vec2, "VEC2");
        b.createAccessorModel("accessor", vec3, "VEC3");
        b.createArrayElementBufferViewModel("bufferView");
              
        b.createBufferModel("buffer", "example.bin");
        BufferStructure bufferStructure = b.build();

        // For array ELEMENT buffer views, the byte stride for 
        // multiple accessors should be the element sizes 
        List<DefaultAccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        AccessorModel amVec2 = accessorModels.get(0);
        AccessorModel amVec3 = accessorModels.get(1);

        assertEquals(2, amVec2.getByteStride());
        assertEquals(3, amVec3.getByteStride());
    }
    
    @Test
    public void testByteStrideMultipleOfFourForSingleVecVertexAttribute()
    {
        BufferStructureBuilder b = new BufferStructureBuilder();
        
        int numElements = 3;
        byte vec3[] = new byte[3 * numElements];
        Arrays.fill(vec3, (byte)1);
        
        b.createAccessorModel("accessor", vec3, "VEC3");
        b.createArrayBufferViewModel("bufferView");
              
        b.createBufferModel("buffer", "example.bin");
        BufferStructure bufferStructure = b.build();

        // For array buffer views (vertex attributes), the byte stride for a
        // single accessor should be divisible by 4 
        List<DefaultAccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        AccessorModel amVec3 = accessorModels.get(0);
        assertEquals(4, amVec3.getByteStride());
    }

    @Test
    public void testByteStrideMultipleOfFourForSingleMatVertexAttribute()
    {
        BufferStructureBuilder b = new BufferStructureBuilder();
        
        int numElements = 3;
        byte vec3[] = new byte[3 * numElements];
        Arrays.fill(vec3, (byte)1);
        
        b.createAccessorModel("accessor", vec3, "VEC3");
        b.createArrayBufferViewModel("bufferView");
              
        b.createBufferModel("buffer", "example.bin");
        BufferStructure bufferStructure = b.build();

        // For array buffer views (vertex attributes), the byte stride for a
        // single accessor should be divisible by 4 
        List<DefaultAccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        AccessorModel amVec3 = accessorModels.get(0);
        assertEquals(4, amVec3.getByteStride());
    }
    
    @Test
    public void testByteStrideCommonForMultipleVertexAttributes()
    {
        BufferStructureBuilder b = new BufferStructureBuilder();
        
        int numElements = 3;
        byte vec2[] = new byte[2 * numElements]; 
        byte vec3[] = new byte[3 * numElements];
        Arrays.fill(vec2, (byte)1);
        Arrays.fill(vec3, (byte)1);
        
        b.createAccessorModel("accessor", vec2, "VEC2");
        b.createAccessorModel("accessor", vec3, "VEC3");
        b.createArrayBufferViewModel("bufferView");
              
        b.createBufferModel("buffer", "example.bin");
        BufferStructure bufferStructure = b.build();
        
        // For array buffer views (vertex attributes), the byte stride for
        // multiple accessors should be the same, namely the smallest
        // multiple of 4 that is at least as large as any element size
        List<DefaultAccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        AccessorModel amVec2 = accessorModels.get(0);
        AccessorModel amVec3 = accessorModels.get(1);
        assertEquals(4, amVec2.getByteStride());
        assertEquals(4, amVec3.getByteStride());
    }
    
    @Test
    public void testByteStrideForMatrixVertexAttributes()
    {
        BufferStructureBuilder b = new BufferStructureBuilder();
        
        int numElements = 2;
        byte mat2[] = new byte[] 
        {
            1, 2, -1, -1,
            3, 4, -1, -1,
            2, 3, -1, -1,
            4, 5, -1, -1,
        };
        
        float vec3[] = new float[3 * numElements];
        Arrays.fill(vec3, 1.0f);
        
        b.createAccessorModel("accessor", mat2, "MAT2");
        b.createAccessorModel("accessor", vec3, "VEC3");
        b.createArrayBufferViewModel("bufferView");
              
        b.createBufferModel("buffer", "example.bin");
        BufferStructure bufferStructure = b.build();
        
        // For array buffer views (vertex attributes), the byte stride for
        // multiple accessors should be the same, namely the smallest
        // multiple of 4 that is at least as large as any element size
        List<DefaultAccessorModel> accessorModels = 
            bufferStructure.getAccessorModels();
        AccessorModel amMat2 = accessorModels.get(0);
        AccessorModel amVec3 = accessorModels.get(1);
        assertEquals(12, amMat2.getByteStride());
        assertEquals(12, amVec3.getByteStride());
        
        //System.out.println(AccessorDatas.createString(
        //    amMat2.getAccessorData(), 1));
    }

    
}
