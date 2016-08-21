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
package de.javagl.jgltf.validator;

/**
 * A warning- or error message that was created by a {@link Validator}
 */
public final class ValidatorMessage
{
    /**
     * The message
     */
    private final String message;
    
    /**
     * The context where the message occurred
     */
    private final ValidatorContext context;
    
    /**
     * Creates a new instance. A copy of the given context object will
     * be stored. If the context is <code>null</code>, then an empty
     * context will be stored.
     * 
     * @param message The warning- or error message
     * @param context The context of where the message occurred
     */
    ValidatorMessage(String message, ValidatorContext context)
    {
        this.message = message;
        this.context = new ValidatorContext(context);
    }
    
    /**
     * Returns the message as a string
     * 
     * @return The message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Returns the {@link ValidatorContext} describing where this message
     * occurred
     * 
     * @return The {@link ValidatorContext}
     */
    public ValidatorContext getContext()
    {
        return context;
    }
}