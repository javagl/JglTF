package de.javagl.jgltf.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import de.javagl.jgltf.impl.v1.Buffer;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Image;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.io.v1.BinaryGltfV1;
import de.javagl.jgltf.model.io.v1.GltfAssetV1;
import de.javagl.jgltf.model.v1.GltfModelV1;

public class BasicTestIO
{
    public static void main(String[] args) throws Exception
    {
        String pathString = "C:/Develop/JglTF/TestModels/v1/Triangle/glTF/Triangle.gltf";
        URI uri = Paths.get(pathString).toUri();
        
        //testGltfReader(uri);
        //testGltfAssetReader(uri);
        //testGltfModelReader(uri);
        
        //testEmbeddedConversion(uri);
        

        testGltfModelLoadingWithTasks(uri);

    }
    
//    private static void testEmbeddedConversion(URI uri) throws IOException
//    {
//        GltfAssetReader gltfAssetReader = new GltfAssetReader();
//        InputStream inputStream = uri.toURL().openStream();
//        gltfAssetReader.read(inputStream);
//        
//        GltfAssetV1 asset = gltfAssetReader.getAsGltfAssetV1();
//        
//        UriReader uriReader = UriReaders.create(IO.getParent(uri));
//        GltfDataResolverV1 gltfDataResolver =
//            new GltfDataResolverV1(asset.getGltf(), asset.getGltfData(), uriReader);
//        gltfDataResolver.resolve();
//        
//        GltfDataToEmbeddedConverter x = new GltfDataToEmbeddedConverter();
//        GltfAssetV1 converted = x.convert(asset);
//        
//        System.out.println(converted);
//        Buffer yyy = converted.getGltf().getBuffers().values().iterator().next();
//        System.out.println(yyy.getUri());
//    }
    

    private static void testGltfAssetReader(URI uri) throws IOException
    {
        GltfAssetReader gltfAssetReader = new GltfAssetReader();
        InputStream inputStream = uri.toURL().openStream();
        gltfAssetReader.read(inputStream);
        
        GltfAssetV1 asset = gltfAssetReader.getAsGltfAssetV1();
        
//        UriReader uriReader = UriReaders.create(IO.getParent(uri));
//        GltfDataResolverV1 gltfDataResolver =
//            new GltfDataResolverV1(asset.getGltf(), asset.getGltfData(), uriReader);
//        gltfDataResolver.resolve();
        
        System.out.println(asset);
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

    private static void testGltfReader(URI uri) throws IOException
    {
        GltfReader gltfReader = new GltfReader();
        InputStream inputStream = uri.toURL().openStream();
        gltfReader.read(inputStream);
        System.out.println(gltfReader.getVersion());
        
        GlTF gltf = gltfReader.getAsGltfV1();
        System.out.println(gltf);
    }
    
    
    
    
    
    
    
    private static void testGltfModelLoadingWithTasks(URI uri) throws IOException
    {
        GltfReader gltfReader = new GltfReader();
        InputStream inputStream = uri.toURL().openStream();
        RawGltfData rawGltfData = RawGltfDataReader.read(inputStream);
        ByteBuffer jsonData = rawGltfData.getJsonData();
        try (InputStream jsonInputStream =
            Buffers.createByteBufferInputStream(jsonData))
        {
            gltfReader.read(jsonInputStream);
        }
        GlTF gltf = gltfReader.getAsGltfV1();
        
        GltfModelV1 gltfModel = new GltfModelV1(gltf, rawGltfData.getBinaryData());
        
//        UriReader uriReader = UriReaders.create(IO.getParent(uri));
//        GltfReferenceLoaderV1 gltfReferenceLoader =
//            gltfModel.createReferenceLoader();
//        gltfReferenceLoader.loadAll(uriReader);
        
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
