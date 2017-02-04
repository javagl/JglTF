/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;

import java.util.ArrayList;
import java.util.List;


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
     * Names of glTF extensions used somewhere in this asset. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<String> extensionsUsed;
    /**
     * Names of glTF extensions required to properly load this asset. 
     * (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     */
    private List<String> extensionsRequired;
    /**
     * An array of accessors. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A typed view into a bufferView. A bufferView contains raw 
     * binary data. An accessor provides a typed view into a bufferView or a 
     * subset of a bufferView similar to how WebGL's `vertexAttribPointer()` 
     * defines an attribute in a buffer. (optional) 
     * 
     */
    private List<Accessor> accessors;
    /**
     * An array of keyframe animations. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A keyframe animation. (optional) 
     * 
     */
    private List<Animation> animations;
    /**
     * Metadata about the glTF asset. (required) 
     * 
     */
    private Asset asset;
    /**
     * An array of buffers. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A buffer points to binary geometry, animation, or skins. 
     * (optional) 
     * 
     */
    private List<Buffer> buffers;
    /**
     * An array of bufferViews. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A view into a buffer generally representing a subset of 
     * the buffer. (optional) 
     * 
     */
    private List<BufferView> bufferViews;
    /**
     * An array of cameras. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A camera's projection. A node can reference a camera to 
     * apply a transform to place the camera in the scene. (optional) 
     * 
     */
    private List<Camera> cameras;
    /**
     * An array of images. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Image data used to create a texture. Image can be 
     * referenced by URI or `bufferView` index. `mimeType` is required in the 
     * latter case. (optional) 
     * 
     */
    private List<Image> images;
    /**
     * An array of materials. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The material appearance of a primitive. (optional) 
     * 
     */
    private List<Material> materials;
    /**
     * An array of meshes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A set of primitives to be rendered. A node can contain one 
     * or more meshes. A node's transform places the mesh in the scene. 
     * (optional) 
     * 
     */
    private List<Mesh> meshes;
    /**
     * An array of nodes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A node in the node hierarchy. A node can have either the 
     * `camera`, `meshes`, or `skeletons`/`skin`/`meshes` properties defined. 
     * In the later case, all `primitives` in the referenced `meshes` contain 
     * `JOINT` and `WEIGHT` attributes and the referenced 
     * `material`/`technique` from each `primitive` has parameters with 
     * `JOINT` and `WEIGHT` semantics. A node can have either a `matrix` or 
     * any combination of `translation`/`rotation`/`scale` (TRS) properties. 
     * TRS properties are converted to matrices and postmultiplied in the `T 
     * * R * S` order to compose the transformation matrix; first the scale 
     * is applied to the vertices, then the rotation, and then the 
     * translation. If none are provided, the transform is the identity. When 
     * a node is targeted for animation (referenced by an 
     * animation.channel.target), only TRS properties may be present; 
     * `matrix` will not be present. (optional) 
     * 
     */
    private List<Node> nodes;
    /**
     * An array of samplers. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Texture sampler properties for filtering and wrapping 
     * modes. (optional) 
     * 
     */
    private List<Sampler> samplers;
    /**
     * The index of the default scene. (optional) 
     * 
     */
    private Integer scene;
    /**
     * An array of scenes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The root nodes of a scene. (optional) 
     * 
     */
    private List<Scene> scenes;
    /**
     * An array of skins. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Joints and matrices defining a skin. (optional) 
     * 
     */
    private List<Skin> skins;
    /**
     * An array of textures. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A texture and its sampler. (optional) 
     * 
     */
    private List<Texture> textures;
    /**
     * Names of WebGL extensions required to render this asset. 
     * (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Valid values: ["OES_element_index_uint"] 
     * 
     */
    private List<String> glExtensionsUsed;

    /**
     * Names of glTF extensions used somewhere in this asset. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param extensionsUsed The extensionsUsed to set
     * 
     */
    public void setExtensionsUsed(List<String> extensionsUsed) {
        if (extensionsUsed == null) {
            this.extensionsUsed = extensionsUsed;
            return ;
        }
        this.extensionsUsed = extensionsUsed;
    }

    /**
     * Names of glTF extensions used somewhere in this asset. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The extensionsUsed
     * 
     */
    public List<String> getExtensionsUsed() {
        return this.extensionsUsed;
    }

    /**
     * Add the given extensionsUsed. The extensionsUsed of this instance will 
     * be replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addExtensionsUsed(String element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<String> oldList = this.extensionsUsed;
        List<String> newList = new ArrayList<String>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.extensionsUsed = newList;
    }

    /**
     * Remove the given extensionsUsed. The extensionsUsed of this instance 
     * will be replaced with a list that contains all previous elements, 
     * except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeExtensionsUsed(String element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<String> oldList = this.extensionsUsed;
        List<String> newList = new ArrayList<String>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.extensionsUsed = null;
        } else {
            this.extensionsUsed = newList;
        }
    }

    /**
     * Returns the default value of the extensionsUsed<br> 
     * @see #getExtensionsUsed 
     * 
     * @return The default extensionsUsed
     * 
     */
    public List<String> defaultExtensionsUsed() {
        return new ArrayList<String>();
    }

    /**
     * Names of glTF extensions required to properly load this asset. 
     * (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @param extensionsRequired The extensionsRequired to set
     * 
     */
    public void setExtensionsRequired(List<String> extensionsRequired) {
        if (extensionsRequired == null) {
            this.extensionsRequired = extensionsRequired;
            return ;
        }
        this.extensionsRequired = extensionsRequired;
    }

    /**
     * Names of glTF extensions required to properly load this asset. 
     * (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional) 
     * 
     * @return The extensionsRequired
     * 
     */
    public List<String> getExtensionsRequired() {
        return this.extensionsRequired;
    }

    /**
     * Add the given extensionsRequired. The extensionsRequired of this 
     * instance will be replaced with a list that contains all previous 
     * elements, and additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addExtensionsRequired(String element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<String> oldList = this.extensionsRequired;
        List<String> newList = new ArrayList<String>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.extensionsRequired = newList;
    }

    /**
     * Remove the given extensionsRequired. The extensionsRequired of this 
     * instance will be replaced with a list that contains all previous 
     * elements, except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeExtensionsRequired(String element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<String> oldList = this.extensionsRequired;
        List<String> newList = new ArrayList<String>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.extensionsRequired = null;
        } else {
            this.extensionsRequired = newList;
        }
    }

    /**
     * Returns the default value of the extensionsRequired<br> 
     * @see #getExtensionsRequired 
     * 
     * @return The default extensionsRequired
     * 
     */
    public List<String> defaultExtensionsRequired() {
        return new ArrayList<String>();
    }

    /**
     * An array of accessors. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A typed view into a bufferView. A bufferView contains raw 
     * binary data. An accessor provides a typed view into a bufferView or a 
     * subset of a bufferView similar to how WebGL's `vertexAttribPointer()` 
     * defines an attribute in a buffer. (optional) 
     * 
     * @param accessors The accessors to set
     * 
     */
    public void setAccessors(List<Accessor> accessors) {
        if (accessors == null) {
            this.accessors = accessors;
            return ;
        }
        this.accessors = accessors;
    }

    /**
     * An array of accessors. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A typed view into a bufferView. A bufferView contains raw 
     * binary data. An accessor provides a typed view into a bufferView or a 
     * subset of a bufferView similar to how WebGL's `vertexAttribPointer()` 
     * defines an attribute in a buffer. (optional) 
     * 
     * @return The accessors
     * 
     */
    public List<Accessor> getAccessors() {
        return this.accessors;
    }

    /**
     * Add the given accessors. The accessors of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addAccessors(Accessor element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Accessor> oldList = this.accessors;
        List<Accessor> newList = new ArrayList<Accessor>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.accessors = newList;
    }

    /**
     * Remove the given accessors. The accessors of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeAccessors(Accessor element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Accessor> oldList = this.accessors;
        List<Accessor> newList = new ArrayList<Accessor>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.accessors = null;
        } else {
            this.accessors = newList;
        }
    }

    /**
     * Returns the default value of the accessors<br> 
     * @see #getAccessors 
     * 
     * @return The default accessors
     * 
     */
    public List<Accessor> defaultAccessors() {
        return new ArrayList<Accessor>();
    }

    /**
     * An array of keyframe animations. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A keyframe animation. (optional) 
     * 
     * @param animations The animations to set
     * 
     */
    public void setAnimations(List<Animation> animations) {
        if (animations == null) {
            this.animations = animations;
            return ;
        }
        this.animations = animations;
    }

    /**
     * An array of keyframe animations. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A keyframe animation. (optional) 
     * 
     * @return The animations
     * 
     */
    public List<Animation> getAnimations() {
        return this.animations;
    }

    /**
     * Add the given animations. The animations of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addAnimations(Animation element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Animation> oldList = this.animations;
        List<Animation> newList = new ArrayList<Animation>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.animations = newList;
    }

    /**
     * Remove the given animations. The animations of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeAnimations(Animation element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Animation> oldList = this.animations;
        List<Animation> newList = new ArrayList<Animation>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.animations = null;
        } else {
            this.animations = newList;
        }
    }

    /**
     * Returns the default value of the animations<br> 
     * @see #getAnimations 
     * 
     * @return The default animations
     * 
     */
    public List<Animation> defaultAnimations() {
        return new ArrayList<Animation>();
    }

    /**
     * Metadata about the glTF asset. (required) 
     * 
     * @param asset The asset to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setAsset(Asset asset) {
        if (asset == null) {
            throw new NullPointerException((("Invalid value for asset: "+ asset)+", may not be null"));
        }
        this.asset = asset;
    }

    /**
     * Metadata about the glTF asset. (required) 
     * 
     * @return The asset
     * 
     */
    public Asset getAsset() {
        return this.asset;
    }

    /**
     * An array of buffers. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A buffer points to binary geometry, animation, or skins. 
     * (optional) 
     * 
     * @param buffers The buffers to set
     * 
     */
    public void setBuffers(List<Buffer> buffers) {
        if (buffers == null) {
            this.buffers = buffers;
            return ;
        }
        this.buffers = buffers;
    }

    /**
     * An array of buffers. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A buffer points to binary geometry, animation, or skins. 
     * (optional) 
     * 
     * @return The buffers
     * 
     */
    public List<Buffer> getBuffers() {
        return this.buffers;
    }

    /**
     * Add the given buffers. The buffers of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addBuffers(Buffer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Buffer> oldList = this.buffers;
        List<Buffer> newList = new ArrayList<Buffer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.buffers = newList;
    }

    /**
     * Remove the given buffers. The buffers of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeBuffers(Buffer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Buffer> oldList = this.buffers;
        List<Buffer> newList = new ArrayList<Buffer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.buffers = null;
        } else {
            this.buffers = newList;
        }
    }

    /**
     * Returns the default value of the buffers<br> 
     * @see #getBuffers 
     * 
     * @return The default buffers
     * 
     */
    public List<Buffer> defaultBuffers() {
        return new ArrayList<Buffer>();
    }

    /**
     * An array of bufferViews. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A view into a buffer generally representing a subset of 
     * the buffer. (optional) 
     * 
     * @param bufferViews The bufferViews to set
     * 
     */
    public void setBufferViews(List<BufferView> bufferViews) {
        if (bufferViews == null) {
            this.bufferViews = bufferViews;
            return ;
        }
        this.bufferViews = bufferViews;
    }

    /**
     * An array of bufferViews. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A view into a buffer generally representing a subset of 
     * the buffer. (optional) 
     * 
     * @return The bufferViews
     * 
     */
    public List<BufferView> getBufferViews() {
        return this.bufferViews;
    }

    /**
     * Add the given bufferViews. The bufferViews of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addBufferViews(BufferView element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<BufferView> oldList = this.bufferViews;
        List<BufferView> newList = new ArrayList<BufferView>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.bufferViews = newList;
    }

    /**
     * Remove the given bufferViews. The bufferViews of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeBufferViews(BufferView element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<BufferView> oldList = this.bufferViews;
        List<BufferView> newList = new ArrayList<BufferView>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.bufferViews = null;
        } else {
            this.bufferViews = newList;
        }
    }

    /**
     * Returns the default value of the bufferViews<br> 
     * @see #getBufferViews 
     * 
     * @return The default bufferViews
     * 
     */
    public List<BufferView> defaultBufferViews() {
        return new ArrayList<BufferView>();
    }

    /**
     * An array of cameras. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A camera's projection. A node can reference a camera to 
     * apply a transform to place the camera in the scene. (optional) 
     * 
     * @param cameras The cameras to set
     * 
     */
    public void setCameras(List<Camera> cameras) {
        if (cameras == null) {
            this.cameras = cameras;
            return ;
        }
        this.cameras = cameras;
    }

    /**
     * An array of cameras. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A camera's projection. A node can reference a camera to 
     * apply a transform to place the camera in the scene. (optional) 
     * 
     * @return The cameras
     * 
     */
    public List<Camera> getCameras() {
        return this.cameras;
    }

    /**
     * Add the given cameras. The cameras of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addCameras(Camera element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Camera> oldList = this.cameras;
        List<Camera> newList = new ArrayList<Camera>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.cameras = newList;
    }

    /**
     * Remove the given cameras. The cameras of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeCameras(Camera element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Camera> oldList = this.cameras;
        List<Camera> newList = new ArrayList<Camera>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.cameras = null;
        } else {
            this.cameras = newList;
        }
    }

    /**
     * Returns the default value of the cameras<br> 
     * @see #getCameras 
     * 
     * @return The default cameras
     * 
     */
    public List<Camera> defaultCameras() {
        return new ArrayList<Camera>();
    }

    /**
     * An array of images. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Image data used to create a texture. Image can be 
     * referenced by URI or `bufferView` index. `mimeType` is required in the 
     * latter case. (optional) 
     * 
     * @param images The images to set
     * 
     */
    public void setImages(List<Image> images) {
        if (images == null) {
            this.images = images;
            return ;
        }
        this.images = images;
    }

    /**
     * An array of images. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Image data used to create a texture. Image can be 
     * referenced by URI or `bufferView` index. `mimeType` is required in the 
     * latter case. (optional) 
     * 
     * @return The images
     * 
     */
    public List<Image> getImages() {
        return this.images;
    }

    /**
     * Add the given images. The images of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addImages(Image element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Image> oldList = this.images;
        List<Image> newList = new ArrayList<Image>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.images = newList;
    }

    /**
     * Remove the given images. The images of this instance will be replaced 
     * with a list that contains all previous elements, except for the 
     * removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeImages(Image element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Image> oldList = this.images;
        List<Image> newList = new ArrayList<Image>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.images = null;
        } else {
            this.images = newList;
        }
    }

    /**
     * Returns the default value of the images<br> 
     * @see #getImages 
     * 
     * @return The default images
     * 
     */
    public List<Image> defaultImages() {
        return new ArrayList<Image>();
    }

    /**
     * An array of materials. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The material appearance of a primitive. (optional) 
     * 
     * @param materials The materials to set
     * 
     */
    public void setMaterials(List<Material> materials) {
        if (materials == null) {
            this.materials = materials;
            return ;
        }
        this.materials = materials;
    }

    /**
     * An array of materials. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The material appearance of a primitive. (optional) 
     * 
     * @return The materials
     * 
     */
    public List<Material> getMaterials() {
        return this.materials;
    }

    /**
     * Add the given materials. The materials of this instance will be 
     * replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addMaterials(Material element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Material> oldList = this.materials;
        List<Material> newList = new ArrayList<Material>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.materials = newList;
    }

    /**
     * Remove the given materials. The materials of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeMaterials(Material element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Material> oldList = this.materials;
        List<Material> newList = new ArrayList<Material>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.materials = null;
        } else {
            this.materials = newList;
        }
    }

    /**
     * Returns the default value of the materials<br> 
     * @see #getMaterials 
     * 
     * @return The default materials
     * 
     */
    public List<Material> defaultMaterials() {
        return new ArrayList<Material>();
    }

    /**
     * An array of meshes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A set of primitives to be rendered. A node can contain one 
     * or more meshes. A node's transform places the mesh in the scene. 
     * (optional) 
     * 
     * @param meshes The meshes to set
     * 
     */
    public void setMeshes(List<Mesh> meshes) {
        if (meshes == null) {
            this.meshes = meshes;
            return ;
        }
        this.meshes = meshes;
    }

    /**
     * An array of meshes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A set of primitives to be rendered. A node can contain one 
     * or more meshes. A node's transform places the mesh in the scene. 
     * (optional) 
     * 
     * @return The meshes
     * 
     */
    public List<Mesh> getMeshes() {
        return this.meshes;
    }

    /**
     * Add the given meshes. The meshes of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addMeshes(Mesh element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Mesh> oldList = this.meshes;
        List<Mesh> newList = new ArrayList<Mesh>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.meshes = newList;
    }

    /**
     * Remove the given meshes. The meshes of this instance will be replaced 
     * with a list that contains all previous elements, except for the 
     * removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeMeshes(Mesh element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Mesh> oldList = this.meshes;
        List<Mesh> newList = new ArrayList<Mesh>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.meshes = null;
        } else {
            this.meshes = newList;
        }
    }

    /**
     * Returns the default value of the meshes<br> 
     * @see #getMeshes 
     * 
     * @return The default meshes
     * 
     */
    public List<Mesh> defaultMeshes() {
        return new ArrayList<Mesh>();
    }

    /**
     * An array of nodes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A node in the node hierarchy. A node can have either the 
     * `camera`, `meshes`, or `skeletons`/`skin`/`meshes` properties defined. 
     * In the later case, all `primitives` in the referenced `meshes` contain 
     * `JOINT` and `WEIGHT` attributes and the referenced 
     * `material`/`technique` from each `primitive` has parameters with 
     * `JOINT` and `WEIGHT` semantics. A node can have either a `matrix` or 
     * any combination of `translation`/`rotation`/`scale` (TRS) properties. 
     * TRS properties are converted to matrices and postmultiplied in the `T 
     * * R * S` order to compose the transformation matrix; first the scale 
     * is applied to the vertices, then the rotation, and then the 
     * translation. If none are provided, the transform is the identity. When 
     * a node is targeted for animation (referenced by an 
     * animation.channel.target), only TRS properties may be present; 
     * `matrix` will not be present. (optional) 
     * 
     * @param nodes The nodes to set
     * 
     */
    public void setNodes(List<Node> nodes) {
        if (nodes == null) {
            this.nodes = nodes;
            return ;
        }
        this.nodes = nodes;
    }

    /**
     * An array of nodes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A node in the node hierarchy. A node can have either the 
     * `camera`, `meshes`, or `skeletons`/`skin`/`meshes` properties defined. 
     * In the later case, all `primitives` in the referenced `meshes` contain 
     * `JOINT` and `WEIGHT` attributes and the referenced 
     * `material`/`technique` from each `primitive` has parameters with 
     * `JOINT` and `WEIGHT` semantics. A node can have either a `matrix` or 
     * any combination of `translation`/`rotation`/`scale` (TRS) properties. 
     * TRS properties are converted to matrices and postmultiplied in the `T 
     * * R * S` order to compose the transformation matrix; first the scale 
     * is applied to the vertices, then the rotation, and then the 
     * translation. If none are provided, the transform is the identity. When 
     * a node is targeted for animation (referenced by an 
     * animation.channel.target), only TRS properties may be present; 
     * `matrix` will not be present. (optional) 
     * 
     * @return The nodes
     * 
     */
    public List<Node> getNodes() {
        return this.nodes;
    }

    /**
     * Add the given nodes. The nodes of this instance will be replaced with 
     * a list that contains all previous elements, and additionally the new 
     * element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addNodes(Node element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Node> oldList = this.nodes;
        List<Node> newList = new ArrayList<Node>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.nodes = newList;
    }

    /**
     * Remove the given nodes. The nodes of this instance will be replaced 
     * with a list that contains all previous elements, except for the 
     * removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeNodes(Node element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Node> oldList = this.nodes;
        List<Node> newList = new ArrayList<Node>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.nodes = null;
        } else {
            this.nodes = newList;
        }
    }

    /**
     * Returns the default value of the nodes<br> 
     * @see #getNodes 
     * 
     * @return The default nodes
     * 
     */
    public List<Node> defaultNodes() {
        return new ArrayList<Node>();
    }

    /**
     * An array of samplers. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Texture sampler properties for filtering and wrapping 
     * modes. (optional) 
     * 
     * @param samplers The samplers to set
     * 
     */
    public void setSamplers(List<Sampler> samplers) {
        if (samplers == null) {
            this.samplers = samplers;
            return ;
        }
        this.samplers = samplers;
    }

    /**
     * An array of samplers. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Texture sampler properties for filtering and wrapping 
     * modes. (optional) 
     * 
     * @return The samplers
     * 
     */
    public List<Sampler> getSamplers() {
        return this.samplers;
    }

    /**
     * Add the given samplers. The samplers of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addSamplers(Sampler element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Sampler> oldList = this.samplers;
        List<Sampler> newList = new ArrayList<Sampler>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.samplers = newList;
    }

    /**
     * Remove the given samplers. The samplers of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeSamplers(Sampler element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Sampler> oldList = this.samplers;
        List<Sampler> newList = new ArrayList<Sampler>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.samplers = null;
        } else {
            this.samplers = newList;
        }
    }

    /**
     * Returns the default value of the samplers<br> 
     * @see #getSamplers 
     * 
     * @return The default samplers
     * 
     */
    public List<Sampler> defaultSamplers() {
        return new ArrayList<Sampler>();
    }

    /**
     * The index of the default scene. (optional) 
     * 
     * @param scene The scene to set
     * 
     */
    public void setScene(Integer scene) {
        if (scene == null) {
            this.scene = scene;
            return ;
        }
        this.scene = scene;
    }

    /**
     * The index of the default scene. (optional) 
     * 
     * @return The scene
     * 
     */
    public Integer getScene() {
        return this.scene;
    }

    /**
     * An array of scenes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The root nodes of a scene. (optional) 
     * 
     * @param scenes The scenes to set
     * 
     */
    public void setScenes(List<Scene> scenes) {
        if (scenes == null) {
            this.scenes = scenes;
            return ;
        }
        this.scenes = scenes;
    }

    /**
     * An array of scenes. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The root nodes of a scene. (optional) 
     * 
     * @return The scenes
     * 
     */
    public List<Scene> getScenes() {
        return this.scenes;
    }

    /**
     * Add the given scenes. The scenes of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addScenes(Scene element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Scene> oldList = this.scenes;
        List<Scene> newList = new ArrayList<Scene>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.scenes = newList;
    }

    /**
     * Remove the given scenes. The scenes of this instance will be replaced 
     * with a list that contains all previous elements, except for the 
     * removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeScenes(Scene element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Scene> oldList = this.scenes;
        List<Scene> newList = new ArrayList<Scene>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.scenes = null;
        } else {
            this.scenes = newList;
        }
    }

    /**
     * Returns the default value of the scenes<br> 
     * @see #getScenes 
     * 
     * @return The default scenes
     * 
     */
    public List<Scene> defaultScenes() {
        return new ArrayList<Scene>();
    }

    /**
     * An array of skins. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Joints and matrices defining a skin. (optional) 
     * 
     * @param skins The skins to set
     * 
     */
    public void setSkins(List<Skin> skins) {
        if (skins == null) {
            this.skins = skins;
            return ;
        }
        this.skins = skins;
    }

    /**
     * An array of skins. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Joints and matrices defining a skin. (optional) 
     * 
     * @return The skins
     * 
     */
    public List<Skin> getSkins() {
        return this.skins;
    }

    /**
     * Add the given skins. The skins of this instance will be replaced with 
     * a list that contains all previous elements, and additionally the new 
     * element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addSkins(Skin element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Skin> oldList = this.skins;
        List<Skin> newList = new ArrayList<Skin>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.skins = newList;
    }

    /**
     * Remove the given skins. The skins of this instance will be replaced 
     * with a list that contains all previous elements, except for the 
     * removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeSkins(Skin element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Skin> oldList = this.skins;
        List<Skin> newList = new ArrayList<Skin>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.skins = null;
        } else {
            this.skins = newList;
        }
    }

    /**
     * Returns the default value of the skins<br> 
     * @see #getSkins 
     * 
     * @return The default skins
     * 
     */
    public List<Skin> defaultSkins() {
        return new ArrayList<Skin>();
    }

    /**
     * An array of textures. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A texture and its sampler. (optional) 
     * 
     * @param textures The textures to set
     * 
     */
    public void setTextures(List<Texture> textures) {
        if (textures == null) {
            this.textures = textures;
            return ;
        }
        this.textures = textures;
    }

    /**
     * An array of textures. (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;A texture and its sampler. (optional) 
     * 
     * @return The textures
     * 
     */
    public List<Texture> getTextures() {
        return this.textures;
    }

    /**
     * Add the given textures. The textures of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addTextures(Texture element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Texture> oldList = this.textures;
        List<Texture> newList = new ArrayList<Texture>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.textures = newList;
    }

    /**
     * Remove the given textures. The textures of this instance will be 
     * replaced with a list that contains all previous elements, except for 
     * the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeTextures(Texture element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Texture> oldList = this.textures;
        List<Texture> newList = new ArrayList<Texture>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.textures = null;
        } else {
            this.textures = newList;
        }
    }

    /**
     * Returns the default value of the textures<br> 
     * @see #getTextures 
     * 
     * @return The default textures
     * 
     */
    public List<Texture> defaultTextures() {
        return new ArrayList<Texture>();
    }

    /**
     * Names of WebGL extensions required to render this asset. 
     * (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Valid values: ["OES_element_index_uint"] 
     * 
     * @param glExtensionsUsed The glExtensionsUsed to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setGlExtensionsUsed(List<String> glExtensionsUsed) {
        if (glExtensionsUsed == null) {
            this.glExtensionsUsed = glExtensionsUsed;
            return ;
        }
        for (String glExtensionsUsedElement: glExtensionsUsed) {
            if (!"OES_element_index_uint".equals(glExtensionsUsedElement)) {
                throw new IllegalArgumentException((("Invalid value for glExtensionsUsedElement: "+ glExtensionsUsedElement)+", valid: [\"OES_element_index_uint\"]"));
            }
        }
        this.glExtensionsUsed = glExtensionsUsed;
    }

    /**
     * Names of WebGL extensions required to render this asset. 
     * (optional)<br> 
     * Default: []<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;The elements of this array (optional)<br> 
     * &nbsp;&nbsp;Valid values: ["OES_element_index_uint"] 
     * 
     * @return The glExtensionsUsed
     * 
     */
    public List<String> getGlExtensionsUsed() {
        return this.glExtensionsUsed;
    }

    /**
     * Add the given glExtensionsUsed. The glExtensionsUsed of this instance 
     * will be replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addGlExtensionsUsed(String element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<String> oldList = this.glExtensionsUsed;
        List<String> newList = new ArrayList<String>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.glExtensionsUsed = newList;
    }

    /**
     * Remove the given glExtensionsUsed. The glExtensionsUsed of this 
     * instance will be replaced with a list that contains all previous 
     * elements, except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeGlExtensionsUsed(String element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<String> oldList = this.glExtensionsUsed;
        List<String> newList = new ArrayList<String>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.glExtensionsUsed = null;
        } else {
            this.glExtensionsUsed = newList;
        }
    }

    /**
     * Returns the default value of the glExtensionsUsed<br> 
     * @see #getGlExtensionsUsed 
     * 
     * @return The default glExtensionsUsed
     * 
     */
    public List<String> defaultGlExtensionsUsed() {
        return new ArrayList<String>();
    }

}
