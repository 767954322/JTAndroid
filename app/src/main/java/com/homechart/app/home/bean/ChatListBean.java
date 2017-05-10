package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class ChatListBean implements Serializable{

    private int status;
    private String info;
    private ChatListDataBean data;

    public ChatListBean(int status, ChatListDataBean data, String info) {
        this.status = status;
        this.data = data;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ChatListDataBean getData() {
        return data;
    }

    public void setData(ChatListDataBean data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ChatListBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
