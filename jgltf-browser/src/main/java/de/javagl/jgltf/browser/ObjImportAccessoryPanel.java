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
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.obj.BufferStrategy;

/**
 * A panel that will be used as the accessory component for the file chooser
 * for importing an OBJ file in the {@link GltfBrowserApplication}. If offers
 * configuration settings for the OBJ import
 */
class ObjImportAccessoryPanel extends JPanel
{
    /**
     * Serial UIID 
     */
    private static final long serialVersionUID = 557070448719270804L;
    
    /**
     * The combo box for the {@link BufferStrategy} for the OBJ import
     */
    private final JComboBox<BufferStrategy> bufferStrategyComboBox;
    
    /**
     * The combo box for the component type of the indices
     */
    private final JComboBox<Integer> indicesComponentTypeComboBox;
    
    /**
     * The checkbox indicating whether random colors should be assigned
     * to the parts that are created by the OBJ importer
     */
    private final JCheckBox assigningRandomColorsToPartsCheckBox;
    
    /**
     * Default constructor
     */
    ObjImportAccessoryPanel()
    {
        super(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            "OBJ import configuration settings:"));
        
        JPanel mainPanel = new JPanel(new GridLayout(0,2));
    
        mainPanel.add(new JLabel("Buffer creation strategy:"));
        bufferStrategyComboBox = 
            new JComboBox<BufferStrategy>(BufferStrategy.values());
        bufferStrategyComboBox.setSelectedItem(BufferStrategy.BUFFER_PER_FILE);
        mainPanel.add(bufferStrategyComboBox);
        
        mainPanel.add(new JLabel("Component type for mesh primitive indices:"));
        indicesComponentTypeComboBox = new JComboBox<Integer>(new Integer[] { 
            GltfConstants.GL_UNSIGNED_BYTE, 
            GltfConstants.GL_UNSIGNED_SHORT, 
            GltfConstants.GL_UNSIGNED_INT});
        indicesComponentTypeComboBox.setSelectedItem(
            GltfConstants.GL_UNSIGNED_SHORT);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer()
        {
            /**
             * Serial UID
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus)
            {
                super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                Number number = (Number)value;
                setText(GltfConstants.stringFor(number.intValue()));
                return this;
            }
        };
        indicesComponentTypeComboBox.setRenderer(renderer);
        mainPanel.add(indicesComponentTypeComboBox);
        
        assigningRandomColorsToPartsCheckBox = 
            new JCheckBox("Assign random colors to parts", false);
        mainPanel.add(assigningRandomColorsToPartsCheckBox);
        
        add(mainPanel, BorderLayout.NORTH);
    }
    
    /**
     * Returns the selected {@link BufferStrategy}
     * 
     * @return The {@link BufferStrategy}
     */
    BufferStrategy getSelectedBufferStrategy()
    {
        return (BufferStrategy) bufferStrategyComboBox.getSelectedItem();
    }
    
    /**
     * Returns the selected component type for the indices
     * 
     * @return The component type for the indices
     */
    Integer getSelectedIndicesComponentType()
    {
        return (Integer) indicesComponentTypeComboBox.getSelectedItem();
    }
    
    /**
     * Returns whether random colors should be assigned to the parts
     * that have been created when splitting the OBJ data
     * 
     * @return The flag
     */
    boolean isAssigningRandomColorsToParts()
    {
        return assigningRandomColorsToPartsCheckBox.isSelected();
    }
}
