/*
 * EXT_structural_metadata JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.structural_metadata;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * An array of binary property values. 
 * 
 * Auto-generated for propertyTable.property.schema.json 
 * 
 */
public class PropertyTableProperty
    extends GlTFProperty
{

    /**
     * The index of the buffer view containing property values. The data type 
     * of property values is determined by the property definition: When 
     * `type` is `BOOLEAN` values are packed into a bitstream. When `type` is 
     * `STRING` values are stored as byte sequences and decoded as UTF-8 
     * strings. When `type` is `SCALAR`, `VECN`, or `MATN` the values are 
     * stored as the provided `componentType` and the buffer view 
     * `byteOffset` must be aligned to a multiple of the `componentType` 
     * size. When `type` is `ENUM` values are stored as the enum's 
     * `valueType` and the buffer view `byteOffset` must be aligned to a 
     * multiple of the `valueType` size. Each enum value in the array must 
     * match one of the allowed values in the enum definition. `arrayOffsets` 
     * is required for variable-length arrays and `stringOffsets` is required 
     * for strings (for variable-length arrays of strings, both are 
     * required). (required) 
     * 
     */
    private Integer values;
    /**
     * The index of the buffer view containing offsets for variable-length 
     * arrays. The number of offsets is equal to the property table `count` 
     * plus one. The offsets represent the start positions of each array, 
     * with the last offset representing the position after the last array. 
     * The array length is computed using the difference between the 
     * subsequent offset and the current offset. If `type` is `STRING` the 
     * offsets index into the string offsets array (stored in 
     * `stringOffsets`), otherwise they index into the property array (stored 
     * in `values`). The data type of these offsets is determined by 
     * `arrayOffsetType`. The buffer view `byteOffset` must be aligned to a 
     * multiple of the `arrayOffsetType` size. (optional) 
     * 
     */
    private Integer arrayOffsets;
    /**
     * The index of the buffer view containing offsets for strings. The 
     * number of offsets is equal to the number of string elements plus one. 
     * The offsets represent the byte offsets of each string in the property 
     * array (stored in `values`), with the last offset representing the byte 
     * offset after the last string. The string byte length is computed using 
     * the difference between the subsequent offset and the current offset. 
     * The data type of these offsets is determined by `stringOffsetType`. 
     * The buffer view `byteOffset` must be aligned to a multiple of the 
     * `stringOffsetType` size. (optional) 
     * 
     */
    private Integer stringOffsets;
    /**
     * The type of values in `arrayOffsets`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     */
    private String arrayOffsetType;
    /**
     * The type of values in `stringOffsets`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     */
    private String stringOffsetType;
    /**
     * An offset to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `offset` if both are 
     * defined. (optional) 
     * 
     */
    private Object offset;
    /**
     * A scale to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `scale` if both are 
     * defined. (optional) 
     * 
     */
    private Object scale;
    /**
     * Maximum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the maximum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     */
    private Object max;
    /**
     * Minimum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the minimum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     */
    private Object min;

    /**
     * The index of the buffer view containing property values. The data type 
     * of property values is determined by the property definition: When 
     * `type` is `BOOLEAN` values are packed into a bitstream. When `type` is 
     * `STRING` values are stored as byte sequences and decoded as UTF-8 
     * strings. When `type` is `SCALAR`, `VECN`, or `MATN` the values are 
     * stored as the provided `componentType` and the buffer view 
     * `byteOffset` must be aligned to a multiple of the `componentType` 
     * size. When `type` is `ENUM` values are stored as the enum's 
     * `valueType` and the buffer view `byteOffset` must be aligned to a 
     * multiple of the `valueType` size. Each enum value in the array must 
     * match one of the allowed values in the enum definition. `arrayOffsets` 
     * is required for variable-length arrays and `stringOffsets` is required 
     * for strings (for variable-length arrays of strings, both are 
     * required). (required) 
     * 
     * @param values The values to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setValues(Integer values) {
        if (values == null) {
            throw new NullPointerException((("Invalid value for values: "+ values)+", may not be null"));
        }
        this.values = values;
    }

    /**
     * The index of the buffer view containing property values. The data type 
     * of property values is determined by the property definition: When 
     * `type` is `BOOLEAN` values are packed into a bitstream. When `type` is 
     * `STRING` values are stored as byte sequences and decoded as UTF-8 
     * strings. When `type` is `SCALAR`, `VECN`, or `MATN` the values are 
     * stored as the provided `componentType` and the buffer view 
     * `byteOffset` must be aligned to a multiple of the `componentType` 
     * size. When `type` is `ENUM` values are stored as the enum's 
     * `valueType` and the buffer view `byteOffset` must be aligned to a 
     * multiple of the `valueType` size. Each enum value in the array must 
     * match one of the allowed values in the enum definition. `arrayOffsets` 
     * is required for variable-length arrays and `stringOffsets` is required 
     * for strings (for variable-length arrays of strings, both are 
     * required). (required) 
     * 
     * @return The values
     * 
     */
    public Integer getValues() {
        return this.values;
    }

    /**
     * The index of the buffer view containing offsets for variable-length 
     * arrays. The number of offsets is equal to the property table `count` 
     * plus one. The offsets represent the start positions of each array, 
     * with the last offset representing the position after the last array. 
     * The array length is computed using the difference between the 
     * subsequent offset and the current offset. If `type` is `STRING` the 
     * offsets index into the string offsets array (stored in 
     * `stringOffsets`), otherwise they index into the property array (stored 
     * in `values`). The data type of these offsets is determined by 
     * `arrayOffsetType`. The buffer view `byteOffset` must be aligned to a 
     * multiple of the `arrayOffsetType` size. (optional) 
     * 
     * @param arrayOffsets The arrayOffsets to set
     * 
     */
    public void setArrayOffsets(Integer arrayOffsets) {
        if (arrayOffsets == null) {
            this.arrayOffsets = arrayOffsets;
            return ;
        }
        this.arrayOffsets = arrayOffsets;
    }

    /**
     * The index of the buffer view containing offsets for variable-length 
     * arrays. The number of offsets is equal to the property table `count` 
     * plus one. The offsets represent the start positions of each array, 
     * with the last offset representing the position after the last array. 
     * The array length is computed using the difference between the 
     * subsequent offset and the current offset. If `type` is `STRING` the 
     * offsets index into the string offsets array (stored in 
     * `stringOffsets`), otherwise they index into the property array (stored 
     * in `values`). The data type of these offsets is determined by 
     * `arrayOffsetType`. The buffer view `byteOffset` must be aligned to a 
     * multiple of the `arrayOffsetType` size. (optional) 
     * 
     * @return The arrayOffsets
     * 
     */
    public Integer getArrayOffsets() {
        return this.arrayOffsets;
    }

    /**
     * The index of the buffer view containing offsets for strings. The 
     * number of offsets is equal to the number of string elements plus one. 
     * The offsets represent the byte offsets of each string in the property 
     * array (stored in `values`), with the last offset representing the byte 
     * offset after the last string. The string byte length is computed using 
     * the difference between the subsequent offset and the current offset. 
     * The data type of these offsets is determined by `stringOffsetType`. 
     * The buffer view `byteOffset` must be aligned to a multiple of the 
     * `stringOffsetType` size. (optional) 
     * 
     * @param stringOffsets The stringOffsets to set
     * 
     */
    public void setStringOffsets(Integer stringOffsets) {
        if (stringOffsets == null) {
            this.stringOffsets = stringOffsets;
            return ;
        }
        this.stringOffsets = stringOffsets;
    }

    /**
     * The index of the buffer view containing offsets for strings. The 
     * number of offsets is equal to the number of string elements plus one. 
     * The offsets represent the byte offsets of each string in the property 
     * array (stored in `values`), with the last offset representing the byte 
     * offset after the last string. The string byte length is computed using 
     * the difference between the subsequent offset and the current offset. 
     * The data type of these offsets is determined by `stringOffsetType`. 
     * The buffer view `byteOffset` must be aligned to a multiple of the 
     * `stringOffsetType` size. (optional) 
     * 
     * @return The stringOffsets
     * 
     */
    public Integer getStringOffsets() {
        return this.stringOffsets;
    }

    /**
     * The type of values in `arrayOffsets`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     * @param arrayOffsetType The arrayOffsetType to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setArrayOffsetType(String arrayOffsetType) {
        if (arrayOffsetType == null) {
            this.arrayOffsetType = arrayOffsetType;
            return ;
        }
        if ((((!"UINT8".equals(arrayOffsetType))&&(!"UINT16".equals(arrayOffsetType)))&&(!"UINT32".equals(arrayOffsetType)))&&(!"UINT64".equals(arrayOffsetType))) {
            throw new IllegalArgumentException((("Invalid value for arrayOffsetType: "+ arrayOffsetType)+", valid: [UINT8, UINT16, UINT32, UINT64]"));
        }
        this.arrayOffsetType = arrayOffsetType;
    }

    /**
     * The type of values in `arrayOffsets`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     * @return The arrayOffsetType
     * 
     */
    public String getArrayOffsetType() {
        return this.arrayOffsetType;
    }

    /**
     * Returns the default value of the arrayOffsetType<br> 
     * @see #getArrayOffsetType 
     * 
     * @return The default arrayOffsetType
     * 
     */
    public String defaultArrayOffsetType() {
        return "UINT32";
    }

    /**
     * The type of values in `stringOffsets`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     * @param stringOffsetType The stringOffsetType to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setStringOffsetType(String stringOffsetType) {
        if (stringOffsetType == null) {
            this.stringOffsetType = stringOffsetType;
            return ;
        }
        if ((((!"UINT8".equals(stringOffsetType))&&(!"UINT16".equals(stringOffsetType)))&&(!"UINT32".equals(stringOffsetType)))&&(!"UINT64".equals(stringOffsetType))) {
            throw new IllegalArgumentException((("Invalid value for stringOffsetType: "+ stringOffsetType)+", valid: [UINT8, UINT16, UINT32, UINT64]"));
        }
        this.stringOffsetType = stringOffsetType;
    }

    /**
     * The type of values in `stringOffsets`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     * @return The stringOffsetType
     * 
     */
    public String getStringOffsetType() {
        return this.stringOffsetType;
    }

    /**
     * Returns the default value of the stringOffsetType<br> 
     * @see #getStringOffsetType 
     * 
     * @return The default stringOffsetType
     * 
     */
    public String defaultStringOffsetType() {
        return "UINT32";
    }

    /**
     * An offset to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `offset` if both are 
     * defined. (optional) 
     * 
     * @param offset The offset to set
     * 
     */
    public void setOffset(Object offset) {
        if (offset == null) {
            this.offset = offset;
            return ;
        }
        this.offset = offset;
    }

    /**
     * An offset to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `offset` if both are 
     * defined. (optional) 
     * 
     * @return The offset
     * 
     */
    public Object getOffset() {
        return this.offset;
    }

    /**
     * A scale to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `scale` if both are 
     * defined. (optional) 
     * 
     * @param scale The scale to set
     * 
     */
    public void setScale(Object scale) {
        if (scale == null) {
            this.scale = scale;
            return ;
        }
        this.scale = scale;
    }

    /**
     * A scale to apply to property values. Only applicable when the 
     * component type is `FLOAT32` or `FLOAT64`, or when the property is 
     * `normalized`. Overrides the class property's `scale` if both are 
     * defined. (optional) 
     * 
     * @return The scale
     * 
     */
    public Object getScale() {
        return this.scale;
    }

    /**
     * Maximum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the maximum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @param max The max to set
     * 
     */
    public void setMax(Object max) {
        if (max == null) {
            this.max = max;
            return ;
        }
        this.max = max;
    }

    /**
     * Maximum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the maximum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @return The max
     * 
     */
    public Object getMax() {
        return this.max;
    }

    /**
     * Minimum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the minimum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @param min The min to set
     * 
     */
    public void setMin(Object min) {
        if (min == null) {
            this.min = min;
            return ;
        }
        this.min = min;
    }

    /**
     * Minimum value present in the property values. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types. This is the minimum of all 
     * property values, after the transforms based on the `normalized`, 
     * `offset`, and `scale` properties have been applied. (optional) 
     * 
     * @return The min
     * 
     */
    public Object getMin() {
        return this.min;
    }

}
