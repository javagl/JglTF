package de.javagl.jgltf.obj.v2;

import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.junit.Test;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.obj.v2.ObjGltfAssetCreatorV2;
import de.javagl.obj.Obj;
import de.javagl.obj.Objs;

@SuppressWarnings("javadoc")
public class TestObjAlignmentV2
{
    /**
     * Test whether the padding bytes are inserted that are necessary to
     * align the accessors to the size of their component type.
     */
    @Test
    public void testObjAlignment() 
    {
        // Create an OBJ consisting of a single triangle
        IntBuffer indices = IntBuffer.wrap(new int[] { 0,1,2 });
        FloatBuffer vertices = FloatBuffer.wrap(new float[] 
        {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
        });
        Obj obj = Objs.createFromIndexedTriangleData(
            indices, vertices, null, null);
        
        // Create the GltfData, using GL_UNSIGNED_SHORT indices, causing
        // an offset of 6 bytes. (This means that 2 padding bytes will
        // have to be inserted for the subsequent vertex positions data)
        ObjGltfAssetCreatorV2 objGltfAssetCreator = new ObjGltfAssetCreatorV2();
        objGltfAssetCreator.setIndicesComponentType(
            GltfConstants.GL_UNSIGNED_SHORT);
        GltfAssetV2 gltfAsset = 
            objGltfAssetCreator.convert(obj, null, "test", null);
        
        // Obtain the glTF and the accessor and buffer view of the vertices
        GlTF gltf = gltfAsset.getGltf();

        List<Accessor> accessors = gltf.getAccessors();
        Accessor accessor = accessors.get(1); 
        
        List<BufferView> bufferViews = gltf.getBufferViews();
        BufferView bufferView = bufferViews.get(0);
        
        // Compute the byte offset of the accessor referring to the
        // buffer view, and the total byte offset referring to the buffer
        int accessorByteOffset = accessor.getByteOffset();
        int totalByteOffset = 
            accessor.getByteOffset() + bufferView.getByteOffset();
        
        // Check whether the data is properly aligned
        final int sizeOfFloat = 4;
        assertEquals("Byte offset must be divisble by 4",
            0, accessorByteOffset % sizeOfFloat);
        assertEquals("Total byte offset must be divisible by 4",
            0, totalByteOffset % sizeOfFloat);
        
    }
}
