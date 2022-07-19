/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.jgltf.model.io.test;

import java.util.Map;

/**
 * A plain structure, <b>only for the test package</b>, used to deserialize
 * the glTF-SampleModels 'model-index.json' file.
 */
@SuppressWarnings("javadoc")
public class ModelIndexEntry
{
    public String name;
    public String screenshot;
    public Map<String, String> variants;
}