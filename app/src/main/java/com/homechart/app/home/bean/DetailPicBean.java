package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/4.
 */

public class DetailPicBean implements Serializable {
    private int status;
    private String info;
    private DetailPicDataBean data;

    public DetailPicBean(int status, String info, DetailPicDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
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

    public DetailPicDataBean getData() {
        return data;
    }

    public void setData(DetailPicDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DetailPicBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
