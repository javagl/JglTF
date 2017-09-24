package de.javagl.jgltf.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Scene;

@SuppressWarnings("javadoc")
public class TestValidator
{
    @Test
    public void testEmptyGltfPasses() 
    {
        GlTF gltf = new GlTF();
        Validator validator = new Validator(gltf);
        ValidatorResult validatorResult = validator.validate();
        assertFalse(validatorResult.hasErrors());
    }
    
    @Test
    public void testInvalidNodeId() 
    {
        GlTF gltf = new GlTF();
        Scene scene = new Scene();
        scene.addNodes("INVALID_ID");
        gltf.addScenes("scene0", scene);
        
        Validator validator = new Validator(gltf);
        ValidatorResult validatorResult = validator.validate();
        assertEquals(1, validatorResult.getErrors().size());
    }
    
    //String uriString = "https://raw.githubusercontent.com/javagl/" + 
    //    "gltfTutorialModels/master/" + 
    //    "SimpleSkin/glTF-Embedded-buffers/SimpleSkin.gltf";
    //GltfDataReader gltfDataReader = new GltfDataReader();
    //GltfData gltfData = gltfDataReader.readGltfData(new URI(uriString));
    //gltf = gltfData.getGltf();
    
    
}
