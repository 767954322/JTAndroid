package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GPD on 2017/3/2.
 */

public class TabOneChild implements Serializable {

    private String space_tag_id;
    private String space_tag_name;
    private String space_tag_image_url;
    private int space_tag_image_height;
    private int space_tag_image_width;
    private List<TabTwoChild> child;

    public TabOneChild(String space_tag_id, List<TabTwoChild> child, String space_tag_name, String space_tag_image_url, int space_tag_image_height, int space_tag_image_width) {
        this.space_tag_id = space_tag_id;
        this.child = child;
        this.space_tag_name = space_tag_name;
        this.space_tag_image_url = space_tag_image_url;
        this.space_tag_image_height = space_tag_image_height;
        this.space_tag_image_width = space_tag_image_width;
    }

    public TabOneChild() {
    }

    public String getSpace_tag_id() {
        return space_tag_id;
    }

    public void setSpace_tag_id(String space_tag_id) {
        this.space_tag_id = space_tag_id;
    }

    public List<TabTwoChild> getChild() {
        return child;
    }

    public void setChild(List<TabTwoChild> child) {
        this.child = child;
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

    @Override
    public String toString() {
        return "TabOneChild{" +
                "space_tag_id='" + space_tag_id + '\'' +
                ", space_tag_name='" + space_tag_name + '\'' +
                ", space_tag_image_url='" + space_tag_image_url + '\'' +
                ", space_tag_image_height=" + space_tag_image_height +
                ", space_tag_image_width=" + space_tag_image_width +
                ", child=" + child +
                '}';
    }
}
