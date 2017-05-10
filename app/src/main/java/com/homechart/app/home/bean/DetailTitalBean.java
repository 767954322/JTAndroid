package com.homechart.app.home.bean;

import java.io.Serializable;

public class DetailTitalBean implements Serializable {

    private int status;
    private String info;
    private DetailTitalDataBean data;

    public DetailTitalBean(int status, String info, DetailTitalDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public DetailTitalBean() {
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

    public DetailTitalDataBean getData() {
        return data;
    }

    public void setData(DetailTitalDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DetailTitalBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
