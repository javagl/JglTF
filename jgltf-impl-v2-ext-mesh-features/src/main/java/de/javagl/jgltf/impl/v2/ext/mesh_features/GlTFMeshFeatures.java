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
 * glTF extension that assigns properties to features in a model. 
 * 
 * Auto-generated for glTF.EXT_mesh_features.schema.json 
 * 
 */
public class GlTFMeshFeatures
    extends GlTFProperty
{

    /**
     * An object defining classes and enums. (optional) 
     * 
     */
    private Schema schema;
    /**
     * The URI (or IRI) of the external schema file. (optional) 
     * 
     */
    private String schemaUri;
    /**
     * An array of property table definitions, which may be referenced by 
     * index. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Features conforming to a class, organized as property 
     * values stored in columnar arrays. (optional) 
     * 
     */
    private List<PropertyTable> propertyTables;
    /**
     * An array of property texture definitions, which may be referenced by 
     * index. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Features conforming to a class, organized as property 
     * values stored in texture channels. (optional) 
     * 
     */
    private List<PropertyTexture> propertyTextures;

    /**
     * An object defining classes and enums. (optional) 
     * 
     * @param schema The schema to set
     * 
     */
    public void setSchema(Schema schema) {
        if (schema == null) {
            this.schema = schema;
            return ;
        }
        this.schema = schema;
    }

    /**
     * An object defining classes and enums. (optional) 
     * 
     * @return The schema
     * 
     */
    public Schema getSchema() {
        return this.schema;
    }

    /**
     * The URI (or IRI) of the external schema file. (optional) 
     * 
     * @param schemaUri The schemaUri to set
     * 
     */
    public void setSchemaUri(String schemaUri) {
        if (schemaUri == null) {
            this.schemaUri = schemaUri;
            return ;
        }
        this.schemaUri = schemaUri;
    }

    /**
     * The URI (or IRI) of the external schema file. (optional) 
     * 
     * @return The schemaUri
     * 
     */
    public String getSchemaUri() {
        return this.schemaUri;
    }

    /**
     * An array of property table definitions, which may be referenced by 
     * index. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Features conforming to a class, organized as property 
     * values stored in columnar arrays. (optional) 
     * 
     * @param propertyTables The propertyTables to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setPropertyTables(List<PropertyTable> propertyTables) {
        if (propertyTables == null) {
            this.propertyTables = propertyTables;
            return ;
        }
        if (propertyTables.size()< 1) {
            throw new IllegalArgumentException("Number of propertyTables elements is < 1");
        }
        this.propertyTables = propertyTables;
    }

    /**
     * An array of property table definitions, which may be referenced by 
     * index. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Features conforming to a class, organized as property 
     * values stored in columnar arrays. (optional) 
     * 
     * @return The propertyTables
     * 
     */
    public List<PropertyTable> getPropertyTables() {
        return this.propertyTables;
    }

    /**
     * Add the given propertyTables. The propertyTables of this instance will 
     * be replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addPropertyTables(PropertyTable element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<PropertyTable> oldList = this.propertyTables;
        List<PropertyTable> newList = new ArrayList<PropertyTable>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.propertyTables = newList;
    }

    /**
     * Remove the given propertyTables. The propertyTables of this instance 
     * will be replaced with a list that contains all previous elements, 
     * except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removePropertyTables(PropertyTable element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<PropertyTable> oldList = this.propertyTables;
        List<PropertyTable> newList = new ArrayList<PropertyTable>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.propertyTables = null;
        } else {
            this.propertyTables = newList;
        }
    }

    /**
     * An array of property texture definitions, which may be referenced by 
     * index. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Features conforming to a class, organized as property 
     * values stored in texture channels. (optional) 
     * 
     * @param propertyTextures The propertyTextures to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setPropertyTextures(List<PropertyTexture> propertyTextures) {
        if (propertyTextures == null) {
            this.propertyTextures = propertyTextures;
            return ;
        }
        if (propertyTextures.size()< 1) {
            throw new IllegalArgumentException("Number of propertyTextures elements is < 1");
        }
        this.propertyTextures = propertyTextures;
    }

    /**
     * An array of property texture definitions, which may be referenced by 
     * index. (optional)<br> 
     * Minimum number of items: 1<br> 
     * Array elements:<br> 
     * &nbsp;&nbsp;Features conforming to a class, organized as property 
     * values stored in texture channels. (optional) 
     * 
     * @return The propertyTextures
     * 
     */
    public List<PropertyTexture> getPropertyTextures() {
        return this.propertyTextures;
    }

    /**
     * Add the given propertyTextures. The propertyTextures of this instance 
     * will be replaced with a list that contains all previous elements, and 
     * additionally the new element. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void addPropertyTextures(PropertyTexture element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<PropertyTexture> oldList = this.propertyTextures;
        List<PropertyTexture> newList = new ArrayList<PropertyTexture>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.propertyTextures = newList;
    }

    /**
     * Remove the given propertyTextures. The propertyTextures of this 
     * instance will be replaced with a list that contains all previous 
     * elements, except for the removed one.<br> 
     * If this new list would be empty, then it will be set to 
     * <code>null</code>. 
     * 
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     * 
     */
    public void removePropertyTextures(PropertyTexture element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<PropertyTexture> oldList = this.propertyTextures;
        List<PropertyTexture> newList = new ArrayList<PropertyTexture>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.propertyTextures = null;
        } else {
            this.propertyTextures = newList;
        }
    }

}
