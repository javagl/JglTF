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

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.Camera;
import de.javagl.jgltf.impl.TechniqueParameters;
import de.javagl.jgltf.model.GltfAnimations;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.animation.AnimationManager;
import de.javagl.jgltf.model.animation.AnimationManager.AnimationPolicy;
import de.javagl.jgltf.model.animation.AnimationRunner;
import de.javagl.jgltf.validator.Validator;

/**
 * Abstract base implementation of a {@link GltfViewer}
 */
public abstract class AbstractGltfViewer implements GltfViewer
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(AbstractGltfViewer.class.getName());
    
    /**
     * A supplier of the viewport size. This will be passed to the
     * {@link GltfModel} constructor, and eventually provide the data for the 
     * uniforms that have {@link TechniqueParameters#getSemantic()
     * semantic} <code>VIEWPORT</code>
     */
    private final Supplier<float[]> viewportSupplier = new Supplier<float[]>()
    {
        private final float viewport[] = new float[4];

        @Override
        public float[] get()
        {
            Component c = getRenderComponent();
            viewport[0] = 0;
            viewport[1] = 0;
            viewport[2] = c.getWidth();
            viewport[3] = c.getHeight();
            return viewport;
        }
    };
    
    /**
     * A supplier for the aspect ratio. This will be passed to the 
     * {@link GltfModel} constructor, and provide the aspect ratio
     * of the rendering window. (If this was <code>null</code>,
     * then the aspect ratio of the glTF {@link Camera} would be
     * used, but this would hardly ever match the actual aspect
     * ratio of the rendering component...)
     */
    private final DoubleSupplier aspectRatioSupplier = () -> 
    {
        Component c = getRenderComponent();
        return (double)c.getWidth() / c.getHeight();
    };
    
    /**
     * An optional {@link ExternalCamera}. 
     * See {@link #setExternalCamera(ExternalCamera)} for details.
     */
    private ExternalCamera externalCamera;
    
    /**
     * Tasks that have to be executed before the next rendering pass,
     * on the rendering thread
     */
    private final List<Runnable> beforeRenderTasks;
    
    /**
     * The map from {@link GltfData} instances to their {@link RenderedGltf}
     * counterparts
     */
    private final Map<GltfData, RenderedGltf> renderedGltfs;
    
    /**
     * The {@link AnimationManager}
     */
    private AnimationManager animationManager;
    
    /**
     * The {@link AnimationRunner}
     */
    private AnimationRunner animationRunner;
    
    /**
     * Default constructor
     */
    protected AbstractGltfViewer()
    {
        this.beforeRenderTasks = Collections.synchronizedList(
            new ArrayList<Runnable>());
        this.renderedGltfs = new LinkedHashMap<GltfData, RenderedGltf>();
        
        this.animationManager = 
            GltfAnimations.createAnimationManager(AnimationPolicy.LOOP);
        this.animationManager.addAnimationManagerListener(a ->
        {
            triggerRendering();
        });
        this.animationRunner = new AnimationRunner(animationManager);
        
        setAnimationsRunning(true);
    }
    
    @Override
    public final void setExternalCamera(ExternalCamera externalCamera)
    {
        this.externalCamera = externalCamera;
    }
    
    @Override
    public final void setAnimationsRunning(boolean running)
    {
        if (running)
        {
            animationRunner.start();
        }
        else
        {
            animationRunner.stop();
        }
    }
    
    @Override
    public abstract Component getRenderComponent();
    
    /**
     * Returns the {@link GlContext} of this viewer
     * 
     * @return The {@link GlContext} of this viewer
     */
    protected abstract GlContext getGlContext();
    
    @Override
    public final void triggerRendering()
    {
        if (getRenderComponent() != null)
        {
            getRenderComponent().repaint();
        }
    }
    
    @Override
    public final void addGltfData(GltfData gltfData)
    {
        Objects.requireNonNull(gltfData, "The gltfData may not be null");
        
        Validator validator = new Validator(gltfData.getGltf());
        boolean isValid = validator.isValid();
        if (!isValid)
        {
            logger.warning("The glTF is not valid");
            return;
        }
        
        addBeforeRenderTask(() -> createRenderedGltf(gltfData));
        triggerRendering();
    }
    
    /**
     * Create a {@link RenderedGltf} for the given {@link GltfData}, and
     * store it in the {@link #renderedGltfs} map
     * 
     * @param gltfData The {@link GltfData}
     */
    private void createRenderedGltf(GltfData gltfData)
    {
        Supplier<float[]> viewMatrixSupplier = null;
        Supplier<float[]> projectionMatrixSupplier = null;
        if (externalCamera != null)
        {
            viewMatrixSupplier = () -> 
                externalCamera.getViewMatrix();
            projectionMatrixSupplier = () ->
                externalCamera.getProjectionMatrix();
        }
            
        GltfModel gltfModel = new GltfModel(
            gltfData, null, viewportSupplier, aspectRatioSupplier);
        RenderedGltf renderedGltf = new RenderedGltf(
            gltfModel, getGlContext(), 
            viewMatrixSupplier, 
            projectionMatrixSupplier);
        renderedGltfs.put(gltfData, renderedGltf);
        
        GltfAnimations.addAnimations(animationManager, gltfData);
    }
    
    /**
     * Add a task to be executed once, before the next rendering pass,
     * on the rendering thread
     * 
     * @param beforeRenderTask The task to be executed
     */
    private void addBeforeRenderTask(Runnable beforeRenderTask)
    {
        beforeRenderTasks.add(beforeRenderTask);
    }
    
    /**
     * The method that may be called by implementations to execute the
     * actual rendering pass. It will call {@link #prepareRender()}
     * (then execute all tasks that have been scheduled internally,
     * for execution on the rendering thread), and then call 
     * {@link #render()}.
     */
    protected final void doRender()
    {
        prepareRender();
        beforeRender();
        render();
    }

    /**
     * Will be called at the beginning of each rendering pass. May be 
     * used to do basic setup, e.g. to make the required GL context
     * current.
     */
    protected abstract void prepareRender();
    
    /**
     * Will be called between {@link #prepareRender()} and {@link #render()},
     * and process all {@link #beforeRenderTasks}
     */
    private void beforeRender()
    {
        synchronized (beforeRenderTasks)
        {
            while (beforeRenderTasks.size() > 0)
            {
                Runnable beforeRenderTask = beforeRenderTasks.get(0);
                beforeRenderTask.run();
                beforeRenderTasks.remove(0);
            }
        }
    }
    
    /**
     * The actual rendering method. Subclasses implementing this method
     * will usually call {@link #renderGltfs()}.
     */
    protected abstract void render();

    /**
     * Render all glTFs that have been added via {@link #addGltfData(GltfData)}
     */
    protected final void renderGltfs()
    {
        for (RenderedGltf renderedGltf : renderedGltfs.values())
        {
            renderedGltf.render();
        }
    }


    

}
