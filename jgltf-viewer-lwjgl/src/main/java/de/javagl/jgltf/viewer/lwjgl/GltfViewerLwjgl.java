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

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Canvas;
import java.awt.Component;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

import de.javagl.jgltf.viewer.AbstractGltfViewer;
import de.javagl.jgltf.viewer.GlContext;

/**
 * Implementation of a glTF viewer based on LWJGL
 */
public class GltfViewerLwjgl extends AbstractGltfViewer
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GltfViewerLwjgl.class.getName());
    
    /**
     * The AWTGLCanvas, i.e. the rendering component of this renderer
     */
    private Component glComponent;
    
    /**
     * The {@link GlContext}
     */
    private final GlContextLwjgl glContext;
    
    /**
     * Creates a new GltfViewerJogl
     */
    public GltfViewerLwjgl()
    {
        try
        {
            this.glComponent = new AWTGLCanvas()
            {
                /**
                 * Serial UID
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void paintGL()
                {
                    doRender();
                    try
                    {
                        swapBuffers();
                    }
                    catch (LWJGLException e)
                    {
                        logger.severe("Could not swap buffers");
                    }
                }
                
            };
        }
        catch (LWJGLException e)
        {
            logger.severe("Could not create AWTGLCanvas");
            this.glComponent = new Canvas();
        }
        
        this.glContext = new GlContextLwjgl();
    }
    
    @Override
    public GlContext getGlContext()
    {
        return glContext;
    }
    
    @Override
    public Component getRenderComponent()
    {
        return glComponent;
    }
    
    @Override
    protected void prepareRender()
    {
        // Nothing to do here
    }
    
    @Override
    protected void render()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        // TODO: These should actually be set based on the values
        // that are found in the glTF.techniques.states
        glEnable(GL_DEPTH_TEST);
        renderGltfs();
    }

    

}
