/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl;

import java.util.Map;
import java.util.Set;


/**
 * The root object for a glTF asset. 
 * 
 * Auto-generated for glTF.schema.json 
 * 
 */
public class GlTF
    extends GlTFProperty
{

    /**
     * Names of extensions used somewhere in this asset. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private Set<String> extensionsUsed;
    /**
     * A dictionary object of accessors. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Accessor> accessors;
    /**
     * A dictionary object of keyframe animations. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Animation> animations;
    /**
     * Metadata about the glTF asset. (optional)<br> 
     * Default: {} 
     * 
     */
    private Asset asset;
    /**
     * A dictionary object of buffers. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Buffer> buffers;
    /**
     * A dictionary object of bufferViews. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, BufferView> bufferViews;
    /**
     * A dictionary object of cameras. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Camera> cameras;
    /**
     * A dictionary object of images. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Image> images;
    /**
     * A dictionary object of materials. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Material> materials;
    /**
     * A dictionary object of meshes. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Mesh> meshes;
    /**
     * A dictionary object of nodes. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Node> nodes;
    /**
     * A dictionary object of programs. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Program> programs;
    /**
     * A dictionary object of samplers. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Sampler> samplers;
    /**
     * The ID of the default scene. (optional) 
     * 
     */
    private String scene;
    /**
     * A dictionary object of scenes. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Scene> scenes;
    /**
     * A dictionary object of shaders. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Shader> shaders;
    /**
     * A dictionary object of skins. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Skin> skins;
    /**
     * A dictionary object of techniques. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Technique> techniques;
    /**
     * A dictionary object of textures. (optional)<br> 
     * Default: {} 
     * 
     */
    private Map<String, Texture> textures;

    /**
     * Names of extensions used somewhere in this asset. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param extensionsUsed The extensionsUsed to set
     * 
     */
    public void setExtensionsUsed(Set<String> extensionsUsed) {
        if (extensionsUsed == null) {
            this.extensionsUsed = extensionsUsed;
            return ;
        }
        this.extensionsUsed = extensionsUsed;
    }

    /**
     * Names of extensions used somewhere in this asset. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The extensionsUsed
     * 
     */
    public Set<String> getExtensionsUsed() {
        return this.extensionsUsed;
    }

    /**
     * A dictionary object of accessors. (optional)<br> 
     * Default: {} 
     * 
     * @param accessors The accessors to set
     * 
     */
    public void setAccessors(Map<String, Accessor> accessors) {
        if (accessors == null) {
            this.accessors = accessors;
            return ;
        }
        this.accessors = accessors;
    }

    /**
     * A dictionary object of accessors. (optional)<br> 
     * Default: {} 
     * 
     * @return The accessors
     * 
     */
    public Map<String, Accessor> getAccessors() {
        return this.accessors;
    }

    /**
     * A dictionary object of keyframe animations. (optional)<br> 
     * Default: {} 
     * 
     * @param animations The animations to set
     * 
     */
    public void setAnimations(Map<String, Animation> animations) {
        if (animations == null) {
            this.animations = animations;
            return ;
        }
        this.animations = animations;
    }

    /**
     * A dictionary object of keyframe animations. (optional)<br> 
     * Default: {} 
     * 
     * @return The animations
     * 
     */
    public Map<String, Animation> getAnimations() {
        return this.animations;
    }

    /**
     * Metadata about the glTF asset. (optional)<br> 
     * Default: {} 
     * 
     * @param asset The asset to set
     * 
     */
    public void setAsset(Asset asset) {
        if (asset == null) {
            this.asset = asset;
            return ;
        }
        this.asset = asset;
    }

    /**
     * Metadata about the glTF asset. (optional)<br> 
     * Default: {} 
     * 
     * @return The asset
     * 
     */
    public Asset getAsset() {
        return this.asset;
    }

    /**
     * A dictionary object of buffers. (optional)<br> 
     * Default: {} 
     * 
     * @param buffers The buffers to set
     * 
     */
    public void setBuffers(Map<String, Buffer> buffers) {
        if (buffers == null) {
            this.buffers = buffers;
            return ;
        }
        this.buffers = buffers;
    }

    /**
     * A dictionary object of buffers. (optional)<br> 
     * Default: {} 
     * 
     * @return The buffers
     * 
     */
    public Map<String, Buffer> getBuffers() {
        return this.buffers;
    }

    /**
     * A dictionary object of bufferViews. (optional)<br> 
     * Default: {} 
     * 
     * @param bufferViews The bufferViews to set
     * 
     */
    public void setBufferViews(Map<String, BufferView> bufferViews) {
        if (bufferViews == null) {
            this.bufferViews = bufferViews;
            return ;
        }
        this.bufferViews = bufferViews;
    }

    /**
     * A dictionary object of bufferViews. (optional)<br> 
     * Default: {} 
     * 
     * @return The bufferViews
     * 
     */
    public Map<String, BufferView> getBufferViews() {
        return this.bufferViews;
    }

    /**
     * A dictionary object of cameras. (optional)<br> 
     * Default: {} 
     * 
     * @param cameras The cameras to set
     * 
     */
    public void setCameras(Map<String, Camera> cameras) {
        if (cameras == null) {
            this.cameras = cameras;
            return ;
        }
        this.cameras = cameras;
    }

    /**
     * A dictionary object of cameras. (optional)<br> 
     * Default: {} 
     * 
     * @return The cameras
     * 
     */
    public Map<String, Camera> getCameras() {
        return this.cameras;
    }

    /**
     * A dictionary object of images. (optional)<br> 
     * Default: {} 
     * 
     * @param images The images to set
     * 
     */
    public void setImages(Map<String, Image> images) {
        if (images == null) {
            this.images = images;
            return ;
        }
        this.images = images;
    }

    /**
     * A dictionary object of images. (optional)<br> 
     * Default: {} 
     * 
     * @return The images
     * 
     */
    public Map<String, Image> getImages() {
        return this.images;
    }

    /**
     * A dictionary object of materials. (optional)<br> 
     * Default: {} 
     * 
     * @param materials The materials to set
     * 
     */
    public void setMaterials(Map<String, Material> materials) {
        if (materials == null) {
            this.materials = materials;
            return ;
        }
        this.materials = materials;
    }

    /**
     * A dictionary object of materials. (optional)<br> 
     * Default: {} 
     * 
     * @return The materials
     * 
     */
    public Map<String, Material> getMaterials() {
        return this.materials;
    }

    /**
     * A dictionary object of meshes. (optional)<br> 
     * Default: {} 
     * 
     * @param meshes The meshes to set
     * 
     */
    public void setMeshes(Map<String, Mesh> meshes) {
        if (meshes == null) {
            this.meshes = meshes;
            return ;
        }
        this.meshes = meshes;
    }

    /**
     * A dictionary object of meshes. (optional)<br> 
     * Default: {} 
     * 
     * @return The meshes
     * 
     */
    public Map<String, Mesh> getMeshes() {
        return this.meshes;
    }

    /**
     * A dictionary object of nodes. (optional)<br> 
     * Default: {} 
     * 
     * @param nodes The nodes to set
     * 
     */
    public void setNodes(Map<String, Node> nodes) {
        if (nodes == null) {
            this.nodes = nodes;
            return ;
        }
        this.nodes = nodes;
    }

    /**
     * A dictionary object of nodes. (optional)<br> 
     * Default: {} 
     * 
     * @return The nodes
     * 
     */
    public Map<String, Node> getNodes() {
        return this.nodes;
    }

    /**
     * A dictionary object of programs. (optional)<br> 
     * Default: {} 
     * 
     * @param programs The programs to set
     * 
     */
    public void setPrograms(Map<String, Program> programs) {
        if (programs == null) {
            this.programs = programs;
            return ;
        }
        this.programs = programs;
    }

    /**
     * A dictionary object of programs. (optional)<br> 
     * Default: {} 
     * 
     * @return The programs
     * 
     */
    public Map<String, Program> getPrograms() {
        return this.programs;
    }

    /**
     * A dictionary object of samplers. (optional)<br> 
     * Default: {} 
     * 
     * @param samplers The samplers to set
     * 
     */
    public void setSamplers(Map<String, Sampler> samplers) {
        if (samplers == null) {
            this.samplers = samplers;
            return ;
        }
        this.samplers = samplers;
    }

    /**
     * A dictionary object of samplers. (optional)<br> 
     * Default: {} 
     * 
     * @return The samplers
     * 
     */
    public Map<String, Sampler> getSamplers() {
        return this.samplers;
    }

    /**
     * The ID of the default scene. (optional) 
     * 
     * @param scene The scene to set
     * 
     */
    public void setScene(String scene) {
        if (scene == null) {
            this.scene = scene;
            return ;
        }
        this.scene = scene;
    }

    /**
     * The ID of the default scene. (optional) 
     * 
     * @return The scene
     * 
     */
    public String getScene() {
        return this.scene;
    }

    /**
     * A dictionary object of scenes. (optional)<br> 
     * Default: {} 
     * 
     * @param scenes The scenes to set
     * 
     */
    public void setScenes(Map<String, Scene> scenes) {
        if (scenes == null) {
            this.scenes = scenes;
            return ;
        }
        this.scenes = scenes;
    }

    /**
     * A dictionary object of scenes. (optional)<br> 
     * Default: {} 
     * 
     * @return The scenes
     * 
     */
    public Map<String, Scene> getScenes() {
        return this.scenes;
    }

    /**
     * A dictionary object of shaders. (optional)<br> 
     * Default: {} 
     * 
     * @param shaders The shaders to set
     * 
     */
    public void setShaders(Map<String, Shader> shaders) {
        if (shaders == null) {
            this.shaders = shaders;
            return ;
        }
        this.shaders = shaders;
    }

    /**
     * A dictionary object of shaders. (optional)<br> 
     * Default: {} 
     * 
     * @return The shaders
     * 
     */
    public Map<String, Shader> getShaders() {
        return this.shaders;
    }

    /**
     * A dictionary object of skins. (optional)<br> 
     * Default: {} 
     * 
     * @param skins The skins to set
     * 
     */
    public void setSkins(Map<String, Skin> skins) {
        if (skins == null) {
            this.skins = skins;
            return ;
        }
        this.skins = skins;
    }

    /**
     * A dictionary object of skins. (optional)<br> 
     * Default: {} 
     * 
     * @return The skins
     * 
     */
    public Map<String, Skin> getSkins() {
        return this.skins;
    }

    /**
     * A dictionary object of techniques. (optional)<br> 
     * Default: {} 
     * 
     * @param techniques The techniques to set
     * 
     */
    public void setTechniques(Map<String, Technique> techniques) {
        if (techniques == null) {
            this.techniques = techniques;
            return ;
        }
        this.techniques = techniques;
    }

    /**
     * A dictionary object of techniques. (optional)<br> 
     * Default: {} 
     * 
     * @return The techniques
     * 
     */
    public Map<String, Technique> getTechniques() {
        return this.techniques;
    }

    /**
     * A dictionary object of textures. (optional)<br> 
     * Default: {} 
     * 
     * @param textures The textures to set
     * 
     */
    public void setTextures(Map<String, Texture> textures) {
        if (textures == null) {
            this.textures = textures;
            return ;
        }
        this.textures = textures;
    }

    /**
     * A dictionary object of textures. (optional)<br> 
     * Default: {} 
     * 
     * @return The textures
     * 
     */
    public Map<String, Texture> getTextures() {
        return this.textures;
    }

}
