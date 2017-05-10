package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/6.
 */

public class DesinerCaseDataItemBean implements Serializable {

    private String item_num;
    private String album_id;
    private String album_name;
    private String description;
    private String image_url;
    private DesinerCaseDataItemPropertyBean property;

    public DesinerCaseDataItemBean(String item_num, String album_id, String album_name, String description, String image_url, DesinerCaseDataItemPropertyBean property) {
        this.item_num = item_num;
        this.album_id = album_id;
        this.album_name = album_name;
        this.description = description;
        this.image_url = image_url;
        this.property = property;
    }

    public DesinerCaseDataItemBean() {
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public DesinerCaseDataItemPropertyBean getProperty() {
        return property;
    }

    public void setProperty(DesinerCaseDataItemPropertyBean property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "DesinerCaseDataItemBean{" +
                "item_num='" + item_num + '\'' +
                ", album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", property=" + property +
                '}';
    }
}
