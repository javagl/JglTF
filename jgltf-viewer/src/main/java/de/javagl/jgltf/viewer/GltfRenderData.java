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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.v1.BufferView;
import de.javagl.jgltf.impl.v1.GlTF;
import de.javagl.jgltf.impl.v1.Program;
import de.javagl.jgltf.impl.v1.Sampler;
import de.javagl.jgltf.impl.v1.Texture;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.gl.GltfTechniqueModel;

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
     * The GL vertex array objects
     */
    private final List<Integer> glVertexArrays;
    
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
     * The {@link GltfTechniqueModel} that contains the elements for 
     * rendering a glTF asset with GL
     */
    private final GltfTechniqueModel gltfTechniqueModel;
    
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
     * @param gltfTechniqueModel The {@link GltfTechniqueModel}
     * @param glContext The {@link GlContext}
     */
    GltfRenderData(GltfData gltfData, GltfTechniqueModel gltfTechniqueModel, 
        GlContext glContext)
    {
        this.gltfData = Objects.requireNonNull(gltfData, 
            "The gltfData may not be null");
        this.gltfTechniqueModel = Objects.requireNonNull(gltfTechniqueModel,
            "The gltfTechniqueModel may not be null");
        this.glContext = Objects.requireNonNull(glContext,
            "The glContext may not be null");
        
        this.gltf = gltfData.getGltf();
        
        this.glVertexArrays = new ArrayList<Integer>();
        this.programIdToGlProgram = new LinkedHashMap<String, Integer>();
        this.textureIdToGlTexture = new LinkedHashMap<String, Integer>();
        this.bufferViewIdToGlBufferView = new LinkedHashMap<String, Integer>();
    }

    /**
     * Add the given GL vertex array 
     * 
     * @param glVertexArray The GL vertex array
     */
    void addGlVertexArray(int glVertexArray)
    {
        glVertexArrays.add(glVertexArray);
    }
    
    /**
     * Returns an unmodifiable view on the GL vertex arrays
     *  
     * @return The GL vertex arrays
     */
    Collection<Integer> getGlVertexArrays()
    {
        return Collections.unmodifiableList(glVertexArrays);
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
        
        Program program = gltfTechniqueModel.obtainProgram(programId);
        if (program == null)
        {
            logger.warning("Program for " + programId + " not found");
            return null;
        }
        
        String vertexShaderId = program.getVertexShader();
        String vertexShaderSource = 
            gltfTechniqueModel.obtainVertexShaderSource(vertexShaderId);
        if (vertexShaderSource == null)
        {
            logger.warning("Source of vertex shader " + 
                vertexShaderId + " not found");
            return null;
        }
        
        String fragmentShaderId = program.getFragmentShader();
        String fragmentShaderSource = 
            gltfTechniqueModel.obtainFragmentShaderSource(fragmentShaderId);
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
     * Returns an unmodifiable view on the GL programs
     * 
     * @return The GL programs
     */
    Collection<Integer> getGlPrograms()
    {
        return Collections.unmodifiableCollection(
            programIdToGlProgram.values());
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
        String samplerId = texture.getSampler();
        Sampler sampler = gltf.getSamplers().get(samplerId);

        String textureImageId = texture.getSource();
        ByteBuffer imageData = gltfData.getImageData(textureImageId);
        
        int internalFormat = Optionals.of(
            texture.getInternalFormat(), texture.defaultInternalFormat());

        // The image data is read with the built-in routines, and
        // always returned as RBGA pixels. So the format and type
        // from the texture are ignored here.
        //int format = Optional
        //    .ofNullable(texture.getFormat())
        //    .orElse(texture.defaultFormat());
        //int type = Optional
        //    .ofNullable(texture.getType())
        //    .orElse(texture.defaultType());

        // Use the fixed format and type for the RGBA pixels
        int format = GltfConstants.GL_RGBA;
        int type = GltfConstants.GL_UNSIGNED_BYTE;
        
        PixelData pixelData = PixelDatas.create(imageData);
        int width = pixelData.getWidth();
        int height = pixelData.getHeight();
        ByteBuffer pixelsRGBA = pixelData.getPixelsRGBA();
        int glTexture = glContext.createGlTexture(
            pixelsRGBA, internalFormat, width, height, format, type);

        int minFilter = Optionals.of(
            sampler.getMinFilter(), sampler.defaultMinFilter());
        int magFilter = Optionals.of(
            sampler.getMagFilter(), sampler.defaultMagFilter());
        int wrapS = Optionals.of(
            sampler.getWrapS(), sampler.defaultWrapS());
        int wrapT = Optionals.of(
            sampler.getWrapT(), sampler.defaultWrapT());
        
        glContext.setGlTextureParameters(
            glTexture, minFilter, magFilter, wrapS, wrapT);
        
        logger.fine("Creating GL texture for texture " + textureId + " DONE");
        return glTexture;
    }

    /**
     * Returns an unmodifiable view on the GL textures
     * 
     * @return The GL textures
     */
    Collection<Integer> getGlTextures()
    {
        return Collections.unmodifiableCollection(
            textureIdToGlTexture.values());
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
        
        int target = Optionals.of(
            bufferView.getTarget(), GltfConstants.GL_ARRAY_BUFFER);
        
        int glBufferView = 
            glContext.createGlBufferView(
                target, byteLength, bufferViewData.slice());
        logger.fine("Creating GL bufferView for bufferView " + bufferViewId + 
            " DONE");
        return glBufferView;
    }

    /**
     * Returns an unmodifiable view on the GL buffer views
     * 
     * @return The GL buffer views
     */
    Collection<Integer> getGlBufferViews()
    {
        return Collections.unmodifiableCollection(
            bufferViewIdToGlBufferView.values());
    }

    
}
