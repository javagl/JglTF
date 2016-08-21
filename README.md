# JglTF - Java libraries for [glTF](https://github.com/KhronosGroup/glTF/) 

**Note:** These libraries are still subject to change.

## What is glTF?

The following image gives an overview of glTF:

![gltfOverview-0.2.0.png](/images/gltfOverview-0.2.0.png)

The libraries currently available here are

* [jgltf-impl](https://github.com/javagl/JglTF/tree/master/jgltf-impl) : 
  A very simple set of classes that represent glTF data, auto-generated 
  from the glTF JSON schema
* [jgltf-model](https://github.com/javagl/JglTF/tree/master/jgltf-model) : 
  A library built on top of `jgltf-impl` that offers functionality for 
  reading glTF data and accessing the associated data in a form that is more
  convenient for the use in Java
* [jgltf-obj](https://github.com/javagl/JglTF/tree/master/jgltf-obj) : 
  A library/application for loading OBJ files and converting them into
  glTF assets.
* [jgltf-browser](https://github.com/javagl/JglTF/tree/master/jgltf-browser) : 
  A simple standalone application that combines functionalities 
  of the JglTF libraries: It allows loading glTF from files or URLs 
  via drag-and-drop, offers a basic functionality for browsing through the 
  glTF structure, showing the images, shader code and accessor data
  in a structured form, importing OBJ files as glTF, and saving glTF as 
  standard, embedded or binary glTF files.
* [jgltf-viewer](https://github.com/javagl/JglTF/tree/master/jgltf-viewer) : 
  A base library for glTF viewers
  * [jgltf-viewer-jogl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-jogl) : 
  A glTF viewer based on JOGL
  * [jgltf-viewer-lwjgl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-lwjgl) : 
  A glTF viewer based on LWJGL 2
* [jgltf-validator](https://github.com/javagl/JglTF/tree/master/jgltf-validator) : 
  A simple glTF validator, only intended for internal use
  
   

