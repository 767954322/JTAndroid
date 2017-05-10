package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15/015.
 */

public class JiYanPhoneNumDataBean implements Serializable {

    private int limit_time;
    private int bool_status;

    public JiYanPhoneNumDataBean(int limit_time, int bool_status) {
        this.limit_time = limit_time;
        this.bool_status = bool_status;
    }

    public JiYanPhoneNumDataBean() {
    }

    public int getLimit_time() {
        return limit_time;
    }

    public void setLimit_time(int limit_time) {
        this.limit_time = limit_time;
    }

    public int getBool_status() {
        return bool_status;
    }

    public void setBool_status(int bool_status) {
        this.bool_status = bool_status;
    }

    @Override
    public String toString() {
        return "JiYanPhoneNumDataBean{" +
                "limit_time=" + limit_time +
                ", bool_status=" + bool_status +
                '}';
    }
}
