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
     * Element type represented by each property value. `VECN` is a vector of 
     * `N` numeric components. `MATN` is an `N ⨉ N` matrix of numeric 
     * components stored in column-major order. `ARRAY` is fixed-length when 
     * `componentCount` is defined, and is variable-length otherwise. 
     * (optional)<br> 
     * Default: "SINGLE"<br> 
     * Valid values: [SINGLE, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, ARRAY] 
     * 
     */
    private String type;
    /**
     * Enum ID as declared in the `enums` dictionary. Required when 
     * `componentType` is `ENUM`. (optional) 
     * 
     */
    private String enumType;
    /**
     * Data type of an element's components. When `type` is `SINGLE`, then 
     * `componentType` is also the data type of the element. When 
     * `componentType` is `ENUM`, `enumType` is required. (required)<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64, FLOAT32, FLOAT64, BOOLEAN, STRING, ENUM] 
     * 
     */
    private String componentType;
    /**
     * Number of components per element for fixed-length `ARRAY` elements. 
     * Always undefined for variable-length `ARRAY` and all other element 
     * types. (optional)<br> 
     * Minimum: 2 (inclusive) 
     * 
     */
    private Integer componentCount;
    /**
     * Specifies whether integer values are normalized. This applies when 
     * `componentType` is an integer type. For unsigned integer component 
     * types, values are normalized between `[0.0, 1.0]`. For signed integer 
     * component types, values are normalized between `[-1.0, 1.0]`. For all 
     * other component types, this property must be false. (optional)<br> 
     * Default: false 
     * 
     */
    private Boolean normalized;
    /**
     * Maximum allowed value for the property. Only applicable for 
     * single-value numeric types, fixed-length arrays of numeric types, 
     * `VECN`, and `MATN` types. For single-value numeric types this is a 
     * single number. For fixed-length arrays, `VECN`, and `MATN` types, this 
     * is an array of component-wise maximum values. The `normalized` 
     * property has no effect on the maximum, which always contains integer 
     * values. (optional) 
     * 
     */
    private Object max;
    /**
     * Minimum allowed value for the property. Only applicable for 
     * single-value numeric types, fixed-length arrays of numeric types, 
     * `VECN`, and `MATN` types. For single-value numeric types this is a 
     * single number. For fixed-length arrays, `VECN`, and `MATN` types, this 
     * is an array of component-wise minimum values. The `normalized` 
     * property has no effect on the minimum, which always contains integer 
     * values. (optional) 
     * 
     */
    private Object min;
    /**
     * If required, the property must be present for every feature of its 
     * class. If not required, individual features may include `noData` 
     * values, or the entire property may be omitted from a property table or 
     * texture. As a result, `noData` has no effect on a required property. 
     * Client implementations may use required properties to make performance 
     * optimizations. (optional)<br> 
     * Default: false 
     * 
     */
    private Boolean required;
    /**
     * A `noData` value represents missing data — also known as a sentinel 
     * value — wherever it appears. If omitted (excluding variable-length 
     * `ARRAY` properties), property values exist for all features, and the 
     * property is required in property tables or textures instantiating the 
     * class. For variable-length `ARRAY` elements, `noData` is implicitly 
     * `[]` and the property is never required; an additional `noData` array, 
     * such as `["UNSPECIFIED"]`, may be provided if necessary. For 
     * fixed-length `ARRAY` properties, `noData` must be an array of length 
     * `componentCount`. For `VECN` properties, `noData` must be an array of 
     * length `N`. For `MATN` propperties, `noData` must be an array of 
     * length `N²`. `BOOLEAN` properties may not specify `noData` values. 
     * `ENUM` `noData` values must use a valid enum `name`, not an integer 
     * value. (optional) 
     * 
     */
    private Object noData;
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
     * Element type represented by each property value. `VECN` is a vector of 
     * `N` numeric components. `MATN` is an `N ⨉ N` matrix of numeric 
     * components stored in column-major order. `ARRAY` is fixed-length when 
     * `componentCount` is defined, and is variable-length otherwise. 
     * (optional)<br> 
     * Default: "SINGLE"<br> 
     * Valid values: [SINGLE, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, ARRAY] 
     * 
     * @param type The type to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setType(String type) {
        if (type == null) {
            this.type = type;
            return ;
        }
        if ((((((((!"SINGLE".equals(type))&&(!"VEC2".equals(type)))&&(!"VEC3".equals(type)))&&(!"VEC4".equals(type)))&&(!"MAT2".equals(type)))&&(!"MAT3".equals(type)))&&(!"MAT4".equals(type)))&&(!"ARRAY".equals(type))) {
            throw new IllegalArgumentException((("Invalid value for type: "+ type)+", valid: [SINGLE, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, ARRAY]"));
        }
        this.type = type;
    }

    /**
     * Element type represented by each property value. `VECN` is a vector of 
     * `N` numeric components. `MATN` is an `N ⨉ N` matrix of numeric 
     * components stored in column-major order. `ARRAY` is fixed-length when 
     * `componentCount` is defined, and is variable-length otherwise. 
     * (optional)<br> 
     * Default: "SINGLE"<br> 
     * Valid values: [SINGLE, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4, ARRAY] 
     * 
     * @return The type
     * 
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the default value of the type<br> 
     * @see #getType 
     * 
     * @return The default type
     * 
     */
    public String defaultType() {
        return "SINGLE";
    }

    /**
     * Enum ID as declared in the `enums` dictionary. Required when 
     * `componentType` is `ENUM`. (optional) 
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
     * Enum ID as declared in the `enums` dictionary. Required when 
     * `componentType` is `ENUM`. (optional) 
     * 
     * @return The enumType
     * 
     */
    public String getEnumType() {
        return this.enumType;
    }

    /**
     * Data type of an element's components. When `type` is `SINGLE`, then 
     * `componentType` is also the data type of the element. When 
     * `componentType` is `ENUM`, `enumType` is required. (required)<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64, FLOAT32, FLOAT64, BOOLEAN, STRING, ENUM] 
     * 
     * @param componentType The componentType to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setComponentType(String componentType) {
        if (componentType == null) {
            throw new NullPointerException((("Invalid value for componentType: "+ componentType)+", may not be null"));
        }
        if (((((((((((((!"INT8".equals(componentType))&&(!"UINT8".equals(componentType)))&&(!"INT16".equals(componentType)))&&(!"UINT16".equals(componentType)))&&(!"INT32".equals(componentType)))&&(!"UINT32".equals(componentType)))&&(!"INT64".equals(componentType)))&&(!"UINT64".equals(componentType)))&&(!"FLOAT32".equals(componentType)))&&(!"FLOAT64".equals(componentType)))&&(!"BOOLEAN".equals(componentType)))&&(!"STRING".equals(componentType)))&&(!"ENUM".equals(componentType))) {
            throw new IllegalArgumentException((("Invalid value for componentType: "+ componentType)+", valid: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, UINT64, FLOAT32, FLOAT64, BOOLEAN, STRING, ENUM]"));
        }
        this.componentType = componentType;
    }

    /**
     * Data type of an element's components. When `type` is `SINGLE`, then 
     * `componentType` is also the data type of the element. When 
     * `componentType` is `ENUM`, `enumType` is required. (required)<br> 
     * Valid values: [INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, 
     * UINT64, FLOAT32, FLOAT64, BOOLEAN, STRING, ENUM] 
     * 
     * @return The componentType
     * 
     */
    public String getComponentType() {
        return this.componentType;
    }

    /**
     * Number of components per element for fixed-length `ARRAY` elements. 
     * Always undefined for variable-length `ARRAY` and all other element 
     * types. (optional)<br> 
     * Minimum: 2 (inclusive) 
     * 
     * @param componentCount The componentCount to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setComponentCount(Integer componentCount) {
        if (componentCount == null) {
            this.componentCount = componentCount;
            return ;
        }
        if (componentCount< 2) {
            throw new IllegalArgumentException("componentCount < 2");
        }
        this.componentCount = componentCount;
    }

    /**
     * Number of components per element for fixed-length `ARRAY` elements. 
     * Always undefined for variable-length `ARRAY` and all other element 
     * types. (optional)<br> 
     * Minimum: 2 (inclusive) 
     * 
     * @return The componentCount
     * 
     */
    public Integer getComponentCount() {
        return this.componentCount;
    }

    /**
     * Specifies whether integer values are normalized. This applies when 
     * `componentType` is an integer type. For unsigned integer component 
     * types, values are normalized between `[0.0, 1.0]`. For signed integer 
     * component types, values are normalized between `[-1.0, 1.0]`. For all 
     * other component types, this property must be false. (optional)<br> 
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
     * Specifies whether integer values are normalized. This applies when 
     * `componentType` is an integer type. For unsigned integer component 
     * types, values are normalized between `[0.0, 1.0]`. For signed integer 
     * component types, values are normalized between `[-1.0, 1.0]`. For all 
     * other component types, this property must be false. (optional)<br> 
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
     * Maximum allowed value for the property. Only applicable for 
     * single-value numeric types, fixed-length arrays of numeric types, 
     * `VECN`, and `MATN` types. For single-value numeric types this is a 
     * single number. For fixed-length arrays, `VECN`, and `MATN` types, this 
     * is an array of component-wise maximum values. The `normalized` 
     * property has no effect on the maximum, which always contains integer 
     * values. (optional) 
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
     * Maximum allowed value for the property. Only applicable for 
     * single-value numeric types, fixed-length arrays of numeric types, 
     * `VECN`, and `MATN` types. For single-value numeric types this is a 
     * single number. For fixed-length arrays, `VECN`, and `MATN` types, this 
     * is an array of component-wise maximum values. The `normalized` 
     * property has no effect on the maximum, which always contains integer 
     * values. (optional) 
     * 
     * @return The max
     * 
     */
    public Object getMax() {
        return this.max;
    }

    /**
     * Minimum allowed value for the property. Only applicable for 
     * single-value numeric types, fixed-length arrays of numeric types, 
     * `VECN`, and `MATN` types. For single-value numeric types this is a 
     * single number. For fixed-length arrays, `VECN`, and `MATN` types, this 
     * is an array of component-wise minimum values. The `normalized` 
     * property has no effect on the minimum, which always contains integer 
     * values. (optional) 
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
     * Minimum allowed value for the property. Only applicable for 
     * single-value numeric types, fixed-length arrays of numeric types, 
     * `VECN`, and `MATN` types. For single-value numeric types this is a 
     * single number. For fixed-length arrays, `VECN`, and `MATN` types, this 
     * is an array of component-wise minimum values. The `normalized` 
     * property has no effect on the minimum, which always contains integer 
     * values. (optional) 
     * 
     * @return The min
     * 
     */
    public Object getMin() {
        return this.min;
    }

    /**
     * If required, the property must be present for every feature of its 
     * class. If not required, individual features may include `noData` 
     * values, or the entire property may be omitted from a property table or 
     * texture. As a result, `noData` has no effect on a required property. 
     * Client implementations may use required properties to make performance 
     * optimizations. (optional)<br> 
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
     * If required, the property must be present for every feature of its 
     * class. If not required, individual features may include `noData` 
     * values, or the entire property may be omitted from a property table or 
     * texture. As a result, `noData` has no effect on a required property. 
     * Client implementations may use required properties to make performance 
     * optimizations. (optional)<br> 
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
     * A `noData` value represents missing data — also known as a sentinel 
     * value — wherever it appears. If omitted (excluding variable-length 
     * `ARRAY` properties), property values exist for all features, and the 
     * property is required in property tables or textures instantiating the 
     * class. For variable-length `ARRAY` elements, `noData` is implicitly 
     * `[]` and the property is never required; an additional `noData` array, 
     * such as `["UNSPECIFIED"]`, may be provided if necessary. For 
     * fixed-length `ARRAY` properties, `noData` must be an array of length 
     * `componentCount`. For `VECN` properties, `noData` must be an array of 
     * length `N`. For `MATN` propperties, `noData` must be an array of 
     * length `N²`. `BOOLEAN` properties may not specify `noData` values. 
     * `ENUM` `noData` values must use a valid enum `name`, not an integer 
     * value. (optional) 
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
     * A `noData` value represents missing data — also known as a sentinel 
     * value — wherever it appears. If omitted (excluding variable-length 
     * `ARRAY` properties), property values exist for all features, and the 
     * property is required in property tables or textures instantiating the 
     * class. For variable-length `ARRAY` elements, `noData` is implicitly 
     * `[]` and the property is never required; an additional `noData` array, 
     * such as `["UNSPECIFIED"]`, may be provided if necessary. For 
     * fixed-length `ARRAY` properties, `noData` must be an array of length 
     * `componentCount`. For `VECN` properties, `noData` must be an array of 
     * length `N`. For `MATN` propperties, `noData` must be an array of 
     * length `N²`. `BOOLEAN` properties may not specify `noData` values. 
     * `ENUM` `noData` values must use a valid enum `name`, not an integer 
     * value. (optional) 
     * 
     * @return The noData
     * 
     */
    public Object getNoData() {
        return this.noData;
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
