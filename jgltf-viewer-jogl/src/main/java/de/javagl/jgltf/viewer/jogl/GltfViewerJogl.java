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

import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Logger;

import com.jogamp.opengl.GL;
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
public class GltfViewerJogl extends AbstractGltfViewer<Component>
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
            
            GL glBase = drawable.getGL();
            glBase.setSwapInterval(0);
            
            if (!(glBase instanceof GL3))
            {
                logger.severe("Could not obtain a GL3 instance");
                logger.severe(createGlContextDebugString(glBase));
                return;
            }
            
            initComplete = true;
        }
        
        /**
         * Create a string with debug information about the given GL instance
         * 
         * @param glBase The GL instance
         * @return The string
         */
        private String createGlContextDebugString(GL glBase)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("GLProfile: " + getGLProfile()).append("\n");
            
            sb.append("Availability:").append("\n");
            for (String profile : GLProfile.GL_PROFILE_LIST_ALL)
            {
                sb.append("  " + profile + " : " + 
                    GLProfile.isAvailable(profile)).append("\n");
            }
            sb.append("Context information:\n" + 
                glBase.getContext().toString()).append("\n");
            return sb.toString();
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
        GLProfile profile = getGLProfile();
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
    
    /**
     * Returns the GLProfile that should be used for creating the GL context
     * 
     * @return The GLProfile
     */
    private GLProfile getGLProfile()
    {
        return GLProfile.getMaxProgrammable(true);        
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
    public int getWidth()
    {
        return glComponent.getWidth();
    }
    
    @Override
    public int getHeight()
    {
        return glComponent.getHeight();
    }
    
    @Override
    public void triggerRendering()
    {
        if (getRenderComponent() != null)
        {
            getRenderComponent().repaint();
        }
    }

    @Override
    protected void prepareRender()
    {
        //gl = new TraceGL3(glComponent.getGL().getGL3(), System.out);
        
        // The check whether this cast is valid was 
        // done during the initialization:
        gl = (GL3)glComponent.getGL();
        glContext.setGL(gl);
    }
    
    @Override
    protected void render()
    {
        // Enable the color and depth mask explicitly before calling glClear.
        // When they are not enabled, they will not be cleared!
        gl.glColorMask(true, true, true, true);
        gl.glDepthMask(true); 
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        renderGltfModels();
    }

    

}
