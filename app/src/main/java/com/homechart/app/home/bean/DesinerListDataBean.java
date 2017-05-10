package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerListDataBean implements Serializable{

    private List<DesinerListDataItemBean> profession;
    private int next;

    public DesinerListDataBean() {
    }

    public DesinerListDataBean(List<DesinerListDataItemBean> profession, int next) {
        this.profession = profession;
        this.next = next;
    }

    public List<DesinerListDataItemBean> getProfession() {
        return profession;
    }

    public void setProfession(List<DesinerListDataItemBean> profession) {
        this.profession = profession;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "DesinerListDataBean{" +
                "profession=" + profession +
                ", next=" + next +
                '}';
    }
}
