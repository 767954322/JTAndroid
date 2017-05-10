package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GPD on 2017/3/1.
 */

public class PicDataBean implements Serializable{

    private String total;
    private int next;
    private List<PicDateItemBean> items;

    public PicDataBean(String total, int next, List<PicDateItemBean> items) {
        this.total = total;
        this.next = next;
        this.items = items;
    }

    public PicDataBean() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<PicDateItemBean> getItems() {
        return items;
    }

    public void setItems(List<PicDateItemBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PicDataBean{" +
                "total='" + total + '\'' +
                ", next=" + next +
                ", items=" + items +
                '}';
    }
}
