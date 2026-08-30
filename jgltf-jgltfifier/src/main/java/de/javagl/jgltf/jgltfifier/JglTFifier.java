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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.logging.Logger;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JVar;
import com.sun.codemodel.writer.FileCodeWriter;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.SceneModel;
import de.javagl.jgltf.model.creation.GltfModelBuilder;
import de.javagl.jgltf.model.io.GltfModelWriter;

/**
 * A class for converting a glTF model into the JglTF code that generating the
 * glTF model.
 */
public class JglTFifier
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(JglTFifier.class.getName());

    /**
     * The glTF model that was passed to the
     * {@link #generate(GltfModel, Config)} method
     */
    private GltfModel gltfModel;

    /**
     * The PRELIMINARY configuration that was passed in to the
     * {@link #generate(GltfModel, Config)} method
     */
    private Config config;

    /**
     * The code model for building the code that builds the glTF model
     */
    private JCodeModel codeModel;

    /**
     * The class that will be generated
     */
    private JDefinedClass definedClass;

    /**
     * The {@link Externalization} for accessors
     */
    private Externalization<AccessorModel> accessorExternalization;

    /**
     * The {@link Externalization} for images
     */
    private Externalization<ImageModel> imageExternalization;

    /**
     * Creates a new instance
     */
    public JglTFifier()
    {
        // Default constructor
    }

    /**
     * Generate the class that contains the code to generate the given glTF
     * model.
     * 
     * @param gltfModel The glTF model
     * @param config The PRELIMINARY configuration
     */
    public void generate(GltfModel gltfModel, Config config)
    {
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
        this.config =
            Objects.requireNonNull(config, "The config may not be null");

        this.codeModel = new JCodeModel();

        Path externalizationPath = Paths.get(config.outputRootDirectory,
            config.generatedDataDirectory);

        BiPredicate<AccessorModel, Integer> accessorExternalizationPredicate =
            (accessorModel, accessorIndex) ->
            {
                int s = accessorModel.getElementSizeInBytes();
                int c = accessorModel.getCount();
                int totalSize = c * s;
                int accessorExternalizationThresholdBytes =
                    config.accessorExternalizationThresholdBytes;
                return totalSize > accessorExternalizationThresholdBytes;
            };
        this.accessorExternalization = new Externalization<AccessorModel>(
            externalizationPath, accessorExternalizationPredicate);

        this.imageExternalization = new Externalization<ImageModel>(
            externalizationPath, (m, i) -> true);

        initializeClass();

        createMainMethod();
        createCreateModelMethod();
        createWriteMethod();

        boolean needsReadFileMethod = accessorExternalization.isApplied()
            || imageExternalization.isApplied();
        if (needsReadFileMethod)
        {
            createReadFileMethod();
        }
    }

    /**
     * Initialize the code model class that will be generated
     */
    private void initializeClass()
    {
        String packageName = config.packageName;
        String className = config.className;

        String fullClassName = packageName + "." + className;
        JDefinedClass definedClass = codeModel._getClass(fullClassName);
        if (definedClass == null)
        {
            try
            {
                definedClass = codeModel._class(fullClassName, ClassType.CLASS);
            }
            catch (JClassAlreadyExistsException e)
            {
                // Can not happen here
                e.printStackTrace();
                return;
            }
        }
        Comments.add(definedClass, "Auto-generated by JglTFifier");
        this.definedClass = definedClass;
    }

    /**
     * Create the main method in the generated class
     */
    private void createMainMethod()
    {
        String outputGltfFileName = config.outputGltfFileName;

        JClass gltfModelClass = findClass(GltfModel.class);

        JMethod method = definedClass.method(JMod.PUBLIC | JMod.STATIC,
            codeModel.VOID, "main");
        method.param(String[].class, "args");
        method._throws(IOException.class);

        Comments.add(method, "The entry point of this application.<br>", "<br>",
            "This will create a glTF model and write it to "
                + outputGltfFileName,
            "", "@param args Not used",
            "@throws IOException If the file can not be written");

        JBlock block = method.body();

        // Generated generated = new Generated();
        JVar generatedVar =
            block.decl(definedClass, "generated", JExpr._new(definedClass));

        // GltfModel gltfModel = generated.createModel();
        JVar gltfModelVar = block.decl(gltfModelClass, "gltfModel",
            JExpr.invoke(generatedVar, "createModel"));

        // write(gltfModel)
        block.invoke("write").arg(gltfModelVar).arg(outputGltfFileName);
    }

    /**
     * Create the method for creating the current glTF model
     */
    private void createCreateModelMethod()
    {
        JClass gltfModelClass = findClass(GltfModel.class);
        JClass gltfModelBuilderClass = findClass(GltfModelBuilder.class);

        JMethod method =
            definedClass.method(JMod.PRIVATE, GltfModel.class, "createModel");

        Comments.add(method, "Create the glTF model", "",
            "@return The glTF model");

        JBlock block = method.body();
        createModelElements(block);

        // GltfModelBuilder gltfModelBuilder = GltfModelBuilder.create();
        JVar gltfModelBuilderVar = block.decl(gltfModelBuilderClass,
            "gltfModelBuilder", gltfModelBuilderClass.staticInvoke("create"));

        List<SceneModel> sceneModels = gltfModel.getSceneModels();
        for (int i = 0; i < sceneModels.size(); i++)
        {
            // gltfModelBuilder.addSceneModel(sceneModel);
            block.add(gltfModelBuilderVar.invoke("addSceneModel")
                .arg(JExpr._this().ref("sceneModel" + i)));

        }

        List<AnimationModel> animationModels = gltfModel.getAnimationModels();
        for (int i = 0; i < animationModels.size(); i++)
        {
            // gltfModelBuilder.addAnimationModel(animationModel);
            block.add(gltfModelBuilderVar.invoke("addAnimationModel")
                .arg(JExpr._this().ref("animationModel" + i)));

        }

        // GltfModel gltfModel = gltfModelBuilder.build();
        JVar gltfModelVar = block.decl(gltfModelClass, "gltfModel",
            gltfModelBuilderVar.invoke("build"));

        // return gltfModel;
        block._return(gltfModelVar);
    }

    /**
     * Create the code for creating the elements of the current glTF model and
     * add them to the given block
     * 
     * @param block The block
     */
    private void createModelElements(JBlock block)
    {
        AccessorsCodeCreator accessorsCodeCreator = new AccessorsCodeCreator(
            codeModel, definedClass, gltfModel, accessorExternalization);
        accessorsCodeCreator.create(block);

        ImagesCodeCreator imagesCodeCreator = new ImagesCodeCreator(codeModel,
            definedClass, gltfModel, imageExternalization);
        imagesCodeCreator.create(block);

        TexturesCodeCreator texturesCodeCreator =
            new TexturesCodeCreator(codeModel, definedClass, gltfModel);
        texturesCodeCreator.create(block);

        MaterialsCodeCreator materialsCodeCreator =
            new MaterialsCodeCreator(codeModel, definedClass, gltfModel);
        materialsCodeCreator.create(block);

        MeshesCodeCreator meshesCodeCreator =
            new MeshesCodeCreator(codeModel, definedClass, gltfModel);
        meshesCodeCreator.create(block);

        CamerasCodeCreator camerasCodeCreator =
            new CamerasCodeCreator(codeModel, definedClass, gltfModel);
        camerasCodeCreator.create(block);

        NodesCodeCreator nodesCodeCreator =
            new NodesCodeCreator(codeModel, definedClass, gltfModel);
        nodesCodeCreator.create(block);

        SkinsCodeCreator skinsCodeCreator =
            new SkinsCodeCreator(codeModel, definedClass, gltfModel);
        skinsCodeCreator.create(block);

        NodesConnectionCodeCreator nodesConnectionCodeCreator =
            new NodesConnectionCodeCreator(codeModel, definedClass, gltfModel);
        nodesConnectionCodeCreator.create(block);

        AnimationsCodeCreator animationsCodeCreator =
            new AnimationsCodeCreator(codeModel, definedClass, gltfModel);
        animationsCodeCreator.create(block);

        ScenesCodeCreator scenesCodeCreator =
            new ScenesCodeCreator(codeModel, definedClass, gltfModel);
        scenesCodeCreator.create(block);
    }

    /**
     * Create a method in the current defined class to write a glTF model to a
     * certain output file.
     */
    private void createWriteMethod()
    {
        JClass gltfModelWriterClass = findClass(GltfModelWriter.class);
        JClass fileClass = findClass(File.class);

        JMethod method = definedClass.method(JMod.PUBLIC | JMod.STATIC,
            codeModel.VOID, "write");
        JVar gltfModelVar = method.param(GltfModel.class, "gltfModel");
        JVar fileNameVar = method.param(String.class, "fileName");
        method._throws(IOException.class);

        Comments.add(method, "Write the given model to the specified file", "",
            "@param gltfModel The GltfModel", "@param fileName The file name",
            "@throws IOException If the file can not be written");

        JBlock block = method.body();

        JVar gltfModelWriterVar = block.decl(gltfModelWriterClass,
            "gltfModelWriter", JExpr._new(gltfModelWriterClass));

        JVar fileVar = block.decl(fileClass, "file",
            JExpr._new(fileClass).arg(fileNameVar));

        JInvocation writeBinaryInvocation = gltfModelWriterVar
            .invoke("writeBinary").arg(gltfModelVar).arg(fileVar);
        block.add(writeBinaryInvocation);
    }

    /**
     * Write the generated class to the output directory
     *
     * @throws IOException If an IO error occurs
     */
    public void writeGeneratedClass() throws IOException
    {
        File destinationDirectory = Paths
            .get(config.outputRootDirectory, config.sourceCodeRootDirectory)
            .toFile();

        logger.info("Writing generated class to " + destinationDirectory);

        // A wrapper around the CodeModel writer that just
        // inserts the header
        CodeWriter source = new CodeWriter()
        {
            private final CodeWriter delegate =
                new FileCodeWriter(destinationDirectory);

            @Override
            public OutputStream openBinary(JPackage pkg, String fileName)
                throws IOException
            {
                OutputStream result = delegate.openBinary(pkg, fileName);
                String headerCode = createHeaderCode(
                    "JglTFifier - " + config.outputGltfFileName);
                result.write(headerCode.getBytes());
                return result;
            }

            @Override
            public void close() throws IOException
            {
                delegate.close();
            }
        };
        CodeWriter resource = new FileCodeWriter(destinationDirectory);
        codeModel.build(source, resource);
    }

    /**
     * Create a simple method to read a file in the generated class
     */
    private void createReadFileMethod()
    {
        JClass byteBufferClass = findClass(ByteBuffer.class);
        JClass randomAccessFileClass = findClass(RandomAccessFile.class);
        JClass fileChannelClass = findClass(FileChannel.class);
        JClass ioExceptionClass = findClass(IOException.class);
        JClass byteOrderClass = findClass(ByteOrder.class);

        JMethod method = definedClass.method(JMod.PRIVATE | JMod.STATIC,
            byteBufferClass, "readFile");
        JVar fileNameVar = method.param(String.class, "fileName");

        Comments.add(method,
            "Read the contents of the specified file into a byte buffer", "",
            "@param fileName The file name",
            "@return The byte buffer, or null on IO errors");

        JBlock block = method.body();

        JTryBlock tryBlock = block._try();
        JBlock tryBody = tryBlock.body();

        JInvocation newFileInvocation = JExpr._new(randomAccessFileClass)
            .arg(fileNameVar).arg(JExpr.lit("r"));
        JVar fileVar =
            tryBody.decl(randomAccessFileClass, "file", newFileInvocation);

        JVar channelVar = tryBody.decl(fileChannelClass, "channel",
            fileVar.invoke("getChannel"));

        JVar fileSizeVar =
            tryBody.decl(codeModel.LONG, "fileSize", channelVar.invoke("size"));

        JInvocation allocateInvocation =
            byteBufferClass.staticInvoke("allocate")
                .arg(JExpr.cast(codeModel.INT, fileSizeVar));
        JVar bufferVar =
            tryBody.decl(byteBufferClass, "buffer", allocateInvocation);

        tryBody.add(bufferVar.invoke("order")
            .arg(byteOrderClass.staticRef("LITTLE_ENDIAN")));

        tryBody.add(channelVar.invoke("read").arg(bufferVar));
        tryBody.add(bufferVar.invoke("position").arg(JExpr.lit(0)));

        tryBody.add(channelVar.invoke("close"));
        tryBody.add(fileVar.invoke("close"));

        tryBody._return(bufferVar);

        JCatchBlock catchBlock = tryBlock._catch(ioExceptionClass);
        JVar eVar = catchBlock.param("e");
        JBlock catchBody = catchBlock.body();

        catchBody.add(eVar.invoke("printStackTrace"));
        catchBody._return(JExpr._null());
    }

    /**
     * Create the string that should be inserted as the header for the generated
     * class.
     * 
     * @param headerTitle The title
     * @return The header code
     */
    private static String createHeaderCode(String headerTitle)
    {
        String headerCode = "" + "/*\n" + " * " + headerTitle + "\n" + " * \n"
            + " * Generated with JglTFifier\n"
            + " * https://github.com/javagl/JglTF\n" + " */\n";
        return headerCode;
    }

    /**
     * Find the given class in the code model
     * 
     * @param c The class
     * @return The ... class, actually, but for code model
     */
    private JClass findClass(Class<?> c)
    {
        return codeModel._ref(c).boxify();
    }

}
