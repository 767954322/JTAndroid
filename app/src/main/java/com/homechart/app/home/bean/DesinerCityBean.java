package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerCityBean implements Serializable{

    private int status;
    private String info;
    private DesinerCityDataBean data;

    public DesinerCityBean(int status, String info, DesinerCityDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public DesinerCityBean() {
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

    public DesinerCityDataBean getData() {
        return data;
    }

    public void setData(DesinerCityDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DesinerCityBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
