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
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class summarizing the result produced by a {@link Validator}.
 */
class ValidatorResult
{
    /**
     * The {@link ValidatorMessage}s that indicate errors
     */
    private final List<ValidatorMessage> errors;

    /**
     * The {@link ValidatorMessage}s that indicate warnings
     */
    private final List<ValidatorMessage> warnings;
    
    /**
     * Create a new, empty validator result
     */
    ValidatorResult()
    {
        this.errors = new ArrayList<ValidatorMessage>();
        this.warnings = new ArrayList<ValidatorMessage>();
    }
    
    /**
     * Add all errors and warnings from the given result to this one
     * 
     * @param that The result to add to this one
     */
    void add(ValidatorResult that)
    {
        this.errors.addAll(that.errors);
        this.warnings.addAll(that.warnings);
    }

    /**
     * Add a {@link ValidatorMessage} with the given message string and
     * context to this result. If the given context is <code>null</code>,
     * then an empty context will be used.
     * 
     * @param errorMessage The error message
     * @param validatorContext The {@link ValidatorContext}
     */
    void addError(String errorMessage, ValidatorContext validatorContext)
    {
        this.errors.add(
            new ValidatorMessage(errorMessage, validatorContext));
    }
    
    /**
     * Returns an unmodifiable view on {@link ValidatorMessage}s for errors 
     * that have been collected in this result
     * 
     * @return The errors
     */
    List<ValidatorMessage> getErrors()
    {
        return Collections.unmodifiableList(errors);
    }
    
    /**
     * Returns whether this result contains any error 
     * {@link ValidatorMessage}s
     * 
     * @return Whether this result contains errors
     */
    boolean hasErrors()
    {
        return !errors.isEmpty();
    }
    

    /**
     * Add a {@link ValidatorMessage} with the given message string and
     * context to this result. If the given context is <code>null</code>,
     * then an empty context will be used.
     * 
     * @param warningMessage The warning message
     * @param validatorContext The {@link ValidatorContext}
     */
    void addWarning(String warningMessage, ValidatorContext validatorContext)
    {
        this.warnings.add(
            new ValidatorMessage(warningMessage, validatorContext));
    }
    
    /**
     * Returns an unmodifiable view on {@link ValidatorMessage}s for warnings 
     * that have been collected in this result
     * 
     * @return The warnings
     */
    List<ValidatorMessage> getWarnings()
    {
        return Collections.unmodifiableList(warnings);
    }
    
    /**
     * Returns whether this result contains any warning 
     * {@link ValidatorMessage}s
     * 
     * @return Whether this result contains warnings
     */
    boolean hasWarnings()
    {
        return !warnings.isEmpty();
    }
    
    
    
    /**
     * If this validator result {@link #hasErrors() has errors}, then they 
     * will be printed to the given logger, with the given log level
     * 
     * @param logger The logger
     * @param level The log level
     */
    void logErrors(Logger logger, Level level)
    {
        if (hasErrors())
        {
            List<ValidatorMessage> errors = getErrors();
            logger.log(level, "Errors (" + errors.size() + ")");
            for (ValidatorMessage validatorMessage : errors)
            {
                logger.log(level, "  " + validatorMessage.getMessage() + 
                    ", context: " + validatorMessage.getContext());
            }
        }
    }
    
}