package de.javagl.jgltf.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.function.Supplier;

import org.junit.Test;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.model.io.GltfReader;

@SuppressWarnings("javadoc")
public class TestTechniqueParametersTypes
{
    /**
     * Test whether the types of the objects that are returned by the 
     * suppliers created from {@link GltfModels#createGenericSupplier}
     * are the expected types
     * 
     * @throws IOException If the input glTF can not be read
     */
    @Test
    public void testTechniqueParametersTypes() throws IOException 
    {
        InputStream inputStream = 
            TestTechniqueParametersTypes.class.getResourceAsStream(
                "/testModels/testTechniqueParametersTypes/" + 
                "techniqueParametersTypes.gltf");
        GltfReader gltfReader = new GltfReader();
        GlTF gltf = gltfReader.readGltf(inputStream);
        
        Technique technique = gltf.getTechniques().get("technique0");
        Material material = new Material();
        
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleInt", technique, material), 
            Integer.class, -1);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleUnsignedInt", technique, material), 
            Integer.class, -1);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleFloat", technique, material), 
            Float.class, -1);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleIntVec3", technique, material), 
            int[].class, 3);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleFloatVec3", technique, material), 
            float[].class, 3);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleElementIntArray", technique, material), 
            int[].class, 1);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleElementUnsignedIntArray", technique, material), 
            int[].class, 1);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleElementFloatArray", technique, material), 
            float[].class, 1);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleElementIntVec3Array", technique, material), 
            int[].class, 3);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "singleElementFloatVec3Array", technique, material), 
            float[].class, 3);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "intVec3Array", technique, material), 
            int[].class, 2 * 3);
            
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "floatVec3Array", technique, material), 
            float[].class, 2 * 3);
        
        assertReturnTypeIs(GltfModels.createGenericSupplier(
            "sampler2D", technique, material), 
            String.class, -1);
        
   }
    
    private static void assertReturnTypeIs(
        Supplier<?> supplier, Class<?> c, int arrayLength)
    {
        Object object = supplier.get();
        assertNotNull(object);
        assertEquals(object.getClass(), c);
        
        if (arrayLength >= 0)
        {
            assertTrue(object.getClass().isArray());
            assertEquals(Array.getLength(object), arrayLength);
        }
    }
    
}