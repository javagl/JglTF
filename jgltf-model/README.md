# jgltf-model - A Java glTF model 

Classes for handling [glTF](https://github.com/KhronosGroup/glTF/), the GL 
Transmission Format, in Java

**Note:** This library is still subject to change.

This library extends the basic auto-generated 
[jgltf-impl-v1](https://github.com/javagl/JglTF/tree/master/jgltf-impl-v1) and
[jgltf-impl-v2](https://github.com/javagl/JglTF/tree/master/jgltf-impl-v2) libraries
by classes that offer the entities and data that appear in a
glTF in a form that is more convenient for the use in Java. 

The classes serve as a thin abstraction layer around the underlying implementation.
The `GltfModel` class offers the functionality for accessing the data elements 
that are defined in a glTF. The library also offers convenience classes for reading
glTF models from files or input streams, or writing them to output files. 
This refers to standard glTF assets, and assets that contain embedded data in 
data URIs, as well as binary glTF assets.
