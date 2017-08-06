package de.javagl.jgltf.viewer.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Array;
//import java.util.function.Supplier;
//
//import org.junit.Test;
//
//import de.javagl.jgltf.impl.v1.GlTF;
//import de.javagl.jgltf.impl.v1.Material;
//import de.javagl.jgltf.impl.v1.Technique;
//import de.javagl.jgltf.model.io.v1.GltfReaderV1;
//
//@SuppressWarnings("javadoc")
//public class TestTechniqueParametersTypes
//{
//    /**
//     * Test whether the types of the objects that are returned by the 
//     * suppliers created from {@link GltfModels#createGenericSupplier}
//     * are the expected types
//     * 
//     * @throws IOException If the input glTF can not be read
//     */
//    @Test
//    public void testTechniqueParametersTypes() throws IOException 
//    {
//        GlTF gltf = null;
//        try (InputStream inputStream = 
//            TestTechniqueParametersTypes.class.getResourceAsStream(
//                "/testModels/testTechniqueParametersTypes/" + 
//                "techniqueParametersTypes.gltf"))
//        {
//            GltfReaderV1 gltfReader = new GltfReaderV1();
//            gltf = gltfReader.read(inputStream);
//        }
//        
//        Technique technique = gltf.getTechniques().get("technique0");
//        Material material = new Material();
//        
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleInt", technique, material), 
//            int[].class, 1);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleUnsignedInt", technique, material), 
//            int[].class, 1);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleFloat", technique, material), 
//            float[].class, 1);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleIntVec3", technique, material), 
//            int[].class, 3);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleFloatVec3", technique, material), 
//            float[].class, 3);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleElementIntArray", technique, material), 
//            int[].class, 1);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleElementUnsignedIntArray", technique, material), 
//            int[].class, 1);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleElementFloatArray", technique, material), 
//            float[].class, 1);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleElementIntVec3Array", technique, material), 
//            int[].class, 3);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "singleElementFloatVec3Array", technique, material), 
//            float[].class, 3);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "intVec3Array", technique, material), 
//            int[].class, 2 * 3);
//            
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "floatVec3Array", technique, material), 
//            float[].class, 2 * 3);
//        
//        assertReturnTypeIs(UniformGetters.createGenericSupplier(
//            "sampler2D", technique, material), 
//            String[].class, 1);
//   }
//    
//    private static void assertReturnTypeIs(
//        Supplier<?> supplier, Class<?> c, int arrayLength)
//    {
//        Object object = supplier.get();
//        assertNotNull(object);
//        assertEquals(object.getClass(), c);
//        
//        if (arrayLength >= 0)
//        {
//            assertTrue(object.getClass().isArray());
//            assertEquals(Array.getLength(object), arrayLength);
//        }
//    }
//    
//}