/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.viewer;

import java.nio.ByteBuffer;

/**
 * An interface intended as a minimal abstraction layer for different
 * Java OpenGL bindings, to be used with the glTF viewer. <br>
 * <br>
 * Many details in this interface are (intentionally) not specified. 
 * This is only intended for internal use.  
 */
public interface GlContext
{
    /**
     * Create an OpenGL program with a vertex- and fragment shader created
     * from the given sources. If it is not possible to create the GL
     * program (due to compile- or link errors), then an error message
     * may be printed, and <code>null</code> will be returned.
     * 
     * @param vertexShaderSource The vertex shader source code
     * @param fragmentShaderSource The fragment shader source code
     * @return The GL program
     */
    Integer createGlProgram(
        String vertexShaderSource,
        String fragmentShaderSource);

    /**
     * Instruct the underling GL implementation to use the given program
     * 
     * @param glProgram The GL program
     */
    void useGlProgram(int glProgram);

    /**
     * Delete the given GL program
     * 
     * @param glProgram The GL program
     */
    void deleteGlProgram(int glProgram);
    
    /**
     * Enable all the states that are found in the given list, by calling
     * <code>glEnable</code> for all of them.
     * 
     * @param states The states
     */
    void enable(Iterable<? extends Number> states);

    /**
     * Disable all the states that are found in the given list, by calling
     * <code>glDisable</code> for all of them.
     * 
     * @param states The states
     */
    void disable(Iterable<? extends Number> states);
    
    /**
     * Returns the location of the specified uniform in the given program
     *  
     * @param glProgram The GL program
     * @param uniformName The name of the uniform
     * @return The uniform location
     */
    int getUniformLocation(int glProgram, String uniformName);

    /**
     * Returns the location of the specified attribute in the given program
     *  
     * @param glProgram The GL program
     * @param attributeName The name of the attribute
     * @return The attribute location
     */
    int getAttributeLocation(int glProgram, String attributeName);
    
    /**
     * Set the value of the specified uniform
     * 
     * @param type The type of the uniform
     * @param location The uniform location
     * @param count The number of elements to set
     * @param value The value to set
     */
    void setUniformiv(int type, int location, int count, int value[]);

    /**
     * Set the value of the specified uniform
     * 
     * @param type The type of the uniform
     * @param location The uniform location
     * @param count The number of elements to set
     * @param value The value to set
     */
    void setUniformfv(int type, int location, int count, float value[]);

    /**
     * Set the value of the specified uniform
     * 
     * @param type The type of the uniform
     * @param location The uniform location
     * @param count The number of elements to set
     * @param value The value to set
     */
    void setUniformMatrixfv(int type, int location, int count, float value[]);

    /**
     * Set the value of the specified uniform
     * 
     * @param location The uniform location
     * @param textureIndex The index of the texture unit
     * @param glTexture The GL texture
     */
    void setUniformSampler(int location, int textureIndex, int glTexture);

    /**
     * Create an OpenGL vertex array object
     * 
     * @return The vertex array object 
     */
    int createGlVertexArray();

    /**
     * Delete the given GL vertex array object
     * 
     * @param glVertexArray The GL vertex array object
     */
    void deleteGlVertexArray(int glVertexArray);
    
    /**
     * Create an OpenGL buffer view (vertex buffer object) from the given data
     * 
     * @param target The target, GL_ARRAY_BUFFER or GL_ELEMENT_ARRAY_BUFFER
     * @param byteLength The length of the buffer data, in bytes
     * @param bufferViewData The actual buffer data
     * @return The GL buffer
     */
    int createGlBufferView(
        int target, int byteLength, ByteBuffer bufferViewData);

    /**
     * Create a vertex attribute in the given GL vertex array
     * 
     * @param glVertexArray The GL vertex array object
     * @param target The target, GL_ARRAY_BUFFER or GL_ELEMENT_ARRAY_BUFFER
     * @param glBufferView The GL buffer view (vertex buffer object)
     * @param attributeLocation The attribute location to bind to
     * @param size The size of the attribute data, in bytes
     * @param type The type of the attribute
     * @param stride The stride between elements of the attribute, in bytes
     * @param offset The offset of the attribute data, in bytes
     */
    void createVertexAttribute(int glVertexArray, int target, int glBufferView,
        int attributeLocation, int size, int type, int stride, int offset);

    /**
     * Delete the given GL buffer
     * 
     * @param glBufferView The GL buffer
     */
    void deleteGlBufferView(int glBufferView);
    
    /**
     * Create an OpenGL texture from the given texture parameters
     * 
     * @param pixelDataARGB The pixel data, as an array containing the
     * pixels as integers in the ARGB format (as obtained from a BufferedImage)
     * @param internalFormat The internal format
     * @param width The width
     * @param height The height
     * @param format The format
     * @param type The type
     * @return The GL texture
     */
    int createGlTexture(ByteBuffer pixelDataARGB, 
        int internalFormat, int width, int height, int format, int type);

    /**
     * Set the parameters for the given GL texture
     * 
     * @param glTexture The GL texture
     * @param minFilter The minimization filter method
     * @param magFilter The magnification filter method
     * @param wrapS The wrapping method along the S-axis
     * @param wrapT The wrapping method along the T-axis
     */
    void setGlTextureParameters(
        int glTexture, int minFilter, int magFilter, int wrapS, int wrapT);

    /**
     * Delete the given GL texture
     * 
     * @param glTexture The GL texture
     */
    void deleteGlTexture(int glTexture);
    
    /**
     * Render an indexed object, described by the given parameters
     * 
     * @param glVertexArray The GL vertex array
     * @param mode The rendering mode (GL_TRIANGLES, for example)
     * @param glIndicesBuffer The indices buffer object
     * @param numIndices The number of indices
     * @param indicesType The type of the indices
     * @param offset The offset in the indices buffer, in bytes
     */
    void renderIndexed(int glVertexArray, int mode, int glIndicesBuffer, 
        int numIndices, int indicesType, int offset);

    /**
     * Render a non-indexed object, described by the given vertex array 
     * 
     * @param glVertexArray The GL vertex array
     * @param mode The rendering mode (GL_TRIANGLES, for example)
     * @param numVertices The number of vertices of the object
     */
    void renderNonIndexed(int glVertexArray, int mode, int numVertices);

}