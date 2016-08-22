# jgltf-viewer

A viewer for glTF.

**Note:** This library is still subject to change.

There are viewer implementations based on different Java OpenGL bindings:

* [jgltf-viewer-jogl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-jogl) - A glTF viewer based on [JOGL](http://jogamp.org/jogl/www/)
* [jgltf-viewer-lwjgl](https://github.com/javagl/JglTF/tree/master/jgltf-viewer-lwjgl) - A glTF viewer based on [LWJGL version 2](http://legacy.lwjgl.org/)

The following is a complete, standalone program that downloads the "Duck" model 
from the [Khronos glTF samples repository](https://github.com/KhronosGroup/glTF/tree/master/sampleModels)
and displays it in a frame:

```Java
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.io.GltfDataReader;
import de.javagl.jgltf.viewer.GltfViewer;
import de.javagl.jgltf.viewer.jogl.GltfViewerJogl;
import de.javagl.jgltf.viewer.lwjgl.GltfViewerLwjgl;

public class GltfViewerMiniDemo
{
    public static void main(String[] args) throws Exception
    {
        String uriString = 
            "https://raw.githubusercontent.com/KhronosGroup/glTF/master/" + 
            "sampleModels/Duck/glTF/Duck.gltf";
        GltfDataReader r = new GltfDataReader();
        GltfData gltfData = r.readGltfData(new URI(uriString));
        SwingUtilities.invokeLater(() -> createAndShowGui(gltfData));
    }
    
    private static void createAndShowGui(GltfData gltfData)
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create a viewer based on JOGL or LWJGL:
        GltfViewer gltfViewer = new GltfViewerJogl();
        //GltfViewer gltfViewer = new GltfViewerLwjgl();
        
        gltfViewer.addGltfData(gltfData);
        f.getContentPane().add(gltfViewer.getRenderComponent());
        f.setSize(500,500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
```

