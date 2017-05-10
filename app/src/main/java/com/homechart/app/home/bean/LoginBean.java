package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/17/017.
 */

public class LoginBean implements Serializable{

    private int status;
    private String info;
    private LoginDataBean data;

    public LoginBean(int status, String info, LoginDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public LoginBean() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LoginDataBean getData() {
        return data;
    }

    public void setData(LoginDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
