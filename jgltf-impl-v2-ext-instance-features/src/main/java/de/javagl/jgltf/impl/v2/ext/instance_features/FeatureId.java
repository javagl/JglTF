/*
 * EXT_instance_features JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016-2021 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2.ext.instance_features;

import de.javagl.jgltf.impl.v2.GlTFProperty;


/**
 * Feature IDs stored in a GPU mesh instancing attribute 
 * 
 * Auto-generated for featureId.schema.json 
 * 
 */
public class FeatureId
    extends GlTFProperty
{

    /**
     * The number of unique features in the attribute. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     */
    private Integer featureCount;
    /**
     * A value that indicates that no feature is associated with this 
     * instance. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer nullFeatureId;
    /**
     * A label assigned to this feature ID set. Labels must be alphanumeric 
     * identifiers matching the regular expression 
     * `^[a-zA-Z_][a-zA-Z0-9_]*$`. (optional) 
     * 
     */
    private String label;
    /**
     * An attribute containing feature IDs. When this is omitted, then the 
     * feature IDs are assigned to the GPU instances by their index. 
     * (optional) 
     * 
     */
    private Integer attribute;
    /**
     * The index of the property table containing per-feature property 
     * values. Only applicable when using the `EXT_structural_metadata` 
     * extension. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer propertyTable;

    /**
     * The number of unique features in the attribute. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @param featureCount The featureCount to set
     * @throws NullPointerException If the given value is <code>null</code>
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setFeatureCount(Integer featureCount) {
        if (featureCount == null) {
            throw new NullPointerException((("Invalid value for featureCount: "+ featureCount)+", may not be null"));
        }
        if (featureCount< 1) {
            throw new IllegalArgumentException("featureCount < 1");
        }
        this.featureCount = featureCount;
    }

    /**
     * The number of unique features in the attribute. (required)<br> 
     * Minimum: 1 (inclusive) 
     * 
     * @return The featureCount
     * 
     */
    public Integer getFeatureCount() {
        return this.featureCount;
    }

    /**
     * A value that indicates that no feature is associated with this 
     * instance. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param nullFeatureId The nullFeatureId to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setNullFeatureId(Integer nullFeatureId) {
        if (nullFeatureId == null) {
            this.nullFeatureId = nullFeatureId;
            return ;
        }
        if (nullFeatureId< 0) {
            throw new IllegalArgumentException("nullFeatureId < 0");
        }
        this.nullFeatureId = nullFeatureId;
    }

    /**
     * A value that indicates that no feature is associated with this 
     * instance. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The nullFeatureId
     * 
     */
    public Integer getNullFeatureId() {
        return this.nullFeatureId;
    }

    /**
     * A label assigned to this feature ID set. Labels must be alphanumeric 
     * identifiers matching the regular expression 
     * `^[a-zA-Z_][a-zA-Z0-9_]*$`. (optional) 
     * 
     * @param label The label to set
     * 
     */
    public void setLabel(String label) {
        if (label == null) {
            this.label = label;
            return ;
        }
        this.label = label;
    }

    /**
     * A label assigned to this feature ID set. Labels must be alphanumeric 
     * identifiers matching the regular expression 
     * `^[a-zA-Z_][a-zA-Z0-9_]*$`. (optional) 
     * 
     * @return The label
     * 
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * An attribute containing feature IDs. When this is omitted, then the 
     * feature IDs are assigned to the GPU instances by their index. 
     * (optional) 
     * 
     * @param attribute The attribute to set
     * 
     */
    public void setAttribute(Integer attribute) {
        if (attribute == null) {
            this.attribute = attribute;
            return ;
        }
        this.attribute = attribute;
    }

    /**
     * An attribute containing feature IDs. When this is omitted, then the 
     * feature IDs are assigned to the GPU instances by their index. 
     * (optional) 
     * 
     * @return The attribute
     * 
     */
    public Integer getAttribute() {
        return this.attribute;
    }

    /**
     * The index of the property table containing per-feature property 
     * values. Only applicable when using the `EXT_structural_metadata` 
     * extension. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param propertyTable The propertyTable to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setPropertyTable(Integer propertyTable) {
        if (propertyTable == null) {
            this.propertyTable = propertyTable;
            return ;
        }
        if (propertyTable< 0) {
            throw new IllegalArgumentException("propertyTable < 0");
        }
        this.propertyTable = propertyTable;
    }

    /**
     * The index of the property table containing per-feature property 
     * values. Only applicable when using the `EXT_structural_metadata` 
     * extension. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The propertyTable
     * 
     */
    public Integer getPropertyTable() {
        return this.propertyTable;
    }

}
