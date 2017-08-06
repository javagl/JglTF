package de.javagl.jgltf.validator;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.model.io.v1.GltfReaderV1;

/**
 * Utility to validate the sample models. For internal use only.
 */
@SuppressWarnings("javadoc")
public class ValidateAll
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(ValidateAll.class.getName());
    
    public static void main(String[] args) throws Exception
    {
        //List<String> allUriStrings = getKhronosSampleModelsUriStrings();
        List<String> allUriStrings = getTutorialSampleModelsUriStrings();
        
        List<String> uriStrings = allUriStrings.stream()
            //.filter(s -> !s.contains("glTF-Embedded"))
            //.filter(s -> !s.contains("glTF-Binary"))
            .collect(Collectors.toList());

        ValidatorResult validatorResult = new ValidatorResult();
        for (String uriString : uriStrings)
        {
            validatorResult.add(validate(uriString));
        }
        
        logger.info("Summary:\n" + validatorResult.createString());
    }
    
    private static ValidatorResult validate(String uriString) throws Exception
    {
        logger.info("Validating "+uriString);
        
        GltfReaderV1 gltfReader = new GltfReaderV1();
        URI uri = new URI(uriString);
        try (InputStream inputStream = uri.toURL().openStream())
        {
            GlTF gltf = gltfReader.read(inputStream);
            Validator validator = new Validator(gltf);
            ValidatorResult validatorResult = validator.validate();
            
            logger.info("Result:\n" + validatorResult.createString());
            return validatorResult;
        }
    }
    
    
    private static List<String> getTutorialSampleModelsUriStrings()
    {
        String path = "https://raw.githubusercontent.com/KhronosGroup/" + 
            "glTF-Sample-Models/master/1.1/";

        List<String> tutorialSampleModelUriStrings = Arrays.asList(
            path + "TriangleWithoutIndices/glTF/TriangleWithoutIndices.gltf",
            path + "TriangleWithoutIndices/glTF-Embedded/TriangleWithoutIndices.gltf",
            path + "Triangle/glTF/Triangle.gltf",
            path + "Triangle/glTF-Embedded/Triangle.gltf",
            path + "AnimatedTriangle/glTF/AnimatedTriangle.gltf",
            path + "AnimatedTriangle/glTF-Embedded/AnimatedTriangle.gltf",
            path + "SimpleMaterial/glTF/SimpleMaterial.gltf",
            path + "SimpleMaterial/glTF-Embedded-buffer/SimpleMaterial.gltf",
            path + "SimpleMeshes/glTF/SimpleMeshes.gltf",
            path + "SimpleMeshes/glTF-Embedded/SimpleMeshes.gltf",
            path + "AdvancedMaterial/glTF/AdvancedMaterial.gltf",
            path + "AdvancedMaterial/glTF-Embedded-buffer/AdvancedMaterial.gltf",
            path + "SimpleOpacity/glTF/SimpleOpacity.gltf",
            path + "SimpleOpacity/glTF-Embedded-buffer/SimpleOpacity.gltf",
            path + "SimpleTexture/glTF/SimpleTexture.gltf",
            path + "SimpleTexture/glTF-Embedded-buffer/SimpleTexture.gltf",
            path + "Cameras/glTF/Cameras.gltf",
            path + "Cameras/glTF-Embedded/Cameras.gltf",
            path + "SimpleSkin/glTF/SimpleSkin.gltf",
            path + "SimpleSkin/glTF-Embedded-buffers/SimpleSkin.gltf"
        );
        
        return tutorialSampleModelUriStrings;
    }
    
    private static List<String> getKhronosSampleModelsUriStrings()
    {
        String path = "https://raw.githubusercontent.com/KhronosGroup/" + 
            "glTF-Sample-Models/master/1.0/";

        List<String> khronosSampleModelUriStrings = Arrays.asList(
            path + "2CylinderEngine/glTF/2CylinderEngine.gltf",
            path + "2CylinderEngine/glTF-Embedded/2CylinderEngine.gltf",
            path + "2CylinderEngine/glTF-Binary/2CylinderEngine.glb",
            path + "Box/glTF/Box.gltf",
            path + "Box/glTF-Embedded/Box.gltf",
            path + "Box/glTF-Binary/Box.glb",
            path + "BoxAnimated/glTF/BoxAnimated.gltf",
            path + "BoxAnimated/glTF-Embedded/BoxAnimated.gltf",
            path + "BoxAnimated/glTF-Binary/BoxAnimated.glb",
            path + "BoxSemantics/glTF/BoxSemantics.gltf",
            path + "BoxSemantics/glTF-Embedded/BoxSemantics.gltf",
            path + "BoxSemantics/glTF-Binary/BoxSemantics.glb",
            path + "BoxTextured/glTF/BoxTextured.gltf",
            path + "BoxTextured/glTF-Embedded/BoxTextured.gltf",
            path + "BoxTextured/glTF-Binary/BoxTextured.glb",
            path + "BoxWithoutIndices/glTF/BoxWithoutIndices.gltf",
            path + "BoxWithoutIndices/glTF-Embedded/BoxWithoutIndices.gltf",
            path + "BoxWithoutIndices/glTF-Binary/BoxWithoutIndices.glb",
            path + "BrainStem/glTF/BrainStem.gltf",
            path + "BrainStem/glTF-Embedded/BrainStem.gltf",
            path + "BrainStem/glTF-Binary/BrainStem.glb",
            path + "Buggy/glTF/Buggy.gltf",
            path + "Buggy/glTF-Embedded/Buggy.gltf",
            path + "Buggy/glTF-Binary/Buggy.glb",
            path + "CesiumMan/glTF/CesiumMan.gltf",
            path + "CesiumMan/glTF-Embedded/CesiumMan.gltf",
            path + "CesiumMan/glTF-Binary/CesiumMan.glb",
            path + "CesiumMilkTruck/glTF/CesiumMilkTruck.gltf",
            path + "CesiumMilkTruck/glTF-Embedded/CesiumMilkTruck.gltf",
            path + "CesiumMilkTruck/glTF-Binary/CesiumMilkTruck.glb",
            path + "Duck/glTF/Duck.gltf",
            path + "Duck/glTF-Embedded/Duck.gltf",
            path + "Duck/glTF-Binary/Duck.glb",
            path + "GearboxAssy/glTF/GearboxAssy.gltf",
            path + "GearboxAssy/glTF-Embedded/GearboxAssy.gltf",
            path + "GearboxAssy/glTF-Binary/GearboxAssy.glb",
            path + "Monster/glTF/Monster.gltf",
            path + "Monster/glTF-Embedded/Monster.gltf",
            path + "Monster/glTF-Binary/Monster.glb",
            path + "ReciprocatingSaw/glTF/ReciprocatingSaw.gltf",
            path + "ReciprocatingSaw/glTF-Embedded/ReciprocatingSaw.gltf",
            path + "ReciprocatingSaw/glTF-Binary/ReciprocatingSaw.glb",
            path + "RiggedFigure/glTF/RiggedFigure.gltf",
            path + "RiggedFigure/glTF-Embedded/RiggedFigure.gltf",
            path + "RiggedFigure/glTF-Binary/RiggedFigure.glb",
            path + "RiggedSimple/glTF/RiggedSimple.gltf",
            path + "RiggedSimple/glTF-Embedded/RiggedSimple.gltf",
            path + "RiggedSimple/glTF-Binary/RiggedSimple.glb",
            path + "VC/glTF/VC.gltf",
            path + "VC/glTF-Embedded/VC.gltf",
            path + "VC/glTF-Binary/VC.glb",
            path + "WalkingLady/glTF/WalkingLady.gltf",
            path + "WalkingLady/glTF-Embedded/WalkingLady.gltf",
            path + "WalkingLady/glTF-Binary/WalkingLady.glb"
        );
        return khronosSampleModelUriStrings;
    }
}
