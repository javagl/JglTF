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
package de.javagl.jgltf.viewer.jogl;

import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_T;
import static com.jogamp.opengl.GL.GL_TRUE;
import static com.jogamp.opengl.GL2ES2.GL_COMPILE_STATUS;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_INFO_LOG_LENGTH;
import static com.jogamp.opengl.GL2ES2.GL_VALIDATE_STATUS;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
import static com.jogamp.opengl.GL2ES3.GL_TEXTURE_BASE_LEVEL;
import static com.jogamp.opengl.GL2ES3.GL_TEXTURE_MAX_LEVEL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import com.jogamp.opengl.GL3;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.viewer.GlContext;

/**
 * Implementation of a {@link GlContext} based on JOGL
 */
class GlContextJogl implements GlContext
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GlContextJogl.class.getName());
    
    /**
     * The actual GL context
     */
    private GL3 gl;
    
    /**
     * Set the GL context to be used internally
     * 
     * @param gl The GL context
     */
    void setGL(GL3 gl)
    {
        this.gl = gl;
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
        
        Integer glVertexShader = createGlShader(
            GL_VERTEX_SHADER, vertexShaderSource);
        if (glVertexShader == null)
        {
            logger.warning("Creating vertex shader FAILED");
            return null;
        }

        logger.fine("Creating vertex shader DONE");
        
        logger.fine("Creating fragment shader...");
        Integer glFragmentShader = createGlShader(
            GL_FRAGMENT_SHADER, fragmentShaderSource);
        if (glFragmentShader == null)
        {
            logger.warning("Creating fragment shader FAILED");
            return null;
        }
        logger.fine("Creating fragment shader DONE");
        
        int glProgram  = gl.glCreateProgram();

        gl.glAttachShader(glProgram, glVertexShader);
        gl.glDeleteShader(glVertexShader);

        gl.glAttachShader(glProgram, glFragmentShader);
        gl.glDeleteShader(glFragmentShader);
        
        gl.glLinkProgram(glProgram);
        gl.glValidateProgram(glProgram);
        
        int validateStatus[] = { 0 };
        gl.glGetProgramiv(glProgram, GL_VALIDATE_STATUS, validateStatus, 0);
        if (validateStatus[0] != GL_TRUE)
        {
            printProgramLogInfo(glProgram);
            return null;
        }
        return glProgram;
    }
    
    @Override
    public void useGlProgram(int glProgram)
    {
        gl.glUseProgram(glProgram);
    }
    
    @Override
    public void deleteGlProgram(int glProgram)
    {
        gl.glDeleteProgram(glProgram);
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
                    gl.glEnable(state.intValue());
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
                    gl.glDisable(state.intValue());
                }
            }
        }
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
        Integer glShader = createGlShaderImpl(shaderType, shaderSource);
        if (glShader != null)
        {
            return glShader;
        }
                
        // If the shader source code does not contain a #version number, 
        // then, depending on the com.jogamp.opengl.GLProfile that was 
        // chosen for the viewer, certain warnings may be treated as 
        // errors. As a workaround, pragmatically insert a version 
        // number and try again...
        // (Also see https://github.com/javagl/JglTF/issues/12)
        if (!shaderSource.contains("#version"))
        {
            String versionString = "#version 120";
            logger.warning("Inserting GLSL version specifier \"" + 
                versionString + "\" in shader code");
            String shaderSourceWithVersion = 
                versionString + "\n" + shaderSource;
            return createGlShaderImpl(shaderType, shaderSourceWithVersion);
        }
        return null;
    }
    
    /**
     * Implementation for {@link #createGlShader(int, String)}.
     * 
     * @param shaderType The shader type
     * @param shaderSource The shader source code
     * @return The GL shader, or <code>null</code> if it cannot be compiled
     */
    private Integer createGlShaderImpl(int shaderType, String shaderSource)
    {
        int glShader = gl.glCreateShader(shaderType);
        gl.glShaderSource(
            glShader, 1, new String[]{shaderSource}, null);
        gl.glCompileShader(glShader);     
        int compileStatus[] = { 0 };
        gl.glGetShaderiv(glShader, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] != GL_TRUE)
        {
            printShaderLogInfo(glShader);
            return null;
        }
        return glShader;
        
    }
    
    
    @Override
    public int getUniformLocation(int glProgram, String uniformName)
    {
        gl.glUseProgram(glProgram);
        return gl.glGetUniformLocation(glProgram, uniformName);
    }

    @Override
    public int getAttributeLocation(int glProgram, String attributeName)
    {
        gl.glUseProgram(glProgram);
        return gl.glGetAttribLocation(glProgram, attributeName);
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
                gl.glUniform1iv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_INT_VEC2:
            {
                gl.glUniform2iv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_INT_VEC3:
            {
                gl.glUniform3iv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_INT_VEC4:   
            {
                gl.glUniform4iv(location, count, value, 0);
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
                gl.glUniform1fv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC2:
            {
                gl.glUniform2fv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC3:
            {
                gl.glUniform3fv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC4:   
            {
                gl.glUniform4fv(location, count, value, 0);
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
                gl.glUniformMatrix2fv(location, count, false, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_MAT3:
            {
                gl.glUniformMatrix3fv(location, count, false, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_MAT4:
            {
                gl.glUniformMatrix4fv(location, count, false, value, 0);
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
        gl.glActiveTexture(GL_TEXTURE0+textureIndex);
        gl.glBindTexture(GL_TEXTURE_2D, glTexture);
        gl.glUniform1i(location, textureIndex);
    }
    
    @Override
    public int createGlVertexArray()
    {
        int vertexArrayArray[] = {0};
        gl.glGenVertexArrays(1, vertexArrayArray, 0);
        int glVertexArray = vertexArrayArray[0];
        return glVertexArray;
    }
    
    @Override
    public void deleteGlVertexArray(int glVertexArray)
    {
        gl.glDeleteVertexArrays(1, new int[] { glVertexArray }, 0);
    }

    @Override
    public int createGlBufferView(
        int target, int byteLength, ByteBuffer bufferViewData)
    {
        int bufferViewArray[] = {0};
        gl.glGenBuffers(1, bufferViewArray, 0);
        int glBufferView = bufferViewArray[0];
        gl.glBindBuffer(target, glBufferView);
        gl.glBufferData(target, byteLength, bufferViewData, GL_STATIC_DRAW);
        return glBufferView;
    }
    
    @Override
    public void createVertexAttribute(int glVertexArray, 
        int target, int glBufferView, int attributeLocation, 
        int size, int type, int stride, int offset)
    {
        gl.glBindVertexArray(glVertexArray);
        gl.glBindBuffer(target, glBufferView);
        gl.glVertexAttribPointer(
            attributeLocation, size, type, false, stride, offset);
        gl.glEnableVertexAttribArray(attributeLocation);
    }
    
    @Override
    public void updateVertexAttribute(int glVertexArray, 
        int target, int glBufferView, int offset, int size, ByteBuffer data)
    {
        gl.glBindVertexArray(glVertexArray);
        gl.glBindBuffer(target, glBufferView);
        gl.glBufferSubData(target, offset, size, data);
    }
    
    
    @Override
    public void deleteGlBufferView(int glBufferView)
    {
        gl.glDeleteBuffers(1, new int[] { glBufferView }, 0);
    }
    

    @Override
    public int createGlTexture(
        ByteBuffer pixelData, int internalFormat, 
        int width, int height, int format, int type)
    {
        int textureArray[] = {0};
        gl.glGenTextures(1, textureArray, 0);
        int glTexture = textureArray[0];

        gl.glBindTexture(GL_TEXTURE_2D, glTexture);
        gl.glTexImage2D(
            GL_TEXTURE_2D, 0, internalFormat, width, height, 
            0, format, type, pixelData);
        
        return glTexture;
    }
    
    @Override
    public void setGlTextureParameters(int glTexture, 
        int minFilter, int magFilter, int wrapS, int wrapT)
    {
        gl.glBindTexture(GL_TEXTURE_2D, glTexture);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS); 
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT); 
    }
    
    @Override
    public void deleteGlTexture(int glTexture)
    {
        gl.glDeleteTextures(1, new int[] { glTexture }, 0);
    }
    
    
    
    
    @Override
    public void renderIndexed(
        int glVertexArray, int mode, int glIndicesBuffer, 
        int numIndices, int indicesType, int offset)
    {
        gl.glBindVertexArray(glVertexArray);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndicesBuffer);
        gl.glDrawElements(mode, numIndices, indicesType, offset);
    }
    
    @Override
    public void renderNonIndexed(int glVertexArray, int mode, int numVertices)
    {
        gl.glBindVertexArray(glVertexArray);
        gl.glDrawArrays(mode, 0, numVertices);
    }
    
    
    @Override
    public void setBlendColor(float r, float g, float b, float a)
    {
        gl.glBlendColor(r, g, b, a);
    }

    @Override
    public void setBlendEquationSeparate(int modeRgb, int modeAlpha)
    {
        gl.glBlendEquationSeparate(modeRgb, modeAlpha);
    }

    @Override
    public void setBlendFuncSeparate(
        int srcRgb, int dstRgb, int srcAlpha, int dstAlpha)
    {
        gl.glBlendFuncSeparate(srcRgb, dstRgb, srcAlpha, dstAlpha);
    }

    @Override
    public void setColorMask(boolean r, boolean g, boolean b, boolean a)
    {
        gl.glColorMask(r, g, b, a);
    }

    @Override
    public void setCullFace(int mode)
    {
        gl.glCullFace(mode);
    }

    @Override
    public void setDepthFunc(int func)
    {
        gl.glDepthFunc(func);
    }

    @Override
    public void setDepthMask(boolean mask)
    {
        gl.glDepthMask(mask);
    }

    @Override
    public void setDepthRange(float zNear, float zFar)
    {
        gl.glDepthRange(zNear, zFar);
    }

    @Override
    public void setFrontFace(int mode)
    {
        gl.glFrontFace(mode);
    }

    @Override
    public void setLineWidth(float width)
    {
        gl.glLineWidth(width);
    }

    @Override
    public void setPolygonOffset(float factor, float units)
    {
        gl.glPolygonOffset(factor, units);
    }

    @Override
    public void setScissor(int x, int y, int width, int height)
    {
        gl.glScissor(x, y, width, height);
    }
    

    /**
     * For debugging: Print shader log info
     * 
     * @param id shader ID
     */
    private void printShaderLogInfo(int id) 
    {
        IntBuffer infoLogLength = ByteBuffer
            .allocateDirect(4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
        gl.glGetShaderiv(id, GL_INFO_LOG_LENGTH, infoLogLength);
        if (infoLogLength.get(0) > 0) 
        {
            infoLogLength.put(0, infoLogLength.get(0) - 1);
        }

        ByteBuffer infoLog = ByteBuffer
            .allocateDirect(infoLogLength.get(0))
            .order(ByteOrder.nativeOrder());
        gl.glGetShaderInfoLog(id, infoLogLength.get(0), null, infoLog);

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
        IntBuffer infoLogLength = ByteBuffer
            .allocateDirect(4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
        gl.glGetProgramiv(id, GL_INFO_LOG_LENGTH, infoLogLength);
        if (infoLogLength.get(0) > 0) 
        {
            infoLogLength.put(0, infoLogLength.get(0) - 1);
        }

        ByteBuffer infoLog = ByteBuffer
            .allocateDirect(infoLogLength.get(0))
            .order(ByteOrder.nativeOrder());
        gl.glGetProgramInfoLog(id, infoLogLength.get(0), null, infoLog);

        String infoLogString = 
            Charset.forName("US-ASCII").decode(infoLog).toString();
        if (infoLogString.trim().length() > 0)
        {
            logger.warning("program log:\n"+infoLogString);
        }
    }

}
