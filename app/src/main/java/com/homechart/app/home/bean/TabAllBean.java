package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/2.
 */

public class TabAllBean implements Serializable {


    private int status;
    private String info;
    private TabDataBean data;

    public TabAllBean(int status, TabDataBean data, String info) {
        this.status = status;
        this.data = data;
        this.info = info;
    }

    public TabAllBean() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TabDataBean getData() {
        return data;
    }

    public void setData(TabDataBean data) {
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
        return "TabAllBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
