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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.gl.ProgramModel;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.image.PixelData;
import de.javagl.jgltf.model.image.PixelDatas;

/**
 * A class maintaining the data for rendering a glTF with OpenGL.<br>
 * <br>
 * The class offers methods to obtain the GL representations of
 * {@link ProgramModel}, {@link TextureModel} and {@link BufferViewModel} 
 * objects. These GL representations are the integer values of the 
 * corresponding GL objects, as generated with <code>glCreateProgram</code>, 
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
     * The mapping from glTF {@link ProgramModel} keys to GL programs
     */
    private final Map<ProgramModel, Integer> programModelToGlProgram;

    /**
     * The mapping from glTF {@link TextureModel} keys to GL textures
     */
    private final Map<TextureModel, Integer> textureModelToGlTexture;

    /**
     * The mapping from glTF {@link BufferViewModel} keys to GL buffers
     */
    private final Map<BufferViewModel, Integer> bufferViewModelToGlBufferView;
    
    /**
     * The {@link GlContext} that will create the GL objects
     */
    private final GlContext glContext;
    
    /**
     * Create the render data that operates on the given  {@link GlContext}
     * 
     * @param glContext The {@link GlContext}
     */
    GltfRenderData(GlContext glContext)
    {
        this.glContext = Objects.requireNonNull(glContext,
            "The glContext may not be null");
        
        this.glVertexArrays = new ArrayList<Integer>();
        this.programModelToGlProgram = 
            new LinkedHashMap<ProgramModel, Integer>();
        this.textureModelToGlTexture = 
            new LinkedHashMap<TextureModel, Integer>();
        this.bufferViewModelToGlBufferView = 
            new LinkedHashMap<BufferViewModel, Integer>();
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
     * Obtain the OpenGL program for the given glTF {@link ProgramModel}.<br>
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
     * @param programModel The {@link ProgramModel}
     * @return The GL program
     */
    Integer obtainGlProgram(ProgramModel programModel)
    {
        Integer glProgram = programModelToGlProgram.get(programModel);
        if (glProgram == null)
        {
            if (!programModelToGlProgram.containsKey(programModel))
            {
                glProgram = createGlProgram(programModel);
                programModelToGlProgram.put(
                    programModel, glProgram);
            }
        }
        return glProgram;
    }
    
    /**
     * Internal method to create the GL program for the given 
     * {@link ProgramModel}. If the GL program can not be created for any 
     * reason, then a warning will be printed and <code>null</code> will 
     * be returned.
     * 
     * @param programModel The {@link ProgramModel}
     * @return The GL program
     */
    private Integer createGlProgram(ProgramModel programModel)
    {
        logger.fine("Creating GL program for " + programModel);
        
        ShaderModel vertexShaderModel = 
            programModel.getVertexShaderModel();
        ShaderModel fragmentShaderModel = 
            programModel.getFragmentShaderModel();
        Integer glProgram = glContext.createGlProgram(
            vertexShaderModel.getShaderSource(),
            fragmentShaderModel.getShaderSource());
        if (glProgram != null)
        {
            logger.fine("Creating GL program for " + programModel + " DONE");
        }
        else
        {
            logger.warning("Creating GL program for " 
                + programModel + " FAILED");
        }
        return glProgram;
    }
    
    /**
     * Obtain the OpenGL texture for the given glTF {@link TextureModel}.<br>
     * <br>
     * If the OpenGL texture for the specified texture has already been 
     * requested, then the previously created GL texture will be returned.
     * Otherwise, the GL texture will be created, stored for later 
     * retrieval, and returned.<br>
     * <br>
     * If the GL texture can not be created (e.g. due to missing or invalid
     * image data), then a warning will be printed and an unspecified 
     * placeholder texture will be returned.
     *  
     * @param textureModel The {@link TextureModel}
     * @return The GL texture
     */
    Integer obtainGlTexture(TextureModel textureModel)
    {
        Integer glTexture = textureModelToGlTexture.get(textureModel);
        if (glTexture == null)
        {
            if (!textureModelToGlTexture.containsKey(textureModel))
            {
                glTexture = createGlTexture(textureModel);
                textureModelToGlTexture.put(
                    textureModel, glTexture);
            }
        }
        return glTexture;
    }
    
    /**
     * Internal method to create the GL texture for the given 
     * {@link TextureModel}. If the image data for the texture cannot
     * be read, then a warning will be printed, and an unspecified 
     * placeholder texture will be returned.
     * 
     * @param textureModel The {@link TextureModel} key
     * @return The GL texture
     */
    private Integer createGlTexture(TextureModel textureModel)
    {
        logger.fine("Creating GL texture for texture " + textureModel);
        
        int internalFormat = GltfConstants.GL_RGBA;
        int format = GltfConstants.GL_RGBA;
        int type = GltfConstants.GL_UNSIGNED_BYTE;
        
        ImageModel imageModel = textureModel.getImageModel();
        ByteBuffer imageData = imageModel.getImageData();
        PixelData pixelData = PixelDatas.create(imageData);
        if (pixelData == null)
        {
            logger.warning("Could not extract pixel data from image");
            pixelData = PixelDatas.createErrorPixelData();
        }
        int width = pixelData.getWidth();
        int height = pixelData.getHeight();
        ByteBuffer pixelsRGBA = pixelData.getPixelsRGBA();
        int glTexture = glContext.createGlTexture(
            pixelsRGBA, internalFormat, width, height, format, type);

        int minFilter = Optionals.of(
            textureModel.getMinFilter(), 
            GltfConstants.GL_NEAREST_MIPMAP_LINEAR);
        int magFilter = Optionals.of(
            textureModel.getMagFilter(),
            GltfConstants.GL_LINEAR);
        int wrapS = Optionals.of(
            textureModel.getWrapS(),
            GltfConstants.GL_REPEAT);
        int wrapT = Optionals.of(
            textureModel.getWrapT(),
            GltfConstants.GL_REPEAT);
        
        glContext.setGlTextureParameters(
            glTexture, minFilter, magFilter, wrapS, wrapT);
        
        logger.fine(
            "Creating GL texture for texture " + textureModel + " DONE");
        return glTexture;
    }

    /**
     * Obtain the OpenGL buffer for the given glTF {@link BufferViewModel}.<br>
     * <br>
     * If the OpenGL buffer for the specified bufferView has already been 
     * requested, then the previously created GL buffer will be returned.
     * Otherwise, the GL buffer will be created, stored for later 
     * retrieval, and returned.<br>
     * <br>
     * If the GL buffer can not be created (e.g. due to missing data), then 
     * a warning will be printed and <code>null</code> will be returned.
     *  
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The GL buffer
     */
    Integer obtainGlBufferView(BufferViewModel bufferViewModel)
    {
        Objects.requireNonNull(bufferViewModel, 
            "The bufferViewModel may not be null");
        
        Integer glBufferView = 
            bufferViewModelToGlBufferView.get(bufferViewModel);
        if (glBufferView == null)
        {
            if (!bufferViewModelToGlBufferView.containsKey(bufferViewModel))
            {
                glBufferView = createGlBufferView(bufferViewModel);
                bufferViewModelToGlBufferView.put(
                    bufferViewModel, glBufferView);
            }
        }
        return glBufferView;
    }
    
    /**
     * Internal method to create the GL buffer for the given 
     * {@link BufferViewModel}. If the GL buffer can not be created for 
     * any reason, then a warning will be printed and <code>null</code> 
     * will be returned.
     * 
     * @param bufferViewModel The {@link BufferViewModel}
     * @return The GL buffer
     */
    private Integer createGlBufferView(BufferViewModel bufferViewModel)
    {
        logger.fine("Creating GL bufferView for bufferView " + bufferViewModel);
        
        Integer byteLength = bufferViewModel.getByteLength();
        ByteBuffer bufferViewData = bufferViewModel.getBufferViewData();
        int target = Optionals.of(
            bufferViewModel.getTarget(), GltfConstants.GL_ARRAY_BUFFER);
        int glBufferView = glContext.createGlBufferView(
            target, byteLength, bufferViewData.slice());
        logger.fine("Creating GL bufferView for bufferView " 
                + bufferViewModel + " DONE");
        return glBufferView;
    }

    /**
     * Delete all GL resources that have been created internally.
     */
    void delete()
    {
        Collection<Integer> glTextures = 
            textureModelToGlTexture.values();
        for (int glTexture : glTextures)
        {
            glContext.deleteGlTexture(glTexture);
        }
        Collection<Integer> glBufferViews = 
            bufferViewModelToGlBufferView.values();
        for (int glBufferView : glBufferViews)
        {
            glContext.deleteGlBufferView(glBufferView);
        }
        Collection<Integer> glPrograms = 
            programModelToGlProgram.values();
        for (int glProgram : glPrograms)
        {
            glContext.deleteGlProgram(glProgram);
        }
        for (int glVertexArray : glVertexArrays)
        {
            glContext.deleteGlVertexArray(glVertexArray);
        }
    }
    
}
