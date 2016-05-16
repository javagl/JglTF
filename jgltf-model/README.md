# jgltf-model - A Java glTF model 

Classes for handling [glTF](https://github.com/KhronosGroup/glTF/), the GL 
Transmission Format, in Java

**Note:** This library is still subject to change.

This library extends the basic auto-generated 
[jgltf-impl](https://github.com/javagl/JglTF/tree/master/jgltf-impl) library
by classes that offer the entities and data that appear in a
glTF in a form that is more convenient for the use in Java. For example,
the raw data buffers of glTF are offered as `ByteBuffer` instances, and 
images are offered as `BufferedImage` instances.

The library also offers convenience classes for loading this data that is 
associated with glTF. This refers to glTF that contain embedded data in 
data URIs, as well as glTF that are stored as binary data with the 
`KHR_binary_glTF` extension.

There are different implementations of classes for accessing the data
that is provided by a glTF accessor, in a typed form, for example, using
an `AccessorFloatData`.  

The `GltfModel` class offers additional functionality for accessing the
data elements that are defined in a glTF, like the matrices that are 
implicitly defined by certain semantics.  

Finally, there is an `animation` package that contains utility classes
for animations (which are not necessarily related to glTF, but 
coincidentally offer much of the functionality that is required
for the animations in glTF).

 
