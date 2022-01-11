package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;

@SuppressWarnings("javadoc")
public class AccessorModelsTest
{
    @Test
    public void testCommonVertexAttributeByteStride()
    {
        DefaultAccessorModel accessorModel0 = new DefaultAccessorModel(
            GltfConstants.GL_FLOAT, 0, ElementType.VEC3);
        
        DefaultAccessorModel accessorModel1 = new DefaultAccessorModel(
            GltfConstants.GL_FLOAT, 0, ElementType.VEC2);
        
        DefaultAccessorModel accessorModel2 = new DefaultAccessorModel(
            GltfConstants.GL_FLOAT, 0, ElementType.VEC3);
        
        List<AccessorModel> accessorModels = 
            Arrays.asList(accessorModel0, accessorModel1, accessorModel2);
        int expectedCommonByteStride = 12; 
        int actualCommonByteStride = 
            AccessorModels.computeCommonVertexAttributeByteStride(
                accessorModels);
        assertEquals(expectedCommonByteStride, actualCommonByteStride);
    }
    
    @Test
    public void testCommonVertexAttributeByteStrideForBytes()
    {
        DefaultAccessorModel accessorModel0 = new DefaultAccessorModel(
            GltfConstants.GL_UNSIGNED_BYTE, 0, ElementType.VEC3);

        List<AccessorModel> accessorModels = 
            Arrays.asList(accessorModel0);
        int expectedCommonByteStride = 4; 
        int actualCommonByteStride = 
            AccessorModels.computeCommonVertexAttributeByteStride(
                accessorModels);
        assertEquals(expectedCommonByteStride, actualCommonByteStride);
    }
    
    @Test
    public void testCommonVertexAttributeByteStrideForMat2Bytes()
    {
        DefaultAccessorModel accessorModel0 = new DefaultAccessorModel(
            GltfConstants.GL_UNSIGNED_BYTE, 0, ElementType.MAT2);

        List<AccessorModel> accessorModels = 
            Arrays.asList(accessorModel0);
        
        // This includes the column padding, see 3.6.2.4. Data Alignment
        int expectedCommonByteStride = 8;
        int actualCommonByteStride = 
            AccessorModels.computeCommonVertexAttributeByteStride(
                accessorModels);
        assertEquals(expectedCommonByteStride, actualCommonByteStride);
    }
    
    
}
