package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class ChatListDataUserBean implements Serializable{

    private String user_id;
    private String avatar;
    private String nickname;

    public ChatListDataUserBean(String user_id, String nickname, String avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "ChatListDataUserBean{" +
                "user_id='" + user_id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
