package de.javagl.jgltf.browser;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.io.GltfDataReader;
import de.javagl.jgltf.model.io.GltfWriter;

/**
 * An "integration test" for the {@link MaterialValuesEditorPanel}
 */
@SuppressWarnings("javadoc")
class MaterialValuesEditorPanelTest
{
    public static void main(String[] args) 
        throws IOException, URISyntaxException
    {
        String uriString = "file:/C:/Develop/KhronosGroup/" + 
            "glTF-Sample-Models/1.0/Duck/glTF/Duck.gltf";
        GltfDataReader r = new GltfDataReader();
        GltfData gltfData = r.readGltfData(new URI(uriString));
        GlTF gltf = gltfData.getGltf();        

        String materialId = "blinn3-fx";
        SwingUtilities.invokeLater(() -> createAndShowGui(gltf, materialId));
    }

    private static void createAndShowGui(GlTF gltf, String materialId)
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        MaterialValuesEditorPanel materialValuesEditorPanel = 
            new MaterialValuesEditorPanel(gltf, materialId, null);
        f.getContentPane().add(materialValuesEditorPanel, BorderLayout.CENTER);
        
        JButton button = new JButton("Print");
        button.addActionListener(e ->
        {
            try
            {
                new GltfWriter().writeGltf(gltf, System.out);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        });
        f.getContentPane().add(button, BorderLayout.SOUTH);

        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
