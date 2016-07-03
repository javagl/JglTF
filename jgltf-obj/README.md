# jgltf-obj - An OBJ to glTF converter

**Note:** This library is still subject to change.

This library allows converting OBJ files into glTF assets. It supports
embedded and binary glTF encoding. 

The library also splits large OBJ files into multiple parts, so that they
may be loaded as glTF assets despite the limitation of glTF to 
`unsigned short` indices. The following is a screenshot of the famous
happy buddha model with 1 million triangles and 500k vertices. It has
been split into multiple mesh primitives, which are rendered here with
different colors for illustration:

![BuddhaGltfParts01.png](https://github.com/javagl/JglTF/blob/master/images/BuddhaGltfParts01.png)