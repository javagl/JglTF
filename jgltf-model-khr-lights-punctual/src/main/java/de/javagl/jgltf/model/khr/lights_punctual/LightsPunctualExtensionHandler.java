/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.jgltf.model.khr.lights_punctual;

import java.util.List;

import de.javagl.jgltf.impl.v2.khr.lights_punctual.GlTFLightsPunctual;
import de.javagl.jgltf.impl.v2.khr.lights_punctual.Light;
import de.javagl.jgltf.impl.v2.khr.lights_punctual.LightSpot;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.Optionals;
import de.javagl.jgltf.model.extensions.ExtensionHandler;

/**
 * Implementation of an {@link ExtensionHandler} for 
 * <code>KHR_lights_punctual</code>
 */
public class LightsPunctualExtensionHandler implements ExtensionHandler
{
    @Override
    public String getExtensionName()
    {
        return "KHR_lights_punctual";
    }
    
    @Override
    public Class<?> getOwningModelClass()
    {
        return GltfModel.class;
    }

    @Override
    public Class<?> getImplClass()
    {
        return GlTFLightsPunctual.class;
    }

    @Override
    public Class<?> getModelClass()
    {
        return LightsPunctualModel.class;
    }

    @Override
    public Object convertToModel(
        GltfModel gltfModel, Object owningModelObject, Object object)
    {
        DefaultLightsPunctualModel model = new DefaultLightsPunctualModel();
        GlTFLightsPunctual impl = (GlTFLightsPunctual) object;
        List<Light> lights = impl.getLights();
        for (Light light : lights)
        {
            LightModel lightModel = new DefaultLightModel();
            lightModel.setColor(Optionals.clone(light.getColor()));
            lightModel.setIntensity(light.getIntensity());
            
            LightSpot spot = light.getSpot();
            if (spot != null)
            {
                DefaultLightSpotModel spotModel = new DefaultLightSpotModel();
                spotModel.setInnerConeAngle(spot.getInnerConeAngle());
                spotModel.setOuterConeAngle(spot.getOuterConeAngle());
                lightModel.setSpot(spotModel);
            }
            lightModel.setRange(light.getRange());
            model.addLightModel(lightModel);
        }
        return model;
    }

}
