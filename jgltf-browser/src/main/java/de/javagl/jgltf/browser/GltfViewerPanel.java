/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.browser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import de.javagl.jgltf.model.BoundingBoxes;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.viewer.GltfViewer;
import de.javagl.jgltf.viewer.RenderedCamera;
import de.javagl.jgltf.viewer.jogl.GltfViewerJogl;
import de.javagl.jgltf.viewer.lwjgl.GltfViewerLwjgl;

/**
 * A panel that contains a {@link GltfViewer} component, together with some
 * basic control components
 */
class GltfViewerPanel extends JPanel
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = -6213789785308105683L;

    /**
     * The {@link GltfModel} that is shown in the {@link GltfViewer}
     */
    private final GltfModel gltfModel;
    
    /**
     * The {@link GltfViewer} that may display the {@link GltfModel}
     */
    private GltfViewer<? extends Component> gltfViewer;
    
    /**
     * The container for the {@link GltfViewer#getRenderComponent()}
     */
    private final JPanel viewerComponentContainer;
    
    /**
     * The toggle button for the animations
     */
    private JToggleButton animationsRunningButton;
    
    /**
     * The combo box model containing the {@link RenderedCamera} instances 
     * that have been found in the {@link GltfModel}
     */
    private DefaultComboBoxModel<RenderedCamera> renderedCamerasComboBoxModel;
    
    /**
     * The external camera 
     */
    private final ExternalCameraRendering externalCamera;
    
    /**
     * Creates a new viewer panel for the given {@link GltfModel}
     * 
     * @param gltfModel The {@link GltfModel}
     */
    GltfViewerPanel(GltfModel gltfModel)
    {
        super(new BorderLayout());
        this.gltfModel = gltfModel;

        viewerComponentContainer = new JPanel(new GridLayout(1,1));
        add(viewerComponentContainer, BorderLayout.CENTER);
        
        this.externalCamera = new ExternalCameraRendering();
        
        add(createControlPanel(), BorderLayout.NORTH);
    }
    
    
    /**
     * Dispose the current {@link GltfViewer}. This will stop all animations,
     * remove the {@link GltfModel} from the viewer, and set the viewer 
     * to <code>null</code>
     */
    void disposeGltfViewer()
    {
        if (gltfViewer != null)
        {
            gltfViewer.setAnimationsRunning(false);
            gltfViewer.removeGltfModel(gltfModel);
        }
        gltfViewer = null;
    }
    
    /**
     * Create and return the control panel
     * 
     * @return The control panel
     */
    private JPanel createControlPanel()
    {
        JPanel mainControlPanel = 
            new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 1));
        
        // The button to show the current glTF
        JButton showButton = new JButton("Show current glTF");
        mainControlPanel.add(showButton);

        // The combo box for selecting the JOGL- or LWJGL viewer implementation
        mainControlPanel.add(new JLabel("Viewer implementation:"));
        JComboBox<String> viewerImplementationComboBox =
            new JComboBox<String>(new String[] { "JOGL", "LWJGL" });
        viewerImplementationComboBox.setSelectedIndex(0);
        mainControlPanel.add(viewerImplementationComboBox);
        
        // When the "show" button is clicked or a viewer implementation
        // is selected, then create the viewer component
        ActionListener actionListener = e -> 
        {
            createViewer(String.valueOf(
                viewerImplementationComboBox.getSelectedItem()));
            showButton.setEnabled(false);
        };
        showButton.addActionListener(actionListener);
        viewerImplementationComboBox.addActionListener(actionListener);
        
        // The toggle button for the animations
        animationsRunningButton = new JToggleButton("Animation");
        animationsRunningButton.setSelected(false);
        animationsRunningButton.addActionListener(e -> 
        {
            if (gltfViewer != null)
            {
                gltfViewer.setAnimationsRunning(
                    animationsRunningButton.isSelected());
            }
        });
        mainControlPanel.add(animationsRunningButton);
        
        JPanel cameraControlPanel = 
            new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 1));
        
        // The combo box for selecting the camera
        cameraControlPanel.add(new JLabel("Camera:"));
        renderedCamerasComboBoxModel = 
            new DefaultComboBoxModel<RenderedCamera>();
        JComboBox<RenderedCamera> renderedCamerasComboBox = 
            new JComboBox<RenderedCamera>(renderedCamerasComboBoxModel);
        renderedCamerasComboBox.setRenderer(new DefaultListCellRenderer() 
        {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, 
                boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                if (value == null)
                {
                    setText("(External camera)");
                }
                else
                {
                    RenderedCamera renderedCamera = (RenderedCamera)value;
                    setText(renderedCamera.toString());
                }
                return this;
            }
        });
        renderedCamerasComboBox.addActionListener(e ->
        {
            RenderedCamera renderedCamera = 
                (RenderedCamera)renderedCamerasComboBox.getSelectedItem();
            if (gltfViewer != null)
            {
                if (renderedCamera != null)
                {
                    gltfViewer.setRenderedCamera(renderedCamera);
                }
                else
                {
                    gltfViewer.setRenderedCamera(externalCamera);
                }
            }
        });
        updateRenderedCamerasComboBox();
        cameraControlPanel.add(renderedCamerasComboBox);
        
        JButton resetCameraButton = new JButton("Reset camera");
        resetCameraButton.addActionListener(e -> resetExternalCamera());
        cameraControlPanel.add(resetCameraButton);
        
        JButton fitCameraButton = new JButton("Fit camera");
        fitCameraButton.addActionListener(e -> fitExternalCamera());
        cameraControlPanel.add(fitCameraButton);
        
        JPanel p = new JPanel(new GridLayout(0,1));
        p.add(mainControlPanel);
        p.add(cameraControlPanel);
        return p;
    }

    /**
     * Create the {@link GltfViewer} depending on the given implementation
     * name, which may be "JOGL" or "LWJGL"
     * 
     * @param implementation The implementation name
     */
    private void createViewer(String implementation)
    {
        if ("JOGL".equals(implementation))
        {
            createViewer(GltfViewerJogl::new);
        }
        else
        {
            createViewer(GltfViewerLwjgl::new);
        }
    }
    
    /**
     * Update the combo box containing the {@link RenderedCamera} names, 
     * based on the current {@link GltfModel}
     */
    private void updateRenderedCamerasComboBox()
    {
        renderedCamerasComboBoxModel.removeAllElements();
        renderedCamerasComboBoxModel.addElement(null);
        if (gltfViewer != null)
        {
            List<RenderedCamera> renderedCameras = 
                gltfViewer.getRenderedCameras();
            for (RenderedCamera renderedCamera : renderedCameras)
            {
                renderedCamerasComboBoxModel.addElement(renderedCamera);
            }
        }
    }
    
    /**
     * Reset the external camera to its initial configuration
     */
    private void resetExternalCamera()
    {
        de.javagl.rendering.core.view.Camera camera = 
            externalCamera.getCamera();
        camera.setEyePoint(new Point3f(0, 0, 1));
        camera.setViewPoint(new Point3f(0, 0, 0)); 
        camera.setUpVector(new Vector3f(0, 1, 0));
        camera.setFovDegY(60.0f);            
    }
    
    /**
     * Fit the external camera to show the whole scene contents 
     */
    private void fitExternalCamera()
    {
        // Note: This is a VERY simple implementation that does not 
        // guarantee the "tightest fitting" view configuration, but 
        // generously moves the camera so that for usual scenes 
        // everything is visible, regardless of the aspect ratio
        
        float minMax[] = BoundingBoxes.computeBoundingBoxMinMax(gltfModel);
        
        // Compute diagonal length and center of the bounding box
        Point3f min = new Point3f();
        min.x = minMax[0];
        min.y = minMax[1];
        min.z = minMax[2];
        
        Point3f max = new Point3f();
        max.x = minMax[3];
        max.y = minMax[4];
        max.z = minMax[5];

        float diagonalLength = max.distance(min);

        Point3f center = new Point3f();
        Point3f size = new Point3f();
        size.sub(max, min);
        center.scaleAdd(0.5f, size, min);
        
        // Compute the normal of the view plane (i.e. the normalized
        // direction from the view point to the eye point)
        de.javagl.rendering.core.view.Camera camera = 
            externalCamera.getCamera();
        Vector3f viewPlaneNormal = new Vector3f();
        Point3f eyePoint = camera.getEyePoint();
        Point3f viewPoint = camera.getViewPoint();
        viewPlaneNormal.sub(eyePoint, viewPoint);
        viewPlaneNormal.normalize();
        
        // Compute the required viewing distance, and apply
        // it to the camera
        float fovRadY = (float) Math.toRadians(camera.getFovDegY());
        float distance = 
            (float) (diagonalLength * 0.5 / Math.tan(fovRadY * 0.5));
        
        Point3f newViewPoint = new Point3f(center);
        Point3f newEyePoint = new Point3f();
        newEyePoint.scaleAdd(distance, viewPlaneNormal, newViewPoint);

        camera.setEyePoint(newEyePoint);
        camera.setViewPoint(newViewPoint); 
    }

    /**
     * Create the viewer component using the given constructor. 
     * 
     * @param constructor The constructor.
     */
    private void createViewer(
        Supplier<? extends GltfViewer<? extends Component>> constructor)
    {
        disposeGltfViewer();
        animationsRunningButton.setSelected(false);
        animationsRunningButton.setEnabled(false);
        viewerComponentContainer.removeAll();
        try
        {
            gltfViewer = constructor.get();
            gltfViewer.setAnimationsRunning(false);
            gltfViewer.addGltfModel(gltfModel);
            
            Component renderComponent = gltfViewer.getRenderComponent();
            externalCamera.setComponent(renderComponent);
            
            gltfViewer.setRenderedCamera(externalCamera);
            viewerComponentContainer.add(renderComponent);
            animationsRunningButton.setEnabled(true);
            
            updateRenderedCamerasComboBox();
        }
        catch (Throwable t)
        {
            // The constructor may throw everything. Particularly, when 
            // the native library can not be found, it will throw an
            // UnsatisfiedLinkError (oh how we love it...). 
            // All this is handled here, pragmatically...
            StringWriter stringWriter = new StringWriter();
            t.printStackTrace(new PrintWriter(stringWriter));
            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setText(
                "Error while creating viewer:\n" + stringWriter.toString());
            viewerComponentContainer.add(new JScrollPane(textArea));
        }
        revalidate();
        repaint();
    }
    
    // For some reason, calling repaint() on this panel does
    // not trigger an actual repaint of the render component. 
    // TODO Figure out why, then remove this workaround: 
    @Override
    public void repaint(long tm, int x, int y, int width, int height) 
    {
        super.repaint(tm, x, y, width, height);
        if (gltfViewer != null)
        {
            gltfViewer.triggerRendering();
        }
    }
}
