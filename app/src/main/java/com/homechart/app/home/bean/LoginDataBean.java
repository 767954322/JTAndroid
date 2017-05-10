package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/17/017.
 */

public class LoginDataBean implements Serializable{
    private String ticket;
    private LoginUserInfoBean user_info;

    public LoginDataBean(String ticket, LoginUserInfoBean user_info) {
        this.ticket = ticket;
        this.user_info = user_info;
    }

    public LoginDataBean() {
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public LoginUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(LoginUserInfoBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "LoginDataBean{" +
                "ticket='" + ticket + '\'' +
                ", user_info=" + user_info +
                '}';
    }
}
