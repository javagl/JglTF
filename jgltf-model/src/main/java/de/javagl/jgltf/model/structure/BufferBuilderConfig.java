package de.javagl.jgltf.model.structure;

/**
 * A <b>package-private</b> class storing the configuration settings for
 * a {@link DefaultBufferBuilderStrategy}.
 * 
 * This class is not part of the public API.
 */
class BufferBuilderConfig
{
    /**
     * Whether to create one buffer view for each attribute accessor
     */
    boolean bufferViewPerAttributeAccessor = true;
    
    /**
     * Whether to create one buffer per mesh primitive
     */
    boolean bufferPerMeshPrimitive = false;
    
    /**
     * Whether to create one buffer per mesh
     */
    boolean bufferPerMesh = false;
    
    /**
     * Whether to create one buffer for all meshes
     */
    boolean bufferForMeshes = false;

    /**
     * Whether to create one buffer per animation
     */
    boolean bufferPerAnimation = false;
    
    /**
     * Whether to create one buffer for all animations
     */
    boolean bufferForAnimations = false;
    
    /**
     * Whether to create one buffer per skin
     */
    boolean bufferPerSkin = false;
    
    /**
     * Whether to create one buffer for all skins
     */
    boolean bufferForSkins = false;
    
    /**
     * Whether images should be stored in buffer views
     */
    boolean imagesInBufferViews = false;
    
    /**
     * Whether to create one buffer per image
     */
    boolean bufferPerImage = false;
    
    /**
     * Whether to create one buffer for all images
     */
    boolean bufferForImages = false;

    /**
     * Whether to create one buffer for all additional accessors
     */
    boolean bufferForAdditionalAccessors = false;
}
