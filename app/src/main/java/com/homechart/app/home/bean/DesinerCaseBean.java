package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/6.
 */

public class DesinerCaseBean implements Serializable{

    private int status;
    private String info;
    private DesinerCaseDataBean data;

    public DesinerCaseBean(DesinerCaseDataBean data, int status, String info) {
        this.data = data;
        this.status = status;
        this.info = info;
    }

    public DesinerCaseBean() {
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

    public DesinerCaseDataBean getData() {
        return data;
    }

    public void setData(DesinerCaseDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DesinerCaseBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
