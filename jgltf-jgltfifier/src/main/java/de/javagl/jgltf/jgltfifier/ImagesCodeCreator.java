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

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.impl.DefaultImageModel;
import de.javagl.jgltf.model.io.MimeTypes;

/**
 * A code creator for the images code
 */
class ImagesCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(ImagesCodeCreator.class.getName());

    /**
     * The glTF model
     */
    private final GltfModel gltfModel;

    /**
     * The {@link Externalization} handler
     */
    private final Externalization<ImageModel> externalization;

    /**
     * Creates a new instance
     * 
     * @param codeModel The code model
     * @param definedClass The defined class
     * @param gltfModel The glTF model
     * @param externalization The {@link Externalization}
     */
    ImagesCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel, Externalization<ImageModel> externalization)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
        this.externalization = Objects.requireNonNull(externalization,
            "The externalization may not be null");
    }

    @Override
    protected void create(JBlock block)
    {

        List<ImageModel> imageModels = gltfModel.getImageModels();
        if (imageModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Images (" + imageModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < imageModels.size(); i++)
        {
            block
                .directStatement("// Image " + i + " of " + imageModels.size());
            ImageModel imageModel = imageModels.get(i);
            createImage(block, imageModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given image, and add it to the given
     * block
     * 
     * @param block The block
     * @param imageModel The image
     * @param imageIndex The index of the image
     */
    private void createImage(JBlock block, ImageModel imageModel,
        int imageIndex)
    {
        JClass defaultImageModelClass = findClass(DefaultImageModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultImageModelClass,
            "imageModel" + imageIndex);

        JMethod method = createImageCreationMethod(imageModel, imageIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given image model
     * 
     * @param imageModel The image model
     * @param imageIndex The image index
     * @return The method
     */
    private JMethod createImageCreationMethod(ImageModel imageModel,
        int imageIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createImageModel" + imageIndex);
        Comments.add(method, "Create the specified image model");

        JBlock block = method.body();
        createImageCreationCode(block, imageModel, imageIndex);
        return method;
    }

    /**
     * Create the code that creates the given image model and add it to the
     * given block
     * 
     * @param block The block
     * @param imageModel The image model
     * @param imageIndex The image index
     */
    private void createImageCreationCode(JBlock block, ImageModel imageModel,
        int imageIndex)
    {
        externalization.setApplied(true);
        Path generatedDataPath = externalization.getPath();
        generatedDataPath.toFile().mkdirs();

        // Collect the required types
        JClass defaultImageModelClass = findClass(DefaultImageModel.class);
        JClass byteBufferClass = findClass(ByteBuffer.class);

        // Create a URI if there is none
        String uri = imageModel.getUri();
        if (uri == null)
        {
            String extension = determineImageFileNameExtension(imageModel);
            uri = "image_" + imageIndex + "." + extension;
        }

        // Write the file to the output directory
        Path fullOutputPath = generatedDataPath.resolve(uri);

        logger.info("Writing image " + imageIndex + " to " + fullOutputPath);
        ByteBuffer imageData = imageModel.getImageData();
        IO.writeUnchecked(fullOutputPath.toString(), imageData);

        // ByteBuffer imageX_data = readFile(...);
        JInvocation readFileInvocation =
            JExpr.invoke("readFile").arg(JExpr.lit(fullOutputPath.toString()));
        JVar imageDataVar = block.decl(byteBufferClass,
            "image" + imageIndex + "_data", readFileInvocation);

        // this.imageModelX = new DefaultImageModel();
        JFieldRef imageVar = JExpr._this().ref("imageModel" + imageIndex);
        block.assign(imageVar, JExpr._new(defaultImageModelClass));

        // Call all setters
        block.add(imageVar.invoke("setUri").arg(JExpr.lit(uri)));
        block.add(imageVar.invoke("setImageData").arg(imageDataVar));
        String mimeType = imageModel.getMimeType();
        if (mimeType != null)
        {
            block.add(imageVar.invoke("setMimeType").arg(JExpr.lit(mimeType)));
        }
    }

    /**
     * Determine the extension for an image file name (without the
     * <code>"."</code> dot), for the given {@link ImageModel}
     * 
     * (NOTE: This is also a private method in the "UriStrings" class)
     * 
     * @param imageModel The {@link ImageModel}
     * @return The file extension
     */
    private static String determineImageFileNameExtension(ImageModel imageModel)
    {
        // Try to figure out the MIME type
        String mimeTypeString = imageModel.getMimeType();
        if (mimeTypeString == null)
        {
            ByteBuffer imageData = imageModel.getImageData();
            mimeTypeString =
                MimeTypes.guessImageMimeTypeStringUnchecked(imageData);
        }

        // Try to figure out the extension based on the MIME type
        if (mimeTypeString != null)
        {
            String extensionWithoutDot = MimeTypes
                .imageFileNameExtensionForMimeTypeString(mimeTypeString);
            if (extensionWithoutDot != null)
            {
                return extensionWithoutDot;
            }
        }
        logger.warning("Could not determine file extension for image URI");
        return "";
    }

}
