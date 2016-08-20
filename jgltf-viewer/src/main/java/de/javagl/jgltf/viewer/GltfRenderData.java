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

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.BufferView;
import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Program;
import de.javagl.jgltf.impl.Sampler;
import de.javagl.jgltf.impl.Texture;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfData;

/**
 * A class maintaining the data for rendering a glTF with OpenGL.<br>
 * <br>
 * The class offers methods to obtain the GL representations of
 * {@link Program}s, {@link Texture}s and {@link BufferView}s based
 * on the respective ID of these objects in the glTF. These GL 
 * representations are the integer values of the corresponding GL
 * objects, as generated with <code>glCreateProgram</code>, 
 * <code>glGenTextures</code> and <code>glGenBuffers</code>. <br>
 * <br>
 * The actual creation of these objects will be done lazily when
 * the objects are requested for the first time. Since the objects
 * are created using a {@link GlContext}, the methods of this class
 * may only be called from the OpenGL thread (when the GL context
 * is current). 
 */
class GltfRenderData
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GltfRenderData.class.getName());
    
    /**
     * The mapping from glTF {@link Program} IDs to GL programs
     */
    private final Map<String, Integer> programIdToGlProgram;

    /**
     * The mapping from glTF {@link Texture} IDs to GL textures
     */
    private final Map<String, Integer> textureIdToGlTexture;

    /**
     * The mapping from glTF {@link BufferView} IDs to GL buffers
     */
    private final Map<String, Integer> bufferViewIdToGlBufferView;
    
    /**
     * The {@link GltfData} that stores the data elements of the glTF
     */
    private final GltfData gltfData;
    
    /**
     * The {@link GlContext} that will create the GL objects
     */
    private final GlContext glContext;
    
    /**
     * The {@link GlTF} that was obtained from the {@link GltfData}
     */
    private final GlTF gltf;
    
    /**
     * Create the render data for the given {@link GltfData}. The GL objects
     * will be created using the given {@link GlContext}
     * 
     * @param gltfData The {@link GltfData}
     * @param glContext The {@link GlContext}
     */
    GltfRenderData(GltfData gltfData, GlContext glContext)
    {
        this.gltfData = Objects.requireNonNull(gltfData, 
            "The gltfData may not be null");
        this.glContext = Objects.requireNonNull(glContext,
            "The glContext may not be null");
        
        this.gltf = gltfData.getGltf();
        
        this.programIdToGlProgram = new LinkedHashMap<String, Integer>();
        this.textureIdToGlTexture = new LinkedHashMap<String, Integer>();
        this.bufferViewIdToGlBufferView = new LinkedHashMap<String, Integer>();
    }
    
    /**
     * Obtain the OpenGL program for the given glTF {@link Program} ID.<br>
     * <br>
     * If the OpenGL program for the specified program has already been 
     * requested, then the previously created GL program will be returned.
     * Otherwise, the GL program will be created, stored for later 
     * retrieval, and returned.<br>
     * <br>
     * If the GL program can not be created (due to missing data, or
     * due to compile- or link errors), then a warning will be printed
     * and <code>null</code> will be returned.
     *  
     * @param programId The {@link Program} ID
     * @return The GL program
     */
    Integer obtainGlProgram(String programId)
    {
        Objects.requireNonNull(programId, "The programId may not be null");
        
        Integer glProgram = programIdToGlProgram.get(programId);
        if (glProgram == null)
        {
            if (!programIdToGlProgram.containsKey(programId))
            {
                glProgram = createGlProgram(programId);
                programIdToGlProgram.put(programId, glProgram);
            }
        }
        return glProgram;
    }
    
    /**
     * Internal method to create the GL program for the given {@link Program}
     * ID. If the GL program can not be created for any reason, then a 
     * warning will be printed and <code>null</code> will be returned.
     * 
     * @param programId The {@link Program} ID
     * @return The GL program
     */
    private Integer createGlProgram(String programId)
    {
        logger.fine("Creating GL program for program " + programId);

        Program program = gltf.getPrograms().get(programId);
        
        String vertexShaderId = program.getVertexShader();
        String vertexShaderSource = 
            gltfData.getShaderAsString(vertexShaderId);
        if (vertexShaderSource == null)
        {
            logger.warning("Source of vertex shader " + 
                vertexShaderId + " not found");
            return null;
        }
        
        String fragmentShaderId = program.getFragmentShader();
        String fragmentShaderSource = 
            gltfData.getShaderAsString(fragmentShaderId);
        if (fragmentShaderSource == null)
        {
            logger.warning("Source of fragment shader " + 
                fragmentShaderId + " not found");
            return null;
        }
        
        Integer glProgram = glContext.createGlProgram(
            vertexShaderSource, fragmentShaderSource);
        if (glProgram != null)
        {
            logger.fine("Creating GL program for program " + 
                programId + " DONE");
        }
        else
        {
            logger.warning("Creating GL program for program " + 
                programId + " FAILED");
        }
        return glProgram;
    }
    

    
    
    /**
     * Obtain the OpenGL texture for the given glTF {@link Texture} ID.<br>
     * <br>
     * If the OpenGL texture for the specified texture has already been 
     * requested, then the previously created GL texture will be returned.
     * Otherwise, the GL texture will be created, stored for later 
     * retrieval, and returned.<br>
     * <br>
     * If the GL program can not be created (e.g. due to missing data), then 
     * a warning will be printed and <code>null</code> will be returned.
     *  
     * @param textureId The {@link Program} ID
     * @return The GL texture
     */
    Integer obtainGlTexture(String textureId)
    {
        Objects.requireNonNull(textureId, "The textureId may not be null");
        
        Integer glTexture = textureIdToGlTexture.get(textureId);
        if (glTexture == null)
        {
            if (!textureIdToGlTexture.containsKey(textureId))
            {
                glTexture = createGlTexture(textureId);
                textureIdToGlTexture.put(textureId, glTexture);
            }
        }
        return glTexture;
    }
    
    /**
     * Internal method to create the GL texture for the given {@link Texture}
     * ID. If the GL texture can not be created for any reason, then a 
     * warning will be printed and <code>null</code> will be returned.
     * 
     * @param textureId The {@link Texture} ID
     * @return The GL texture
     */
    private Integer createGlTexture(String textureId)
    {
        logger.fine("Creating GL texture for texture " + textureId);
        
        Texture texture = gltf.getTextures().get(textureId);
        String textureImageId = texture.getSource();
        BufferedImage textureImage = 
            gltfData.getImageAsBufferedImage(textureImageId);
        if (textureImage == null)
        {
            logger.warning(
                "Texture image data " + textureImageId + 
                " for texture " + textureId + " not found");
            return null;
        }
        String samplerId = texture.getSampler();
        Sampler sampler = gltf.getSamplers().get(samplerId);
            
        ByteBuffer pixelDataARGB = 
            ImageUtils.getImagePixelsARGB(textureImage, false);
        int width = textureImage.getWidth();
        int height = textureImage.getHeight();
        
        int format = Optional
            .ofNullable(texture.getFormat())
            .orElse(texture.defaultFormat());
        int internalFormat = Optional
            .ofNullable(texture.getInternalFormat())
            .orElse(texture.defaultInternalFormat());
        int type = Optional
            .ofNullable(texture.getType())
            .orElse(texture.defaultType());
        
        int glTexture = 
            glContext.createGlTexture(
                pixelDataARGB, internalFormat, width, height, format, type);

        int minFilter = Optional
            .ofNullable(sampler.getMinFilter())
            .orElse(sampler.defaultMinFilter());
        int magFilter = Optional
            .ofNullable(sampler.getMagFilter())
            .orElse(sampler.defaultMagFilter());
        int wrapS = Optional
            .ofNullable(sampler.getWrapS())
            .orElse(sampler.defaultWrapS());
        int wrapT = Optional
            .ofNullable(sampler.getWrapT())
            .orElse(sampler.defaultWrapT());
        
        glContext.setGlTextureParameters(
            glTexture, minFilter, magFilter, wrapS, wrapT);
        
        logger.fine("Creating GL texture for texture " + textureId + " DONE");
        return glTexture;
    }

    
    /**
     * Obtain the OpenGL buffer for the given glTF {@link BufferView} ID.<br>
     * <br>
     * If the OpenGL buffer for the specified bufferView has already been 
     * requested, then the previously created GL buffer will be returned.
     * Otherwise, the GL buffer will be created, stored for later 
     * retrieval, and returned.<br>
     * <br>
     * If the GL buffer can not be created (e.g. due to missing data), then 
     * a warning will be printed and <code>null</code> will be returned.
     *  
     * @param bufferViewId The {@link BufferView} ID
     * @return The GL buffer
     */
    Integer obtainGlBufferView(String bufferViewId)
    {
        Objects.requireNonNull(bufferViewId, 
            "The bufferViewId may not be null");
        
        Integer glBufferView = bufferViewIdToGlBufferView.get(bufferViewId);
        if (glBufferView == null)
        {
            if (!bufferViewIdToGlBufferView.containsKey(bufferViewId))
            {
                glBufferView = createGlBufferView(bufferViewId);
                bufferViewIdToGlBufferView.put(bufferViewId, glBufferView);
            }
        }
        return glBufferView;
    }
    
    /**
     * Internal method to create the GL buffer for the given {@link BufferView}
     * ID. If the GL buffer can not be created for any reason, then a 
     * warning will be printed and <code>null</code> will be returned.
     * 
     * @param bufferViewId The {@link BufferView} ID
     * @return The GL buffer
     */
    private Integer createGlBufferView(String bufferViewId)
    {
        logger.fine("Creating GL bufferView for bufferView " + bufferViewId);

        BufferView bufferView = gltf.getBufferViews().get(bufferViewId);
        
        Integer byteLength = bufferView.getByteLength();
        if (byteLength == null)
        {
            // TODO: This will no longer be valid in glTF 1.0.1
            logger.warning("No byteLength found in bufferView "+bufferViewId);
            logger.warning("This will no longer be valid in glTF 1.0.1");
            return null;
        }
        
        ByteBuffer bufferViewData = gltfData.getBufferViewData(bufferViewId);
        if (bufferViewData == null)
        {
            logger.warning("No data found for bufferView " + bufferViewId);
            return null;
        }
        
        int target = Optional
            .ofNullable(bufferView.getTarget())
            .orElse(GltfConstants.GL_ARRAY_BUFFER);
        
        int glBufferView = 
            glContext.createGlBufferView(
                target, byteLength, bufferViewData.slice());
        logger.fine("Creating GL bufferView for bufferView " + bufferViewId + 
            " DONE");
        return glBufferView;
    }
    
}
