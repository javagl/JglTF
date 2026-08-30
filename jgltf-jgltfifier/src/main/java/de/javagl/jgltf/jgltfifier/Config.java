/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.jgltfifier;

/**
 * A <b>HIGHLY PRELIMINARY</b> class holding different configuration options.
 */
public class Config
{
    /**
     * The root directory of all output
     */
    public String outputRootDirectory = "./";

    /**
     * The root directory of the source code output.
     * 
     * This is relative to the <code>outputRootDirectory</code>.
     */
    public String sourceCodeRootDirectory = "src/main/java/";

    /**
     * The directory to which external data (images and accessors) will be
     * written.
     * 
     * This is relative to the <code>outputRootDirectory</code>.
     * 
     * The generated class will refer to this directory for reading the
     * externalized data.
     */
    public String generatedDataDirectory = "data/generatedData";
    
    /**
     * The package name for the class that will be generated
     */
    public String packageName = "de.javagl.jgltf.jgltfifier.generated";

    /**
     * The name of the class that will be generated
     */
    public String className = "Generated";

    /**
     * The name of the glTF (GLB) file that will be generated when the main
     * method of the generated class is executed.
     * 
     * This is the full path, in the context of the generated class (and
     * therefore, independent of the working context of the JglTFifier itself)
     */
    public String outputGltfFileName = "Generated.glb";

    /**
     * The number of bytes that an accessor must have before it is written to an
     * external file.
     */
    public int accessorExternalizationThresholdBytes = 16384;
}