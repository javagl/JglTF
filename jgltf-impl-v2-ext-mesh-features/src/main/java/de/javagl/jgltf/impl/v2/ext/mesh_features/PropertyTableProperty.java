/*
 * 3D Tiles EXT_mesh_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.mesh_features;

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
     * `componentType` is `BOOLEAN` values are packed into a bitstream. When 
     * `componentType` is `STRING` values are stored as byte sequences and 
     * decoded as UTF-8 strings. When `componentType` is a numeric type 
     * values are stored as the provided `componentType`. When 
     * `componentType` is `ENUM` values are stored as the enum's `valueType`. 
     * Each enum value in the buffer must match one of the allowed values in 
     * the enum definition. When `type` is `ARRAY` elements are packed 
     * tightly together and the data type is based on the `componentType` 
     * following the same rules as above. `arrayOffsetBufferView` is required 
     * for variable-size arrays and `stringOffsetBufferView` is required for 
     * strings (for variable-length arrays of strings, both are required). 
     * The buffer view `byteOffset` must be aligned to a multiple of the 
     * `componentType` size. (required) 
     * 
     */
    private Integer bufferView;
    /**
     * The type of values in `arrayOffsetBufferView`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     */
    private String arrayOffsetType;
    /**
     * The index of the buffer view containing offsets for variable-length 
     * arrays. The number of offsets is equal to the property table `count` 
     * plus one. The offsets represent the start positions of each array, 
     * with the last offset representing the position after the last array. 
     * The array length is computed using the difference between the current 
     * offset and the subsequent offset. If `componentType` is `STRING` the 
     * offsets index into the string offsets array (stored in 
     * `stringOffsetBufferView`), otherwise they index into the property 
     * array (stored in `bufferView`). The data type of these offsets is 
     * determined by `arrayOffsetType`. The buffer view `byteOffset` must be 
     * aligned to a multiple of the `arrayOffsetType` size. (optional) 
     * 
     */
    private Integer arrayOffsetBufferView;
    /**
     * The type of values in `stringOffsetBufferView`. (optional)<br> 
     * Default: "UINT32"<br> 
     * Valid values: [UINT8, UINT16, UINT32, UINT64] 
     * 
     */
    private String stringOffsetType;
    /**
     * The index of the buffer view containing offsets for strings. The 
     * number of offsets is equal to the number of string components plus 
     * one. The offsets represent the byte offsets of each string in the main 
     * `bufferView`, with the last offset representing the byte offset after 
     * the last string. The string byte length is computed using the 
     * difference between the current offset and the subsequent offset. The 
     * data type of these offsets is determined by `stringOffsetType`. The 
     * buffer view `byteOffset` must be aligned to a multiple of the 
     * `stringOffsetType` size. (optional) 
     * 
     */
    private Integer stringOffsetBufferView;

    /**
     * The index of the buffer view containing property values. The data type 
     * of property values is determined by the property definition: When 
     * `componentType` is `BOOLEAN` values are packed into a bitstream. When 
     * `componentType` is `STRING` values are stored as byte sequences and 
     * decoded as UTF-8 strings. When `componentType` is a numeric type 
     * values are stored as the provided `componentType`. When 
     * `componentType` is `ENUM` values are stored as the enum's `valueType`. 
     * Each enum value in the buffer must match one of the allowed values in 
     * the enum definition. When `type` is `ARRAY` elements are packed 
     * tightly together and the data type is based on the `componentType` 
     * following the same rules as above. `arrayOffsetBufferView` is required 
     * for variable-size arrays and `stringOffsetBufferView` is required for 
     * strings (for variable-length arrays of strings, both are required). 
     * The buffer view `byteOffset` must be aligned to a multiple of the 
     * `componentType` size. (required) 
     * 
     * @param bufferView The bufferView to set
     * @throws NullPointerException If the given value is <code>null</code>
     * 
     */
    public void setBufferView(Integer bufferView) {
        if (bufferView == null) {
            throw new NullPointerException((("Invalid value for bufferView: "+ bufferView)+", may not be null"));
        }
        this.bufferView = bufferView;
    }

    /**
     * The index of the buffer view containing property values. The data type 
     * of property values is determined by the property definition: When 
     * `componentType` is `BOOLEAN` values are packed into a bitstream. When 
     * `componentType` is `STRING` values are stored as byte sequences and 
     * decoded as UTF-8 strings. When `componentType` is a numeric type 
     * values are stored as the provided `componentType`. When 
     * `componentType` is `ENUM` values are stored as the enum's `valueType`. 
     * Each enum value in the buffer must match one of the allowed values in 
     * the enum definition. When `type` is `ARRAY` elements are packed 
     * tightly together and the data type is based on the `componentType` 
     * following the same rules as above. `arrayOffsetBufferView` is required 
     * for variable-size arrays and `stringOffsetBufferView` is required for 
     * strings (for variable-length arrays of strings, both are required). 
     * The buffer view `byteOffset` must be aligned to a multiple of the 
     * `componentType` size. (required) 
     * 
     * @return The bufferView
     * 
     */
    public Integer getBufferView() {
        return this.bufferView;
    }

    /**
     * The type of values in `arrayOffsetBufferView`. (optional)<br> 
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
     * The type of values in `arrayOffsetBufferView`. (optional)<br> 
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
     * The index of the buffer view containing offsets for variable-length 
     * arrays. The number of offsets is equal to the property table `count` 
     * plus one. The offsets represent the start positions of each array, 
     * with the last offset representing the position after the last array. 
     * The array length is computed using the difference between the current 
     * offset and the subsequent offset. If `componentType` is `STRING` the 
     * offsets index into the string offsets array (stored in 
     * `stringOffsetBufferView`), otherwise they index into the property 
     * array (stored in `bufferView`). The data type of these offsets is 
     * determined by `arrayOffsetType`. The buffer view `byteOffset` must be 
     * aligned to a multiple of the `arrayOffsetType` size. (optional) 
     * 
     * @param arrayOffsetBufferView The arrayOffsetBufferView to set
     * 
     */
    public void setArrayOffsetBufferView(Integer arrayOffsetBufferView) {
        if (arrayOffsetBufferView == null) {
            this.arrayOffsetBufferView = arrayOffsetBufferView;
            return ;
        }
        this.arrayOffsetBufferView = arrayOffsetBufferView;
    }

    /**
     * The index of the buffer view containing offsets for variable-length 
     * arrays. The number of offsets is equal to the property table `count` 
     * plus one. The offsets represent the start positions of each array, 
     * with the last offset representing the position after the last array. 
     * The array length is computed using the difference between the current 
     * offset and the subsequent offset. If `componentType` is `STRING` the 
     * offsets index into the string offsets array (stored in 
     * `stringOffsetBufferView`), otherwise they index into the property 
     * array (stored in `bufferView`). The data type of these offsets is 
     * determined by `arrayOffsetType`. The buffer view `byteOffset` must be 
     * aligned to a multiple of the `arrayOffsetType` size. (optional) 
     * 
     * @return The arrayOffsetBufferView
     * 
     */
    public Integer getArrayOffsetBufferView() {
        return this.arrayOffsetBufferView;
    }

    /**
     * The type of values in `stringOffsetBufferView`. (optional)<br> 
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
     * The type of values in `stringOffsetBufferView`. (optional)<br> 
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
     * The index of the buffer view containing offsets for strings. The 
     * number of offsets is equal to the number of string components plus 
     * one. The offsets represent the byte offsets of each string in the main 
     * `bufferView`, with the last offset representing the byte offset after 
     * the last string. The string byte length is computed using the 
     * difference between the current offset and the subsequent offset. The 
     * data type of these offsets is determined by `stringOffsetType`. The 
     * buffer view `byteOffset` must be aligned to a multiple of the 
     * `stringOffsetType` size. (optional) 
     * 
     * @param stringOffsetBufferView The stringOffsetBufferView to set
     * 
     */
    public void setStringOffsetBufferView(Integer stringOffsetBufferView) {
        if (stringOffsetBufferView == null) {
            this.stringOffsetBufferView = stringOffsetBufferView;
            return ;
        }
        this.stringOffsetBufferView = stringOffsetBufferView;
    }

    /**
     * The index of the buffer view containing offsets for strings. The 
     * number of offsets is equal to the number of string components plus 
     * one. The offsets represent the byte offsets of each string in the main 
     * `bufferView`, with the last offset representing the byte offset after 
     * the last string. The string byte length is computed using the 
     * difference between the current offset and the subsequent offset. The 
     * data type of these offsets is determined by `stringOffsetType`. The 
     * buffer view `byteOffset` must be aligned to a multiple of the 
     * `stringOffsetType` size. (optional) 
     * 
     * @return The stringOffsetBufferView
     * 
     */
    public Integer getStringOffsetBufferView() {
        return this.stringOffsetBufferView;
    }

}
