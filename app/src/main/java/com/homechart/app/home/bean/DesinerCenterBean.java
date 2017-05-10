package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/6.
 */

public class DesinerCenterBean implements Serializable {

    private int status;
    private String info;
    private DesinerCenterDataBean data;

    public DesinerCenterBean(int status, String info, DesinerCenterDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public DesinerCenterBean() {
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

    public DesinerCenterDataBean getData() {
        return data;
    }

    public void setData(DesinerCenterDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DesinerCenterBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
