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
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.creation.AccessorModels;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * A code creator for the accessors code
 */
class AccessorsCodeCreator extends AbstractCodeCreator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(AccessorsCodeCreator.class.getName());

    /**
     * The glTF model
     */
    private final GltfModel gltfModel;

    /**
     * The {@link Externalization} handler
     */
    private final Externalization<AccessorModel> externalization;

    /**
     * Creates a new instance
     * 
     * @param codeModel The code model
     * @param definedClass The defined class
     * @param gltfModel The glTF model
     * @param externalization The {@link Externalization}
     */
    AccessorsCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel, Externalization<AccessorModel> externalization)
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

        List<AccessorModel> accessorModels = gltfModel.getAccessorModels();
        if (accessorModels.isEmpty())
        {
            return;
        }

        block.directStatement("// Accessors (" + accessorModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < accessorModels.size(); i++)
        {
            block.directStatement(
                "// Accessor " + i + " of " + accessorModels.size());
            AccessorModel accessorModel = accessorModels.get(i);
            createAccessor(block, accessorModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");

    }

    /**
     * Create the code for creating the given accessor, and add it to the given
     * block
     * 
     * @param block The block
     * @param accessorModel The accessor
     * @param accessorIndex The index of the accessor
     */
    private void createAccessor(JBlock block, AccessorModel accessorModel,
        int accessorIndex)
    {
        JClass defaultAccessorModelClass =
            findClass(DefaultAccessorModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultAccessorModelClass,
            "accessorModel" + accessorIndex);

        JMethod method =
            createAccessorCreationMethod(accessorModel, accessorIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given accessor model
     * 
     * @param accessorModel The accessor model
     * @param accessorIndex The accessor index
     * @return The method
     */
    private JMethod createAccessorCreationMethod(AccessorModel accessorModel,
        int accessorIndex)
    {
        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createAccessorModel" + accessorIndex);
        Comments.add(method, "Create the specified accessor model");

        JBlock block = method.body();
        createAccessorCreationCode(block, accessorModel, accessorIndex);
        return method;
    }

    /**
     * Create the code that creates the given accessor model
     * 
     * @param block The block
     * @param accessorModel The accessor model
     * @param accessorIndex The accessor index
     */
    private void createAccessorCreationCode(JBlock block,
        AccessorModel accessorModel, int accessorIndex)
    {

        JClass gltfConstantsClass = findClass(GltfConstants.class);
        JClass accessorModelsClass = findClass(AccessorModels.class);

        // Collect information from model
        int componentType = accessorModel.getComponentType();
        String componentTypeString = GltfConstants.stringFor(componentType);
        JExpression typeExpr =
            JExpr.lit(accessorModel.getElementType().toString());
        JExpression normalizedExpr = JExpr.lit(accessorModel.isNormalized());

        // Create the expression that provides a ByteBuffer with
        // the required data, depending on the type of the accessor
        JExpression bufferExpression;

        boolean doExternalize =
            externalization.shouldApply(accessorModel, accessorIndex);
        if (doExternalize)
        {
            bufferExpression = createBufferExpressionExternal(block,
                accessorModel, accessorIndex);
        }
        else
        {
            Class<?> componentDataType = accessorModel.getComponentDataType();
            if (componentDataType == float.class)
            {
                bufferExpression = createBufferExpressionFloat(block,
                    accessorModel, accessorIndex);
            }
            else if (componentDataType == int.class)
            {
                bufferExpression = createBufferExpressionInt(block,
                    accessorModel, accessorIndex);
            }
            else if (componentDataType == short.class)
            {
                bufferExpression = createBufferExpressionShort(block,
                    accessorModel, accessorIndex);
            }
            else if (componentDataType == byte.class)
            {
                bufferExpression = createBufferExpressionByte(block,
                    accessorModel, accessorIndex);
            }
            else
            {
                logger.severe("Unknown accessor component data type: "
                    + componentDataType);
                return;
            }
        }

        // Create the expression that calls AccessorModels.create(...)
        // with the collected parameters
        JInvocation createExpr = accessorModelsClass.staticInvoke("create")
            .arg(gltfConstantsClass.staticRef(componentTypeString))
            .arg(typeExpr).arg(normalizedExpr).arg(bufferExpression);

        // this.accessorModelX = ...
        block.assign(JExpr._this().ref("accessorModel" + accessorIndex),
            createExpr);
    }

    /**
     * Create the expression that provides a byte buffer with the data for the
     * given accessor model, and add statements that are required for the
     * expression to the given block.
     * 
     * @param block The block
     * @param accessorModel The accessor model
     * @param accessorIndex The index of the accessor model
     * @return The expression
     */
    private JExpression createBufferExpressionFloat(JBlock block,
        AccessorModel accessorModel, int accessorIndex)
    {
        AccessorData accessorData = accessorModel.getAccessorData();

        // Collect the required types
        JClass floatBufferClass = findClass(FloatBuffer.class);
        JType floatArrayType = findClass(float[].class);
        JClass buffersClass = findClass(Buffers.class);

        // float accessorModelX_array[] = new float[] { ... }
        JArray array = JExpr.newArray(getCodeModel().FLOAT);
        AccessorFloatData accessorFloatData = (AccessorFloatData) accessorData;
        int n = accessorFloatData.getTotalNumComponents();
        for (int c = 0; c < n; c++)
        {
            array.add(JExpr.lit(accessorFloatData.get(c)));
        }
        JVar accessorArrayVar = block.decl(floatArrayType,
            "accessorModel" + accessorIndex + "_array", array);

        // The actual expression:
        // Buffers.createByteBufferFrom(FloatBuffer.wrap(accessorModelX_array))
        JInvocation wrapExpr =
            floatBufferClass.staticInvoke("wrap").arg(accessorArrayVar);
        JInvocation createByteBufferExpr =
            buffersClass.staticInvoke("createByteBufferFrom").arg(wrapExpr);

        return createByteBufferExpr;
    }

    /**
     * Create the expression that provides a byte buffer with the data for the
     * given accessor model, and add statements that are required for the
     * expression to the given block.
     * 
     * @param block The block
     * @param accessorModel The accessor model
     * @param accessorIndex The index of the accessor model
     * @return The expression
     */
    private JExpression createBufferExpressionByte(JBlock block,
        AccessorModel accessorModel, int accessorIndex)
    {
        AccessorData accessorData = accessorModel.getAccessorData();

        // Collect the required types
        JClass byteBufferClass = findClass(ByteBuffer.class);
        JType byteArrayType = findClass(byte[].class);

        // byte accessorModelX_array[] = new byte[] { ... }
        JArray array = JExpr.newArray(getCodeModel().BYTE);
        AccessorByteData accessorByteData = (AccessorByteData) accessorData;
        int n = accessorByteData.getTotalNumComponents();
        for (int c = 0; c < n; c++)
        {
            array.add(JExpr.lit(accessorByteData.get(c)));
        }
        JVar accessorArrayVar = block.decl(byteArrayType,
            "accessorModel" + accessorIndex + "_array", array);

        // The actual expression:
        // Buffer.wrap(accessorModelX_array)
        JInvocation wrapExpr =
            byteBufferClass.staticInvoke("wrap").arg(accessorArrayVar);
        return wrapExpr;
    }

    /**
     * Create the expression that provides a byte buffer with the data for the
     * given accessor model, and add statements that are required for the
     * expression to the given block.
     * 
     * @param block The block
     * @param accessorModel The accessor model
     * @param accessorIndex The index of the accessor model
     * @return The expression
     */
    private JExpression createBufferExpressionShort(JBlock block,
        AccessorModel accessorModel, int accessorIndex)
    {
        AccessorData accessorData = accessorModel.getAccessorData();

        // Collect the required types
        JClass shortBufferClass = findClass(ShortBuffer.class);
        JType shortArrayType = findClass(short[].class);
        JClass buffersClass = findClass(Buffers.class);

        // short accessorModelX_array[] = new short[] { ... }
        JArray array = JExpr.newArray(getCodeModel().SHORT);
        AccessorShortData accessorShortData = (AccessorShortData) accessorData;
        int n = accessorShortData.getTotalNumComponents();
        for (int c = 0; c < n; c++)
        {
            array.add(JExpr.lit(accessorShortData.get(c)));
        }
        JVar accessorArrayVar = block.decl(shortArrayType,
            "accessorModel" + accessorIndex + "_array", array);

        // The actual expression:
        // Buffers.createByteBufferFrom(ShortBUffer.wrap(accessorModelX_array))
        JInvocation wrapExpr =
            shortBufferClass.staticInvoke("wrap").arg(accessorArrayVar);
        JInvocation createByteBufferExpr =
            buffersClass.staticInvoke("createByteBufferFrom").arg(wrapExpr);
        return createByteBufferExpr;
    }

    /**
     * Create the expression that provides a byte buffer with the data for the
     * given accessor model, and add statements that are required for the
     * expression to the given block.
     * 
     * @param block The block
     * @param accessorModel The accessor model
     * @param accessorIndex The index of the accessor model
     * @return The expression
     */
    private JExpression createBufferExpressionInt(JBlock block,
        AccessorModel accessorModel, int accessorIndex)
    {
        AccessorData accessorData = accessorModel.getAccessorData();

        // Collect the required types
        JClass intBufferClass = findClass(IntBuffer.class);
        JType intArrayType = findClass(int[].class);
        JClass buffersClass = findClass(Buffers.class);

        // int accessorModelX_array[] = new int[] { ... }
        JArray array = JExpr.newArray(getCodeModel().INT);
        AccessorIntData accessorIntData = (AccessorIntData) accessorData;
        int n = accessorIntData.getTotalNumComponents();
        for (int c = 0; c < n; c++)
        {
            array.add(JExpr.lit(accessorIntData.get(c)));
        }
        JVar accessorArrayVar = block.decl(intArrayType,
            "accessorModel" + accessorIndex + "_array", array);

        // The actual expression:
        // Buffers.createByteBufferFrom(IntBuffer.wrap(accessorModelX_array))
        JInvocation wrapExpr =
            intBufferClass.staticInvoke("wrap").arg(accessorArrayVar);
        JInvocation createByteBufferExpr =
            buffersClass.staticInvoke("createByteBufferFrom").arg(wrapExpr);
        return createByteBufferExpr;
    }

    /**
     * Create the expression that provides a byte buffer with the data for the
     * given accessor model, when the accessor model data was written to an
     * external file, and add statements that are required for the expression to
     * the given block.
     * 
     * @param block The block
     * @param accessorModel The accessor model
     * @param accessorIndex The index of the accessor model
     * @return The expression
     */
    private JExpression createBufferExpressionExternal(JBlock block,
        AccessorModel accessorModel, int accessorIndex)
    {
        externalization.setApplied(true);

        Path generatedDataPath = externalization.getPath();
        generatedDataPath.toFile().mkdirs();

        // Collect the required types
        JClass byteBufferClass = findClass(ByteBuffer.class);
        AccessorData accessorData = accessorModel.getAccessorData();
        ByteBuffer rawByteBuffer = accessorData.createByteBuffer();

        // Write the data to the output file
        String uri = "accessor" + accessorIndex + "_data.raw";
        Path fullOutputPath = generatedDataPath.resolve(uri);

        logger.info(
            "Writing accessor " + accessorIndex + " to " + fullOutputPath);
        IO.writeUnchecked(fullOutputPath.toString(), rawByteBuffer);

        // ByteBuffer accessorX_data = readFile(...);
        JInvocation readFileInvocation =
            JExpr.invoke("readFile").arg(JExpr.lit(fullOutputPath.toString()));
        JVar bufferDataVar = block.decl(byteBufferClass,
            "accessor" + accessorIndex + "_data", readFileInvocation);

        return bufferDataVar;
    }

}
