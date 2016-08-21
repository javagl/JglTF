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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.javagl.jgltf.impl.GlTF;
import de.javagl.jgltf.impl.Scene;

/**
 * A basic validator for glTF data. Note that this class is only intended 
 * for internal use. It does NOT do a full-fledged, systematic validation, 
 * but only offers basic sanity checks. Do NOT rely on this.
 */
public class Validator extends AbstractGltfValidator
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(Validator.class.getName());
    
    /**
     * The log level for errors
     */
    private final Level errorLogLevel = Level.SEVERE;
    
    /**
     * The {@link SceneValidator}
     */
    private final SceneValidator sceneValidator;
    
    /**
     * Default constructor
     * 
     * @param gltf The {@link GlTF} whose elements should be validated
     */
    public Validator(GlTF gltf)
    {
        super(gltf);
        this.sceneValidator = new SceneValidator(gltf);
    }
    
    /**
     * Returns whether the {@link GlTF} that was given in the constructor
     * is valid. If it is not valid, then the errors will be printed,
     * and <code>false</code> will be returned.
     * 
     * @return Whether the {@link GlTF} is valid
     */
    public boolean isValid()
    {
        ValidatorResult validatorResult = validate();
        if (validatorResult.hasErrors())
        {
            validatorResult.logErrors(logger, errorLogLevel);
            return false;
        }
        return true;
    }

    /**
     * Validate the {@link GlTF}, and return the {@link ValidatorResult}
     * 
     * @return The {@link ValidatorResult}
     */
    public ValidatorResult validate()
    {
        ValidatorContext context = new ValidatorContext("glTF");
        ValidatorResult validatorResult = new ValidatorResult();
        
        String defaultSceneId = getGltf().getScene();
        validatorResult.add(sceneValidator.validateScene(
            defaultSceneId, context.with("scene")));
        if (validatorResult.hasErrors())
        {
            return validatorResult;
        }
        
        Map<String, Scene> scenes = getGltf().getScenes();
        for (String sceneId : scenes.keySet())
        {
            if (!sceneId.equals(defaultSceneId))
            {
                validatorResult.add(sceneValidator.validateScene(
                    sceneId, context));
                if (validatorResult.hasErrors())
                {
                    return validatorResult;
                }
            }
        }
        return validatorResult;
    }
    
    
    
    
        
    
    
    
    


    
    

    

    
    

    
}
