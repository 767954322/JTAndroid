package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/1.
 */

public class HomePicDateBean  implements Serializable{

    private int status;
    private String info;
    private PicDataBean data;

    public HomePicDateBean(int status, String info, PicDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public HomePicDateBean() {
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

    public PicDataBean getData() {
        return data;
    }

    public void setData(PicDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HomePicDateBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
