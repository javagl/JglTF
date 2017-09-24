# jgltf-viewer

A viewer for glTF.

**Note:** This library is still subject to change.

There are viewer implementations based on different Java OpenGL bindings:

* [jgltf-viewer-jogl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-jogl) - A glTF viewer based on [JOGL](http://jogamp.org/jogl/www/)
* [jgltf-viewer-lwjgl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-lwjgl) - A glTF viewer based on [LWJGL version 2](http://legacy.lwjgl.org/)

The following is a complete, standalone program that downloads the "Duck" model 
from the [Khronos glTF samples repository](https://github.com/KhronosGroup/glTF-Sample-Models/)
and displays it in a frame:

```Java
import java.awt.Component;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import de.javagl.jgltf.viewer.GltfViewer;
import de.javagl.jgltf.viewer.jogl.GltfViewerJogl;
import de.javagl.jgltf.viewer.lwjgl.GltfViewerLwjgl;

public class GltfViewerMiniDemo
{
    public static void main(String[] args) throws Exception
    {
        String uriString = 
            "https://raw.githubusercontent.com/KhronosGroup/" + 
            "glTF-Sample-Models/master/2.0/Duck/glTF/Duck.gltf";
        GltfModelReader r = new GltfModelReader();
        GltfModel gltfModel = r.read(new URI(uriString));
        SwingUtilities.invokeLater(() -> createAndShowGui(gltfModel));
    }
    
    private static void createAndShowGui(GltfModel gltfModel)
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create a viewer based on JOGL or LWJGL:
        GltfViewer<Component> gltfViewer = new GltfViewerJogl();
        //GltfViewer<Component> gltfViewer = new GltfViewerLwjgl();
        
        gltfViewer.addGltfModel(gltfModel);
        f.getContentPane().add(gltfViewer.getRenderComponent());
        f.setSize(500,500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
```

