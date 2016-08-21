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

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;

import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Logger;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import de.javagl.jgltf.viewer.AbstractGltfViewer;
import de.javagl.jgltf.viewer.GlContext;

/**
 * Implementation of a glTF viewer based on JOGL
 */
public class GltfViewerJogl extends AbstractGltfViewer
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GltfViewerJogl.class.getName());
    
    /**
     * The GLCanvas, i.e. the rendering component of this renderer
     */
    private final GLCanvas glComponent;
    
    /**
     * The event listener that will be attached to the GL component
     */
    private final GLEventListener glEventListener = new GLEventListener()
    {
        /**
         * Whether the {@link #init(GLAutoDrawable)} method was already called
         */
        private boolean initComplete;

        @Override
        public void init(GLAutoDrawable drawable)
        {
            initComplete = false;

            GL3 gl = (GL3)drawable.getGL();
            gl.setSwapInterval(0);
            initComplete = true;
        }
        
        
        @Override
        public void display(GLAutoDrawable drawable) 
        {
            if (!initComplete)
            {
                return;
            }
            doRender();
        }
        
        @Override
        public void reshape(
            GLAutoDrawable drawable, int x, int y, int width, int height)
        {
            GL3 gl = (GL3)drawable.getGL();
            gl.glViewport(0, 0, width, height);
        }

        @Override
        public void dispose(GLAutoDrawable arg0)
        {
            // Nothing to do here
        }
    };
    
    /**
     * The current JOGL GL context
     */
    private GL3 gl;
    
    /**
     * The {@link GlContext}
     */
    private final GlContextJogl glContext;
    
    /**
     * Creates a new GltfViewerJogl
     */
    public GltfViewerJogl()
    {
        GLProfile profile = GLProfile.getMaxProgrammable(true);
        logger.config("GLProfile: " + profile);
        
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);
        
        glComponent = new GLCanvas(capabilities);
        glComponent.addGLEventListener(glEventListener);
        
        // Without setting the minimum size, the canvas cannot 
        // be resized when it is embedded in a JSplitPane
        glComponent.setMinimumSize(new Dimension(10, 10));
        
        glContext = new GlContextJogl();
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
        //gl = new TraceGL3(glComponent.getGL().getGL3(), System.out);
        gl = glComponent.getGL().getGL3();
        glContext.setGL(gl);
    }
    
    @Override
    protected void render()
    {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        // TODO: These should actually be set based on the values
        // that are found in the glTF.techniques.states
        gl.glEnable(GL_DEPTH_TEST);
        renderGltfs();
    }

    

}
