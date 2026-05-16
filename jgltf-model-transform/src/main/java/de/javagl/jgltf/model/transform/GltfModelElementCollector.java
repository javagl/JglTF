/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.transform;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.gl.ProgramModel;
import de.javagl.jgltf.model.gl.ShaderModel;
import de.javagl.jgltf.model.gl.TechniqueModel;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.impl.DefaultAnimationModel;
import de.javagl.jgltf.model.impl.DefaultCameraModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.impl.DefaultMeshModel;
import de.javagl.jgltf.model.impl.DefaultNodeModel;
import de.javagl.jgltf.model.impl.DefaultSceneModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;

/**
 * Internal class for preparing a model for structural computations like
 * pruning.
 * 
 * Its only method is {@link #process(DefaultGltfModel)}, which updates the
 * collections of the glTF model with missing elements. This means that, for
 * example, the TextureModel objects that are reachable from the scene- or
 * animation models and that are not yet contained in the list of texture models
 * of the glTF will be added to this list.
 */
class GltfModelElementCollector
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(GltfModelElementCollector.class.getName());

    /**
     * The glTF model
     */
    private DefaultGltfModel gltfModel;

    /**
     * The processed model elements
     */
    private final Set<ModelElement> processed;

    /**
     * The set of {@link AnimationModel} objects
     */
    private final Set<AnimationModel> animationModelsSet;

    /**
     * The set of {@link CameraModel} objects
     */
    private final Set<CameraModel> cameraModelsSet;

    /**
     * The set of {@link ImageModel} objects
     */
    private final Set<ImageModel> imageModelsSet;

    /**
     * The set of {@link MaterialModel} objects
     */
    private final Set<MaterialModel> materialModelsSet;

    /**
     * The set of {@link MeshModel} objects
     */
    private final Set<MeshModel> meshModelsSet;

    /**
     * The set of {@link NodeModel} objects
     */
    private final Set<NodeModel> nodeModelsSet;

    /**
     * The set of {@link SceneModel} objects
     */
    private final Set<SceneModel> sceneModelsSet;

    /**
     * The set of {@link SkinModel} objects
     */
    private final Set<SkinModel> skinModelsSet;

    /**
     * The set of {@link TextureModel} objects
     */
    private final Set<TextureModel> textureModelsSet;

    /**
     * The set of {@link TechniqueModel} objects
     */
    private final Set<TechniqueModel> techniqueModelsSet;

    /**
     * The set of {@link ProgramModel} objects
     */
    private final Set<ProgramModel> programModelsSet;

    /**
     * The set of {@link ShaderModel} objects
     */
    private final Set<ShaderModel> shaderModelsSet;

    /**
     * A set of {@link AccessorModel} objects
     */
    private final Set<AccessorModel> accessorModelsSet;

    /**
     * Default constructor
     */
    GltfModelElementCollector()
    {
        this.processed = new LinkedHashSet<ModelElement>();

        this.animationModelsSet = new LinkedHashSet<AnimationModel>();
        this.cameraModelsSet = new LinkedHashSet<CameraModel>();
        this.imageModelsSet = new LinkedHashSet<ImageModel>();
        this.materialModelsSet = new LinkedHashSet<MaterialModel>();
        this.meshModelsSet = new LinkedHashSet<MeshModel>();
        this.nodeModelsSet = new LinkedHashSet<NodeModel>();
        this.sceneModelsSet = new LinkedHashSet<SceneModel>();
        this.skinModelsSet = new LinkedHashSet<SkinModel>();
        this.textureModelsSet = new LinkedHashSet<TextureModel>();
        this.techniqueModelsSet = new LinkedHashSet<TechniqueModel>();
        this.programModelsSet = new LinkedHashSet<ProgramModel>();
        this.shaderModelsSet = new LinkedHashSet<ShaderModel>();
        this.accessorModelsSet = new LinkedHashSet<AccessorModel>();
    }

    /**
     * Process the given glTF model
     * 
     * @param gltfModel The glTF model
     */
    void process(DefaultGltfModel gltfModel)
    {
        logger.info("Collecting elements in glTF model...");

        this.gltfModel = gltfModel;
        this.processed.clear();

        this.animationModelsSet.clear();
        this.cameraModelsSet.clear();
        this.imageModelsSet.clear();
        this.materialModelsSet.clear();
        this.meshModelsSet.clear();
        this.nodeModelsSet.clear();
        this.sceneModelsSet.clear();
        this.skinModelsSet.clear();
        this.textureModelsSet.clear();
        this.techniqueModelsSet.clear();
        this.programModelsSet.clear();
        this.shaderModelsSet.clear();
        this.accessorModelsSet.clear();

        this.animationModelsSet.addAll(gltfModel.getAnimationModels());
        this.cameraModelsSet.addAll(gltfModel.getCameraModels());
        this.imageModelsSet.addAll(gltfModel.getImageModels());
        this.materialModelsSet.addAll(gltfModel.getMaterialModels());
        this.meshModelsSet.addAll(gltfModel.getMeshModels());
        this.nodeModelsSet.addAll(gltfModel.getNodeModels());
        this.sceneModelsSet.addAll(gltfModel.getSceneModels());
        this.skinModelsSet.addAll(gltfModel.getSkinModels());
        this.textureModelsSet.addAll(gltfModel.getTextureModels());
        this.accessorModelsSet.addAll(gltfModel.getAccessorModels());

        addModelElement(gltfModel);
        
        logger.info("Collecting elements in glTF model DONE");
    }

    /**
     * Add the given model element
     * 
     * @param modelElement The {@link ModelElement}
     */
    private void addModelElement(ModelElement modelElement)
    {
        boolean added = processed.add(modelElement);
        if (!added)
        {
            return;
        }
        if (modelElement instanceof AnimationModel)
        {
            addMissingDefaultModelElement(modelElement, animationModelsSet,
                DefaultAnimationModel.class,
                DefaultGltfModel::addAnimationModel);
        }
        else if (modelElement instanceof CameraModel)
        {
            addMissingDefaultModelElement(modelElement, cameraModelsSet,
                DefaultCameraModel.class, DefaultGltfModel::addCameraModel);
        }
        else if (modelElement instanceof ImageModel)
        {
            addMissingDefaultModelElement(modelElement, imageModelsSet,
                DefaultImageModel.class, DefaultGltfModel::addImageModel);
        }
        else if (modelElement instanceof MaterialModel)
        {
            addMissingDefaultModelElement(modelElement, materialModelsSet,
                MaterialModel.class, DefaultGltfModel::addMaterialModel);
        }
        else if (modelElement instanceof MeshModel)
        {
            addMissingDefaultModelElement(modelElement, meshModelsSet,
                DefaultMeshModel.class, DefaultGltfModel::addMeshModel);
        }
        else if (modelElement instanceof NodeModel)
        {
            addMissingDefaultModelElement(modelElement, nodeModelsSet,
                DefaultNodeModel.class, DefaultGltfModel::addNodeModel);
        }
        else if (modelElement instanceof SceneModel)
        {
            addMissingDefaultModelElement(modelElement, sceneModelsSet,
                DefaultSceneModel.class, DefaultGltfModel::addSceneModel);
        }
        else if (modelElement instanceof SkinModel)
        {
            addMissingDefaultModelElement(modelElement, skinModelsSet,
                DefaultSkinModel.class, DefaultGltfModel::addSkinModel);
        }
        else if (modelElement instanceof TextureModel)
        {
            addMissingDefaultModelElement(modelElement, textureModelsSet,
                DefaultTextureModel.class, DefaultGltfModel::addTextureModel);
        }
        else if (modelElement instanceof TechniqueModel)
        {
            techniqueModelsSet.add((TechniqueModel) modelElement);
        }
        else if (modelElement instanceof ProgramModel)
        {
            programModelsSet.add((ProgramModel) modelElement);
        }
        else if (modelElement instanceof ShaderModel)
        {
            shaderModelsSet.add((ShaderModel) modelElement);
        }
        else if (modelElement instanceof AccessorModel)
        {
            addMissingDefaultModelElement(modelElement, accessorModelsSet,
                DefaultAccessorModel.class, DefaultGltfModel::addAccessorModel);
        }
        else if (modelElement instanceof BufferViewModel)
        {
            logger.fine("BufferViewModel: " + modelElement);
        }
        else if (modelElement instanceof BufferModel)
        {
            logger.fine("BufferModel: " + modelElement);
        }
        else
        {
            logger.fine("Generic model element: " + modelElement);
        }

        Set<ModelElement> referencedModelElements =
            modelElement.getReferencedModelElements();
        for (ModelElement referenced : referencedModelElements)
        {
            addModelElement(referenced);
        }
    }

    /**
     * Add the given element to the given set. If it was not yet contained in
     * this set, then it will also be added to the glTF model.
     * 
     * @param <B> The base type
     * @param <D> The default implementation type
     * 
     * @param modelElement The model element
     * @param set The set of elements
     * @param defaultModelType The default implementation type
     * @param adder The adder for the model
     */
    private <B, D extends B> void addMissingDefaultModelElement(
        ModelElement modelElement, Set<B> set,
        Class<? extends D> defaultModelType,
        BiConsumer<DefaultGltfModel, D> adder)
    {
        if (defaultModelType.isInstance(modelElement))
        {
            @SuppressWarnings("unchecked")
            D d = (D) modelElement;
            boolean added = set.add(d);
            if (added)
            {
                logger.info("Adding missing model: " + modelElement);
                adder.accept(gltfModel, d);
            }
        }
        else
        {
            logger.severe("Expected " + defaultModelType + ", but got "
                + modelElement.getClass());
        }
    }

}
