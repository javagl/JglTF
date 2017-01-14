package de.javagl.jgltf.obj;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import org.junit.Test;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfData;
import de.javagl.obj.Obj;
import de.javagl.obj.Objs;

@SuppressWarnings("javadoc")
public class TestObjAlignment
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
        ObjGltfDataCreator objGltfDataCreator = new ObjGltfDataCreator();
        objGltfDataCreator.setIndicesComponentType(
            GltfConstants.GL_UNSIGNED_SHORT);
        GltfData gltfData = objGltfDataCreator.convert(obj, null, "test", null);
        
        // Obtain the glTF and the accessor and buffer view of the vertices
        GlTF gltf = gltfData.getGltf();

        Map<String, Accessor> accessors = gltf.getAccessors();
        Accessor accessor = accessors.get("verticesAccessor"); 
        
        Map<String, BufferView> bufferViews = gltf.getBufferViews();
        BufferView bufferView = bufferViews.get("verticesBufferView");
        
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
