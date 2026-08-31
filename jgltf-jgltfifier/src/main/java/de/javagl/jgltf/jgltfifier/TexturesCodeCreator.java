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
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.impl.DefaultTextureModel;

/**
 * A code creator for the textures code
 */
class TexturesCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(TexturesCodeCreator.class.getName());

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
    TexturesCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<TextureModel> textureModels = gltfModel.getTextureModels();
        if (textureModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Textures (" + textureModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < textureModels.size(); i++)
        {
            block.directStatement(
                "// Texture " + i + " of " + textureModels.size());
            TextureModel textureModel = textureModels.get(i);
            createTexture(block, textureModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given texture, and add it to the given
     * block
     * 
     * @param block The block
     * @param textureModel The texture
     * @param textureIndex The index of the texture
     */
    private void createTexture(JBlock block, TextureModel textureModel,
        int textureIndex)
    {
        JClass defaultTextureModelClass = findClass(DefaultTextureModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultTextureModelClass,
            "textureModel" + textureIndex);

        JMethod method =
            createTextureCreationMethod(textureModel, textureIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given texture model
     * 
     * @param textureModel The texture model
     * @param textureIndex The texture index
     * @return The method
     */
    private JMethod createTextureCreationMethod(TextureModel textureModel,
        int textureIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createTextureModel" + textureIndex);
        Comments.add(method, "Create the specified texture model");

        JBlock block = method.body();
        createTextureCreationCode(block, textureModel, textureIndex);
        return method;
    }

    /**
     * Create the code that creates the given texture model and add it to the
     * given block
     * 
     * @param block The block
     * @param textureModel The texture model
     * @param textureIndex The texture index
     */
    private void createTextureCreationCode(JBlock block,
        TextureModel textureModel, int textureIndex)
    {
        // Collect the required types
        JClass defaultTextureModelClass = findClass(DefaultTextureModel.class);
        JClass gltfConstantsClass = findClass(GltfConstants.class);

        // Obtain the index of the image model for the texture
        ImageModel imageModel = textureModel.getImageModel();
        int imageIndex = gltfModel.getImageModels().indexOf(imageModel);
        if (imageIndex == -1)
        {
            logger.severe(
                "Could not find image model for " + "texture " + textureIndex);
        }

        // this.textureModelX = new DefaultTextureModel()
        JFieldRef textureVar = JExpr._this().ref("textureModel" + textureIndex);
        block.assign(textureVar, JExpr._new(defaultTextureModelClass));

        // textureModelX.setImageModel(imageModelY);
        block.add(textureVar.invoke("setImageModel")
            .arg(JExpr._this().ref("imageModel" + imageIndex)));

        // Call the required setters, for example,
        // textureModelX.setMinFilter(GltfConstants.GL_LINEAR);
        Integer minFilter = textureModel.getMinFilter();
        if (minFilter != null)
        {
            String minFilterString = GltfConstants.stringFor(minFilter);
            block.add(textureVar.invoke("setMinFilter")
                .arg(gltfConstantsClass.staticRef(minFilterString)));
        }
        Integer magFilter = textureModel.getMagFilter();
        if (magFilter != null)
        {
            String magFilterString = GltfConstants.stringFor(magFilter);
            block.add(textureVar.invoke("setMagFilter")
                .arg(gltfConstantsClass.staticRef(magFilterString)));
        }
        Integer wrapS = textureModel.getWrapS();
        if (wrapS != null)
        {
            String wrapSString = GltfConstants.stringFor(wrapS);
            block.add(textureVar.invoke("setWrapS")
                .arg(gltfConstantsClass.staticRef(wrapSString)));
        }
        Integer wrapT = textureModel.getWrapT();
        if (wrapT != null)
        {
            String wrapTString = GltfConstants.stringFor(wrapT);
            block.add(textureVar.invoke("setWrapT")
                .arg(gltfConstantsClass.staticRef(wrapTString)));
        }
    }

}
