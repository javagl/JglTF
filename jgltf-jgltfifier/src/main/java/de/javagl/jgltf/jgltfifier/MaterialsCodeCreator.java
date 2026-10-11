/*
 * www.javagl.de - JglTF
 *
 * Copyright 2023-2024 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.jgltfifier;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.NormalTextureInfoModel;
import de.javagl.jgltf.model.OcclusionTextureInfoModel;
import de.javagl.jgltf.model.PbrMaterialModel;
import de.javagl.jgltf.model.PbrMaterialModel.AlphaMode;
import de.javagl.jgltf.model.PbrMetallicRoughnessModel;
import de.javagl.jgltf.model.TextureInfoModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultNormalTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultOcclusionTextureInfoModel;
import de.javagl.jgltf.model.impl.DefaultPbrMaterialModel;
import de.javagl.jgltf.model.impl.DefaultPbrMetallicRoughnessModel;
import de.javagl.jgltf.model.impl.DefaultTextureInfoModel;

/**
 * A code creator for the materials code
 */
class MaterialsCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(MaterialsCodeCreator.class.getName());

    /**
     * The glTF model
     */
    private final GltfModel gltfModel;

    /**
     * Creates a new instance
     * 
     * @param codeModel The code model
     * @param definedClass The defined class
     * @param gltfModel The glTF model
     */
    MaterialsCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<MaterialModel> materialModels = gltfModel.getMaterialModels();
        if (materialModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Materials (" + materialModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < materialModels.size(); i++)
        {
            block.directStatement(
                "// Material " + i + " of " + materialModels.size());
            MaterialModel materialModel = materialModels.get(i);
            createMaterial(block, materialModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given material, and add it to the given
     * block
     * 
     * @param block The block
     * @param materialModel The material
     * @param materialIndex The index of the material
     */
    private void createMaterial(JBlock block, MaterialModel materialModel,
        int materialIndex)
    {
        JClass defaultPbrMaterialModelClass =
            findClass(DefaultPbrMaterialModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultPbrMaterialModelClass,
            "materialModel" + materialIndex);

        JMethod method =
            createMaterialCreationMethod(materialModel, materialIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given material model
     * 
     * @param materialModel The material model
     * @param materialIndex The material index
     * @return The method
     */
    private JMethod createMaterialCreationMethod(MaterialModel materialModel,
        int materialIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createMaterialModel" + materialIndex);
        Comments.add(method, "Create the specified material model");

        JBlock block = method.body();
        createMaterialCreationCode(block, materialModel, materialIndex);
        return method;
    }

    /**
     * Create the code that creates the given material model and add it to the
     * given block
     * 
     * @param block The block
     * @param materialModel The material model
     * @param materialIndex The material index
     */
    private void createMaterialCreationCode(JBlock block,
        MaterialModel materialModel, int materialIndex)
    {
        // Collect the required types
        JClass defaultPbrMaterialModelClass =
            findClass(DefaultPbrMaterialModel.class);
        JClass alphaModeClass = findClass(AlphaMode.class);

        // this.materialModelX = new DefaultPbrMaterialModel()
        JFieldRef materialVar =
            JExpr._this().ref("materialModel" + materialIndex);
        block.assign(materialVar, JExpr._new(defaultPbrMaterialModelClass));

        if (!(materialModel instanceof PbrMaterialModel))
        {
            logger.severe("Unhandled material model type: " + materialModel);
            return;
        }
        PbrMaterialModel pbrMaterialModel = (PbrMaterialModel) materialModel;

        // Call the required setters

        // Basic settings
        Boolean doubleSided = pbrMaterialModel.isDoubleSided();
        if (doubleSided != null)
        {
            block.add(materialVar.invoke("setDoubleSided")
                .arg(JExpr.lit(doubleSided)));
        }

        // Alpha settings
        Double alphaCutoff = pbrMaterialModel.getAlphaCutoff();
        if (alphaCutoff != null)
        {
            block.add(materialVar.invoke("setAlphaCutoff")
                .arg(JExpr.lit(alphaCutoff)));
        }

        AlphaMode alphaMode = pbrMaterialModel.getAlphaMode();
        if (alphaMode != null)
        {
            block.add(materialVar.invoke("setAlphaMode")
                .arg(alphaModeClass.staticRef(alphaMode.toString())));
        }

        // Metallic-roughness
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel =
            pbrMaterialModel.getPbrMetallicRoughnessModel();
        if (pbrMetallicRoughnessModel != null)
        {
            JVar pbrMetallicRoughnessModelVar =
                createPbrMetallicRoughnessCreationCode(block,
                    pbrMetallicRoughnessModel, materialIndex);
            block.add(materialVar.invoke("setPbrMetallicRoughnessModel")
                .arg(pbrMetallicRoughnessModelVar));
        }

        // Emissive
        double[] emissiveFactor = pbrMaterialModel.getEmissiveFactor();
        if (emissiveFactor != null)
        {
            block.add(materialVar.invoke("setEmissiveFactor")
                .arg(newDoubleArrayWith(emissiveFactor)));
        }
        TextureInfoModel emissiveTextureInfoModel =
            pbrMaterialModel.getEmissiveTexture();
        if (emissiveTextureInfoModel != null)
        {
            JVar textureInfoModelVar =
                createTextureInfoCreationCode(block, emissiveTextureInfoModel,
                    "emissiveTextureInfoModel", materialIndex);

            block.add(materialVar.invoke("setEmissiveTexture")
                .arg(textureInfoModelVar));
        }

        NormalTextureInfoModel normalTextureInfoModel =
            pbrMaterialModel.getNormalTexture();
        if (normalTextureInfoModel != null)
        {
            JVar textureInfoModelVar = createNormalTextureInfoCreationCode(
                block, normalTextureInfoModel, "normalTextureInfoModel",
                materialIndex);

            block.add(materialVar.invoke("setNormalTexture")
                .arg(textureInfoModelVar));
        }

        OcclusionTextureInfoModel occlusionTextureInfoModel =
            pbrMaterialModel.getOcclusionTexture();
        if (occlusionTextureInfoModel != null)
        {
            JVar textureInfoModelVar = createOcclusionTextureInfoCreationCode(
                block, occlusionTextureInfoModel, "occlusionTextureInfoModel",
                materialIndex);

            block.add(materialVar.invoke("setOcclusionTexture")
                .arg(textureInfoModelVar));
        }
    }

    /**
     * Create the code for creating the given PBR metallic roughness, and add it
     * to the given block
     * 
     * @param block The block
     * @param pbrMetallicRoughnessModel The model
     * @param materialIndex The index of the material
     * @return The pbrMetallicRoughnessModel variable
     */
    private JVar createPbrMetallicRoughnessCreationCode(JBlock block,
        PbrMetallicRoughnessModel pbrMetallicRoughnessModel, int materialIndex)
    {
        // Collect the required types
        JClass defaultPbrMetallicRoughnessModelClass =
            findClass(DefaultPbrMetallicRoughnessModel.class);

        // DefaultPbrMetallicRoughnessModel pbrMetallicRoughnessModelX =
        // new DefaultPbrMetallicRoughnessModel()
        JVar pbrMetallicRoughnessModelVar =
            block.decl(defaultPbrMetallicRoughnessModelClass,
                "pbrMetallicRoughnessModel" + materialIndex,
                JExpr._new(defaultPbrMetallicRoughnessModelClass));

        // Metallic-roughness
        Double metallicFactor = pbrMetallicRoughnessModel.getMetallicFactor();
        if (metallicFactor != null)
        {
            block.add(pbrMetallicRoughnessModelVar.invoke("setMetallicFactor")
                .arg(JExpr.lit(metallicFactor)));
        }

        Double roughnessFactor = pbrMetallicRoughnessModel.getRoughnessFactor();
        if (roughnessFactor != null)
        {
            block.add(pbrMetallicRoughnessModelVar.invoke("setRoughnessFactor")
                .arg(JExpr.lit(roughnessFactor)));
        }

        // Base color
        double[] baseColorFactor =
            pbrMetallicRoughnessModel.getBaseColorFactor();
        if (baseColorFactor != null)
        {
            block.add(pbrMetallicRoughnessModelVar.invoke("setBaseColorFactor")
                .arg(newDoubleArrayWith(baseColorFactor)));
        }
        TextureInfoModel baseColorTextureInfoModel =
            pbrMetallicRoughnessModel.getBaseColorTexture();
        if (baseColorTextureInfoModel != null)
        {
            JVar textureInfoModelVar =
                createTextureInfoCreationCode(block, baseColorTextureInfoModel,
                    "baseColorTextureInfoModel", materialIndex);

            block.add(pbrMetallicRoughnessModelVar.invoke("setBaseColorTexture")
                .arg(textureInfoModelVar));
        }

        // Metallic-roughness
        TextureInfoModel metallicRoughnessTextureInfoModel =
            pbrMetallicRoughnessModel.getMetallicRoughnessTexture();
        if (metallicRoughnessTextureInfoModel != null)
        {
            JVar textureInfoModelVar = createTextureInfoCreationCode(block,
                metallicRoughnessTextureInfoModel,
                "metallicRoughnessTextureInfoModel", materialIndex);

            block.add(pbrMetallicRoughnessModelVar
                .invoke("setMetallicRoughnessTexture")
                .arg(textureInfoModelVar));
        }
        return pbrMetallicRoughnessModelVar;
    }

    /**
     * Create the code for creating the given texture info, and add it to the
     * given block
     * 
     * @param block The block
     * @param textureInfoModel The model
     * @param textureInfoVariableNamePrefix The prefix for the variable name
     * @param materialIndex The index of the material
     * @return The variable
     */
    private JVar createTextureInfoCreationCode(JBlock block,
        TextureInfoModel textureInfoModel, String textureInfoVariableNamePrefix,
        int materialIndex)
    {
        // Collect the required types
        JClass defaultTextureInfoModelClass =
            findClass(DefaultTextureInfoModel.class);

        // DefaultTextureInfoModel baseColorTextureInfoModelX =
        // new DefaultTextureInfoModel()
        JVar textureInfoModelVar = block.decl(defaultTextureInfoModelClass,
            textureInfoVariableNamePrefix + materialIndex,
            JExpr._new(defaultTextureInfoModelClass));

        TextureModel textureModel = textureInfoModel.getTextureModel();
        if (textureModel != null)
        {
            int textureIndex =
                gltfModel.getTextureModels().indexOf(textureModel);
            if (textureIndex == -1)
            {
                logger.severe("Could not find texture model for "
                    + textureInfoVariableNamePrefix + " in material "
                    + materialIndex);
                return textureInfoModelVar;
            }
            // textureInfoX.set...TextureModel(textureModelY);
            JExpression textureExpression =
                JExpr._this().ref("textureModel" + textureIndex);
            block.add(textureInfoModelVar.invoke("setTextureModel")
                .arg(textureExpression));
        }
        Integer texCoord = textureInfoModel.getTexCoord();
        if (texCoord != null)
        {
            block.add(textureInfoModelVar.invoke("setTexCoord")
                .arg(JExpr.lit(texCoord)));
        }
        return textureInfoModelVar;
    }

    /**
     * Create the code for creating the given texture info, and add it to the
     * given block
     * 
     * @param block The block
     * @param normalTextureInfoModel The model
     * @param normalTextureInfoVariableNamePrefix The prefix for the variable
     *        name
     * @param materialIndex The index of the material
     * @return The variable
     */
    private JVar createNormalTextureInfoCreationCode(JBlock block,
        NormalTextureInfoModel normalTextureInfoModel,
        String normalTextureInfoVariableNamePrefix, int materialIndex)
    {
        // Collect the required types
        JClass defaultNormalTextureInfoModelClass =
            findClass(DefaultNormalTextureInfoModel.class);

        // DefaultNormalTextureInfoModel textureInfoModelX =
        // new DefaultNormalTextureInfoModel()
        JVar normalTextureInfoModelVar =
            block.decl(defaultNormalTextureInfoModelClass,
                normalTextureInfoVariableNamePrefix + materialIndex,
                JExpr._new(defaultNormalTextureInfoModelClass));

        TextureModel textureModel = normalTextureInfoModel.getTextureModel();
        if (textureModel != null)
        {
            int textureIndex =
                gltfModel.getTextureModels().indexOf(textureModel);
            if (textureIndex == -1)
            {
                logger.severe("Could not find texture model for "
                    + normalTextureInfoVariableNamePrefix + " in material "
                    + materialIndex);
                return normalTextureInfoModelVar;
            }
            // textureInfoX.set...TextureModel(textureModelY);
            JExpression textureExpression =
                JExpr._this().ref("textureModel" + textureIndex);
            block.add(normalTextureInfoModelVar.invoke("setTextureModel")
                .arg(textureExpression));
        }

        Integer texCoord = normalTextureInfoModel.getTexCoord();
        if (texCoord != null)
        {
            block.add(normalTextureInfoModelVar.invoke("setTexCoord")
                .arg(JExpr.lit(texCoord)));
        }

        Double normalScale = normalTextureInfoModel.getScale();
        if (normalScale != null)
        {
            block.add(normalTextureInfoModelVar.invoke("setScale")
                .arg(JExpr.lit(normalScale)));
        }

        return normalTextureInfoModelVar;
    }

    /**
     * Create the code for creating the given texture info, and add it to the
     * given block
     * 
     * @param block The block
     * @param occlusionTextureInfoModel The model
     * @param occlusionTextureInfoVariableNamePrefix The prefix for the variable
     *        name
     * @param materialIndex The index of the material
     * @return The variable
     */
    private JVar createOcclusionTextureInfoCreationCode(JBlock block,
        OcclusionTextureInfoModel occlusionTextureInfoModel,
        String occlusionTextureInfoVariableNamePrefix, int materialIndex)
    {
        // Collect the required types
        JClass defaultOcclusionTextureInfoModelClass =
            findClass(DefaultOcclusionTextureInfoModel.class);

        // DefaultOcclusionTextureInfoModel textureInfoModelX =
        // new DefaultOcclusionTextureInfoModel()
        JVar occlusionTextureInfoModelVar =
            block.decl(defaultOcclusionTextureInfoModelClass,
                occlusionTextureInfoVariableNamePrefix + materialIndex,
                JExpr._new(defaultOcclusionTextureInfoModelClass));

        TextureModel textureModel = occlusionTextureInfoModel.getTextureModel();
        if (textureModel != null)
        {
            int textureIndex =
                gltfModel.getTextureModels().indexOf(textureModel);
            if (textureIndex == -1)
            {
                logger.severe("Could not find texture model for "
                    + occlusionTextureInfoVariableNamePrefix + " in material "
                    + materialIndex);
                return occlusionTextureInfoModelVar;
            }
            // textureInfoX.set...TextureModel(textureModelY);
            JExpression textureExpression =
                JExpr._this().ref("textureModel" + textureIndex);
            block.add(occlusionTextureInfoModelVar.invoke("setTextureModel")
                .arg(textureExpression));
        }

        Integer texCoord = occlusionTextureInfoModel.getTexCoord();
        if (texCoord != null)
        {
            block.add(occlusionTextureInfoModelVar.invoke("setTexCoord")
                .arg(JExpr.lit(texCoord)));
        }

        Double occlusionStrength = occlusionTextureInfoModel.getStrength();
        if (occlusionStrength != null)
        {
            block.add(occlusionTextureInfoModelVar.invoke("setScale")
                .arg(JExpr.lit(occlusionStrength)));
        }

        return occlusionTextureInfoModelVar;
    }

}
