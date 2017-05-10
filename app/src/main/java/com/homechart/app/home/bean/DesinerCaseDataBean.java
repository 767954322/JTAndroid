package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GPD on 2017/3/6.
 */

public class DesinerCaseDataBean implements Serializable {

    private List<DesinerCaseDataItemBean> albums;
    private int next;
    private String total;

    public DesinerCaseDataBean(List<DesinerCaseDataItemBean> albums, int next, String total) {
        this.albums = albums;
        this.next = next;
        this.total = total;
    }

    public DesinerCaseDataBean() {
    }

    public List<DesinerCaseDataItemBean> getAlbums() {
        return albums;
    }

    public void setAlbums(List<DesinerCaseDataItemBean> albums) {
        this.albums = albums;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "DesinerCaseDataBean{" +
                "albums=" + albums +
                ", next=" + next +
                ", total='" + total + '\'' +
                '}';
    }
}
