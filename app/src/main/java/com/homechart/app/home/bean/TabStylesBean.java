package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/2.
 */

public class TabStylesBean implements Serializable {


    private String style_tag_id;
    private String style_tag_name;
    private String style_tag_image_url;
    private int style_tag_image_height;
    private int style_tag_image_width;

    public TabStylesBean(String style_tag_id, String style_tag_name, String style_tag_image_url, int style_tag_image_height, int style_tag_image_width) {
        this.style_tag_id = style_tag_id;
        this.style_tag_name = style_tag_name;
        this.style_tag_image_url = style_tag_image_url;
        this.style_tag_image_height = style_tag_image_height;
        this.style_tag_image_width = style_tag_image_width;
    }

    public TabStylesBean() {
    }

    public String getStyle_tag_id() {
        return style_tag_id;
    }

    public void setStyle_tag_id(String style_tag_id) {
        this.style_tag_id = style_tag_id;
    }

    public String getStyle_tag_name() {
        return style_tag_name;
    }

    public void setStyle_tag_name(String style_tag_name) {
        this.style_tag_name = style_tag_name;
    }

    public String getStyle_tag_image_url() {
        return style_tag_image_url;
    }

    public void setStyle_tag_image_url(String style_tag_image_url) {
        this.style_tag_image_url = style_tag_image_url;
    }

    public int getStyle_tag_image_height() {
        return style_tag_image_height;
    }

    public void setStyle_tag_image_height(int style_tag_image_height) {
        this.style_tag_image_height = style_tag_image_height;
    }

    public int getStyle_tag_image_width() {
        return style_tag_image_width;
    }

    public void setStyle_tag_image_width(int style_tag_image_width) {
        this.style_tag_image_width = style_tag_image_width;
    }

    @Override
    public String toString() {
        return "TabStylesBean{" +
                "style_tag_id='" + style_tag_id + '\'' +
                ", style_tag_name='" + style_tag_name + '\'' +
                ", style_tag_image_url='" + style_tag_image_url + '\'' +
                ", style_tag_image_height=" + style_tag_image_height +
                ", style_tag_image_width=" + style_tag_image_width +
                '}';
    }
}
