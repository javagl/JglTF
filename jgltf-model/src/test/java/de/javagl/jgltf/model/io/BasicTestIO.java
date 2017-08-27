package de.javagl.jgltf.model.io;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Locale;

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.GltfModel;

public class BasicTestIO
{
    public static void main(String[] args) throws Exception
    {
        String pathString = null;
        //pathString = "C:/Develop/JglTF/TestModels/v1/Triangle/glTF/";
        //pathString = "C:/Develop/JglTF/TestModels/v1/Triangle/glTF-Embedded/";
        pathString = "C:/Develop/JglTF/TestModels/v2/Triangle/glTF/";
        //pathString = "C:/Develop/JglTF/TestModels/v2/Triangle/glTF-Embedded/";
        String modelFileName = "Triangle.gltf";
        URI uri = Paths.get(pathString + modelFileName).toUri();
        
        testGltfModelReader(uri);
    }
    
    private static void testGltfModelReader(URI uri) throws IOException
    {
        GltfModelReader gltfModelReader = new GltfModelReader();
        GltfModel gltfModel = gltfModelReader.read(uri);
        System.out.println(gltfModel);
        
        for (AccessorModel accessorModel : gltfModel.getAccessorModels())
        {
            AccessorData accessorData = accessorModel.getAccessorData();
            System.out.println(accessorData);
            if (accessorData.getComponentType() == short.class)
            {
                AccessorShortData accessorShortData = (AccessorShortData)accessorData;
                System.out.println(accessorShortData.createString(Locale.ENGLISH, "%d", -1));
            }
            if (accessorData.getComponentType() == float.class)
            {
                AccessorFloatData accessorFloatData = (AccessorFloatData)accessorData;
                System.out.println(accessorFloatData.createString(Locale.ENGLISH, "%.2f", -1));
            }
        }
    }

}
