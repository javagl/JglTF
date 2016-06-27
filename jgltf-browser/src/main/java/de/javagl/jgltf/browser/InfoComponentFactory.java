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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import de.javagl.jgltf.impl.Accessor;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Image;
import de.javagl.jgltf.impl.Shader;
import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfData;

/**
 * A class for creating components that display information about 
 * glTF elements
 */
class InfoComponentFactory
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(InfoComponentFactory.class.getName());
    
    /**
     * The {@link GltfData} that the information will be obtained from
     */
    private final GltfData gltfData;
    
    /**
     * The FileChooser for saving files
     */
    private final JFileChooser saveFileChooser;
    
    /**
     * Create a factory for creating info components for the elements
     * that appear in the given {@link GltfData}
     * 
     * @param gltfData The backing {@link GltfData}
     */
    InfoComponentFactory(GltfData gltfData)
    {
        this.gltfData = gltfData;
        this.saveFileChooser = new JFileChooser();
    }
    
    /**
     * Create the info component for the given tree selection path
     * 
     * @param selectionPath The tree selection path
     * @return The info component (may be <code>null</code>)
     */
    JComponent createInfoComponent(TreePath selectionPath)
    {
        if (selectionPath == null)
        {
            return null;
        }
        Object selectedValue = ObjectTrees.getNodeEntryValue(selectionPath);
        String pathString = ObjectTrees.createPathString(selectionPath);

        logger.fine("selected      "+selectedValue);
        logger.fine("pathString is "+pathString);
        
        // Check if the selected path is a GL constant. In this case, 
        // just display the string representation of the GL constant
        Set<String> glConstantPaths = new LinkedHashSet<String>();
        glConstantPaths.add("glTF.shaders.*.type");
        glConstantPaths.add("glTF.accessors.*.componentType");
        glConstantPaths.add("glTF.techniques.*.parameters.*.type");
        glConstantPaths.add("glTF.techniques.*.states.enable.*");
        glConstantPaths.add("glTF.textures.*.format");
        glConstantPaths.add("glTF.textures.*.internalFormat");
        glConstantPaths.add("glTF.textures.*.target");
        glConstantPaths.add("glTF.textures.*.type");
        glConstantPaths.add("glTF.bufferViews.*.target");
        for (String glConstantPath : glConstantPaths)
        {
            if (RegEx.matches(pathString, glConstantPath))
            {
                if (selectedValue instanceof Number)
                {
                    Number glConstant = (Number)selectedValue;
                    String glConstantString = 
                        GltfConstants.stringFor(glConstant.intValue());
                    return createMessageInfoPanel(
                        "GL constant: " + glConstantString);
                }
                return createMessageInfoPanel(
                    "Unexpected entry: " + selectedValue);
            }
        }
        
        return createGenericInfoComponent(selectedValue);
    }
    
    
    /**
     * Create an info component for the given selected object, depending
     * on its type. May return <code>null</code> if the given object 
     * has a type for which no info component exists
     * 
     * @param selectedValue The selected value
     * @return The info component, or <code>null</code>
     */
    private JComponent createGenericInfoComponent(Object selectedValue)
    {
        if (selectedValue instanceof Image)
        {
            return createImageInfoComponent(selectedValue);
        }
        if (selectedValue instanceof Shader)
        {
            return createShaderInfoComponent(selectedValue);
        }
        if (selectedValue instanceof Accessor)
        {
            Accessor accessor = (Accessor)selectedValue;
            return createAccessorInfoComponent(accessor);
        }
        return null;
    }

    /**
     * Create an info component for the given {@link Accessor}
     * 
     * @param accessor The {@link Accessor}
     * @return The info component
     */
    private JComponent createAccessorInfoComponent(Accessor accessor)
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Elements per row:"));
        JSpinner numElementsPerRowSpinner = 
            new JSpinner(new SpinnerNumberModel(1, 0, (1<<20), 1));
        controlPanel.add(numElementsPerRowSpinner);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.add(new JLabel("Accessor data:"), BorderLayout.NORTH);
        JTextArea textArea = new JTextArea(
            createDataString(accessor, 1));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        dataPanel.add(new JScrollPane(textArea));

        panel.add(dataPanel, BorderLayout.CENTER);
        
        numElementsPerRowSpinner.addChangeListener(e -> 
        {
            Object valueObject = numElementsPerRowSpinner.getValue();
            Number valueNumber = (Number)valueObject;
            int elementsPerRow = valueNumber.intValue();
            if (elementsPerRow <= 0)
            {
                elementsPerRow = Integer.MAX_VALUE;
            }
            textArea.setText(createDataString(accessor, elementsPerRow));
            SwingUtilities.invokeLater(() ->
                textArea.scrollRectToVisible(new Rectangle(0,0,1,1)));
        });

        return panel;
        
        
    }
    
    /**
     * Create a (possibly large) string representation of the data of 
     * the given accessor
     * 
     * @param accessor The accessor
     * @param elementsPerRow The number of elements per row
     * @return The string
     */
    private String createDataString(Accessor accessor, int elementsPerRow)
    {
        try
        {
            return createDataStringUnchecked(accessor, elementsPerRow);
        }
        catch (Exception e)
        {
            // When there have been errors in the input file, many
            // things can go wrong when trying to create the data
            // string. Handle this case here ... pragmatically: 
            return "(Error: " + e.getMessage() + ")";
        }
    }
    
    /**
     * Create a (possibly large) string representation of the data of 
     * the given accessor
     * 
     * @param accessor The accessor
     * @param elementsPerRow The number of elements per row
     * @return The string
     */
    private String createDataStringUnchecked(
        Accessor accessor, int elementsPerRow)
    {
        if (accessor == null)
        {
            return "(null)";
        }
        if (AccessorDatas.hasByteComponents(accessor))
        {
            AccessorByteData accessorData = 
                AccessorDatas.createByte(accessor, gltfData);
            String accessorDataString = 
                accessorData.createString(
                    Locale.ENGLISH, "%4d", elementsPerRow);
            return accessorDataString;
        }
        if (AccessorDatas.hasShortComponents(accessor))
        {
            AccessorShortData accessorData = 
                AccessorDatas.createShort(accessor, gltfData);
            String accessorDataString = 
                accessorData.createString(
                    Locale.ENGLISH, "%6d", elementsPerRow);
            return accessorDataString;
        }
        if (AccessorDatas.hasIntComponents(accessor))
        {
            AccessorIntData accessorData = 
                AccessorDatas.createInt(accessor, gltfData);
            String accessorDataString = 
                accessorData.createString(
                    Locale.ENGLISH, "%11d", elementsPerRow);
            return accessorDataString;
        }
        if (AccessorDatas.hasFloatComponents(accessor))
        {
            AccessorFloatData accessorData = 
                AccessorDatas.createFloat(accessor, gltfData);
            String accessorDataString = 
                accessorData.createString(
                    Locale.ENGLISH, "%10.5f", elementsPerRow);
            return accessorDataString;
        }
        return "Invalid accessor component type: "+
            GltfConstants.stringFor(accessor.getComponentType());

        
    }

    /**
     * Create an info component for the given selected value (which is 
     * assumed to be an {@link Shader})
     * 
     * @param selectedValue The selected value
     * @return The info component
     */
    private JComponent createShaderInfoComponent(Object selectedValue)
    {
        GlTF gltf = gltfData.getGltf();
        Map<String, Shader> map = gltf.getShaders();
        String key = findKey(map, selectedValue);
        if (key == null)
        {
            return createMessageInfoPanel("Could not find shader in glTF");
        }
        String shaderString = gltfData.getShaderAsString(key);
        if (shaderString == null)
        {
            return createMessageInfoPanel(
                "Could not find shader data for " + selectedValue + 
                "with ID " + key);
        }
        return createShaderInfoPanel(shaderString);
    }

    /**
     * Create an info component for the given selected value (which is 
     * assumed to be an {@link Image})
     * 
     * @param selectedValue The selected value
     * @return The info component
     */
    private JComponent createImageInfoComponent(Object selectedValue)
    {
        GlTF gltf = gltfData.getGltf();
        Map<String, Image> map = gltf.getImages();
        String key = findKey(map, selectedValue);
        if (key == null)
        {
            return createMessageInfoPanel("Could not find image in glTF");
        }
        BufferedImage bufferedImage = gltfData.getImageAsBufferedImage(key);
        if (bufferedImage == null)
        {
            return createMessageInfoPanel(
                "Could not find image data for " + selectedValue + 
                "with ID " + key);
        }
        return createImageInfoPanel(bufferedImage);
    }

    /**
     * Create an info component with the given message
     * 
     * @param string The message string
     * @return The component
     */
    private JComponent createMessageInfoPanel(String string)
    {
        JPanel messageInfoPanel = new JPanel(new FlowLayout());
        messageInfoPanel.add(new JLabel(string));
        return messageInfoPanel;
    }

    /**
     * Create an info component with the given image
     * 
     * @param image The image
     * @return The component
     */
    private JComponent createImageInfoPanel(BufferedImage image)
    {
        JPanel imageInfoPanel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Image:"));
        
        JButton saveButton = new JButton("Save as PNG...");
        saveButton.addActionListener(
            e -> saveImageAs(imageInfoPanel, image));
        controlPanel.add(saveButton);
        
        imageInfoPanel.add(controlPanel, BorderLayout.NORTH);
        imageInfoPanel.add(new JScrollPane(
            new JLabel(new ImageIcon(image))), BorderLayout.CENTER);
        return imageInfoPanel;
    }
    
    /**
     * Open the save file chooser offering the option to save the given
     * image as a file
     * 
     * @param parent The parent component
     * @param image The image
     */
    private void saveImageAs(JComponent parent, BufferedImage image)
    {
        int option = saveFileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            File file = saveFileChooser.getSelectedFile();
            if (file.exists())
            {
                int overwriteOption = 
                    JOptionPane.showConfirmDialog(
                        parent, "File exists. Overwrite?");
                if (overwriteOption == JOptionPane.CANCEL_OPTION)
                {
                    saveImageAs(parent, image);
                    return;
                }
                if (overwriteOption != JOptionPane.YES_OPTION)
                {
                    return;
                }
            }
            try
            {
                ImageIO.write(image, "png", file);
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(parent, 
                    "Error while writing file: "+e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * Create an info component with the given shader code
     * 
     * @param shaderString The shader code
     * @return The component
     */
    private JComponent createShaderInfoPanel(String shaderString)
    {
        JPanel shaderInfoPanel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Shader source code:"));
        
        JButton saveButton = new JButton("Save as...");
        saveButton.addActionListener(
            e -> saveShaderAs(shaderInfoPanel, shaderString));
        controlPanel.add(saveButton);
        
        shaderInfoPanel.add(controlPanel, BorderLayout.NORTH);
        JTextArea textArea = new JTextArea(shaderString);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        shaderInfoPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return shaderInfoPanel;
    }

    /**
     * Open the save file chooser offering the option to save the given
     * shader source code as a file
     * 
     * @param parent The parent component
     * @param shaderString The shader string
     */
    private void saveShaderAs(JComponent parent, String shaderString)
    {
        int option = saveFileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            File file = saveFileChooser.getSelectedFile();
            if (file.exists())
            {
                int overwriteOption = 
                    JOptionPane.showConfirmDialog(
                        parent, "File exists. Overwrite?");
                if (overwriteOption == JOptionPane.CANCEL_OPTION)
                {
                    saveShaderAs(parent, shaderString);
                    return;
                }
                if (overwriteOption != JOptionPane.YES_OPTION)
                {
                    return;
                }
            }
            
            try (FileWriter fileWriter = new FileWriter(file))
            {
                fileWriter.write(shaderString);
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(parent, 
                    "Error while writing file: "+e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Find the key of the given map that is mapped to the given value.
     * Returns <code>null</code> if the given map is <code>null</code>,
     * or if no matching key is found
     * 
     * @param map The map
     * @param value The value
     * @return The key
     */
    private static <K> K findKey(Map<K, ?> map, Object value)
    {
        if (map == null)
        {
            return null;
        }
        for (Entry<K, ?> entry : map.entrySet())
        {
            if (entry.getValue() == value)
            {
                return entry.getKey();
            }
        }
        return null;
    }
    
}
