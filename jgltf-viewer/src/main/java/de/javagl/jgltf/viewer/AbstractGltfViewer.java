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
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.animation.Animation;
import de.javagl.jgltf.model.animation.AnimationManager;
import de.javagl.jgltf.model.animation.AnimationManager.AnimationPolicy;
import de.javagl.jgltf.model.animation.AnimationRunner;

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
     * {@link RenderedGltfModel} constructor, and eventually provide the data 
     * for the uniforms that have the <code>VIEWPORT</code> semantic.
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
     * The {@link ViewConfiguration}
     */
    private final ViewConfiguration viewConfiguration;
    
    /**
     * Tasks that have to be executed before the next rendering pass,
     * on the rendering thread
     */
    private final List<Runnable> beforeRenderTasks;
    
    /**
     * The map from {@link GltfModel} instances to their 
     * {@link RenderedGltfModel} counterparts
     */
    private final Map<GltfModel, RenderedGltfModel> renderedGltfModels;
    
    /**
     * The map from {@link GltfModel} instances to all 
     * {@link RenderedCamera} instances that have been
     * created for them
     */
    private final Map<GltfModel, List<RenderedCamera>> modelRenderedCameras;
    
    /**
     * The list of {@link GltfModel} instances that have been added.
     */
    private final List<GltfModel> gltfModels;
    
    /**
     * The {@link AnimationManager}
     */
    private final AnimationManager animationManager;
    
    /**
     * The {@link AnimationRunner}
     */
    private final AnimationRunner animationRunner;
    
    /**
     * The map from {@link GltfModel} instances to the lists of 
     * model {@link Animation}s that have been created for the
     * glTF animations
     */
    private final Map<GltfModel, List<Animation>> modelAnimations;
    
    /**
     * Default constructor
     */
    protected AbstractGltfViewer()
    {
        this.viewConfiguration = 
            new ViewConfiguration(viewportSupplier);

        this.beforeRenderTasks = Collections.synchronizedList(
            new ArrayList<Runnable>());
        this.renderedGltfModels = 
            new LinkedHashMap<GltfModel, RenderedGltfModel>();
        this.modelRenderedCameras = 
            new LinkedHashMap<GltfModel, List<RenderedCamera>>();
        this.gltfModels = new ArrayList<GltfModel>();
        this.animationManager = 
            GltfAnimations.createAnimationManager(AnimationPolicy.LOOP);
        this.animationManager.addAnimationManagerListener(a ->
        {
            triggerRendering();
        });
        this.animationRunner = new AnimationRunner(animationManager);
        this.modelAnimations = new LinkedHashMap<GltfModel, List<Animation>>();
        
        setAnimationsRunning(true);
    }
    
    @Override
    public final void setRenderedCamera(RenderedCamera renderedCamera)
    {
        viewConfiguration.setRenderedCamera(renderedCamera);
        triggerRendering();
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
    public final void addGltfModel(GltfModel gltfModel)
    {
        Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
        gltfModels.add(gltfModel);
        addBeforeRenderTask(() -> createRenderedGltf(gltfModel));
        triggerRendering();
        
        List<RenderedCamera> renderedCameras = createRenderedCameras(gltfModel);
        modelRenderedCameras.put(gltfModel, renderedCameras);
        
        // If no camera has been defined, set the current camera
        // to be the first camera of the given model.
        if (viewConfiguration.getRenderedCamera() == null)
        {
            if (!renderedCameras.isEmpty())
            {
                RenderedCamera renderedCamera = renderedCameras.get(0);
                viewConfiguration.setRenderedCamera(renderedCamera);
            }
        }
    }
    
    /**
     * Create a {@link RenderedGltfModel} for the given {@link GltfModel}, and
     * store it in the {@link #renderedGltfModels} map
     * 
     * @param gltfModel The {@link GltfModel}
     */
    private void createRenderedGltf(GltfModel gltfModel)
    {
        logger.info("Creating rendered glTF");
        
        GlContext glContext = getGlContext();
        RenderedGltfModel renderedGltfModel = new DefaultRenderedGltfModel(
            glContext, gltfModel, viewConfiguration);
        renderedGltfModels.put(gltfModel, renderedGltfModel);
        
        List<Animation> currentModelAnimations = 
            GltfAnimations.createModelAnimations(
                gltfModel.getAnimationModels());
        modelAnimations.put(gltfModel, currentModelAnimations);
        animationManager.addAnimations(currentModelAnimations);
    }
    
    @Override
    public void removeGltfModel(GltfModel gltfModel)
    {
        Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
        gltfModels.remove(gltfModel);
        modelRenderedCameras.remove(gltfModel);
        addBeforeRenderTask(() -> deleteRenderedGltfModel(gltfModel));
        List<Animation> currentModelAnimations = modelAnimations.get(gltfModel);
        if (currentModelAnimations != null)
        {
            animationManager.removeAnimations(currentModelAnimations);
        }
        modelAnimations.remove(gltfModel);
        triggerRendering();
    }
    
    /**
     * Delete the {@link RenderedGltfModel} that is associated with the given
     * {@link GltfModel} 
     * 
     * @param gltfModel The {@link GltfModel}
     */
    private void deleteRenderedGltfModel(GltfModel gltfModel)
    {
        RenderedGltfModel renderedGltfModel = renderedGltfModels.get(gltfModel);
        if (renderedGltfModel == null)
        {
            logger.warning(
                "No renderedGltfModel found for gltfModel " + gltfModel);
            return;
        }

        logger.info("Deleting rendered glTF");
        
        renderedGltfModel.delete();
        renderedGltfModels.remove(gltfModel);
    }
    
    /**
     * Create all {@link RenderedCamera} instances that can be created from
     * the given model. This will create one {@link RenderedCamera} instance
     * for each node that refers to a camera.
     * 
     * @param gltfModel The {@link GltfModel}
     * @return The {@link RenderedCamera} instances
     */
    private List<RenderedCamera> createRenderedCameras(GltfModel gltfModel)
    {
        List<RenderedCamera> renderedCameras = new ArrayList<RenderedCamera>();
        List<NodeModel> nodeModels = gltfModel.getNodeModels();
        List<CameraModel> cameraModels = gltfModel.getCameraModels();
        for (int i = 0; i < nodeModels.size(); i++)
        {
            NodeModel nodeModel = nodeModels.get(i);
            CameraModel cameraModel = nodeModel.getCameraModel();
            if (cameraModel != null)
            {
                int cameraIndex = cameraModels.indexOf(cameraModel);
                String nodeName = Optionals.of(
                    nodeModel.getName(), "node " + i);
                String cameraName = Optionals.of(
                        cameraModel.getName(), "camera " + cameraIndex);
                String name = cameraName + " at " + nodeName;
                RenderedCamera renderedCamera = new DefaultRenderedCamera(name,
                    nodeModel, cameraModel, aspectRatioSupplier);
                renderedCameras.add(renderedCamera);
            }
        }
        return renderedCameras;
    }
    
    
    @Override
    public List<RenderedCamera> getRenderedCameras()
    {
        List<RenderedCamera> renderedCameras = new ArrayList<RenderedCamera>();
        for (GltfModel gltfModel : gltfModels)
        {
            renderedCameras.addAll(modelRenderedCameras.get(gltfModel));
        }
        return Collections.unmodifiableList(renderedCameras);
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
     * will usually call {@link #renderGltfModels()}.
     */
    protected abstract void render();

    /**
     * Render all glTF models that have been added via 
     * {@link #addGltfModel(GltfModel)}
     */
    protected final void renderGltfModels()
    {
        for (RenderedGltfModel renderedGltfModel : renderedGltfModels.values())
        {
            renderedGltfModel.render();
        }
    }


    

}
