# jgltf-obj - An OBJ to glTF converter

**Note:** This library is still subject to change.

This is a library for converting OBJ files into glTF assets. It contains a main class, 
[`ObjToGltf`](https://github.com/javagl/JglTF/blob/master/jgltf-obj/src/main/java/de/javagl/jgltf/obj/ObjToGltf.java), 
so that it can also be used as a standalone, command-line application. 

The [`jgltf-browser`](https://github.com/javagl/JglTF/tree/master/jgltf-browser) 
internally uses this library, and allows loading OBJ files and converting them 
into standard-, embedded- or binary glTF files. 
See the [releases](https://github.com/javagl/JglTF/releases) page for the
latest releases of the `jgltf-browser`.

This library also splits large OBJ files into multiple parts, so that they
may be loaded as glTF assets despite the limitation of glTF to 
`unsigned short` indices. The following is a screenshot of the famous
happy buddha model with 1 million triangles and 500k vertices. It has
been split into multiple mesh primitives, which are rendered here with
different colors for illustration:

![BuddhaGltfParts01.png](https://github.com/javagl/JglTF/blob/master/images/BuddhaGltfParts01.png)
