package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/1.
 */

public class PicDateItemBean implements Serializable {

    private String item_id;
    private String image_id;
    private String user_id;
    private String album_id;
    private String album_name;
    private String space_tag_name;
    private String space_tag_id;
    private String style_tag_name;
    private String style_tag_id;
    private String nickname;
    private String avatar;
    private String type;
    private String title;
    private String description;
    private String big_image_url;
    private int big_image_width;
    private int big_image_height;

    public PicDateItemBean(String item_id, String image_id, String user_id, String album_id, String album_name, String space_tag_name, String space_tag_id, String style_tag_name, String style_tag_id, String nickname, String avatar, String type, String title, String description, String big_image_url, int big_image_width, int big_image_height) {
        this.item_id = item_id;
        this.image_id = image_id;
        this.user_id = user_id;
        this.album_id = album_id;
        this.album_name = album_name;
        this.space_tag_name = space_tag_name;
        this.space_tag_id = space_tag_id;
        this.style_tag_name = style_tag_name;
        this.style_tag_id = style_tag_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.type = type;
        this.title = title;
        this.description = description;
        this.big_image_url = big_image_url;
        this.big_image_width = big_image_width;
        this.big_image_height = big_image_height;
    }

    public PicDateItemBean() {
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getSpace_tag_name() {
        return space_tag_name;
    }

    public void setSpace_tag_name(String space_tag_name) {
        this.space_tag_name = space_tag_name;
    }

    public String getSpace_tag_id() {
        return space_tag_id;
    }

    public void setSpace_tag_id(String space_tag_id) {
        this.space_tag_id = space_tag_id;
    }

    public String getStyle_tag_name() {
        return style_tag_name;
    }

    public void setStyle_tag_name(String style_tag_name) {
        this.style_tag_name = style_tag_name;
    }

    public String getStyle_tag_id() {
        return style_tag_id;
    }

    public void setStyle_tag_id(String style_tag_id) {
        this.style_tag_id = style_tag_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBig_image_url() {
        return big_image_url;
    }

    public void setBig_image_url(String big_image_url) {
        this.big_image_url = big_image_url;
    }

    public int getBig_image_width() {
        return big_image_width;
    }

    public void setBig_image_width(int big_image_width) {
        this.big_image_width = big_image_width;
    }

    public int getBig_image_height() {
        return big_image_height;
    }

    public void setBig_image_height(int big_image_height) {
        this.big_image_height = big_image_height;
    }

    @Override
    public String toString() {
        return "PicDateItemBean{" +
                "item_id='" + item_id + '\'' +
                ", image_id='" + image_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                ", space_tag_name='" + space_tag_name + '\'' +
                ", space_tag_id='" + space_tag_id + '\'' +
                ", style_tag_name='" + style_tag_name + '\'' +
                ", style_tag_id='" + style_tag_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", big_image_url='" + big_image_url + '\'' +
                ", big_image_width=" + big_image_width +
                ", big_image_height=" + big_image_height +
                '}';
    }
}
