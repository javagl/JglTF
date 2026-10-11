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

import java.util.List;
import java.util.Objects;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import de.javagl.jgltf.model.CameraModel;
import de.javagl.jgltf.model.CameraOrthographicModel;
import de.javagl.jgltf.model.CameraPerspectiveModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.impl.DefaultCameraModel;
import de.javagl.jgltf.model.impl.DefaultCameraOrthographicModel;
import de.javagl.jgltf.model.impl.DefaultCameraPerspectiveModel;

/**
 * A code creator for the cameras code
 */
class CamerasCodeCreator extends AbstractCodeCreator
{
    /**
     * The glTF model
     */
    private final GltfModel gltfModel;

    /**
     * Creates a new instance
     * 
     * @param codeModel The code model
     * @param definedClass The defined class
     * @param gltfModel The glTF model
     */
    CamerasCodeCreator(JCodeModel codeModel, JDefinedClass definedClass,
        GltfModel gltfModel)
    {
        super(codeModel, definedClass);
        this.gltfModel =
            Objects.requireNonNull(gltfModel, "The gltfModel may not be null");
    }

    @Override
    protected void create(JBlock block)
    {
        List<CameraModel> cameraModels = gltfModel.getCameraModels();
        if (cameraModels.isEmpty())
        {
            return;
        }
        block.directStatement("// Cameras (" + cameraModels.size() + ")");
        block.directStatement(" ");

        for (int i = 0; i < cameraModels.size(); i++)
        {
            block.directStatement(
                "// Camera " + i + " of " + cameraModels.size());
            CameraModel cameraModel = cameraModels.get(i);
            createCamera(block, cameraModel, i);
            block.directStatement(" ");
        }
        block.directStatement(" ");
    }

    /**
     * Create the code for creating the given camera, and add it to the given
     * block
     * 
     * @param block The block
     * @param cameraModel The camera
     * @param cameraIndex The index of the camera
     */
    private void createCamera(JBlock block, CameraModel cameraModel,
        int cameraIndex)
    {
        JClass defaultCameraModelClass = findClass(DefaultCameraModel.class);

        getDefinedClass().field(JMod.PRIVATE, defaultCameraModelClass,
            "cameraModel" + cameraIndex);

        JMethod method = createCameraCreationMethod(cameraModel, cameraIndex);
        block.invoke(method);
    }

    /**
     * Create the method that creates the given camera model
     * 
     * @param cameraModel The camera model
     * @param cameraIndex The camera index
     * @return The method
     */
    private JMethod createCameraCreationMethod(CameraModel cameraModel,
        int cameraIndex)
    {

        JMethod method = getDefinedClass().method(JMod.PRIVATE, void.class,
            "createCameraModel" + cameraIndex);
        Comments.add(method, "Create the specified camera model");

        JBlock block = method.body();
        createCameraCreationCode(block, cameraModel, cameraIndex);
        return method;
    }

    /**
     * Create the code that creates the given camera model and add it to the
     * given block
     * 
     * @param block The block
     * @param cameraModel The camera model
     * @param cameraIndex The camera index
     */
    private void createCameraCreationCode(JBlock block, CameraModel cameraModel,
        int cameraIndex)
    {
        // Collect the required types
        JClass defaultCameraModelClass = findClass(DefaultCameraModel.class);
        JClass defaultCameraPerspectiveModelClass =
            findClass(DefaultCameraPerspectiveModel.class);
        JClass defaultCameraOrthographicModelClass =
            findClass(DefaultCameraOrthographicModel.class);

        // this.cameraModelX = new DefaultCameraModel()
        JFieldRef cameraVar = JExpr._this().ref("cameraModel" + cameraIndex);
        block.assign(cameraVar, JExpr._new(defaultCameraModelClass));

        CameraPerspectiveModel cameraPerspectiveModel =
            cameraModel.getCameraPerspectiveModel();
        CameraOrthographicModel cameraOrthographicModel =
            cameraModel.getCameraOrthographicModel();

        if (cameraPerspectiveModel != null)
        {
            JVar cameraPerspectiveVar =
                block.decl(defaultCameraPerspectiveModelClass,
                    "cameraPerspectiveModel" + cameraIndex,
                    JExpr._new(defaultCameraPerspectiveModelClass));

            // Call the required setters
            Double aspectRatio = cameraPerspectiveModel.getAspectRatio();
            if (aspectRatio != null)
            {
                block.add(cameraPerspectiveVar.invoke("setAspectRatio")
                    .arg(JExpr.lit(aspectRatio)));
            }

            Double yfov = cameraPerspectiveModel.getYfov();
            if (yfov != null)
            {
                block.add(cameraPerspectiveVar.invoke("setYfov")
                    .arg(JExpr.lit(yfov)));
            }

            Double zfar = cameraPerspectiveModel.getZfar();
            if (zfar != null)
            {
                block.add(cameraPerspectiveVar.invoke("setZfar")
                    .arg(JExpr.lit(zfar)));
            }

            Double znear = cameraPerspectiveModel.getZnear();
            if (znear != null)
            {
                block.add(cameraPerspectiveVar.invoke("setZnear")
                    .arg(JExpr.lit(znear)));
            }

            block.add(cameraVar.invoke("setCameraPerspectiveModel")
                .arg(cameraPerspectiveVar));
        }

        if (cameraOrthographicModel != null)
        {
            JVar cameraOrthographicVar =
                block.decl(defaultCameraOrthographicModelClass,
                    "cameraOrthographicModel" + cameraIndex,
                    JExpr._new(defaultCameraOrthographicModelClass));

            // Call the required setters
            Double xmag = cameraOrthographicModel.getXmag();
            if (xmag != null)
            {
                block.add(cameraOrthographicVar.invoke("setXmag")
                    .arg(JExpr.lit(xmag)));
            }
            Double ymag = cameraOrthographicModel.getYmag();
            if (ymag != null)
            {
                block.add(cameraOrthographicVar.invoke("setYmag")
                    .arg(JExpr.lit(ymag)));
            }

            Double zfar = cameraOrthographicModel.getZfar();
            if (zfar != null)
            {
                block.add(cameraOrthographicVar.invoke("setZfar")
                    .arg(JExpr.lit(zfar)));
            }

            Double znear = cameraOrthographicModel.getZnear();
            if (znear != null)
            {
                block.add(cameraOrthographicVar.invoke("setZnear")
                    .arg(JExpr.lit(znear)));
            }

            block.add(cameraVar.invoke("setCameraOrthographicModel")
                .arg(cameraOrthographicVar));
        }
    }

}
