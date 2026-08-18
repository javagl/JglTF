# jgltf-browser - A Java glTF browser 

A utility for loading, viewing, creating, browsing and converting 
[glTF](https://github.com/KhronosGroup/glTF/) data.

**Note:** This application is still subject to change.

After building and installing the JglTF libraries with

    mvn clean install

from the main `JglTF` directory, change into the `jgltf-browser` directory and call

    mvn clean compile assembly:single
    
to create the standalone browser application (with all dependencies)


An overview of the current functionality:

* Drag-and-drop glTF links or files into the browser window

* Directly load sample models from the repository 

![GltfBrowserSamples.png](../images/GltfBrowserSamples.png)

* Viewing the models 

![GltfBrowserMonster.png](../images/GltfBrowserMonster.png)

* Importing OBJ files 

![GltfBrowserObjImport.png](../images/GltfBrowserObjImport.png)

* Converting to embedded- or binary glTF 

![GltfBrowserConversion.png](../images/GltfBrowserConversion.png)

* Basic browsing by right-clicking references and selecting the referenced
element:
 
![GltfBrowser_browsing_01.png](../images/GltfBrowser_browsing_01.png)

* Viewing images (of textures):

![GltfBrowser_images_01.png](../images/GltfBrowser_images_01.png)


* Viewing the source code of vertex- and fragment shaders:

![GltfBrowser_shaders_01.png](../images/GltfBrowser_shaders_01.png)


* Viewing the data that is provided by accessors:

![GltfBrowser_accessors_01.png](../images/GltfBrowser_accessors_01.png)


