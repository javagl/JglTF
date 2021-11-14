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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;

/**
 * Utility methods and classes related to morphing.
 */
class Morphing
{
    // TODO This is a first working implementation. It can (and should) be
    // improved in many ways. For example: There are very few error checks...
    
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(Morphing.class.getName());
    
    /**
     * A class representing an attribute to which morphing can be applied.
     */
    static class MorphableAttribute
    {
        /**
         * A newly created {@link AccessorModel} containing the morphed data
         */
        private final AccessorModel morphedAccessorModel;
        
        /**
         * The original {@link AccessorModel} that represents the original
         * values of this attribute, without morphing. 
         */
        private final AccessorModel baseAccessorModel;
        
        /**
         * The accessor data of the morph targets 
         */
        private final List<AccessorFloatData> targetAccessorFloatDatas;

        /**
         * Creates a new isntance
         * 
         * @param morphedAccessorModel A newly created {@link AccessorModel}
         * (with a newly created  {@link BufferViewModel} and 
         * {@link BufferModel}) that exactly represents the morphed attribute 
         * data. The contents of this model will be overwritten by this 
         * instance, when {@link #updateMorphedAccessorData(float[])} is
         * called
         * @param baseAccessorModel The base {@link AccessorModel} for the 
         * attribute
         * @param targetAccessorFloatDatas The {@link AccessorFloatData} 
         * elements that have been obtained from the morph target accessors
         */
        private MorphableAttribute(AccessorModel morphedAccessorModel,
            AccessorModel baseAccessorModel,
            Collection<? extends AccessorFloatData> targetAccessorFloatDatas)
        {
            this.baseAccessorModel = baseAccessorModel;
            this.morphedAccessorModel = morphedAccessorModel;
            this.targetAccessorFloatDatas = 
                Collections.unmodifiableList(
                    new ArrayList<AccessorFloatData>(targetAccessorFloatDatas));
        }

        /**
         * Returns the {@link AccessorModel} that contains the morphed data.
         * This {@link AccessorModel} will be used for rendering. It refers
         * to a {@link BufferViewModel} and a {@link BufferModel} that contain
         * exactly the morphed data. This model (and thus, the underlying 
         * buffer data) will be updated when 
         * {@link #updateMorphedAccessorData(float[])} is called.
         * 
         * @return The {@link AccessorModel} containing the morphed data.
         */
        AccessorModel getMorphedAccessorModel()
        {
            return morphedAccessorModel;
        }
        
        /**
         * Returns the number of morph targets for this attribute
         * 
         * @return The number of morph targets
         */
        int getNumTargets()
        {
            return targetAccessorFloatDatas.size();
        }
        
        /**
         * Update the data of the {@link #getMorphedAccessorModel() morphed
         * accessor model} based on the given weights.
         * 
         * @param weights The morph target weights.
         */
        void updateMorphedAccessorData(float weights[])
        {
            AccessorFloatData morphedAccessorData = 
                (AccessorFloatData) morphedAccessorModel.getAccessorData();
            AccessorFloatData baseAccessorData = 
                (AccessorFloatData) baseAccessorModel.getAccessorData();
            
            combine3D(
                morphedAccessorData, 
                baseAccessorData, 
                targetAccessorFloatDatas, 
                weights);
        }
    }
    /**
     * Returns whether the attribute with the given semantic in the given
     * {@link MeshPrimitiveModel} is "morphable". <br>
     * <br>
     * This is supposed to do the basic tests that are necessary in order
     * to call {@link #createMorphableAttribute} with the same parameters
     * and obtain a valid result. <br>
     * <br>
     * If the underlying data is supposed to contain morph data, but the
     * data is invalid in any way, an error message might be printed.
     * Further details are not specified.<br>
     * 
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     * @param semantic The semantic string (e.g. <code>"POSITION"</code>)
     * @return Whether the attribute is morphable.
     */
    static boolean isMorphableAttribute(
        MeshPrimitiveModel meshPrimitiveModel, String semantic)
    {
        // Check if the attribute exists at all
        Map<String, AccessorModel> meshPrimitiveAttributes =
            meshPrimitiveModel.getAttributes();
        AccessorModel accessorModel = meshPrimitiveAttributes.get(semantic);
        if (accessorModel == null)
        {
            return false;
        }
        
        // Check if there are morph targets for the attribute,
        // and whether each morph target has float components
        boolean hasMorphTargetsForSemantic = false;
        List<Map<String, AccessorModel>> morphTargets =
            meshPrimitiveModel.getTargets();
        for (Map<String, AccessorModel> morphTarget : morphTargets)
        {
            AccessorModel targetAccessorModel = morphTarget.get(semantic);
            if (targetAccessorModel != null)
            {
                hasMorphTargetsForSemantic = true;
                if (targetAccessorModel.getComponentDataType() != float.class)
                {
                    logger.severe("Morph target accessor for " + semantic 
                        + "does not have float component type, but "
                        + targetAccessorModel.getComponentDataType());
                    return false;
                }
            }
        }
        
        // Check if the attribute has float components. (This is done
        // last, to prevent printing the error message when it is 
        // not necessary)
        if (hasMorphTargetsForSemantic)
        {
            if (accessorModel.getComponentDataType() != float.class)
            {
                logger.severe("Accessor for " + semantic 
                    +" does not have float component type, but "
                    + accessorModel.getComponentDataType());
                return false;
            }
        }
        
        return hasMorphTargetsForSemantic;
    }
    
    /**
     * Create a {@link MorphableAttribute} for the specified attribute of
     * the given {@link MeshPrimitiveModel}.<br>
     * <br>
     * This method and the returned {@link MorphableAttribute} make several
     * assumptions about the structure and types of the data. Most of these
     * assumptions are covered by checking whether a certain attribute is
     * a morphable attribute, by calling 
     * {@link #isMorphableAttribute(MeshPrimitiveModel, String)}
     * 
     * @param meshPrimitiveModel The {@link MeshPrimitiveModel}
     * @param semantic The semantic string (e.g. <code>"POSITION"</code>) 
     * @return The {@link MorphableAttribute}
     */
    static MorphableAttribute createMorphableAttribute(
        MeshPrimitiveModel meshPrimitiveModel, String semantic)
    {
        Map<String, AccessorModel> meshPrimitiveAttributes =
            meshPrimitiveModel.getAttributes();
        AccessorModel baseAccessorModel = meshPrimitiveAttributes.get(semantic);
        
        List<AccessorFloatData> targetAccessorFloatDatas = 
            new ArrayList<AccessorFloatData>();
        List<Map<String, AccessorModel>> morphTargets =
            meshPrimitiveModel.getTargets();
        for (Map<String, AccessorModel> morphTarget : morphTargets)
        {
            AccessorModel targetAccessorModel = morphTarget.get(semantic);
            AccessorData targetAccessorData = 
                targetAccessorModel.getAccessorData();
            AccessorFloatData targetAccessorFloatData = 
                (AccessorFloatData)targetAccessorData;
            targetAccessorFloatDatas.add(targetAccessorFloatData);
        }
        
        AccessorModel instantiatedAccessorModel =
            AccessorModelCreation.instantiate(baseAccessorModel, 
                "buffer_for_morphed_attribute_" + semantic + ".bin");
        return new MorphableAttribute(instantiatedAccessorModel,
            baseAccessorModel, targetAccessorFloatDatas);
    }
    
    /**
     * Fill the given morphed accessor data with the data that is derived 
     * from the given base accessor data, and the targets and the weights.
     * The computation will be
     * <pre><code>
     *  morphed = base + targets[i] * weights[i]  (for all i)
     * </code></pre>
     * assuming that the elements of the accessors are at least 3D vectors. 
     * 
     * @param morphed The morphed accessor data
     * @param base The base accessor data
     * @param targets The targets accessor data
     * @param weights The weights
     */
    private static void combine3D(
        AccessorFloatData morphed, AccessorFloatData base,
        List<? extends AccessorFloatData> targets, float weights[])
    {
        // The number of components is fixed to be 3 here: Accessors that 
        // refer to tangents may have 4 components, but the values that 
        // are stored in the morph targets only have 3 components. The 
        // fourth component of tangents indicates the handedness, 
        // which cannot be morphed.
        int numComponents = 3;
        int numElements = morphed.getNumElements();
        for (int e = 0; e < numElements; e++)
        {
            for (int c = 0; c < numComponents; c++)
            {
                float r = base.get(e, c);
                for (int i = 0; i < weights.length; i++)
                {
                    float w = weights[i];
                    AccessorFloatData target = targets.get(i);
                    float d = target.get(e, c);
                    r += w * d;
                }
                morphed.set(e, c, r);
            }
        }
    }


    
    /**
     * Private constructor to prevent instantiation
     */
    private Morphing()
    {
        // Private constructor to prevent instantiation
    }
}
