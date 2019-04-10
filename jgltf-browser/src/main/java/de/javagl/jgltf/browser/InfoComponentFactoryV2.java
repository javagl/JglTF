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
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;

/**
 * Utility class for creating info components for elements of a glTF 2.0 model
 */
class InfoComponentFactoryV2
{
    /**
     * The parent {@link InfoComponentFactory}
     */
    private final InfoComponentFactory parent;
    
    /**
     * The {@link GltfModel}
     */
    private final GltfModel gltfModel;
    
    /**
     * The {@link GlTF}
     */
    private final GlTF gltf;
    
    /**
     * Default constructor
     * 
     * @param parent The parent {@link InfoComponentFactory}
     * @param gltfModel The {@link GltfModel}
     * @param gltf The {@link GlTF}
     */
    InfoComponentFactoryV2(
        InfoComponentFactory parent, GltfModel gltfModel, GlTF gltf)
    {
        this.parent = parent;
        this.gltfModel = gltfModel;
        this.gltf = gltf;
    }
    
    /**
     * Create an info component for the given selected object, depending
     * on its type. May return <code>null</code> if the given object 
     * has a type for which no info component exists
     * 
     * @param selectedValue The selected value
     * @return The info component, or <code>null</code>
     */
    JComponent createGenericInfoComponent(Object selectedValue)
    {
        if (selectedValue instanceof Image)
        {
            return createImageInfoComponent(selectedValue);
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
        InfoComponentFactory.updateTextInBackground(textArea, 
            () -> createDataString(accessor, elementsPerRow));
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
        List<Accessor> accessors = Optionals.of(gltf.getAccessors());
        int index = accessors.indexOf(accessor);
        if (index == -1)
        {
            return "Invalid accessor index";
        }
        AccessorModel accessorModel = 
            gltfModel.getAccessorModels().get(index);
        AccessorData accessorData = accessorModel.getAccessorData();
        return AccessorDatas.createString(accessorData, elementsPerRow);
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
        List<Image> images = Optionals.of(gltf.getImages());
        int index = images.indexOf(selectedValue);
        if (index == -1)
        {
            return parent.createMessageInfoPanel(
                "Could not find image in glTF");
        }
        ImageModel imageModel = gltfModel.getImageModels().get(index);
        ByteBuffer imageData = imageModel.getImageData();
        BufferedImage bufferedImage = 
            InfoComponentFactory.readAsBufferedImage(imageData);
        if (bufferedImage == null)
        {
            return parent.createMessageInfoPanel(
                "Could not find image data for " + selectedValue + 
                " with index " + index);
        }
        return parent.createImageInfoPanel(bufferedImage);
    }

}
