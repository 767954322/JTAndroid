package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/4.
 */

public class DetailPicItemCateTagBean implements Serializable{

    private String tag_name;
    private String id;

    public DetailPicItemCateTagBean(String tag_name, String id) {
        this.tag_name = tag_name;
        this.id = id;
    }

    public DetailPicItemCateTagBean() {
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DetailPicItemCateTagBean{" +
                "tag_name='" + tag_name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
