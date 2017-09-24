# JglTF - Java libraries for [glTF](https://github.com/KhronosGroup/glTF/) 

**Note:** These libraries are still subject to change.

## What is glTF?

The following image gives an overview of glTF:

![gltfOverview-2.0.0-small.png](/images/gltfOverview-2.0.0-small.png)

A larger version of this image, as well as PDF versions, can be found on the [glTF overview releases page](https://github.com/javagl/gltfOverview/releases).

---

## A note about glTF 2.0 support:

The libraries that are available here mostly support glTF 1.0 and glTF 2.0.
Some special features that have been introduced in glTF 2.0 are not fully
supported yet. This mainly refers to rendering glTF 2.0 assets in the 
`jgltf-viewer`, particularly the support of PBR (physically based rendering)
and certain animation interpolation types. Support for these features will 
be implemented soon.

---


The libraries currently available here are

* [jgltf-impl-v1](https://github.com/javagl/JglTF/tree/master/jgltf-impl-v1) and
  [jgltf-impl-v2](https://github.com/javagl/JglTF/tree/master/jgltf-impl-v2) : 
  A very simple set of classes that represent glTF 1.0 and 2.0 data, 
  auto-generated from the glTF JSON schema
  
* [jgltf-model](https://github.com/javagl/JglTF/tree/master/jgltf-model) : 
  A library built on top of `jgltf-impl-v1` and `jgltf-impl-v2` that offers 
  functionality for reading glTF data and accessing the associated data 
  in a form that is more convenient for the use in Java. It serves as a
  thin abstraction layer around the auto-generated classes.
  
* [jgltf-obj](https://github.com/javagl/JglTF/tree/master/jgltf-obj) : 
  A library/application for loading OBJ files and converting them into
  glTF 1.0 or 2.0 assets.
  
* [jgltf-browser](https://github.com/javagl/JglTF/tree/master/jgltf-browser) : 
  A simple standalone application that combines functionalities 
  of the JglTF libraries: It allows loading glTF 1.0 or 2.0 from files or 
  URLs via drag-and-drop, offers a basic functionality for browsing through 
  the glTF structure, showing the images, shader code and accessor data
  in a structured form, importing OBJ files as glTF, and saving glTF as 
  standard, embedded or binary glTF files.
  
* [jgltf-viewer](https://github.com/javagl/JglTF/tree/master/jgltf-viewer) : 
  A base library for glTF viewers
  * [jgltf-viewer-jogl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-jogl) : 
  A glTF viewer based on JOGL
  * [jgltf-viewer-lwjgl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-lwjgl) : 
  A glTF viewer based on LWJGL 2

* [jgltf-impl-v2-technique-webgl](https://github.com/javagl/JglTF/tree/master/jgltf-impl-v2-technique-webgl) :
  Auto-generated classes for the glTF 2.0 `KHR_technique_webgl` extension
  
* [jgltf-validator](https://github.com/javagl/JglTF/tree/master/jgltf-validator) : 
  A simple glTF validator, only intended for internal use
  
   

