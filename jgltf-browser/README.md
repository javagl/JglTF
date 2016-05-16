# jgltf-browser - A Java glTF browser 

A *very* simple utility for browsing 
[glTF](https://github.com/KhronosGroup/glTF/) data.

**Note:** This application is still subject to change.

Build with

    mvn clean compile assembly:single
    
to create a simple, standalone application.


An overview of the current functionality:

* Drag-and-drop glTF links or files into the browser window
* Basic browsing by right-clicking references and selecting the referenced
element:
 
![GltfBrowser_browsing_01.png](https://github.com/javagl/JglTF/images/GltfBrowser_browsing_01.png)

* Viewing images (of textures):

![GltfBrowser_images_01.png](https://github.com/javagl/JglTF/images/GltfBrowser_images_01.png)


* Viewing the source code of vertex- and fragment shaders:

![GltfBrowser_shaders_01.png](https://github.com/javagl/JglTF/images/GltfBrowser_shaders_01.png)


* Viewing the data that is provided by accessors:

![GltfBrowser_accessors_01.png](https://github.com/javagl/JglTF/images/GltfBrowser_accessors_01.png)


