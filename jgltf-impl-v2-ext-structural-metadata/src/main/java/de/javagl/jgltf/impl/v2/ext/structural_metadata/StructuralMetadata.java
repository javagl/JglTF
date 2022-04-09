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
 * Structural metadata about a glTF element. 
 * 
 * Auto-generated for EXT_structural_metadata.schema.json 
 * 
 */
public class StructuralMetadata
    extends GlTFProperty
{

    /**
     * The index of the property table containing per-feature property 
     * values. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer propertyTable;
    /**
     * The feature index (row index) used for looking up property values for 
     * this element. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     */
    private Integer index;

    /**
     * The index of the property table containing per-feature property 
     * values. (optional)<br> 
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
     * values. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The propertyTable
     * 
     */
    public Integer getPropertyTable() {
        return this.propertyTable;
    }

    /**
     * The feature index (row index) used for looking up property values for 
     * this element. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @param index The index to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setIndex(Integer index) {
        if (index == null) {
            this.index = index;
            return ;
        }
        if (index< 0) {
            throw new IllegalArgumentException("index < 0");
        }
        this.index = index;
    }

    /**
     * The feature index (row index) used for looking up property values for 
     * this element. (optional)<br> 
     * Minimum: 0 (inclusive) 
     * 
     * @return The index
     * 
     */
    public Integer getIndex() {
        return this.index;
    }

}
