package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/4.
 */

public class DetailTitalDataBean implements Serializable {

    private String album_id;
    private String album_name;
    private String description;
    private String type;
    private String big_image_url;
    private DetailTitalUserBean user;
    private DetailTitalPropertyBean property;

    public DetailTitalDataBean(String album_id, String album_name, String description, String type, String big_image_url, DetailTitalUserBean user, DetailTitalPropertyBean property) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.description = description;
        this.type = type;
        this.big_image_url = big_image_url;
        this.user = user;
        this.property = property;
    }

    public DetailTitalDataBean() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBig_image_url() {
        return big_image_url;
    }

    public void setBig_image_url(String big_image_url) {
        this.big_image_url = big_image_url;
    }

    public DetailTitalUserBean getUser() {
        return user;
    }

    public void setUser(DetailTitalUserBean user) {
        this.user = user;
    }

    public DetailTitalPropertyBean getProperty() {
        return property;
    }

    public void setProperty(DetailTitalPropertyBean property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "DetailTitalDataBean{" +
                "album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", big_image_url='" + big_image_url + '\'' +
                ", user=" + user +
                ", property=" + property +
                '}';
    }
}
