package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class PutImgBean implements Serializable {

    private int status;
    private String info;
    private PutImgDataBean data;

    public PutImgBean(int status, PutImgDataBean data, String info) {
        this.status = status;
        this.data = data;
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PutImgDataBean getData() {
        return data;
    }

    public void setData(PutImgDataBean data) {
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
        return "PutImgBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
