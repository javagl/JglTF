

### 2.0.4-SNAPSHOT

  - Information about the `extensionsUsed`/`extensionsRequired` and the `asset` that had been found in the input glTF JSON had not been passed on to the `...Model` classes. Now, there now is an `ExtensionsModel` and an `AssetModel` that are both part of the `GltfModel` and that keep track of this information. See [#77](https://github.com/javagl/JglTF/pull/77)
  - The buffer views for skin- and animation data had their `target` property set, although it must remain undefined. This caused validation errors. This was fixed in [#85](https://github.com/javagl/JglTF/pull/85)
  - When reading an embedded glTF, modifying the data, and writing it as an embedded glTF, then the data URIs had not been updated to reflect the changes. Although this exact usage pattern was not supposed to be supported, the data URIs for embedded assets are now always created from the current data when they are written. See [#91](https://github.com/javagl/JglTF/pull/91)
  - When the input data of a binary glTF was larger than indicated by the length information of the header, then this caused an error. Now, this is ignored (and only prints a log message). See [b2bfd33](https://github.com/javagl/JglTF/commit/b2bfd33dc29221cb7c27720cb09dd2a4b09de9d3)
  - For certain matrix type accessors, the glTF specification defines very specific alignment requirements for the columns of the matrices. These requirements had not been taken into account, and could cause invalid data to be written. This was fixed in [#93](https://github.com/javagl/JglTF/pull/93), with a follow-up fix in [3b83b96](https://github.com/javagl/JglTF/commit/3b83b96d0329270b0d503e8314582c1e5a7dbde8)
  - The inverse bind matrices of a skin are optional, but certain classes assumed that they are present. This was fixed in [#95](https://github.com/javagl/JglTF/pull/95)
  - Several bugs have been fixed in the `jgltf-model-builder` package via [#103](https://github.com/javagl/JglTF/pull/103) :
    - When a mesh primitive with morph targets was passed to a `GltfModelBuilder`, then the morph targets had not been taken into account when building the buffer structure
    - When a material with a normal texture was passed to a `GltfModelBuilder`, then this texture was not properly added to the model
    - When a `GltfModel` was read (from a glTF/GLB file), then the `meshModel.getWeights()` method always returned `null`
    - When an `ImageModel` with JPG MIME type was created from image data that included an alpha component, then the resulting image was invalid
    - It was possible to pass an `AccessorModel` to a `GltfModelBuilder` even when the `AccessorModel` already had an associated `BufferViewModel`
    - The `GltfModelBuilder` did not properly handle the case that the same `AccessorModel` instance was added multiple times (for example, as part of different mesh primitives)
    - Additional convenience classes have been added in the `jgltf-model-builder` package
    
### 2.0.3 (2022-08-08)
  - Start of change log...