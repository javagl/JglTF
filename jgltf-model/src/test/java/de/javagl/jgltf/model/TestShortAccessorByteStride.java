package de.javagl.jgltf.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.javagl.jgltf.model.io.GltfModelReader;

@SuppressWarnings("javadoc")
public class TestShortAccessorByteStride
{
    @Test
    public void testShortAccessorByteStride() throws IOException 
    {
        // The input file contains a single accessor, bufferView, and buffer
        // that represent 3 short values. The buffer view has a byte stride
        // of 4 (for being used as a vertex attribute). 
        // Previous versions of the glTF model reader did not properly 
        // pass the byte stride information to the accessor model.
        
        String basePath = "./src/test/resources/testModels/v2/"; 
        Path inputPath = Paths.get(basePath);
        String inputFileName = "testShortAccessorByteStride.gltf";
        Path inputFile = Paths.get(inputPath.toString(), inputFileName);
        
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(inputFile.toUri());
        
        AccessorModel accessorModel = gltfModel.getAccessorModels().get(0);
        AccessorShortData accessorData = 
            (AccessorShortData) accessorModel.getAccessorData();
        
        System.out.println(accessorData.get(0));
        System.out.println(accessorData.get(1));
        System.out.println(accessorData.get(2));

        assertEquals(12, accessorData.get(0));
        assertEquals(23, accessorData.get(1));
        assertEquals(34, accessorData.get(2));
        
    }
    
    // The code for creating the data of the input file, for reference:
    /*
    void creation()
    {
        int elementCount = 3;
        int byteStride = 4;
        ElementType elementType = ElementType.SCALAR;
        
        DefaultBufferModel bufferModel = new DefaultBufferModel();
        ByteBuffer bb = ByteBuffer.allocate(elementCount * byteStride);
        ShortBuffer sb = bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        sb.put(0, (short)12);
        sb.put(2, (short)23);
        sb.put(4, (short)34);
        bufferModel.setBufferData(bb);
        
        DefaultBufferViewModel bufferViewModel = 
            new DefaultBufferViewModel(null);
        bufferViewModel.setBufferModel(bufferModel);
        bufferViewModel.setByteStride(byteStride);
        bufferViewModel.setByteLength(elementCount * byteStride);
        
        DefaultAccessorModel accessorModel = new DefaultAccessorModel(
            GltfConstants.GL_UNSIGNED_SHORT, elementCount, elementType);
        accessorModel.setBufferViewModel(bufferViewModel);
    }
    */
}
