/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Tests for creating glTF assets with morph targets<br>
 */
@SuppressWarnings("javadoc")
public class MeshPrimitiveBuilderMorphTargetsTangentsTest
{
    @Test
    public void testBuildTangentTargets() throws IOException
    {
        // Regression test for https://github.com/javagl/JglTF/issues/167

        // @formatter:off
        int indices[] = { 0, 1, 2, };
        float positions[] =
        { 
            0.0f, 0.0f, 0.0f, 
            1.0f, 0.0f, 0.0f, 
            0.5f, 0.5f, 0.0f, 
        };
        // The tangents are always VEC4
        float tangents[] =
        { 
            1.0f, 0.0f, 0.0f, 1.0f, 
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f
        };
        // The tangent displacements are always VEC3
        float tangentDisplacement[] =
        { 
            -1.0f, 1.0f, 0.0f, 
            -1.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
        };
        // @formatter:on
        MeshPrimitiveBuilder b = MeshPrimitiveBuilder.create();
        b.setTriangles();
        b.setIntIndicesAsShort(IntBuffer.wrap(indices));
        b.addPositions3D(FloatBuffer.wrap(positions));
        b.addTangents4D(FloatBuffer.wrap(tangents));
        
        // The core of the test: The morph target will automatically be
        // assumed to be VEC3 here.
        b.addMorphTarget(0, "TANGENT", Buffers
            .createByteBufferFrom(FloatBuffer.wrap(tangentDisplacement)));

        DefaultMeshPrimitiveModel mp = b.build();

        List<Map<String, AccessorModel>> targets = mp.getTargets();

        assertNotNull(targets);
        assertEquals(1, targets.size());

        Map<String, AccessorModel> target = targets.get(0);
        AccessorModel morphData = target.get("TANGENT");
        assertEquals(morphData.getElementType(), ElementType.VEC3);
    }

}
