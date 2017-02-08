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
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Material;
import de.javagl.jgltf.impl.v1.Technique;
import de.javagl.jgltf.impl.v1.TechniqueParameters;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.gl.Techniques;

/**
 * A panel that allows editing the {@link Material#getValues() values}
 * of a {@link Material} of a {@link GlTF}, <b>in-place</b>. <br>
 * <br> 
 * <b>This class is solely intended for internal tests and debugging!
 * It does modifications to the actual {@link GlTF} that may not be
 * reflected in the tree!</b>
 */
class MaterialValuesEditorPanel extends JPanel
{
    /**
     * Serial UID 
     */
    private static final long serialVersionUID = -121889548464611136L;

    /**
     * Create a panel for editing the values of the specified material in 
     * the given {@link GlTF} <b>in-place</b>.
     * 
     * @param gltf The {@link GlTF}
     * @param materialId The {@link Material} ID
     * @param callback An optional callback that may be called on changes
     */
    MaterialValuesEditorPanel(GlTF gltf, String materialId, Runnable callback)
    {
        super(new BorderLayout());
        
        add(new JLabel(materialId), BorderLayout.NORTH);
        
        Map<String, Material> materials = gltf.getMaterials();
        Material material = materials.get(materialId);
        Map<String, Object> values = Optionals.of(material.getValues());
        
        String techniqueId = material.getTechnique();
        Technique technique = gltf.getTechniques().get(techniqueId);
        
        JPanel panel = new JPanel(new GridLayout(0,2));
        Map<String, TechniqueParameters> parameters = 
            Optionals.of(technique.getParameters());
        for (Entry<String, TechniqueParameters> entry : parameters.entrySet())
        {
            String parameterName = entry.getKey();
            TechniqueParameters techniqueParameters = entry.getValue();
            
            //System.out.println(
            //    "For " + parameterName + " got " + techniqueParameters);
            
            if (isEditableFloatType(techniqueParameters))
            {
                panel.add(new JLabel(parameterName));
                panel.add(createEditor(
                    parameterName, techniqueParameters, values, callback));
            }
        }
        add(panel, BorderLayout.CENTER);
    }
     
    
    /**
     * Create an editor for the specified parameter of a material.
     * 
     * @param parameterName The parameter name
     * @param techniqueParameters The {@link TechniqueParameters}
     * @param values The values, as of {@link Material#getValues()}
     * @param callback An optional callback that may be called on changes
     * @return The editor
     */
    private static JTextField createEditor(
        String parameterName, TechniqueParameters techniqueParameters, 
        Map<String, Object> values, Runnable callback)
    {
        int numComponents = 
            Techniques.getNumComponentsForTechniqueParametersType(
                techniqueParameters.getType());
        
        // If the parameter does not have a value assigned, create one
        Object parameterValue = values.get(parameterName);
        if (parameterValue == null)
        {
            Object defaultValue = techniqueParameters.getValue();
            if (defaultValue == null)
            {
                parameterValue = new ArrayList<Number>(
                    Collections.nCopies(numComponents, 0.0));
                values.put(parameterName, parameterValue);
            }
            else
            {
                parameterValue = defaultValue;
                values.put(parameterName, parameterValue);
            }
        }
        
        // If the parameter is a single number, convert it into a 
        // list of numbers
        if (parameterValue instanceof Number)
        {
            Number oldParameterValue = (Number)parameterValue;
            List<Number> newParameterValue = new ArrayList<Number>();
            newParameterValue.add(oldParameterValue);
            parameterValue = newParameterValue;
            
            values.put(parameterName, parameterValue);
        }
        
        // Set up the text field which is the actual editor for the value
        @SuppressWarnings("unchecked")
        List<Number> numbers = (List<Number>)parameterValue;
        Consumer<List<? extends Number>> consumer = n -> 
        {
            for (int i=0; i<n.size(); i++)
            {
                numbers.set(i, n.get(i));
            }
            if (callback != null)
            {
                callback.run();
            }
        };
        JTextField editor = createFloatListEditor(numComponents, consumer);
        
        // Set the initial value for the text field
        String initialValue = numbers.stream()
            .map(Number::toString)
            .collect(Collectors.joining(", "));
        editor.setText(initialValue);
        return editor;
    }
   
    /**
     * Returns whether the type of the given {@link TechniqueParameters} is
     * a type that can be edited with this class - that is, a scalar float
     * value or a float vector without a semantic
     * 
     * @param techniqueParameters The {@link TechniqueParameters}
     * @return Whether the type is an editable float type
     */
    private static boolean isEditableFloatType(
        TechniqueParameters techniqueParameters)
    {
        if (techniqueParameters.getSemantic() != null)
        {
            return false;
        }
        switch (techniqueParameters.getType())
        {
            case GltfConstants.GL_FLOAT: 
            case GltfConstants.GL_FLOAT_VEC2:   
            case GltfConstants.GL_FLOAT_VEC3:   
            case GltfConstants.GL_FLOAT_VEC4:   
            {
                return true;
            }
            default:
                break;
        }
        return false;
    }

    /**
     * Create a text field that may be used to enter a list of float values.
     * The text will be parsed with {@link #parseFloatList(String)}. If a
     * valid float list with the given length can be parsed, it will be 
     * passed to the given consumer.
     *  
     * @param length The expected length of the float list
     * @param consumer The consumer
     * @return The text field
     */
    private static JTextField createFloatListEditor(
        int length, Consumer<List<? extends Number>> consumer)
    {
        JTextField textField = new JTextField();
        Document document = textField.getDocument();
        document.addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                triggerUpdate();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                triggerUpdate();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                triggerUpdate();
            }
            
            void triggerUpdate()
            {
                List<Float> list = parseFloatList(textField.getText());
                //System.out.println("Got "+list+" expected "+length);
                if (list != null && list.size() == length)
                {
                    textField.setForeground(Color.BLACK);
                    consumer.accept(list);
                }
                else
                {
                    textField.setForeground(Color.RED);
                }
            }
        
        });
        return textField;
    }
    
    /**
     * Parse a list of float values from the given string. The given string
     * will be split into tokens that may be separated by space 
     * <code>' '</code> or comma <code>','</code> characters, and each
     * token will be parsed into a float value. If the string cannot be
     * parsed, then <code>null</code> will be returned.
     * 
     * @param string The input string
     * @return The list of float values, or <code>null</code>
     */
    private static List<Float> parseFloatList(String string)
    {
        String tokens[] = string.replaceAll("^[,\\s]+", "").split("[,\\s]+");
        List<Float> list = new ArrayList<Float>();
        for (int i=0; i<tokens.length; i++)
        {
            try
            {
                list.add(Float.parseFloat(tokens[i]));
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }
        return list;
    }
    
    
}