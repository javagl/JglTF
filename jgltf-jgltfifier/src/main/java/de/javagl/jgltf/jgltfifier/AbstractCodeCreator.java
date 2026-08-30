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

import java.util.Objects;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;

/**
 * Abstract base class for classes that can create code in one block of a code
 * model
 */
abstract class AbstractCodeCreator
{
    /**
     * The code model for building the code that builds the glTF model
     */
    private JCodeModel codeModel;

    /**
     * The class that will be generated
     */
    private final JDefinedClass definedClass;

    /**
     * Creates a new instance
     * 
     * @param codeModel The code model
     * @param definedClass The defined class
     */
    AbstractCodeCreator(JCodeModel codeModel, JDefinedClass definedClass)
    {
        this.codeModel =
            Objects.requireNonNull(codeModel, "The codeModel may not be null");
        this.definedClass = Objects.requireNonNull(definedClass,
            "The definedClass may not be null");
    }

    /**
     * Creates the code in the given block
     * 
     * @param block The block
     */
    protected abstract void create(JBlock block);

    /**
     * Returns the code model
     * 
     * @return The code model
     */
    JCodeModel getCodeModel()
    {
        return codeModel;
    }

    /**
     * Returns the class that will be generated
     * 
     * @return The class
     */
    JDefinedClass getDefinedClass()
    {
        return definedClass;
    }

    /**
     * Find the given class in the code model
     * 
     * @param c The class
     * @return The ... class, actually, but for code model
     */
    protected final JClass findClass(Class<?> c)
    {
        return codeModel._ref(c).boxify();
    }

    /**
     * Create a new expression to create a double array with the given values,
     * or the "null" expression of the given array is null
     * 
     * @param inputArray The input array
     * @return The expression
     */
    protected final JExpression newDoubleArrayWith(double inputArray[])
    {
        if (inputArray == null)
        {
            return JExpr._null();
        }
        JArray array = JExpr.newArray(codeModel.DOUBLE);
        for (int i = 0; i < inputArray.length; i++)
        {
            array.add(JExpr.lit(inputArray[i]));
        }
        return array;
    }

}
