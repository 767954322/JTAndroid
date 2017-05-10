package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15/015.
 */

public class JiYanPhoneNumBean implements Serializable{

    private int status;
    private String info;
    private JiYanPhoneNumDataBean data;


    public JiYanPhoneNumBean(int status, String info, JiYanPhoneNumDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public JiYanPhoneNumBean() {
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

    public JiYanPhoneNumDataBean getData() {
        return data;
    }

    public void setData(JiYanPhoneNumDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JiYanPhoneNumBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
