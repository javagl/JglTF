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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An object describing a context of a {@link Validator}. This may, for 
 * example, be a path of a JSON tree. Instances of this class are 
 * immutable. 
 */
public final class ValidatorContext
{
    /**
     * The elements describing the breadcrumbs of the validation context
     */
    private final List<String> elements;
    
    /**
     * Creates a new, empty instance
     */
    ValidatorContext()
    {
        this.elements = new ArrayList<String>();
    }

    /**
     * Creates a new context that only contains the given element
     * 
     * @param element The element contained in this context 
     */
    ValidatorContext(String element)
    {
        this.elements = new ArrayList<String>();
        this.elements.add(element);
    }

    /**
     * Creates a new instance that is a copy of the given context
     * 
     * @param that The context to copy
     */
    ValidatorContext(ValidatorContext that)
    {
        if (that == null)
        {
            this.elements = new ArrayList<String>();
        }
        else
        {
            this.elements = new ArrayList<String>(that.elements);
        }
    }
    
    /**
     * Returns a new {@link ValidatorContext} that was extended with the
     * given element
     * 
     * @param element The element
     * @return The new {@link ValidatorContext}
     */
    ValidatorContext with(String element)
    {
        ValidatorContext result = new ValidatorContext(this);
        result.elements.add(element);
        return result;
    }
    
    @Override
    public String toString()
    {
        return elements.stream().collect(Collectors.joining(" -> "));
    }
}