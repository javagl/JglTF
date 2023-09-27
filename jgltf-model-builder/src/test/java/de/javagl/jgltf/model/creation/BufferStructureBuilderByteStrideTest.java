package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferViewModel;
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

    
    @Test
    public void testByteStrideForMatrixComponents()
    {
        byte data[] =
        {
            1, 2, -1, -1,
            3, 4, -1, -1,
            11, 22, -1, -1,
            33, 44, -1, -1,
        };
        
        BufferStructureBuilder b = new BufferStructureBuilder();
        
        b.createAccessorModel("accessor", data, "MAT2");
        b.createArrayBufferViewModel("buffer view");
        b.createBufferModel("buffer", "data.bin");
        
        // The accessor model will contain only the entries
        // 1, 2
        // 3, 4
        // 11, 22
        // 33, 44
        // The remaining bytes are padding bytes for the matrix
        // columns. In the resulting buffer, these bytes should
        // therefore remain 0 
        BufferStructure bufferStructure = b.build();
        ByteBuffer bufferData = 
            bufferStructure.getBufferModels().get(0).getBufferData();
        byte expected[] = new byte[] 
        {
            1, 2, 0, 0,
            3, 4, 0, 0,
            11, 22, 0, 0,
            33, 44, 0, 0,
        };
        byte actual[] = new byte[16];
        bufferData.slice().get(actual);
        assertArrayEquals(expected, actual);
    }

    
    @Test
    public void testByteStrideForAttributes()
    {
        // Create an accessor with GL_UNSIGNED_SHORT data, as part of
        // a buffer view model for GL_ARRAY_BUFFER, meaning that it 
        // will be a vertex attribute, which requires an alignment 
        // of 4 for each element
        short data[] =
        {
            1, 2, 3, 4
        };
        BufferStructureBuilder b = new BufferStructureBuilder();
        b.createAccessorModel("accessor", data, "SCALAR");
        b.createArrayBufferViewModel("buffer view");
        b.createBufferModel("buffer", "data.bin");
        
        BufferStructure bufferStructure = b.build();
        BufferViewModel bufferViewModel = 
            bufferStructure.getBufferViewModels().get(0);
        
        // The byte stride must be 4, because it is 
        // a vertex attribute
        Integer expectedByteStride = 4;
        Integer actualByteStride = bufferViewModel.getByteStride();

        int expectedByteLength = 16;
        int actualByteLength = bufferViewModel.getByteLength();

        ByteBuffer bufferData = 
            bufferStructure.getBufferModels().get(0).getBufferData();
        byte expectedBufferData[] = new byte[] 
        {
            1, 0, 0, 0,
            2, 0, 0, 0,
            3, 0, 0, 0,
            4, 0, 0, 0,
        };
        byte actualBufferData[] = new byte[bufferData.capacity()];
        bufferData.slice().get(actualBufferData);
        
        assertEquals(expectedByteStride, actualByteStride);
        assertEquals(expectedByteLength, actualByteLength);
        assertArrayEquals(expectedBufferData, actualBufferData);
    }

    @Test
    public void testByteStrideForNonAttributes()
    {
        // Create an accessor with GL_UNSIGNED_SHORT data, as part of
        // a buffer view model for GL_ARRAY_ELEMENT_BUFFER, meaning that it 
        // will NOT be a vertex attribute, and the elements can be 
        // tightly packed
        short data[] =
        {
            1, 2, 3, 4
        };
        BufferStructureBuilder b = new BufferStructureBuilder();
        b.createAccessorModel("accessor", data, "SCALAR");
        b.createArrayElementBufferViewModel("buffer view");
        b.createBufferModel("buffer", "data.bin");
        
        BufferStructure bufferStructure = b.build();
        BufferViewModel bufferViewModel = 
            bufferStructure.getBufferViewModels().get(0);
        
        // The byte stride must be null, because the
        // elements are tightly packed
        Integer expectedByteStride = null;
        Integer actualByteStride = bufferViewModel.getByteStride();

        int expectedByteLength = 8;
        int actualByteLength = bufferViewModel.getByteLength();

        ByteBuffer bufferData = 
            bufferStructure.getBufferModels().get(0).getBufferData();
        byte expectedBufferData[] = new byte[] 
        {
            1, 0,
            2, 0,
            3, 0,
            4, 0,
        };
        byte actualBufferData[] = new byte[bufferData.capacity()];
        bufferData.slice().get(actualBufferData);
        
        assertEquals(expectedByteStride, actualByteStride);
        assertEquals(expectedByteLength, actualByteLength);
        assertArrayEquals(expectedBufferData, actualBufferData);
    }
    
}
