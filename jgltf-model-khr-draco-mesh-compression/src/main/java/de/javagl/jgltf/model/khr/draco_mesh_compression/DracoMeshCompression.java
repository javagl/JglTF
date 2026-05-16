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
package de.javagl.jgltf.model.khr.draco_mesh_compression;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.impl.DefaultMeshPrimitiveModel;

/**
 * Utility methods related to the <code>KHR_draco_mesh_compression</code>
 * extension.
 */
public class DracoMeshCompression
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(DracoMeshCompression.class.getName());

    /**
     * Apply the <code>KHR_draco_mesh_compression</code> to all mesh primitives
     * of the given glTF model, with unspecified default compression options.
     * 
     * This will apply draco compression to all attributes of all mesh primitive
     * (if Draco can be applied to them)
     * 
     * @param gltfModel The glTF model
     */
    public static void apply(GltfModel gltfModel)
    {
        apply(gltfModel, new DracoOptions());
    }
    
    /**
     * Apply the <code>KHR_draco_mesh_compression</code> to all mesh primitives
     * of the given glTF model.
     * 
     * This will apply draco compression to all attributes of all mesh primitive
     * (if Draco can be applied to them)
     * 
     * @param gltfModel The glTF model
     * @param dracoOptions The draco options
     */
    public static void apply(GltfModel gltfModel, DracoOptions dracoOptions)
    {
        List<MeshModel> meshModels = gltfModel.getMeshModels();
        for (MeshModel meshModel : meshModels)
        {
            List<MeshPrimitiveModel> meshPrimitiveModels =
                meshModel.getMeshPrimitiveModels();
            for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitiveModels)
            {
                if (!(meshPrimitiveModel instanceof DefaultMeshPrimitiveModel))
                {
                    logger.warning(
                        "Expected DefaultMeshPrimitiveModel, but found "
                            + meshPrimitiveModel.getClass());
                    continue;
                }
                DefaultMeshPrimitiveModel defaultMeshPrimitiveModel =
                    (DefaultMeshPrimitiveModel) meshPrimitiveModel;
                apply(defaultMeshPrimitiveModel, dracoOptions);
            }
        }
    }

    /**
     * Apply the <code>KHR_draco_mesh_compression</code> to the given mesh
     * primitive, using the given options.
     * 
     * This will apply draco compression to all attributes of the given mesh
     * primitive (if Draco can be applied to them)
     * 
     * @param defaultMeshPrimitiveModel The mesh primitive model
     * @param dracoOptions The draco options
     */
    private static void apply(
        DefaultMeshPrimitiveModel defaultMeshPrimitiveModel,
        DracoOptions dracoOptions)
    {
        DefaultDracoMeshCompressionModel dracoMeshCompressionModel =
            new DefaultDracoMeshCompressionModel();
        dracoMeshCompressionModel
            .setDracoOptions(DracoOptions.copy(dracoOptions));
        Map<String, AccessorModel> attributes =
            defaultMeshPrimitiveModel.getAttributes();
        for (String attributeName : attributes.keySet())
        {
            dracoMeshCompressionModel.addAttribute(attributeName);
        }
        defaultMeshPrimitiveModel.addExtensionModel(
            "KHR_draco_mesh_compression", dracoMeshCompressionModel);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private DracoMeshCompression()
    {
        // Private constructor to prevent instantiation
    }
}
