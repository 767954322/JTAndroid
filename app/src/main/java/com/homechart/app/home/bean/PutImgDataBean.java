package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class PutImgDataBean implements Serializable{

    private String image_id;

    public PutImgDataBean(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    @Override
    public String toString() {
        return "PutImgDataBean{" +
                "image_id='" + image_id + '\'' +
                '}';
    }
}
