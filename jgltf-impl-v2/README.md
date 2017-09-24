# jgltf-impl-v2 - A Java glTF 2 implementation

An implementation of [glTF](https://github.com/KhronosGroup/glTF/),
the GL Transmission Format.

**Note:** These classes are automatically generated from the 
glTF JSON schema of the [glTF specification](https://github.com/KhronosGroup/glTF/tree/master/specification/)
using [JsonModelGen](https://github.com/javagl/JsonModelGen). Details of the 
implementation of these classes may change in future versions.

## Current generator configuration

`JsonModelGen` was mainly created in order to generate this particular 
glTF model implementation. It does not yet offer any configuration
settings. The current (fixed) configuration generates a simple model 
for glTF, with the following properties:

* JSON arrays that contain numbers, integers or booleans are represented 
  as Java arrays (`float[]`, `int[]` and `boolean[]`, respectively)
* JSON arrays with objects are represented with `List<Type>`
* Single number- integer- or boolean-properties are represented with
  the reference types `Float`, `Integer` and `Boolean`. They are *not*
  represented with primitive types (`float`, `int` or `boolean`) in
  order to be able to assign `null` to the properties that are not
  explicitly *required* according to the specification. 

The setter methods perform a basic validation of the parameters. 
For example, if a certain property is constrained to be greater
than 0.0 in the JSON schema, then trying to set a value that is not
greater than 0.0 will cause an `IllegalArgumentException` to be
thrown. Similarly, a `NullPointerException` will be thrown when
trying to set a property to `null` that was explicitly marked
as *required* in the specification.

To simplify usage, the fields and setter- and getter methods contain basic 
JavaDoc documentation that was extracted from the JSON schema, including 
information about the valid values for the properties.

# Usage 
    
This library has **no dependencies** and is very simplistic. It does not 
offer any convenience functionality - not even for *reading* a glTF file. 
Reading the JSON part of a glTF file may be done with 
[jackson-databind](https://github.com/FasterXML/jackson-databind)
as follows:

`GlTF gltf = new ObjectMapper().readValue(inputStream, GlTF.class);`

(that's it). 

A more generic solution for reading glTF data, including improved 
error handling and the option to conveniently obtain the associated
(binary) data of glTF is offered by the 
[jgltf-model](https://github.com/javagl/JglTF/tree/master/jgltf-model) library.


 