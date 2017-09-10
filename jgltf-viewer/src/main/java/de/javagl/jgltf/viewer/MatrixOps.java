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
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.model.MathUtils;

/**
 * A class for building suppliers of float arrays that represent matrices.
 * The suppliers MAY always return the same instances of float arrays,
 * so callers MUST NOT store or modify the returned arrays.<br>
 * <br>
 * This class does not perform validations of the matrix size. The user
 * has to make sure that the operations are appropriate for the current
 * matrix size.
 */
class MatrixOps
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(MatrixOps.class.getName());
    
    /**
     * The supplier that provides the input matrix
     */
    private final Supplier<float[]> inputSupplier;
    
    /**
     * The chain of functions that will be applied. Starting with the
     * matrix from the {@link #inputSupplier}, each function will be
     * applied to the result that was returned by the previous function.
     * The result of the final function will be returned by the 
     * supplier that is created with {@link #build()}.
     */
    private final List<Function<float[], float[]>> functions;
    
    /**
     * Create a builder for matrix operations that obtains its initial
     * matrix from the given supplier.
     * 
     * @param inputSupplier The input matrix supplier
     * @return The builder
     */
    static MatrixOps create4x4(Supplier<float[]> inputSupplier)
    {
        return new MatrixOps(inputSupplier);
    }

    /**
     * Create a builder for matrix operations that starts with an identity
     * matrix
     * 
     * @return The builder
     */
    static MatrixOps create4x4()
    {
        return create4x4(createIdentitySupplier4x4());
    }
    
    /**
     * Creates a supplier that provides a 4x4 identity matrix. 
     * 
     * @return The supplier
     */
    private static Supplier<float[]> createIdentitySupplier4x4()
    {
        float matrix[] = new float[16];
        return () ->
        {
            MathUtils.setIdentity4x4(matrix);
            return matrix;
        };
    }

    /**
     * Private constructor
     * 
     * @param inputSupplier The supplier of the input matrix
     */
    private MatrixOps(Supplier<float[]> inputSupplier)
    {
        this.inputSupplier = inputSupplier; 
        this.functions = new ArrayList<Function<float[], float[]>>();
    }
    
    /**
     * Adds the operation to multiply the current matrix with the matrix
     * that is provided by the given supplier
     * 
     * @param operandSupplier The supplier of the operand
     * @return This builder
     */
    MatrixOps multiply4x4(Supplier<float[]> operandSupplier)
    {
        float result[] = new float[16];
        functions.add(named("multiply4x4", input -> 
        {
            MathUtils.mul4x4(input, operandSupplier.get(), result);
            return result;
        }));
        return this;
    }
    
    /**
     * Adds the operation to invert the current matrix
     * 
     * @return This builder
     */
    MatrixOps invert4x4()
    {
        float result[] = new float[16];
        functions.add(named("invert4x4", input -> 
        {
            MathUtils.invert4x4(input, result);
            return result;
        }));
        return this;
    }

    /**
     * Adds the operation to invert the current matrix
     * 
     * @return This builder
     */
    MatrixOps invert3x3()
    {
        float result[] = new float[9];
        functions.add(named("invert3x4", input -> 
        {
            MathUtils.invert3x3(input, result);
            return result;
        }));
        return this;
    }
    
    /**
     * Adds the operation to transpose the current matrix
     * 
     * @return This builder
     */
    MatrixOps transpose4x4()
    {
        float result[] = new float[16];
        functions.add(named("transpose4x4", input -> 
        {
            MathUtils.transpose4x4(input, result);
            return result;
        }));
        return this;
    }
    
    /**
     * Adds the operation to convert the current 4x4 matrix into a 3x3 
     * matrix by extracting the upper left (rotation and scale) part
     * 
     * @return This builder
     */
    MatrixOps getRotationScale()
    {
        float result[] = new float[9];
        functions.add(named("getRotationScale", input -> 
        {
            MathUtils.getRotationScale(input, result);
            return result;
        }));
        return this;
    }
    
    /**
     * Adds the operation to print a log message about the current matrix
     * 
     * @param name The name of the matrix, which will be part of the message
     * @param level The log level
     * @return This builder
     */
    MatrixOps log(String name, Level level)
    {
        functions.add(named("log "+name, input -> 
        {
            if (logger.isLoggable(level))
            {
                logger.log(level,
                    name + ":\n" + MathUtils.createMatrixString(input));
            }
            return input;
        }));
        return this;
    }
    
    /**
     * Returns a function that wraps the given function, but returns the
     * given name as its <code>toString</code> representation
     * 
     * @param <S> The domain type
     * @param <T> The codomain type
     * 
     * @param name The name
     * @param function The delegate function
     * @return The named function
     */
    private static <S, T> Function<S, T> named(
        String name, Function<S, T> function) 
    {
        return new Function<S, T>()
        {
            @Override
            public T apply(S s)
            {
                return function.apply(s);
            }
            
            @Override
            public String toString()
            {
                return name;
            }
        };
    }
    
    /**
     * Build the supplier of the result matrix
     * 
     * @return The supplier
     */
    Supplier<float[]> build()
    {
        return () ->
        {
            float current[] = inputSupplier.get();
            for (Function<float[], float[]> function : functions)
            {
                current = function.apply(current);
                if (logger.isLoggable(Level.FINEST))
                {
                    logger.finest("current is " + Arrays.toString(current)
                        + " after " + function);
                }
            }
            return current;
        };  
    }
}