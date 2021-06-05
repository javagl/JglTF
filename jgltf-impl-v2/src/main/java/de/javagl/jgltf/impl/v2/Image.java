/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package de.javagl.jgltf.impl.v2;



/**
 * Image data used to create a texture. Image can be referenced by URI or 
 * `bufferView` index. `mimeType` is required in the latter case. 
 * 
 * Auto-generated for image.schema.json 
 * 
 */
public class Image
    extends GlTFChildOfRootProperty
{

    /**
     * The uri of the image. (optional) 
     * 
     */
    private String uri;
    /**
     * The image's MIME type. Required if `bufferView` is defined. 
     * (optional)<br> 
     * Valid values: ["image/jpeg", "image/png"] 
     * 
     */
    private String mimeType;
    /**
     * The index of the bufferView that contains the image. Use this instead 
     * of the image's uri property. (optional) 
     * 
     */
    private Integer bufferView;

    /**
     * The uri of the image. (optional) 
     * 
     * @param uri The uri to set
     * 
     */
    public void setUri(String uri) {
        if (uri == null) {
            this.uri = uri;
            return ;
        }
        this.uri = uri;
    }

    /**
     * The uri of the image. (optional) 
     * 
     * @return The uri
     * 
     */
    public String getUri() {
        return this.uri;
    }

    /**
     * The image's MIME type. Required if `bufferView` is defined. 
     * (optional)<br> 
     * Valid values: ["image/jpeg", "image/png"] 
     * 
     * @param mimeType The mimeType to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     * 
     */
    public void setMimeType(String mimeType) {
        if (mimeType == null) {
            this.mimeType = mimeType;
            return ;
        }
        if ((!"image/jpeg".equals(mimeType))&&(!"image/png".equals(mimeType))) {
            throw new IllegalArgumentException((("Invalid value for mimeType: "+ mimeType)+", valid: [\"image/jpeg\", \"image/png\"]"));
        }
        this.mimeType = mimeType;
    }

    /**
     * The image's MIME type. Required if `bufferView` is defined. 
     * (optional)<br> 
     * Valid values: ["image/jpeg", "image/png"] 
     * 
     * @return The mimeType
     * 
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * The index of the bufferView that contains the image. Use this instead 
     * of the image's uri property. (optional) 
     * 
     * @param bufferView The bufferView to set
     * 
     */
    public void setBufferView(Integer bufferView) {
        if (bufferView == null) {
            this.bufferView = bufferView;
            return ;
        }
        this.bufferView = bufferView;
    }

    /**
     * The index of the bufferView that contains the image. Use this instead 
     * of the image's uri property. (optional) 
     * 
     * @return The bufferView
     * 
     */
    public Integer getBufferView() {
        return this.bufferView;
    }

}
