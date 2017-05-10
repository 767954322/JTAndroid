package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerListDataItemBean implements Serializable {
    private String nickname;
    private String user_id;
    private String provinceCity;
    private String ideabook_num;
    private String project_num;
    private String big_image_url;
    private int big_image_width;
    private int big_image_height;
    private String item_num;
    private String description;
    private String name;
    private String service_price;
    private int auth_status;
    private String avatar;

    public DesinerListDataItemBean(String nickname, String user_id, String provinceCity, String ideabook_num, String project_num, String big_image_url, int big_image_width, int big_image_height, String item_num, String description, String name, String service_price, int auth_status, String avatar) {
        this.nickname = nickname;
        this.user_id = user_id;
        this.provinceCity = provinceCity;
        this.ideabook_num = ideabook_num;
        this.project_num = project_num;
        this.big_image_url = big_image_url;
        this.big_image_width = big_image_width;
        this.big_image_height = big_image_height;
        this.item_num = item_num;
        this.description = description;
        this.name = name;
        this.service_price = service_price;
        this.auth_status = auth_status;
        this.avatar = avatar;
    }

    public DesinerListDataItemBean() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public String getIdeabook_num() {
        return ideabook_num;
    }

    public void setIdeabook_num(String ideabook_num) {
        this.ideabook_num = ideabook_num;
    }

    public String getProject_num() {
        return project_num;
    }

    public void setProject_num(String project_num) {
        this.project_num = project_num;
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

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public int getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(int auth_status) {
        this.auth_status = auth_status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "DesinerListDataItemBean{" +
                "nickname='" + nickname + '\'' +
                ", user_id='" + user_id + '\'' +
                ", provinceCity='" + provinceCity + '\'' +
                ", ideabook_num='" + ideabook_num + '\'' +
                ", project_num='" + project_num + '\'' +
                ", big_image_url='" + big_image_url + '\'' +
                ", big_image_width=" + big_image_width +
                ", big_image_height=" + big_image_height +
                ", item_num='" + item_num + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", service_price='" + service_price + '\'' +
                ", auth_status=" + auth_status +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
