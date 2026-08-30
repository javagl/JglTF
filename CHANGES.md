
### 3.0.0-dev

This is a major version release with a few breaking changes:

- The API has generally been changed from `float` to `double`.
  - This is a wide-ranging change, but for users, the update to the new version should be trivial: In nearly all cases, the update consists of a plain text replacement that changes `float` to `double`. Further details of this change are described in [PR #126](https://github.com/javagl/JglTF/pull/126)
- Refactoring of the material model
  - The previous material representations - `MaterialModelV1` and `MaterialModelV2` for glTF 1.0 and 2.0, respectively - did already carry a note that they may be refactored in the future. The most important change is that of `MaterialModelV2`. It previously tried to "flatten" certain elements of the full glTF PBR material representation. This didn't allow the fine-grained control over the elements of the PBR model that is required for proper support of the PBR extensions. The new material model is the `PbrMaterialModel` which properly reflects the structure of the glTF PBR material model. Details of this change are summarized in [PR #134](https://github.com/javagl/JglTF/pull/134).
- Skipped implementation-level `enum` validation
  - The low-level representation of the glTF structures (i.e. the `impl` classes) originally performed a validation of `enum` values against the set of predefined valid values. This did not allow for extensions to define new enum constants. So this validation is skipped now. Details are described in [PR #143](https://github.com/javagl/JglTF/pull/143)
- Improved support for extensions.
  - The state of glTF 2.x is essentially "frozen", and the development of new features solely happens via extensions. In order to support these extensions, the new version of JglTF allows adding support for new extensions via "plug-ins". These are projects that are only added to the classpath as a dependency, discovered using a `ServiceLoader`, and offer the functionalities that are required for handling a certain extension. This did require a considerable amount of internal refactorings. The current state should be considered as a preview feature that has been tested with most of the extensions that are currently ratified by the Khronos group, but some of the internal mechanisms may still change in the next release. Details are described in [PR #135](https://github.com/javagl/JglTF/pull/135).


### 2.0.5-SNAPSHOT

- ...

### 2.0.4 (2024-07-16)

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