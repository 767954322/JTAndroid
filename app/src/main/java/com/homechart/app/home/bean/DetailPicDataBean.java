package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GPD on 2017/3/4.
 */

public class DetailPicDataBean implements Serializable{

    private int plan_num;
    private int next;
    private List<DetailPicItemBean> items;

    public DetailPicDataBean(int plan_num, int next, List<DetailPicItemBean> items) {
        this.plan_num = plan_num;
        this.next = next;
        this.items = items;
    }

    public DetailPicDataBean() {
    }

    public int getPlan_num() {
        return plan_num;
    }

    public void setPlan_num(int plan_num) {
        this.plan_num = plan_num;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<DetailPicItemBean> getItems() {
        return items;
    }

    public void setItems(List<DetailPicItemBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "DetailPicDataBean{" +
                "plan_num=" + plan_num +
                ", next=" + next +
                ", items=" + items +
                '}';
    }
}
