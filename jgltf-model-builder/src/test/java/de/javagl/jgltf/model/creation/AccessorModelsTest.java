package de.javagl.jgltf.model.creation;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.creation.AccessorModels;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;

@SuppressWarnings("javadoc")
public class AccessorModelsTest
{
    @Test
    public void testCommonByteStride()
    {
        DefaultAccessorModel accessorModel0 = new DefaultAccessorModel(
            GltfConstants.GL_FLOAT, 0, ElementType.VEC3);
        accessorModel0.setByteStride(3 * Float.BYTES);
        DefaultAccessorModel accessorModel1 = new DefaultAccessorModel(
            GltfConstants.GL_FLOAT, 0, ElementType.VEC2);
        accessorModel1.setByteStride(2 * Float.BYTES);
        DefaultAccessorModel accessorModel2 = new DefaultAccessorModel(
            GltfConstants.GL_FLOAT, 0, ElementType.VEC3);
        accessorModel2.setByteStride(3 * Float.BYTES);
        List<AccessorModel> accessorModels = 
            Arrays.asList(accessorModel0, accessorModel1, accessorModel2);
        int expectedCommonByteStride = 12; 
        int actualCommonByteStride = 
            AccessorModels.computeCommonByteStride(accessorModels);
        assertEquals(expectedCommonByteStride, actualCommonByteStride);
    }
    
    
}
