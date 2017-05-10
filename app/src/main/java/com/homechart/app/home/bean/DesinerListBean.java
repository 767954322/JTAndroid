package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerListBean implements Serializable {

    private int status;
    private String info;
    private DesinerListDataBean data;

    public DesinerListBean(int status, String info, DesinerListDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public DesinerListBean() {
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

    public DesinerListDataBean getData() {
        return data;
    }

    public void setData(DesinerListDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DesinerListBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
