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
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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
import de.javagl.jgltf.model.io.Buffers;
import de.javagl.jgltf.model.io.GltfWriter;
import de.javagl.jgltf.model.io.JsonUtils;
import de.javagl.swing.tasks.SwingTask;
import de.javagl.swing.tasks.SwingTaskExecutors;

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
     * The JSON string that the glTF was read from
     */
    private final String jsonString;
    
    /**
     * The FileChooser for saving files
     */
    private final JFileChooser saveFileChooser;
    
    /**
     * Create a factory for creating info components for the elements
     * that appear in the given {@link GltfData}
     * 
     * @param gltfData The backing {@link GltfData}
     * @param jsonString The JSON string that the glTF was read from. This 
     * may be <code>null</code> if the given {@link GltfData} was not read, 
     * but created programmatically.
     */
    InfoComponentFactory(GltfData gltfData, String jsonString)
    {
        this.gltfData = gltfData;
        this.jsonString = jsonString;
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

        boolean showLog = false;
        showLog = true;
        if (showLog)
        {
            // Avoid printing a whole data URI in the log
            String selectedValueString = String.valueOf(selectedValue);
            if (selectedValueString.length() > 100)
            {
                selectedValueString = 
                    selectedValueString.substring(0, 100) + "...";
            }
            logger.fine("selected      "+selectedValueString);
            logger.fine("pathString is "+pathString);
        }
        
        if (pathString.equals("glTF"))
        {
            return createJsonInfoComponent();
        }
        
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
     * Creates an info component that shows the JSON string representation
     * of the {@link GlTF} that was given in the constructor, or allows 
     * creating the JSON representation of the current {@link GlTF} if
     * the given one was <code>null</code>.
     * 
     * @return The info component
     */
    private JComponent createJsonInfoComponent()
    {
        JPanel jsonInfoPanel = new JPanel(new FlowLayout());
        
        if (jsonString == null)
        {
            jsonInfoPanel.add(new JLabel(
                "<html>The glTF was not read from a JSON input, <br>" + 
                "but created programmatically.</html>"));
            JButton createButton = new JButton("Create JSON...");
            createButton.addActionListener( e -> 
            {
                createButton.setText("Creating JSON...");
                createButton.setEnabled(false);
                createJson(jsonInfoPanel);
            });
            jsonInfoPanel.add(createButton);
        }
        else
        {
            jsonInfoPanel.setLayout(new BorderLayout());
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton formatButton = new JButton("Format JSON");
            formatButton.addActionListener( e -> 
            {
                String formattedJsonString = JsonUtils.format(jsonString);
                JComponent textInfoPanel = 
                    createTextInfoPanel("JSON:", formattedJsonString);
                jsonInfoPanel.removeAll();
                jsonInfoPanel.setLayout(new GridLayout(1,1));
                jsonInfoPanel.add(textInfoPanel);
                jsonInfoPanel.revalidate();
            });
            buttonPanel.add(formatButton);
            jsonInfoPanel.add(buttonPanel, BorderLayout.NORTH);
            
            JComponent textInfoPanel = 
                createTextInfoPanel("JSON:", jsonString);
            jsonInfoPanel.add(textInfoPanel, BorderLayout.CENTER);
            
            
        }
        
        return jsonInfoPanel;
    }
    
    /**
     * Start a background task to create the component containing the JSON 
     * representation of the current {@link GlTF}, and place it into the 
     * given panel.
     * 
     * @param jsonInfoPanel The target panel
     */
    private void createJson(JPanel jsonInfoPanel)
    {
        GlTF gltf = gltfData.getGltf();
        SwingTask<String, ?> swingTask = new SwingTask<String, Void>()
        {
            @Override
            protected String doInBackground() throws Exception
            {
                String createdJsonString = createJsonString(gltf);
                return createdJsonString;
            }
        };
        swingTask.addDoneCallback(task -> 
        {
            try
            {
                String createdJsonString = task.get();
                JComponent textInfoPanel = 
                    createTextInfoPanel("JSON:", createdJsonString);
                jsonInfoPanel.removeAll();
                jsonInfoPanel.setLayout(new GridLayout(1,1));
                jsonInfoPanel.add(textInfoPanel);
                jsonInfoPanel.revalidate();
            } 
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException e)
            {
                jsonInfoPanel.removeAll();
                jsonInfoPanel.setLayout(new GridLayout(1,1));
                JTextArea textArea = new JTextArea();
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                jsonInfoPanel.add(new JScrollPane(textArea));
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                textArea.setText(sw.toString());
                jsonInfoPanel.revalidate();
            }
        });
        SwingTaskExecutors.create(swingTask)
            .build()
            .execute();            
    }

    /**
     * Creates a JSON string representation of the given glTF. This string
     * may contain only an error message if creating the actual JSON string
     * caused an exception.
     * 
     * @param gltf The {@link GlTF}
     * @return The The JSON string
     */
    private static String createJsonString(GlTF gltf)
    {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
            GltfWriter gltfWriter = new GltfWriter();
            gltfWriter.writeGltf(gltf, baos);
            return new String(baos.toByteArray());
        }
        catch (IOException e)
        {
            return e.getMessage();
        }
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
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        dataPanel.add(new JScrollPane(textArea));
        
        updateAccessorDataText(textArea, accessor, 1);

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
            updateAccessorDataText(textArea, accessor, elementsPerRow);
        });

        return panel;
    }
    
    /**
     * Update the text in the given text area to show the data of the
     * given {@link Accessor}, with the specified number of elements 
     * per row. Depending on the size of the accessor data, this may
     * take long, so this is implemented as a background task that
     * is shielded by a modal dialog.
     * 
     * @param textArea The target text area 
     * @param accessor The {@link Accessor}
     * @param elementsPerRow The number of elements per row
     */
    private void updateAccessorDataText(
        JTextArea textArea, Accessor accessor, int elementsPerRow)
    {
        SwingTask<String, ?> swingTask = new SwingTask<String, Void>()
        {
            @Override
            protected String doInBackground() throws Exception
            {
                return createDataString(accessor, elementsPerRow);
            }
            
            @Override
            protected void done()
            {
                try
                {
                    String string = get();
                    textArea.setText(string);
                    SwingUtilities.invokeLater(() ->
                        textArea.scrollRectToVisible(new Rectangle(0,0,1,1)));
                } 
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                catch (ExecutionException e)
                {
                    // Should never happen: createDataString handles this
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    textArea.setText(sw.toString());
                }
            }
        };
        SwingTaskExecutors.create(swingTask)
            .setMillisToPopup(1000)
            .build()
            .execute();            
        
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
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return "Error while creating string. " + 
                "Input file may be invalid.\n" + sw.toString();
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
        ByteBuffer shaderData = gltfData.getShaderData(key);
        String shaderString = Buffers.readAsString(shaderData);
        if (shaderString == null)
        {
            return createMessageInfoPanel(
                "Could not find shader data for " + selectedValue + 
                "with ID " + key);
        }
        return createTextInfoPanel("Shader source code:", shaderString);
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
        ByteBuffer imageData = gltfData.getImageData(key);
        BufferedImage bufferedImage = Buffers.readAsBufferedImage(imageData);
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
     * Create an info component with the given text
     * 
     * @param title The title for the component
     * @param text The text
     * @return The component
     */
    private JComponent createTextInfoPanel(String title, String text)
    {
        JPanel textInfoPanel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel(title));
        
        JButton saveButton = new JButton("Save as...");
        saveButton.addActionListener(
            e -> saveTextAs(textInfoPanel, text));
        controlPanel.add(saveButton);
        
        textInfoPanel.add(controlPanel, BorderLayout.NORTH);
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textInfoPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return textInfoPanel;
    }

    /**
     * Open the save file chooser offering the option to save the given
     * text as a file
     * 
     * @param parent The parent component
     * @param text The text
     */
    private void saveTextAs(JComponent parent, String text)
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
                    saveTextAs(parent, text);
                    return;
                }
                if (overwriteOption != JOptionPane.YES_OPTION)
                {
                    return;
                }
            }
            
            try (FileWriter fileWriter = new FileWriter(file))
            {
                fileWriter.write(text);
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
            if (Objects.equals(entry.getValue(), value))
            {
                return entry.getKey();
            }
        }
        return null;
    }
    
}
