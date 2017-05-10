package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/4.
 */

public class DetailTitalUserBean implements Serializable{

    private String user_id;
    private String nickname;
    private String provinceCity;
    private String avatar;
    private String profession;
    private int auth_status;

    public DetailTitalUserBean(String user_id, String nickname, String provinceCity, String avatar, String profession, int auth_status) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.provinceCity = provinceCity;
        this.avatar = avatar;
        this.profession = profession;
        this.auth_status = auth_status;
    }

    public DetailTitalUserBean() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(int auth_status) {
        this.auth_status = auth_status;
    }

    @Override
    public String toString() {
        return "DetailTitalUserBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", provinceCity='" + provinceCity + '\'' +
                ", avatar='" + avatar + '\'' +
                ", profession='" + profession + '\'' +
                ", auth_status=" + auth_status +
                '}';
    }
}
