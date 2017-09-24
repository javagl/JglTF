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
package de.javagl.jgltf.viewer.lwjgl;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDepthRange;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPolygonOffset;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.glBlendColor;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBlendEquationSeparate;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetProgram;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2;
import static org.lwjgl.opengl.GL20.glUniform3;
import static org.lwjgl.opengl.GL20.glUniform4;
import static org.lwjgl.opengl.GL20.glUniformMatrix2;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.viewer.GlContext;

/**
 * Implementation of a {@link GlContext} based on LWJGL
 */
class GlContextLwjgl implements GlContext
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GlContextLwjgl.class.getName());

    /**
     * A buffer that will be used temporarily for the values of
     * integer uniforms. This is a direct buffer that is created
     * and resized as necessary in {@link #putIntBuffer(int[])}
     */
    private IntBuffer uniformIntBuffer = null;

    /**
     * A buffer that will be used temporarily for the values of
     * float uniforms. This is a direct buffer that is created
     * and resized as necessary in {@link #putFloatBuffer(float[])}
     */
    private FloatBuffer uniformFloatBuffer = null;

    /**
     * Put the given values into a direct IntBuffer and return it.
     * The returned buffer may always be a slice of the same instance.
     * This method is supposed to be called only from the OpenGL thread.
     *
     * @param value The value
     * @return The IntBuffer
     */
    private IntBuffer putIntBuffer(int value[])
    {
        int total = value.length;
        if (uniformIntBuffer == null || uniformIntBuffer.capacity() < total)
        {
            uniformIntBuffer = ByteBuffer
                .allocateDirect(total * Integer.BYTES)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        }
        uniformIntBuffer.position(0);
        uniformIntBuffer.limit(uniformIntBuffer.capacity());
        uniformIntBuffer.put(value);
        uniformIntBuffer.flip();
        return uniformIntBuffer;
    }

    /**
     * Put the given values into a direct IntBuffer and return it.
     * The returned buffer may always be a slice of the same instance.
     * This method is supposed to be called only from the OpenGL thread.
     *
     * @param value The value
     * @return The IntBuffer
     */
    private FloatBuffer putFloatBuffer(float value[])
    {
        int total = value.length;
        if (uniformFloatBuffer == null || uniformFloatBuffer.capacity() < total)
        {
            uniformFloatBuffer = ByteBuffer
                .allocateDirect(total * Float.BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        }
        uniformFloatBuffer.position(0);
        uniformFloatBuffer.limit(uniformFloatBuffer.capacity());
        uniformFloatBuffer.put(value);
        uniformFloatBuffer.flip();
        return uniformFloatBuffer;
    }

    @Override
    public Integer createGlProgram(
        String vertexShaderSource, String fragmentShaderSource)
    {
        if (vertexShaderSource == null)
        {
            logger.warning("The vertexShaderSource is null");
            return null;
        }
        if (fragmentShaderSource == null)
        {
            logger.warning("The fragmentShaderSource is null");
            return null;
        }

        logger.fine("Creating vertex shader...");

        Integer glVertexShader =
            createGlShader(GL_VERTEX_SHADER, vertexShaderSource);
        if (glVertexShader == null)
        {
            logger.warning("Creating vertex shader FAILED");
            return null;
        }

        logger.fine("Creating vertex shader DONE");

        logger.fine("Creating fragment shader...");
        Integer glFragmentShader =
            createGlShader(GL_FRAGMENT_SHADER, fragmentShaderSource);
        if (glFragmentShader == null)
        {
            logger.warning("Creating fragment shader FAILED");
            return null;
        }
        logger.fine("Creating fragment shader DONE");

        int glProgram  = glCreateProgram();

        glAttachShader(glProgram, glVertexShader);
        glDeleteShader(glVertexShader);

        glAttachShader(glProgram, glFragmentShader);
        glDeleteShader(glFragmentShader);

        glLinkProgram(glProgram);
        glValidateProgram(glProgram);

        int validateStatus = glGetProgram(glProgram, GL_VALIDATE_STATUS);
        if (validateStatus != GL_TRUE)
        {
            printProgramLogInfo(glProgram);
            return null;
        }
        return glProgram;
    }

    /**
     * Creates an OpenGL shader with the given type, from the given source
     * code, and returns the GL shader object. If the shader cannot be
     * compiled, then <code>null</code> will be returned.
     *
     * @param shaderType The shader type
     * @param shaderSource The shader source code
     * @return The GL shader
     */
    private Integer createGlShader(int shaderType, String shaderSource)
    {
        int glShader = glCreateShader(shaderType);
        glShaderSource(glShader, shaderSource);
        glCompileShader(glShader);
        int compileStatus = glGetShader(glShader, GL_COMPILE_STATUS);
        if (compileStatus != GL_TRUE)
        {
            printShaderLogInfo(glShader);
        }
        return glShader;
    }

    @Override
    public void useGlProgram(int glProgram)
    {
        glUseProgram(glProgram);
    }

    @Override
    public void deleteGlProgram(int glProgram)
    {
        glDeleteProgram(glProgram);
    }

    @Override
    public void enable(Iterable<? extends Number> states)
    {
        if (states != null)
        {
            for (Number state : states)
            {
                if (state != null)
                {
                    glEnable(state.intValue());
                }
            }
        }
    }

    @Override
    public void disable(Iterable<? extends Number> states)
    {
        if (states != null)
        {
            for (Number state : states)
            {
                if (state != null)
                {
                    glDisable(state.intValue());
                }
            }
        }
    }

    @Override
    public int getUniformLocation(int glProgram, String uniformName)
    {
        glUseProgram(glProgram);
        return glGetUniformLocation(glProgram, uniformName);
    }

    @Override
    public int getAttributeLocation(int glProgram, String attributeName)
    {
        glUseProgram(glProgram);
        return glGetAttribLocation(glProgram, attributeName);
    }

    @Override
    public void setUniformiv(int type, int location, int count, int value[])
    {
        if (value == null)
        {
            logger.warning("Invalid uniform value: " + value);
            return;
        }
        switch (type)
        {
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            {
                IntBuffer b = putIntBuffer(value);
                glUniform1(location, b);
                break;
            }
            case GltfConstants.GL_INT_VEC2:
            {
                IntBuffer b = putIntBuffer(value);
                glUniform2(location, b);
                break;
            }
            case GltfConstants.GL_INT_VEC3:
            {
                IntBuffer b = putIntBuffer(value);
                glUniform3(location, b);
                break;
            }
            case GltfConstants.GL_INT_VEC4:
            {
                IntBuffer b = putIntBuffer(value);
                glUniform4(location, b);
                break;
            }
            default:
                logger.warning("Invalid uniform type: " +
                    GltfConstants.stringFor(type));
        }
    }

    @Override
    public void setUniformfv(int type, int location, int count, float value[])
    {
        if (value == null)
        {
            logger.warning("Invalid uniform value: " + value);
            return;
        }
        switch (type)
        {
            case GltfConstants.GL_FLOAT:
            {
                FloatBuffer b = putFloatBuffer(value);
                glUniform1(location, b);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC2:
            {
                FloatBuffer b = putFloatBuffer(value);
                glUniform2(location, b);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC3:
            {
                FloatBuffer b = putFloatBuffer(value);
                glUniform3(location, b);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC4:
            {
                FloatBuffer b = putFloatBuffer(value);
                glUniform4(location, b);
                break;
            }
            default:
                logger.warning("Invalid uniform type: " +
                    GltfConstants.stringFor(type));
        }

    }

    @Override
    public void setUniformMatrixfv(
        int type, int location, int count, float value[])
    {
        if (value == null)
        {
            logger.warning("Invalid uniform value: " + value);
            return;
        }
        switch (type)
        {
            case GltfConstants.GL_FLOAT_MAT2:
            {
                FloatBuffer b = putFloatBuffer(value);
                glUniformMatrix2(location, false, b);
                break;
            }
            case GltfConstants.GL_FLOAT_MAT3:
            {
                FloatBuffer b = putFloatBuffer(value);
                glUniformMatrix3(location, false, b);
                break;
            }
            case GltfConstants.GL_FLOAT_MAT4:
            {
                FloatBuffer b = putFloatBuffer(value);
                glUniformMatrix4(location, false, b);
                break;
            }
            default:
                logger.warning("Invalid uniform type: " +
                    GltfConstants.stringFor(type));
        }
    }


    @Override
    public void setUniformSampler(int location, int textureIndex, int glTexture)
    {
        glActiveTexture(GL_TEXTURE0+textureIndex);
        glBindTexture(GL_TEXTURE_2D, glTexture);
        glUniform1i(location, textureIndex);
    }


    @Override
    public int createGlVertexArray()
    {
        int glVertexArray = glGenVertexArrays();
        return glVertexArray;
    }

    @Override
    public void deleteGlVertexArray(int glVertexArray)
    {
        glDeleteVertexArrays(glVertexArray);
    }

    @Override
    public int createGlBufferView(
        int target, int byteLength, ByteBuffer bufferViewData)
    {
        int glBufferView = glGenBuffers();
        glBindBuffer(target, glBufferView);
        ByteBuffer data = bufferViewData.slice();
        data.limit(byteLength);
        glBufferData(target, bufferViewData, GL_STATIC_DRAW);
        return glBufferView;
    }

    @Override
    public void createVertexAttribute(int glVertexArray,
        int target, int glBufferView, int attributeLocation,
        int size, int type, int stride, int offset)
    {
        glBindVertexArray(glVertexArray);
        glBindBuffer(target, glBufferView);
        glVertexAttribPointer(
            attributeLocation, size, type, false, stride, offset);
        glEnableVertexAttribArray(attributeLocation);
    }

    @Override
    public void updateVertexAttribute(int glVertexArray, 
        int target, int glBufferView, int offset, int size, ByteBuffer data)
    {
        glBindVertexArray(glVertexArray);
        glBindBuffer(target, glBufferView);
        glBufferSubData(target, offset, data);
    }
    
    @Override
    public void deleteGlBufferView(int glBufferView)
    {
        glDeleteBuffers(glBufferView);
    }

    @Override
    public int createGlTexture(
        ByteBuffer pixelData, int internalFormat,
        int width, int height, int format, int type)
    {
        int glTexture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, glTexture);
        glTexImage2D(
            GL_TEXTURE_2D, 0, internalFormat, width, height,
            0, format, type, pixelData);

        return glTexture;
    }

    @Override
    public void setGlTextureParameters(int glTexture,
        int minFilter, int magFilter, int wrapS, int wrapT)
    {
        glBindTexture(GL_TEXTURE_2D, glTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT);
    }

    @Override
    public void deleteGlTexture(int glTexture)
    {
        glDeleteTextures(glTexture);
    }

    @Override
    public void renderIndexed(
        int glVertexArray, int mode, int glIndicesBuffer,
        int numIndices, int indicesType, int offset)
    {
        glBindVertexArray(glVertexArray);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndicesBuffer);
        glDrawElements(mode, numIndices, indicesType, offset);
    }

    @Override
    public void renderNonIndexed(int glVertexArray, int mode, int numVertices)
    {
        glBindVertexArray(glVertexArray);
        glDrawArrays(mode, 0, numVertices);
    }

    @Override
    public void setBlendColor(float r, float g, float b, float a)
    {
        glBlendColor(r, g, b, a);
    }

    @Override
    public void setBlendEquationSeparate(int modeRgb, int modeAlpha)
    {
        glBlendEquationSeparate(modeRgb, modeAlpha);
    }

    @Override
    public void setBlendFuncSeparate(
        int srcRgb, int dstRgb, int srcAlpha, int dstAlpha)
    {
        glBlendFuncSeparate(srcRgb, dstRgb, srcAlpha, dstAlpha);
    }

    @Override
    public void setColorMask(boolean r, boolean g, boolean b, boolean a)
    {
        glColorMask(r, g, b, a);
    }

    @Override
    public void setCullFace(int mode)
    {
        glCullFace(mode);
    }

    @Override
    public void setDepthFunc(int func)
    {
        glDepthFunc(func);
    }

    @Override
    public void setDepthMask(boolean mask)
    {
        glDepthMask(mask);
    }

    @Override
    public void setDepthRange(float zNear, float zFar)
    {
        glDepthRange(zNear, zFar);
    }

    @Override
    public void setFrontFace(int mode)
    {
        glFrontFace(mode);
    }

    @Override
    public void setLineWidth(float width)
    {
        glLineWidth(width);
    }

    @Override
    public void setPolygonOffset(float factor, float units)
    {
        glPolygonOffset(factor, units);
    }

    @Override
    public void setScissor(int x, int y, int width, int height)
    {
        glScissor(x, y, width, height);
    }

    /**
     * For debugging: Print shader log info
     *
     * @param id shader ID
     */
    private void printShaderLogInfo(int id)
    {
        IntBuffer infoLogLength = ByteBuffer.allocateDirect(4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
        glGetShader(id, GL_INFO_LOG_LENGTH, infoLogLength);
        if (infoLogLength.get(0) > 0)
        {
            infoLogLength.put(0, infoLogLength.get(0)-1);
        }

        ByteBuffer infoLog = ByteBuffer.allocateDirect(infoLogLength.get(0))
            .order(ByteOrder.nativeOrder());
        glGetShaderInfoLog(id, infoLogLength, infoLog);

        String infoLogString =
            Charset.forName("US-ASCII").decode(infoLog).toString();
        if (infoLogString.trim().length() > 0)
        {
            logger.warning("shader log:\n"+infoLogString);
        }
    }

    /**
     * For debugging: Print program log info
     *
     * @param id program ID
     */
    private void printProgramLogInfo(int id)
    {
        IntBuffer infoLogLength = ByteBuffer.allocateDirect(4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
        glGetProgram(id, GL_INFO_LOG_LENGTH, infoLogLength);
        if (infoLogLength.get(0) > 0)
        {
            infoLogLength.put(0, infoLogLength.get(0)-1);
        }

        ByteBuffer infoLog = ByteBuffer.allocateDirect(infoLogLength.get(0))
            .order(ByteOrder.nativeOrder());
        glGetProgramInfoLog(id, infoLogLength, infoLog);

        String infoLogString =
            Charset.forName("US-ASCII").decode(infoLog).toString();
        if (infoLogString.trim().length() > 0)
        {
            logger.warning("program log:\n"+infoLogString);
        }
    }


}
