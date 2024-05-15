/*
 * www.javagl.de - JglTF
 *
 * Copyright 2024 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io;

/**
 * Package-private enumeration of glTF "flavors" for the tests: 
 * Default, binary, and embedded
 */
enum GltfFlavor
{
    /**
     * Default structure: JSON with external references
     */
    DEFAULT("glTF"),
    
    /**
     * Binary glTF structure
     */
    BINARY("glTF-Binary"),
    
    /**
     * Embedded glTF structure
     */
    EMBEDDED("glTF-Embedded");
    
    /**
     * A string representation of the flavor (also a directory name)
     */
    private String string;
    
    /**
     * Creates a new instance
     * 
     * @param string A string representation of the flavor
     */
    GltfFlavor(String string)
    {
        this.string = string;
    }
    
    /**
     * Returns the file extension for this flavor, without the dot
     * 
     * @return The file extension
     */
    String getExtension() 
    {
        if (this == BINARY)
        {
            return "glb";
        }
        return "gltf";
    }

    @Override
    public String toString()
    {
        return string;
    }
    
}