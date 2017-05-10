package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class ChatListDataBean implements Serializable {

    private List<ChatListDataUserBean> users;

    public ChatListDataBean(List<ChatListDataUserBean> users) {
        this.users = users;
    }

    public List<ChatListDataUserBean> getUsers() {
        return users;
    }

    public void setUsers(List<ChatListDataUserBean> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ChatListDataBean{" +
                "users=" + users +
                '}';
    }
}
