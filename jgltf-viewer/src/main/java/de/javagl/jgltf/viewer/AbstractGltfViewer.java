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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;

import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.GltfAnimations;
import de.javagl.jgltf.model.GltfData;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.animation.Animation;
import de.javagl.jgltf.model.animation.AnimationManager;
import de.javagl.jgltf.model.animation.AnimationManager.AnimationPolicy;
import de.javagl.jgltf.model.animation.AnimationRunner;
import de.javagl.jgltf.validator.Validator;

/**
 * Abstract base implementation of a {@link GltfViewer}
 * 
 * @param <C> The type of the render component of this viewer
 */
public abstract class AbstractGltfViewer<C> implements GltfViewer<C>
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(AbstractGltfViewer.class.getName());
    
    /**
     * A supplier of the viewport size. This will be passed to the
     * {@link GltfModel} constructor, and eventually provide the data for the 
     * uniforms that have the <code>VIEWPORT</code> semantic.
     */
    private final Supplier<float[]> viewportSupplier = new Supplier<float[]>()
    {
        private final float viewport[] = new float[4];

        @Override
        public float[] get()
        {
            viewport[0] = 0;
            viewport[1] = 0;
            viewport[2] = getWidth();
            viewport[3] = getHeight();
            return viewport;
        }
    };
    
    /**
     * A supplier for the aspect ratio. This will provide the aspect ratio
     * of the rendering window. (If this was <code>null</code>,
     * then the aspect ratio of the glTF camera would be
     * used, but this would hardly ever match the actual aspect
     * ratio of the rendering component...)
     */
    private final DoubleSupplier aspectRatioSupplier = () -> 
    {
        return (double)getWidth() / getHeight();
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
     * The map from {@link GltfData} instances to their {@link GltfModel}
     * counterparts
     */
    private final Map<GltfData, GltfModel> gltfModels;
    
    /**
     * The {@link AnimationManager}
     */
    private AnimationManager animationManager;
    
    /**
     * The {@link AnimationRunner}
     */
    private AnimationRunner animationRunner;
    
    /**
     * The map from {@link GltfData} instances to the lists of 
     * model {@link Animation}s that have been created for the
     * glTF animations
     */
    private final Map<GltfData, List<Animation>> modelAnimations;
    
    /**
     * Default constructor
     */
    protected AbstractGltfViewer()
    {
        this.beforeRenderTasks = Collections.synchronizedList(
            new ArrayList<Runnable>());
        this.renderedGltfs = new LinkedHashMap<GltfData, RenderedGltf>();
        this.gltfModels = new LinkedHashMap<GltfData, GltfModel>();
        this.animationManager = 
            GltfAnimations.createAnimationManager(AnimationPolicy.LOOP);
        this.animationManager.addAnimationManagerListener(a ->
        {
            triggerRendering();
        });
        this.animationRunner = new AnimationRunner(animationManager);
        this.modelAnimations = new LinkedHashMap<GltfData, List<Animation>>();
        
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
    public abstract C getRenderComponent();
    
    /**
     * Returns the {@link GlContext} of this viewer
     * 
     * @return The {@link GlContext} of this viewer
     */
    protected abstract GlContext getGlContext();
    
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
        
        GltfModel gltfModel = new GltfModel(gltfData.getGltf());
        gltfModels.put(gltfData, gltfModel);
        
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
        
        logger.info("Creating rendered glTF");
        
        GltfModel gltfModel = gltfModels.get(gltfData);
        RenderedGltf renderedGltf = new RenderedGltf(
            gltfModel, gltfData, getGlContext(), 
            viewportSupplier,
            aspectRatioSupplier,
            viewMatrixSupplier, 
            projectionMatrixSupplier);
        renderedGltfs.put(gltfData, renderedGltf);
        
        List<Animation> currentModelAnimations = 
            GltfAnimations.createModelAnimations(gltfData);
        modelAnimations.put(gltfData, currentModelAnimations);
        animationManager.addAnimations(currentModelAnimations);
    }
    
    @Override
    public void removeGltfData(GltfData gltfData)
    {
        Objects.requireNonNull(gltfData, "The gltfData may not be null");
        addBeforeRenderTask(() -> deleteRenderedGltf(gltfData));
        List<Animation> currentModelAnimations = modelAnimations.get(gltfData);
        if (currentModelAnimations != null)
        {
            animationManager.removeAnimations(currentModelAnimations);
        }
        modelAnimations.remove(gltfData);
        triggerRendering();
    }
    
    /**
     * Delete the {@link RenderedGltf} that is associated with the given
     * {@link GltfData} 
     * 
     * @param gltfData The {@link GltfData}
     */
    private void deleteRenderedGltf(GltfData gltfData)
    {
        RenderedGltf renderedGltf = renderedGltfs.get(gltfData);
        if (renderedGltf == null)
        {
            logger.warning("No renderedGltf found for gltfData");
            return;
        }

        logger.info("Deleting rendered glTF");
        
        gltfModels.remove(gltfData);
        renderedGltf.delete();
        renderedGltfs.remove(gltfData);
    }
    
    @Override
    public List<CameraModel> getCameraModels(GltfData gltfData)
    {
        List<CameraModel> cameraModels = new ArrayList<CameraModel>();
        if (gltfData == null)
        {
            for (GltfModel gltfModel : gltfModels.values())
            {
                cameraModels.addAll(gltfModel.getCameraModels());
            }
        }
        else
        {
            GltfModel gltfModel = gltfModels.get(gltfData);
            if (gltfModel == null)
            {
                throw new IllegalArgumentException(
                    "The given gltfData is not contained in this viewer");
            }
            cameraModels.addAll(gltfModel.getCameraModels());
        }
        return Collections.unmodifiableList(cameraModels);
    }
    
    @Override
    public void setCurrentCameraModel(
        GltfData gltfData, CameraModel cameraModel)
    {
        if (gltfData == null)
        {
            for (RenderedGltf renderedGltf : renderedGltfs.values())
            {
                renderedGltf.setCurrentCameraModel(cameraModel);
            }
        }
        else
        {
            RenderedGltf renderedGltf = renderedGltfs.get(gltfData);
            if (renderedGltf == null)
            {
                throw new IllegalArgumentException(
                    "The given gltfData is not contained in this viewer");
            }
            renderedGltf.setCurrentCameraModel(cameraModel);
        }
        triggerRendering();
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
