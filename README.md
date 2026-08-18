# JglTF - Java libraries for [glTF](https://github.com/KhronosGroup/glTF/) 

**Note:** These libraries are still subject to change.

## What is glTF?

The following image gives an overview of glTF:

![gltfOverview-2.0.0-small.png](/images/gltfOverview-2.0.0-small.png)

A larger version of this image, as well as PDF versions, can be found on the [glTF overview releases page](https://github.com/javagl/gltfOverview/releases).

---

## Project Overview

The [`jgltf-model`](/jgltf-model) 
project allows reading and writing glTF models, and accessing the elements 
of that glTF model, while hiding most of the implementation details.

Adding the [`jgltf-model-extensions`](jgltf-model-extensions)
project as a dependency in the Maven POM will automatically integrate the 
support for all glTF extensions that are currently supported by JglTF. 
(See the 'Extensions' section below for details)

The [`jgltf-model-builder`](jgltf-model-builder) project is built on 
top of the `jgltf-model` project, and offers convenience functions
for building glTF models programmatically.

The [`jgltf-model-transform`](jgltf-model-transform) project is an 
**experimental** project that aims at supporting structural 
modifications of existing glTF models. For example, it contains functions for 
pruning unused elements from glTF models, and for revalidating glTF models 
after elements have been added or removed.

### Model and Implementation projects

The implementations of core glTF and its extensions always come in two flavors:

- The `impl` project contains classes for the low-level representation of
  glTF and its extensions. These classes are auto-generated from the JSON
  schema, and generally not visible to users.
- The `model` projects offer a convenience layer on top of the `impl` 
  projects, and offer structures that can be read and manipulated more easily.

For example, the [`jgltf-impl-v1`](jgltf-impl-v1) and [`jgltf-impl-v2`](jgltf-impl-v2) 
projects contain the classes that represent glTF 1.0 and 2.0 data, and are
auto-generated from the glTF JSON schema. The `jgltf-model` project offers
the convenience layer on top of these projects.


## Extensions

Users only have to add the [`jgltf-model-extensions`](jgltf-model-extensions) 
project as one of their Maven dependencies. This project does not contain any
functionality for itself. It only declares dependencies to all extensions that
are currently supported by JglTF. The implementations will then automatically
discover the classes that are required for the extension support, using a
Service Loader.

### Supported Extensions

The extensions that are currently supported by JglTF are listed here:

- [`EXT_mesh_gpu_instancing`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Vendor/EXT_mesh_gpu_instancing/README.md) via [`jgltf-model-ext-mesh-gpu-instancing`](./jgltf-model-ext-mesh-gpu-instancing)
- [`EXT_texture_webp`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Vendor/EXT_texture_webp/README.md) via [`jgltf-model-ext-texture-webp`](jgltf-model-ext-texture-webp)
- [`KHR_draco_mesh_compression`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_draco_mesh_compression/README.md) via [`jgltf-model-khr-draco-mesh-compression`](jgltf-model-khr-draco-mesh-compression)
- [`KHR_lights_punctual`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_lights_punctual/README.md) via [`jgltf-model-khr-lights-punctual`](jgltf-model-khr-lights-punctual)
- [`KHR_materials_anisotropy`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_anisotropy/README.md) via [`jgltf-model-khr-materials-anisotropy`](jgltf-model-khr-materials-anisotropy)
- [`KHR_materials_clearcoat`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_clearcoat/README.md) via [`jgltf-model-khr-materials-clearcoat`](jgltf-model-khr-materials-clearcoat)
- [`KHR_materials_dispersion`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_dispersion/README.md) via [`jgltf-model-khr-materials-dispersion`](jgltf-model-khr-materials-dispersion)
- [`KHR_materials_emissive_strength`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_emissive_strength/README.md) via [`jgltf-model-khr-materials-emissive-strength`](jgltf-model-khr-materials-emissive-strength)
- [`KHR_materials_ior`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_ior/README.md) via [`jgltf-model-khr-materials-ior`](jgltf-model-khr-materials-ior)
- [`KHR_materials_iridescence`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_iridescence/README.md) via [`jgltf-model-khr-materials-iridescence`](jgltf-model-khr-materials-iridescence)
- [`KHR_materials_sheen`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_sheen/README.md) via [`jgltf-model-khr-materials-sheen`](jgltf-model-khr-materials-sheen)
- [`KHR_materials_specular`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_specular/README.md) via [`jgltf-model-khr-materials-specular`](jgltf-model-khr-materials-specular)
- [`KHR_materials_transmission`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_transmission/README.md) via [`jgltf-model-khr-materials-transmission`](jgltf-model-khr-materials-transmission)
- [`KHR_materials_unlit`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_unlit/README.md) via [`jgltf-model-khr-materials-unlit`](jgltf-model-khr-materials-unlit)
- [`KHR_materials_variants`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_variants/README.md) via [`jgltf-model-khr-materials-variants`](jgltf-model-khr-materials-variants)
- [`KHR_materials_volume`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_materials_volume/README.md) via [`jgltf-model-khr-materials-volume`](jgltf-model-khr-materials-volume)
- [`KHR_mesh_quantization`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_mesh_quantization/README.md) via [`jgltf-model-khr-mesh-quantization`](jgltf-model-khr-mesh-quantization)
- [`KHR_node_visibility`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_node_visibility/README.md) via [`jgltf-model-khr-node-visibility`](jgltf-model-khr-node-visibility)
- [`KHR_texture_basisu`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_texture_basisu/README.md) via [`jgltf-model-khr-texture-basisu`](jgltf-model-khr-texture-basisu)
- [`KHR_texture_transform`](https://github.com/KhronosGroup/glTF/blob/main/extensions/2.0/Khronos/KHR_texture_transform/README.md) via [`jgltf-model-khr-texture-transform`](jgltf-model-khr-texture-transform)

For each of of these `...model...` projects, there is the corresponding 
`...impl-v2...` project that contains the low-level classes.


## Further Projects

### OBJ to glTF Converter

The [`jgltf-obj`](jgltf-obj) is a library/application for loading OBJ files and 
converting them into glTF 1.0 or 2.0 assets.

### glTF Viewer

Note: This viewer was originally implemented for glTF 1.0. It does not fully
support glTF 2.0 and PBR (physically based rendering).
  
The [`jgltf-viewer`](jgltf-viewer) is a base library for glTF viewers. 
The [`jgltf-viewer-jogl`](jgltf-viewer-jogl) and 
[`jgltf-viewer-lwjgl`](jgltf-viewer-lwjgl) projects contain implementations 
of this viewer based on JOGL and LWJGL.

### glTF Browser

The [`jgltf-browser`](jgltf-browser) is a simple standalone application that 
combines functionalities of the JglTF libraries: It allows loading glTF 
1.0 or 2.0 from files or URLs via drag-and-drop, offers a basic functionality 
for browsing through the glTF structure, showing the images, shader code and 
accessor data in a structured form, importing OBJ files as glTF, and saving 
glTF as standard, embedded or binary glTF files.
