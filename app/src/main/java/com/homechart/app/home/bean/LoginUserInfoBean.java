package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/17/017.
 */

public class LoginUserInfoBean implements Serializable {
    private String user_id;
    private String nickname;
    private String avatar;
    private String ticket;
    private String mobile;
    private String email;
    private String profession;
    private String project_num;
    private String ease_username;
    private String ease_password;

    public LoginUserInfoBean(String user_id, String nickname, String avatar, String ticket, String mobile, String email, String profession, String project_num, String ease_username, String ease_password) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.ticket = ticket;
        this.mobile = mobile;
        this.email = email;
        this.profession = profession;
        this.project_num = project_num;
        this.ease_username = ease_username;
        this.ease_password = ease_password;
    }

    public LoginUserInfoBean() {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProject_num() {
        return project_num;
    }

    public void setProject_num(String project_num) {
        this.project_num = project_num;
    }

    public String getEase_username() {
        return ease_username;
    }

    public void setEase_username(String ease_username) {
        this.ease_username = ease_username;
    }

    public String getEase_password() {
        return ease_password;
    }

    public void setEase_password(String ease_password) {
        this.ease_password = ease_password;
    }

    @Override
    public String toString() {
        return "LoginUserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", ticket='" + ticket + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", profession='" + profession + '\'' +
                ", project_num='" + project_num + '\'' +
                ", ease_username='" + ease_username + '\'' +
                ", ease_password='" + ease_password + '\'' +
                '}';
    }
}
