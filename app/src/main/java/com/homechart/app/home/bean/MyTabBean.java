package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/2.
 */

public class MyTabBean implements Serializable {

    /**
     * "space_tag_id": "2000003",
     * "space_tag_name": "客厅",
     * "space_tag_image_url": "https://img.idcool.com.cn/picture/m0_w360_h360/201701/40/2mYZ0_dtrE61IvuglmDUpA.jpg",
     * "space_tag_image_height": 360,
     * "space_tag_image_width": 360,
     */
    private String space_tag_id;
    private String space_tag_name;
    private String space_tag_image_url;
    private int space_tag_image_height;
    private int space_tag_image_width;
    private int space_tag_enable;

    public MyTabBean(String space_tag_id, String space_tag_name, String space_tag_image_url, int space_tag_image_height, int space_tag_image_width, int space_tag_enable) {
        this.space_tag_id = space_tag_id;
        this.space_tag_name = space_tag_name;
        this.space_tag_image_url = space_tag_image_url;
        this.space_tag_image_height = space_tag_image_height;
        this.space_tag_image_width = space_tag_image_width;
        this.space_tag_enable = space_tag_enable;
    }

    public String getSpace_tag_id() {
        return space_tag_id;
    }

    public void setSpace_tag_id(String space_tag_id) {
        this.space_tag_id = space_tag_id;
    }

    public String getSpace_tag_name() {
        return space_tag_name;
    }

    public void setSpace_tag_name(String space_tag_name) {
        this.space_tag_name = space_tag_name;
    }

    public String getSpace_tag_image_url() {
        return space_tag_image_url;
    }

    public void setSpace_tag_image_url(String space_tag_image_url) {
        this.space_tag_image_url = space_tag_image_url;
    }

    public int getSpace_tag_image_height() {
        return space_tag_image_height;
    }

    public void setSpace_tag_image_height(int space_tag_image_height) {
        this.space_tag_image_height = space_tag_image_height;
    }

    public int getSpace_tag_image_width() {
        return space_tag_image_width;
    }

    public void setSpace_tag_image_width(int space_tag_image_width) {
        this.space_tag_image_width = space_tag_image_width;
    }

    public int getSpace_tag_enable() {
        return space_tag_enable;
    }

    public void setSpace_tag_enable(int space_tag_enable) {
        this.space_tag_enable = space_tag_enable;
    }

    @Override
    public String toString() {
        return "MyTabBean{" +
                "space_tag_id='" + space_tag_id + '\'' +
                ", space_tag_name='" + space_tag_name + '\'' +
                ", space_tag_image_url='" + space_tag_image_url + '\'' +
                ", space_tag_image_height=" + space_tag_image_height +
                ", space_tag_image_width=" + space_tag_image_width +
                ", space_tag_enable=" + space_tag_enable +
                '}';
    }
}
