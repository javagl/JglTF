/*
 * 3D Tiles EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

import java.util.ArrayList;
import java.util.List;
import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * An object defining the values of an enum. 
 * 
 * Auto-generated for enum.schema.json 
 * 
 */
public class MetadataEnum
    extends GlTFProperty
{

    /**
     * The name of the enum, e.g. for display purposes. (optional) 
     * 
     */
    private String name;
    /**
     * The description of the enum. (optional) 
     * 
     */
    private String description;
    /**
     * The type of the integer enum value. (optional)<br> 
     * Default: "UINT16"<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64] 
     * 
     */
    private String valueType;
    /**
     * An array of enum values. Duplicate names or duplicate integer values 
     * are not allowed. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An enum value. (optional) 
     * 
     */
    private List<EnumValue> values;

    /**
     * The name of the enum, e.g. for display purposes. (optional) 
     * 
     * @param name The name to set
     * 
     */
    public void setName(String name) {
        if (name == null) {
            this.name = name;
            return ;
        }
        this.name = name;
    }

    /**
     * The name of the enum, e.g. for display purposes. (optional) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * The description of the enum. (optional) 
     * 
     * @param description The description to set
     * 
     */
    public void setDescription(String description) {
        if (description == null) {
            this.description = description;
            return ;
        }
        this.description = description;
    }

    /**
     * The description of the enum. (optional) 
     * 
     * @return The description
     * 
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * The type of the integer enum value. (optional)<br> 
     * Default: "UINT16"<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64] 
     * 
     * @param valueType The valueType to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setValueType(String valueType) {
        if (valueType == null) {
            this.valueType = valueType;
            return ;
        }
        if ((((((((!"INT8".equals(valueType))&&(!"UINT8".equals(valueType)))&&(!"INT16".equals(valueType)))&&(!"UINT16".equals(valueType)))&&(!"INT32".equals(valueType)))&&(!"UINT32".equals(valueType)))&&(!"INT64".equals(valueType)))&&(!"UINT64".equals(valueType))) {
            throw new IllegalArgumentException((("Invalid value for valueType: "+ valueType)+", valid: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, UINT64]"));
        }
        this.valueType = valueType;
    }

    /**
     * The type of the integer enum value. (optional)<br> 
     * Default: "UINT16"<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64] 
     * 
     * @return The valueType
     * 
     */
    public String getValueType() {
        return this.valueType;
    }

    /**
     * Returns the default value of the valueType<br> 
     * @see #getValueType 
     * 
     * @return The default valueType
     * 
     */
    public String defaultValueType() {
        return "UINT16";
    }

    /**
     * An array of enum values. Duplicate names or duplicate integer values 
     * are not allowed. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An enum value. (optional) 
     * 
     * @param values The values to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setValues(List<EnumValue> values) {
        if (values == null) {
            throw new NullPointerException((("Invalid value for values: "+ values)+", may not be null"));
        }
        if (values.size()< 1) {
            throw new IllegalArgumentException("Number of values elements is < 1");
        }
        this.values = values;
    }

    /**
     * An array of enum values. Duplicate names or duplicate integer values 
     * are not allowed. (required)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;An enum value. (optional) 
     * 
     * @return The values
     * 
     */
    public List<EnumValue> getValues() {
        return this.values;
    }

    /**
     * Add the given values. The values of this instance will be replaced 
     * with a list that contains all previous elements, and additionally the 
     * new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addValues(EnumValue element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<EnumValue> oldList = this.values;
        List<EnumValue> newList = new ArrayList<EnumValue>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.values = newList;
    }

    /**
     * Remove the given values. The values of this instance will be replaced 
     * with a list that contains all previous elements, except for the 
     * removed one. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removeValues(EnumValue element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<EnumValue> oldList = this.values;
        List<EnumValue> newList = new ArrayList<EnumValue>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        this.values = newList;
    }

}
