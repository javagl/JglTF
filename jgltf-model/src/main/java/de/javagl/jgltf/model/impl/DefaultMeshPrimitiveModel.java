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
package de.javagl.jgltf.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.ModelElement;
import de.javagl.jgltf.model.ModelElements;
import de.javagl.jgltf.model.TextureInfoModel;

/**
 * Implementation of a {@link MeshPrimitiveModel}
 */
public final class DefaultMeshPrimitiveModel extends AbstractModelElement
    implements MeshPrimitiveModel
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(DefaultMeshPrimitiveModel.class.getName());

    /**
     * The attributes of this mesh primitive model
     */
    private final Map<String, AccessorModel> attributes;

    /**
     * The {@link AccessorModel} for the indices data
     */
    private AccessorModel indices;

    /**
     * The {@link MaterialModel} that should be used for rendering
     */
    private MaterialModel materialModel;

    /**
     * The rendering mode
     */
    private final int mode;

    /**
     * The morph targets
     */
    private final List<Map<String, AccessorModel>> targets;

    /**
     * Creates a new instance
     * 
     * @param mode The rendering mode
     */
    public DefaultMeshPrimitiveModel(int mode)
    {
        this.mode = mode;
        this.attributes = new LinkedHashMap<String, AccessorModel>();
        this.targets = new ArrayList<Map<String, AccessorModel>>();
    }

    /**
     * Put the given {@link AccessorModel} into the attributes, under the given
     * name
     * 
     * @param name The name
     * @param accessorModel The {@link AccessorModel}
     * @return The old value that was stored under the given name
     */
    public AccessorModel putAttribute(String name, AccessorModel accessorModel)
    {
        Objects.requireNonNull(accessorModel,
            "The accessorModel may not be null");
        return attributes.put(name, accessorModel);
    }

    /**
     * Remove the specified {@link AccessorModel} from the attributes
     * 
     * @param name The name of the attribute
     * @return The removed {@link AccessorModel}, or <code>null</code>
     */
    public AccessorModel removeAttribute(String name)
    {
        return attributes.remove(name);
    }

    /**
     * Set the {@link AccessorModel} for the indices
     * 
     * @param indices The indices
     */
    public void setIndices(AccessorModel indices)
    {
        this.indices = indices;
    }

    /**
     * Set the {@link MaterialModel}
     * 
     * @param materialModel The {@link MaterialModel}
     */
    public void setMaterialModel(MaterialModel materialModel)
    {
        this.materialModel = materialModel;
    }

    /**
     * Add the given morph target. A reference to the given map will be stored.
     * 
     * @param target The target
     */
    public void addTarget(Map<String, AccessorModel> target)
    {
        Objects.requireNonNull(target, "The target may not be null");
        this.targets.add(target);
    }

    /**
     * Put the given {@link AccessorModel} into the specified morph target,
     * under the given name
     * 
     * @param index The morph target index
     * @param name The name
     * @param accessorModel The {@link AccessorModel}
     * @return The old value that was stored under the given name
     */
    public AccessorModel putTarget(int index, String name,
        AccessorModel accessorModel)
    {
        Objects.requireNonNull(accessorModel,
            "The accessorModel may not be null");
        Map<String, AccessorModel> target = this.targets.get(index);
        return target.put(name, accessorModel);
    }

    @Override
    public Map<String, AccessorModel> getAttributes()
    {
        return Collections.unmodifiableMap(attributes);
    }

    @Override
    public AccessorModel getIndices()
    {
        return indices;
    }

    @Override
    public int getMode()
    {
        return mode;
    }

    @Override
    public MaterialModel getMaterialModel()
    {
        return materialModel;
    }

    @Override
    public List<Map<String, AccessorModel>> getTargets()
    {
        return Collections.unmodifiableList(targets);
    }

    @Override
    public Set<ModelElement> getReferencedModelElements()
    {
        Set<ModelElement> modelElements = getReferencedExtensionModelElements();
        if (indices != null)
        {
            modelElements.add(indices);
        }
        for (AccessorModel accessorModel : attributes.values())
        {
            modelElements.add(accessorModel);
        }
        for (Map<String, AccessorModel> target : targets)
        {
            for (AccessorModel accessorModel : target.values())
            {
                modelElements.add(accessorModel);
            }
        }
        if (materialModel != null)
        {
            modelElements.add(materialModel);
        }
        return modelElements;
    }

    @Override
    public boolean removeModelElements(
        Collection<? extends ModelElement> modelElementsToRemove)
    {
        removeExtensionModelElements(modelElementsToRemove);
        boolean removeThis = false;
        if (modelElementsToRemove.contains(indices))
        {
            setIndices(null);
            removeThis = true;
        }

        Set<String> attributeKeysToRemove = new LinkedHashSet<String>();
        for (Entry<String, AccessorModel> entry : attributes.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (modelElementsToRemove.contains(value))
            {
                attributeKeysToRemove.add(key);
            }
        }
        for (String attributeKeyToRemove : attributeKeysToRemove)
        {
            attributes.remove(attributeKeyToRemove);
        }
        if (attributes.isEmpty())
        {
            removeThis = true;
        }

        if (materialModel != null)
        {
            if (modelElementsToRemove.contains(materialModel))
            {
                setMaterialModel(null);
            }
            else if (includesTextureCoordinates(attributeKeysToRemove))
            {
                if (referencesTextureCoordinateAttributeFrom(materialModel,
                    attributeKeysToRemove))
                {
                    logger.fine("Removal of texture coordinates attribute "
                        + "requires removal of material from mesh primitive");
                    setMaterialModel(null);
                }
            }
        }

        Set<Map<String, AccessorModel>> targetsToRemove =
            new LinkedHashSet<Map<String, AccessorModel>>();
        for (Map<String, AccessorModel> target : targets)
        {
            for (String attributeKeyToRemove : attributeKeysToRemove)
            {
                if (target.containsKey(attributeKeyToRemove))
                {
                    targetsToRemove.add(target);
                }
            }
            for (Entry<String, AccessorModel> entry : target.entrySet())
            {
                Object value = entry.getValue();
                if (modelElementsToRemove.contains(value))
                {
                    targetsToRemove.add(target);
                }
            }
        }
        targets.removeAll(targetsToRemove);
        return removeThis;
    }

    /**
     * Returns whether the given set contains an attribute that starts with
     * <code>TEXCOORD_</code>
     * 
     * @param attributeNames The attribute names
     * @return The result
     */
    private static boolean
        includesTextureCoordinates(Set<String> attributeNames)
    {
        for (String attributeNameToRemove : attributeNames)
        {
            if (attributeNameToRemove.startsWith("TEXCOORD_"))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the given material model refers to any
     * {@link TextureInfoModel} that needs a texture coordinate set whose name
     * is contained in the given set of attribute names.
     * 
     * @param materialModel The material model
     * @param attributeNames The attribute names
     * @return The result
     */
    private static boolean referencesTextureCoordinateAttributeFrom(
        MaterialModel materialModel, Collection<String> attributeNames)
    {
        Set<TextureInfoModel> textureInfoModels =
            ModelElements.collectReferencedModelElements(materialModel,
                TextureInfoModel.class);
        for (TextureInfoModel textureInfoModel : textureInfoModels)
        {
            Integer texCoord = textureInfoModel.getTexCoord();
            if (texCoord == null)
            {
                texCoord = 0;
            }
            String referencedTexCoord = "TEXCOORD_" + texCoord;
            if (attributeNames.contains(referencedTexCoord))
            {
                return true;
            }
        }
        return false;
    }

}
