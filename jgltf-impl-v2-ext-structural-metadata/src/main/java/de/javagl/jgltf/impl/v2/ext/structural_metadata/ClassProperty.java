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
 * A class property. 
 * 
 * Auto-generated for class.property.schema.json 
 * 
 */
public class ClassProperty
    extends GlTFProperty
{

    /**
     * The name of the property, e.g. for display purposes. (optional) 
     * 
     */
    private String name;
    /**
     * The description of the property. (optional) 
     * 
     */
    private String description;
    /**
     * The element type. (required)<br> 
     * Valid values: [SCALAR, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, STRING, 
     * BOOLEAN, ENUM] 
     * 
     */
    private String type;
    /**
     * The datatype of the element's components. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. (optional)<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64, FLOAT32, FLOAT64] 
     * 
     */
    private String componentType;
    /**
     * Enum ID as declared in the `enums` dictionary. Required when `type` is 
     * `ENUM`. (optional) 
     * 
     */
    private String enumType;
    /**
     * Whether the property is an array. When `count` is defined the property 
     * is a fixed-length array. Otherwise the property is a variable-length 
     * array. (optional)<br> 
     * Default: false 
     * 
     */
    private Boolean array;
    /**
     * The number of array elements. May only be defined when `array` is 
     * true. (optional)<br> 
     * Minimum: 2 (inclusive) 
     * 
     */
    private Integer count;
    /**
     * Specifies whether integer values are normalized. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types with integer component types. For 
     * unsigned integer component types, values are normalized between `[0.0, 
     *  1.0]`. For signed integer component types, values are normalized 
     * between `[-1.0, 1.0]`. For all other component types, this property 
     * must be false. (optional)<br> 
     * Default: false 
     * 
     */
    private Boolean normalized;
    /**
     * An offset to apply to property values. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types when the component type is `FLOAT32` or 
     * `FLOAT64`, or when the property is `normalized`. (optional) 
     * 
     */
    private Object offset;
    /**
     * A scale to apply to property values. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types when the component type is `FLOAT32` or 
     * `FLOAT64`, or when the property is `normalized`. (optional) 
     * 
     */
    private Object scale;
    /**
     * Maximum allowed value for the property. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. This is the maximum of all property values, 
     * after the transforms based on the `normalized`, `offset`, and `scale` 
     * properties have been applied. (optional) 
     * 
     */
    private Object max;
    /**
     * Minimum allowed value for the property. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. This is the minimum of all property values, 
     * after the transforms based on the `normalized`, `offset`, and `scale` 
     * properties have been applied. (optional) 
     * 
     */
    private Object min;
    /**
     * If required, the property must be present in every entity conforming 
     * to the class. If not required, individual entities may include 
     * `noData` values, or the entire property may be omitted. As a result, 
     * `noData` has no effect on a required property. Client implementations 
     * may use required properties to make performance optimizations. 
     * (optional)<br> 
     * Default: false 
     * 
     */
    private Boolean required;
    /**
     * A `noData` value represents missing data — also known as a sentinel 
     * value — wherever it appears. `BOOLEAN` properties may not specify 
     * `noData` values. This is given as the plain property value, without 
     * the transforms from the `normalized`, `offset`, and `scale` 
     * properties. Must not be defined if `required` is true. (optional) 
     * 
     */
    private Object noData;
    /**
     * A default value to use when encountering a `noData` value or an 
     * omitted property. The value is given in its final form, taking the 
     * effect of `normalized`, `offset`, and `scale` properties into account. 
     * Must not be defined if `required` is true. (optional) 
     * 
     */
    private Object defaultProperty;
    /**
     * An identifier that describes how this property should be interpreted. 
     * The semantic cannot be used by other properties in the class. 
     * (optional) 
     * 
     */
    private String semantic;

    /**
     * The name of the property, e.g. for display purposes. (optional) 
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
     * The name of the property, e.g. for display purposes. (optional) 
     * 
     * @return The name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * The description of the property. (optional) 
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
     * The description of the property. (optional) 
     * 
     * @return The description
     * 
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * The element type. (required)<br> 
     * Valid values: [SCALAR, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, STRING, 
     * BOOLEAN, ENUM] 
     * 
     * @param type The type to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException((("Invalid value for type: "+ type)+", may not be null"));
        }
        if ((((((((((!"SCALAR".equals(type))&&(!"VEC2".equals(type)))&&(!"VEC3".equals(type)))&&(!"VEC4".equals(type)))&&(!"MAT2".equals(type)))&&(!"MAT3".equals(type)))&&(!"MAT4".equals(type)))&&(!"STRING".equals(type)))&&(!"BOOLEAN".equals(type)))&&(!"ENUM".equals(type))) {
            throw new IllegalArgumentException((("Invalid value for type: "+ type)+", valid: [SCALAR, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, STRING, BOOLEAN, ENUM]"));
        }
        this.type = type;
    }

    /**
     * The element type. (required)<br> 
     * Valid values: [SCALAR, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, STRING, 
     * BOOLEAN, ENUM] 
     * 
     * @return The type
     * 
     */
    public String getType() {
        return this.type;
    }

    /**
     * The datatype of the element's components. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. (optional)<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64, FLOAT32, FLOAT64] 
     * 
     * @param componentType The componentType to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setComponentType(String componentType) {
        if (componentType == null) {
            this.componentType = componentType;
            return ;
        }
        if ((((((((((!"INT8".equals(componentType))&&(!"UINT8".equals(componentType)))&&(!"INT16".equals(componentType)))&&(!"UINT16".equals(componentType)))&&(!"INT32".equals(componentType)))&&(!"UINT32".equals(componentType)))&&(!"INT64".equals(componentType)))&&(!"UINT64".equals(componentType)))&&(!"FLOAT32".equals(componentType)))&&(!"FLOAT64".equals(componentType))) {
            throw new IllegalArgumentException((("Invalid value for componentType: "+ componentType)+", valid: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, UINT64, FLOAT32, FLOAT64]"));
        }
        this.componentType = componentType;
    }

    /**
     * The datatype of the element's components. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. (optional)<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64, FLOAT32, FLOAT64] 
     * 
     * @return The componentType
     * 
     */
    public String getComponentType() {
        return this.componentType;
    }

    /**
     * Enum ID as declared in the `enums` dictionary. Required when `type` is 
     * `ENUM`. (optional) 
     * 
     * @param enumType The enumType to set
     * 
     */
    public void setEnumType(String enumType) {
        if (enumType == null) {
            this.enumType = enumType;
            return ;
        }
        this.enumType = enumType;
    }

    /**
     * Enum ID as declared in the `enums` dictionary. Required when `type` is 
     * `ENUM`. (optional) 
     * 
     * @return The enumType
     * 
     */
    public String getEnumType() {
        return this.enumType;
    }

    /**
     * Whether the property is an array. When `count` is defined the property 
     * is a fixed-length array. Otherwise the property is a variable-length 
     * array. (optional)<br> 
     * Default: false 
     * 
     * @param array The array to set
     * 
     */
    public void setArray(Boolean array) {
        if (array == null) {
            this.array = array;
            return ;
        }
        this.array = array;
    }

    /**
     * Whether the property is an array. When `count` is defined the property 
     * is a fixed-length array. Otherwise the property is a variable-length 
     * array. (optional)<br> 
     * Default: false 
     * 
     * @return The array
     * 
     */
    public Boolean isArray() {
        return this.array;
    }

    /**
     * Returns the default value of the array<br> 
     * @see #isArray 
     * 
     * @return The default array
     * 
     */
    public Boolean defaultArray() {
        return false;
    }

    /**
     * The number of array elements. May only be defined when `array` is 
     * true. (optional)<br> 
     * Minimum: 2 (inclusive) 
     * 
     * @param count The count to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setCount(Integer count) {
        if (count == null) {
            this.count = count;
            return ;
        }
        if (count< 2) {
            throw new IllegalArgumentException("count < 2");
        }
        this.count = count;
    }

    /**
     * The number of array elements. May only be defined when `array` is 
     * true. (optional)<br> 
     * Minimum: 2 (inclusive) 
     * 
     * @return The count
     * 
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * Specifies whether integer values are normalized. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types with integer component types. For 
     * unsigned integer component types, values are normalized between `[0.0, 
     *  1.0]`. For signed integer component types, values are normalized 
     * between `[-1.0, 1.0]`. For all other component types, this property 
     * must be false. (optional)<br> 
     * Default: false 
     * 
     * @param normalized The normalized to set
     * 
     */
    public void setNormalized(Boolean normalized) {
        if (normalized == null) {
            this.normalized = normalized;
            return ;
        }
        this.normalized = normalized;
    }

    /**
     * Specifies whether integer values are normalized. Only applicable to 
     * `SCALAR`, `VECN`, and `MATN` types with integer component types. For 
     * unsigned integer component types, values are normalized between `[0.0, 
     *  1.0]`. For signed integer component types, values are normalized 
     * between `[-1.0, 1.0]`. For all other component types, this property 
     * must be false. (optional)<br> 
     * Default: false 
     * 
     * @return The normalized
     * 
     */
    public Boolean isNormalized() {
        return this.normalized;
    }

    /**
     * Returns the default value of the normalized<br> 
     * @see #isNormalized 
     * 
     * @return The default normalized
     * 
     */
    public Boolean defaultNormalized() {
        return false;
    }

    /**
     * An offset to apply to property values. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types when the component type is `FLOAT32` or 
     * `FLOAT64`, or when the property is `normalized`. (optional) 
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
     * An offset to apply to property values. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types when the component type is `FLOAT32` or 
     * `FLOAT64`, or when the property is `normalized`. (optional) 
     * 
     * @return The offset
     * 
     */
    public Object getOffset() {
        return this.offset;
    }

    /**
     * A scale to apply to property values. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types when the component type is `FLOAT32` or 
     * `FLOAT64`, or when the property is `normalized`. (optional) 
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
     * A scale to apply to property values. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types when the component type is `FLOAT32` or 
     * `FLOAT64`, or when the property is `normalized`. (optional) 
     * 
     * @return The scale
     * 
     */
    public Object getScale() {
        return this.scale;
    }

    /**
     * Maximum allowed value for the property. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. This is the maximum of all property values, 
     * after the transforms based on the `normalized`, `offset`, and `scale` 
     * properties have been applied. (optional) 
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
     * Maximum allowed value for the property. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. This is the maximum of all property values, 
     * after the transforms based on the `normalized`, `offset`, and `scale` 
     * properties have been applied. (optional) 
     * 
     * @return The max
     * 
     */
    public Object getMax() {
        return this.max;
    }

    /**
     * Minimum allowed value for the property. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. This is the minimum of all property values, 
     * after the transforms based on the `normalized`, `offset`, and `scale` 
     * properties have been applied. (optional) 
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
     * Minimum allowed value for the property. Only applicable to `SCALAR`, 
     * `VECN`, and `MATN` types. This is the minimum of all property values, 
     * after the transforms based on the `normalized`, `offset`, and `scale` 
     * properties have been applied. (optional) 
     * 
     * @return The min
     * 
     */
    public Object getMin() {
        return this.min;
    }

    /**
     * If required, the property must be present in every entity conforming 
     * to the class. If not required, individual entities may include 
     * `noData` values, or the entire property may be omitted. As a result, 
     * `noData` has no effect on a required property. Client implementations 
     * may use required properties to make performance optimizations. 
     * (optional)<br> 
     * Default: false 
     * 
     * @param required The required to set
     * 
     */
    public void setRequired(Boolean required) {
        if (required == null) {
            this.required = required;
            return ;
        }
        this.required = required;
    }

    /**
     * If required, the property must be present in every entity conforming 
     * to the class. If not required, individual entities may include 
     * `noData` values, or the entire property may be omitted. As a result, 
     * `noData` has no effect on a required property. Client implementations 
     * may use required properties to make performance optimizations. 
     * (optional)<br> 
     * Default: false 
     * 
     * @return The required
     * 
     */
    public Boolean isRequired() {
        return this.required;
    }

    /**
     * Returns the default value of the required<br> 
     * @see #isRequired 
     * 
     * @return The default required
     * 
     */
    public Boolean defaultRequired() {
        return false;
    }

    /**
     * A `noData` value represents missing data — also known as a sentinel 
     * value — wherever it appears. `BOOLEAN` properties may not specify 
     * `noData` values. This is given as the plain property value, without 
     * the transforms from the `normalized`, `offset`, and `scale` 
     * properties. Must not be defined if `required` is true. (optional) 
     * 
     * @param noData The noData to set
     * 
     */
    public void setNoData(Object noData) {
        if (noData == null) {
            this.noData = noData;
            return ;
        }
        this.noData = noData;
    }

    /**
     * A `noData` value represents missing data — also known as a sentinel 
     * value — wherever it appears. `BOOLEAN` properties may not specify 
     * `noData` values. This is given as the plain property value, without 
     * the transforms from the `normalized`, `offset`, and `scale` 
     * properties. Must not be defined if `required` is true. (optional) 
     * 
     * @return The noData
     * 
     */
    public Object getNoData() {
        return this.noData;
    }

    /**
     * A default value to use when encountering a `noData` value or an 
     * omitted property. The value is given in its final form, taking the 
     * effect of `normalized`, `offset`, and `scale` properties into account. 
     * Must not be defined if `required` is true. (optional) 
     * 
     * @param defaultProperty The defaultProperty to set
     * 
     */
    public void setDefaultProperty(Object defaultProperty) {
        if (defaultProperty == null) {
            this.defaultProperty = defaultProperty;
            return ;
        }
        this.defaultProperty = defaultProperty;
    }

    /**
     * A default value to use when encountering a `noData` value or an 
     * omitted property. The value is given in its final form, taking the 
     * effect of `normalized`, `offset`, and `scale` properties into account. 
     * Must not be defined if `required` is true. (optional) 
     * 
     * @return The defaultProperty
     * 
     */
    public Object getDefaultProperty() {
        return this.defaultProperty;
    }

    /**
     * An identifier that describes how this property should be interpreted. 
     * The semantic cannot be used by other properties in the class. 
     * (optional) 
     * 
     * @param semantic The semantic to set
     * 
     */
    public void setSemantic(String semantic) {
        if (semantic == null) {
            this.semantic = semantic;
            return ;
        }
        this.semantic = semantic;
    }

    /**
     * An identifier that describes how this property should be interpreted. 
     * The semantic cannot be used by other properties in the class. 
     * (optional) 
     * 
     * @return The semantic
     * 
     */
    public String getSemantic() {
        return this.semantic;
    }

}
